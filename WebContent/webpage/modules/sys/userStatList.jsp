<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>日志管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
	$(document).ready(function() {
		//外部js调用
		laydate({
			elem : '#beginDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
			event : 'focus' //响应事件。如果没有传入event，则按照默认的click
		});
		laydate({
			elem : '#endDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
			event : 'focus' //响应事件。如果没有传入event，则按照默认的click
		});
		$(".close-link").on("click",function(){
			var index = parent.layer.getFrameIndex(window.name);
	        parent.layer.close(index);
		});
	})
</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>账号操作列表</h5>
				<div class="ibox-tools">
					<a class="collapse-link"> <i class="fa fa-chevron-up"></i>
					</a>
					<a class="close-link"> <i class="fa fa-times"></i>
					</a>
				</div>
			</div>

			<div class="ibox-content">
				<sys:message content="${message}" />

				<!-- 查询条件 -->
				<div class="row">
					<div class="col-sm-12">
						<form:form id="searchForm" action="${ctx}/sys/userStat/list" method="post"
							class="form-inline">
							<input id="pageNo" name="pageNo" type="hidden"
								value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden"
								value="${page.pageSize}" />
							<table:sortColumn id="orderBy" name="orderBy"
								value="${page.orderBy}" callback="sortOrRefresh();" />
							<!-- 支持排序 -->
							<div class="form-group">

								<span>用户ID：</span> <input id="createBy.loginName" name="createBy.loginName"
									type="text" maxlength="50" class="form-control input-sm"
									value="${userStat.createBy.loginName}" /> <span>日期范围：&nbsp;</span> <input
									id="beginDate" name="beginDate" type="text" maxlength="20"
									class="laydate-icon form-control layer-date input-sm"
									value="<fmt:formatDate value="${userStat.beginDate}" pattern="yyyy-MM-dd"/>" />
								<label>&nbsp;--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label><input
									id="endDate" name="endDate" type="text" maxlength="20"
									class=" laydate-icon form-control layer-date input-sm"
									value="<fmt:formatDate value="${userStat.endDate}" pattern="yyyy-MM-dd"/>" />&nbsp;&nbsp;
								&nbsp;
							</div>
						</form:form>
						<br />
					</div>
				</div>


				<!-- 工具栏 -->
				<div class="row">
					<div class="col-sm-12">
						<div class="pull-left">
							<!--<shiro:hasPermission name="sys:user:stat">
								<table:delRow url="${ctx}/sys/log/deleteAll" id="contentTable"></table:delRow>
								<button class="btn btn-white btn-sm "
									onclick="confirmx('确认要清空日志吗？','${ctx}/sys/log/empty')"
									title="清空">
									<i class="fa fa-trash"></i> 清空
								</button>
							</shiro:hasPermission>-->
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
							<th>被操作账号</th>
							<th>操作类型</th>
							<th>操作者账号</th>
							<th>操作者IP</th>
							<th>操作时间</th>
							<th>备注</th>
					</thead>
					<tbody>
						<%
						    request.setAttribute("strEnter", "\n");
						    request.setAttribute("strTab", "\t");
						%>
						<c:forEach items="${page.list}" var="userStat">
							<tr>
								<td><input type="checkbox" id="${userStat.id}" class="i-checks"></td>
								<td>${userStat.user.loginName}</td>
								<td>${userStat.operationType.label}</td>
								<td>${userStat.createBy.loginName}</td>
								<td>${userStat.operationIp}</td>
								<td><fmt:formatDate value="${userStat.createDate}" type="both" /></td>
								<td>${userStat.remarks}</td>
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