<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
  <head>
    <title>指定管理员</title>
    <meta name="decorator" content="default" />
    <%@include file="/webpage/include/treeview.jsp"%>
    <script type="text/javascript">
	var validateForm;
	function doSubmit() {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		if (validateForm.form()) {
			var ids = "";
			$("#userdata").find("li").each(function(){ 
				ids += $(this).attr('uid')+",";
			});
			$("#userids").val(ids);
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
    	.zTree_center {margin: 0 auto;height: 300px;width: 680px;margin-top: 10px;}
    	.zTree_left {height: 300px;width: 280px;overflow: auto;border: 1px solid #CECECE;border-radius:5px;float: left}
    	.zTree_right {height: 300px;width: 280px;overflow: auto;border: 1px solid #CECECE;border-radius:5px;float: right}
    	.zTree_middle {height: 300px;width: 120px;overflow: auto;float: left;position:relative;}
    	.zTree_middle_select {position:absolute;left:5%;top:35%;}
    	.zTree_middle_input input[type=button]{margin-bottom: 5px;width: 80px;margin-left: 15px;}
    	.userdata ul{list-style:none;margin: 0px;}
    	#userdata {list-style: none;margin-top: 10px;}
    </style>
</head>

<BODY>
<div class="content_wrap">
	<input type="hidden" value="${application.id}" id="appId">
	<div class="zTree_center">
		<div class="zTree_left">
			<div class="input-group input-group-sm">
			  <input type="text" class="form-control" id="userName" placeholder="名称" aria-label="用户名">
			  <div class="input-group-btn" >
			    <input type="button"  class="btn btn-primary" value="搜索" onclick="refresh()"/>
			  </div>
			</div>
	        <div id="tree" class="ztree"></div>
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
	    <form:form id="inputForm"  modelAttribute="application" action="${ctx}/ident/application/saveAuthority"
	     method="post" class="form-inline">
	     	<form:input type="hidden" path="id" />
	     	<input type="hidden" name="userids" value="" id="userids"/>
			<div class="zTree_right">
		    	<ul id="userdata">
	    		</ul>
		    </div>
		</form:form>
	</div>
	 <script type="text/javascript">
	 	var appId = $("#appId").val();
		/*初始化数据*/
		$(function(){
			initManagerData();
		});
	 	var zTree;
		var setting = {
			view: {
            	dblClickExpand: false
            },
            check: {
                enable: true
            },
            data: {
                simpleData: {
                    enable: true
                }
            },
            edit: {
                enable: false
            },
			async : {
				enable : true,
				url : "${ctx}/ident/application/tree",
				autoParam : [ "id"],
				otherParam: { "appId":appId, "userName":function(){return $("#userName").val();}},
				dataFilter: ajaxDataFilter
			},
			key : {
				name : 'name',
				children : 'children'
			},
			callback : {
				onClick : function(event, treeId, treeNode) {
					var id = treeNode.id == '0' ? '' : treeNode.id;
					if (treeNode.obj == "user") {
						$('#officeContent').attr(
								"src",
								"${ctx}/unifiedAuth/user/list?user.id="
										+ id + "&user.name="
										+ treeNode.name);
					} else {
						$('#officeContent').attr(
								"src",
								"${ctx}/unifiedAuth/office/list?office.id="
										+ id + "&office.name="
										+ treeNode.name);
					}
				},
				beforeAsync: zTreeBeforeAsync
			}
		}

		function refreshTree() {
			zTree = $.fn.zTree.init($("#tree"), setting);
		}
		
		refreshTree();

		function zTreeBeforeAsync() {
			appId = $("#appId").val();
		}
		
		function search() {
			var searchUserName = $("#searchUserName").val();
			if (searchUserName) {
				$('#officeContent').attr(
						"src",
						"${ctx}/unifiedAuth/user/userList?userName="
								+ searchUserName);
			}
		}
		
		
		function refresh() {
			var treeObj = $.fn.zTree.getZTreeObj("tree");
			treeObj.reAsyncChildNodes(null, "refresh", true);
		}
        
		/*添加*/
        function add() {
        	var zTree = $.fn.zTree.getZTreeObj("tree");
			nodes = zTree.getCheckedNodes(true);
        	for(var i=0;i<nodes.length;i++){
        		var userid = "user_"+nodes[i].id;
        		if (!nodes[i].isParent && $("#" + userid).length <= 0) {
	        		$("#userdata").append("<li id='"+userid+"' uid="+nodes[i].id+"><input type='checkbox' class='i-checks' name='user'/>"+nodes[i].name+"</li>");
					$("#userdata :checkbox").iCheck({checkboxClass:"icheckbox_square-green",radioClass:"iradio_square-green"});
					zTree.setChkDisabled(nodes[i], true);
        		}
	        }
        }
		
		
		function initManagerData() {
			var appId = $("#appId").val();
			$.ajax({
				type : "post",
				url : "${ctx}/ident/application/getManagerUser",
				data : {id:appId},
				success : function(data) {
					if (data != '') {
						$.each(data, function(e){
							var userid = "user_" + this.id;
							$("#userdata").append("<li id='"+userid+"' uid="+this.id+"><input type='checkbox' class='i-checks' name='user'/>"+this.name+"</li>");
							$("#userdata :checkbox").iCheck({checkboxClass:"icheckbox_square-green",radioClass:"iradio_square-green"});
						});
					}
				}
			});
		}
        
		/*移除*/
        function remove() {
        	//disable();
        	var ids='';
        	$("#userdata").find(":checked").each(function(){ 
				if (true == $(this).prop('checked')) {
					ids += $(this).parent("div").parent("li").attr("id");
					$(this).parent("div").parent("li").remove();
				} 
			}); 
        	var zTree = $.fn.zTree.getZTreeObj("tree");
        	nodes = zTree.getNodesByParam("chkDisabled", true);
        	for(var i=0;i<nodes.length;i++){
        		if (ids != '') {
            		if (ids.indexOf(nodes[i].id) != -1) {
            			zTree.setChkDisabled(nodes[i], false, false, false);
    		        	zTree.checkNode(nodes[i], false, false);
            		}
        		}
	        }
        }
		
		/*禁用*/
		function disable() {
			var ids='';
        	$("#userdata").find(":checked").each(function(){ 
				if (true == $(this).prop('checked')) {
					ids += $(this).parent("div").parent("li").attr("id");
					$(this).parent("div").parent("li").remove();
				} 
			}); 
			var zTree = $.fn.zTree.getZTreeObj("tree");
        	nodes = zTree.getCheckedNodes(true);
        	for(var i=0;i<nodes.length;i++){
        		zTree.setChkDisabled(nodes[i], false, true, true);
        		if (ids != '') {
            		if (ids.indexOf(nodes[i].id) != -1) {
    		        	zTree.setChkDisabled(nodes[i], false, false, true);
            		}
        		}
	        }
		}
		
		/*数据过滤*/
		function ajaxDataFilter(treeId, parentNode, responseData) {
			if (responseData) {
				var array = new Array();
				$("#userdata").find("li").each(function(e){
					array[e] = $(this).attr("uid");
				});
				for(var i =0; i < responseData.length; i++) {
					console.log($.inArray(responseData[i].id, array));
					if ($.inArray(responseData[i].id, array) > -1) {
						responseData[i].chkDisabled = true;
						responseData[i].checked = true;
            		}
			    }
		    }
			return responseData;
		}
    </script>
</div>
</body>
</html>