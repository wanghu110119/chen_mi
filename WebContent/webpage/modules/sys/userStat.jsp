<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/echarts.jsp"%>
<meta name="decorator" content="default" />
<div class="row">
	<div class="col-sm-12" style="margin: 20px;">
		<form:form id="searchForm" modelAttribute="dict" method="post"
			class="form-inline" onSubmit="return false;">
			<span>时间类型：</span>
			<input type="radio" name="timeType" value="years" />年份 <input
				type="radio" name="timeType" value="year" />某年 <input type="radio"
				name="timeType" value="month" />某月
				<input type="radio" name="timeType" value="cutWeek" />本周
				<input type="radio" name="timeType" value="cutDay" />当天
			
			&nbsp;&nbsp;&nbsp;&nbsp;
			<span id="yearsSelect" style="display: none"> <span>开始：</span>
				<select id="yearsStart" name="yearsStart"
				class="form-control input-sm">
					<option value="" selected="selected">选择开始年份</option>
			</select> &nbsp;&nbsp;&nbsp;&nbsp; <span>结束：</span> <select id="yearsEnd"
				name="" yearsEnd"" class="form-control input-sm">
					<option value="" selected="selected">选择结束年份</option>
			</select>
			</span>

			<span id="yearSelect" style="display: none"> <span>年份：</span>
				<select id="year" name="year" class="form-control input-sm">
					<option value="" selected="selected">选择年份</option>
			</select>
			</span>

			<span id="monthSelect" style="display: none"> <span>年份：</span>
				<select id="monthYear" name="monthYear"
				class="form-control input-sm">
					<option value="" selected="selected">选择年份</option>
			</select>&nbsp;&nbsp;&nbsp;&nbsp; <span>月份：</span> <select id="month"
				name="month" class="form-control input-sm">
					<option value="" selected="selected">选择月份</option>
			</select>
			</span>

			<div class="pull-right" style="margin-right: 50px;">
				<button class="btn btn-primary btn-rounded btn-outline btn-sm "
					onclick="getStat()">
					<i class="fa fa-search"></i> 统计
				</button>
				<button class="btn btn-primary btn-rounded btn-outline btn-sm "
					onclick="list()">
					<i class="fa fa-list"></i> 操作列表
				</button>
				<button id="pie1"
					class="btn btn-primary btn-rounded btn-outline btn-sm "
					style="display: none" onclick="toShowPie1()">百分比变化</button>
				<button id="pie2"
					class="btn btn-primary btn-rounded btn-outline btn-sm "
					onclick="toShowPie2()">总和百分比</button>
			</div>

		</form:form>
	</div>
</div>
<div class="col-sm-12">
	<div id="line_normal" class="main000 col-sm-6" style="height: 500px;"></div>
	<div id="pie_normal" class="main000 col-sm-6" style="height: 500px;"></div>
</div>
<script type="text/javascript">
	var tempData = null;
	function toShowPie1() {
		if (tempData) {
			$("#pie1").hide();
			$("#pie2").show();
			showPie(tempData);
		}
	}
	function toShowPie2() {
		if (tempData) {
			$("#pie2").hide();
			$("#pie1").show();
			showPie2(tempData);
		}
	}
	function list(id) {
		var data = {
			type : 2,
			title : "",
			content : ""
		}

		data.title = '账号操作列表';
		data.content = "${ctx}/sys/userStat/list";
		var index = layer.open(data);
		layer.full(index);
	}
	function getStat() {
		var data = getParameter();
		if (data) {
			//发送参数获取统计结果
			$.ajax({
				type : "GET",
				url : "${ctx}/sys/userStat/getStatForLine",
				data : data,
				dataType : "json",
				success : function(data) {
					tempData = data;
					if (timeType == "cutDay") {
						showLineDay(tempData);
					} else {
						showLine(tempData);
					}
					if ($("#pie1").is(":hidden")) {
						showPie(tempData);
					}
					if ($("#pie2").is(":hidden")) {
						showPie2(tempData);
					}
				}
			});
		}
	}

	function showLineDay(data) {
		option.title.subtext = data.times[0][0].slice(0, 10);
		option.legend.data = [];
		var xAxis = [];
		var seriesData = [];
		var series = [];
		for (var i = 0; i < data.series.length; i++) {
			xAxis.push(data.series[i].name);
			seriesData.push(data.series[i].data[0])
		}
		option.xAxis[0].data = xAxis;
		series.push({
			"yAxisIndex" : 0,
			"name" : "",
			"type" : "bar",
			"symbol" : "none",
			"data" : seriesData
		});
		option.series = series;
		myChart.clear();
		myChart.setOption(option);
	}

	function showPie2(data) {
		optionPie2.legend.data = data.legend;
		optionPie2.title.subtext = option.title.subtext;
		var seriesData = [];
		for (var i = 0; i < data.series.length; i++) {
			var items = data.series[i].data;
			var sum = 0;
			for (var j = 0; j < items.length; j++) {
				sum += items[j];
			}
			seriesData.push({
				value : sum,
				name : data.series[i].name
			});
		}
		optionPie2.series[0].data = seriesData;
		myChart2.clear();
		myChart2.setOption(optionPie2);
	}
	function showPie(data) {
		createTimeline(data);
		var options = [];
		var length = data.series[0].data.length;
		for (var i = 0; i < length; i++) {
			var optionTemp;
			if (i == 0) {
				optionTemp = {
					title : {
						text : '百分比变化',
						subtext : option.title.subtext
					},
					tooltip : {
						trigger : 'item',
						formatter : "{a} <br/>{b} : {c} ({d}%)"
					},
					legend : {
						data : data.legend
					},
					toolbox : {
						show : true,
						feature : {
							saveAsImage : {
								show : true
							},
							magicType : {
								show : true,
								type : [ 'pie', 'funnel' ],
							},
							restore : {
								show : true
							}
						}
					}
				}
			} else {
				optionTemp = {};
			}
			var tempData = [];
			for (var j = 0; j < data.series.length; j++) {
				tempData.push({
					value : data.series[j].data[i],
					name : data.series[j].name
				});
			}
			optionTemp.series = [ {
				name : '账户操作',
				type : 'pie',
				center : [ '50%', '45%' ],
				radius : '50%',
				data : tempData
			} ]
			options.push(optionTemp);
		}
		optionPie.options = options;
		//console.log(optionPie);
		myChart2.clear();
		myChart2.setOption(optionPie);
	}
	function createTimeline(data) {
		//构造时间线
		var timelinedata = [];
		for (var i = 0; i < data.times.length; i++) {
			var timeStr = data.times[i][0];
			timeStr = timeStr.slice(0, 10);
			timelinedata.push(timeStr);
		}
		optionPie.timeline.data = timelinedata;
		if (timeType == "years") {
			optionPie.timeline.label = {
				formatter : function(s) {
					var times = s.split('-')
					return times[0];
				}
			}
			return;
		}
		if (timeType == "year") {
			optionPie.timeline.label = {
				formatter : function(s) {
					var times = s.split('-')
					return times[1];
				}
			}
			return;
		}
		if (timeType == "month") {
			optionPie.timeline.label = {
				formatter : function(s) {
					var times = s.split('-')
					return times[2];
				}
			}
			return;
		}
		if (timeType == "cutWeek") {
			optionPie.timeline.label = {
				formatter : function(s) {
					for (var i = 0; i < timelinedata.length; i++) {
						if (timelinedata[i] == s) {
							if (i == 7) {
								return "周日";
							} else {
								return "周" + (i + 1);
							}
						}
					}
				}
			}
			return;
		}
		if (timeType == "cutDay") {
			optionPie.timeline.label = {
				formatter : function(s) {
					return s;
				}
			}
			return;
		}
	}
	function showLine(data) {
		if (timeType == "cutWeek") {
			option.title.subtext = data.times[0][0].slice(0, 10) + "至"
					+ data.times[6][0].slice(0, 10);
		}
		option.legend.data = data.legend;
		option.xAxis[0].data = data.xAxis;
		var series = [];
		for (var i = 0; i < data.series.length; i++) {
			series.push({
				"yAxisIndex" : 0,
				"name" : data.series[i].name,
				"type" : "line",
				"symbol" : "none",
				"data" : data.series[i].data
			});
		}
		option.series = series;
		myChart.clear();
		myChart.setOption(option);
	}
	var timeType;
	function getParameter() {
		timeType = $('input:radio[name="timeType"]:checked').val();
		if (!timeType) {
			layer.msg('请先选择时间类型', {
				icon : 3,
				time : 2000
			});
			return null;
		}
		var data = {};
		data.timeType = timeType;
		var text = "";
		if (timeType == "years") {
			var yearsStart = $("#yearsStart").val();
			var yearsEnd = $("#yearsEnd").val();
			if (yearsStart > yearsEnd) {
				layer.msg('开始年份不能大于结束年份', {
					icon : 3,
					time : 2000
				});
				return null;
			}
			data.yearsStart = yearsStart;
			data.yearsEnd = yearsEnd;
			text = yearsStart + "年至" + yearsEnd + "年";
		} else if (timeType == "year") {
			var year = $("#year").val();
			data.year = year;
			text = year + "年";
		} else if (timeType == "month") {
			var monthYear = $("#monthYear").val();
			var month = $("#month").val();
			data.monthYear = monthYear;
			data.month = month;
			text = monthYear + "年" + month + "月";
		} else if (timeType == "cutWeek") {
		} else if (timeType == "cutDay") {
		}
		option.title.subtext = text;
		return data;
	}
	var option = {
		"calculable" : true,
		"title" : {
			"text" : "账号操作",
			"subtext" : "账号操作对比曲线"
		},
		"toolbox" : {
			"feature" : {
				"saveAsImage" : {
					"show" : true,
					"title" : "保存为图片",
					"type" : "png",
					"lang" : [ "点击保存" ]
				},
				magicType : {
					show : true,
					type : [ 'line', 'bar' ]
				},
				"dataZoom" : {
					"show" : true,
					"title" : {
						"dataZoom" : "区域缩放",
						"dataZoomReset" : "区域缩放后退"
					}
				},
				"restore" : {
					"show" : true,
					"title" : "还原"
				}
			},
			"show" : true
		},
		"tooltip" : {
			"trigger" : "axis"
		},
		"legend" : {
			"data" : []
		},
		"dataZoom" : {
			"start" : 0,
			"end" : 100,
			"realtime" : true,
			"show" : true
		},
		"xAxis" : [ {
			"type" : "category",
			"name" : "时间",
			"data" : []
		} ],
		"yAxis" : [ {
			"type" : "value",
			"name" : "次数"
		} ],
		"series" : []
	};
	//饼图配置
	var optionPie = {
		timeline : {
			data : [],
			label : {
				formatter : function(s) {
					return s.slice(0, 4);
				}
			},
			autoPlay : false,
			playInterval : 2000
		},
		options : []
	}
	var optionPie2 = {
		title : {
			text : '百分比',
			subtext : '纯属虚构',
		},
		tooltip : {
			trigger : 'item',
			formatter : "{a} <br/>{b} : {c} ({d}%)"
		},
		legend : {
			data : []
		},
		toolbox : {
			show : true,
			feature : {
				saveAsImage : {
					show : true
				},
				magicType : {
					show : true,
					type : [ 'pie', 'funnel' ]
				},
				restore : {
					show : true
				}
			}
		},
		calculable : true,
		series : [ {
			name : '',
			type : 'pie',
			radius : '55%',
			center : [ '50%', '60%' ],
			data : []
		} ]
	};
	var myChart;
	var myChart2;
	$(function() {
		require([ 'echarts', 'echarts/chart/line', 'echarts/chart/bar',
				'echarts/chart/pie', 'echarts/chart/funnel' ], function(ec) {
			myChart = ec.init(document.getElementById('line_normal'));
			myChart2 = ec.init(document.getElementById('pie_normal'));
		});
		$("input[name=timeType]").each(function() {
			$(this).click(function() {
				var timeType = $(this).val();
				if (timeType == "years") {
					$("#yearsSelect").show();
					$("#yearSelect").hide();
					$("#monthSelect").hide();
				} else if (timeType == "year") {
					$("#yearsSelect").hide();
					$("#yearSelect").show();
					$("#monthSelect").hide();
				} else if (timeType == "month") {
					$("#yearsSelect").hide();
					$("#yearSelect").hide();
					$("#monthSelect").show();
				} else if (timeType == "cutWeek") {
					$("#yearsSelect").hide();
					$("#yearSelect").hide();
					$("#monthSelect").hide();
				} else if (timeType == "cutDay") {
					$("#yearsSelect").hide();
					$("#yearSelect").hide();
					$("#monthSelect").hide();
				}
			});
		});
		init();
	});

	function init() {
		var myDate = new Date();
		var cutYear = myDate.getFullYear();
		var html = "";
		//html+='<option value="" selected="selected">选择年份</option>';
		for (var i = cutYear; i > (cutYear - 10); i--) {
			html += '<option value="'+i+'">' + i + '</option>';
		}
		$("#yearsStart").html(html);
		$("#yearsEnd").html(html);
		$("#year").html(html);
		$("#monthYear").html(html);
		html = "";
		for (var i = 1; i <= 12; i++) {
			html += '<option value="'+i+'">' + i + '月</option>';
		}
		$("#month").html(html);
	}
</script>
