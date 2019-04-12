<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<link rel="stylesheet"
	href="//at.alicdn.com/t/font_a3o0c57h6fv6xbt9.css">
<title>统一信息门户</title>
<link href="${ctxStatic }/portal/css/index.css" rel="stylesheet">
<link rel="stylesheet" type="text/css"
	href="${ctxStatic }/css/style.css">
<script src="${ctxStatic }/js/jquery.js"></script>
</head>
<body>
<!-- X-bj背景图片设置-->
<div class="X-bj">
    <!-- x-fudong内容下的遮罩-->
    <div class="X-fudong">
    </div>
    <!--Xin-head 右上角的导航菜单-->
<div class="Xin-head">
    <a href="/mht_service/static/serviceCenter.html"><span> 服务大厅</span></a>
    <a href="${ctxStatic }/newsCenter.html"><span> 新闻中心</span></a>
    <a href="http://192.168.3.145:8020"><span> 问卷评教</span></a>
    <span>
        <img class="X-chuang" src="${ctxStatic }/images/Xin-1.jpg">
        <div> 1</div>
    </span>
</div>
    <!-- 页面右上角浮动弹层-->
    <div class="X-fud">
<!--        <div style="height: 36px;width: 100%;background-color: #f8f8f8; text-align: center;display: inline-block;"> -->
<%--        		<a href="${ctx }/cms/index" style="color: #333; margin-top: 20px; margin-bottom: 5px;">更多</a> --%>
<!--        </div> -->
		<br />
		<br />
    </div>
 <!-- 页面主要内容-->
<div class="Xin-contain">
    <div>
        <img align="center" src="${ctxStatic }/images/logo.png">
    </div>
    <br />
    <br />
    <!-- 搜索-->
    <div class="Xin-sousuo">
        <input class="X-input" placeholder=""> 
        <button>搜索</button>
    </div>
    <P class="X-bt">热门搜索服务</P>
    <!-- 热点新闻-->
    <div class="X-news">
        <!--热点新闻左-->
        <div class="X-newsleft">
        </div>
        <!--热点新闻右-->
        <div class="X-newsright">
            <p>热门搜索</p>
            <div>安放调服</div>
            <div>车辆管理</div>
            <div>行迹分析</div>
            <div>门径系统</div>
            <div>心理健康</div>
            <div>心理健康</div>
            <div>安放调服</div>
            <div>车辆管理</div>
            <div>行迹分析</div>
            <div>门径系统</div>
            <div>心理健康</div>
            <div>心理健康</div>
        </div>
    </div>

</div>

</div>
<!--浮动弹层效果-->
<script type="text/javascript">
	$(function(){
		$.ajax({
			url:"/mht_service/a/portal/serviceManage/adminList",
			type:"GET",
			success:function(data){
				$(".X-newsleft").empty;
				var categorys = data.body.categorys;
				for(var i=0; i < categorys.length; i++){
					var src = categorys[i].serviceImg == null ? "/mht/static/images/student.png" : "/mht_service"+categorys[i].serviceImg; 
					var url = categorys[i].serviceUrl == null ? "":categorys[i].serviceUrl;
					$(".X-newsleft").prepend("<div style='position:relative;'><a href='"+url+"'><img class='iconImg' src="+src+"><a></div>");
				}
			}
		})
		
		$.ajax({
			url:"${ctx}/cms/composing",
			type:"GET",
			success:function(data){
				$(".X-fud").empty;
				var compos = data.body.compos;
				for(var i=0; i < compos.length; i++){
					var url = compos[i].remarks == null ? "" : compos[i].remarks;
					var src = "/mht_service"+compos[i].type;
					$(".X-fud").prepend("<a href='"+url+"'><div ><p><img src='"+src+"' style='width: 50px;height: 50px;'></p><p>"+compos[i].composingName+"</p></div></a>");
				}
			}
		})
		
	})
	$(".X-chuang").mouseover(function () {
	    $(".X-fud").show();
	});
	$(".X-fud").hover(function () {
	    $(".X-fud").show();
	}, function () {
	    $(".X-fud").hide();
	});
</script>



<div data-v-77f7f116="" style="font-size: 14px;line-height: 40px;height: 40px;text-align: center;  color: #b2b2b2;background-color: rgb(30, 30, 30);z-index: 2;"><span data-v-77f7f116="">Copyrignt © 2004-2016 明厚天股份 版权所有     四川省明厚天信息技术股份有限公司 蜀ICP备0902</span></div>
</body>

</html>
