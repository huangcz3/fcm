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
			 	iopScene:{	
					text:"运营场景",
					dataId:"iopScene"
				},
				iopModel:{
					text:"运营模型",
					dataId:"iopModel"
				},
			 	iopCase:{
					text:"运营案例",
					dataId:"iopCase"
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
	       	// 	 $li.click(function(){
	       	// 	 	$(this).addClass("selected").siblings(".selected").removeClass("selected");
		    //      window.location.href=$(this).attr("id");
	      //   });
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