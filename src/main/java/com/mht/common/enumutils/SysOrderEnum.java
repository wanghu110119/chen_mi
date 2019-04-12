package com.mht.common.enumutils;

/**
 * @ClassName: SysOrderEnum
 * @Description: 预约状态
 * @author com.mhout.xyb
 * @date 2017年8月4日 上午10:19:32 
 * @version 1.0.0
 */
public enum SysOrderEnum {
	
	PASS("审核通过", "1"),
	
	NOTPASS("审核未通过", "2"),
	
	UNAUDITED("未审核", "0"), 
    
	AUDITED("已审核", "1"),
    
    NOCHARGE("免费", "2"),
    
    ENTERINTO("车辆已进入", "2"),
    
    HASCHARGE("已收费", "3");
	
    private String name;  
    private String param;  
    
    private SysOrderEnum(String name, String param) {  
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
