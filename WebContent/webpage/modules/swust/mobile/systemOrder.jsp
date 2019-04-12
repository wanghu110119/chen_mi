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
</style>
<div class="col-xs-12">
	<p class="header">
	<span style="width:200px;display:inlin-block;height:35px;line-height:35px;position:relative;">
		<input type="search" style="height:35px;line-height:35px;border-radius:33px;width:200px;" placeholder="请输入搜索内容" id="name"> <span></span>
		<button style="position:absolute;right:13px;top:-3px;" onclick="searchMsg()">
			<img src="${ctxStatic }/swust/images/graySearch.png"/>
		</button>
	</span>
		<button class="right-hand" onclick="exportSysOrder()">导出</button>
	</p>
	<ul class="state-menu">
		<li class="active"><a href="#not-check" data-toggle="tab"
			onclick="searchSysOrder(0)">待审核列表</a></li>
		<li><a href="#checked" data-toggle="tab"
			onclick="searchSysOrder(1)">已审核列表</a>
		</li>
		<select  id="orderSelect">
				<option style="text-align: center" value="">全部</option>
							<option value="0">正常预约</option>
							<option value="red">超时预约</option>
						</select>
		<li id="batch">
			<button onclick="batch(this)" btype="3">批量删除</button>
			<button onclick="batch(this)" btype="1">批量通过</button>
			<button onclick="batch(this)" btype="2">批量驳回</button>
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
				<h3 class="modal-title text-center">预约事由</h3>
			</div>
			<div class="modal-body">
				<p class="text-center" id="orderReason">${order.orderReason}</p>
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
<div class="modal fade bs-example-modal-lg warning-box" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h3 class="modal-title text-center" id="warningModal">确认操作？</h3>
            </div>
            <div class="modal-body">
                <h5 class="text-center text-danger" id="reminder"></h5>
            </div>
            <div class="modal-footer text-center">
            	<input type="hidden" id="submitOnly" />
                <button type="button" class="btn btn-md confirm" onclick="batchSubmit()" id="submitModal">确认</button>
                <button type="button" class="btn btn-md confirm" data-dismiss="modal">取消</button>
            </div>
        </div>
    </div>
    <!--重置密码模态框-->
</div>
<script type="text/javascript">
	$('.warning-box').on('click','button[data-dismiss="modal"]',function(){
		$('.modal-backdrop').hide();
	})
	var type = "0";
	var btype = "1";
	$(function(){
		//初始化数据
		searchSysOrder("0");
		//切换标签
		$(".state-menu").on("click","a",function(){
			var href = $(this).attr("href");
			if (href == "#not-check") {
				$("#batch").show();
			} else {
				$("#batch").hide();
			}
		});
		
		$("#orderSelect").change(function(){
			 searchSysOrder($("#stateKey").val());
		})
		
		
	});
	
	function searchMsg() {
		searchSysOrder(type);
	}
	
	function page(n,s){
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
	
	/*导出*/
	function exportSysOrder() {
		var name = $("#name").val();
		location.href="mobile/swust/appointment/export?state="+type+"&orderName="+name;
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
	
	/*通过、驳回*/
	var onlyId = "";
	var stateOnly = "";
	function rebut(id, state) {
		onlyId = id;
		stateOnly = state;
		$("#submitOnly").val("2");
		var msg = "驳回";
		//通过
		if (state == "1") {
			msg = "通过";
		}
		$("#reminder").html("确认"+msg+"？")
		$("#warningModal").html("确认提示！");
		$("#submitModal").show();
		$(".warning-box").modal("show");
	};
	
	/*批量操作确认*/
	var selectIds = new Array();
	function batch(param) {
		$("#submitOnly").val("1");
		$("#orderList table tbody").find("input[type='checkbox']:checked").each(function(){ 
			var msg = $(this).val();
			selectIds.push(msg);
		})
		if (selectIds.length > 0) {
			var msg = $(param).html();
			btype = $(param).attr("btype");
			$("#reminder").html("确认选中"+msg+"？")
			$("#warningModal").html("确认提示！");
			$("#submitModal").show();
			$(".warning-box").modal("show");
		} else {
			$("#submitModal").hide();
			$("#warningModal").html("警告提示！");
			$("#reminder").html("请选择需要批量操作的信息！");
			$(".warning-box").modal("show");
		}	
	}
	function deleteOrder(param) {
		$("#submitOnly").val("1");
			selectIds.push(param);
		if (selectIds.length > 0) {
			btype = $(param).attr("btype");
			$("#reminder").html("确认删除？")
			$("#warningModal").html("确认提示！");
			$("#submitModal").show();
			$(".warning-box").modal("show");
		} 
	}
	/*批量操作提交*/
	function batchSubmit() {
		var submitType = $("#submitOnly").val();
		//批量
		if (submitType == "1") {
			var url = "";
			//批量驳回
			if (btype == "2") {
				url = "mobile/swust/appointment/batchReject";
			}
			else if (btype == "3") {
				url = "mobile/swust/appointment/batchDelete";
			}
			//批量通过
			else {
				url = "mobile/swust/appointment/batchPass";
			}
			$.ajax({
				type : "post",
				url : url,
				traditional: true,
				data : {
					ids : selectIds
				},
				success : function(data) {
					$("#submitModal").hide();
					$("#warningModal").html("消息提示！");
					$("#reminder").html(data.msg);
					$(".warning-box").modal("show");
					setTimeout(function(){
						$(".warning-box").modal("hide");
					},2000); 
					searchSysOrder("0");
					selectIds = new Array();
				}
			});
		} else {
			var url = "mobile/swust/appointment/pass";
			//驳回
			if (stateOnly == "0") {
				url = "mobile/swust/appointment/reject";
			}
			$.ajax({
				type : "post",
				url : url,
				data : {
					id : onlyId
				},
				success : function(data) {
					$("#submitModal").hide();
					$("#warningModal").html("消息提示！");
					$("#reminder").html(data.msg);
					$(".warning-box").modal("show");
					setTimeout(function(){
						$(".warning-box").modal("hide");
					},2000); 
					searchSysOrder("0");
				}
			});	
		}
	}
	
	function sendSms() {
		$.ajax({
			type : "post",
			url : "mobile/swust/appointment/sendSms",
			success : function(data) {
			}
		});	
	}
</script>

