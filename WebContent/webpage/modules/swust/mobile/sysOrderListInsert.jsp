<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head lang="zh-cn">
    <meta charset="UTF-8">
    <!--2 viewport-->
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no">
    <!--3、x-ua-compatible-->
    <meta http-equiv="x-ua-compatible" content="IE=edge">
    <title>新预约</title>
    <!--4、引入两个兼容文件-->
    <!--[if lt IE 9]>
    <script src="js/html5shiv.min.js"></script>
    <script src="js/respond.min.js"></script>
    <![endif]-->
    <!--6、引入 bootstrap.css-->
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/newBooking.min.css">
    <link rel="stylesheet" href="css/header.min.css">
    <script src="http://www.my97.net/dp/My97DatePicker/WdatePicker.js"></script>
</head>
<body>
    <div id="header">

    </div>
    <div class="container-fluid change-padding">
        <p class="my-booking">预约信息</p>
    </div>
    <div class="container">
        <div class="row some-padding">
            <div class="col-xs-12 col-sm-5">
                <form>
                    <div class="form-group">
                        <label for="name">拜访人：</label>
                        <input type="text" class="form-control" id="name">
                    </div>
                    <div class="form-group">
                        <label for="company">工作单位：</label>
                        <input type="text" class="form-control" id="company">
                    </div>
                    <div class="form-group">
                        <label for="car-number">车牌号码：</label>
                        <input type="text" class="form-control" id="car-number">
                    </div>
                    <div class="form-group">
                        <label for="begin-time">起始时间：</label>
                        <input type="datetime-local" class="form-control" id="begin-time">
                    </div>
                </form>
            </div>
            <div class="col-xs-12 col-sm-5 col-sm-offset-2">
                <form>
                    <div class="form-group">
                        <label for="phone">手机：</label>
                        <input type="text" class="form-control" id="phone">
                    </div>
                    <div class="form-group">
                        <label for="school">校内对接单位：</label>
                        <input type="text" class="form-control" id="school">
                    </div>
                    <div class="form-group">
                        <label for="car-category">车辆类型：</label>
                        <input type="text" class="form-control" id="car-category">
                    </div>
                    <div class="form-group">
                        <label for="end-time">截止时间：</label>
                        <input type="datetime-local" class="form-control" id="end-time">
                    </div>

                </form>
            </div>
            <div class="col-xs-12 some-margin">
                <div class="form-group">
                    <label for="detail">预约事由：</label>
                    <textarea class="form-control" id="detail">

                    </textarea>
                </div>
                <div class="form-group">
                    <label for="extra-notice">备注：</label>
                    <textarea class="form-control" id="extra-notice">

                    </textarea>
                </div>
                <div class="form-group text-right">
                    <button class="confirm">提交</button>
                </div>
            </div>
        </div>
    </div>
    <!--模态密码-->
    <div class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" id="modal-pwd">
        <div class="modal-dialog modal-md" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h3 class="modal-title text-center">修改密码</h3>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal">
                        <div class="form-group">
                            <label for="old-pwd" class="col-sm-2 control-label">旧密码:</label>
                            <div class="col-sm-10">
                                <input type="password" class="form-control" id="old-pwd">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="new-pwd" class="col-sm-2 control-label">新密码:</label>
                            <div class="col-sm-10">
                                <input type="password" class="form-control" id="new-pwd">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="inputPassword3" class="col-sm-2 control-label">确认密码:</label>
                            <div class="col-sm-10">
                                <input type="password" class="form-control" id="inputPassword3">
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer text-center">
                    <button type="button" class="btn btn-md confirm">确认</button>
                </div>
            </div>
        </div>
    </div>
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/newBooking.js"></script>
</body>
</html>
<%-- <html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>
<form action="mobile/swust/order/insertOrder">
	拜访人名字<input type="text" name="orderName"/>
	电话号码<input type="text" name="orderPhone"/>
	车牌<input type="text" name="carNumber"/>
	车类型<input type="text" name="carType"/>
	开始时间<input type="Date" name="beginTime"/>
	结束时间<input type="Date" name="endTime"/>
	所在院系<input type="text" name="officeId"/>
	预约事由<input type="text" name="orderReason"/>
	 <input type="text" name="remarks"/>
	<input type="submit" value="提交"/>
	</form>
</body>
</html> --%>