package com.mht.modules.ident.service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.mht.common.config.Global;
import com.mht.common.json.ApplicationAuthDto;
import com.mht.common.json.ZTreeDto;
import com.mht.common.persistence.Page;
import com.mht.common.service.CrudService;
import com.mht.common.utils.FileUtils;
import com.mht.common.utils.IdGen;
import com.mht.common.utils.StringUtils;
import com.mht.modules.audit.dao.SafeStrategyDao;
import com.mht.modules.audit.entity.SafeStrategy;
import com.mht.modules.ident.dao.ApplicationDao;
import com.mht.modules.ident.dao.ApplicationUserDao;
import com.mht.modules.ident.dao.IdentityGroupDao;
import com.mht.modules.ident.entity.Application;
import com.mht.modules.ident.entity.ApplicationUser;
import com.mht.modules.ident.entity.IdentityGroup;
import com.mht.modules.sys.dao.OfficeDao;
import com.mht.modules.sys.dao.RoleDao;
import com.mht.modules.sys.dao.UserDao;
import com.mht.modules.sys.entity.Office;
import com.mht.modules.sys.entity.Role;
import com.mht.modules.sys.entity.User;
import com.mht.modules.sys.utils.UserUtils;
import com.mht.modules.unifiedauth.dao.AuthUserDao;
import com.mht.modules.unifiedauth.entity.AuthIdentityGroup;
import com.mht.modules.unifiedauth.entity.AuthOffice;
import com.mht.modules.unifiedauth.entity.AuthRole;
import com.mht.modules.unifiedauth.entity.AuthUser;
import com.mht.modules.unifiedauth.service.AuthIdentityGroupService;
import com.mht.modules.unifiedauth.service.AuthOfficeService;
import com.mht.modules.unifiedauth.service.AuthRoleService;
import com.mht.modules.unifiedauth.service.AuthUserService;

/**
 * @ClassName: ApplicationService
 * @Description: 
 * @author com.mhout.xyb
 * @date 2017年3月29日 下午4:09:43 
 * @version 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class ApplicationService extends CrudService<ApplicationDao, Application>{
	
	@Autowired
    private AuthUserDao authUserDaoDao;

	@Autowired
	private UserDao userDao;
	
	@Autowired
	ApplicationDao applicationDao;
	
	@Autowired
	ApplicationUserDao applicationUserDao;
	
	@Autowired
	RoleDao roleDao;
	
	@Autowired
	SafeStrategyDao safeStrategyDao;
	
	@Autowired
	IdentityGroupDao identityGroupDao;
	
	@Autowired
	OfficeDao officeDao;
	
	@Autowired
	AuthUserService authUserService;
	
	@Autowired
	AuthRoleService authRoleService;
	
	@Autowired
	AuthIdentityGroupService authIdentityGroupService;
	
	@Autowired
	AuthOfficeService authOfficeService;
	
	public Page<Application> find(Page<Application> page, Application application) {
		application.setPage(page);
		page.setList(applicationDao.findList(application));
		return page;
	}
	
	public Page<Application> findSys(Page<Application> page, Application application) {
		application.setPage(page);
		User user = UserUtils.getUser();
		if (user.isAdmin()) {
			page.setList(applicationDao.findList(application));
		} else {
			application.setCreateBy(user);
			page.setList(applicationDao.findSys(application));
		}
		return page;
	}
	
	public List<Application> findAllUserApplication(Application application) {
		List<Application> list = new ArrayList<Application>();
		User user = UserUtils.getUser();
		if (user.isAdmin()) {
			list = applicationDao.findList(application);
		} else {
			application.setCreateBy(user);
			list = applicationDao.findSys(application);
		}
		return list;
	}
	
	public List<User> findManagerUser(Application application) {
		return applicationDao.findManagerUser(application, null);
	}
	
	/**
	 * @Title: deleteApplication 
	 * @Description: 逻辑删除应用
	 * @param application
	 * @author com.mhout.xyb
	 */
	@Transactional(readOnly = false)
    public void deleteApplication(Application application) {
		applicationDao.deleteByLogic(application);
	}
	
	 /**
     * @Title: getTree
     * @Description: TODO获取组织结构和用户树
     * @param id
     * @return
     * @author com.mhout.sx
     */
    public List<ZTreeDto> getTree(String id, Application application, String userName) {
        // TODO Auto-generated method stub
        String parentId;
        if (id == null || "".equals(id)) {
            parentId = "0";
        } else {
            parentId = id;
        }
        List<String> valueList = new ArrayList<String>();
        boolean msg = setValueMessage(userName, valueList);
        // 获取下一级部门
        List<Office> list = this.authUserDaoDao.findOfficeByParent(parentId);
        List<ZTreeDto> tree = new ArrayList<ZTreeDto>();
        if(msg) {
        	 if (list == null || list.size() == 0) {
             } else {
                 for (Office office : list) {
                 	if (CollectionUtils.isNotEmpty(valueList) &&
                 			!valueList.contains(office.getId())) {
                 		continue;
                 	}
                 	valueList.contains(office.getId());
                     ZTreeDto treeObj = this.getTreeObject(office);
                     treeObj.setChkDisabled(false);
                     treeObj.setNocheck(false);
                     treeObj.setOpen(false);
                     tree.add(treeObj);
                 }
             }
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        String path = request.getContextPath();
        // 获取部门下面人员
        List<User> list2 = this.authUserDaoDao.findUserByOffice(parentId);
        if (list2 == null || list2.size() == 0) {
        } else {
            for (User user : list2) {
            	if (StringUtils.isNotBlank(userName) && !(user.getName().indexOf(userName) > -1)) {
            		continue;
            	}
                ZTreeDto treeObj = this.getTreeObject(user, path);
                //判断应用是否存在
                treeObj.setChkDisabled(false);
                treeObj.setNocheck(false);
                tree.add(treeObj);
            }
        }
        return tree;
    }
    
    /**
     * @Title: setValueMessage 
     * @Description: 排除设置
     * @param userName
     * @param valueList
     * @return
     * @author com.mhout.xyb
     */
    public boolean setValueMessage(String userName, List<String> valueList) {
    	if (StringUtils.isNotBlank(userName)) {
        	List<Office> list = this.authUserDaoDao.findByUserOfficeIds(userName);
        	if (list != null && list.size() > 0) {
        		for (Office office : list) {
        			String value = office.getId();
        			String value2 = office.getParentIds();
        			valueList.add(value);
        			String[] ids = value2.split("\\,");
        			if (ids != null && ids.length > 0) {
        				for (String value3 : ids) {
        					valueList.add(value3);
        				}
        			}
        		}
        	} else {
        		return false;
        	} 
        }
    	return true;
    }
    
    /**
     * @Title: getTreeObject
     * @Description: TODO User转换为ztree
     * @param user
     * @return
     * @author com.mhout.sx
     */
    private ZTreeDto getTreeObject(User user, String path) {
        ZTreeDto dto = new ZTreeDto();
        dto.setObj("user");
        dto.setName(user.getName());
        dto.setId(user.getId());
        dto.setIsParent(false);
        // dto.setIconSkin("fa fa-user");

        dto.setIcon(path + ZTreeDto.USER_ICON);
        return dto;
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
        return dto;
    }
	
	/**
	 * @Title: saveAppl 
	 * @Description: 应用新增
	 * @param application
	 * @param fileIn
	 * @throws IOException
	 * @author com.mhout.xyb
	 */
	@Transactional(readOnly = false)
	public void saveAppl(Application application, MultipartFile fileIn) throws IOException {
		//保存
		if (application.getIsNewRecord()){
			saveFile("1", application, fileIn);
			application.preInsert();
			application.setSerial(IdGen.uuid());
			dao.insert(application);
			if (!application.getCreateBy().isAdmin()) {
				saveApplicationManager(application, application.getCreateBy().getId() + ",");
			}
		}else{
			saveFile("2", application, fileIn);
			application.preUpdate();
			dao.update(application);
		}
	}
	
	
	public void saveFile(String type, Application application, MultipartFile fileIn) throws IOException {
		//文件上传
		boolean message = fileIn != null && fileIn.getInputStream() != null 
				&& StringUtils.isNotBlank(fileIn.getOriginalFilename());
		if (message) {
			String userName = application.getPic();
			String fileName = fileIn.getOriginalFilename();
			boolean isnew = StringUtils.isBlank(userName) || StringUtils.isNotBlank(userName) && !userName.equals(fileName);
			if (isnew) {
				//修改文件名
				String uuid = IdGen.uuid();
				fileName = uuid + fileName.substring(fileName.lastIndexOf("."), fileName.length());
				//获取全路径
				String path = Global.getApplUploadPath() + Global.APPLICATION_BASE_URL;
				FileUtils.uploadFile(path, fileName, fileIn.getInputStream());
				application.setPic(Global.APPLICATION_BASE_URL + fileName);
			}
		} else {
			if ("1".equals(type)) {
				application.setPic(Global.APPLICATION_IMAGE);
			}
		}
	}
	
	/**
	 * @Title: saveUserManager 
	 * @Description: 保存应用管理员
	 * @param application
	 * @param ids
	 * @author com.mhout.xyb
	 */
	@Transactional(readOnly = false)
	public void saveApplicationManager(Application application, String ids) {
		applicationUserDao.deleteByApp(application.getId());
		if (application != null && StringUtils.isNotBlank(ids)) {
			String[] userIds = ids.split("\\,");
			for (String id : userIds) {
				User user = userDao.get(id);
				ApplicationUser applicationUser = new ApplicationUser();
				applicationUser.setId(IdGen.uuid());
				applicationUser.setApplication(application);
				applicationUser.setUser(user);
				applicationUser.setCreateBy(UserUtils.getUser());
				applicationUser.setCreateDate(new Date());
				applicationUser.setUpdateBy(UserUtils.getUser());
				applicationUser.setUpdateDate(new Date());
				List<ApplicationUser> list = applicationUserDao.findList(applicationUser);
				if (user != null && list == null || list.size() == 0) {
					//保存
					applicationUserDao.insert(applicationUser);
				}
			}
		}
	}
	
	/**
	 * @Title: getAuthList 
	 * @Description: 获取权限信息
	 * @param type
	 * @return
	 * @author com.mhout.xyb
	 */
	public List<ApplicationAuthDto> getAuthList(String type, String name,
			HttpServletRequest request, HttpServletResponse response) {
		List<ApplicationAuthDto> list = new ArrayList<ApplicationAuthDto>();
		if ("1".equals(type)) {
			User reuser = new User();
			reuser.setName(name);
			Page<User> page = new Page<User>(request, response);
			reuser.setPage(page);
			page.setList(userDao.findList(reuser));
			List<User> users = page.getList();
			if (users != null && users.size() > 0) {
				for (User user : users) {
					ApplicationAuthDto dto = new ApplicationAuthDto();
					dto.setId(user.getId());
					dto.setName(user.getName());
					dto.setType("1");
					dto.setCount(page.getTotalPage());
					list.add(dto);
				}
			}
		} else if ("2".equals(type)) {
			Role role = new Role();
			role.setName(name);
			List<Role> roles = roleDao.findList(role);
			if (roles != null && roles.size() > 0) {
				for (Role role2 : roles) {
					ApplicationAuthDto dto = new ApplicationAuthDto();
					dto.setId(role2.getId());
					dto.setName(role2.getName());
					dto.setType("2");
					list.add(dto);
				}
			}
		} else if ("3".equals(type)) {
			IdentityGroup identityGroup = new IdentityGroup();
			identityGroup.setGroupName(name);
			List<IdentityGroup> groups = identityGroupDao.findList(identityGroup);
			if (groups != null && groups.size() > 0) {
				for (IdentityGroup group : groups) {
					ApplicationAuthDto dto = new ApplicationAuthDto();
					dto.setId(group.getId());
					dto.setName(group.getGroupName());
					dto.setType("4");
					list.add(dto);
				}
			}
		} else if ("4".equals(type)) {
			Office office = new Office();
			office.setName(name);
			Office office2 = new Office();
			office2.setId("1");
			office.setParent(office2);
			List<Office> offices = officeDao.findList(office);
			if (offices != null && offices.size() > 0) {
				for (Office office3 : offices) {
					ApplicationAuthDto dto = new ApplicationAuthDto();
					dto.setId(office3.getId());
					dto.setName(office3.getName());
					dto.setType("3");
					list.add(dto);
				}
			}
		}
		return list;
	}
	
	/**
	 * @Title: saveUserAuthority 
	 * @Description: 管理成员
	 * @param type
	 * @param application
	 * @param ids
	 * @author com.mhout.xyb
	 */
	@Transactional(readOnly = false)
	public void saveUserAuthority(String type, Application application, String ids) {
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(type)) {
			String[] idString = ids.split(",");
			for (String id : idString) {
				pbSaveUser(id, type, application.getId());
			}
		}
		
	}
	
	/**
	 * @Title: pbSaveUser 
	 * @Description: 公共保存
	 * @param addId
	 * @param type
	 * @param id
	 * @author com.mhout.xyb
	 */
	@Transactional(readOnly = false)
	private void pbSaveUser(String addId, String type, String id) {
		//用户
		if ("1".equals(type)) {
			AuthUser authUser = new AuthUser();
			authUser.setApplyId(id);
			authUser.setUserId(addId);
			authUser.setAccessAuth("1");
			List<AuthUser> list = new ArrayList<AuthUser>();
			list.add(authUser);
			authUserService.saveAuths(list, null, addId);
		} 
		//角色
		else if("2".equals(type)) {
			AuthRole authRole = new AuthRole();
			authRole.setApplyId(id);
			authRole.setRoleId(addId);
			authRole.setAccessAuth("1");
			List<AuthRole> list = new ArrayList<AuthRole>();
			list.add(authRole);
			authRoleService.saveForm(list, addId);
		}
		//用户组
		else if("3".equals(type)) {
			AuthIdentityGroup group = new AuthIdentityGroup();
			group.setApplyId(id);
			group.setIdentityGroupId(addId);
			group.setAccessAuth("1");
			List<AuthIdentityGroup> list = new ArrayList<AuthIdentityGroup>();
			list.add(group);
			authIdentityGroupService.saveForm(list, addId);
		}
		//组织机构
		else if("4".equals(type)) {
			AuthOffice office = new AuthOffice();
			office.setApplyId(id);
			office.setOfficeId(addId);
			office.setAccessAuth("1");
			List<AuthOffice> list = new ArrayList<AuthOffice>();
			list.add(office);
			authOfficeService.saveAuths(list, null, addId);
		}
	}
	
	/**
     * 
     * @Title: findByUserAndType
     * @Description: TODO 通过应用类型查询用户拥有的应用
     * @param user
     * @param string
     * @return
     * @author com.mhout.wj
     */
    public List<Application> findByUserAndType(String id, String type) {
        List<Application> apps = applicationDao.findByUserAndType(id, type);
        return apps;
    }
    
    /**
     * 
     * @Title: findAllSystemApps
     * @Description: TODO 查询所用系统应用
     * @param user
     * @param string
     * @return
     * @author com.mhout.wj
     */
    public List<Application> findAllSystemApps() {
    	List<Application> apps = applicationDao.findAllSystemApps();
    	return apps;
    }
    
    /**
     * @Title: findAppAuth 
     * @Description: 查找App权限
     * @param appId
     * @param secret
     * @return
     * @author com.mhout.xyb
     */
    public boolean findAppAuth(String appId, String secret) {
    	List<Application> list = applicationDao.findAppAuth(appId, secret);
    	if(CollectionUtils.isNotEmpty(list)) {
    		return true;
    	}
    	return false;
    }
    
    /**
     * @Title: checkCustomApp 
     * @Description: 检查用户应用权限
     * @return
     * @author com.mhout.xyb
     */
    public boolean checkCustomApp(String code) {
    	SafeStrategy safeStrategy = safeStrategyDao.findByCode(code);
    	boolean msg = UserUtils.getUser().isAdmin() || safeStrategy != null 
    			&& Global.ALLOW.equals(safeStrategy.getValue());
    	if (msg) {
    		return true;
    	} else {
    		return false;
    	}
	}
    
    /**
     * @Title: checkName 
     * @Description: 查询应用名称是否存在
     * @param application
     * @return
     * @author com.mhout.xyb
     */
    public String checkName(Application application, String oldname) {
    	List<Application> list = applicationDao.findByName(application);
    	if (CollectionUtils.isNotEmpty(list)) {
    		String name = list.get(0).getName();
    		if (!name .equals(oldname)) {
    			return "false";
    		}
    	}
    	return "true";
    }
	
}
