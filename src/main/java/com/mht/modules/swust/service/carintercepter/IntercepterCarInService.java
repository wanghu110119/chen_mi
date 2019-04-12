package com.mht.modules.swust.service.carintercepter;

import java.util.Date;

import com.mht.modules.swust.entity.SysOrderlist;


public interface IntercepterCarInService {

	SysOrderlist selectOrderListByCarId(String carId);

	void updateByAccreditTime(Date date);

}
