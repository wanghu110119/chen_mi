package com.mht.modules.swust.websocket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.Session;

/**
 * @ClassName: SwustSocketContant
 * @Description: socket消息常量信息
 * @author com.mhout.xyb
 * @date 2017年8月2日 下午3:28:59 
 * @version 1.0.0
 */
public class SwustSocketContant {
	
	/**
	 * 用户websocket缓存
	 */
	public static Map<Session, String> swustMap = new ConcurrentHashMap<Session, String>();

}
