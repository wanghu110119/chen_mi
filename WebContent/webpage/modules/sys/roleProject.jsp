<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>角色项目管理</title>
	<meta name="decorator" content="default"/>
	<style type="text/css">
		.index {
			padding: 5px;
		}
		.list-group a:hover{
			background-color: #1ab394;
		    border-color: #1ab394;
		    color: #FFFFFF;
		    z-index: 2;
		}
		.list-group span{
			float: right;
		}
	</style>
</head>
<body>
	<div class="index">
		<div class="list-group">
			<c:forEach items="${splist}" var="item">  
			  <a href="#" onclick="openDialog('权限设置', '${ctx}/sys/role/auth?id=${role.id}&sprId=${item.id}','350px', '500px')" class="list-group-item">${item.name} 
			  <span><i class="fa fa-angle-double-right"></i></span></a>
			</c:forEach>
		</div>
	</div>
</body>
</html>