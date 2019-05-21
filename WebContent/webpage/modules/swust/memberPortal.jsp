<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page isELIgnored="false" %>
<html>
<head lang="zh-cn">
	<title>会员管理</title>
	<meta charset="UTF-8">
    <!--2 viewport-->
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no">
    <!--3、x-ua-compatible-->
    <meta http-equiv="x-ua-compatible" content="IE=edge">
    <title>会员管理</title>
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
#orderchoose-excel{
			margin:10px 0;
			visibility:hidden;
		}
		
		#orderchoose-label span{
			color:#fff;
			font-size:20px;
			position:relative;
			top:5px;
		}
		#orderchoose-label{
			width: 80px;
    		height: 30px;
    		color: #fff;
    		background: #f37022;
    		border-radius: 15px;
    		text-align:center !important;
    	}
    	@media (max-width:768px) {
    		#hide-it{
    			display:none;
    		}
    	}	
    	.modal-body{
    		    max-height: 500px;
   				 overflow-x: hidden;
   				 overflow-y: auto;
    	}
    </style>
</head>
<body>
	<!--导航栏-->
    <%@include file="/webpage/include/swustHeader.jsp" %>
<!--预约文字-->
	<input type="hidden" value="${ctx}/swust/order/list"/>
    <div class="container-fluid change-padding">
        <div class="row">
            <div class="col-xs-12">
                <p class="my-booking" ><span style="cursor: pointer;" onclick="model('1')">我的预约  </span>&nbsp;     &nbsp;       <span style="cursor: pointer;" onclick="model('2')">会员管理</span></p>
                
                <ul class="state-menu">
                    <li class="active">
                    	<p style=" font-size: 23px"   >会员列表</p>
                    </li>
                    <li>
                        <button class="glyphicon glyphicon-plus"  > 新增会员</button>
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

<!--新增  -->
<div class="modal fade" tabindex="-1" id="msg-reminder">
	<div class="modal-dialog modal-md">
		<div class="modal-content" style="
    margin-top: 240px;">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h3 class="modal-title text-center" id="textChange">新增会员卡服务</h3>
			</div>
			<div class="modal-body">
				<div class="row">
					<form class="form-horizontal" id="formSubmitAdd" action="" novalidate="novalidate">
						<div class="col-xs-12 col-sm-6">
							<div class="form-group">
								<label for="smsCarId" class="col-sm-5 control-label"><span style="color:red">* </span>会员卡号:</label>
								<div class="col-sm-7">
									<input type="text" class="form-control required valid" name="name" id="smsCarId" maxlength="15" aria-required="true">
									<input type="hidden" id="oldcompany" value="流体力学">
								</div>
							</div>
							<div class="form-group">
								<label for="wechat" class="col-sm-5 control-label">微信号码:</label>
								<div class="col-sm-7">
									<input  class="form-control " name="wechat" id="wechat" maxlength="15"  >
								</div>
							</div>
							<div class="form-group">
								<label for="smsCarType" class="col-sm-5 control-label">会员等级:</label>
								<div class="col-sm-7">
									<select name="carType" id="smsCarType" class="form-control">
										<option value="0">普通用户</option>
										<option value="1">黄金会员</option>
										<option value="2">铂金会员</option>
										<option value="3">钻石会员</option>
										<option value="4">爸爸豁茶</option>
									</select>
								</div>
							</div>
							<div class="form-group">
								<label for="smsServiceTime" class="col-sm-5 control-label">剩余次数:</label>
								<div class="col-sm-7">
									<input  class="form-control " type="number" name="serviceTime" id="smsServiceTime" maxlength="15"  >
								</div>
							</div>
						</div>
						
						<div class="col-xs-12 col-sm-6">
							<div class="form-group">
								<label for="smsOwner" class="col-sm-5 control-label"><span style="color:red">* </span>会员姓名:</label>
								<div class="col-sm-7">
									<input type="text" class="form-control valid" value="" name="owner" maxlength="15" id="smsOwner" aria-required="true">
								</div>
							</div>
							<div class="form-group">
								<label for="smsPhone" class="col-sm-5 control-label"><span style="color:red">* </span>联系方式:</label>
								<div class="col-sm-7">
									<input type="text" class="form-control" maxlength="12" name="phone" id="smsPhone">
								</div>
							</div>
							<div class="form-group">
								<label for="money" class="col-sm-5 control-label">添加金额:</label>
								<div class="col-sm-7">
									<input   class="form-control " type="number"  maxlength="15" name="money" id="money"  >
								</div>
							</div>
							<div class="form-group">
								<label for="gift" class="col-sm-5 control-label">赠送金额:</label>
								<div class="col-sm-7">
									<input  class="form-control "   name="gift" id="gift" value = "0">
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
			<div class="modal-footer text-center">
			<button type="button" class="btn btn-md confirm" onclick="doSubmit()">确认</button>
			</div>
		</div>
	</div>
</div>


<!--警示框-->
<div class="modal fade bs-example-modal-lg warning-box" tabindex="-1" role="dialog"
	id="smsreminderWarning">
    <div class="modal-dialog modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h3 class="modal-title text-center" id="smsdiswarningModal">确认操作？</h3>
            </div>
            <div class="modal-body">
                <h5 class="text-center text-danger" id="smsreminderModal"></h5>
            </div>
            <div class="modal-footer text-center">
            	<button type="button" class="btn btn-md confirm"  id="smsdissubmitModal">确认</button>
                <button type="button" class="btn btn-md confirm" data-dismiss="modal" >关闭</button>
            </div>
        </div>
    </div>
</div>


<div class="modal fade bs-example-modal-lg" id="success-box">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h3 class="modal-title text-center">成功</h3>
            </div>
            <div class="modal-footer text-center">
                <button type="button" class="btn btn-md confirm" data-dismiss="modal">完成</button>
            </div>
        </div>
    </div>
</div>
</body>
<script src="${ctxStatic }/swust/js/jquery.min.js"></script>
<script src="${ctxStatic }/swust/js/bootstrap.min.js"></script>
<script src="${ctxStatic }/swust/js/booking.js"></script>
<script src="${ctxStatic}/jquery-validation/1.14.0/jquery.validate.min.js" type="text/javascript"></script>
<script type="text/javascript">



 

function clearList(){
	$("#oid").val("");
	$("#smsCarId").val("");
	$("#smsOwner").val("");
	$("#smsPhone").val("");
	$("#wechat").val("");
	$("#smsServiceTime").val("");
	$("#smsCarType").val("");
	$("#money").val("");
	$("#gift").val("");
}

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
	function searchMsg(pageNo) {
		$.ajax({
			type:"POST",
			url:"${ctx}/swust/car/portalList",
			data:"name="+$("#reaserch").val()+"&pageNo="+pageNo,
			success:function(date){
				$("#reaserch").attr("onchange","openwin('0','1')");
				$("#reaserch").attr("placeholder","输入姓名或卡号");
				$("#not-check").empty();
				$("#not-check").html(date);
				$(".buttons:button:nth-child(3)").addClass("active");
			}
		});
	}
		$(function(){
			
			$('button:contains("新增")').click(function(){
				clearList();
				$("#smsCarId-error").html("");
				$("#smsOwner-error").html("");
				$("#smsPhone-error").html("");
				$("#smsCarId").attr("class","form-control required");
			      $("#smsOwner").attr("class","form-control required");
			      $("#smsPhone").attr("class","form-control required");
				$("#smsCarId").removeData("previousValue"); 
				$('#msg-reminder').modal('show');
			});
			
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
		$("#inputPassword3").val("");
		$("#old-pwd-error").html("");
		$("#new-pwd-error").html("");
		$("#inputPassword3-error").html("");
		}

		var submit = false;
		var password = false;
		var newandold = false;
		$("#dosubmit").click(function(){
			if(submit&&newandold){
				$.ajax({
					type:"POST",
					url:"${ctx}/swust/order/updateUserByPassword",
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
			url:"${ctx}/swust/order/UserSelectPassword",
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
		openwin("0","${page.pageNo}");
	});
	function openwin(a,b) {
		$.ajax({
			type:"POST",
			url:"${ctx}/swust/car/portalList",
			data:"name="+$("#reaserch").val()+"&pageNo="+b,
			success:function(date){
				$("#reaserch").attr("onchange","openwin('0','1')");
				$("#reaserch").attr("placeholder","输入姓名或卡号");
				$("#not-check").empty();
				$("#not-check").html(date);
				$(".buttons:button:nth-child(3)").addClass("active");
			}
		});
	} 
	function start() {
		location.href = "${ctx}/swust/Member";
	}
	function add() {
		location.href = "${ctx}/swust/Member/addList";
	}
	var validateFormH;
	function doSubmit(){
		
		validateFormH = $("#formSubmitAdd").validate({
			rules:{
				name:{
					checkName:false,
					required:true,
					remote:{                                          
			               type:"POST",//验证卡号是否存在
			               url:"${ctx}/swust/car/checkCardID",
			               data:{
									name:function(){return $("#smsCarId").val();}
				               } 
			            }
				},
				owner:{
					checkName:true,
					required:true
				},
				serviceTime:{
					required:false,
					number:true,
					checkName:false
				},
				phone:{
					required:true,
					rangelength:[11,11],
					number:true
				},
				money:{
					number:true
				},
				gift:{
					number:true
				}
			},
			messages:{
				name:{
					required:"请输入会员卡号",
					remote:"输入的会员卡号已经存在"
				},
				owner:{
					required:"请输入会员姓名"
				},
				serviceTime:{
					number:"请输入数字"
				},
				phone:{
					rangelength:"请输入正确的联系方式",
					required:"请输入联系方式",
					number:"请输入正确的联系方式"
				},
				money:{
					number:"请输入数字"
				},
				gift:{
					number:"请输入数字"
				}

			}
		});
		jQuery.validator.addMethod("checkName", function(value, element) {
	        var char = /^[a-zA-Z\u4e00-\u9fa5]+$/;
	        return this.optional(element) || char.test(value);   
	    }, $.validator.format("只能输入中文、英文"));
		
		
		
		
		
	  if($("#formSubmitAdd").valid()){
		  console.log($("#gift").val()+"!!!!!");
		  $.ajax({
				type : "POST",
				url : "${ctx}/swust/car/insertSysCar",
				data : {
					id:$("#oid").val(),
					money:$("#money").val(),
					carId:$("#smsCarId").val(),
					userName:$("#smsOwner").val(),
					giftMoney:$("#gift").val(),
					wechat:$("#wechat").val(),
					phone:$("#smsPhone").val(),
					disable:'1',
					carType:$("#smsCarType").val(),
					effectiveTime:$("#smsServiceTime").val()
				},
				success : function(data) {
					$("#msg-reminder").modal("hide");
					$("#managerModal").modal("hide");
					$("#smsdissubmitModal").hide();
					$("#smsdiswarningModal").html("消息提示！");
					$("#smsreminderModal").html(data.msg);
					$("#success-box").modal("show");
					setTimeout(function(){
						$("#success-box").modal("hide");
					},1000);
					openwin("0","1");
				}
			});
		  return true;
	  }
	  return false;
	}
</script>

</html>