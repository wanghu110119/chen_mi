<%@ page contentType="text/html;charset=UTF-8"%>
<div>
	<table class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th>院系名称</th>
				<th>访问趋势</th>
				<th>增加量</th>
		</thead>
		<tbody id="trend-data">
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
			if (msg == "trend") {
				trend();
			}
		});
	});
	
	/*访问量请求*/
	function trend() {
		$.ajax({
			url : "${ctx}/audit/appvisit/getVisitTrend",
			type : "post",
			data : {grade:"2"},
			async : false,
			success : function(data) {
				$("#trend-data").empty();
				if (data != '' && data.length > 0) {
					$.each(data,function(){
						var value = this.value;
						var yvalue = this.yvalue;
						var cvalue = (value == 0 || value == yvalue) ? "<span class='text-info'><i class='fa fa-minus'></i></span>":
							value > yvalue ? "<span class='text-danger'><i class='fa fa-arrow-up'></i></span>" :
							"<span class='text-warning'><i class='fa fa-arrow-down'></i></span>";
						var add = yvalue == 0 ? value * 100 + ".00%": (value == 0 || value == yvalue)  ? "0.00%":
							value - yvalue > 0 ? toDecimal((value - yvalue)/yvalue) + "%":"0.00%";
						var html = "<tr>"
									+"<td>"+this.name+"</td>"
									+"<td>"+cvalue+"</td>"
									+"<td>"+add+"</td>"
								   +"</tr>"
						$("#trend-data").append(html);
					});
				}
			}
		});
	}
	
	/*小数点转换*/
	function toDecimal(x) {    
        var f = parseFloat(x);    
        if (isNaN(f)) {    
            return false;    
        }    
        var f = Math.round(x*100*100)/100;    
        var s = f.toString();    
        var rs = s.indexOf('.');    
        if (rs < 0) {    
            rs = s.length;    
            s += '.';    
        }    
        while (s.length <= rs + 2) {    
            s += '0';    
        }    
        return s;    
    }
</script>