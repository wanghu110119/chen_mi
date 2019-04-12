<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>用户管理</title>
<meta name="decorator" content="default" />
<%@include file="/webpage/include/treeview.jsp"%>
<style type="text/css">
.auth-top{background-color:#f3f3f4;border:1px solid #f3f3f4;width: 100%;margin-bottom: 10px;padding-top: 13px;border: 1px solid #f3f3f4;}
#left-ul{margin-top: 10px;}
.left-index{border: 1px solid #e7eaec;border-radius: 2px;background-color: #f3f3f4;}
.ztree {
	overflow: auto;
	margin: 0;
	_margin-top: 10px;
	padding: 10px 0 0 10px;
}
</style>
<script type="text/javascript">
	function refresh() {//刷新
		window.location = "${ctx}/unifiedAuth/role";
	}
</script>
</head>
<body class="gray-bg">

	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-content">
				<sys:message content="${message}" />

				<!-- 查询条件 -->
				<div class="row auth-top">
					<div class="col-sm-12">
						<form:form id="searchForm" modelAttribute="user" action=""
							method="post" class="form-inline" onSubmit="return false;">
							<div class="form-group">
								<span>角色名称：</span> <input type="text"
									class=" form-control input-sm" id="searchRoleName" />
								<button class="btn btn-primary btn-rounded btn-outline btn-sm "
									onclick="search()">
									<i class="fa fa-search"></i> 查询
								</button>
							</div>
						</form:form>
						<br />
					</div>
				</div>

				<div id="content" class="row">
					<div id="left" class="leftBox col-sm-1 left-index">
						<div style="height: 10px;">
							<a onclick="refresh()" class="pull-right" style="margin-right: 10px;"> <i
								class="fa fa-refresh"></i>
							</a>
						</div>
						<ul class="nav nav-tabs" role="tablist" id="left-ul">
						    <li role="presentation" class="active"><a href="#role" aria-controls="role" role="tab" data-toggle="tab">角色</a></li>
						</ul>
						<div class="tab-content" style="background-color: #fff;">
						    <div role="tabpanel" class="tab-pane active" id="role">
								<div id="ztree" class="ztree leftBox-content"></div>
							</div>
						</div>
					</div>
					<div id="right" class="col-sm-11  animated fadeInRight">
						<iframe id="authRoleContent" name="authRoleContent" src=""
							width="100%" height="91%" frameborder="0"></iframe>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		var setting = {
			data : {
				simpleData : {
					enable : true,
					idKey : "id",
					pIdKey : "pId",
					rootPId : '0'
				}
			},
			callback : {
				onClick : function(event, treeId, treeNode) {
					var id = treeNode.id == '0' ? '' : treeNode.id;
					$('#authRoleContent').attr(
							"src",
							"${ctx}/unifiedAuth/role/list?id=" + id + "&name="
									+ treeNode.name);
				}
			}
		};

		function refreshTree() {
			$.getJSON("${ctx}/sys/role/treeData", function(data) {
				$.fn.zTree.init($("#ztree"), setting, data).expandAll(true);
				var treeObj = $.fn.zTree.getZTreeObj("ztree");
				var nodes = treeObj.getNodes();
				if (nodes.length > 0) {
					var id = nodes[0].id;
					var name = nodes[0].name;
					$('#authRoleContent').attr(
							"src",
							"${ctx}/unifiedAuth/role/list?id=" + id + "&name="
									+ name);
				}
			});
		}
		refreshTree();

		var leftWidth = 230; // 左侧窗口大小
		var htmlObj = $("html"), mainObj = $("#main");
		var frameObj = $("#left, #openClose, #right, #right iframe");
		function wSize() {
			var strs = getWindowSize().toString().split(",");
			htmlObj.css({
				"overflow-x" : "hidden",
				"overflow-y" : "hidden"
			});
			mainObj.css("width", "auto");
			frameObj.height(strs[0] - 120);
			var leftWidth = ($("#left").width() < 0 ? 0 : $("#left").width());
			$("#right").width(
					$("#content").width() - leftWidth - $("#openClose").width()
							- 65);
			$(".ztree").width(leftWidth - 10).height(frameObj.height() - 80);
		}

		function search() {
			var searchRoleName = $("#searchRoleName").val();
			if (searchRoleName) {
				$('#authRoleContent').attr(
						"src",
						"${ctx}/unifiedAuth/role/roleList?roleName="
								+ searchRoleName);
			}
		}
	</script>
	<script src="${ctxStatic}/common/wsize.min.js" type="text/javascript"></script>
</body>
</html>