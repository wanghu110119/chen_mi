<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>表单配置</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		    $('#contentTable thead tr th input.i-checks').on('ifChecked', function(event){ //ifCreated 事件应该在插件初始化之前绑定 
		    	  $('#contentTable tbody tr td input.i-checks').iCheck('check');
		    	});
	
		    $('#contentTable thead tr th input.i-checks').on('ifUnchecked', function(event){ //ifCreated 事件应该在插件初始化之前绑定 
		    	  $('#contentTable tbody tr td input.i-checks').iCheck('uncheck');
		    	});
		    
		});
		function genCode(){

			// var url = $(this).attr('data-url');
			  var size = $("#contentTable tbody tr td input.i-checks:checked").size();
			  if(size == 0 ){
					top.layer.alert('请至少选择一条数据!', {icon: 0, title:'警告'});
					return;
				  }

			  if(size > 1 ){
					top.layer.alert('只能选择一条数据!', {icon: 0, title:'警告'});
					return;
				  }
			    var id =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("id");
			    var tableType =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").closest("td").next().text();
			    if(tableType.indexOf("附表")>=0){
			    	top.layer.alert('不能选择附表生成代码，请选择主表!', {icon: 0, title:'警告'});
					return;

			    }
			    var isSync = $("#contentTable tbody tr td input.i-checks:checkbox:checked").closest("td").next().next().next().next().next().next().text();
			    if(isSync.indexOf("未同步")>=0){
			    	top.layer.alert('请先同步数据库!', {icon: 0, title:'警告'});
					return;
			    }
			    openDialog('生成代码','${ctx}/gen/genScheme/form?genTable.id='+id,'800px', '500px');
			}
		function createMenu(){

			// var url = $(this).attr('data-url');
			  var size = $("#contentTable tbody tr td input.i-checks:checked").size();
			  if(size == 0 ){
					top.layer.alert('请至少选择一条数据!', {icon: 0, title:'警告'});
					return;
				  }

			  if(size > 1 ){
					top.layer.alert('只能选择一条数据!', {icon: 0, title:'警告'});
					return;
				  }
			    var id =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("id");
			    var tableType =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").closest("td").next().text();
			    if(tableType.indexOf("附表")>=0){

			    	top.layer.alert('不能选择附表创建菜单，请选择主表!', {icon: 0, title:'警告'});
					return;

			    }
			    var isSync = $("#contentTable tbody tr td input.i-checks:checkbox:checked").closest("td").next().next().next().next().next().next().text();
			    if(isSync.indexOf("未同步")>=0){

			    	top.layer.alert('请先同步数据库!', {icon: 0, title:'警告'});
					return;

			    }
			    openDialog('创建菜单','${ctx}/gen/genScheme/menuForm?gen_table_id='+id,'800px', '500px');
			}
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
			<h5>表单列表 </h5>
			<div class="ibox-tools">
				<a class="collapse-link">
					<i class="fa fa-chevron-up"></i>
				</a>
				<a class="dropdown-toggle" data-toggle="dropdown" href="form_basic.html#">
					<i class="fa fa-wrench"></i>
				</a>
				<ul class="dropdown-menu dropdown-user">
					<li><a href="#">选项1</a>
					</li>
					<li><a href="#">选项2</a>
					</li>
				</ul>
				<a class="close-link">
					<i class="fa fa-times"></i>
				</a>
			</div>
	</div>
    <div class="ibox-content">
	<sys:message content="${message}"/>
	
		<!-- 查询条件 -->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="genTable" action="${ctx}/gen/genTable/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>表名：</span>
				<form:input path="nameLike" value="${genTable.nameLike}"  htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
			<span>说明：</span>
				<form:input path="comments" value="${genTable.comments}"  htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
			<span>父表表名：</span>
				<form:input path="parentTable" value="${genTable.parentTable}"  htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="gen:genTable:add">
				<button class="btn btn-white btn-sm" data-toggle="tooltip" data-placement="left" onclick="openDialog('新增业务表','${ctx}/gen/genTable/form','80%','80%')" title="添加"><i class="fa fa-plus"></i>添加</button>
			</shiro:hasPermission>
			<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" title="导入" onclick="openDialog('导入表单','${ctx}/gen/genTable/importTableFromDB','700px', '300px')"><i class="fa fa-folder-open-o"></i>数据库导入表单</button>
			<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" title="生成代码" onclick="genCode()"><i class="fa fa-folder-open-o"></i> 生成代码</button>
	       <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
		
			</div>
		<div class="pull-right">
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
		</div>
	</div>
	</div>	
	
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead><tr>
			<th><input type="checkbox" class="i-checks"></th>
			<th class="sort-column table_type">表类型</th>
			<th class="sort-column name">表名</th>
			<th>说明</th>
			<th class="sort-column class_name">类名</th>
			<th class="sort-column parent_table">主表</th>
			<th class="sort-column isSync">同步数据库</th>
			<shiro:hasPermission name="gen:genTable:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="genTable">
			<tr>
				<td> <input type="checkbox" id="${genTable.id}" class="i-checks"></td>
				<td>
					<c:if test="${genTable.tableType==0}">单表</c:if>
					<c:if test="${genTable.tableType==1}">主表</c:if>
					<c:if test="${genTable.tableType==2}">附表</c:if>
					<c:if test="${genTable.tableType==3}">树结构表</c:if>
				</td>
				<td><a href="${ctx}/gen/genTable/form?id=${genTable.id}">${genTable.name}</a></td>
				<td>${genTable.comments}</td>
				<td>${genTable.className}</td>
				<td title="点击查询子表"><a href="javascript:" onclick="$('#parentTable').val('${genTable.parentTable}');$('#searchForm').submit();">${genTable.parentTable}</a></td>
				<td>${genTable.isSync eq '1' ? '已同步' : '未同步'}</td>
				<shiro:hasPermission name="gen:genTable:edit"><td>
    				<a href="#" onclick="openDialog('修改业务表','${ctx}/gen/genTable/form?id=${genTable.id}','80%','80%')" class="btn btn-success btn-xs"><i class="fa fa-edit"></i>修改</a>
					<a href="${ctx}/gen/genTable/remove?id=${genTable.id}" onclick="return confirmx('确认要移除该条记录吗？', this.href)" class="btn btn-warning btn-xs"><i class="fa fa-trash"></i>移除</a>
					<a href="${ctx}/gen/genTable/delete?id=${genTable.id}" onclick="return confirmx('确认要删除该条记录并删除对应的数据库表吗？', this.href)" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i>删除</a>
					<a href="${ctx}/gen/genTable/synchDb?id=${genTable.id}" onclick="return confirmx('确认要强制同步数据库表吗？同步数据库将删除所有数据重新建表', this.href)" class="btn btn-info btn-xs"><i class="fa fa-database"></i>同步数据库</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<table:page page="${page}"></table:page>
</body>
</html>
