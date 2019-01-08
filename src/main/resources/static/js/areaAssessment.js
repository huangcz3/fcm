
var pageDataList = {};
function getDropList(){
	$.ajax({
		url:'./base/getEvaCityInit',
		type:"get",
		dataType:'json',
		cache:false,
		success:function(res){
			if(res.code != 0){
				showMessage(res.msg,"error");
				return
			}
			res = res.data;
			var areaHtml = '<option id="allArea" value="@">全省</option>';
			var channelHtml = '<option id="allChannel" value="@">全部渠道</option>';
			var area = res.cityOption;
			var channel = res.channelOption;
			//填充地市的下拉列表
			for(var i=0;i<area.length;i++){
				areaHtml += "<option id='"+ area[i].CITYID +"' value='"+ area[i].CITYID +"' >" + area[i].CITYNAME + "</option>"	;
			}
			$("#area").html(areaHtml);
			//填充渠道的下拉列表
			for(var i=0;i<channel.length;i++){
				channelHtml += "<option id='"+ channel[i].CHANNELID +"' value='"+ channel[i].CHANNELID +"' >" + channel[i].CHANNELNAME + "</option>";
			}
			$("#channel").html(channelHtml);
			getPageData();
		},
		error:function(){
			showMessage("出错了！获取数据失败","error");
		}
	});   
}

function getPageData(){    //根据筛选条件，获取页面数据
	var param = {};
	param.areaId = $("#area").val();
	param.time = $("#dateInput").val().split("-").join("");
	param.channelId = $("#channel").val();
	param.filtered = $(".radio-box p input[checked]").val();
	param.page = 1;
	if (pageDataList[param.page]&&
			pageDataList[param.page].isSaved) {
		fillTable(pageDataList[param.page].content);
		return;
	}
	$(".modal").css("display","block");
	$.ajax({
		url:'./areaEvalution/getRegionReportInfo',
		data:param,
		dataType:"json",
		type:"get",
		caches:false,
		success:function(res){
			
			if(res.code == 0){
				res = res.data;
				var prov = res.countIndex;
				var allPage;
				$("#lists").html(res.totals);
				$("#statisticMonth").html(param.time);
				//填充全省统计指标值
				$("#city").html($("#area").find("option:selected").text());
				//填充地址统计指标总值
				$("#statisticChannel").html($("#channel").find("option:selected").text());
				for(var k in prov){
					$("#total_"+k).html(prov[k]);
				}
				allPage = Math.ceil(res.totals/10);
				fillTable(res.cityReportList);
				writeCache(res.cityReportList, 1);
				//分页
				var pagerBox = document.getElementById('page');
				$(pagerBox).html("");
				var pager = new Pager({
					index: 1,      
					total: allPage,
					parent: pagerBox,
					onchange: doChangePage
				});
				$(".modal").css("display","none");
			}else{
				$(".modal").css("display","none");
				showMessage("获取数据异常","error");
			}
		},
		error:function(){
			$(".modal").css("display","none");
			showMessage("出错了！获取数据失败","error");
		}
	});  
}

function fillTable(data){
	var datas = data;
	$("#table tbody").html("");
	if(!datas.length){
		$("#table tbody").append(
				"<tr>"+
				"<td colspan='8'>暂无数据</td>"+		
								
				"</tr>"
				);
	}else{
		for(var i=0;i<datas.length;i++){
			var TASK_NUM = datas[i].TASK_NUM2==undefined ? 0 : datas[i].TASK_NUM2;
			var RESPONES_NUM = datas[i].RESPONSE_NUM==undefined ? 0 : datas[i].RESPONSE_NUM
			$("#table tbody").append(
				"<tr>"+
				"<td><i></i></td>"+
				"<td>"+datas[i].CITY_NAME+"</td>"+
				"<td class='text-align-left'>"+datas[i].CHANNEL_NAME+"</td>"+					
				"<td>"+ TASK_NUM +"</td>"+
				"<td>"+datas[i].ACT_NUM+"</td>"+
				"<td>"+datas[i].OBJ_NUM+"</td>"+
				"<td>"+datas[i].TOUCH_NUM+"</td>"+
				/*"<td>"+ RESPONES_NUM +"</td>"+*/
				"<td><span class='red'>"+datas[i].VIC_NUM+"</span></td>"+			
				"</tr>"
				);
		}
	}

	initTableStyle();
	initTableHover();
}

var initTableStyle = function(){ //表格样式
	/*
	 *所有动态添加列添加背景色
	 */
	var $table = $("#table");
	var len =  $table.find("thead td[data-index]").length;
	for(var col="col-",eachCol,i=5;i<len+1;i++){
		eachCol = col + i;
		$table.find("td[data-index='col-"+i+"']").find('span').addClass(color[(i-5)%4]);
	}
	//表格图标添加
	var imgPath = 'img/';
	var icons = [ "activity_red.png","activity_yellow.png", "activity_green.png","activity_blue.png"];
	imgTds = $table.find("td>i");
	for(var i=0;i<imgTds.length;i++){
		$(imgTds[i]).css("background","url("+imgPath+icons[i%4]+")");
	}
	var table_h = parseInt($(".inner-content").css("height"))-40-20-98-36;
	$table.find("td").css("height",(table_h/11)+"px");
	$table.find("tbody tr:odd").addClass("even");
};

//页码点击事件
function doChangePage(obj){
	var param = {};
	param.areaId = $("#area").val();
	param.time = $("#dateInput").val().split("-").join("");
	param.channelId = $("#channel").val();
	param.filtered = $(".radio-box p input[checked]").val();
	param.page = obj.index;    
	
	$(".modal").css("display","block");
	if (pageDataList[param.page] && 
			pageDataList[param.page].isSaved){
		fillTable(pageDataList[param.page].content);
		$(".modal").css("display","none");
		return;
	}
	$.ajax({
		url:'./areaEvalution/getRegionReportInfo',
		data:param,
		dataType:"json",
		type:"get",
		cache:false,
		success:function(res){
			if(res.code == 0){
				res = res.data;
				fillTable(res.cityReportList);
				writeCache(res.cityReportList, param.page);
				$(".modal").css("display","none");
			}else{
				$(".modal").css("display","none");
				showMessage("获取数据异常","error");
			}
		},
		error:function(){
			$(".modal").css("display","none");
			showMessage("获取数据失败","error");
		}
	})
	
}

function writeCache (data, page) {
	pageDataList[page] = {};
	pageDataList[page].isSaved = true;
	var temp = pageDataList[page]["content"] = [];
	for (var i = 0, len = data.length; i < len; ++i) {
		temp.push({
			CITY_NAME		: data[i].CITY_NAME,
			CHANNEL_NAME: data[i].CHANNEL_NAME,
			TASK_NUM    : data[i].TASK_NUM2,
			ACT_NUM			: data[i].ACT_NUM,
			OBJ_NUM			: data[i].OBJ_NUM,
			TOUCH_NUM		: data[i].TOUCH_NUM,
			RESPONES_NUM: data[i].RESPONSE_NUM,
			VIC_NUM			: data[i].VIC_NUM
		});
	}
	// console.log(temp)
}

$("#ensure").click(function(){
	$("#filter-btn").removeClass("opened");
	$(".filter-panel").css("display","none");
	pageDataList = {};	//修改过滤值时需要清空缓存。
	$("#table tbody").html("");
	getPageData();
});
$("#download").click(function(){
	//数据下载
	var param = {};
	param.areaId = $("#area").val();
	param.time = $("#dateInput").val().split("-").join("");
	param.channelId = $("#channel").val();
	param.filtered = $(".radio-box p input[checked]").val();
	param.page = 1;
	//window.location.href = 'http://10.113.254.17:8080/qcd/getRegionReportDownload?time='+param.time+'&areaId='+param.areaId+'&channelId='+param.channelId+'&filtered='+param.filtered;
	window.location.href = 'http://10.113.254.17:8080/fcm/areaEvalution/getRegionReportDownload?time='+param.time+'&areaId='+param.areaId+'&channelId='+param.channelId+'&filtered='+param.filtered;
});

$(document).ready(function(){
	//初始化网页顶部导航模块
	var topNav = new topNavigation();
	topNav.init();
	topNav.show("#areaAssessment");
	//初始化网页左侧导航模块
	var sideNav = new sideNavigation();
	sideNav.init();
	sideNav.show("#areaAssessment");
});

$(document).ready(function(){

	//页面排版
	var wraper_w = parseInt($(".panel-mid").css("width"));
	$(".grid-wrap ul").css("width",((wraper_w-270)/5-1)+"px");
	   		
	
	
	//默认上月
	var now = new Date();
	var deft = formatDate(now,"yyyy-MM");	
	var times = deft.split("-");  		
	if(times[1] == "01"){
		deft = (parseInt(times[0])-1)+"-12"
	}else{
		var month = (parseInt(times[1])-1).toString();
		if(month.length == 1){
			deft = times[0]+"-0"+(parseInt(times[1])-1);
		}else{
			deft = times[0]+"-"+(parseInt(times[1])-1);
		}		
	}	    		
	$("#dateInput").val(deft);
	
	getDropList();
	
});