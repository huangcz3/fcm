var icons = [ "activity_red.png","activity_yellow.png", "activity_green.png","activity_blue.png"];
var taskIcons = ["file_red.png","file_yellow.png","file_green.png","file_blue.png"];
var imgPath = "./img/";
var popList={};
var taskPopList = {};
var taskSubActs = {};
var temp = {};
var echart = {};
echart.option = {
	tooltip: {
		trigger: "axis",
	},
	legend: {
		x: "center",
		textStyle: {
			color: "#758494"
		},
		//暂时只有两个指标？？？？默认只展示三个指标数据折线，点击图例可查看其它指标
		//selected:{},
		//默认绘制所有指标
		data: []
	},
	calculable:false,
	dataZoom : {
		type:"slider",
		show : true,
		realtime : true,
		//orient: 'vertical',   // 'horizontal'
		//x: 0,
		//y: 36,
		//width: 400,
		height: 20,
		backgroundColor: 'rgba(247,249,252,0.5)',
		//dataBackgroundColor: 'rgba(138,43,226,0.5)',
		//fillerColor: 'rgba(38,143,26,0.6)',
		//handleColor: 'rgba(128,43,16,0.8)',
		handleSize:15,
		//xAxisIndex:[],
		//yAxisIndex:[],
		start : 0,
		end : 100
	},
	xAxis: {
		axisLine: {
			lineStyle: {
				color: "#000"
			}
		},
		type: "category",
		name: "日期",
		boundaryGap: true,
		data: [] //类目型，横坐标的数据
	},
	yAxis: [{
		axisLine: {
			lineStyle: {
				color: "#000"
			}
		},
		splitLine: {
			show: true
		},
		type: "value",
		name: "人数"
	}],
	//动态插入值
	series: []
}
echart.drawChart = function (xLabelData,seriesData) {
	this.option.xAxis.data = xLabelData;
	this.option.series = seriesData;
	this.myChart.setOption(this.option,true);
}
echart.resizeWorldMapContainer = function () {
	var windowWidth = (window.innerWidth*0.77)+'px';
	$(".chart-tab").css("width",windowWidth) ;
};
var getChartInfo = (function () {
	var result = new Object();
	result.getChartParam = function(){
		var param = {};
		var tags = $("#modalTags ul li.selected").find("span");
		var tagArr = [];
		var legendArr = [];
		param.channelId = $("#channelDrop").val();
		$.each(tags,function (i, tag) {
			tagArr.push($(tag).attr("id"));
			legendArr.push($(tag).attr("title"));
		})
		param.legendArr = legendArr;
		param.tagStr = tagArr.join(",");
		return param;
	}
	result.requestChartData = function (actid) {
		//var param = this.getChartParam();
		var param = {};
        param.indexs = "接触量-TOUCH_NUM,营销成功量-VIC_NUM";
		param.activityId = actid;
		$.ajax({
			url: "./activityEvalution/getActEvalutionDetail",
			type:"get",
            data:param,
			dataType:"json",
			cache:false,
			success:function (res) {
				if(res.code != 0 ){
					showMessage(res.msg,"error");
					return
				}
				//填充图表
				var xLabelData = [],sData=[];
				var dataInfo = res.data.data_info;
				$.each(res.data.date_info,function (i, dateStr) {
					xLabelData.push(transToDate(dateStr));
				});
				$.each(dataInfo,function(i,item){
					sData.push({
						name:item.name,
						type:"line",
						data:item.info
					})
				})

				echart.drawChart(xLabelData,sData);
			},
			error:function () {
				showMessage("获取报表信息失败","error");
			}
		})
	}
	return result;
})();


$(document).ready(function(){
	//初始化网页顶部导航模块
	var topNav = new topNavigation();
	topNav.init();
	topNav.show("#activityAssessment");
	//初始化网页左侧导航模块
	var sideNav = new sideNavigation();
	sideNav.init();
	sideNav.show("#activityAssessment");
});

$(document).ready(function(){

	//页面排版
	var wraper_w = parseInt($(".panel-mid").css("width"));
	$(".grid-wrap ul").css("width",((wraper_w-270)/4-1)+"px");
	
	//ie8模拟placeholder
	$("#search").val("支持活动名称、编号、创建人搜索").css("color","#CCD0D9");
	$("#search").focus(function(){
		if($("#search").val() == "支持活动名称、编号、创建人搜索"){
			$(this).val("");
		}
		$(this).css("color","black");
	});
	$("#search").blur(function(){
		if($(this).val() == ""){
			$(this).val("支持活动名称、编号、创建人搜索").css("color","#CCD0D9");
			return;
		}
	});
	
	/*指标选择*/
	$("#selectedTags").tagsInput({
		'width':'260px',
		'height':'70px',
		'defaultText':'', //默认文字
		'interactive':false,
		'onAddTag':function(tag){		
			var spans = $("#selectedTags_tagsinput span.tag>span"),txt;
			for(var i=0;i<spans.length;i++){
				txt = (typeof spans[i].textContent == "string")?spans[i].textContent : spans[i].innerText;
				if(spans[i].innerText.length>15){
					$(spans[i]).addClass('text-over-length');
					$(spans[i]).attr('title',txt);
				}
			}
			
		},
		'onRemoveTag': removeTag,
	});
	disableRemoveTag();    //三个默认指标不可删除
	
	/*指标选择列表滚动条*/
	$("#tags").slimScroll({
		height:'160px',
		color:"#DCE0E8",
		railColor:"#F0F3F8",
		size:"6px",
		distance:"5px",
		railVisible: true,
		alwaysVisible: true
	})	
	
	getDropLists();		
	initTableHover();

	echart.resizeWorldMapContainer();
	echart.myChart = echarts.init(document.getElementById("echartTab"));
	/*window.onresize = function () {
		echart.resizeWorldMapContainer();
		echart.myChart.resize;
	}*/
});

//比较日期大小 "2007-2-2"
function CompareDate(d1,d2)
{
	return ((new Date(d1.replace(/-/g,"\/"))) > (new Date(d2.replace(/-/g,"\/"))));
}

function getDropLists(){
	$.ajax({
		url:'./base/getCityChanelIndexInit',
		type:'get',
		dataType:'json',
		cache:false,
		success:function(res){
			if(res.code != 0){
				showMessage(res.msg,"error");
				return
			}
			var areaHtml = '<option id="allArea" value="@">全省</option>';
			var channelHtml = '<option id="allChannel" value="@">全部渠道</option>';
			var projectHtml = "";
			res = res.data;
			var area = res.cityOption;
			var channel = res.channelOption;
			var project = res.indexOption;
			//填充地市的下拉列表
			for(var i=0;i<area.length;i++){
				areaHtml += "<option id='"+ area[i].CITYID +"' value='"+ area[i].CITYID +"' >" + area[i].CITYNAME + "</option>"	;
			}
			$("#area").html(areaHtml);
			//填充渠道的下拉列表
			for(var i=0;i<channel.length;i++){
				channelHtml += "<option id='"+ channel[i].CHANNELID +"' value='"+ channel[i].CHANNELID +"' >" + channel[i].CHANNELNAME + "</option>"
			}
			$("#channel").html(channelHtml);

			//筛选-设置默认统计周期为当月1日到当日的前3天;
			var now = new Date();
			var today = formatDate(now);
			var sDate;
			var endDate = new Date(now.getTime() - 3*24*3600*1000);
			endDate = formatDate(endDate);
			sDate = new Date(now.setDate(1));
			sDate = formatDate(sDate);
			if(CompareDate(endDate,sDate)){
				$("#startDate").val(sDate);
				$("#endDate").val(endDate);
			}else{
				$("#startDate").val(sDate);
				$("#endDate").val(today);
			}

			var tagArr = [];
			var selectLegend = {};
			//填充展示指标的下拉列表
			for(var  i=0;i<project.length;i++){
				projectHtml += "<li>"+
					"<span title='"+project[i].INDEXNAME+"' class='"+project[i].INDEXNO+"' id="+ project[i].INDEXNO +">"+project[i].INDEXNAME+"</span>"+
					"<i></i>"+
					"</li>";
				tagArr.push(project[i].INDEXNAME);
				if( project[i].INDEXNAME == "目标用户数" || project[i].INDEXNAME == "接触量" || project[i].INDEXNAME == "营销成功量" ){
					selectLegend[project[i].INDEXNAME] = true;
				}else {
					selectLegend[project[i].INDEXNAME] = false;
				}
			}
			/*展示所有指标？？？？暂时物全部指标数据，只展示营销成功量和接触量这两个指标
			echart.option.legend.data = tagArr;
			echart.option.legend.selected = selectLegend;
			*/
            echart.option.legend.data = ["营销成功量","接触量"];

			$("#tags ul").html(projectHtml);
			//$("#modalTags ul").html(projectHtml);
			$(".obj_num,.touch_num,.VIC_NUM").parent().addClass("selected");
			initTagEvent();
			getActData();
			//getTaskData();     //初始化任务列表
		},
		error:function(){
			showMessage("出错！未获取到信息","error");
		}
	});  
}

function getParams(){
	var param = {};
	var _tags = [];
	var _tagNames = [];
	var tagItem = {};
	var tags = $("#tags ul li.selected").find("span");
	if($("#search").val() == "支持活动名称、编号、创建人搜索"){
		param.searchTxt = "";
	}else{
		param.searchTxt = $("#search").val();
	}
	param.areaId = $("#area").val();
	param.areaName = $("#area").find("option:selected").text();
	param.stime = $("#startDate").val().split("-").join("");
	param.endtime = $("#endDate").val().split("-").join("");
	param.channel = $("#channel").val();
	param.channelName = $("#channel").find("option:selected").text()

	for(var  i=0;i<tags.length;i++){
		_tags.push(tags[i].id);
		_tagNames.push($("#"+tags[i].id).html());
	}
	param.tags = _tags;	
	param.tagStr = _tags.join(",");
	param.tagNameStr = _tagNames.join(",");
	return param;
}

function getActData(){  
	var params = getParams();
	params.page = 1;
	$.ajax({
		url:'./activityEvalution/getActEvaList',
		type:'get',
		data:params,
		dataType:"json",
		cache:false,
		success:function(res){
			if(res.code != 0){
				showMessage(res.msg,"error");
				return;
			}
			res = res.data;
			var totalData = res.totals || 0;
			var totalPage = Math.ceil(totalData/10);
			var txt = "";
			var actList = res.actEvaList;
			temp = actList;
			txt = "\""+params.areaName+"\" \""+$("#startDate").val()+"~"+$("#endDate").val()+"\" \""+params.channelName+"\"等相关搜索结果"+totalData+"条"
			$("#activities_num").html(totalData);
			$("#activities .searchResult").html(txt);
			fillTable("#table",actList);
			
			var cols = $("#activities thead").find("[data-index]");
			var slide = new slideTable({
				firstIndex:1,   //第一格可见的列的index值
				displayCols:7,    //可见的列的个数，必须
				totalCols: cols.length,   //总计列数，必须,
				table:"#table"
			});			
			//分页		
			var pagerBox = document.getElementById('page1');
			$(pagerBox).html("");
			var pager = new Pager({
				index: 1,
				total: totalPage,
				parent: pagerBox,
				onchange: doChangeActPage
			});
		},
		error:function(){
			showMessage("出错了！未获取到数据","error");
		}
	})

}
//页码切换事件
function doChangeActPage(obj){
	var params = getParams();
	params.page = obj.index;
	$.ajax({
	 	url:'./activityEvalution/getActEvaList',
	 	data:params,
	 	dataType:"json",
	 	type:"get",
		cache:false,
	 	success:function(res){
	 		var cols = $("#activities thead").find("[data-index]");
	 		if(res.code != 0){
				showMessage(res.msg,"error");
				return;
			}
			res = res.data;
	 		fillTable("#table",res.actEvaList);
		
			var slide = new slideTable({
				firstIndex:1,   //第一格可见的列的index值
				displayCols:7,    //可见的列的个数，必须
				totalCols: cols.length,   //总计列数，必须,
				table:"#table"
			});
	 	},
	 	error:function(){
	 		$(".modal").css("display","none");
	 		showMessage("获取数据失败","error");
	 	}
	 })
}

//填充活动列表内容
function fillTable(table,data){
	var touch = {};
	var vic = {};
	var html = "";
	var theadHtml = "<td class='width-5'>&nbsp;</td>"+
						"<td class='width-13 text-align-left' >活动编号</td>"+
						"<td class='width-10 text-align-left'>活动名称</td>"+
						"<td class=''>地市</td>"+
						"<td class='text-align-left' >执行渠道</td>"+
						"<td class='pre-td slide-btn'>&lt;</td>"+
						"<td class='hide'  data-index='col-1'>创建人</td>"+
						"<td class=' hide' data-index='col-2'>创建时间</td>"+
						"<td class=' hide' data-index='col-3'>开始时间</td>"+
						"<td class=' hide' data-index='col-4'>结束时间</td>";
	var tags = $("#tagsBox ul li.selected").find("span");
	var color = ['red','orange','green','blue'];
	
	for(var i=0;i<tags.length;i++){	
		theadHtml += "<td class='width-9 hide text-align-right' data-index='col-"+(i+5)+"'>"+tags[i].innerHTML+"</td>";
	}
	theadHtml = theadHtml+"<td class='next-td slide-btn'>&gt;</td>"+"<td class='width-5'>操作</td>";;
	$(table+" thead tr").html(theadHtml);     //动态生成表格中thead

	var datas = data;
	var cols = $("#activities thead").find("[data-index]");
	if(!datas){
		$("#activities tbody").html(
			"<tr>"+
			"<td colspan='15'>暂无数据</td>"+

			"</tr>"
		);
	}else{
		for(var i=0;i<datas.length;i++){    //添加表格行数据
			html += "<tr>"+
				"<td><i></i></td>"+
				"<td class='text-align-left' data-id='"+datas[i].CAMP_ID+"' title='"+datas[i].CAMP_ID+"'>"+datas[i].CAMP_ID+"</td>"+
				"<td class='font-highlight text-align-left' title='"+datas[i].CAMP_NAME+"'>"+datas[i].CAMP_NAME+"</td>"+
				"<td title='"+datas[i].CITY_NAME+"'>"+datas[i].CITY_NAME+"</td>"+
				"<td class='text-align-left' title='"+datas[i].CHANNEL_NAME+"'>"+datas[i].CHANNEL_NAME+"</td>"+
				"<td>&nbsp;</td>"+
				"<td title='"+datas[i].CREATE_NAME+"' data-index='col-1'>"+datas[i].CREATE_NAME+"</td>"+
				"<td title='"+datas[i].CREATE_TIME+"' data-index='col-2'>"+datas[i].CREATE_TIME+"</td>"+
				"<td title='"+datas[i].START_TIME+"' data-index='col-3'>"+datas[i].START_TIME+"</td>"+
				"<td title='"+datas[i].END_TIME+"' data-index='col-4'>"+datas[i].END_TIME+"</td>";
			if($("#channel").val() == "@"){
				var key = datas[i].CAMP_ID;
				
				touch[key] = datas[i]['TOUCH_NUM']['TOUCHLIST'];
				vic[key] = datas[i]['VIC_NUM']['VICLIST'];
			
				for(var j=0;j<tags.length;j++){	   //根据展示指标，动态添加表格指标列
					var tmp = tags[j].id;
					var tag = tmp.toUpperCase();
	
					if(tags[j].id == "touch_num" ){	 
						html += "<td class='hide text-align-right' data-role='TOUCH_NUM' data-index='col-"+(j+5)+"'><span>"+datas[i][tag]['ALLTOTAL']+"<b class='pop-btn'></b></span></td>";	
									
						
					}else if(tags[j].id == "VIC_NUM"){
						html += "<td class='hide text-align-right'  data-role='VIC_NUM'  data-index='col-"+(j+5)+"'><span>"+datas[i][tag]['ALLTOTAL']+"<b class='pop-btn'></b></span></td>";					
					
					}else{
						html += "<td class='hide text-align-right' data-index='col-"+(j+5)+"'><span>"+datas[i][tag]+"</span></td>";		
					}							
				}
			}else{
				for(var j=0;j<tags.length;j++){	   //根据展示指标，动态添加表格列
					var tmp = tags[j].id;
					var tag = tmp.toUpperCase();
					if(tags[j].id == "touch_num" || tags[j].id == "VIC_NUM"){	 
						html += "<td class='hide' data-index='col-"+(j+5)+"'><span>"+datas[i][tag]['ALLTOTAL']+"</span></td>";
					}else{
						html += "<td class='hide' data-index='col-"+(j+5)+"'><span>"+datas[i][tag]+"</span></td>";
					}
																		
				}
			}
			
			html +=	"<td>&nbsp;</td>"+
				"<td><a href='#' title='报表' class='operate detail-modal-icon' data-id='"+datas[i].CAMP_ID+"' ></a></td>"+
				"</tr>";
		}

		popList.touchList = touch;
		popList.vicList = vic;
		
		$(table+" tbody").html(html);
	}
	initTableHover();
	initOperateBtn();

	/*
	 *所有动态添加列添加背景色
	 */
	var len =  $(table+" thead td[data-index]").length;
	 for(var col="col-",eachCol,i=5;i<len+1;i++){	 	
 		eachCol = col + i;
 		$(table+" td[data-index='col-"+i+"']").find('span').addClass(color[(i-5)%4]); 		
	 }
	
	//表格图标添加
	imgTds = $(table+" td>i");
	for(var i=0;i<imgTds.length;i++){ 
		$(imgTds[i]).css("background","url("+imgPath+icons[i%4]+")");
	}
	
	var table_h = parseInt($(".inner-content").css("height"))-20-39-40-40;
	$("#table td").css("height",(table_h/11)+"px");
	$("#table tbody tr:odd").addClass("even");
	if($("#channel").val() == "@"){
		initPopEvent("#activities",popList.touchList,popList.vicList);
		return;
	}	
}

///任务列表  -- 暂时隐藏
function getTaskData(){
	var params = getParams();
	params.page = 1;
		$.ajax({
			url:'http://10.108.226.92:9999/getTaskInfo',
			type:'get',
			data:params,
			dataType:"json",
			cache:false,
			success:function(res){
				if(res,code != 0){
					showMessage(res.msg,"error");
					return
				}
				res = res.data;
				var totalData = res.totals || 0;
				var totalPage = Math.ceil(totalData/10);
				var txt = "";
				txt = "\""+params.areaName+"\" \""+$("#startDate").val()+"~"+$("#endDate").val()+"\" \""+params.channelName+"\"等相关搜索结果"+totalData+"条"
				taskSubActs = res.subActs;
				$("#tasks_num").html(totalData);
				$("#tasks .searchResult").html(txt);
				
				fillTaskTable("#table_t",res);
				initExpandBtn();
				taskPopList.vicList = res.vicList;
				taskPopList.touchList = res.touchList;
				
				
				var slide = new slideTable({
					firstIndex:1,   //第一格可见的列的index值
					displayCols:8,    //可见的列的个数，必须
					totalCols: 8 ,   //总计列数，必须,
					table:"#table_t"
				});
				var slide = new slideTable({
					firstIndex:1,   //第一格可见的列的index值
					displayCols:8,    //可见的列的个数，必须
					totalCols: 8 ,   //总计列数，必须,
					table:"#table_t_head"
				});
				
				//分页				
				var pagerBox = document.getElementById('page2');
				$(pagerBox).html("");
				var pager = new Pager({
					index: 1,
					total: totalPage,
					parent: pagerBox,
					onchange: doChangeTaskPage
				});
			},
			error:function(){
				showMessage("获取数据失败","error");
			}
		})
	
}
//分页
function doChangeTaskPage(obj){

}

//填充任务列表内容   -- 暂时隐藏
function fillTaskTable(table,data){
	var html = "";
	var theadHtml = "<td class='width-5'>&nbsp;</td>"+
					"<td class='width-6' >任务编号</td>"+
					"<td class='width-8'>任务名称</td>"+
					"<td class='width-5'>地市</td>"+
					"<td class='pre-td slide-btn'>&lt;</td>"+
					"<td class='width-6 hide'  data-index='col-1'>创建人</td>"+
					"<td class='width-8 hide' data-index='col-2'>创建时间</td>"+
					"<td class='width-8 hide' data-index='col-3'>开始时间</td>"+
					"<td class='width-8 hide' data-index='col-4'>结束时间</td>"+
					"<td class='width-20 hide' data-index='col-5'>执行渠道</td>";
	var tags = $("#tagsBox ul li.selected").find("span");
	var color = ['red','orange','green','blue'];
	
	for(var i=0;i<tags.length;i++){	
		theadHtml += "<td class='hide' data-index='col-"+(i+6)+"'>"+tags[i].innerHTML+"</td>";							
	}
	theadHtml = theadHtml+"<td class='next-td slide-btn width-1'>&gt;</td>";
	$("#table_t_head thead tr").html(theadHtml);
	$(table+" thead tr").html(theadHtml);     //动态生成表格中thead
	
	var datas = data.tableData;
	for(var i=0;i<datas.length;i++){	
		html += "<tr>"+
			"<td class='width-5'><b class='closed'></b><i></i></td>"+
			"<td class='width-6' data-id='"+datas[i].activityId+"'>"+datas[i].activityId+"</td>"+
			"<td class='font-highlight width-8'>"+datas[i].activityName+"</td>"+
			"<td class='width-5'>"+datas[i].areaName+"</td>"+
			"<td class='width-1'>&nbsp;</td>"+
			"<td class='width-6' data-index='col-1'>"+datas[i].createrName+"</td>"+
			"<td class='width-8' data-index='col-2'>"+datas[i].createTime+"</td>"+	
			"<td class='width-8' data-index='col-3'>"+datas[i].startTime+"</td>"+
			"<td class='width-8' data-index='col-4'>"+datas[i].endTime+"</td>"+
			"<td class='text-align-left width-20'  data-index='col-5' title='"+datas[i].channel+"'>"+datas[i].channel+"</td>";
				
			if($("#channel").val() == "@"){
				for(var j=0;j<tags.length;j++){	   //根据展示指标，动态添加表格列
					if(tags[j].id == "touch_num" ){			
						html += "<td class='hide' data-role='touch_num' data-index='col-"+(j+6)+"'><span>"+datas[i][(tags[j].id)]+"<b class='pop-btn'></b></span></td>";					
					}else if(tags[j].id == "VIC_NUM"){
						html += "<td class='hide' data-role='vic_num' data-index='col-"+(j+6)+"'><span>"+datas[i][(tags[j].id)]+"<b class='pop-btn'></b></span></td>";
					}else{
						html += "<td class='hide' data-index='col-"+(j+6)+"'><span>"+datas[i][(tags[j].id)]+"</span></td>";		
					}							
				}
			}else{
				for(var j=0;j<tags.length;j++){	   //根据展示指标，动态添加表格列
					html += "<td class='hide' data-index='col-"+(j+6)+"'><span>"+datas[i][(tags[j].id)]+"</span></td>";													
				}
			}
			
			html +=	"<td class='width-1'>&nbsp;</td>"+
					"</tr>";
	}	
	$(table+" tbody").html(html);
	//表格图标添加
	imgTds = $(table+" td>i");
	for(var i=0;i<imgTds.length;i++){ 
		$(imgTds[i]).css("background","url("+imgPath+taskIcons[i%4]+")");
	}
	
	//start添加子活动
	var tds = $("#table_t td[data-id]");
	var ids = [];
	for(var i=0;i<tds.length;i++){
		ids.push($(tds[i]).attr("data-id"));
	}
	for(var k=0;k<ids.length;k++){
		var id = ids[k];		
		if(taskSubActs[id]){			
			var subAct = taskSubActs[id];
			var html = "";
			for(var i=0;i<subAct.length;i++){          //动态添加活动下任务列表
				html += "<tr class='hidden' data-parent='"+id+"'>"+
				"<td class='width-5'><i></i></td>"+
				"<td class='width-6' data-id='"+subAct[i].activityId+"'>"+subAct[i].activityId+"</td>"+
				"<td class='font-highlight width-8'>"+subAct[i].activityName+"</td>"+
				"<td class='width-5'>"+subAct[i].areaName+"</td>"+
				"<td class='width-1'>&nbsp;</td>"+
				"<td class='width-6' data-index='col-1'>"+subAct[i].createrName+"</td>"+
				"<td class='width-8' data-index='col-2'>"+subAct[i].createTime+"</td>"+	
				"<td class='width-8' data-index='col-3'>"+subAct[i].startTime+"</td>"+
				"<td class='width-8' data-index='col-4'>"+subAct[i].endTime+"</td>"+
				"<td class='text-align-left width-20'  data-index='col-5' title='"+subAct[i].channel+"'>"+subAct[i].channel+"</td>";
				
				if($("#channel").val() == "@"){
					for(var j=0;j<tags.length;j++){	   //根据展示指标，动态添加表格列
						if(tags[j].id == "touch_num" ){			
							html += "<td class='hide' data-role='touch_num' data-index='col-"+(j+6)+"'><span>"+datas[i][(tags[j].id)]+"<b class='pop-btn'></b></span></td>";					
						}else if(tags[j].id == "VIC_NUM"){
							html += "<td class='hide' data-role='vic_num' data-index='col-"+(j+6)+"'><span>"+datas[i][(tags[j].id)]+"<b class='pop-btn'></b></span></td>";
						}else{
							html += "<td class='hide' data-index='col-"+(j+6)+"'><span>"+datas[i][(tags[j].id)]+"</span></td>";		
						}							
					}
				}else{
					for(var j=0;j<tags.length;j++){	   //根据展示指标，动态添加表格列
						html += "<td class='hide' data-index='col-"+(j+6)+"'><span>"+subAct[i][(tags[j].id)]+"</span></td>";													
					}
				}
				html +=	"<td class='width-1'>&nbsp;</td>"+
						"</tr>";
			}
			$("[data-id='"+id+"']").parent("tr").after(html);
			
			//表格添加图标
			var imgTds = $("#table_t [data-parent='"+id+"']").find("i");
			for(var i=0;i<imgTds.length;i++){ 
				$(imgTds[i]).css("background","url("+imgPath+icons[i%4]+")");
				$(imgTds[i]).parent().css("text-align","right");
			}			 
			 //所有动态添加列添加背景色			 
			var len =  $("#table_t thead td[data-index]").length;
			 for(var col="col-",eachCol,i=6;i<len+1;i++){	 	
		 		eachCol = col + i;
		 		$("#table_t td[data-index='col-"+i+"']").find('span').addClass(color[(i-6)%4]); 		
			 }			 
		}
		//end添加子活动		
	}
	
	$(table+" tbody td:last-child").css("border-bottom","none");
	
	 /*
	 *所有动态添加列添加背景色
	 */
	var len =  $(table+" thead td[data-index]").length;
	 for(var col="col-",eachCol,i=6;i<len+1;i++){	 	
 		eachCol = col + i;
 		$(table+" td[data-index='col-"+i+"']").find('span').addClass(color[(i-6)%4]); 		
	 }
	 
	initPopEvent("#tasks");

	 var table_h = parseInt($(".inner-content").css("height"))-20-39-40-40;
	$("#tasks .tbody-box").css("height",table_h+"px");
	$(".table td").css("height",(table_h/11)+"px");
	$("#table_t tbody tr:not([class='hidden']):odd").addClass("even");
	
	initSlideBtn();
	
	/*滚动条*/
	$("#tasks .tbody-box").slimScroll({
		height:table_h,
		color:"#DCE0E8",
		railColor:"#F0F3F8",
		size:"10px",		
		railVisible: true,
		alwaysVisible: true
	})
}

function initSlideBtn(){
	$("#table_t_head .pre-td").click(function(){
		$("#table_t .pre-td").trigger("click");
	});
	$("#table_t_head .next-td").click(function(){
		$("#table_t .next-td").trigger("click");
	})
}

function initExpandBtn(){
	$("#table_t b").click(function(){	
		var id = $(this).parent().siblings("[data-id]").attr("data-id");
		if($(this).hasClass("closed")){
			var tags = $("#tagsBox ul li.selected").find("span");
			$(this).removeClass("closed").addClass("opened");
			$("#table_t [data-parent='"+id+"']").removeClass("hidden");	
			$("#table_t tbody tr").removeClass("even");
			$("#table_t tbody tr:not([class='hidden']):odd").addClass("even");
		}else{
			$(this).removeClass("opened").addClass("closed");			
			$("#table_t [data-parent='"+id+"']").addClass("hidden");			
		}
	});
}

function initOperateBtn(){
	//报表查询按钮事件
	$(".detail-modal-icon").click(function(e){
		e.preventDefault();
		$(".modal-wrap-detail").show();
		var actId = $(this).attr("data-id");
		/*if(temp){
			$("#channelDrop").html("");
			$.each(temp,function(i,item){
				if(item.CAMP_ID == actId){
					xMin = new Date(transToDate(item.START_TIME));
					xMax = new Date(transToDate(item.END_TIME));
					$("#channelDrop").append("<option id='"+item.CHANNEL_ID+"'>"+item.CHANNEL_NAME+"</option>");
					getChartInfo.requestChartData(actId,xMin,xMax);
				}
			});
		}*/
		getChartInfo.requestChartData(actId);
	})
}

function removeTag(tag){  //指标下拉列表取消选中
	var tags = $("#tagsBox ul li span");
	for(var i=0; i<tags.length;i++){
		if( tags[i].innerHTML == tag){
			$(tags[i]).parent().removeClass("selected");
		}
	}
	disableRemoveTag();
}

function disableRemoveTag(){
	var tags = $("#selectedTags_tagsinput>span>span");
	for(var i=0;i<tags.length;i++){
		var txt = (tags[i].innerHTML.split("&"))[0];
		if(txt=="目标用户数"||txt=="接触量"||txt=="营销成功量"){
			$(tags[i]).siblings("a").remove();
		}
	}
}

$("#ensure").click(function(){
	$("#filter-btn").removeClass("opened");
	$(".filter-panel").css("display","none");
	var sel = $(".panel-title>span.selected").find("a").attr("href");
	if(sel == "#activities"){
		getActData();
	}else{
		getTaskData();
	}
	
});


// 搜索
$("#search").keypress(function(e) {
	if (e.which == 13) {
		$("#ensure").trigger('click');
	}
}).next().click(function(e) {
	$("#ensure").trigger('click');
});


function initPopEvent(table,touch,vic){
	var $pop = $(".pop-box");
	$(table+" td span").mouseover(function(){
		var actId = $(this).parent().siblings("[data-id]").attr("data-id");
		var html = "";
		var list;
		var role = $(this).parent().attr("data-role");
		var isPop = $(this).find("b");		
		if(!isPop.length){
			return;
		}				
		if(role == "TOUCH_NUM"){
			list = touch[actId];
			for(var i=0;i<list.length;i++){
				html += "<ul>"+
							"<li>"+list[i].NAME+"</li>"+
							"<li class='number'>"+list[i].VALUE+"</li>"+
						"</ul>";
			}
		}else{
			list = vic[actId];
			for(var i=0;i<list.length;i++){
				html += "<ul>"+
							"<li>"+list[i].NAME+"</li>"+
							"<li class='number'>"+list[i].VALUE+"</li>"+
						"</ul>";
			}
		}		
		$pop.html(html);
		
		var color = $(this).css("background-color");
		var pos = this.getBoundingClientRect();
		var tab_h = $(".inner-content")[0].getBoundingClientRect();
		var pop_h = parseInt($(".pop-box").css("height"));
		
		if(tab_h.bottom-pos.bottom<pop_h){
			$pop.css("top",(pos.top-pop_h)+"px");
		}else{
			$pop.css("top",(pos.top+25)+"px");
		}				
		$pop.css("left",(pos.right-180)+"px");		
		$(this).css("border-radius","0");
		$pop.css("border-color",color);
		$(".pop-box li.number").css("color",color);
			
		$pop.show();
	});
	$(table+" td span").mouseout(function(){
		$(this).css("border-radius","15px");
		$pop.html("");
		$pop.hide();
	})
}

function initTagEvent(){
	$("#tags ul li").click(function(){
		var thisId = $(this).find("span").attr("id");
		if(thisId == "VIC_NUM" || thisId == "touch_num" || thisId == "obj_num"){
			return;
		}	
		var txt = $(this).find('span').html();
		$(this).toggleClass('selected');
		if($(this).hasClass("selected")){
			$("#selectedTags").addTag(txt);
		}else{
			$("#selectedTags").removeTag(txt);
		}
	});
	$("#modalTags ul li").click(function(){
		var thisId = $(this).find("span").attr("id");
		if(thisId == "VIC_NUM" || thisId == "touch_num" || thisId == "obj_num"){
			return;
		}
		$(this).toggleClass('selected');
	});
}

//模态框关闭，清空已选的指标
$(".modal-box-cancel").click(function(){
	$("#modalTags ul li.selected").removeClass("selected");
	$("#modalTags").find(".obj_num,.touch_num,.VIC_NUM").parent().addClass("selected");
    echart.myChart.clear(); //清空图表实例对象
	echart.myChart.hideLoading();//清除动画
})

$("#download").click(function(){
	var params = getParams();
	console.log(params);
	var sel = $(".panel-title>span.selected").find("a").attr("href");
	if(sel == "#activities"){
		window.location.href = 'http://10.113.254.17:8080/fcm/activityEvalution/getActivitiesDownload?searchTxt='+params.searchTxt+'&areaId='+params.areaId+'&stime='+params.stime+'&endtime='+params.endtime+'&channel='+params.channel+'&tagStr='+params.tagStr+'&tagNameStr='+params.tagNameStr;
	}else{
		window.location.href = 'http://10.113.254.17:8080/fcm/activityEvalution/getTasksDownload?searchTxt='+params.searchTxt+'&areaId='+params.areaId+'&sTime='+params.sTime+'&endTime='+params.endTime+'&channel='+params.channel+'&tagStr='+params.tagStr;
	}
	
})
$(document).click(function(e){  //点击页面空白处，指标栏隐藏
	var e = e || window.event,
	target = e.target || e.srcElement;
	var $target = $(target);
	var tagsBox = $("#tagsBox");
	var selTags = $("#selectedTags_tagsinput");
	if( tagsBox.css("display") == "none"){				
		if( $target[0].id == selTags[0].id ){
			tagsBox.css("display","block");
		}			 	
	}else{
		if( ($target.closest("#tagsBox")).length ){
			var $parent = $target.closest("#tagsBox");

			if( $parent[0].id != tagsBox[0].id ){
				tagsBox.css("display","none");
			}
		}else{
			if( $target[0].id != selTags[0].id ){
				tagsBox.css("display","none");
			}
		}
	}

	//模态框，指标下拉列表隐藏
	var $modalTags = $("#modalTags");
	if( $modalTags.css("display") == "block") {
		if($target.closest("#modalTags").length){
			$modalTags.css("display","block");
		}else{
			$modalTags.css("display","none");
		}
	}else{
		if($target.hasClass("j-chooseTags")){
			$modalTags.css("display","block");
		}
	}
});