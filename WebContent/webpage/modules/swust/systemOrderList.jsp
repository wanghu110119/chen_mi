<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<table class="table table-striped" id="orderList">
	<thead>
		<tr>
			<c:choose>
             	<c:when test="${sysOrderlist.state eq '0' }">
             	<th><input type="checkbox" class="checkAll1"></th>
             	</c:when>
             	<c:otherwise>
             	<th></th>
             	</c:otherwise>
             </c:choose>
			<th>编号</th>
			<th>玩家姓名</th>
			<th>目前人数</th>
			<th>联系方式</th>
			<th>玩家来源</th>
			<th>预约房间</th>
			<th>剧本主题</th>
			<th>起始时间</th>
			<th>截止时间</th>
			<th>预估时限</th>
			<th>特殊备注</th>
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
				<td id="${row.id}"><span style="color:red;">
				<c:if test="${sysOrderlist.state eq '0' }">
							<input type="checkbox" name="checkss" value="${row.id}" class="i-checks">
				</c:if>
				</span></td>
				<td><span style="color:red;">${row.orderId}</span></td>
				<td><span style="color:red;">${row.orderName}</span></td>
				<td><span style="color:red;">${row.company}</span></td>
				<td><span style="color:red;">${row.orderPhone}</span></td>
				<td><span style="color:red;">${row.carNumber}</span></td>
				<td><span style="color:red;">
				<c:if test="${row.carType eq '1'}">恐怖主题房（6）</c:if>
			 <c:if test="${row.carType eq '2'}">古风主题房（7）</c:if>
			 <c:if test="${row.carType eq '3'}">摩洛哥主题房（8）</c:if>
			 <c:if test="${row.carType eq '4'}">不给钱，蹲客厅</c:if>
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
								<button class="allow" onclick="rebut('${row.id}',1)">已收款</button>
								<button class="refuse" onclick="rebut('${row.id}',0)">未收款</button>
								<button class="refuse" onclick="deleteOrder('${row.id}')">取订</button>
							</li>
						</c:when>
						<c:otherwise>
							<c:if test="${row.pass eq '1' or row.pass eq '3'}">
								<span style="color: green">已收款</span>
							</c:if>
							<c:if test="${row.pass eq '2'}">
								<span style="color: red">未收款</span>
							</c:if>
						</c:otherwise>
					</c:choose></td>
			</tr>
		
		</c:when>
		
		
		<c:otherwise>
			<tr>
				<td id="${row.id}">
					<c:if test="${sysOrderlist.state eq '0' }">
							<input type="checkbox" name="checkss" value="${row.id}" class="i-checks">
				</c:if>
					</td>
				<td>${row.orderId}</td>
				<td>${row.orderName}</td>
				<td>${row.company}</td>
				<td>${row.orderPhone}</td>
				<td>${row.carNumber}</td>
				<td>
				<c:if test="${row.carType eq '1'}">深邃恐怖黑（6）</c:if>
			 <c:if test="${row.carType eq '2'}">典雅复古风（7）</c:if>
			 <c:if test="${row.carType eq '3'}">激情摩洛哥（8）</c:if>
			 <c:if test="${row.carType eq '4'}">没钱蹲客厅（9+）</c:if>
				</td>
				<td>${row.office.name}</td>
				<td>${fns:formatDateTime(row.beginTime)}</td>
				<td>${fns:formatDateTime(row.endTime)}</td>
				<td>${row.accreditTime}</td>
				<td><button class="check-view" onclick="reason('${row.id}')">查看</button></td>
				<td><c:choose>
						<c:when test="${sysOrderlist.state eq '0' }">
							<li>
								<button class="allow" onclick="rebut('${row.id}',1)">收款</button>
								<button class="refuse" onclick="rebut('${row.id}',0)">未收款</button>
								<button class="refuse" onclick="deleteOrder('${row.id}')">删除</button>
							</li>
						</c:when>
						<c:otherwise>
							<c:if test="${row.pass eq '1' or row.pass eq '3'}">
								<span style="color: green">已收款</span>
							</c:if>
							<c:if test="${row.pass eq '2'}">
								<span style="color: red">未收款</span>
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
