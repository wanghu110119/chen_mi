<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<table class="table table-striped" id="orderList">
	<thead>
		<tr>
			<th><input type="checkbox" class="checkAll1"></th>
			<th>序号</th>
			<th>拜访人</th>
			<th>工作单位</th>
			<th>手机号码</th>
			<th>车牌号码</th>
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
		<c:choose>
		<c:when test="${row.color eq 'red'}">
			<tr>
				<td id="${row.id}"><span style="color:red;"><input type="checkbox" name="checkss"
					value="${row.id}" class="i-checks"></span></td>
				<td><span style="color:red;">${row.orderId}</span></td>
				<td><span style="color:red;">${row.orderName}</span></td>
				<td><span style="color:red;">${row.company}</span></td>
				<td><span style="color:red;">${row.orderPhone}</span></td>
				<td><span style="color:red;">${row.carNumber}</span></td>
				<td><span style="color:red;">
				<c:if test="${row.carType eq '1'}">小型汽车</c:if>
			 <c:if test="${row.carType eq '2'}">大型汽车</c:if>
			 <c:if test="${row.carType eq '3'}">超大型汽车</c:if>
			 </span>
				</td>
				<td><span style="color:red;">${row.office.name}</span></td>
				<td><span style="color:red;">${fns:formatDateTime(row.beginTime)}</span></td>
				<td><span style="color:red;">${fns:formatDateTime(row.endTime)}</span></td>
				<td><span style="color:red;">${row.accreditTime}</span></td>
				<td><span style="color:red;"><button class="check-view" onclick="reason('${row.id}')">查看</button></span></td>
				<td><c:choose>
						<c:when test="${sysOrderlist.state eq '0' }">
							<li>
								<button class="allow" onclick="rebut('${row.id}',1)">通过</button>
								<button class="refuse" onclick="rebut('${row.id}',0)">驳回</button>
								<button class="refuse" onclick="deleteOrder('${row.id}')">删除</button>
							</li>
						</c:when>
						<c:otherwise>
							<c:if test="${row.pass eq '1' or row.pass eq '3'}">
								<span style="color: green">已通过</span>
							</c:if>
							<c:if test="${row.pass eq '2'}">
								<span style="color: red">未通过</span>
							</c:if>
						</c:otherwise>
					</c:choose></td>
			</tr>
		
		</c:when>
		
		
		<c:otherwise>
			<tr>
				<td id="${row.id}"><input type="checkbox" name="checkss"
					value="${row.id}" class="i-checks"></td>
				<td>${row.orderId}</td>
				<td>${row.orderName}</td>
				<td>${row.company}</td>
				<td>${row.orderPhone}</td>
				<td>${row.carNumber}</td>
				<td>
				<c:if test="${row.carType eq '1'}">小型汽车</c:if>
			 <c:if test="${row.carType eq '2'}">大型汽车</c:if>
			 <c:if test="${row.carType eq '3'}">超大型汽车</c:if>
				</td>
				<td>${row.office.name}</td>
				<td>${fns:formatDateTime(row.beginTime)}</td>
				<td>${fns:formatDateTime(row.endTime)}</td>
				<td>${row.accreditTime}</td>
				<td><button class="check-view" onclick="reason('${row.id}')">查看</button></td>
				<td><c:choose>
						<c:when test="${sysOrderlist.state eq '0' }">
							<li>
								<button class="allow" onclick="rebut('${row.id}',1)">通过</button>
								<button class="refuse" onclick="rebut('${row.id}',0)">驳回</button>
								<button class="refuse" onclick="deleteOrder('${row.id}')">删除</button>
							</li>
						</c:when>
						<c:otherwise>
							<c:if test="${row.pass eq '1' or row.pass eq '3'}">
								<span style="color: green">已通过</span>
							</c:if>
							<c:if test="${row.pass eq '2'}">
								<span style="color: red">未通过</span>
							</c:if>
						</c:otherwise>
					</c:choose></td>
			</tr>
		
		</c:otherwise>
		</c:choose>
		</c:forEach>
		
		<tr>
		<td colspan="13">
				<div class="buttons text-right">
                    ${page}
                </div>
			</td>
		</tr>
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	</tbody>
</table>

<script src="${ctxStatic}/swust/js/backstagePlatform.js"></script>
