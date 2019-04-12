<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>安全策略配置</title>
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
	height: 180px;
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
	text-indent:25px;
}

.s-content-left-main {
	padding: 10px;
}

input[type="radio"] {
	margin-top: 0px;
}
</style>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>安全策略配置</h5>
			</div>
			<div class="ibox-content">
			</div>
		</div>
	</div>
	<script type="text/javascript">
		$(function(){
			/*初始化数据*/
			baseData();
		});
		
		function baseData() {
			$.ajax({
				url : "${ctx}/audit/safeStrategy/getTypeList",
				type : "post",
				async : false,
				success : function(data) {
					if (data != "" && data.length > 0) {
						$(".ibox-content").empty();
						var html="";
						var rid="";
						$.each(data, function(){
							var id = this.dict.id;
							html += baseHtml(this.dict.label, id);
						});
						$(".ibox-content").html(html);
						$.each(data, function(e){
							var id = this.dict.id;
							rid = id;
							$("#con-"+id).html(contentData(id));
						});
						$(".ibox-content :radio").iCheck({checkboxClass:"icheckbox_square-green",radioClass:"iradio_square-green"});
					}
				}
			});
		}
		
		function baseHtml(name,id) {
			var html = "<div class='s-main'>"
						 +"<div class='s-top'>"
						   +"<div class='s-title'>"+name+"</div>"
						   +"</div>"
						 +"</div>"
						 +"<div class='s-content'>"
							+"<div class='s-content-left' id='con-"+id+"'>"
							+"</div>"
							+"<div class='s-content-right' id='remark-"+id+"'></div>"
						 +"</div>"
					   +"</div>";
			return html;
		}
		
		function contentData(id) {
			var html="";
			$.ajax({
				url : "${ctx}/audit/safeStrategy/list",
				type : "post",
				data : {"did":id},
				async : false,
				success : function(data) {
					if (data != "" && data.length > 0) {
						var bd = data.length;
						var remark = "",rid = "",ids="";
						$.each(data, function(sd){
							rid = this.dict.id;
							ids += this.id+",";
							html += contentHtml(this, sd+1, bd, ids);
							remark += (this.remarks==null||this.remarks=="")?"":"<p>"+(sd+1)+"."+this.remarks+"</p>";
						});
						$("#remark-"+rid).html(remark);
					}
				}
			});
			return html;
		}
		
		function contentHtml(data, sd, bd, ids) {
			var html =  "<div class='s-content-left-main'>"
						 +"<form class='form-inline'>"
							+"<div class='form-group' id="+data.id+" type="+data.paramType+">"
							  +"<label>"+data.label+"：</label>"
							  +paramHtml(data)
							+"</div>"
							+btnHtml(data, sd, bd, ids)
						   +"</form>"
						  +"</div>";
			return html;
		}
		
		function btnHtml(data, sd, bd, ids) {
			var msg = "";
			if (sd == bd) {
				var id = data.dict.id;
				msg = "<div style='float: right; margin-right: 5%;'>"
						+"<input type='button' class='btn btn-primary btn-sm' value='保存' onclick='save(\""+ids+"\")'>"
						+"&nbsp;&nbsp;<input type='button' class='btn btn-warning btn-sm' value='默认' onclick='defaultSetting(\""+id+"\")'>"
					  +"</div>";
			}
			return msg;
		}
		
		function paramHtml(data) {
			var type = data.paramType;
			var html = "";
			var label = data.dcr == null ? "":data.dcr;
			//select
			if (type == "1") {
				html += "<span>"+label+"&nbsp;&nbsp;</span>"
						 +"<select class='form-control'>"
				 		 	+ select(data.paramValue, data.value)
				 		 +"</select>";
			} 
			//radio
			else if (type == "2") {
				html += radio(data.paramValue, data.value)
			}
			return html;
		}
		
		function select(value, svalue) {
			var html = "";
			if (value != "") {
				var msg = value.split(",");
				$.each(msg, function(index,value){
					var select = svalue == value ?"selected=selected":""
					html += "<option value="+value+" "+select+">"+value+"</option>";
				});
			}
			return html;
		}
		
		function radio(value, svalue) {
			var html = "";
			if (value != "") {
				var msg = value.split(",");
				$.each(msg, function(index,value){
					var checked = svalue == value ?"checked=checked":""
					html += "<label class='radio-inline'>"
								+"<input type='radio' name='value' value="+value+" class='i-checks' "+checked+"> "+value
						   +"</label>";
				});
			}
			return html;
		}
		
		/*保存*/
		function save(ids) {
			layer.confirm('是否保存信息?', function(index){
				var msg = ids.split(",");
				var paramValue = new Array();
				$.each(msg, function(index,id){
					if (id != null && id != "") {
						var type = $("#"+id).attr("type");
						if (type == "1") {
							var value = $("#"+id).find("select option:selected").val();
							var detail = {"id":id,"value":value};
							paramValue.push(detail);
						} else if (type == "2") {
							var value = $("#"+id).find("input[type='radio']:checked").val();
							var detail = {"id":id,"value":value};
							paramValue.push(detail);
						}
					}
				});
				$.ajax({
					url : "${ctx}/audit/safeStrategy/save",
					type : "post",
					contentType:"application/json",
					data : JSON.stringify(paramValue),
					async : false,
					success : function(data) {
						layer.msg('保存成功！', {icon: 1});
					}
				});
			});  
		}
		
		/*默认*/
		function defaultSetting(id) {
			layer.confirm('是否恢复默认值?', function(index){
				$.ajax({
					url : "${ctx}/audit/safeStrategy/defaultValue",
					type : "post",
					data : {"id":id},
					async : false,
					success : function(data) {
						layer.msg('修改成功！', {icon: 1});
						window.location.href="${ctx}/audit/safeStrategy";
					}
				});
			});
		}
	</script>
</body>
</html>