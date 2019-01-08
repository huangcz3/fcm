function popupConfig(type) {
	/**
	 * type = 1; 历史导入弹框
	 * type = 2;自定义导入弹框
	 * type = 3; 集市导入弹框
	 * type = 4; 推荐业务弹框
	 * type = 5;
	 * type = 6; 数字内容
	 */
	/*var tableCheckboxEvent = function(e) {
		var isChecked = $(this).closest("tr").hasClass("selected");
		if (isChecked) {
			$(this).closest("tr").removeClass("selected");
			$(this).closest("tr").find("span").removeClass("selected");
			$(this).closest("tr").removeClass("selected").find("input").removeAttr("checked");
		} else {

			$(this).closest("tr").addClass("selected");
			$(this).closest("tr").find("span").addClass("selected");
			$(this).closest("tr").find("input").attr("checked", "checked");
		}
	}
*/	var popup_1 = function($context) {
		var template = '<!-- 目标用户-历史导入弹框 -->' +
			'<div id="historyInput" class="popup-box history-popup modal-item">' +
			'<ul class="tab-nav clearfix" >' +
			'<li class="selected">' +
			'<a href="##" data-href="hisCustomInput" data-type="3">自定义导入</a>' +
			'</li>' +
			'<li>' +
			'<a href="##" data-href="hisLabelInput" data-type="1">标签导入</a>' +
			'</li>' +
			'<li>' +
			'<a href="##" data-href="hisGroupInput" data-type="2">集市导入</a>' +
			'</li>' +
			'</ul>' +
			'<div class="tab-content selected"  id="hisCustomInput">' +
			'<div class="row" >' +
			'<div class="lable-table-wrap modal-box-wrap">' +
			'<table id="customTable" cellspacing="0">' +
			'</table>' +
			'</div>' +
			'<div id="customPage" class="popup-page"></div>' +
			'</div>' +
			'<div class="row btns">' +
			'<button id="" class="ensure border_radius" data-type="3">确定</button>' +
			'<button id="" class="cancel border_radius">取消</button>' +
			'</div>' +
			'</div>' +
			'<div class="tab-content"  id="hisLabelInput">' +
			'<div class="row" >' +
			'<div class="lable-table-wrap">' +
			'<table id="labelTable" cellspacing="0">' +
			'</table>' +
			'</div>' +
			'<div id="labelPage" class="popup-page"></div>' +
			'</div>' +
			'<div class="row btns">' +
			'<button id="" class="ensure border_radius" data-type="1">确定</button>' +
			'<button id="" class="cancel border_radius">取消</button>' +
			'</div>' +
			'</div>' +
			'<div class="tab-content"  id="hisGroupInput">' +
			'<div class="row" >' +
			'<div class="lable-table-wrap">' +
			'<table id="groupTable" cellspacing="0">' +
			'</table>' +
			'</div>' +
			'<div id="groupPage" class="popup-page"></div>' +
			'</div>' +
			'<div class="row btns">' +
			'<button id="" class="ensure border_radius" data-type="2">确定</button>' +
			'<button id="" class="cancel border_radius">取消</button>' +
			'</div>' +
			'</div>' +
			'</div>';

		if ($("#historyInput").length) {
			return;
		} else {
			$context.append(template);

		}

		var fillCustomTable = function(data,id) {
			var html = '';
			if(data && data.length !== 0){
				for (var i = 0; i < data.length; i++) {
					html += "<tr class='checkbox'>";
					html += "<td style='width: 5%;'>";
					html += "<label>";
					html += "<input type='checkbox'>";
					html += "<span></span>";
					html += "</label>";
					html += "</td>";
					html += "<td class='hide' data-id='" + data[i].customerGroupId + "' data-amount='" + data[i].amount + "' data-cycle='" + data[i].cocGroupCycle + "'>" + data[i].customerGroupId + "</td>";
					html += "<td>" + data[i].customerGroupName + "</td>";
					html += "<td>" + data[i].amount + "</td>";
					html += "</tr>";
				}
				$(id).html(html);
			}else{
				$(id).html("<tr><td colspan='4'>暂无数据</td></tr>");
			}

		}

		//历史导入
		function getHisInputList(type) { //type: 3.自定义导入 ； 2. 集市导入； 1.标签导入
			var total = 1;
			$.ajax({
				url: "./customerGroups/?createType=" + type + '&currentPage=1&pageSize=5',
				type: 'get',
				datatType: 'json',
				success: function(res) {
					if (res.code == 0) {
						var data = res.data.dataList;
						var totalPage = res.data.totalPage || 1;
						switch (type) {
							case "3":
								fillCustomTable(data,"#customTable");
								initPage("#customPage", 3, totalPage, pageChangeFuncs(3), res.data.totalRecord);
								break;
							case "1":
								fillCustomTable(data,"#labelTable");
								initPage("#labelPage", 1, totalPage, pageChangeFuncs(1), res.data.totalRecord);
								break;
							case "2":

								fillCustomTable(data,"#groupTable");
								initPage("#groupPage", 2, totalPage, pageChangeFuncs(2), res.data.totalRecord);
								break;
							default:
						}
					}else{
						//start测试
						 /*if(res.code == "-1"){
						 res = cocCustomerGroupInfos;
						 data = res.data.dataList;
						 totalPage = res.data.totalPage || 1;
						 fillCustomTable(data,"#labelTable");
						 initPage("#labelPage", 1, totalPage, pageChangeFuncs(1), res.data.totalRecord);
						 return
						 }*/
						 //end测试
						showMessage(res.msg,"error");
					}
				},
				error: function() {
					showMessage("获取信息失败","error");
				}
			})
		}


		//分页上下页点击事件
		function pageChangeFuncs(type) {
			var id;
			if(type == 3){
				id = "#customTable"
			}else if(type == 2){
				id = "#groupTable";
			}else{
				id = "#labelTable"
			}
			return function customDoChange(obj) {
				var param = getCustomerInputParam();

				var array = [];
				isRemoveAll("#historyInputWrap");
				$(id + " td label input[checked='checked']").closest("tr")
					.find("td[data-id]").each(function(index, item) {
						//array.push($(item).attr("data-id")+ "_(" + $(item).attr("data-amount")  + ")_" + $(item).next().text()+"_"+type+ "_" + $(item).attr("data-cycle") );
						var tagItem = {};
						tagItem.cycle = $(item).attr("data-cycle") || 0;
						tagItem.id = $(item).attr("data-id");
						tagItem.amount = $(item).attr("data-amount");
						tagItem.name = $(item).next().text();
						tagItem.type = type.toString();
						array.push(tagItem);
					});
				if (array.length) {
					if (shouldRemoveAll_l) {
						$("#selectedUser").tagsinput("removeAll");
						shouldRemoveAll_l = false;
					}
				}
				for (var i = 0; i < array.length; ++i) {
					/*var temp = array[i].split("_")
					var tag = {
						id: temp[0]+"_"+temp[2] + "_" + temp[3]+ "_" +temp[4],
						text: temp[2] +"" +temp[1]
					}*/
					var tag = {
						"id": array[i].id,
						"text": array[i].name+"("+array[i].amount+")",
						"name": array[i].name,
						"createType":array[i].type,
						"cycle": array[i].cycle
					}
					$("#selectedUser").tagsinput('add', tag);
				}
				isTabChanged_l = false;
				$.ajax({
					url: "./customerGroups/",
					type: 'get',
					data: param,
					datatType: 'json',
					success: function(res) {
						if (res.code === 0) {
							fillCustomTable(res.data.dataList,id);
						}else{
							showMessage(res.msg,"error")
						}
					},
					error: function() {
						showMessage("获取信息失败","error")
					}
				});
			}
		}

		//生成分页
		function initPage(id, type, pages, func, totals) {
			$(id).html("");
			var pager = new Pager({
				type: type,
				total: pages,
				parent: $(id)[0],
				onchange: func,
			});
			$(id + " .cur").before("<span>第</span>");
			$(id + " .total-page").after("<span>页,共" + (totals || "0") + "条</span>");
		}

		$(function() {
			$("#historyInput .tab-nav li").click(function t(e) {
				isTabChanged_l = false;

				/*if((this !== t.$lastEle) && t.isStart) { // if the target ele isn't the lastele,
					isTabChanged_l = true;	// then clear tagsinput.
					//shouldRemoveAll_l = true;
				}*/
				var type = $(this).find("a").attr("data-type");

				type = type.toString();
				getHisInputList(type);
				/*t.$lastEle = this;
				t.isStart = true;*/
			})
			$("#historyInput table").on('click', '.checkbox label span',popupEvents.tableCheckboxEvent);
		})
	};

	//自定义导入
	var popup_2 = function($context) {
		var template = '<!-- 目标用户-自定义导入弹框 -->' +
			'<div id="customInput" class="popup-box custom-popup modal-item">' +
			'<ul class="tab-nav clearfix" >' +
			'<li class="selected">' +
			'<a href="##" data-href="standardMode">标准模式</a>' +
			'</li>' +
			'<li>' +
			'<a href="##" data-href="customMode">自定义模式</a>' +
			'</li>' +
			'</ul>' +
			'<div class="tab-content selected"  id="standardMode">' +

			'<div class="row fileinput fileinput-new fileUpLoad"  data-provides="fileinput">' +
			'<span class="btn-file ">' +
			'<span class="fileinput-new"> 选择文件</span>' +
			'<span class="fileinput-exists"> 另选文件 </span>' +
			'<input type="hidden" name="file">' +
			'<input type="file"  name="file" id="file"  accept="text/plain,text/csv"> </span>' +
			'<span class="fileinput-filename"></span> &nbsp;' +
			'<a href="javascript:;" class="close fileinput-exists" data-dismiss="fileinput">× </a>' +
			'</div>' +

			'<div class="row btns">' +
			'<button id="" class="ensure border_radius">确定</button>' +
			'<button id="" class="cancel border_radius">取消</button>' +
			'<a href=" ./files/downLoadTemplate?customizeFlag=0" class="link-grey">模板下载</a>' +
			'</div>' +
			'</div>' +
			'<div class="tab-content"  id="customMode">' +
			'<div class="row fileinput fileinput-new fileUpLoad" data-provides="fileinput" >' +
			'<span class="btn-file ">' +
			'<span class="fileinput-new"> 选择文件</span>' +
			'<span class="fileinput-exists"> 另选文件 </span>' +
			'<input type="file" name="file" id="file2" accept="text/plain,text/csv"> </span>' +
			'<span class="fileinput-filename"></span> &nbsp;' +
			'<a href="javascript:;" class="close fileinput-exists" data-dismiss="fileinput">× </a>' +
			'</div>' +
			'<div class="row btns">' +
			'<button id="" class="ensure border_radius">确定</button>' +
			'<button id="" class="cancel border_radius">取消</button>' +
			'<a href=" ./files/downLoadTemplate?customizeFlag=1" class="link-grey">模板下载</a>' +
			'</div>' +
			'</div>' +
			'</div>';


		if ($("#customInput").length) {
			return;
		} else {
			$context.append(template);
		}

	}

	//集市导入
	var popup_3 = function($context) {
		var template = '<!-- 目标用户-集市导入弹框 -->' +
			'<div id="marketInput" class="popup-box market-popup modal-item">' +
			'<form id="marketImportForm">'+
			'<div class="tab-content selected">' +
			'<div class="row" >' +
			'<label>数据库</label>' +
			'<select id="dataBaseList" name="mppDbName"></select>' +
			'</div>' +
			'<div class="row" >' +
			'<label>schema</label>' +
			'<input type="text" name="mppSchema"/>' +
			'</div>' +
			'<div class="row" >' +
			'<label>数据库表名</label>' +
			'<input type="text" name="mppTableName"/>' +
			'</div>' +
			'<div class="row" >' +
			'<label>客户群名称</label>' +
			'<input type="text" name="customerGroupName"/>' +
			'</div>' +
			'<div class="row">' +
			'<label>手机号字段名</label>' +
			'<input type="text" name="mppPhoneColumn" class="j-phoneNumber"/>' +
			'</div>' +
			'<div class="row">' +
			// '<label>是否包含营销语字段名</label>' +
			// '<div class="radio-box clearfix">'+
			// '<p>'+
			// '<input class="radio-input" type="radio" name="customizeFlag" value="1" checked>'+
			// '<label><span class="selected"></span>是</label>'+
			// '</p>'+
			// '<p>'+
			// '<input class="radio-input" type="radio" name="customizeFlag" value="0">'+
			// '<label><span></span>否</label>'+
			// '</p>'+
			// '</div>'+
			'<p class="checkbox margin-top-10 j-validate-null">'+
			'<input type="checkbox" value="1" name="customizeFlag">'+
			'<label class="col-mar"><span></span>包含营销语字段</label>'+
			'</p>'+
			'</div>' +
			'<div class="row hide u-marketing-column">' +
			'<label>营销语字段名</label>' +
			'<input type="text" name="mppMarketingColumn"/>' +
			'</div>' +
			'<div class="row btns">' +
			'<button class="ensure border_radius">确定</button>' +
			'<button class="cancel border_radius">取消</button>' +
			'</div>' +
			'</div>' +
			'</form>'+
			'</div>';
		if ($("#marketInput").length) {
			return;
			$("#marketInput").css("display", "block");
		} else {
			$context.append(template);
			// $(".radio-box p").click(function(){
			// 	var $this = $(this);
			// 	var $sib = $this.siblings("p");
			// 	var $thisInput = $this.find("input");
			// 	var $slogan = $(".u-marketing-column");
			// 	$sib.find("input").removeProp("checked");
			// 	$sib.find("span").removeClass("selected");
			// 	$thisInput.prop("checked",true);
			// 	$this.find("span").addClass("selected");
            //
			// 	if($thisInput.val() == "1"){
			// 		$slogan.show()
			// 	}else{
			// 		$slogan.hide();
			// 		$slogan.find("input").val("");
			// 	}
			// });
			var $panel = $("#marketInput");
			$panel.find(".checkbox label").on("click",function () {
				var $this = $(this)
					$prev = $this.prev(),
				    $slogan = $(".u-marketing-column");

				htmlEvents.checkboxEvent.bind(this)();
				if( $prev.prop("checked") ){
					$slogan.removeClass("hide")
				}else{
					$slogan.addClass("hide");
					$slogan.find("input").val("");
				}
			});
		}

	}

	//推荐业务
	var popup_4 = function($context) {
		var template = '<!-- 统计口径-推荐业务 -->' +
			'<div id="recommand" class="popup-box reco-popup modal-item" style="width: 500px;">' +
			'<ul class="tab-nav clearfix" >' +
			'<li class="selected">' +
			'<a href="##" data-href="fee" data-type="1">资费</a>' +
			'</li>' +
			'<li>' +
			'<a href="##" data-href="vicAct" data-type="2">营销活动</a>' +
			'</li>' +
			'<li>' +
			'<a href="##" data-href="brodcast" data-type="3">宣传</a>' +
			'</li>' +
            /*'<li>' +
			 '<a href="##" data-href="common" data-type="4">常用</a>' +
			 '</li>' +*/
			'</ul>' +
			'<div class="tab-content selected"  id="fee" data-type="0">' +
			'<div class="row wrap-border" >' +
			'<div class="lable-table-wrap">' +
			'<div class="search-box">' +
			'<input type="text" class="search" style="width: 100%;">' +
			'<i></i>' +
			'</div>' +
			'<table id="recoTable">' +
			'</table>' +
			'</div>' +
			'<div id="recoPage" class="popup-page"></div>' +
			'</div>' +
			'<div class="row btns">' +
			'<button id="" class="ensure border_radius">确定</button>' +
			'<button id="" class="cancel border_radius">取消</button>' +
			'</div>' +
			'</div>' +
			'<div class="tab-content"  id="vicAct" data-type="1">' +
			'<div class="row wrap-border" >' +
			'<div class="lable-table-wrap">' +
			'<div class="search-box">' +
			'<input type="text" class="search" style="width: 100%;">' +
			'<i></i>' +
			'</div>' +
			'<div class="lable-table">'+
			'<table id="vicActTable">' +
			'</table>' +
			'</div>'+
			'</div>' +
			'<div id="vicActPage" class="popup-page"></div>' +
			'</div>' +
			'<div class="row btns">' +
			'<button id="" class="ensure border_radius">确定</button>' +
			'<button id="" class="cancel border_radius">取消</button>' +
			'</div>' +
			'</div>' +
			'<div class="tab-content"  id="brodcast" data-type="2">' +
			'<div class="row wrap-border" >' +
			'<div class="lable-table-wrap">' +
			'<div class="search-box">' +
			'<input type="text" class="search" style="width: 100%;">' +
			'<i></i>' +
			'</div>' +
			'<table id="brodcastTable">' +
			'</table>' +
			'</div>' +
			'<div id="brodcastPage" class="popup-page"></div>' +
			'</div>' +
			'<div class="row btns">' +
			'<button id="" class="ensure border_radius">确定</button>' +
			'<button id="" class="cancel border_radius">取消</button>' +
			'</div>' +
			'</div>' +
            '<div class="tab-content"  id="common" data-type="3">' +
            '<div class="row wrap-border" >' +
            '<div class="lable-table-wrap">' +
            '<div class="search-box">' +
            '<input type="text" class="search" style="width: 100%;">' +
            '<i></i>' +
            '</div>' +
            '<table id="commonTable">' +
            '</table>' +
            '</div>' +
            '<div id="commonPage" class="popup-page"></div>' +
            '</div>' +
            '<div class="row btns">' +
            '<button id="" class="ensure border_radius">确定</button>' +
            '<button id="" class="cancel border_radius">取消</button>' +
            '</div>' +
            '</div>' +
			'</div> ';

		if ($("#recommand").length) {
			return;
			$("#recommand").css("display", "block");
		} else {
			$context.append(template);
		}

		//填充资费列表
		var fillFeeTable = function(data) {
				var html = '';
				for (var i = 0; i < data.length; i++) {
					html += "<tr class='checkbox'>";
					html += "<td style='width:30px'>";
					html += "<label>";
					html += "<input type='radio' name='fee'>";
					html += "<span></span>";
					html += "</label>";
					html += "</td>";
					html += "<td style='width: 25%' data-id='" + data[i].PRC_ID + "'>" + data[i].PRC_ID + "</td>";
					html += "<td data-name='" + data[i].PRC_NAME + "'>" + data[i].PRC_NAME + "</td>";
					html += "</tr>";
				}

				$('#recoTable').html(html);
			}
			//填充营销活动列表
		var fillVicActTable = function(data) {
			var html = "";
			for (var i = 0; i < data.length; ++i) {
				html += "<tr class='checkbox big-class'>";
				html += "<td style='width:50px '>";
				html += "<label>";
				html += "<input type='radio' name='fee'>";
				html += "<span></span>";
				html += "</label>";
				html += "</td>";
				html += "<td style='width: 25%' data-id='" + data[i].PRC_ID + "'>" + data[i].PRC_ID + "</td>";
				html += "<td data-name='" + data[i].PRC_NAME + "'>" + data[i].PRC_NAME + "</td>";
				html += "</tr>"; // 大类写好，写小类了哈。
				if (!data[i].CHILD)
					continue;
				for (var j = 0; j < data[i].CHILD.length; ++j) {
					html += "<tr class='checkbox small-class' data-father='"+ data[i].PRC_ID+"'>";
					html += "<td style='width:50px ;padding-left:20px'>";
					html += "<label>";
					html += "<input type='radio' name='fee'>";
					html += "<span></span>";
					html += "</label>";
					html += "</td>";
					html += "<td style='padding-left:10px;width: 25%' title='"+ data[i].CHILD[j].WAY_ID +"' data-id='" + data[i].CHILD[j].WAY_ID + "'>" + data[i].CHILD[j].WAY_ID + "</td>";
					html += "<td title='"+data[i].CHILD[j].WAY_NAME+"' data-name='" + data[i].CHILD[j].WAY_NAME + "'>" + data[i].CHILD[j].WAY_NAME + "</td>";
					html += "</tr>";
				}
			}
			$('#vicActTable').html(html);
	}

		//填充宣传列表
		var fillBrodcastTable = function(data) {
			var html = '';
			for (var i = 0; i < data.length; i++) {
				html += "<tr class='checkbox'>";
				html += "<td style='width:30px '>";
				html += "<label>";
				html += "<input type='radio' name='fee'>";
				html += "<span></span>";
				html += "</label>";
				html += "</td>";
				html += "<td style='width: 25%' data-id='" + data[i].PRC_ID + "'>" + data[i].PRC_ID + "</td>";
				html += "<td data-name='" + data[i].PRC_NAME + "'>" + data[i].PRC_NAME + "</td>";
				html += "</tr>";
			}

			$('#brodcastTable').html(html);
		}
        //填充常用列表
        var fillCommonTable = function(data) {
            var html = '';
            for (var i = 0; i < data.length; i++) {
                html += "<tr class='checkbox'>";
                html += "<td style='width:30px'>";
                html += "<label>";
                html += "<input type='radio' name='common'>";
                html += "<span></span>";
                html += "</label>";
                html += "</td>";
                html += "<td style='width: 25%' data-id='" + data[i].PRC_ID + "'>" + data[i].PRC_ID + "</td>";
                html += "<td data-name='" + data[i].PRC_NAME + "'>" + data[i].PRC_NAME + "</td>";
                html += "</tr>";
            }

            $('#commonTable').html(html);
        }

		var getRecommandList = function(type, page) { //type是推荐业务类型，1：自费;2：营销活动;3：宣传
			var total = 1;
			// var param = {
			// 	prcType:0,
			// 	nowPageNum:1,
			// 	queryStr:""};
			var param = getBusinessParam();
			if (page) {
				param.nowPageNum = page;
			}
			$.ajax({
				url: "./base/getRecommendProductInfo",
				data: param,
				dataType: "json",
				type: "get",
				success: function(data) {
					if (data.code === 0) {
						data = data.data;
						var total = data.allTotals || 1;
						switch (type) {
							case "1":
								fillFeeTable(data.businessList);
								initPage("#recoPage", 1, total, pageChangeFuncs(1), data);
								break;
							case "2":
								fillVicActTable(data.businessList);
								initPage("#vicActPage", 2, total, pageChangeFuncs(2), data);
								break;
							case "3":
								fillBrodcastTable(data.businessList);
								initPage("#brodcastPage", 3, total, pageChangeFuncs(3), data);
								break;
							case "4":
                                fillCommonTable(data.businessList);
                                initPage("#commonPage", 4, total, pageChangeFuncs(4), data);
                                break;
							default:
						}
					}else{
						showMessage(data.msg,"error");
					}
				},
				error: function(err) {
					showMessage("获取数据失败","error");
				}
			});

		}

		//分页上下页点击事件
		function pageChangeFuncs(type) {
			return function doChange(obj) {

				var id = "";
				switch (type) {
					case 1:
						id = "#recoTable";
						break;
					case 2:
						id = "#vicActTable";
						break;
					case 3:
						id = "#brodcastTable";
					case 4:
						id = "#commonTable";
						break;
				}

                var array = [];
                var checked = $(id + " td label input[checked='checked']").closest("tr").find("td[data-id]");
				$.each(checked,function(index, item) {
                        //array.push($(item).attr("data-id")+"_"+$(item).next().text());
						var tagItem = {};
						tagItem.id = $(item).attr("data-id");
						tagItem.text = $(item).next().text();
						if ($(item).closest('tr').hasClass('big-class')){  //营销活动，大类下有小类，传小类;如果大类下无小类，则传大类
							if( checked.length != 1 ){
								return true;
							}
						}
						array.push(tagItem);
                    });
                if (array.length) {
                    if(type == 3){   //宣传只会传一个值，每次tagsinput值时，都会清空
                        shouldRemoveAll = true;
                    }
					if( type == 2){  //营销活动，只能传相同大类下的小类，大类不同，清空
						var nowBigClass = $('#recommand .tab-nav [data-href="vicAct"]').data("bigclassId");
						if( nowBigClass ){
							if( lastBigClass && lastBigClass != nowBigClass){
								shouldRemoveAll = true;
							}
							lastBigClass = nowBigClass
						}
					}
                    if (shouldRemoveAll) {
                        $("#chosedBussiness").tagsinput("removeAll");
                        shouldRemoveAll = false;
                    }					}
                for (var i = 0; i < array.length; ++i) {

                   var tag = {
					   "id": array[i].id,
					   "text": array[i].text
				   }
                    $("#chosedBussiness").tagsinput('add', tag);
                }

				isTabChanged = false;
				var param = getBusinessParam();
				$.ajax({
					url: "./base/getRecommendProductInfo",
					type: 'get',
					data: param,
					datatType: 'json',
					success: function(res) {
						if (res.code === 0) {
							switch (type) {
								case 1:
									fillFeeTable(res.data.businessList);
									break;
								case 2:
									fillVicActTable(res.data.businessList);
									break;
								case 3:
									fillBrodcastTable(res.data.businessList);
                                    break
                                case 4:
                                    fillCommonTable(res.data.businessList);
									break;
							}
						}else{
                            showMessage(res.msg,"error");
                        }
					},
					error: function() {
                        showMessage("获取信息失败","error");
					}
				});
			}
		}

		//生成分页
		function initPage(id, type, total, func, data) {
			$(id).html("");
			var pager = new Pager({
				type: type,
				total: Math.ceil(total / 5),
				parent: $(id)[0],
				onchange: func,
			});
			$(id + " .cur").before("<span>第</span>");
			$(id + " .total-page").after("<span>页,共" + (data.allTotals) + "条</span>");
		}

		//推荐业务tab面板点击事件
		$(function() {
			var t = {};
            function clearChoosedItem (context) {
                $(context).closest("tbody").find("tr").removeClass('selected')
                    .find("input").attr("checked", false)
                    .next("span").removeClass("selected");
                $(context).closest('tr').addClass('selected')
                    .find("input").attr("checked", 'checked')
                    .next("span").addClass("selected");
            }
			//弹窗tab切换，获取数据
			$("#recommand .tab-nav>li").click(function t() {
				isTabChanged = false;
				nowChoosedItem = 0;
				if((this !== t.$lastEle) && t.isStart) { // if the target ele isn't the lastele,
					isTabChanged = true;	// then clear tagsinput.
					shouldRemoveAll = true;
				}
				$(this).addClass("selected").siblings(".selected").removeClass("selected");
				var id = $(this).find("a").attr("data-href");
				$("#" + id).addClass("selected").siblings(".selected").removeClass("selected");
				var type = $(this).find("a").attr("data-type");
				getRecommandList(type,1);
				t.$lastele = this;
				t.isStart = true;
			});

            //资费表格最多选5个
            $("#recoTable").on('click', ' .checkbox label span', function tt(e) {
				var isChecked = $(this).closest("tr").hasClass("selected");
				var nowNavItem = $("#recommand .tab-nav li.selected")[0];
				if ( nowNavItem != tt.mainIndex) {
					nowChoosedItem = 0;
					tt.mainIndex = nowNavItem; // 缓存现在的导航
				}
				if (isChecked) {
					--nowChoosedItem;
					$(this).closest("tr").removeClass("selected");
					$(this).closest("tr").find("span").removeClass("selected");
					$(this).closest("tr").removeClass("selected").find("input").removeAttr("checked");
				} else {
					if (nowChoosedItem === 5 && $(nowNavItem).text() != '营销活动') // 营销活动不及条数
						return;
					++nowChoosedItem;
					$(this).closest("tr").addClass("selected");
					$(this).closest("tr").find("span").addClass("selected");
					$(this).closest("tr").find("input").attr("checked", "checked");
				}
			});

            //常用表格最多选5个
            $("#commonTable").on('click', ' .checkbox label span', function tt(e) {
                var isChecked = $(this).closest("tr").hasClass("selected");
                var nowNavItem = $("#recommand .tab-nav li.selected")[0];
                if ( nowNavItem != tt.mainIndex) {
                    nowChoosedItem = 0;
                    tt.mainIndex = nowNavItem; // 缓存现在的导航
                }
                if (isChecked) {
                    --nowChoosedItem;
                    $(this).closest("tr").removeClass("selected");
                    $(this).closest("tr").find("span").removeClass("selected");
                    $(this).closest("tr").removeClass("selected").find("input").removeAttr("checked");
                } else {
                    if (nowChoosedItem === 5 && $(nowNavItem).text() != '营销活动')
                        return;
                    ++nowChoosedItem;
                    $(this).closest("tr").addClass("selected");
                    $(this).closest("tr").find("span").addClass("selected");
                    $(this).closest("tr").find("input").attr("checked", "checked");
                }
            });


            //营销活动表格复选框，绑定点击事件
            // 若选择了大类,则全选小类, 传送大类值，大类只能选择一个
            // 若只选择一个小类（只可选择一个小类）, 则传小类值
            $("#vicAct table .checkbox label span").unbind("click");
            $('#vicActTable').on("click", " .checkbox label span", function(e) {
                var $closestTr = $(this).closest("tr");
                var $nowBigClassId = ( $closestTr.hasClass('big-class') && $closestTr.find('[data-id]').text() ) ||
                    ($closestTr.hasClass('small-class') && $($closestTr.prevAll(".big-class")[0]).find('[data-id]').text() );
                if (!t.$dataTarget) {
                    t.$dataTarget = $('#recommand .tab-nav [data-href="vicAct"]');
                    t.$dataTarget.data('bigclassId',
                        ($closestTr.hasClass('big-class') && $closestTr.find('[data-name]').text())
                        || ($closestTr.hasClass('small-class') && $($closestTr.prevAll(".big-class")[0]).find('[data-id]').text()));
                    t.$lastBigClassId = ( $closestTr.hasClass('big-class') && $closestTr.find('[data-id]').text() )
                        || ($closestTr.hasClass('small-class') && $($closestTr.prevAll(".big-class")[0]).find('[data-id]').text());
                }

                if ($closestTr.hasClass("big-class selected")) {	//该大类取消选中，则全部取消子类
                    $closestTr.removeClass("selected").find("input").attr("checked", false).next("span").removeClass("selected");
                    $(this).closest("tr").nextUntil(".big-class").removeClass('selected')
                        .find("input").attr("checked", false)
                        .next("span").removeClass("selected");
                    t.$dataTarget.data("bigclassId",'');
                }else if ($closestTr.hasClass("big-class")){	// 选中大类，则全选小类
					if( t.$lastBigClassId != $nowBigClassId ){
						t.$lastBigClassId = $nowBigClassId
					}
					clearChoosedItem(this);
                    $closestTr.nextUntil(".big-class").addClass('selected')
                        .find("input").attr("checked", "checked")
                        .next("span").addClass("selected");
                    t.$dataTarget.data("bigclassId", $closestTr.find('[data-id]').text()); // 存入大类名称
                }else if($closestTr.hasClass("small-class selected")) { // 该小类取消选中
                    /*if($($closestTr.prevAll(".big-class")[0]).hasClass("selected")){
                        $(this).closest("tbody").find("tr").removeClass('selected')
                            .find("input").attr("checked", false)
                            .next("span").removeClass("selected");
                    }else{*/
                        $closestTr.removeClass("selected").find("input").attr("checked", false).next("span").removeClass("selected");
                   // }

					//大类选中状态时，一个小类取消选中，大类自动取消勾选
					var all = $($closestTr.prevAll(".big-class")[0]).nextUntil(".big-class");
					var sel = $($closestTr.prevAll(".big-class")[0]).nextUntil(".big-class",".selected");
					if( all.length != sel.length  && $($closestTr.prevAll(".big-class")[0]).hasClass("selected") ){
						$($closestTr.prevAll(".big-class")[0]).removeClass('selected')
							.find("input").attr("checked", false)
							.next("span").removeClass("selected");
					}

					//无选中小类
					if( !$("#vicActTable tr.selected").length ){
						t.$dataTarget.data("bigclassId","");
					}
                }else if($closestTr.hasClass("small-class")){ // 勾选小类
                    //clearChoosedItem(this);
					if( t.$lastBigClassId != $nowBigClassId ){  //跨大类选小类，删除原来所选
						clearChoosedItem(this);
						t.$lastBigClassId = $nowBigClassId;
					}else{
						$closestTr.addClass('selected')
							.find("input").attr("checked", "checked")
							.next("span").addClass("selected");
					}
					//选择了所有小类，大类自动勾选
					var all = $($closestTr.prevAll(".big-class")[0]).nextUntil(".big-class");
					var sel = $($closestTr.prevAll(".big-class")[0]).nextUntil(".big-class",".selected");
					if( all.length == sel.length ){
						$($closestTr.prevAll(".big-class")[0]).addClass("selected")
							.find("input").attr("checked", "checked")
							.next("span").addClass("selected");
					}

                    t.$dataTarget.data("bigclassId", $($closestTr.prevAll(".big-class")[0]).find('[data-id]').text());
                }

            })
			$("#vicAct .lable-table-wrap .lable-table").slimScroll({
				height: '220px',
				color: "#a2a8af",
				alwaysVisible: true,
				size: "5px",
			});

            //宣传表格复选框点击，只能选择一个
            $("#brodcast").on("click", " .checkbox label span", function(){
                var $this = $(this);
                clearChoosedItem(this);
            });

			function query (e) {
				var type = $("#recommand .tab-nav>li.selected")
										.find("a").attr("data-type");
				getRecommandList(type, 1);
			}
			$("#recommand .tab-content").on("keypress",".search-box .search", function (e) {
				if (e.keyCode == 13)
					query(e);
			});
			$("#recommand .tab-content").on("click",".search-box i", query);
		});
	}


	// 活动关联
	var popup_5 = function($context) {
		var template = '<!-- 活动关联-选择活动 -->' +
			'<div id="task_ass" class="popup-box reco-popup modal-item">' +
			'<p>待选活动<span id="task_num">324</span></p>' +
			'<div class="tab-content selected"  id="fee">' +
			'<div class="row wrap-border" >' +
			'<div class="lable-table-wrap">' +
			'<div class="search-box">' +
			'<input type="text" class="search h-block">' +
			'<i></i>' +
			'</div>' +
			'<table>' +
			'<thead><tr><td width="width: 8%;">&nbsp;</td><td style="width: 20%;">活动名称</td><td  style="width: 30%;">推荐业务</td><td  style="width: 20%;">开始时间</td><td  style="width: 20%;">结束时间</td></tr></thead>' +
			'<tbody id="recoTable"></tbody>' +
			'</table>' +
			'</div>' +
			'<div id="recoPage" class="popup-page"></div>' +
			'</div>' +
			'<div class="row btns">' +
			'<button id="" class="ensure border_radius">确定</button>' +
			'<button id="" class="cancel border_radius">取消</button>' +
			'</div>' +
			'</div>' +
			'</div> ';

		if ($("#task_ass").length) {
			return;
			$("#task_ass").css("display", "block");
		} else {
			$context.append(template);
		}

		var fillTable = function(popup) {
				var html = '';
				for (var j = 0; j < popup.length; j++) {
					console.log(popup[j]);
					html += "<tr class='hidden checkbox'>";
					html += "<td>";
					html += "<label>";
					html += "<input type='checkbox'>";
					html += "<span></span>";
					html += "</label>";
					html += "</td>";
					html += "<td>" + popup[j].taskName + "</td>";
					html += "<td>" + popup[j].recommandTask + "</td>";
					html += "<td>" + popup[j].startDate + "</td>";
					html += "<td>" + popup[j].endDate + "</td>";
					html += "</tr>";
				}

				$('#recoTable').html(html);
			}


		//历史导入-标签导入

		var data = vicTaskPopupDataTest;
		data.totals = 22;
		fillTable(data);

		var pager = new Pager({
			type: 1,
			total: 5,
			parent: $("#recoPage")[0],
			// onchange:labelDoChange,
		});
		$("#recoPage .cur").before("<span>第</span>");
		$("#recoPage .total-page").after("<span>页,共" + data.totals + "条</span>");

		$(function() {
			$("#recoPage .ensure").click(function() {
				var sel = $("#recoTable td>p>input[checked]").closest("tr").children("td:last-child");
				//console.log(sel)
			})
		})
	}

	//数字内容
	var popup_6 = function($context) {
		var template = '<!-- 数字内容弹框 -->' +
			'<div id="figureCont" class="popup-box history-popup modal-item">' +
			'<ul class="tab-nav clearfix" >' +
			'<li class="selected">' +
			/*'<button class="j-get-result-btn">获取结果</button>' +*/
			'</li>' +
			'</ul>' +
			'<div class="tab-content selected" >' +
			'<div class="row" >' +
			'<div class="lable-table-wrap modal-box-wrap">' +
			'<table id="figureTable" cellspacing="0">' +
			/*'<tr><td>请跳转至数字内容页选择用户群，确定后再点击获取结果按钮</td></tr>'+*/
			'</table>' +
			'</div>' +
			'<div id="figurePage" class="popup-page"></div>' +
			'</div>' +
			'<div class="row btns">' +
			'<button id="" class="ensure border_radius" data-type="4">确定</button>' +
			'<button id="" class="cancel border_radius">取消</button>' +
			'</div>' +
			'</div>' ;
		if ($("#figureCont").length) {
			return;
		} else {
			$context.append(template);

		}

		$("#figureCont table").on('click', '.checkbox label span',popupEvents.tableCheckboxEvent);
	}


  var popup_7 = function($context) {
    var template = '<!-- 目标用户-历史导入弹框 -->' +
        '<div id="smartMapshow" class="popup-box smartMap-popup modal-item">' +
        '<ul class="tab-nav clearfix" >' +
        '<li class="selected">' +
        '<button id="choseArea" class="ensure border_radius btp">&nbsp;设定用户群&nbsp;</button>' +
        '</li>' +
        '<li>' +
        '<button id="showArea" class="ensure border_radius btp">查看已选用户群</button>' +
        '</li>' +
        '</ul>' +
        '<div class="tab-content selected"  id="smartMapInput">' +
        '<div class="row" >' +
        '<div class="lable-table-wrap modal-box-wrap">' +
        '<table id="smartMapTable">' +
        '</table>' +
        '</div>' +
        '</div>' +
        '<div class="row btns">' +
        '<button id="choseUser" class="ensure border_radius" data-type="3">确定</button>' +
        '<button id="" class="cancel border_radius">取消</button>' +
        '</div>' +
        '</div>';


    if ($("#smartMapshow").length) {
      return;
    } else {
      $context.append(template);
    }

    $("#choseArea").click(function(){
      updateActivityId();
      var taskId = $("#smartMapActivityId").val();
      clearTable("smartMapTable");
      clearTable("intelligentMapTableR");
      //window.open("http://10.113.254.17:8080/mobd-web/page/newTask?taskId="+taskId+"&taskType=1");
      //window.open("http://10.113.254.17:8080/mobd-web/page/newTask?taskId="+taskId+"&taskType=0&userId="+userId+"&city="+cityId);
		goToSmartMap(taskId,0);
    })

    $("#showArea").click(function(){
      var activityId = $("#smartMapActivityId").val();
      getChoseMapDataAndReloadTable(activityId);
    })

    layui.use('table', function(){
      var table = layui.table;

      table.render({
        elem: '#smartMapTable',
        //url:'/demo/table/user/',
				width:224,
        page: {
          layout: ['count', 'prev', 'page', 'next'] //自定义分页布局
					,limit:5
          ,groups: 1 //只显示 1 个连续页码
          ,first: false //不显示首页
          ,last: false //不显示尾页

        },
      cols: [[
				   {field:'name', title: '名称',width:110}
          ,{field:'type', title: '类型',width:110}
        ]],
				data:[]
      });
    });

  }
  var limitcount = 5;
  var curnum = 1;
  var popup_8 = function($context) {
    var template =
        '<div id="customerInsightshow" class="popup-box customerInsight-popup modal-item">' +
        '<div class="tab-content selected"  id="smartMapInput">' +
        '<div class="row" >' +
        '<div class="lable-table-wrap modal-box-wrap">' +
        '<table id="customerInsightTable" lay-filter="customerInsight">' +
        '</table>' +
        '<div id="laypage"></div>' +
        '</div>' +
        '</div>' +
        '<div class="row btns">' +
        '<button id="choseResult" class="ensure border_radius" data-type="3">确定</button>' +
        '<button id="" class="cancel border_radius">取消</button>' +
        '</div>' +
        '</div>';


    if ($("#customerInsightshow").length) {
      return;
    } else {
      $context.append(template);
    }

    var iconLength = $("#customerInsight").width();
    var showLength = $("#customerInsightshow").outerWidth();

    //$("#customerInsightshow").css("left",iconLength - showLength);
    //$("#customerInsightshow").css("left",0);
	  if((iconLength - showLength) < 0){
		  $("#customerInsight").css("top",0);
		  $("#customerInsightshow").css("left",iconLength - showLength);
	  }else{
		  $("#customerInsight").css("top",10);
		  $("#customerInsightshow").css("left",0);
	  }

    getResult(curnum,limitcount);
	}


  //列表查询方法
  function getResult(start,limitsize) {
    layui.use(['table','laypage'], function(){
      var table = layui.table;
      var laypage = layui.laypage;
      limit:5;
      table.render({
        elem: '#customerInsightTable'
        , url: "./customerInsight/pageResults?currentPage="+start+"&pageSize="+limitsize
				, width:340
        , cols: [[
          	{type:'checkbox',width:40}
          , {field: 'name', title: '名称',width:295}
        ]]
        , page: false
        ,done: function(res,curr, count){
          //如果是异步请求数据方式，res即为你接口返回的信息。
          //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
          laypage.render({
            elem:'laypage'
            ,count:count
            ,curr:curnum
            ,limit:limitcount
            ,groups:1
            ,layout: ['prev', 'next','page','count']
            ,first: false //不显示首页
            ,last: false //不显示尾页
            ,jump:function (obj,first) {
              if(!first){
                curnum = obj.curr;
                limitcount = obj.limit;
                getResult(curnum,limitcount);
              }
            }
          })
        }
      })
    })
  }



	switch (type) {
		case 1:
			return popup_1;
			break;
		case 2:
			return popup_2;
			break;
		case 3:
			return popup_3;
			break;
		case 4:
			return popup_4;
			break;
		case 5:
			return popup_5;
			break;
		case 6:
			return popup_6;
			break;
    case 7:
      return popup_7;
      break;
    case 8:
      return popup_8;
      break;
		default:
	};
}

var popupEvents = (function(){
	return events = {
			tableCheckboxEvent: function(e) {       //表格中复选框点击事件
				var isChecked = $(this).closest("tr").hasClass("selected");
				if (isChecked) {
					$(this).closest("tr").removeClass("selected");
					$(this).closest("tr").find("span").removeClass("selected");
					$(this).closest("tr").removeClass("selected").find("input").removeAttr("checked");
				} else {

					$(this).closest("tr").addClass("selected");
					$(this).closest("tr").find("span").addClass("selected");
					$(this).closest("tr").find("input").attr("checked", "checked");
				}
			}
	}

})();


