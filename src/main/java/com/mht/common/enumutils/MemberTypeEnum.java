package com.mht.common.enumutils;

/**
 * @ClassName: CarTypeEnum
 * @Description: 车辆类型
 * @author com.mhout.wzw
 * @date 2017年8月3日 上午10:56:07 
 * @version 1.0.0
 */
public enum MemberTypeEnum {
	
	NONE("普通用户", "0"), 
	
	SMALL("黄金会员", "1"), 
    
	LARGE("铂金会员", "2"),
    
    VERYLARGE("钻石会员", "3"),
	
	SUPER("爸爸豁茶","4");
	
    private String name;  
    private String param;  
    
    private MemberTypeEnum(String name, String param) {  
        this.name = name;
        this.param = param;  
    }
    
	public String getName() {
		return name;
	}
	
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
}
