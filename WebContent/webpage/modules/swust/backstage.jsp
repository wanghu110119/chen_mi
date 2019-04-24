
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="format-detection" content="telephone=no">
<div id="system" class="tab-pane">
	<style>
.back-right li span {
	display: none;
}

.back-right li:hover span {
	display: block;
}

.table-condensed thead tr:first-child {
	visibility: hidden;
}

.table-condensed {
	margin-top: -30px !important;
}

.table-condensed thead tr:first-child th {
	visibility: hidden !important;
}

.days {
	width: 100%;
	margin: 0 0 20px 14%;
	height: 34px;
}

.days input {
	height: 34px;
	border: 1px solid #ccc;
	text-align: center;
}

.input-group {
	width: 100% !important;
	margin-left: 0;
	color: #a2aebd;
}

#charge input {
	text-align: right;
	width: 3em;
	border-bottom: 2px solid #515c6b;
}

.type-time {
	position: absolute;
	left: 10px;
	top: 6px;
}

.back-change {
	padding-left: 15px;
}

@media ( max-width :768px) {
	#charge input {
		text-align: center;
		border-radius: 0;
		background-color: transparent;
		padding: 2px;
		height: 30px;
		color: #000;
	}
	.back-right {
		margin: 10px 0;
	}
	#days {
		margin-left: 15.5% !important;
	}
	#forDays {
		padding-left: 40px !important;;
	}
	#charge tr td:first-child, #charge tr th:first-child {
		width: 150px !important;
	}
	#charge tr td:nth-child(2), #charge tr th:nth-child(2) {
		width: 540px;
	}
	#charge tr td:last-child, #charge tr th:last-child {
		width: 200px !important;
	}
}
</style>
	<div class="row">
		<div class="col-xs-12">
			<h5 class="back-change">背景更换</h5>
			<div class="row">
				<div class="col-xs-12 col-sm-7 back-left">
					<img src="${ctxStatic}/${photo.photoPath}" class="img-responsive"
						id="preview-skin">
				</div>
				<div class="col-xs-12 col-sm-5">
					<ul class="back-right">
						<c:forEach items="${requestScope.photolist }" var="list">
							<li style="position: relative;"><img
								src="${ctxStatic}/${list.photoPath}" class="img-responsive"
								onclick="changepicture('${list.id}')"
								style="width: 180; height: 101"> <span
								onclick="deletepicture('${list.id}')"
								style="cursor: pointer; position: absolute; top: 2px; right: 2px; width: 20px; height: 20px; border-radius: 50%; font-size: 20px; background: #ddd; color: #adadad; text-align: center; line-height: 20px;">
									&times; </span></li>
						</c:forEach>
						<li>
							<!-- <a href="javascript:void(0);" class="file"> -->
							<form id="jveForm" method="post" enctype="multipart/form-data">
								<a href="javascript:void(0);" class="file"> 选择背景 <input
									type="file" name="photo" id="choose-filee"
									onchange="uploadPic()">
								</a>
							</form>
						</li>
					</ul>
				</div>
			</div>
		</div>
		<script type="text/javascript">
			function uploadPic() {
				var formData = new FormData($("#jveForm")[0]);
				$.ajax({
					url : "${ctx}/swust/system/insertUserPhoto",
					type : 'POST',
					data : formData,
					async : false,
					cache : false,
					contentType : false,
					processData : false,
					success : function(data) {
						console.log(data);
						if (!data.success) {
							alert("请上传正确的图片格式:JPG,PNG,JPEG");
						}
					},
				});
				$.ajax({
					type : "POST",
					url : "${ctx}/swust/system/system",
					success : function(date) {
						$("#system").empty();
						$("#system").html(date);
					}
				})
			}
		</script>
		<div class="col-xs-12">
			<div class="row">
				<div class="col-xs-12 col-sm-5 less-width">
					<h5>预约时限</h5>
					<form>
						<div class="form-group col-sm-6">
							<label style="text-align: left !important;">起始时间：</label>
							<div class="input-group date form_datetime required">
								<c:choose>
									<c:when test="${requestScope.redio.id eq '1' }">
										<input type="radio" name="type-time"
											class="type-time first-choice-radio" checked="checked"
											onclick="changeRedio(1)" />
									</c:when>
									<c:otherwise>
										<input type="radio" name="type-time"
											class="type-time first-choice-radio" onclick="changeRedio(1)" />
									</c:otherwise>
								</c:choose>
								<input id="beginTime" type='text'
									class="form-control parameter required md first-choice"
									readonly value="${requestScope.beginTime }">
							</div>
						</div>
						<div class="form-group col-sm-6">
							<label style="text-align: left !important">截止时间：</label>
							<div id="form_end_time"
								class="input-group date form_datetime required">
								<input class="form-control md first-choice"
									value="${requestScope.endTime }" id="endTime" name="endTime"
									type="text" readonly>
							</div>
						</div>
						<div class="form-group col-sm-6">
							<label style="text-align: left !important" id="forDays">预约天数（天）：</label>
							<div class="input-group date form_datetime required">
								<c:choose>
									<c:when test="${requestScope.redio.id eq '2' }">
										<input type="radio" name="type-time"
											class="type-time second-choice-radio" checked="checked"
											onclick="changeRedio(2)" />
									</c:when>
									<c:otherwise>
										<input type="radio" name="type-time"
											class="type-time second-choice-radio"
											onclick="changeRedio(2)" />
									</c:otherwise>
								</c:choose>
								<select class="form-control md second-choice" id="days"
									style="margin-left: 15.5%; width: 69.5%;"
									onchange="changeDate()"></select>
							</div>
						</div>


						<div class="form-group col-sm-6">
							<label style="text-align: left !important">管理员号码：</label>
							<div id="admin_phone"
								class="input-group date form_datetime required">
								<input class="form-control md "
									value="${requestScope.adminPhone}" id="adminPhone"
									name="adminPhone" type="text" onchange="changeMobile()"
									oninput="if(value.length>11)value=value.slice(0,11)"
									onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9-]+/,'');}).call(this)"
									onblur="this.v();">
							</div>
						</div>


						<div class="table-responsive" id="charge">
							<table class="table">
								<thead>
									<tr>
										<th>房间选择</th>
										<th>收费设置</th>
									</tr>
								</thead>
								<tbody>
									<!-- 小型汽车 -->
									<c:forEach items="${carList }" var="list" begin="0" end="0">
										<tr id="tr_${list.id }">

											<td>${list.remarks }<input type="hidden" name="id"
												value="${list.id }" /> <input type="hidden" name="remarks"
												value="${list.remarks }" />
											</td>
											<td id="wrap-input">起价 <input type="number" min="0"
												value="${list.defaultTime }" name="defaultTime"
												onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9-]+/,'');}).call(this)"
												onblur="this.v();" onchange="changeMoney('${list.id }')">元/3小时，3小时后每小时加收
												<input type="number" min="0" value="${list.addMoney }"
												name="addMoney" onchange="changeMoney('${list.id }')">元。一天24小时不超过
												<input type="number" min="0" value="${list.maxMoney }"
												onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9-]+/,'');}).call(this)"
												onblur="this.v();" name="maxMoney"
												onchange="changeMoney('${list.id }')">元。 <input
												type="button" style="color: red; border: 1px; width: 57px"
												onclick="SMSNumber('${list.carTypeId}','${list.remarks }')"
												value="短信提醒" />
											</td>
										</tr>
									</c:forEach>
									<!-- 大型汽车 -->
									<c:forEach items="${carList }" var="list" begin="1" end="3">
										<tr id="tr_${list.id }">
											<td>${list.remarks }<input type="hidden" name="id"
												value="${list.id }" /> <input type="hidden" name="remarks"
												value="${list.remarks }" />
											</td>
											<td><input type="number" min="0"
												value="${list.addMoney }"
												onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9-]+/,'');}).call(this)"
												onblur="this.v();" name="addMoney"
												onchange="changeMoney('${list.id }')">元/次，每8小时为1次，不足8小时按8小时计。一天24小时不超过
												<input type="number" min="0" value="${list.maxMoney }"
												onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9-]+/,'');}).call(this)"
												onblur="this.v();" name="maxMoney"
												onchange="changeMoney('${list.id }')">元。 <input
												type="button" style="color: red; border: 1px; width: 57px"
												onclick="SMSNumber('${list.carTypeId }','${list.remarks }')"
												value="短信提醒" /></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</form>
				</div>
				<div class="col-xs-12 col-sm-7">
					<h5>短信设置</h5>
					<div class="wrap">
						<p class="text-center">短信使用情况</p>
						<ul class="circle">
							<li><span class="circle">
									${requestScope.message.total } </span> <br> <span class="text">
									共计 </span></li>
							<li><span class="circle green">
									${requestScope.message.used } </span> <br> <span class="text">
									已发出 </span></li>
							<li><span class="circle orange">
									${requestScope.message.surplus } </span> <br> <span class="text">
									剩余 </span></li>
						</ul>


						<div class="content">
							<p>
								<span>短信内容</span> <span
									class="save glyphicon glyphicon-file pull-right"
									onclick="changeMessage()"></span> <span
									class="modify pull-right"> <img
									src="${ctxStatic}/swust/images/modifyMsg.png">
								</span>
							</p>
							<textarea disabled rows="4" id="messageInfo">${requestScope.message.artical }</textarea>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<div class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog"
	id="phone-view">
	<div class="modal-dialog modal-sm" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h3 class="modal-title text-center">编辑管理员电话</h3>
			</div>
			<div class="modal-body">
				<p class="text-center" id="phone"></p>
				<p class="text-center" id="phoneRemarks">
					<c:forEach var="list" items="${requestScope.phoneList }">
					<input class="form-control md  text-center" value="htfhtfhfthtf"
						name="adminPhone" type="text" style="width: 50%; margin: 6px auto"
						value="${list.mobile }"
						onchange="changeMobile()"
						oninput="if(value.length>11)value=value.slice(0,11)"
						onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9-]+/,'');}).call(this)"
						onblur="this.v();">
					</c:forEach>
				</p>
			</div>
			<div class="modal-footer text-center">
				<button type="button" class="btn btn-md confirm"
					onclick="addPhone()">添加</button>
			</div>
		</div>
	</div>
</div>



<script src="${ctxStatic }/swust/js/jquery.min.js"></script>
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


<script type="text/javascript">
	function SMSNumber(carType, remarks) {
		$("#phoneRemarks").empty();
		$.ajax({
			url : "${ctx}/swust/system/adminPhone",
			type : "post",
			data : {
				"carType" : carType,
			},
			success : function(data) {
				console.log(data);
				var index = data.body.phoneList;
				for ( var i = 0; i < index.length; i++) {
					$("#phoneRemarks")
					.append(
							"<input class='form-control md  text-center'  value='"+index[i].mobile+"' name='adminPhone' type='text' style='width: 50%;margin: 6px auto'onchange='changeMobile()'oninput='if(value.length>11)value=value.slice(0,11)'onkeyup='(this.v=function(){this.value=this.value.replace(/[^0-9-]+/,'');}).call(this)'onblur='this.v();'>")
				}
				$("#phone").html(remarks)
				$("#phone-view").modal("show").html();
			},
		})
	}

	function addPhone() {
		$("#phoneRemarks")
				.append(
						"<input class='form-control md  text-center'  name='adminPhone' type='text' style='width: 50%;margin: 6px auto'onchange='changeMobile()'oninput='if(value.length>11)value=value.slice(0,11)'onkeyup='(this.v=function(){this.value=this.value.replace(/[^0-9-]+/,'');}).call(this)'onblur='this.v();'>")
	
	}

	function days() {
		var html = "";
		for (var i = 1; i <= 30; i++) {
			html += "<option>" + i + "</option>"
		}
		$("#days").html(html);
	}
	$(function() {
		days();
		$("#days").val('${requestScope.redio.sum}')
		// 	.val('${requestScope.redio.sum}');  
		var status1 = $('.first-choice-radio').attr('checked');
		var status2 = $('.second-choice-radio').attr('checked');
		if (status1) {
			$('.second-choice').attr('disabled', true);
		}
		if (status2) {
			$('.first-choice').attr('disabled', true);
		}
	})
	$(".first-choice-radio").click(function() {
		$('.second-choice').attr('disabled', true);
		$('.first-choice').removeAttr('disabled');
	})
	$(".second-choice-radio").click(function() {
		$('.first-choice').attr('disabled', true);
		$('.second-choice').removeAttr('disabled');
	})
	$(".modify").click(function() {
		$("textarea").removeAttr("disabled").focus();
		$(".save").css("visibility", "visible").click(function() {
			$(this).css("visibility", "hidden");
			$("textarea").attr("disabled", "disabled");
		})
	})
	function changeRedio(id) {
		$.ajax({
			url : "${ctx}/swust/system/changeRedio",
			type : "post",
			data : {
				"id" : id,
			},
			success : function(data) {

			},
		})
	}
	function changeMessage() {
		$.ajax({
			url : "${ctx}/swust/system/changeMessage",
			type : 'post',
			data : {
				"message" : $("#messageInfo").val(),
			},
			success : function(data) {

			},
		});
	}
	function ChangeDay() {
		$.ajax({
			url : "${ctx}/swust/system/changeDay",
			type : 'post',
			data : {
				"changeDay" : $("#changeDay").val(),
			},
		});
	}

	function changeTime(beginTime, endTime) {
		$.ajax({
			url : "${ctx}/swust/system/changeTime",
			type : 'post',
			data : {
				"beginTime" : beginTime,
				"endTime" : endTime,
				"day" : $("#changeDay").val()
			},
			success : function(data) {

			},
		});
	}
	function changeMoney(id) {
		$.ajax({
			url : "${ctx}/swust/system/changeMoney",
			type : 'post',
			data : {
				"id" : getValueByName(id, 'id'),
				"addMoney" : getValueByName(id, 'addMoney'),
				"defaultTime" : getValueByName(id, 'defaultTime'),
				"maxMoney" : getValueByName(id, 'maxMoney'),
				"moneyMonth" : getValueByName(id, 'moneyMonth')
			},
		});
	}

	function getValueByName(id, name) {
		return $("#tr_" + id).find("input[name='" + name + "']").val();
	}

	$("#choose-filee")
			.change(
					function() {
						var $file = $(this);
						var fileObj = $file[0];
						var windowURL = window.URL || window.webkitURL;
						var dataURL;
						if (fileObj && fileObj.files && fileObj.files[0]) {
							dataURL = windowURL
									.createObjectURL(fileObj.files[0]);
							$(
									'<li><img src="${dataURL}" class="img-responsive"/></li>')
									.insertBefore(".back-right li:last-child");
							systemcontroler();
						}
					});
	function changeDate() {
		$.ajax({
			url : "${ctx}/swust/system/changeDate",
			type : 'post',
			data : {
				"beginDate" : $("#days").val()
			},
		})
	}

	function changeMobile() {
		$.ajax({
			url : "${ctx}/swust/order/updateUserMobile",
			type : 'post',
			data : {
				"mobile" : $("#adminPhone").val()
			},
		})
	}

	function changepicture(id) {
		$.ajax({
			url : "${ctx}/swust/system/changepicture",
			type : 'POST',
			data : "id=" + id,
			success : function(data) {
			},
		});
	}
	$(".back-right").on("click", "img", function() {
		changepicture('${list.id}');
		$(".back-left img").attr("src", $(this).attr("src"));
	});
	function deletepicture(id) {
		$.ajax({
			url : "${ctx}/swust/system/deletepicture",
			type : 'POST',
			data : "id=" + id,
			success : function(data) {
			},
		});
	}

	$(".back-right").on("click", "span", function() {
		$(this).parent().remove();
	})
</script>