$(document).ready(function(){
	//初始化网页顶部导航模块
	var topNav = new topNavigation();
	topNav.init();
	topNav.show("#sysManage");
	//初始化网页左侧导航模块
	var sideNav = new sideNavigation();
	sideNav.init("#sysManagePriority");
	//sideNav.show("#sysManagePriority");
});

$(document).ready(function(){
	//初始化活动列表
	var init = new Init();
	init.getPageData();
	
	//ie8模拟placeholder
	$("#search").val("支持活动名称搜索").css("color","#CCD0D9");
	$("#search").focus(function(){
		if($("#search").val() == "支持活动名称搜索"){
			$(this).val("");
		}
		$(this).css("color","black");
	});
	$("#search").blur(function(){
		if($(this).val() == ""){
			$(this).val("支持活动名称搜索").css("color","#CCD0D9");
			return;
		}
	});
	// 搜索
	$("#search").keypress(function(e) {
		if (e.which == 13) {
			init.getPageData();
		}
	}).next().click(function(e) {
		init.getPageData();
	});
	$.ajaxSetup({
		cache:false
	})
});



//活动列表
var Init = function(){
	var me = this;
	this.getPageData = function(){    //获取表格信息
		var param = {};
		param.currentPage = 1;
		param.pageSize = 10;
		if($("#search").val() == "支持活动名称搜索"){
			param.activityName = "";
		}else{
			param.activityName = $("#search").val();
		}	
		$.ajax({
				url: './priorities/',
				type:'get',
				data:param,
				dataType:'json',
				cache:false,
				success:function(res){
					//console.log(res);
					if(res.code == 0){
						 fillTable(res.data);
						 pagination(res.data.totalPage);
					}else{
						showMessage(res.msg,"error");
					}
		           
				},
				error:function(){
					showMessage("获取数据失败","error");
				} 
				
			})
	};
	var pagination = function(totalPage){  //分页
		var pagerBox = document.getElementById('page');
		$(pagerBox).html("");
		var pager = new Pager({
			index: 1,
			total: totalPage,
			parent: pagerBox,
			onchange: doChangePage
		});
		function doChangePage(obj){
			var param = {};
			param.currentPage = obj.index;
			param.pageSize = 10;
			if($("#search").val() == "支持活动名称搜索"){
				param.activityName = "";
			}else{
				param.activityName = $("#search").val();
			}		
			$.ajax({
				url: './priorities/',
				type:'get',
				data:param,
				dataType:'json',
				cache:false,
				success:function(res){
					if(res.code == 0){
						 fillTable(res.data);
					}else{
						showMessage(res.msg,"error")
					}				
				},
				error:function(){
					showMessage("获取数据失败","error")
				} 
				
			})
		}
	}; 
	var fillTable = function(param){   //填充表格
		var data= param.dataList;
		var html = '';
		$("#activities_num").text("("+ param.totalRecord +")")
		if(!data || data.length == 0){
			$(".panel-bottom tbody").html("<tr><td colspan='7'>暂无数据</td></tr>");	
		}else{
			for(var i=0;i<data.length;i++){
				html += '<tr>'+
						'<td>'+
						'<i></i>'+			
						'</td>'+
						'<td class="text-align-left">'+data[i].activityName+'</td>'+
						'<td>'+data[i].startTime+'</td>'+
						'<td>'+data[i].endTime+'</td>'+
						'<td>'+data[i].createTime+'</td>'+
						'<td>'+data[i].creatorName+ '</td>'+
						'<td data-id="'+data[i].activityId+'">'+				
						'<a href="#" title="置顶" class="operate top-icon"></a>'+				
						'</td>'+
						'</tr>';
			}
			$(".panel-bottom tbody").html(html);
		}	
		initTableStyle();
		initOperateEvent();
		initTableHover();
	};	
	var initTableStyle = function(){ //表格样式
		 /*
		 *所有动态添加列添加背景色
		 */
		var len =  $("#table thead td[data-index]").length;
		 for(var col="col-",eachCol,i=5;i<len+1;i++){	 	
	 		eachCol = col + i;
	 		$("#table td[data-index='col-"+i+"']").find('span').addClass(color[(i-5)%4]); 		
		 }	
		//表格图标添加
		var imgPath = 'img/';
		var icons = [ "activity_red.png","activity_yellow.png", "activity_green.png","activity_blue.png"];
		imgTds = $("#table td>i");
		for(var i=0;i<imgTds.length;i++){ 
			$(imgTds[i]).css("background","url("+imgPath+icons[i%4]+")");
		}		
		var table_h = parseInt($(".inner-content").css("height"))-20-39-40-20;
		$("#table td").css("height",(table_h/11)+"px");
		$("#table tbody tr:odd").addClass("even");

	};
	
	var initOperateEvent = function(){  //表格操作
		$(".top-icon").click(function(){  //置顶
			var camp_id = $(this).parent().attr("data-id");
			var update = function(){    //优先级置顶提交
				//console.log(camp_id)
				$.blockUI.defaults.overlayCSS.backgroundColor = '#fff';
				$.blockUI.defaults.overlayCSS.opacity = '0.8';
				$.blockUI({
					message:"正在进行置顶操作，请稍等...",
					css:{
						border:"none",
						fontSize:"16px"
					}
				});

				$.ajax({
					url: './priorities/',
					type:'post',
					dataType:'json',
					cache:false,
					data:{'activityId':camp_id},
					success:function(res){
						if(res.code == 0){
							$.unblockUI();
							showMessage("操作成功","success",1500);
							//刷新页面
							me.getPageData();
						}else{
							$.unblockUI();
							showMessage(res.msg,"error");
						}
					},
					error:function(){
						$.unblockUI();
						showMessage("置顶失败","error");
					}
				});
			}
			showConfirm("确定要置顶此活动吗？",update);		
		})
	}
}