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
				<form:form id="searchForm" modelAttribute="parents"
					action="${ctx}/account/parents/list" method="post"
					class="form-inline">
					<input id="pageNo" name="pageNo" type="hidden"
						value="${page.pageNo}" />
					<input id="pageSize" name="pageSize" type="hidden"
						value="${page.pageSize}" />
					<input id="officeId" name="officeId" type="hidden"
						value="${parents.officeId}" />
					<input id="officeParentIds" name="officeParentIds" type="hidden"
						value="${parents.officeParentIds}" />
					<table:sortColumn id="orderBy" name="orderBy"
						value="${page.orderBy}" callback="sortOrRefresh();" />
					<!-- 支持排序 -->
					<div class="form-group">
						<span>账号：</span>
						<form:input path="loginName" htmlEscape="false" maxlength="50"
							class=" form-control input-sm" />
						<span>姓名：</span>
						<form:input path="name" htmlEscape="false" maxlength="50"
							class=" form-control input-sm" />
						<span>证件号：</span>
						<form:input path="idNo" htmlEscape="false" maxlength="50"
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
					<shiro:hasPermission name="account:parents:add">
						<table:addRow url="${ctx}/account/parents/form" title="用户"
							width="800px" height="625px" target="parentsContent"></table:addRow>
						<!-- 增加按钮 -->
					</shiro:hasPermission>
					<shiro:hasPermission name="account:parents:import">
						<table:importExcel url="${ctx}/account/parents/import"></table:importExcel>
						<!-- 导入按钮 -->
					</shiro:hasPermission>
					<shiro:hasPermission name="account:parents:export">
						<table:exportExcel url="${ctx}/account/parents/export"></table:exportExcel>
						<!-- 导出按钮 -->
					</shiro:hasPermission>
					<shiro:hasPermission name="account:parents:import">
						<button class="btn btn-white btn-sm " id="btnImportPhoto"
							data-toggle="tooltip" data-placement="left" onclick=""
							title="批量上传头像">批量上传头像</button>
						<div id="importPhotoBox" class="hide">
							<form id="importForm" action="/mht/a/account/common/importPhoto"
								target="parentsContent" method="post"
								enctype="multipart/form-data"
								style="padding-left: 20px; text-align: center;"
								onsubmit="loading('正在导入，请稍等...');">
								<br> 头像命名规则：
								<!-- <input type="radio" name="photoNameType" value="0" checked> 学号   -->
								<input type="radio" name="photoNameType" value="1" checked>
								身份证号 <input id="uploadFile2" name=file type="file"
									style="width: 330px" accept=".zip">导入文件不能超过10M，仅允许导入“zip”格式文件！<br>
								<input type="hidden" name="url" value="/account/parents/list" />
								<script type="text/javascript">
									$(function(){
										$("#uploadFile2").change(function(){
											var dom = document.getElementById("uploadFile2");
	 										var fileSize = dom.files[0].size;
	 										if (fileSize > 1024*1024*10) {
	 											$("#uploadFile").val("");
	 											layer.msg("文件大小不能超过10M", {
	 												icon : 5,
	 												time : 2000
	 											});
	 										}
										});
									});
									function toVaild () {
										var dom = document.getElementById("uploadFile2");
 										var fileSize = dom.files[0].size;
 										if (fileSize == 0) {
 											layer.msg("文件不能空", {
 												icon : 5,
 												time : 2000
 											});
 										}
 										if (fileSize > 1024*1024*10) {
 											layer.msg("文件大小不能超过10M", {
 												icon : 5,
 												time : 2000
 											});
 											return false;
 										}
										return true;
									}
								</script>
							</form>
						</div>
						<script type="text/javascript">
							$(document)
									.ready(
											function() {
												$("#btnImportPhoto")
														.click(
																function() {
																	top.layer
																			.open({
																				type : 1,
																				area : [
																						500,
																						300 ],
																				title : "家长头像上传",
																				content : $(
																						"#importPhotoBox")
																						.html(),
																				btn : [
																						'确定',
																						'关闭' ],
																				btn1 : function(
																						index,
																						layero) {
																					var inputForm = top
																							.$("#importForm");
																					inputForm.attr("target","parentsContent");
																					top
																							.$(
																									"#importForm")
																							.submit();
																					top.layer
																							.close(index);
																				},

																				btn2 : function(
																						index) {
																					top.layer
																							.close(index);
																				}
																			});
																});

											});
						</script>
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
					<!-- <th><input type="checkbox" class="i-checks"></th> -->
					<th class="sort-column login_name">登录账号</th>
					<th class="name">姓名</th>
					<th class="sort-column id_no">证件号</th>
					<th class=" origin">数据来源</th>
					<th class=" studentNames">学生</th>
					<th class="roles">角色</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${page.list}" var="user">
					<tr>
						<!-- <td> <input type="checkbox" id="${user.id}" class="i-checks"></td> -->
						<td><a href="#"
							onclick="openDialogView('查看用户', '${ctx}/account/parents/form?id=${user.id}','800px', '680px')">${user.loginName}</a></td>
						<td>${user.name}</td>
						<td>${user.idNo}</td>
						<td>${user.originName}</td>
						<td>${user.studentNames}</td>
						<td>${roleTypeName}${not empty user.roleNames ? ','.concat(user.roleNames) : ''}</td>
						<td><shiro:hasPermission name="account:parents:view">
								<a href="#"
									onclick="openDialogView('查看用户', '${ctx}/account/parents/form?id=${user.id}','800px', '680px')"
									class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i>
									查看</a>
							</shiro:hasPermission> <shiro:hasPermission name="account:parents:edit">
								<a href="#"
									onclick="openDialog('修改用户', '${ctx}/account/parents/form?id=${user.id}','800px', '700px', 'parentsContent')"
									class="btn btn-success btn-xs"><i class="fa fa-edit"></i>
									修改</a>
							</shiro:hasPermission> <shiro:hasPermission name="account:parents:edit">
								<a href="#"
									onclick="openDialog('修改用户密码', '${ctx}/account/common/updatePassword?id=${user.id}&url=/account/parents/list','500px', '250px', 'parentsContent')"
									class="btn btn-success btn-xs"><i class="fa fa-edit"></i>
									修改密码</a>
							</shiro:hasPermission> <shiro:hasPermission name="account:parents:del">
								<a href="${ctx}/account/parents/delete?id=${user.id}"
									onclick="return confirmx('确认要删除该家长吗？', this.href)"
									class="btn btn-danger btn-xs"><i class="fa fa-trash"></i>
									删除</a>
							</shiro:hasPermission></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<table:page page="${page}"></table:page>
	</div>
</body>
</html>