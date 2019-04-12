<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>安全策略配置</title>
<meta name="decorator" content="default" />
<style type="text/css">
.content-left {
	width: 25%;
	float: left;
	height: 500px;
	padding-right: 10px;
}

.content-right {
	width: 75%;
	height: 500px;
	float: right;
}

.main {
	width: 100%;
	height: 500px;
}

.d-echarts {
	border: 1px solid #e3e3e3;
	height: 450px;
	padding: 5px;
	border-radius: 3px;
}

.app-main {
	padding: 10px;
}

#app-list {
	overflow: auto;
	max-height: 400px;
}

.condition {
	height: 40px;
}
</style>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>实时访问统计</h5>
			</div>
			<div class="ibox-content">
				<div class="main">
					<div class="content-left">
						<div>
							<ul class="nav nav-tabs" role="tablist" id="title-type">
								<li role="presentation" class="active"><a href="#app"
									aria-controls="app" role="tab" data-toggle="tab">按应用查看</a></li>
								<li role="presentation"><a href="#section"
									aria-controls="section" role="tab" data-toggle="tab">按院系查看</a></li>
							</ul>
							<div class="tab-content">
								<div role="tabpanel" class="tab-pane active" id="app">
									<div class="app-main">
										<div class="list-group" id="app-list">
											<c:forEach items="${apps}" var="item" varStatus="status">
												<a href="#" aid="${item.id}"
													class="list-group-item ${status.index eq 0 ?'active':''}">
													${item.name} </a>
											</c:forEach>
										</div>
									</div>
								</div>
								<div role="tabpanel" class="tab-pane" id="section">
									<div class="app-main">
										<div class="list-group" id="section-list">
										
										</div>
									</div>
								</div>
							</div>

						</div>
					</div>
					<div class="content-right">
						<div class="condition">
							<form class="form-inline">
								<div class="form-group">
									<label>间隔时间：</label> <select class="form-control"
										id="paramtime">
										<option value=1>1min</option>
										<option value=2>10min</option>
										<option value=3>30min</option>
										<option value=4>3hour</option>
										<option value=5>1day</option>
										<option value=6>3day</option>
									</select>
								</div>
								<div class="form-group">&nbsp;&nbsp;&nbsp;</div>
<!-- 								<div class="form-group"> -->
<!-- 									<label>院系：</label> <select class="form-control"> -->
<!-- 										<option>计算机学院</option> -->
<!-- 										<option>电子学院</option> -->
<!-- 									</select> -->
<!-- 								</div> -->
							</form>
						</div>
						<div id="main" class="d-echarts"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- ECharts单文件引入 -->
	<script src="http://echarts.baidu.com/build/dist/echarts.js"></script>
	<script type="text/javascript">
		var myChart;
		var timeTicket;
		//路径配置
		require.config({
			paths : {
				echarts : '${ctxStatic}/echarts-2.2.7/build/dist'
			}
		});

		$(function() {
			$("#app-list a").click(function() {
				$("#app-list").find("a").each(function() {
					$(this).removeClass("active");
				});
				$(this).addClass("active");
				chartData($(this).attr("aid"),"");
			});

			$("#section-list").on("click","a", function() {
				$("#section-list").find("a").each(function() {
					$(this).removeClass("active");
				});
				$(this).addClass("active");
				chartData("", $(this).attr("aid"));
			});

			$("#paramtime").change(function() {
				setTime();
			});
			
			$("#title-type").find("a").click(function(){
				var value = $(this).attr("aria-controls");
				if (value == "app") {
					init(1);
				} else if (value == "section") {
					init(2);
				}
			});
			
			getOffices();
			
			init(1);
		})
		
		function init(type) {
			if (type == 1) {
				$("#app-list").find("a").each(function(index) {
					if (index==0) {
						chartData($(this).attr("aid"), "");
					}
				});
			} else {
				$("#section-list").find("a").each(function(index) {
					if (index==0) {
						chartData("", $(this).attr("aid"));
					}
				});
			}
		}
		
		function getOffices(id) {
			$.ajax({
				url : "${ctx}/audit/realtime/getOffices",
				type : "post",
				async : false,
				success : function(data) {
					if (data != "" && data.length > 0) {
						$("#section-list").empty();
						$.each(data, function(index){
							var value = index == 0?"active":"";
							$("#section-list").append("<a href='#' class='list-group-item "+value+"' aid="+this.id+">"+this.name+"</a>");
						});
					}
				}
			});
			
		}

		/*设置实时数据时间*/
		function setTime(aid, oid) {
			var time = getTime($("#paramtime").val());
			var type = $("#paramtime").val();
			var lastData = 11;
			var axisData;
			clearInterval(timeTicket);
			timeTicket = setInterval(function() {
				$.ajax({
					url : "${ctx}/audit/realtime/getTimerData",
					type : "post",
					data : {"id":aid,"type":type,"oid":oid},
					async : false,
					success : function(data) {
						if (data != "" && data.length > 0) {
							$.each(data,function(){
								myChart.addData(this.timer);
							})
						}
					}
				});
			}, time);
		}

		function getTime(key) {
			var time = 60 * 1000;
			switch (key) {
			case "1":
				time = 60*1000;
				break;
			case "2":
				time = 10 * 60 * 1000;
				break;
			case "3":
				time = 30 * 60 * 1000;
				break;
			case "4":
				time = 3 * 60 * 60 * 1000;
				break;
			case "5":
				time = 24 * 60 * 60 * 1000;
				break;
			case "6":
				time = 3 * 24 * 60 * 60 * 1000;
				break;
			default:
				break;
			}
			return time;
		}
		
		function chartData(id, oid) {
			var type = $("#paramtime").val();
			$.ajax({
				url : "${ctx}/audit/realtime/getRealTime",
				type : "post",
				data : {"id":id,"type":type,"oid":oid},
				async : false,
				success : function(data) {
					if (data != "" && data.length > 0) {
						$.each(data, function(){
							show("main", this, id, oid);
						});
					}
				}
			});
		}
		
		/*加载图表*/
		var myChart;
		function show(id, data, aid, oid) {
			/*图表对象初始化*/
			require([ 'echarts', 'echarts/chart/line' ], function(ec) {
				myChart = ec.init(document.getElementById(id));
	 			var option = {
	 				tooltip : {
	 					trigger : 'axis'
	 				},
	 				legend : {
	 					 orient: 'horizontal',
	 					 x: 'center',
	 					y: 'top', 
	 					data : data.section
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
	 				xAxis : [ {
	 					type : 'category',
	 					boundaryGap : false,
	 					data : data.value
	 				} ],
	 				yAxis : [ {
	 					type : 'value'
	 				} ],
	 				series : data.list
	 			};
	 			myChart.setOption(option,true); 
	 			
	 			setTime(aid, oid);
	 			/*动态加载图标宽度*/
	 			window.addEventListener("resize", function() {
	 				myChart.resize();
	 			})
			});
		}
	</script>
</body>
</html>