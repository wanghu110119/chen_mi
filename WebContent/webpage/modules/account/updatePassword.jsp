<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>机构管理</title>
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
<body class="hideScroll">
	<form:form id="inputForm" modelAttribute="post" action="${ctx}/account/common/savePassword" method="post" class="form-horizontal">
		<input type="hidden" id="id" name="id" value="${id} "/>
		<input type="hidden" id="url" name="url" value="${url} "/>
		<sys:message content="${message}"/>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		       <tr>
		         <td class="width-15 active"><label class="pull-right"><font color="red">*</font>新密码:</label></td>
		         <td class="width-35">
		         <input id="newPassword" name="newPassword" type="password" value="" maxlength="50" minlength="${pwsmin }"
		         class="form-control ${empty student.id?'required':''} ${complex ? 'specialChar beginChar pwsChar':''}" rangelength="[${pwsmin },20]" aria-required="true" aria-invalid="false"></td>
		      </tr>
		      <tr>
		          <td  class="width-15"  class="active"><label class="pull-right"><font color="red">*</font>确认密码:</label></td>
                 <td class="width-35">
                 <input id="confirmNewPassword" name="confirmNewPassword" type="password" class="form-control required" value="" maxlength="50" minlength="${pwsmin }"
                 class="form-control ${empty student.id?'required':''} ${complex ? 'specialChar beginChar pwsChar':''}" rangelength="[${pwsmin },20]" equalto="#newPassword" aria-required="true"></td>
		      </tr>
	      </tbody>
	      </table>
	</form:form>
</body>
</html>