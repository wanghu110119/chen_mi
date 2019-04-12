<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>应用认证管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	var validateForm;
	function doSubmit() {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		if (validateForm.form()) {
			$("#inputForm").submit();
			return true;
		}
		clear();
		return false;
	}
	$(document).ready(
			function() {
				//$("#name").focus();
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
	#picDec {display: none;}
</style>
</head>
<body class="hideScroll">
	<form:form id="inputForm" modelAttribute="application"
		action="${ctx}/ident/userapplication/save" enctype="multipart/form-data"
		method="post" class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<input type="hidden" name="modifyType" value="${modifyType}"/>
		<table
			class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
				<form:input type="hidden" path="type" value="1"/>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font
							color="red">*</font>应用名称：</label></td>
					<td class="width-35"><form:input path="name"
							htmlEscape="false" maxlength="40" class="form-control required" /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font
							color="red">*</font>接入类型：</label></td>
					<td class="width-35 appl_radio">
						<form:radiobutton path="accessWay" value="2" checked="true" class="required" /><span>&nbsp;超链接&nbsp;</span>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font
							color="red">*</font>应用图标：</label></td>
					<td class="width-35">
						<div class="upload">
<%-- 							<form:input type="hidden" path="uploadType" id="uploadTypeValue"/> --%>
							<select id="uploadType" name="uploadType" class="form-control">
								<option value="1">系统内置</option>
								<option value="2">本地上传</option>
							</select>
						</div>
						<div class="upload">
							<form:input path="pic" id="picname"  maxlength="200" class="form-control"  disabled="true"/>
						</div>
						<div class="upload">
							<input type="button" id="picDec" class="form-control" value="选择图标"/>
							<span id="uploadbtn" style="display: none;">
								<input name="button" type="button" class="form-control" onclick="file.click();" value="上传图标" />
								<input type="file" id="file" name="file" accept="image/jpeg,image/jpg,image/png"
								 onchange="picname.value=this.value" style="display:none">
							</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font
							color="red">*</font>应用URL：</label></td>
					<td class="width-35"><form:input path="url"
							htmlEscape="false" maxlength="200" class="form-control required isurl" /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font
							color="red">*</font>应用IP策略：</label></td>
					<td class="width-35 appl_radio" id="ipStrategy">
						<form:radiobutton path="ipStrategy" value="1" checked="true" class="required" /><span>&nbsp;IP地址&nbsp;</span>
						<form:radiobutton path="ipStrategy" value="2" class="required" /><span>&nbsp;IP地址段&nbsp;</span>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font
							color="red">*</font>应用IP：</label></td>
					<td class="width-35">
						<div id="singleIp">
							<div>
								<form:input path="ip"
								htmlEscape="false" maxlength="200" placeholder="请输入ip地址" id="sid" class="form-control required isip" />
							</div>
						</div>
						<div id="sectionIp">
							<div> ~ </div>
							<div>
								<form:input path="maxip"
								htmlEscape="false" maxlength="200" placeholder="ip段结束值" class="form-control required isMaxIp"/>
							</div>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</form:form>
	<script type="text/javascript">
		$(function(){
			/*上传选中事件*/
			$("#uploadType").change(function(){
				var msg = $(this).val();
				if (msg == 1) {
					$("#uploadbtn").hide();
					//$("#picDec").show();
					$("#picDec").hide();
				} else if (msg == 2) {
					$("#picDec").hide();
					$("#uploadbtn").show();
				}
			});
			
			/*ip策略切换*/
			$("#ipStrategy").find("input[type=radio]").click(function(){
				var msg = $(this).val();
				if (msg == 1) {
					$("#sectionIp").hide();
					$("#singleIp").show();
				} else if (msg == 2) {
					$("#sectionIp").show();
				}
			});
			/*显示ip策略选项*/
			display();
// 			initUpload();
			
		});
		
		function display() {
			var ipStrategy = $("#ipStrategy").find("input[type='radio']:checked").val();
			if ("1" == ipStrategy) {
				$("#sectionIp").hide();
				$("#singleIp").show();
			} else if ("2" == ipStrategy) {
				$("#sectionIp").show();
			}
		}
		
// 		function initUpload() {
// 			var msg = $("#uploadTypeValue").val();
// 			$("#uploadType").val(msg);
// 		}
	</script>
</body>
</html>