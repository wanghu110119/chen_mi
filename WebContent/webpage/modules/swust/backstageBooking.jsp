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
<title>成都沉谜推理探案馆预约管理平台</title>
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
	 	@media (max-width:768px){
	 		#shadow-wrap{
	 			width:100% !important;
	 		}
	 		.change-width h3{
	 			padding:3rem 0;
	 		}
	 		.change-width .settings{
	 			padding:3rem 0;
	 		}
	 	}
	 	.error{
	 	color:red;
	 	}
	 	.modal-body{
    		    max-height: 500px;
   				 overflow-x: hidden;
   				 overflow-y: auto;
    	}
	 </style>
</head>
<body>
	<span class="glyphicon glyphicon-menu-hamburger toggle-menu" style="position:fixed;top:25px;left:20px;font-size:24px;color:#3eaab7;z-index:19;"></span>
	<div class="container-fluid">
		<div style="width:100%;height:100%;position:fixed;z-index:20;background-color:transparent;" id="shadow-wrap">
		<div class="col-sm-2 change-width">
			<h3 class="text-center">
				<span>成都沉迷推理探案馆</span> <br> 预约管理系统
			</h3>
			<div class="wrap-img">
				<img src="${ctxStatic}/${(empty headPhoto.photoPath)? 'swust/images/logo.png':headPhoto.photoPath}"
					style="width:100%;height:100%;" class="img-responsive img-circle"> <span class="show-name" id="loginName">${fns:getUser().name}</span>
			</div>
			<ul class="settings">
				<li><a href="#" title="修改密码" class="modify-password" onclick="init()"> <img
						src="${ctxStatic}/swust/images/reset.png" alt="">
				</a></li>
				<li><a href="${ctx}/logout" title="退出登录"> <img
						src="${ctxStatic}/swust/images/exit-back.png" alt="">
				</a></li>
				<li><a href="#" title="修改头像 " class="edit-user"> <img
						src="${ctxStatic}/swust/images/user-back.png" alt="">
				</a></li>
			</ul>
			<ul class="book-list">
				<li class="active"><a href="#my-booking" data-toggle="tab"
					onclick="mybook()" class="change-bg"> <span
						class="glyphicon glyphicon-list-alt"></span> &nbsp;预约管理
				</a></li>
				<li><a href="#companyControl" data-toggle="tab"
					onclick="companyControler()"> <span
						class="glyphicon glyphicon-briefcase"></span> &nbsp;剧本管理
				</a></li>
				<li><a href="#smsSend" data-toggle="tab"
					onclick="SMSSend()"> <span
						class="glyphicon glyphicon-envelope"></span> &nbsp;会员管理
				</a></li>
				<li><a href="#system" data-toggle="tab"
					onclick="systemcontroler()"> <span
						class="glyphicon glyphicon-cog"></span> &nbsp;系统设置
				</a></li>
				<li><a href="#qrcode" data-toggle="tab"
					onclick="toQrCode()"> <span
						class="glyphicon glyphicon-qrcode"  ></span> &nbsp;物料管理
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
                            <label for="choose-header" style="margin-bottom: 10px;">选择头像(请勿包含特殊字符):</label>
                            <input type="file" name="photo" id="choose-header" accept="image/jpeg, image/jpg, image/png">
                        </div>
                        <div class="form-group" style="margin:10px 0; width: 106px;height: 106px;">
                            <img src="images/logo.png" style="width: 100%;max-height:100%;" id="now-image">
                        </div>
                        <div class="form-group">
                            <label for="edit-name" style="margin-bottom: 10px;">修改昵称(汉字、字母或数字20字以内):</label>
                            <input type="text" class="form-control" onkeyup="this.value=this.value.replace(/[^A-Za-z0-9\u4e00-\u9fa5]/g,'')" id="edit-name" maxlength="20" placeholder="请输入的名字(20字以内)">
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
                    <form class="form-horizontal" id ="changeform">
                        <div class="form-group">
                            <label for="old-pwd" class="col-sm-2 control-label col-sm-offset-2" >旧密码:</label>
                            <div class="col-sm-6">
                                <input type="password" class="form-control" id="old-pwd" name ="oldPassword">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="new-pwd" class="col-sm-2 control-label col-sm-offset-2">新密码:</label>
                            <div class="col-sm-6">
                                <input type="password" class="form-control" id="new-pwd" name ="newPassword">
                            </div>
                        </div>
                        <div class="form-group" >
                            <label for="inputPassword3" class="col-sm-2 control-label col-sm-offset-2">确认密码:</label>
                            <div class="col-sm-6">
                                <input type="password"  class="form-control" id="inputPassword3" name="confirmPassword">
                                <span id="authentication" ></span>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer text-center">
                    <button type="button" class="btn btn-md confirm" id="dosubmit" onclick="changePwdS()">确认</button>
                </div>
            </div>
        </div>
    </div>
    <script type="${ctxStatic}/swust/js/swust.js"></script>
	<script src="${ctxStatic}/swust/js/jquery.min.js"></script>
	<script src="${ctxStatic}/swust/js/bootstrap.min.js"></script>
	<script src="${ctxStatic}/swust/js/backstagePlatform.js"></script>
	<script src="${ctxStatic}/jquery-validation/1.14.0/jquery.validate.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-validation/1.14.0/localization/messages_zh.min.js" type="text/javascript"></script>
<!-- 引入自定义的jQuery validate的扩展校验 -->
<%-- <script src="${ctxStatic}/common/validateExtend.js" type="text/javascript"></script> --%>
<script
	src="${ctxStatic}/jquery-validation/1.14.0/jquery.validate.min.js"
	type="text/javascript"></script>
<script
	src="${ctxStatic}/jquery-validation/1.14.0/jquery.form.js"
	type="text/javascript"></script>
	<script src="${ctxStatic }/swust/js/layDate/laydate/laydate.js"></script>
	<script type="text/javascript">
	function changePwdS() {
	var validateFormC;
	validateFormC = $("#changeform").validate({
		rules:{
			oldPassword:{
				checkName:true,
				rangelength:[6,15],
				required: true,
				remote:{                                          
	               type:"POST",//验证旧密码是否存在
	               url:"${ctx}/swust/order/checkPwd",
	               data:{
	            	   oldPassword:function(){return $("#old-pwd").val();},
		               } 
	            }
			},
			newPassword:{
				checkName:true,
				notEqual:true,
				required: true,
				rangelength:[6,15]
			},
			confirmPassword:{
				checkName:true,
				equalTo: "#new-pwd",
				required: true,
			}	
		},
		messages:{
			oldPassword:{
				rangelength:"请输入长度在{0}到{1}之间的字母、数字",
				required:"请输入旧密码",
				remote:"旧密码输入错误"
			},
			newPassword:{
				rangelength:"请输入长度在{0}到{1}之间的汉字或者字母",
				required:"请输入新密码",
			},
			confirmPassword:{
				required:"请输入确认密码",
				equalTo:"两次输入密码不一致"
			}

		}
		
	});
	jQuery.validator.addMethod("checkName", function(value, element) {
        var char = /^[0-9a-zA-Z]*$/g;
        return this.optional(element) || char.test(value);   
    }, $.validator.format("只能包含英文、数字"));
	
	jQuery.validator.addMethod("notEqual", function(value, element) {
        var oldPassword =$("#old-pwd").val();
        if(value!=oldPassword){
        	return true;
        }
        return false;   
    }, $.validator.format("新密码不能与旧密码一致"));
	
		//验证通过后的执行方法
        if($("#changeform").valid()){
        	 $.ajax({
        	type : "POST",
        	dataType:"json",
        	url : "${ctx}/swust/order/updateNewUserByPassword",
			data : $("#changeform").serialize(),
			async : false,
			success : function(data) {
				if(data.success){
					submit = true;
					setTimeout(function(){
					$('#modal-pwd').modal('hide');
					},800);
					alert("修改成功");
					}
			}
        });
        }
	};
// 	$("#dosubmit").click(function() {
// 		$("#changeform").submit();
// 		setTimeout(function(){
// 		$('#modal-pwd').modal('hide');
// 	},800);
// 		alert("修改成功");
// 	});
	
	
	
	
	
	
	
	
	if(window.screen.width<768){
		$('.toggle-menu').click(function(){
			$('#shadow-wrap').fadeIn(500);
		})
		$('#shadow-wrap').click(function(){
			$('#shadow-wrap').fadeOut(500);
		})
	}
	if(window.screen.width>1000){
			$('#shadow-wrap').css('width',0)
	}
	function toQrCode () {
	$.ajax({
		type : "POST",
		url : "${ctx}/swust/meeting/init",
		success : function(date) {
			$("#qrcode").empty();
			$("#qrcode").html(date);
		}
	});
	}
	
	
	
	function changeheadphoto(){
		  var formData = new FormData($("#photoForm")[0]);
		  console.log(formData);
          $.ajax({  
          	          url: "${ctx}/swust/system/changeheadphoto" ,  
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
//           	        	 alert(data.msg)
          	          },  
               });
          $.ajax({
				type : "POST",
				url : "${ctx}/swust/system/changename",
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
		$("#inputPassword3").val("");
		$("#old-pwd-error").html("");
		$("#new-pwd-error").html("");
		$("#inputPassword3-error").html("");
		}
	var submit = false;
	var password = false;
	var newandold = false;
// 	$("#dosubmit").click(function(){
// 		if(submit&&newandold){
// 			$.ajax({
// 				type:"POST",
// 				url:"${ctx}/swust/order/updateUserByPassword",
// 				data:"newPassword="+$("#new-pwd").val()+"&oldPassword="+$("#old-pwd").val(),
// 				success:function(date){
// 				$("#authentication").html(date.msg);
// 				if(date.success){
// 					$("#authentication").attr("style","font-size: 20px; text-align: center;color: green;");
// 					submit = true;
// 					setTimeout(function(){
// 					$('#modal-pwd').modal('hide');
// 				},800);
// 					}else{
// 					$("#authentication").attr("style","font-size: 20px; text-align: center;color: red;");
// 					submit = false;
// 						}	
// 						}
// 					});
// 			}
// 	});
// 	$("#old-pwd").change(function(){
// 		$.ajax({
// 		type:"POST",
// 		url:"${ctx}/swust/order/UserSelectPassword",
// 		data:"newPassword="+$("#new-pwd").val()+"&oldPassword="+$("#old-pwd").val(),
// 		success:function(date){
// 		$("#authentication").html(date.msg);
// 		if(date.success){
// 			$("#authentication").attr("style","font-size: 20px; text-align: center;color: green;");
// 			submit = true;
// 			}else{
// 			$("#authentication").attr("style","font-size: 20px; text-align: center;color: red;");
// 			submit = false;
// 				}
// 				}
// 			});
// 		});
// 	function premitPassword(){
// 		$("#authentication").attr("style","font-size: 20px; text-align: center;color: red;");
// 		if(!submit){
// 			$("#authentication").html("原密码输入有误");
// 			return;
// 		}else if($("#new-pwd").val()==$("#old-pwd").val()){
// 			$("#authentication").html("新密码不能与原密码一致");
// 			newandold = false;
// 			return;
// 		} else if($("#new-pwd").val()!=$("#inputPassword3").val()){
// 			newandold = false;
// 			$("#authentication").html("两次输入密码有误");
// 			return;
// 		}else{
// 			newandold=true;
// 			$("#authentication").attr("style","font-size: 20px; text-align: center;color: green;");
// 			$("#authentication").html("修改密码可以使用");
// 			return;
// 			}
// 	}

	
		$(document).ready(function() {
			mybook();
		});
		function systemcontroler() {
			$.ajax({
				type : "POST",
				url : "${ctx}/swust/system/system",
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
				url : "${ctx}/swust/manager",
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
				url : "${ctx}/swust/appointment",
				success : function(date) {
					$("#my-booking").empty();
					$("#my-booking").html(date);
					
					laydate.render({
						elem: '#startDate',
						type:'datetime',
						range: true,
						done: function(value, date, endDate){  	
							console.log(date);
							console.log(value);
							var index =  value.split(" - ");
							$("#beginTime").val(index[0]);
							$("#endTime").val(index[1]);
							searchSysOrder($("#stateKey").val());
							}
						//	changeTime(value, $("#overDate").val());
					});
// 					laydate.render({
// 						elem: '#startDate',
// 						type:'datetime',
// 						done: function(value, date){  		
// 							var overDate = $("#overDate").val();
// 							if(value > overDate){
// 								$("#overDate").val("");
// 						//		alert("起始时间不能大于结束时间!");
// 								return;
// 							}
// 						//	changeTime(value, $("#overDate").val());
// 				        }
// 					});
// 					laydate.render({
// 						elem: '#overDate',
// 						type:'datetime',
// 						done: function(value, date){  
// 							var startDate = $("#startDate").val();
// 							if(value < startDate){
// 								$("#startDate").val("");
// 						//		alert("起始时间不能大于结束时间!");
// 								return;
// 							}
// 						//	changeTime($("#startDate").val(), value);
// 				        }
// 					});
					
				}
			});
		}
		function SMSSend() {
			
			$.ajax({
				type : "POST",
				url : "${ctx}/swust/car",
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
	          console.log( fileObj.files[0]);
	          var flag = fileObj.files[0].name;
	          var size = fileObj.files[0].size/1024/1024;
	          var reg =/^.*[a-zA-Z0-9\u4e00-\u9fa5]\.(?:png|jpg|jpeg)$/;
	          if(reg.test(flag) && size<1){
	        	  $img.attr('src', dataURL);
	          $(".edit-header button:contains('确认')").click(function () {
	            $(".wrap-img>img").attr("src",dataURL);
	          })
	          }
	          else{
	        	  alert("请上传：1MB以下不包含特殊字符的jpg/jpeg/png格式图片");
	        	  $("#choose-header").val(null);
	        	  return;
	          }
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