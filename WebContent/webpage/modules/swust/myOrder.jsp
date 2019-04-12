<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
                <div class="col-xs-12">
                    <p class="header">
                        <input type="search" placeholder="请输入搜索内容">
                        <span></span>
                        <button class="glyphicon glyphicon-search zoom"></button>
                        <button class="right-hand" onclick="">导出</button>
                    </p>
                    <ul class="state-menu">
                        <li class="active"><a href="#not-check" data-toggle="tab" onclick="pendingApproval(0)">待审核列表</a></li>
                        <li><a href="#checked" data-toggle="tab" onclick="pendingApproval(1)">已审核列表</a></li>
                        <li>
                            <button  onclick="lostRebut(1)">批量通过</button>
                            <button  onclick="lostRebut(0)">批量驳回</button>
<!--                             <button onclick="sendSmsMsg()">短信测试</button> -->
                        </li>
                    </ul>
                    <div class="tab-content">
                        <div class="table-responsive tab-pane active" id="not-check">
                            <table class="table table-striped">
                                <thead>
                                    <tr>
                                        <th>
                                            <input type="checkbox" class="checkAll1" onclick="allCheck()" id="allCheck">
                                        	编号
                                        </th>
                                        <th>来访人</th>
                                        <th>工作单位</th>
                                        <th>手机号</th>
                                        <th>车牌号</th>
                                        <th>车辆类型</th>
                                        <th>校内对接单位</th>
                                        <th>起始时间</th>
                                        <th>截止时间</th>
                                        <th>授权时限</th>
                                        <th>预约事由</th>
                                        <c:choose>
                                        	<c:when test="${sysOrderlist.state eq '0' }">
                                        	<th>操作</th>
                                        	</c:when>
                                        	<c:otherwise>
                                        	<th>审核状态</th>
                                        	</c:otherwise>
                                        </c:choose>
                                    </tr>
                                </thead>
                                <tbody class="t1">                                  
                                    <c:forEach items="${page.list}" var="row">
                     <tr>
	                       <td id="${row.id}">
	                       <input type="checkbox"  name="checkss"  value="${row.id}"class="i-checks">${row.orderId}
	                       </td>                                                                  	
                           <td>${row.orderName}</td>                        
		                   <td>${row.company}</td>
		                   <td>${row.orderPhone}</td>
		                   <td>${row.carNumber}</td>
		                   <td>
		                   <c:if test="${list.carType eq '1'}">小型汽车</c:if>
			 <c:if test="${list.carType eq '2'}">大型汽车</c:if>
			 <c:if test="${list.carType eq '3'}">超大型汽车</c:if>
		                   
		                   </td>
		                   <td>${row.office.name}</td>	
		                   <td>${row.beginTime}</td>
		                   <td>${row.endTime}</td>
		                   <td>${row.accreditTime}</td>	
		                   <td><button class="check-view" onclick="reason('${row.id}')">查看</button></td>
	                       <td >
	                       <c:choose>
                                        	<c:when test="${sysOrderlist.state eq '0' }">
                           <li>
	                           <button class="allow"  onclick="rebut('${row.id}',1)">通过</button>
                               <button class="refuse" onclick="rebut('${row.id}',0)">驳回</button>                            
                           </li >
                                        	</c:when>
                                        	<c:otherwise>
								<c:if test="${row.pass eq '1'}"><span style="color: green">已通过</span></c:if>
								<c:if test="${row.pass eq '0'}"><span style="color: red">未通过</span></c:if>
                                        	</c:otherwise>
                           </c:choose>
	                       </td>
	                  </tr>
                   </c:forEach>
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
                    </div>	
                </div>
                <script type="text/javascript">

				function allCheck() {
					$("[name=checkss]").prop("checked",$("#allCheck").prop("checked"))
				}
				
                function  lostRebut(a){	 
         		  var  obj = document.getElementsByName("checkss");
         		    for(var i=0;i<obj.length;i++){
         		        if(obj[i].checked){	        	
         		        	rebut(obj[i].value,a);}
         		    }
         		   mybook(); 
            	};

                
                function reason(id){
                    var thing=id;          
            		   $.ajax({
            		         type : "post",
            		         url : "${ctx}/swust/appointment/findRemark",
            		         data : {id:thing},
            	             success : function(data) {
            	            	 console.log(data);
            	            	 var arr = new Array();
            					 arr = data.msg.split(",");
//             					 alert(arr[0]+"++++"arr[1]);
                	             $("#orderReason").html(arr[0])
                	             $("#remarks").html(arr[1])
            	            	$("#check-view").modal("show").html();
            		         } 
            	       });
            	   };
            	   function rebut(id,state){
            	  		var myId = id;
            	 		 $.ajax({
            	 			     type : "post",
            	 		         url : "${ctx}/swust/appointment/statu",
            	 		         data : {id:myId,state:state},
            	 	             success : function() {
            	 	                mybook(); 
            	 		         }
            	 		  });
            	  	  }; 
            	  	function pendingApproval(a){
            	  		$.ajax({
                   			type:"POST",
                   			url:"${ctx}/swust/appointment",
                   			data :"state="+a,
                   			success:function(date){
                   				$("#my-booking").empty();
                   				$("#my-booking").html(date);
                   			}
                   		});
              	  	  };
                </script>
            <div class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" id="check-view">
        <div class="modal-dialog modal-sm" role="document">
            <div class="modal-content" >
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span></button>
                    <h3 class="modal-title text-center">预约事由</h3>
                </div>
                <div class="modal-body">
                    <p class="text-center" id="orderReason">${order.orderReason}</p>
                    <p class="text-center">
                        <span class="notice">备注：</span>
                        <span id="remarks"></span>  
                    </p>
                </div>
                <div class="modal-footer text-center">
                    <button type="button" class="btn btn-md confirm" data-dismiss="modal">关闭</button>
                </div>
            </div>
        </div>
    </div>
    <script>
	    function sendSmsMsg() {
			$.ajax({
		         type : "post",
		         url : "${ctx}/swust/appointment/sendSmsMsg",
	         success : function(data) {
	         	alert(data)
		      	}
		  	 });
		}
    </script>