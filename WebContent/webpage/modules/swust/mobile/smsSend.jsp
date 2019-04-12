<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
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
	.error{
	color:red;
	}
.state-menu{
	height:36px;
	top:10px !important;
}
@media (max-width:768px){
	.state-menu{
		height:auto;
	}
	#unique{
		margin-left:40px;
	}
}
</style>
<div class="col-xs-12">
	<p class="header">
	<span style="width:200px;display:inlin-block;height:35px;line-height:35px;position:relative;" id="unique">
		<input type="search" style="height:35px;line-height:35px;border-radius:33px;width:200px;" placeholder="请输入搜索车牌号" id="named"> <span></span>
		<button style="position:absolute;right:13px;top:-3px;" onclick="searchMsg()">
			<img src="${ctxStatic }/swust/images/graySearch.png"/>
		</button>
	</span>
	</p>
	<ul class="state-menu">
		<li id="batch">
			<button onclick="batchDeleteSms()"> 批量删除</button>
			<button onclick="clearList()"> 新增</button>
			<button onclick="exportSysOrder()">导出</button>
		</li>
	</ul>
	<div class="tab-content">
		<div class="table-responsive tab-pane active" id="not-check">
		<input type="hidden" id="stateKey" value="0">
			<div id="carList">
			
				
			</div>
		</div>
	</div>
</div>


<!--新增  -->
<div class="modal fade" tabindex="-1" id="msg-reminder">
	<div class="modal-dialog modal-md">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h3 class="modal-title text-center" id="textChange">新增短信提醒服务</h3>
			</div>
			<div class="modal-body">
				<div class="row">
					<form class="form-horizontal" id="formSubmitAdd" action="" novalidate="novalidate">
						<div class="col-xs-12 col-sm-6">
							<div class="form-group">
								<label for="smsCarId" class="col-sm-5 control-label"><span style="color:red">* </span>车牌号码:</label>
								<div class="col-sm-7">
									<input type="text" class="form-control required valid" name="name" id="smsCarId" aria-required="true">
									<input type="hidden" id="oldcompany" value="流体力学">
								</div>
							</div>
							<div class="form-group">
								<label for="smsCarType" class="col-sm-5 control-label">车辆类型:</label>
								<div class="col-sm-7">
									<select name="carType" id="smsCarType">
										<option value="1">小型汽车</option>
										<option value="2">大型汽车</option>
										<option value="3">超大型汽车</option>
									</select>
<!-- 									<input type="text" class="form-control " name="carType" id="carType"> -->
								</div>
							</div>
							<div class="form-group">
								<label for="smsServiceTime" class="col-sm-5 control-label">服务时限(月):</label>
								<div class="col-sm-7">
									<input  class="form-control " name="serviceTime" id="smsServiceTime" placeholder="请输入月份基数">
								</div>
							</div>
						</div>
						<div class="col-xs-12 col-sm-6">
							<div class="form-group">
								<label for="smsOwner" class="col-sm-5 control-label"><span style="color:red">* </span>车主姓名:</label>
								<div class="col-sm-7">
									<input type="text" class="form-control valid" value="" name="owner" id="smsOwner" aria-required="true">
									<input type="hidden" id="oldcompanyName" value="ltlx">
								</div>
							</div>
							<div class="form-group">
								<label for="smsPhone" class="col-sm-5 control-label"><span style="color:red">* </span>联系电话:</label>
								<div class="col-sm-7">
									<input type="text" class="form-control" name="phone" id="smsPhone">
								</div>
							</div>
							<div class="form-group">
								<label for="money" class="col-sm-5 control-label">服务费用:</label>
								<div class="col-sm-7">
									<input type="text" class="form-control " name="money" id="money">
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
			<div class="modal-footer text-center">
			<button type="button" class="btn btn-md confirm" onclick="doSubmit()">确认</button>
				<button type="button" class="btn btn-md confirm"
					data-dismiss="modal">关闭</button>
			</div>
		</div>
	</div>
</div>



 <!--警示框  -->
<div class="modal fade bs-example-modal-lg warning-box2">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h3 class="modal-title text-center" id="warningModal">成功</h3>
            </div>
            <div class="modal-body">
                <h5 class="text-center text-danger" id="reminder"></h5>
            </div>
            <div class="modal-footer text-center">
            	<input type="hidden" id="submitOnly" />
<!--                 <button type="button" class="btn btn-md confirm" onclick="batchSubmit()" id="submitModal">确认</button> -->
                <button type="button" class="btn btn-md confirm" data-dismiss="modal">完成</button>
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
<script type="text/javascript">
	var type = "0";
	var btype = "1";
	$(function(){
	
		searchMsg(1);
	});
	var validateFormH;
	function doSubmit(){
		
		validateFormH = $("#formSubmitAdd").validate({
			rules:{
				name:{
					checkName:false,
					required:true,
					rangelength:[7,7],
					remote:{                                          
		               type:"POST",//验证单位是否存在
		               url:"mobile/swust/car/checkName",
		               data:{
		                 carId:function(){return $("#smsCarId").val()},
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
				}
			},
			messages:{
				name:{
					required:"请输入车牌号",
					rangelength:"请输入正确的车牌号",
					remote:"车牌号已经存在"
				},
				owner:{
					required:"请输入车主姓名"
				},
				serviceTime:{
					number:"请输入数字"
				},
				phone:{
					rangelength:"请输入正确的电话号码",
					required:"请输入电话号码",
					number:"请输入正确的电话号码"
				},
				money:{
					number:"请输入数字"
				}

			}
		});
		jQuery.validator.addMethod("checkName", function(value, element) {
	        var char = /^[a-zA-Z\u4e00-\u9fa5]+$/;
	        return this.optional(element) || char.test(value);   
	    }, $.validator.format("只能输入中文、英文"));
		
		
		console.log(validateFormH.form()+"+++++++");
		console.log($("#formSubmitAdd").valid()+"----------");
		
		
		
	  if($("#formSubmitAdd").valid()){
		  $.ajax({
				type : "POST",
				url : "mobile/swust/car/insertSysCar",
				data : {
					id:$("#oid").val(),
					money:$("#money").val(),
					carId:$("#smsCarId").val(),
					userName:$("#smsOwner").val(),
					phone:$("#smsPhone").val(),
					disable:'1',
					carType:$("#smsCarType").val(),
					effectiveTime:$("#smsServiceTime").val()
				},
				success : function(data) {
					$("#msg-reminder").modal("hide");
					$("#managerModal").modal("hide");
					$("#dissubmitModal").hide();
					$("#diswarningModal").html("消息提示！");
					$("#reminderModal").html(data.msg);
					$("#success-box").modal("show");
					setTimeout(function(){
						$("#success-box").modal("hide");
					},2000);
					searchMsg(1);
				}
			});
		  return true;
	  }
	  return false;
	}


	function submitReset() {
		$.ajax({
			type : "POST",
			url : "mobile/swust/car/insertSysCar",
			data : {
				carId:$("#carID").val(),
				userName:$("#owner").val(),
				phone:$("#phone").val(),
				carType:$("#carType").val(),
				effectiveTime:$("#serviceTime").val(),
				money:$("#money").val()
			},
			success : function(data) {
				$("#reset").modal("hide");
				$("#managerModal").modal("hide");
				$("#dissubmitModal").hide();
				$("#diswarningModal").html("消息提示！");
				$("#reminderModal").html(data.msg);
				$(".warning-box").modal("show");
				setTimeout(function(){
					$(".warning-box").modal("hide");
				},2000);
			}
		});
	}
	
	
	function searchMsg(pageNo) {
		$.ajax({
			type : "POST",
			url : "mobile/swust/car/list",
			data : {
				deFlag:"0",
				name:$("#named").val(),
				pageNo:$("#pageNo").val(),
				pageSize:10,
				},
			success : function(date) {
				$("#carList").empty();
				$("#carList").html(date);
			}
		})
	}
	
	function page(n,s){
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		searchMsg();
		return false;
	}
	function clearList(){
		$("#oid").val("");
		$("#smsCarId").val("");
		$("#smsOwner").val("");
		$("#smsPhone").val("");
		$("#smsServiceTime").val("");
		$("#money").val("");
	}
	
	/*导出*/
	function exportSysOrder() {
		var name = $("#name").val();
		location.href="mobile/swust/car/export?pageNo="+$("#pageNo").val()+"&name="+$("#named").val();
	}
	
	
	function searchSysOrder(state) {
		type = state;
		$("#stateKey").val(state);
		var name = $("#name").val();
		var pageNo = $("#pageNo").val();
		var pageSize = $("#pageSize").val();
		$.ajax({
			type : "POST",
			url : "mobile/swust/appointment/list",
			data : {
				state:state,
				orderName:name,
				pageNo:pageNo,
				pageSize:pageSize,
				color:$('#orderSelect option:selected').val()
				},
			success : function(date) {
				$("#orderList").empty();
				$("#orderList").html(date);
			}
		});
	}
	

	/*事由*/
	function reason(id) {
		var thing = id;
		$.ajax({
			type : "post",
			url : "mobile/swust/appointment/findRemark",
			data : {
				id : thing
			},
			success : function(data) {
				var arr = new Array();
				arr = data.msg.split(",");
				$("#orderReason").html(arr[0])
				$("#remarks").html(arr[1])
				$("#check-view").modal("show").html();
			}
		});
	};
	
	
	function sendSms() {
		$.ajax({
			type : "post",
			url : "mobile/swust/appointment/sendSms",
			success : function(data) {
			}
		});	
	}
	$(function(){
		$('button:contains("新增")').click(function(){
			$("#smsCarId-error").html("");
			$("#smsOwner-error").html("");
			$("#smsPhone-error").html("");
			$("#smsCarId").attr("class","form-control required");
		      $("#smsOwner").attr("class","form-control required");
		      $("#smsPhone").attr("class","form-control required");
			$("#smsCarId").removeData("previousValue"); 
			$('#msg-reminder').modal('show');
		})
		
		
		
	})
</script>

