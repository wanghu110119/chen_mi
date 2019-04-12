
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


grgd

<script src="${ctxStatic }/swust/js/jquery.min.js"></script>
<script type="text/javascript">
	function request () {
	    $.ajax({
	       url:'https:api.weishao.com.cn/oauth/token',
	        type:'POST',
	        dataType:"json",
	        data:{
	            grant_type:"client_credentials",
	            app_key:"a1df9a1985bc3b85",
	            app_secret:"86149db2bcac4ba5df2bb3cd296a2328",
	            scope:"base_api"
	         },
	        success:function(res){
	            console.log(res.access_token);
	            getUser(res.access_token);
	        },
	        error:function(err){
	            console.log(err);
	        }
	    })
	}
	function getUser (token) {
        $.ajax({
            url:'https:api.weishao.com.cn/api/v2/index.php/user/authUser',
            dataType:'json',
            type:'POST',
            data:{
                verify:"verify",
                access_token:token
            },
            success:function(data){
//             	alert(data);
                conasole.log(data)
            },
            error:function(err){
                console.log(err);
            }
        })
    }
	$(function(){
		request();
		name() ;
	})
	
	function name() {
		  $.ajax({
		         type : "post",
		         url : "http://202.115.160.151:8060/cas/login",
		       	 data:{
		       		username: "mht01",
		       		password: "123456",
		       		execution: "e1s1",
		       		_eventId:"submit"
		       	 },
	             success : function(data) {
	            	 console.log(data)
		         } 
	         });
	}
</script>






