<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<c:forEach items="${requestScope.photolist }" var="list">
<li><img src="${ctxStatic}/${list.photoPath}"
	class="img-responsive" onclick="changepicture('${list.id}')">
</li>
</c:forEach>
<li>
	<form id="jvForm" method="post" enctype="multipart/form-data">
		<a href="javascript:void(0);" class="file"> 选择背景 <input
			type="file" name="photo" id="choose-file" onchange="uploadPic()">
		</a>
	</form>
</li>