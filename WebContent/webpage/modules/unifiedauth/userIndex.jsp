<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>用户管理</title>
<meta name="decorator" content="default" />
<%@include file="/webpage/include/treeview.jsp"%>
<style type="text/css">
.ztree {
	overflow: auto;
	margin: 0;
	_margin-top: 10px;
	padding: 10px 0 0 10px;
}
.auth-top{background-color:#f3f3f4;border:1px solid #f3f3f4;width: 100%;margin-bottom: 10px;padding-top: 13px;border: 1px solid #f3f3f4;}
#left-ul{margin-top: 10px;}
.left-index{border: 1px solid #e7eaec;border-radius: 2px;background-color: #f3f3f4;}
</style>
<script type="text/javascript">
	function refresh() {//刷新

		window.location = "${ctx}/unifiedAuth/user";
	}
</script>
</head>
<body class="gray-bg">

	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-content">
				<sys:message content="${message}" />
				<div class="row auth-top">
					<div class="col-sm-12">
						<form:form id="searchForm" modelAttribute="user" action=""
							method="post" class="form-inline" onSubmit="return false;">
							<div class="form-group">
								<span>用户名称：</span> <input type="text"
									class=" form-control input-sm" id="searchUserName" />
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
						    <li role="presentation" class="active"><a href="#teacher" aria-controls="teacher" role="tab" data-toggle="tab">行政单位</a></li>
						    <li role="presentation"><a href="#student" aria-controls="student" role="tab" data-toggle="tab">教学单位</a></li>
						</ul>
						<div class="tab-content" style="background-color: #fff;">
						    <div role="tabpanel" class="tab-pane active" id="teacher">
								<div id="ztree" class="ztree leftBox-content"></div>
							</div>
						    <div role="tabpanel" class="tab-pane" id="student">
								<div id="ztree2" class="ztree leftBox-content"></div>
							</div>
						</div>
					</div>
					<div id="right" class="col-sm-11  animated fadeInRight">
						<iframe id="officeContent" name="officeContent" src=""
							width="100%" height="91%" frameborder="0"></iframe>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		$(function(){
			$("#left-ul a").click(function(){
				var msg = $(this).attr("aria-controls");
				if ("teacher" == msg) {
					type = 1;
				} else if ("student" == msg) {
					type = 2;
				}
			});
			
		});
		
		var zTree, zTree2, initValue=1, type=1;
		var setting = {
			check : {
				enable : false
			},
			data : {

			},
			async : {
				enable : true,
				url : "${ctx}/unifiedAuth/user/tree",
				autoParam : [ "id" ],
				otherParam: {"type":function(){return type}},
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
								"${ctx}/unifiedAuth/user/list?user.id=" + id
										+ "&user.name=" + treeNode.name);
					} else {
// 						$('#officeContent').attr(
// 								"src",
// 								"${ctx}/unifiedAuth/office/list?office.id="
// 										+ id + "&office.name=" + treeNode.name);
					}
				}
			}
		}
		
		/*参数回调方法*/
		function ajaxDataFilter(treeId, parentNode, responseData) {
			if (responseData) {
				if (initValue == 1) {
					initValue = 2;
					initMsg(responseData[0].id, responseData[0].name);
				}
			}
			return responseData;
		}
		
		/*初始化信息*/
		function initMsg(id, name) {
			$('#officeContent').attr(
					"src",
					"${ctx}/unifiedAuth/office/list?office.id="
							+ id + "&office.name=" + name);
		}
		

		function refreshTree() {
			zTree = $.fn.zTree.init($("#ztree"), setting);
		}
		
		function refreshTree2() {
			zTree2 = $.fn.zTree.init($("#ztree2"), setting);
		}
		refreshTree();
		refreshTree2();
		function search() {
			var searchUserName = $("#searchUserName").val();
			if (searchUserName) {
				$('#officeContent').attr(
						"src",
						"${ctx}/unifiedAuth/user/userList?userName="
								+ searchUserName);
			}
		}

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
	</script>
	<script src="${ctxStatic}/common/wsize.min.js" type="text/javascript"></script>
</body>
</html>