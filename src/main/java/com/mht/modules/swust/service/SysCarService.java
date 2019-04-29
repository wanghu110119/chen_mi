package com.mht.modules.swust.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mht.common.service.CrudService;
import com.mht.common.utils.StringUtils;
import com.mht.modules.swust.dao.SysCarDao;
import com.mht.modules.swust.entity.SysCar;
import com.mht.modules.sys.entity.Office;
import com.mht.modules.sys.entity.User;

@Service
@Transactional(readOnly = true)
public class SysCarService extends CrudService<SysCarDao, SysCar> {

	@Autowired
	private SysCarDao carDao;
	

	public String checkName( String carId) {
		String exist = "true";
		//校验单位名称
		//登录名
		if (StringUtils.isNotBlank(carId)) {
				
				SysCar sysCar = new SysCar();
				sysCar.setCarId(carId);
				sysCar = carDao.get(sysCar);
				exist = sysCar != null ? "false":"true";
		}
		//false:目标存在不可用 true:目标不存在可用
		return exist;
	}

	public String checkCardID( String carId) {
		String exist = "true";
		//校验单位名称
		//登录名
		if (StringUtils.isNotBlank(carId)) {
				
				SysCar sysCar = new SysCar();
				sysCar.setCarId(carId);
				List<SysCar> sysCarList = carDao.findList(sysCar);
				exist = sysCarList.size() > 0 ? "false":"true";
		}
		//false:目标存在不可用 true:目标不存在可用
		return exist;
	}
	
	
	
	public String insertCarId() {
		int carId=1;
		String code = "00";
		SysCar index = carDao.selectMaxRemark();
		if(index==null){
			 carId = 1;
			 return "0001";
		}else{
		carId =Integer.parseInt(index.getRemarks());
		}
		return String.valueOf(carId+1);
	}

	public String findCarMaxRemarks() {
		return carDao.findCarMaxRemarks();
	}
@Transactional(readOnly=false)
	public void batchDelete(String[] ids) {
		carDao.batchDelete(ids);
	}

	
	
	
}
