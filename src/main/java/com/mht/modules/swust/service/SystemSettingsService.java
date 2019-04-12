package com.mht.modules.swust.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.mht.modules.swust.entity.SysPhotolist;
import com.mht.modules.swust.entity.SysUser;
/**
 * 
 * @ClassName: SystemSettingsService
 * @Description: 
 * @author com.mhout.wzw
 * @date 2017年7月26日 下午4:47:10 
 * @version 1.0.0
 */
public interface SystemSettingsService {
	/**
	 * 
	 * @Title: updateByPhoto 
	 * @Description: 上传图片方法
	 * @param user
	 * @param path
	 * @param photo
	 * @author com.mhout.wzw
	 */
	void updateByPhoto(SysUser user, String path, MultipartFile photo);

	/**
	 * 
	 * @Title: SelectAllByPhoto 
	 * @Description: 查询所有背景图片
	 * @param user
	 * @return
	 * @author com.mhout.wzw
	 */
	List<SysPhotolist> SelectAllByPhoto(SysUser user);
/**
 * 
 * @Title: selectPhotoByDefaultKey 
 * @Description: 页面打开默认查询对象
 * @return
 * @author com.mhout.xyb
 */
	SysPhotolist selectPhotoByDefaultKey();

}
