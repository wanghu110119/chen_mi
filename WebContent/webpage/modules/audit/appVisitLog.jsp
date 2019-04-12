<%@ page contentType="text/html;charset=UTF-8"%>
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

	})
</script>
<div class="row">
	<div class="col-sm-12">
		<form:form id="searchForm" action="${ctx}/audit/appvisit"
			method="post" class="form-inline">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
			<input id="pageSize" name="pageSize" type="hidden"
				value="${page.pageSize}" />
			<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}"
				callback="sortOrRefresh();" />
			<!-- 支持排序 -->
			<div class="form-group">
				<span>用户姓名：&nbsp;</span> <input type="text" maxlength="20"
					class="form-control input-sm" name="user.name"
					value="${appUserRecord.user.name}" /> <span>&nbsp;日期范围：&nbsp;</span> <input
					id="beginDate" name="beginDate" type="text" maxlength="20"
					class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${appUserRecord.beginDate}" pattern="yyyy-MM-dd"/>" />
				<label>&nbsp;--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label><input
					id="endDate" name="endDate" type="text" maxlength="20"
					class=" laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${appUserRecord.endDate}" pattern="yyyy-MM-dd"/>" />
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
			<th>用户登录名</th>
			<th>用户姓名</th>
			<th>应用名称</th>
			<th>应用URL</th>
			<th>应用类型</th>
			<th>访问时间</th>
	</thead>
	<tbody>
		<%
			request.setAttribute("strEnter", "\n");
			request.setAttribute("strTab", "\t");
		%>
		<c:forEach items="${page.list}" var="visit">
			<tr>
				<td>${visit.user.loginName}</td>
				<td>${visit.user.name}</td>
				<td>${visit.application.name}</td>
				<td>${visit.application.url}</td>
				<td><c:choose>
						<c:when test="${visit.application.accessWay eq 1}">
									       	单点登录
									    </c:when>
						<c:when test="${visit.application.accessWay eq 2}">
									                       超链接
									    </c:when>
					</c:choose></td>
				<td><fmt:formatDate value="${visit.createDate}" type="both" /></td>
			</tr>
		</c:forEach>
	</tbody>
</table>

<!-- 分页代码 -->
<table:page page="${page}"></table:page>
<br />
<br />