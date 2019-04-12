<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>SSO配置</title>
<meta name="decorator" content="default" />
<style type="text/css">
.s-main {
	width: 100%;
	margin-bottom: 10px;
}

.s-top {
	width: 100%;
	height: 30px;
	border-bottom: 1px solid #1ab394;
}

.s-title {
	width: 100px;
	color: #fff;
	background-color: #1ab394;
	text-align: center;
	line-height: 30px;
	height: 30px;
}

.s-content {
	width: 100%;
	padding: 10px;
	height: 220px;
	margin: 0px auto;
}

.s-content-left {
	width: 50%;
	float: left;
	border-right: 1px solid #e7eaec;
	padding-left: 8%;
}

.s-content-right {
	width: 50%;
	float: right;
	margin: 0px auto;
	padding: 5px;
	text-indent: 25px;
}

.s-content-left-main {
	padding: 10px;
}

input[type="radio"] {
	margin-top: 0px;
}

.clist {
	height: 30px;
}
.saveBtn {
	width: 100%;
}
.saveBtn input {
	float: right;
	margin-right: 10%;
}
</style>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>SSO配置</h5>
			</div>
			<div class="ibox-content">
				<div class='s-main'>
					<div class='s-top'>
						<div class='s-title'>唯一标识配置</div>
					</div>
				</div>
				<div class='s-content'>
					<div class='s-content-left'>
						
					</div>
					<div class='s-content-right'></div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		$(function() {
			/*初始化数据*/
			baseData();
		});

		function baseData() {
			$.ajax({
				url : "${ctx}/sys/sso/list",
				type : "post",
				async : false,
				success : function(data) {
					if (data != "" && data.length > 0) {
						$(".s-content-left").empty();
						var html = "";
						$.each(data,function(){
							var checked = this.status == "1"?"checked=checked":"";
							html += "<div class='clist'><input type='checkbox' id='"+this.id+"' "+checked+" class='i-checks'/>"+this.name+"</div>"
						});
						html += "<div class='saveBtn'><input type='button' class='btn btn-primary btn-sm' value='保存' onclick='save()' /></div>";
						$(".s-content-left").html(html);
						$(".ibox-content :checkbox").iCheck({
							checkboxClass : "icheckbox_square-green",
							radioClass : "iradio_square-green"
						});
					}
				}
			});
		}
		
		function save() {
			layer.confirm('是否修改参数信息?', function(index){
				var list = new Array();
				$(".s-content-left").find("input[type='checkbox']").each(function(){ 
					var id = $(this).attr("id");
					if (true == $(this).prop('checked')) {
						list.push({id:id,status:'1'});
					} else {
						list.push({id:id,status:'2'});
					}
				}); 
				$.ajax({
					url : "${ctx}/sys/sso/edit",
					type : "post",
					data : JSON.stringify(list),
					contentType : "application/json",
					async : false,
					success : function(data) {
						if (data != "") {
							if (data.success) {
								baseData();
								layer.msg(data.msg, {icon: 1});
							} else {
								layer.msg(data.msg, {icon: 5});
							}
						}
					}
				});
			});
		}
	</script>
</body>
</html>