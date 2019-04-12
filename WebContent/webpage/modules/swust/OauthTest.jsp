<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
	<title>登录测试</title>
	<meta name="decorator" content="default"/>
</head>
<body>
	<input type="text" class="form-control" id="username"/>
	<input type="password" class="form-control" id="password"/>
	<input type="button" class="btn btn-sm" id="submitUser"/>
	<script src="${ctxStatic }/swust/js/jquery.min.js"></script>
	<script type="text/javascript">
		$(function() {
			$("#submitUser").on("click", function(){
				var username = $("#username").val();
				var password = $("#password").val();
// 				$.ajax({
// 					type : "post",
// 					url : "/mht_oeg/api/swust/meeting/requestServerCode",
// // 					data : {username : username, password:password,ticket:username,code:username}, 
// 					headers: {'Cookie' : document.cookie },
// 					success : function(data) {
// 						console.log(data);
// // 						location.href = data.msg;
// 					}
// 				});
				location.href = "/mht_oeg/api/swust/meeting/requestServerCode"
			});
		})
	
	</script>
</body>
</html>