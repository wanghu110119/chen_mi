<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>扩展字段管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $("#inputForm").submit();
			  return true;
		  }
	
		  return false;
		}
		$(document).ready(function() {
			validateForm = $("#inputForm").validate({
				rules: {
					fieldName: {remote: "${ctx}/fieldconfig/fieldConfig/checkFieldName?oldFieldName=" + encodeURIComponent('${fieldConfig.fieldName}')}//设置了远程验证，在初始化时必须预先调用一次。
				},
				messages: {
					fieldName: {remote: "字段名称已存在"}
				},
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});		
			
			//在ready函数中预先调用一次远程校验函数，是一个无奈的回避案。(刘高峰）
			//否则打开修改对话框，不做任何更改直接submit,这时再触发远程校验，耗时较长，
			//submit函数在等待远程校验结果然后再提交，而layer对话框不会阻塞会直接关闭同时会销毁表单，因此submit没有提交就被销毁了导致提交表单失败。
			$("#inputForm").validate().element($("#fieldName"));			
			
			if(!'${fieldConfig.isNecessary}'){
				$($("input[name='isNecessary']")[1]).iCheck('check');
			}  			
			if(!'${fieldConfig.isModify}'){
				$($("input[name='isModify']")[0]).iCheck('check');
			}
			if(!'${fieldConfig.isUsable}'){
				$($("input[name='isUsable']")[0]).iCheck('check');
			}
			var dataType = $("select[name='dataType']").val();
			changeFormByDataType(dataType);
			$("select[name='dataType']").change(function(){
				var dataType = $(this).val();
				changeFormByDataType(dataType);
			});
			
		});
		
		/**
		* 根据dataType设置form表单中的某些字段是否显示
		*/
		function changeFormByDataType(dataType){			
			if(dataType == 'select' || dataType == 'radiobox' || dataType == 'checkbox'){	
				$('#listValue').show();
			}else{
				$('#listValue').hide();
			}
		}	
	</script>
</head>
<body class="hideScroll">
		<form:form id="inputForm" modelAttribute="fieldConfig" action="${ctx}/fieldconfig/fieldConfig/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>字段名称：</label></td>
					<td class="width-35">
						<input id="oldFieldName" name="oldFieldName" type="hidden" value="${fieldConfig.fieldName}">
						<form:input path="fieldName" htmlEscape="false" maxlength="20" class="form-control required noChinese" />
					</td>					
				</tr>	
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>字段中文名：</label></td>
					<td class="width-35">
						<form:input path="fieldCName" htmlEscape="false" maxlength="20" class="form-control required"/>
					</td>					
				</tr>	
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>数据类型：</label></td>
					<td class="width-35">
						<form:select path="dataType" class="form-control ">							
							<form:options items="${fns:getDictList('data_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>						
					</td>
				</tr>				
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>是否必填：</label></td>
					<td class="width-35">
						<form:radiobuttons path="isNecessary" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required i-checks "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>是否可修改：</label></td>
					<td class="width-35">
						<form:radiobuttons path="isModify" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required i-checks "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>是否可用：</label></td>
					<td class="width-35">
						<form:radiobuttons path="isUsable" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required i-checks "/>
					</td>
				</tr>
				<tr id="listValue">
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>列表值</label></td>
					<td class="width-35">
						<form:input path="listValue" htmlEscape="false"  class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>数据长度：</label></td>
					<td class="width-35">
						<form:select path="expression" class="form-control " style="width:100px;">
							<option value="1" ${ (fieldConfig.expression == null || fieldConfig.expression == "1") ? "selected" : ""  } >&le;</option>
							<option value="0" ${ (fieldConfig.expression != null && fieldConfig.expression == "0") ? "selected" : ""  }>=</option>							
						</form:select>
						<form:input path="length" htmlEscape="false" maxlength="10"  class="form-control required digits" style="width:60px;"/>
					</td>					
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">备注：</label></td>
					<td class="width-35">
						<form:textarea htmlEscape="true" path="remarks" rows="5" maxlength="200" style="width:90%;" wrap="off" />						
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>