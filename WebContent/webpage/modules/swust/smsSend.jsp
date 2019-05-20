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
#smschoose-excel{
			margin:10px 0;
			visibility:hidden;
		}
		
		#smschoose-label span{
			color:#fff;
			font-size:20px;
			position:relative;
			top:5px;
		}
		#smschoose-label{
			width: 80px;
    		height: 30px;
    		color: #fff;
    		background: #f37022;
    		border-radius: 15px;
    		text-align:center !important;
		}

@media (max-width:768px){
	.state-menu{
		height:auto;
	}
	#unique{
		margin-left:40px;
	}
	#importButton{
	display: none;
	}
	#exportButton{
	display: none;
	}
}
</style>
<div class="col-xs-12">
	<p class="header">
	<span style="width:200px;display:inlin-block;height:35px;line-height:35px;position:relative;" id="unique">
		<input type="search" style="height:35px;line-height:35px;border-radius:33px;width:200px;" placeholder="请输入会员卡号或姓名" id="named"> <span></span>
		<button style="position:absolute;right:13px;top:-3px;" onclick="searchMsg()">
			<img src="${ctxStatic }/swust/images/graySearch.png"/>
		</button>
	</span>
	</p>
	<ul class="state-menu">
		<li id="batch">
			<button onclick="batchDeleteSms()"> 批量删除</button>
			<button onclick="clearList()"> 新增</button>
			<button onclick="exportSysOrder()" id="exportButton">导出</button>
			<button style="width: 110px" onclick="exportCostOrChargeDetailList()">导出历史记录</button>
<!-- 			<button style="margin-top: 8px" onclick="importSysOrder()" id="importButton">导入</button> -->
			<input type="hidden" id="cacheId" value="" />
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
								<label for="smsCarType" class="col-sm-5 control-label">会员等级:</label>
								<div class="col-sm-7">
									<select name="carType" id="smsCarType" class="form-control">
										<option value="1">黄金会员</option>
										<option value="2">铂金会员</option>
										<option value="3">钻石会员</option>
										<option value="4">爸爸豁茶</option>
									</select>
<!-- 									<input type="text" class="form-control " name="carType" id="carType"> -->
								</div>
							</div>
							<div class="form-group">
								<label for="smsServiceTime" class="col-sm-5 control-label">剩余次数(次):</label>
								<div class="col-sm-7">
									<input  class="form-control " name="serviceTime" id="smsServiceTime" maxlength="15" placeholder="请输入天数">
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
									<input type="text" class="form-control " maxlength="15" name="money" id="money">
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
                <h3 class="modal-title text-center" id="smswarningModal">成功</h3>
            </div>
            <div class="modal-body">
                <h5 class="text-center text-danger" id="reminder"></h5>
            </div>
            <div class="modal-footer text-center">
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


<!--导入-->
<div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog"
	id="smsimport">
    <div class="modal-dialog modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h3 class="modal-title text-center">
                	请选择导入的EXCEL
                </h3>
            </div>
            <div class="modal-body text-center">
               <label for="smschoose-excel" id="smschoose-label">
                	<span class="glyphicon glyphicon-plus"></span>
                </label>
                
                <form class="form-horizontal" id="smsjvForm" action="${ctx}/swust/car/import" method="post" enctype="multipart/form-data" >
                	<input type="file" name="file" id="smschoose-excel" accept="application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" />	<span id="smsfile-name"></span>
            	</form>
            	
            </div>
            <div class="modal-footer text-center">
            	<input type="hidden" id="smssubmitOnly" />
                <button type="button" class="btn btn-md confirm" data-dismiss="modal" id="smsimport-result">确认</button>
                <button  class="btn btn-md confirm" onclick="exportSMS()">下载模板</button>
            </div>
        </div>
    </div>
</div>

<!--新增  -->
<div class="modal fade" tabindex="-1" id="history-list">
	<div class="modal-dialog modal-md" style="width: 332px">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h3 class="modal-title text-center" id="textHistory">导出会员卡历史详情</h3>
			</div>
			<div class="modal-body">
				<div class="row">
					<form class="form-horizontal" id="formSubmitHistory" action="" novalidate="novalidate">
						<div class="col-xs-12 col-sm-12">
							 <input id="startHistoryDate" type='text' style="border: 1px solid #3eaab7; height: 30px !important;width: 100%;margin: 10px"  placeholder="选择时间">
							 <input type="hidden" id = "beginHistoryTime"/>
							<input type="hidden" id = "endHistoryTime"/>
						</div>
					</form>
				</div>
			</div>
			<div class="modal-footer text-center">
			<button type="button" class="btn btn-md confirm" onclick="HistorySubmit()">导出</button>
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
/**
 * 导出模板
 */
function exportSMS() {
	location.href="${ctx}/swust/car/exportModel";
}

function HistorySubmit() {
	$("#history-list").modal("hide");
	location.href="${ctx}/swust/car/exportCostOrChargeHistory?beginTime="+$("#beginHistoryTime").val()+"&endTime="+$("#endHistoryTime").val();
}
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
					number:true
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
					required:"请输入会员卡号",
					rangelength:"请输入正确的会员卡号"
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
				}

			}
		});
		jQuery.validator.addMethod("checkName", function(value, element) {
	        var char = /^[a-zA-Z\u4e00-\u9fa5]+$/;
	        return this.optional(element) || char.test(value);   
	    }, $.validator.format("只能输入中文、英文"));
		
		
		
		
		
	  if($("#formSubmitAdd").valid()){
		  $.ajax({
				type : "POST",
				url : "${ctx}/swust/car/insertSysCar",
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
					$("#smsdissubmitModal").hide();
					$("#smsdiswarningModal").html("消息提示！");
					$("#smsreminderModal").html(data.msg);
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
			url : "${ctx}/swust/car/insertSysCar",
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
				$("#smsdissubmitModal").hide();
				$("#smsdiswarningModal").html("消息提示！");
				$("#smsreminderModal").html(data.msg);
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
			url : "${ctx}/swust/car/list",
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
		});
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
		location.href="${ctx}/swust/car/export?pageNo="+$("#pageNo").val()+"&name="+$("#named").val();
	}
	/*导入*/
	function importSysOrder() {
		$("#smsfile-name").html("");
		$("#smschoose-excel").val("");
		$('#smsimport').modal('show');
	}
	
	$('#smsimport-result').click(function(){
		if($('#smsfile-name')[0].innerHTML===""){
			return true;
		}
		else{
			var formData = new FormData($("#smsjvForm")[0]);
            $.ajax({  
            	          url: "${ctx}/swust/car/import" ,  
            	          type: 'POST',  
            	          data: formData,  
            	          async: false,  
            	          cache: false,  
            	          contentType: false,  
            	          processData: false,  
            	          success: function (data) { 
            	        	  $("#smsgetResult").html(data.msg);
            	        	  searchMsg();
            	          },  
                 });
			$("#smsimport-info").modal('show');
		}
	})
	
	$('#smschoose-excel').change(function(){
			var fileName=$(this).val();
			var start=fileName.lastIndexOf('\\');
			var result=fileName.slice(start+1);
			if(result.indexOf(".xlsx")>=0){
				$('#smsfile-name').html(result);
			}else{
			alert("请上传正确的文件格式（.xlsx文件）");
			}
		})
	
	
	function searchSysOrder(state) {
		type = state;
		$("#stateKey").val(state);
		var name = $("#name").val();
		var pageNo = $("#pageNo").val();
		var pageSize = $("#pageSize").val();
		$.ajax({
			type : "POST",
			url : "${ctx}/swust/appointment/list",
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
			url : "${ctx}/swust/appointment/findRemark",
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
			url : "${ctx}/swust/appointment/sendSms",
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
		});
		$('button:contains("历史")').click(function(){
			$("#startHistoryDate").val("");
			$('#history-list').modal('show');
		});
		
		
	});
</script>

