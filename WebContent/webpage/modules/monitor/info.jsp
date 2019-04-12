<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html lang="en"
	class="app js no-touch no-android chrome no-firefox no-iemobile no-ie no-ie10 no-ie11 no-ios no-ios7 ipad">
<head>
<meta name="decorator" content="default" />
<script src="${ctxStatic}/echarts-2.2.7/build/dist/echarts-all.js"
	type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/common/systemInfo.js"></script>
<script type="text/javascript">
	function modifySer(key) {
		$.ajax({
			async : false,
			url : "${ctx}/monitor/modifySetting?" + key + "="
					+ $("#" + key).val(),
			dataType : "json",
			success : function(data) {
				if (data.success) {
					alert("更新成功！");
				} else {
					alert("更新失败！");
				}
			}
		});
	}
</script>
<style type="text/css">
	.main {
		width: 100%;
		height: 380px;
	}
	.main-title {
		width: 100%;
		height: 40px;
		background-color: #f3f3f4;
		border: 1px solid #e7eaec;
		line-height: 35px;
		padding:5px;
	}
	.main-title i{
		color: #1ab394;
		font-size: 16px;
	}
	.main-content {
		padding: 5px;
	}
	.meter {
		height: 240px;
	}
</style>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>服务器状态监控</h5>
			</div>
			<div class="ibox-content">
					<div class="main">
						<div class="main-title">
							<span><i class="fa fa-rss-square"></i> 实时监控</span>
						</div>
						<div class="row main-content">
							<div style="width: 100%;">
								<div style="width: 30%;float: left;">
									<div id="main_one" class="meter"></div>
								</div>
								<div style="width: 40%;float: left;">
									<div id="main_two" class="meter"></div>
								</div>
								<div style="width: 30%;float: left;">
									<div id="main_three" class="meter">
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="main">
					<div class="main-title">
						<span><i class="fa fa-briefcase"></i> 警告设置</span>
					</div>
					<div class="main-content">
						<table class="table table-striped"
								width="100%" style="vertical-align: middle;">
								<thead>
									<tr style="background-color: #faebcc; text-align: center;">
										<td width="100">名称</td>
										<td width="100">参数</td>
										<td width="205">预警设置</td>
										<td width="375">邮箱设置</td>
									</tr>
								</thead>
								<tbody id="tbody">
									<tr>
										<td
											style='padding-left: 10px; text-align: left; vertical-align: middle;'>CPU</td>
										<td
											style='padding-left: 10px; text-align: left; vertical-align: middle;'>当前使用率：<span
											id="td_cpuUsage" style="color: red;">50</span> %
										</td>
										<td align="left">
											<form class="form-inline">
  												<div class="form-group">
  													<label>使用率超出</label>
											   	 	<input type="text" class="form-control" name='cpu' id='cpu' value='${cpu}'>%
  												</div>
  											</form>
  											<div>&nbsp;</div>
											<form class="form-inline">
											  <div class="form-group">
											    <label>发送邮箱提示</label>
											    <input type="button" class="btn btn-info btn-xs" onclick='modifySer("cpu");' value='修改'>
											  </div>
											</form>
										</td>
										<td rowspan='3' align="center" style="vertical-align: middle;">
											<div class="input-group input-group-sm"  style='width: 250px;'>
										      <input type="text" class="form-control" value='${toEmail}'>
										      <span class="input-group-btn">
										        <button class="btn btn-info" 
										        	onclick='modifySer("toEmail");' type="button">修改</button>
										      </span>
										    </div>
										</td>
									</tr>
									<tr>
										<td
											style='padding-left: 10px; text-align: left; vertical-align: middle;'>服务器内存</td>
										<td
											style='padding-left: 10px; text-align: left; vertical-align: middle;'>当前使用率：<span
											id="td_serverUsage" style="color: blue;">50</span> %
										</td>
										<td align="left">
											<form class="form-inline">
  												<div class="form-group">
  													<label>使用率超出</label>
											   	 	<input type="text" class="form-control" name='ram' id='ram' value='${ram}'>%
  												</div>
  											</form>
  											<div>&nbsp;</div>
											<form class="form-inline">
											  <div class="form-group">
											    <label>发送邮箱提示</label>
											    <input type="button" class="btn btn-info btn-xs" onclick='modifySer("ram");' value='修改'>
											  </div>
											</form>
										</td>
									</tr>
									<tr>
										<td
											style='padding-left: 10px; text-align: left; vertical-align: middle;'>JVM内存</td>
										<td
											style='padding-left: 10px; text-align: left; vertical-align: middle;'>当前使用率：<span
											id="td_jvmUsage" style="color: green;">50</span> %
										</td>
										<td align="left">
											<form class="form-inline">
  												<div class="form-group">
  													<label>使用率超出</label>
											   	 	<input type="text" class="form-control" name='jvm' id='jvm' value='${jvm}'>%
  												</div>
  											</form>
  											<div>&nbsp;</div>
											<form class="form-inline">
											  <div class="form-group">
											    <label>发送邮箱提示</label>
											    <input type="button" class="btn btn-info btn-xs" onclick='modifySer("jvm");' value='修改'>
											  </div>
											</form>
									</tr>
								</tbody>
							</table>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6">
						<div class="main-title">
							<span><i class="fa fa-th-list"></i> 服务器信息</span>
						</div>
						<div class="main-content">
							<div style="height: 370px;"
									class="embed-responsive embed-responsive-16by9">
									<iframe class="embed-responsive-item"
										src="${ctx}/monitor/systemInfo"></iframe>
								</div>
						</div>
					</div>
					<div class="col-sm-6">
						<div class="main-title">
							<span><i class="fa fa-fire"></i> 实时监控</span>
						</div>
						<div class="main-content">
							<div id="main" style="height: 370px;"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
		</div>
</body>
</html>
