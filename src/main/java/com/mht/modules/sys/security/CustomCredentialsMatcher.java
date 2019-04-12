package com.mht.modules.sys.security;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

import com.mht.common.utils.MD5Utils;

/**
 * @ClassName: CustomCredentialsMatcher
 * @Description: 自定义密码验证
 * @author com.mhout.xyb
 * @date 2017年6月28日 下午1:36:38 
 * @version 1.0.0
 */
public class CustomCredentialsMatcher extends SimpleCredentialsMatcher {

	@Override
	public boolean doCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo info) {

		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		Object accountCredentials = getCredentials(info);
		String pwdType = String.valueOf(token.getPassword());
		if (pwdType.length() == 32) {
			return equals(pwdType, accountCredentials);
		}
		String pwdUser = String.valueOf(token.getPassword());
		return equals(MD5Utils.make(pwdUser), accountCredentials);
	}

}
