<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head lang="zh-cn">
<meta charset="UTF-8">
<!--2 viewport-->
<meta name="viewport"
	content="width=device-width,initial-scale=1,user-scalable=no">
<!--3、x-ua-compatible-->
<meta http-equiv="x-ua-compatible" content="IE=edge">
<title>新预约</title>
<!--4、引入两个兼容文件-->
<!--[if lt IE 9]>
    <script src="js/html5shiv.min.js"></script>
    <script src="js/respond.min.js"></script>
    <![endif]-->
<!--6、引入 bootstrap.css-->
<link rel="stylesheet" href="${ctxStatic }/swust/css/bootstrap.min.css">
<link rel="stylesheet" href="${ctxStatic }/swust/css/newBooking.min.css">
<link rel="stylesheet" href="${ctxStatic }/swust/css/header.min.css">
<link rel="shortcut icon" href="${ctxStatic}/swust/images/logo.png"
	type="image/x-icon">

<link href="${ctxStatic }/swust/css/bootstrap-datetimepicker.min.css"
	rel="stylesheet" />
<script src="${ctxStatic }/swust/js/jquery.min.js"></script>
<style type="text/css">
input.error {
	border: 1px solid red;
}

label.error {
	color: red;
}

.red {
	font-size: 16px;
	color: #CD2626;
}
</style>

<!-- <script src="http://www.my97.net/dp/My97DatePicker/WdatePicker.js"></script> -->
</head>
<body>
	<%@include file="/webpage/include/swustHeader.jsp"%>
	<div class="container-fluid change-padding">
		<p class="my-booking">新增预约</p>
	</div>
	<form id="form" method="post">
		<div class="container">
			<div class="row some-padding">
				<div class="col-xs-12 col-sm-5">
					<div class="form-group">
						<label for="orderName"><span class="red">*&nbsp;</span>来访人：</label>
						<input type="text" class="form-control chinaEn required"
							id="orderName" name="orderName">
					</div>
					<div class="form-group">
						<label for="company">来访人单位：</label> <input type="text"
							class="form-control" id="company.name" name="company">
					</div>
					<div class="form-group">
						<label for="car-number"><span class="red">*&nbsp;</span>车牌号码：</label>
						<input type="text" placeholder="例：川A12345"
							class="form-control required licenseNum" id="carNumber"
							name="carNumber">
					</div>
					<c:choose>
						<c:when test="${requestScope.redio.id eq '1' }">
						<div class="form-group">
					                    	<label for="car-number"><span class="red">*&nbsp;</span>起始时间：  请于<fmt:formatDate value="${requestScope.beginTimeShow }" pattern="HH:mm:ss"/>之后</label>
						                    <div id="form_start_time" class="input-group date form_datetime required" data-date-format="yyyy-mm-dd hh:ii:ss" data-link-format="yyyy-mm-dd hh:ii:ss">
												<input class="form-control parameter required" value="${requestScope.beginTime }" type="text" id="beginTime" name="beginTime" readonly >
												<span class="input-group-addon">
													<span class="glyphicon glyphicon-calendar"></span>
												</span>
											</div>
											<label id = "beginTime-error" class="error" for="beginTime"></label>
					                    </div>
						</c:when>
						<c:otherwise>
						<div class="form-group">
						<label for="car-number"><span class="red">*&nbsp;</span>起始日期</label>
						<div id="form_start_Date"
							class="input-group date form_datetime required"
							data-date-format="yyyy-mm-dd" data-link-format="yyyy-mm-dd">
							<input class="form-control parameter required"
								value="${requestScope.beginDate }" type="text" id="beginDate"
								name="beginTime" readonly> <span
								class="input-group-addon"> <span
								class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
						<label id="beginDate-error" class="error" for="beginTime"></label>
					</div>
						</c:otherwise>
					</c:choose>
					<div class="form-group">
						<label for="Reservations"><span class="red">*&nbsp;</span>预约事由：</label>
						<select name="orderReason" class="form-control">
								<option value="因公来访">1.因公来访</option>
								<option value="学术工作交流">2.学术工作交流</option>
								<option value="校园招聘">3.校园招聘</option>
								<option value="校友返校">4.校友返校</option>
								<option value="其它">5.其它</option>
						</select>
					</div>
				</div>
				<div class="col-xs-12 col-sm-5 col-sm-offset-2">
					<div class="form-group">
						<label for="phone"><span class="red">*&nbsp;</span>手机号码：</label> <input
							type="text" class="form-control isMobile required"
							id="orderPhone" name="orderPhone">
					</div>
					<div class="form-group">
						<label for="school"><span class="red">*&nbsp;</span>校内对接单位：</label>
						<select name="office.id" class="form-control">
							<option value="">请选择</option>
							<c:forEach items="${officelist }" var="list">
								<option value="${list.id }">${list.name }</option>
							</c:forEach>
						</select>
					</div>
					<div class="form-group">
						<label for="car-category"><span class="red">*&nbsp;</span>车辆类型：</label>
						<!--  <input type="text" class="form-control" id="car-category" name = "carType"> -->
						<select name="carType" class="form-control" id="car-category">
							<option value="">请选择</option>
							<c:forEach items="${cartype }" var="list">
								<option value="${list.carTypeId }">${list.remarks }</option>
							</c:forEach>
						</select>
					</div>
					<c:choose>
						<c:when test="${requestScope.redio.id eq '1' }">
						<div class="form-group">
					                    	<label for="car-number"><span class="red">*&nbsp;</span>截止时间： 请于<fmt:formatDate value="${requestScope.endTimeShow }" pattern="HH:mm:ss"/>之前</td>    </label>
						                    <div id="form_end_time" class="input-group date form_datetime required" data-date-format="yyyy-mm-dd hh:ii:ss" data-link-format="yyyy-mm-dd hh:ii:ss">
												<input class="form-control parameter" value="${requestScope.endTime }" id="endTime" name="endTime" type="text"  readonly >
												<span class="input-group-addon">
													<span class="glyphicon glyphicon-calendar"></span>
												</span>
											</div>
											<label id = "endTime-error" class="error" for="endTime"></label>
					                    </div>
						</c:when>
						<c:otherwise>
							<div class="form-group">
						<label for="car-number"><span class="red">*&nbsp;</span>截止日期： 预约时长请于${requestScope.redio.sum }天之内
							</td> </label>
						<div id="form_end_Date"
							class="input-group date form_datetime required"
							data-date-format="yyyy-mm-dd" data-link-format="yyyy-mm-dd">
							<input class="form-control parameter"
								value="${requestScope.endDate }" id="endDate" name="endTime"
								type="text" readonly> <span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
						<input type="hidden" name="disable" value="2">
						<label id="endDate-error" class="error" for="endTime"></label>
					</div>
						</c:otherwise>
					</c:choose>
					<div class="form-group">
						<label for="extra-notice">备注：</label>
						<textarea class="form-control" id="extra-notice" name="remarks"
							maxlength="140"></textarea>
					</div>
				</div>

				<div class="col-xs-12 some-margin">
					<div class="form-group text-right">
						<button class="confirm" type="button" onclick="formsubmit()">提交</button>
					</div>
				</div>

			</div>
		</div>
	</form>
	<script src="${ctxStatic }/swust/js/bootstrap.min.js"></script>
	<script src="${ctxStatic }/swust/js/newBooking.js"></script>
	<script
		src="${ctxStatic}/jquery-validation/1.14.0/jquery.validate.min.js"
		type="text/javascript"></script>
	<script
		src="${ctxStatic}/jquery-validation/1.14.0/localization/messages_zh.min.js"
		type="text/javascript"></script>
	<!-- 引入自定义的jQuery validate的扩展校验 -->
	<script src="${ctxStatic}/common/validateExtend.js"
		type="text/javascript"></script>
	<script type="text/javascript"
		src="${ctxStatic }/swust/js/bootstrap-datetimepicker.min.js"
		charset="UTF-8"></script>
	<script type="text/javascript"
		src="${ctxStatic }/swust/js/bootstrap-datetimepicker.zh-CN.js"
		charset="UTF-8"></script>
	<script type="text/javascript">
var index = true;
var order = false;
function timeValidate(where,time) {
	$.ajax({
		cache: true,
		type:"POST",
		url:"mobile/swust/order/timeValidate",
		 data:{
			 time:time,
			 startTime:$("#beginTime").val(),
			finishTime:$("#endTime").val()
			 },
         dataType:'json',
         error: function(request) {
             alert(request);
         },
         success: function(data) {
        	 if(!data.success){
        		index = !data.success;
        		 $("#"+where).html(data.msg);
        	 }else{
        		index = !data.success;
        		 $("#"+where).html(data.msg);
        		 $("#"+where).attr("style","color: red;");
        	 }
         }
	});
}


function DateValidate(where,time) {
	$.ajax({
		cache: true,
		type:"POST",
		url:"mobile/swust/order/dateValidate",
		 data:{
			 time:time,
			 beginDate:'${requestScope.beginDate}',
			 endDate:'${requestScope.endDate}'
			 },
         dataType:'json',
         error: function(request) {
             alert(request);
         },
         success: function(data) {
        	 if(!data.success){
        		index = !data.success;
        		 $("#"+where).html(data.msg);
        	 }else{
        		index = !data.success;
        		 $("#"+where).html(data.msg);
        		 $("#"+where).attr("style","color: red;");
        	 }
         }
	});
}

function formsubmit() {
	$("#form").validate({
		rules:{
			orderName:{
				rangelength:[2,10]
			},
			"company.name":"required",
			"office.id":"required",
			carType:"required"
		},
		messages:{
			orderName:{
				rangelength:"请输入长度在{0}到{1}之间的汉字或者字母"
			},
			"company.name":"请输入单位名称",
			orderPhone:{
				required:"请输入联系人电话号码"
			},
			"office.id":"请选择接待部门",
			carType:"请选择车辆类型"

		}
});
	order=$("#form").valid();
	if(index&&order){
	$.ajax({
		cache: true,
		type:"POST",
		url:"/mht_oeg/mobile/swust/order/addOrder",
		 data:$('#form').serialize(),// 你的formid
         async: false,
         dataType:'json',
         error: function(request) {
             alert("Connection error");
         },
         success: function(data) {
            location.href = "${ctx}";
         }
	});
	}
}
Date.prototype.Format = function (fmt) {  
    var o = {  
        "M+": this.getMonth()+1, //月份   
        "d+": this.getDate(), //日   
        "h+": this.getHours(), //小时   
        "m+": this.getMinutes(), //分   
        "s+": this.getSeconds(), //秒   
        "q+": Math.floor((this.getMonth()+3)/3),  
        "S": this.getMilliseconds() 
    };  
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));  
    for (var k in o)  
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));  
    return fmt;  
}  
  
var date= new Date().Format("yyyy-MM-dd");//Format("输入你想要的时间格式:yyyy-MM-dd,yyyyMMdd")  

$("#form_start_Date").datetimepicker({
    language : 'zh-CN',
	weekStart : 1,
	todayBtn : 1,
	autoclose : 1,
	startDate: '${requestScope.beginDate }',
	todayHighlight : 1,
	startView : 2,
	minuteStep : 5,
	minView : 2,
	forceParse : 0
}).on('change', function (ev) {
	var index = "${requestScope.redio.sum }";
    index = Number(index);
    var etime = $("#beginDate").val();
    var date = new Date($("#beginDate").val());//获取当前时间  
    date.setDate(date.getDate()+index);//设置天数xx天  
    $("#form_end_Date").datetimepicker('setStartDate', etime);
    $("#form_end_Date").datetimepicker('setEndDate', date.Format("yyyy-MM-dd"));
    $("#endDate").val(etime);
});
$("#form_end_Date").datetimepicker({
    language : 'zh-CN',
	weekStart : 1,
	todayBtn : 1,
	autoclose : 1,
	todayHighlight : 1,
	startDate: '${requestScope.beginDate }',
	endDate: '${requestScope.endDate }',
	startView : 2,
	minuteStep : 5,
	minView : 2,
	forceParse : 0
}).on('change', function (ev) {
    var etime = $("#beginDate").val();
    var endTime = $("#endDate").val();
    $("#form_end_Date").datetimepicker('setStartDate', etime);
    if($("#beginDate").val()> $("#endDate").val()){
    	$("#endDate").val($("#beginDate").val());
    }
});


$("#form_start_time").datetimepicker({
    language : 'zh-CN',
	weekStart : 1,
	todayBtn : 1,
	autoclose : 1,
	startDate: new Date(),
	todayHighlight : 1,
	startView : 2,
	minuteStep : 1,
	minView : 0,
	forceParse : 0
}).on('change', function (ev) {
    var etime = $("#beginTime").val();
    $("#form_end_time").datetimepicker('setStartDate', etime);
    $("#endTime").val(etime);
    timeValidate("beginTime-error",etime);
});
$("#form_end_time").datetimepicker({
    language : 'zh-CN',
	weekStart : 1,
	todayBtn : 1,
	autoclose : 1,
	todayHighlight : 1,
	startDate: new Date(),
	startView : 2,
	minuteStep : 1,
	minView : 0,
	forceParse : 0
}).on('change', function (ev) {
    var etime = $("#beginTime").val();
    var endTime = $("#endTime").val();
    $("#form_end_time").datetimepicker('setStartDate', etime);
    if($("#beginTime").val()> $("#endTime").val()){
    	$("#endTime").val($("#beginTime").val());
    }
    timeValidate("endTime-error",endTime);
});

 
		function init(){
			$("#authentication").html("");
			$("#new-pwd").val("");
			$("#old-pwd").val("");
			$("#inputPassword3").val("")
			$("#modal-pwd").modal("show");
			}
    </script>
</body>
</html>