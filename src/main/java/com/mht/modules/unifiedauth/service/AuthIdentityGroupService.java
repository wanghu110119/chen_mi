
package com.mht.modules.unifiedauth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mht.common.config.Global;
import com.mht.common.service.CrudService;
import com.mht.modules.ident.entity.IdentityGroup;
import com.mht.modules.unifiedauth.dao.AuthIdentityGroupDao;
import com.mht.modules.unifiedauth.entity.AuthIdentityGroup;

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
public class AuthIdentityGroupService extends CrudService<AuthIdentityGroupDao, AuthIdentityGroup> {
	@Autowired
	private AuthIdentityGroupDao authIdentityGroupDao;
	/**
	 * 
	 * @Title: findListByIdentityGroupId 
	 * @Description: 根据ID查询用户组对应的应用及其权限
	 * @param id
	 * @return
	 * @author com.mhout.zjh
	 */
	public List<AuthIdentityGroup> findListByIdentityGroupId(String id){
		String type = Global.SYSTEM;
    	String status = Global.ENABLE;
    	String accessway = Global.CASACESSWAY;
		List<AuthIdentityGroup> list = this.authIdentityGroupDao.findListByIdentityGroupId(id,type,status,accessway);
		return list;
	}
	/**
	 * 保存表单数据
	 * @Title: saveForm 
	 * @Description: TODO
	 * @author com.mhout.zjh
	 */
	@Transactional(readOnly = false)
	public void saveForm(List<AuthIdentityGroup> list,String identityGroupId){
		//删除原有数据
		this.authIdentityGroupDao.deleteByIdentityGroupId(identityGroupId);
		if(list != null){
			for (int i = 0; i < list.size(); i++) {
				AuthIdentityGroup authIdentityGroup = list.get(i);
				this.authIdentityGroupDao.insert(authIdentityGroup);
			}
		}
	}
	/**
	 * 
	 * @Title: findIdentityGroupsByName 
	 * @Description: 根据角色名称查询角色列表
	 * @return
	 * @author com.mhout.zjh
	 */
	public List<IdentityGroup> findIdentityGroupsByName(String groupName){
		List<IdentityGroup> list = this.authIdentityGroupDao.findIdentityGroupsByName(groupName); 
		return list;
	}
	/**
     * 
     * @Title: findByAppId
     * @Description: TODO 通过appID获取用户组信息
     * @param id
     * @return
     * @author com.mhout.wj
     */
    public List<AuthIdentityGroup> findByAppId(String id) {
        List<AuthIdentityGroup> list = this.authIdentityGroupDao.findByAppId(id);
        return list;
    }
}
