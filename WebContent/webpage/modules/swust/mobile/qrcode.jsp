<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<style type="text/css">
input.error {
	border: 1px solid red;
}

label.error {
	display: show;
	color: red;
}

.red {
	font-size: 12px;
	color: #CD2626;
}

.right-hand {
	margin-right: 10px;
}

#choose-excel {
	margin: 10px 0;
	visibility: hidden;
}

#choose-label {
	width: 80px;
	height: 30px;
	color: #fff;
	background: #f37022;
	border-radius: 15px;
	text-align: center !important;
}

#choose-label span {
	color: #fff;
	font-size: 20px;
	position: relative;
	top: 5px;
}

.modal-sm2 {
	width: 260px !important;
}

#import-info .modal-body {
	max-height: 500px;
	overflow-x: hidden;
	overflow-y: auto;
}

@media ( max-width :768px) {
	.modal-header-code {
		margin-top: 60px;
	}
	#delAllCode{
		margin-top:10px;
	}
	.table-responsive>.table>tbody>tr>td{
				    white-space: normal;
			}
}
</style>
<div class="col-xs-12">
	<p class="header">
		<span
			style="width: 200px; height: 35px; line-height: 35px; position: relative; margin-right: 80px;">
			<input type="search" placeholder="请输入搜索内容" id="nameSearch"
			style="height: 35px; line-height: 35px; border-radius: 33px; width: 200px;">
			<button onclick="qrCodeSearch()"
				style="position: absolute; right: 13px; top: -3px;">
				<img src="${ctxStatic }/swust/images/graySearch.png" />
			</button>
		</span>
		<button class="right-hand" onclick="deleteQrCodes()" id="delAllCode" >批量删除</button>
		<button class="right-hand" id="newQrcode">新增</button>
	</p>
	<div class="table-responsive">
		<table class="table table-striped">
			<thead>
				<tr>
					<th><input type="checkbox" class="checkAll1"></th>
					<th>会议主题</th>
					<th>主办单位</th>
					<th>时间段</th>
					<th>二维码名称</th>
					<th>二维码图片</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody id="qrList">
				<c:forEach items="${page.list }" var="list">
				<tr>
					<td id="${list.id}"><span style="color:red;"><input type="checkbox" name="checkss"
					value="${list.id}" class="i-checks"></span></td>
					<td>${list.orderReason }</td>
					<td>${list.office.name }</td>
					<td><fmt:formatDate value="${list.beginTime }" pattern="MM-dd HH:mm"/>~<fmt:formatDate value="${list.endTime }" pattern="MM-dd HH:mm"/></td>
					<td>${list.qrCodeImage }</td>
					<td>
					<input type="hidden" value="${list.qrCodeAddress }">
					<span class='text-success showQr' style="cursor: pointer;">详情</span>
					</td>
					<td>
						<input type="hidden" value="${list.id }">
						<button class="refuse" onclick="deleteQrCode('${list.id }')">删除</button>
					</td>
				</tr>
				</c:forEach>
				<tr>
				<td colspan="13">
					<div class="buttons text-left">
						${page }
			         </div>
				</td>
				</tr>
				
			</tbody>
		</table>
		<input type="hidden" id="pwdblank" value="" />
	</div>
</div>
<!--新增模态款-->
<div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog"
	id="qrcodeModal">
	<div class="modal-dialog modal-md" role="document">
		<div class="modal-content">
			<div class="modal-header modal-header-code">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h3 class="modal-title text-center" id="changetitle">新增会议预约</h3>
			</div>
			<div class="modal-body">
				<div class="row">
					<form class="form-horizontal" id="formsubmit">
						<input type="hidden" name="id" id="oid" />
						<div class="col-xs-12 col-sm-6">
							<div class="form-group">
								<label for="company" class="col-sm-5 control-label"><span
									style="color: red">* </span>会议主题:</label>
								<div class="col-sm-7">
									<input type="text" class="form-control required " name="orderReason"
										id="company"> <input type="hidden" id="oldcompany">
								</div>
							</div>
							<div class="form-group">
								<label for="car-number" class="col-sm-5 control-label"><span
									class="red">*&nbsp;</span>起始日期:</label>
								<div id="form_start_Date"
									class="input-group date form_datetime required col-sm-7"
									data-date-format="yyyy-mm-dd" data-link-format="yyyy-mm-dd"
									style="padding-left: 15px; padding-right: 15px;">
									<input class="form-control parameter required"
										value="${requestScope.beginDate }" type="text" id="beginDate"
										name="beginTime" readonly
										style="background-color: #fff; border-radius: 4px; width: 123%">
									<span class="input-group-addon"
										style="border: 1px solid transparent; background-color: #fff;">
										<span class="glyphicon glyphicon-calendar"
										style="display: none; border: 1px solid transparent;"></span>
									</span>
								</div>
								<label id="beginDate-error" class="error" for="beginTime"></label>
							</div>
							<div class="form-group">
								<label for="qrCodeImg"
									class="col-sm-5 control-label input-width"><span
									style="color: red">* </span>二维码名称:</label>
								<div class="col-sm-7">
									<input type="text" class="form-control required" value=""
										name="qrCodeImage" id="qrCodeImage"> <input
										type="hidden" id="oldcompanyName">
								</div>
							</div>
<!-- 							<div class="form-group text-center"> -->
<!-- 								<img alt="二维码" src="" -->
<!-- 									style="max-width: 120px; max-height: 120px;"> -->
<!-- 							</div> -->
						</div>
						<div class="col-xs-12 col-sm-6">
							<div class="form-group">
								<label for="orderTime"
									class="col-sm-5 control-label input-width"><span
									style="color: red">* </span>主办单位:</label>
								<div class="col-sm-7">
									<!-- 									<input type="text" class="form-control required" value="" -->
									<!-- 										name="sysOrderlist.office.id" id="orderTime"> <input -->
									<!-- 										type="hidden" id="oldcompanyName"> -->
									<select name="office.id" class="form-control">
										<option value="">请选择</option>
										<c:forEach items="${requestScope.officelist }" var="list">
											<option value="${list.id }">${list.name }</option>
										</c:forEach>
									</select>
								</div>
							</div>
							<div class="form-group">
								<label for="car-number" class="col-sm-5 control-label"><span
									class="red">*&nbsp;</span>截止日期：
									</td> </label>
								<div id="form_end_Date"
									class="input-group date form_datetime required  col-sm-7"
									data-date-format="yyyy-mm-dd" data-link-format="yyyy-mm-dd"
									style="padding-left: 15px; padding-right: 15px;">
									<input class="form-control parameter"
										value="${requestScope.endDate }" id="endDate" name="endTime"
										type="text" readonly
										style="background-color: #fff; border-radius: 4px; width: 136%">
									<span class="input-group-addon"> <span
										class="glyphicon glyphicon-calendar"></span>
									</span>
								</div>
								<input type="hidden" name="disable" value="2"> <label
									id="endDate-error" class="error" for="endTime"></label>
							</div>
							<div class="form-group">
								<label for="phone" class="col-sm-5 control-label">选择路径:</label>
								<div class="col-sm-7">
									<input type="text" class="form-control "
										name="qrCodeAddress" id="qRCodeAddress">
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
			<div class="modal-footer text-center">
				<button type="button" class="btn btn-md confirm" id="dosubmit">生成二维码</button>
				<button type="button" class="btn btn-md confirm"
					data-dismiss="modal" id="finishChange">取消</button>
			</div>
		</div>
	</div>
</div>
<!--二维码框-->
<div class="modal fade bs-example-modal-lg " tabindex="-1"
	role="dialog" id="imageList">
	<div class="modal-dialog modal-sm" role="document">
		<div class="modal-content">
			<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">×</span>
				</button>
				<h3 class="modal-title text-center" id="diswarningModal">二维码图片</h3>
			</div>
			<div class="modal-body text-center">
				<img alt="二维码图片" id="qrImage" 
					style="margin: 20px auto; max-width: 200px; max-height: 200px;">
			</div>
			<div class="modal-footer text-center">
				<button type="button" class="btn btn-md confirm" data-dismiss="modal" onclick="qrCodeSearch();">关闭</button>
			</div>
		</div>
	</div>
</div>

<!--导入-->
<div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog"
	id="import">
	<div class="modal-dialog modal-sm modal-sm2" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h3 class="modal-title text-center">请选择导入的EXCEL</h3>
			</div>
			<div class="modal-body text-center">
				<label for="choose-excel" id="choose-label"> <span
					class="glyphicon glyphicon-plus"></span>
				</label>

				<form class="form-horizontal" id="jvForm"
					action="mobile/swust/manager/import" method="post"
					enctype="multipart/form-data">
					<input type="file" name="file" id="choose-excel"
						accept="application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" />
					<span id="file-name"></span>
				</form>

			</div>
			<div class="modal-footer text-center">
				<input type="hidden" id="submitOnly" />
				<button type="button" class="btn btn-md confirm"
					data-dismiss="modal" id="import-result">确认</button>
			</div>
		</div>
	</div>
</div>
<div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog"
	id="import-info">
	<div class="modal-dialog modal-md">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h3 class="modal-title text-center">导入结果</h3>
			</div>
			<div class="modal-body text-center">
				<p class='text-danger' id="getResult"></p>
			</div>
			<div class="modal-footer text-center">
				<input type="hidden" id="submitOnly" />
				<button type="button" class="btn btn-md confirm"
					data-dismiss="modal" id="import-result">确认</button>
			</div>
		</div>
	</div>
</div>

<%-- 	<form class="form-horizontal" id="grfesf" method="post" action="mobile/swust/manager/import" enctype="multipart/form-data" > --%>
<!-- 		<input type="file" id="uploadFile" name="file"> -->
<!-- 		<button type="submit">提交</button> -->
<!-- 	</form> -->



<script src="${ctxStatic}/swust/js/backstagePlatform.js"></script>
<script
	src="${ctxStatic}/jquery-validation/1.14.0/jquery.validate.min.js"
	type="text/javascript"></script>
<script
	src="${ctxStatic}/jquery-validation/1.14.0/localization/messages_zh.min.js"
	type="text/javascript"></script>
<!-- 引入自定义的jQuery validate的扩展校验 -->
<script src="${ctxStatic}/common/validateExtend.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/jquery-validation/1.14.0/jquery.form.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/common/validateExtend.js"
	type="text/javascript"></script>
<script type="text/javascript"
	src="${ctxStatic }/swust/js/bootstrap-datetimepicker.min.js"
	charset="UTF-8"></script>
<script type="text/javascript"
	src="${ctxStatic }/swust/js/bootstrap-datetimepicker.zh-CN.js"
	charset="UTF-8"></script>
<script type="text/javascript">
// 	Date.prototype.Format = function(fmt) {
// 		var o = {
// 			"M+" : this.getMonth() + 1, //月份   
// 			"d+" : this.getDate(), //日   
// 			"h+" : this.getHours(), //小时   
// 			"m+" : this.getMinutes(), //分   
// 			"s+" : this.getSeconds(), //秒   
// 			"q+" : Math.floor((this.getMonth() + 3) / 3),
// 			"S" : this.getMilliseconds()
// 		};
// 		if (/(y+)/.test(fmt))
// 			fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "")
// 					.substr(4 - RegExp.$1.length));                                     
// 		for ( var k in o)
// 			if (new RegExp("(" + k + ")").test(fmt))
// 				fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
// 						: (("00" + o[k]).substr(("" + o[k]).length)));
// 		return fmt;
// 	}

// 	var date = new Date().Format("yyyy-MM-dd");//Format("输入你想要的时间格式:yyyy-MM-dd,yyyyMMdd")  

	$("#form_start_Date").datetimepicker({
		language : 'zh-CN',
		weekStart : 1,
		todayBtn : 1,
		autoclose : 1,
		startDate : new Date(),
		todayHighlight : 1,
		startView : 2,
		minuteStep : 5,
		minView : 2,
		forceParse : 0
	}).on('change', function(ev) {
		var index = "${requestScope.redio.sum }";
		index = Number(index);
		var etime = $("#beginDate").val();
		var date = new Date($("#beginDate").val());//获取当前时间  
		date.setDate(date.getDate() + index);//设置天数xx天  
		$("#form_end_Date").datetimepicker('setStartDate', etime);
		//     $("#form_end_Date").datetimepicker('setEndDate', date.Format("yyyy-MM-dd"));
		$("#endDate").val(etime);
	});
	$("#form_end_Date").datetimepicker({
		language : 'zh-CN',
		weekStart : 1,
		todayBtn : 1,
		autoclose : 1,
		todayHighlight : 1,
		startView : 2,
		minuteStep : 5,
		minView : 2,
		forceParse : 0
	}).on('change', function(ev) {
		var etime = $("#beginDate").val();
		var endTime = $("#endDate").val();
		$("#form_end_Date").datetimepicker('setStartDate', etime);
		if ($("#beginDate").val() > $("#endDate").val()) {
			$("#endDate").val($("#beginDate").val());
		}
	});

	$('.showQr').click(function() {
		$("#qrImage").attr("src",$(this).prev().val());
		$('#imageList').modal('show');
	})
	$(function() {
		$('#import-table').click(function() {
			$("#file-name").html("");
			$("#choose-excel").val("");
			$('#import').modal('show');
			// 			$("#file-name").html("");
		})
		$('#choose-excel').change(function() {
			var fileName = $(this).val();
			var start = fileName.lastIndexOf('\\');
			var result = fileName.slice(start + 1);
			$('#file-name').html(result);
		})
		$('#import-result').click(function() {
			if ($('#file-name')[0].innerHTML === "") {
				return true;
			} else {
				var formData = new FormData($("#jvForm")[0]);
				$.ajax({
					url : "mobile/swust/manager/import",
					type : 'POST',
					data : formData,
					async : false,
					cache : false,
					contentType : false,
					processData : false,
					success : function(data) {
						$("#getResult").html(data.msg);
					},
				});
				$("#import-info").modal('show');
			}
		})
		//初始化列表
		$("#formsubmit").validate({
			rules : {
				orderReson : {
					checkName : true,
					rangelength : [ 2, 15 ],
				},
				qrCodeImage : {
					checkName : true,
					rangelength : [ 2, 15 ],
				},
			},
			messages : {
				orderReason : {
					rangelength : "请输入长度在{0}到{1}之间的汉字或者字母",
				},
				qrCodeImg : {
					rangelength : "请输入长度在{0}到{1}之间的汉字或者字母",
				},

			},
			submitHandler : function(form) {
				//验证通过后的执行方法
				$(form).ajaxSubmit({
					type : "POST",
					dataType : "json",
					url : "mobile/swust/meeting/createQrCode",
					data : $("#formsubmit").serialize(),
					async : false,
					success : function(data) {
						$("#qrcodeModal").modal("hide");
						$('.modal-backdrop').hide();
						$("#imageList").modal("show");
						$("#qrImage").prop("src",data.body.qrCode); 
					}
				});
			}
		});
		jQuery.validator.addMethod("checkName", function(value, element) {
			var char = /^[a-zA-Z\u4e00-\u9fa5]+$/;
			return this.optional(element) || char.test(value);
		}, $.validator.format("单位名只能包含中文、英文"));

		$("#newQrcode").click(function() {
			$("#qrcodeModal").modal("show");
		});
		
		
	});


	/*单位查询*/
	function qrCodeSearch() {
		var name = $("#nameSearch").val();
		var pageNo = $("#pageNo").val();
		var pageSize = $("#pageSize").val();
		$.ajax({
			type : "POST",
			url : "mobile/swust/meeting/init",
			data : {
				orderName : name,
				pageNo : pageNo,
				pageSize : pageSize
			},
			success : function(data) {
				$("#qrcode").empty();
				$("#qrcode").html(data);
				$("#nameSearch").val(name);
			}
		});
		$(".modal-backdrop").hide();
	}

	/*分页信息*/
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		qrCodeSearch() ;
		return false;
	}


	function submitReset() {
		$.ajax({
			type : "POST",
			url : "mobile/swust/manager/resetpassword",
			data : {
				loginName : rsetpsw
			},
			success : function(data) {
				$("#reset").modal("hide");
				$("#managerModal").modal("hide");
				$("#dissubmitModal").hide();
				$("#diswarningModal").html("消息提示！");
				$("#reminderModal").html(data.msg);
				$(".warning-box").modal("show");
				setTimeout(function() {
					$(".warning-box").modal("hide");
				}, 2000);
			}
		});
	}

	var onlyId = "";
	var stateOnly = "";

	function disableSubmit() {
				$("#dissubmitModal").hide();
				$("#diswarningModal").html("消息提示！");
				$("#reminderModal").html(data.msg);
				$("#reminderWarning").modal("show");
				setTimeout(function() {
					$("#reminderWarning").modal("hide");
				}, 2000);
	}

	$("#dosubmit").click(function() {
		$("#formsubmit").submit();
	});
	function deleteQrCodes() {
		var selectIds = new Array(); 
		$("#qrList").find("input[type='checkbox']:checked").each(function(){ 
			var msg = $(this).val();
			selectIds.push(msg);
		})
		$.ajax({
			type : "POST",
			url : "mobile/swust/meeting/batchDelete",
			traditional: true,
			data : {
				ids : selectIds,
			},
		});
		qrCodeSearch();
	}
	
	
	function deleteQrCode (id){
		var importModule = new Array(); 
		importModule.push(id);
		$.ajax({
			type : "POST",
			url : "mobile/swust/meeting/batchDelete",
			traditional: true,
			data : {
				ids : importModule,
			},
		});
		qrCodeSearch();
	};
	function _close () {
		console.log($("#imageList"));
			$("#imageList").remove();
	}
</script>