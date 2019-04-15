package com.mht.modules.swust.web;

import java.util.Date;
import java.util.List;
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
import com.mht.common.beanvalidator.BeanValidators;
import com.mht.common.config.Global;
import com.mht.common.json.AjaxJson;
import com.mht.common.persistence.Page;
import com.mht.common.utils.DateUtils;
import com.mht.common.utils.StringUtils;
import com.mht.common.utils.excel.ExportExcel;
import com.mht.common.utils.excel.ImportExcel;
import com.mht.common.web.BaseController;
import com.mht.modules.swust.entity.SysCar;
import com.mht.modules.swust.entity.SysOrderlist;
import com.mht.modules.swust.service.SysCarService;
import com.mht.modules.sys.entity.Office;

@Controller
@RequestMapping(value = "a/swust/car")
public class MemberCotroller extends BaseController {
	@Autowired
	private SysCarService carService;

	/**
	 * @Title: index
	 * @Description: 跳转主页面
	 * @return
	 * @author com.mhout.wzw
	 */
	@RequestMapping(value = { "" })
	public String index(Model model) {
		return "modules/swust/smsSend";
	}

	@RequestMapping(value = { "portal" })
	public String portal(Model model) {
		return "/modules/swust/memberPortal";
	}
	
	
	@ModelAttribute("sysCar")
	public SysCar get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			SysCar car = new SysCar();
			car.setId(id);
			return carService.get(car);
		} else {
			return new SysCar();
		}
	}

	
	@RequestMapping(value = { "portalList" })
	public String hallList(String name, HttpServletRequest request, SysCar sysCar, HttpServletResponse response,
			Model model) {
		if (name != null && !"".equals(name)) {
			sysCar.setCarId(name);
		}
		Page<SysCar> page = carService.findPage(new Page<SysCar>(request, response), sysCar);
		model.addAttribute("page", page);
		return "/modules/swust/memberList";
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = { "getCar" })
	public AjaxJson getCarMessage(String name, String state, HttpServletRequest request, SysCar sysCar,
			HttpServletResponse response, Model model) {
		AjaxJson json = new AjaxJson();
		json.put("sysCar", sysCar);
		return json;
	}

	@RequestMapping(value = { "list" })
	public String SelectCarList(String name, HttpServletRequest request, SysCar sysCar, HttpServletResponse response,
			Model model) {
		if (name != null && !"".equals(name)) {
			sysCar.setCarId(name);
		}
		Page<SysCar> page = carService.findPage(new Page<SysCar>(request, response), sysCar);
		model.addAttribute("page", page);
		return "modules/swust/smsSendList";
	}

	@RequestMapping(value = { "add" })
	public void InsertCarMessage(String name, String state, HttpServletRequest request, SysCar sysCar,
			HttpServletResponse response, Model model) {
		carService.save(sysCar);
	}

	@RequestMapping(value = { "addList" })
	public void addList(String name, String state, HttpServletRequest request, SysCar sysCar,
			HttpServletResponse response, Model model) {
		carService.save(sysCar);
	}
	
	
	/**
	 * 
	 * @Title: importFile
	 * @Description: 把符合条件的预约信息以excel文件格式导出
	 * @param file
	 * @param redirectAttributes
	 * @return
	 * @author com.mhout.wzw
	 */
	@ResponseBody
	@RequestMapping(value = "export", method = RequestMethod.GET)
	public AjaxJson exportFile(SysCar sysCar, HttpServletRequest request, HttpServletResponse response, String name,
			RedirectAttributes redirectAttributes) {
		if (name != null && !"".equals(name)) {
			sysCar.setCarId(name);
		}
		AjaxJson ajaxJson = new AjaxJson();
		boolean issuccess = true;
		String msg = "操作成功!";
		try {
			String fileName = "沉迷探案馆会员清单" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<SysCar> page = carService.findPage(new Page<SysCar>(request, response, -1), sysCar);
			for (int i = 0; i < page.getList().size() - 1; i++) {
				for (int j = 0; j < page.getList().size() - i - 1; j++) {
					int fro = Integer.parseInt(page.getList().get(j).getRemarks());
					int next = Integer.parseInt(page.getList().get(j + 1).getRemarks());
					if (fro > next) {
						SysCar temp;
						temp = page.getList().get(j);
						page.getList().set(j, page.getList().get(j + 1));
						page.getList().set(j + 1, temp);
					}
				}
			}
			new ExportExcel("会员清单", SysCar.class).setDataList(page.getList()).write(response, fileName).dispose();
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

	@ResponseBody
	@RequestMapping(value = "exportModel", method = RequestMethod.GET)
	public AjaxJson exportModel(SysCar sysCar, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		AjaxJson ajaxJson = new AjaxJson();
		boolean issuccess = true;
		String msg = "操作成功!";
		try {
			String fileName = "短信提醒服务车辆导入模板.xlsx";
			List<SysCar> list = Lists.newArrayList();
			sysCar = new SysCar();
			sysCar.setRemarks("");
			sysCar.setCarId("（必填）");
			sysCar.setCarType("1");
			sysCar.setBeginTime(new Date());
			sysCar.setEffectiveTimeList("1");
			sysCar.setEndTime(new Date());
			sysCar.setMoneyList("1");
			sysCar.setPhone("18000000000");
			sysCar.setUserName("姓名");
			list.add(sysCar);
			new ExportExcel("短信提醒", SysCar.class, 2).setDataList(list).write(response, fileName).dispose();
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
			String val = (String) ei.getCellValue(ei.getRow(0), 0);
			if (!"短信提醒".equals(val)) {
				addMessage(redirectAttributes, "导入车辆安全信息失败！导入信息有误");
				ajax.setMsg("抱歉：请下载正确的模板后再次导入数据！");
				return ajax;
			}
			List<SysCar> list = ei.getDataList(SysCar.class);
			for (SysCar sysCar : list) {
				sysCar.synchronizationTime();
				try {
					if (sysCar.getCarId() == null || "".equals(sysCar.getCarId().replaceAll(" ", ""))) {
						failureMsg.append("<br/>导入车辆的车牌不能为空; ");
						failureNum++;
						continue;
					} else if (sysCar.getUserName() == null || "".equals(sysCar.getUserName().replaceAll(" ", ""))) {
						failureMsg.append("<br/>导入车辆" + sysCar.getCarId() + "的车主姓名不能为空; ");
						failureNum++;
					} else if (checkNameAndTime(sysCar)) {
						failureMsg.append(returnNameAndTime(sysCar));
						failureNum++;
					} else if ("true".equals(checkSysCar(sysCar))) {
						if (checkInput(sysCar)) {
							if (checkCarType(sysCar.getCarType())) {
								String code = carService.findCarMaxRemarks();
								String year = DateUtils.getYearBack();
								if (code != null) {
									code = code.replaceAll(" ", "");
								}
								if (StringUtils.isNotBlank(code)) {
									if (code.indexOf(year) > -1) {
										// 序号加1
										String end = String.valueOf(Integer.parseInt(code) + 1);
										sysCar.setRemarks(end);
									} else {
										sysCar.setRemarks(DateUtils.getYearBack() + "001");
									}
								} else {
									sysCar.setRemarks(DateUtils.getYearBack() + "001");
								}
								carService.save(sysCar);
								successNum++;
							} else {
								failureMsg.append("<br/>导入车辆 " + sysCar.getCarId() + " 的车辆类型不正确; ");
								failureNum++;
							}
						} else {
							failureMsg.append("<br/>导入车辆 " + sysCar.getCarId() + " 的车牌信息输入不合规则; ");
							failureNum++;
						}
					} else {
						failureMsg.append("<br/>导入车辆 " + sysCar.getCarId() + " 的相关信息已存在; ");
						failureNum++;
					}
				} catch (ConstraintViolationException ex) {
					failureMsg.append("<br/>车辆 " + sysCar.getCarId() + " 导入失败");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList) {
						failureMsg.append(message + "; ");
						failureNum++;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					failureMsg.append("<br/>车辆" + sysCar.getCarId() + " 导入失败");
				}
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "，失败 " + failureNum + " 条车辆信息");
			}
			System.out.println(failureMsg);
			addMessage(redirectAttributes, "已成功导入 " + successNum + " 条车辆信息；失败：" + failureNum + "条，原因：" + failureMsg);
			ajax.setMsg("已成功导入 " + successNum + " 条车辆信息" + failureMsg.toString());
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入车辆信息失败！失败信息：" + e.getMessage());
			ajax.setMsg("导入车辆信息失败！");
		}
		return ajax;
	}

	private boolean checkNameAndTime(SysCar sysCar) {
		// TODO Auto-generated method stub
		String name = sysCar.getUserName().replaceAll(" ", "");
		String synx = "^[a-zA-Z\u4E00-\u9FA5]+$";
		Pattern pattern = Pattern.compile(synx);
		Matcher match = pattern.matcher(name);
		boolean b = match.matches();
		// if(sysCar.getBeginTime().getTime()>sysCar.getEndTime().getTime()){
		// return true;
		// }
		if (sysCar.getPhone().replaceAll(" ", "").length() != 11) {
			return true;
		}
		if (!b) {
			return true;
		}

		return false;
	}

	private String returnNameAndTime(SysCar sysCar) {
		// TODO Auto-generated method stub
		String name = sysCar.getUserName().replaceAll(" ", "");
		String synx = "^[a-zA-Z\u4E00-\u9FA5]+$";
		Pattern pattern = Pattern.compile(synx);
		Matcher match = pattern.matcher(name);
		boolean b = match.matches();
		// if(sysCar.getBeginTime().getTime()>sysCar.getEndTime().getTime()){
		// return "<br/>导入车辆 " + sysCar.getCarId() + " 的截止日期应该在开始日期之后; ";
		// }
		if (sysCar.getPhone().replaceAll(" ", "").length() != 11) {
			return "<br/>导入车辆 " + sysCar.getCarId() + " 的电话号码输入有误; ";
		}
		if (!b) {
			return "<br/>导入车辆 " + sysCar.getCarId() + " 的车主姓名只能为汉字和字母; ";
		}

		return "<br/>导入车辆 " + sysCar.getCarId() + " 的信息输入不合规则; ";
	}

	private String checkSysCar(SysCar sysCar) {
		if (sysCar.getCarId() == null || "".equals(sysCar.getCarId()) || sysCar.getUserName() == null
				|| "".equals(sysCar.getUserName())) {
			return "false";
		}
		return carService.checkName(sysCar.getCarId());
	}

	/**
	 * 检查输入EXCEL输入是否有误
	 * 
	 * @param user
	 * @return
	 */
	private boolean checkInput(SysCar sysCar) {
		Pattern patternCarId = Pattern.compile("^[\u4e00-\u9fa5|WJ]{1}[A-Z0-9]{6}$");
		boolean check = false;
		boolean a = patternCarId.matcher(sysCar.getCarId()).matches();
		if (a) {
			check = true;
		}
		return check;
	}

	/**
	 * @Title: checkName
	 * @Description: TODO
	 * @return
	 * @author com.mhout.wzw
	 */
	@ResponseBody
	@RequestMapping(value = "checkName", method = RequestMethod.POST)
	public String checkName(@RequestParam(required = false) String name, @RequestParam(required = false) String carId) {
		return carService.checkName(carId);
	}

	/**
	 * @Title: checkCarId
	 * @Description: TODO
	 * @return
	 * @author com.mhout.wzw
	 */
	@ResponseBody
	@RequestMapping(value = "checkCarId", method = RequestMethod.POST)
	public boolean checkCarId(@RequestParam(required = false) String name, String id,
			@RequestParam(required = false) String carId) {
		AjaxJson json = new AjaxJson();
		SysCar car = new SysCar();
		car.setId(id);
		car = carService.get(car);
		if (carId != null && carId.equals(car.getCarId())) {
			json.setSuccess(true);
			return true;
		} else if (carService.checkName(carId).equals("true")) {
			json.setSuccess(true);
			return true;
		} else {
			json.setSuccess(false);
			return false;
		}
	}

	@ResponseBody
	@RequestMapping("insertSysCar")
	public AjaxJson insertSysCar(SysCar sysCar) {
		AjaxJson ajaxJson = new AjaxJson();
		// sysCar.setRemarks(carService.insertCarId());
		if (sysCar.getRemarks() == null || "".equals(sysCar.getRemarks())) {
			String code = carService.findCarMaxRemarks();
			String year = DateUtils.getYearBack();
			if (code != null) {
				code = code.replaceAll(" ", "");
			}
			if (StringUtils.isNotBlank(code)) {
				if (code.indexOf(year) > -1) {
					// 序号加1
					String end = String.valueOf(Integer.parseInt(code) + 1);
					sysCar.setRemarks(end);
				} else {
					sysCar.setRemarks(DateUtils.getYearBack() + "001");
				}
			} else {
				sysCar.setRemarks(DateUtils.getYearBack() + "001");
			}
		}
		carService.save(sysCar);
		ajaxJson.setMsg("添加成功");
		ajaxJson.setSuccess(true);
		return ajaxJson;

	}

	@ResponseBody
	@RequestMapping("consumption")
	public AjaxJson consumption(SysCar sysCar) {
		AjaxJson ajaxJson = new AjaxJson();
		int money = sysCar.getMoney();
		sysCar.setMoney(money+sysCar.getCostMoney());
		carService.save(sysCar);
		ajaxJson.setMsg("消费完成");
		ajaxJson.setSuccess(true);
		return ajaxJson;
	}
	
	
	
	@ResponseBody
	@RequestMapping("rechargeTime")
	public AjaxJson rechargeTime(SysCar sysCar, int rechargeTime, int rechargeMoney) {
		AjaxJson ajaxJson = new AjaxJson();
		sysCar.setMoney(sysCar.getMoney() + rechargeMoney);
		sysCar.setEffectiveTime(sysCar.getEffectiveTime() + rechargeTime);
		carService.save(sysCar);
		ajaxJson.setMsg("修改成功");
		ajaxJson.setSuccess(true);
		return ajaxJson;
	}

	@ResponseBody
	@RequestMapping("continue")
	public AjaxJson recharge(SysCar sysCar) {
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.setMsg("修改成功");
		ajaxJson.setSuccess(true);
		return ajaxJson;
	}

	@ResponseBody
	@RequestMapping(value = { "batchDelete" }, method = RequestMethod.POST)
	public AjaxJson batchPass(@RequestParam(value = "ids") String[] ids) {
		AjaxJson ajaxJson = new AjaxJson();
		carService.batchDelete(ids);
		ajaxJson.setSuccess(true);
		ajaxJson.setMsg("操作成功");
		return ajaxJson;
	}

	private boolean checkCarType(String carType) {
		boolean b = false;
		if ("1".equals(carType) || "2".equals(carType) || "3".equals(carType)) {
			b = true;
		}
		return b;
	}

}
