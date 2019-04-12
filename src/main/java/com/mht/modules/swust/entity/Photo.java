/**
 * 
 */
package com.mht.modules.swust.entity;

/**
 * @ClassName: Photo
 * @Description: 
 * @author com.mhout.dhn
 * @date 2017年7月27日 下午7:05:10 
 * @version 1.0.0
 */
public class Photo {
	/**
	 * @ClassName: Photo
	 * @Description: 大图片的路径
	 * @author com.mhout.dhn
	 * @date 2017年7月27日 下午7:05:10 
	 * @version 1.0.0
	 */
      private String bigPhotoUrl;
      /**
  	 * @ClassName: Photo
  	 * @Description: 小图片的路径
  	 * @author com.mhout.dhn
  	 * @date 2017年7月27日 下午7:05:10 
  	 * @version 1.0.0
  	 */
      private String smaPhotoUrl;
	  public String getBigPhotoUrl() {
		return bigPhotoUrl;
	}
	  public void setBigPhotoUrl(String bigPhotoUrl) {
	 	this.bigPhotoUrl = bigPhotoUrl;
    }
	  public String getSmaPhotoUrl() {
		return smaPhotoUrl;
	}
	  public void setSmaPhotoUrl(String smaPhotoUrl) {
	 	this.smaPhotoUrl = smaPhotoUrl;
	}
      
      
      
      
}
