package com.mht.common.enumutils;

/**
 * @ClassName: SwustReplyEnum
 * @Description: 接口消息回复状态
 * @author com.mhout.wzw
 * @date 2017年8月3日 上午10:11:25 
 * @version 1.0.0
 */
public enum SwustReplyEnum {
	
    NONE("无车辆信息", "-1"), 
    
    PAY("收费", "0"),
    
    FREE("免费", "1"),
    
    OVER("已离场", "1"),
    
    PREMIT("已审核","1"),
    
    FINISH("已收费", "3"),
	
	IN("车辆已入场","3"),
	
	TIMEOUT("已过期","2");
	
    private String name;  
    private String param;  
    
    private SwustReplyEnum(String name, String param) {  
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
