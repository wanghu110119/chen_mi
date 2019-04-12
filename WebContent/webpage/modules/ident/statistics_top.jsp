<%@ page contentType="text/html;charset=UTF-8"%>
<script src="${ctxStatic}/echarts-2.2.7/build/dist/echarts.js"></script>
<script src="${ctxStatic}/jquery/jquery-2.1.1.min.js"></script>
<script type="text/javascript">
	// 路径配置
	require.config({
		paths : {
			echarts : '${ctxStatic}/echarts-2.2.7/build/dist'
		}
	});
	var arrX = null;
	var arrY = null;
	var myChart;
	var option;
	var joinTitle = "账号入组情况";
	var outTitle = "账号出组情况";
    //手持加载，绑定事件
	$(document).ready(function() {
		getData($("#joinType").val(), $("#joinGroup").val(), 0, "join",joinTitle);
		getData($("#outType").val(), $("#outGroup").val(), 1, "out",outTitle);
		$("#joinType").change(function() {
			getData($("#joinType").val(), $("#joinGroup").val(), 0, "join",joinTitle);
		});
		$("#joinGroup").change(function() {
			getData($("#joinType").val(), $("#joinGroup").val(), 0, "join",joinTitle);
		});
		$("#outType").change(function() {
			getData($("#outType").val(), $("#outGroup").val(), 1, "out",outTitle);
		});
		$("#outGroup").change(function() {
			getData($("#outType").val(), $("#outGroup").val(), 1, "out",outTitle);
		});
	});
	//ajax請求數據
	function getData(type, group, action, id,title) {
		$.ajax({
			url : '${ctx}/auth/identityGroup/getStatictisData/',
			type : 'POST', //GET
			async : true, //或false,是否异步
			data : {
				dateType : type,
				action : action,
				group : group
			},
			dataType : 'json', //返回的数据格式：json/xml/html/script/jsonp/text
			success : function(data, textStatus, jqXHR) {
				arrX = new Array();
				arrY = new Array()
				for (var i = 0; i < data.length; i++) {
					arrX.push(data[i].time);
					arrY.push(data[i].value);
				}
				drawEchart(id,title);
			}
		})
	}
    //画图表
	function drawEchart(id,title) {
		// 使用
		require([ 'echarts', 'echarts/chart/line' // 使用折线图就加载line模块，按需加载
		], 
		function(ec) {
			// 基于准备好的dom，初始化echarts图表
			myChart = ec.init(document.getElementById(id));
			options = {
				title : {
					text : title
				},
				tooltip : {
					show : true
				},
				//                     legend: {
				//                         data:['数量']
				//                     },
				dataZoom : {
					show : true,
				},
				toolbox : {
					show : true,
					feature : {
						mark : {
							show : true
						},
						dataView : {
							show : true,
							readOnly : false
						},
						magicType : {
							show : true,
							type : [ 'line', 'bar' ]
						},
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
					data : arrX
				} ],
				yAxis : [ {
					type : 'value',
					axisLabel : {
						formatter : '{value} 个'
					}
				} ],
				series : [ {
					"name" : "时间",
					"type" : "line",
					"data" : arrY
				} ]
			};

			// 为echarts对象加载数据 
			myChart.setOption(options);
		});
	}
</script>
<style>
    .auth-record {
        float: left;
        width: 45%;
        height: 400px;
        border: 1px solid #e3e3e3;
        margin-top: 20px;
        margin-left: 50px;
    }
    .index-auth-record {
        width: 95%;
        height: 350px;
        margin-left: 5px;
    }
</style>
