<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>序号</th>
                                    <th>玩家姓名</th>
                                    <th>联系方式</th>
                                    <th>玩家来源</th>
                                    <th>预约房间</th>
                                    <th>起始时间</th>
                                    <th>结束时间</th>
                                    <th>授权时间</th>
                                    <th>剧本主题</th>
                                   <c:if test="${page.list[0].state eq '1'}"><th>收费金额</th></c:if> 
                                    <th>预约事由</th>
                                    <th>进行状态</th>
                                </tr>
                            </thead>
                            <tbody id="unpass">
                            <c:forEach items="${page.list}" var="list">
                            <c:choose>
                            <c:when test="${list.color eq 'red'}">
                            
                             <tr>
             <td><span style="color:red;">${list.orderId }</span></td> 
			 <td><span style="color:red;">${list.orderName }</span></td> 
			 <td><span style="color:red;">${list.orderPhone }</span></td> 
			 <td><span style="color:red;">${list.carNumber }</span></td> 
			 <td>
			 <span style="color:red;">
			 <c:if test="${list.carType eq '1'}">恐怖主题房（6）</c:if>
			 <c:if test="${list.carType eq '2'}">古风主题房（7）</c:if>
			 <c:if test="${list.carType eq '3'}">摩洛哥主题房（8）</c:if>
			 <c:if test="${list.carType eq '4'}">不给钱，蹲客厅</c:if>
			</span>
			 </td> 
			 <td><span style="color:red;"><fmt:formatDate value="${list.beginTime }" pattern="MM-dd HH:mm"/></span></td> 
			 <td><span style="color:red;"><fmt:formatDate value="${list.endTime }" pattern="MM-dd HH:mm"/></span></td> 
			 <td><span style="color:red;">${list.accreditTime }(小时)</span></td> 
			 <td><span style="color:red;">${list.office.name }</span></td> 
			 <td><span style="color:red;">
			 </td> 
			 
			 
			 <td><button class="check-view" id="${list.id }" onclick="detail('${list.id }')" value="${list.id }">查看</button></td> 
			 <td>
			 <c:choose >
			 <c:when test="${list.state eq '0'}">
				<span style="color: #EEB422;">未到达</span>
			 </c:when>
			 <c:otherwise>
			<c:if test="${list.pass eq '1'}">
				<span style="color: #3CB371;">已收费</span>
			</c:if>
			<c:if test="${list.pass eq '2'}">
				<span style="color: #EE6A50;">未收费</span>
			</c:if>
			<c:if test="${list.pass eq '3'}">
				<span style="color: black;">已完成</span>
			</c:if>
			 </c:otherwise>
			 </c:choose>
			 </td> 
        </tr>
                            
                            
                            </c:when>
                            <c:otherwise>
                             <tr>
             <td><span class="${list.orderId }">${list.orderId }</span></td> 
			 <td>${list.orderName }</td> 
			 <td>${list.orderPhone }</td> 
			 <td>${list.carNumber }</td> 
			 <td>
			 <c:if test="${list.carType eq '1'}">恐怖主题房（6）</c:if>
			 <c:if test="${list.carType eq '2'}">古风主题房（7）</c:if>
			 <c:if test="${list.carType eq '3'}">摩洛哥主题房（8）</c:if>
			 <c:if test="${list.carType eq '4'}">不给钱，蹲客厅</c:if>
			 </td> 
			 <td><fmt:formatDate value="${list.beginTime }" pattern="MM-dd HH:mm"/></td> 
			 <td><fmt:formatDate value="${list.endTime }" pattern="MM-dd HH:mm"/></td> 
			 <td>${list.accreditTime }(小时)</td> 
			 <td>${list.office.name }</td> 
			 <td><button class="check-view" id="${list.id }" onclick="detail('${list.id }')" value="${list.id }">查看</button></td> 
			 <td>
			 <c:choose >
			 <c:when test="${list.state eq '0'}">
				<span style="color: #EEB422;">未达到</span>
			 </c:when>
			 <c:otherwise>
			<c:if test="${list.pass eq '1'}">
				<span style="color: #3CB371;">已收费</span>
			</c:if>
			<c:if test="${list.pass eq '2'}">
				<span style="color: #EE6A50;">未收费</span>
			</c:if>
			<c:if test="${list.pass eq '3'}">
				<span style="color: black;">已完成</span>
			</c:if>
			 </c:otherwise>
			 </c:choose>
			 </td> 
        </tr>
                            
                            
                            </c:otherwise>
                            </c:choose>
                           
        </c:forEach>
                            </tbody>
</table>
<form action="${ctx}/swust/order/list">
	<div class='buttons text-right' style="text-align: center;">
		<button type="button" onclick="search('${sysOrderlist.state}','1')"
			class="no-style" style="border-bottom-color: red">首页</button>
		<button class="no-style"
			onclick="search('${sysOrderlist.state}','${page.pageNo-1}')"
			type="button" class="">上一页</button>
		<c:forEach begin="${page.begin }" end="${page.end }" var="p">
			<button type="button" class="btn btn-sm" id="${p}"
				onclick="search('${sysOrderlist.state}','${p}')">${p}</button>
		</c:forEach>
		<button type="button" id="" class="no-style"
			onclick="search('${sysOrderlist.state}','${page.pageNo+1}')">下一页</button>
		<button class="no-style" style="border-bottom-color: red"
				onclick="search('${sysOrderlist.state}','${page.last }')" type="button" class="">尾页</button> 
		<span>当前第${page.pageNo}页/共${page.last }页</span>
	</div>
</form>

<script type="text/javascript">
function reaserch() {
	$("#hiddenkey").val($("#reaserch").val());
	search('${sysOrderlist.state}',1);
}

	function detail(id){
// 		$.post("${ctx}/swust/order/UserSelectOrderReason?id="+id);
		$.ajax({
			type:"POST",
			url:"${ctx}/swust/order/UserSelectOrderReason",
			data:"id="+id,
			success:function(date){
				var arr = new Array();
				 arr = date.msg.split(",");
				 if(arr[0]!=null&&arr[0]!=''&&arr[0]!='null'){
				$("#order_reason").html(arr[0]);
				 }else{
					 $("#order_reason").html("");
				 }
				if(arr[1] === ''){
					arr[1]="未设置备注";
					}
				$("#remarks").html(arr[1]); 
				$(".buttons:button:nth-child(3)").addClass("active");
			}
		})
	} 




$(function () {
	  $("#nav").on("click",".search",function () {
	    $(this).siblings("input").addClass("change-long");
	  });
	  $(".state-menu").on("click","a",function () {
	    $(this).addClass("current").parent().siblings().children("a").removeClass("current")
	  });
	  $(".buttons").on("click","button",function () {
	    $(this).addClass("active").siblings().removeClass("active")
	  });
	  $("table").on("click",".check-view",function () {
	    $("#check-view").modal("show");
	  });
	//  $("button:contains('新增')").click(function () {
//	    location.href="newBooking.html";
	//  });
	  
	});
	function search(a,b) {
	if(b>'${page.last }'||b<1){
			return
		}
	if(a==="red"||a===""){
		$.ajax({
			type:"POST",
			url:"${ctx}/swust/order/list",
			data:"color="+a+"&pageNo="+b+"&name="+$("#hiddenkey").val()+"&state="+$("#statekey").val(),
			success:function(date){
				$("#not-check").empty();
				$("#not-check").html(date);
				$("#"+b).addClass("active");
			}
		});
		return
	}
	$.ajax({
		type:"POST",
		url:"${ctx}/swust/order/list",
		data:"state="+$("#statekey").val()+"&pageNo="+b+"&name="+$("#hiddenkey").val()+"&color="+$('#orderSelect option:selected').val(),
		success:function(date){
// 			$("#statekey").val(a);
			$("#not-check").empty();
			$("#not-check").html(date);
			$("#"+b).addClass("active");
		}
	});
}
	
	
	


</script>
			 
			 
			 
			 
			 
			 
			 
			 
			 
			 
			 
			 
			 
			 
			 
			 
			 