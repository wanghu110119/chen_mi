/**
 * 
 */
package com.mht.common.datasource.constants;

/**
 * @ClassName: DefaultDataSource
 * @Description: 默认的项目数据源枚举
 * @author 华强
 * @date 2017年6月6日 下午7:43:04 
 * @version 1.0.0
 */
public enum DefaultDataSource {
	
	DATA_SOURCE("dataSource"),//统一认证平台数据源
	DATA_SOURCE_BDC("dataSourceBdc"),//数据共享平台数据源
	DATA_SOURCE_SDEP("dataSourceSdep"),//数据交换平台数据源
	DATA_SOURCE_PORTAL("dataSourcePortal"),//门户平台数据源
	DATA_SOURCE_BI("dataSourceBi"), //BI数据源
	DATA_SOURCE_MHT_DATA_CENTER("dataSourceMhtDataCenter"),//学校中心数据库
	DATA_SOURCE_SERVICE("dataSourceService");//服务大厅数据库
	
	private String dataSourceName;
	
	DefaultDataSource(String dataSourceName){
		this.dataSourceName = dataSourceName;
	}

	public String getDataSourceName() {
		return dataSourceName;
	}
}
