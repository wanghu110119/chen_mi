package com.mht.modules.swust.web.mobile;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import com.google.gson.Gson;
import com.mht.common.config.Global;
import com.mht.common.enumutils.SysOrderEnum;
import com.mht.common.json.AjaxJson;
import com.mht.common.persistence.Page;
import com.mht.common.sms.AliSmsUtils;
import com.mht.common.utils.DateUtils;
import com.mht.common.utils.StringUtils;
import com.mht.common.utils.excel.ExportExcel;
import com.mht.common.web.BaseController;
import com.mht.modules.swust.entity.SMSMessage;
import com.mht.modules.swust.entity.SysOrderlist;
import com.mht.modules.swust.entity.SysPhotolist;
import com.mht.modules.swust.service.impl.OrderUserServiceImpl;
import com.mht.modules.sys.entity.User ;
import com.mht.modules.sys.utils.UserUtils;

/**
 * @ClassName: AppointmentController
 * @Description:
 * @author com.mhout.dhn
 * @date 2017年7月26日 下午3:06:05
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "mobile/swust/appointment")
public class AppointmentControllers extends BaseController {

	@Autowired
	OrderUserServiceImpl service;

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
	@RequestMapping(value = { "" } , method = RequestMethod.POST)
	public String index(Model model) {
		return "modules/swust/mobile/systemOrder";
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = { "initphoto" } , method = RequestMethod.POST)
	public AjaxJson initPhoto() {
		AjaxJson json  = new AjaxJson(); 
		User user = UserUtils.getUser();
		List<SysPhotolist> headPhoto = service.initHeadPhoto("1", user.getId());
		if(headPhoto.size()==0){
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

	@RequestMapping(value = { "list" } , method = RequestMethod.POST)
	public String list(SysOrderlist sysOrderlist, HttpServletRequest request, HttpServletResponse response,String color,
			Model model) {
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
		return "modules/swust/mobile/systemOrderList";
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
	@RequestMapping(value = {"batchPass"}, method = RequestMethod.POST)
	public AjaxJson batchPass(@RequestParam(value = "ids")String[] ids) {
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
	@RequestMapping(value = {"batchDelete"}, method = RequestMethod.POST)
	public AjaxJson batchDelete(@RequestParam(value = "ids")String[] ids) {
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
	@RequestMapping(value = {"batchReject"}, method = RequestMethod.POST)
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
	@RequestMapping(value = {"pass"}, method = RequestMethod.POST)
	public AjaxJson pass(SysOrderlist sysOrderlist) {
		AjaxJson ajaxJson = new AjaxJson();
		sysOrderlist.setPass("1");
		sysOrderlist.setState(SysOrderEnum.AUDITED.getParam());
		service.save(sysOrderlist);
		//通过发送短信
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
	@RequestMapping(value = {"reject"}, method = RequestMethod.POST)
	public AjaxJson reject(SysOrderlist sysOrderlist) {
		AjaxJson ajaxJson = new AjaxJson();
		sysOrderlist.setPass(SysOrderEnum.NOTPASS.getParam());
		sysOrderlist.setState(SysOrderEnum.AUDITED.getParam());
		service.save(sysOrderlist);
		//驳回发送短信
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
	 * @Title: sendSms 
	 * @Description: TODO
	 * @return
	 * @author com.mhout.xyb
	 */
	@ResponseBody
	@RequestMapping("sendSms")
	public String sendSms() {
//		AliSmsUtils.sendSmsAliyun("13880461969","川A88888", "1");
		SMSMessage sms = new SMSMessage();
		sms.setNumber("川B66666");
		sms.setBegin("8月01日12点");
		sms.setEnd("8月02日12点");
		String msg = new Gson().toJson(sms);
		AliSmsUtils.sendSmsAliyun("15882032402",msg, "2");
		AliSmsUtils.sendSmsAliyun("15882032402","川A88888", "1");
		return "true";
	}

	public static void main(String[] args) {
		
	}
	
	
	
	
}

