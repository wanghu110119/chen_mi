package com.mht.modules.swust.web.mobile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mht.common.json.AjaxJson;
import com.mht.common.persistence.Page;
import com.mht.common.utils.DateUtils;
import com.mht.common.utils.StringUtils;
import com.mht.common.utils.excel.ExportExcel;
import com.mht.common.web.BaseController;
import com.mht.modules.swust.entity.SysCar;
import com.mht.modules.swust.entity.SysOrderlist;
import com.mht.modules.swust.service.SysCarService;
import com.mht.modules.sys.entity.Office;
@Controller
@RequestMapping(value = "mobile/swust/car")
public class CarSafetyCotrollers  extends BaseController{
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
		return "modules/swust/mobile/smsSend";
	}
	
	@ModelAttribute("sysCar")
	public SysCar get(@RequestParam(required = false) String id ){
		if (StringUtils.isNotBlank(id)){
			SysCar car = new SysCar();
			car.setId(id);
			return carService.get(car);
		} else {
			return new SysCar();
		}
	}
	@ResponseBody
	@RequestMapping(value={"getCar"})
	public AjaxJson getCarMessage(String name,String state,HttpServletRequest request, SysCar sysCar,
			HttpServletResponse response, Model model){
		AjaxJson json = new AjaxJson();
		json.put("sysCar", sysCar);
		return json;
	}
	
	
	@RequestMapping(value={"list"})
	public String SelectCarList(String name,HttpServletRequest request, SysCar sysCar,
			HttpServletResponse response, Model model){
		if(name!=null&&!"".equals(name)){
			sysCar.setCarId(name);
		}
		Page<SysCar>  page = carService.findPage(new Page<SysCar>(request, response), sysCar);
				model.addAttribute("page", page);
		return "modules/swust/mobile/smsSendList";
	}
	
	@RequestMapping(value={"add"})
	public void InsertCarMessage(String name,String state,HttpServletRequest request, SysCar sysCar,
			HttpServletResponse response, Model model){
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
	public AjaxJson exportFile(SysCar sysCar, HttpServletRequest request, HttpServletResponse response,String name,
			RedirectAttributes redirectAttributes) {
		if(name!=null&&!"".equals(name)){
			sysCar.setCarId(name);
		}
		AjaxJson ajaxJson = new AjaxJson();
		boolean issuccess = true;
		String msg = "操作成功!";
		try {
			String fileName = "短信提醒服务车辆清单" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<SysCar> page = carService.findPage(new Page<SysCar>(request, response, -1), sysCar);
			for (int i = 0; i < page.getList().size()-1; i++) {
				for (int j = 0; j < page.getList().size()-i-1; j++) {
					int fro = Integer.parseInt(page.getList().get(j).getRemarks());
					int next = Integer.parseInt(page.getList().get(j+1).getRemarks());
					if(fro>next){
						SysCar temp;
						temp=page.getList().get(j);
						page.getList().set(j,page.getList().get(j+1));
						page.getList().set(j+1,temp);
					}
				}
			}
			new ExportExcel("短信提醒服务车辆清单", SysCar.class).setDataList(page.getList()).write(response, fileName).dispose();
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
	public String checkName(@RequestParam(required = false) String name, 
			@RequestParam(required = false)String carId) {
		return carService.checkName( carId);
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
			@RequestParam(required = false)String carId) {
		AjaxJson json = new AjaxJson();
		SysCar car = new SysCar();
		car.setId(id);
		 car= carService.get(car);
		if(carId!=null&&carId.equals(car.getCarId())){
			json.setSuccess(true);
			return true;
		}
		else if(carService.checkName( carId).equals("true")){
			json.setSuccess(true);
			return true;
		}else{
			json.setSuccess(false);
			return false;
		}
	}
	
	
	@ResponseBody
	@RequestMapping("insertSysCar")
	public AjaxJson insertSysCar(SysCar sysCar) {
		AjaxJson ajaxJson = new AjaxJson();
//		sysCar.setRemarks(carService.insertCarId());
		if(sysCar.getRemarks()==null||"".equals(sysCar.getRemarks())){
			String code = carService.findCarMaxRemarks();
			Integer newcode = Integer.parseInt(code) + 1;
			if (code.length() < 2) {
				sysCar.setRemarks("00" + newcode);
			} else if (code.length() < 3) {
				sysCar.setRemarks("0" + newcode);
			} else {
				sysCar.setRemarks(String.valueOf(newcode));
			}
		}
		sysCar.setEffectiveTime(sysCar.getEffectiveTime()*30);
		carService.save(sysCar);
		ajaxJson.setMsg("添加成功");
		ajaxJson.setSuccess(true);
		return ajaxJson;
		
	}
	@ResponseBody
	@RequestMapping("rechargeTime")
	public AjaxJson rechargeTime(SysCar sysCar,int rechargeTime,int rechargeMoney) {
		AjaxJson ajaxJson = new AjaxJson();
		sysCar.setMoney(sysCar.getMoney()+rechargeMoney);
		sysCar.setEffectiveTime(sysCar.getEffectiveTime()+rechargeTime*30);
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
	@RequestMapping(value = {"batchDelete"}, method = RequestMethod.POST)
	public AjaxJson batchPass(@RequestParam(value = "ids")String[] ids) {
		AjaxJson ajaxJson = new AjaxJson();
		carService.batchDelete(ids);
		ajaxJson.setSuccess(true);
		ajaxJson.setMsg("操作成功");
		return ajaxJson;
	}
	
	
	
	
	
	
	
	
	
	
}








