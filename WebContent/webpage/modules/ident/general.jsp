<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<script src="${ctxStatic}/echarts-2.2.7/build/dist/echarts.js"></script>
<script type="text/javascript">
	// 路径配置
	require.config({
		paths : {
			echarts : '${ctxStatic}/echarts-2.2.7/build/dist'
		} 
	});
</script>
<script type="text/javascript"
	src="${ctxStatic}/common/systemInfo.js"></script>
<style type="text/css">
    .main000 {
        height: 360px;
        /*width: 778px !important;*/
        overflow: hidden;
        width : 45%;
        padding : 10px;
        float : left;
        margin-bottom: 10px;
        margin-top: 20px;
        margin-left: 50px;
        border: 1px solid #e3e3e3;
        -webkit-border-radius: 4px;
           -moz-border-radius: 4px;
                border-radius: 4px;
        -webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.05);
           -moz-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.05);
                box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.05);
    }
</style>
<style type="text/css">
    .main001 {
        height: 360px;
        /*width: 778px !important;*/
        overflow: hidden;
        width : 80%;
        float : left;
    }
</style>
	<div id="bar_normal"  class="main000" ></div>
		<echarts:bar 
		  	id="bar_normal"
			title="今日账号概况" 
			xAxisData="${xAxisData}" 
			yAxisData="${yAxisData}" 
			yAxisName="" />
	<div id="line_normal"  class="main000"></div>
			<echarts:line 
			        id="line_normal"
					title="今日账号认证概况" 
					xAxisData="${xxAxisData}" 
					yAxisData="${yyAxisData}" 
					yAxisName="" />

	<table style="width: 100%; border: 1px solid #e3e3e3;">
		<tr>
			<td width="27%"><div id="pie_1" class="main001"></div>
				<echarts:pie
				    id="pie_1"
					title="今日主服务器状态" 
					orientData="${orientData}"/></td>
			<td width="27%"><div id="pie_2"  class="main001"></div>
				<echarts:pie
				    id="pie_2"
					title="今日辅服务器状态" 
					orientData="${orientData}"/></td>
			<td width="27%"><div id="pie_3"  class="main001"></div>
				<echarts:pie
				    id="pie_3"
					title="今日负载均衡服务器状态" 
					orientData="${orientData}"/></td>
		</tr>
	</table>
