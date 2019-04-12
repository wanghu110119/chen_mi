<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>个人信息</title>
<meta name="decorator" content="default" />
<style type="text/css">
.top-base {
	height: 150px;
	background-color: #fff;
	padding: 10px;
	border: 1px solid #F0F0F0;
}

.top-base img {
	width: 80px;
	height: 80px;
}

.top-base ul {
	width: 100%;
	height: 130px;
	text-align: center;
}

.top-base ul li {
	float: left;
	list-style: none;
	width: 30%;
	height: 130px;
	border-right: 2px solid #F0F0F0;
}

.top-title {
	padding: 5px 0px 5px 0px;
	font-size: 16px;
}

.top-message {
	padding: 5px 0px 5px 0px;
	color: #F15F5C;
	font-size: 12px;
}

.top-message span {
	font-size: 28px;
	font-weight: bold;
}

.top-detail a {
	display: block;
	width: 120px;
	height: 35px;
	border: 1px solid #F0F0F0;
	text-align: center;
	margin: 0px auto;
	font-size: 12px;
	line-height: 35px;
	cursor: pointer;
	color: #676a6c;
}

.top-detail {
	margin: 0px auto;
}

.top-detail a:hover {
	border: 1px solid #1ab394;
	background-color: #1ab394;
	color: #fff;
	border-radius: 3px;
}

.top-left {
	width: 100%;
	height: 130px;
}

.top-user {
	width: 70%;
	height: 130px;
	float: left;
	margin: 0px auto;
	padding: 5px 5px 5px 5px;
	text-align: left;
}

.top-img {
	width: 30%;
	height: 130px;
	float: left;
	margin: 0px auto;
	padding: 5px 5px 5px 5px;
	text-align: center;
}

.top-user img {
	float: right;
}

.top-user-msg {
	width: 100%;
	height: 25px;
	line-height: 25px;
}

.top-user-title {
	font-size: 13px;
	font-weight: bold;
	font-family: sans-serif;
}

.detail-base {
	padding: 15px;
	margin-top: 30px;
	width: 100%;
	height: 350px;
	background-color: #fff;
	border: 1px solid #F0F0F0;
}

.detail-title {
	width: 100%;
	height: 35px;
	border-bottom: 1px solid #F0F0F0;
}

.detail-title-left {
	width: 10%;
	float: left;
	height: 35px;
	line-height: 35px;
	font-size: 16px;
	font-family: sans-serif;
	font-weight: bold;
}

.detail-title-right {
	width: 90%;
	float: right;
	height: 35px;
	line-height: 35px;
	text-align: right;
}

.detail-content {
	margin-top: 30px;
}

.detail-code img {
	width: 150px;
	height: 150px;
}
</style>
<script type="text/javascript">
		$(document).ready(function() {
			if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端，就使用自适应大小弹窗
				width='auto';
				height='auto';
			}else{//如果是PC端，根据用户设置的width和height显示。
				width='700px';
				height='500px';
			}

			$("#userPassWordBtn").click(function(){
				top.layer.open({
				    type: 2, 
				    area: [width, height],
				    title:"修改密码",
				    content: "${ctx}/sys/user/modifyPwd" ,
				    btn: ['确定', '关闭'],
				    yes: function(index, layero){
				    	 var body = top.layer.getChildFrame('body', index);
				         var inputForm = body.find('#inputForm');
				         var btn = body.find('#btnSubmit');
				         var top_iframe = top.getActiveTab().attr("name");//获取当前active的tab的iframe 
				         inputForm.attr("target",top_iframe);//表单提交成功后，从服务器返回的url在当前tab中展示
				         inputForm.validate({
								rules: {
								},
								messages: {
									confirmNewPassword: {equalTo: "输入与上面相同的密码"}
								},
								submitHandler: function(form){
									loading('正在提交，请稍等...');
									form.submit();
									
								},
								errorContainer: "#messageBox",
								errorPlacement: function(error, element) {
									$("#messageBox").text("输入有误，请先更正。");
									if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
										error.appendTo(element.parent().parent());
									} else {
										error.insertAfter(element);
									}
								}
							});
					     if(inputForm.valid()){
				        	  loading("正在提交，请稍等...");
				        	  inputForm.submit();
				        	  top.layer.close(index);//关闭对话框。
				          }else{
					          return;
				          }
						
						
					  },
					  cancel: function(index){ 
		    	       }
				}); 
			});
			
			$("#userInfoBtn").click(function(){
				top.layer.open({
				    type: 2,  
				    area: [width, height],
				    title:"个人信息编辑",
				    content: "${ctx}/sys/user/infoEdit" ,
				    btn: ['确定', '关闭'],
				    yes: function(index, layero){
				    	 var body = top.layer.getChildFrame('body', index);
				         var inputForm = body.find('#inputForm');
				         var top_iframe = top.getActiveTab().attr("name");//获取当前active的tab的iframe 
				         inputForm.attr("target",top_iframe);//表单提交成功后，从服务器返回的url在当前tab中展示
				         inputForm.validate();
				         if(inputForm.valid()){
				        	  loading("正在提交，请稍等...");
				        	  inputForm.submit();
				          }else{
					          return;
				          }
				        
						 top.layer.close(index);//关闭对话框。
						
					  },
					  cancel: function(index){ 
		    	       }
				}); 
			});

			$("#userImageBtn").click(function(){
				top.layer.open({
				    type: 2,  
				    area: [width, height],
				    title:"上传头像",
				    content: "${ctx}/sys/user/imageEdit" ,
				  //  btn: ['确定', '关闭'],
				    yes: function(index, layero){
				    	 var body = top.layer.getChildFrame('body', index);
				         var inputForm = body.find('#inputForm');
				         var top_iframe = top.getActiveTab().attr("name");//获取当前active的tab的iframe 
				         inputForm.attr("target",top_iframe);//表单提交成功后，从服务器返回的url在当前tab中展示
				         inputForm.validate();
				         if(inputForm.valid()){
				        	  loading("正在提交，请稍等...");
				        	  inputForm.submit();
				          }else{
					          return;
				          }
				        
						 top.layer.close(index);//关闭对话框。
						
					  },
					  cancel: function(index){ 
		    	       }
				}); 
			});
			
		});
		$(function(){
			var date = new Date();
			$("#dayHello").html(getDayHello(date.getHours()));
			
		});
		
		function getDayHello(hour) {
			var msg = "夜里好";
			if(hour < 6){
				msg = "凌晨好";
			} else if (hour < 9) {
				msg = "早上好";
			} else if (hour < 12) {
				msg =  "上午好";
			} else if (hour < 14) {
				msg = "中午好";
			} else if (hour < 17) {
				msg = "下午好";
			} else if (hour < 19) {
				msg = "傍晚好";
			} else if (hour < 22) {
				msg = "晚上好";
			} else {
				msg = "夜里好";
			}
			return msg;
		}
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<sys:message hideType="1" content="${message}" />
		<div class="top-base">
			<ul>
				<li>
					<div class="top-left">
						<div class="top-img">
<!-- 							<img alt="" class="img-thumbnail" -->
<!-- 								src="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000 -->
<!-- 							&sec=1492496132121&di=5930bcf218f149c62302026fb6b1d706&imgtype=0 -->
<!-- 							&src=http%3A%2F%2Fimg3.duitang.com%2Fuploads%2Fblog%2F201406%2F30%2F20140630105358_zv2HM.jpeg"> -->
														<img alt="" class="img-thumbnail" src="${fns:getUserImage(ctxStatic)}">
						</div>
						<div class="top-user">
							<div class="top-user-msg">
								<span class="top-user-title" id="dayHello"></span>&nbsp;,&nbsp;<span>${user.loginName}</span>
							</div>
							<div class="top-user-msg">
								<span class="top-user-title">上次登录 IP&nbsp;:</span> <span>${user.oldLoginIp}</span>
							</div>
							<div class="top-user-msg">
								<span class="top-user-title">时间：</span> <span><fmt:formatDate
										value="${user.oldLoginDate}" type="both" dateStyle="full" /></span>
							</div>
						</div>
					</div>
				</li>
<!-- 				<li> -->
<!-- 					<div class="top-title"> -->
<!-- 						<span>我的邮件</span> -->
<!-- 					</div> -->
<!-- 					<div class="top-message"> -->
<%-- 						<span>${noReadCount}</span>&nbsp;条 --%>
<!-- 					</div> -->
<!-- 					<div class="top-detail"> -->
<!-- 						<div class="top-btn"> -->
<!-- 							<a class="J_menuItem" -->
<%-- 								href="${ctx}/iim/mailBox/list?orderBy=sendtime desc"> <i --%>
<!-- 								class="fa fa-envelope"></i> <span> 查看所有邮件</span> -->
<!-- 							</a> -->
<!-- 						</div> -->
<!-- 					</div> -->
<!-- 				</li> -->
<!-- 				<li> -->
<!-- 					<div class="top-title"> -->
<!-- 						<span>我的消息</span> -->
<!-- 					</div> -->
<!-- 					<div class="top-message"> -->
<%-- 						<span>${notifyCount}</span>&nbsp;条 --%>
<!-- 					</div> -->
<!-- 					<div class="top-detail"> -->
<%-- 						<a class="J_menuItem" href="${ctx }/oa/oaNotify/self "> <i --%>
<!-- 							class="fa fa-bell"></i><span>查看所有 </span> -->
<!-- 						</a> -->
<!-- 					</div> -->
<!-- 				</li> -->
			</ul>
		</div>
		<div class="detail-base">
			<div class="detail-title">
				<div class="detail-title-left">
					<div>个人信息</div>
				</div>
				<div class="detail-title-right">
					<div class="detail-edit">
						<div class="btn-group btn-group-xs">
							<button type="button" class="btn btn-primary" style="width: 80px;cursor: auto;">编辑</button>
							<button type="button" class="btn btn-primary dropdown-toggle"
								data-toggle="dropdown" aria-haspopup="true"
								aria-expanded="false">
								<span class="caret"></span> <span class="sr-only">Toggle
									Dropdown</span>
							</button>
							<ul class="dropdown-menu" style="min-width:100%;">
								<li><a id="userImageBtn" data-toggle="modal"
									data-target="#register">更换头像</a></li>
								<li><a href="#"
									onclick="openDialog('个人信息编辑', '${ctx}/sys/user/infoEdit','800px', '600px')"
									title="个人信息编辑" data-toggle="modal" data-target="#register">编辑资料</a></li>
							</ul>
						</div>
					</div>
				</div>
			</div>
			<div class="detail-content">
				<table class="table table-bordered">
					<tbody>
						<tr>
							<td class="width-15"><strong>账号</strong></td>
							<td class="width-35">${user.loginName}</td>
							<td class="width-15"><strong>证件号</strong></td>
							<td class="width-35">${user.idNo}</td>
						</tr>
						<tr>
							<td class="width-15"><strong>别名</strong></td>
							<td class="width-35">${user.account}</td>
							<td class="width-15"><strong>民族</strong></td>
							<td class="width-35">${fns:getDictLabel(user.nation, 'nation', '')}</td>
						</tr>
						<tr>
							<td class="width-15"><strong>姓名</strong></td>
							<td class="width-35">${user.name}</td>
							<td class="width-15"><strong>学校</strong></td>
							<td class="width-35">${user.company.name}</td>
						</tr>
						<tr>
							<td class="width-15"><strong>邮箱</strong></td>
							<td class="width-35">${user.email}</td>
							<td class="width-15"><strong>组织机构</strong></td>
							<td class="width-35">${user.office.name}</td>
						</tr>
						<tr>
							<td class="width-15"><strong>手机</strong></td>
							<td class="width-35">${user.mobile}</td>
							<td class="width-15"><strong>备注</strong></td>
							<td class="width-35">${user.remarks}</td>
						</tr>
						<tr>
							<td><strong>用户角色</strong></td>
							<td>${user.roleNames}</td>
							<td><strong>用户类型</strong></td>
							<td>${fns:getDictLabel(user.userType, 'sys_user_type', '无')}</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
<!-- 		<div class="detail-base"> -->
<!-- 			<div class="detail-title"> -->
<!-- 				<div class="detail-title-left"> -->
<!-- 					<div>注册信息</div> -->
<!-- 				</div> -->
<!-- 				<div class="detail-title-right"> -->
<!-- 					<div class="detail-edit"> -->
<!-- 						<div class="btn-group btn-group-xs"> -->
<!-- 							<button type="button" class="btn btn-primary">编辑</button> -->
<!-- 							<button type="button" class="btn btn-primary dropdown-toggle" -->
<!-- 								data-toggle="dropdown" aria-haspopup="true" -->
<!-- 								aria-expanded="false"> -->
<!-- 								<span class="caret"></span> <span class="sr-only">Toggle -->
<!-- 									Dropdown</span> -->
<!-- 							</button> -->
<!-- 							<ul class="dropdown-menu"> -->
<!-- 								<li><a id="userPassWordBtn" data-toggle="modal" -->
<!-- 									data-target="#register">更换密码</a></li> -->
<!-- 								<li><a href="#" data-toggle="modal" data-target="#register">更换手机号</a> -->
<!-- 								</li> -->
<!-- 							</ul> -->
<!-- 						</div> -->
<!-- 					</div> -->
<!-- 				</div> -->
<!-- 			</div> -->
<!-- 			<div class="detail-content"> -->
<!-- 				<div class="row"> -->
<!-- 					<div class="col-sm-6"> -->
<!-- 						<table class="table table-bordered"> -->
<!-- 							<tbody> -->
<!-- 								<tr> -->
<!-- 									<td><strong>用户名</strong></td> -->
<%-- 									<td>${user.loginName}</td> --%>
<!-- 								</tr> -->
<!-- 								<tr> -->
<!-- 									<td><strong>注册手机号码</strong></td> -->
<%-- 									<td>${user.mobile}</td> --%>
<!-- 								</tr> -->
<!-- 								<tr> -->
<!-- 									<td><strong>用户角色</strong></td> -->
<%-- 									<td>${user.roleNames}</td> --%>
<!-- 								</tr> -->
<!-- 								<tr> -->
<!-- 									<td><strong>用户类型</strong></td> -->
<%-- 									<td>${fns:getDictLabel(user.userType, 'sys_user_type', '无')}</td> --%>
<!-- 								</tr> -->
<!-- 							</tbody> -->

<!-- 						</table> -->
<!-- 					</div> -->
<!-- 					<div class="col-sm-6"> -->
<!-- 						<div class="detail-code"> -->
<%-- 														<img width="100%" class="img-thumbnail" src="${user.qrCode}"> --%>
<!-- 							<img width="100%" class="img-thumbnail" -->
<!-- 								src="http://www.jeeplus.org:8080/jeeplus/userfiles/1/qrcode/test.png"> -->
<!-- 						</div> -->
<!-- 					</div> -->
<!-- 				</div> -->
<!-- 			</div> -->
<!-- 		</div> -->
	</div>
</body>
</html>