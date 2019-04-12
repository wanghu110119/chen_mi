<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>分配人员</title>
	<meta name="decorator" content="blank"/>
	<%@include file="/webpage/include/treeview.jsp" %>
	<script type="text/javascript">
		
	var type="1";
	var selectedTree;
    $(function() {
    	selectedTree = $.fn.zTree.init($("#selectedTree"), setting, selectedNodes);
        $("#left a").click(function(){
            var msg = $(this).attr("aria-controls");
            if ("teacher" == msg) {
                type="1";
            } else if ("student" == msg) {
                type="2";
            }
        });
    });
    var setting = {
        data : {
            simpleData : {
                enable : true,
                idKey : "id",
                pIdKey : "pId",
                rootPId : '0'
            }
        },
        view: {selectedMulti:false,nameIsHTML:true,showTitle:false,dblClickExpand:false},
        callback : {
            onClick : treeOnClick
        }
    };

    function refreshTree() {
        $.getJSON("${ctx}/sys/office/treeData", function(data) {
            //$.fn.zTree.init($("#ztree"), setting, data).expandAll(true);
            $.fn.zTree.init($("#ztree"), setting, data);
        });
    }
    function refreshTree2() {
        $.getJSON("${ctx}/sys/office/treeData2", function(data) {
            $.fn.zTree.init($("#ztree2"), setting, data);
        });
    }
    refreshTree();
    refreshTree2();
	
		var pre_selectedNodes =[
   		        <c:forEach items="${userList}" var="user">
   		        {id:"${user.id}",
   		         pId:"0",
   		         name:"<font color='red' style='font-weight:bold;'>${user.name}</font>"},
   		        </c:forEach>];
		
		var selectedNodes =[
		        <c:forEach items="${userList}" var="user">
		        {id:"${user.id}",
		         pId:"0",
		         name:"<font color='red' style='font-weight:bold;'>${user.name}</font>"},
		        </c:forEach>];
		
		var pre_ids = "${selectIds}".split(",");
		var ids = "${selectIds}".split(",");
		
		

		//点击选择项回调
		function treeOnClick(event, treeId, treeNode, clickFlag){
			$.fn.zTree.getZTreeObj(treeId).expandNode(treeNode);
			if("ztree"==treeId){//教工
				$.get("${ctx}/auth/identityGroup/getTeacherByOffice?officeId=" + treeNode.id, function(userNodes){
					$.fn.zTree.init($("#userTree"), setting, userNodes);
				});
			}
			if("ztree2"==treeId){//学生
				$.get("${ctx}/auth/identityGroup/getStudentByOffice?officeId=" + treeNode.id, function(userNodes){
                    $.fn.zTree.init($("#userTree"), setting, userNodes);
                });
			}
			if("userTree"==treeId){
				if($.inArray(String(treeNode.id), ids)<0){
					selectedTree.addNodes(null, treeNode);
					ids.push(String(treeNode.id));
				}
				//console.log(ids);
			};
			if("selectedTree"==treeId){
				if($.inArray(String(treeNode.id), pre_ids)<0){
					selectedTree.removeNode(treeNode);
					ids.splice($.inArray(String(treeNode.id), ids), 1);
				}else{
					layer.msg('原有成员不能删除！', {
                        icon : 3,
                        time : 2000
                    });
				}
				console.log(pre_ids);
			}
		};
		function clearAssign(){
			var submit = function (v, h, f) {
			    if (v == 'ok'){
					var tips="";
					if(pre_ids.sort().toString() == ids.sort().toString()){
						tips = "未给【${group.groupName}】分配新成员！";
					}else{
						tips = "已选人员清除成功！";
					}
					ids=pre_ids.slice(0);
					selectedNodes=pre_selectedNodes;
					$.fn.zTree.init($("#selectedTree"), setting, selectedNodes);
			    	top.$.jBox.tip(tips, 'info');
			    } else if (v == 'cancel'){
			    	// 取消
			    	top.$.jBox.tip("取消清除操作！", 'info');
			    }
			    return true;
			};
			tips="确定清除=【${group.groupName}】下的已选人员？";
			top.$.jBox.confirm(tips, "清除确认", submit);
		};
	</script>
</head>
<body>
	
	<div id="assignRole" class="row" style="padding: 20px 0px; margin: 0px">
		<div class="col-sm-4" style="border-right: 1px solid #A8A8A8;">
			<ul class="nav nav-tabs" role="tablist" id="left-ul">
                            <li role="presentation" class="active"><a href="#teacher" aria-controls="teacher" role="tab" data-toggle="tab">行政单位</a></li>
                            <li role="presentation"><a href="#student" aria-controls="student" role="tab" data-toggle="tab">教学单位</a></li>
                        </ul>
                        <div class="tab-content" style="background-color: #fff;">
                            <div role="tabpanel" class="tab-pane active" id="teacher">
                                <div id="ztree" class="ztree leftBox-content"></div>
                            </div>
                            <div role="tabpanel" class="tab-pane" id="student">
                                <div id="ztree2" class="ztree leftBox-content"></div>
                            </div>
                        </div>
		</div>
		<div class="col-sm-4">
			<p>待选人员：</p>
			<div id="userTree" class="ztree"></div>
		</div>
		<div class="col-sm-4" style="padding-left:16px;border-left: 1px solid #A8A8A8;">
			<p>已选人员：</p>
			<div id="selectedTree" class="ztree"></div>
		</div>
	</div>
</body>
</html>
