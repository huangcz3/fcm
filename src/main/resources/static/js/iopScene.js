
var sceneAttrs = {
	activityObjective: {
		"1": "新增客户类",
		"2": "存量保有类",
		"3": "价值提升类",
		"4": "离网预警类",
		"9": "其他类"
	},
	activityType: {
		"1": "4G产品类",
		"2": "终端类",
		"3": "流量类",
		"4": "数字化服务类",
		"5": "基础服务类",
		"6": "PCC类",
		"7": "宽带类",
		"9": "其他类"
	},
	offerType: {
		"1": "电信服务",
		"2": "客户服务",
		"3": "数字内容服务",
		"4": "实物",
		"5": "虚拟物品"
	},
	channelId: {
		"1": "营业厅",
		"2": "掌厅",
		"3": "网厅",
		"4": "自助终端",
		"5": "短信",
		"6": "彩信",
		"7": "手机APP营业厅",
		"8": "掌上BOSS",
		"9": "微信",
		"10": "139邮箱",
		"11": "自有业务产品及APP",
		"12": "ESOP",
		"13": "外呼",
		"14": "省内自有其它类触点"
	},
	timeId: {
		"0": "无事件",
		"1": "互联网使用事件",
		"2": "社会事件",
		"3": "位置行踪事件",
		"4": "业务办理事件",
		"5": "业务使用事件",
		"6": "周期业务事件",
		"7": "自助系统接触事件",
		"8": "PCC事件",
		"9": "其它事件"
	},
	timeRule: {
		"D": "日",
		"W": "周",
		"M": "月"
	},
	flow: {
		"1": "一级策划省级执行",
		"2": "省级策划互联网执行",
		"3": "省级策划省级执行",
		"4": "一级策划电渠执行",
		"5": "一级策划互联网执行",
		"99": "其他"
	},
	removeCustomerGroup:{
		"removeEmployee":"内部号码",
		"removeRedList":"红名单",
		"removeSensitive": "敏感客户",
		"removeCancel10086": "取消10086流量提醒客户"
	}
}
var sceneGeneralFuns = (function(){
    var funs = new Object();
	funs.getParams = function () {
		var params = {};
		params.searchVal = $("#search").val() || "";
		params.activityType = $("#actType").val() || "";
		params.activityStartTime = $("#startDate").val().split("-").join("") || "";
		params.activityEndTime = $("#endDate").val().split("-").join("") || "";
		return params;
	}
	/*
	 * select,选择 “其他”，弹出输入框,绑定select；
	 * @param event.data: option"其他"的value
	 **/
	funs.otherParamInput = function (event) {
		var $this = $(this);
		var $hideInput = $this.parent().next();
		if ($this.val() == event.data) {
			$hideInput.removeClass("hidden");
		}else {
			$hideInput.addClass("hidden");
			$hideInput.val("");
		}
	}
	funs.transToKeyValue = function (data) {
		if( !data ){
			return
		}
		var mapList = [];
		for( var  k in data){
			var obj = {};
			obj.key = k;
			obj.value = data[k];
			mapList.push(obj);
		}
		return mapList;
	}
	funs.getNameById = function (objName,id){
		return sceneAttrs[objName][id];
	}
	/**
	 * 获取关联活动详情
	 * @param id
	 */
	funs.getRelativeActivity = function (id) {
		$.ajax({
			url: "./policyScene/getRelativeActivity?activityId="+id,
			type: "get",
			dataType: "json",
			success: function (res) {
				if( res.code != 0 ){
					showMessage(res.msg,"error");
					return
				}
				funs.autoFillNewActivity(res.data.activityInfo,res.data.indexs);
			}
		})
	}
	funs.autoFillNewActivity = function (info,indexs) {
		var activityType = info.businessTypeId == 6?1:info.businessTypeId; //PCC类暂时不可选？？？？
		var date = formatDate(new Date(),"yyyy/MM/dd hh:mm:ss");
		var customerGroup = info.activityCustomerGroupInfoList[0].customerGroupName;
		var offerName = info.activityRecommendProducts[0].productName;
		var $channelId = $('[name="channelId"]');
		var channelId = info.activityChannelDetailList[0].channelId;
		var channelRules = info.activityChannelDetailList[0].activityChannelRuleDetailList;

		var remove = [],removeMap = sceneAttrs.removeCustomerGroup,sgmtRule = '';
		for (var k in removeMap){
			if( info[k] == 1 ){
				remove.push(removeMap[k]);
			}
		}
		sgmtRule = remove.length == 0?"无":("剔除"+remove.join("，"));


		$("[name='activityName'],[name='activityDescribe']").val(info.activityName);
		$('[name="activityObjective"]').val(info.marketingPurposeId);
		$('[name="activityType"]').val(activityType);
		$("#activityStartTime,[name='activityEndTime']").val(date);
		$('[name="sgmtName"]').val(customerGroup);
		$('[name="sgmtSiftRule"]').val(sgmtRule);
		$('[name="offerName"]').val(offerName);
		if( channelId == "q03" ){
			$channelId.val("2")
		}else if( channelId == "f01" ){
			$channelId.val("8")
		}else {
			$channelId.val("14");
		}
		$.each(channelRules,function (i, rule) {
			if( rule.ruleId == "R001" ){
				$('[name="marketInfo"]').val(rule.ruleValue);
				return false
			}
		})
		//效果评估
		$('[name="succContactUserNum"]').val(!indexs?0:indexs.TOUCH_NUM);
		$('[name="succContactRatio"]').val(!indexs?0:indexs.TOUCH_RATE);
		$('[name="succMarketUserNum"]').val(!indexs?0:indexs.VIC_NUM);
		$('[name="succMarketRatio"]').val(!indexs?0:indexs.VIC_RATE);
		$('[name="responseRatio"],[name="inputOutputRatio"]').val(0);

	}

    return funs;
})();

var initIopSceneTable = function () {
	var me = this;
	this.getPageData = function(type){
		var params = getparams(type);
		var tableBoxId,pageId;
		switch(type.toString())
		{
			case "1":
				tableBoxId = "provinceScene";
				pageId = "page1";
				break;
			case "2":
				tableBoxId = "groupAssign";
				pageId = "page2";
				break;
			default:
		}
		$.ajax({
			 url:"./policyScene/getPolicySceneList",
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
				 $("#"+tableBoxId+" .searchResult").html(searchResultTxt(res.totals));
				 $("#"+pageId).html("");
				 fillTable(data,tableBoxId);
				 initPager(pageId,totalPage,type);
			 }
		 });

	}
	var searchResultTxt = function(total){
		var txt = "";
		var type = $("#actType").find("option:selected").text();
		var startTime = $("#startDate").val();
		var endTime = $("#endDate").val();
		txt = "\""+type+"\"、 \""+startTime+"~"+endTime+"\"相关搜索结果"+total+"个";
		return txt;
	}
	var getparams = function(type){
		var params = {};
		params.searchVal = $("#search").val() || "";
		params.activityType = $("#actType").val() || "";
		params.activityStartTime = $("#startDate").val().split("-").join("") || "";
		params.activityEndTime = $("#endDate").val().split("-").join("") || "";
		params.sceneType = type;  //type: 1.本省上传；2.集团下发
		params.lengthRow = 10;
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
	var doChangePage = function(pager){       //params.type: 1 本省待上传, 2 本省上传,3 集团下发
		var params = {};
		var tableBoxId = "";
		params = getparams(pager.type);
		params.page = pager.index;

		switch(pager.type.toString())
		{
			case "1":
				tableBoxId = "provinceScene";
				pageId = "page1";
				break;
			case "2":
				tableBoxId = "groupAssign";
				pageId = "page2";
				break;
			default:
		}
		$.ajax({
			url:'./policyScene/getPolicySceneList',
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
				var columns = id == "provinceScene"?11:7;
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
					var uploadTime = !data[i].uploadTime?"无":generalExtrFuncs.transDate(data[i].uploadTime);
					var createTime = !data[i].createTime?"无":generalExtrFuncs.transDate(data[i].createTime);
					// var isPCC = data[i].activityType == "6"?"是":"否";
					var status = "";
					if( id == "provinceScene" ) {
						status = iopPublicAttrs.state[data[i].flag];
					}else{
						status = "集团审批通过并发布";
					}
					html += "<tr>";
					html +=	"<td><i></i></td>";
					html +=	"<td class='actId text-align-left' title='"+data[i].activityName+"'>"+data[i].activityName+"</td>";
					html +=	"<td>"+data[i].proName+"</td>";
					if( id == "provinceScene" ) {
						html += "<td>" + data[i].createName + "</td>";
						html +=	"<td>"+createTime+"</td>";
						html += "<td data-uploadId='" + uploadId + "'>" + uploadName + "</td>";
						html +=	"<td>"+uploadTime+"</td>";
					}
					// html +=	"<td>"+isPCC+"</td>";
					html +=	"<td tytle='"+ sceneGeneralFuns.getNameById("activityType",data[i].activityType) +"'>"+sceneGeneralFuns.getNameById("activityType",data[i].activityType)+"</td>";
					html +=	"<td class='text-align-left' title='"+ data[i].activityDescribe +"'>"+data[i].activityDescribe+"</td>";
					html +=	"<td>"+status+"</td>";
					html +=	"<td class='text-align-center' data-activityId='"+data[i].activityId+"'>";
					if( id == "provinceScene" ){
						if( data[i].isCanUpload == 1){
							html += "<a href='#' title='上传' class='operate upload-icon'></a>";
						}else{
							html += "<a class='operate visible-hidden' href='#' title='上传' class='operate upload-icon'></a>";
						}
					}
					html += "<a href='#' title='详情' class='operate detail-modal-icon'></a>";
					html += "<a href='#' title='点评' class='operate comment-icon'></a>";
					if( id == "provinceScene" ){
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
					url: "./policyScene/uploadSceneActivity",
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
						showMessage("上传失败","error");
					}
				})
			};
			showConfirm("你确认要将此活动上传一级IOP吗？",ensureUpload);
		});
		$("#"+id+" .operate.detail-modal-icon").click(function(){       //详情按钮点击事件
			$.blockUI.defaults.overlayCSS.backgroundColor = '#fff';
			$.blockUI.defaults.overlayCSS.opacity = '0.8';
			$.blockUI({
				message:"正在查询，请稍等...",
				css:{
					border:"none",
					fontSize:"16px"
				}
			});

			var actId = $(this).parent().attr("data-activityid");
			$(".modal-wrap-detail").data("currentType",type);
			$(".modal-wrap-detail").data("currentActId",actId);
			$.ajax({
				url:'./policyScene/getSceneActivityDetail?activityId='+actId,
				type:'get',
				dataType:'json',
				success:function(res){
					$.unblockUI();
					if( res.code != 0 ){
						showMessage(res.msg,"error");
						return
					}
					var data = res.data;
					for( var k in data ){
						if( k == "campaignList" ){
							var campaignList = data[k];
							$.each(campaignList,function (i, campaign) {
								for( var j in campaign ){
									var bo = null;
									if( j == "offerList"){
										bo = campaign[j][0];
									}else{
										bo = campaign[j];
									}
									for( var p in bo ){
										var $elem = $("[data-name='"+p+"']");
										if($elem.length){
											var txt = "";
											if( sceneAttrs[p] ){
												txt = bo[p]?sceneGeneralFuns.getNameById(p,bo[p]) : "无";
											}else{
												txt = bo[p] || "无";
											}
											$elem.text(txt);
										}
									}
								}
							})
						}else if( k == "kpiBO" ){
							var kpiBo = data[k];
							for ( var j in kpiBo ){
								if( j == "commonKpiMap"){
									if( kpiBo[j] ){
										var commonKpiMap = JSON.parse(kpiBo[j]);
										for( var p in commonKpiMap){
											var $elem = $("[data-name='"+p+"']");
											if($elem.length){
												var txt = commonKpiMap[p] || "无";
												$elem.text(txt);
											}
										}
									}
								}
							}
						}else{
							if( k == "actAttrExtMap" ){
								var extMap = JSON.parse(data[k]);
								for( var p in extMap ){
									if( p == "attachmentName" ){
										var attachmentName = extMap[p];
										var attachmentId = data["attachmentId"].split(";");
										var files = attachmentName.split(";");
										var html = "";
										if( attachmentName != "" ){
											$.each(files,function (i, file) {
												html += "<a href='##' title='点击下载' class='j-download-file' data-attachmentId='"+attachmentId[i]+"'>"+file+"</a>";
											})
										}else{
											html = "无"
										}
										$("[data-name='attachmentName']").html(html);
									}else{
										var $elem = $("[data-name='"+p+"']");
										if($elem.length){
											var txt = "";
											if( sceneAttrs[p] ){
												txt = extMap[p]?sceneGeneralFuns.getNameById(p,extMap[p]) : "无";
											}else{
												text = extMap[p] || "无";
											}
											$elem.text(txt);
										}
									}
								}
							}else{
								var $elem = $("[data-name='"+k+"']");
								if($elem.length){
									var txt = "";
									if( sceneAttrs[k] ){
										txt = data[k]?sceneGeneralFuns.getNameById(k,data[k]) : "无";
									}else{
										txt = data[k] || "无";
									}
									$elem.text(txt);
								}
							}
						}
					}
				},
				error: function () {
					$.unblockUI();
					showMessage("获取数据失败","error");
				}
			})
			$(".modal-wrap-detail").show();
		});
		$("#"+id+" .operate.delete04-icon").click(function () {
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
					url: "./policyScene/deleteSceneActivity",
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
						showMessage("操作成功","success");
						me.getPageData(type);
					},
					error:function(){
						$.unblockUI();
						showMessage("删除失败","error");
					}
				})
			};
			showConfirm("确定删除该活动吗？",ensureUpload);
		});
	}
}
//活动关联
var initRelativeActivity = (function(){
	var relativeActivity = new Object();
	var getParams = function(){
		var params = {
			cityId:0,
			channelId:"@",
			activityState:"@",
			currentPage:1,
			lengthRow:5,
			startTime:"",
			endTime:""
		}
		params.searchVal = $(".j-rel-search").val();
		return params;
	}

	var getTableData = function(){
		var params = getParams();
		$.ajax({
			url: "./policyScene/getActivityList",
			type: "get",
			data: params,
			dataType: "json",
			success:function (res) {
				if( res.code != 0 ){
					showMessage(res.msg,"error")
					return;
				}
				fillTable(res.data.activityList);
				pagination(res.data.allTotals);
			},
			error:function () {
				showMessage("获取数据失败","error");
			}
		});
	}
	var pagination = function(totals){
		var pagerBox = document.getElementById('pager');
		$(pagerBox).html("");
		var pager = new Pager({
			index: 1,
			total: Math.ceil(totals/5),
			parent: pagerBox,
			onchange: doChangePage
		});
		$("#pager .cur").before("<span>第</span>");
		$("#pager .total-page").after("<span>页,共" + (totals || "0") + "条</span>");

		function doChangePage(obj){
			var params = getParams();
			params.currentPage = obj.index;
			$.ajax({
				url: "./policyScene/getActivityList",
				type:'get',
				data:params,
				dataType:'json',
				success:function(res){
					if( res.code != 0 ){
						showMessage(res.msg,"error");
						return
					}
					fillTable(res.data.activityList);
				},
				error:function(){
					showMessage("获取数据失败","error");
				}
			})
		}
	};
	var fillTable = function(data) {
		var html = '';
		if( !data || !data.length ){
			html = "<tr><td colspan='3'>暂无数据</td></tr>";
		}else{
			for (var i = 0; i < data.length; i++) {
				html += "<tr class='checkbox'>";
				html += "<td style='width:30px '>";
				html += "<label>";
				html += "<input type='checkbox'>";
				html += "<span></span>";
				html += "</label>";
				html += "</td>";
				html += "<td style='width: 25%' data-id='" + data[i].activityId + "'>" + data[i].activityId + "</td>";
				html += "<td data-name='" + data[i].activityName + "'>" + data[i].activityName + "</td>";
				html += "</tr>";
			}
		}


		$('#relativeActivity').html(html);
		initCheckbox();
	}

	var initCheckbox = function(){
		$("#relativeActivity .checkbox label span").click(function(e) {
			var  $this = $(this);
			var $table = $this.closest("table");
			var $sibTrs = $this.closest("tr").siblings();
			$sibTrs.find("span").removeClass("selected");
			$sibTrs.find("input").removeAttr("checked");
			$this.toggleClass("selected");
			var isChecked = $this.hasClass("selected");
			isChecked ? $this.prev("input").attr("checked", "checked") : $this.prev("input").removeAttr("checked");
		});
	}

	relativeActivity.query = function(){
		getTableData();
	}

	$(".cancel").click(function(){
		$(".j-relate-activity>span").removeClass("opened");
		$(".table-box").addClass("hide");
	});
	$(".j-rel-search").keypress(function (e) {
		if (e.keyCode == 13)
			getTableData();
	});
	$(".j-rel-search").next().click(function () {
		getTableData();
	})

	return relativeActivity;
})();
$(function(){
	//绑定otherParamInput？？？？？暂时隐藏此功能
	/*$(".j-other-target").on("change",9,sceneGeneralFuns.otherParamInput);
	$(".j-other-type").on("change",9,sceneGeneralFuns.otherParamInput);
	$(".j-other-quotastatus").on("change",9,sceneGeneralFuns.otherParamInput);
	$(".j-list-single-event").on("click","label.j-other-area",function(){
		var $prev = $(this).prev();
		var $hideInput = $prev.parents(".col-3").next();
		if( $prev.attr("checked") ){
			$hideInput.removeClass("hidden");
		}else{
			$hideInput.addClass("hidden");
			$hideInput.val("");
		}
	});*/
	//搜索
	var $search = $("#search");
	var getPageData = function () {
		var type = $(".panel-title span.title.selected").attr("data-type");
		$("#filter-btn").removeClass("opened");
		$(".filter-panel").hide();
		if( type == 1 ){
			sceneGeneralFuns.initProvTable.getPageData(1);
		}else{
			sceneGeneralFuns.initGroupTable.getPageData(2);
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
	//弹出新建模态框
	$(".create-new").on("click",function(){
		$(".modal-wrap-new").show();
		$.ajax({
			url: "./policyScene/getNewActBasicInfo",
			type: "get",
			dataType:"json",
			success: function (res) {
				if( res.code != 0 ){
					showMessage(res.msg,"error");
					return
				}
				var data = res.data;
				$(".modal-wrap-new").data("currentActId",data.activityId);
				for (var k in data){
					var $elem = $("#"+k);
					var $nameElem = $("[name='"+k+"']");
					if( $elem.length && $nameElem.length){
						$elem.text(data[k]);
						$nameElem.val(data[k]);
					}
				}
			}
		})
	});
	//保存新建
	$("#saveModel").on("click",function (e) {
		var formList = $(".modal-wrap-new").find("form");
		var param = {},paramObj = {},result = true;
		var fileNames = [],fileIds = [];
		var attachments = $("#attachment").find("[name='attachmentName']");
		$.each(formList,function (i, form) {
			var $form = $(form);
			var k = $form.attr("id");
			var formData = $form.serializeJson();
			//result = sceneGeneralFuns.checkForm(formData);
			result = iopPublicFuns.checkForm(formData);
			if( result ){
				paramObj[k] = formData;
				return true;
			}else{
				return false;
			}
		});
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
		});
		if( !result ){
			return;
		}

        param = paramObj.formBasicInfo;
        var campaignList = [],campaign = {},offerList = [],kpiBO = {};
		var activityId = param.activityId,
            activityName = param.activityName;
        kpiBO.activityId = activityId;
        kpiBO.commonKpiMap = JSON.stringify(paramObj.kpiBO);
        param.kpiBO = kpiBO;
        // paramObj.pccToStrategeBO.activityId = activityId;
        // param.pccToStrategeBO = paramObj.pccToStrategeBO;
		paramObj.formAttachment.attachmentName = fileNames.join(";");
		param.attachmentId = fileIds.join(";");
		param.actAttrExtMap = JSON.stringify(paramObj.formAttachment);
        campaign.activityId = activityId;
        campaign.campaignId = "cam_"+activityId;
        campaign.campaignName = "cam_"+activityName;
		campaign.campaignStartTime = param.activityStartTime;
		campaign.campaignEndTime = param.activityEndTime;
        paramObj.segmentBO.campaignId = campaign.campaignId;
        paramObj.offerBO.campaignId = campaign.campaignId;
        paramObj.channelBO.campaignId = campaign.campaignId;
		paramObj.channelBO.channelName = $("[name='channelId']").find("option:selected").text();
        paramObj.timeBO.campaignId = campaign.campaignId;
        campaign.segmentBO = paramObj.segmentBO;
        campaign.channelBO = paramObj.channelBO;
        campaign.timeBO = paramObj.timeBO;
        offerList.push(paramObj.offerBO);
        campaign.offerList = offerList;

        campaignList.push(campaign);
        param.campaignList = campaignList;
		$.ajax({
			url: "./policyScene/savePolicySceneActivity",
			type: "post",
			data: JSON.stringify(param),
			contentType: "application/json;charset=UTF-8;",
			success: function (res) {
				if( res.code != 0 ){
					showMessage(res.msg,"error");
					return
				}
				$(".modal-wrap-new").hide();
				showMessage("保存成功","success");
				sceneGeneralFuns.initProvTable.getPageData(1);
				//清空表单
				$.each(formList,function (i, form) {
					$(form)[0].reset();
				});

				var $table = $("#relativeActivity");
				$table.find("label input").removeAttr("checked");
				$table.find("label span").removeClass("selected");
				$(".j-chose-activity").hide();
				$("#relativeActId,#relativeActName").text("");

				$(".modal-wrap-new .modal-box-content").slimScroll({
					scrollTo: "0px"
				});
			},
			error: function () {
				showMessage("保存失败","error");
			}
		})
	});
	//数据下载
	$("#download").on("click",function(){
		var params = sceneGeneralFuns.getParams();
		var sceneType = $(".panel-title span.title.selected").attr("data-type");
		window.location.href = './policyScene/getPolicySceneListDownload?searchVal='+params.searchVal+
			'&activityType='+params.activityType+
			'&activityStartTime='+params.activityStartTime+
			'&activityEndTime='+params.activityEndTime+
			'&sceneType='+sceneType
	});
	iopPublicFuns.fileEvent(5);
	//展开关联活动表格
	$(".j-relate-activity>span").click(function () {
		var $parent = $(this).parent();
		$(this).toggleClass("opened");
		$(".table-box").toggleClass("hide");
		if( !$parent.attr("data-iscreated") ){
			$parent.attr("data-iscreated","created");
			initRelativeActivity.query();
		}
	});
	//关联活动确定按钮点击事件
	$("#ensureChoose").on("click",function (e) {
		e.preventDefault();
		var $table = $("#relativeActivity");
		var $checked = $table.find("tr td:first-child input[checked]");
		if( $checked.length ){
			var id = $checked.closest("td").siblings("[data-id]").attr("data-id");
			var name = $checked.closest("td").siblings("[data-name]").attr("data-name");
			$("#relativeActId").text(id);
			$("#relativeActName").text(name);
			$(".j-chose-activity").show();
			sceneGeneralFuns.getRelativeActivity(id);
		}
		$(this).next().trigger("click");
	})
})

$(document).ready(function(){
	//初始化网页顶部导航模块
	var topNav = new topNavigation();
	topNav.init();
	topNav.show("#tacticLib");
	//初始化网页左侧导航模块
	var sideNav = new sideNavigation();
	sideNav.init();
	sideNav.show("#iopScene");
});

$(document).ready(function(){
	eventEmitter.on("changeTab", function () {
		$(".panel-title .rt .create-new").toggle();
	});

	$.ajaxSetup({
		cache: false,
		dataType: 'json',
		error: function () {
			showMessage("获取数据失败","error");
		}
	});

	var scrollH = ($(window).height()-200),
		scrollConfig = {
			color: "#a2a8af",
			alwaysVisible : true,
			size: "5px"
		}

	$(".modal-wrap-new .modal-box-content").slimScroll($.extend(scrollConfig,{height: scrollH-50}));
	$(".modal-wrap-detail .modal-box-content").slimScroll($.extend(scrollConfig,{height: scrollH}));



	var initProvTable = new initIopSceneTable();
	initProvTable.getPageData(1);
	sceneGeneralFuns.initProvTable = initProvTable;

	var initGroupTable = new initIopSceneTable();
	initGroupTable.getPageData(2);
	sceneGeneralFuns.initGroupTable = initGroupTable;

});