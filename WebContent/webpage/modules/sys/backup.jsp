<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>系统还原与备份</title>
<meta name="decorator" content="default" />
<style>
.main {
	padding: 10px;
}

.main input {
	margin-right: 10px;
}

.content {
	background-color: #f3f3f4;
	border: 1px solid #f3f3f4;
	margin-top: 10px;
	padding: 10px;
}

ul li {
	list-style: none;
}

.backup {
	padding:10px;
}

.restore-title {
	width:100%;
	text-align: center;
}
.restore-title select {
	width: 50%;
}

.restore-content {
	margin-top:10px;
	padding: 10px;
	background-color: #f3f3f4;
	border: 1px solid #f3f3f4;
}

.param-select {
	padding:10px;
}

#schedule button {
	margin:5px;
	cursor: inherit;
}
img {
	width: 30px;
	height: 10px;
}

#backlog {
	width: 300px;
}

</style>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>系统还原与备份</h5>
			</div>
			<div class="ibox-content">
				<div>
				</div>
				<div class="main">
					<div class="title">
						<input type="button" class="btn btn-primary btn-sx" value="一键备份" onclick="show('备份数据', '开始备份', '取消备份','#backup')">
						<input type="button" class="btn btn-primary btn-sx" value="还原备份" onclick="show('还原备份', '还原备份', '取消还原','#restore')">
						<input type="button" class="btn btn-primary btn-sx" value="系统重置">
					</div>
					<div class="content">
						<h3>操作说明</h3>
						<div>
							<ul>
								<li>1.一键备份：将备份所有用户的信息以及用户的应用权限。</li>
								<li>2.还原备份：还原以前备份的所有用户信息以及用户的应用权限。</li>
								<li>3.系统重置：统一鉴权平台将删除用户数据以及恢复初始化设置。</li>
							</ul>
						</div>
						<h3>备份事项</h3>
						<div>
							<ul>
								<li>1.建议备份数据时，不要执行其他操作。</li>
								<li>2.单击一键备份弹出备份窗口，在单击开始备份才会执行备份操作。</li>
							</ul>
						</div>
						<h3>还原事项</h3>
						<div>
							<ul>
								<li>1.建议在还原数据时，不要执行其他操作。</li>
								<li>2.单击还原备份弹出还原窗口，选择需要还原的备份文件，在单击还原备份才会执行还原操作。</li>
								<li>3.还原数据库后当前数据库中的值将被会被覆盖。</li>
								<li>4.还原成功后请重启Tomcat服务器。</li>
							</ul>
						</div>
						<h3>系统重置</h3>
						<div>
							<ul>
								<li>1.建议在系统重置时，不要执行其他操作。</li>
								<li>2.单击系统重置弹出重置窗口，在单击开始重置才会执行重置操作。</li>
								<li>3.还原数据库后当前数据库中的值将被覆盖。</li>
								<li>4.重置成功后请重启Tomcat服务器。</li>
							</ul>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div id="backup" style="display: none;">
			<form class="form-inline param-select">
				<div class="form-group">
			    	<label>平台应用：</label>
			    	<select class="form-control" id="backup-sp">
						<c:forEach var="item" items="${splist}" varStatus="status">
							<option value="${item.id}">${item.name}</option>
						</c:forEach>
					</select>
			  	</div>
			</form>
			<div class="backup">
				<h4>备份事项</h4>
				<div>
					<ul>
						<li>1.建议在备份数据时，不要执行其他操作。</li>
						<li>2.单击开始备份才会执行备份操作。</li>
					</ul>
				</div>
				<h4>备份进度</h4>
				<div id="schedule"></div>
			</div>
		</div>
		<div id="restore" style="display: none;">
			<div class="backup">
				<form class="form-inline param-select">
					<div class="form-group">
				    	<label>平台应用：</label>
				    	<select class="form-control" id="restore-sp">
							<c:forEach var="item" items="${splist}" varStatus="status">
								<option value="${item.id}">${item.name}</option>
							</c:forEach>
						</select>
				  	</div>
				</form>
				<div class="restore-title">
					<select class="form-control" id="backlog">
						<option value="1">-请选择备份文件-</option>
					</select>
				</div>
				<div class="restore-content">
					<h4>还原事项</h4>
					<div>
						<ul>
							<li>1.建议在还原数据时，不要执行其他操作。</li>
							<li>2.单击还原备份弹出还原窗口，选择需要还原的备份文件，在单击还原备份才会执行还原操作。</li>
							<li>3.还原数据库后当前数据库中的值将被会被覆盖。</li>
							<li>4.还原成功后请重启Tomcat服务器。</li>
						</ul>
					</div>
				</div>
				<h4>还原进度</h4>
				<div id="restore-se"></div>
			</div>
		</div>
		
	</div>
	<script type="text/javascript">
		$(function(){
			$("#restore-sp").change(function(){
				log();
			});
			log();
		});
		function show(title,sub,close,id){
			layer.open({
			    type: 1,  
			    area: ['500px', '450px'],
			    title: title,
		        maxmin: true, 
			    content: $(id) ,
			    btn: [sub, close],
			    yes: function(index, layero){
					if ("备份数据" == title) {
						backup();
					} else if ("还原备份" == title) {
						restore();
					}
				},
			    cancel: function(index){ 
			    	$("#schedule").empty();
			    	clearInterval(timer);
			    },
			    btn2:function(index, layero){
			    	$("#schedule").empty();
			    	clearInterval(timer);
			    }
			}); 
		}
		
		/*备份*/
		function backup() {
			$("#schedule").empty();
			clearInterval(timer);
			var sid = $("#backup-sp").val();
			if (sid != null && sid != '') {
				$.ajax({
					url:"${ctx}/sys/backup/copy",
					type:"post",
					data:{sid : sid},
					dataType:"json",
					success:function(data){
						if (data!=null && data != "") {
							if (data.success) {
								$("#schedule").append(template("btn-info","开始备份"));
								schedule("1", sid, "schedule");
							} else {
								layer.msg(data.msg, {icon: 5});
							}
						} 
					}
				});
			}
		}
		
		/*还原*/
		function restore() {
			$("#restore-se").empty();
			var sid = $("#restore-sp").val();
			clearInterval(timer);
			var logid = $("#backlog").val();
			if (logid != null && logid != '' && logid!="1") {
				$.ajax({
					url:"${ctx}/sys/backup/restore",
					type:"post",
					data:{logid : logid},
					success:function(data){
						if (data!=null && data != "") {
							if (data.success) {
								$("#restore-se").append(template("btn-info","开始还原"));
								schedule("2", sid, "restore-se");
							} else {
								layer.msg(data.msg, {icon: 5});
							}
						} 
					}
				});
			} else {
				layer.msg("请选择需要还原的备份文件", {icon: 5});
			}
		}
		
		/*进度查询*/
		var timer;
		function schedule(type, sid, id) {
			var ing=0,fail=0,success=0;
			//进度
			timer = setInterval(function(){
					$.ajax({
						url:"${ctx}/sys/backup/schedule",
						type:"post",
						data:{sid : sid, type:type},
						success:function(data){
							if (data != "") {
								//进行中
								if (data == "2") {
									if (ing == 0) {
										$("#"+id).append(template("btn-warning",type == "1"?"备份中":"还原中"));
										ing = 1;
									}
								}
								//失败
								else if (data == "3") {
									if (fail == 0) {
										var html = "<button class='btn btn-danger btn-sm'>"+type == "1"?"备份失败":"还原失败"+"</button>"
										$("#"+id).append(html);
										fail = 1;
									}
									clearInterval(timer);
								} 
								//成功
								else if (data == "4") {
									if (success == 0) {
										var html = "<button class='btn btn-primary btn-sm'>"+(type == "1"?"备份成功":"还原成功")+"</button>"
										$("#"+id).append(html);
										success = 1;
									}
									clearInterval(timer);
								}
							}
						}
					});
				},500);
		}
		
		/*进度模板*/
		function template(style, name) {
			var html = "<button class='btn "+style+" btn-sm'>"+name+"</button>"
						 +"<span>"
						 + "<img src='${ctxStatic}/common/img/schedule.gif'>"
						 +"</span>";
			return html;
		}
		
		
		/*备份日志*/
		function log() {
			$("#backlog").empty();
			$("#backlog").append("<option value='1'>-请选择备份文件-</option>");
			var sid = $("#restore-sp").val();
			$.ajax({
				url:"${ctx}/sys/backup/loglist",
				type:"post",
				data:{sid : sid},
				success:function(data){
					if (data != null && data != "") {
						$.each(data, function(){
							$("#backlog").append("<option value="+this.id+">"+this.name+"</option")
						});			
					}
				}
			});
		}
		
	</script>
</body>
</html>