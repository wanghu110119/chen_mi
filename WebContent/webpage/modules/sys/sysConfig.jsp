<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>系统显示配置</title>
<meta name="decorator" content="default" />
<!-- SUMMERNOTE -->
 <link href="${ctxStatic}/summernote/summernote.css" rel="stylesheet">
 <link href="${ctxStatic}/summernote/summernote-bs3.css" rel="stylesheet">
 <script src="${ctxStatic}/summernote/summernote.min.js"></script>
 <script src="${ctxStatic}/summernote/summernote-zh-CN.js"></script>
 <script src="${ctxStatic}/common/ajaxfileupload.js"></script>
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
	width: 110px;
	color: #fff;
	background-color: #1ab394;
	text-align: center;
	line-height: 30px;
	height: 30px;
}

.s-content {
	width: 100%;
	padding: 10px;
	height: 380px;
	margin: 0px auto;
}

.s-content-left {
	width: 50%;
	float: left;
	border-right: 1px solid #e7eaec;
	padding-left: 2%;
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

#sysproject {
	display: none;
}
.upload{float: left;margin-left:5px;}
</style>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>系统显示配置</h5>
			</div>
			<div class="ibox-content">
			</div>
		</div>
		<div id="sysproject">
		 	<form class="form-inline">
			<div class="form-group">
			  <label>选择维护系统：</label>
			  <select class="form-control">
			  	<c:forEach items="${projects}" var="item" varStatus="status">
				  	<option value="${item.id}">${item.name}</option>
				  </c:forEach> 
			  </select>
			</div>
		   </form>
		</div>
	</div>
	<script type="text/javascript">
		$(function(){
			/*初始化数据*/
			baseData();
			/*初始化文本框*/
			$('.summernote').summernote({
				height: 150,
				lang: 'zh-CN',
			  	codemirror: {
			    	theme: 'monokai'
			  	},
				toolbar: [
				   ['style', ['bold', 'italic', 'underline', 'clear']],
				   ['fontsize', ['fontsize']],
				   ['color', ['color']],
				   ['para', ['ul', 'ol', 'paragraph']],
				   ['height', ['height']],
				  ]
			});
		});
		
		function baseData() {
			$.ajax({
				url : "${ctx}/sys/config/getTypeList",
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
							if (this.dict.value == "1") {
								var value = $("#sysproject").find("select").val();
								$("#con-"+id).find("select").val(value);
							}
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
				url : "${ctx}/sys/config/list",
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
			var label = (data.label == null || data.label == '') ? "":data.label+"：";
			var type = data.paramType;
			var html = "";
			if (data.dict.value == "1") {
				//设置选中值
				if (data.sysProject.id != null 
					&& data.sysProject.id != '') {
					 $("#sysproject").find("select").each(function(){
						 $(this).val(data.sysProject.id);
					 });
				}
				var msg = $("#sysproject").html();
				html += "<div class='s-content-left-main' id='p-"+data.id+"'>" +msg+ "</div>";
			}
			html +=  "<div class='s-content-left-main'>"
						 +"<form class='form-inline' method='post' enctype='multipart/form-data'>"
							+"<div class='form-group' id="+data.id+" type="+data.paramType+" dvalue="+data.dict.value+">"
							  +"<label>"+label+"</label>"
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
				msg = "<div style='width:100%;margin-top:10px;text-align: right;'>"
						+"<input type='button' class='btn btn-primary btn-sm' value='保存' onclick='save(\""+ids+"\")'>"
					  +"</div>";
			}
			return msg;
		}
		
		function paramHtml(data) {
			var type = data.paramType;
			var html = "";
			//text
			if (type == "1") {
				html += "<input type='text' class='form-control' value='"+data.value+"'/>"
			} 
			//textarea
			else if (type == "2") {
				html += "<div class='summernote' id='summernote-"+data.id+"'>"+data.value+"</div>";
			}
			//file
			else if (type == "3") {
				var filePath = (data.value != null && data.value != '')?data.value : "";
				html += "<div class='input-group'>"
			      			+"<input type='text' id='picname' class='form-control' value='"+filePath+"' placeholder='' disabled='disabled'>"
		     	 			+"<span class='input-group-btn'>"
		        			+"<button class='btn btn-primary' type='button' class='form-control' onclick='file.click()'>上传文件</button>"
		        			+"<button class='btn btn-warning' type='button' class='form-control' onclick='ajaxFileUpload(\""+data.id+"\")'>确认</button>"
		        			+"<input type='file' id='file' name='file' accept='image/jpeg,image/jpg,image/png'"
		        				+" onchange='picname.value=this.value' style='display:none'>"
						    +"</span>"
					   +"</div>";
			}
			return html;
		}
		
		
		/*保存*/
		function save(ids) {
			layer.confirm('是否保存信息?', function(index){
				var msg = ids.split(",");
				var paramValue = new Array();
				var isFile = false;
				$.each(msg, function(index,id){
					if (id != null && id != "") {
						var type = $("#"+id).attr("type");
						var dvalue = $("#"+id).attr("dvalue");
						var projectId = "";
						if (dvalue != "" && dvalue == "1") {
							projectId = $("#p-"+id).find("select").val();
						}
						if (type == "1") {
							var value = $("#"+id).find("input").val();
							var detail = {"id":id,"value":value, "sysProject":{"id":projectId}};
							paramValue.push(detail);
						} else if (type == "2") {
							var value = $("#summernote-"+id).code();
							var detail = {"id":id,"value":value, "sysProject":{"id":projectId}};
							paramValue.push(detail);
						} else if (type == "3") {
						}
					}
				});
				$.ajax({
					url : "${ctx}/sys/config/save",
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
		
		/*文件上传*/
		function ajaxFileUpload(id) {
			 $.ajaxFileUpload({
                   url: '${ctx}/sys/config/saveLogo?id='+id, 
                   secureuri: false, 
                   type: 'post',
                   fileElementId: 'file',
                   success: function (data, status){
                	   layer.msg('上传成功！', {icon: 1});
                   },
                   error: function (data, status, e)
                   {
                	   layer.msg('上传失败！', {icon: 5});
                   }
               });
		}
	</script>
</body>
</html>