<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>用户管理</title>
<meta name="decorator" content="default" />
</head>
<body>
	<div class="wrapper wrapper-content">
		<sys:message content="${message}" />
		<!-- 查询条件 -->
		<div class="row">
			<div class="col-sm-12">
				<form:form id="searchForm" modelAttribute="user"
					action="${ctx}/sys/user/list" method="post" class="form-inline">
					<div class="form-group">
						<span>${office.name }</span> <input type="hidden" id="officeId"
							value="${office.id }" />
					</div>
				</form:form>
				<br />
			</div>
		</div>

		<table id="contentTable"
			class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
			<thead>
				<tr>
					<th class="sort-column login_name">系统名称</th>
					<th class="sort-column name">系统配置</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${apps}" var="app">
					<tr appid="${app.apply.id}">
						<td>${app.apply.name}</td>
						<td>
							<c:if
								test="${not empty app.isEdit and app.isEdit eq '2'}">
								<span><input type="checkbox" isEdit="${app.isEdit}" class="i-checks accessAuth"
									${app.closeType eq '1' ? '':'checked=checked'}>访问权限</span>
							</c:if> 
							<c:if
								test="${not empty app.accessAuth and empty app.isEdit or app.isEdit eq '1'}">
								<input type="checkbox" class="i-checks accessAuth"
									 checked="checked">访问权限</c:if> <c:if
								test="${empty app.accessAuth and empty app.isEdit or app.isEdit eq '1'}">
								<input type="checkbox" class="i-checks accessAuth">访问权限</c:if>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<shiro:hasPermission name="unifiedAuth:office:save">
			<button class="btn btn-primary  btn-sm " onclick="save()">提交</button>
		</shiro:hasPermission>
	</div>
	<script type="text/javascript">
		function save() {
			var trs = $("#contentTable tbody").find("tr");
			var list = [];
			var close = [];
			var officeId = $("#officeId").val();
			for (var i = 0; i < trs.length; i++) {
				var tr = $(trs[i]);
				var accessAuth = tr.find(".accessAuth").is(':checked');
				if (accessAuth) {
					list.push({
						officeId : officeId,
						applyId : tr.attr("appid"),
						accessAuth : "1"
					});
				} else {
					var msg = tr.find(".accessAuth").attr("isEdit");
					if (msg == 2) {
						close.push({
							officeId : officeId,
							applyId : tr.attr("appid"),
							accessAuth : "1"
						});
					}
				}
			}
			$.ajax({
				type : "POST",
				url : "${ctx}/unifiedAuth/office/save",
				data : {
					closeOffices : JSON.stringify(close),
					authOffices : JSON.stringify(list),
					officeId : officeId
				},
				dataType : "json",
				success : function(data) {
					if (data.success) {
						layer.msg('保存成功', {
							icon : 1,
							time : 2000
						});
					} else {
						layer.msg('保存出错', {
							icon : 3,
							time : 2000
						});
					}
				}
			});
			//alert(trs.length);
		}
	</script>
</body>
</html>