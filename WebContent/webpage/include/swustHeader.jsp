<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
    <style type="text/css">
    	p {
		  	word-break:break-word;
		}
	#orderSelect{
	font-size: 14px;
    color: #515c6b;
    width: 79px;
    height: 20px;
    border:1px solid #3eaab7;
}
@media (max-width:768px) {
/* 	button.collapsed{ */
/* 		display:none; */
/* 	} */
}
    </style>
<nav class="navbar navbar-default navbar-fixed-top" id="nav">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#my-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a href="${ctx}/swust/order" class="navbar-brand">
               成都沉迷推理探案馆预约管理系统
            </a>
        </div>
        <div id="my-collapse" class="collapse navbar-collapse">
            <!--导航栏右侧-->
            <ul class="nav navnar-nav navbar-right" id="login-head">
               <li class="pull-left">
                   <input type="search" id="reaserch" onchange="reaserch()" placeholder="输入姓名或电话">
                   <button class="search" >
                   		<img src="${ctxStatic }/swust/images/whiteSearch.png"/>
                   </button>
                   <input type="hidden" value="" id="hiddenType"/>
                   <input type="hidden" value="" id="hiddenkey"/>
                   <input type="hidden" value="" id="statekey"/>
               </li>
               <li class="pull-left">
                   <a href="#" data-toggle="dropdown" class="dropdown-toggle">
<%--                        <img src="${ctxStatic }/swust/images/user-head.png"> --%>
                       <span>
                       <c:choose>
                       	<c:when test="${empty fns:getSysUser().office.name||fns:getSysUser().office.name eq ''||fns:getSysUser().office.name eq null}">
                       		未命名
                       	</c:when>
                       	<c:otherwise>
                       	${fns:getSysUser().office.name}	
                       	</c:otherwise>
                       </c:choose>
                       </span>
                       <span class="caret"></span>
                   </a>
                   <ul class="dropdown-menu">
                       <li>
                           <a href="#" class="modifyPwd" onclick="init()">
                               <img src="${ctxStatic }/swust/images/modifyPwd.png" alt="">
                               &nbsp;修改密码
                           </a>
                       </li>
                       <li>
                           <a href="${ctx}/logout">
                               <img src="${ctxStatic }/swust/images/exit.png" alt="">
                               &nbsp;注销
                           </a>
                       </li>
                   </ul>
               </li>
            </ul>
        </div>
    </div>
</nav>
 <!-- 修改密码模态框 -->
    <div class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" id="modal-pwd">
        <div class="modal-dialog modal-md" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h3 class="modal-title text-center">修改密码</h3>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" id ="changeform">
                        <div class="form-group">
                            <label for="old-pwd" class="col-sm-2 control-label col-sm-offset-2" >旧密码:</label>
                            <div class="col-sm-6">
                                <input type="password" class="form-control" id="old-pwd" name ="oldPassword">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="new-pwd" class="col-sm-2 control-label col-sm-offset-2">新密码:</label>
                            <div class="col-sm-6">
                                <input type="password" class="form-control" id="new-pwd" name ="newPassword">
                            </div>
                        </div>
                        <div class="form-group" >
                            <label for="inputPassword3" class="col-sm-2 control-label col-sm-offset-2">确认密码:</label>
                            <div class="col-sm-6">
                                <input type="password"  class="form-control" id="inputPassword3" name="confirmPassword">
                                <span id="authentication" ></span>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer text-center">
                    <button type="button" class="btn btn-md confirm" id="dosubmit" onclick="changePwdS()">确认</button>
                </div>
            </div>
        </div>
    </div>
    
    <script src="${ctxStatic }/swust/js/jquery.min.js"></script>
    <script
	src="${ctxStatic}/jquery-validation/1.14.0/jquery.validate.min.js"
	type="text/javascript"></script>
<script
	src="${ctxStatic}/jquery-validation/1.14.0/jquery.form.js"
	type="text/javascript"></script>
<script type="text/javascript">
function model(type) {
	if(type=='2'){
		$("#hiddenType").val("2");
		location.href="${ctx}/swust/car/portal";
	}else{
		$("#hiddenType").val("1");
		location.href="${ctx}/swust/order";
	}
}



function changePwdS() {
	var validateFormC;
	validateFormC = $("#changeform").validate({
		rules:{
			oldPassword:{
				checkName:true,
				rangelength:[6,15],
				required: true,
				remote:{                                          
	               type:"POST",//验证旧密码是否存在
	               url:"${ctx}/swust/order/checkPwd",
	               data:{
	            	   oldPassword:function(){return $("#old-pwd").val();},
		               } 
	            }
			},
			newPassword:{
				checkName:true,
				notEqual:true,
				required: true,
				rangelength:[6,15]
			},
			confirmPassword:{
				checkName:true,
				equalTo: "#new-pwd",
				required: true,
			}	
		},
		messages:{
			oldPassword:{
				rangelength:"请输入长度在{0}到{1}之间的字母、数字",
				required:"请输入旧密码",
				remote:"旧密码输入错误"
			},
			newPassword:{
				rangelength:"请输入长度在{0}到{1}之间的汉字或者字母",
				required:"请输入新密码",
			},
			confirmPassword:{
				required:"请输入确认密码",
				equalTo:"两次输入密码不一致"
			}

		}
		
	});
	jQuery.validator.addMethod("checkName", function(value, element) {
        var char = /^[0-9a-zA-Z]*$/g;
        return this.optional(element) || char.test(value);   
    }, $.validator.format("只能包含英文、数字"));
	
	jQuery.validator.addMethod("notEqual", function(value, element) {
        var oldPassword =$("#old-pwd").val();
        if(value!=oldPassword){
        	return true;
        }
        return false;   
    }, $.validator.format("新密码不能与旧密码一致"));
	
		//验证通过后的执行方法
        if($("#changeform").valid()){
        	 $.ajax({
        	type : "POST",
        	dataType:"json",
        	url : "${ctx}/swust/order/updateNewUserByPassword",
			data : $("#changeform").serialize(),
			async : false,
			success : function(data) {
				if(data.success){
					submit = true;
					setTimeout(function(){
					$('#modal-pwd').modal('hide');
					},800);
					alert("修改成功");
					}
			}
        });
        }
	};





		//初始化修改密码模态框
		function init(){
			$("#authentication").html("");
			$("#new-pwd").val("");
			$("#old-pwd").val("");
			$("#inputPassword3").val("")
			}

		var submit = false;
		var password = false;
		var newandold = false;
</script>
