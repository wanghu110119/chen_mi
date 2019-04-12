package com.mht.modules.sys.manager;

import java.io.Serializable;
import java.util.Date;

import com.mht.modules.sys.entity.User;

/***
 * 在线用户对象**
 * 
 * @author JueYue
 * @date 2013-9-28
 * @version 1.0
 */
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    private User user;

    /**
     * 用户IP
     */
    private String ip;
    /**
     * 登录时间
     */
    private Date logindatetime;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(java.lang.String ip) {
        this.ip = ip;
    }

    public Date getLogindatetime() {
        return logindatetime;
    }

    public void setLogindatetime(Date logindatetime) {
        this.logindatetime = logindatetime;
    }

}
