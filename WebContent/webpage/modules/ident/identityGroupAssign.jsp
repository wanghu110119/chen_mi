<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>分配用户</title>
	<meta name="decorator" content="default"/>
</head>
<body>
	
	<div class="wrapper wrapper-content">
	<div class="container-fluid breadcrumb">
		<div class="row-fluid span12">
			<span class="span4">组名: <b>${group.groupName}</b></span>
		</div>
		<div class="row-fluid span8">
			<span class="span4">描述: ${group.description}</span>
		</div>
	</div>
	<sys:message content="${message}"/>
	<div class="breadcrumb">
		<form id="assignForm" action="${ctx}/auth/identityGroup/assignUserToIdentityGroup" method="post" class="hide">
			<input type="hidden" name="id" value="${group.id}"/>
			<input id="idsArr" type="hidden" name="idsArr" value=""/>
		</form>
		<button id="assignButton" type="submit"  class="btn btn-outline btn-primary btn-sm" title="添加人员"><i class="fa fa-plus"></i> 添加人员</button>
		<script type="text/javascript">
			$("#assignButton").click(function(){
				
		top.layer.open({
		    type: 2, 
		    area: ['800px', '600px'],
		    title:"选择用户",
	        maxmin: true, //开启最大化最小化按钮
		    content: "${ctx}/auth/identityGroup/userToIdentityGroup?id=${group.id}" ,
		    btn: ['确定', '关闭'],
		    yes: function(index, layero){
    	       var pre_ids = layero.find("iframe")[0].contentWindow.pre_ids;
				var ids = layero.find("iframe")[0].contentWindow.ids;
				if(ids[0]==''){
						ids.shift();
						pre_ids.shift();
					}
					if(pre_ids.sort().toString() == ids.sort().toString()){
						top.$.jBox.tip("未给【${group.groupName}】分配新成员！", 'info');
						return false;
					};
			    	// 执行保存
			    	loading('正在提交，请稍等...');
			    	var idsArr = "";
			    	for (var i = 0; i<ids.length; i++) {
			    		idsArr = (idsArr + ids[i]) + (((i + 1)== ids.length) ? '':',');
			    	}
			    	$('#idsArr').val(idsArr);
			    	$('#assignForm').submit();
				    top.layer.close(index);
			  },
			  cancel: function(index){ 
    	       }
		}); 
			});
		</script>
	</div>
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead><tr><th>归属学校</th><th>组织机构</th><th>登录名</th><th>姓名</th><th>手机</th><th>操作</th></tr></thead>
		<tbody>
		<c:forEach items="${userList}" var="user">
			<tr>
				<td>${user.company.name}</td>
				<td>${user.office.name}</td>
				<td><a href="${ctx}/sys/user/form?id=${user.id}">${user.loginName}</a></td>
				<td>${user.name}</td>
				<td>${user.mobile}</td>
				<td>
					<a href="${ctx}/auth/identityGroup/outIdentityGroup?userId=${user.id}&id=${group.id}" 
						onclick="return confirmx('确认要将用户<b>[${user.name}]</b>从<b>[${group.groupName}]</b>角色中移除吗？', this.href)">移除</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	</div>
</body>
</html>
