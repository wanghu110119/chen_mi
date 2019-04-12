<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<style type="text/css">
    	input.error{  
			border: 1px solid red;  
		}
		label.error{
			display:show;
			color:red;
		}
		.red{
			font-size:12px;
			color:#CD2626;
		}
		#choose-excel{
			margin:10px 0;
			visibility:hidden;
		}
		#choose-label{
			width: 80px;
    		height: 30px;
    		color: #fff;
    		background: #f37022;
    		border-radius: 15px;
    		text-align:center !important;
		}
		#choose-label span{
			color:#fff;
			font-size:20px;
			position:relative;
			top:5px;
		}
		.modal-sm2{
			width:260px !important;
		}
		#import-info .modal-body{
			max-height:500px;
			overflow-x:hidden;
			overflow-y:auto;
		}
		@media (max-width:768px) {
			#delAll{
				margin-top:10px;
				float:inherit;
			}
			.table-responsive>.table>tbody>tr>td{
				    white-space: normal;
			}
		}
    </style>
<div class="col-xs-12">
	<p class="header">
		<span style="width:200px;height:35px;line-height:35px;position:relative;margin-right:80px;">
			<input type="search" placeholder="请输入搜索内容" id="nameSearch" style="height:35px;line-height:35px;border-radius:33px;width:200px;">
			<button
				onclick="officeSearch()" style="position:absolute;right:13px;top:-3px;">
					<img src="${ctxStatic }/swust/images/graySearch.png"/>
			</button>
		</span>
		<button class="right-hand" onclick="isDisable(id, '4')" id="delAll">批量删除</button>
		<button class="right-hand" id = "addManager">新增</button>
		<button class="right-hand" onclick="exportOffice()" id="exportOffice">导出</button>
		<button class="right-hand" id="import-table">导入</button>
	</p>
	<div class="table-responsive">
		<table class="table table-striped">
			<thead>
				<tr>
					<th><input type="checkbox" class="checkAll1"></th>
					<th>编号</th>
					<th>单位名称</th>
					<th>单位用户名</th>
					<th>联系人</th>
					<th>联系电话</th>
					<th>状态</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody id="managerlist">

			</tbody>
		</table>
		<input type="hidden" id="pwdblank" value="" />
	</div>
</div>
<!--新增模态款-->
<div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog"
	id="managerModal">
	<div class="modal-dialog modal-md" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h3 class="modal-title text-center" id="changetitle">新增单位</h3>
			</div>
			<div class="modal-body">
				<div class="row">
					<form class="form-horizontal" id="formsubmit">
						<input type="hidden" name="id" id="oid"/>
						<div class="col-xs-12 col-sm-6">
							<div class="form-group">
								<label for="company" class="col-sm-5 control-label"><span style="color:red">* </span>单位名称:</label>
								<div class="col-sm-7">
									<input type="text" class="form-control required " name="name" id="company">
									<input type="hidden" id="oldcompany">
								</div>
							</div>
							<div class="form-group">
								<label for="contact" class="col-sm-5 control-label">联系人:</label>
								<div class="col-sm-7">
									<input type="text" class="form-control" name="primaryPerson.name" id="contact">
								</div>
							</div>
						</div>
						<div class="col-xs-12 col-sm-6">
							<div class="form-group">
								<label for="companyName"
									class="col-sm-5 control-label input-width"><span style="color:red">* </span>单位用户名:</label>
								<div class="col-sm-7">
									<input type="text" class="form-control required" value="" name="primaryPerson.loginName" id="companyName">
									<input type="hidden" id="oldcompanyName">
								</div>
							</div>
							<div class="form-group">
								<label for="phone" class="col-sm-5 control-label">联系电话:</label>
								<div class="col-sm-7">
									<input type="text" class="form-control " name="primaryPerson.phone" id="phone">
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
			<div class="modal-footer text-center">
				<button type="button" class="btn btn-md confirm" id="dosubmit">确认</button>
				<button type="button" class="btn btn-md confirm"
					data-dismiss="modal" id="finishChange">取消</button>
			</div>
		</div>
	</div>
</div>
<!--警示框-->
<div class="modal fade bs-example-modal-lg warning-box" tabindex="-1" role="dialog"
	id="reminderWarning">
    <div class="modal-dialog modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h3 class="modal-title text-center" id="diswarningModal">确认操作？</h3>
            </div>
            <div class="modal-body">
                <h5 class="text-center text-danger" id="reminderModal"></h5>
            </div>
            <div class="modal-footer text-center">
            	<input type="hidden" id="submitOnly" />
            	<button type="button" class="btn btn-md confirm" onclick="disableSubmit()" id="dissubmitModal">确认</button>
                <button type="button" class="btn btn-md confirm" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>

<!--导入-->
<div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog"
	id="import">
    <div class="modal-dialog modal-sm modal-sm2" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h3 class="modal-title text-center">
                	请选择导入的EXCEL
                </h3>
            </div>
            <div class="modal-body text-center">
               <label for="choose-excel" id="choose-label">
                	<span class="glyphicon glyphicon-plus"></span>
                </label>
                
                <form class="form-horizontal" id="jvForm" action="mobile/swust/manager/import" method="post" enctype="multipart/form-data" >
                	<input type="file" name="file" id="choose-excel" accept="application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" />	<span id="file-name"></span>
            	</form>
            	
            </div>
            <div class="modal-footer text-center">
            	<input type="hidden" id="submitOnly" />
                <button type="button" class="btn btn-md confirm" data-dismiss="modal" id="import-result">确认</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog"
	id="import-info">
    <div class="modal-dialog modal-md">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h3 class="modal-title text-center">
                	导入结果
                </h3>
            </div>
            <div class="modal-body text-center">
            	<p class='text-danger' id = "getResult"></p>
            </div>
            <div class="modal-footer text-center">
            	<input type="hidden" id="submitOnly" />
                <button type="button" class="btn btn-md confirm" data-dismiss="modal" id="import-result">确认</button>
            </div>
        </div>
    </div>
</div>
<!--重置密码模态框-->
	<div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog"
		id="reset">
		<div class="modal-dialog modal-sm" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h3 class="modal-title text-center">修改信息</h3>
				</div>
				<div class="modal-body">
					<h5 class="text-center">是否重置密码？</h5>
				</div>
				<div class="modal-footer text-center">
					<button type="button" class="btn btn-md confirm" onclick="submitReset()">确认</button>
					<button type="button" class="btn btn-md confirm"
						data-dismiss="modal">取消</button>
				</div>
			</div>
		</div>
	</div>
	
<%-- 	<form class="form-horizontal" id="grfesf" method="post" action="mobile/swust/manager/import" enctype="multipart/form-data" > --%>
<!-- 		<input type="file" id="uploadFile" name="file"> -->
<!-- 		<button type="submit">提交</button> -->
<!-- 	</form> -->
	
	
	
<script src="${ctxStatic}/swust/js/backstagePlatform.js"></script>
<script
	src="${ctxStatic}/jquery-validation/1.14.0/jquery.validate.min.js"
	type="text/javascript"></script>
<script
	src="${ctxStatic}/jquery-validation/1.14.0/localization/messages_zh.min.js"
	type="text/javascript"></script>
<!-- 引入自定义的jQuery validate的扩展校验 -->
<script src="${ctxStatic}/common/validateExtend.js"
	type="text/javascript"></script>
<script
	src="${ctxStatic}/jquery-validation/1.14.0/jquery.form.js"
	type="text/javascript"></script>
<script type="text/javascript">

function deleteManagers() {
	var selectIds = new Array(); 
	$("#qrList").find("input[type='checkbox']:checked").each(function(){ 
		var msg = $(this).val();
		selectIds.push(msg);
	})
	$.ajax({
		type : "POST",
		url : "mobile/swust/manager/batchDelete",
		traditional: true,
		data : {
			ids : selectIds,
		},
	});
	officeSearch();
}

function deleteQrCode (id){
	var importModule = new Array(); 
	importModule.push(id);
	$.ajax({
		type : "POST",
		url : "mobile/swust/manager/batchDelete",
		traditional: true,
		data : {
			ids : importModule,
		},
	});
	officeSearch();
};
	
	$(function() {
		$('#import-table').click(function(){
			$("#file-name").html("");
			$("#choose-excel").val("");
			$('#import').modal('show');
// 			$("#file-name").html("");
		})
		$('#choose-excel').change(function(){
			var fileName=$(this).val();
			var start=fileName.lastIndexOf('\\');
			var result=fileName.slice(start+1);
			$('#file-name').html(result);
		})
		$('#import-result').click(function(){
			if($('#file-name')[0].innerHTML===""){
				return true;
			}
			else{
				var formData = new FormData($("#jvForm")[0]);
                $.ajax({  
                	          url: "mobile/swust/manager/import" ,  
                	          type: 'POST',  
                	          data: formData,  
                	          async: false,  
                	          cache: false,  
                	          contentType: false,  
                	          processData: false,  
                	          success: function (data) { 
                	        	  $("#getResult").html(data.msg);
                	          },  
                     });
				$("#import-info").modal('show');
			}
		})
		//初始化列表
		officeSearch();
		$("#formsubmit").validate({
			rules:{
				name:{
					checkName:true,
					rangelength:[2,15],
					remote:{                                          
		               type:"POST",//验证单位是否存在
		               url:"mobile/swust/manager/checkName",
		               data:{
		                 name:function(){return $("#company").val();},
		                 oldname:function(){return $("#oldcompany").val();}
		               } 
		            }
				},
				"primaryPerson.loginName":{
					checkName:false,
					rangelength:[2,10],
					remote:{                                          
		               type:"POST",//验证单位是否存在
		               url:"mobile/swust/manager/checkName",
		               data:{
		                 loginname:function(){return $("#companyName").val();},
						 oldname:function(){return $("#oldcompanyName").val();}
		               } 
		            }
				},
				"primaryPerson.name":{
					rangelength:[2,10],
					checkName:true
				},
				"primaryPerson.phone":{
					number:true
				}	
			},
			messages:{
				"primaryPerson.loginName":{
					rangelength:"请输入长度在{0}到{1}之间的字母、数字",
					remote:"单位用户名已经存在"
				},
				name:{
					rangelength:"请输入长度在{0}到{1}之间的汉字或者字母",
					remote:"单位名称已经存在"
				},
				"primaryPerson.name":{
					rangelength:"请输入长度在{0}到{1}之间的汉字或者字母"
				},
				"primaryPerson.phone":{
					number:"请输入正确的电话号码"
				}

			},
			submitHandler : function(form) {
				//验证通过后的执行方法
	            $(form).ajaxSubmit({
	            	type : "POST",
	            	dataType:"json",
	            	url : "mobile/swust/manager/save",
	    			data : $("#formsubmit").serialize(),
	    			async : false,
	    			success : function(data) {
	    				$("#managerModal").modal("hide");
	    				$("#dissubmitModal").hide();
	    				$("#diswarningModal").html("消息提示！");
						$("#reminderModal").html(data.msg);
						$(".warning-box").modal("show");
						setTimeout(function(){
							$(".warning-box").modal("hide");
						},2000); 
						officeSearch();
	    			}
	            });
	        }
		});
		jQuery.validator.addMethod("checkName", function(value, element) {
	        var char = /^[a-zA-Z\u4e00-\u9fa5]+$/;
	        return this.optional(element) || char.test(value);   
	    }, $.validator.format("单位名只能包含中文、英文"));
		
		 $("#addManager").click(function () {
			 $("#company").val("");
				$("#phone").val("");
				$("#contact").val("");
				$("#companyName").val("");
		      $("#managerModal").modal("show");
		      $("#oldcompany").val("");
		      $(".error").html("");
		      $("#company").attr("class","form-control required");
		      $("#companyName").attr("class","form-control required");
		      $("#changetitle").html("新增单位");
		  });
	});
	
	
	/*导出*/
	function exportOffice() {
		var name = $("#nameSearch").val();
		location.href="mobile/swust/manager/export?pageNo="+$("#pageNo").val()+"&name="+$("#nameSearch").val();
	}
	
	/*编辑单位信息*/
	function updateManage(id) {
// 		alert(id);
		officeSearch();
		$.ajax({
			type : "POST",
			url : "mobile/swust/manager/getOffice",
			data : {
				id : id
			},
			success : function(data) {
				$("#oid").val(data.id);
				$("#company").val(data.name);
				$("#oldcompany").val(data.name);
				$("#contact").val(data.primaryPerson.name);
				$("#companyName").val(data.primaryPerson.loginName);
				$("#oldcompanyName").val(data.primaryPerson.loginName);
				$("#phone").val(data.primaryPerson.phone);  
				$("#managerModal").modal("show")
				$("#changetitle").html("编辑单位");
// 				$("#finishChange").html("成功");form-control isMobile error
				$("#companyName-error").html("");
				$("#company-error").html("");
				$("#phone-error").html("");
				$("#contact-error").html("");
				$("#company").attr("class","form-control required valid");
				$("#companyName").attr("class","form-control required valid");
				$("#phone").attr("class","form-control ");
				$("#contact").attr("class","form-control ");
			}
		});
	}

	/*单位查询*/
	function officeSearch() {
		var name = $("#nameSearch").val();
		var pageNo = $("#pageNo").val();
		var pageSize = $("#pageSize").val();
		$.ajax({
			type : "POST",
			url : "mobile/swust/manager/list",
			data : {
				name : name,
				pageNo : pageNo,
				pageSize : pageSize
			},
			success : function(data) {
				$("#managerlist").empty();
				$("#managerlist").html(data);
			}
		});
	}

	/*分页信息*/
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		officeSearch();
		return false;
	}

	/*重置密码*/
	var rsetpsw;
	function resetPsw(loginName) {
		rsetpsw = loginName;
		$("#reset").modal("show");
	}
	
	function submitReset() {
		$.ajax({
			type : "POST",
			url : "mobile/swust/manager/resetpassword",
			data : {loginName:rsetpsw},
			success : function(data) {
				$("#reset").modal("hide");
				$("#managerModal").modal("hide");
				$("#dissubmitModal").hide();
				$("#diswarningModal").html("消息提示！");
				$("#reminderModal").html(data.msg);
				$(".warning-box").modal("show");
				setTimeout(function(){
					$(".warning-box").modal("hide");
				},2000);
			}
		});
	}
	
	
	var onlyId = new Array();
	var stateOnly = "";
	function isDisable(id, type) {
		onlyId.push(id) ;
		stateOnly = type;
		$("#submitOnly").val("2");
		var msg = "	启用";
		if (type == "1") {
			msg = "禁用";
		}else if(type == "3" || type == "4"){
			msg = "删除";
		}
		
		
		$("#reminderModal").html("确认"+msg+"？")
		$("#diswarningModal").html("确认提示！");
		$("#dissubmitModal").show();
		$("#reminderWarning").modal("show");
	}
	
	function disableSubmit() {
		var url = "mobile/swust/manager/enable";
		if (stateOnly == "1") {
			url = "mobile/swust/manager/disable";
		}else if(stateOnly == "4"){
			url = "mobile/swust/manager/batchDelete";
			$("#managerlist").find("input[type='checkbox']:checked").each(function(){ 
				var msg = $(this).val();
				onlyId.push(msg);
			})
		}else if(stateOnly == "3"){
			url = "mobile/swust/manager/batchDelete";
		}
		$.ajax({
			type : "post",
			url : url,
			traditional: true,
			data : {
				ids : onlyId
			},
			success : function(data) {
				onlyId = new Array();
				$("#dissubmitModal").hide();
				$("#diswarningModal").html("消息提示！");
				$("#reminderModal").html(data.msg);
				$("#reminderWarning").modal("show");
				setTimeout(function(){
					$("#reminderWarning").modal("hide");
				},2000); 
				officeSearch();
			}
		});	
	}
	
	$("#dosubmit").click(function() {
		$("#formsubmit").submit();
	});
</script>