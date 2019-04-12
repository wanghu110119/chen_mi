package com.mht.modules.swust.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;

import com.mht.common.config.Global;
import com.mht.common.utils.IdGen;
import com.mht.common.utils.SpringContextHolder;
import com.mht.modules.swust.dao.SysCarMoneyDao;
import com.mht.modules.swust.entity.SysOrderlist;
import com.mht.modules.swust.service.impl.OrderUserServiceImpl;
import com.mht.modules.sys.utils.UserUtils;
import com.swetake.util.Qrcode;

public class QRCodeCreate {
	private static  OrderUserServiceImpl service  = SpringContextHolder.getBean(OrderUserServiceImpl.class);

	public  static SysOrderlist createQRCode(SysOrderlist order,HttpServletRequest request) {
		String path = request.getServletContext().getContextPath()+"/static/swust/qrCode/";
		String realPath = request.getServletContext().getRealPath("/")+"/static/swust/qrCode/";
		 Qrcode qrcode = new Qrcode();  
		 String gen = IdGen.uuid();
	       qrcode.setQrcodeErrorCorrect('M');//纠错等级（分为L、M、H三个等级）  
	       qrcode.setQrcodeEncodeMode('B');//N代表数字，A代表a-Z，B代表其它字符  
	       qrcode.setQrcodeVersion(0);
	       //生成二维码中要存储的信息  
	       order.setOrderName(order.getOrderReason());
	       order.setUserId(UserUtils.getUser().getId());
			service.saveOrder(order);
	       String qrData = "";
		qrData = request.getScheme()+"://"+Global.getConfig("hostAddress")+":"+request.getServerPort()+request.getContextPath() +"/api/swust/meeting/add?id="+order.getId();  
//	       String qrData = "哥老关些，大家一起high起来，来是come去是go，点头yes，摇头no，谢谢就是三块肉"; 
	       //设置一下二维码的像素  
	       int width = 67+12*(6-1);  
	       int height = 67+12*(6-1);  
	       BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  
	       //绘图  
	       Graphics2D gs = bufferedImage.createGraphics();  
	       gs.setBackground(Color.WHITE);  
	       gs.setColor(Color.BLACK);  
	       gs.clearRect(0, 0, width, height);//清除下画板内容  
	         
	       //设置下偏移量,如果不加偏移量，有时会导致出错。  
	       int pixoff = 2;  
	         
	       byte[] d;
		try {
			d = qrData.getBytes("gb2312");
			  if(d.length > 0 && d.length <120){  
		           boolean[][] s = qrcode.calQrcode(d);  
		           for(int i=0;i<s.length;i++){  
		               for(int j=0;j<s.length;j++){  
		                   if(s[j][i]){  
		                       gs.fillRect(j*3+pixoff, i*3+pixoff, 3, 3);  
		                   }  
		               }  
		           }  
		       }
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  
	       gs.dispose();  
	       bufferedImage.flush();  
	       try {
	    	   if(order.getQrCodeAddress()!=null && !order.getQrCodeAddress().equals("")){
	    		   File file = new File(order.getQrCodeAddress()+"/"+gen+".png");
		    		file.mkdirs();
				ImageIO.write(bufferedImage, "png", file);
	    	   }
	    	   File file = new File(realPath+gen+".png");
	    		file.mkdirs();
			ImageIO.write(bufferedImage, "png", file);
//			ImageIO.write(bufferedImage, "png", new File("H:/code/qrcode.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
	       order.setQrCodeAddress(path+gen+".png");
	       service.saveOrder(order);
		return order;  
	}
	public static void main(String[] args) {
		SysOrderlist order = new SysOrderlist();
				order.setQrCodeAddress("H:/code/");
				order.setOrderReason("西南科大空气动力学学术讨论");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
