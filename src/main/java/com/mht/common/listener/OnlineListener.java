package com.mht.common.listener;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * 监听在线用户上线下线 add by duanql 2013-06-07
 */
public class OnlineListener implements ServletContextListener, HttpSessionAttributeListener, HttpSessionListener {

    public OnlineListener() {
    }

    private ServletContext application = null;

    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void contextInitialized(ServletContextEvent event) {
        // 初始化一个application对象
        this.application = event.getServletContext();
        // 设置一个列表属性，用于保存在线用户名
        this.application.setAttribute("online", new LinkedList<String>());

    }

    // 往会话中添加属性时会回调的方法
    public void attributeAdded(HttpSessionBindingEvent event) {
        // 取得用户名列表
        List<String> online = (List<String>) this.application.getAttribute("online");
        if ("username".equals(event.getName())) {
            // 将当前用户名添加到列表中
            online.add((String) event.getValue());
        }
        // 将添加后的列表重新设置到application属性中
        this.application.setAttribute("online", online);
    }

    public void attributeRemoved(HttpSessionBindingEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void attributeReplaced(HttpSessionBindingEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void sessionCreated(HttpSessionEvent arg0) {
        // TODO Auto-generated method stub

    }

    // 会话销毁时会回调的方法
    public void sessionDestroyed(HttpSessionEvent event) {
        // 取得用户名列表
        List<String> online = (List<String>) this.application.getAttribute("online");
        // 取得当前用户名
        String username = (String) event.getSession().getAttribute("username");
        // 将此用户名从列表中删除
        online.remove(username);
        // 将删除后的列表重新设置到application属性中
        this.application.setAttribute("online", online);
    }

}
