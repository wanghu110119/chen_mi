<%@ page contentType="text/html;charset=UTF-8"%>
<div>
	<table class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th>院系名称</th>
				<th>环比前日</th>
				<th>日访问量</th>
				<th>访问人数/总用户数</th>
		</thead>
		<tbody id="user-data">
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
			if (msg == "user") {
				user();
			}
		});
	});
	
	/*访问量请求*/
	function user() {
		$.ajax({
			url : "${ctx}/audit/appvisit/getVisitUser",
			type : "post",
			data : {grade:'2'},
			async : false,
			success : function(data) {
				$("#user-data").empty();
				if (data != '' && data.length > 0) {
					$.each(data,function(){
						var yvalue = this.yvalue;
						var value = this.value;
						var cvalue = value > yvalue ? "<span class='text-danger'><i class='fa fa-arrow-up'></i></span>" :
							value < yvalue ? "<span class='text-warning'><i class='fa fa-arrow-down'></i></span>":
								"<span class='text-info'><i class='fa fa-minus'></i></span>";
						var html = "<tr>"
									+"<td>"+this.name+"</td>"
									+"<td>"+cvalue+"</td>"
									+"<td>"+this.value+"</td>"
									+"<td>"+this.percent+"</td>"
								   +"</tr>"
						$("#user-data").append(html);
					});
				}
			}
		});
	}
</script>