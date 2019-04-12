package com.mht.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: RegularUtils
 * @Description: 正则校验
 * @author com.mhout.xyb
 * @date 2017年7月4日 下午4:21:55 
 * @version 1.0.0
 */
public class RegularUtils {
	
	/**
	 * @Title: telphone 
	 * @Description: 手机号校验
	 * @param phone
	 * @return
	 * @author com.mhout.xyb
	 */
	public static boolean telphone(String phone) {
		Pattern p = Pattern.compile("^1[3,5,7,8]\\d{9}$");
		Matcher m = p.matcher(phone);
		return m.matches();
	}
	
	/**
     * 验证邮箱
     * @param email
     * @return
     */
    public static boolean checkEmail(String email){
        boolean flag = false;
        try{
                String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
                Pattern regex = Pattern.compile(check);
                Matcher matcher = regex.matcher(email);
                flag = matcher.matches();
            }catch(Exception e){
                flag = false;
            }
        return flag;
    }
    
    

}
