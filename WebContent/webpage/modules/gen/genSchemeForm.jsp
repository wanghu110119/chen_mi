<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>生成方案管理</title>
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
			$("#name").focus();
			validateForm = $("#inputForm").validate({
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
		});
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="genScheme" action="${ctx}/gen/genScheme/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="genTable.id"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>代码风格:</label>
			<div class="controls">
				<select id="category" name="category" class="required form-control" aria-required="true">
					<option value="curd" selected="selected">增删改查（单表）</option><option value="curd_many">增删改查（一对多）</option><option value="treeTable">树结构表（一体）</option>
				</select>
				<span class="help-inline">
					生成结构：{包名}/{模块名}/{分层(dao,entity,service,web)}/{子模块名}/{java类}
				</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>生成包路径:</label>
			<div class="controls">
				<input id="packageName" name="packageName" class="required form-control" type="text" value="com.mht.modules" maxlength="500" aria-required="true">
				<span class="help-inline">建议模块包：com.mht.modules</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>生成模块名:</label>
			<div class="controls">
				<input id="moduleName" name="moduleName" class="required form-control" type="text" value="ct" maxlength="500" aria-required="true">
				<span class="help-inline">可理解为子系统名，例如 sys</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">生成子模块名:</label>
			<div class="controls">
				<input id="subModuleName" name="subModuleName" class="form-control" type="text" value="" maxlength="500">
				<span class="help-inline">可选，分层下的文件夹，例如 </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>生成功能描述:</label>
			<div class="controls">
				<input id="functionName" name="functionName" class="required form-control" type="text" value="代码生成模块功能测试" maxlength="500" aria-required="true">
				<span class="help-inline">将设置到类描述</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>生成功能名:</label>
			<div class="controls">
				<input id="functionNameSimple" name="functionNameSimple" class="required form-control" type="text" value="成功" maxlength="500" aria-required="true">
				<span class="help-inline">用作功能提示，如：保存“某某”成功</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>生成功能作者:</label>
			<div class="controls">
				<input id="functionAuthor" name="functionAuthor" class="required form-control" type="text" value="张继平" maxlength="500" aria-required="true">
				<span class="help-inline">功能开发者</span>
			</div>
		</div>
	</form:form>
</body>
</html>
