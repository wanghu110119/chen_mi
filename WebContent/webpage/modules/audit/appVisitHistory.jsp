<%@ page contentType="text/html;charset=UTF-8"%>
<div>
	<table class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th>应用名称</th>
				<th>昨天</th>
				<th>今天</th>
				<th>本周</th>
				<th>本月</th>
				<th>本年</th>
				<th>全部</th>
		</thead>
		<tbody id="history-data">
			<tr>
				<td></td>
				<td></td>
				<td></td>
			</tr>
		</tbody>
	</table>
</div>
<script type="text/javascript">
	$(function(){
		/*选项卡过滤*/
		$('#div-title li a').on('shown.bs.tab', function(e) {
			var msg = $(this).attr("aria-controls");
			if (msg == "history") {
				history();
			}
		});
	});
	
	/*访问量请求*/
	function history() {
		$.ajax({
			url : "${ctx}/audit/appvisit/getVisitHistory",
			type : "post",
			async : false,
			success : function(data) {
				$("#history-data").empty();
				if (data != '' && data.length > 0) {
					$.each(data,function(){
						var html = "<tr>"
									+"<td>"+this.name+"</td>"
									+"<td>"+this.yvalue+"</td>"
									+"<td>"+this.value+"</td>"
									+"<td>"+this.wvalue+"</td>"
									+"<td>"+this.mvalue+"</td>"
									+"<td>"+this.yeavalue+"</td>"
									+"<td>"+this.yallvalue+"</td>"
								   +"</tr>"
						$("#history-data").append(html);
					});
				}
			}
		});
	}
</script>