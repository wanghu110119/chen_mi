<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title></title>
	<meta name="decorator" content="default"/>
</head>
<body>
	<div class="wrapper wrapper-content">
    <sys:message content="${message}"/>
		<!-- 查询条件 -->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="" action="" method="post" class="form-inline">
		<div class="form-group">
			<span>${groupName}</span>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th class="sort-column ">用户组名称</th>
				<th class="sort-column ">描述</th>
				<th class="sort-column ">备注</th>
				<th class="sort-column ">操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${list}" var="identityGroup">
			<tr identityGroupid="${identityGroup.id}">
				<td>${identityGroup.groupName}</td>
				<td>${identityGroup.description}</td>
				<td>${identityGroup.remarks}</td>
				<td>
				    <shiro:hasPermission name="unifiedAuth:identityGroup:save">
                        <a href="${ctx}/unifiedAuth/identityGroup/list?id=${identityGroup.id}&groupName=${identityGroup.groupName}" class="btn btn-info btn-xs" >授权</a>
                    </shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
	</div>
	<script type="text/javascript">
	   function gotoAuth(id,name){
		   window.location="${ctx}/unifiedAuth/identityGroup/list?id="+id+"&groupName="+name
	   }
	   
	</script>
</body>
</html>