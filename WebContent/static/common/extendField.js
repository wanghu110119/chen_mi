var extendField = (function(extendField){
	if(extendField) return extendField;
	extendField ={};
	//该参数用于保存当前每一个group中table的下标，便于对数据进行分组，在一对多数据时有使用
	var groupsIndex = {};
	/**
	 * 根据扩展字段，拼接成table的html语句，并使用html()把table塞入指定的div中;
	 * @param groupRole 用户类型 1 学生 2 老师 3 家长
	 * @param userId 用户id
	 * @param inputDivId table放的DivID
	 * @param fieldSize 每一行有多少个字段
	 */
	extendField.initExtendFieldTable = function(groupRole, userId,url,inputDivId, fieldSize){
		$.ajax({
			url : url,
			type : 'post',
			dataType : 'json',
			data : { 
				'groupRole' : groupRole,
				'userId' : userId
			},
			success : function(result){				
				if (result.success) {
					var groups = result.body.groups;
					var extendInfos = result.body.extendInfos;
					html = "";
					for(var i = 0 ; i < groups.length ; i ++ ){
						groupsIndex[groups[i].groupName] = 0;
						html += getGroupDivInfo(groups[i],fieldSize);
					}
					//将生成的html填充到指定的dom元素中
					$("#" + inputDivId).html(html);
					
					//填充已填写的数据
					setExtendInfo(extendInfos, groups, fieldSize);
					//使用icheck初始化表单中的radioBox
					$("#" + inputDivId + " :radio").iCheck({checkboxClass:"icheckbox_square-green",radioClass:"iradio_square-green"});
					//使用icheck初始化表单中的checkBox
					$("#" + inputDivId + " :checkbox").iCheck({checkboxClass:"icheckbox_square-green",radioClass:"iradio_square-green"});						
				}else{
					layer.alert(result.msg);
				}
				
				$("#" + inputDivId).delegate(".infoAdd","click",function(){
					var groupName = $(this).val();	
					
					for(var i = 0 ; i < groups.length ;i++){
						if(groupName == groups[i].groupName ){
							var newTable=$("<div class='panel panel-success'><button type='button' class='btn btn-danger btn-xs delField' title='删除'><i class='fa fa-trash'></i> 删除</button></div>");
							$(this).parent().next().append(newTable.append(getTableInfo(groups[i].configs,fieldSize,groupName)));
							break;
						}
					}
					//使用icheck初始化表单中的radioBox
					$("#" + inputDivId + " :radio").iCheck({checkboxClass:"icheckbox_square-green",radioClass:"iradio_square-green"});
					//使用icheck初始化表单中的checkBox
					$("#" + inputDivId + " :checkbox").iCheck({checkboxClass:"icheckbox_square-green",radioClass:"iradio_square-green"});	
				});
			
				$("#" + inputDivId).delegate(".delField","click",function(){
					$(this).parent().remove();
				});
			},
			error : function(){
				layer.alert("获取字段异常！");
			}
		});
	};
	
	/**
	 * 绑定用户已保存的数据值
	 */
	var setExtendInfo = function(extendInfo, groups, fieldSize){
		$.each(extendInfo,function(groupName,values){
			for(var i = 0 ; i < values.length ; i++){
				if(i > 0){
					for(var j = 0 ; j < groups.length ;i++){
						if(groupName == groups[j].groupName ){
							var newTable=$("<div class='panel panel-success'><button type='button' class='btn btn-danger btn-xs delField' title='删除'><i class='fa fa-trash'></i> 删除</button></div>");
							$("#group_"+groupName).parent().next().append(newTable.append(getTableInfo(groups[j].configs,fieldSize,groupName)));
							break;
						}
					}
				}
				$.each(values[i],function(key,value){
					if(value){
						var dom = '[name="'+ groupName +'.'+ key +'.'+ i +'"]';
						var domObject = $(dom)[0]; //从jQuery对象中得到原生的DOM对象
						if(domObject){
							if(domObject.tagName == "INPUT"){
								if($(dom).attr("type") == "radio"){
									$.each($(dom),function(){
										if($(this).val() == value){
											$(this).attr("checked",true)
										}
									});
								}else if($(dom).attr("type") == "checkbox"){
									value = value.split(";");
									$.each($(dom),function(){
										for(var k = 0; k < value.length; k++){
											if($(this).val() == value[k]){
												$(this).attr("checked",true)
											}
										}
									});
								}else{
									$(dom).val(value);
								}
							}else{
								$(dom).val(value);
							}
						}
					}
				});
			}
		});
	}
	
	
	var getGroupDivInfo = function(group,fieldSize){		
		var groupInfo = '<div class="panel panel-success">';
		groupInfo += 		'<div class="panel-heading">';
		groupInfo +=			'<i class="fa fa-rss-square"></i>'+group.groupCName;
		if(group.groupType == "MANY"){
			groupInfo += 		'&nbsp;&nbsp;<button id="group_'+ group.groupName +'" value="'+ group.groupName +'" type="button" class="btn btn-info btn-xs infoAdd" title="添加"><i class="fa fa-plus"></i> 添加</button>';
		}
		groupInfo +=		'</div>';		
		groupInfo += 		'<div class="panel-body">';
		groupInfo += 	getTableInfo(group.configs,fieldSize,group.groupName);
		groupInfo += '</div></div>';	
		return groupInfo;
	}
	
	var getTableInfo = function(configs,fieldSize,groupName){
		var currentTableIndex = groupsIndex[groupName];
		var table = "<table class='table table-bordered  table-condensed dataTables-example dataTable no-footer'>";
		table += "<tbody>";
		for(var i = 0 ; i < configs.length ; i+=fieldSize){
			table += "<tr>";
			table += getTdByConfig(configs[i],currentTableIndex,groupName);
			for(var j = 1 ; j < fieldSize ; j++){
				if(i+j < configs.length){
					table += getTdByConfig(configs[i+j],currentTableIndex,groupName);
				}else{
					table += "<td class='width-15 active'></td><td class='width-35'></td>";
				}
			}
			table +="</tr>"
		}
		table += "</tbody>";
		table += "</table> ";
		groupsIndex[groupName] = groupsIndex[groupName]+1;
		return table;
	}
	
	
	/**
	 * 通过字段config生成td。生成的td有两个，一个为名称td，一个为输入td。
	 */
	var getTdByConfig = function(config,currentTableIndex,groupName){
		var tdHtml = "<td class='width-15 active'><label class='pull-right'>"+ config.fieldCName +"：</label></td>" 
		tdHtml += "<td class='width-35'>";
		
		var fieldName = groupName+"."+config.colName+"."+ currentTableIndex;
		//fieldValue 已经进行修改，后期预计作为默认值字段，暂时对该字段不维护
		var fieldValue = config.fieldValue ? config.fieldValue : "";
		var listValue = config.listValue ? config.listValue : "";
		var validateHtml = getValidateHtml(config);

		switch (config.dataType) {
			case "input":
				tdHtml += getInputTd(fieldName,fieldValue,validateHtml);
				break;
			case "textarea":
				tdHtml += getTextAreaTd(fieldName,fieldValue,validateHtml);
				break;
			case "select":
				tdHtml += getSelectTd(fieldName,fieldValue,listValue,validateHtml);
				break;
			case "radiobox":
				tdHtml += getRadioBoxTd(fieldName,fieldValue,listValue,validateHtml);
				break;
			case "checkbox":
				tdHtml += getCheckBoxTd(fieldName,fieldValue,listValue,validateHtml);
				break;
			case "dateselect":
				tdHtml += getDateselectTd(fieldName,fieldValue,validateHtml);
				break;
			default:
				break;
			}
		tdHtml += "</td>";
		return tdHtml;
	};
	
	/**
	 * 获取input框
	 * @param config
	 * @returns
	 */
	var getInputTd = function (fieldName,fieldValue,validateHtml){
		return "<input name='"+ fieldName + "' " + validateHtml + " value='"+ fieldValue +"' type='text'>";
	};
	
	/**
	 * 获取textArea框
	 * @param config
	 * @returns
	 */
	var getTextAreaTd = function (fieldName,fieldValue,validateHtml){
		return "<textarea name='"+ fieldName + "' " + validateHtml +" rows='3'>"+ fieldValue +"</textarea>";
	}
	
	/**
	 * 获取Select
	 * @param config
	 * @returns
	 */
	var getSelectTd = function(fieldName,fieldValue,listValue,validateHtml){
		var selectHtml = "<select name='"+ fieldName + "' " + validateHtml +">";
		if(listValue){
			listValue = listValue.split(";");
			for(var i = 0 ; i < listValue.length; i++){
				selectHtml += "<option value='"+ listValue[i] +"' "+ (fieldValue == listValue[i] ? "selected" : "" )+ ">"+ listValue[i] +"</option>";
			}
		}
		selectHtml += "</select>";
		return selectHtml;
	}
	
	/**
	 * 获取radioBox
	 * @param config
	 * @returns
	 */
	var getRadioBoxTd = function(fieldName,fieldValue,listValue,validateHtml) {
		var radioHtml = "";
		if(listValue){
			listValue = listValue.split(";");
			for(var i = 0 ; i < listValue.length; i++){
				radioHtml += "<input  name='"+ fieldName + "' " + validateHtml +" type='radio' value='"+ listValue[i] +"' "+ (fieldValue == listValue[i] ? "checked=true" : "" )+ ">"+ listValue[i];
			}
		}
		return radioHtml;
	}
	
	/**
	 * 获取checkBox
	 * @param config
	 * @returns
	 */
	var getCheckBoxTd = function(fieldName,fieldValue,listValue,validateHtml){
		var checkBoxHtml = "";
		if(listValue){
			listValue = listValue.split(";");
			if(fieldValue){
				fieldValue = fieldValue.split(";");
			}
			for(var i = 0 ; i < listValue.length; i++){
				checkBoxHtml += "<input name='"+ fieldName + "' " + validateHtml +" type='checkbox' value='"+ listValue[i] +"'";
				if(fieldValue){
					for(var j = 0 ; j < fieldValue.length ;j++ ){
						if(fieldValue[j] == listValue[i]){
							checkBoxHtml += "checked=true";
							break;
						}
					}
				}							
				checkBoxHtml += ">"+ listValue[i];
			}
		};
		return checkBoxHtml;
	}
	
	/**
	 * 获取dateselect
	 * @param config
	 * @returns
	 */
	var getDateselectTd = function(fieldName,fieldValue,validateHtml){
		return "<input name='"+ fieldName + "' " + validateHtml +" onclick=\"laydate({istime: true, format: 'YYYY-MM-DD hh:mm:ss'})\" value='"+ fieldValue +"' type='text'>";
	}
	
	
	/**
	 * 根据配置，获取校验规则html
	 */
	var getValidateHtml = function (config){
		return getClassHtml(config) + getDisableHtml(config) + getLengthValidate(config);
	}
	
	/**
	 * 根据配置设置class
	 * @param config
	 * @returns
	 */
	var getClassHtml = function (config){
		var classStyle = "class='form-control ";
		//设置是否必填
		if(config.isNecessary == 1){
			classStyle += " required ";
		}
		
		if(config.dataType == "dateselect"){
			classStyle += " laydate-icon layer-date "
		}
		classStyle += "'";	
		return classStyle;
	};
	
	var getLengthValidate = function(config){
		if(config.expression == 0 ){
			return " data-rule-rangelength='["+ config.length +","+ config.length +"]' data-msg-rangelength='只能填写"+ config.length +"个字符' "
		}else{
			return " data-rule-maxlength='"+ config.length+"'";
		}
	}
	
	/**
	 * 根据配置获取该dom元素是否可修改
	 * @param config
	 * @returns 
	 */
	var getDisableHtml = function(config){
		return config.isModify == 0 ? " disabled = 'disabled' " : "";
	}
	
	return extendField;
})(extendField);