
package com.mht.modules.unifiedauth.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mht.common.config.Global;
import com.mht.common.json.ZTreeDto;
import com.mht.common.service.CrudService;
import com.mht.common.utils.StringUtils;
import com.mht.modules.ident.entity.Application;
import com.mht.modules.sys.dao.OfficeDao;
import com.mht.modules.sys.dao.UserDao;
import com.mht.modules.sys.entity.Office;
import com.mht.modules.sys.entity.User;
import com.mht.modules.unifiedauth.dao.AuthOfficeDao;
import com.mht.modules.unifiedauth.dao.AuthUserDao;
import com.mht.modules.unifiedauth.entity.AuthOffice;

/**
 * @ClassName: AuthOfficeService
 * @Description:
 * @author com.mhout.sx
 * @date 2017年3月29日 下午3:05:45
 * @version 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class AuthOfficeService extends CrudService<AuthOfficeDao, AuthOffice> {

    @Autowired
    private AuthOfficeDao authOfficeDao;

    @Autowired
    private AuthUserDao authUserDaoDao;
    
    @Autowired
    private OfficeDao officeDao;

    @Autowired
    private UserDao userDao;

    /**
     * @Title: getTree
     * @Description: TODO获取组织结构和用户树
     * @param id
     * @return
     * @author com.mhout.sx
     */
    public List<ZTreeDto> getTree(String id, String type) {
        // TODO Auto-generated method stub
    	 //判断类型
        if (StringUtils.isNotBlank(type) && !"2".equals(type)) {
        	type = "";
        }
        String parentId;
        if (id == null || "".equals(id)) {
            parentId = "0";
            type = "";
        } else {
            parentId = id;
        }
        // 获取下一级部门
        List<Office> list = this.authOfficeDao.findOfficeByParentAndType(parentId, type);
        List<ZTreeDto> tree = new ArrayList<ZTreeDto>();
        if (list == null || list.size() == 0) {
        } else {
            for (Office office : list) {
                ZTreeDto treeObj = this.getTreeObject(office);
                tree.add(treeObj);
            }
        }
        return tree;
    }

    /**
     * @Title: getTreeObject
     * @Description: TODO Office转换为ztree
     * @param user
     * @return
     * @author com.mhout.sx
     */
    private ZTreeDto getTreeObject(Office office) {
        ZTreeDto dto = new ZTreeDto();
        dto.setObj("office");
        dto.setName(office.getName());
        dto.setId(office.getId());
        // 判断当前还有没有子级
        List<Office> list = this.authUserDaoDao.findOfficeByParent(office.getId());
        if (list == null || list.isEmpty()) {
            dto.setIsParent(false);
        }
        return dto;
    }

    /**
     * @Title: findListByOffice
     * @Description: TODO 获取所有应用和应用是否分配给该部门
     * @param authOffice
     * @return
     * @author com.mhout.sx
     */
    public List<AuthOffice> findListByOffice(AuthOffice authOffice) {
    	String officeId = authOffice.getOffice().getId();
    	String type = Global.SYSTEM;
    	String status = Global.ENABLE;
    	String accessway = Global.CASACESSWAY;
    	//获取父级访问权限
    	List<AuthOffice> plist = new ArrayList<AuthOffice>();
    	List<String> parentIds =  new ArrayList<String>();
    	if (StringUtils.isNotBlank(officeId)) {
    		Office office = this.officeDao.get(officeId);
			String ids = office.getParentIds();
    		if (StringUtils.isNotBlank(ids)) {
    			String[] list = ids.split("\\,");
    			int msg = list.length;
    			for (int i = msg ;i>0;i--) {
    				parentIds.clear();
    				parentIds.add(list[i-1]);
    				plist = this.authOfficeDao.findListByOffice(null,type,status,accessway,parentIds);
    				if (CollectionUtils.isNotEmpty(plist)) {
    					break;
    				}
    			}
    		}
    	} else {
    		parentIds.add("0");
    	}
    	List<AuthOffice> userlist = this.authOfficeDao.findListByOffice(officeId,type,status,accessway,null);
    	List<String> ids = new ArrayList<String>();
    	if (CollectionUtils.isNotEmpty(plist)) {
    		for (AuthOffice authOffice2 : plist) {
    			String close = authOffice2.getCloseType();
    			if (StringUtils.isNotBlank(close) && "2".equals(close)) {
    				ids.add(authOffice2.getApply() == null ? "" : authOffice2.getApply().getId());
    			}
    		}
    	}
    	List<AuthOffice> relist = new ArrayList<AuthOffice>();
    	if (CollectionUtils.isNotEmpty(userlist)) {
    		for (AuthOffice authOffice2 : userlist) {
    			String id = authOffice2.getApply() == null ? "" : authOffice2.getApply().getId();
    			String close = authOffice2.getCloseType();
    			if (StringUtils.isNotBlank(close) && "1".equals(close)) {
    				authOffice2.setAccessAuth("");
    			}
				if (ids.contains(id)) {
    				authOffice2.setIsEdit("2");
    			}
    			relist.add(authOffice2);
    		}
    	}
        return relist;
    }

    /**
     * @Title: saveAuths
     * @Description: 保存应用和部门的对应关系
     * @param list
     * @param officeId
     * @author com.mhout.sx
     */
    @Transactional(readOnly = false)
    public void saveAuths(List<AuthOffice> list, List<AuthOffice> closelist,  String officeId) {
        // TODO Auto-generated method stub
        // 通过officeid删除
        this.authOfficeDao.deleteByOffice(officeId);
        if (list != null) {
            for (AuthOffice authOffice : list) {
                authOffice.preInsert();
                authOffice.setCloseType("2");
                this.authOfficeDao.insert(authOffice);
            }
        }
        if (closelist != null) {
        	for (AuthOffice authOffice : closelist) {
                authOffice.preInsert();
                authOffice.setCloseType("1");
                this.authOfficeDao.insert(authOffice);
            }
        }
    }

    /**
     * @Title: getApplicationByUser
     * @Description: TODO获取用户部门拥有的应用
     * @param user
     * @return
     * @author com.mhout.sx
     */
    public List<Application> getApplicationByUser(User user) {
        // if (user.getOffice() == null || user.getOffice().getId() == null) {
        // user = this.userDao.get(user);
        // }
        // // 通过officeId获取Application
        // return
        // this.authOfficeDao.getApplicationByOffice(user.getOffice().getId());
        return null;
    }

    /**
     * @Title: findOfficeByName
     * @Description: TODO 搜索阻止机构
     * @param officeName
     * @return
     * @author com.mhout.sx
     */
    public List<Office> findOfficeByName(String officeName) {
        // TODO Auto-generated method stub
        return this.authOfficeDao.findOfficeByName("%" + officeName + "%");
    }

    /**
     * 
     * @Title: findByAppId
     * @Description: TODO 通过AppID获取部门信息
     * @param id
     * @return
     * @author com.mhout.wj
     */
    public List<AuthOffice> findByAppId(String id) {
        return this.authOfficeDao.findByAppId(id);
    }
}
