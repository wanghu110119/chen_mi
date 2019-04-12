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
			format: 'YYYY-MM-DD hh:mm:ss',
			istime: true,
			elem : '#beginDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
			event : 'focus' //响应事件。如果没有传入event，则按照默认的click
		});
		laydate({
			format: 'YYYY-MM-DD hh:mm:ss',
			istime: true,
			elem : '#endDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
			event : 'focus' //响应事件。如果没有传入event，则按照默认的click
		});

	})
</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>我的登录日志列表</h5>
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
						<form:form id="searchForm" action="${ctx}/sys/user/userLogList/" method="post"
							class="form-inline">
							<input id="pageNo" name="pageNo" type="hidden"
								value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden"
								value="${page.pageSize}" />
							<table:sortColumn id="orderBy" name="orderBy"
								value="${page.orderBy}" callback="sortOrRefresh();" />
							<!-- 支持排序 -->
							<div class="form-group">
								<span>日期范围：&nbsp;</span> <input id="beginDate" name="beginDate"
									type="text" maxlength="20"
									class="laydate-icon form-control layer-date input-sm"
									value="<fmt:formatDate value="${log.beginDate}" type="both"/>" />
								<label>&nbsp;--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label><input
									id="endDate" name="endDate" type="text" maxlength="20"
									class=" laydate-icon form-control layer-date input-sm"
									value="<fmt:formatDate value="${log.endDate}" type="both"/>" />
							</div>
						</form:form>
						<br />
					</div>
				</div>


				<!-- 工具栏 -->
				<div class="row">
					<div class="col-sm-12">
						<div class="pull-left">
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
							<th>操作用户</th>
							<th>操作类型</th>
							<th>操作者IP</th>
							<th>操作时间</th>
							<th>操作内容</th>
					</thead>
					<tbody>
						<%
							request.setAttribute("strEnter", "\n");
							request.setAttribute("strTab", "\t");
						%>
						<c:forEach items="${page.list}" var="log">
							<tr>
								<td>${log.createBy.name}</td>
								<td>
									<c:choose>
									    <c:when test="${log.type eq 3}">
									       	登录
									    </c:when>
									    <c:when test="${log.type eq 4}">
									                        登出
									    </c:when>
									</c:choose>
								</td>
								<td>${log.remoteAddr}</td>
								<td><fmt:formatDate value="${log.createDate}" type="both" /></td>
								<td>${log.title}</td>
							</tr>
							<c:if test="${not empty log.exception}">
								<tr>
									<td colspan="8"
										style="word-wrap: break-word; word-break: break-all;">
										<%-- 					用户代理: ${log.userAgent}<br/> --%> <%-- 					提交参数: ${fns:escapeHtml(log.params)} <br/> --%>
										异常信息: <br />
										${fn:replace(fn:replace(fns:escapeHtml(log.exception), strEnter, '<br/>'), strTab, '&nbsp; &nbsp; ')}
									</td>
								</tr>
							</c:if>
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