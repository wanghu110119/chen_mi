<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<nav class="navbar navbar-default navbar-fixed-top" id="nav">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#my-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a href="#" class="navbar-brand">
                成都沉迷推理探案馆预约管理系统
            </a>
        </div>
        <div id="my-collapse" class="collapse navbar-collapse">
            <!--导航栏右侧-->
            <ul class="nav navnar-nav navbar-right" id="login-head">
               <li class="pull-left">
                   <input type="search" placeholder="请输入搜索内容"><button class="glyphicon glyphicon-search search"></button>
               </li>
               <li class="pull-left">
                   <a href="#" data-toggle="dropdown" class="dropdown-toggle">
                       <img src="images/user-head.png">
                       <span>政法学院</span>
                       <span class="caret"></span>
                   </a>
                   <ul class="dropdown-menu">
                       <li>
                           <a href="#" class="modifyPwd">
                               <img src="images/modifyPwd.png" alt="">
                               &nbsp;修改密码
                           </a>
                       </li>
                       <li>
                           <a href="#">
                               <img src="images/exit.png" alt="">
                               &nbsp;注销
                           </a>
                       </li>
                   </ul>
               </li>
            </ul>
        </div>
    </div>
</nav>