// $.ajax({
// 	cache: false
// });
Header=function(){
	return {
	def:{
		author:"zhanggm2",
		date:"2017-4-27",
		name:"script of OMP page header model"
	},
	 element:"#nav",
	 nav:{
			MMSsecurity:{
				icon:"../images/icon_e.png",
				text:"彩信保障",
				dataId:"MMSsecurity"
			},
			baseMonitor:{
				icon:"../images/icon_f.png",
				text:"基础监控",
				dataId:"baseMonitor"
			},
			appMonitor:{
				icon:"../images/ods_m.png",
				text:"应用监控",
				dataId:"appMonitor"
			},
			speMonitor:{
				icon:"../images/icon_0.png",
				text:"专题监控",
				dataId:"speMonitor"
			},
			confModel:{
				icon:"../images/icon_q.png",
				text:"配置模块",
				dataId:"confModel"
			}
	    },
   buildNavigation:function(){
	   var _this=this;
	   var isfirst=true;
       $(_this.element).empty();
       $(".page-left").css("height",document.documentElement.clientHeight+"px");
       $(".page-right").css("width",document.documentElement.offsetWidth-$(".page-left").width()-10+"px");
       $.each(this.nav,function(k,v){
       	var $li=$("<li class='nav-item' data-id='"+k+"'></li>");
       	var $divImg=$("<div class='nav-item-icon'><img src='"+v.icon+"'></div>");
       	var $divText=$("<div class='nav-item-text'>"+v.text+"</div>");
       	var $divTmp=$("<div style='clear:both;'><div>");
       	$li.append($divImg);
       	$li.append($divText);
       	if(isfirst){
       	   $li.addClass("nav-item-active");
       	   isfirst=false;
       	}
       	$li.click(function(){
	        	$(".nav-item-active").removeClass("nav-item-active");
	        	$(this).addClass("nav-item-active");
	        	window.location.href=$(this).attr("data-id");
            });
       $(_this.element).append($li);
      });
    },
    setOdsStatus:function(status,timeout) {
	 var floor=status;
	 if(timeout=="1")
	 {     
		 floor=99;
	 }
	 var images = {};
	 images[1]="../images/icon_t.png";
	 images[2]="../images/icon_p.png";
	 images[3]="../images/icon_u.png";
	 images[-2]="../images/icon_r.png";
	 images[4]="../images/icon_s.png";
	 images[99]="../images/ods_n.png";
	 return "<img src="+ images[floor]+">";
  },
  dataformat:function(s, n)   
  {   
	 	 if (s=="" || s==null)
	 	 {return "0";}
	    n = n > 0 && n <= 20 ? n : 2;   
	    s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";   
	    var l = s.split(".")[0].split("").reverse(),   
	    r = s.split(".")[1];   
	    t = "";   
	    for(i = 0; i < l.length; i ++ )   
	    {   
	       t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");   
	    }   
	    return t.split("").reverse().join("") + "." + r;   
	 },
    show:function(menu){
    	$(".nav-item-active").removeClass("nav-item-active");
        $(".nav-item[data-id='"+menu+"']").addClass("nav-item-active");
    },
	init:function(){
		this.buildNavigation();
	}
	};
};