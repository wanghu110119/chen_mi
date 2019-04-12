<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>业务表管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var validateForm;
		function doSubmit() {
			return validateForm.form() ? ($("#inputForm").submit(), !0) : !1
		};
		$(document).ready(function () {
			validateForm = $("#inputForm").validate({
					ignore: "",
					submitHandler: function (a) {
						loading("正在提交，请稍等...");
						$("input[type=checkbox]").each(function () {
							$(this).after('<input type="hidden" name="' + $(this).attr("name") + '" value="' + ($(this).attr("checked") ? "1" : "0") + '"/>');
							$(this).attr("name", "_" + $(this).attr("name"))
						});
						a.submit()
					},
					errorContainer: "#messageBox",
					errorPlacement: function (a, b) {
						$("#messageBox").text("输入有误，请先更正。");
						b.is(":checkbox") || b.is(":radio") || b.parent().is(".input-append") ? a.appendTo(b.parent().parent()) : a.insertAfter(b)
					}
				});
			resetColumnNo();
			$("#tableType").change(function () {
				"3" == $("#tableType").val() ? addForTreeTable() : removeForTreeTable()
			});
			var b,
			c;
			$("#contentTable1").tableDnD({
				onDragClass: "myDragClass",
				onDrop: function (a, d) {
					c = $(d).index();
					var f = $("#tab-2 #contentTable2 tbody tr:eq(" + c + ")"),
					e = $("#tab-2 #contentTable2 tbody tr:eq(" + b + ")");
					b < c ? e.insertAfter(f) : e.insertBefore(f);
					f = $("#tab-3 #contentTable3 tbody tr:eq(" + c + ")");
					e = $("#tab-3 #contentTable3 tbody tr:eq(" + b + ")");
					b < c ? e.insertAfter(f) : e.insertBefore(f);
					f = $("#tab-4 #contentTable4 tbody tr:eq(" + c + ")");
					e = $("#tab-4 #contentTable4 tbody tr:eq(" + b + ")");
					b < c ? e.insertAfter(f) : e.insertBefore(f);
					resetColumnNo()
				},
				onDragStart: function (a, c) {
					b = $(c).index()
				}
			})
		});
		function resetColumnNo() {
			$("#tab-4 #contentTable4 tbody tr").each(function (b, c) {
				$(this).find("span[name*=columnList],select[name*=columnList],input[name*=columnList]").each(function () {
					var a = $(this).attr("name"),
					c = a.split(".")[1],
					c = "columnList[" + b + "]." + c;
					$(this).attr("name", c);
					0 <= a.indexOf(".sort") && ($(this).val(b), $(this).next().text(b))
				});
				$(this).find("label[id*=columnList]").each(function () {
					var a = $(this).attr("id").split(".")[1],
					a = "columnList[" + b + "]." + a;
					$(this).attr("id", a);
					$(this).attr("for", "columnList[" + b + "].jdbcType")
				});
				$(this).find("input[name*=name]").each(function () {
					var a = $(this).attr("name").split(".")[1],
					a = "page-columnList[" + b + "]." + a;
					$(this).attr("name", a)
				});
				$(this).find("input[name*=comments]").each(function () {
					var a = $(this).attr("name").split(".")[1],
					a = "page-columnList[" + b + "]." + a;
					$(this).attr("name", a)
				})
			});
			$("#tab-3 #contentTable3 tbody tr").each(function (b, c) {
				$(this).find("span[name*=columnList],select[name*=columnList],input[name*=columnList]").each(function () {
					var a = $(this).attr("name"),
					c = a.split(".")[1],
					c = "columnList[" + b + "]." + c;
					$(this).attr("name", c);
					0 <= a.indexOf(".sort") && ($(this).val(b), $(this).next().text(b))
				});
				$(this).find("label[id*=columnList]").each(function () {
					var a = $(this).attr("id").split(".")[1],
					a = "columnList[" + b + "]." + a;
					$(this).attr("id", a);
					$(this).attr("for", "columnList[" + b + "].jdbcType")
				});
				$(this).find("input[name*=name]").each(function () {
					var a = $(this).attr("name").split(".")[1],
					a = "page-columnList[" + b + "]." + a;
					$(this).attr("name", a)
				});
				$(this).find("input[name*=comments]").each(function () {
					var a = $(this).attr("name").split(".")[1],
					a = "page-columnList[" + b + "]." + a;
					$(this).attr("name", a)
				})
			});
			$("#tab-2 #contentTable2 tbody tr").each(function (b, c) {
				$(this).find("span[name*=columnList],select[name*=columnList],input[name*=columnList]").each(function () {
					var a = $(this).attr("name"),
					c = a.split(".")[1],
					c = "columnList[" + b + "]." + c;
					$(this).attr("name", c);
					0 <= a.indexOf(".sort") && ($(this).val(b), $(this).next().text(b))
				});
				$(this).find("label[id*=columnList]").each(function () {
					var a = $(this).attr("id").split(".")[1],
					a = "columnList[" + b + "]." + a;
					$(this).attr("id", a);
					$(this).attr("for", "columnList[" + b + "].jdbcType")
				});
				$(this).find("input[name*=name]").each(function () {
					var a = $(this).attr("name").split(".")[1],
					a = "page-columnList[" + b + "]." + a;
					$(this).attr("name", a)
				});
				$(this).find("input[name*=comments]").each(function () {
					var a = $(this).attr("name").split(".")[1],
					a = "page-columnList[" + b + "]." + a;
					$(this).attr("name", a)
				})
			});
			$("#tab-1 #contentTable1 tbody tr").each(function (b, c) {
				$(this).find("span[name*=columnList],select[name*=columnList],input[name*=columnList]").each(function () {
					var a = $(this).attr("name"),
					c = a.split(".")[1],
					c = "columnList[" + b + "]." + c;
					$(this).attr("name", c);
					0 <= a.indexOf(".sort") && ($(this).val(b), $(this).next().text(b))
				});
				$(this).find("label[id*=columnList]").each(function () {
					var a = $(this).attr("id").split(".")[1],
					a = "columnList[" + b + "]." + a;
					$(this).attr("id", a);
					$(this).attr("for", "columnList[" + b + "].jdbcType")
				});
				$(this).find("input[name*=name]").change(function () {
					var a = "page-" + $(this).attr("name");
					$("#tab-2 #contentTable2 tbody tr input[name='" + a + "']").val($(this).val());
					$("#tab-3 #contentTable3 tbody tr input[name='" + a + "']").val($(this).val());
					$("#tab-4 #contentTable4 tbody tr input[name='" + a + "']").val($(this).val())
				});
				$(this).find("input[name*=comments]").change(function () {
					var a = "page-" + $(this).attr("name");
					$("#tab-2 #contentTable2 tbody tr input[name='" + a + "']").val($(this).val());
					$("#tab-3 #contentTable3 tbody tr input[name='" + a + "']").val($(this).val());
					$("#tab-4 #contentTable4 tbody tr input[name='" + a + "']").val($(this).val())
				})
			});
			$("#contentTable2 tbody tr select[name*=javaType]").change(function () {
				var b = $(this).children("option:selected").val(),
				c = $(this);
				if ("Custom" == b || "newadd" == $(this).children("option:selected").attr("class"))
					top.layer.open({
						type: 1,
						title: "输入自定义java对象",
						area: ["600px", "360px"],
						shadeClose: !0,
						content: '<div class="wrapper wrapper-content"><div class="col-md-12"><div class="form-group"> <label class="col-sm-3 control-label">包名：</label> <div class="col-sm-9"> <input type="text" id="packagePath" name="" class="form-control required" placeholder="请输入自定义对象所在的包路径"> <span class="help-block m-b-none">必须是存在的package</span> </div> </div> <div class="form-group"> <label class="col-sm-3 control-label">类名：</label> <div class="col-sm-9"> <input type="text" id="className" name="" class="form-control required" placeholder="请输入自定义对象的类名"> <span class="help-block m-b-none">必须是存在的class对象</span> </div> </div></div></div>',
						btn: ["确定", "关闭"],
						yes: function (a, b) {
							var f = top.$("#packagePath").val(),
							e = top.$("#className").val(),
							g = f + "." + e;
							top.$("<option>").val(g).text(e);
							"" == e.trim() || "" == f.trim() ? top.layer.alert("包名和类名都不允许为空!", {
								icon: 0
							}) : (c.children("option:selected").text(e), c.children("option:selected").val(g), c.children("option:selected").attr("class", "newadd"), top.layer.close(a))
						},
						cancel: function (a) {}
					}), "Custom" != b && "newadd" == $(this).children("option:selected").attr("class") && (top.$("#packagePath").val($(this).children("option:selected").val().substring(0, $(this).children("option:selected").val().lastIndexOf("."))), top.$("#className").val($(this).children("option:selected").text()))
			})
		};
		function addColumn() {
			var b = $("#template1").clone();
			b.removeAttr("style");
			b.removeAttr("id");
			var c = $("#template2").clone();
			c.removeAttr("style");
			c.removeAttr("id");
			var a = $("#template3").clone();
			a.removeAttr("style");
			a.removeAttr("id");
			var d = $("#template4").clone();
			d.removeAttr("style");
			d.removeAttr("id");
			$("#tab-1 #contentTable1 tbody").append(b);
			$("#tab-2 #contentTable2 tbody").append(c);
			$("#tab-3 #contentTable3 tbody").append(a);
			$("#tab-4 #contentTable4 tbody").append(d);
			b.find("input:checkbox").iCheck({
				checkboxClass: "icheckbox_square-green",
				radioClass: "iradio_square-blue",
				increaseArea: "20%"
			});
			c.find("input:checkbox").iCheck({
				checkboxClass: "icheckbox_square-green",
				radioClass: "iradio_square-blue",
				increaseArea: "20%"
			});
			a.find("input:checkbox").iCheck({
				checkboxClass: "icheckbox_square-green",
				radioClass: "iradio_square-blue",
				increaseArea: "20%"
			});
			d.find("input:checkbox").iCheck({
				checkboxClass: "icheckbox_square-green",
				radioClass: "iradio_square-blue",
				increaseArea: "20%"
			});
			resetColumnNo();
			$("#contentTable1").tableDnD({
				onDragClass: "myDragClass",
				onDrop: function (a, b) {
					toIndex = $(b).index();
					var c = $("#tab-2 #contentTable2 tbody tr:eq(" + toIndex + ")"),
					d = $("#tab-2 #contentTable2 tbody tr:eq(" + fromIndex + ")");
					fromIndex < toIndex ? d.insertAfter(c) : d.insertBefore(c);
					c = $("#tab-3 #contentTable3 tbody tr:eq(" + toIndex + ")");
					d = $("#tab-3 #contentTable3 tbody tr:eq(" + fromIndex + ")");
					fromIndex < toIndex ? d.insertAfter(c) : d.insertBefore(c);
					c = $("#tab-4 #contentTable4 tbody tr:eq(" + toIndex + ")");
					d = $("#tab-4 #contentTable4 tbody tr:eq(" + fromIndex + ")");
					fromIndex < toIndex ? d.insertAfter(c) : d.insertBefore(c);
					resetColumnNo()
				},
				onDragStart: function (a, b) {
					fromIndex = $(b).index()
				}
			});
			return !1
		};
		function removeForTreeTable() {
			$("#tab-1 #contentTable1 tbody").find("#tree_11,#tree_12,#tree_13,#tree_14").remove();
			$("#tab-2 #contentTable2 tbody").find("#tree_21,#tree_22,#tree_23,#tree_24").remove();
			$("#tab-3 #contentTable3 tbody").find("#tree_31,#tree_32,#tree_33,#tree_34").remove();
			$("#tab-4 #contentTable4 tbody").find("#tree_41,#tree_42,#tree_43,#tree_44").remove();
			resetColumnNo();
			return !1
		};
		function addForTreeTable() {
			if (!$("#tab-1 #contentTable1 tbody").find("input[name*=name][value=parent_id]").val()) {
				var b = $("#template1").clone();
				b.removeAttr("style");
				b.attr("id", "tree_11");
				b.find("input[name*=name]").val("parent_id");
				b.find("input[name*=comments]").val("父级编号");
				b.find("span[name*=jdbcType]").val("varchar(64)");
				var c = $("#template2").clone();
				c.removeAttr("style");
				c.attr("id", "tree_21");
				c.find("input[name*=name]").val("parent_id");
				c.find("select[name*=javaType]").val("This");
				c.find("input[name*=javaField]").val("parent.id|name");
				c.find("input[name*=isList]").removeAttr("checked");
				c.find("select[name*=showType]").val("treeselect");
				var a = $("#template3").clone();
				a.removeAttr("style");
				a.attr("id", "tree_31");
				a.find("input[name*=name]").val("parent_id");
				var d = $("#template4").clone();
				d.removeAttr("style");
				d.attr("id", "tree_41");
				d.find("input[name*=name]").val("parent_id");
				d.find("input[name*=isNull]").removeAttr("checked");
				$("#tab-1 #contentTable1 tbody").append(b);
				$("#tab-2 #contentTable2 tbody").append(c);
				$("#tab-3 #contentTable3 tbody").append(a);
				$("#tab-4 #contentTable4 tbody").append(d);
				b.find("input:checkbox").iCheck({
					checkboxClass: "icheckbox_square-green",
					radioClass: "iradio_square-blue",
					increaseArea: "20%"
				});
				c.find("input:checkbox").iCheck({
					checkboxClass: "icheckbox_square-green",
					radioClass: "iradio_square-blue",
					increaseArea: "20%"
				});
				a.find("input:checkbox").iCheck({
					checkboxClass: "icheckbox_square-green",
					radioClass: "iradio_square-blue",
					increaseArea: "20%"
				});
				d.find("input:checkbox").iCheck({
					checkboxClass: "icheckbox_square-green",
					radioClass: "iradio_square-blue",
					increaseArea: "20%"
				})
			};
			$("#tab-1 #contentTable1 tbody").find("input[name*=name][value=parent_ids]").val() || (b = $("#template1").clone(), b.removeAttr("style"), b.attr("id", "tree_12"), b.find("input[name*=name]").val("parent_ids"), b.find("input[name*=comments]").val("\u6240\u6709\u7236\u7ea7\u7f16\u53f7"), b.find("span[name*=jdbcType]").val("varchar(2000)"), c = $("#template2").clone(), c.removeAttr("style"), c.attr("id", "tree_22"), c.find("input[name*=name]").val("parent_ids"), c.find("select[name*=javaType]").val("String"), c.find("input[name*=javaField]").val("parentIds"), c.find("select[name*=queryType]").val("like"), c.find("input[name*=isList]").removeAttr("checked"), a = $("#template3").clone(), a.removeAttr("style"), a.attr("id", "tree_32"), a.find("input[name*=name]").val("parent_ids"), d = $("#template4").clone(), d.removeAttr("style"), d.attr("id", "tree_42"), d.find("input[name*=name]").val("parent_ids"), d.find("input[name*=isNull]").removeAttr("checked"), $("#tab-1 #contentTable1 tbody").append(b), $("#tab-2 #contentTable2 tbody").append(c), $("#tab-3 #contentTable3 tbody").append(a), $("#tab-4 #contentTable4 tbody").append(d), b.find("input:checkbox").iCheck({
					checkboxClass: "icheckbox_square-green",
					radioClass: "iradio_square-blue",
					increaseArea: "20%"
				}), c.find("input:checkbox").iCheck({
					checkboxClass: "icheckbox_square-green",
					radioClass: "iradio_square-blue",
					increaseArea: "20%"
				}), a.find("input:checkbox").iCheck({
					checkboxClass: "icheckbox_square-green",
					radioClass: "iradio_square-blue",
					increaseArea: "20%"
				}), d.find("input:checkbox").iCheck({
					checkboxClass: "icheckbox_square-green",
					radioClass: "iradio_square-blue",
					increaseArea: "20%"
				}));
			$("#tab-1 #contentTable1 tbody").find("input[name*=name][value=name]").val() || (b = $("#template1").clone(), b.removeAttr("style"), b.attr("id", "tree_13"), b.find("input[name*=name]").val("name"), b.find("input[name*=comments]").val("\u540d\u79f0"), b.find("span[name*=jdbcType]").val("varchar(100)"), c = $("#template2").clone(), c.removeAttr("style"), c.attr("id", "tree_23"), c.find("input[name*=name]").val("name"), c.find("select[name*=javaType]").val("String"), c.find("input[name*=javaField]").val("name"), c.find("input[name*=isQuery]").attr("checked", "checked"), c.find("select[name*=queryType]").val("like"), a = $("#template3").clone(), a.removeAttr("style"), a.attr("id", "tree_33"), a.find("input[name*=name]").val("name"), d = $("#template4").clone(), d.removeAttr("style"), d.attr("id", "tree_43"), d.find("input[name*=name]").val("name"), d.find("input[name*=isNull]").removeAttr("checked"), $("#tab-1 #contentTable1 tbody").append(b), $("#tab-2 #contentTable2 tbody").append(c), $("#tab-3 #contentTable3 tbody").append(a), $("#tab-4 #contentTable4 tbody").append(d), b.find("input:checkbox").iCheck({
					checkboxClass: "icheckbox_square-green",
					radioClass: "iradio_square-blue",
					increaseArea: "20%"
				}), c.find("input:checkbox").iCheck({
					checkboxClass: "icheckbox_square-green",
					radioClass: "iradio_square-blue",
					increaseArea: "20%"
				}), a.find("input:checkbox").iCheck({
					checkboxClass: "icheckbox_square-green",
					radioClass: "iradio_square-blue",
					increaseArea: "20%"
				}), d.find("input:checkbox").iCheck({
					checkboxClass: "icheckbox_square-green",
					radioClass: "iradio_square-blue",
					increaseArea: "20%"
				}));
			$("#tab-1 #contentTable1 tbody").find("input[name*=name][value=sort]").val() || (b = $("#template1").clone(), b.removeAttr("style"), b.attr("id", "tree_14"), b.find("input[name*=name]").val("sort"), b.find("input[name*=comments]").val("\u6392\u5e8f"), b.find("span[name*=jdbcType]").val("decimal(10,0)"), c = $("#template2").clone(), c.removeAttr("style"), c.attr("id", "tree_24"), c.find("input[name*=name]").val("sort"), c.find("select[name*=javaType]").val("Integer"), c.find("input[name*=javaField]").val("sort"), c.find("input[name*=isList]").removeAttr("checked"), a = $("#template3").clone(), a.removeAttr("style"), a.attr("id", "tree_34"), a.find("input[name*=name]").val("sort"), d = $("#template4").clone(), d.removeAttr("style"), d.attr("id", "tree_44"), d.find("input[name*=name]").val("sort"), d.find("input[name*=isNull]").removeAttr("checked"), $("#tab-1 #contentTable1 tbody").append(b), $("#tab-2 #contentTable2 tbody").append(c), $("#tab-3 #contentTable3 tbody").append(a), $("#tab-4 #contentTable4 tbody").append(d), b.find("input:checkbox").iCheck({
					checkboxClass: "icheckbox_square-green",
					radioClass: "iradio_square-blue",
					increaseArea: "20%"
				}), c.find("input:checkbox").iCheck({
					checkboxClass: "icheckbox_square-green",
					radioClass: "iradio_square-blue",
					increaseArea: "20%"
				}), a.find("input:checkbox").iCheck({
					checkboxClass: "icheckbox_square-green",
					radioClass: "iradio_square-blue",
					increaseArea: "20%"
				}), d.find("input:checkbox").iCheck({
					checkboxClass: "icheckbox_square-green",
					radioClass: "iradio_square-blue",
					increaseArea: "20%"
				}));
			resetColumnNo();
			return !1
		};
		function delColumn() {
			$("input[name='ck']:checked").closest("tr").each(function () {
				var b = $(this).find("input[name*=name]").attr("name");
				$(this).remove();
				$("#tab-2 #contentTable2 tbody tr input[name='page-" + b + "']").closest("tr").remove();
				$("#tab-3 #contentTable3 tbody tr input[name='page-" + b + "']").closest("tr").remove();
				$("#tab-4 #contentTable4 tbody tr input[name='page-" + b + "']").closest("tr").remove()
			});
			resetColumnNo();
			return !1
		};
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"></li>
	</ul>
		<table style="display:none">
			<tbody>
			<tr id="template1" style="display:none">
				<td>
					<input type="hidden" name="columnList[0].sort" value="0" maxlength="200" class="form-control required   digits">
					<label>0</label>
					<input type="hidden" class="form-control" name="columnList[0].isInsert" value="1">
					<input type="hidden" class="form-control" name="columnList[0].isEdit" value="1">
				</td>
				<td>
					<input type="checkbox" class="form-control" name="ck" value="1">
				</td>
				<td>
					<input type="text" class="form-control" name="columnList[0].name" value="">
				</td>
				<td>
					<input type="text" class="form-control" name="columnList[0].comments" value="" maxlength="200">
				</td>
				<td>
					<select name="columnList[${vs.index}].jdbcType" class="form-control required m-b" aria-required="true">
						<c:forEach items="${config.jdbcTypeList}" var="dict">
							<option value="${dict.value}" title="${dict.description}">${dict.label}</option>
						</c:forEach>
					</select>
				</td>
				<td>
					<input type="checkbox" class="form-control" name="columnList[0].isPk" value="1">
				</td>
			</tr>
			<tr id="template2" style="display:none">
				<td>
					<input type="text" class="form-control" readonly="readonly" name="page-columnList[0].name" value="">
				</td>
				<td>
					<input type="text" class="form-control" name="page-columnList[0].comments" value="" maxlength="200" readonly="readonly">
				</td>
				<td>
					<select name="columnList[${vs.index}].javaType" class="form-control required m-b" aria-required="true">
						<c:forEach items="${config.javaTypeList}" var="dict">
							<option value="${dict.value}" title="${dict.description}">${dict.label}</option>
						</c:forEach>
					</select>
				</td>
				<td>
					<input type="text" name="columnList[0].javaField" value="" maxlength="200" class="form-control required ">
				</td>
				<td>
					<input type="checkbox" class="form-control  " name="columnList[0].isForm" value="1" checked="">
				</td>
				<td>
					<input type="checkbox" class="form-control  " name="columnList[0].isList" value="1" checked="">
				</td>
				<td>
					<input type="checkbox" class="form-control  " name="columnList[0].isQuery" value="1">
				</td>
				<td>
					<select name="columnList[${vs.index}].queryType" class="form-control required m-b" aria-required="true">
						<c:forEach items="${config.queryTypeList}" var="dict">
							<option value="${dict.value}" title="${dict.description}">${dict.label}</option>
						</c:forEach>
					</select>
				</td>
				<td>
					<select name="columnList[${vs.index}].showType" class="form-control required m-b" aria-required="true">
						<c:forEach items="${config.showTypeList}" var="dict">
							<option value="${dict.value}" title="${dict.description}">${dict.label}</option>
						</c:forEach>
					</select>
				</td>
				<td>
					<input type="text" name="columnList[0].dictType" value="" maxlength="200" class="form-control   ">
				</td>
			</tr>
			<tr id="template3" style="display:none">
				<td>
					<input type="text" class="form-control" readonly="readonly" name="page-columnList[0].name" value="">
				</td>
				<td>
					<input type="text" class="form-control" name="page-columnList[0].comments" value="" maxlength="200" readonly="readonly">
				</td>
				<td>
					<input type="text" name="columnList[0].tableName" value="" maxlength="200" class="form-control  ">
				</td>
				<td>
					<input type="text" name="columnList[0].fieldLabels" value="" maxlength="200" class="form-control  ">
				</td>
				<td>
					<input type="text" name="columnList[0].fieldKeys" value="" maxlength="200" class="form-control  ">
				</td>
				<td>
					<input type="text" name="columnList[0].searchLabel" value="" maxlength="200" class="form-control  ">
				</td>
				<td>
					<input type="text" name="columnList[0].searchKey" value="" maxlength="200" class="form-control  ">
				</td>
				
			</tr>
			
			<tr id="template4" style="display:none">
				<td>
					<input type="text" class="form-control" readonly="readonly" name="page-columnList[0].name" value="">
				</td>
				<td>
					<input type="text" class="form-control" name="page-columnList[0].comments" value="" maxlength="200" readonly="readonly">
				</td>
				<td>
					<input type="checkbox" class="form-control " name="columnList[0].isNull" value="1" checked="">
				</td>
				<td>
					<select name="columnList[${vs.index}].validateType" class="form-control m-b">
						<c:forEach items="${config.validateTypeList}" var="dict">
							<option value="${dict.value}" title="${dict.description}">${dict.label}</option>
						</c:forEach>
					</select>
				</td>
				<td>
					<input type="text" name="columnList[0].minLength" value="" maxlength="200" class="form-control  ">
				</td>
				<td>
					<input type="text" name="columnList[0].maxLength" value="" maxlength="200" class="form-control  ">
				</td>
				<td>
					<input type="text" name="columnList[0].minValue" value="" maxlength="200" class="form-control  ">
				</td>
				<td>
					<input type="text" name="columnList[0].maxValue" value="" maxlength="200" class="form-control  ">
				</td>
			</tr>
		</tbody>
	</table>
	<form:form id="inputForm" modelAttribute="genTable" action="${ctx}/gen/genTable/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="isSync"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>表名:</label></td>
					<td class="width-35">
						<form:input path="name" htmlEscape="false" maxlength="200" class="form-control required" aria-required="true"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>说明:</label></td>
					<td class="width-35">
						<form:input path="comments" htmlEscape="false" maxlength="200" class="form-control required" aria-required="true"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">表类型</label></td>
					<td class="width-35">
						<select id="tableType" name="tableType" class="form-control m-b">
							<option value="0">单表</option><option value="1">主表</option><option value="2">附表</option><option value="3">树结构表</option>
						</select>
						<span class="help-inline">如果是附表，请指定主表表名和当前表的外键</span>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>类名:</label></td>
					<td class="width-35">
						<form:input path="className" htmlEscape="false" maxlength="200" class="form-control required" aria-required="true"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">主表表名:</label></td>
					<td class="width-35">
						<form:select path="parentTable" cssClass="form-control m-b">
							<form:option value="">无</form:option>
							<form:options items="${tableList}" itemLabel="nameAndComments" itemValue="name" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">当前表外键：</label></td>
					<td class="width-35">
						<form:input path="parentTableFk" htmlEscape="false" maxlength="200" class="form-control" aria-required="true"/>
					</td>
				</tr>
			</tbody>
		</table>
		<button class="btn btn-white btn-sm" onclick="return addColumn()"><i class="fa fa-plus"> 增加</i></button>
		<button class="btn btn-white btn-sm" onclick="return delColumn()"><i class="fa fa-minus"> 删除</i> </button>
		<br>
		<div class="tabs-container">
		<ul class="nav nav-tabs">
			<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true"> 数据库属性</a>
			</li>
			<li class=""><a data-toggle="tab" href="#tab-2" aria-expanded="false">页面属性</a>
			</li>
			<li class=""><a data-toggle="tab" href="#tab-4" aria-expanded="false">页面校验</a>
			</li>
			<li class=""><a data-toggle="tab" href="#tab-3" aria-expanded="false">grid选择框（自定义java对象）</a>
			</li>
		</ul>
		<div class="tab-content">
		<div id="tab-1" class="tab-pane active">
		<div class="panel-body">
			<table id="contentTable1" class="table table-striped table-bordered table-hover  dataTables-example dataTable">
				<thead>
					<tr style="cursor: move;">
						<th width="40px">序号</th>
						<th>操作</th>
						<th title="数据库字段名">列名</th>
						<th title="默认读取数据库字段备注">说明</th>
						<th title="数据库中设置的字段类型及长度">物理类型</th>
						<th title="是否是数据库主键">主键</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${genTable.columnList }" var="column" varStatus="vs">
										<tr>
										<td>
											<input type="hidden" name="columnList[${vs.index}].sort" value="${vs.index }" maxlength="200" class="form-control required   digits" aria-required="true">
											<label>${vs.index}</label>
											<input type="hidden" name="columnList[${vs.index}].isInsert" value="1">
											<input type="hidden" name="columnList[${vs.index}].isEdit" value="0">
										</td>
										<td>
											<input type="checkbox" class="i-checks" name="ck" value="1">
										</td>
										<td>
											<input type="text" class="form-control" name="columnList[${vs.index}].name" value="${column.name }">
										</td>
										<td>
											<input type="text" class="form-control" name="columnList[${vs.index}].comments" value="${column.comments }" maxlength="200">
										</td>
										<td>
											<select name="columnList[${vs.index}].jdbcType" class="form-control required m-b" aria-required="true">
												<c:forEach items="${config.jdbcTypeList}" var="dict">
													<option value="${dict.value}" ${dict.value==column.jdbcType?'selected':''} title="${dict.description}">${dict.label}</option>
												</c:forEach>
											</select>
										</td>
										<td>
											<input type="checkbox" class="i-checks" name="columnList[${vs.index}].isPk" value="1"  ${column.isPk eq '1' ? 'checked' : ''}>
										</td>
										</tr>
									</c:forEach>
				</tbody>
			</table>
		</div>
		</div>
		<div id="tab-2" class="tab-pane">
                            <div class="panel-body">
                                 <table id="contentTable2" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
	                             <thead>
								 <tr>
									<th title="数据库字段名" width="15%">列名</th>
									<th title="默认读取数据库字段备注">说明</th>
									<th title="实体对象的属性字段类型" width="15%">Java类型</th>
									<th title="实体对象的属性字段（对象名.属性名|属性名2|属性名3，例如：用户user.id|name|loginName，属性名2和属性名3为Join时关联查询的字段）">Java属性名称 <i class="icon-question-sign"></i></th>
									<th title="选中后该字段被加入到查询列表里">表单</th>
									<th title="选中后该字段被加入到查询列表里">列表</th>
									<th title="选中后该字段被加入到查询条件里">查询</th>
									<th title="该字段为查询字段时的查询匹配放松" width="15%">查询匹配方式</th>
									<th title="字段在表单中显示的类型" width="15%">显示表单类型</th>
									<th title="显示表单类型设置为“下拉框、复选框、点选框”时，需设置字典的类型">字典类型</th>
								 </tr>
								 </thead>
							<tbody>
								<c:forEach items="${genTable.columnList }" var="column" varStatus="vs">
									<tr>
										<td>
											<input type="text" class="form-control" readonly="readonly" name="page-columnList[${vs.index}].name" value="${column.name }">
										</td>
										<td>
											<input type="text" class="form-control" name="page-columnList[${vs.index}].comments" value="${column.comments }" maxlength="200" readonly="readonly">
										</td>
										<td>
											<select name="columnList[${vs.index}].javaType" class="form-control required m-b" aria-required="true">
												<c:forEach items="${config.javaTypeList}" var="dict">
													<option value="${dict.value}" ${dict.value==column.javaType?'selected':''} title="${dict.description}">${dict.label}</option>
												</c:forEach>
											</select>
										</td>
										<td>
											<input type="text" name="columnList[${vs.index}].javaField" value="${column.javaField }" maxlength="200" class="form-control required " aria-required="true">
										</td>
										<td>
											<input type="checkbox" class="i-checks" name="columnList[${vs.index}].isForm" value="1"  ${column.isForm eq '1' ? 'checked' : ''}>
										</td>
										<td>
											<input type="checkbox" class="i-checks" name="columnList[${vs.index}].isList" value="1"  ${column.isList eq '1' ? 'checked' : ''}>
										</td>
										<td>
											<input type="checkbox" class="i-checks" name="columnList[${vs.index}].isQuery" value="1"  ${column.isQuery eq '1' ? 'checked' : ''}>
										</td>
										<td>
											<select name="columnList[${vs.index}].queryType" class="form-control required m-b" aria-required="true">
												<c:forEach items="${config.queryTypeList}" var="dict">
													<option value="${dict.value}" ${dict.value==column.queryType?'selected':''} title="${dict.description}">${dict.label}</option>
												</c:forEach>
											</select>
										</td>
										<td>
											<select name="columnList[${vs.index}].showType" class="form-control required m-b" aria-required="true">
												<c:forEach items="${config.showTypeList}" var="dict">
													<option value="${dict.value}" ${dict.value==column.showType?'selected':''} title="${dict.description}">${dict.label}</option>
												</c:forEach>
											</select>
										<td>
											<input type="text" name="columnList[${vs.index}].dictType" value="${column.dictType }" maxlength="200" class="form-control">
										</td>
									</tr>
								</c:forEach>
							</tbody>
							</table>
                          </div>
                        </div>
                        <div id="tab-3" class="tab-pane">
                            <div class="panel-body">
                                 <table id="contentTable3" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
                              <thead>
								<tr>
								<th title="数据库字段名" width="15%">列名</th>
								<th title="默认读取数据库字段备注">说明</th>
								<th title="实体对象的属性字段类型" width="15%">table表名</th>
								<th title="实体对象的属性字段说明（label1|label2|label3，用户名|登录名|角色）">JAVA属性说明<i class="icon-question-sign"></i></th>
								<th title="选中后该字段被加入到查询列表里">JAVA属性名称</th>
								<th title="选中后该字段被加入到查询列表里">检索标签</th>
								<th title="选中后该字段被加入到查询条件里">检索key</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${genTable.columnList }" var="column" varStatus="vs">
									<tr>
										<td>
											<input type="text" class="form-control" readonly="readonly" name="page-columnList[${vs.index}].name" value="${column.name }">
										</td>
										<td>
											<input type="text" class="form-control" name="page-columnList[${vs.index}].comments" value="${column.comments }" maxlength="200" readonly="readonly">
										</td>
										<td>
											<input type="text" name="columnList[${vs.index}].tableName" value="${column.tableName }" maxlength="200" class="form-control">
										</td>
										<td>
											<input type="text" name="columnList[${vs.index}].fieldLabels" value="${column.fieldLabels }" maxlength="200" class="form-control">
										</td>
										<td>
											<input type="text" name="columnList[${vs.index}].fieldKeys" value="${column.fieldKeys }" maxlength="200" class="form-control">
										</td>
										<td>
											<input type="text" name="columnList[${vs.index}].searchLabel" value="${column.searchLabel }" maxlength="200" class="form-control">
										</td>
										<td>
											<input type="text" name="columnList[${vs.index}].searchKey" value="${column.searchKey }" maxlength="200" class="form-control">
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
                            </div>
                        </div>
                        <div id="tab-4" class="tab-pane">
						<div class="panel-body">
							<table id="contentTable4" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
                              <thead>
								<tr>
									<th title="数据库字段名" width="15%">列名</th>
									<th title="默认读取数据库字段备注">说明</th>
									<th title="字段是否可为空值，不可为空字段自动进行空值验证">可空</th>
									<th title="校验类型">校验类型<i class="icon-question-sign"></i></th>
									<th title="最小长度">最小长度</th>
									<th title="最大长度">最大长度</th>
									<th title="最小值">最小值</th>
									<th title="最大值">最大值</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${genTable.columnList }" var="column" varStatus="vs">
									<tr>
										<td>
											<input type="text" class="form-control" readonly="readonly" name="page-columnList[${vs.index}].name" value="${column.name }">
										</td>
										<td>
											<input type="text" class="form-control" name="page-columnList[${vs.index}].comments" value="${column.comments }" maxlength="200" readonly="readonly">
										</td>
										<td>
											<input type="checkbox" class="i-checks" name="columnList[${vs.index}].isNull" value="1"  ${column.isNull eq '1' ? 'checked' : ''}>
										</td>
										<td>
											<select name="columnList[${vs.index}].validateType" class="form-control m-b">
												<c:forEach items="${config.validateTypeList}" var="dict">
													<option value="${dict.value}" ${dict.value==column.validateType?'selected':''} title="${dict.description}">${dict.label}</option>
												</c:forEach>
											</select>
										</td>
										<td>
											<input type="text" name="columnList[${vs.index}].minLength" value="${column.minLength }" maxlength="200" class="form-control">
										</td>
										<td>
											<input type="text" name="columnList[${vs.index}].maxLength" value="${column.maxLength }" maxlength="200" class="form-control">
										</td>
										<td>
											<input type="text" name="columnList[${vs.index}].minValue" value="${column.minValue }" maxlength="200" class="form-control">
										</td>
										<td>
											<input type="text" name="columnList[${vs.index}].maxValue" value="${column.maxValue }" maxlength="200" class="form-control">
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					</div>
                    </div>
                </div>
	</form:form>
</body>
</html>
