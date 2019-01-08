
var sideNavigation=function(){
	return {
		def:{
			author:"shaojinyu",
			date:"2017-5-16",
			name:"script of OMP page header model"
		},
		 element:"#sideNav",
		 nav:{
			 	comAssessment:{	
					text:"综合评估",
					dataId:"comAssessment",
					url:"comAssessment.html"
				},
				activityAssessment:{
					text:"活动评估",
					dataId:"activityAssessment",
					url:"activityAssessment.html"
				},
				areaAssessment:{
					text:"地市评估",
					dataId:"areaAssessment",
					url:"areaAssessment.html"
				},
				 channelAssessment:{
					 text:"渠道评估",
					 dataId:"channelAssessment",
					 url:"channelAssessment.html"
				 },
			    taskAssessment:{
				    text:"任务评估",
				    dataId:"taskAssessment",
				    url:"taskAssessment.html"
			    },
			 	redSeaReport:{
				 text:"红海行动",
				 dataId:"redSeaReport",
				 url:"redSeaReport.html"
			 }
		    },
	   buildNavigation:function(){
		   var _this=this;
		   var isfirst=true;
		   var html = "";
	       $(_this.element).empty();
	    
	       $.each(this.nav,function(index,item){  
	       	var $li=$("<li id='"+item.dataId+"'></li>");
	       	var $a=$('<a href="'+item.url+'">'+item.text+'</a>');  
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