package com.mht.modules.swust.web;

import java.text.SimpleDateFormat;
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
import com.mht.modules.swust.entity.MemberDetail;
import com.mht.modules.swust.entity.SysCar;
import com.mht.modules.swust.entity.SysOrderlist;
import com.mht.modules.swust.service.MemberDetailService;
import com.mht.modules.swust.service.SysCarService;
import com.mht.modules.sys.entity.Office;
import com.mht.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value = "a/swust/car")
public class MemberCotroller extends BaseController {
	@Autowired
	private SysCarService carService;
	@Autowired
	private MemberDetailService memberService;

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
	@ResponseBody
	@RequestMapping(value = { "checkCardID" })
	public String checkCardID(String name, Model model) {
		return carService.checkCardID(name);
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
	public AjaxJson getCarMessage(  SysCar sysCar ) {
		AjaxJson json = new AjaxJson();
		MemberDetail memberDetail = memberService.sumCostAndCharge(sysCar);
		sysCar.setMemberDetail(memberDetail);
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

	
	/**
	 * 
	 * @Title: exportDetail
	 * @Description: 把符合条件的预约信息以excel文件格式导出
	 * @param file
	 * @param redirectAttributes
	 * @return
	 * @author com.mhout.wzw
	 */
	@ResponseBody
	@RequestMapping(value = "exportCostOrChargeHistory", method = RequestMethod.GET)
	public AjaxJson exportCostOrChargeHistory( MemberDetail member,HttpServletRequest request, HttpServletResponse response ) {
		AjaxJson ajaxJson = new AjaxJson();
		boolean issuccess = true;
		SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd");
		String begin =  format.format(member.getBeginTime());
		String end =  format.format(member.getEndTime());
		String msg = "操作成功!";
		try {
			String fileName = "沉迷探案馆会员"+begin+"~~"+end+"消费详情.xlsx";
			List<MemberDetail> page = memberService.findList( member);
			page.add(memberService.sum());
			new ExportExcel("沉迷探案馆会员"+begin+"~~"+end+"消费详情", MemberDetail.class).setDataList(page ).write(response, fileName).dispose();
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
	 * exportCostOrChargeHistory
	 * @Title: exportDetail
	 * @Description: 把符合条件的预约信息以excel文件格式导出
	 * @param file
	 * @param redirectAttributes
	 * @return
	 * @author com.mhout.wzw
	 */
	@ResponseBody
	@RequestMapping(value = "exportDetail", method = RequestMethod.GET)
	public AjaxJson exportDetail(SysCar sysCar, HttpServletRequest request, HttpServletResponse response ) {
		AjaxJson ajaxJson = new AjaxJson();
		boolean issuccess = true;
		String msg = "操作成功!";
		try {
			String fileName = "沉迷探案馆会员"+sysCar.getUserName()+"一年内消费详情" + DateUtils.getDate("MMdd-HH") + ".xlsx";
			MemberDetail member = new MemberDetail();
//			member.setYear(true);
			member.setCardId(sysCar.getCarId());
			List<MemberDetail> page = memberService.findList( member);
			page.add(memberService.sumCostAndChargeByID(member));
			new ExportExcel("沉谜探案馆会员——"+sysCar.getUserName()+"  消费历史", MemberDetail.class).setDataList(page ).write(response, fileName).dispose();
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
		sysCar.setTotalMoney(sysCar.getMoney()+sysCar.getGiftMoney());
		carService.save(sysCar);
		
		MemberDetail member = new MemberDetail();
		member.setGiftMoney(sysCar.getGiftMoney());
		member.setAddMoney(sysCar.getMoney()+"");
		member.setType("1");
		member.setAddTime(sysCar.getEffectiveTime());
		member.setCar(sysCar);
		memberService.save(member);
		
		ajaxJson.setMsg("添加成功");
		ajaxJson.setSuccess(true);
		return ajaxJson;

	}
	
	@ResponseBody
	@RequestMapping("editSysCar")
	public AjaxJson editSysCar(SysCar sysCar) {
		AjaxJson ajaxJson = new AjaxJson();
		carService.save(sysCar);
		ajaxJson.setMsg("编辑成功");
		ajaxJson.setSuccess(true);
		return ajaxJson;

	}

	@ResponseBody
	@RequestMapping("consumption")
	public AjaxJson consumption(SysCar sysCar) {
		AjaxJson ajaxJson = new AjaxJson();
		MemberDetail member = new MemberDetail();
		if(sysCar.getCostMoney()!=0){
			member.setCostMoney(""+-sysCar.getCostMoney());
		}
		if(sysCar.getCostTime()!=0) {
			member.setCostTime(sysCar.getCostTime());
		}
		member.setType("0");
		member.setCar(sysCar);
		memberService.save(member);
		int money = sysCar.getTotalMoney();
		sysCar.setTotalMoney(money+sysCar.getCostMoney());
		sysCar.setEffectiveTime(sysCar.getEffectiveTime()-sysCar.getCostTime());
		carService.save(sysCar);
		ajaxJson.setMsg("消费完成");
		ajaxJson.setSuccess(true);
		return ajaxJson;
	}
	
	
	
	@ResponseBody
	@RequestMapping("rechargeTime")
	public AjaxJson rechargeTime(SysCar sysCar, int rechargeTime, int rechargeMoney) {
		AjaxJson ajaxJson = new AjaxJson();
		MemberDetail member = new MemberDetail();
		member.setAddMoney(rechargeMoney+"");
		member.setGiftMoney(sysCar.getGiftMoney());
		member.setType("1");
		member.setCar(sysCar);
		memberService.save(member);
		sysCar.setMoney(sysCar.getMoney() + rechargeMoney);
		sysCar.setEffectiveTime(sysCar.getEffectiveTime() + rechargeTime);
		sysCar.setTotalMoney(sysCar.getTotalMoney() + sysCar.getGiftMoney()+rechargeMoney);
		carService.save(sysCar);
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
	public static void main(String[] args) {
		System.out.println(1^2);
		System.out.println(1^1);
		System.out.println(5^3);
		System.out.println(3&3);
		System.out.println(1^122);
		String a = "a";
		String b = "a";
		StringBuffer sba = new StringBuffer("a");
		StringBuffer sbb = new StringBuffer("a");
		System.out.println(a==b);
		System.out.println(sba==sbb);
	}

}
