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
<style type="text/css">
    .main000 {
        height: 360px;
        /*width: 778px !important;*/
        overflow: hidden;
        width : 90%;
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
    .main001 {
        height: 360px;
        overflow: hidden;
        width : 80%;
        float : left;
    }
    .index{width: 100%;}
    .index-left{float: left;width: 50%;}
    .index-right{float: right;width: 50%;}
</style>
	<div class="index">
		<div class="index-left">
			<div id="bar_normal"  class="main000" ></div>
			<echarts:bar 
			  	id="bar_normal"
				title="今日账号概况" 
				xAxisData="${xAxisData}" 
				yAxisData="${yAxisData}" 
				yAxisName="" />
		</div>
		<div class="index-right">
			<div id="line_normal"  class="main000"></div>
			<echarts:line 
			        id="line_normal"
					title="今日账号认证概况" 
					xAxisData="${xxAxisData}" 
					yAxisData="${yyAxisData}" 
					yAxisName="" />
		</div>
	</div>
