/**
 * 对jQuery validate校验进行扩展
 */

isMaxIp = function(value){
	console.log(value);
	return false;
}
/*
 * 不允许输入中文
 */
$.validator.addMethod('noChinese', function( value, element ){
    // /^[^\u4e00-\u9fa5]+$/ 不包含中文
    return this.optional( element ) || /^[^\u4e00-\u9fa5]+$/.test( value );
}, '请不要输入中文');

/* url地址校验 */
$.validator.addMethod("isurl", function(value, element) {   
    var tel = getUrlExp();
    return this.optional(element) || (tel.test(value));
}, "请正确填写您的URL");

/* ip地址校验 */
$.validator.addMethod("isip",function(value, element){
	var ip = /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/; 
	return this.optional(element) || (ip.test(value) && (RegExp.$1 < 256 && RegExp.$2 < 256 && RegExp.$3 < 256 && RegExp.$4 < 256)); 
}, "Ip地址格式错误");

/* ip段校验 */
$.validator.addMethod("isMaxIp",function(value, element){
	/* 判断是否为正整数 */
	var patrn = /^[0-9]*[1-9][0-9]*$/;
    if (!patrn.exec(value)) {
    	return false
    } else {
    	/* 判断是否参数是否满足 */
		var ip = $("#sid").val();
		if (ip != '') {
			ip = ip.substring(ip.lastIndexOf(".") + 1, ip.length);
			var ipN = parseInt(ip);
			var valueN = parseInt(value);
			if (valueN >= ipN && valueN <= 255) {
				return true;
			}
		}
        return false;
    }
},"ip段必须在区间内");

/* 手机号验证 */
$.validator.addMethod("telphone", function(value, element) { 
    var tel = /^1[3,4,5,7,8]\d{9}$/; 
    return this.optional(element) || (tel.test(value));
}, "请输入正确的手机号码");

function getUrlExp() {
	var msg = "^((https|http)?://)"         
    + "(([0-9]{1,3}\.){3}[0-9]{1,3}"     
    + "|"        
    + "([0-9a-zA-Z_!~*'()-]+\.)*"      
    + "([0-9a-zA-Z][0-9a-zA-Z-]{0,61})?[0-9a-zA-Z]\."
    + "[a-zA-Z]{2,6})"       
    + "(:[0-9]{1,4})?"        
    + "((/?)|"         
    + "(/[0-9a-zA-Z_!~*'().;?:@&=+$,%#-]+)+/?)$";
	return new RegExp(msg);
}

/* 账号特殊字符 */
$.validator.addMethod("specialChar", function(value, element) { 
	var pattern = new RegExp("[`~!@#$^&*()=|{}':;,.<>/?~！@#￥……&*（）——|【】‘；：”“'。，、？%+ 　\"\\\\]");  
    var specialStr = "";  
    for(var i=0;i<value.length;i++){  
         specialStr += value.substr(i, 1).replace(pattern, '');  
    }  
    if( specialStr == value){  
        return true;  
    }  
    return false;  
}, "不能包含特殊字符");

/* 字母开头 */
$.validator.addMethod("beginChar", function(value, element){
	var begin = new RegExp("^[a-zA-Z]");
	return this.optional(element) || (begin.test(value));
}, "必须以字母开头");

/* 密码校验 */
$.validator.addMethod("pwsChar", function(value, element){
	var tel = /^[a-zA-Z0-9_]{6,20}$/; 
    return this.optional(element) || (tel.test(value));
}, "密码必须包含数字、字母！");
 
/* 身份证验证 */
$.validator.addMethod("isIdCardNo", function(value, element) {
	return this.optional(element) || isIdCardNo(value);
}, "请正确输入您的身份证号码");

/*身份证验证处理方法*/
function isIdCardNo(num) {
	var factorArr = new Array(7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2,1);
	var parityBit=new Array("1","0","X","9","8","7","6","5","4","3","2");
	var varArray = new Array();
	var intValue;
	var lngProduct = 0;
	var intCheckDigit;
	var intStrLen = num.length;
	var idNumber = num;
	// initialize
	if ((intStrLen != 15) && (intStrLen!= 18)) {
		return false;
	}
	// check and set value
	for(i=0;i<intStrLen;i++) {
		varArray[i] = idNumber.charAt(i);
		if ((varArray[i] < "0" || varArray[i]> "9") && (i != 17)){
			return false;
		} else if (i < 17) {
			varArray[i] = varArray[i] * factorArr[i];
		}
	}
	if (intStrLen == 18) {
		// check date
		var date8 = idNumber.substring(6,14);
		if (isDate8(date8) == false) {
			return false;
		}
		// calculate the sum of the products
		for(i=0;i<17;i++) {
			lngProduct = lngProduct + varArray[i];
		}
		// calculate the check digit
		intCheckDigit = parityBit[lngProduct % 11];
		// check last digit
		if (varArray[17] != intCheckDigit) {
			return false;
		}
	}
	else{ 
		// length is 15
		// check date
		var date6 = idNumber.substring(6,12);
		if (isDate6(date6) == false) {
			return false;
		}
	}
	return true;
}

function isDate6(sDate) {
	if(!/^[0-9]{6}$/.test(sDate)) {
		return false;
	}
	var year, month, day;
	year = sDate.substring(0, 4);
	month = sDate.substring(4, 6);
	if (year < 1700 || year > 2500)
		return false
	if (month < 1 || month > 12) 
		return false
	return true
}

function isDate8(sDate) {
	if(!/^[0-9]{8}$/.test(sDate)) {
		return false;
	}
	var year, month, day;
	year = sDate.substring(0, 4);
	month = sDate.substring(4, 6);
	day = sDate.substring(6, 8);
	var iaMonthDays = [31,28,31,30,31,30,31,31,30,31,30,31]
	if (year < 1700 || year > 2500)
		return false
	if (((year % 4 == 0) && (year % 100!= 0)) || (year % 400 == 0)) 
		iaMonthDays[1]=29;
	if (month < 1 || month > 12) 
		return	false
	if (day < 1 || day >iaMonthDays[month - 1]) 
		return false
	return true
}

//中文和英文
$.validator.addMethod("chinaEn", function(value, element){
	var tel = /^[a-zA-Z.^\u4e00-\u9fa5]{1,}$/; 
    return this.optional(element) || (tel.test(value));
}, "只能包含汉字或者字母");

//汉字数字字母
$.validator.addMethod("chinaEnNum", function(value, element){
	var tel = /^[a-zA-Z0-9.^\u4e00-\u9fa5]{1,}$/; 
    return this.optional(element) || (tel.test(value));
}, "只能包含汉字、字母、数字");

/*字母、数字*/
$.validator.addMethod("enNum", function(value, element){
	var tel = /^[a-zA-Z0-9]{1,}$/; 
    return this.optional(element) || (tel.test(value));
}, "只能包含字母或者数字");

/*手机号验证*/
$.validator.addMethod("isMobile", function(value, element) {
    var length = value.length;
    var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;
    return this.optional(element) || (length == 11 && mobile.test(value));
}, "请正确填写您的手机号码");

//车牌号码验证
$.validator.addMethod("licenseNum", function(value, element){
	var tel = /^[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}$/; 
    return this.optional(element) || (tel.test(value));
}, "请输入正确的车牌号码");

