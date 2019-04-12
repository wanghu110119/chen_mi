<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/echarts.jsp"%>
<meta name="decorator" content="default" />
<div class="aut-record">
	<div class="main-aut-record-title">
		<div style="width: 80px; float: left; line-height: 30px;">
			<label>统计选项：</label>
		</div>
		<div style="width: 35%; float: left;">
			<select id="type" class="form-control">
				<option value="1">认证概况</option>
			</select>
		</div>
	</div>
	<div class="index-aut-record">
		<div style="margin-bottom: 10px; margin-left: 5px;"
			id="successDateDiv">
			<label>认证成功时间：</label> <input id="successDate" name="successDate"
				type="text" readonly="readonly" maxlength="20"
				class="laydate-icon form-control layer-date input-sm" />
		</div>
		<div id="dateSuccess" class="main-aut-record"></div>
	</div>
	<div class="index-aut-record">
		<div style="margin-bottom: 10px;">
			<label>认证失败时间：</label> <input id="faultDate" name="faultDate"
				type="text" readonly="readonly" maxlength="20"
				class="laydate-icon form-control layer-date input-sm" />
		</div>
		<div id="dateFault" class="main-aut-record"></div>
	</div>
</div>

<div class="aut-record">
	<div class="index-aut-record">
		<div class="main-aut-record-title">
			<div style="margin-bottom: 10px;">
				<div style="width: 100px; float: left; line-height: 30px;">
					<label>认证成功类型：</label>
				</div>
				<div style="width: 35%; float: left;">
					<select id="cnSuccessDate" class="form-control">
						<option value="1">本周</option>
						<option value="2">本月</option>
						<option value="3">本年</option>
					</select>
				</div>
			</div>
		</div>
		<div id="cnSuccess" class="main-aut-record"></div>
	</div>
	<div class="index-aut-record">
		<div class="main-aut-record-title">
			<div style="margin-bottom: 10px;">
				<div style="width: 100px; float: left; line-height: 30px;">
					<label>认证失败类型：</label>
				</div>
				<div style="width: 35%; float: left;">
					<select id="cnFaultDate" class="form-control">
						<option value="1">本周</option>
						<option value="2">本月</option>
						<option value="3">本年</option>
					</select>
				</div>
			</div>
		</div>
		<div id="cnFault" class="main-aut-record"></div>
	</div>
</div>

<script type="text/javascript">
	var mydate = new Date();
	var date = mydate.getFullYear() + "-" + (mydate.getMonth()+1) + "-"
		+ mydate.getDate();
	//路径配置
	require.config({
		paths : {
			echarts : '${ctxStatic}/echarts-2.2.7/build/dist'
		}
	});
	$(function() {
		//今日成功
		getData(1, {
			successDate : date
		}, '');
		//今日失败
		getData(2, {
			faultDate : date
		}, '');
		//类型成功
		getData(3, {
			cnSuccessDate : $("#cnSuccessDate").val()
		}, $("#cnSuccessDate").find("option:selected").text());
		//类型失败
		getData(4, {
			cnFaultDate : $("#cnFaultDate").val()
		}, $("#cnFaultDate").find("option:selected").text());
		
		laydate({
			elem : '#successDate',
			event : 'focus',
			choose : function(datas) {
				getData(1, {
					successDate : datas
				});
			}
		});
		laydate({
			elem : '#faultDate',
			event : 'focus',
			choose : function(datas) {
				getData(2, {
					faultDate : datas
				});
			}
		});

		/**/
		$("#cnSuccessDate").change(function() {
			var value = $("#cnSuccessDate").val();
			var name = $("#cnSuccessDate").find("option:selected").text();
			getData(3, {
				cnSuccessDate : value
			}, name);
		});
		$("#cnFaultDate").change(function() {
			var value = $("#cnFaultDate").val();
			var name = $("#cnFaultDate").find("option:selected").text();
			getData(4, {
				cnFaultDate : value
			}, name);
		});
	});

	/*ajax初始化数据*/
	function getData(type, requestData, typeName) {
		var text, subtext, legend;
		$.ajax({
			url : "${ctx}/ident/autrecord/list",
			data : requestData,
			async : false,
			success : function(data) {
				if (type == 1) {
					text = "认证成功概况";
					subtext = '统一鉴权平台成功概况统计';
					legend = '今日认证成功概况统计';
					show("dateSuccess", data, text, subtext, legend);
				} else if (type == 2) {
					text = "认证失败概况";
					subtext = '统一鉴权平台失败概况统计';
					legend = '今日认证失败概况统计';
					show("dateFault", data, text, subtext, legend);
				} else if (type == 3) {
					text = "认证成功概况";
					subtext = '统一鉴权平台成功概况统计';
					legend = typeName + '认证成功概况统计';
					show("cnSuccess", data, text, subtext, legend);
				} else if (type == 4) {
					text = "认证失败概况";
					subtext = '统一鉴权平台失败概况统计';
					legend = typeName + '认证失败概况统计';
					show("cnFault", data, text, subtext, legend);
				}
			}

		});
	}

	/*加载图表*/
	function show(id, data, text, subtext, legend) {
		var myChart;
		/*图表对象初始化*/
		require([ 'echarts', 'echarts/chart/line', 'echarts/chart/bar' ],
				function(ec) {
					myChart = ec.init(document.getElementById(id));
					var options = {
						title : {
							text : text,
							subtext : subtext
						},
						tooltip : {
							trigger : 'axis'
						},
						legend : {
							data : [ legend ]
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
									type : [ 'line' ]
								},
								restore : {
									show : true
								},
								saveAsImage : {
									show : true
								}
							}
						},
						calculable : true,
						dataZoom : {
							show : true
						},
						xAxis : [ {
							type : 'category',
							boundaryGap : false,
							data : data.time
						} ],
						yAxis : [ {
							type : 'value'
						} ],
						series : [ {
							name : legend,
							type : 'line',
							smooth:true, 
							stack : '次数',
							data : data.value
						} ]
					};
					myChart.setOption(options);
					/*动态加载图标宽度*/
					window.addEventListener("resize", function() {
						myChart.resize();
					});
				});
	}
</script>
