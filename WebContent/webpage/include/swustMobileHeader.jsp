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
<script type="text/javascript">
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
		$("#dosubmit").click(function(){
			if(submit&&newandold){
				$.ajax({
					type:"POST",
					url:"${ctx}/swust/order/updateUserByPassword",
					data:"newPassword="+$("#new-pwd").val()+"&oldPassword="+$("#old-pwd").val(),
					success:function(date){
					$("#authentication").html(date.msg);
					if(date.success){
						$("#authentication").attr("style","font-size: 20px; text-align: center;color: green;");
						submit = true;
						setTimeout(function(){
							$('#modal-pwd').modal('hide');
						},800);
						}else{
						$("#authentication").attr("style","font-size: 20px; text-align: center;color: red;");
						submit = false;
							}	
							}
						});
				}
		});
		$("#old-pwd").change(function(){
			$.ajax({
			type:"POST",
			url:"${ctx}/swust/order/UserSelectPassword",
			data:"newPassword="+$("#new-pwd").val()+"&oldPassword="+$("#old-pwd").val(),
			success:function(date){
			$("#authentication").html(date.msg);
			if(date.success){
				$("#authentication").attr("style","font-size: 20px; text-align: center;color: green;");
				submit = true;
				}else{
				$("#authentication").attr("style","font-size: 20px; text-align: center;color: red;");
				submit = false;
					}
					}
				});
			});
		function premitPassword(){
				$("#authentication").attr("style","font-size: 20px; text-align: center;color: red;");
				if(!submit){
					$("#authentication").html("密码有错误，请重新输入");
					return;
				}else if($("#new-pwd").val()==$("#old-pwd").val()){
					$("#authentication").html("新密码不能与旧密码一致");
					newandold = false;
					return;
				} else if($("#new-pwd").val()!=$("#inputPassword3").val()){
					newandold = false;
					$("#authentication").html("两次输入密码有误");
					return;
				}else{
					newandold=true;
					$("#authentication").attr("style","font-size: 20px; text-align: center;color: green;");
					$("#authentication").html("修改密码可以使用");
					return;
					}
			}
</script>
