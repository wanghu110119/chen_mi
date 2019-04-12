package com.mht.modules.swust.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;

import com.mht.modules.swust.entity.SysUser;
import com.mht.modules.swust.service.OrderUserService;
import com.mht.modules.swust.service.impl.OrderUserServiceImpl;
import com.mht.modules.sys.entity.User;
import com.mht.modules.sys.security.UsernamePasswordToken;


public class Myrealm extends AuthorizingRealm{

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		if(principals==null){
			throw new AuthorizationException("PrincipalCollection 不能为空！！！！！！！！！！！！");
		}
		OrderUserServiceImpl service = new OrderUserServiceImpl();
		//获取登录名
		String name = (String)principals.fromRealm(getName()).iterator().next();
		User user = service.UserSelectByAcount(name);
		Set<String> roles = new HashSet<>();
		roles.add(user.getRoleType());
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		if(user!=null){
			info.setRoles(roles);
		}
		return info;
	}
	
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth)
			throws AuthenticationException {
		OrderUserServiceImpl service = new OrderUserServiceImpl();
		UsernamePasswordToken token = (UsernamePasswordToken)auth;
		User user = service.UserSelectByAcount(token.getUsername());
		SimpleAuthenticationInfo info = null;
		if(token.getUsername().equals(user.getAccount())&&token.getPassword().equals(user.getPassword())){
			info= new SimpleAuthenticationInfo(user.getAccount(),user.getPassword(),getName());
		}
		return info;
	}

}
















