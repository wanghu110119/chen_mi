<%@ page contentType="text/html;charset=UTF-8" %>
<div id="message"></div>

<script language="javascript"type="text/javascript">  
	 var websocket = null;
	 var id = '${fns:getUser().id}';
     //判断当前浏览器是否支持WebSocket
     if ('WebSocket' in window) {
         websocket = new WebSocket("ws://"+window.document.domain+":8080/mht_oeg/websocket/"+id);
     }
     else {
         alert('当前浏览器 Not support websocket')
     }
 
     //连接发生错误的回调方法
     websocket.onerror = function () {
     };
 
     //连接成功建立的回调方法
     websocket.onopen = function () {
     }
 
     //接收到消息的回调方法
     var index = 200;
     websocket.onmessage = function (event) {
         var num = index++;
         alert(num)
         var message = eval("("+event.data+")");
         if (message != '') {
				var baseDiv = "<div style='position:fixed;bottom:52px;right:52px;z-index:"+num+";' id='layer"+message.number+"'>"
								+ "<div class='row'>"
									+"<div style='width:321px;height:200px;font-size:13px;'>"
								    +"<div class='panel panel-primary'>"
									    +"<div class='panel-heading'>"
									    	+"<span>收费提示</span>"
									    +"</div>"
									    +"<div class='panel-body'>"
									    	+"<div style='height:180px;overflow:auto;'>"
												+"<div>车辆"+message.number+"停车时间超过预约时限，需要收费</div>"
												+"<div>预约时间："+message.beginDate+" - "+message.endDate+"</div>"
												+"<div>离校时间："+message.date+"</div>"
												+"<div>收费金额："+message.money+"</div>"
												+"<div style='text-align:right;'>"
													+"<button class='btn btn-sm btn-primary' onclick='closeMsg("+message.number+")'>确定</button>"
												+"</div>"
									    	+"</div>"
									    +"</div>"
								  +"</div>"
						      +"</div>"
						    +"</div>";
				$("#message").append(baseDiv);
	
         }
     }
 
     //连接关闭的回调方法
     websocket.onclose = function () {
     }
 
     //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
     window.onbeforeunload = function () {
         closeWebSocket();
     }
 
     //关闭WebSocket连接
     function closeWebSocket() {
         websocket.close();
     }

     //关闭消息框
     function closeMsg(number) {
		$("#layer"+number).remove();
     }
 
</script>
