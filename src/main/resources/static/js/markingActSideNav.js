
var sideNavigation=function(){
	return {
		def:{
			author:"shaojinyu",
			date:"2017-5-16",
			name:"script of OMP page header model"
		},
		 element:"#sideNav",
		 nav:{
			 	actManagement:{	
					text:"活动管理",
					dataId:"marketingActivities"
				},
				taskManagement:{
					text:"任务管理",
					dataId:"taskManage"
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