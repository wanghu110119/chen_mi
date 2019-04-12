package com.mht.modules.swust.utils;

import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * @ClassName: PhotoUtils
 * @Description: 
 * @author com.mhout.wzw
 * @date 2017年7月31日 下午8:28:37 
 * @version 1.0.0
 */
public class PhotoUtils {
	public static String getPhotoType(MultipartFile photo){
		String phototype = photo.getOriginalFilename();
		int index = phototype.lastIndexOf(".");
		String type = phototype.substring(index+1);
		return type;
	}
}
