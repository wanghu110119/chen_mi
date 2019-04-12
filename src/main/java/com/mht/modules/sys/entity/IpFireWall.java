package com.mht.modules.sys.entity;

import org.hibernate.validator.constraints.Length;

import com.mht.common.persistence.DataEntity;

/**
 * @ClassName: IpFireWall
 * @Description: IP访问管理
 * @author com.mhout.xyb
 * @date 2017年5月11日 下午3:34:17
 * @version 1.0.0
 */
public class IpFireWall extends DataEntity<IpFireWall> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Dict dict; // Ip类型 1.黑名单 2.白名单
	private String ip; // ip地址
	private String maxIp; // ip最大值
	private String ipType; // ip段类型 （1、ip地址 2、ip段）
	private String derc; // 描述

	public Dict getDict() {
		return dict;
	}

	public void setDict(Dict dict) {
		this.dict = dict;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIpType() {
		return ipType;
	}

	public void setIpType(String ipType) {
		this.ipType = ipType;
	}

	public String getMaxIp() {
		return maxIp;
	}

	public void setMaxIp(String maxIp) {
		this.maxIp = maxIp;
	}

	@Length(min = 0, max = 255, message = "用户类型长度必须介于 1 和 255 之间")
	public String getDerc() {
		return derc;
	}

	public void setDerc(String derc) {
		this.derc = derc;
	}
	
}
