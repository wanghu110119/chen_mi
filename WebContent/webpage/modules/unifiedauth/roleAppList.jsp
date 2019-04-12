]<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>角色授权（应用）</title>
	<meta name="decorator" content="default"/>
</head>
<body>
	<div class="wrapper wrapper-content ">
	<sys:message content="${message}"/>
	
	<div class="row">
	<div class="col-sm-12">
		<div class="form-group">
			<span>${role.name }</span>
			<input type="hidden" id="roleId" value="${role.id}" />
			<input type="hidden" id="roleName" value="${role.name}" />
		</div>	
	<br/>
	</div>
	</div>	
			<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
				<thead>
					<tr>
						<th class="sort-column login_name">应用名称</th>
						<th class="sort-column name">系统配置</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${list}" var="roleAuth">
						<tr appid="${roleAuth.apply.id}">
							<td>
<%-- 								<a  href="#" onclick="openDialogView('查看应用', '${ctx}/sys/role/form?id=${roleAuth.apply.id}','800px', '500px')"> --%>
									${roleAuth.apply.name}
<!-- 								</a> -->
							</td>
							<td>
							<c:if
								test="${not empty roleAuth.isEdit and roleAuth.isEdit eq '2'}">
								<span><input type="checkbox" isEdit="${roleAuth.isEdit}" class="i-checks accessAuth"
									${roleAuth.closeType eq '1' ? '':'checked=checked'}>访问权限</span>
							</c:if> 
							<c:if
								test="${not empty roleAuth.accessAuth and empty roleAuth.isEdit or roleAuth.isEdit eq '1'}">
								<input type="checkbox" class="i-checks accessAuth"
									 checked="checked">访问权限</c:if> <c:if
								test="${empty roleAuth.accessAuth and empty roleAuth.isEdit or roleAuth.isEdit eq '1'}">
								<input type="checkbox" class="i-checks accessAuth">访问权限</c:if> <%-- 								<input class="i-checks accessAuth"  name="authAccess" type="checkbox" ${empty roleAuth.accessAuth ?"":"checked='checked'"} /> --%> <!-- 								<span>访问权限</span> -->
						</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		
		<shiro:hasPermission name="unifiedAuth:role:save">
			<c:if test="${ not empty list}">
				<button class="btn btn-primary  btn-sm " onclick="save()">提交</button>
			</c:if>
		</shiro:hasPermission>
	</div>
<script type="text/javascript">
	function save(){
		var trs=$("#contentTable tbody").find("tr");
		   var list=[];
		   for(var i=0;i<trs.length;i++){
			   var tr=$(trs[i]);
			   var accessAuth=tr.find(".accessAuth").is(':checked');
			   if(accessAuth){
				   list.push({
					   roleId:$("#roleId").val(),
					   applyId:tr.attr("appid"),
					   accessAuth:"1"
				   });
			   }
		   }
	        $.ajax({
	            type: "POST",
	            url: "${ctx}/unifiedAuth/role/save",
	            data : {
	            	authRoleForm:JSON.stringify(list),
	            	id:$("#roleId").val(),
	            },
	            dataType: "JSON",
	            success: function(data){
	            	 if(data.success){
	                	  layer.msg('保存成功', {
	                          icon : 1,
	                          time : 2000
	                      });
	                  }else{
	                	  layer.msg('保存出错', {
	                          icon : 5,
	                          time : 2000
	                      });
	                  }
	            }
	        });
		}
	</script>
</body>
</html>