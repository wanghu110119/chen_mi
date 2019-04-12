<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>数据选择</title>
<meta name="decorator" content="blank" />
<style>
 ul{
 list-style: none
 }
</style>
</head>
<body>
    <div style="padding: 20px">
        <ul id="groupUl">
        <c:forEach items="${groups}" var="group">
            <c:if test="${not empty group.otherId}">
            <li><input type="checkbox" class="i-checks group"  checked="checked" groupId="${group.id }" groupName="${group.groupName }">${group.groupName }</li></c:if>
            <c:if test="${empty group.otherId}">
            <li><input type="checkbox" class="i-checks group" groupId="${group.id }" groupName="${group.groupName }">${group.groupName }</li></c:if>
        </c:forEach>
        </ul>
    </div>
	<script type="text/javascript">
        var groupUl;
        $(function(){
        	groupUl=$("#groupUl");
        });
    </script>
</body>