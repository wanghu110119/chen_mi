<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>用户管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/webpage/include/treeview.jsp" %>
	<style type="text/css">
		.ztree {overflow:auto;margin:0;_margin-top:10px;padding:10px 0 0 10px;}
	</style>
	<script type="text/javascript">
		function refresh(){//刷新
			
			window.location="${ctx}/account/student/index";
		}
	</script>
</head>
<body class="gray-bg">
	
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-content">
	<sys:message content="${message}"/>
	<div id="content" class="row">
		<div id="left"  style="background-color:#e7eaec" class="leftBox col-sm-1">
			<div class="input-group input-group-sm" style="margin-top: 10px;">
			  <input type="text" class="form-control" id="userName" placeholder="名称" aria-label="用户名">
			  <div class="input-group-btn" >
			    <input type="button"  class="btn btn-primary" value="搜索" onclick="refreshTree()"/>
			  </div>
			</div>
			<div id="ztree" class="ztree leftBox-content"></div>
		</div>
		<div id="right"  class="col-sm-11  animated fadeInRight">
			<iframe id="studentContent" name="studentContent" src="${ctx}/account/student/list" width="100%" height="91%" frameborder="0"></iframe>
		</div>
	</div>
	</div>
	</div>
	<script type="text/javascript">
		var setting = {data:{simpleData:{enable:true,idKey:"id",pIdKey:"pId",rootPId:'0'}},
			callback:{onClick:function(event, treeId, treeNode){
					var id = treeNode.id == '0' ? '' :treeNode.id;
					$('#studentContent').attr("src","${ctx}/account/student/list?officeId="+id+"&officeParentIds="+treeNode.pIds);
				}
			}
		};
		function refreshTree() {
			var userName = $("#userName").val();
            $.getJSON("${ctx}/sys/office/treeData2?name="+userName, function(data) {
                $.fn.zTree.init($("#ztree"), setting, data);
            });
        }
		refreshTree();
		 
		var leftWidth = 180; // 左侧窗口大小
		var htmlObj = $("html"), mainObj = $("#main");
		var frameObj = $("#left, #openClose, #right, #right iframe");
		function wSize(){
			var strs = getWindowSize().toString().split(",");
			htmlObj.css({"overflow-x":"hidden", "overflow-y":"hidden"});
			mainObj.css("width","auto");
			frameObj.height(strs[0] - 120);
			var leftWidth = ($("#left").width() < 0 ? 0 : $("#left").width());
			$("#right").width($("#content").width()- leftWidth - $("#openClose").width() -60);
			$(".ztree").width(leftWidth - 10).height(frameObj.height() - 46);
		}
	</script>
	<script src="${ctxStatic}/common/wsize.min.js" type="text/javascript"></script>
</body>
</html>