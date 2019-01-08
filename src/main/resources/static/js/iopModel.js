
var modelGeneralFuns = (function(){
	var funs = new Object();
	funs.getParams = function () {
		var params = {};
		params.searchVal = $("#search").val() || "";
		params.startTime = $("#startDate").val().split("-").join("") || "";
		params.endTime = $("#endDate").val().split("-").join("") || "";
		params.modelType = $("#actType").val();
		return params;
	}

	return funs;
})();

var initIopModelTable = function (type) {
	var me = this;
	this.getPageData = function(type){
		var params = getparams(type);
		var tableBoxId,pageId;
		switch(type.toString())
		{
			case "1":
				tableBoxId = "provinceModel";
				pageId = "page1";
				break;
			case "2":
				tableBoxId = "groupAssign";
				pageId = "page2";
				break;
			default:
		}
		$.ajax({
			url:"./policyModel/getPolicyModelList",
			type:"get",
			data:params,
			dataType:"json",
			success:function(res){
				if(res.code != 0){
					showMessage(res.msg,"error");
					return
				}
				var data = res.data.activityList;
				var totals = res.data.totals;
				var totalPage = Math.ceil(totals/10);
				$("#"+tableBoxId+"Num").html(totals);
				// $("#"+tableBoxId+" .searchResult").html(searchResultTxt(res.totals));
				$("#"+pageId).html("");
				fillTable(data,tableBoxId);
				initPager(pageId,totalPage,type);
			}
		});

	}
	var getparams = function(type){
		var params = {};
		params.searchVal = $("#search").val() || "";
		params.startTime = $("#startDate").val().split("-").join("") || "";
		params.endTime = $("#endDate").val().split("-").join("") || "";
		params.modelType = $("#actType").val();
		params.type = type;//type: 1.本省上传；2.集团下发
		params.pageSize = 10;
		params.page = 1;
		return params;
	}
	var initPager = function(id,total,type){
		var pagerBox = document.getElementById(id);
		var pager = new Pager({
			index: 1,
			total: total,
			parent: pagerBox,
			type: type,
			onchange: doChangePage
		});
	}
	var doChangePage = function(pager){       //params.type: 1 本省待上传, 2 集团下发
		var params = {};
		var tableBoxId = "";
		params = getparams(pager.type);
		params.page = pager.index;

		switch(pager.type.toString())
		{
			case "1":
				tableBoxId = "provinceModel";
				pageId = "page1";
				break;
			case "2":
				tableBoxId = "groupAssign";
				pageId = "page2";
				break;
			default:
		}
		$.ajax({
			url:'./policyModel/getPolicyModelList',
			type:'get',
			data:params,
			dataType:'json',
			cache:false,
			success:function(res){
				if ( res.code != 0 ){
					showMessage(res.msg,"error");
					return
				}
				var data = res.data.activityList;
				fillTable(data,tableBoxId);
			}
		})

		/*	//测试
		 fillTable(params,tableBoxId);*/
	}
	var fillTable = function(data,id){
		$("#"+id+" tbody").html("");
		if(!data.length){
			var columns = id == "provinceModel"?12:10;
			$("#"+id+" tbody").append(
				"<tr>"+
				"<td class='text-align-center' colspan='"+columns+"'>暂无数据</td>"+
				"</tr>"
			);
		}else{
			var html = "";
			for(var i=0;i<data.length;i++){
				var uploadName = !data[i].uploadName?"无" : data[i].uploadName;
				var uploadId = !data[i].uploadId?"" : data[i].uploadId;
				var uploadTime = !data[i].uploadDateTime?"无":generalExtrFuncs.transFullTime(data[i].uploadDateTime);
				var updateTime = !data[i].updateTime?"无":generalExtrFuncs.transFullTime(data[i].updateTime);
				var createTime = !data[i].createTime?"无":generalExtrFuncs.transFullTime(data[i].createTime);
				var status = "";
				if( id == "provinceModel" ){
					status = iopPublicAttrs.state[data[i].flag];
				}else{
					status = "集团审批通过并发布";
				}

				html += "<tr>";
				html +=	"<td><i></i></td>";
				html +=	"<td class='text-align-left' title='"+data[i].modelName+"'>"+data[i].modelName+"</td>";
				html +=	"<td>"+data[i].proName+"</td>";
				html +=	"<td>"+iopPublicAttrs.activityType[data[i].modelType]+"</td>";
				html +=	"<td class='text-align-left'>"+data[i].modelSimpleDesc+"</td>";
				if( id == "provinceModel" ){
					html += "<td>"+data[i].createName+"</td>";
					html +=	"<td data-uploadId='"+uploadId+"'>"+ uploadName +"</td>";
				}
				html += "<td>"+createTime+"</td>";
				html += "<td>"+updateTime+"</td>";
				html +=	"<td>"+uploadTime+"</td>";
				html +=	"<td>"+status+"</td>";
				html +=	"<td class='text-align-center' data-activityId='"+data[i].modelId+"'>";
				if( id == "provinceModel" ){
					if( data[i].isCanUpload == 1){
						html += "<a href='#' title='上传' class='operate upload-icon'></a>";
					}else{
						html += "<a class='operate visible-hidden' href='#' title='上传' class='operate upload-icon'></a>";
					}
				}
				html += "<a href='#' title='详情' class='operate detail-modal-icon'></a>";
				html += "<a href='#' title='点评' class='operate comment-icon'></a>";
				if( id == "provinceModel" ){
					if ( data[i].isCanDelete == 1 ){
						html += "<a href='#' title='删除' class='operate delete04-icon'></a>";
					}else{
						html += "<a class='operate visible-hidden' href='#' title='删除' class='operate delete04-icon'></a>";
					}
				}
				html +=	"</td></tr>";
				$("#"+id+" tbody").html(html);
			}
		}
		initOperate(id);
		initTableStyle(id);
		initTableHover();
	}
	var initTableStyle = function(id){ //表格样式
		/*
		 *所有动态添加列添加背景色
		 */
		var $table = $("#"+id);
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
		var table_h = parseInt($(".inner-content").css("height"))-20-39-40-40;
		$table.find("td").css("height",(table_h/11)+"px");
		$table.find("tbody tr:odd").addClass("even");

	}
	var initOperate = function(id){
		var type = id == "groupAssign"?2:1;
		$("#"+id+" .operate.upload-icon").click(function(){      //上传按钮点击事件
			var actId = $(this).parent().attr("data-activityid");
			var ensureUpload = function(){
				$.blockUI.defaults.overlayCSS.backgroundColor = '#fff';
				$.blockUI.defaults.overlayCSS.opacity = '0.8';
				$.blockUI({
					message:"正在上传，请稍等...",
					css:{
						border:"none",
						fontSize:"16px"
					}
				});
				$.ajax({
					url: "./policyModel/uploadPolicyModelActivity",
					type:'post',
					data:{"modelId":actId},
					dataType:'json',
					cache: false,
					success:function(res){
						$.unblockUI();
						if(res.code !== 0){
							showMessage(res.msg,"error");
							return
						}
						showMessage("上传成功","success");
						me.getPageData(type);
					},
					error:function(){
						$.unblockUI();
						showMessage("删除失败","error");
					}
				})
			};
			showConfirm("你确认要将此活动上传一级IOP吗？",ensureUpload);
		});
		$("#"+id+" .operate.detail-modal-icon").click(function(){       //详情按钮点击事件
			var actId = $(this).parent().attr("data-activityid");
			$(".modal-wrap-detail").data("currentType",type);
			$(".modal-wrap-detail").data("currentActId",actId);
			$.ajax({
				url:'./policyModel/getPolicyModelDetail?modelId='+actId,
				type:'get',
				dataType:'json',
				success:function(res){
					if( res.code != 0 ){
						showMessage(res.msg,"error");
						return
					}
					var data = res.data;
					for( var k in data){
						var $elem = $("[data-name='"+k+"']");
						if(k == "dataDesc" || k == "dataHandle" || k == "modeEva" || k == "modelKpiBO"){
							var dataObj = data[k];
							for ( var p in dataObj ){
								if( p == "modelId" ){
									continue;
								}
								var $pelem = $("[data-name='"+p+"']");
								if($pelem.length){
									if( typeof($pelem.attr("data-file")) !== "undefined"){
										var	html = dataObj[p]? "<a href='##' title='点击下载' class='j-download-file' data-attachmentId=''>"+dataObj[p]+"</a>":"无";
										$pelem.html(html);
									}else {
										var txt = dataObj[p] || "无";
										$pelem.text(txt);
									}
								}
							}
						}else {
							if($elem.length){
								if( typeof($elem.attr("data-file")) !== "undefined"){
									var	html = data[k]?"<a href='##' title='点击下载' class='j-download-file' data-attachmentId=''>"+data[k]+"</a>":"无";
									$elem.html(html);
								}else{
									var txt = "";
									if( k == "createTime" || k == "updateTime" ){
										txt = data[k]?generalExtrFuncs.transFullTime(data[k]):"无";
									}else {
										txt = data[k] || "无";
									}
									$elem.text(txt);
								}
							}
						}
					}
				}
			})
			$(".modal-wrap-detail").show();
		});
		$("#"+id+" .operate.delete04-icon").click(function(){      //上传按钮点击事件
			var actId = $(this).parent().attr("data-activityid");
			var ensureUpload = function(){
				$.blockUI.defaults.overlayCSS.backgroundColor = '#fff';
				$.blockUI.defaults.overlayCSS.opacity = '0.8';
				$.blockUI({
					message:"活动删除中，请稍等...",
					css:{
						border:"none",
						fontSize:"16px"
					}
				});
				$.ajax({
					url: "./policyModel/deleteModelActivity",
					type:'post',
					data:{"modelId":actId},
					dataType:'json',
					cache: false,
					success:function(res){
						$.unblockUI();
						if(res.code !== 0){
							showMessage(res.msg,"error");
							return
						}
						showMessage("操作成功","success");
						me.getPageData(type);
					},
					error:function(){
						$.unblockUI();
						showMessage("删除失败","error");
					}
				})
			};
			showConfirm("确定要删除该活动吗？",ensureUpload);
		});
	}
}

$(function () {
	//搜索
	var $search = $("#search");
	var getPageData = function () {
		var type = $(".panel-title span.title.selected").attr("data-type");
		$("#filter-btn").removeClass("opened");
		$(".filter-panel").hide();
		if( type == 1 ){
			modelGeneralFuns.initProvTable.getPageData(1);
		}else{
			modelGeneralFuns.initGroupTable.getPageData(2);
		}
	}
	$search.on("keypress",function (e) {
		if (e.keyCode == 13)
			getPageData()
	});
	$search.next().on("click",getPageData);
	$("#ensure").on("click",function (e) {
		e.preventDefault();
		getPageData();
	});

	$(".tab-nav>span").on("click",iopPublicFuns.toggleCreateNewBtn);

	$(".create-new").on("click",iopPublicFuns.generateActivityId);
	//保存新建案例
	$("#saveNew").click(function (e) {
		e.preventDefault();
		var formList = $(".modal-wrap-new").find("form");
		var paramObj = {},result = true;
		var modelId = $("#activityId").text();
		$.each(formList,function (i, form) {
			var $form = $(form);
			var id = $form.attr("id");
			var formData = $form.serializeJson();

			result = iopPublicFuns.checkForm(formData);

			if( result ){
				for ( var k in formData ){
					var $file = $("[name='"+k+"']");
					var fileName = $file.val();
					if( typeof($file.attr("data-attachmentid")) !== "undefined"){ //文件上传域
						if ( fileName == "" || fileName == "点击此处选取上传文件" ){
							formData[k] = "";
						}else{
							if( fileName.indexOf("\\") != -1 ){
								fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
							}
							formData[k] = fileName;
						}
					}
				}
				if( id.indexOf("formData") != -1 ){
					$.extend(paramObj,formData);
				}else{
					formData.modelId = modelId;
					paramObj[id] = formData;
				}
				return true;
			}else{
				return false;
			}
		});

		if( !result ){
			return
		}
		paramObj.updateTime = paramObj.updateTime.replace(/\-|\:|\s/g,"");
		$.ajax({
			url: "./policyModel/saveNewPolicyModel",
			type: "post",
			data: JSON.stringify(paramObj),
			contentType: "application/json;charset=utf-8;",
			success: function(res) {
				if ( res.code != 0 ){
					showMessage(res.msg,"error");
					return
				}
				$(".modal-wrap-new").hide();
				showMessage("保存成功","success");
				modelGeneralFuns.initProvTable.getPageData(1);
				//重置表单
				$.each(formList,function (i, form) {
					$(form)[0].reset();
				})
			},
			error: function () {
				showMessage("保存失败","error");
			}
		})
	});
	//数据下载
	$("#download").on("click",function(){
		var params = modelGeneralFuns.getParams();
		var type = $(".panel-title span.title.selected").attr("data-type");
		window.location.href = './policyModel/getPolicyModelListDownload?searchVal='+params.searchVal+
			'&startTime='+params.startTime+
			'&endTime='+params.endTime+
			'&modelType='+params.modelType +
			'&type='+type;
	});

	//文件上传和附件下载
	iopPublicFuns.fileEvent(6);
})

$(document).ready(function(){
	//初始化网页顶部导航模块
	var topNav = new topNavigation();
	topNav.init();
	topNav.show("#tacticLib");
	//初始化网页左侧导航模块
	var sideNav = new sideNavigation();
	sideNav.init();
	sideNav.show("#iopModel");
});

$(document).ready(function(){
	eventEmitter.on("changeTab", function () {
		$(".panel-title .rt .create-new").toggle();
	})

	var scrollH = ($(window).height()-200) + "px";
	$('.modal-box-content').slimScroll({
		height: scrollH,
		color: "#a2a8af",
		alwaysVisible : true,
		size: "5px",
	});

	$.ajaxSetup({
		cache: false,
		dataType: 'json',
		error: function () {
			showMessage("获取数据失败","error");
		}
	})

	//初始化表格
	var initProvTable = new initIopModelTable();
	initProvTable.getPageData(1);
	modelGeneralFuns.initProvTable = initProvTable;

	var initGroupTable = new initIopModelTable();
	initGroupTable.getPageData(2);
	modelGeneralFuns.initGroupTable = initGroupTable;
})