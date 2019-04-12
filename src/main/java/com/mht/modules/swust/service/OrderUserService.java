package com.mht.modules.swust.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mht.common.service.CrudService;
import com.mht.modules.swust.dao.SysOrderlistDao;
import com.mht.modules.swust.entity.PageBean;
import com.mht.modules.swust.entity.SysOrderlist;
import com.mht.modules.swust.entity.SysUser;
import com.mht.modules.sys.dao.LogDao;
import com.mht.modules.sys.entity.Log;

/**
 * 
 * @ClassName: OrderUserService
 * @Description: 
 * @author com.mhout.wzw
 * @date 2017年7月26日 下午3:41:02 
 * @version 1.0.0
 */
public interface OrderUserService{
	/**
	 * 
	 * @Title: UserLogin 
	 * @Description: 用户登录
	 * @param user
	 * @return
	 * @author com.mhout.wzw
	 */
	SysUser UserLogin(SysUser user);
	/**
	 * 
	 * @Title: selectOrderListByUserId 
	 * @Description: 通过用户ID查询预约订单
	 * @param id
	 * @param pb
	 * @return
	 * @author com.mhout.wzw
	 */
	List<SysOrderlist> selectOrderListByUserId(String id, PageBean pb);
/**
 * 
 * @Title: insertOrder 
 * @Description: 添加订单
 * @param order
 * @author com.mhout.wzw
 */
	void insertOrder(SysOrderlist order);
/**
 * 
 * @Title: UserSelectOrderReason 
 * @Description: Ajax用户查询预定事由
 * @param id
 * @return
 * @author com.mhout.wzw
 */
	SysOrderlist UserSelectOrderReason(String id);
/**
 * 
 * @Title: userSelectByStateAndUserId 
 * @Description: 查询审核条件下的预定
 * @param id
 * @param state
 * @param pb
 * @return
 * @author com.mhout.wzw
 */
	List<SysOrderlist> userSelectByStateAndUserId(String id, String state,PageBean pb);
/**
 * 
 * @Title: UserSelectByAcount 
 * @Description: 查询预定总数
 * @param name
 * @return
 * @author com.mhout.xyb
 */
	SysUser UserSelectByAcount(String name);
/**
 * 
 * @Title: UserSelectPassword 
 * @Description: AJax判定密码是否正确
 * @param user
 * @return
 * @author com.mhout.xyb
 */
	SysUser UserSelectPassword(SysUser user);
/**
 * 
 * @Title: updateUserByPassword 
 * @Description: 更新密码
 * @param user
 * @author com.mhout.xyb
 */
	void updateUserByPassword(SysUser user);


}
