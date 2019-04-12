/**
 * Copyright &copy; 2015-2020 <a href="http://www.mht.org/">mht</a> All rights reserved.
 */
package com.mht.modules.fieldconfig.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mht.common.json.AjaxJson;
import com.mht.common.web.BaseController;
import com.mht.modules.account.constant.GroupRole;
import com.mht.modules.fieldconfig.entity.FieldGroup;
import com.mht.modules.fieldconfig.service.FieldGroupService;
import com.mht.modules.fieldconfig.service.UserExtendInfoService;
/**
 * 
* @ClassName: FieldConfigController 
* @Description: 扩展字段配置Controller
* @author 华强 
* @date 2017年3月30日 上午11:04:37 
*
 */
@Controller
@RequestMapping(value = "${adminPath}/fieldconfig/userExtendInfo")
public class UserExtendInfoController extends BaseController {

	@Autowired
	private FieldGroupService fieldGroupService;
	@Autowired
	private UserExtendInfoService userExtendInfoService;
	/**
	 * 获取可使用的扩展字段,如果用户id不为空,则将用户的对应扩展信息放在字段中
	 */
	@RequestMapping(value = {"usablefieldList"})
	@ResponseBody
	public AjaxJson usableFieldList(String groupRole,String userId ) {
		AjaxJson ajaxJson = new AjaxJson();
		try{
			//获取分组信息（包括字段信息）
			List<FieldGroup> groups = fieldGroupService.findFieldGroupByGroupRole(GroupRole.valueOf(groupRole));			
			ajaxJson.put("groups", groups);
			Map<String,List<Map<String,String>>> extendInfos = new HashMap<String, List<Map<String,String>>>();
			//根据分组获取该用户填写的每个分组的信息
			if(CollectionUtils.isNotEmpty(groups)){
				for(FieldGroup group : groups){
					List<Map<String,String>> groupValues = userExtendInfoService.getExendInfoByGroupIdAndUserId(group.getId(),userId);
					if(CollectionUtils.isNotEmpty(groupValues)){
						extendInfos.put(group.getGroupName(), groupValues);
					}
				}
			}
			ajaxJson.put("extendInfos", extendInfos);
			ajaxJson.setSuccess(true);
		}catch (Exception e) {
			e.printStackTrace();
			ajaxJson.setMsg("获取扩展字段异常！");
			ajaxJson.setSuccess(false);
		}
		return ajaxJson;
	}
	
}