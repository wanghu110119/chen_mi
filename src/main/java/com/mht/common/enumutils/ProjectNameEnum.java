package com.mht.common.enumutils;

/**
 * @ClassName: ProjectNameEnum
 * @Description: 项目名称枚举类
 * @author com.mhout.xyb
 * @date 2017年7月13日 上午9:43:16 
 * @version 1.0.0
 */
public enum ProjectNameEnum {
	//统一鉴权
    UAP("uap", 1), 
    //bi
    BI("bi", 2),
    //数据共享中心
    BDC("bdc",3),
    //数据交换平台
    SDEP("sdep",4),
    //服务大厅
    SERVICE("service",5),
    //统一信息门户
    PORTAL("portal",6),
    //资源服务平台
    RSP("rsp",7);  
	
    private String name;  
    private int index;  
    
    private ProjectNameEnum(String name, int index) {  
        this.name = name;
        this.index = index;  
    }
	public String getName() {
		return name;
	}
	public int getIndex() {
		return index;
	}
	
}
