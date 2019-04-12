<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>岗位管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/webpage/include/treetable.jsp" %>
	<script type="text/javascript">
		function refresh(){//刷新或者排序，页码不清零
			window.location="${ctx}/sys/post/list";
    	}
		//parent.refreshTree();
	</script>
</head>
<body>
	<div class="wrapper wrapper-content">
	<sys:message content="${message}"/>
	<div class="row">
    <div class="col-sm-12">
    <form:form id="searchForm" modelAttribute="user" action="${ctx}/sys/post/list" method="post" class="form-inline">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
    </form:form>
    </div>
    </div>
		<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="sys:office:add">
				<table:addRow url="${ctx}/sys/post/form?parent.id=0" title="岗位" width="500px" height="400px" ></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
	        <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
			</div>
	</div>
	</div>
	<table id="treeTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead><tr>
		<th>岗位名称</th>
		<th>岗位编码</th>
		<th>备注</th>
		<shiro:hasPermission name="sys:post:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody id="treeTableList"><c:forEach items="${page.list}" var="row">
		  <tr id="${row.id }" pId="${row.parent.id }">
            <td><a  href="#" onclick="openDialogView('查看岗位', '${ctx}/sys/post/form?id=${row.id}','500px', '400px')">${row.name}</a></td>
            <td>${row.code}</td>
            <td>${row.remarks}</td>
            <td>
                <shiro:hasPermission name="sys:post:view">
                    <a href="#" onclick="openDialogView('查看岗位', '${ctx}/sys/post/form?id=${row.id}','800px', '620px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:post:edit">
                    <a href="#" onclick="openDialog('修改岗位', '${ctx}/sys/post/form?id=${row.id}','500px', '400px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:post:del">
                    <a href="${ctx}/sys/post/delete?id=${row.id}" onclick="return confirmxParent('要删除该岗位及所有子岗位项吗？', this.href,true)" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:post:add">
                    <a href="#" onclick="openDialog('添加下级岗位', '${ctx}/sys/post/form?parent.id=${row.id}','500px', '400px')" class="btn  btn-primary btn-xs"><i class="fa fa-plus"></i> 添加下级岗位</a>
                </shiro:hasPermission>
            </td>
        </tr></c:forEach>
		</tbody>
	</table>
	<table:page page="${page}"></table:page>
	</div>
	<script type="text/template" id="treeTableTpl">
		<tr id="{{row.id}}" pId="{{pid}}">
			<td><a  href="#" onclick="openDialogView('查看机构', '${ctx}/sys/office/form?id={{row.id}}','800px', '620px')">{{row.name}}</a></td>
			<td>{{row.code}}</td>
			<td>{{row.remarks}}</td>
			<td>
				<shiro:hasPermission name="sys:post:view">
					<a href="#" onclick="openDialogView('查看机构', '${ctx}/sys/office/form?id={{row.id}}','800px', '620px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="sys:post:edit">
					<a href="#" onclick="openDialog('修改机构', '${ctx}/sys/office/form?id={{row.id}}','800px', '620px', 'officeContent')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="sys:post:del">
					<a href="${ctx}/sys/office/delete?id={{row.id}}" onclick="return confirmx('要删除该机构及所有子机构项吗？', this.href)" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="sys:post:add">
					<a href="#" onclick="openDialog('添加下级机构', '${ctx}/sys/office/form?parent.id={{row.id}}','800px', '620px', 'officeContent')" class="btn  btn-primary btn-xs"><i class="fa fa-plus"></i> 添加下级机构</a>
				</shiro:hasPermission>
			</td>
		</tr>
	</script>
</body>
</html>