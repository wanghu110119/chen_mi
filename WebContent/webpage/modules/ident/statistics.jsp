<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/modules/ident/statistics_top.jsp"%>
<meta name="decorator" content="default" />
<div class="auth-record">
	<div>
		<select id="joinType">
			<c:forEach items="${types}" var="type">
				<option value="${type.value}">${type.label}</option>
			</c:forEach>
		</select> <select id="joinGroup">
			<option value="">全部</option>
			<c:forEach items="${groupList}" var="group">
				<option value="${group.id}">${group.groupName}</option>
			</c:forEach>
		</select>
	</div>
	<!-- 为ECharts准备一个具备大小（宽高）的Dom -->
	<div id="join"  class="index-auth-record"></div>
</div>
<div class="auth-record">
    <div>
        <select id="outType">
            <c:forEach items="${types}" var="type">
                <option value="${type.value}">${type.label}</option>
            </c:forEach>
        </select> <select id="outGroup">
            <option value="">全部</option>
            <c:forEach items="${groupList}" var="group">
                <option value="${group.id}">${group.groupName}</option>
            </c:forEach>
        </select>
    </div>
    <!-- 为ECharts准备一个具备大小（宽高）的Dom -->
    <div id="out" class="index-auth-record"></div>
</div>

