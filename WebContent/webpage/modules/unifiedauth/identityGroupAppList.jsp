]<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>用户组授权（应用）</title>
	<meta name="decorator" content="default"/>
</head>
<body>
	<div class="wrapper wrapper-content ">
	<sys:message content="${message}"/>
	
	<div class="row">
	<div class="col-sm-12">
		<div class="form-group">
			<span>${identityGroup.groupName }</span>
			<input type="hidden" id="identityGroupId" value="${identityGroup.id}" />
			<input type="hidden" id="identityGroupName" value="${identityGroup.groupName}" />
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
					<c:forEach items="${list}" var="identityGroupAuth">
						<tr appid="${identityGroupAuth.apply.id}">
							<td>
<%-- 								<a  href="#" onclick="openDialogView('查看应用', '${ctx}/sys/role/form?id=${roleAuth.apply.id}','800px', '500px')"> --%>
									${identityGroupAuth.apply.name}
<!-- 								</a> -->
							</td>
							<td>
								<input class="i-checks accessAuth"  name="authAccess" type="checkbox" ${empty identityGroupAuth.accessAuth ?"":"checked='checked'"} />
								<span>访问权限</span>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		
		<shiro:hasPermission name="unifiedAuth:identityGroup:save">
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
					   identityGroupId:$("#identityGroupId").val(),
					   applyId:tr.attr("appid"),
					   accessAuth:"1"
				   });
			   }
		   }
	        $.ajax({
	            type: "POST",
	            url: "${ctx}/unifiedAuth/identityGroup/save",
	            data : {
	            	authForm:JSON.stringify(list),
	            	id:$("#identityGroupId").val(),
	            },
	            dataType: "JSON",
	            success: function(data){
	            	 if(data.success){
	                	  layer.msg(data.msg, {
	                          icon : 1,
	                          time : 2000
	                      });
	                  }else{
	                	  layer.msg(data.msg, {
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