package com.mht.modules.sys.entity;

import com.mht.common.persistence.DataEntity;
import com.mht.modules.ident.entity.SysProject;

/**
 * @ClassName: SysConfig
 * @Description: 系统配置
 * @author com.mhout.xyb
 * @date 2017年5月15日 上午10:59:18
 * @version 1.0.0
 */
public class SysConfig extends DataEntity<SysConfig> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SysProject sysProject;
	private Dict dict;
	private String code;
	private String label;
	private String paramType;
	private String value;

	public SysProject getSysProject() {
		return sysProject;
	}

	public void setSysProject(SysProject sysProject) {
		this.sysProject = sysProject;
	}

	public Dict getDict() {
		return dict;
	}

	public void setDict(Dict dict) {
		this.dict = dict;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getParamType() {
		return paramType;
	}

	public void setParamType(String paramType) {
		this.paramType = paramType;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
}
