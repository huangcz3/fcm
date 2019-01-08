+function () {
	var tzx = window.Tzx = {};
	tzx.tempVar = {};
}();


Tzx.init = function (){
	this.tempVar.contentBox = $(".modal-box-content"); // 
	this.tempVar.contentItem = $(".modal-box-content .content-item"); // 缓存目标，防止每次scroll时遍历DOM
	$(".modal-box-nav li a").click(function (e) {
		e.preventDefault();  // 禁止锚点定位
		if ($(this).parent().hasClass("on")){
			return;
		}

		var $modalContentBox = $(this).closest(".modal-wrap").find(".modal-box-content");

		$(this).parent().toggleClass("on").siblings().removeClass("on");
		
		// 取消滚动事件
		$modalContentBox.unbind("scroll");
		var px = ($($(this).attr("href")).offset().top - $modalContentBox.offset().top
				+ $modalContentBox.scrollTop() )+ "px";
		var height = $modalContentBox.slimScroll({
											scrollTo: px
										});
		// 恢复滚动
		setTimeout(function () {
			$modalContentBox.scroll(temp);
		}, 500)
		return false;
	})


	$(".tab-group .tab-title").click(function (e) {
		var $this = $(this);
		$this.next("div").toggle();
		$this.toggleClass("on");
	})

	
	var nav = function () {
		var top  = Tzx.tempVar.contentBox.offset().top;
		var $contentItem = Tzx.tempVar.contentItem;
		$contentItem.each(function () {
			if (Math.abs($(this).offset().top - top) < 100) {
				
				$(".modal-box-nav a[href='#" + $(this).attr("id")+"']")
					.parent().addClass("on").siblings().removeClass("on");
			}
		});
		

	}

	
	// 滚动事件
	function temp() {
		throttler(nav, {
										time: 50,
										context: Tzx.init
			});
	}
	$(".modal-box-content").scroll(temp);

	$('.modal-box-content').slimScroll({
	    height: '450px',
	    color: "#a2a8af",
	    alwaysVisible : true,
	    size: "5px",
	});
	
	$(".modal-box-close").click(function(){
		$(".modal-wrap").hide();
		/*$(".modal-box .row").each(function(){
			$(this).css("height", $(this).height())
		});*/
	});
	$(".operate.detail-modal-icon").click(function (e) {
		setTimeout(function () {
			$(".modal-box .row").each(function(){
				$(this).css("height", $(this).height())
		});
		},100);
	})

	 $(".higher").focus(function (e) {	
		// 对box-sizing: border 求高度，用outerHeight, 而不是height()
		 $(this).css("height",2*$(this).outerHeight())
	 }).blur(function (e) {
		 $(this).css("height",0.5*$(this).outerHeight())
	 });

	$(".modal-box-cancel").click(function (e) {
		e.preventDefault();
		$(".modal-wrap").hide();
		$(this).closest('.modal-box-content').slimScroll({
			scrollTo: '0px'
		})
	})
	
}


$(function(){
	Tzx.init();
})




