package com.mht.common.utils.excel.fieldtype;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.mht.common.enumutils.SysOrderEnum;


public class StatusType {

	/**
	 * 获取对象值（导入）
	 */
	public static Object getValue(String val) {
		return val;
	}

	/**
	 * 获取对象值（导出）
	 */
	public static String setValue(Object val) {
		List<String> list = (List<String>)val;
		if (CollectionUtils.isNotEmpty(list)) {
			String state = list.get(0);
			String pass = list.get(1);
			if (SysOrderEnum.UNAUDITED.getParam().equals(state)) {
				return SysOrderEnum.UNAUDITED.getName();
			} else if (SysOrderEnum.AUDITED.getParam().equals(state)) {
				if (SysOrderEnum.NOTPASS.getParam().equals(pass)) {
					return SysOrderEnum.NOTPASS.getName();
				} else {
					return SysOrderEnum.PASS.getName();
				}
			}
		}
		return "";
	}
}
