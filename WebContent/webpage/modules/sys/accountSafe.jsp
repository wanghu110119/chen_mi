<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>个人信息</title>
<meta name="decorator" content="default" />
<style type="text/css">
.index {
	width: 100%;
	height: 600px;
	padding: 20px;
	background-color: #fff;
}

.index-content {
	margin: 0px auto;
}

.index-password {
	margin: 0px auto;
	width: 80%;
	max-width:800px;
	height: 500px;
}

.alert-warning {
	margin-top: 20px;
}

.password-form {
	font-size: 12px;
	font-weight: bold;
}
.form-actions input{
	width: 120px;
}
</style>
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
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<sys:message hideType="1" content="${message}" />
		<div class="index">
			<ul class="nav nav-tabs" role="tablist" id="top-div">
				<li role="presentation" class="active"><a href="#password"
					style="background-color: #1ab394; color: #fff; border: 1px solid #1ab394"
					aria-controls="password" role="tab" data-toggle="tab">密码修改</a></li>
<!-- 				<li role="presentation"><a href="#phone" aria-controls="phone" -->
<!-- 					role="tab" data-toggle="tab">手机绑定</a></li> -->
<!-- 				<li role="presentation"><a href="#email" aria-controls="email" -->
<!-- 					role="tab" data-toggle="tab">邮箱绑定</a></li> -->
			</ul>
			<div class="tab-content">
				<div role="tabpanel" class="tab-pane active" id="password">
					<div class="index-content">
						<div class="index-password">
							<div class="alert alert-warning" role="alert">
								<i class="fa fa-exclamation-circle"></i>&nbsp;温馨提示，你正在通过旧密码修改密码！
							</div>
							<div class="password-form">
								<form:form id="inputForm" modelAttribute="user"
									action="${ctx}/sys/user/modifyPwd" method="post"
									class="form-horizontal form-group">
									<form:hidden path="id" />
									<sys:message hideType="1" content="${message}" />
									<div class="control-group"></div>

									<div class="control-group">
										<label class="col-sm-3 control-label"><font
											color="red">*</font>旧密码:</label>
										<div class="controls">
											<input id="oldPassword" name="oldPassword" type="password"
												value="" maxlength="50"
		        								class="form-control max-width-250 required"
												 />
										</div>
									</div>
									<div class="control-group">
										<label class="col-sm-3 control-label"><font
											color="red">*</font>新密码:</label>
										<div class="controls">
											<input id="newPassword" name="newPassword" type="password"
												value="" maxlength="50"minlength="${pwsmin }"
		         								class="form-control max-width-250 required ${complex ? 'specialChar beginChar pwsChar':''}" rangelength="[${pwsmin },20]"
		         								/>
										</div>
									</div>
									<div class="control-group">
										<label class="control-label"><font color="red">*</font>确认新密码:</label>
										<div class="controls">
											<input id="confirmNewPassword" name="confirmNewPassword"
												type="password" value="" maxlength="50" minlength="${pwsmin }"
		         								class="form-control max-width-250 required ${complex ? 'specialChar beginChar pwsChar':''}" rangelength="[${pwsmin },20]"
												equalTo="#newPassword"></input>
										</div>
									</div>
									<div class="form-actions">
										<input id="btnSubmit" class="btn btn-info" type="submit"
											value="保存修改" />
									</div>
								</form:form>
							</div>
						</div>
					</div>
				</div>
				<div role="tabpanel" class="tab-pane" id="phone"></div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		$("#top-div a").click(function() {
			$("#top-div").find("a").each(function() {
				$(this).css({
					"background-color" : "#fff",
					"color" : "#A7B1C2",
					"border" : "none",
					"border-bottom" : "1px solid #ddd"
				});
			});
			$(this).css({
				"background-color" : "#1ab394",
				"color" : "#fff"
			});
		});
	</script>
</body>
</html>