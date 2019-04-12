<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<style>
	.continue,.edit-info{
	    width: 45px;
    height: 25px;
    border-radius: 12px;
    border: 1px solid #5398ff;
    color: #5398ff;
    }
    .continue:hover,.edit-info:hover{
    background:#5398ff;
    color:#fff;
    }
    .continue-add,.edit-info{
	    width: 45px;
    height: 25px;
    border-radius: 12px;
    border: 1px solid #5398ff;
    color: #5398ff;
    }
    .continue-add:hover,.edit-info:hover{
    background:#5398ff;
    color:#fff;
    }
</style>
<!--新增  -->
<div class="modal fade" tabindex="-1" id="continue">
	<div class="modal-dialog modal-md">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h3 class="modal-title text-center">续费会员卡服务</h3>
			</div>
			<div class="modal-body">
				<div class="row">
					<form class="form-horizontal" id="charge-Form" novalidate="novalidate">
						<input type="hidden" name="id" id="oid-change" value="">
						<div class="col-xs-12 col-sm-6">
							<div class="form-group">
								<label for="recharge-CarId" class="col-sm-5 control-label" style="text-align:center"><span style="color:red">* </span>会员卡号:</label>
								<div class="col-sm-7">
									<input type="text" class="form-control required valid"  readonly="readonly" name="recharge-CarId" id="recharge-CarId" aria-required="true">
								</div>
							</div>
							<div class="form-group">
								<label for="recharge-Phone" class="col-sm-5 control-label">联系方式:</label>
								<div class="col-sm-7">
									<input type="text" class="form-control " readonly="readonly" name="recharge-Phone" id="recharge-Phone">
								</div>
							</div>
							<div class="form-group">
								<label for="recharge-Time" class="col-sm-5 control-label">续约次数(次):</label>
								<div class="col-sm-7">
									<input type="number" class="form-control " min="0" step="1" name="recharge-Time" id="recharge-Time">
								</div>
							</div>
						</div>
						<div class="col-xs-12 col-sm-6">
							<div class="form-group">
								<label for="recharge-Owner" class="col-sm-5 control-label"><span style="color:red">* </span>会员姓名:</label>
								<div class="col-sm-7">
									<input type="text" class="form-control required valid" value="" readonly="readonly" name="recharge-Owner" id="recharge-Owner" aria-required="true">
									<input type="hidden" id="oldcompanyName" value="ltlx">
								</div>
							</div>	
							<div class="form-group">
								<label for="surplus-Time" class="col-sm-5 control-label">剩余次数(次):</label>
								<div class="col-sm-7">
									<input type="text" class="form-control " readonly="readonly" name="surplus-Time" id="surplus-Time">
								</div>
							</div>
							<div class="form-group">
								<label for="recharge-Money" class="col-sm-5 control-label">续费费用:</label> 
								<div class="col-sm-7">
									<input type="number" class="form-control " min="0" name="recharge-Money" id="recharge-Money">
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
			<div class="modal-footer text-center">
			<button type="button" class="btn btn-md confirm" onclick="rechargeFormSubmit()" data-dismiss="modal">确认</button>
				<input type="hidden" id = "inputSubmit">
				<button type="button" class="btn btn-md confirm"
					data-dismiss="modal">关闭</button>
			</div>
		</div>
	</div>
</div>
<table class="table table-striped" id="orderList">
	<thead>
		<tr>
			<th><input type="checkbox" class="checkAll1"></th>
			<th>编号</th>
			<th>会员卡号</th>
			<th>会员姓名</th>
			<th>会员等级</th>
			<th>手机号码</th>
			<th>剩余次数（次）</th>
			<th>剩余费用（元）</th>
			<th>起始时间</th>
			<th>截止时间</th>
             	<th>操作</th>
		</tr>
	</thead>
	<tbody class="t1">
		<c:forEach items="${page.list}" var="row">
			<tr>
				<td id="${row.id}"><span style="color:red;"><input type="checkbox" name="checkss"
					value="${row.id}" class="i-checks"></span></td>
				<td>${row.remarks}</td>
				<td>${row.carId}</td>
				<td>${row.userName}</td>
				<td>
				<c:if test="${row.carType eq '1'}">黄金会员</c:if>
			 <c:if test="${row.carType eq '2'}">铂金会员</c:if>
			 <c:if test="${row.carType eq '3'}">钻石会员</c:if>
			 <c:if test="${row.carType eq '4'}">土豪爸爸</c:if>
				</td>
				<td>${row.phone}</td>
				<td>${row.effectiveTime}</td>
				<td>${row.money}</td>
				<td><fmt:formatDate value="${row.beginTime }" pattern="yyyy-MM-dd"/></td>
				<td><fmt:formatDate value="${row.endTime }" pattern="yyyy-MM-dd"/></td>
				<td>
				<button class="continue-add">
				<input type="hidden"  value="${row.id}"/>
				续费
				</button>
				<button class="edit-info">
				<input type="hidden"  value="${row.id}"/>
				编辑
				</button>
				<button class="refuse" onclick="deleteSms('${row.id}')">
				删除
				</button>
				</td>
			</tr>
		</c:forEach>
		
		<tr>
		<td colspan="13">
				<div class="buttons text-right">
                    ${page}
                </div>
			</td>
		</tr>
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	</tbody>
</table>

<!--编辑  -->
<div class="modal fade" tabindex="-1" id="msg-reminder-change">
	<div class="modal-dialog modal-md">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h3 class="modal-title text-center" id="textChange-change">新增会员卡服务</h3>
			</div>
			<div class="modal-body">
				<div class="row">
					<form class="form-horizontal" id="formsubmit-change" action="" novalidate="novalidate">
						<div class="col-xs-12 col-sm-6">
							<div class="form-group">
								<label for="smsCarId-change" class="col-sm-5 control-label"><span style="color:red">* </span>会员卡等级:</label>
								<div class="col-sm-7">
									<input type="text" class="form-control required valid" onchange="" name="name" id="smsCarId-change" aria-required="true">
								</div>
							</div>
							<div class="form-group">
								<label for="smsCarType-change" class="col-sm-5 control-label">会员等级:</label>
								<div class="col-sm-7">
									<select name="carType-change" id="smsCarType-change" class="form-control">
										<option value="1">黄金会员</option>
										<option value="2">铂金会员</option>
										<option value="3">钻石会员</option>
										<option value="4">土豪爸爸</option>
									</select>
<!-- 									<input type="text" class="form-control " name="carType" id="carType"> -->
								</div>
							</div>
							<div class="form-group">
								<label for="smsServiceTime-change" class="col-sm-5 control-label">添加次数(次):</label>
								<div class="col-sm-7">
									<input  class="form-control " name="serviceTime-change" id="smsServiceTime-change" placeholder="请输入天数">
								</div>
							</div>
						</div>
						<div class="col-xs-12 col-sm-6">
							<div class="form-group">
								<label for="smsOwner-change" class="col-sm-5 control-label"><span style="color:red">* </span>会员姓名:</label>
								<div class="col-sm-7">
									<input type="text" class="form-control valid" value="" name="owner" id="smsOwner-change" aria-required="true">
									<input type="hidden" id="oldcompanyName" value="ltlx">
								</div>
							</div>
							<div class="form-group">
								<label for="smsPhone-change" class="col-sm-5 control-label"><span style="color:red">* </span>联系方式:</label>
								<div class="col-sm-7">
									<input type="text" class="form-control" name="phone-change" id="smsPhone-change">
								</div>
							</div>
							<div class="form-group">
								<label for="money-change" class="col-sm-5 control-label">添加费用:</label>
								<div class="col-sm-7">
									<input type="text" class="form-control " name="money-change" id="money-change">
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
			<div class="modal-footer text-center">
			<button type="button" class="btn btn-md confirm" onclick="doSubmitChange()" >确认</button>
				<button type="button" class="btn btn-md confirm"
					data-dismiss="modal">关闭</button>
			</div>
		</div>
	</div>
</div>


<script src="${ctxStatic}/swust/js/backstagePlatform.js"></script>
<script>
function batchDeleteSms(){
	var selectIds = new Array();
	$(".t1").find("input[type='checkbox']:checked").each(function(){ 
		var msgs = $(this).val();
		selectIds.push(msgs);
	})
	if(selectIds.length>0){
		$("#smsdissubmitModal").attr("onclick","batchSubmitDeleteSms()");
		$("#smsreminderModal").html("确认删除？")
		$("#smsdiswarningModal").html("确认提示！");
		$("#smsdissubmitModal").show();
		$("#smsreminderWarning").modal("show");
	}else{
		$("#smsdissubmitModal").hide();
		$("#smsreminderModal").html("请选择需要批量操作的信息！");
		$("#smsdiswarningModal").html("警告提示！");
		$("#smsreminderWarning").modal("show");
	}
	}
	
function batchSubmitDeleteSms(){
	$(".modal-backdrop").hide();		
	var selectIds = new Array(); 
	$("#orderList tbody").find("input[type='checkbox']:checked").each(function(){ 
		var msg = $(this).val();
		selectIds.push(msg);
	})
	$.ajax({
		type : "POST",
		url : "${ctx}/swust/car/batchDelete",
		traditional: true,
		data : {
			ids : selectIds,
		},
	});
	SMSSend();
	}
	
function deleteSms(id){
	$("#cacheId").val(id);
	$("#smsdissubmitModal").attr("onclick","deleteSubmitSms()");
	$("#smsreminderModal").html("确认删除？")
	$("#smsdiswarningModal").html("确认提示！");
	$("#smsdissubmitModal").show();
	$("#smsreminderWarning").modal("show");
}

function deleteSubmitSms(){
	$(".modal-backdrop").hide();	
	var id= $("#cacheId").val();
	var selectIds = new Array(); 
	selectIds.push(id);
	$.ajax({
		type : "POST",
		url : "${ctx}/swust/car/batchDelete",
		traditional: true,
		data : {
			ids : selectIds,
		},
	});
	SMSSend();
}

$(function(){
	document.getElementById("recharge-Time").addEventListener("input",function(event){
	       event.target.value = event.target.value.replace(/\-/g,""); 
	    });
	document.getElementById("recharge-Money").addEventListener("input",function(event){
	       event.target.value = event.target.value.replace(/\-/g,""); 
	    });

	validateChangeForm = $("#formsubmit-change").validate({
		rules:{
			name:{
				checkName:false,
				required:true,
				number:true
			},
			owner:{
				checkName:false,
				required:true
			},
			serviceTime:{
				required:false,
				number:true,
				checkName:false
			},
			"phone-change":{
				required:true,
				rangelength:[11,11],
				number:true
			},
			money:{
				number:true
			}
		},
		messages:{
			name:{
				rangelength:"请输入正确的会员卡号",
				number:"请输入正确会员卡号"
			},
			owner:{
				required:"请输入会员姓名"
			},
			serviceTime:{
				number:"请输入数字"
			},
			"phone-change":{
				rangelength:"请输入正确的联系方式",
				number:"请输入正确的联系方式"
			},
			money:{
				number:"请输入数字"
			}

		},
	});
	jQuery.validator.addMethod("checkName", function(value, element) {
        var char = /^[a-zA-Z\u4e00-\u9fa5]+$/;
        return this.optional(element) || char.test(value);   
    }, $.validator.format("只能输入中文、英文"));
	
});
var validateChangeForm;
var carId =false;
function validateCarId() {
	$.ajax({
		type:"post",
		url:"${ctx}/swust/car/checkCarId",
		data:{
			carId:$("#smsCarId-change").val(),
			id:$("#oid-change").val()
		},
		success:function(data){
			if(data.success){
				carId=true;
				$("#smsCarId-change").attr("class","form-control required valid");
			}else{
				carId=false;
				if($("#smsCarId-change-error").html()===""){
					console.log(carId);
					$("#smsCarId-change").attr("class","form-control required error")
					$("#smsCarId-change-error").attr("style","");
					$("#smsCarId-change-error").html("输入的用户名已存在")
					return;
				}
				if($("#smsCarId-change-error").length>0){
					console.log(carId);
					return;
				}
				$("#smsCarId-change").attr("class","form-control required error")
	            var label_var=$("<label for='smsCarId-change' id='smsCarId-change-error' class='error'>输入的用户名已存在</label>")
	            label_var.insertAfter($('#smsCarId-change'));
				console.log(carId);
			}
		}
	})
}
function doSubmitChange(){
  if(validateChangeForm.form()){
	  $.ajax({
			type : "POST",
			url : "${ctx}/swust/car/insertSysCar",
			data : {
				id:$("#oid-change").val(),
				money:$("#money-change").val(),
				carId:$("#smsCarId-change").val(),
				userName:$("#smsOwner-change").val(),
				phone:$("#smsPhone-change").val(),
				carType:$("#smsCarType-change").val(),
				effectiveTime:$("#smsServiceTime-change").val()
			},
			success : function(data) {
				$("#msg-reminder-change").modal("hide");
				$("#success-box").modal("show");
				setTimeout(function(){
					$("#success-box").modal("hide");
					$('.modal-backdrop').hide();
				},2000)
				searchMsg(1);
			}
		});
	  return true;
  }
  return false;
}



	$('table').on('click','.continue-add',function(){
		var sid=$(this).children().val();
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
			})
		}
	).on('click','.edit-info',function(){
		var ssid=$(this).children().val();
			$.ajax({
				type:"post",
				url : "${ctx}/swust/car/getCar",
				data: {id:ssid},
				success : function(data) {
					$("#smsCarId-change").val(data.body.sysCar.carId);
					$("#smsCarType-change").val(data.body.sysCar.carType);
					$("#smsPhone-change").val(data.body.sysCar.phone);
					$("#smsOwner-change").val(data.body.sysCar.userName);
					$("#smsServiceTime-change").val(data.body.sysCar.effectiveTime);
					$("#money-change").val(data.body.sysCar.money);
					$("#oid-change").val(data.body.sysCar.id)
				}
			});
			$("#smsCarId-change").attr("class","form-control required valid");
			$("#smsCarId-change-error").html("");
			carId=true;
				$("#textChange-change").html("编辑短信提醒服务");
			$('#msg-reminder-change').modal('show');
			
	});
	
	
	function rechargeFormSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
	if($("#recharge-Money").val()===""||$("#recharge-Money").val()==null){
		$("#recharge-Money").val(0)
	}
	if($("#recharge-Time").val()===""||$("#recharge-Time").val()==null){
		$("#recharge-Time").val(0)
	}
		$('#continue').modal('hide');
		  $.ajax({
				type : "POST",
				url : "${ctx}/swust/car/rechargeTime",
				data : {
					id:$("#inputSubmit").val(),
					rechargeMoney:$("#recharge-Money").val(),
					carId:$("#recharge-CarId").val(),
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
					searchMsg(1);
					setTimeout(function(){
						$("#success-box").modal("hide");
						$('.modal-backdrop').hide();
					},2000)
				}
			});
	}
</script>	
<script src="${ctxStatic}/swust/js/backstagePlatform.js"></script>




