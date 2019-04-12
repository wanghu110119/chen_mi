<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>应用管理</title>
<meta name="decorator" content="default" />
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>应用认证列表</h5>
				<div class="ibox-tools">
					<a class="collapse-link"> <i class="fa fa-chevron-up"></i>
					</a> <a class="dropdown-toggle" data-toggle="dropdown"
						href="form_basic.html#"> <i class="fa fa-wrench"></i>
					</a>
					<ul class="dropdown-menu dropdown-user">
						<li><a href="#">选项1</a></li>
						<li><a href="#">选项2</a></li>
					</ul>
					<a class="close-link"> <i class="fa fa-times"></i>
					</a>
				</div>
			</div>
			<div class="ibox-content">
				<sys:message content="${message}" />

				<!-- 查询条件 -->
				<div class="row">
					<div class="col-sm-12">
						<form:form id="searchForm" modelAttribute="apply"
							action="${ctx}/ident/aut/list" method="post" class="form-inline">
							<input id="pageNo" name="pageNo" type="hidden"
								value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden"
								value="${page.pageSize}" />
							<table:sortColumn id="orderBy" name="orderBy"
								value="${page.orderBy}" callback="sortOrRefresh();" />
							<!-- 支持排序 -->
							<div class="form-group">
								<span>名称：</span>
								<form:input path="name" htmlEscape="false" maxlength="200"
									class=" form-control input-sm" />
							</div>
						</form:form>
						<br />
					</div>
				</div>

				<!-- 工具栏 -->
				<div class="row">
					<div class="col-sm-12">
						<div class="pull-left">
							<shiro:hasPermission name="ident:aut:add">
								<table:addRow url="${ctx}/ident/aut/form" title="应用" width="500px"
									height="300px"></table:addRow>
								<!-- 增加按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission name="ident:aut:edit">
								<table:editRow url="${ctx}/ident/aut/form" id="contentTable"
									title="应用" width="500px" height="300px"></table:editRow>
								<!-- 编辑按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission name="ident:aut:del">
								<table:delRow url="${ctx}/ident/aut/deleteAll" id="contentTable"></table:delRow>
								<!-- 删除按钮 -->
							</shiro:hasPermission>

							<button class="btn btn-white btn-sm " data-toggle="tooltip"
								data-placement="left" onclick="sortOrRefresh()" title="刷新">
								<i class="glyphicon glyphicon-repeat"></i> 刷新
							</button>
						</div>
						<div class="pull-right">
							<button class="btn btn-primary btn-rounded btn-outline btn-sm "
								onclick="search()">
								<i class="fa fa-search"></i> 查询
							</button>
							<button class="btn btn-primary btn-rounded btn-outline btn-sm "
								onclick="reset()">
								<i class="fa fa-refresh"></i> 重置
							</button>
						</div>
					</div>
				</div>

				<table id="contentTable"
					class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th><input type="checkbox" class="i-checks"></th>
							<th>应用账号</th>
							<th>应用名称</th>
							<th>应用URL</th>
							<th>创建用户</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.list}" var="apply">
							<tr>
								<td><input type="checkbox" id="${apply.id}"
									class="i-checks"></td>
								<td>${fns:abbr(apply.user.name,50)}</td>
								<td>${fns:abbr(apply.name,50)}</td>
								<td>${fns:abbr(apply.url,50)}</td>
								<td>${fns:abbr(apply.createBy.name,50)}</td>
								<td><shiro:hasPermission name="ident:aut:view">
										<a href="#"
											onclick="openDialogView('查看应用', '${ctx}/ident/aut/form?id=${apply.id}','500px', '300px')"
											class="btn btn-info btn-xs btn-circle"><i
											class="fa fa-search-plus"></i></a>
									</shiro:hasPermission> <shiro:hasPermission name="ident:aut:edit">
										<a href="#"
											onclick="openDialog('修改应用', '${ctx}/ident/aut/form?id=${apply.id}','500px', '300px')"
											class="btn btn-success btn-xs btn-circle"><i
											class="fa fa-edit"></i></a>
									</shiro:hasPermission> <shiro:hasPermission name="ident:aut:del">
										<a href="${ctx}/ident/aut/delete?id=${apply.id}"
											onclick="return confirmx('确认要删除该应用吗？', this.href)"
											class="btn btn-danger btn-xs btn-circle"><i
											class="fa fa-trash"></i></a>
									</shiro:hasPermission></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<!-- 分页代码 -->
				<table:page page="${page}"></table:page>
				<br /> <br />
			</div>
		</div>
	</div>
</body>
</html>