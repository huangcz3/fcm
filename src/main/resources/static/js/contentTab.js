/*
	作用：1，点击分页按钮切换样式及tab页；
				2，输出全局变量 nowPage ， 0为第一个分页，1为第二个分页，以此类推。
	注意：需引入generalFunc.js 中 eventEmitter 模块作为事件发射器。
				selected的样式。

*/

+function(){
	var $as = $("span.title.lt");
	window.nowPage = 0;
	$as.click(function (e) {
		e.preventDefault();
		if ($(this).hasClass("selected"))
			return;
		var $thisA = $(this).find("a");
		window.nowPage = $as.find("a").index($thisA);
		var id = $thisA.attr("href");
		$(this).addClass("selected").siblings(".selected").removeClass("selected");
		$(id).addClass("selected").siblings(".selected").removeClass("selected");
		eventEmitter.emit("changeTab");	
	});
}(eventEmitter);
