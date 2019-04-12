<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>数据选择</title>
<meta name="decorator" content="blank" />
<%@include file="/webpage/include/treeview.jsp"%>
<style>
 ul{
 list-style: none
 }
</style>
</head>
<body>
    <div style="padding: 20px">
        <ul id="postUl">
        <c:forEach items="${posts}" var="post">
            <c:if test="${not empty post.otherId}">
            <li><input type="checkbox" class="i-checks post"  checked="checked" postId="${post.id }" postName="${post.name }">${post.name }</li></c:if>
            <c:if test="${empty post.otherId}">
            <li><input type="checkbox" class="i-checks post" postId="${post.id }" postName="${post.name }">${post.name }</li></c:if>
        </c:forEach>
        </ul>
    </div>
	<script type="text/javascript">
        var postUl;
        $(function(){
        	postUl=$("#postUl");
        });
    </script>
</body>