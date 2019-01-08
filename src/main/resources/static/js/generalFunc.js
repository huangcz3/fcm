if ( !Array.prototype.forEach ) {   //ie8及以下不支持forEach方法
	Array.prototype.forEach = function forEach( callback, thisArg ) {
		var T, k;
		if ( this == null ) {
			throw new TypeError( "this is null or not defined" );
		}
		var O = Object(this);
		var len = O.length >>> 0;
		if ( typeof callback !== "function" ) {
			throw new TypeError( callback + " is not a function" );
		}
		if ( arguments.length > 1 ) {
			T = thisArg;
		}
		k = 0;

		while( k < len ) {

			var kValue;
			if ( k in O ) {
				kValue = O[ k ];
				callback.call( T, kValue, k, O );
			}
			k++;
		}
	};
}
String.prototype.trim = function () {   //trim方法
	return this .replace(/^\s\s*/, '' ).replace(/\s\s*$/, '' );
}
//判断浏览器是否是IE8
function isIE8(){
	if(navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion.match(/8./i) == "8."){
		return true;
	}else{
		return false;
	}
} 
if (!Function.prototype.bind) { 
	Function.prototype.bind = function (oThis) { 
		if (typeof this !== "function") { 
			throw new TypeError("Function.prototype.bind - what is trying to be bound is not callable"); 
		} 
		var aArgs = Array.prototype.slice.call(arguments, 1), 
		fToBind = this, 
		fNOP = function () {}, 
		fBound = function () { 
			return fToBind.apply(this instanceof fNOP && oThis 
				? this
				: oThis, 
				aArgs.concat(Array.prototype.slice.call(arguments))); 
		}; 
		fNOP.prototype = this.prototype; 
		fBound.prototype = new fNOP(); 
		return fBound; 
	}; 
}
//调用日期控件
function sDate(type) {
	var date = new Date();
	var styleDate = "yyyy-MM-dd";
	if(type == "month") {
		styleDate = "yyyy-MM";
	} else if(type == "year") {
		styleDate = "yyyy";
	}
	var date_text = formatDate(date, styleDate);
	return date_text;
}
function transDateFormat(date){  // 2017-06-55 ==> 20170605
	return date.split("-").join("");
}
function transToDate(str) { // 20170605 ==> 2017-06-55
	var date = str.replace(/^(\d{4})(\d{2})(\d{2})$/,"$1-$2-$3");
	return date;
}
//格式化日期方法
function formatDate(date, format) {
	var paddNum = function(num) {
		num += "";
		return num.replace(/^(\d)$/, "0$1");
	}
	//指定格式字符
	var cfg = {
	yyyy 	: date.getFullYear(), //年: 4位
	yy 		: date.getFullYear().toString().substring(2), //年: 2位
	M 		: date.getMonth() + 1, //月: 如果1位的时候不补0
	MM 		: paddNum(date.getMonth() + 1), //月: 如果1位的时候补0
	d 		: date.getDate(), //日: 如果1位的时候不补0
	dd 		: paddNum(date.getDate()), //日: 如果1位的时候补0
	hh 		: paddNum(date.getHours()), //时: 如果1位的时候补0
	mm 		: paddNum(date.getMinutes()), //分: 如果1位的时候补0
	ss 		: date.getSeconds() //秒
};
format || (format = "yyyy-MM-dd");
return format.replace(/([a-z])(\1)*/ig, function(m) {return cfg[m];});
}
//点击筛选按钮
$("#filter-btn").click(function(){
	$("#filter-btn").addClass("opened");
	$(".filter-panel").css("display","block");
});
//剔重和未剔重按钮切换
$(".radio-box p").click(function(){
	$(this).siblings("p").find("input").attr("checked",false);
	$(this).siblings("p").find("span").removeClass("selected");
	$(this).find("input").attr("checked",true);
	$(this).find("span").addClass("selected");
});
//筛选弹框，点击取消按钮
$("#cancel").click(function(e){
	e.preventDefault();
	$("#filter-btn").removeClass("opened");
	$(".filter-panel").css("display","none");
});

function initTableHover(){
	$(".table tbody tr").mouseover(function(){
		$(this).addClass("hovering");
	})
	$(".table tbody tr").mouseout(function(){
		$(this).removeClass("hovering");
	})
}

/*
 * Tzx eventEmitter
 * 2017.06.01
 * on 方法注册
 * emit 广播事件
 */
 var eventEmitter = {
 	strs: [],
 	funcArray: {},
 	on: function (str, fun) {
 		if (arguments.length < 2)
 			return;
 		this.strs.push(str);
 		!this.funcArray[str]? this.funcArray[str]=[fun]:this.funcArray[str].push(fun);
 	},
 	emit: function (str) {
 		if(arguments.length!=1)
 			return;
 		for(var i in this.funcArray[str])
 			this.funcArray[str][i]&&this.funcArray[str][i]();
 	}
 };

/*
 *	Tzx  节流器函数：throttler
 *	2017年6月13日 14:50:23
 *	用于scroll的回调，防止短时间内多次调用，造成卡顿
 *	参数fn , 为需要节流的函数(适合计算量大或者运行时间长)， param 为参数对象，可设置time
 */
 var throttler = function(fn, param){
 	var isClear = arguments[0], fn;
 	if ( isClear ===  true ) {
 		fn = arguments[1];
 		fn._throttleID && clearTimeout(fn._throttleID);
 	} else {
 		fn = isClear;
 		param = arguments[1];
 		var p = {
 			context: param.context || null,
 			args: param.args || [],
 			time: param.time || 100,
 		}
		arguments.callee(true,fn);//清除计时器
		fn._throttleID = setTimeout( function (){
			fn.apply(p.context, p.args)
		}, p.time);
	}
}

/*	
 *	Tzx addFullScrOverlay 添加全屏掩盖层
 *	添加文字提示或者确认框	
 *	typeString: tip框类型字符串， in ["error", "success", "confirm"]	
 *	typeParam:  为 提示框参数，包含 text (失败或成功所显示的文字) ; confirmTitle 确认框标题
 *	返回值：返回掩盖层jquery对象
 */
 var addFullScrOverlay = function ( overlayParam, typeString, typeParam, timeout ) {
 	var o = {
 		background: overlayParam && overlayParam.background || "rgba(255,255,255,0.8)",
		// opacity: overlayParam &&  overlayParam.opacity || 0.7,
	}
	var param = {
		text : typeParam && typeParam.text || "默认内容",
		confirmTitle: typeParam && typeParam.confirmTitle || "您确认吗？",
		cbEnsure: typeParam && typeParam.cbEnsure || function () {},
		cbCancle: typeParam && typeParam.cbCancle || function () {},
		context: typeParam && typeParam.context || undefined
	}
	var $overlay = $("<div class='full-scr-overlay'></div>");
	$overlay.css(o);
	$("body").append($overlay);
	if ( typeString ) {
		switch (typeString) {
			case "error":
			var $tip = $("<div class='tip'></div>");
			var $text = $("<p class='tip-error'></p>");
			$text.text(param.text);
			$tip.append($text);
			$overlay.append($tip);
			break;
			case "success":
			var $tip = $("<div class='tip'></div>");
			var $text = $("<p class='tip-success'></p>");
			$text.text(param.text);
			$tip.append($text);
			$overlay.append($tip);
			break;
			case "confirm":
			var $tip = $("<div class='tip tip-confirm'></div>");
			var $confirmText = $("<p class='title'></p>");
			$confirmText.text(param.confirmTitle);
			$tip.append($confirmText);
			var $btnGroup = $("<div class='btn-group'></div>");
			var times = new Date().getTime();
			var classEnsure = "ensure" + times;
			var classCancel = "cancel" + times;
			$btnGroup.append($("<a href='javascript:;' id='ensureUpload' class='"+
				classEnsure+" btn-group-ensure'>确定</a>"));
			$btnGroup.append($("<a href='javascript:;' id='cancelUpload' class='"+
				classCancel+" btn-group-cancel'>取消</a>"));
			$tip.append($btnGroup);
			$overlay.append($tip);
			$("."+classEnsure).click(function (e) {
				e.preventDefault();
				param.cbEnsure.bind(param.context)();
				$overlay.remove();
				$(this).unbind();
			})
			$("."+classCancel).click(function (e) {
				e.preventDefault();
				param.cbCancle.bind(param.context)();
				$overlay.remove();
				$(this).unbind();
			})
			break;
		}
	}
	if (typeString !== "confirm") {
		setTimeout(function () {
			$overlay.remove();
		},timeout);
		return;
	}
}
/**
 * @param  {message} 输入需要显示的信息
 * @param  {state} 输入状态字符串，可选内容为： ["success", "error"]
 * @param  {timeout} 显示提示框的最小时长,默认2000毫秒
 * @return {null} 无返回值
 */
 function showMessage (message, state, timeout) {
 	addFullScrOverlay(null, state,  {
 		text: message
 	}, timeout || 2000);
 }
/**
 * @param  {message} 确认框所要显示的文字
 * @param  {cbEnsure} 点击确定时的回调函数
 * @param  {cbCancle} 点击取消时的回调函数
 * @param  {context} 环境上下文,默认为undefined
 * @return {null} 无返回值
 * @author Tzx
 * @create_time 2017年7月11日 11:45:54
 */
 function showConfirm (message, cbEnsure, cbCancle, context) {
 	addFullScrOverlay(null, "confirm",  {
 		confirmTitle: message || "确定吗?",
 		cbEnsure: cbEnsure,
 		cbCancle: cbCancle,
 		context: context
 	});
 }


/*!
 * @param  {str} 需要测试的字符串
 * @param  {typeString} 测试字符串的方式,可选内容为: ["phone-number"]
 * @return {boolean}
 */
 function regTest (str, typeString) {
 	switch(typeString) {
 		case "phone-number":
 		var reg = /^1\d{10}$/;
 		return reg.test(str);
 		break;
 		case "city-id":
 		
 		break;
 		default: 
 		return;
 	}
 }

//掌上冲浪字数检查
function checkMaxInput(obj, maxLen) {
	if (obj == null || obj == undefined || obj == "") {
		return;
	}
	if (maxLen == null || maxLen == undefined || maxLen == "") {
		maxLen = 100;
	}
	var $obj = $(obj);
	var newid = $obj.attr("id") + 'msg';
	var $msg = $("#" + newid);
	if ($msg.length == 0) {
		$obj.after('<i id="' + newid + '" class=\'Max_msg\' >已输入' + (obj.value.length) + '字</i>');
	} else {
		$msg.html('已输入' + (obj.value.length) + '字');
	}
}
//清空剩除字数提醒信息
function resetMaxmsg() {
	$("i.Max_msg").remove();
}
//字数超出限制
function overWordsTyped(obj,maxLen){
	if (obj.value.length > maxLen) { //如果输入的字数超过了限制
    	$(obj).addClass("over-words");
    	$(obj).next().text("超出最大字数"+(obj.value.length-maxLen)+"个字，请调整").addClass("over-max-msg");
      }else{
    	  $(obj).removeClass("over-words");
    	  resetMaxmsg();
      }
}
//触点短信剩余字数检查
function checkMaxInput1(obj, maxLen) {
	if (obj == null || obj == undefined || obj == "") {
		return;
	}
	if (maxLen == null || maxLen == undefined || maxLen == "") {
		maxLen = 100;
	}
	var $obj = $(obj);
	var newid = $obj.attr("id") + 'msg';
	
    var $msg = $("#" + newid);
    if ($msg.length == 0) {
    	if(maxLen >= obj.value.length){	
    		if($(obj).hasClass("over-words")){
    			$(obj).removeClass("over-words");
    		}
    		if($msg.hasClass("over-max-msg")){
    			$msg.removeClass("over-max-msg");
    		}
    		
    		$obj.after('<i id="' + newid + '" class=\'Max_msg\' >剩' + (maxLen - obj.value.length) + '个字</i>');
    	}else{
    		$(obj).addClass("over-words");
    		$obj.after('<i id="' + newid + '" class=\'Max_msg over-max-msg\' >超出' + ( obj.value.length-maxLen) + '个字</i>');
    	}
    }else{
    	if(maxLen >= obj.value.length){	
    		if($(obj).hasClass("over-words")){
    			$(obj).removeClass("over-words");
    		}
    		if($msg.hasClass("over-max-msg")){
    			$msg.removeClass("over-max-msg");
    		}
    		$msg.html('剩' + (maxLen - obj.value.length) + '个字');
    	}else{
 
    		$(obj).addClass("over-words");
    		$msg.addClass("over-max-msg");
    		$msg.html('超出' + ( obj.value.length-maxLen) + '个字');
    	}
    	
    }
}
//form表单json序列化数据
$.fn.serializeJson = function() {
	var o = {};
	var a = this.serializeArray();
	$.each(a, function() {
		if (o[this.name]) {
			if (!o[this.name].push) {
				o[this.name] = [ o[this.name] ];
			}
			o[this.name].push(this.value || '');
		} else {
			o[this.name] = this.value || '';
		}
	});
	return o;
}


var generalExtrFuncs = (function () {
	var funcs = {};
	funcs.transDate = function (param) { //transform   2017-09-27 15:17:03.252	 to   2017-09-27
		/*var date = new Date(param);
		 var year = date.getFullYear();
		 var month = date.getMonth()+1;
		 var day = date.getDate();
		 return year+split+month+split+day*/
		return param.substring(0,10);
	}
	funcs.transFullTime = function (p) { //transform 20171212151703252 to 2017-12-12
		var param = p.substring(0,8);
	return transToDate(param);
}
	funcs.IEVersion = function() {
		var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
		var isIE = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1; //判断是否IE<11浏览器
		var isEdge = userAgent.indexOf("Edge") > -1 && !isIE; //判断是否IE的Edge浏览器
		var isIE11 = userAgent.indexOf('Trident') > -1 && userAgent.indexOf("rv:11.0") > -1;
		if(isIE) {
			var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
			reIE.test(userAgent);
			var fIEVersion = parseFloat(RegExp["$1"]);
			if(fIEVersion == 7) {
				return 7;
			} else if(fIEVersion == 8) {
				return 8;
			} else if(fIEVersion == 9) {
				return 9;
			} else if(fIEVersion == 10) {
				return 10;
			} else {
				return 6;//IE版本<=7
			}
		} else if(isEdge) {
			return 'edge';//edge
		} else if(isIE11) {
			return 11; //IE11
		}else{
			return -1;//不是ie浏览器
		}
	}
	funcs.getMyBrowser = function(){
		var userAgent = navigator.userAgent.toLowerCase(); //取得浏览器的userAgent字符串
		var isOpera = userAgent.indexOf("opera") > -1;
		if (isOpera) { //判断是否Opera浏览器
			return "Opera"
		}else if (userAgent.indexOf("firefox") > -1) { //判断是否Firefox浏览器
			return "FireFox";
		}else if (userAgent.indexOf("chrome") > -1){
			return "Chrome";
		}else if (userAgent.indexOf("safari") > -1) { //判断是否Safari浏览器
			return "Safari";
		}else{
			if ( this.IEVersion() != -1 ) {   //判断是否IE浏览器
				return "IE"+this.IEVersion();
			}else{
				return "未检测到，请自行查看"
			}
		}
	}
	funcs.toggleCheckbox = function(e) {
		var $this = $(this);
		var $parent = $this.parent();
		var $prev = $this.prev();
		$parent.toggleClass("selected");
		if ($prev) {
			if ($prev.attr("checked")) {
				$prev.removeAttr("checked");
			} else {
				$prev.attr("checked", true);
			}
		}
	}
	funcs.checkPhoneNum = function (e) {
		if(!$(e.target).val()){
			return;
		}
		var arr = $(e.target).val().split(",");
		var phoneNumbers = [];
		var isWrong = false;
		var wrongNum = [];
		for (var i = 0; i < arr.length; ++i) {
			if (!regTest(arr[i], "phone-number")) {
				isWrong = true;
				wrongNum.push(arr[i]);
			} else {
				phoneNumbers.push(arr[i]);
			}
		}
		var wrongNumString = wrongNum.join(", ");
		if (wrongNumString.length > 35) {
			wrongNumString = wrongNumString.substring(0, 35) + "...";
		}
		if (isWrong) {
			var str = "存在错误号码，\""+wrongNumString+"\",请及时修改!"
			showMessage(str,"error", 3000);
			//alert("存在错误号码，"+wrongNum.join(", ")+"已剔除！");
			// $(e.target).val(phoneNumbers.join(","))
		}
	}
	funcs.limitPhoneNum = function () {
		var $this = $(this);
		$this.val($this.val().replace(/[^\d,]*/g,''));
	}

	return funcs;
})();

//checkbox选中/取消事件
$(".j-checxbox-list").on("click","label",generalExtrFuncs.toggleCheckbox);

//筛选框隐藏
$(document).on('click', function (e) {
	if ( !$(e.target).hasClass('filter-btn')
		&& !$(e.target).closest('.filter-panel').length ) {
		$("#filter-btn").removeClass("opened");
		$(".filter-panel").css("display","none");
	}
	if (e.target !== $('.more-icon-box.selected')[0] && !$(e.target).hasClass('operate more-icon') ) {
		if($('.more-icon-box.selected').length){
			$('.more-icon-box.selected').removeClass("selected");
		}
	}
});


