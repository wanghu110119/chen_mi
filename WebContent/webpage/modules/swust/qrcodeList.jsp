<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<c:forEach items="${page.list }" var="list">
				<tr>
					<td id="${list.id}"><span style="color:red;"><input type="checkbox" name="checkss"
					value="${list.id}" class="i-checks"></span></td>
					<td>${list.orderReason }</td>
					<td>${list.office.name }</td>
					<td><fmt:formatDate value="${list.beginTime }" pattern="yyyy-MM-dd"/>~<fmt:formatDate value="${list.endTime }" pattern="yyyy-MM-dd "/></td>
					<td>${list.qrCodeImage }</td>
					<td>
					<input type="hidden" value="${list.qrCodeAddress }">
					<span class='text-success showQr' style="cursor: pointer;">详情</span>
					</td>
					<td>
						<input type="hidden" value="${list.id }">
						<button class="refuse" onclick="deleteQrCode('${list.id }')">删除</button>
					</td>
				</tr>
				</c:forEach>
				<tr>
				<td colspan="13">
					<div class="buttons text-right">
						${page }
			         </div>
				</td>
				</tr>