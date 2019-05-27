<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<style>
.continue, .edit-info,.detail {
	width: 45px;
	height: 25px;
	border-radius: 12px;
	border: 1px solid #5398ff;
	color: #5398ff;
}

.continue:hover, .edit-info:hover,.detail:hover {
	background: #5398ff;
	color: #fff;
}

.continue-add, .edit-info,.detail {
	width: 45px;
	height: 25px;
	border-radius: 12px;
	border: 1px solid #5398ff;
	color: #5398ff;
}

.continue-add:hover, .edit-info:hover,.detail:hover {
	background: #5398ff;
	color: #fff;
}
</style>
<table class="table table-striped">
	<thead>
		<tr>
			<th>编号</th>
			<th>会员卡号</th>
			<th>会员姓名</th>
			<th>会员等级</th>
			<th>手机号码</th>
			<th>微信号</th>
			<th>剩余次数（次）</th>
			<th>剩余费用（元）</th>
			<th>起始时间</th>
			<th>截止时间</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody id="unpass">
		<c:forEach items="${page.list}" var="row">
			<tr>
				<td>${row.remarks}</td>
				<td>${row.carId}</td>
				<td>${row.userName}</td>
				<td>
				<c:choose>
					<c:when test="${row.carType eq '1'}">黄金会员</c:when> <c:when
						test="${row.carType eq '2'}">铂金会员</c:when> <c:when
						test="${row.carType eq '3'}">钻石会员</c:when> <c:when
						test="${row.carType eq '4'}">土豪爸爸</c:when>
						<c:otherwise>
							普通用户
						</c:otherwise>
				</c:choose>
				</td>
				<td>${row.phone}</td>
				<td>${row.wechat}</td>
				<td>${row.effectiveTime}</td>
				<td>${row.totalMoney}</td>
				<td><fmt:formatDate value="${row.beginTime }"
						pattern="yyyy-MM-dd" /></td>
				<td><fmt:formatDate value="${row.endTime }"
						pattern="yyyy-MM-dd" /></td>
				<td>
					<button class="continue-add">
						<input type="hidden" value="${row.id}" /> 续费
					</button>
					<button class="edit-info">
						<input type="hidden" value="${row.id}" /> 消费
					</button>
					<button class="detail">
						<input type="hidden" value="${row.id}" /> 明细
					</button>
				</td>
			</tr>
		</c:forEach>

		<tr>
			<td colspan="13">
				<div class="buttons text-right">${page}</div>
			</td>
		</tr>
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
	</tbody>
	</tbody>
</table>



<div class="modal fade" tabindex="-1" id="continue">
	<div class="modal-dialog modal-md">
		<div class="modal-content" style="margin-top: 240px;">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h3 class="modal-title text-center">续费会员卡服务</h3>
			</div>
			<div class="modal-body">
				<div class="row">
					<form class="form-horizontal" id="charge-Form"
						novalidate="novalidate">
						<input type="hidden" name="id" id="oid-change" value="">
						<input type="hidden" name="id" id="addMoney" value="">
						<div class="col-xs-12 col-sm-6">
							<div class="form-group">
								<label for="recharge-CarId" class="col-sm-5 control-label"
									style="text-align: center"><span style="color: red">*
								</span>会员卡号:</label>
								<div class="col-sm-7">
									<input type="text" class="form-control required valid"
										readonly="readonly" name="recharge-CarId" id="recharge-CarId"
										aria-required="true">
								</div>
							</div>
							<div class="form-group">
								<label for="recharge-Phone" class="col-sm-5 control-label">联系方式:</label>
								<div class="col-sm-7">
									<input type="text" class="form-control " readonly="readonly"
										name="recharge-Phone" id="recharge-Phone">
								</div>
							</div>
							<div class="form-group">
								<label for="recharge-Time" class="col-sm-5 control-label">续约次数:</label>
								<div class="col-sm-7">
									<input type="number" class="form-control " min="0" step="1"
										name="recharge-Time" id="recharge-Time">
								</div>
							</div>
							<div class="form-group">
								<label for="giftTime" class="col-sm-5 control-label">赠送次数:</label>
								<div class="col-sm-7">
									<input type="number" class="form-control " min="0" step="1"
										name="giftTime" id="giftTime">
								</div>
							</div>
						</div>
						<div class="col-xs-12 col-sm-6">
							<div class="form-group">
								<label for="recharge-Owner" class="col-sm-5 control-label"><span
									style="color: red">* </span>会员姓名:</label>
								<div class="col-sm-7">
									<input type="text" class="form-control required valid" value=""
										readonly="readonly" name="recharge-Owner" id="recharge-Owner"
										aria-required="true"> <input type="hidden"
										id="oldcompanyName" value="ltlx">
								</div>
							</div>
							<div class="form-group">
								<label for="surplus-Time" class="col-sm-5 control-label">剩余次数(次):</label>
								<div class="col-sm-7">
									<input type="text" class="form-control " readonly="readonly"
										name="surplus-Time" id="surplus-Time">
								</div>
							</div>
							<div class="form-group">
								<label for="recharge-Money" class="col-sm-5 control-label">续费金额:</label>
								<div class="col-sm-7">
									<input type="number" class="form-control " value="0"
										name="recharge-Money" id="recharge-Money">
								</div>
							</div>
							<div class="form-group">
								<label for="giftMoney" class="col-sm-5 control-label">赠送金额:</label>
								<div class="col-sm-7">
									<input type="number" class="form-control "   value="0"
										name="giftsMoney" id="giftsMoney">
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
			<div class="modal-footer text-center">
				<button type="button" class="btn btn-md confirm"
					onclick="rechargeFormSubmit()" data-dismiss="modal">确认</button>
				<input type="hidden" id="inputSubmit">
			</div>
		</div>
	</div>
</div>
<!--编辑  -->
<div class="modal fade" tabindex="-1" id="msg-reminder-change">
	<div class="modal-dialog modal-md">
		<div class="modal-content" style="margin-top: 240px">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h3 class="modal-title text-center" id="textChange-change">会员卡消费</h3>
			</div>
			<div class="modal-body">
				<div class="row">
					<form class="form-horizontal" id="formsubmit-change" action=""
						novalidate="novalidate">
						<div class="col-xs-12 col-sm-6">
							<div class="form-group">
								<label for="smsCarId-change" class="col-sm-5 control-label"><span
									style="color: red">* </span>会员卡号:</label>
								<div class="col-sm-7">
									<input type="hidden" id="leaveMoney">
									<input type="hidden" id="leaveTime">
									<input type="text" class="form-control required valid"
										readonly="readonly" onchange="" name="name"
										id="smsCarId-change" aria-required="true">
								</div>
							</div>
							<div class="form-group">
								<label for="smsCarType-change" class="col-sm-5 control-label">会员等级:</label>
								<div class="col-sm-7">
									<select name="carType-change" id="smsCarType-change"
										class="form-control">
										<option value="0">普通用户</option>
										<option value="1">黄金会员</option>
										<option value="2">铂金会员</option>
										<option value="3">钻石会员</option>
										<option value="4">土豪爸爸</option>
									</select>
									<!-- 									<input type="text" class="form-control " name="carType" id="carType"> -->
								</div>
							</div>
							<div class="form-group">
								<label for="smsServiceTime-change"
									class="col-sm-5 control-label">消费金额:</label>
								<div class="col-sm-7">
									<!-- 									<input  class="form-control " name="serviceTime-change" id="smsServiceTime-change"  > -->
									<select name="smsServiceTime-change" id="smsServiceTime-change"
										class="form-control">
										<option value="0">0</option>
										<option value="-98">98</option>
										<option value="-108">108</option>
										<option value="-118">118</option>
										<option value="-128">128</option>
										<option value="-88">88</option>
										<option value="-78">78</option>
										<option value="-49">49</option>
										<option value="-64">64</option>
									</select>
								</div>
							</div>
						</div>
						<div class="col-xs-12 col-sm-6">
							<div class="form-group">
								<label for="smsOwner-change" class="col-sm-5 control-label"><span
									style="color: red">* </span>会员姓名:</label>
								<div class="col-sm-7">
									<input type="text" readonly="readonly"
										class="form-control valid" value="" name="owner"
										id="smsOwner-change" aria-required="true">
								</div>
							</div>
							<div class="form-group">
								<label for="money-change" class="col-sm-5 control-label">消费次数:</label>
								<div class="col-sm-7">
									<input type="number" class="form-control " name="money-change" value = "0"
										id="money-change">
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
			<div class="modal-footer text-center">
				<button type="button" class="btn btn-md confirm"
					onclick="doSubmitChange()">确认</button>
			</div>
		</div>
	</div>
</div>



<div class="modal fade" tabindex="-1" id="detail">
	<div class="modal-dialog modal-md">
		<div class="modal-content" style="margin-top: 240px;">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h3 class="modal-title text-center">会员充值消费详情</h3>
			</div>
			<div class="modal-body">
				<div class="row">
					<form class="form-horizontal" id="recharge-Form"
						novalidate="novalidate">
						<input type="hidden" name="id" id="detail-change" value="">
						<div class="col-xs-12 col-sm-6">
							<div class="form-group">
								<label for="detail-CarId" class="col-sm-5 control-label"
									style="text-align: center"><span style="color: red">*
								</span>会员卡号:</label>
								<div class="col-sm-7">
									<input type="text" class="form-control required valid"
										readonly="readonly" name="detail-CarId" id="detail-CarId"
										aria-required="true">
								</div>
							</div>
							<div class="form-group">
								<label for="detail-Phone" class="col-sm-5 control-label">消费总计:</label>
								<div class="col-sm-7">
									<input type="text" class="form-control " readonly="readonly"
										name="detail-Phone" id="detail-Phone">
								</div>
							</div>
						</div>
						<div class="col-xs-12 col-sm-6">
							<div class="form-group">
								<label for="recharge-Owner" class="col-sm-5 control-label"><span
									style="color: red">* </span>会员姓名:</label>
								<div class="col-sm-7">
									<input type="text" class="form-control required valid" value=""
										readonly="readonly" name="detail-Owner" id="detail-Owner"
										aria-required="true"> <input type="hidden"
										id="detailName" value="ltlx">
								</div>
							</div>
							<div class="form-group">
								<label for="detail-Time" class="col-sm-5 control-label">充值总计:</label>
								<div class="col-sm-7">
									<input type="text" class="form-control " readonly="readonly"
										name="detail-Time" id="detail-Time">
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
			<div class="modal-footer text-center">
<!-- 				<button type="button" class="btn btn-md confirm" -->
<!-- 					onclick="rechargeFormSubmit()" data-dismiss="modal">确认</button> -->
					<button type="button" class="btn btn-md confirm"
					onclick="detailRechargeExport()" data-dismiss="modal">导出</button>
				<input type="hidden" id="detailSubmit">
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">

function page(a,b,detail) {
	$.ajax({
		type:"POST",
		url:"${ctx}/swust/car/portalList",
		data:"pageNo="+a,
		success:function(date){
			$("#reaserch").attr("placeholder","输入姓名或卡号");
			$("#reaserch").attr("onchange","openwin('0','1')");
			$("#not-check").empty();
			$("#not-check").html(date);
			$(".buttons:button:nth-child(3)").addClass("active");
//				search(0,1);
		}
	});
} 


	function detailRechargeExport() {
		location.href = "${ctx}/swust/car/exportDetail?id=" + $("#detailSubmit").val();
	}


	function detail(id){
		$.ajax({
			type:"POST",
			url:"${ctx}/swust/order/UserSelectOrderReason",
			data:"id="+id,
			success:function(date){
				var arr = new Array();
				 arr = date.msg.split(",");
				 if(arr[0]!=null&&arr[0]!=''&&arr[0]!='null'){
				$("#order_reason").html(arr[0]);
				 }else{
					 $("#order_reason").html("");
				 }
				if(arr[1] === ''){
					arr[1]="未设置备注";
					}
				$("#remarks").html(arr[1]); 
				$(".buttons:button:nth-child(3)").addClass("active");
			}
		});
	} 

	function rechargeFormSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		if($("#recharge-Money").val()===""||$("#recharge-Money").val()==null){
			$("#recharge-Money").val(0);
		}
		if($("#recharge-Time").val()===""||$("#recharge-Time").val()==null){
			$("#recharge-Time").val(0);
		}
			$('#continue').modal('hide');
			  $.ajax({
					type : "POST",
					url : "${ctx}/swust/car/rechargeTime",
					data : {
						id:$("#inputSubmit").val(),
						rechargeMoney:$("#recharge-Money").val(),
						carId:$("#recharge-CarId").val(),
						giftTime:$("#giftTime").val(),
						giftMoney:$("#giftsMoney").val(),
						totalMoney:$("#addMoney").val(),
						userName:$("#recharge-Owner").val(),
						rechargeTime:$("#recharge-Time").val(),
						carType:$("#recharge-Cartype").val(),
					},
					success : function(data) {
						$("#msg-reminder").modal("hide");
						$("#managerModal").modal("hide");
						$("#smsdiswarningModal").html("消息提示！");
						$("#smsreminderModal").html(data.msg);
						$("#success-box").modal("show");
						openwin("0","1");
						setTimeout(function(){
							$("#success-box").modal("hide");
							$('.modal-backdrop').hide();
						},1000);
					}
				});
		}


$(function () {
	  $("#nav").on("click",".search",function () {
	    $(this).siblings("input").addClass("change-long");
	  });
	  $(".state-menu").on("click","a",function () {
	    $(this).addClass("current").parent().siblings().children("a").removeClass("current")
	  });
	  $(".buttons").on("click","button",function () {
	    $(this).addClass("active").siblings().removeClass("active")
	  });
	  $("table").on("click",".check-view",function () {
	    $("#check-view").modal("show");
	  });
		$('table').on('click','.continue-add',function(){
			var sid=$(this).children().val();
			$("#addMoney").val($(this).parent().prev().prev().prev().html());
			$("#inputSubmit").val(sid);
				$.ajax({
					type:"post",
					url : "${ctx}/swust/car/getCar",
					data: {id:sid},
					success : function(data) {
						$("#recharge-CarId").val(data.body.sysCar.carId);
						$("#recharge-Phone").val(data.body.sysCar.phone);
						$("#recharge-Owner").val(data.body.sysCar.userName);
						$("#surplus-Time").val(data.body.sysCar.effectiveTime);
						$("#recharge-Money").val("");
						$("#recharge-Time").val("");
						$('#continue').modal('show');
					}
				});
			}
		).on('click','.detail',function(){
			var sid=$(this).children().val();
			$("#inputSubmit").val(sid);
				$.ajax({
					type:"post",
					url : "${ctx}/swust/car/getCar",
					data: {id:sid},
					success : function(data) {
						$("#detail-CarId").val(data.body.sysCar.carId);
						$("#detail-Phone").val(data.body.sysCar.memberDetail.costMoney);
						$("#detail-Owner").val(data.body.sysCar.userName);
						$("#detail-Time").val(data.body.sysCar.memberDetail.addMoney);
						$("#detailSubmit").val(data.body.sysCar.id);
						$('#detail').modal('show');
					}
				});
			}
		).on('click','.edit-info',function(){
			var ssid=$(this).children().val();
			$("#leaveMoney").val($(this).parent().prev().prev().prev().html());
			$("#leaveTime").val($(this).parent().prev().prev().prev().prev().html());
				$.ajax({
					type:"post",
					url : "${ctx}/swust/car/getCar",
					data: {id:ssid},
					success : function(data) {
						$("#smsCarId-change").val(data.body.sysCar.carId);
						$("#smsCarType-change").val(data.body.sysCar.carType);
						$("#smsPhone-change").val(data.body.sysCar.phone);
						$("#smsOwner-change").val(data.body.sysCar.userName);
						$("#oid-change").val(data.body.sysCar.id);
					}
				});
				$("#smsCarId-change").attr("class","form-control required valid");
				$("#smsCarId-change-error").html("");
				carId=true;
					$("#textChange-change").html("会员卡消费");
				$('#msg-reminder-change').modal('show');
				
		});
	});

	
function doSubmitChange(){
	  if(validateChangeForm.form()){
		  $.ajax({
				type : "POST",
				url : "${ctx}/swust/car/consumption",
				data : {
					id:$("#oid-change").val(),
					carId:$("#smsCarId-change").val(),
					userName:$("#smsOwner-change").val(),
					carType:$("#smsCarType-change").val(),
					costMoney:$("#smsServiceTime-change").val(),
					costTime:$("#money-change").val()
					
				},
				success : function(data) {
					$("#msg-reminder-change").modal("hide");
					$("#success-box").modal("show");
					setTimeout(function(){
						$("#success-box").modal("hide");
						$('.modal-backdrop').hide();
					},1000);
					openwin("0","1");
				}
			});
		  return true;
	  }
	  return false;
	}
validateChangeForm = $("#formsubmit-change").validate({
	rules:{
		name:{
			required:true,
		},
		owner:{
			checkName:false,
			required:true
		},
		"money-change":{
			required:false,
			number:true,
			checkTimes:true
		},
		"phone-change":{
			required:true,
			rangelength:[11,11],
			number:true
		},
		"smsServiceTime-change":{
			checkMoney:true
		}
	},
	messages:{
		name:{
			required:"请输入",
		},
		owner:{
			required:"请输入会员姓名"
		},
		"money-change":{
			number:"请输入数字"
		},
		"phone-change":{
			rangelength:"请输入正确的联系方式",
			number:"请输入正确的联系方式"
		}

	},
});
jQuery.validator.addMethod("checkName", function(value, element) {
    var char = /^[a-zA-Z\u4e00-\u9fa5]+$/;
    return this.optional(element) || char.test(value);   
}, $.validator.format("只能输入中文、英文"));

jQuery.validator.addMethod("checkMoney", function(value, element) {
    if((-value)<=$("#leaveMoney").val()){
    	return true;
    }else{
    	return false; 
    }
}, $.validator.format("余额不足！请充值"));

jQuery.validator.addMethod("checkTimes", function(value, element) {
	console.log(value+"!!!!!!"+$("#leaveTime").val())
	var ind = $("#leaveTime").val();
    if(parseInt(value)<=parseInt(ind)){
    	return true;
    }else{
    	return false; 
    }
}, $.validator.format("消费次数不足！请充值"));

</script>
















