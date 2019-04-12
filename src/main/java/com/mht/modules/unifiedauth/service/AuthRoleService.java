
package com.mht.modules.unifiedauth.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mht.common.config.Global;
import com.mht.common.service.CrudService;
import com.mht.common.utils.StringUtils;
import com.mht.modules.sys.dao.RoleDao;
import com.mht.modules.sys.entity.Office;
import com.mht.modules.sys.entity.Role;
import com.mht.modules.unifiedauth.dao.AuthOfficeDao;
import com.mht.modules.unifiedauth.dao.AuthRoleDao;
import com.mht.modules.unifiedauth.entity.AuthOffice;
import com.mht.modules.unifiedauth.entity.AuthRole;

/**
 * 
 * @ClassName: AuthRoleService
 * @Description:
 * @author com.mhout.zjh
 * @date 2017年3月29日 下午11:14:56
 * @version 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class AuthRoleService extends CrudService<AuthOfficeDao, AuthOffice> {
	
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private AuthRoleDao authRoleDao;

	/**
	 * 
	 * @Title: findListById
	 * @Description: 根据ID查询角色对应的应用及其权限
	 * @param roleId
	 * @return
	 * @author com.mhout.zjh
	 */
	public List<AuthRole> findListById(String roleId) {
		String type = Global.SYSTEM;
    	String status = Global.ENABLE;
    	String accessway = Global.CASACESSWAY;
    	List<AuthRole> userlist = this.authRoleDao.findListByRoleId(roleId,type,status,accessway,null);
        return userlist;
	}

	/**
	 * 保存表单数据
	 * 
	 * @Title: saveForm
	 * @Description: TODO
	 * @author com.mhout.zjh
	 */
	@Transactional(readOnly = false)
	public void saveForm(List<AuthRole> list, String roleId) {
		// 删除原有数据
		this.authRoleDao.deleteByRoleId(roleId);
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				AuthRole authRole = list.get(i);
				authRoleDao.insert(authRole);
			}
		}
	}

	/**
	 * 
	 * @Title: findRolesByName
	 * @Description: 根据角色名称查询角色列表
	 * @return
	 * @author com.mhout.zjh
	 */
	public List<Role> findRolesByName(String roleName) {
		List<Role> list = this.authRoleDao.findRolesByName(roleName);
		return list;
	}
    /**
     * 
     * @Title: findRolesByName
     * @Description: 根据appId获取认证角色信息
     * @return
     * @author com.mhout.zjh
     */
    public List<AuthRole> findByAppId(String id) {
        List<AuthRole> list = this.authRoleDao.findByAppId(id);
        return list;
    }
}
