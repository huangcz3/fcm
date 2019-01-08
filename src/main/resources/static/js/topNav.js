// $.ajax({
// 	cache: false
// });
var topNavigation=function(){
	return {
	def:{
		author:"shaojinyu",
		date:"2017-5-16",
		name:"script of OMP page header model"
	},
	 element:"#headerNav",
	 nav:{
			effectAssessment:{	
				text:"效果评估",
				dataId:"comAssessment",
				url: "comAssessment.html"
			},
			marketActivity:{
				text:"营销活动",
				dataId:"marketingAct",
				url: "vicActManage.html"
			},
			 iopTools:{
				 text:"营销工具",
				 dataId:"iopTools",
				 url: "iopTools.html"
			 },
			/*dgtContent:{

				text:"数字内容",
				dataId:"dgtContent",
				url: "##"
			},
			 tagLib:{
				 text:"标签库",
				 dataId:"tagLib",
				 url: "##"
			 },
			pcc:{
				text:"PCC",
				dataId:"pcc",
				url: "##"
			},
			tacticLib:{
			
				text:"策略库",
				dataId:"tacticLib",
				url: "iopScene.html"
			},
            //zd:{
			//	 text:"驻点顾问",
			//	 dataId:"zd",
			//	 url: "##"
			//},
		 	//hgj:{
			//	 text:"惠购街",
			//	 dataId:"hgj",
			//	 url: "##"
			// },
		 	marketingInsight:{
				 text:"客户洞察",
				 dataId:"marketingInsight",
				 url: "##"
			 },*/
			sysManage:{
				text:"系统管理",
				dataId:"sysManage",
				url: "sysManagePriority.html"
			}
	    },
     templates: {
            helpBox:  '<div class="help rt">'+
                       '<a class="help-icon j-help-icon">帮助</a>'+
                       '<div class="help-drop-box hidden">'+
                       '<b class="connect-arrow"></b>'+
                       '<p>'+
                       '<a href=" ./files/downLoadTemplate?customizeFlag=3" class="help-drop-icon download-docs-icon"></a>'+
                       '<span>使用文档</span>'+
                       '</p>'+
                       '<p>'+
                       '<a href="#" class="help-drop-icon url-icon j-change-browser-url"></a>'+
                       '<span>获取链接</span>'+
                       '</p>'+
                       '</div>'+
                       '</div>',
            urlModal:  '<!--帮助网址-->'+
                        '<div class="float-url-box">'+
                            '<input type="text" class="j-get-url">'+
                            '<b class="j-close-url-box">×</b>'+
                        '</div>'
     },
   buildNavigation:function(){
	   var _this=this;
	   var isfirst=true;
	   var html = "";
       $(_this.element).empty();
    
       $.each(this.nav,function(index,item){  
       	var $li=$("<li id='"+item.dataId+"'></li>");
       	var $a = "";
	   $a=$('<a href="'+item.url+'">'+item.text+'</a>');

       
       	$li.append($a);
 
       	if(isfirst){
       	   $li.addClass("selected");
       	   isfirst=false;
       	}
     //  	 $li.click(function(){
     //  	 	$(this).addClass("selected").siblings(".selected").removeClass("selected");
	  //       window.location.href=$(this).attr("id") + "";
       //  });
       	 $(_this.element).append($li);
      });
       $(".user").find("b[title]").remove();
      $(".nav-wrap").append(_this.templates.helpBox);
       $("body").append(_this.templates.urlModal);
    },

    show:function(menu){
    	$(menu).addClass("selected").siblings(".selected").removeClass("selected");
    },
	init:function(){
		this.buildNavigation();
		$("#dgtContent").click(function(){  //跳转数字内容
			$.ajax({
				url: "./miguContentConsole/token",
				type:"get",
				dataType:"json",
				cache:false,
				success:function(res){
					if(res.code != 0){
						showMessage(res.msg,"error");
						return
					}
					var backData = res.data;
					if(backData.success){
						var urlParam = backData.result;   
						window.open("http://10.113.254.17:8080/migu-content-console/contents/default?sid="+urlParam);
					}
				},
				error:function(){
					
					showMessage("跳转至数字内容页失败","error");
				}
			})
		});
		$("#pcc").click(function(){  //跳转PCC
			$.ajax({
				url: "./pcc/getPccToken",
				type:"get",
				dataType:"json",
				cache:false,
				success:function(res){
					if(res.code != 0){
						showMessage(res.msg,"error");
						return
					}
					var token = res.data;
					window.open("http://10.113.254.17:8080/pop-web/SSOAUTH?token="+token);
				},
				error:function(){
					showMessage("跳转至PCC失败","error");
				}
			})
		});

		$("#zd").click(function(){  //跳转驻点顾问
			$.ajax({
				url: "./zd/driving",
				type:"get",
				dataType:"json",
				cache:false,
				success:function(res){
					if(res.code != 0){
						showMessage(res.msg,"error");
						return
					}
					var param = res.data;
					window.open("http://223.87.19.88:18080/login/loginPcApiSiChuan?param="+param);
				},
				error:function(){
					showMessage("跳转至驻点顾问失败","error");
				}
			})
		});

        $("#hgj").click(function(){  //跳转慧购街模块
            $.ajax({
                url: "./hgj/jumpHGJ",
                type:"get",
                dataType:"json",
                cache:false,
                success:function(res){
                    if(res.code != 0){
                        showMessage(res.msg,"error");
                        return
                    }
                    var param = res.data;
                    window.open("http://10.113.254.17:8080/SmartBuy/?userName="+param);

                    //window.open("http://10.113.254.17:8080/SmartBuy/?userName="+param);
                },
                error:function(){
                    showMessage("跳转至慧购街失败","error");
                }
            })
        });

		$("#tagLib").click(function(){   //跳转标签库
			$.ajax({
				url: "./Coc/loginUrl",
				type:'get',
				cache:false,
				dataType:'json',
				success:function(res){
					if(res.code == 0){
						window.open(res.data);
					}else{
						showMessage(res.msg,'error')
					}
				},
				error:function(){
					showMessage("跳转标签库失败","error")
				}
			})
		});
		$("#marketingInsight").click(function () {
			//获取登录用户id
			$.ajax({
				url: "./manage/getDescInfo",
				type: "get",
				cache: false,
				dataType: "json",
				success: function (res) {
					if (res.code != 0) {
						showMessage(res.msg,'error');
						return
					}
					if( res.data ){
						window.open("http://10.113.254.17:8080/pm_insight/build/throughLogin?PN="+encodeURIComponent(res.data));
					}
				},
				error: function(){
					showMessage("跳转失败","error");
				}
			});
		});
		$("#autoMarketing").click(function(){  //自动化运营跳转
			$.ajax({
				url: "./miguContentConsole/token",
				type:"get",
				dataType:"json",
				cache:false,
				success:function(res){
					if(res.code != 0){
						showMessage(res.msg,"error");
						return
					}
					var backData = res.data;
					if(backData.success){
						var urlParam = backData.result;
						window.open("http://10.113.254.17:8080/migu-content-console/activities/getDefaultPage?sid="+urlParam);
					}
				},
				error:function(){

					showMessage("跳转至数字内容页失败","error");
				}
			})
		});
	}
	};
};
$(function () {
	var currentUUID = "";
	//导航----帮助
	$(document).on("click",function(e){
		if (e.target !== $('.help-drop-box')[0] && !$(e.target).hasClass("help-icon") ) {
			if(!$('.help-drop-box').hasClass("hidden")){
				$('.help-drop-box').addClass("hidden");
			}
		}
	});
	$(".nav-wrap").on("click",".j-help-icon",function (e) {
		e.preventDefault();
		$(".help-drop-box").toggleClass("hidden");
	});
	$(".nav-wrap").on("click",".j-change-browser-url",function () {
		$.ajax({
			url: "./base/getOtherBrowserURL?uuId="+currentUUID,
			type: "get",
			dataType: "json",
			cache: false,
			success: function (res) {
				if( res.code != 0 ){
					showMessage(res.msg,"error");
					return
				}
				currentUUID = res.data.split("=")[1];
				$(".j-get-url").val(res.data);
                $(".float-url-box").show();
			},
			error: function () {
				showMessage("获取链接失败","error");
			}
		})
	});
	$("body").on("click",".j-close-url-box",function (e) {
		e.preventDefault();
		$(".float-url-box").hide().val("");
	});
});
$(document).ready(function () {
    $(".content-wrap").css("height",(document.documentElement.clientHeight-46)+"px");
    var w_main = parseInt($(".content-wrap").css("width"));


});

var userId;
var cityId;
$(document).ready(function () {
	//获取登录用户名
	$.ajax({
		url: "./base/getUserInfo",
		type: "get",
		cache: false,
		dataType: "json",
		async: false,
		success: function (data) {
			if (data.code !== 0) {
				showMessage(data.msg,'error');
				window.location="/fcm/error_info.html";
			}else{
				$("#user").text(data.data.userName || "请登录");
				userId= data.data.userId;
				cityId= data.data.cityId;
				//window.location="/fcm/error_info.html";
			}

		},
		error:function () {
			showMessage("获取当前登录用户名失败","error");
		}
	});
});

function goToSmartMap(taskId,type){
	var postForm = document.createElement("form");

	postForm.method = "post";
	postForm.action = "http://10.113.254.17:8080/mobd-web/page/newTask?taskId="+taskId+"&taskType="+type;
	postForm.target = "_blank";

	var input = document.createElement("input");
	input.setAttribute("name", "city");
	input.setAttribute("value", cityId);
	input.setAttribute("type", "hidden");
	postForm.appendChild(input);

	var input2 = document.createElement("input");
	input2.setAttribute("name", "userId");
	input2.setAttribute("value", userId);
	input2.setAttribute("type", "hidden");
	postForm.appendChild(input2);


	document.body.appendChild(postForm);
	postForm.submit();
	document.body.removeChild(postForm);
}
