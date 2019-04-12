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
			<span>${officeName }</span>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th class="sort-column ">机构名称</th>
				<th class="sort-column ">编号</th>
				<th class="sort-column ">上级机构</th>
				<th class="sort-column ">操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${offices}" var="office">
			<tr userid="${office.id}">
				<td>${office.name}</td>
				<td>${office.code}</td>
				<td>${office.parent.name}</td>
				<td>
				    <shiro:hasPermission name="unifiedAuth:office:save">
                        <a href="${ctx}/unifiedAuth/office/list?office.id=${office.id}&office.name=${office.name}" class="btn btn-info btn-xs" >授权</a>
                    </shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
	</div>
</body>
</html>