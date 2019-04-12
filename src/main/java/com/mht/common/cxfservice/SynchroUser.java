package com.mht.common.cxfservice;

import javax.jws.WebService;

import com.mht.common.cxfservice.entity.UserVo;
import com.mht.common.json.AjaxJson;

/**
 * @ClassName: SynchroUser
 * @Description: 用户数据同步接口
 * @author com.mhout.xyb
 * @date 2017年6月5日 上午10:12:33 
 * @version 1.0.0
 */
@WebService
public interface SynchroUser {
	
	/**
	 * @Title: synch 
	 * @Description: 用户同步
	 * @param synchroUser
	 * @return
	 * @author com.mhout.xyb
	 */
	public AjaxJson synch(UserVo synchUser);


}
