<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>成功管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $("#inputForm").submit();
			  return true;
		  }
		  return false;
		}
		$(document).ready(function() {
			validateForm = $("#inputForm").validate({
				rules: {
					groupName: {remote: "${ctx}/fieldconfig/fieldGroup/checkGroupName?oldGroupName=" + encodeURIComponent('${fieldGroup.groupName}')}//设置了远程验证，在初始化时必须预先调用一次。
				},
				messages: {
					groupName: {remote: "字段名称已存在"}
				},
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			
			/*
			*如果改变了分组所属角色，则清空已配置字段
			*/
			$("#groupRole").change(function(){
				$("#fieldTBody").html("");
			});
						
			$("#fieldAdd").click(function(){
				//获取已选中的id
				var ids = $.map($("#fieldTBody .fieldId"), function(n) {
					return $(n).val();
				});				
				// 正常打开	
				top.layer.open({
				    type: 2, 
				    area: ['800px', '620px'],
				    title:"选择扩展字段",
				    content: "${ctx}/fieldconfig/fieldConfig/usableList?fieldConfigIds="+ ids.join(",")+"&groupId="+$("#groupId").val()+"&groupRole="+$("#groupRole").val(),
				    btn: ['确定', '关闭'],
				    yes: function(index, layero){ //或者使用btn1
						var inputs = top.layer.getChildFrame('body', index).find("#contentTable :checked");//h.find("iframe").contents();
						$("#fieldTBody").html("");
						var cIndex = 0 ;
						$(inputs).each(function(){							
							fiedId = $(this).attr("id");
							fieldCName = $(this).parents("td").next().next().html();
							dataType = $(this).parents("td").next().next().next().html();							
							var trHtml = "<tr>"
											+"<td><input class='fieldId' name='configs["+ cIndex +"].id' value='"+ fiedId +"' type='hidden'>"+ fieldCName +"</td>"
											+"<td>"+ dataType +"</td>"
											+"<td><button type='button' class='btn btn-danger btn-xs delField' title='删除'><i class='fa fa-trash'></i> 删除</button></td>";
											+"</tr>";
							$("#fieldTBody").append(trHtml);
							cIndex++;
						});
						top.layer.close(index);
					},
					cancel: function(index){ //或者使用btn2
		    	           //按钮【按钮二】的回调
		    	       }
				}); 
			});
			//点击删除，将改行对应字段删除
			$("#fieldTBody").delegate(".delField","click",function(){
				$(this).parent().parent().remove();
			});
		});
	</script>
</head>
<body class="hideScroll">
		<form:form id="inputForm" modelAttribute="fieldGroup" action="${ctx}/fieldconfig/fieldGroup/save" method="post" class="form-horizontal">
		<form:hidden id="groupId" path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>分组英文名：</label></td>
					<td class="width-35">
						<form:input path="groupName" htmlEscape="false" maxlength="20" class="form-control required noChinese"/>
					</td>					
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>分组中文名：</label></td>
					<td class="width-35">
						<form:input path="groupCName" htmlEscape="false" maxlength="20"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>分组类型：</label></td>
					<td class="width-35">
						<form:select path="groupType" class="form-control ">
							<form:options items="${groupTypes}" itemLabel="label" itemValue="value" htmlEscape="false"/>												
						</form:select>						
					</td>					
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>所属角色：</label></td>
					<td class="width-35">
						<form:select id="groupRole" path="groupRole" class="form-control ">
							<form:options items="${groupRoles}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>	
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>分组字段：</label></td>
					<td class="width-35">
						<div class="panel panel-success">
							<div class="panel-heading">
								<button id="fieldAdd" type="button" class="btn btn-info btn-xs" title="添加"><i class="fa fa-plus"></i> 添加</button>
							</div>		
							<div class="panel-body" style="height:250px; overflow:auto;">
								<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
									<thead>
										<tr>										
											<th>字段中文名</th>
											<th>字段类型</th>											
											<th>操作</th>
										</tr>
									</thead>
									<tbody id="fieldTBody">
										<c:forEach items="${configs}" var="fieldConfig" varStatus="status">
											<tr>
												<td> <input class='fieldId' name='configs[${status.index }].id' value='${ fieldConfig.id}' type='hidden'>${fieldConfig.fieldCName }</td>				
												<td>
													${fieldConfig.dataType}
												</td>
												<td>
													<button type="button" class='btn btn-danger btn-xs delField' title='删除'><i class='fa fa-trash'></i> 删除</button>
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</div>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">备注：</label></td>
					<td class="width-35">
						<form:textarea htmlEscape="true" path="remarks" rows="5" maxlength="200" style="width:90%;" wrap="off" />						
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>