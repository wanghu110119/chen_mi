package com.mht.common.enumutils;

/**
 * @ClassName: CarTypeEnum
 * @Description: 车辆类型
 * @author com.mhout.wzw
 * @date 2017年8月3日 上午10:56:07 
 * @version 1.0.0
 */
public enum CarTypeEnum {
	
	SMALL("深邃恐惧黑", "1"), 
    
	LARGE("典雅复古风", "2"),
    
    VERYLARGE("激情摩洛哥", "3"),
	
	SUPER("没钱蹲客厅","4");
	
    private String name;  
    private String param;  
    
    private CarTypeEnum(String name, String param) {  
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
