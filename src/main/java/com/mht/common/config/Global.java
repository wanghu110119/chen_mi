/**
 * Copyright &copy; 2015-2020 <a href="http://www.mht.org/">mht</a> All rights reserved.
 */
package com.mht.common.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.ibatis.io.Resources;
import org.apache.shiro.subject.Subject;
import org.springframework.core.io.DefaultResourceLoader;

import com.ckfinder.connector.ServletContextFactory;
import com.google.common.collect.Maps;
import com.mht.common.utils.PropertiesLoader;
import com.mht.common.utils.StringUtils;

/**
 * 全局配置类
 * @author mht
 * @version 2014-06-25
 */
public class Global {

	/**
	 * 当前对象实例
	 */
	private static Global global = new Global();
	
	public static Map<String, Subject> logoutmap = new ConcurrentHashMap<>();
	
	
	/**
	 * 保存全局属性值
	 */
	private static Map<String, String> map = Maps.newHashMap();
	
	/**
	 * 数据备份还原状态
	 */
	public static Map<String, String> datamap = new ConcurrentHashMap<>();
	
	/**
	 * 线程池调用
	 */
	public static ExecutorService threadPool = Executors.newFixedThreadPool(10);  
	
	/**
	 * 属性文件加载对象
	 */
	private static PropertiesLoader loader = new PropertiesLoader("mht.properties");
	
	/**
	 * 数据备份还原参数
	 */
	public static final String BACKUP = "backup";
	public static final String RESTORE = "restore";

	/**
	 * 显示/隐藏
	 */
	public static final String SHOW = "1";
	public static final String HIDE = "0";

	/**
	 * 是/否
	 */
	public static final String YES = "1";
	public static final String NO = "0";
	
	/**
	 * 对/错
	 */
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	
	/**
	 * 启用/禁用
	 */
	public static final String ENABLE = "1";
	public static final String DISABLE = "2";
	
	/**
	 * 系统/自定义（应用）
	 */
	public static final String SYSTEM = "1";
	public static final String USERDEFINED = "2";
	
	/**
	 * 单点/超链接接入方式
	 */
	public static final String CASACESSWAY = "1";
	public static final String HTTPACESSWAY = "2";
	public static final String CASEANDHTTP = "3";
	
	public static final String LOGOUT = "log_logout";
	public static final String LOGIN = "log_login";
	public static final String APP = "auth_app";
	public static final String MANAGERAPP = "auth_manager_app";
	public static final String PWSCOMPLEX = "password_complex";
	public static final String PWSMIN = "password_min";
	public static final String COMPLEX = "不要求";
	public static final String RECORD = "记录";
	public static final String ALLOW = "允许";
	public static final String CDNS = "c_dns";
	public static final String CNAME = "c_name";
	public static final String UNAME = "u_name";
	public static final String ULOGO = "u_logo";
	public static final String ROLEUSER = "general_user";
	public static final String ROLESYSTEM = "system";
	public static final String ROLESUPERSYSTEM = "super_system";
	
	/**
	 * 上传文件基础虚拟路径
	 */
	public static final String USERFILES_BASE_URL = "/userfiles/";
	
	/**
	 * 系统设置上传图片路径
	 */
	public static final String SYSTEM_DISPLAY_URL = "/display/";
	
	/**
	 * 系统设置上传图片路径
	 */
	public static final String SYS_BANNER_URL = "/banner/";
	
	/**
	 * 系统设置上传图片路径
	 */
	public static final String SYS_APPICON_URL = "/appIcon/";
	
	/**
	 * 应用默认图标
	 */
	public static final String APPLICATION_IMAGE = "/static/images/application.png";
	
	/**
	 * 默认头像
	 */
	public static final String PHOTO_IMAGE = "/images/photo.png";
	
	/**
	 * 应用图标上传路径
	 */
	public static final String APPLICATION_BASE_URL = "/application/";
	
	/**
	 * 获取当前对象实例
	 */
	public static Global getInstance() {
		return global;
	}
	
	/**
	 * 获取配置
	 * @see ${fns:getConfig('adminPath')}
	 */
	public static String getConfig(String key) {
		String value = map.get(key);
		if (value == null){
			value = loader.getProperty(key);
			map.put(key, value != null ? value : StringUtils.EMPTY);
		}
		return value;
	}
	
	/**
	 * 获取管理端根路径
	 */
	public static String getAdminPath() {
		return getConfig("adminPath");
	}
	
	/**
	 * 获取前端根路径
	 */
	public static String getFrontPath() {
		return getConfig("frontPath");
	}
	
	/**
	 * 获取URL后缀
	 */
	public static String getUrlSuffix() {
		return getConfig("urlSuffix");
	}
	
	/**
	 * 是否是演示模式，演示模式下不能修改用户、角色、密码、菜单、授权
	 */
	public static Boolean isDemoMode() {
		String dm = getConfig("demoMode");
		return "true".equals(dm) || "1".equals(dm);
	}
	
	/**
	 * 在修改系统用户和角色时是否同步到Activiti
	 */
	public static Boolean isSynActivitiIndetity() {
		String dm = getConfig("activiti.isSynActivitiIndetity");
		return "true".equals(dm) || "1".equals(dm);
	}
    
	/**
	 * 页面获取常量
	 * @see ${fns:getConst('YES')}
	 */
	public static Object getConst(String field) {
		try {
			return Global.class.getField(field).get(null);
		} catch (Exception e) {
			// 异常代表无配置，这里什么也不做
		}
		return null;
	}

	/**
	 * 获取上传文件的根目录
	 * @return
	 */
	public static String getUserfilesBaseDir() {
		String dir = getConfig("userfiles.basedir");
		if (StringUtils.isBlank(dir)){
			try {
				dir = ServletContextFactory.getServletContext().getRealPath("/");
			} catch (Exception e) {
				return "";
			}
		}
		if(!dir.endsWith("/")) {
			dir += "/";
		}
//		System.out.println("userfiles.basedir: " + dir);
		return dir;
	}
	
    /**
     * 获取工程路径
     * @return
     */
    public static String getProjectPath(){
    	// 如果配置了工程路径，则直接返回，否则自动获取。
		String projectPath = Global.getConfig("projectPath");
		if (StringUtils.isNotBlank(projectPath)){
			return projectPath;
		}
		try {
			File file = new DefaultResourceLoader().getResource("").getFile();
			if (file != null){
				while(true){
					File f = new File(file.getPath() + File.separator + "src" + File.separator + "main");
					if (f == null || f.exists()){
						break;
					}
					if (file.getParentFile() != null){
						file = file.getParentFile();
					}else{
						break;
					}
				}
				projectPath = file.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return projectPath;
    }
    
    /**
	 * 写入properties信息
	 * 
	 * @param key
	 *            名称
	 * @param value
	 *            值
	 */
	public static void modifyConfig(String key, String value) {
		try {
			// 从输入流中读取属性列表（键和元素对）
			Properties prop = getProperties();
			prop.setProperty(key, value);
			String path = Global.class.getResource("/mht.properties").getPath();
			FileOutputStream outputFile = new FileOutputStream(path);
			prop.store(outputFile, "modify");
			outputFile.close();
			outputFile.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 返回　Properties
	 * @param fileName 文件名　(注意：加载的是src下的文件,如果在某个包下．请把包名加上)
	 * @param 
	 * @return
	 */
	public static Properties getProperties(){
		Properties prop = new Properties();
		try {
			Reader reader = Resources.getResourceAsReader("/mht.properties");
			prop.load(reader);
		} catch (Exception e) {
			return null;
		}
		return prop;
	}

	public static String getApplUploadPath() {
		String path = "";
		try {
			path = ServletContextFactory.getServletContext().getRealPath("/");
			path = path.substring(0, path.lastIndexOf("webapps"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return path;
	}
	
	public static void main(String[] args) {
		System.out.println(getApplUploadPath());
	}
}
