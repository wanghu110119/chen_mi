package com.mht.modules.swust.service.impl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.websocket.Session;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.mht.common.config.Global;
import com.mht.common.enumutils.SwustReplyEnum;
import com.mht.common.enumutils.SysOrderEnum;
import com.mht.common.service.CrudService;
import com.mht.common.sms.AliSmsUtils;
import com.mht.common.utils.DateUtils;
import com.mht.common.utils.MD5Utils;
import com.mht.common.utils.StringUtils;
import com.mht.modules.account.service.CommonService;
import com.mht.modules.swust.dao.BackstageTimeDao;
import com.mht.modules.swust.dao.SysCarMoneyDao;
import com.mht.modules.swust.dao.SysMessageDao;
import com.mht.modules.swust.dao.SysOfficeDao;
import com.mht.modules.swust.dao.SysOrderlistDao;
import com.mht.modules.swust.dao.SysPhotolistDao;
import com.mht.modules.swust.dao.SysUserDao;
import com.mht.modules.swust.entity.MessageDto;
import com.mht.modules.swust.entity.SMSMessage;
import com.mht.modules.swust.entity.SysBackstageTime;
import com.mht.modules.swust.entity.SysCarMoney;
import com.mht.modules.swust.entity.SysMessage;
import com.mht.modules.swust.entity.SysOrderlist;
import com.mht.modules.swust.entity.SysPhotolist;
import com.mht.modules.swust.utils.ConstantUtil;
import com.mht.modules.swust.websocket.SwustSocketContant;
import com.mht.modules.sys.dao.OfficeDao;
import com.mht.modules.sys.dao.UserDao;
import com.mht.modules.sys.entity.Office;
import com.mht.modules.sys.entity.User;
import com.mht.modules.sys.utils.UserUtils;

/**
 * 
 * @ClassName: OrderUserServiceImpl
 * @Description:
 * @author com.mhout.wzw
 * @date 2017年7月26日 下午2:50:44
 * @version 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class OrderUserServiceImpl extends CrudService<SysOrderlistDao, SysOrderlist> {
	@Autowired
	private SysUserDao userDao;
	@Autowired
	private SysOrderlistDao orderDao;
	@Autowired
	private SysOfficeDao officeDao;
	@Autowired
	private SysMessageDao messageDao;
	@Autowired
	private BackstageTimeDao timeDao;
	@Autowired
	private SysCarMoneyDao moneyDao;
	@Autowired
	private SysPhotolistDao photoDao;

	public User UserLogin(User user) {
		user.setPassword(MD5Utils.make(user.getPassword()));
		UsernamePasswordToken token = new UsernamePasswordToken(user.getAccount(), user.getPassword());
		Subject subject = SecurityUtils.getSubject();
		subject.login(token);
		token.setRememberMe(true);
		return user;
	}


	public void insertOrder(SysOrderlist order) {
		orderDao.insert(order);
	}

	public SysOrderlist UserSelectOrderReason(String id) {
		return orderDao.selectByPrimaryKey(Integer.parseInt(id));
	}


	public User UserSelectByAcount(String Acount) {
		return userDao.SelectByAcount(Acount);
	}

	public User UserSelectPassword(User user) {
		User selectuser = userDao.selectByPrimaryKey(user.getId());
		if (selectuser.getPassword().equals(user.getPassword())) {
			return selectuser;
		} else {
			return null;
		}
	}

	public void updateUserByPassword(User user) {
		userDao.updateByPrimaryKey(user);

	}

	@Transactional(readOnly = false)
	public void updatePasswordById(String id, String loginName, String newPassword) {
		User user = new User(id);
		user.setPassword(CommonService.encryption(newPassword));
		userDao.updatePasswordById(user);
		// 清除用户缓存
		user.setLoginName(loginName);
		UserUtils.clearCache(user);
	}
	
	@Transactional(readOnly = false)
	public void updateAdminMobile(String mobile) {
		userDao.updateMobile(mobile);
	}
	

	public List<Office> findOfficeList() {
		return officeDao.selectAll();
	}

	public List<SysOrderlist> selectByCarId(SysOrderlist sysOrderlist) {
		return orderDao.selectByCarId(sysOrderlist);
	}

	public List<SysCarMoney> selectAllByCar() {
		return moneyDao.selectAll();
	}

	/**
	 * @Title: dispose
	 * @Description: 消息处理
	 * @author com.mhout.wzw
	 */
	public void dispose(SysOrderlist order) {
		// 历史消息记录

		// 计费
		Double money = ConstantUtil.getmoney(order.getCarType(), order.getEndTime().getTime());
		MessageDto message = new MessageDto();
		message.setNumber(order.getCarNumber());
		message.setBeginDate(DateUtils.formatDateTime(order.getBeginTime()));
		message.setEndDate(DateUtils.formatDateTime(order.getEndTime()));
		message.setDate(DateUtils.formatDateTime(new Date()));
		message.setMoney(money + "");
		Gson gson = new Gson();
		String result = gson.toJson(message);
		// 消息推送
		Map<Session, String> map = SwustSocketContant.swustMap;
		User user = UserUtils.getUser();
		if (map.size() > 0 && user != null) {
			for (Map.Entry<Session, String> entry : SwustSocketContant.swustMap.entrySet()) {
				if (user.getId().equals(entry.getValue())) {
					Session webSocket = entry.getKey();
					try {
						webSocket.getBasicRemote().sendText(result);
					} catch (IOException e) {
						e.printStackTrace();
						continue;
					}
				}
			}
		}
	}

	public SysOrderlist permissionCarIn(SysOrderlist sysOrderlist, Date date) {
		long begintime = (sysOrderlist.getBeginTime().getTime());
		long endtime = (sysOrderlist.getEndTime().getTime());
		long nowtime = new Date().getTime();
		if ((double) nowtime >= (double) begintime && (double) nowtime <= (double) endtime) {
			return sysOrderlist;
		}

		return null;
	}

	/**
	 * @Title: saveOrder
	 * @Description: 保存预约
	 * @param sysOrderlist
	 * @author com.mhout.xyb
	 */
	@Transactional(readOnly = false)
	public void saveOrder(SysOrderlist sysOrderlist) {
		String orderId=null;
		if(sysOrderlist.getOrderId()!=null){
			 orderId = sysOrderlist.getOrderId().replaceAll(" ", "");
		}
		if (sysOrderlist.getIsNewRecord()||"".equals(orderId)) {
			// 查找最大值
			 orderId = orderDao.findMaxOrder();
			if (StringUtils.isNotBlank(orderId)) {
				String year = DateUtils.getYearBack();
				if (orderId.indexOf(year) > -1) {
					// 序号加1
					String end = String.valueOf(Integer.parseInt(orderId) + 1);
					sysOrderlist.setOrderId(end);
				} else {
					sysOrderlist.setOrderId(getStartId());
				}
			} else {
				sysOrderlist.setOrderId(getStartId());
			}
			Double atime = DateUtils.getTwoDateForHour(sysOrderlist.getBeginTime(), sysOrderlist.getEndTime());
			sysOrderlist.setAccreditTime(atime.toString());
			sysOrderlist.setState(SysOrderEnum.UNAUDITED.getParam());
			sysOrderlist.preInsert();
			dao.insert(sysOrderlist);
			if("2".equals(sysOrderlist.getCarType())){
				Global.threadPool.execute(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						sysOrderlist.setOrderPhone(orderDao.selectSmsPhone());
						sendSmsToAdmin(sysOrderlist);
					}
				});
			}
		} else {
			sysOrderlist.preUpdate();
			dao.update(sysOrderlist);
		}
		
	}

	protected boolean sendSmsToAdmin(SysOrderlist sysOrderlist) {
		boolean istrue = false;
		// 短信计量
		List<SysMessage> list = messageDao.selectAllMessage();
		if (CollectionUtils.isNotEmpty(list)) {
			SysMessage message = list.get(0);
			if (Integer.parseInt(message.getSurplus()) < 1) {
				return istrue;
			}
			message.setUsed(new StringBuilder().append((Integer.parseInt(message.getUsed()) + 1)).toString());
			message.setSurplus(new StringBuilder().append((Integer.parseInt(message.getSurplus()) - 1)).toString());
			messageDao.update(message);
		}
			SMSMessage sms = new SMSMessage();
			sms.setNumber(sysOrderlist.getCarNumber());
			String begin = DateUtils.formatDate(sysOrderlist.getBeginTime(), "MM月dd日HH点mm分");
			String end = DateUtils.formatDate(sysOrderlist.getEndTime(), "MM月dd日HH点mm分");
			sms.setBegin(begin);
			sms.setEnd(end);
			String msg = new Gson().toJson(sms);
			return AliSmsUtils.sendSmsToAdmin (sysOrderlist.getOrderPhone(), msg, "2");
	}


	/**
	 * @Title: getStartId
	 * @Description: 获取开始序列
	 * @return
	 * @author com.mhout.xyb
	 */
	private String getStartId() {
		String startId = DateUtils.getYearBack() + "001";
		return startId;
	}

	/**
	 * @Title: batch
	 * @Description: 批量操作
	 * @param ids
	 * @return
	 * @author com.mhout.xyb
	 */
	@Transactional(readOnly = false)
	public boolean batch(String[] ids, String type) {
		if (ids.length > 0 && StringUtils.isNotBlank(type)) {
			Global.threadPool.execute(new Runnable() {
				@Override
				public void run() {
					for (String id : ids) {
						SysOrderlist sysOrderlist = orderDao.get(id);
						if (sysOrderlist != null) {
							// 通过
							if ("1".equals(type)) {
								sysOrderlist.setPass("1");
								sysOrderlist.setState(SysOrderEnum.AUDITED.getParam());
								orderDao.update(sysOrderlist);
								sendSmsMessage(sysOrderlist, Global.TRUE);
							}
							// 驳回
							else if ("2".equals(type)) {
								sysOrderlist.setPass("2");
								sysOrderlist.setState(SysOrderEnum.AUDITED.getParam());
								orderDao.update(sysOrderlist);
								sendSmsMessage(sysOrderlist, Global.FALSE);
							}
						}
					}
				}
			});
		}
		return true;
	}

	/**
	 * 初始化时间以及查询时间
	 * 
	 * @return
	 */
	public SysBackstageTime selectBackstageTime() {
		SysBackstageTime defaultTime = timeDao.selectBackstageTime();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
		Date nowTime = calendar.getTime();
		Date tomorrow = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = sdf.format(tomorrow);
		double dateString = (double) tomorrow.getTime()
				+ (Integer.parseInt(defaultTime.getRemarks()) - 1) * 24 * 60 * 60 * 1000;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String d = format.format(dateString);
		Date date = new Date();
		try {
			date = format.parse(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String endStr = sdf.format(date);
		String endTime = endStr.substring(0, 11) + "18:00:00";
		String beginTime = str.substring(0, 11) + "09:00:00";
		if (defaultTime.getBeginTime() != null || !"".equals(defaultTime.getBeginTime())
				|| defaultTime.getEndTime() != null || !"".equals(defaultTime.getEndTime())) {
			endTime = endStr.substring(0, 11) + sdf.format(defaultTime.getEndTime()).substring(11);
			beginTime = str.substring(0, 11) + sdf.format(defaultTime.getBeginTime()).substring(11);
		}
		Date beginDate = new Date();
		Date endDate = new Date();
		try {
			beginDate = format.parse(beginTime);
			endDate = format.parse(endTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		defaultTime.setBeginTime(beginDate);
		defaultTime.setEndTime(endDate);
		return defaultTime;
	}

	public List<SysMessage> selectAllMessage() {
		return messageDao.selectAllMessage();
	}

	@Transactional(readOnly = false)
	public void changeMessage(String message) {
		SysMessage sysMessage = messageDao.selectAllMessage().get(0);
		sysMessage.setArtical(message);
		messageDao.update(sysMessage);
	}

	public SysPhotolist initializationLogin() {
		SysPhotolist photo = new SysPhotolist();
		photo.setState("1");
		return photoDao.selectByCount(photo);
	}

	public List<SysPhotolist> initHeadPhoto(String name, String id) {
		return photoDao.selectAll(name, id);
	}

	/**
	 * @Title: sendSmsMessage
	 * @Description: 发送短信公共方法
	 * @param sysOrderlist
	 * @param type
	 *            1.通过 2.驳回
	 * @return
	 * @author com.mhout.wzw
	 */
	@Transactional(readOnly = false)
	public boolean sendSmsMessage(SysOrderlist sysOrderlist, String type) {
		boolean istrue = false;
		// 短信计量
		List<SysMessage> list = messageDao.selectAllMessage();
		if (CollectionUtils.isNotEmpty(list)) {
			SysMessage message = list.get(0);
			if (Integer.parseInt(message.getSurplus()) < 1) {
				return istrue;
			}
			message.setUsed(new StringBuilder().append((Integer.parseInt(message.getUsed()) + 1)).toString());
			message.setSurplus(new StringBuilder().append((Integer.parseInt(message.getSurplus()) - 1)).toString());
			messageDao.update(message);
		}
		// 通过
		if (Global.TRUE.equals(type)) {
			SMSMessage sms = new SMSMessage();
			sms.setNumber(sysOrderlist.getCarNumber());
			String begin = DateUtils.formatDate(sysOrderlist.getBeginTime(), "MM月dd日HH点mm分");
			String end = DateUtils.formatDate(sysOrderlist.getEndTime(), "MM月dd日HH点mm分");
			sms.setBegin(begin);
			sms.setEnd(end);
			String msg = new Gson().toJson(sms);
			return AliSmsUtils.sendSmsAliyun(sysOrderlist.getOrderPhone(), msg, "2");
		} else if (Global.FALSE.equals(type)) {
			return AliSmsUtils.sendSmsAliyun(sysOrderlist.getOrderPhone(), sysOrderlist.getCarNumber(), "1");
		}

		return istrue;
	}

	@Transactional(readOnly = false)
	public void updateOrderList(SysOrderlist sysOrderlist) {
		orderDao.update(sysOrderlist);
	}

	public SysBackstageTime selectByRemark() {
		return timeDao.selectByRemark("1");
	}

	public SysBackstageTime selectByDisable() {
		// TODO Auto-generated method stub
		return timeDao.selectByDisable();
	}

	public SysOrderlist selectByNotIn(SysOrderlist sysOrderlist) {
		sysOrderlist.setPass(SwustReplyEnum.PREMIT.getParam());
		List<SysOrderlist> orderList = selectByCarId(sysOrderlist);
		if (orderList.size() == 0) {
			return null;
		} else {
			return orderList.get(0);
		}
	}
	@Transactional(readOnly=false)
	public String freeOrPay(SysOrderlist sysOrderlist) {
		if (StringUtils.isBlank(sysOrderlist.getPayFor()) || "0".equals(sysOrderlist.getPayFor())) {
			// 付费目标
			double money = ConstantUtil.getmoney(sysOrderlist.getCarType(), sysOrderlist.getEndTime().getTime());
			StringBuilder sb = new StringBuilder();
			sb.append(money);
			sysOrderlist.setPayMoney(sb.toString());
			if (new Date().getTime() >= sysOrderlist.getEndTime().getTime()) {
				sysOrderlist.setState(SwustReplyEnum.FINISH.getParam());
			} else {
				sysOrderlist.setPass(SwustReplyEnum.PREMIT.getParam());
			}
			super.save(sysOrderlist);
			dispose(sysOrderlist);
			return sb.toString();
		} else if (StringUtils.isNotBlank(sysOrderlist.getPayFor()) && "1".equals(sysOrderlist.getPayFor())) {
			// 免费目标
			sysOrderlist.setPayMoney(SwustReplyEnum.FREE.getName());
			if (new Date().getTime() >= sysOrderlist.getEndTime().getTime()) {
				sysOrderlist.setState(SwustReplyEnum.FINISH.getParam());
			} else {
				sysOrderlist.setPass(SwustReplyEnum.PREMIT.getParam());
			}
			super.save(sysOrderlist);
			dispose(sysOrderlist);
			return SwustReplyEnum.FREE.getParam();
		}
		return SwustReplyEnum.NONE.getParam();
	}
	@Transactional(readOnly = false)
	public void batchDelete(String[] ids) {
		// TODO Auto-generated method stub
		orderDao.batchDelete(ids);
	}
/**
 * 查询当前登录用户是否有权登录预约平台
 * @param user
 * @return
 */
	public boolean getInSystem(User user) {
		// TODO Auto-generated method stub
		User OegUser = userDao.getByLoginName(user);
		if(OegUser!=null&&OegUser.getOffice()!=null){
			return true;
		}
		return false;
	}


public List<SysOrderlist> selectRemoveOrder() {
	// TODO Auto-generated method stub
	return orderDao.selectRemoveOrder(new Date());
}

@Transactional(readOnly = false)
public void deleteRemoveOrder(List<SysOrderlist> list) {
	// TODO Auto-generated method stub
	orderDao.deleteRemoveOrder(list);
}


public String selectSmsPhone() {
	// TODO Auto-generated method stub
	return orderDao.selectSmsPhone();
}
}










