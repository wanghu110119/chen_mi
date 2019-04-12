<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head lang="zh-cn">
<meta charset="UTF-8">
<!--2 viewport-->
<meta name="viewport"
	content="width=device-width,initial-scale=1,user-scalable=no">
<!--3、x-ua-compatible-->
<meta http-equiv="x-ua-compatible" content="IE=edge">
<title>西南科技大学车辆出入预约系统</title>
<!--4、引入两个兼容文件-->
<!--[if lt IE 9]>
    <script src="js/html5shiv.min.js"></script>
    <script src="js/respond.min.js"></script>
    <![endif]-->
<!--6、引入 bootstrap.css-->
<link rel="stylesheet" href="${ctxStatic}/swust/css/bootstrap.min.css">
<link rel="stylesheet" href="${ctxStatic}/swust/css/backstageBooking.min.css">
<link href="${ctxStatic }/swust/css/bootstrap-datetimepicker.min.css" rel="stylesheet" />
 <link rel="shortcut icon" href="${ctxStatic}/swust/images/logo.png" type="image/x-icon">
	 <style>
	 	label{
	 		padding-left:0 15px !important;
	 		text-align:right !important;
	 	}
	 	@media (min-width:1200px){
	 		#shadow-wrap{
	 			width:0 !important;
	 		}
	 	}
	 	@media (max-width:768px){
	 		#shadow-wrap{
	 			width:100% !important;
	 		}
	 	}
	 </style>
</head>
<body>
	<span class="glyphicon glyphicon-menu-hamburger toggle-menu" style="position:fixed;top:25px;left:20px;font-size:24px;color:#3eaab7;z-index:19;"></span>
	<div class="container-fluid">
		<div style="width:100%;height:100%;position:fixed;z-index:20;background-color:transparent;" id="shadow-wrap">
		<div class="col-sm-2 change-width">
			<h3 class="text-center">
				<span>西南科技大学</span> <br> 车辆出入预约系统
			</h3>
			<div class="wrap-img">
				<img src="${ctxStatic}/${(empty headPhoto.photoPath)? 'swust/images/logo.png':headPhoto.photoPath}"
					style="width:100%;height:100%;" class="img-responsive img-circle"> <span class="show-name" id="loginName">${fns:getUser().name}</span>
			</div>
			<ul class="settings">
				<li><a href="#" title="修改头像 " class="edit-user"> <img
						src="${ctxStatic}/swust/images/user-back.png" alt="">
				</a></li>
				<li><a href="#" title="修改密码" class="modify-password" onclick="init()"> <img
						src="${ctxStatic}/swust/images/reset.png" alt="">
				</a></li>
				<li><a href="${ctx}/logout" title="退出登录"> <img
						src="${ctxStatic}/swust/images/exit-back.png" alt="">
				</a></li>
			</ul>
			<ul class="book-list">
				<li class="active"><a href="#my-booking" data-toggle="tab"
					onclick="mybook()" class="change-bg"> <span
						class="glyphicon glyphicon-list-alt"></span> &nbsp;预约管理
				</a></li>
				<li><a href="#companyControl" data-toggle="tab"
					onclick="companyControler()"> <span
						class="glyphicon glyphicon-briefcase"></span> &nbsp;单位管理
				</a></li>
				<li><a href="#smsSend" data-toggle="tab"
					onclick="SMSSend()"> <span
						class="glyphicon glyphicon-envelope"></span> &nbsp;短信提醒
				</a></li>
				<li><a href="#system" data-toggle="tab"
					onclick="systemcontroler()"> <span
						class="glyphicon glyphicon-cog"></span> &nbsp;系统设置
				</a></li>
				<li><a href="#qrcode" data-toggle="tab"
					onclick="toQrCode()"> <span
						class="glyphicon glyphicon-qrcode" style="margin-left:16px;"></span> &nbsp;二维码预约
				</a></li>
			</ul>
		</div>
		</div>
		<div class="col-xs-12 tab-content">
			<div id="my-booking" class="tab-pane active"></div>
			<div id="companyControl" class="tab-pane"></div>
			<div id="system" class="tab-pane"></div>
			<div id="smsSend" class="tab-pane"></div>
			<div id="qrcode" class="tab-pane"></div>
			<input type="hidden" value="" id="qrcodeSearch">
		</div>
<!-- 		<div id="smsSend" class=""></div> -->
	</div>
	<!--编辑头像-->
    <div class="modal fade edit-header">
        <div class="modal-dialog modal-sm">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h3 class="modal-title text-center">修改管理员信息</h3>
                </div>
                <div class="modal-body">
                    <form id="photoForm" method="post" enctype="multipart/form-data">
                        <div class="form-group">
                            <label for="choose-header" style="margin-bottom: 10px;">选择头像:</label>
                            <input type="file" name="photo" id="choose-header">
                        </div>
                        <div class="form-group" style="margin:10px 0; width: 106px;height: 106px;">
                            <img src="images/logo.png" style="width: 100%;" id="now-image">
                        </div>
                        <div class="form-group">
                            <label for="edit-name" style="margin-bottom: 10px;">修改昵称:</label>
                            <input type="text" class="form-control" id="edit-name" placeholder="请输入修改的名字">
                        </div>
                    </form>
                </div>
                <div class="modal-footer text-center">
                    <button type="button" class="btn btn-md confirm" onclick="changeheadphoto()">确认</button>
                    <button type="button" class="btn btn-md confirm" data-dismiss="modal">取消</button>
                </div>
            </div>
        </div>
    </div>
	<!--查看信息模态框-->
	<div class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog"
		id="check-view">
		<div class="modal-dialog modal-sm" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h3 class="modal-title text-center">预约事由</h3>
				</div>
				<div class="modal-body">
					<p class="text-center">王晓波老师学校交换交流，需到文学院。</p>
					<p class="text-center">
						<span class="notice">备注：</span> 王晓波老师学校交换交流，需到文学院。
					</p>
				</div>
				<div class="modal-footer text-center">
					<button type="button" class="btn btn-md confirm"
						data-dismiss="modal">关闭</button>
				</div>
			</div>
		</div>
	</div>
	<!--编辑模态框-->
	<!--重置密码模态框-->
	<div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog"
		id="reset">
		<div class="modal-dialog modal-sm" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h3 class="modal-title text-center">修改信息</h3>
				</div>
				<div class="modal-body">
					<h5 class="text-center">是否重置密码？</h5>
				</div>
				<div class="modal-footer text-center">
					<button type="button" class="btn btn-md confirm">确认</button>
					<button type="button" class="btn btn-md confirm"
						data-dismiss="modal">取消</button>
				</div>
			</div>
		</div>
	</div>
	  <!-- 修改密码模态框 -->
    <div class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" id="modal-pwd">
        <div class="modal-dialog modal-md" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h3 class="modal-title text-center">修改密码</h3>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" id ="changeform" action="mobile/swust/order/updateUserByPassword">
                        <div class="form-group">
                            <label for="old-pwd" class="col-sm-2 control-label col-sm-offset-2" >旧密码:</label>
                            <div class="col-sm-6">
                                <input type="password" class="form-control" id="old-pwd" name ="oldPassword">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="new-pwd" class="col-sm-2 control-label col-sm-offset-2">新密码:</label>
                            <div class="col-sm-6">
                                <input type="password" onchange="premitPassword()" class="form-control" id="new-pwd" name ="newPassword">
                            </div>
                        </div>
                        <div class="form-group" >
                            <label for="inputPassword3" class="col-sm-2 control-label col-sm-offset-2">确认密码:</label>
                            <div class="col-sm-6">
                                <input type="password" onchange="premitPassword()" class="form-control" id="inputPassword3">
                                <span id="authentication" ></span>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer text-center">
                    <button type="button" class="btn btn-md confirm" id="dosubmit">确认</button>
                </div>
            </div>
        </div>
    </div>
	<script src="${ctxStatic}/swust/js/jquery.min.js"></script>
	<script src="${ctxStatic}/swust/js/bootstrap.min.js"></script>
	<script src="${ctxStatic}/swust/js/backstagePlatform.js"></script>
	<script src="${ctxStatic}/jquery-validation/1.14.0/jquery.validate.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-validation/1.14.0/localization/messages_zh.min.js" type="text/javascript"></script>
<!-- 引入自定义的jQuery validate的扩展校验 -->
<script src="${ctxStatic}/common/validateExtend.js" type="text/javascript"></script>
<script
	src="${ctxStatic}/jquery-validation/1.14.0/jquery.validate.min.js"
	type="text/javascript"></script>
<script
	src="${ctxStatic}/jquery-validation/1.14.0/localization/messages_zh.min.js"
	type="text/javascript"></script>
<!-- 引入自定义的jQuery validate的扩展校验 -->
<script src="${ctxStatic}/common/validateExtend.js"
	type="text/javascript"></script>
<script
	src="${ctxStatic}/jquery-validation/1.14.0/jquery.form.js"
	type="text/javascript"></script>
	<script src="${ctxStatic }/swust/js/layDate/laydate/laydate.js"></script>
	<script type="text/javascript">
	if(window.screen.width<768){
		$('.toggle-menu').click(function(){
			$('#shadow-wrap').fadeIn(500);
		})
		$('#shadow-wrap').click(function(){
			$('#shadow-wrap').fadeOut(500);
		})
	}
	function toQrCode () {
// 		$('#qrcode').load('webpage/modules/swust/qrcode.jsp');
// location.href="mobile/swust/meeting/init"
	$.ajax({
		type : "POST",
		url : "mobile/swust/meeting/init",
		success : function(date) {
			$("#qrcode").empty();
			$("#qrcode").html(date);
		}
	});
	}
	function changeheadphoto(){
		  var formData = new FormData($("#photoForm")[0]);
          $.ajax({  
          	          url: "mobile/swust/system/changeheadphoto" ,  
          	          type: 'POST',  
          	          data: formData,  
          	          async: false,  
          	          cache: false,  
          	          contentType: false,  
          	          processData: false, 
          	          error:function (data) {
              	         alert(data.msg)
          	          },
          	          success: function (data) {
          	        	 alert(data.msg)
          	          },  
               });
          $.ajax({
				type : "POST",
				url : "mobile/swust/system/changename",
				 data: "name="+$("#edit-name").val(),
				success : function(date) {
					$("#loginName").val(date.msg);
				}
			})
		}


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
					setTimeout(function(){
					$('#modal-pwd').modal('hide');
				},800);
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
			$("#authentication").html("原密码输入有误");
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

	
		$(document).ready(function() {
			mybook();
			
		});
		/* var start = {
				elem: '#beginTime',				
				type:'time',							
				istime: true,
				istoday: false,
				choose: function(datas){
					debugger;
					end.min = datas; //开始日选好后，重置结束日的最小日期
					end.start = datas //将结束日的初始值设定为开始日
			}
		};
		var end = {
				elem: '#endTime',			
				type:'time',							
				istime: true,
				istoday: false,
				choose: function(datas){
					start.max = datas; //结束日选好后，重置开始日的最大日期
			}
		}; */
		function systemcontroler() {
			$.ajax({
				type : "POST",
				url : "mobile/swust/system/system",
				success : function(date) {
					$("#system").empty();
					$("#system").html(date);
						laydate.render({
							elem: '#beginTime',
							type:'time',
							done: function(value, date, endDate){  		
								var endTime = $("#endTime").val();
								if(value > endTime){
									alert("起始时间不能大于结束时间!");
									return;
								}
								changeTime(value, $("#endTime").val());
					        }
						});
						laydate.render({
							elem: '#endTime',
							type:'time',
							done: function(value, date, endDate){  
								var beginTime = $("#beginTime").val();
								if(value < beginTime){
									alert("起始时间不能大于结束时间!");
									return;
								}
								changeTime($("#beginTime").val(), value);
					        }
						});
// 						laydate.render({
// 							elem: '#beginDate',
// 							type:'date',
// 							range:'--',
// 							min:0,
// 							done: function(value, date, endDate){  
// 								var beginTime = $("#beginDate").val();
// 								changeTime($("#beginDate").val(), value);
// 								console.log(value)
//            		   var arr = value.split("--")
//            	   $.ajax({
//            		   url:"mobile/swust/system/changeDate",
//            	  		type: 'post',  
//     	        		 data: {
//     					"beginDate" : arr[0],
//     					"endDate":arr[1]
//         	      },
//            	   })
// 					        }
// 						});
						laydate.render({
							elem: '#endDate',
							type:'date',
							done: function(value, date, endDate){  
								var beginTime = $("#beginDate").val();
								if(value < beginTime){
									alert("起始时间不能大于结束时间!");
									return;
								}
// 								changeTime($("#endDate").val(), value);
					        }
						});
				}
			})
		};
		function companyControler() {
			$.ajax({
				type : "POST",
				url : "mobile/swust/manager",
				success : function(date) {
					$("#companyControl").empty();
					$("#companyControl").html(date);
				}
			})
		};

		function returnLogin(){
			location.href="${ctx}/logout";
			}
		
		function mybook() {
			
			$.ajax({
				type : "POST",
				url : "mobile/swust/appointment",
				success : function(date) {
					$("#my-booking").empty();
					$("#my-booking").html(date);
				}
			});
		}
		function SMSSend() {
			
			$.ajax({
				type : "POST",
				url : "mobile/swust/car",
				success : function(date) {
					$("#smsSend").empty();
					$("#smsSend").html(date);
				}
			});
		}
		
		
		$(".edit-user").click(function(){
	          $(".edit-header").modal("show");
	          var nowSrc=$(".wrap-img>img").attr("src");
	          $("#now-image").attr("src",nowSrc);
	          var username=$(".show-name").html();
	          $("#edit-name").val(username);
	        });
	        /*修改名字*/
	      $(".edit-header button:contains('确认')").click(function(){
	        var usernameText=$("#edit-name").val();
	        if(usernameText===""){
	          $("#edit-name").val("昵称不能为空！！").css("color","red");
	        }
	        else
	        $(".show-name").html(usernameText);
	        $(".edit-header").modal("hide");
	      });
	      /*修改图片*/
	      $("#choose-header").change(function () {
	        var $file = $(this);
	        var fileObj = $file[0];
	        var windowURL = window.URL || window.webkitURL;
	        var dataURL;
	        var $img = $("#now-image");

	        if (fileObj && fileObj.files && fileObj.files[0]) {
	          dataURL = windowURL.createObjectURL(fileObj.files[0]);
	          $img.attr('src', dataURL);
	          $(".edit-header button:contains('确认')").click(function () {
	            $(".wrap-img>img").attr("src",dataURL);
	          })
	        } else {
	          dataURL = $file.val();
	          var imgObj = document.getElementById("now-image");
	          // 两个坑:
	          // 1、在设置filter属性时，元素必须已经存在在DOM树中，动态创建的Node，也需要在设置属性前加入到DOM中，先设置属性在加入，无效；
	          // 2、src属性需要像下面的方式添加，上面的两种方式添加，无效；
	          imgObj.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)";
	          imgObj.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = dataURL;

	        }
	      });
	</script>
</body>
</html>