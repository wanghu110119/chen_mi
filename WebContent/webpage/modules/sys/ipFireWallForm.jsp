<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>IP访问控制管理</title>
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
	<form:form id="inputForm" modelAttribute="ipFireWall"
		action="${ctx}/sys/ipFireWall/save" enctype="multipart/form-data"
		method="post" class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<table
			class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font
							color="red">*</font>IP地址类型：</label></td>
					<td class="width-35 appl_radio" id="ipStrategy">
						<form:radiobutton path="ipType" value="1" checked="true" class="required" /><span>&nbsp;IP地址&nbsp;</span>
						<form:radiobutton path="ipType" value="2" class="required" /><span>&nbsp;IP地址段&nbsp;</span>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font
							color="red">*</font>IP：</label></td>
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
								<form:input path="maxIp"
								htmlEscape="false" maxlength="200" placeholder="ip段结束值" class="form-control required isMaxIp"/>
							</div>
						</div>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font
							color="red">*</font>访问控制类型：</label></td>
					<td class="width-35">
						<form:select path="dict.id" class="form-control">
							<form:options items="${fns:getDictList('sys_ip_fire_wall')}" itemLabel="label" itemValue="id" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">描述：</label></td>
					<td class="width-35">
						<form:textarea path="derc"
						htmlEscape="false" maxlength="255" placeholder="ip描述" class="form-control"/>
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
					$("#picDec").show();
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
	</script>
</body>
</html>