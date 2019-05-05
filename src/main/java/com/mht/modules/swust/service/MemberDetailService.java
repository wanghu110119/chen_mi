package com.mht.modules.swust.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mht.common.service.CrudService;
import com.mht.modules.swust.dao.MemberDetailMapper;
import com.mht.modules.swust.entity.MemberDetail;
import com.mht.modules.swust.entity.SysCar;
import com.mht.modules.sys.utils.UserUtils;
@Service
@Transactional(readOnly = true)
public class MemberDetailService extends CrudService<MemberDetailMapper, MemberDetail> {

	@Autowired
	MemberDetailMapper mapper;
	
	public MemberDetail sumCostAndCharge(SysCar car) {
		MemberDetail memberDetail = new MemberDetail();
		memberDetail.setCar(car);
		return mapper.sumCostOrCharge(memberDetail);
	}

	public MemberDetail sum() {
		// TODO Auto-generated method stub
		return mapper.sumCostAndCharge();
	}

}
