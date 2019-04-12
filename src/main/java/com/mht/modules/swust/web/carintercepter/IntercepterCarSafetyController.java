package com.mht.modules.swust.web.carintercepter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mht.common.enumutils.SwustReplyEnum;
import com.mht.common.json.AjaxJson;
import com.mht.common.sms.AliSmsUtils;
import com.mht.common.utils.StringUtils;
import com.mht.common.web.BaseController;
import com.mht.modules.swust.dao.SysCarMoneyDao;
import com.mht.modules.swust.dao.SysMessageDao;
import com.mht.modules.swust.entity.SysCar;
import com.mht.modules.swust.entity.SysCarMoney;
import com.mht.modules.swust.entity.SysMessage;
import com.mht.modules.swust.entity.SysOrderlist;
import com.mht.modules.swust.service.SysCarService;
import com.mht.modules.swust.service.impl.OrderUserServiceImpl;
import com.mht.modules.swust.utils.ConstantUtil;

@Controller
@RequestMapping(value = "${apiPath}/swust/order/intercepter")
public class IntercepterCarSafetyController extends BaseController{
	
	@Autowired
	OrderUserServiceImpl service;
	
	@Autowired
	private SysCarService carService;
	
	@Autowired
	private SysCarMoneyDao moneydao;
	
	@Autowired
	private SysMessageDao messageDao;
	/**
	 * 
	 * @Title: get 
	 * @Description: TODO 系统运行时的前置数据
	 * @param id
	 * @return
	 * @author com.mhout.xyb
	 */
	@ModelAttribute("sysOrderlist")
	public SysOrderlist get(@RequestParam(required = false) String id ){
		if (StringUtils.isNotBlank(id)){
			return service.get(id);
		} else {
			return new SysOrderlist();
		}
	}
	/**
	 * 
	 * @Title: CarIn 
	 * @Description: TODO 车辆进入时的入场许可
	 * @param carId
	 * @param model
	 * @param sysOrderlist
	 * @return
	 * @author com.mhout.wzw
	 */
	@ResponseBody
	@RequestMapping(value="carin")
	public AjaxJson CarIn(String carId,SysOrderlist sysOrderlist){
		AjaxJson json = new AjaxJson();
		sysOrderlist.setCarNumber(carId);
		sysOrderlist.setPass(SwustReplyEnum.PREMIT.getParam());
		List<SysOrderlist> orderList =  service.selectByCarId(sysOrderlist);
		if(orderList.size() == 0 || StringUtils.isBlank(orderList.get(0).getCarNumber())){
			json.setMsg(SwustReplyEnum.NONE.getParam());
		}else{
			for (int i = 0; i < orderList.size(); i++) {
				SysOrderlist order = orderList.get(i);
				order = service.permissionCarIn(order,new Date());
				if(order!=null){
					json.setMsg(SwustReplyEnum.PAY.getParam());
					order.setPass(SwustReplyEnum.IN.getParam());
					service.save(order);
					break;
				}else{
					json.setMsg(SwustReplyEnum.NONE.getParam());
				}
			}
		}
		return json;
	}
	/**
	 * 
	 * @Title: CarOut 
	 * @Description: TODO 车辆出场时是否可以允许和是否缴费
	 * @param carId
	 * @param model
	 * @param sysOrderlist
	 * @return   
	 * @author com.mhout.wzw
	 */
	@ResponseBody
	@RequestMapping(value="carout")
	public AjaxJson CarOut(String carId, 
		SysOrderlist sysOrderlist){
		AjaxJson json = new AjaxJson();
		sysOrderlist.setCarNumber(carId);
		sysOrderlist.setState(SwustReplyEnum.PREMIT.getParam());
		List<SysOrderlist>  selectList =  service.selectByCarId(sysOrderlist);
		List<SysOrderlist>  orderList = new ArrayList<>();
		SysOrderlist order = null;
		for (SysOrderlist var : selectList) {
			if(new Date().getTime()<var.getEndTime().getTime()){
				orderList.add(var);
			}
		}
			if (orderList.size()==0) {
				sysOrderlist = service.selectByNotIn(sysOrderlist);
				if(sysOrderlist==null){
					//无车辆信息
					json.setMsg(SwustReplyEnum.NONE.getParam());
				}else{
					json.setMsg(service.freeOrPay(sysOrderlist));
				}
				return json;
			} else if (StringUtils.isBlank(orderList.get(0).getPayFor()) || "0".equals(orderList.get(0).getPayFor())){
				//收费
				order = orderList.get(0);
				double money = ConstantUtil.getmoney(order.getCarType(), order.getEndTime().getTime());	
				StringBuilder sb = new StringBuilder();
				sb.append(money);
					json.setMsg(sb.toString());
					order.setPayMoney(sb.toString());
					if(new Date().getTime()>=order.getEndTime().getTime()){
						order.setState(SwustReplyEnum.FINISH.getParam());
					}else{
						order.setPass(SwustReplyEnum.PREMIT.getParam());
					}
			}
		service.save(order);
		service.dispose(order);
		return json;
	}
	
	
	/**
	 * 
	 * @Title: carSafety 
	 * @Description: TODO 车辆出场时安全检验
	 * @param carId
	 * @param model
	 * @param SysCar
	 * @return
	 * @author com.mhout.wzw
	 */
	@ResponseBody
	@RequestMapping(value="carsafety")
	public AjaxJson CarSafety(String carId, 
			Model model){
		AjaxJson json = new AjaxJson();
		SysCar car = new SysCar();
		car.setCarId(carId);
		car = carService.get(car);
		if(car!=null&&SwustReplyEnum.PREMIT.getParam().equals(car.getDisable())){
			json.setMsg(SwustReplyEnum.PREMIT.getParam());
			 boolean SMS =  AliSmsUtils.sendSmsInternalVehicle(car.getPhone(), car.getCarId());
			 if(SMS){
				 
				 List<SysMessage> list = messageDao.selectAllMessage();
					if (CollectionUtils.isNotEmpty(list)) {
						SysMessage message = list.get(0);
						if(Integer.parseInt(message.getSurplus())<1){
							System.out.println("=======WORNING: SMS RUN OUT--"+new Date()+"=======");
							json.setMsg("0");
							json.setSuccess(false);
							return json;
						}
						message.setUsed(new StringBuilder().append((Integer.parseInt(message.getUsed())+1)).toString());
						message.setSurplus(new StringBuilder().append((Integer.parseInt(message.getSurplus())-1)).toString());
						messageDao.update(message);
					}
				 System.out.println("=======SMS SEND SUCCESS:"+car.getPhone()+"--"+new Date()+"=======");
			 }
		}else{
			json.setMsg(SwustReplyEnum.PAY.getParam());
		}
		
		return json;
	}
	
	
	
}







