
var caseGeneralFuns = (function(){
	var funs = new Object();
	funs.getParams = function () {
		var params = {};
		params.searchVal = $("#search").val() || "";
		params.activityStartTime = $("#startDate").val().split("-").join("") || "";
		params.activityEndTime = $("#endDate").val().split("-").join("") || "";
		return params;
	}

	return funs;
})();

var initIopCaseTable = function () {
	var me = this;
	this.getPageData = function(type){
		var params = getparams(type);
		var tableBoxId,pageId;
		switch(type.toString())
		{
			case "1":
				tableBoxId = "provinceCase";
				pageId = "page1";
				break;
			case "2":
				tableBoxId = "groupAssign";
				pageId = "page2";
				break;
			default:
		}
		$.ajax({
			url:"./policyCase/getPolicyCaseList",
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
				//$("#"+tableBoxId+" .searchResult").html(searchResultTxt(res.totals));
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
		params.caseType = type;  //type: 1.本省上传；2.集团下发
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
				tableBoxId = "provinceCase";
				pageId = "page1";
				break;
			case "2":
				tableBoxId = "groupAssign";
				pageId = "page2";
				break;
			default:
		}
		$.ajax({
			url:'./policyCase/getPolicyCaseList',
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
			var columns = id == "provinceCase"?8:6;
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
				var upDateTime = !data[i].upDateTime?"无":generalExtrFuncs.transFullTime(data[i].upDateTime);
                var createTime = !data[i].createTime?"无":generalExtrFuncs.transFullTime(data[i].createTime);
				var status = "";
				if( id == "provinceCase" ) {
					status = iopPublicAttrs.state[data[i].flag];
				}else{
					status = "集团审批通过并发布";
				}
				html += "<tr>";
				html +=	"<td><i></i></td>";
				html +=	"<td class='actId text-align-left' title='"+data[i].title+"'>"+data[i].title+"</td>";
				html +=	"<td>"+data[i].proName+"</td>";
				html +=	"<td>"+upDateTime+"</td>";
				if( id == "provinceCase" ){
					html +=	"<td data-uploadId='"+uploadId+"'>"+ uploadName +"</td>";
					html += "<td>"+createTime+"</td>";
				}
				html +=	"<td>"+status+"</td>";
				html +=	"<td data-activityId='"+data[i].caseId+"'>";
				if( id == "provinceCase" ){
					if( data[i].isCanUpload == 1){
						html += "<a href='#' title='上传' class='operate upload-icon'></a>";
					}else{
						html += "<a class='operate visible-hidden' href='#' title='上传' class='operate upload-icon'></a>";
					}
				}
				html += "<a href='#' title='详情' class='operate detail-modal-icon'></a>";
				html += "<a href='#' title='点评' class='operate comment-icon'></a>";
				if( id == "provinceCase" ){
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

	};
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
					url: "./policyCase/uploadPolicyCase",
					type:'post',
					data:{"activityId":actId},
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
				url:'./policyCase/getPolicyCaseDetail?activityId='+actId,
				type:'get',
				dataType:'json',
				success:function(res){
					if( res.code != 0 ){
						showMessage(res.msg,"error");
						return
					}
					var data = res.data;
					for( var k in data){
						if( k == "caseAttrMap" ){
							var attrMap = JSON.parse(data[k]);
							var attachmentId = data["attachmentId"].split(",");
							var files = attrMap.attachmentName.split(",");
							var html = "";
							if( attrMap.attachmentName != "" ){
								$.each(files,function (i, file) {
									html += "<a href='##' title='点击下载' class='j-download-file' data-attachmentId='"+attachmentId[i]+"'>"+file+"</a>";
								})
							}else{
								html = "无"
							}
							$("[data-name='attachmentName']").html(html);
						}else{
							var $elem = $("[data-name='"+k+"']");
							if($elem.length){
								var txt = "";
								if( k == "upDateTime" ){
									txt = data[k]?generalExtrFuncs.transFullTime(data[k]):"无";
								}else {
									txt = data[k] || "无";
								}
								$elem.text(txt);
							}
						}
					}
				}
			})
			$(".modal-wrap-detail").show();
		});
		$("#"+id+" .operate.delete04-icon").click(function(){      //删除按钮点击事件
			var actId = $(this).parent().attr("data-activityid");
			var ensureUpload = function(){
				$.blockUI.defaults.overlayCSS.backgroundColor = '#fff';
				$.blockUI.defaults.overlayCSS.opacity = '0.8';
				$.blockUI({
					message:"删除活动中，请稍等...",
					css:{
						border:"none",
						fontSize:"16px"
					}
				});
				$.ajax({
					url: "./policyCase/deletePolicyCase",
					type:'post',
					data:{"activityId":actId},
					dataType:'json',
					cache: false,
					success:function(res){
						$.unblockUI();
						if(res.code !== 0){
							showMessage(res.msg,"error");
							return
						}
						showMessage("删除成功","success");
						me.getPageData(type);
					},
					error:function(){
						$.unblockUI();
						showMessage("删除失败","error");
					}
				})
			};
			showConfirm("确定呀删除该活动吗？",ensureUpload);
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
			caseGeneralFuns.initProvTable.getPageData(1);
		}else{
			caseGeneralFuns.initGroupTable.getPageData(2);
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
		var extMap = {},fileNames = [],fileIds = [];
		var params = $("#basicInfo").serializeJson();
		var result = iopPublicFuns.checkForm(params);
		var attachments = $("#attachment").find("[name='attachmentName']");
		$.each(attachments,function (i, file) {
			var $file = $(file);
			var fileName = $file.val();
			var fileId = $file.attr("data-attachmentId");
			if ( fileName == "" || fileName == "点击此处选取上传文件" ){
				return true;
			}
			if( fileId == "" ){
				showMessage("请先上传已选附件","error");
				result = false;
				return false;
			}
			if( fileName.indexOf("\\") != -1 ){
				fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
			}
			fileNames.push(fileName);
			fileIds.push(fileId)

		})
		extMap.attachmentName = fileNames.join(",");
		params.caseAttrMap = JSON.stringify(extMap);
		params.attachmentId = fileIds.join(",");

		// var result = caseGeneralFuns.checkForm(params);
		if( !result ){
			return
		}
		$.ajax({
			url: "./policyCase/saveNewPolicyCase",
			type: "post",
			data: JSON.stringify(params),
			contentType: "application/json;charset=utf-8;",
			success: function(res) {
				if ( res.code != 0 ){
					showMessage(res.msg,"error");
					return
				}
				$(".modal-wrap-new").hide();
				showMessage("保存成功","success");
				caseGeneralFuns.initProvTable.getPageData(1);
				//清空表单
				var formList = $(".modal-wrap-new form");
				$.each(formList,function (i, form) {
					$(form)[0].reset();
				});
			},
			error: function () {
				showMessage("保存失败","error");
			}
		})
	});
	//数据下载
	$("#download").on("click",function(){
		var params = caseGeneralFuns.getParams();
		var caseType = $(".panel-title span.title.selected").attr("data-type");
		window.location.href = './policyCase/getPolicyCaseListDownload?searchVal='+params.searchVal+
			'&startTime='+params.activityStartTime+
			'&endTime='+params.activityEndTime+
			'&caseType='+caseType
	});

	//文件上传和附件下载
	iopPublicFuns.fileEvent(7);
})

$(document).ready(function(){
	//初始化网页顶部导航模块
	var topNav = new topNavigation();
	topNav.init();
	topNav.show("#tacticLib");
	//初始化网页左侧导航模块
	var sideNav = new sideNavigation();
	sideNav.init();
	sideNav.show("#iopCase");
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
	var initProvTable = new initIopCaseTable();
	initProvTable.getPageData(1);
	caseGeneralFuns.initProvTable = initProvTable;

	var initGroupTable = new initIopCaseTable();
	initGroupTable.getPageData(2);
	caseGeneralFuns.initGroupTable = initGroupTable;
})