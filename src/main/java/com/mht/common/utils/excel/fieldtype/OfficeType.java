/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.mht.common.utils.excel.fieldtype;

import com.mht.common.utils.SpringContextHolder;
import com.mht.common.utils.StringUtils;
import com.mht.modules.swust.dao.SysOfficeDao;
import com.mht.modules.sys.dao.OfficeDao;
import com.mht.modules.sys.entity.Office;
import com.mht.modules.sys.utils.UserUtils;

/**
 * 字段类型转换
 * @author jeeplus
 * @version 2013-03-10
 */
public class OfficeType {
	private static SysOfficeDao officeDao = SpringContextHolder.getBean(SysOfficeDao.class);
	/**
	 * 获取对象值（导入）
	 */
	public static Object getValue(String val) {
//		for (Office e : UserUtils.getOfficeList()){
		for (Office e : officeDao.findAllList(new Office())){
			if (StringUtils.trimToEmpty(val).equals(e.getName())){
				return e;
			}
		}
		return null;
	}

	/**
	 * 设置对象值（导出）
	 */
	public static String setValue(Object val) {
		if (val != null && ((Office)val).getName() != null){
			return ((Office)val).getName();
		}
		return "";
	}
}
