<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>扩展字段管理</title>
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
		<h5>扩展字段列表 </h5>
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
	<form:form id="searchForm" modelAttribute="fieldConfig" action="${ctx}/fieldconfig/fieldConfig/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>字段名称：</span>
				<form:input path="fieldName" htmlEscape="false" maxlength="50" class=" form-control input-sm"/>
			<span>数据类型：</span>
				<form:select path="dataType" class="form-control ">
					<option value="">--请选择--</option>
					<form:options items="${fns:getDictList('data_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
		</div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="fieldconfig:fieldConfig:add">
				<table:addRow url="${ctx}/fieldconfig/fieldConfig/form" title="扩展字段" width="400px;" height="650px;"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="fieldconfig:fieldConfig:edit">
			    <table:editRow url="${ctx}/fieldconfig/fieldConfig/form" title="扩展字段" id="contentTable" width="400px;" height="650px;"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="fieldconfig:fieldConfig:del">
				<table:delRow url="${ctx}/fieldconfig/fieldConfig/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="fieldconfig:fieldConfig:import">
				<table:importExcel url="${ctx}/fieldconfig/fieldConfig/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="fieldconfig:fieldConfig:export">
	       		<table:exportExcel url="${ctx}/fieldconfig/fieldConfig/export"></table:exportExcel><!-- 导出按钮 -->
	       	</shiro:hasPermission>
	       <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
		
			</div>
		<div class="pull-right">
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
		</div>
	</div>
	</div>
	
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th> <input type="checkbox" class="i-checks"></th>				
				<th  class="sort-column fieldName">字段名称</th>
				<th  class="sort-column fieldCName">字段中文名</th>				
				<th  class="sort-column dataType">数据类型</th>				
				<th  class="sort-column isNecessary">是否必填</th>
				<th  class="sort-column isModify">是否可修改</th>
				<th  class="sort-column isUsable">是否可用</th>			
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="fieldConfig">
			<tr>
				<td> <input type="checkbox" id="${fieldConfig.id}" class="i-checks"></td>				
				<td>
					${fieldConfig.fieldName}
				</td>
				<td>
					${fieldConfig.fieldCName}
				</td>				
				<td>
					${fns:getDictLabel(fieldConfig.dataType, 'data_type', '')}					
				</td>				
				<td>
					${fns:getDictLabel(fieldConfig.isNecessary, 'yes_no', '')}
				</td>
				<td>
					${fns:getDictLabel(fieldConfig.isModify, 'yes_no', '')}				
				</td>	
				<td>
					${fns:getDictLabel(fieldConfig.isUsable, 'yes_no', '')}				
				</td>			
				<td>
					<shiro:hasPermission name="fieldconfig:fieldConfig:view">
						<a href="#" onclick="openDialogView('查看扩展字段', '${ctx}/fieldconfig/fieldConfig/form?id=${fieldConfig.id}','400px', '650px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="fieldconfig:fieldConfig:edit">
    					<a href="#" onclick="openDialog('修改扩展字段', '${ctx}/fieldconfig/fieldConfig/form?id=${fieldConfig.id}','400px', '650px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="fieldconfig:fieldConfig:del">
						<a href="${ctx}/fieldconfig/fieldConfig/delete?id=${fieldConfig.id}" onclick="return confirmx('确认要删除该扩展字段吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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