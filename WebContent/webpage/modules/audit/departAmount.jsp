<%@ page contentType="text/html;charset=UTF-8"%>
<div>
	<table class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th>院系名称</th>
				<th>访问次数</th>
				<th>占比</th>
		</thead>
		<tbody id="visit-data">
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
			if (msg == "visit") {
				visit();
			}
		});
	});
	
	/*访问量请求*/
	function visit() {
		$.ajax({
			url : "${ctx}/audit/appvisit/getVisitAmount",
			type : "post",
			async : false,
			data : {grade:"2"},
			success : function(data) {
				$("#visit-data").empty();
				if (data != '' && data.length > 0) {
					$.each(data,function(){
						var html = "<tr>"
									+"<td>"+this.name+"</td>"
									+"<td>"+this.value+"</td>"
									+"<td>"+this.percent+"</td>"
								   +"</tr>"
						$("#visit-data").append(html);
					});
				}
			}
		});
	}
</script>