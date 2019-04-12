<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>项目管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	var validateForm;
	function doSubmit() {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		if (validateForm.form()) {
			$("#inputForm").submit();
			return true;
		}

		return false;
	}
	$(document).ready(
			function() {
				
				validateForm = $("#inputForm")
						.validate(
								{
									submitHandler : function(form) {
										loading('正在提交，请稍等...');
										form.submit();
									},
									errorContainer : "#messageBox",
									errorPlacement : function(error, element) {
										$("#messageBox").text("输入有误，请先更正。");
										if (element.is(":checkbox")
												|| element.is(":radio")
												|| element.parent().is(
														".input-append")) {
											error.appendTo(element.parent()
													.parent());
										} else {
											error.insertAfter(element);
										}
									}
								});
			});
</script>
<style type="text/css">
	.appl_radio {vertical-align:middle;}
	.appl_radio input[type=radio] {margin:0 0 0 0;}
	.upload{float: left;margin-left:5px;}
	#sectionIp{display: none;}
</style>
</head>
<body class="hideScroll">
	<form:form id="inputForm" modelAttribute="sysProject"
		action="${ctx}/ident/sysProject/save" enctype="multipart/form-data"
		method="post" class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<table
			class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font
							color="red">*</font>项目名称：</label></td>
					<td class="width-35"><form:input path="name"
							htmlEscape="false" maxlength="200" class="form-control required" /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font
							color="red">*</font>项目地址：</label></td>
					<td class="width-35">
						<form:input path="url"
							htmlEscape="false" maxlength="200" class="form-control required isurl" />
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font
							color="red">*</font>项目代码：</label></td>
					<td class="width-35">
						<form:input path="code"
							htmlEscape="false" maxlength="200" class="form-control required" />
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font
							color="red">*</font>项目IP：</label></td>
					<td class="width-35">
						<div>
							<form:input path="ip"
							htmlEscape="false" maxlength="200" placeholder="请输入ip地址" id="sid" class="form-control required isip" />
						</div>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font
							color="red">*</font>应用状态：</label></td>
					<td class="width-35 appl_radio">
						<form:radiobutton path="status" value="1" checked="true" class="required" /><span>&nbsp;可用&nbsp;</span>
						<form:radiobutton path="status" value="2" class="required" /><span>&nbsp;不可用&nbsp;</span>
					</td>
				</tr>
			</tbody>
		</table>
	</form:form>
</body>
</html>