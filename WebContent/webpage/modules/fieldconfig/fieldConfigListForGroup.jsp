<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>扩展字段管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//勾选选中的字段
			var fieldConfigIds = "${fieldConfigIds}";
			if(fieldConfigIds){
				fieldConfigIds = fieldConfigIds.split(",");
				for(var i = 0 ; i < fieldConfigIds.length ; i++){
					$('#'+fieldConfigIds[i]).iCheck('check');
				}
			}
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
	<form:form id="searchForm" modelAttribute="fieldConfig" 
		action="${ctx}/fieldconfig/fieldConfig/usableList?fieldConfigIds=${fieldConfigIds}&groupId=${groupId}&groupRole=${groupRole}" method="post" class="form-inline">
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
				<th></th>				
				<th  class="sort-column fieldName">字段名称</th>
				<th  class="sort-column fieldCName">字段中文名</th>				
				<th  class="sort-column dataType">数据类型</th>				
				<th  class="sort-column isNecessary">是否必填</th>
				<th  class="sort-column isModify">是否可修改</th>	
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${fieldConfigs}" var="fieldConfig">
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
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<br/>
	<br/>
	</div>
	</div>
</div>
</body>
</html>