package com.mht.modules.swust.entity;

/**
 * @ClassName: MessageDto
 * @Description: 推送消息服务
 * @author com.mhout.wzw
 * @date 2017年8月3日 上午11:34:32 
 * @version 1.0.0
 */
public class MessageDto {
	
	private String number;
	private String beginDate;
	private String endDate;
	private String date;
	private String money;
	
	public MessageDto() {
		
	}
	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	
	

}
