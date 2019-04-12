package com.mht.common.utils.excel.fieldtype;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.mht.common.enumutils.CarTypeEnum;

public class CarType {
	
	/**
	 * 获取对象值（导入）
	 */
	public static Object getValue(String val) {
		List<String> list = new ArrayList<>();
		if (CarTypeEnum.SMALL.getName().equals(val)) {
			list.add(CarTypeEnum.SMALL.getParam());
		} else if (CarTypeEnum.LARGE.getName().equals(val)) {
			list.add(CarTypeEnum.LARGE.getParam());
		} else if (CarTypeEnum.VERYLARGE.getName().equals(val)) {
			list.add(CarTypeEnum.VERYLARGE.getParam());
		}else{
			list.add("0");
		}
		return list;
	}

	/**
	 * 获取对象值（导出）
	 */
	public static String setValue(Object val) {
		List<String> list = (List<String>)val;
		if (CollectionUtils.isNotEmpty(list)) {
			String type = list.get(0);
			if (CarTypeEnum.SMALL.getParam().equals(type)) {
				return CarTypeEnum.SMALL.getName();
			} else if (CarTypeEnum.LARGE.getParam().equals(type)) {
				return CarTypeEnum.LARGE.getName();
			} else if (CarTypeEnum.VERYLARGE.getParam().equals(type)) {
				return CarTypeEnum.VERYLARGE.getName();
			}else if(CarTypeEnum.SUPER.getParam().equals(type)){
				return CarTypeEnum.SUPER.getName();
			}
		}
		return "";
	}

}
