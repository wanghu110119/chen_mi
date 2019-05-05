package com.mht.modules.swust.dao;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.swust.entity.MemberDetail;
import java.util.List;
@MyBatisDao
public interface MemberDetailMapper extends CrudDao<MemberDetail> {
	
		public MemberDetail sumCostOrCharge(MemberDetail memberDetail);
		
		public MemberDetail sumCostAndCharge();
	
}