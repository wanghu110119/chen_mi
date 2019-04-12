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
	<div id="ztree" class="ztree" style="padding: 15px 20px;"></div>
	<script type="text/javascript">
    var tree;
    var setting = {
        data : {
            simpleData : {
                enable : true,
                idKey : "id",
                pIdKey : "pId",
                rootPId : '0'
            }
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

    function refreshTree() {
        $.getJSON("${ctx}/sys/office/postTreeData?id="+$("#id").val(), function(data) {
        	tree=$.fn.zTree.init($("#ztree"), setting, data);
        	tree.expandAll(true);
        });
    }
    refreshTree();
</script>
</body>