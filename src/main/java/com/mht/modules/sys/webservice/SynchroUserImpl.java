package com.mht.modules.sys.webservice;

import javax.jws.WebService;

import com.mht.common.json.AjaxJson;

/**
 * @ClassName: SynchroUserImpl
 * @Description: 同步接口实现
 * @author com.mhout.xyb
 * @date 2017年6月5日 上午10:36:16 
 * @version 1.0.0
 */
@WebService(endpointInterface = "com.mht.modules.sys.webservice.SynchroUser",serviceName="synchroUser")
public class SynchroUserImpl implements SynchroUser{

	@Override
	public AjaxJson synch(String synchroUser) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public AjaxJson synch(SynchroUser synchroUser) {
//		System.out.println("用户数据同步-----------");
//		return null;
//	}

}
