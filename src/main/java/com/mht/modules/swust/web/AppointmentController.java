package com.mht.modules.swust.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.mht.common.beanvalidator.BeanValidators;
import com.mht.common.config.Global;
import com.mht.common.enumutils.SysOrderEnum;
import com.mht.common.json.AjaxJson;
import com.mht.common.persistence.Page;
import com.mht.common.sms.AliSmsUtils;
import com.mht.common.utils.DateUtils;
import com.mht.common.utils.StringUtils;
import com.mht.common.utils.excel.ExportExcel;
import com.mht.common.utils.excel.ImportExcel;
import com.mht.common.web.BaseController;
import com.mht.modules.swust.dao.BackstageTimeDao;
import com.mht.modules.swust.dao.SysOfficeDao;
import com.mht.modules.swust.entity.SMSMessage;
import com.mht.modules.swust.entity.SysBackstageTime;
import com.mht.modules.swust.entity.SysCar;
import com.mht.modules.swust.entity.SysOrderlist;
import com.mht.modules.swust.entity.SysPhotolist;
import com.mht.modules.swust.service.ManagerService;
import com.mht.modules.swust.service.impl.OrderUserServiceImpl;
import com.mht.modules.sys.dao.OfficeDao;
import com.mht.modules.sys.entity.Office;
import com.mht.modules.sys.entity.User;
import com.mht.modules.sys.utils.UserUtils;

/**
 * @ClassName: AppointmentController
 * @Description:
 * @author com.mhout.dhn
 * @date 2017年7月26日 下午3:06:05
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "a/swust/appointment")
public class AppointmentController extends BaseController {

	@Autowired
	OrderUserServiceImpl service;

	@Autowired
	private SysOfficeDao officeDao;
	@Autowired
	private BackstageTimeDao timeDao;

	/**
	 * @Title: get
	 * @Description:测试通过
	 * @param id
	 * @return
	 * @author com.mhout.dhn
	 */
	@ModelAttribute("sysOrderlist")
	public SysOrderlist get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return service.get(id);
		} else {
			return new SysOrderlist();
		}
	}

	/**
	 * @Title: index
	 * @Description: 跳转主页面
	 * @return
	 * @author com.mhout.xyb
	 */
	@RequestMapping(value = { "" }, method = RequestMethod.POST)
	public String index(Model model) {
		return "modules/swust/systemOrder";
	}

	@ResponseBody
	@RequestMapping(value = { "initphoto" }, method = RequestMethod.POST)
	public AjaxJson initPhoto() {
		AjaxJson json = new AjaxJson();
		User user = UserUtils.getUser();
		List<SysPhotolist> headPhoto = service.initHeadPhoto("1", user.getId());
		if (headPhoto.size() == 0) {
			headPhoto.add(new SysPhotolist());
		}
		json.setMsg(headPhoto.get(0).getPhotoPath());
		return json;
	}

	/**
	 * @Title: list
	 * @Description: 列表页面
	 * @param appointment
	 * @return
	 * @author com.mhout.dhn
	 */

	@RequestMapping(value = { "list" }, method = RequestMethod.POST)
	public String list(SysOrderlist sysOrderlist, HttpServletRequest request, HttpServletResponse response,
			String color, Model model) {
		sysOrderlist.setColor(color);
		Page<SysOrderlist> page = service.findPage(new Page<SysOrderlist>(request, response), sysOrderlist);
		model.addAttribute("sysOrderlist", sysOrderlist);
		model.addAttribute("user", UserUtils.getUser());

		if ("".equals(color) || color == null) {
			Map<String, List<SysOrderlist>> sortMap = new HashMap<>();
			for (SysOrderlist list : page.getList()) {
				String carNum = list.getCarNumber();
				sortMap.put(carNum, new ArrayList<SysOrderlist>());
			}
			for (SysOrderlist add : page.getList()) {
				sortMap.get(add.getCarNumber()).add(add);
			} // map生成完成

			Iterator iter = sortMap.keySet().iterator();
			while (iter.hasNext()) {
				String key = (String) iter.next();
				List<SysOrderlist> val = sortMap.get(key);
				for (int i = 0; i < val.size(); i++) {
					for (int j = i + 1; j < val.size(); j++) {
						if (Math.abs(val.get(i).getBeginTime().getTime() - val.get(j).getBeginTime().getTime()) < 36
								* 60 * 60 * 1000) {
							val.get(i).setColor("red");
							val.get(j).setColor("red");
							service.updateOrderList(val.get(i));
							service.updateOrderList(val.get(j));
						}
					}
				}
			}
		}

		model.addAttribute("page", page);
		return "modules/swust/systemOrderList";
	}

	/**
	 * @Title:
	 * @Description:查询预约事由
	 * @param Remark
	 * @return
	 * @author com.mhout.dhn
	 */
	@ResponseBody
	@RequestMapping(value = "findRemark")
	public AjaxJson getRemarks(String id, Model model) {
		AjaxJson ajax = new AjaxJson();
		SysOrderlist sysOrderlist = service.get(id);
		if (sysOrderlist.getRemarks() == null || sysOrderlist.getRemarks().equals("")) {
			sysOrderlist.setRemarks("未设置备注");
		}
		StringBuilder sb = new StringBuilder();
		sb.append(sysOrderlist.getOrderReason());
		sb.append("," + sysOrderlist.getRemarks());
		ajax.setMsg(sb.toString());
		return ajax;
	}

	/**
	 * @Title: batchPass
	 * @Description: 批量通过
	 * @return
	 * @author com.mhout.xyb
	 */
	@ResponseBody
	@RequestMapping(value = { "batchPass" }, method = RequestMethod.POST)
	public AjaxJson batchPass(@RequestParam(value = "ids") String[] ids) {
		AjaxJson ajaxJson = new AjaxJson();
		service.batch(ids, "1");
		ajaxJson.setSuccess(true);
		ajaxJson.setMsg("操作成功");
		return ajaxJson;
	}

	/**
	 * 
	 * @param ids
	 * @return批量删除
	 */
	@ResponseBody
	@RequestMapping(value = { "batchDelete" }, method = RequestMethod.POST)
	public AjaxJson batchDelete(@RequestParam(value = "ids") String[] ids) {
		AjaxJson ajaxJson = new AjaxJson();
		service.batchDelete(ids);
		ajaxJson.setSuccess(true);
		ajaxJson.setMsg("操作成功");
		return ajaxJson;
	}

	/**
	 * @Title: batchReject
	 * @Description: 批量驳回
	 * @return
	 * @author com.mhout.xyb
	 */
	@ResponseBody
	@RequestMapping(value = { "batchReject" }, method = RequestMethod.POST)
	public AjaxJson batchReject(@RequestParam(value = "ids") String[] ids) {
		AjaxJson ajaxJson = new AjaxJson();
		service.batch(ids, "2");
		ajaxJson.setSuccess(true);
		ajaxJson.setMsg("操作成功");
		return ajaxJson;
	}

	/**
	 * @Title: pass
	 * @Description: 通过
	 * @param id
	 * @return
	 * @author com.mhout.xyb
	 */
	@ResponseBody
	@RequestMapping(value = { "pass" }, method = RequestMethod.POST)
	public AjaxJson pass(SysOrderlist sysOrderlist) {
		AjaxJson ajaxJson = new AjaxJson();
		sysOrderlist.setPass("1");
		sysOrderlist.setState(SysOrderEnum.AUDITED.getParam());
		service.save(sysOrderlist);
		// 通过发送短信
		service.sendSmsMessage(sysOrderlist, Global.TRUE);
		ajaxJson.setSuccess(true);
		ajaxJson.setMsg("操作成功");
		return ajaxJson;
	}

	/**
	 * @Title: reject
	 * @Description: 驳回
	 * @param id
	 * @return
	 * @author com.mhout.xyb
	 */
	@ResponseBody
	@RequestMapping(value = { "reject" }, method = RequestMethod.POST)
	public AjaxJson reject(SysOrderlist sysOrderlist) {
		AjaxJson ajaxJson = new AjaxJson();
		sysOrderlist.setPass(SysOrderEnum.NOTPASS.getParam());
		sysOrderlist.setState(SysOrderEnum.AUDITED.getParam());
		service.save(sysOrderlist);
		// 驳回发送短信
		service.sendSmsMessage(sysOrderlist, Global.FALSE);
		ajaxJson.setSuccess(true);
		ajaxJson.setMsg("操作成功");
		return ajaxJson;
	}

	/**
	 * 
	 * @Title: importFile
	 * @Description: 把符合条件的预约信息以excel文件格式导出
	 * @param file
	 * @param redirectAttributes
	 * @return
	 * @author com.mhout.dhn
	 */
	@ResponseBody
	@RequestMapping(value = "export", method = RequestMethod.GET)
	public AjaxJson exportFile(SysOrderlist sysOrderlist, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		AjaxJson ajaxJson = new AjaxJson();
		boolean issuccess = true;
		String msg = "操作成功!";
		try {
			String fileName = "预约数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<SysOrderlist> page = service.findPage(new Page<SysOrderlist>(request, response, -1), sysOrderlist);
			new ExportExcel("预约数据", SysOrderlist.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			issuccess = false;
			msg = "导出数据失败!";
		}
		ajaxJson.setSuccess(issuccess);
		ajaxJson.setMsg(msg);
		return ajaxJson;
	}

	/**
	 * 
	 * @param sysOrderlist
	 *            导出模板
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "exportModel", method = RequestMethod.GET)
	public AjaxJson exportModel(SysOrderlist sysOrderlist , HttpServletResponse response ) {
		AjaxJson ajaxJson = new AjaxJson();
		boolean issuccess = true;
		String msg = "操作成功!";
		try {
			String fileName = "沉迷探案馆预约数据导入模板.xlsx";
			List<SysOrderlist> list = Lists.newArrayList();
			sysOrderlist.setCarNumber("大众，美团");
			sysOrderlist.setCarType("1");
			Office office = new Office();
			office.setName("XXX学院");
			sysOrderlist.setOffice(office);
			sysOrderlist.setOrderName("（必填）");
			sysOrderlist.setOrderId("XXXXX");
			sysOrderlist.setCompany("来访人单位");
			sysOrderlist.setBeginTime(new Date());
			sysOrderlist.setOrderPhone("18000000000");
			sysOrderlist.setAccreditTime("XX");
			sysOrderlist.setEndTime(new Date());
			list.add(sysOrderlist);
			new ExportExcel("预约数据", SysOrderlist.class,2).setDataList(list).write(response, fileName).dispose();
			return ajaxJson;
		} catch (Exception e) {
			e.printStackTrace();
			issuccess = false;
			msg = "导出模板失败!";
		}
		ajaxJson.setSuccess(issuccess);
		ajaxJson.setMsg(msg);
		return ajaxJson;
	}

	/**
	 * @Title: sendSms
	 * @Description: TODO
	 * @return
	 * @author com.mhout.xyb
	 */
	@ResponseBody
	@RequestMapping("sendSms")
	public String sendSms() {
		// AliSmsUtils.sendSmsAliyun("13880461969","川A88888", "1");
		SMSMessage sms = new SMSMessage();
		sms.setNumber("川B66666");
		sms.setBegin("8月01日12点");
		sms.setEnd("8月02日12点");
		String msg = new Gson().toJson(sms);
		AliSmsUtils.sendSmsAliyun("15882032402", msg, "2");
		AliSmsUtils.sendSmsAliyun("15882032402", "川A88888", "1");
		return "true";
	}

	public static void main(String[] args) {

	}

	/**
	 * @Title: importFile
	 * @Description: TODO excel导入
	 * @param file
	 * @param redirectAttributes
	 * @return
	 * @author com.mhout.wzw
	 */
	@ResponseBody
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public AjaxJson importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		AjaxJson ajax = new AjaxJson();
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			ajax.setMsg("演示模式，不允许操作！");
			return ajax;
		}
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			String val = (String)ei.getCellValue(ei.getRow(0), 0);
			if(!"预约数据".equals(val)){
				addMessage(redirectAttributes, "导入预约信息失败！导入信息有误");
				ajax.setMsg("抱歉：请下载正确的模板后再次导入数据！");
				return ajax;
			}
			List<SysOrderlist> list = ei.getDataList(SysOrderlist.class);
			for (SysOrderlist order : list) {
				try {
					if(order.getCarType().equals("0")&&order.getOrderPhone().equals("")&&order.getOrderName().equals("")&&
							order.getCarNumber().equals("")&&order.getCompany().equals("")){
						continue;
					}else if(checkOrderName(order.getOrderName())){
						failureMsg.append(returnOrderName(order.getOrderName()));
						failureNum++;
						continue;
					}
					else if (order.getOffice()!=null&&order.getOffice().getName() != null&&!"".equals(order.getOffice().getName())
							&& "true".equals(checkOffice(order.getOffice().getName()))) {
						if (order.getCarNumber()!=null&&order.getCarType()!=null&&checkInput(order.getCarNumber(), order.getCarType())) {
							if (checkCarType(order.getCarType())) {
								String phone = order.getOrderPhone().replaceAll(" ", "");
								order.setOrderPhone(phone);
								if (phone != null && !"".equals(phone) && phone.length()==11) {
									SysBackstageTime time = timeDao.selectByDisable();
									order.synchronizationTime();
									if (order.getBeginTime()!=null&&order.getEndTime()!=null&&checkDate(order, time)) {
										order.setUserId(UserUtils.getUser().getId());
										order.setColor("0");
										Calendar cal = Calendar.getInstance();
										cal.setTime(order.getEndTime());
										cal.add(Calendar.HOUR, 24);// 24小时制
										cal.add(Calendar.SECOND, -1);
										order.setEndTime(cal.getTime());
										service.saveOrder(order);
										successNum++;
									} else if ("1".equals(time.getId())) {
										SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
										failureMsg.append("<br/>导入预约: " + order.getOrderName() + " 的预约时长请在一天的"
												+ formatter.format(time.getBeginTime()) + "~"
												+ formatter.format(time.getEndTime()) + "之内; ");
										failureNum++;
									} else if ("2".equals(time.getId())) {
										failureMsg.append("<br/>导入预约: " + order.getOrderName() + " 的预约时长请在当前时间之后" + time.getSum()
												+ "天之内; ");
										failureNum++;
									}
								} else {
									failureMsg.append("<br/>导入预约: " + order.getOrderName() + " 的电话号码输入有误; ");
									failureNum++;
								}
							} else {
								failureMsg.append("<br/>导入预约: " + order.getOrderName() + " 的车辆类型不存在; ");
								failureNum++;
							}
						} else {
							failureMsg.append("<br/>导入预约:" + order.getOrderName() + "的车牌信息输入不合规则; ");
							failureNum++;
						}
					} else {
						failureMsg.append("<br/>导入预约: " + order.getOrderName() + " 校内对接单位不存在; ");
						failureNum++;
					}
				} catch (ConstraintViolationException ex) {
					failureMsg.append("<br/>预约 " + order.getOrderName() + "，请验证校内对接单位是否存在或被禁用 ");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList) {
						failureMsg.append(message + "; ");
						failureNum++;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					failureMsg.append("<br/>预约" + order.getOrderName() + " 请验证单位是否存在或是否有权限登录该系统或被禁用");
					failureNum++;
				}
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "，失败 " + failureNum + " 条预约信息，导入信息如下 ");
				failureNum++;
			}
			System.out.println(failureMsg);
			addMessage(redirectAttributes,
					"已成功导入 " + successNum + " 条预约信息；失败：" + failureNum + "条，原因：" + failureMsg.toString());
			ajax.setMsg("已成功导入 " + successNum + " 条预约信息" + failureMsg.toString());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			addMessage(redirectAttributes, "导入预约信息失败！导入信息有误");
			ajax.setMsg("导入预约信息失败！导入信息有误");
		}
		return ajax;
	}

	private boolean checkOrderName(String orderName) {
		// TODO Auto-generated method stub
		String name = orderName.replaceAll(" ", "");
		String synx = "^[a-zA-Z\u4E00-\u9FA5]+$";
		Pattern pattern = Pattern.compile(synx);
		Matcher match=pattern.matcher(name);
		boolean b=match.matches();
		if("".equals(name)){
			return true;
		}else if(!b||name.length()<2||name.length()>10){
			return true;
		}
		
		return false;
	}
	
	private String returnOrderName(String orderName) {
		// TODO Auto-generated method stub
		String name = orderName.replaceAll(" ", "");
		String synx = "^[a-zA-Z\u4E00-\u9FA5]+$";
		Pattern pattern = Pattern.compile(synx);
		Matcher match=pattern.matcher(name);
		boolean b=match.matches();
		if("".equals(name)){
			return "<br/>导入预约:"+name+" 的来访人不能为空; ";
		}else if(!b||name.length()<2||name.length()>10){
			return "<br/>导入预约:"+name+" 的来访人姓名请为2~10位的字母或者汉字; ";
		}
		
		return "<br/>导入预约:"+name+" 的来访人姓名请为2~10位的字母或者汉字; ";
	}
	
	
	
	

	private boolean checkDate(SysOrderlist order, SysBackstageTime time) {
		if(order.getEndTime().getTime()<order.getBeginTime().getTime()){
			return false;
		}
		long now = new Date().getTime();
		long begin = time.getBeginTime().getTime()<now?now:time.getBeginTime().getTime();
		long end = time.getEndTime().getTime()<begin?begin:time.getEndTime().getTime();
		long timeBegin = order.getBeginTime().getTime();
		long timeEnd = order.getEndTime().getTime();
		if ("1".equals(time.getId())) {
			SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat formatterHour = new SimpleDateFormat("HH:mm:ss");
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String stringBegin = formatterDate.format(order.getBeginTime())+" "
			+formatterHour.format(time.getBeginTime().getTime()<now?new Date():time.getBeginTime());
			String stringEnd = formatterDate.format(order.getBeginTime())+" "
			+formatterHour.format( time.getEndTime().getTime()<begin?(time.getBeginTime().getTime()<now?new Date():time.getBeginTime()):time.getEndTime());
			try {
				end = formatter.parse(stringEnd).getTime();
				begin =  formatter.parse(stringBegin).getTime();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if((begin < timeBegin) && (end > timeEnd)){
				return true;
			}
			return false;
		} else if ("2".equals(time.getId()) && time.getSum() != null) {
			end = begin + Integer.parseInt(time.getSum()) * 24 * 3600 * 1000;
			if (begin < timeBegin && end > timeEnd) {
				return true;
			}
		}
		return false;
	}

	private boolean checkCarType(String carType) {
		boolean b = false;
		if ("1".equals(carType) || "2".equals(carType) || "3".equals(carType)) {
			b = true;
		}
		return b;
	}

	private boolean checkInput(String sysCar, String carType) {
		Pattern patternCarId = Pattern.compile("^[\u4e00-\u9fa5|WJ]{1}[A-Z0-9]{6}$");
		boolean check = false;
		boolean a = patternCarId.matcher(sysCar).matches();
		if (a) {
			check = true;
		}
		return check;
	}

	public String checkOffice(String name) {
		String exist = "true";
		// true表示存在：可以添加
		// 校验单位名称
		if (StringUtils.isNotBlank(name)) {
			Office office = officeDao.findByName(name);
			exist = office == null ? "false" : "true";
		}
		return exist;
	}
}
