<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>自定义应用管理</title>
<meta name="decorator" content="default" />
<style>
	img {width:50px;height: 50px;}
/* 	table {text-align: center;} */
</style>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>应用列表</h5>
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
						<form:form id="searchForm" modelAttribute="application"
							action="${ctx}/ident/userapplication/list" method="post" class="form-inline">
							<input id="pageNo" name="pageNo" type="hidden"
								value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden"
								value="${page.pageSize}" />
							<table:sortColumn id="orderBy" name="orderBy"
								value="${page.orderBy}" callback="sortOrRefresh();" />
							<!-- 支持排序 -->
							<div class="form-group">
								<span>应用名称：</span>
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
							<shiro:hasPermission name="ident:userapplication:add">
								<table:addRow url="${ctx}/ident/userapplication/form" title="应用" width="800px"
									height="500px"></table:addRow>
								<!-- 增加按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission name="ident:userapplication:edit">
								<table:editRow url="${ctx}/ident/userapplication/form" id="contentTable"
									title="应用" width="800px" height="500px"></table:editRow>
								<!-- 编辑按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission name="ident:userapplication:del">
								<table:delRow url="${ctx}/ident/userapplication/deleteByLogic" id="contentTable"></table:delRow>
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
							<th class="sort-column pic">应用图标</th>
							<th class="sort-column name">应用名称</th>
							<th class="sort-column access_way">接入类型</th>
							<th class="sort-column url">应用URL</th>
							<th class="sort-column uc.name">创建用户</th>
							<th>用户角色</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.list}" var="application">
							<tr>
								<td><input type="checkbox" id="${application.id}"
									class="i-checks"></td>
								<td>
									<c:choose>  
				                        <c:when test="${fns:contains(application.pic,'static')}">  
				                            <img class="img-thumbnail" src="${ctxStatic}${fns:substringAfterLast(application.pic, 'static')}"> 
				                        </c:when>  
				                        <c:otherwise>  
				                            <img class="img-thumbnail" src="${ctx}/ident/application/download?pic=${application.pic}">
				                        </c:otherwise>  
				                    </c:choose>  
								</td>
								<td>${fns:abbr(application.name,50)}</td>
								<td>
									<c:choose>  
				                        <c:when test="${application.accessWay eq 1 }">
				                        	单点登录
				                        </c:when>  
				                        <c:otherwise>
				                        	超链接
				                        </c:otherwise>  
				                    </c:choose>
				                </td>
								<td>${fns:abbr(application.url,50)}</td>
								<td>${fns:abbr(application.createBy.name,50)}</td>
								<td class="width-15">
									${fns:getUserRole(application.createBy.id)}
								</td>
								<td class="width-5">
									<shiro:hasPermission name="ident:userapplication:system">
										<c:if test="${isable eq '1'}">
											<a href="${ctx}/ident/userapplication/system?id=${application.id}"
											onclick="return confirmx('确认是否要添加到通用吗？', this.href)"
											title="添加到通用"
											class="btn btn-primary btn-xs btn-circle"><i
											class="fa fa-file"></i></a>
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