<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
			<div class="col-xs-12">
                    <p class="header">
                        <input type="search" placeholder="请输入搜索内容">
                        <span></span>
                        <button class="glyphicon glyphicon-search zoom"></button>
                        <button class="right-hand">新增</button>
                    </p>
                     <div class="table-responsive">
                         <table class="table table-striped">
                             <thead>
                                <tr>
                                    <th>编号</th>
                                    <th>单位名称</th>
                                    <th>单位用户名</th>
                                    <th>联系人</th>
                                    <th>联系电话</th>
                                    <th>操作</th>
                                </tr>
                             </thead>
                             <tbody>
                             <c:forEach items="${ requestScope.page.list}" var="list">
                                <tr>
                                    <td>${list.code }</td>
                                    <td>${list.name }</td>
                                    <td>${list.primaryPerson.loginName }</td>
                                    <td>${list.primaryPerson.name }</td>
                                    <td>${list.primaryPerson.phone }</td>
                                    <td>
                                        <button class="edit" id="${list.id }" onclick="updateManage('${list.id }')">编辑</button>
                                        <button class="allow reset" id="resetpassword" onclick="updateManage('${list.id }')">重置密码</button>
                                        <button class="refuse">禁用</button>
                                    </td>
                                </tr>
                                </c:forEach>  
                             </tbody>
                         </table>
                         <div class="buttons text-right">
                             <button class="no-style">上一页</button>
                             <button class="active">1</button>
                             <button>2</button>
                             <button>3</button>
                             <button class="no-style">...</button>
                             <button>9</button>
                             <button class="no-style">下一页</button>
                         </div>
                         <input type="hidden" id="pwdblank" value=""/>  
                     </div>
                </div>
                
                
                
                <div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" id="edit">
        <div class="modal-dialog modal-md" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h3 class="modal-title text-center">修改信息</h3>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-xs-12 col-sm-6">
                            <form class="form-horizontal" id="formsubmit">
                            <input id="hidden" type="hidden" name="id" value=""/>
                                <div class="form-group">
                                    <label for="company1" class="col-sm-4 control-label"><span color="red">* </span>单位名称:</label>
                                    <div class="col-sm-8">
                                        <input type="text" name="name" class="form-control" id="company1">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="contact1" class="col-sm-4 control-label">联系人:</label>
                                    <div class="col-sm-8">
                                        <input type="text" name="primaryPerson.name" class="form-control" id="contact1">
                                    </div>
                                </div>
                            <!-- </form> -->
                        </div>
                        <div class="col-xs-12 col-sm-6">
                            <!-- <form class="form-horizontal"> -->
                                <div class="form-group">
                                    <label for="companyName1" class="col-sm-4 control-label input-width"><span color="red">* </span>单位用户名:</label>
                                    <div class="col-sm-8">
                                        <input type="text" name="primaryPerson.loginName" class="form-control" id="companyName1">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="phone1" class="col-sm-4 control-label">手机号码:</label>
                                    <div class="col-sm-8">
                                        <input type="password" name="phone" class="form-control" id="phone1">
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <div class="modal-footer text-center">
                    <button type="button" class="btn btn-md confirm" id="dosubmit">确认</button>
                    <button type="button" class="btn btn-md confirm" data-dismiss="modal">取消</button>
                </div>
            </div>
        </div>
    </div>
                <script src="${ctxStatic}/swust/js/backstagePlatform.js"></script>
                <script type="text/javascript">
              function resetpassword() {
            	  var id= $("#hidden").val();
            	  $.ajax({
                      type: "POST",
                      url:"${ctx}/swust/manager/resetpassword",
                      data:"id="+id,
                      success: function(data) {
                      	mybook();
                      }
                  });
				}  
                $("#dosubmit").click(function() {
            		$.ajax({
                        cache: true,
                        type: "POST",
                        url:"${ctx}/swust/manager/updateOffice",
                        data:$("#formsubmit").serialize(),// 你的formid
                        async: false,
                        
                        success: function(data) {
                        	mybook();
                        }
                    });
            	});
                 function updateManage(id) {
                	 $("#hidden").val(id);
                     }
				
                </script>