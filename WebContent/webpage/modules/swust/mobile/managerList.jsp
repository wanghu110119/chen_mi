<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<c:forEach items="${page.list}" var="list">
	<tr>
		<td id="${list.id}"><span style="color:red;"><input type="checkbox" name="checkss"
					value="${list.id}" class="i-checks"></span></td>
		<td>${list.code }</td>
		<td>${list.name }</td>
		<td>${list.primaryPerson.loginName }</td>
		<td>${list.primaryPerson.name }</td>
		<td>${list.primaryPerson.phone }</td>
		<td>
			<c:choose>
             	<c:when test="${list.useable eq '1' }">
             	<span class="text-success">启用</span>
             	</c:when>
             	<c:otherwise>
             	<span class="text-danger">禁用</span>
             	</c:otherwise>
             </c:choose>
		</td>
		<td>
			<button class="edit" id="${list.id }"
				onclick="updateManage('${list.id }')">编辑</button>
			<button class="allow reset"
				onclick="resetPsw('${list.primaryPerson.loginName }')">重置密码</button>
			<c:choose>
             	<c:when test="${list.useable eq '1' }">
             	<button class="refuse" onclick="isDisable('${list.id }', '1')">禁用</button>
             	</c:when>
             	<c:otherwise>
             	<button class="allow" onclick="isDisable('${list.id }', '2')">启用</button>
             	</c:otherwise>
             </c:choose>
             <button class="refuse" onclick="isDisable('${list.id }', '3')">删除</button>
		</td>
	</tr>
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