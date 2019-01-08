// $.ajax({
// 	cache: false
// });
var sideNavigation=function(){
	return {
		def:{
			author:"shaojinyu",
			date:"2017-5-16",
			name:"script of OMP page header model",
			description:"有权限控制，初始化时.int(menu)时要传入当前menu的id"
		},
		 element:"#sideNav",
		 nav:{
			 sysManagePriority:{
			 	text:"优先级",
			 	dataId:"sysManagePriority"
			 },
			 sysManage10086:{
			 	text:"渠道设置",
			 	dataId:"sysManage10086"
			 },
			 sysManageBWR:{
			 	text:"黑白红查询",
			 	dataId:"sysManageBWR"
			 },
			 /*sysManageCRM:{
			 	text:"CRM前台弹窗",
			 	dataId:"sysManageCRM"
			 },*/
			  sysManageQuota:{
			 	 text:"渠道配额",
			 	 dataId:"sysManageQuota"
			  },
             sysManageAppsetting:{
                 text:"审批人设置",
                 dataId:"sysManageAppsetting"
             },
			 // sysManage10086GroupSend:{
				//  text:"10086群发设置",
				//  dataId:"sysManage10086GroupSend"
			 // },
			 // sysManageSMSApproval:{
			 //  	text:"短信审批设置",
				//  dataId:"sysManageSMSApproval"
			 // }
			 /*sysManagePushQuery:{
				 text:"推送查询",
				 dataId:"sysManagePushQuery"
			 }*/
		 },
		deftRole:"user,approver",
	   buildNavigation:function(data){
		   var _this=this;
		   var isfirst=true;
		   var html = "";
		   var isAdmin = data.indexOf("admin");
		   var isAdmin10086 = data.indexOf("admin10086");
	       $(_this.element).empty();

	       $.each(this.nav,function(index,item){
		    if(item.dataId == "sysManage10086" && isAdmin == "-1"){
				return true;
			}
			if( item.dataId == "sysManageAppsetting" && isAdmin == -1 ){
				return true
			}
			if( item.dataId == "sysManage10086GroupSend" && isAdmin10086 == -1  ){
				return true
			}
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
		init:function(menu){
			var self = this;
			//获取权限
			$.ajax({
				url: " ./manage/getUserRole",
				type:"get",
				dataType:"json",
				cache:false,
				success:function (res) {
					if(res.code != 0){
						showMessage(res.msg,"error");
						self.buildNavigation(self.deftRole);
						self.show(menu);
						return
					}
					var data = res.data.roleInfo;
					if(data){
						self.buildNavigation(data);
						self.show(menu);
					}else{
						self.buildNavigation(self.deftRole);
						self.show(menu);
					}
				},
				error:function(){
					showMessage("获取权限失败了","error");
					self.buildNavigation(self.deftRole);
					self.show(menu);
				}
			});
			//this.buildNavigation();
		}
	};
};