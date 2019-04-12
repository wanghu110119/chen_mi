<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head lang="zh-cn">
    <meta charset="UTF-8">
    <!--2 viewport-->
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no">
    <!--3、x-ua-compatible-->
    <meta http-equiv="x-ua-compatible" content="IE=edge">
    <title>等待审核管理员登录检测</title>
    <!--4、引入两个兼容文件-->
    <!--[if lt IE 9]>
    <script src="js/html5shiv.min.js"></script>
    <script src="js/respond.min.js"></script>
    <![endif]-->
    <!--6、引入 bootstrap.css-->
    <link rel="stylesheet" href="${ctxStatic}/swust/css/bootstrap.min.css">
    <link rel="stylesheet" href="${ctxStatic}/swust/css/backstageBooking.min.css">
</head>
<script type="text/javascript">
    
	  function search() {		
           $.ajax({
	         type : "post",
	         url : "mobile/swust/appointment/listAll",
	         data : {abutmentCompany:abutment},
             success : function(data) {
	 	     $("#appointmentPendingTrial").empty();
		     $("#appointmentPendingTrial").html(data); 
	         } 
         });		             
	 };
 
 	  
 	 function rebut(id){
  		var myId = id;
  		var status =0;
 		 $.ajax({
 			     type : "post",
 		         url : "mobile/swust/appointment/statu",
 		         data : {id:myId,auditStatus:status},
 	             success : function(date) {
 	               alert();
 	                   
 		         }
 		  });
  	  };
 	  function pass(id){
 		var myId = id;
 		var status =1;
		 $.ajax({
			     type : "post",
		         url : "mobile/swust/appointment/statu",
		         data : {id:myId,auditStatus:status},
	             success : function() {
	                   
		         }
		  });
 	  };
 	   function  lostPass(){	
		    var i=1;
		    obj = document.getElementsByName("checkss");
		    checkVal = [];
		    for(k in obj){
		        if(obj[k].checked)		        	
		        	checkVal.push(obj[k].value);
		    }
		   $.ajax({
		         type : "post",
		         url : "$mobile/swust/appointment/checkedAll",
		         data : {"checkVal":checkVal,auditStatus:i},
		         dataType: "json",  
		        traditional: true,  
	             success : function(data) {
		 	     $("#appointmentPendingTrial").empty();
			     $("#appointmentPendingTrial").html(data); 
		      }
		  });
	   };
	   function  tabulation(){	
		   
	       var auditStatus = -1;
		   $.ajax({
		         type : "post",
		         url : "mobile/swust/appointment/tabulation",
		         data : {auditStatus:auditStatus},
		         traditional: true,  
	             success : function(data) {
		 	     $("#appointmentPendingTrial").empty();
			     $("#appointmentPendingTrial").html(data); 
		     }
		  });
	   };
	   function  pendingApproval(){	
		   debugger;
		   var auditStatus = 0;
		   $.ajax({
		         type : "post",
		         url : "mobile/swust/appointment/list",
		         data :{auditStatus:auditStatus},
		         traditional: true,  
	             success : function(data){
		 	     $("#appointmentPendingTrial").empty();
			     $("#appointmentPendingTrial").html(data); 
		     }
		  });
	   };
	 </script>
<body>
    <div class="container-fluid">
        <div class="col-sm-2 change-width hidden-xs">
            <h3 class="text-center">
                <span>审核管理员</span>
                <br>
                登录检测
            </h3>
            <div class="wrap-img">
                <img src="${ctxStatic}/swust/images/person-Logo.png" class="img-responsive">
                <span>政法学院</span>
            </div>
            <ul class="settings">
                <li>
                    <a href="#">
                        <img src="${ctxStatic}/swust/images/user-back.png" alt="">
                    </a>
                </li>
                <li>
                    <a href="#">
                        <img src="${ctxStatic}/swust/images/reset.png" alt="">
                    </a>
                </li>
                <li>
                    <a href="#">
                        <img src="${ctxStatic}/swust/images/exit-back.png" alt="">
                    </a>
                </li>
            </ul>
            <ul class="book-list">
                <li class="active">
                    <a href="#my-booking" data-toggle="tab"onclick="mybook()">
                        <span class="glyphicon glyphicon-list-alt"></span>
                        &nbsp;我的预约
                    </a>
                </li>
                <li>
                    <a href="#companyControl" data-toggle="tab" onclick="companyControler()" >
                        <span class="glyphicon glyphicon-briefcase"></span>
                        &nbsp;单位管理
                    </a>
                </li>
                <li>
                    <a href="#systempage" data-toggle="tab" onclick="systemcontroler()" >
                        <span class="glyphicon glyphicon-cog" ></span>
                        &nbsp;系统设置
                    </a>
                </li>
            </ul>
        </div>
        <div class="col-xs-12 tab-content" id="booking">
           <div id="my-booking" class="tab-pane active">
                
            </div> 
        </div>
            <!-- --------------单位管理页面开始----------------- -->
            
            <div id="companyControl" class="tab-pane">
               <!--  <div class="col-xs-12">
                    <p class="header">
                        <input type="search" placeholder="请输入搜索内容">
                        <span></span>
                        <button class="glyphicon glyphicon-search zoom"></button>
                        <button class="right-hand">新增</button>
                    </p>
                     <div class="table-responsive">
                         <table class="table table-striped">
                             <thead>
                                <tr>
                                    <th>编号</th>
                                    <th>单位名称</th>
                                    <th>单位用户名</th>
                                    <th>联系人</th>
                                    <th>联系电话</th>
                                    <th>操作</th>
                                </tr>
                             </thead>
                             <tbody>
                                <tr>
                                    <td>001</td>
                                    <td>EA Company</td>
                                    <td>CAt</td>
                                    <td>TOM</td>
                                    <td>13556464978</td>
                                    <td>
                                        <button class="check-view">编辑</button>
                                        <button class="allow reset">重置密码</button>
                                        <button class="refuse">禁用</button>
                                    </td>
                                </tr>                               
                             </tbody>
                         </table>
                         <div class="buttons text-right">
                             <button class="no-style">上一页</button>
                             <button class="active">1</button>
                             <button>2</button>
                             <button>3</button>
                             <button class="no-style">...</button>
                             <button>9</button>
                             <button class="no-style">下一页</button>
                         </div>
                     </div>
                </div> -->
            </div>
            
            <!-- --------------系统管理页面开始----------------- -->
        <div id="systempage" class="tab-pane">
           
           
           
            </div>
    </div>
    <div class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" id="check-view">
        <div class="modal-dialog modal-sm" role="document">
            <div class="modal-content" >
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span></button>
                    <h3 class="modal-title text-center">预约事由</h3>
                </div>
                <div class="modal-body">
                    <p class="text-center">${order.orderReason}</p>
                    <p class="text-center">
                        <span class="notice">备注：</span>
                        ${order.remarks}  
                    </p>
                </div>
                <div class="modal-footer text-center">
                    <button type="button" class="btn btn-md confirm" data-dismiss="modal">关闭</button>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" id="addNew">
        <div class="modal-dialog modal-md" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h3 class="modal-title text-center">新增单位</h3>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-xs-12 col-sm-6">
                            <form class="form-horizontal">
                                <div class="form-group">
                                    <label for="company" class="col-sm-4 control-label">单位名称:</label>
                                    <div class="col-sm-8">
                                        <input type="text" class="form-control" id="company">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="contact" class="col-sm-4 control-label">联系人:</label>
                                    <div class="col-sm-8">
                                        <input type="text" class="form-control" id="contact">
                                    </div>
                                </div>
                            </form>
                        </div>
                        <div class="col-xs-12 col-sm-6">
                            <form class="form-horizontal">
                                <div class="form-group">
                                    <label for="companyName" class="col-sm-4 control-label" id="input-width">单位用户名:</label>
                                    <div class="col-sm-8">
                                        <input type="text" class="form-control" id="companyName">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="phone" class="col-sm-4 control-label">联系电话:</label>
                                    <div class="col-sm-8">
                                        <input type="password" class="form-control" id="phone">
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <div class="modal-footer text-center">
                    <button type="button" class="btn btn-md confirm">确认</button>
                    <button type="button" class="btn btn-md confirm" data-dismiss="modal">取消</button>
                </div>
            </div>
        </div>
    </div>
    
<script src="${ctxStatic}/swust/js/jquery.min.js"></script>
<script src="${ctxStatic}/swust/js/bootstrap.min.js"></script>
<script src="${ctxStatic}/swust/js/backstagePlatform.js"></script>
 <script type="text/javascript">
	$(document).ready(function () {
   		/* $("#mybookinger").click(function(){
       		$.ajax({
       			type:"POST",
       			url:"mobile/swust/appointment",
       			success:function(date){
       				$("#booking").empty();
       				$("#booking").html(date);
       			}
       		});
       	}) */
       	mybook();
	});
	function systemcontroler(){
//        	$("#systemcontroler").click(function(){
           		$.ajax({
           			type:"POST",
           			url:"mobile/swust/system/system",
           			success:function(date){
           				$("#systempage").empty();
           				$("#systempage").html(date);
           			}
           		})
           	};
//           	$("#companyControler").click(function(){
	
	function companyControler(){
           		$.ajax({
           			type:"POST",
           			url:"mobile/swust/manager",
           			success:function(date){
           				$("#companyControler").empty();
           				$("#companyControler").html(date);
           			}
           		})
           	} ;
        
		function mybook(){
			$.ajax({
       			type:"POST",
       			url:"mobile/swust/appointment",
       			success:function(date){
       				$("#booking").empty();
       				$("#booking").html(date);
       			}
       		});
			}
           </script>
</body>
</html>

