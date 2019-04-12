<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>学生管理</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/common/extendField.js" type="text/javascript"></script>
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
// 			$("#no").focus();
			validateForm = $("#inputForm").validate({
				focusInvalid: false,
				onkeyup: false,
				rules: {
					loginName: {remote: "${ctx}/sys/user/checkLoginName?uid=${student.id}&${student.loginName}"},
					idNo: {remote: "${ctx}/sys/user/checkLoginName?uid=${student.id}&${student.idNo}"},
					email: {remote: "${ctx}/sys/user/checkLoginName?uid=${student.id}&${student.email}"},
					no: {remote: "${ctx}/sys/user/checkLoginName?uid=${student.id}&${student.no}"},
					mobile: {remote: "${ctx}/sys/user/checkLoginName?uid=${student.id}&${student.mobile}"}
				},
				messages: {
					loginName: {remote: "用户登录名已存在"},
					idNo:{remote: "证件号已存在"},
					email:{remote: "邮箱已存在"},
					no:{remote: "学号已存在"},
					mobile:{remote: "手机号已存在"},
					confirmNewPassword: {equalTo: "输入与上面相同的密码"}
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
			$("#inputForm").validate().element($("#loginName"));
			$("#inputForm").validate().element($("#idNo"));
			$("#inputForm").validate().element($("#email"));
			$("#inputForm").validate().element($("#no"));
			$("#inputForm").validate().element($("#mobile"));
			
			/**
			* 初始化扩展字段
			*/
			extendField.initExtendFieldTable("STUDENT", $("#id").val(), '${ctx}/fieldconfig/userExtendInfo/usablefieldList',"extendInfoDiv",2);
		});
	</script>
</head>
<body class="hideScroll">
	<form:form id="inputForm" modelAttribute="student" action="${ctx}/account/student/save" method="post" class="form-horizontal">
		<form:hidden id="id" path="id"/>
		<sys:message content="${message}"/>
		<div class="panel panel-primary">
			<div class="panel-heading">
				<i class="fa fa-rss-square"></i>基础字段
			</div>		
			<div class="panel-body">
				<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
				   <tbody>
				      <tr>
                         <td class="active"><label class="pull-right"><font color="red">*</font>登录账号:</label></td>
                         <td><input id="oldLoginName" name="oldLoginName" type="hidden" value="${student.loginName}">
                         <form:input path="loginName" htmlEscape="false" maxlength="50" class="form-control required specialChar beginChar" rangelength="[6,20]" /></td>
                         <td class="active"><label class="pull-right"><font color="red">*</font>别名:</label></td>
                         <td>
                             <form:input path="account" htmlEscape="false" maxlength="50" class="form-control required userName specialChar beginChar" rangelength="[6,20]"/></td>
                      </tr>
                       <c:if test="${empty student.id}"> <tr>
                         <td class="active"><label class="pull-right"><c:if test="${empty student.id}"><font color="red">*</font></c:if>密码:</label></td>
                         <td><input id="newPassword" name="newPassword" type="password" value="" maxlength="50" minlength="${pwsmin }" 
                         			class="form-control ${empty student.id?'required':''} ${complex ? 'specialChar beginChar pwsChar':''}" rangelength="[${pwsmin },20]"/>
                           </td>
                         <td class="active"><label class="pull-right"><c:if test="${empty student.id}"><font color="red">*</font></c:if>确认密码:</label></td>
                         <td><input id="confirmNewPassword" name="confirmNewPassword" type="password"  
                         	class="form-control ${empty student.id?'required':''} ${complex ? 'specialChar beginChar pwsChar':''}" rangelength="[${pwsmin },20]" value="" maxlength="50" minlength="${pwsmin }" equalTo="#newPassword"/></td>
                      </tr></c:if>
                      <tr>
                         <td class="active"><label class="pull-right"><font color="red">*</font>姓名:</label></td>
                         <td><form:input path="name" htmlEscape="false" maxlength="50" class="form-control required"/></td>
                         <td class="active"><label class="pull-right"><font color="red">*</font>证件号:</label></td>
                         <td>
                             <form:input path="idNo" htmlEscape="false" maxlength="32" class="form-control required userName isIdCardNo"/></td>
                      </tr>
                      <tr>
                         <td class="width-15"  class="active"> <label class="pull-right"><font color="red">*</font>归属学校:</label></td>
                         <td class="width-35">
                         <input type="hidden" name="company.id" value="${student.company.id}"/>
                         <form:input path="company.name" htmlEscape="false" maxlength="100" class="form-control" readonly="true"/></td>
                         
                         <td class="width-15" class="active"><label class="pull-right"><font color="red">*</font>组织机构:</label></td>
                         <td class="width-35"><sys:treeselect id="office" name="office.id" value="${student.office.id}" labelName="office.name" labelValue="${student.office.name}"
                            title="部门" url="/sys/office/treeData2?type=2" cssClass="form-control required" notAllowSelectParent="true"/></td>
                      </tr>
                      <tr>
                         <td class="active"><label class="pull-right"><font color="red">*</font>手机:</label></td>
                         <td><form:input path="mobile" htmlEscape="false" maxlength="100" class="form-control required isMobile"/></td>
                          <td class="active"><label class="pull-right"><font color="red">*</font>邮箱:</label></td>
                         <td><form:input path="email" htmlEscape="false" maxlength="100" class="form-control email required"/></td>
                      </tr>
                      <tr>
                         <td class="active"><label class="pull-right"><font color="red">*</font>学号:</label></td>
                         <td><form:input path="no" htmlEscape="false" maxlength="50" class="form-control required"/></td>
                         <td class="active"><label class="pull-right">是否允许登录:</label></td>
                         <td><form:select path="loginFlag"  class="form-control">
                            <form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                        </form:select></td>
                         
                      </tr>
                      <tr>
                         <td class="active"><label class="pull-right">默认角色:</label></td>
                         <td>
                         <input type="hidden" id="roleType" name="roleType" value="${student.roleType }"/>
                         <form:input path="roleTypeName" htmlEscape="false" maxlength="100" class="form-control" readonly="true"/></td>
                         <td class="active"><label class="pull-right">其他角色:</label></td>
                         <td>
                         <input id="roleIds" name="roleIds" class="form-control" type="hidden" value="${student.roleIds}">
    <div class="input-group">
        <input id="roleNames" name="roleNames" readonly="readonly" type="text" value="${student.roleNames}" class="form-control valid" style=""  aria-invalid="false">
             <span class="input-group-btn">
                 <button type="button" id="roleButton" class="btn   btn-primary  "><i class="fa fa-search"></i>
                 </button> 
             </span>
            
    </div>
     <label id="roleName-error" class="error" for="roleNames" style="display:none"></label>
<script type="text/javascript">
    $("#roleButton, #roleNames").click(function(){
        // 是否限制选择，如果限制，设置为disabled
        if ($("#roleButton").hasClass("disabled")){
            return true;
        }
        var url="${ctx}/account/common/roleTree?id=";
        if($("#id").val()){
        	url+=$("#id").val();
        }
        url+="&roleType="+$("#roleType").val();
        // 正常打开 
        top.layer.open({
            type: 2, 
            area: ['300px', '420px'],
            title:"选择角色",
            //ajaxData:{selectIds: $("#officeId").val()},
            content: url ,
            btn: ['确定', '关闭']
               ,yes: function(index, layero){ //或者使用btn1
                        var tree = layero.find("iframe")[0].contentWindow.tree;//h.find("iframe").contents();
                        var ids = [], names = [], nodes = [];
                        nodes = tree.getCheckedNodes(true);
                        for(var i=0; i<nodes.length; i++) {//
                            ids.push(nodes[i].id);
                            names.push(nodes[i].name);//
                        }
                        //console.log(ids);
                        //console.log(names);
                        $("#roleIds").val(ids.join(",").replace(/u_/ig,""));
                        $("#roleNames").val(names.join(","));
                        $("#roleNames").focus();
                        top.layer.close(index);
                               },
        cancel: function(index){ //或者使用btn2
                   //按钮【按钮二】的回调
               }
        }); 
    
    });
</script>
                         </td>
                      </tr>
                      <tr>
                         <td class="active"><label class="pull-right">用户组:</label></td>
                         <td>
                         <input id="groupIds" name="groupIds" class="form-control" type="hidden" value="${student.groupIds}">
    <div class="input-group">
        <input id="groupNames" name="groupNames" readonly="readonly" type="text" value="${student.groupNames}" class="form-control valid" style=""  aria-invalid="false">
             <span class="input-group-btn">
                 <button type="button" id="groupButton" class="btn   btn-primary  "><i class="fa fa-search"></i>
                 </button> 
             </span>
            
    </div>
     <label id="groupName-error" class="error" for="roleNames" style="display:none"></label>
<script type="text/javascript">
    $("#groupButton, #groupNames").click(function(){
        // 是否限制选择，如果限制，设置为disabled
        if ($("#groupButton").hasClass("disabled")){
            return true;
        }
        var url="${ctx}/account/common/groups?id=";
        if($("#id").val()){
            url+=$("#id").val();
        }
        // 正常打开 
        top.layer.open({
            type: 2, 
            area: ['300px', '420px'],
            title:"选择用户组",
            //ajaxData:{selectIds: $("#officeId").val()},
            content: url ,
            btn: ['确定', '关闭']
               ,yes: function(index, layero){ //或者使用btn1
            	   var groupUl = layero.find("iframe")[0].contentWindow.groupUl;
                   var ids = [], names = [];
                   var groups=groupUl.find(".group");
                   for(var i=0; i<groups.length; i++) {
                       var group=$(groups[i]);
                       if(group.is(':checked')){
                           ids.push(group.attr("groupId"));
                           names.push(group.attr("groupName"));
                       }
                   }
                   $("#groupIds").val(ids.join(",").replace(/u_/ig,""));
                   $("#groupNames").val(names.join(","));
                   $("#groupNames").focus();
                   top.layer.close(index);
                               },
        cancel: function(index){ //或者使用btn2
                   //按钮【按钮二】的回调
               }
        }); 
    
    });
</script>
                         </td>
                         <td><label class="pull-right">数据来源:</label></td>
                         <td><input type="hidden" id="origin" name="origin" value="${student.origin }"/>
                         <form:input path="originName" htmlEscape="false" maxlength="100" class="form-control" readonly="true"/></td>
                      </tr>
				      
				      <tr>
				      <td><label class="pull-right">性别:</label></td>
                         <td><form:select path="gender" class="form-control">
                    <form:options items="${fns:getDictList('sex')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select></td>
						<td><label class="pull-right">民族:</label></td>
                         <td>
                         <form:select path="nation" class="form-control">
                    <form:options items="${fns:getDictList('nation')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                         </td>
				      </tr>
				      
				       <tr>
				         
				         <td class="active">
					         <label class="pull-right">
					         	备注:
					         </label>
				         </td>
				         <td>
				         		<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="form-control"/>
				         </td>
				      </tr>

					</tbody>
				</table>  
			</div>
		</div>
		 <div id="extendInfoDiv">
		
		</div> 		 
	</form:form>
</body>
</html>