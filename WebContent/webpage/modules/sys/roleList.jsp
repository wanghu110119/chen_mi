<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>角色管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/webpage/include/treetable.jsp" %>
	<script type="text/javascript">
		//$(document).ready(function() {
		//	$("#treeTable").treeTable({expandLevel : 5});
		//});
		function refresh(){//刷新或者排序，页码不清零
			window.location="${ctx}/sys/role/list";
    	}
	</script>
</head>
<body>
	<div class="wrapper wrapper-content">
	<sys:message content="${message}"/>
	<div class="row">
    <div class="col-sm-12">
    <form:form id="searchForm" modelAttribute="role" action="${ctx}/sys/role/list" method="post" class="form-inline">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <form:input path="parent.id" htmlEscape="false" type="hidden" />
        <form:input path="parentIds" htmlEscape="false" type="hidden" />
        <table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
    </form:form>
    </div>
    </div>
		<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="sys:office:add">
				<table:addRow url="${ctx}/sys/role/form?parent.id=0" title="岗位" width="800px" height="500px" ></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
	        <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
			</div>
	</div>
	</div>
	<table id="treeTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead><tr>
		<th>角色名称</th>
		<th>角色编码</th>
		<th>备注</th>
		<shiro:hasPermission name="sys:role:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody id="treeTableList"><c:forEach items="${page.list}" var="row">
		  <tr id="${row.id }" pId="${row.parent.id }">
            <td><a  href="#" onclick="openDialogView('查看角色', '${ctx}/sys/role/form?id=${row.id}','800px', '500px')">${row.name}</a></td>
            <td>${row.enname}</td>
            <td>${row.remarks}</td>
            <td>
                <shiro:hasPermission name="sys:role:view">
                        <a href="#" onclick="openDialogView('查看角色', '${ctx}/sys/role/form?id=${row.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="sys:role:edit"> 
                        <c:if test="${(role.sysData eq fns:getDictValue('是', 'yes_no', '1') && fns:getUser().admin)||!(role.sysData eq fns:getDictValue('是', 'yes_no', '1'))}">
                            <a href="#" onclick="openDialog('修改角色', '${ctx}/sys/role/form?id=${row.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
                        </c:if>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="sys:role:del"> 
                        <a href="${ctx}/sys/role/delete?id=${row.id}" onclick="return confirmxParent('确认要删除该角色吗？', this.href,true)" class="btn  btn-danger btn-xs" target="_parent"><i class="fa fa-trash"></i> 删除</a>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="sys:role:assign"> 
                        <a href="#" onclick="openDialogView('项目权限设置', '${ctx}/sys/role/sysProject?id=${row.id}','350px', '500px')" class="btn btn-primary btn-xs" ><i class="fa fa-edit"></i> 权限设置</a> 
                        </shiro:hasPermission>
                        <shiro:hasPermission name="sys:role:assign"> 
                        <a href="#" onclick="openDialogView('分配用户', '${ctx}/sys/role/assign?id=${row.id}','800px', '600px')"  class="btn  btn-warning btn-xs" ><i class="glyphicon glyphicon-plus"></i> 分配用户</a>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="sys:role:add">
		                    <a href="#" onclick="openDialog('添加下级角色', '${ctx}/sys/role/form?parent.id=${row.id}','800px', '500px')" class="btn  btn-primary btn-xs"><i class="fa fa-plus"></i> 添加下级角色</a>
		                </shiro:hasPermission>
                        </td>
            </td>
        </tr></c:forEach>
		</tbody>
	</table>
	<table:page page="${page}"></table:page>
	</div>
</body>
</html>