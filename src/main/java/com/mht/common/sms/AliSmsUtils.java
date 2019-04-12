package com.mht.common.sms;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.mht.common.config.Global;
import com.mht.common.utils.DateUtils;
import com.mht.modules.sys.utils.UserUtils;

/**
 * @ClassName: AliSmsUtils
 * @Description: 阿里大鱼短信工具类
 * @author com.mhout.xyb
 * @date 2017年8月2日 下午8:08:35 
 * @version 1.0.0
 */
public class AliSmsUtils {
	
	//产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";
    
    private final static Logger log = LoggerFactory.getLogger(AliSmsUtils.class);
	
	/**
	 * @Title: sendMessage 
	 * @Description: 发送消息
	 * @param recNum
	 * @param content
	 * @return
	 * @author com.mhout.xyb
	 */
//	public boolean sendMessage(String recNum, String content) {
//		TaobaoClient client = new DefaultTaobaoClient(Global.getConfig("dayu.url"), Global.getConfig("dayu.app.key"),
//				Global.getConfig("dayu.app.secret"));
//		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
//		req.setExtend(Global.getConfig("dayu.req.extent")); // "123456"
//		req.setSmsType(Global.getConfig("dayu.req.smstype"));// 默认：短信类型
//		if (StringUtils.isNumeric(content)) {
//			req.setSmsFreeSignName(Global.getConfig("dayu.smsfreesignname"));// 模板签名
//			req.setSmsParamString("{\"number\":\"" + content + "\",\"begin\":\"" + content + "\","
//					+ "\"end\":\"" + content + "\"}");
//		} else {
//			req.setSmsFreeSignName(Global.getConfig("dayu.smsteminformplatecode"));// 短信通知模板签名
//			req.setSmsParamString(content);
//		}
//		req.setRecNum(recNum);
//		req.setSmsTemplateCode(Global.getConfig("dayu.smstemplatecode"));// 短信模板code
//
//		AlibabaAliqinFcSmsNumSendResponse rsp = null;
//		try {
//			rsp = client.execute(req);
//		} catch (ApiException e) {
//			e.getMessage();
//			return false;
//		}
//		return rsp.getResult().getSuccess();
//	}
//	
//	public static boolean send(String recNum, String content) {
//		AliSmsUtils messageUtil = new AliSmsUtils();
//		return messageUtil.sendMessage(recNum, content);
//	}
	
	/**
	 * @Title: sendSmsAliyun 
	 * @Description: 发送短信（阿里云）
	 * @param recNum
	 * @param Content
	 * @param type 1.单个数据模板 2.多个数据模板
	 * @author com.mhout.xyb
	 */
	public static boolean sendSmsAliyun(String recNum, String content, String type) {
		boolean istrue = false;
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", Global.getConfig("dayu.app.key"), Global.getConfig("dayu.app.secret"));
        try {
			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
	        IAcsClient acsClient = new DefaultAcsClient(profile);
	        SendSmsRequest request = new SendSmsRequest();
	        request.setPhoneNumbers(recNum);
	        request.setSignName(Global.getConfig("dayu.smsfreesignname"));
	        if ("1".equals(type)) {
	        	request.setTemplateCode(Global.getConfig("dayu.smstemplatecode"));
	            request.setTemplateParam("{\"number\":\""+content+"\"}");
	        } else {
	        	request.setTemplateCode(Global.getConfig("dayu.smsteminformplatecode"));
	        	request.setTemplateParam(content);
	        }
	        request.setOutId("yourOutId");
			SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
			String msg = sendSmsResponse.getMessage();
			if (msg.contains("OK")) {
				istrue = true;
			} else {
				log.info("SMS error {} " , msg);
			}
		} catch (ServerException e) { 
			e.printStackTrace();
			istrue = false;
		} catch (ClientException e) {
			e.printStackTrace();
			istrue = false;
		}
        return istrue;
	}

	
	/**
	 * @Title: sendSmsToAdmin 
	 * @Description: 发送短信（阿里云）
	 * @param recNum
	 * @param Content
	 * @param type 1.单个数据模板 2.多个数据模板
	 * @author com.mhout.xyb
	 */
	public static boolean sendSmsToAdmin( String recNum,String content, String type) {
		boolean istrue = false;
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", Global.getConfig("dayu.app.key"), Global.getConfig("dayu.app.secret"));
        try {
			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
	        IAcsClient acsClient = new DefaultAcsClient(profile);
	        SendSmsRequest request = new SendSmsRequest();
	        request.setPhoneNumbers(recNum);
	        request.setSignName(Global.getConfig("dayu.smsfreesignname"));
	        if ("1".equals(type)) {
	        	request.setTemplateCode(Global.getConfig("dayu.smstemplatecode"));
	            request.setTemplateParam("{\"number\":\""+content+"\"}");
	        } else {
	        	request.setTemplateCode(Global.getConfig("dayu.smsteminformplatecode"));
	        	request.setTemplateParam(content);
	        }
	        request.setOutId("yourOutId");
			SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
			String msg = sendSmsResponse.getMessage();
			if (msg.contains("OK")) {
				log.info("SMS success {} " , msg);
				istrue = true;
			} else {
				log.info("SMS error {} " , msg);
			}
		} catch (ServerException e) { 
			e.printStackTrace();
			istrue = false;
		} catch (ClientException e) {
			e.printStackTrace();
			istrue = false;
		}
        return istrue;
	}
	
	
	
	
	@Test
	public  void test() {
		sendSmsInternalVehicle("15882032402","川DSA321");
	}
	
	/**
	 * 内部车辆出入短信请求
	 * @param recNum 接收短信号码
	 * @param content
	 * @param type
	 * @return
	 */
	public static boolean sendSmsInternalVehicle(String recNum, String carNum) {
		boolean istrue = false;
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", Global.getConfig("dayu.app.key"), Global.getConfig("dayu.app.secret"));
        try {
			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
	        IAcsClient acsClient = new DefaultAcsClient(profile);
	        SendSmsRequest request = new SendSmsRequest();
	        request.setPhoneNumbers(recNum);
	        request.setSignName(Global.getConfig("dayu.smssafetysignname"));
	        	request.setTemplateCode(Global.getConfig("dayu.smsinternalvehicletemplatecode"));
	        	request.setTemplateParam("{\"number\":\""+carNum+"\",\"time\":\""+DateUtils.getDate("MM月dd日  HH:mm ")+"\"}");
	        request.setOutId("yourOutId");
			SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
			String msg = sendSmsResponse.getMessage();
			if (msg.contains("OK")) {
				istrue = true;
			} else {
				log.info("SMS error {} " , msg);
			}
		} catch (ServerException e) { 
			e.printStackTrace();
			istrue = false;
		} catch (ClientException e) {
			e.printStackTrace();
			istrue = false;
		}
        return istrue;
	}
}
