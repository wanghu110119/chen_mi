<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>数据选择</title>
<meta name="decorator" content="blank" />
<%@include file="/webpage/include/treeview.jsp"%>
</head>
<body>
    <input type="hidden" id="id" value="${id}"/>
    <input type="hidden" id="roleType" value="${roleType}"/>
	<div id="ztree" class="ztree" style="padding: 15px 20px;"></div>
	<script type="text/javascript">
    var tree;
    var setting = {
    		data : {

            },
    		async : {
                enable : true,
                url : "${ctx}/account/common/officePostTreeData",
                autoParam : [ "id" ],
                otherParam: {"userId":function(){return $("#id").val()}}
            },
            key : {
                name : 'name',
                children : 'children'
            },
        check: {
            enable: true,
            chkStyle: "checkbox",
            chkboxType: { "Y": "", "N": "" }
        },
        callback : {
            /*onClick : function(event, treeId, treeNode) {
                var id = treeNode.pId == '0' ? '' : treeNode.pId;
                
            }*/
        }
    };
    tree=$.fn.zTree.init($("#ztree"), setting);
    /*function refreshTree() {
        $.getJSON("${ctx}/account/common/roleTreeData?id="+$("#id").val()+"&roleType="+$("#roleType").val(), function(data) {
        	tree=$.fn.zTree.init($("#ztree"), setting, data);
        	tree.expandAll(true);
        });
    }
    refreshTree();*/
</script>
</body>