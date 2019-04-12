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
	<form:form id="searchForm" modelAttribute="user" action="" method="post" class="form-inline">
		<div class="form-group">
			<span>${postName}</span>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th class="sort-column ">岗位名称</th>
				<th class="sort-column ">岗位代码</th>
				<th class="sort-column ">备注</th>
				<th class="sort-column ">操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${list}" var="post">
			<tr roleid="${post.id}">
				<td>${post.name}</td>
				<td>${post.code}</td>
				<td>${post.remarks}</td>
				<td>
				    <shiro:hasPermission name="unifiedAuth:post:save">
                        <a href="${ctx}/unifiedAuth/post/list?id=${post.id}&name=${post.name}" class="btn btn-info btn-xs" >授权</a>
                    </shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
	</div>
	<script type="text/javascript">
	   function gotoAuth(id,name){
		   window.location="${ctx}/unifiedAuth/post/list?id="+id+"&name="+name
	   }
	   
	</script>
</body>
</html>