<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page isELIgnored="false" %>
<html>
<head lang="zh-cn">
	<title>我的预约</title>
	<meta charset="UTF-8">
    <!--2 viewport-->
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no">
    <!--3、x-ua-compatible-->
    <meta http-equiv="x-ua-compatible" content="IE=edge">
    <title>预约</title>
    <!--4、引入两个兼容文件-->
    <!--[if lt IE 9]>
    <script src="js/html5shiv.min.js"></script>
    <script src="js/respond.min.js"></script>
    <![endif]-->
    <!--6、引入 bootstrap.css-->
    <link rel="stylesheet" href="${ctxStatic }/swust/css/bootstrap.min.css">
    <link rel="stylesheet" href="${ctxStatic }/swust/css/booking.min.css">
    <link rel="stylesheet" href="${ctxStatic }/swust/css/header.min.css">
     <link rel="shortcut icon" href="${ctxStatic}/swust/images/logo.png" type="image/x-icon">
    
    <style type="text/css">
    	p {
		  	word-break:break-word;
		}
	#orderSelect{
	font-size: 14px;
    color: #515c6b;
    width: 79px;
    height: 20px;
    border:1px solid #3eaab7;
}
    </style>
</head>
<body>
	<!--导航栏-->
    <%@include file="/webpage/include/swustHeader.jsp" %>
<!--预约文字-->
	<input type="hidden" value="mobile/swust/order/list"/>
    <div class="container-fluid change-padding">
        <div class="row">
            <div class="col-xs-12">
                <p class="my-booking">我的预约</p>
                <ul class="state-menu">
                    <li class="active">
                    	<a style="cursor: pointer;"  btype="1" data-toggle="tab">待审核</a>
                    </li>
                    <li>
						<a style="cursor: pointer;"  btype="2" data-toggle="tab">已审核</a>
                    </li>
                    <li>
						<select  id="orderSelect" style="text-align: center">
							<option style="text-align: center" onchange="search('',1)" value="" >全部</option>
							<option onchange="search('',1)" value="0">正常预约</option>
							<option onchange="search('red',1)" value="red">超时预约</option>
						</select>
                    </li>
                    <li>
                        <button class="glyphicon glyphicon-plus" onclick="add()"> 新增预约</button>
                    </li>
                </ul>
                <div class="tab-content">
					<div class="table-responsive tab-pane active" id="not-check">

					</div>
				</div>
          	</div>
      	</div>
   	</div>
  
<!--查看事由模态框-->
<div class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" id="check-view">
        <div class="modal-dialog modal-sm" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button"  class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h3 class="modal-title text-center">预约事由</h3>
                </div>
                <div class="modal-body">
                    <p class="text-center" id="order_reason"></p>
                    <p class="text-center">
                      <span style="color:green">备注：
                      </span> <span id ="remarks"> 
                        </span>
                    </p>
                </div>
                <div class="modal-footer text-center">
                    <button type="button" class="btn btn-md confirm" data-dismiss="modal">关闭</button>
                </div>
            </div>
        </div>
    </div>
</body>
<script src="${ctxStatic }/swust/js/jquery.min.js"></script>
<script src="${ctxStatic }/swust/js/bootstrap.min.js"></script>
<script src="${ctxStatic }/swust/js/booking.js"></script>
<script src="${ctxStatic}/jquery-validation/1.14.0/jquery.validate.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-validation/1.14.0/localization/messages_zh.min.js" type="text/javascript"></script>
<!-- 引入自定义的jQuery validate的扩展校验 -->
<script src="${ctxStatic}/common/validateExtend.js" type="text/javascript"></script>
<script type="text/javascript">
		$(function(){
			var userAgent=window.navigator.userAgent.toLowerCase();
			if(userAgent.indexOf("firefox")>=1){
				$('#nav #login-head #reaserch').css({
					float:'left',
				});
			}
			$(".state-menu a").on("click",function(e){
				e.preventDefault();
				$(this).addClass("active").parent().siblings().children().removeClass("active");
				var msg = $(this).attr("btype");
				if (msg == "1") {
					console.log(0);
					$("#statekey").val(0);
					search(0,1); 
				}
				else {
					console.log(3);
					$("#statekey").val(3);
					search(3,1);
				}
			});
			
			$("#orderSelect").change(function(){
				var sel = $(this).val();
				search(sel,1);
			});
			
		});
		
		
		//初始化修改密码模态框
		function init(){
			$("#authentication").html("");
			$("#new-pwd").val("");
			$("#old-pwd").val("");
			$("#inputPassword3").val("")
			}

		var submit = false;
		var password = false;
		var newandold = false;
		$("#dosubmit").click(function(){
			if(submit&&newandold){
				$.ajax({
					type:"POST",
					url:"mobile/swust/order/updateUserByPassword",
					data:"newPassword="+$("#new-pwd").val()+"&oldPassword="+$("#old-pwd").val(),
					success:function(date){
					$("#authentication").html(date.msg);
					if(date.success){
						$("#authentication").attr("style","font-size: 20px; text-align: center;color: green;");
						submit = true;
						setTimeout(start,800);
						}else{
						$("#authentication").attr("style","font-size: 20px; text-align: center;color: red;");
						submit = false;
							}	
							}
						});
				}
		});
		$("#old-pwd").change(function(){
			$.ajax({
			type:"POST",
			url:"mobile/swust/order/UserSelectPassword",
			data:"newPassword="+$("#new-pwd").val()+"&oldPassword="+$("#old-pwd").val(),
			success:function(date){
			$("#authentication").html(date.msg);
			if(date.success){
				$("#authentication").attr("style","font-size: 20px; text-align: center;color: green;");
				submit = true;
				}else{
				$("#authentication").attr("style","font-size: 20px; text-align: center;color: red;");
				submit = false;
					}
					}
				});
			});
		function premitPassword(){
				$("#authentication").attr("style","font-size: 20px; text-align: center;color: red;");
				if(!submit){
					$("#authentication").html("密码有错误，请重新输入");
					return;
				}else if($("#new-pwd").val()==$("#old-pwd").val()){
					$("#authentication").html("新密码不能与旧密码一致");
					newandold = false;
					return;
				} else if($("#new-pwd").val()!=$("#inputPassword3").val()){
					newandold = false;
					$("#authentication").html("两次输入密码有误");
					return;
				}else{
					newandold=true;
					$("#authentication").attr("style","font-size: 20px; text-align: center;color: green;");
					$("#authentication").html("修改密码可以使用");
					return;
					}
			}
	 $(function(){
		//初始化
		openwin("0","${page.pageNo}");
	});
	function openwin(a,b) {
		$.ajax({
			type:"POST",
			url:"mobile/swust/order/list",
			data:"state="+a+"&pageNo="+b,
			success:function(date){
				$("#not-check").empty();
				$("#not-check").html(date);
				$(".buttons:button:nth-child(3)").addClass("active");
				search(0,1);
			}
		});
	} 
	function start() {
		location.href = "mobile/swust/order";
	}
	function add() {
		location.href = "mobile/swust/order/add";
	}
</script>

</html>