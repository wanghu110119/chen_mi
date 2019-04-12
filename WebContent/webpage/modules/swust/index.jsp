
<meta name="viewport" content="width=device-width, initial-scale=1,user-scalable=0">
<body></body>



<script src="/mht_oeg/static/swust/js/jquery.min.js"></script>
<script type="text/javascript">
	function request () {
	    var code = location.href.split("code=")[1].split("&state")[0];
            $.ajax({
                url:"https://api.weishao.com.cn/oauth/token",
                type:"POST",
                dataType:"json",
                data:{
			grant_type:"authorization_code",
			app_key:"a1df9a1985bc3b85",
			app_secret:"86149db2bcac4ba5df2bb3cd296a2328",
			code:code,
			redirect_uri:"http://202.115.160.151:8077/mht_oeg/api/swust/meeting/index"
		},
                success:function(res){
                    console.log(res);
		    getUser(res.access_token);
                },
                error:function(err){
                }
            })
        }
	function getUser (token) {
		$.ajax({
			url:"https://api.weishao.com.cn/userinfo?access_token="+token,
			type:"GET",
			success:function(data){
				location.href = "http://202.115.160.151:8077/mht_oeg/a/cas?username="+data.student_number+"&code="+data.student_number
			}
	        	});
			}
	$(
		function(){
			request();
			
		}	
	)
</script>