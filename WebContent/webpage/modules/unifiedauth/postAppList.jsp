]<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>角色授权（应用）</title>
	<meta name="decorator" content="default"/>
</head>
<body>
	<div class="wrapper wrapper-content ">
	<sys:message content="${message}"/>
	
	<div class="row">
	<div class="col-sm-12">
		<div class="form-group">
			<span>${post.name }</span>
			<input type="hidden" id="postId" value="${post.id}" />
			<input type="hidden" id="postName" value="${post.name}" />
		</div>	
	<br/>
	</div>
	</div>	
			<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
				<thead>
					<tr>
						<th class="sort-column login_name">应用名称</th>
						<th class="sort-column name">系统配置</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${list}" var="postAuth">
						<tr appid="${postAuth.apply.id}">
							<td>
									${postAuth.apply.name}
							</td>
							<td>
								<c:if
									test="${not empty postAuth.isEdit and postAuth.isEdit eq '2'}">
									<span><input type="checkbox" isEdit="${postAuth.isEdit}" class="i-checks accessAuth"
										${postAuth.closeType eq '1' ? '':'checked=checked'}>访问权限</span>
								</c:if> 
								<c:if
									test="${not empty postAuth.accessAuth and empty postAuth.isEdit or postAuth.isEdit eq '1'}">
									<input type="checkbox" class="i-checks accessAuth"
										 checked="checked">访问权限</c:if> <c:if
									test="${empty postAuth.accessAuth and empty postAuth.isEdit or postAuth.isEdit eq '1'}">
									<input type="checkbox" class="i-checks accessAuth">访问权限</c:if>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		
		<shiro:hasPermission name="unifiedAuth:post:save">
			<c:if test="${ not empty list}">
				<input type="button" value="提交" onclick="save()" class="btn btn-primary "/>
			</c:if>
		</shiro:hasPermission>
	</div>
<script type="text/javascript">
	function save(){
		var trs=$("#contentTable tbody").find("tr");
		   var list=[],close = [];
		   var postId = $("#postId").val();
		   for(var i=0;i<trs.length;i++){
			   var tr=$(trs[i]);
			   var accessAuth=tr.find(".accessAuth").is(':checked');
			   if(accessAuth){
				   list.push({
					   postId:postId,
					   applyId:tr.attr("appid"),
					   accessAuth:"1"
				   });
			   } else {
				   var msg = tr.find(".accessAuth").attr("isEdit");
					if (msg == 2) {
						close.push({
							postId : postId,
							applyId : tr.attr("appid"),
							accessAuth : ""
						});
					}
			   }
		   }
	        $.ajax({
	            type: "POST",
	            url: "${ctx}/unifiedAuth/post/save",
	            data : {
	            	authPostsForm:JSON.stringify(list),
	            	closePosts : JSON.stringify(close),
	            	id:$("#postId").val(),
	            },
	            dataType: "JSON",
	            success: function(data){
	            	 if(data.success){
	                	  layer.msg('保存成功', {
	                          icon : 1,
	                          time : 2000
	                      });
	                  }else{
	                	  layer.msg('保存出错', {
	                          icon : 5,
	                          time : 2000
	                      });
	                  }
	            }
	        });
		}
	</script>
</body>
</html>