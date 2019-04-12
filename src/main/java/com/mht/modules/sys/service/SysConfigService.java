package com.mht.modules.sys.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.mht.common.config.Global;
import com.mht.common.service.CrudService;
import com.mht.common.utils.FileUtils;
import com.mht.common.utils.IdGen;
import com.mht.modules.ident.dao.SysProjectDao;
import com.mht.modules.sys.dao.SysConfigDao;
import com.mht.modules.sys.entity.SysConfig;
import com.mht.modules.sys.utils.UserUtils;

/**
 * @ClassName: SysConfigService
 * @Description: 系统配置业务层
 * @author com.mhout.xyb
 * @date 2017年5月15日 上午11:28:06 
 * @version 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class SysConfigService  extends CrudService<SysConfigDao, SysConfig>{
	
	@Autowired
	private SysConfigDao sysConfigDao;
	
	@Autowired
	private SysProjectDao sysProjectDao;
	
	/**
	 * @Title: findTypeList 
	 * @Description: 安全策略配置类型
	 * @param safeStrategy
	 * @return
	 * @author com.mhout.xyb
	 */
	public List<SysConfig> findTypeList(SysConfig sysConfig) {
		return sysConfigDao.findTypeList(sysConfig);
	}
	
	/**
	 * @Title: saveSafe 
	 * @Description: 保存配置
	 * @param safeStrategy
	 * @return
	 * @author com.mhout.xyb
	 */
	@Transactional(readOnly = false)
	public boolean saveSafe(List<SysConfig> list) {
		if (CollectionUtils.isNotEmpty(list)) {
			for (SysConfig sysConfig : list) {
				SysConfig resafe = sysConfigDao.get(sysConfig.getId());
				if (resafe != null) {
					resafe.setCreateBy(UserUtils.getUser());
					resafe.setCreateDate(new Date());
					resafe.setUpdateBy(UserUtils.getUser());
					resafe.setUpdateDate(new Date());
					resafe.setValue(sysConfig.getValue());
					if (StringUtils.isNotBlank(sysConfig.getSysProject().getId())) {
						resafe.setSysProject(sysProjectDao.get(sysConfig.getSysProject().getId()));
					}
					sysConfigDao.update(resafe);
				}
			}
			return false;
		}
		return true;
	}

	/**
	 * @Title: saveFile 
	 * @Description: 文件上传
	 * @param fileIn
	 * @param id
	 * @return
	 * @author com.mhout.xyb
	 */
	@Transactional(readOnly = false)
	public boolean saveFile(MultipartFile fileIn, String id) throws IOException {
		//文件上传
		boolean message = fileIn != null && fileIn.getInputStream() != null 
				&& StringUtils.isNotBlank(fileIn.getOriginalFilename())
				&& StringUtils.isNotBlank(id);
		if (message) {
			SysConfig config = sysConfigDao.get(id);
			if (config != null) {
				String value = getFileName(config.getValue());
				String fileName = fileIn.getOriginalFilename();
				boolean isnew = StringUtils.isBlank(value) || 
						StringUtils.isNotBlank(value) && !value.equals(fileName);
				if (isnew) {
					//修改文件名
					String uuid = IdGen.uuid();
					fileName = uuid + fileName.substring(fileName.lastIndexOf("."), fileName.length());
					//获取全路径
					String path = Global.getApplUploadPath() + Global.APPLICATION_BASE_URL;
					FileUtils.uploadFile(path, fileName, fileIn.getInputStream());
					config.setValue(Global.APPLICATION_BASE_URL + fileName);
					config.setCreateBy(UserUtils.getUser());
					config.setCreateDate(new Date());
					config.setUpdateBy(UserUtils.getUser());
					config.setUpdateDate(new Date());
					sysConfigDao.update(config);
				}
			}
		}
		return message;
	}
	
	/**
	 * @Title: getFileName 
	 * @Description: 获取文件名
	 * @param name
	 * @return
	 * @author com.mhout.xyb
	 */
	private String getFileName(String name) {
		if (StringUtils.isNotBlank(name)) {
			return name.substring(name.lastIndexOf("/"), name.length());
		}
		return null;
	}
	
	/**
	 * @Title: getConfigValue 
	 * @Description: 获取配置参数信息
	 * @param code
	 * @return
	 * @author com.mhout.xyb
	 */
	public String getConfigValue(String code) {
		SysConfig sysConfig = sysConfigDao.findByCode(code);
		if (sysConfig != null) {
			return sysConfig.getValue();
		}
		return "";
	}
}
