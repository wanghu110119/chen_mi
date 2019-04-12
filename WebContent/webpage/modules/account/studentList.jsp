<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>用户管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
	    $("#btnImportPhoto").click(function(){
	        top.layer.open({
	            type: 1, 
	            area: [500, 300],
	            title:"学生头像上传",
	            content:$("#importPhotoBox").html() ,
	            btn: ['确定', '关闭'],
	            btn1: function(index, layero){
	                    var inputForm =top.$("#importForm");
	                    inputForm.attr("target","studentContent");//表单提交成功后，从服务器返回的url在当前tab中展示
	                    top.$("#importForm").submit();
	                    top.layer.close(index);
	              },
	             
	              btn2: function(index){ 
	                  top.layer.close(index);
	               },
	            success: function(){
	            }
	        }); 
	    });
	});
	
</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<sys:message content="${message}" />
		<!-- 查询条件 -->
		<div class="row">
			<div class="col-sm-12">
				<form:form id="searchForm" modelAttribute="student"
					action="${ctx}/account/student/list" method="post"
					class="form-inline">
					<input id="pageNo" name="pageNo" type="hidden"
						value="${page.pageNo}" />
					<input id="pageSize" name="pageSize" type="hidden"
						value="${page.pageSize}" />
					<input id="officeId" name="officeId" type="hidden"
						value="${student.officeId}" />
					<input id="officeParentIds" name="officeParentIds" type="hidden"
						value="${student.officeParentIds}" />
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
					<shiro:hasPermission name="account:student:add">
						<table:addRow url="${ctx}/account/student/form" title="用户"
							width="800px" height="625px" target="studentContent"></table:addRow>
						<!-- 增加按钮 -->
					</shiro:hasPermission>
					<shiro:hasPermission name="account:student:import">
						<table:importExcel url="${ctx}/account/student/import"></table:importExcel>
						<!-- 导入按钮 -->
					</shiro:hasPermission>
					<shiro:hasPermission name="account:student:export">
						<table:exportExcel url="${ctx}/account/student/export"></table:exportExcel>
						<!-- 导出按钮 -->
					</shiro:hasPermission>
					<shiro:hasPermission name="account:student:import">
						<button class="btn btn-white btn-sm " id="btnImportPhoto"
							data-toggle="tooltip" data-placement="left" onclick=""
							title="批量上传头像">批量上传头像</button>
						<div id="importPhotoBox" class="hide">
							<form id="importForm" action="/mht/a/account/common/importPhoto"
								target="studentContent" method="post"
								enctype="multipart/form-data"
								style="padding-left: 20px; text-align: center;"
								onsubmit="loading('正在导入，请稍等...');">
								<br> 头像命名规则：<input type="radio" name="photoNameType"
									value="0" checked> 学号 <input type="radio"
									name="photoNameType" value="1"> 身份证号 <input
									id="uploadFile2" name="file" type="file" style="width: 330px"  accept=".zip">导入文件不能超过10M，仅允许导入“zip”格式文件！<br>
								<input type="hidden" name="url" value="/account/student/list" />
								<script type="text/javascript">
									$(function(){
										$("#uploadFile2").change(function(){
											var dom = document.getElementById("uploadFile2");
	 										var fileSize = dom.files[0].size;
	 										if (fileSize > 1024*1024*10) {
	 											$("#uploadFile2").val("");
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
					<th class="sort-column no">学号</th>
					<th class="sort-column id_no">证件号</th>
					<th class=" origin">数据来源</th>
					<th class=" office">组织机构</th>
					<th class="roles">角色</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${page.list}" var="user">
					<tr>
						<!-- <td> <input type="checkbox" id="${user.id}" class="i-checks"></td> -->
						<td><a href="#"
							onclick="openDialogView('查看用户', '${ctx}/account/student/form?id=${user.id}','800px', '680px')">${user.loginName}</a></td>
						<td>${user.name}</td>
						<td>${user.no}</td>
						<td>${user.idNo}</td>
						<td>${user.originName}</td>
						<td>${user.office.name}</td>
						<td>${roleTypeName}${not empty user.roleNames ? ','.concat(user.roleNames) : ''}</td>
						<td><shiro:hasPermission name="account:student:view">
								<a href="#"
									onclick="openDialogView('查看用户', '${ctx}/account/student/form?id=${user.id}','800px', '680px')"
									class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i>
									查看</a>
							</shiro:hasPermission> <shiro:hasPermission name="account:student:edit">
								<a href="#"
									onclick="openDialog('修改用户', '${ctx}/account/student/form?id=${user.id}','800px', '700px', 'studentContent')"
									class="btn btn-success btn-xs"><i class="fa fa-edit"></i>
									修改</a>
							</shiro:hasPermission> <shiro:hasPermission name="account:student:edit">
								<a href="#"
									onclick="openDialog('修改用户密码', '${ctx}/account/common/updatePassword?id=${user.id}&url=/account/student/list','500px', '250px', 'studentContent')"
									class="btn btn-success btn-xs"><i class="fa fa-edit"></i>
									修改密码</a>
							</shiro:hasPermission> <shiro:hasPermission name="account:student:del">
								<a href="${ctx}/account/student/delete?id=${user.id}"
									onclick="return confirmx('确认要删除该学生吗？', this.href)"
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