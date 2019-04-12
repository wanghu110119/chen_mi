package com.mht.modules.ident.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mht.common.utils.StringUtils;
import com.mht.common.web.BaseController;
import com.mht.modules.ident.entity.AutRecord;
import com.mht.modules.ident.entity.AutRecordListData;
import com.mht.modules.echarts.entity.ChinaWeatherDataBean;
import com.mht.modules.ident.service.AutRecordService;

/**
 * @ClassName: AutRecordController
 * @Description: 认证记录Controller
 * @author com.mhout.xyb
 * @date 2017年3月24日 上午9:46:32 
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "${adminPath}/ident/autrecord")
public class AutRecordController extends BaseController{
	
	@Autowired
	private AutRecordService autRecordService;
	
	@ModelAttribute("autRecord")
	public AutRecord get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return autRecordService.get(id);
		}else{
			return new AutRecord();
		}
	}
	
	/**
	 * @Title: List 
	 * @Description: 认证统计
	 * @param autRecord
	 * @param request
	 * @param response
	 * @return
	 * @author com.mhout.xyb
	 */
	@ResponseBody
	@RequestMapping(value = {"list"})
	public AutRecordListData List(AutRecord autRecord, HttpServletRequest request, HttpServletResponse response) {
		AutRecordListData data = autRecordService.findAutRecordListData(autRecord);
		return data;
	}
	
	/**
	 * @Title: index 
	 * @Description: 认证统计
	 * @param autRecord
	 * @param chinaWeatherDataBean
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @author com.mhout.xyb
	 */
	@RequestMapping(value = {"index", ""})
	public String index(AutRecord autRecord, ChinaWeatherDataBean chinaWeatherDataBean,
			HttpServletRequest request, HttpServletResponse response, Model model) {
		return "modules/ident/lineAutRecord";
	}
	
}
