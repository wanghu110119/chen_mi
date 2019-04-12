<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>字段配置管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {			
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>字段配置列表</h5>
		<div class="ibox-tools">
			<a class="collapse-link">
				<i class="fa fa-chevron-up"></i>
			</a>
		</div>
	</div>
    
    <div class="ibox-content">
	<sys:message content="${message}"/>
	
	<!--查询条件-->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="fieldGroup" action="${ctx}/fieldconfig/fieldGroup/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="fieldconfig:fieldGroup:add">
				<table:addRow url="${ctx}/fieldconfig/fieldGroup/form" title="字段分组" width="500px;" height="650px;"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="fieldconfig:fieldGroup:edit">
			    <table:editRow url="${ctx}/fieldconfig/fieldGroup/form" title="字段分组" width="500px;" height="650px;" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="fieldconfig:fieldGroup:del">
				<table:delRow url="${ctx}/fieldconfig/fieldGroup/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="fieldconfig:fieldGroup:import">
				<table:importExcel url="${ctx}/fieldconfig/fieldGroup/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="fieldconfig:fieldGroup:export">
	       		<table:exportExcel url="${ctx}/fieldconfig/fieldGroup/export"></table:exportExcel><!-- 导出按钮 -->
	       	</shiro:hasPermission>
	       <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
		
			</div>
<!-- 		<div class="pull-right"> -->
<!-- 			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button> -->
<!-- 			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button> -->
<!-- 		</div> -->
	</div>
	</div>
	
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th> <input type="checkbox" class="i-checks"></th>
				<th  class="sort-column groupName">分组英文名</th>
				<th  class="sort-column groupCName">分组中文名</th>
				<th  class="sort-column groupType">分组类型</th>
				<th  class="sort-column groupRole">所属角色</th>				
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="fieldGroup">
			<tr>
				<td> <input type="checkbox" id="${fieldGroup.id}" class="i-checks"></td>				
				<td>
					${fieldGroup.groupName}
				</td>
				<td>
					${fieldGroup.groupCName}
				</td>
				<td>
					<c:forEach items="${groupTypes }" var="groupTypeDict">				
						<c:if test="${fieldGroup.groupType == groupTypeDict.value }">
							${ groupTypeDict.label }
						</c:if> 
					</c:forEach>
				</td>
				<td>					
					 <c:forEach items="${groupRoles}" var="groupRoleDict">					 	
						<c:if test="${fieldGroup.groupRole == groupRoleDict.value }">							
							${ groupRoleDict.label }
						</c:if>
					</c:forEach> 
				</td>
				<td>
					<shiro:hasPermission name="fieldconfig:fieldGroup:view">
						<a href="#" onclick="openDialogView('查看字段分组', '${ctx}/fieldconfig/fieldGroup/form?id=${fieldGroup.id}','500px', '650px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="fieldconfig:fieldGroup:edit">
    					<a href="#" onclick="openDialog('修改字段分组', '${ctx}/fieldconfig/fieldGroup/form?id=${fieldGroup.id}','500px', '650px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="fieldconfig:fieldGroup:del">
						<a href="${ctx}/fieldconfig/fieldGroup/delete?id=${fieldGroup.id}" onclick="return confirmx('确认要删除该字段分组吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
					</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
		<!-- 分页代码 -->
	<table:page page="${page}"></table:page>
	<br/>
	<br/>
	</div>
	</div>
</div>
</body>
</html>