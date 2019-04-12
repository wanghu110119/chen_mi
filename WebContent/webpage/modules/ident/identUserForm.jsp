<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
  <head>
    <title>指定管理成员</title>
    <meta name="decorator" content="default" />
    <script type="text/javascript">
	var validateForm;
	function doSubmit() {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		if (validateForm.form()) {
			var ids = "";
			$("#addUserData").find("li").each(function(){ 
				ids += $(this).attr('uid')+",";
			});
			$("#ids").val(ids);
			$("#type").val(type);
			$("#inputForm").submit();
			return true;
		}
		return false;
	}
	$(document).ready(
		function() {
		validateForm = $("#inputForm").validate({
							submitHandler : function(form) {
								loading('正在提交，请稍等...');
								form.submit();
							}
						});
	});
    </script>
    <style type="text/css">
    	.content_wrap {margin: 0 auto;}
    	.zTree_center {margin: 0 auto;height: 360px;width: 680px;margin-top: 10px;}
    	.zTree_left {height: 380px;width: 280px;overflow: auto;border: 1px solid #CECECE;border-radius:5px;float: left}
    	.zTree_right {height: 380px;width: 280px;overflow: auto;border: 1px solid #CECECE;border-radius:5px;float: right}
    	.zTree_middle {height: 380px;width: 120px;overflow: auto;float: left;position:relative;}
    	.zTree_middle_select {position:absolute;left:18%;top:35%;}
    	.zTree_middle_input input[type=button]{margin-bottom: 10px;width: 80px;}
    	.option {margin-bottom: 10px;margin-top: 10px;text-align: center;}
    	.userdata {padding: 10px 10px 10px 10px;height: 280px;overflow: auto;margin-top: 10px;}
    	.userdata ul{list-style:none;margin: 0px;}
    	#addUserData {list-style:none;margin-top: 10px;}
    	#pageParam button{margin-right: 5px;}
    </style>
</head>

<BODY>
<div class="content_wrap">
	<div class="zTree_center">
		<div class="zTree_left">
			<div class="option">
				<input type="button" class="btn btn-primary btn-xs" value="用户" onclick="checkauth('1',this)"/>
		        <input type="button" class="btn btn-default btn-xs" value="角色" onclick="checkauth('2',this)"/>
		        <input type="button" class="btn btn-default btn-xs" value="用户组" onclick="checkauth('3',this)"/>
		        <input type="button" class="btn btn-default btn-xs" value="组织机构" onclick="checkauth('4',this)"/>
			</div>
			<div class="input-group input-group-sm">
			  <input type="text" class="form-control" id="name" placeholder="名称" aria-label="用户名">
			  <div class="input-group-btn">
			    <input type="button" class="btn btn-primary" value="搜索" onclick="search()"/>
			  </div>
			</div>
			<div class="userdata">
				<ul id="userdata">
				</ul>
				<div align="center" id="pageParam">
					
				</div>
			</div>
	    </div>
	    <div class="zTree zTree_middle">
	    	<div class="zTree_middle_select">
	    		<div class="zTree_middle_input">
			    	<input type="button" class="btn btn-primary btn-xs" value="添加" onclick="add()">
	    		</div>
	    		<div class="zTree_middle_input">
		    		<input type="button" class="btn btn-danger btn-xs" value="移除" onclick="remove()">
	    		</div>
	    	</div>
	    </div>
	    <form:form id="inputForm"  modelAttribute="application" action="${ctx}/ident/application/saveUserAuthority"
	     method="post" class="form-inline">
	     	<form:input type="hidden" path="id" />
	     	<input type="hidden" name="ids" value="" id="ids"/>
	     	<input type="hidden" name="type" value="" id="type"/>
			<div class="zTree_right">
		    	<ul id="addUserData">
	    		</ul>
		    </div>
		</form:form>
	</div>
	 <script type="text/javascript">
	 	var type = '1';
		/*添加*/
        function add() {
        	$(".userdata").find(":checked").each(function(){ 
				if (true == $(this).prop('checked')) {
					var id = $(this).attr("id");
					var uid = id.substring(3,id.length);
					var userid = "user_" + id;
					var name = $(this).attr("uname");
					if ($("#" + userid).length <= 0) {
						$("#addUserData").append("<li id='"+userid+"' uid='"+uid+"'><input type='checkbox' class='i-checks' id='up_"+id+"'  name='user'/>"+name+"</li>");
						$("#addUserData :checkbox").iCheck({checkboxClass:"icheckbox_square-green",radioClass:"iradio_square-green"});
					}
				} 
			});
        }
        
		/*移除*/
        function remove() {
        	var ids='';
        	$("#addUserData").find(":checked").each(function(){ 
				if (true == $(this).prop('checked')) {
					var id = $(this).attr("id");
					id = id.substring(3, id.length)
					$("#" + id).iCheck('uncheck');
					$("#user_" + id).remove();
				} 
			}); 
        }
		
		$(function(){
			search();
		});
		
		function search() {
			$("#userdata").empty();
			$("#pageParam").empty();
			var name = $("#name").val();
			$.ajax({
				url:"${ctx}/ident/application/authlist",
				type: "post",
				data: {type:type, name:name, pageNo:pageNo,
					pageSize:pageSize},
				async : false,
				success:function(data){
					if (data != "" && data.length > 0) {
						var count = 0;
						$.each(data, function(){
							count = this.count;
							$("#userdata").append("<li id='"+this.id+"'><input type='checkbox' class='i-checks' id='up_"
									+this.id+"' uname='"+this.name+"' />"+this.name+"</li>");
							$("#userdata :checkbox").iCheck({checkboxClass:"icheckbox_square-green",radioClass:"iradio_square-green"});
						})
						if (type == "1") {
							var html = "<button class='btn btn-primary btn-xs' onclick='page(1)'>首页</button>"
								  +"<button class='btn btn-primary btn-xs' onclick='page("+(count==1?1:pageNo==count?count:pageNo-1<=1?1:pageNo-1)+")'>上一页</button>"
								  +"<button class='btn btn-primary btn-xs' onclick='page("+(count==1?1:pageNo==count?count:pageNo+1>=count?count:pageNo+1)+")'>下一页</button>"
								  +"<button class='btn btn-primary btn-xs' onclick='page("+count+")'>尾页</button>";
							$("#pageParam").html(html);
						}
					}
				}
			});
		}
		
		function checkauth(param, obj) {
			if (type != param) {
				$("#addUserData").empty();
				$("#userdata").empty();
			}
			type = param;
			$(".option").find("input").each(function(){
				var style = $(this).attr("class");
				if (style.indexOf("primary") >= 0) {
					$(this).attr("class", "btn btn-default btn-xs");
				}
			});
			$(obj).attr("class", "btn btn-primary btn-xs");
			search();
		}
		
		var pageNo=1,pageSize=10;
		function page(size) {
			pageNo = size;
			search();
		}
    </script>
</div>
</body>
</html>