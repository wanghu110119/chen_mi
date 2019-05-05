<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<style type="text/css">
p {
	word-break: break-word;
}

#orderSelect {
	font-size: 14px;
	color: #515c6b;
	width: 79px;
	height: 20px;
	border: 1px solid #3eaab7;
}

#carTypeSelect {
	font-size: 14px;
	color: #515c6b;
	width: 140px;
	height: 20px;
	border: 1px solid #3eaab7;
}

#orderchoose-excel {
	margin: 10px 0;
	visibility: hidden;
}

#orderchoose-label span {
	color: #fff;
	font-size: 20px;
	position: relative;
	top: 5px;
}

#orderchoose-label {
	width: 80px;
	height: 30px;
	color: #fff;
	background: #f37022;
	border-radius: 15px;
	text-align: center !important;
}

@media ( max-width :768px) {
	#importButton {
		display: none;
	}
	#exportButton {
		display: none;
	}
}
</style>

<div class="col-xs-12">
	<p class="header">
		<span
			style="width: 200px; display: inlin-block; height: 35px; line-height: 35px; position: relative;">
			<input type="search"
			style="height: 35px; line-height: 35px; border-radius: 33px; width: 200px;"
			placeholder="请输入来访人姓名或电话" id="name"> <span></span>
			<button style="position: absolute; right: 13px; top: -3px;"
				onclick="searchMsg()">
				<img src="${ctxStatic }/swust/images/graySearch.png" />
			</button>
		</span>
<!-- 		<button class="right-hand" onclick="importSysOrder()" -->
<!-- 			id="importButton">导入</button> -->
		<button class="right-hand" onclick="exportSysOrder()"
			id="exportButton">导出</button>
	</p>
	<ul class="state-menu">
		<li class="active"><a href="#not-check" data-toggle="tab"
			onclick="searchSysOrder(0)">未完成列表</a></li>
		<li><a href="#checked" data-toggle="tab"
			onclick="searchSysOrder(1)">已完成列表</a></li>
		<select id="orderSelect">
			<option style="text-align: center" value="">全部</option>
			<option value="0">正常预约</option>
			<option value="red">超时预约</option>
		</select>
		<select id="carTypeSelect">
			<option style="text-align: center" value="">开本房间</option>
			<option value="1">恐怖主题房（6）</option>
			<option value="2">古风主题房（7）</option>
			<option value="3">摩洛哥主题房（8）</option>
			<option value="4">不给钱，蹲客厅</option>
		</select>
		<!-- <input type="text" style="border: 1px solid #3eaab7; height: 20px"
			placeholder="起始时间" />

		<input type="text" style="border: 1px solid #3eaab7; height: 20px"
			placeholder="截止时间" /> -->

				<input id="startDate" type='text'  style="border: 1px solid #3eaab7; height: 20px !important;width: 290px"
					class=" " placeholder="选择时间">
				<input type="hidden" id = "beginTime"/>
				<input type="hidden" id = "endTime"/>
			<!-- 	<input class=" " id="overDate" placeholder="结束时间" style="border: 1px solid #3eaab7; height: 20px !important"
					name="overDate" type="text"> -->



		<li id="batch">
			<button onclick="batch(this)" btype="3">批量删除</button>
			<button onclick="batch(this)" btype="1">批量收款</button>
			<button onclick="batch(this)" btype="2">批量未收款</button>
		</li>
	</ul>
	<div class="tab-content">
		<div class="table-responsive tab-pane active" id="not-check">
			<input type="hidden" id="stateKey" value="0">
			<div id="orderList">

				<!-- 预约管理插入位置 -->

			</div>
		</div>
	</div>
</div>
<div class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog"
	id="check-view">
	<div class="modal-dialog modal-sm" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h3 class="modal-title text-center">特殊备注</h3>
			</div>
			<div class="modal-body">
				<p class="text-center" id="orderReason">${'order.orderReason' eq null ?'无备注':'order.orderReason'}</p>
				<p class="text-center">
					<span class="notice">备注：</span> <span id="remarks"></span>
				</p>
			</div>
			<div class="modal-footer text-center">
				<button type="button" class="btn btn-md confirm"
					data-dismiss="modal">关闭</button>
			</div>
		</div>
	</div>
</div>
<!--警示框-->
<div class="modal fade bs-example-modal-lg warning-box" tabindex="-1"
	role="dialog">
	<div class="modal-dialog modal-sm" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h3 class="modal-title text-center" id="warningModal">确认操作？</h3>
			</div>
			<div class="modal-body">
				<h5 class="text-center text-danger" id="reminder"></h5>
			</div>
			<div class="modal-footer text-center">
				<input type="hidden" id="submitOnly" />
				<button type="button" class="btn btn-md confirm"
					onclick="batchSubmit()" id="submitModal">确认</button>
				<button type="button" class="btn btn-md confirm"
					data-dismiss="modal">取消</button>
			</div>
		</div>
	</div>
	<!--重置密码模态框-->
</div>

<div class="modal fade bs-example-modal-lg warning-box" tabindex="-1"
	role="dialog" id="orderWarningBox">
	<div class="modal-dialog modal-sm" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h3 class="modal-title text-center" id="orderwarningModal">确认操作？</h3>
			</div>
			<div class="modal-body">
				<h5 class="text-center text-danger" id="orderreminder"></h5>
			</div>
			<div class="modal-footer text-center">
				<input type="hidden" id="ordersubmitOnly" />
				<button type="button" class="btn btn-md confirm"
					onclick="batchSubmit()" id="ordersubmitModal">确认</button>
				<button type="button" class="btn btn-md confirm"
					data-dismiss="modal">取消</button>
			</div>
		</div>
	</div>
	<!--重置密码模态框-->
</div>



<!--导入-->
<div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog"
	id="orderimport">
	<div class="modal-dialog modal-sm" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h3 class="modal-title text-center">请选择导入的EXCEL</h3>
			</div>
			<div class="modal-body text-center">
				<label for="orderchoose-excel" id="orderchoose-label"> <span
					class="glyphicon glyphicon-plus"></span>
				</label>

				<form class="form-horizontal" id="orderjvForm"
					action="${ctx}/swust/appointment/import" method="post"
					enctype="multipart/form-data">
					<input type="file" name="file" id="orderchoose-excel"
						accept="application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" />
					<span id="orderfile-name"></span>
				</form>
			</div>
			<div class="modal-footer text-center">
				<input type="hidden" id="ordersubmitOnly" />
				<button type="button" class="btn btn-md confirm"
					data-dismiss="modal" id="orderimport-result">确认</button>
				<button class="btn btn-md confirm" onclick="exportOrder()">下载模板</button>
			</div>
		</div>
	</div>
</div>

<!-- 导出结果  -->
<div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog"
	id="orderimport-info">
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
				<p class='text-danger' id="ordergetResult"></p>
			</div>
			<div class="modal-footer text-center">
				<button type="button" class="btn btn-md confirm"
					data-dismiss="modal">确认</button>
			</div>
		</div>
	</div>
</div>
<script src="${ctxStatic }/swust/js/bootstrap.min.js"></script>
<script type="text/javascript">
	/**
	 * 导出模板
	 */
	function exportOrder() {
		location.href = "${ctx}/swust/appointment/exportModel";
	}
	function importSysOrder() {
		$("#orderfile-name").html("");
		$("#orderchoose-excel").val("");
		$('#orderimport').modal('show');
	}

	$('#orderimport-result').click(function() {
		if ($('#orderfile-name')[0].innerHTML === "") {
			return true;
		} else {
			var formData = new FormData($("#orderjvForm")[0]);
			$.ajax({
				url : "${ctx}/swust/appointment/import",
				type : 'POST',
				data : formData,
				async : false,
				cache : false,
				contentType : false,
				processData : false,
				success : function(data) {
					$("#ordergetResult").html(data.msg);
					searchMsg();
				},
			});
			$("#orderimport-info").modal('show');
		}
	})

	$('#orderchoose-excel').change(function() {
		var fileName = $(this).val();
		var start = fileName.lastIndexOf('\\');
		var result = fileName.slice(start + 1);
		if (result.indexOf(".xlsx") >= 0) {
			$('#orderfile-name').html(result);
		} else {
			alert("请上传正确的文件格式（.xlsx文件）");
		}
		return;
	});

	$('#orderWarningBox').on('click', 'button[data-dismiss="modal"]',
			function() {
				$('.modal-backdrop').hide();
			});
	var type = "0";
	var btype = "1";
	$(function() {
		//初始化数据
		searchSysOrder("0");
		//切换标签
		$(".state-menu").on("click", "a", function() {
			var href = $(this).attr("href");
			if (href == "#not-check") {
				$("#batch").show();
			} else {
				$("#batch").hide();
			}
		});

		$("#orderSelect").change(function() {
			searchSysOrder($("#stateKey").val());
		})

		$("#carTypeSelect").change(function() {
			searchSysOrder($("#stateKey").val());
		})
	});

	function searchMsg() {
		searchSysOrder(type);
	}

	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		searchSysOrder(type);
		return false;
	}

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
				state : state,
				orderName : name,
				pageNo : pageNo,
				pageSize : pageSize,
				color : $('#orderSelect option:selected').val(),
				carType : $('#carTypeSelect option:selected').val(),
				beginTime : $("#beginTime").val(),
				endTime : $("#endTime").val()
			},
			success : function(date) {
				$("#orderList").empty();
				$("#orderList").html(date);
			}
		});
	}

	/*导出*/
	function exportSysOrder() {
		var name = $("#name").val();
		location.href = "${ctx}/swust/appointment/export?state=" + type
				+ "&orderName=" + name+ "&color=" + $('#orderSelect option:selected').val()+ "&carType=" + $('#carTypeSelect option:selected').val()
				+ "&beginTime=" + $("#beginTime").val()+ "&endTime=" + $("#endTime").val()+ "&pageSize=2000";
		
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

				if (arr[0] === 'null') {
					$("#orderReason").html("批量导入预约")
				} else {
					$("#orderReason").html(arr[0])
				}
				$("#remarks").html(arr[1])
				$("#check-view").modal("show").html();
			}
		});
	};

	/*通过、驳回*/
	var onlyId = "";
	var stateOnly = "";
	function rebut(id, state) {
		onlyId = id;
		stateOnly = state;
		$("#ordersubmitOnly").val("2");
		var msg = "未收款";
		//通过
		if (state == "1") {
			msg = "收款";
		}
		$("#orderreminder").html("确认" + msg + "？")
		$("#orderwarningModal").html("确认提示！");
		$("#ordersubmitModal").show();
		$("#orderWarningBox").modal("show");
	};

	/*批量操作确认*/
	var selectIds = new Array();
	function batch(param) {
		selectIds = new Array();
		$("#ordersubmitOnly").val("1");
		$("#orderList table tbody").find("input[type='checkbox']:checked")
				.each(function() {
					var msg = $(this).val();
					selectIds.push(msg);
				})
		console.log(selectIds.length);
		if (selectIds.length > 0) {
			var msg = $(param).html();
			btype = $(param).attr("btype");
			$("#orderreminder").html("确认选中" + msg + "？")
			$("#orderwarningModal").html("确认提示！");
			$("#ordersubmitModal").show();
			$("#orderWarningBox").modal("show");
		} else {
			$("#ordersubmitModal").hide();
			$("#orderwarningModal").html("警告提示！");
			$("#orderreminder").html("请选择需要批量操作的信息！");
			$("#orderWarningBox").modal("show");
		}
	}
	function deleteOrder(param) {
		$("#ordersubmitOnly").val("1");
		selectIds.push(param);
		btype = '3';
		$("#orderreminder").html("确认删除？")
		$("#orderwarningModal").html("确认提示！");
		$("#ordersubmitModal").show();
		$("#orderWarningBox").modal("show");
	}
	/*批量操作提交*/
	function batchSubmit() {
		var submitType = $("#ordersubmitOnly").val();
		//批量
		if (submitType == "1") {
			var url = "";
			//批量驳回
			if (btype == "2") {
				url = "${ctx}/swust/appointment/batchReject";
			} else if (btype == "3") {
				url = "${ctx}/swust/appointment/batchDelete";
			}
			//批量通过
			else {
				url = "${ctx}/swust/appointment/batchPass";
			}
			$.ajax({
				type : "post",
				url : url,
				traditional : true,
				data : {
					ids : selectIds
				},
				success : function(data) {
					$("#ordersubmitModal").hide();
					$("#orderwarningModal").html("消息提示！");
					$("#orderreminder").html(data.msg);
					$("#orderWarningBox").modal("show");
					setTimeout(function() {
						searchSysOrder("0");
						$("#orderWarningBox").modal("hide");
					}, 2000);
					selectIds = new Array();
				}
			});
		} else {
			var url = "${ctx}/swust/appointment/pass";
			//驳回
			if (stateOnly == "0") {
				url = "${ctx}/swust/appointment/reject";
			}
			$.ajax({
				type : "post",
				url : url,
				data : {
					id : onlyId
				},
				success : function(data) {
					$("#ordersubmitModal").hide();
					$("#orderwarningModal").html("消息提示！");
					$("#orderreminder").html(data.msg);
					$("#orderWarningBox").modal("show");
					setTimeout(function() {
						searchSysOrder("0");
						$("#orderWarningBox").modal("hide");
					}, 2000);
				}
			});
		}

	}

	function sendSms() {
		$.ajax({
			type : "post",
			url : "${ctx}/swust/appointment/sendSms",
			success : function(data) {
			}
		});
	}
</script>

