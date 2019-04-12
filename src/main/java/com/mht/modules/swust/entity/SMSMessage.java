package com.mht.modules.swust.entity;

/**
 * @ClassName: SMSMessage
 * @Description: 消息组装类
 * @author com.mhout.xyb
 * @date 2017年8月9日 下午7:22:58 
 * @version 1.0.0
 */
public class SMSMessage {
	
	private String number; //车牌号码
	private String begin; //开始时间
	private String end; //结束时间
	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getBegin() {
		return begin;
	}
	public void setBegin(String begin) {
		this.begin = begin;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	
	

}
