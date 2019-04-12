package com.mht.common.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.mht.common.datasource.constants.DefaultDataSource;
import com.mht.common.exception.BusinessException;
/**
 * 动态数据源切换配置类
 * @author 张继平
 * @date 2017-05-05
 *
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
	
	
	
	//注意：如果在此处添加了数据源，请维护下DefaultDataSource类中的数据源
	public static final String DATA_SOURCE = "dataSource";//统一认证平台数据源
	public static final String DATA_SOURCE_BDC = "dataSourceBdc";//数据共享平台数据源
	public static final String DATA_SOURCE_SDEP = "dataSourceSdep";//数据交换平台数据源
	public static final String DATA_SOURCE_PORTAL = "dataSourcePortal";//门户平台数据源
	public static final String DATA_SOURCE_BI = "dataSourceBi";//BI平台数据源
	public static final String DATA_SOURCE_MHT_DATA_CENTER = "dataSourceMhtDataCenter";//学校中心数据库
	public static final String DATA_SOURCE_SERVICE = "dataSourceService";//服务大厅数据库
	
	
	
	

	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();
	
	
	 // 数据源Map对象
    private Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
   
    // 能够正常访问的数据源列表
    private List<String> avaiableKeys = new ArrayList<String>();

    // 不可用数据源key列表，在连接不成功后保存key
    private List<String> disableKeys = new ArrayList<String>();

    public List<String> getDisableKeys() {
        return disableKeys;
    }

    public void setDisableKeys(List<String> disableKeys) {
        this.disableKeys = disableKeys;
    }

    public List<String> getAvaiableKeys() {
        return avaiableKeys;
    }
	
	
	/**  
     * @Description: 获取数据源名称  
     * @param   
     * @return String  
     * @throws  
     */  
	public static String getCurrentLookupKey() {    
        return (String) contextHolder.get();    
    }
	/**  
     * @Description: 设置数据源类型  
     * @param dataSource  数据源名称 
     * @return void  
     * @throws  
     */ 
	public static void setCurrentLookupKey(String currentLookupKey) {    
        contextHolder.set(currentLookupKey);    
    }
	 /**  
     * @Description: 清除数据源名称 
     * @param   
     * @return void  
     * @throws  
     */    
    public static void clearDataSource() {    
        contextHolder.remove();    
    }    
	@Override
	protected Object determineCurrentLookupKey() {
		return getCurrentLookupKey();
	}
	
	 /**
     * 批量设置数据源信息
     *
     * @param 数据源信息map
     * @throws Exception
     * @author zhongdt 2016年3月7日
     */
    public void setDataSource(Map<String, Map<String, String>> dataSourceInfo) throws BusinessException {

        if (dataSourceInfo == null || dataSourceInfo.isEmpty()) {
            logger.warn("数据源列为空");
            throw new BusinessException("数据源列为空");
        }
        // 清空动态添加的数据源，只保留系统默认的数据源
        clearTargetDataSource();
        // 根据数据源列表重新构造数据源列表对象
        for (Map.Entry<String, Map<String, String>> entry : dataSourceInfo.entrySet()) {
            String sourceKey = entry.getKey();

            Map<String, String> property = entry.getValue();
            // 必填业务逻辑判断
        	validateDataSource(sourceKey,property);
        	
        	// 构造数据源对象DruidDataSource
        	DruidDataSource sourceBean = createDataSourceBean(sourceKey, property);
            // 构造数据源对象map
            this.targetDataSources.put(sourceKey, sourceBean);
        }
        // 调用父类方法重新设置数据源列表
        super.setTargetDataSources(targetDataSources);

        // 调用次方法告诉spring，需要刷新数据源列表
        afterPropertiesSet();
        // 初始化数据源状态
        initDataSourceStatus();
    }

    /**
     * 添加单个 数据源到动态数据源
     * 
     * @param key:数据源 uniquerKey
     * @param property:连接属性map对象
     * @throws Exception
     * @author zhongdt 2016年3月7日
     */
    public void addDataSource(String sourceKey, Map<String, String> property) throws BusinessException {
    	// 必填业务逻辑判断
    	validateDataSource(sourceKey,property);
        
        // 组装datasource对象
    	DruidDataSource sourceBean =  createDataSourceBean(sourceKey, property);
    	
        // 添加新的数据源到数据源列表
        this.targetDataSources.put(sourceKey, sourceBean);
        // 调用父类方法重新设置数据源列表
        super.setTargetDataSources(targetDataSources);
        // 刷新数据源信息
        afterPropertiesSet();
    }

    /**
     * 修改谋个 数据源信息，并加载到内存中
     *
     * @throws Exception
     * @author zhongdt 2016年3月7日
     * @throws BusinessException 业务异常
     */
    public void modifyDataSource(String sourceKey, Map<String, String> property) throws BusinessException {
        // 必填业务逻辑判断
    	validateDataSource(sourceKey,property);
    	
        // 切换数据源
    	setCurrentLookupKey(sourceKey);
        // 将已经存在的数据源取出
    	DruidDataSource sourceBean = (DruidDataSource) determineTargetDataSource();
        // 关闭数据源连接
        //sourceBean.close();
        
        sourceBean = createDataSourceBean(sourceKey, property, sourceBean);        
        
        // 重新初始化数据源        
        try {
			sourceBean.init();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 throw new BusinessException("重新加载数据源失败:"+e.getMessage());
		}
        // 添加新的数据源到数据源列表
        this.targetDataSources.put(sourceKey, sourceBean);
        // 调用父类方法重新设置数据源列表
        super.setTargetDataSources(targetDataSources);
        // 刷新数据源信息
        afterPropertiesSet();
        // 初始化修改的数据源
        initDataSourceStatus(sourceKey);
    }

    /**
     * 
     * @Title: validateDataSource 
     * @Description: 校验数据源属性是否均填写
     * @param sourceKey 数据源标识
     * @param property 数据源属性
     * @author huaqiang
     */
    private void validateDataSource(String sourceKey, Map<String, String> property){
    	 // 必要逻辑判断
        if (property == null || property.isEmpty()) {
            logger.warn("数据源属性为空:" + sourceKey);
            throw new BusinessException("数据源属性为空:" + sourceKey);
        }
        if (!property.containsKey("jdbcUrl") || StringUtils.isEmpty(property.get("jdbcUrl"))) {
            throw new BusinessException("数据源url为空:" + sourceKey);
        }
        if (!property.containsKey("driverClassName") || StringUtils.isEmpty(property.get("driverClassName"))) {
            throw new BusinessException("数据源连接驱动为空:" + sourceKey);
        }
        if (!property.containsKey("username") || StringUtils.isEmpty(property.get("username"))) {
            throw new BusinessException("数据源用户名为空:" + sourceKey);
        }
        if (!property.containsKey("password") || StringUtils.isEmpty(property.get("password"))) {
            throw new BusinessException("数据源密码为空:" + sourceKey);
        }
    }
    
    /**
     * 
     * @Title: createDataSourceBean 
     * @Description:  创建SourceBean
     * @param sourceKey
     * @param property
     * @return AtomikosDataSourceBean
     * @author huaqiang
     */
    private DruidDataSource createDataSourceBean(String sourceKey , Map<String, String> property){
    	return createDataSourceBean(sourceKey, property, null);
    }
    
    /**
     * 
     * @Title: createDataSourceBean 
     * @Description: 创建SourceBean
     * @param sourceKey
     * @param property
     * @param sourceBean
     * @return AtomikosDataSourceBean
     * @author huaqiang
     */
    private DruidDataSource createDataSourceBean(String sourceKey , Map<String, String> property, DruidDataSource sourceBean){
    	 if(sourceBean == null){
    		 sourceBean = new DruidDataSource();
    	 }
    	 //从property中获取数据库相关属性
    	 String jdbcUrl = property.get("jdbcUrl").trim();
         String driverClassName = property.get("driverClassName").trim();
         String username = property.get("username").trim();
         String password = property.get("password").trim();
         
         //设置数据源连接属性
         sourceBean.setName(sourceKey);         
         sourceBean.setMaxActive(99);
         
         sourceBean.setDriverClassName(driverClassName);         
         sourceBean.setUrl(jdbcUrl);
         sourceBean.setUsername(username);
         sourceBean.setPassword(password);
        
         return sourceBean;
    }
    
    
    /**
     * 删除数据源
     *
     * @param key 数据源 uniqueKey
     * @throws Exception
     * @author zhongdt 2016年3月10日
     */
    public void removeDataSourceByKey(String key) throws BusinessException {
        if (key == null || StringUtils.isEmpty(key)) {
            return;
        }
        // 判断是否存在
        if (!targetDataSources.containsKey(key)) {
            throw new BusinessException("数据源不存在");
        }
        // 同时删除数据源状态列表
        this.avaiableKeys.remove(key);
        this.disableKeys.remove(key);

        targetDataSources.remove(key);
        // 重新刷新数据源
        super.setTargetDataSources(targetDataSources);
        afterPropertiesSet();
    }

    /**
     * 设置可用的数据源key列表
     * 此方法供监控线程调用
     * 
     * @param keys
     * @author zhongdt 2016年3月25日
     */
    public void setAvaiableKeys(List<String> keys) {
        this.avaiableKeys = keys;
    }

    public void removeAvaiableKey(String key) {
        if (this.avaiableKeys.contains(key)) {
            this.avaiableKeys.remove(key);
        }
    }

    public void removeDisableKey(String key) {
        if (this.disableKeys.contains(key)) {
            this.disableKeys.remove(key);
        }
    }

    /**
     * 验证数据源是否存在
     *
     * @param 数据源唯一标识
     * @return true or false
     * @author zhongdt 2016年3月8日
     */
    public boolean isExists(String key) {
        if (StringUtils.isEmpty(key)) {
            return false;
        }
        return targetDataSources.containsKey(key);
    }

    /**
     * 判断数据库是否正常
     *
     * @param key
     * @return true false
     * @author zhongdt 2016年3月21日
     */
    public boolean isValid(String key) {
        if (StringUtils.isEmpty(key)) {
            return false;
        }
        return avaiableKeys.contains(key) ? true : false;
    }

    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
    	//初始化父类的targetDataSources 如果不初始化，在框架启动时调用afterPropertiesSet（）方法会抛出异常
    	super.setTargetDataSources(targetDataSources);
		this.targetDataSources = targetDataSources;
		
	}

	/**
     * 
     * 解析数据库连接失败时返回的最底层的原因
     *
     * @param e
     * @return 数据库抛出的exception
     * @author zhongdt 2016年5月19日
     */
    public  Exception getCause(Exception e) {
        Exception cause = new Exception();
        //递归取最后一层的cause
        while (e != null && (e = (Exception) e.getCause()) != null) {
            cause = e;
            continue;
        }
        return cause;
    }

    /**
     * 初始化单个数据源
     * 方法的注释
     *
     * @param sourcekey
     * @author zhongdt 2016年5月3日
     */
    private void initDataSourceStatus(String sourcekey) {
        // 删除可用 及不可用标识，重新加载
        disableKeys.remove(sourcekey);
        avaiableKeys.remove(sourcekey);
        List<String> keys = new ArrayList<String>();
        // 遍历数据源对象
        for (Entry<Object, Object> entry : this.targetDataSources.entrySet()) {

            String key = entry.getKey() + "";

            if (!sourcekey.equals(key)) {
                continue;
            }
            // 先切换一次数据源，然后再取连接，如果能取到，说明连接正常
           setCurrentLookupKey(entry.getKey() + "");
            try (Connection conn = getConnection()) {
                // 取出一个连接，异常时候会报错
                keys.add(key);
            } catch (Exception e) {
                // 为了避免数据库被锁
                // 只要是有报错，就把数据源信息放到不可用列表中，不在监控，需要在前端通过手动方式查看
                disableKeys.add(entry.getKey() + "");
                logger.error("获取数据源conn失败, uniqueKey =:" + entry.getKey() + ",reason="+ getCause(e).getMessage());
                return;
            }
            avaiableKeys.add(key);
        }

    }

   

    /**
     * 初始化所有数据源连接状态
     *
     * @author zhongdt 2016年5月6日
     */
    private void initDataSourceStatus() {
        List<String> keys = new ArrayList<String>();
        for (Entry<Object, Object> entry : this.targetDataSources.entrySet()) {

            String key = entry.getKey() + "";
            // 如果数据源在不可用的列表中则不进行数据库连接，原因是重复操作会锁用户
            if (disableKeys.contains(key)) {
                continue;
            }
            // 切换数据源
            setCurrentLookupKey(entry.getKey() + "");
            // 获取连接，获取成功则数据源正常，失败则不正常，失败后为了避免锁账户，不再重试
            try (Connection conn = getConnection()) {
                // 取出一个连接，异常时候会报错
                keys.add(key);
            } catch (Exception e) {
                // 为了避免数据库被锁
                // 只要是有报错，就把数据源信息放到不可用列表中，不在监控，需要在前端通过手动方式查看
                disableKeys.add(entry.getKey() + "");
                logger.error("获取数据源conn失败, uniqueKey =:" + entry.getKey() + ",reason=" + getCause(e).getMessage());

            }
        }
        // 重新设置 可用数据源列表
        setAvaiableKeys(keys);
    }

    /**
     * 
     * @Title: clearTargetDataSource 
     * @Description: 清除动态添加的targetDataSource，只保留系统默认的几个数据源
     * @author huaqiang
     */
    private void clearTargetDataSource(){    	
    	Map<Object, Object> newDataSources = new HashMap<Object, Object>();
    	//从数据源中获取系统默认的数据源
    	for(DefaultDataSource dataSource : DefaultDataSource.values()){
    		//将默认的数据源放在心的dataSources中
    		newDataSources.put(dataSource.getDataSourceName(), targetDataSources.get(dataSource.getDataSourceName()));
    	}
    	//再进行赋值，使targetDataSources只保留最初的数据源
    	targetDataSources = newDataSources;
  
    }

}
