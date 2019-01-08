$(document).ready(function(){
	//初始化网页顶部导航模块
	var topNav = new topNavigation();
	topNav.init();
	topNav.show("#marketingAct");
	//初始化网页左侧导航模块
	var sideNav = new sideNavigation();
	sideNav.init();
	sideNav.show("#vicAllNetActivity");
});
var globalAttrs = {};
var transToName = (function(){
	var funcs = new Object();
	funcs.activityType = function (activityType){
		switch(activityType) {
			case 1:
				return "4G产品类";
			case 2:
				return "终端类";
			case 3:
				return "流量类";
			case 4:
				return "数字化服务类";
			case 5:
				return "基础服务类";
			case 6:
				return "PCC类";
			case 9:
				return "其它类";
		}
	}
	funcs.pefrMeans = function(pefrMeans){
		switch(suitArea) {
			case 1:
				return "限速";
			case 2:
				return "提速";
			case 3:
				return "降低优先级";
			case 4:
				return "专属带宽";
			case 5:
				return "差异计费";
			case 6:
				return "营销/提醒";
		}

	}
	funcs.quotaStatus = function(quotaStatus){
		switch(quotaStatus) {
			case 1:
				return "套餐内";
			case 2:
				return "套餐外";
			case 3:
				return "超封顶";
			case 9:
				return "其它";
		}
	}
	funcs.suitArea = function(suitArea) {
		switch(suitArea) {
			case 0:
				return "全部地区";
			case 1:
				return "校园";
			case 2:
				return "医院";
			case 3:
				return "商圈";
			case 4:
				return "居民区";
			case 5:
				return "CBD";
			case 6:
				return "移动营业厅";
			case 9:
				return "其他";
		}
	}
	funcs.suitNetwork = function(suitNetwork){
		switch(suitNetwork) {
			case "01":
				return "2G";
			case "02":
				return "3G";
			case "03":
				return "4G";
			case "11":
				return "2/3G";
			case "12":
				return "3/4G";
			case "13":
				return "2/4G";
			case "21":
				return "2/3/4G";
		}
	}
	funcs.pccClass = function(pccClass) {
		switch(pccClass) {
			case 1:
				return "管控";
			case 2:
				return "提醒";
			case 3:
				return "业务保障";
			case 4:
				return "差异化计费";
			case 6:
				return "营销";
			case 6:
				return "网络能力开放";
			case 7:
				return "后向流量统付";
			case 9:
				return "其它";
		}
	}
	funcs.offerSource = function(offerSource) {
		switch(offerSource) {
			case 1:
				return "电信服务";
			case 2:
				return "客户服务";
			case 3:
				return "数字内容服务";
			case 4:
				return "实物";
		}
	}
	funcs.offerType = function(offerType){
		switch(offerType) {
			case 1:
				return "电信服务";
			case 2:
				return "客户服务";
			case 3:
				return "数字内容服务";
			case 4:
				return "实物";
			case 5:
				return "虚拟物品";
		}
	}
	funcs.commonKpiInfo = function(commonKpiInfo) {
		switch(commonKpiInfo) {
			case "succContactUserNum":
				return "成功接触客户数";
			case "succContactRatio":
				return "接触成功率";
			case "responseRatio":
				return "响应率";
			case "succMarketUserNum":
				return "数字化服务类";
			case "succMarketRatio":
				return "营销成功率";
		}
	}
	funcs.pccKpiInfo = function(pccKpiInfo) {
		switch(pccKpiInfo) {
			case "pccOrderUserNum":
				return "签约用户数";
			case "pccValidUserNum":
				return "策略生效用户数";
			case "pccValidCount":
				return "策略生效次数";
			case "pccOffnetRatio":
				return "签约客户离网率";
		}
	}
	funcs.otherKpiInfo = function (otherKpiInfo) {
		switch(otherKpiInfo) {
			case "pkgUserNum":
				return "套餐流量使用用户数";
			case "pkgVolumn":
				return "套餐流量饱和度";
			case "pkgAliveRatio":
				return "套餐流量活跃度";
			case "custLivingRatio":
				return "低流量用户占比";
			case "volumnUser":
				return "语音使用用户";
			case "pkgCallVolumn":
				return "套餐语音饱和度";
			case "pkgCallAliveRatio":
				return "套餐语音活跃用户占比";
			case "smallCallUserRatio":
				return "低通话量用户占比";
			case "terFluxCUstRatio":
				return "4G终端4G流量客户占比";
			case "fluxCustNum":
				return "4G流量客户数";
			case "fluxUselessDaysRatio":
				return "4G客户中4G流量低使用天数（5天）占比";
			case "fluxUselessUserRatio":
				return "4G客户中4G低流量用户占比";
			case "userOneTimeUserRatio":
				return "月一次使用用户占比";
			case "aliveRatioMonthly":
				return "包月产品活跃度";
			case "userNextMonthAliveRatio":
				return "使用用户次月留存率";
			case "broadbandAliveUserNum":
				return "家庭宽带帐户活跃用户数";
			case "mobaiUserAliveRatio":
				return "魔百和用户活跃度";
			case "uselessUserRatio":
				return "低使用次数用户占比";
			case "broadbandNextAliveRatio":
				return "家庭宽带使用用户次月留存率";
			case "arpuAddRatio":
				return "ARPU提升率";
			case "fluxAddRatio":
				return "流量提升率";
			case "fourGFluxAddRatio":
				return "4G流量提升率";
			case "douAddRatio":
				return "DOU提升率";
			case "fourGDouAddRatio":
				return "4G DOU提升率";
			case "mouAddRatio":
				return "MOU提升率";
			case "durationAddRatio":
				return "通话时长提升率";
		}
	}

	return funcs;
})();
var fillCampaginTab = (function () {
	var funcs = new Object();
	funcs.fillPrivate = function (data) {
		/**
		 *    表格数据填充
		 */
		if( !data ){
			return
		}
		var txt = "";
		for(var k in data){
			if( k == "dwnclietbigfine" ){
				txt = data[k]==1?"是":"否"
			}else if( k == "strOfferbo" ){  //产品策略
				if( data[k] == "null" || !data[k] ){
					continue
				}
				var strOfferbo = JSON.parse(data[k]).offerBO[0];
				var policyTxt = "";
				for(var j in strOfferbo){
					if( $("#"+j).length ){
						if( j == "offerType" ){
							policyTxt = transToName.offerType(parseInt(strOfferbo[j]));
						}else if( j == "offerSource" ){
							policyTxt = "互联网公司产品";
						}else{
							policyTxt = strOfferbo[j]?strOfferbo[j]:"无";
						}
						$("#"+j).text(policyTxt);
					}
				}

			}else if( k == "strPosition" ){ //运营位信息
				if( data[k] == "null" || !data[k]){
					continue
				}
				var strPosition = JSON.parse(data.strPosition).PositionBO;
				var posTxt = "";
				for( var j in strPosition ){
					if( $("#"+j).length ){
						posTxt = strPosition[j]?strPosition[j]:"无";
						$("#"+j).text(posTxt);
					}
				}

			}else if( k == "channelAttrMap" ){
				if( data[k] == "null" || !data[k] ){
					continue
				}
			}else{
				if( $("#"+k).length ){
					txt = data[k]?data[k]:"无";
				}
			}
			$("#"+k).text(txt);
		}
	}
	funcs.fillPublic = function (data) {
		if( !data ){
			return
		}
		//pcc信息
		$("#pccid").text(data.pccId==null?"无":data.pccId);
		$("#pccname").text(data.pccName==null?"无":data.pccName);
		$("#pcclifecycle").text(data.pccLifeCycle==null?"无":data.pccLifeCycle);
		$("#pccclass").text(  data.pccClass==null?"无":transToName.pccClass(parseInt(data.pccClass)));
		$("#suitnetwork").text(data.suitNetwork==null?"无":transToName.suitNetwork(data.suitNetwork));
		$("#goalbusi").text(data.goalBusi==null?"无":data.goalBusi);
		$("#suitarea").text(data.suitArea==null?"无":transToName.suitArea(parseInt(data.suitArea)));
		$("#quotastatus").text(data.quotaStatus==null?"无":transToName.quotaStatus(parseInt(data.quotaStatus)));
		$("#tenltype").text(data.tenlType==null?"无":data.tenlType);
		$("#pefrmeans").text(data.pefrMeans==null?"无":transToName.pefrMeans(parseInt(data.pefrMeans)));
		$("#pccdesc").text(data.pccDesc==null?"无":data.pccDesc);
		//评估信息
		//通用指标
        if( data.strCommonkpilist == "null" || !data.strCommonkpilist){
            $("#commonKpiList").text("无");
        }else{
            var commonKpiList = JSON.parse(data.strCommonkpilist);
            var  comArr=[];
            for(var i=0 ;i<commonKpiList.indexName.length;i++){
                comArr.push(transToName.commonKpiInfo(commonKpiList.indexName[i]));
            }
            $("#commonKpiList").text(comArr.join("，"));
        }

		//pcc指标
        if( data.strPcckpilist == "null" || !data.strPcckpilist ){
            $("#pccKpiList").text("无");
        }else{
            var pccKpiList = JSON.parse(data.strPcckpilist);
            if(pccKpiList.indexNameSpecified){
                var  pccArr = [];
                for(var i=0 ;i<pccKpiList.indexName.length;i++){
                    pccArr.push( transToName.pccKpiInfo(pccKpiList.indexName[i]) );
                }
                $("#strPcckpilist").text(pccArr.join("，"));
            }
        }

		//其他指标
        if( data.strOtherkpilist == "null" || !data.strOtherkpilist ){
            $("#otherKpiList").text("无");
        }else{
            var otherKpiList = JSON.parse(data.strOtherkpilist);
            if(otherKpiList.indexNameSpecified){
                var  otherArr = [];
                for(var i=0 ;i<otherKpiList.indexName.length;i++){
                    otherArr.push( transToName.otherKpiInfo(otherKpiList.indexName[i]) );
                }
                $("#strOtherkpilist").text(otherArr.join("，"));
            }
        }
		//周期
        if( data.cycleType == "null" || !data.cycleType ){
            $("#cycletype").text("无");
        }else{
            $("#cycletype").text(data.cycleType=="D"?"日":"月");
        }
	}
	return funcs;
})();

var tagsOperate = function(){
	this.initTagsIput = function(id){
		//初始化tagsinput
		var $input = $(id);
		$input.tagsinput({
			itemValue: 'id',
			itemText: 'text',
		});
	}
}
/*有table的弹出框，实现tagsinout*/
tagsOperate.prototype.tableTagsinputEvent = function(gid, pid, tid) { //gid是弹出框所在的父级元素ID，pid是table所在的tab内容框的id,tid是tagsinput的input测id,事件代理
	$(gid).on('click', pid + ' .ensure', function () {
		var $table = $(pid).find("table");
		var checked = $table.find("tr td:first-child input[checked]");
		var tagsArr = [];
		$.each(checked, function(i, item) {
			var id = $(item).closest("td").siblings("[data-id]").attr("data-id");
			var name = $(item).closest("td").siblings("[data-name]").attr("data-name");
			var tag = {
				"id": id,
				"text": name
			};
			$(tid).tagsinput('add', tag);
		})

		$(gid).find(".cancel").trigger("click");
		// isNowPageDataAdded = true;
	});
}

//集团主活动派单,后台分页
var initActivityTable = function(){
	var activityTable = new Object();
	activityTable.allData = [];
	activityTable.getPageData = function(){
		var param = getParams();
		param.page = 1;
		$.ajax({
				url:'./iop/getActivityList',
				type:'get',
				data:param,
				dataType:'json',
				success:function(res){
					if( res.code != 0 ){
						showMessage(res.msg,"error");
						return;
					}
					var data = res.data.activityList;
					var total = res.data.totals;

					globalAttrs.authority = res.data.authority;
					if( globalAttrs.authority == 1  ){   //权限控制能否派单 0，-1：否； 1： 是
						$(".j-admin-tab").removeClass("hide");
					}else{
						$(".j-user-tab").removeClass("hide");
					}

					$("#activities_num").text("("+total+")");
					fillTable(data);
		           pagination(Math.ceil(total/10));
				},
				error:function(){
					showMessage("获取数据失败","error");
				} 
				
			})
	};
	var getParams = function(){
		var param = {};
		param.searchVal = $("#search").val();
		param.lengthRow = 10;
		return param;
	};

	var pagination = function(totalPage){
		var params = getParams();
		var pagerBox = document.getElementById('page');
		$(pagerBox).html("");
		var pager = new Pager({
			index: 1,
			total: totalPage,
			parent: pagerBox,
			onchange: doChangePage
		});
		function doChangePage(obj){
			params.page = obj.index;
			$.ajax({
				url: "./iop/getActivityList",
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

	var fillTable = function(data){
		var html = '';
		if( data.length == 0 ){
			html = "<tr><td colspan='9'>暂无数据</td></tr>";
		}else {
			for (var i = 0; i < data.length; i++) {
				html += '<tr>' +
					'<td>' +
					'<i></i>' +
					'</td>' +
					'<td title="' + data[i].activityName + '" class="text-align-left">' + data[i].activityName + '</td>' +
					'<td title="' + data[i].activityTemplateId + '">' + data[i].activityTemplateId + '</td>' +
					'<td title="' + transToName.activityType(parseInt(data[i].activityType)) + '">' + transToName.activityType(parseInt(data[i].activityType)) + '</td>' +
					'<td title="' + data[i].activityStartTime + '">' + data[i].activityStartTime + '</td>' +
					'<td title="' + data[i].activityEndTime + '">' + data[i].activityEndTime + '</td>' +
					'<td title="' + data[i].twicePlanRequestAcctime + '">' + data[i].twicePlanRequestAcctime + '</td>' +
					/*'<td title="' + (data[i].pccName == null ? "无" : data[i].pccName) + '">' + (data[i].pccName == null ? "无" : data[i].pccName) + '</td>' +*/
					'<td class="text-align-left" title="' + data[i].activityDescribe + '">' + data[i].activityDescribe + '</td>' +
					'<td>' +
					'<a href="#" title="详情" class="operate relation-icon" data-id="' + data[i].activityId + '"></a>' +
					'</td>' +
					'</tr>';
			}
		}
		$("#table tbody").html(html);
		
		initTableStyle();
		initTableHover();
		initOperateEvent();
	};

	var initTableStyle = function(){
		 /*
		 *所有动态添加列添加背景色
		 */
		var len =  $("#table thead td[data-index]").length;
		 for(var col="col-",eachCol,i=5;i<len+1;i++){	 	
	 		eachCol = col + i;
	 		$("#table td[data-index='col-"+i+"']").find('span').addClass(color[(i-5)%4]); 		
		 }
		
		//表格图标添加
		imgPath = 'img/';
		icons = ['file_red.png','file_yellow.png','file_green.png','file_blue.png']
		imgTds = $("#table td>i");
		for(var i=0;i<imgTds.length;i++){ 
			$(imgTds[i]).css("background","url("+imgPath+icons[i%4]+")");
		}
		
		var table_h = parseInt($(".inner-content").css("height"))-20-39-40-20;
		$("#table td").css("height",(table_h/11)+"px");
		$("#table tbody tr:odd").addClass("even");

	};
	
	var initOperateEvent = function(){
		/**
		 * 点击查看子活动
		 */
		$(".relation-icon").click(function(){
			var activityId = $(this).attr("data-id");
			$(".modal-wrap-detail .modal-box-content").slimScroll({
				scrollTo: "0px"
			})
			$("#tempActiviyId").val(activityId);
			$(".modal-wrap-detail").show();
			if( globalAttrs.authority == 1 ){
				if( !$("#phoneList").attr("data-isCreated") ) {
					//获取派单下发人员列表
					$.ajax({
						url: "./iop/getAllUserList",
						type: 'get',
						dataType: "json",
						success: function (res) {
							if (res.code != 0) {
								showMessage(res.msg, "error");
								return
							}
							var data = res.data;
							var html = "";
							if (!data || !data.length) {
								return
							}
							$.each(data, function (i, item) {
								html += "<option value='" + item.phoneNo + "'>" + item.userName + "</option>";
							})
							$("#phoneList").html(html).attr("data-isCreated","created");
						},
						error: function () {
							showMessage("获取数据失败", "error");
						}

					})
				}
			}
			//获取主活动下的所有子活动信息（不包含主活动下拿到的 PCC策略 和 效果评估 信息）
			$.ajax({
				url:"./iop/getCampaginInfoById?activityId="+activityId,
				type:"get",
				dataType:"json",
				success:function (res) {
                    if( res.code != 0 ){
                        showMessage(res.msg,"error");
                        return
                    }

					var data = res.data;
					var html = "";
					var campaigns = data.campaginInfoList;
					//var zpList = data.belongersInfoList;
					//动态添加子活动导航
					if( campaigns.length ){
						for(var i=0; i<campaigns.length;i++){
							html += '<li>';
							html +=		'<span data-campaignid="'+campaigns[i].campaignId+'">'+campaigns[i].campaignName+'</span>';
							html +=	'</li>';
						}
						$('.j-campain-nav').html(html);
						//initCampaignNav(campaigns,zpList);
						initCampaignNav(campaigns);
						$('.j-campain-nav li').eq(0).trigger('click');
					}else{
						html += '<li>';
						html +=		'<span>当前活动下无关联子活动</span>';
						html +=	'</li>';
						$('.j-campain-nav').html(html);
						$("#pdUserName").text("无");
					}
				},
				error:function () {
					showMessage("获取数据失败","error");
				}
			});
			//获取子活动下的 PCC策略 和 效果评估 信息（从主活动中获取）
			$.ajax({
				url:"./iop/getCampaginPublicAttrInfo?activityId="+activityId,
				type:"get",
				dataType:"json",
				success:function (res) {
                    if( res.code != 0 ){
                        showMessage(res.msg,"error");
                        return
                    }
					var data = res.data.publicAttrList;
					//填充子活动列表中共有属性 —— PCC策略 和 效果评估
					fillCampaginTab.fillPublic(data);
				},
				error:function () {
					showMessage("获取数据失败","error");
				}
			})
		})
	}

	$("#search").keypress(function (e) {
		if (e.keyCode == 13)
			activityTable.getPageData();
	});
	$("#search").next().click(function () {
		activityTable.getPageData();
	})

	return activityTable;
}
//子活动中-活动关联
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
			url: "./activities/getActivityList",
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
				url: "./activities/getActivityList",
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
			$(this).toggleClass("selected");
			var isChecked = $(this).hasClass("selected");
			isChecked ? $(this).prev("input").attr("checked", "checked") : $(this).prev("input").removeAttr("checked");
		});
	}

	relativeActivity.query = function(){
		getTableData();
	}

	return relativeActivity;
})();

function initCampaignNav(infoList,zpList){
	$('.j-campain-nav li').click(function(){
		$(".modal-wrap-detail .modal-box-content").slimScroll({
			scrollTo: "0px"
		})
		$(this).addClass('selected').siblings(".selected").removeClass('selected');
		var activityId = $("#tempActiviyId").val();
		var campaignId = $(this).find('span').attr('data-campaignid');

		if( globalAttrs.authority != 1 ){
			//获取当前集团下发活动已关联的活动
			$("#chosedActivities").tagsinput('removeAll');
			$.ajax({
				url: "./iop/getRelateCampaginAndCamp?campaignId="+campaignId,
				type: "get",
				dataType: "json",
				success:function (res) {
					if( res.code != 0 ){
						showMessage(res.msg,"error");
						return
					}
					var data = res.data;
					if( !data || !data.length ){
						return
					}
					$.each(data,function(i,item){
						var tagInfo = {};
						tagInfo.id = item.campId;
						tagInfo.text = item.campName
						$("#chosedActivities").tagsinput('add',tagInfo);
					});

				},
				error: function () {
					showMessage("获取关联活动失败","error");
				}
			});
		}
		//获取当前集团下发活动的活动所属人
		$.ajax({
			url: "./iop/getCampaignBelonger?activityId="+activityId+'&campaignId='+campaignId,
			type: "get",
			dataType: "json",
			success:function (res) {
				if( res.code != 0 ){
					showMessage(res.msg,"error");
					return
				}
				var data = res.data;
				/**
				 * 判断是否有权限派单 globalAttrs.authority
				 *  0,-1否； 1：是
				 */
				var name = "未指派";
				if( !data ){
					name = "未指派";
				}else{
					/*for(var j=0;j<zpList.length;j++){
						if(zpList[j].CAMPAIGNID == campaignId){*/
							name = data.USER_NAME;
					/*		break
						}
					}*/
				}
				$("#pdUserName").text(name);


			},
			error: function () {
				showMessage("获取数据失败","error");
			}
		})



		//根据campaginId填充子活动
		if( !infoList || !infoList.length ){
			return
		}
		for (var i=0;i<infoList.length;i++){
			if( infoList[i].campaignId == campaignId ){
				fillCampaginTab.fillPrivate(infoList[i]);
				break;
			}
		}

	})
}

$(document).ready(function(){
	//初始化营销活动列表
	var param;
	var initActTable = new initActivityTable();
	initActTable.getPageData();

	//初始化活动关联列表活动选择tagsinput事件
	var relateActivity = new tagsOperate();
	relateActivity.initTagsIput("#chosedActivities");
	relateActivity.tableTagsinputEvent("#chooseActivity", "#activityTableBox", "#chosedActivities");

	//关联活动初始化slimscroll
	$(".tags-box").slimScroll({
		height: '88px',
		size: '5px',
		distance: '120px', //组件与侧边之间的距离
		alwaysVisible: true, //是否 始终显示组件
		disableFadeOut: false, //是否 鼠标经过可滚动区域时显示组件，离开时隐藏组件
		railVisible: true, //是否 显示轨道
		color: "#D0D4DA",
		railColor: '#F0F3F8', //轨道颜色

	})

	var scrollH = ($(window).height()-290)+"px";
	$(".modal-wrap-detail .modal-box-content").slimScroll({
		height:scrollH,
		color: "#a2a8af",
		alwaysVisible : true,
		size: "5px",
	});

	$.ajaxSetup({
		cache:false
	});

	$(".j-relate-activity>span").click(function (e) {
		e.preventDefault();

		var $parent = $(this).parent();
		$(this).toggleClass("opened");
		$(".table-box").toggleClass("hide");
		if( !$parent.attr("data-iscreated") ){
			$parent.attr("data-iscreated","created");
			initRelativeActivity.query();
		}

	});
	$(".cancel").click(function(){
		$(".j-relate-activity>span").removeClass("opened");
		$(".table-box").addClass("hide");
	});
	$(".j-rel-search").keypress(function (e) {
		if (e.keyCode == 13)
			initRelativeActivity.query();
	});
	$(".j-rel-search").next().click(function () {
		initRelativeActivity.query();
	})
	//取消后清空
	$(".modal-wrap-detail .modal-box-close").click(function () {
		$(".modal-wrap-detail .item-property").next().text("无");
		$(".input-clear").val("");
		$(".text-clear").text("");
		$("#chosedActivities").tagsinput("removeAll");
		$("#chooseActivity").removeAttr("data-iscreated");
		$(".j-rel-search").val("");
	});
	//指派活动
	$(".j-submit").click(function (e) {
		$.blockUI.defaults.overlayCSS.backgroundColor = '#fff';
		$.blockUI.defaults.overlayCSS.opacity = '0.8';
		$.blockUI({
			message:"派单提交中，请稍等...",
			css:{
				border:"none",
				fontSize:"16px"
			}
		});
		e.preventDefault();
		var activityId = $("#tempActiviyId").val();
		var campaignId = $(".j-campain-nav li.selected>span").attr("data-campaignid");
		var executorPhone = $("#phoneList").val();
		var executorName = $("#phoneList").find("option:selected").text();

		$.ajax({
			url: "./iop/setActivityInfoToUser",
			type: "post",
			data:{
					'activityId':activityId,
					'campaignId':campaignId,
					'executorPhone':executorPhone
				},
			dataType: "json",
			success:function (res) {
				$.unblockUI();
				if( res.code != 0 ){
					showMessage(res.msg,"error");
					return
				}
				showMessage("派单转派提交成功","success");
				$("#pdUserName").text(executorName);
			},
			error: function () {
				$.unblockUI();
				showMessage("派单转派提交失败","error");
			}
		})
	});
	//关联活动
	$(".j-submit-relate-activity").click(function(e){
		e.preventDefault();
		$.blockUI.defaults.overlayCSS.backgroundColor = '#fff';
		$.blockUI.defaults.overlayCSS.opacity = '0.8';
		$.blockUI({
			message:"数据保存中,请稍等...",
			css:{
				border:"none",
				fontSize:"16px"
			}
		});

		var campaignId = $(".j-campain-nav li.selected>span").attr("data-campaignid");
		var tagsList = $("#chosedActivities").tagsinput("items");
		var newTagsArr = [];
		if( !tagsList.length ){
			showMessage("请选择关联活动","error");
			return
		}
		$.each(tagsList,function (i, tag) {
			var item = {};
			item.campId = tag.id;
			item.campName = tag.text;
			item.campaignId = campaignId;
			newTagsArr.push(item);
		})
		$.ajax({
			url: "./iop/saveRelateCampaginAndCamp",
			type: "post",
			data: JSON.stringify(newTagsArr),
			dataType:"json",
			contentType:"application/json",
			success:function(res){
				$.unblockUI();
				if( res.code != 0 ){
					showMessage(res.msg,"error");
					return
				}
				showMessage("关联活动提交成功","success");
			},
			error: function () {
				$.unblockUI();
				showMessage("获取数据失败","error");
			}
		})
	})
});









