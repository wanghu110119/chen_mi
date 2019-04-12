<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>项目管理</title>
<meta name="decorator" content="default" />
<style>
	img {width:50px;height: 50px;}
/* 	table {text-align: center;} */
	.checkCenter{text-align: center;}
</style>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>项目管理列表</h5>
				<div class="ibox-tools">
					<a class="collapse-link"> <i class="fa fa-chevron-up"></i>
					</a>
				</div>
			</div>
			<div class="ibox-content">
				<sys:message content="${message}" />

				<!-- 查询条件 -->
				<div class="row">
					<div class="col-sm-12">
						<form:form id="searchForm" modelAttribute="sysProject"
							action="${ctx}/ident/sysProject/list" method="post" class="form-inline">
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
							<shiro:hasPermission name="ident:sysProject:add">
								<table:addRow url="${ctx}/ident/sysProject/form" title="应用" width="600px"
									height="400px"></table:addRow>
								<!-- 增加按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission name="ident:sysProject:edit">
								<table:editRow url="${ctx}/ident/sysProject/form" id="contentTable"
									title="项目" width="600px" height="400px"></table:editRow>
								<!-- 编辑按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission name="ident:sysProject:del">
								<table:delRow url="${ctx}/ident/sysProject/deleteAll" id="contentTable"></table:delRow>
								<!-- 删除按钮 -->
							</shiro:hasPermission>
-
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
					class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
					<thead>
						<tr>
							<th><input type="checkbox" class="i-checks"></th>
							<th class="sort-column name">项目名称</th>
							<th class="sort-column url">项目URL</th>
							<th class="sort-column uc.name">创建用户</th>
							<th class="sort-column status">状态</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.list}" var="sysProject">
							<tr>
								<td><input type="checkbox" id="${sysProject.id}"
									class="i-checks"></td>
								<td>${fns:abbr(sysProject.name,50)}</td>
								<td>${fns:abbr(sysProject.url,50)}</td>
								<td>${fns:abbr(sysProject.createBy.name,50)}</td>
								<td>
									<c:choose>  
				                        <c:when test="${sysProject.status eq 1 }">
				                        	<span class="text-info">可用</span>
				                        </c:when>  
				                        <c:otherwise>
				                        	<span class="text-danger">禁用</span>
				                        </c:otherwise>  
				                    </c:choose>
								</td>
								<td class="width-15">
									<shiro:hasPermission name="ident:sysProject:enable">
										<c:if test="${sysProject.status eq 1}">
											<a href="${ctx}/ident/sysProject/disable?id=${sysProject.id}"
											onclick="return confirmx('确认要禁用该项目吗？', this.href)"
											title="禁用"
											class="btn btn-danger btn-xs btn-circle"><i
											class="fa fa-lock"></i></a>
										</c:if>
									</shiro:hasPermission>
									<shiro:hasPermission name="ident:sysProject:disable">
										<c:if test="${sysProject.status eq 2}">
											<a href="${ctx}/ident/sysProject/enable?id=${sysProject.id}"
											onclick="return confirmx('确认要启用该项目吗？', this.href)"
											title="启用"
											class="btn btn-danger btn-xs btn-circle"><i
											class="fa fa-unlock"></i></a>
										</c:if>
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