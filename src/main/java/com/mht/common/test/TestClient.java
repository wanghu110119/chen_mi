//package com.mht.common.test;
//
//import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
//
//import com.mht.common.cxfservice.SynchroUser;
//import com.mht.common.cxfservice.entity.SynchUser;
//import com.mht.common.cxfservice.entity.UserVo;
//import com.mht.common.json.AjaxJson;
//
//public class TestClient {
//	
//	public static void main(String[] args) {
//        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
//        factory.setServiceClass(SynchroUser.class);
//        factory.setAddress("http://192.168.3.7:8080/mht/server/synchroUser?wsdl");
//        SynchroUser service = (SynchroUser) factory.create();
//        System.out.println("#############Client getUserByName##############");
//        UserVo userVo = new UserVo();
//        userVo.setAppid("22222");
//        userVo.setSecret("22222");
//        service.synch(userVo);
//    }
//
//}
