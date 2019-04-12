package com.mht.modules.swust.websocket;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 * @ClassName: WebSocketTest
 * @Description: 消息服务器端
 * @author com.mhout.xyb
 * @date 2017年8月2日 下午6:25:36 
 * @version 1.0.0
 */
@ServerEndpoint(value = "/websocket/{userId}")
public class SwustSocketServer {
	
	private Session session;

	/**
	 * @Title: onOpen 
	 * @Description: 连接建立成功
	 * @param userId 用户id
	 * @param session
	 * @author com.mhout.xyb
	 */
	@OnOpen
	public void onOpen(@PathParam("userId") String userId, Session session) {
		this.session = session;
		SwustSocketContant.swustMap.put(session, userId);
	}

	/**
	 * @Title: onClose 
	 * @Description: 连接关闭
	 * @author com.mhout.xyb
	 */
	@OnClose
	public void onClose() {
		SwustSocketContant.swustMap.remove(session);
	}

	/**
	 * @Title: onMessage 
	 * @Description: 收到客户端消息
	 * @param message
	 * @param session
	 * @author com.mhout.xyb
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		
	}

	/**
	 * @Title: onError 
	 * @Description: 发生错误
	 * @param session
	 * @param error
	 * @author com.mhout.xyb
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		
	}
	
	/**
	 * @Title: sendMessage 
	 * @Description: 向客户端发送消息
	 * @param message
	 * @throws IOException
	 * @author com.mhout.xyb
	 */
	public void sendMessage(String message) throws IOException {
		this.session.getBasicRemote().sendText(message);
	}

}
