package com.mht.common.utils.excel.fieldtype;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.mht.common.enumutils.CarTypeEnum;
import com.mht.common.enumutils.MemberTypeEnum;

public class MemberType {
	
	/**
	 * 获取对象值（导入）
	 */
	public static Object getValue(String val) {
		List<String> list = new ArrayList<>();
		if (MemberTypeEnum.SMALL.getName().equals(val)) {
			list.add(MemberTypeEnum.SMALL.getParam());
		} else if (MemberTypeEnum.LARGE.getName().equals(val)) {
			list.add(MemberTypeEnum.LARGE.getParam());
		} else if (MemberTypeEnum.NONE.getName().equals(val)) {
			list.add(MemberTypeEnum.NONE.getParam());
		}else if (MemberTypeEnum.VERYLARGE.getName().equals(val)) {
			list.add(MemberTypeEnum.VERYLARGE.getParam());
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
			if (MemberTypeEnum.SMALL.getParam().equals(type)) {
				return MemberTypeEnum.SMALL.getName();
			} else if (MemberTypeEnum.LARGE.getParam().equals(type)) {
				return MemberTypeEnum.LARGE.getName();
			} else if (MemberTypeEnum.NONE.getParam().equals(type)) {
				return MemberTypeEnum.NONE.getName();
			} else if (MemberTypeEnum.VERYLARGE.getParam().equals(type)) {
				return MemberTypeEnum.VERYLARGE.getName();
			}else if(MemberTypeEnum.SUPER.getParam().equals(type)){
				return MemberTypeEnum.SUPER.getName();
			}
		}
		return "";
	}

}
