<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>我的应用</title>
<meta name="decorator" content="default" />
<link href="${ctxStatic}/common/css/animate.css" rel="stylesheet">
<style type="text/css">
#module_list, #module_list {
	margin-left: 4px
}

.contact-box {
	width: 120px;
	height: 120px;
	padding: 0px;
	display: table-cell;
	vertical-align: middle;
	text-align: center;
	padding: 5px;
	border-radius: 3px;
}

.modules {
	float: left;
	margin: 10px;
	border: 1px solid #e7eaec;
}

.m_title, .m_title1 {
	background-color: #ffffff;
	text-align: center;
	position: relative;
}

#loader, #loader1 {
	height: 24px;
	text-align: center
}

.leftprolist {
	z-index: 1000;
	position: absolute;
	width: 120px;
	height: 30px;
	top: 90px;
	left: 0px;
	display: none;
	background-color: rgba(0, 0, 0, 0.45);
}

.leftprolist a {
	margin-top: 5px;
	position: relative;
	z-index: 1111;
	color: white;
}

img {
	width: 120px;
	height: 120px;
	vertical-align: middle;
}

table {
	width: 800px;
	margin-bottom: 20px;
}

.div-title {
	font-size: 12px;
	width: 120px;
	color: #666;
	text-align: center;
	margin-top: 10px;
}

ul li {
	float: left;
	list-style: none;
	margin-right: 30px;
	margin-bottom: 10px;
}

.app-title {
	float: left;
	width: 100%;
	margin: 10px 0px 10px 0px;
	vertical-align: middle;
}

.app-content {
	float: left;
	width: 100%;
}

.app-title button {
	margin-left: 20px;
	vertical-align: middle;
}
.app-message {
	width: 90%;
	margin: 0px auto;
	text-align: center;
}
.selectedDiv {
	z-index: 1000;
	position: absolute;
	width: 18px;
	height: 18px;
	border-radius: 2px;
	cursor:pointer;
	top: 0px;
	left: 0px;
	text-align:center;
	display:none;
	background-color: rgba(26, 179, 148, 0.6);
}
.selectedDiv i{
	padding:0px;
	margin:0px;
	color: #fff;
	font-size: 12px;
}
#sysDeleteAll, #userDeleteAll{
	display: none;
}
</style>
</head>

<body class="gray-bg">
	<div class="app-message"><sys:message content="${message}" /></div>
	<div class="wrapper wrapper-content animated fadeInRight">
		<div class="app-index">
			<div class="alert alert-warning alert-dismissible" style="display: none;" role="alert">
			  <strong>提示!</strong> 系统应用、自定义应用处于编辑状态，鼠标移动到应用图标即可操作。
			</div>
			<div class="app-title">
				<span style="font-size: 20px;">系统应用(<c:out value="${sysSize}" />)
				</span>
				
					<button id="tool" class="btn btn-primary btn-sm">
						<i class="fa fa-cog"></i> 应用管理
					</button>
				<shiro:hasPermission name="ident:application:edit">
					<button class="btn btn-warning btn-sm" id="sysOrder" onclick="saveOrder(1)">
						<i class="fa fa-sort"></i> 保存排序
					</button>
					
					<button class="btn btn-danger btn-sm" id="sysDeleteAll" onclick="deleteAll(1)">
						<i class="fa fa-trash-o"></i> 批量删除
					</button>
				</shiro:hasPermission>
				<button class="btn btn-info btn-sm" onclick="refresh()">
					<i class="fa fa-refresh"></i> 刷新
				</button>
			</div>
			<div class="app-content">
				<div id="module_list">
					<ul>
						<c:forEach items="${system}" var="app" varStatus="status">
							<li>
								<div class="contact-box m_title" appId="${app.id }">
									<div class="selectedDiv" value="1" sid="${app.id }"></div>
									<a href="${app.url}" target="_blank" aid="${app.id}"> <c:choose>
											<c:when test="${fns:contains(app.pic,'static')}">
												<img class="img-thumbnail"
													src="${ctxStatic}${fns:substringAfterLast(app.pic, 'static')}">
											</c:when>
											<c:otherwise>
												<img class="img-thumbnail"
													src="${ctx}/ident/application/download?pic=${app.pic}">
											</c:otherwise>
										</c:choose>
									</a>
									<div class="leftprolist" appId="${app.id}" type="1">
										<shiro:hasPermission name="ident:application:edit">
											<a href="#"
											onclick="openDialog('修改应用', '${ctx}/ident/application/form?id=${app.id}&modifyType=2','800px', '500px')"
											class="btn btn-primary btn-xs">编辑</a>
										</shiro:hasPermission>
										<shiro:hasPermission name="ident:application:del">
											<a
											href="${ctx}/ident/application/deleteByLogic?ids=${app.id}&modifyType=2"
											onclick="return confirmx('确认要删除该应用吗？', this.href)"
											class="btn btn-danger btn-xs">删除</a>
										</shiro:hasPermission>
									</div>
								</div>
								<div class="div-title">${app.name}</div>
							</li>
						</c:forEach>
					</ul>
				</div>
			</div>
		</div>
		<div class="app-index">
			<div class="app-title">
				<span style="font-size: 20px;">自定义应用(<c:out
						value="${selfSize}" />)
				</span>
				
				<button class="btn btn-danger btn-sm" id="userDeleteAll" onclick="deleteAll(2)">
					<i class="fa fa-trash-o"></i> 批量删除
				</button>
				<shiro:hasPermission name="ident:userapplication:edit">
					<button class="btn btn-warning btn-sm" id="myOrder" onclick="saveOrder(2)">
						<i class="fa fa-cog"></i> 保存排序
					</button>
				</shiro:hasPermission>
			</div>
			<div class="app-content">
				<div id="module_list1">
					<ul>
						<c:forEach items="${self}" var="app" varStatus="status">
							<li>
								<div class="contact-box m_title" appId="${app.id }">
									<div class="selectedDiv" value="1" sid="${app.id }"></div>
									<a href="${app.url}" target="_blank"> <c:choose>
											<c:when test="${fns:contains(app.pic,'static')}">
												<img class="img-thumbnail"
													src="${ctxStatic}${fns:substringAfterLast(app.pic, 'static')}">
											</c:when>
											<c:otherwise>
												<img class="img-thumbnail"
													src="${ctx}/ident/application/download?pic=${app.pic}">
											</c:otherwise>
										</c:choose>
									</a>
									<div class="leftprolist" type="2">
										<a href="#"
											onclick="openDialog('修改应用', '${ctx}/ident/userapplication/form?id=${app.id}&modifyType=2','800px', '500px')"
											class="btn btn-primary btn-xs">编辑</a> 
										<a
											href="${ctx}/ident/userapplication/deleteByLogic?ids=${app.id}&modifyType=2"
											onclick="return confirmx('确认要删除该应用吗？', this.href)"
											class="btn btn-danger btn-xs">删除</a>
									</div>
								</div>
								<div class="div-title">${app.name}</div>
							</li>
						</c:forEach>
					</ul>
				</div>
			</div>
		</div>
	</div>

	<script>
		var value = 1;
		var sysOrder = "", myOrder = "";
		var managerIds =  new Array();
		var sysSelected = "", userSelected = "";
		$("#sysOrder").hide();
		$("#myOrder").hide();
		$(document).ready(function() {
			/*移入移出效果*/
			$('.contact-box').each(function() {
				animationHover(this, 'pulse');
			});
			$(".m_title").bind('mouseover', function() {
				if (value == 2) {
					var id = $(this).find(".leftprolist").attr("appId");
					var type = $(this).find(".leftprolist").attr("type");
					if ($.inArray(id,managerIds) > -1 || "2" == type) {
						$(this).find(".leftprolist").show();
						$(this).find(".selectedDiv").show();
					}
				}
			});
			$(".m_title").bind('mouseout', function() {
				if (value == 2) {
					$(this).find(".leftprolist").hide();
				}
			});

			/*判断是否应用管理*/
			$("#tool").on("click", function() {
				if (value == 1) {
					value = 2;
					$.ajax({
						type : "post",
						url : "${ctx}/ident/application/getUserApplication",
						data : {},
						success : function(data) {
							if (data != '') {
								$.each(data, function(e){
									managerIds[e] = this.id;
								});
							}
						}
					});
					$(".alert").show(1000);
// 					$("#module_list").find(".selectedDiv").show(1000);
				} else {
					value = 1;
					$(".selectedDiv").hide();
					$(".alert").hide(1000);
				}
			});
			
			/*系统批量选中*/
			$("#module_list").find(".selectedDiv").click(function() {
				changeSelect(this, 1);
				if (sysSelected == "") {
					$("#sysDeleteAll").hide(1000);
				} else {
					$("#sysDeleteAll").show(1000);
				}
			});
			
			/*自定义批量选中*/
			$("#module_list1").find(".selectedDiv").click(function() {
				changeSelect(this, 2);
				if (userSelected == "") {
					$("#userDeleteAll").hide(1000);
				} else {
					$("#userDeleteAll").show(1000);
				}
			});
			
			/*点击禁用*/
			$(".m_title").children("a").click(function(event){
				if (value == 2) {
					event.preventDefault();
				}
			});
			
			/*系统访问*/
			$("#module_list").find("a").click(function(){
				var id = $(this).attr("aid");
				$.ajax({
					type : "post",
					url : "${ctx}/ident/userapplication/record",
					data : {id : id}, 
					success : function(data) {
					}
				});
			});
		});
		
		/*改变选中事件*/
		function changeSelect(obj, type) {
			var value = $(obj).attr("value");
			var sid = $(obj).attr("sid");
			if (value == 1) {
				$(obj).append("<i class='fa fa-check'></i>");
				$(obj).attr("value","2");
				if (type == 1) {
					sysSelected += sid + ",";
				} else if (type == 2) {
					userSelected += sid + ",";
				}
			} else if (value == 2) {
				$(obj).empty();
				$(obj).attr("value","1");
				if (type == 1) {
					sysSelected = sysSelected.replace((sid + ","), "");
				} else if (type == 2) {
					userSelected = userSelected.replace((sid + ","), "");
				}
			}
		}

		/*系统应用排序*/
		$(function() {
			var $sysList = $("#module_list ul");
			$sysList.sortable({
				opacity : 0.6, //设置拖动时候的透明度
				revert : true, //缓冲效果
				cursor : 'move', //拖动的时候鼠标样式 
				handle : '.m_title', //可以拖动的部位，模块的标题部分 
				update : function() {
					sysOrder = "";
					$("#sysOrder").show(1000);
					$sysList.find(".m_title").each(
						function() {
							if (sysOrder != null && sysOrder != ""
									&& sysOrder != undefined) {
								sysOrder = sysOrder + ","
										+ $(this).attr("appId");
							} else {
								sysOrder = $(this).attr("appId");
							}
					});
				}
			});
		});

		/*自定义应用排序*/
		$(function() {
			var $myList = $("#module_list1 ul");
			$myList.sortable({
				opacity : 0.6, //设置拖动时候的透明度
				revert : true, //缓冲效果
				cursor : 'move', //拖动的时候鼠标样式 
				handle : '.m_title', //可以拖动的部位，模块的标题部分 
				update : function() {
					myOrder = "";
					$("#myOrder").show(1000);
					$myList.find(".m_title").each(
							function() {
								if (myOrder != null && myOrder != ""
										&& myOrder != undefined) {
									myOrder = myOrder + ","
											+ $(this).attr("appId");
								} else {
									myOrder = $(this).attr("appId");
								}
					});
				}
			});
		});

		/*应用排序保存*/
		function saveOrder(type) {
			layer.confirm('确认要修改应用排序吗？', {
				  btn: ['保存','取消']
			}, function(){
				var url = "";
				var new_order = "";
				if (type == 1) {
					url = "${ctx}/ident/application/order";
					new_order = sysOrder;
				} else {
					url = "${ctx}/ident/userapplication/order";
					new_order = myOrder;
				}

				$.ajax({
					type : "post",
					url : url,
					data : {ids : new_order}, 
					success : function(data) {
						layer.msg(data.msg, {
							icon : 1,
							time : 2000
						});
						refresh();
					}
				});
			});
		}
		
		/*刷新页面*/
		function refresh() {
			window.location.href="${ctx}/ident/userapplication/card";
		}
		
		/*批量删除应用*/
		function deleteAll(type) {
			layer.confirm('确定要删除所选应用？', {
				  btn: ['确定','取消']
			}, function(){
				var url, ids;
				if (type == 1) {
					url = "${ctx}/ident/application/deleteAllByLogic";
					ids = sysSelected;
				} else if (type == 2) {
					url = "${ctx}/ident/userapplication/deleteAllByLogic";
					ids = userSelected;
				}
				$.ajax({
					type : "post",
					url : url,
					data : {ids : ids}, 
					success : function(data) {
						layer.msg(data.msg, {
							icon : 1,
							time : 2000
						}, function(){
							refresh();
						});
					}
				});
			});
		}
		
	</script>

</body>

</html>