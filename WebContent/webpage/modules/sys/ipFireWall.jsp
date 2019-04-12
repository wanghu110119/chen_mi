<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>IP访问控制管理</title>
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
				<h5>IP访问控制列表</h5>
				<div class="ibox-tools">
					<a class="collapse-link"> <i class="fa fa-chevron-up"></i>
					</a> 
				</div>
			</div>
			<div class="ibox-content">
				<sys:message content="${message}"/>

				<!-- 查询条件 -->
				<div class="row">
					<div class="col-sm-12">
						<form:form id="searchForm" modelAttribute="ipFireWall"
							action="${ctx}/sys/ipFireWall/index" method="post" class="form-inline">
							<input id="pageNo" name="pageNo" type="hidden"
								value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden"
								value="${page.pageSize}" />
							<table:sortColumn id="orderBy" name="orderBy"
								value="${page.orderBy}" callback="sortOrRefresh();" />
							<!-- 支持排序 -->
							<div class="form-group">
								<span>IP地址或描述：</span>
								<form:input path="derc" htmlEscape="false" maxlength="200"
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
							<shiro:hasPermission name="sys:ipFireWall:add">
								<table:addRow url="${ctx}/sys/ipFireWall/form" title="应用" width="800px"
									height="500px"></table:addRow>
								<!-- 增加按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission name="sys:ipFireWall:edit">
								<table:editRow url="${ctx}/sys/ipFireWall/form" id="contentTable"
									title="IP" width="800px" height="500px"></table:editRow>
								<!-- 编辑按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission name="sys:ipFireWall:del">
								<table:delRow url="${ctx}/sys/ipFireWall/deleteAll" id="contentTable"></table:delRow>
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
					class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
					<thead>
						<tr>
							<th><input type="checkbox" class="i-checks"></th>
							<th class="sort-column ip">IP地址</th>
							<th class="sort-column dict_id">IP类型</th>
							<th class="sort-column derc">描述</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.list}" var="ipFireWall">
							<tr>
								<td class="width-2"><input type="checkbox" id="${ipFireWall.id}"
									class="i-checks"></td>
								<td>
									 ${ipFireWall.ip}
									 <c:if test="${ipFireWall.maxIp ne null and ipFireWall.maxIp ne ''}">
									 	— ${ipFireWall.maxIp}
									 </c:if>
								</td>
								<td>
									<span class="${ipFireWall.dict.value eq '1'?'text-info':'text-danger'}">
										${ipFireWall.dict.label}
									</span>
								</td>
								<td>
									${fns:abbr(ipFireWall.derc,50)}
				                </td>
								<td class="width-15">
									<shiro:hasPermission name="sys:ipFireWall:edit">
										<a href="#"
											onclick="openDialog('编辑', '${ctx}/sys/ipFireWall/form?id=${ipFireWall.id}','800px', '500px')"
											title="编辑"
											class="btn btn-warning btn-xs btn-circle"><i
											class="fa fa-file-text-o"></i></a>
									</shiro:hasPermission>
									<shiro:hasPermission name="sys:ipFireWall:del">
										<a href="${ctx}/sys/ipFireWall/delete?id=${ipFireWall.id}"
										onclick="return confirmx('确认要删除该记录吗？', this.href)"
										title="删除"
										class="btn btn-danger btn-xs btn-circle"><i
										class="fa fa-trash-o"></i></a>
									</shiro:hasPermission>
								</td>
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