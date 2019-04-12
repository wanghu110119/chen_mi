<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>用户管理</title>
	<meta name="decorator" content="default"/>
</head>
<body>
	<div class="wrapper wrapper-content">
    <sys:message content="${message}"/>
		<!-- 查询条件 -->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="user" action="" method="post" class="form-inline">
		<div class="form-group">
			<span>${userName }</span>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th class="sort-column ">姓名</th>
				<th class="sort-column ">登录名</th>
				<th class="sort-column ">组织机构</th>
				<th class="sort-column ">操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${users}" var="user">
			<tr userid="${user.id}">
				<td>${user.name}</td>
				<td>${user.loginName}</td>
				<td>${user.office.name}</td>
				<td>
				    <shiro:hasPermission name="unifiedAuth:user:save">
                        <a href="${ctx}/unifiedAuth/user/list?user.id=${user.id}&user.name=${user.name}" class="btn btn-info btn-xs" >授权</a>
                    </shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
	</div>
	<script type="text/javascript">
	   function gotoAuth(id,name){
		   window.location="${ctx}/unifiedAuth/user/list?user.id="+id+"&user.name="+name;
	   }
	   
	</script>
</body>
</html>