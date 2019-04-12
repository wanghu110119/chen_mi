/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mht.modules.gen.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mht.modules.gen.dao.GenDataBaseDictDao;
import com.mht.modules.gen.dao.GenTableColumnDao;
import com.mht.modules.gen.dao.GenTableDao;
import com.mht.modules.gen.entity.GenTable;
import com.mht.modules.gen.entity.GenTableColumn;
import com.mht.modules.gen.util.GenUtils;
import com.mht.common.config.Global;
import com.mht.common.persistence.Page;
import com.mht.common.service.BaseService;
import com.mht.common.utils.StringUtils;

/**
 * 业务表Service
 * @author ThinkGem
 * @version 2013-10-15
 */
@Service
@Transactional(readOnly = true)
public class GenTableService extends BaseService {

	@Autowired
	private GenTableDao genTableDao;
	@Autowired
	private GenTableColumnDao genTableColumnDao;
	@Autowired
	private GenDataBaseDictDao genDataBaseDictDao;
	
	public GenTable get(String id) {
		GenTable genTable = genTableDao.get(id);
		GenTableColumn genTableColumn = new GenTableColumn();
		genTableColumn.setGenTable(new GenTable(genTable.getId()));
		genTable.setColumnList(genTableColumnDao.findList(genTableColumn));
		return genTable;
	}
	
	public Page<GenTable> find(Page<GenTable> page, GenTable genTable) {
		genTable.setPage(page);
		page.setList(genTableDao.findList(genTable));
		return page;
	}

	public List<GenTable> findAll() {
		return genTableDao.findAllList(new GenTable());
	}
	
	/**
	 * 获取物理数据表列表
	 * @param genTable
	 * @return
	 */
	public List<GenTable> findTableListFormDb(GenTable genTable){
		return genDataBaseDictDao.findTableList(genTable);
	}
	
	/**
	 * 验证表名是否可用，如果已存在，则返回false
	 * @param genTable
	 * @return
	 */
	public boolean checkTableName(String tableName){
		if (StringUtils.isBlank(tableName)){
			return true;
		}
		GenTable genTable = new GenTable();
		genTable.setName(tableName);
		List<GenTable> list = genTableDao.findList(genTable);
		return list.size() == 0;
	}
	
	/**
	 * 获取物理数据表列表
	 * @param genTable
	 * @return
	 */
	public GenTable getTableFormDb(GenTable genTable){
		// 如果有表名，则获取物理表
		if (StringUtils.isNotBlank(genTable.getName())){
			
			List<GenTable> list = genDataBaseDictDao.findTableList(genTable);
			if (list.size() > 0){
				
				// 如果是新增，初始化表属性
				if (StringUtils.isBlank(genTable.getId())){
					genTable = list.get(0);
					// 设置字段说明
					if (StringUtils.isBlank(genTable.getComments())){
						genTable.setComments(genTable.getName());
					}
					genTable.setClassName(StringUtils.toCapitalizeCamelCase(genTable.getName()));
				}
				
				// 添加新列
				List<GenTableColumn> columnList = genDataBaseDictDao.findTableColumnList(genTable);
				for (GenTableColumn column : columnList){
					boolean b = false;
					for (GenTableColumn e : genTable.getColumnList()){
						if (e.getName().equals(column.getName())){
							b = true;
						}
					}
					if (!b){
						genTable.getColumnList().add(column);
					}
				}
				
				// 删除已删除的列
				for (GenTableColumn e : genTable.getColumnList()){
					boolean b = false;
					for (GenTableColumn column : columnList){
						if (column.getName().equals(e.getName())){
							b = true;
						}
					}
					if (!b){
						e.setDelFlag(GenTableColumn.DEL_FLAG_DELETE);
					}
				}
				
				// 获取主键
				genTable.setPkList(genDataBaseDictDao.findTablePK(genTable));
				
				// 初始化列属性字段
				GenUtils.initColumnField(genTable);
				
			}
		}
		return genTable;
	}
	
	@Transactional(readOnly = false)
	public void save(GenTable genTable) {
		if (StringUtils.isBlank(genTable.getId())){
			genTable.preInsert();
			genTableDao.insert(genTable);
		}else{
			genTable.preUpdate();
			genTableDao.update(genTable);
		}
		// 保存列
		for (GenTableColumn column : genTable.getColumnList()){
			column.setGenTable(genTable);
			if (StringUtils.isBlank(column.getId())){
				column.preInsert();
				genTableColumnDao.insert(column);
			}else{
				column.preUpdate();
				genTableColumnDao.update(column);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(GenTable genTable) {
		genTableDao.delete(genTable);
		genTableColumnDao.deleteByGenTable(genTable);
		String dbType = Global.getConfig("jdbc.type");
		if("mysql".equalsIgnoreCase(dbType)){
			genTableDao.buildTable("drop table if exists " + genTable.getName() + " ;");
		}else if("oracle".equalsIgnoreCase(dbType)){
			genTableDao.buildTable("DROP TABLE "+genTable.getName());
		}
	}

	@Transactional(readOnly=false)
	public void saveFromDB(GenTable genT) {
		genT.preInsert();
	    this.genTableDao.insert(genT);
	    for (GenTableColumn column : genT.getColumnList()){
	      column.setGenTable(genT);
	      column.setId(null);
	      column.preInsert();
	      genTableColumnDao.insert(column);
	    }
	}
	
	@Transactional(readOnly=false)
	public void buildTable(String string) {
		genTableDao.buildTable(string);
	}

	@Transactional(readOnly=false)
	public void syncSave(GenTable genTable) {
		genTable.setIsSync("1");
	    genTableDao.update(genTable);
	}

	/**
	 * 同步数据库操作，创建数据库表，支持mysql、oracle
	 * @param genTable
	 */
	@Transactional(readOnly=false)
	public void synchDb(GenTable genTable) {
		String dbType = Global.getConfig("jdbc.type");
		List<GenTableColumn> genColumnList = genTable.getColumnList();
		StringBuffer sql = new StringBuffer();
		if("mysql".equals(dbType)){
			sql.append("drop table if exists " + genTable.getName() + " ;");
			buildTable(sql.toString());
			sql.setLength(0);
			sql.append("create table "+genTable.getName()+" (");
			String pk = "";
			for(GenTableColumn column : genColumnList){
				if("1".equals(column.getIsPk())){
					sql.append(" "+column.getName()+" "+column.getJdbcType()+" comment '"+column.getComments()+"',");
					pk+=column.getName()+",";
				}else{
					sql.append(" "+column.getName()+" "+column.getJdbcType()+" comment '"+column.getComments()+"',");
				}
			}
			sql.append("primary key ("+pk.substring(0, pk.length()-1)+") ");
			sql.append(") comment '"+genTable.getComments()+"'");
			buildTable(sql.toString());
		}else if("oracle".equals(dbType)){
			sql.setLength(0);
			sql.append("DROP TABLE "+genTable.getName());
			buildTable(sql.toString());
			sql.setLength(0);
			sql.append("CREATE TABLE "+genTable.getName()+" (");
			String pk = "";
			for(GenTableColumn column : genColumnList){
				String jdbcType = column.getJdbcType();
				if("integer".equalsIgnoreCase(jdbcType)){
					jdbcType = "number(10,0)";
				}else if(jdbcType.contains("nvarchar(")){
					jdbcType.replace("nvarchar", "nvarchar2");
				}else if("datetime".equalsIgnoreCase(jdbcType)){
					jdbcType = "date";
				}else if(jdbcType.contains("varchar(")){
					jdbcType.replace("varchar", "varchar2");
				}else if("double".equalsIgnoreCase(jdbcType)){
					jdbcType = "float(24)";
				}else if("longblob".equalsIgnoreCase(jdbcType)){
					jdbcType = "blob raw";
				}else if("longtext".equalsIgnoreCase(jdbcType)){
					jdbcType = "clob raw";
				}
				if("1".equals(column.getIsPk())){
					sql.append(" "+column.getName()+" "+jdbcType+",");
					pk += column.getName();
				}else{
					sql.append(" " + column.getName() + " " + jdbcType + ",");
				}
			}
			sql.append(")");
			buildTable(sql.toString());
			buildTable("comment on table "+genTable.getName()+" is '"+genTable.getComments()+"'");
			for(GenTableColumn column : genColumnList){
				buildTable("comment on column " + genTable.getName() + "." + column.getName() + " is  '" + column.getComments() + "'");
			}
			buildTable("alter table " + genTable.getName() +" add constraint PK_" + genTable.getName() + "_" + pk + " primary key (" + pk + ") ");
		}
	}

	@Transactional(readOnly = false)
	public void remove(GenTable genTable) {
		genTableDao.delete(genTable);
		genTableColumnDao.deleteByGenTable(genTable);
	}
}
