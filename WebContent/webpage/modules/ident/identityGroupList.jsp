]<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <title>身份组管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
    function refresh(){//刷新或者排序，页码不清零
        window.location="${ctx}/auth/identityGroup/list";
    }
    </script>
</head>
<body class="gray-bg">
    <div class="wrapper wrapper-content">
    <div class="ibox">
    <div class="ibox-title">
            <h5>组列表 </h5>
            <div class="ibox-tools">
                <a class="collapse-link">
                    <i class="fa fa-chevron-up"></i>
                </a>
            </div>
    </div>
    <div class="ibox-content">
    <sys:message content="${message}"/>
    
        <!-- 查询条件 -->
<!--     <div class="row"> -->
<!--     <div class="col-sm-12"> -->
<%--     <form:form id="searchForm" modelAttribute="role" action="${ctx}/auth/identity/" method="post" class="form-inline"> --%>
<%--         <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/> --%>
<%--         <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/> --%>
<%--         <table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 --> --%>
<!--         <div class="form-group"> -->
<!--             <span>组名：</span> -->
<%--                 <form:input path="groupName" maxlength="64"  class=" form-control input-sm"/> --%>
<!--          </div>  -->
<%--     </form:form> --%>
<!--     <br/> -->
<!--     </div> -->
<!--     </div> -->
    
        <!-- 工具栏 -->
    <div class="row">
    <div class="col-sm-12">
        <div class="pull-left">
<%--             <shiro:hasPermission name="sys:role:add"> --%>
                <table:addRow url="${ctx}/auth/identityGroup/form"  title="用户组"></table:addRow><!-- 增加按钮 -->
<%--             </shiro:hasPermission> --%>
<%--             <shiro:hasPermission name="sys:role:edit"> --%>
                <table:editRow url="${ctx}/auth/identityGroup/form" id="contentTable" title="用户组"></table:editRow><!-- 编辑按钮 -->
<%--             </shiro:hasPermission> --%>
<%--             <shiro:hasPermission name="sys:role:del"> --%>
                <table:delRow url="${ctx}/auth/identityGroup/deleteAll" id="contentTable" ></table:delRow><!-- 删除按钮 -->
<%--             </shiro:hasPermission> --%>
           <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
        
            </div>
<!--         <div class="pull-right"> -->
<!--             <button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button> -->
<!--             <button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button> -->
<!--         </div> -->
    </div>
    </div>
    
    <table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
        <thead>
            <tr>
                <th><input type="checkbox" class="i-checks"></th>
                <th>组名</th>
                <th>组描述</th>
<%--                 <shiro:hasPermission name="sys:identityGroup:edit"> --%>
                    <th>操作</th>
<%--                 </shiro:hasPermission> --%>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${list}" var="obj">
                <tr>
                    <td> <input type="checkbox" id="${obj.id}" class="i-checks"></td>
                    <td><a  href="#" onclick="openDialogView('查看身份组', '${ctx}/auth/identityGroup/form?id=${obj.id}','800px', '500px')">${obj.groupName}</a></td>
                    <td><a  href="#" onclick="openDialogView('查看身份组', '${ctx}/auth/identityGroup/form?id=${obj.id}','800px', '500px')">${obj.description}</a></td>
                    <td>
<%--                         <shiro:hasPermission name="sys:role:view"> --%>
                        <a href="#" onclick="openDialogView('查看身份组', '${ctx}/auth/identityGroup/form?id=${obj.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
<%--                         </shiro:hasPermission> --%>
<%--                         <shiro:hasPermission name="sys:role:edit">  --%>
                        <c:if test="${(role.sysData eq fns:getDictValue('是', 'yes_no', '1') && fns:getUser().admin)||!(role.sysData eq fns:getDictValue('是', 'yes_no', '1'))}">
                            <a href="#" onclick="openDialog('修改身份组', '${ctx}/auth/identityGroup/form?id=${obj.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
                        </c:if>
<%--                         </shiro:hasPermission> --%>
<%--                         <shiro:hasPermission name="sys:role:del">  --%>
                        <a href="${ctx}/auth/identityGroup/delete?id=${obj.id}" onclick="return confirmx('确认要删除该身份组吗？', this.href)" class="btn  btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
<%--                         </shiro:hasPermission> --%>
<%--                         <shiro:hasPermission name="sys:role:assign">  --%>
                        <a href="#" onclick="openDialogView('组内成员', '${ctx}/auth/identityGroup/assign?id=${obj.id}','800px', '600px')"  class="btn  btn-warning btn-xs" ><i class="glyphicon glyphicon-plus"></i> 组内成员</a>
<%--                         </shiro:hasPermission> --%>
                        </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    </div>
    </div>
    </div>
</body>
</html>