// $.ajax({
// 	cache: false
// });
var sideNavigation=function(){
	return {
		def:{
			author:"shaojinyu",
			date:"2017-5-16",
			name:"script of OMP page header model"
		},
		 element:"#sideNav",
		 nav:{
			 	vicActManage:{	
					text:"活动管理",
					dataId:"vicActManage"
				},
			 	sms_manage:{
					text:"发送列表",
					dataId:"sms_manage"
				},
				iopModel:{
					text:"任务管理",
					dataId:"vicTaskManage"
				},
				iopExamp:{
	
					text:"全网活动",
					dataId:"vicAllNetActivity"
				},
			 	channelSuggest:{
					text:"渠道建议",
					dataId:"vicChannelSuggest"
				}
			
		    },
	   buildNavigation:function(){
		   var _this=this;
		   var isfirst=true;
		   var html = "";
	       $(_this.element).empty();
	    
	       $.each(this.nav,function(index,item){  
	       	var $li=$("<li id='"+item.dataId+"'></li>");
	       	var $a=$('<a href="'+item.dataId+ ".html"+'">'+item.text+'</a>');  
	       	$li.append($a);
	 
	       	if(isfirst){
	       	   $li.addClass("selected");
	       	   isfirst=false;
	       	}
	       	// $li.click(function(){
	       	// 	$(this).addClass("selected").siblings(".selected").removeClass("selected");
		       //  window.location.href=$(this).attr("id");
	        // });
	       $(_this.element).append($li);
	      });
	    },
	    show:function(menu){
	    	$(menu).addClass("selected").siblings(".selected").removeClass("selected");
	    },
		init:function(){
			this.buildNavigation();
		}
	};
};