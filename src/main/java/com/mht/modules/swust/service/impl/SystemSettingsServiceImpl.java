package com.mht.modules.swust.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.mht.common.service.CrudService;
import com.mht.common.utils.IdGen;
import com.mht.common.utils.StringUtils;
import com.mht.modules.swust.dao.SysPhotolistDao;
import com.mht.modules.swust.entity.SmsSendMobile;
import com.mht.modules.swust.entity.SysPhotolist;
import com.mht.modules.swust.entity.SysUser;
import com.mht.modules.swust.service.SystemSettingsService;
import com.mht.modules.swust.utils.ConstantUtil;
import com.mht.modules.swust.utils.PhotoUtils;
import com.mht.modules.sys.entity.User;
/**
 * 
 * @ClassName: SystemSettingsServiceImpl
 * @Description: 
 * @author com.mhout.wzw
 * @date 2017年7月26日 下午4:47:00 
 * @version 1.0.0
 */
@Service
public class SystemSettingsServiceImpl  extends CrudService<SysPhotolistDao, SysPhotolist>{
	
	@Autowired
	SysPhotolistDao photoDao;
	
	
	@Transactional(readOnly=false)
	public void updateByPhoto(SysPhotolist photolist,User user, String path, MultipartFile photo) {
		String fileName =IdGen.uuid();
		int hash = fileName.hashCode();
		String has = Integer.toHexString(hash);
		String imgpath = has.charAt(0) + File.separator + has.charAt(1);
		File file = new File(path, imgpath);
		file.mkdirs();
		try {
			photo.transferTo(new File(path + File.separator + imgpath, fileName+photolist.getPhotoType().toLowerCase()));

		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		photolist.setUserId(user.getId());
		photolist.setName("0");
		photolist.setPhotoType(PhotoUtils.getPhotoType(photo).toUpperCase());
		photolist.setPhotoPath("upload/"+has.charAt(0) + "/" + has.charAt(1)+"/"+fileName+"."+photolist.getPhotoType().toLowerCase());
		photolist.setCreateTime(new Date());
		super.save(photolist);
	}
	public List<SysPhotolist> SelectAllByPhoto(User user) {
		return photoDao.selectAll("0", null);
	}
	public SysPhotolist selectPhotoByDefaultKey(SysPhotolist sysPhotolist) {
		
		return photoDao.selectByterm(sysPhotolist);
//		return photoDao.selectByPrimaryKey(Integer.parseInt(ConstantUtil.default_pageCode));
	}
	@Transactional(readOnly=false)
	public void changepicture(String id) {
		photoDao.changepicture(id);
		photoDao.changepictureToNo(id);
	}
	@Transactional(readOnly=false)
	public void deletepicture(String id) {
		photoDao.delete(id);
	}
	@Transactional(readOnly=false)
	public void changeHeadPhoto(SysPhotolist photolist,User user, String path, MultipartFile photo) {
		String fileName =IdGen.uuid();
		int hash = fileName.hashCode();
		String has = Integer.toHexString(hash);
		String imgpath = has.charAt(0) + File.separator + has.charAt(1);
		File file = new File(path, imgpath);
		file.mkdirs();
		try {
			photo.transferTo(new File(path + File.separator + imgpath, fileName+photolist.getPhotoType().toLowerCase()));

		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		photolist.setId(user.getId());
		photolist.setUserId(user.getId());
		photolist.setName("1");
		photolist.setPhotoType(PhotoUtils.getPhotoType(photo).toUpperCase());
		photolist.setPhotoPath("upload/"+has.charAt(0) + "/" + has.charAt(1)+"/"+fileName+"."+photolist.getPhotoType().toLowerCase());
		photolist.setCreateTime(new Date());
		super.save(photolist);
	}
	public List<SmsSendMobile> adminPhone(String carType) {
		// TODO Auto-generated method stub
		return photoDao.selectAdminPhone(carType);
	}

}




