<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head lang="zh-cn">
    <meta charset="UTF-8">
    <!--2 viewport-->
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no">
    <!--3、x-ua-compatible-->
    <meta http-equiv="x-ua-compatible" content="IE=edge">
    <title>登录</title>
    <!--4、引入两个兼容文件-->
    <!--[if lt IE 9]>
    <script src="js/html5shiv.min.js"></script>
    <script src="js/respond.min.js"></script>
    <![endif]-->
    <!--6、引入 bootstrap.css-->
    <link rel="stylesheet" href="${ctxStatic }/swust/css/bootstrap.min.css">
    <link rel="stylesheet" href="${ctxStatic }/swust/css/login.min.css">
    <!-- 引入jquery插件 -->
	<script src="${ctxStatic}/jquery/jquery-2.1.1.min.js" type="text/javascript"></script>
	<!-- jquery.validate 校验 -->
	<script src="${ctxStatic}/jquery-validation/1.14.0/jquery.validate.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-validation/1.14.0/localization/messages_zh.min.js" type="text/javascript"></script>
</head>
<body style="background: url(${ctxStatic }/${photo.photoPath }) 0 0 no-repeat;
	background-attachment: fixed;
	background-size: cover;
	background-position: center">
    <div class="container-fluid">
        <div class="row">
            <div class="col-xs-12">
                <div class="login-box">
                    <img src="${ctxStatic }/swust/images/logo.png" class="img-responsive">
                    <h2>西南科技大学车辆出入预约平台 </h2>
                    
                    
                    
                    <form action="${ctx}/login" method="post"  id="loginform">
                        <input type="text" name="username" placeholder="请输入用户名" id="username" required="required">
                        <span  class="user">
                            <img src="${ctxStatic }/swust/images/user.png">
                        </span>
                        <input type="password" name="password" placeholder="请输入密码" id="password" required="required">
                        <!-- <input type="hidden" name="flag" value="true">	 -->
                        <span style="color:red">${errorMsg }</span>	
                        <span class="password">
                            <img src="${ctxStatic }/swust/images/password.png">
                        </span>
                        <button type="submit" id="dosubmit">登&emsp;录</button>
                    </form>
                </div>
            </div>
        </div>
        <script type="text/javascript">
         	/* $(function(){
	        	("#dosubmit").click(function () {
					$("#loginform").submit();
				});
            }); */ 
            
            $(document).ready(function() {
				$("#loginForm").validate({
					rules: {
						validateCode: {remote: "${pageContext.request.contextPath}/servlet/validateCodeServlet"}
					},
					messages: {
						username: {required: "请填写用户名."},password: {required: "请填写密码."},
						validateCode: {remote: "验证码不正确.", required: "请填写验证码."}
					},
					errorLabelContainer: "#messageBox",
					errorPlacement: function(error, element) {
						error.appendTo($("#loginError").parent());
					} 
				});
			});
            
        </script>
    </div>
    <!-- <script src="js/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script> -->
</body>
</html>