package com.mht.modules.ident.entity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.mht.common.persistence.DataEntity;

/**
 * @ClassName: Application
 * @Description: 应用管理
 * @author com.mhout.xyb
 * @date 2017年3月29日 下午2:56:44
 * @version 1.0.0
 */
public class Application extends DataEntity<Application> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String accessWay; // 接入类型（1、单点登录 2、超链接）
    private String serial; // 应用序列号（UUID）
    private String secret; //应用授权码
    private String name; // 应用名称
    private String url; // url地址
    private String ipStrategy; // ip策略 （1、ip地址 2、ip段）
    private String ip; // ip地址
    private String maxip; //ip段
    private String pic; // 应用图标
    private String type; // 应用类型（1、系统应用 2、自定义应用）
    private String status; // 状态 (1、可用 2、禁用)
    private int sort; // 排序

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    @Length(min = 0, max = 100, message = "应用名称长度必须介于 1 和100 之间")
    @NotNull(message = "应用名称不能为空")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull(message = "接入方式不能为空")
    public String getAccessWay() {
        return accessWay;
    }

    public void setAccessWay(String accessWay) {
        this.accessWay = accessWay;
    }

    @NotNull(message = "URL不能为空")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @NotNull(message = "IP策略不能为空")
    public String getIpStrategy() {
        return ipStrategy;
    }

    public void setIpStrategy(String ipStrategy) {
        this.ipStrategy = ipStrategy;
    }

    @NotNull(message = "IP不能为空")
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @NotNull(message = "应用类型不能为空")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @NotNull(message = "IP段不能为空")
	public String getMaxip() {
		return maxip;
	}

	public void setMaxip(String maxip) {
		this.maxip = maxip;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}
	

}

