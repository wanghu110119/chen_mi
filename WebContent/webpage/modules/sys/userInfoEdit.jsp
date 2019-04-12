<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>个人信息</title>
<meta name="decorator" content="default" />
<style type="text/css">
ul {
	margin-top: 5px;
}

.top-div {
	background-color: #1ab394;
	color: #fff;
}

#top-base {
	margin: 0px 0px 0px 0px;
}

img {
	width: 80px;
	height: 50px;
}

#top-base table tr td {
	text-align: center;
}

.auth-title {
	height: 40px;
	font-size: 16px;
	line-height: 30px;
	padding: 5px 0px 5px 0px;
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
<body>
	<form:form id="inputForm" modelAttribute="user"
		action="${ctx}/sys/user/infoEdit" method="post"
		class="form-horizontal form-group">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<div>
			<ul class="nav nav-tabs" role="tablist" id="top-div">
				<li role="presentation" class="active"><a href="#base"
					aria-controls="base" role="tab" data-toggle="tab"
					style="background-color: #1ab394; color: #fff; border: 1px solid #1ab394">基础信息</a></li>
				<li role="presentation"><a href="#auth" aria-controls="auth"
					role="tab" data-toggle="tab">权限信息</a></li>
			</ul>

			<!-- Tab panes -->
			<div class="tab-content">
				<div role="tabpanel" class="tab-pane active" id="base">
					<div class="row top-base" id="top-base">
						<div class="col-sm-6">
							<table
								class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
								<tbody>
									<tr>
										<td class="width-50 active" colspan="2"><img alt=""
											class="img-thumbnail"
											src="${fns:getUserImage(ctxStatic)}"
											>
<!-- 											src="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000 -->
<!-- 											&sec=1492496132121&di=5930bcf218f149c62302026fb6b1d706&imgtype=0 -->
<!-- 											&src=http%3A%2F%2Fimg3.duitang.com%2Fuploads%2Fblog%2F201406%2F30%2F20140630105358_zv2HM.jpeg" -->
										</td>
									</tr>
									<tr>
										<td class="width-15 active"><label class="pull-right"><font
												color="red">*</font>证件号：</label></td>
										<td class="width-35"><form:input path="idNo"
												htmlEscape="false" maxlength="200"
												class="form-control required" readonly="true" /></td>
									</tr>
									<tr>
										<td class="width-15 active"><label class="pull-right">手机号：</label></td>
										<td class="width-35"><form:input path="mobile"
												htmlEscape="false" maxlength="200"
												class="form-control required telphone" /></td>
									</tr>
									<tr>
										<td class="width-15 active"><label class="pull-right">用户组：</label></td>
										<td class="width-35"><input value="${user.groupNames}"
											maxlength="200" class="form-control" readonly="true" /></td>
									</tr>
									<tr>
										<td class="width-15 active"><label class="pull-right">组织机构：</label></td>
										<td class="width-35"><input value="${user.officeNames}"
											maxlength="200" class="form-control" readonly="true" /></td>
									</tr>
									<tr>
										<td class="width-15 active"><label class="pull-right">角色：</label></td>
										<td class="width-35"><input value="${user.roleNames}"
											maxlength="200" class="form-control" readonly="true" /></td>
									</tr>
									<tr>
										<td class="width-15 active"><label class="pull-right">备注：</label></td>
										<td class="width-35"><form:input path="remarks"
											maxlength="200" class="form-control" readonly="true"/></td>
									</tr>
								</tbody>
							</table>
						</div>
						<div class="col-sm-6">
							<table
								class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
								<tbody>
									<tr>
										<td class="width-15 active"><label class="pull-right"><font
												color="red">*</font>账号：</label></td>
										<td class="width-35"><input value="${user.loginName}"
											maxlength="200" class="form-control" readonly="true" /></td>
									</tr>
									<tr>
										<td class="width-15 active"><label class="pull-right"><font
												color="red">*</font>别名：</label></td>
										<td class="width-35"><form:input path="account"
												htmlEscape="false" maxlength="200"
												class="form-control required" /></td>
									</tr>
									<tr>
										<td class="width-15 active"><label class="pull-right"><font
												color="red">*</font>姓名：</label></td>
										<td class="width-35"><form:input path="name"
												htmlEscape="false" maxlength="200"
												class="form-control required" /></td>
									</tr>
									<tr>
										<td class="width-15 active"><label class="pull-right">邮件地址：</label></td>
										<td class="width-35"><form:input path="email"
												htmlEscape="false" maxlength="200"
												class="form-control email" /></td>
									</tr>
									<tr>
										<td class="width-15 active"><label class="pull-right">性别：</label></td>
										<td class="width-35"><form:select path="gender"
												class="form-control">
												<form:options items="${fns:getDictList('sex')}"
													itemLabel="label" itemValue="value" htmlEscape="false" />
											</form:select></td>
									</tr>
									<tr>
										<td class="width-15 active"><label class="pull-right">民族：</label></td>
										<td class="width-35"><form:select path="nation"
												class="form-control">
												<form:options items="${fns:getDictList('nation')}"
													itemLabel="label" itemValue="value" htmlEscape="false" />
											</form:select></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div role="tabpanel" class="tab-pane" id="auth">
					<div class="auth-title">应用权限信息</div>
					<table id="contentTable"
						class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">

						<tr>
							<th>应用名称</th>
							<th>权限信息</th>
						</tr>
						<c:forEach items="${list}" var="userAuth">
							<tr appid="${userAuth.apply.id}">
								<td>${userAuth.apply.name}</td>
								<td><c:if
										test="${not empty userAuth.isEdit and userAuth.isEdit eq '2'}">
										<span><input type="checkbox"
											isEdit="${userAuth.isEdit}" class="i-checks accessAuth"
											${userAuth.closeType eq '1' ? '':'checked=checked'}>访问权限</span>
									</c:if> <c:if
										test="${not empty userAuth.accessAuth and empty userAuth.isEdit or userAuth.isEdit eq '1'}">
										<input type="checkbox" class="i-checks accessAuth"
											checked="checked">访问权限</c:if> <c:if
										test="${empty userAuth.accessAuth and empty userAuth.isEdit or userAuth.isEdit eq '1'}">
										<input type="checkbox" class="i-checks accessAuth">访问权限</c:if>
								</td>
							</tr>
						</c:forEach>

					</table>
				</div>
			</div>
		</div>
	</form:form>
	<script type="text/javascript">
		$(function() {
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
		});
	</script>
</body>
</html>