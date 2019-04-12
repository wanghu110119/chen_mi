<%@ page contentType="text/html;charset=UTF-8"%>
<div class="row">
	<div class="col-sm-12 main-aut-record-title">
		<div style="margin-bottom: 10px;">
			<div
				style="width: 100px; float: left; line-height: 30px; margin-bottom: 10px;">
				<label>统计日期类型：</label>
			</div>
			<div style="width: 100px; float: left;">
				<select id="visitDate" class="form-control">
					<option value="1">今天</option>
					<option value="2">昨天</option>
					<option value="3">本周</option>
					<option value="4">本月</option>
					<option value="5">本年</option>
					<option value="6">全部</option>
				</select>
			</div>
		</div>
	</div>
	<div id="main" class="col-sm-12"></div>
</div>
<script src="http://echarts.baidu.com/build/dist/echarts.js"></script>
<script type="text/javascript">
	var requestData = {type:"1",grade:"2"};
	$(function() {
		/*选项卡过滤*/
		$('#div-title li a').on('shown.bs.tab', function(e) {
			var msg = $(this).attr("aria-controls");
			if (msg == "statistics") {
				setTimeout(function() {
					getData("今日");
				}, 500);
			}
		});
		
		/*日期类型*/
		$("#visitDate").change(function(){
			var msg = $(this).val();
			var msgname = $(this).find("option:selected").text();
			requestData = {type:msg,grade:"2"}
			getData(msgname);
		});
		
	});

	//路径配置
	require.config({
		paths : {
			echarts : '${ctxStatic}/echarts-2.2.7/build/dist'
		}
	});
	
	/*ajax初始化数据*/
	function getData(subtext) {
		$.ajax({
			url : "${ctx}/audit/appvisit/getVisitStatistics",
			data : requestData,
			async : false,
			success : function(data) {
				show("main", data, subtext);
			}
		});
	}

	/*加载图表*/
	var myChart;
	function show(id, data, subtext) {
		/*图表对象初始化*/
		subtext = subtext + "院系应用访问概况统计分析";
		require([ 'echarts', 'echarts/chart/pie' ], function(ec) {
			myChart = ec.init(document.getElementById(id));
			var options = { 
				title : {
					text : '院系应用访问统计图',
					subtext : subtext,
					x : 'center'
				},
				tooltip : {
					trigger : 'item',
					formatter : "{a} <br/>{b} : {c} ({d}%)"
				},
				legend : {
					orient : 'vertical',
					x : 'left',
					data : data.value
				},
				toolbox : {
					show : true,
					feature : {
						restore : {
							show : true
						},
						saveAsImage : {
							show : true
						}
					}
				},
				calculable : true,
				series : [ {
					name : '应用名称',
					type : 'pie',
					radius : '55%',
					center : [ '50%', '60%' ],
					data : data.list
				} ]
			};
			myChart.setOption(options);
		});
	}
	
	/*动态加载图标宽度*/
	window.addEventListener("resize", function() {
		myChart.resize();
	});
</script>
