<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>制定管理员</title>
<meta name="decorator" content="default" />
<style type="text/css">
	ul,li,div {padding:0;margin:0;}
	ul li {float:left;width:100px;height:30px;line-height:30px;text-align:center;border-bottom:none;margin-right: 10px;cursor: pointer;}
	ul li.fli {background-color:#ccc;color:red;}
	ul {overflow:hidden;zoom:1;list-style-type:none;}
	#tab_con {width:304px;height:200px;}
	#tab_con div {width:304px;height:200px;display:none;border:1px #bbb solid;border-top:none;}
	#tab_con div.fdiv {display:block;background-color:#ccc;}
	.li-select{background-color: #1ab394;}
	.li-select a{color: #ffffff;}
	.li-select span{color: #ffffff;}
</style>
<script type="text/javascript">
	$(function(){
		/*添加选中标签样式*/
		$("#tab").find("li").click(function(){
			var msg = $(this).attr("value");
			display();
			selectShow(msg);
			$("#tab").find("li").each(function(){
				$(this).removeClass("li-select");
			});
			$(this).addClass("li-select");
		});
		
		/*隐藏*/
		function display() {
			$("#groupTable").css("display","none");
			$("#roleTable").css("display","none");
			$("#userTable").css("display","none");
			$("#officeTable").css("display","none");
		}
		
		/*选中显示*/
		function selectShow(type) {
			switch (type) {
			case 1:
				$("#userTable").css("display","");
				break;
			case 2:
				$("#officeTable").css("display","");
				break;
			case 3:
				$("#roleTable").css("display","");
				break;
			case 4:
				$("#groupTable").css("display","");
				break;
			default:
				$("#userTable").css("display","");
				break;
			}
		}
	});
</script>
</head>

<body>
	<ul id="tab"  class="layui-layer-title"	>
		<li class="li-select" value="1"><span>用户</span></li>
		<li value="2"><span>组织机构</span></li>
		<li value="3"><span>角色</span></li>
		<li value="4"><span>用户组</span></li>
	</ul>
	<div class="content_wrap" >
	    <div style="padding:0px 20px 10px 20px">
		<table id="userTable"
			class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
			<thead>
				<tr>
					<th class="sort-column name">账号</th>
					<th class="sort-column name">姓名</th>
					<th class="sort-column name">访问权限</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${userAuth}" var="app">
					<tr>
						<td> ${app.user.name}</td>
						<td> ${app.user.loginName}</td>
						<td><input class="i-checks accessAuth" name="authAccess"
							type="checkbox"
							${empty app.accessAuth ?"":"checked='checked'"} /> <span>访问权限</span>
							<input class="i-checks accessAuth" name="authAccess"
							type="checkbox" ${empty app.accessAuth ?"":""} /> <span>授权权限</span>
							<input class="i-checks accessAuth" name="authAccess"
							type="checkbox" ${empty app.accessAuth ?"":""} /> <span>管理权限</span>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<table id="officeTable" style="display: none"
			class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
			<thead>
				<tr>
					<th class="sort-column name">组织机构</th>
					<th class="sort-column name">访问权限</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${officeAuth}" var="app">
					<tr>
						<td> ${app.office.name}</td>
						<td><input class="i-checks accessAuth" name="authAccess"
							type="checkbox"
							${empty app.accessAuth ?"":"checked='checked'"} /> <span>访问权限</span>
							<input class="i-checks accessAuth" name="authAccess"
							type="checkbox" ${empty app.accessAuth ?"":""} /> <span>授权权限</span>
							<input class="i-checks accessAuth" name="authAccess"
							type="checkbox" ${empty app.accessAuth ?"":""} /> <span>管理权限</span>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<table id="groupTable" style="display: none"
			class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
			<thead>
				<tr>
					<th class="sort-column name">用户组</th>
					<th class="sort-column name">访问权限</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${groupAuth}" var="app">
					<tr>
						<td> ${app.identityGroup.groupName}</td>
						<td>
							<input class="i-checks accessAuth" name="authAccess"
							type="checkbox" ${empty app.accessAuth ?"":"checked='checked'"} /> <span>访问权限</span>
							<input class="i-checks accessAuth" name="authAccess"
							type="checkbox" ${empty app.accessAuth ?"":""} /> <span>授权权限</span>
							<input class="i-checks accessAuth" name="authAccess"
							type="checkbox" ${empty app.accessAuth ?"":""} /> <span>管理权限</span>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<table id="roleTable" style="display: none"
			class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
			<thead>
				<tr>
					<th class="sort-column name">角色</th>
					<th class="sort-column name">访问权限</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${roleAuth}" var="app">
					<tr>
						<td> ${app.role.name}</td>
						<td>
							<input class="i-checks accessAuth" name="authAccess"
							type="checkbox" ${empty app.accessAuth ?"":"checked='checked'"} /> <span>访问权限</span>
							<input class="i-checks accessAuth" name="authAccess"
							type="checkbox" ${empty app.accessAuth ?"":""} /> <span>授权权限</span>
							<input class="i-checks accessAuth" name="authAccess"
							type="checkbox" ${empty app.accessAuth ?"":""} /> <span>管理权限</span>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		</div>
	</div>
	<script type="text/javascript">
		
	
	</script>
</body>
</html>
