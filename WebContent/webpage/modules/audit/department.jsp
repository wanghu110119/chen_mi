<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>院系访问统计</title>
<meta name="decorator" content="default" />
<style>
/* img { */
/* 	width: 50px; */
/* 	height: 50px; */
/* } */
/* 	table {text-align: center;} */
.checkCenter {
	text-align: center;
}
#main{
	height: 400px;
    /* width: 778px !important; */
    overflow: hidden;
    padding: 10px;
    margin-bottom: 10px;
    border: 1px solid #e3e3e3;
    -webkit-border-radius: 4px;
    -moz-border-radius: 4px;
    border-radius: 4px;
    -webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.05);
    -moz-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.05);
    box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.05);
}
</style>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>院系访问统计</h5>
				<div class="ibox-tools">
					<div id="div-title">
						<!-- Nav tabs -->
						<ul class="nav nav-tabs" role="tablist">
							<li role="presentation" class="active"><a href="#log"
								aria-controls="log" role="tab" data-toggle="tab">访问日志</a></li>
							<li role="presentation"><a href="#statistics"
								aria-controls="statistics" role="tab" data-toggle="tab">访问统计图</a></li>
							<li role="presentation"><a href="#visit"
								aria-controls="visit" role="tab" data-toggle="tab">访问量TOP10</a></li>
							<li role="presentation"><a href="#user" aria-controls="user"
								role="tab" data-toggle="tab">用户占比TOP10</a></li>
							<li role="presentation"><a href="#trend"
								aria-controls="trend" role="tab" data-toggle="tab">访问趋势TOP10</a></li>
						</ul>

					</div>
				</div>
				<div class="ibox-content">
					<!-- Tab panes -->
					<div class="tab-content">
						<div role="tabpanel" class="tab-pane active" id="log">
							<%@ include file="/webpage/modules/audit/departLog.jsp"%>
						</div>
						<div role="tabpanel" class="tab-pane" id="statistics">
							<%@ include file="/webpage/modules/audit/departStatistics.jsp"%>
						</div>
						<div role="tabpanel" class="tab-pane" id="visit">
							<%@ include file="/webpage/modules/audit/departAmount.jsp"%>
						</div>
						<div role="tabpanel" class="tab-pane" id="user">
							<%@ include file="/webpage/modules/audit/departUser.jsp"%>
						</div>
						<div role="tabpanel" class="tab-pane" id="trend">
							<%@ include file="/webpage/modules/audit/departTrend.jsp"%>
						</div>
					</div>

				</div>
			</div>
		</div>
	</div>
</body>
</html>