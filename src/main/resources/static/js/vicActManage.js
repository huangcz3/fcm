if ( !Array.prototype.forEach ) {   //ie8及以下不支持forEach方法
    Array.prototype.forEach = function forEach( callback, thisArg ) {
        var T, k;
        if ( this == null ) {
            throw new TypeError( "this is null or not defined" );
        }
        var O = Object(this);
        var len = O.length >>> 0;
        if ( typeof callback !== "function" ) {
            throw new TypeError( callback + " is not a function" );
        }
        if ( arguments.length > 1 ) {
            T = thisArg;
        }
        k = 0;

        while( k < len ) {

            var kValue;
            if ( k in O ) {
                kValue = O[ k ];
                callback.call( T, kValue, k, O );
            }
            k++;
        }
    };
}
String.prototype.trim = function () {   //trim方法
    return this .replace(/^\s\s*/, '' ).replace(/\s\s*$/, '' );
};
String.prototype.limit = function(n) {
    if (this.replace(/[\u4e00-\u9fa5]/g, "**").length <= n) {
        return this;
    } else {
        var len = 0;
        var tmpStr = "";
        for (var i = 0; i < this.length; i++) {//遍历字符串
            if (/[\u4e00-\u9fa5]/.test(this[i])) {//中文 长度为两字节
                len += 2;
            } else {
                len += 1;
            }
            if (len > n - 3) {
                break;
            } else {
                tmpStr += this[i];
            }
        }
        return tmpStr + "...";
    }
};
Array.prototype.removeByValue = function(val) {
    for(var i=0; i<this.length; i++) {
        if(this[i] == val) {
            this.splice(i, 1);
            break;
        }
    }
}

//非实时场景渠道
var sceneFlagChannel = "";
var globalAttrs = {};
globalAttrs.selectedType = null;
//部分全局函数
var htmlEvents = (function(){
    return {
        //展示图片
        showPic: function(param){
            /*$.ajax({
             url:'',
             type:'get',
             async:false,
             data:'',
             success:function(res){
             if(res.code == 0){

             }else{
             showMessage("读取图片信息异常","error")
             }
             },
             error:function(){
             showMessage("读取图片信息失败","error");
             }
             })*/
        },
        //清空活动详情信息
        clearDetails: function(id){
            $(id).find("[data-elemid]").each(function(i,item){
                $(item).text("");
            });
            $(id).find(".sceneTab").html("");
            $(id).find(".channel-tab").html("");
        },
        //清空附件列表
        clearAttachment: function(id){
            $(id).find("[data-attachment]").each(function(i,item){
                $(item).text("");
            });
        },
        //清空审批结果信息
        clearApprove : function(id){
            $(id).find(".result-content>.row").addClass(" hide");
            $(id).find(".result-title span:last-child").removeAttr("class").text("");
            $(id).find(".sub-row i").removeAttr("class");
            $(id).find(".sub-row span").text("");
        },
        //校验是否是url地址
        IsURL : function(str_url){  //只能是http/https开头
            var strRegex = '^((https|http)?://).+';
            var re=new RegExp(strRegex);
            //re.test()
            if (re.test(str_url)) {
                return (true);
            } else {
                return (false);
            }
        },
        //绑定label标签，单选 ( pcc场景)
        singleCheckbox: function (e) {
            $(this).parents(".checkbox ").find("input").not($(this).prev()[0]).removeAttr("checked");
            $(this).parents(".checkbox ").find("label").not(this).removeClass("selected");
            $(this).parents(".checkbox ").find("label").not(this).find("span").removeClass("selected");
            $(this).toggleClass("selected");
            $(this).find("span").toggleClass("selected");
            if ($(this).prev()) {
                if ($(this).prev().attr("checked") == "checked") {
                    $(this).prev().removeAttr("checked");
                } else {
                    $(this).prev().attr("checked", "checked");
                }
            }
        },
        //form表单提交，checkbox事件，.prop改变状态
        checkboxEvent: function(){
            var $this = $(this);
            var $prev = $this.prev();
            $this.toggleClass("selected");
            $this.find("span").toggleClass("selected");
            if ($prev) {
                if ($prev.prop("checked")) {
                    $prev.removeProp("checked");
                } else {
                    $prev.prop("checked", true);
                }
            }
        },
        //绑定$("table td span")标签，单选;temp1函数为多选
        tableSinglleCheckbox: function(e){
            var $this = $(this);
            var isChecked = $this.hasClass("selected");
            if( isChecked ){
                $this.removeClass("selected");
                $this.prev().removeAttr("checked");
            }else{
                $this.closest("table").find("label span.selected").removeClass("selected").prev().removeAttr("checked");
                $this.addClass("selected").prev().attr("checked","checked");
            }
        },
        showHintMsg:function(obj,msg){
            var $obj = $(obj);
            $obj.after("<i class='over-max-msg j-lack-param'>"+msg+"</i>");
            $obj.addClass("j-no-standard");
        },
        removeHintMsg:function(obj){
            if($(obj).hasClass("j-no-standard")){
                $(obj).next().remove();
                $(obj).removeClass("j-no-standard");
            }
        },
        //渠道互斥
        singleConflictChannel: function(channel){
            $childSpan = $(channel).find("span");
            if($childSpan.hasClass("selected")){
                /*if($childSpan.closest(".col-1").siblings(".col-1.conflict").find("span").hasClass("selected")){
					return;
				}else{*/
                $childSpan.closest(".col-1").siblings(".col-1").removeClass("hide");
                /*}*/
            }else{
                $childSpan.closest(".col-1").siblings(".col-1").addClass("hide");
                $("#channelListBox .col-1.hide").find("label>span.selected").trigger("click");
            }
        },
        //用户群
        customerGroup:{
            //集市导入获取数据库列表
            getDatabaseList: function () {
                var $select = $("#dataBaseList");
                if( $select.attr("data-iscreated") ){
                    return
                }
                $.ajax({
                    url: "./customerGroups/getDatabaseListByCity",
                    type: "get",
                    dataType: "json",
                    cache: false,
                }).done(function (res) {
                    if( res.code != 0 ){
                        showMessage(res.msg,"error");
                        return
                    }
                    var list = res.data;
                    var html = "<option value=''>==请选择==</option>";
                    if( !list || !list.length ){
                        $select.html(html);
                        return
                    }
                    $.each(list,function(i,database){
                        html += "<option value='"+ database.databaseId +"'>"+ database.databaseName +"</option>";
                    })
                    $select.html(html);
                    $select.attr("data-iscreated","created");
                }).fail(function () {
                    showMessage("获取数据库信息失败","error");
                })
            },
            //校验集市导入信息是否填写完整
            checkIsCompleted: function (params) {
                var result = true;
                for( var k in params ){
                    if( params[k] == ""){
                        if( k == 'mppMarketingColumn' && params['customizeFlag'] == 0){
                            continue
                        }
                        var text = $("[name='"+k+"']").prev().text();
                        showMessage(text+"不能为空，请填写完整","error");
                        result = false;
                        return
                    }
                }
                return result;
            },
            //创建集市导入用户群
            marketImport: function(params){
                var defer = $.Deferred();
                $.ajax({
                    url: "./customerGroups/createMppCustomerGroup",
                    type: "post",
                    data: JSON.stringify(params),
                    contentType: "application/json;charset='utf-8'",
                }).done(function(res){
                    if( res.code != 0 ){
                        showMessage(res.msg,"error");
                        defer.resolve(res.code);
                        return
                    }
                    var data = res.data;
                    var tag = {
                        "id": data.customerGroupId,
                        "text": data.customerGroupName+"("+data.amount+")",
                        "name": data.customerGroupName,
                        "createType":2,
                        "cycle": data.cocGroupCycle
                    };
                    $("#selectedUser").tagsinput('add',tag);
                    showMessage("用户群创建成功","success");
                    defer.resolve(res.code);
                }).fail(function () {
                    showMessage("用户群创建失败","error");
                    defer.reject();
                })
                return defer.promise();
            }
        },
    }
})();

$(document).ready(function() {
    //初始化网页顶部导航模块
    var topNav = new topNavigation();
    topNav.init();
    topNav.show("#marketingAct");
    //初始化网页左侧导航模块
    var sideNav = new sideNavigation();
    sideNav.init();
    sideNav.show("#vicActManage");
    $.ajaxSetup({
        dataType: "json",
        cache:false,
        error:function(){
            showMessage("获取数据失败","error");
        }
    });
    $(document).on('click', function (e) {
        if ( !$(e.target).hasClass('filter-btn')
            && !$(e.target).closest('.filter-panel').length ) {
            $("#filter-btn").removeClass("opened");
            $(".filter-panel").css("display","none");
        }
        if (e.target !== $('.more-icon-box.selected')[0] && !$(e.target).hasClass('operate more-icon') ) {
            if($('.more-icon-box.selected').length){
                $('.more-icon-box.selected').removeClass("selected");
            }
        }
    });

    //设置blockui全局样式
    $.blockUI.defaults.overlayCSS.backgroundColor = '#fff';
    $.blockUI.defaults.overlayCSS.opacity = '0.8';
    $.blockUI.defaults.css.border = 'none';
    $.blockUI.defaults.css.fontSize = '16px';

    //渠道：微信公众号部分
    var wx1_map = {
        "q1001":"wxgzh_popup",
        "q1002":"wxgzh_banner",
        "q1003":"wxgzh_guess",
        "q1004":"wxgzh_hot"
    };

    var wx2_map = {
        "263":"pic263",
        "310":"pic310",
        "311":"pic311"
    };
    $("#wxgzh_popup").show().siblings().hide();

    $("#wxgzh_config_types").bind("change", function(){
        var divId = wx1_map[this.value];
        // console.log(divId);
        $("#"+divId).show().siblings().hide();
        $("#wxgzh_config_template input").each(function () {
            $(this).removeAttr("sub");
        })
        $("#"+divId +" input").each(function () {
            $(this).attr("sub","1");
        })
    });

    $("#pic263").show().siblings().hide();

    $("#wx_pic").bind("change", function(){
        var divId = wx2_map[this.value];
        // console.log(divId);

        $("#"+divId).find("input").attr("data-ruleid","R005");
        $("#"+divId).siblings().find("input").removeAttr("data-ruleid");

        $("#"+divId).show().siblings().hide();
    });

});


function writeCache(data, page) {
    pageDataList[page] = {};
    pageDataList[page].isSaved = true;
    var temp = pageDataList[page]["content"] = [];
    for (var i = 0, len = data.length; i < len; ++i) {
        temp.push({
            CITY_NAME: data[i].CITY_NAME,
            CHANNEL_NAME: data[i].CHANNEL_NAME,
            TASK_NUM: data[i].TASK_NUM2,
            ACT_NUM: data[i].ACT_NUM,
            OBJ_NUM: data[i].OBJ_NUM,
            TOUCH_NUM: data[i].TOUCH_NUM,
            RESPONES_NUM: data[i].RESPONSE_NUM,
            VIC_NUM: data[i].VIC_NUM
        });
    }

}

$(document).ready(function() {
    //重置详情模态框slimscroll的高度
    var scrollH = $(window).height();

    $(".modal-wrap-new .modal-box-content").slimScroll({
        height:scrollH-200,
        color: "#a2a8af",
        alwaysVisible : true,
        size: "5px",
    });
    $(".detailTab .modal-box-content").slimScroll({
        height:scrollH-300,
        color: "#a2a8af",
        alwaysVisible : true,
        size: "5px",
    });
    $(".approveTab .modal-box-content").slimScroll({
        height:scrollH-300,
        color: "#a2a8af",
        alwaysVisible : true,
        size: "5px",
    })

    //ie8模拟placeholder
    $("#search").val("持活动名称、创建人、营销内容搜索").css("color","#CCD0D9");
    $("#search").focus(function(){
        if($("#search").val() == "持活动名称、创建人、营销内容搜索"){
            $(this).val("");
        }
        $(this).css("color","black");
    });
    $("#search").blur(function(){
        if($(this).val() == ""){
            $(this).val("持活动名称、创建人、营销内容搜索").css("color","#CCD0D9");
            return;
        }
    });

    $(".main a").click(function (e) {
        e.preventDefault();
    })
    //目标用户导入,周期更新单选框切换事件
    /*	$(".j-update-cycle>p").unbind("click");
	$(".j-update-cycle>p").click(function () {
		var $this = $(this);
		$this.addClass("selected").siblings(".selected").removeClass("selected").find("input").removeAttr("checked");
		$this.find("input").attr("checked","checked");
	});*/
    $(".modal-wrap-detail .modal-box-cancel").click(function(){
        htmlEvents.clearDetails('#detailTab_d');   //清空详情
        htmlEvents.clearApprove('#approveTab_d'); //清空审批结果
        htmlEvents.clearAttachment("#attachment_d");//清空附件列表
        $("[data-id='attachment_d']").parent().hide();
    })
    $(".modal-wrap-approve .modal-box-cancel").click(function(){
        htmlEvents.clearDetails('#detailTab');   //清空详情
    })
    $(".j-cancel-view").click(function(){
        $(".modal-wrap-showImg").hide();
        $(".modal-wrap-showImg img").removeAttr("src");
        $(".modal-wrap-showImg .j-img-name").text("");
    })
    //初始化营销活动列表
    var param;
    var init = new Init();
    window.init = init;
    init.getDropLists();

    //初始化模态框里的活动详情点击事件
    var detailEvents = new initDetailModal();
    detailEvents.init();
    $("[data-id='detailTab_d']").parent().click(detailEvents.getActDetails.bind(this));

    //初始化模态框审批事件
    var approveEvents = new initApproveModal();
    approveEvents.init();
    $(".modal-wrap-approve .modal-box-save").click(approveEvents.saveApprove);
    $("[data-id='detailTab']").parent().click(approveEvents.getActDetails.bind(this));

    //点击新建模型按钮，弹出模态框
    $(".panel-title .rt .create-new").click(function() {
        $(".modal-wrap-new").show();
        $(".modal-box-content").slimScroll({
            scrollTo: "0px"
        });
        //剔除用户，默认 剔除
        $(".checkbox .default-selected .col-mar").find("label").addClass("selected");
        $(".checkbox .default-selected .col-mar").find("span").addClass("selected");

        //请求获取新建模型模态框页面信息
        $.ajax({
            url: "./activities/getActBaseInit",
            type: 'get',
            cache: false,
            dataType: 'json',
            success: function(res) {
                //res包含活动编号，优先级，所属地市，所属优先级，渠道，统计口径下的业务类型，营销目的和关联任务
                //填充页面
                if (res.code !== 0) {
                    showMessage(res.msg,'error');
                }else{
                    var html1 = '<li class="selected"><input id="sceneFlag" type="radio" value="0" name="activity_type" checked="checked"/><label> <span ></span>非实时</label>  <i class="icon-left-arrow"></i></li>';
                    var html2 = "",
                        html3 = "",
                        html4 = "";
                    var bussTypes = res.data.businessTypeList;
                    var marktingAims = res.data.marketingPurposeList;
                    var actTypes = res.data.sceneList;
                    var channelInfo = res.data.channelInfoList;
                    var customerInfo = res.data.customerList;
                    //填写活动ID
                    $("#activityId").text(res.data.actBaseInfo.activityId);
                    //填充场景事件活动类型
                    for (var i = 0; i < actTypes.length; i++) {
                        html1 += '<li>' +
                            '<input type="radio" name="activity_type" value="' + actTypes[i].sceneId + '"/>' +
                            '<label>' +
                            '<span></span>' +
                            actTypes[i].sceneName +
                            '</label>' +
                            '<i class="icon-left-arrow hide"></i>'+
                            '</li>';
                    }
                    $("#actTypes").html(html1);


                    var marktingAimsList = [{"marketingPurposeId":"3","marketingPurposeName":"价值提升类","effective":"1"},{"marketingPurposeId":"10","marketingPurposeName":"用户保有类","effective":"1"},{"marketingPurposeId":"14","marketingPurposeName":"其他","effective":"1"}];
                    var bussTypesList1_1 = [{"businessTypeId":"3","businessTypeName":"流量类","effective":"1"},{"businessTypeId":"14","businessTypeName":"其他","effective":"1"}];
                    var bussTypesList1_2 = [{"businessTypeId":"10","businessTypeName":"流量预警","effective":"1"},{"businessTypeId":"11","businessTypeName":"不限量拓展","effective":"1"},{"businessTypeId":"12","businessTypeName":"圈子扩群","effective":"1"},{"businessTypeId":"14","businessTypeName":"其他","effective":"1"}];

                    var bussTypesList2_1 = [{"businessTypeId":"1","businessTypeName":"节日事件类","effective":"1"},{"businessTypeId":"2","businessTypeName":"会员权益类","effective":"1"},{"businessTypeId":"3","businessTypeName":"分时场景类","effective":"1"},{"businessTypeId":"13","businessTypeName":"提速解限类","effective":"1"},{"businessTypeId":"14","businessTypeName":"其他","effective":"1"}];
                    var bussTypesList2_2 = [{"businessTypeId":"4","businessTypeName":"红色预警","effective":"1"},{"businessTypeId":"5","businessTypeName":"橙色预警","effective":"1"},{"businessTypeId":"6","businessTypeName":"蓝色预警","effective":"1"},{"businessTypeId":"14","businessTypeName":"其他","effective":"1"}];
                    var bussTypesList2_3 = [{"businessTypeId":"7","businessTypeName":"价值类","effective":"1"},{"businessTypeId":"8","businessTypeName":"风险类","effective":"1"},{"businessTypeId":"9","businessTypeName":"提升类","effective":"1"},{"businessTypeId":"14","businessTypeName":"其他","effective":"1"}];
                    var bussTypesList2_4 = [{"businessTypeId":"10","businessTypeName":"宽带圈子","effective":"1"},{"businessTypeId":"11","businessTypeName":"电视圈子","effective":"1"},{"businessTypeId":"12","businessTypeName":"手机圈子","effective":"1"},{"businessTypeId":"14","businessTypeName":"其他","effective":"1"}];

                    //填充统计口径-营销目的
                    for (var i = 0; i < marktingAimsList.length; i++) {
                        html3 += '<option value="' + marktingAimsList[i].marketingPurposeId + '">' + marktingAimsList[i].marketingPurposeName + '</option>';
                    }
                    $("#marketingPurpose").html(html3);

                    //填充统计口径-业务类型
                    for (var i = 0; i < bussTypesList1_1.length; i++) {
                        html2 += '<option value="' + bussTypesList1_1[i].businessTypeId + '">' + bussTypesList1_1[i].businessTypeName + '</option>';
                    }
                    $("#bussinessType").html(html2);

                    //填充统计口径-业务类型2
                    var html55;
                    for (var i = 0; i < bussTypesList2_1.length; i++) {
                        html55 += '<option value="' + bussTypesList2_1[i].businessTypeId + '">' + bussTypesList2_1[i].businessTypeName + '</option>';
                    }
                    $("#bussinessSmallType").html(html55);



                    // 绑定联动事件
                    $("#marketingPurpose").change(function () {
                        var marketingPurposeId =  $("#marketingPurpose").val();
                        if (marketingPurposeId == 3) {
                            //填充统计口径-业务类型
                            var html;
                            for (var i = 0; i < bussTypesList1_1.length; i++) {
                                html += '<option value="' + bussTypesList1_1[i].businessTypeId + '">' + bussTypesList1_1[i].businessTypeName + '</option>';
                            }
                            $("#bussinessType").html(html);

                            //填充统计口径-业务类型2
                            var html55;
                            for (var i = 0; i < bussTypesList2_1.length; i++) {
                                html55 += '<option value="' + bussTypesList2_1[i].businessTypeId + '">' + bussTypesList2_1[i].businessTypeName + '</option>';
                            }
                            $("#bussinessSmallType").html(html55);
                        } else if(marketingPurposeId==10) {
                            //填充统计口径-业务类型
                            var html;
                            for (var i = 0; i < bussTypesList1_2.length; i++) {
                                html += '<option value="' + bussTypesList1_2[i].businessTypeId + '">' + bussTypesList1_2[i].businessTypeName + '</option>';
                            }
                            $("#bussinessType").html(html);

                            //填充统计口径-业务类型2
                            var html55;
                            for (var i = 0; i < bussTypesList2_2.length; i++) {
                                html55 += '<option value="' + bussTypesList2_2[i].businessTypeId + '">' + bussTypesList2_2[i].businessTypeName + '</option>';
                            }
                            $("#bussinessSmallType").html(html55);
                        } else if(marketingPurposeId==14) {
                            //填充统计口径-业务类型
                            var html;
                            html += '<option value="' + bussTypesList1_2[3].businessTypeId + '">' + bussTypesList1_2[3].businessTypeName + '</option>';
                            $("#bussinessType").html(html);

                            //填充统计口径-业务类型2
                            var html55;
                            html55 += '<option value="' + bussTypesList2_2[3].businessTypeId + '">' + bussTypesList2_2[3].businessTypeName + '</option>';
                            $("#bussinessSmallType").html(html55);
                        }
                    });

                    // 绑定联动事件
                    $("#bussinessType").change(function () {
                        var bussinessTypeId =  $("#bussinessType").val();
                        if (bussinessTypeId== 10) {
                            //填充统计口径-业务类型2
                            var html55;
                            for (var i = 0; i < bussTypesList2_2.length; i++) {
                                html55 += '<option value="' + bussTypesList2_2[i].businessTypeId + '">' + bussTypesList2_2[i].businessTypeName + '</option>';
                            }
                            $("#bussinessSmallType").html(html55);
                        }
                        if (bussinessTypeId== 11) {
                            //填充统计口径-业务类型2
                            var html55;
                            for (var i = 0; i < bussTypesList2_3.length; i++) {
                                html55 += '<option value="' + bussTypesList2_3[i].businessTypeId + '">' + bussTypesList2_3[i].businessTypeName + '</option>';
                            }
                            $("#bussinessSmallType").html(html55);
                        }
                        if (bussinessTypeId== 12) {
                            //填充统计口径-业务类型2
                            var html55;
                            for (var i = 0; i < bussTypesList2_4.length; i++) {
                                html55 += '<option value="' + bussTypesList2_4[i].businessTypeId + '">' + bussTypesList2_4[i].businessTypeName + '</option>';
                            }
                            $("#bussinessSmallType").html(html55);
                        }
                        if (bussinessTypeId== 14) {
                            //填充统计口径-业务类型2
                            var html55;
                            html55 += '<option value="' + bussTypesList2_2[3].businessTypeId + '">' + bussTypesList2_2[3].businessTypeName + '</option>';
                            $("#bussinessSmallType").html(html55);
                        }

                    });






                    // 填充渠道列表
                    for (var i = 0; i < channelInfo.length; ++i) {
                        if(channelInfo[i].channelId == "d03" || channelInfo[i].channelId == "d01"){
                            html4 += '<div class="col-1 conflict">' +
                                '<input type="checkbox" name="' +
                                channelInfo[i].channelId +
                                '" id="' + channelInfo[i].channelId + '" />' +
                                '<label for="' + channelInfo[i].channelId + '">' +
                                '<span></span>' +
                                channelInfo[i].channelName +
                                '</label>' +
                                '</div>';
                        }else{
                            html4 += '<div class="col-1">' +
                                '<input type="checkbox" name="' +
                                channelInfo[i].channelId +
                                '" id="' + channelInfo[i].channelId + '" />' +
                                '<label for="' + channelInfo[i].channelId + '">' +
                                '<span></span>' +
                                channelInfo[i].channelName +
                                '</label>' +
                                '</div>';
                        }

                        //填充审批人信息
                        var chan = channelInfo[i];
                        var approverList = chan.approverInfoList;
                        var htmlNone = "<option value=''>无</option>";
                        var approverGroup = "<option value=''>审批组</option>";
                        var htmlChannel = "";
                        var htmlContent = "";
                        var $contentApprover = $("#channel-tab ")
                            .find("li[data-channelId='" + chan.channelId + "']")
                            .find('select[data-use="contentApproverId"]');
                        var $channelApprover = $("#channel-tab ")
                            .find("li[data-channelId='" + chan.channelId + "']")
                            .find('select[data-use="channelApproverId"]');
                        if( approverList.length ){
                            $.each(approverList,function(j,approver){
                                var approverLevel = approver.approverLevel;
                                if( approverLevel == 1 ){  //内容审批
                                    if( approver.approverId ){
                                        htmlContent += '<option value="' + approver.approverId + '">' + approver.approverName + '</option>';
                                        if( !$contentApprover.data("approvalGroup") && !$contentApprover.data("approvalRole") ){
                                            $contentApprover.data("approvalGroup",approver.approvalGroup);
                                            $contentApprover.data("approvalRole",approver.approvalRole);
                                        }
                                    }
                                }
                                if(  approverLevel == 2 ){ //渠道审批
                                    if( approver.approverId ){
                                        htmlChannel += '<option value="' + approver.approverId + '">' + approver.approverName + '</option>';
                                        if( !$channelApprover.data("approvalGroup") && !$channelApprover.data("approvalRole") ){
                                            $channelApprover.data("approvalGroup",approver.approvalGroup);
                                            $channelApprover.data("approvalRole",approver.approvalRole);
                                        }
                                    }
                                }
                            });
                            if( htmlContent ){
                                var charNum = htmlContent.split("value").length-1;
                                if( (chan.channelId == "d05" || chan.channelId == "d01" || chan.channelId == "f02" ) && charNum > 1 ){
                                    htmlContent = approverGroup + htmlContent;
                                }
                            }else{
                                htmlContent = htmlNone;
                            }
                            if( htmlChannel ){
                                var _charNum = htmlChannel.split("value").length-1;
                                if( (chan.channelId == "d05" || chan.channelId == "d01" || chan.channelId == "f02" ) &&  _charNum > 1) {
                                    htmlChannel = approverGroup + htmlChannel;
                                }
                            }else{
                                htmlChannel = htmlNone;
                            }
                            $contentApprover.html(htmlContent);
                            $channelApprover.html(htmlChannel);
                        }else{
                            $contentApprover.html(htmlNone);
                            $channelApprover.html(htmlNone);
                        }




                        /*// 活动审批人
						if (channelInfo[i].needContentApproval) {
							var approverList = channelInfo[i].approverInfoList; // 内容审批
							var html1 = '';
							for (var j = 0; j < approverList.length; ++j) {
								if (approverList[j].approverLevel == 1) {
									html1 += '<option value="' + approverList[j].approverId + '">' + approverList[j].approverName + '</option>';
								}
							}
							if (html1 == "") {
								html1 += '<option value="sys">无</option>';
							}
							$("#channel-tab ")
								.find("li[data-channelId='" + channelInfo[i].channelId + "']")
								.find('select[data-use="contentApproverId"]').html(html1);

						}else {
							var approverList = channelInfo[i].approverInfoList; // 内容审批
							var html1 = '<option value="sys">无</option>';
							$("#channel-tab ")
								.find("li[data-channelId='" + channelInfo[i].channelId + "']")
								.find('select[data-use="contentApproverId"]').html(html1);
						}
						// 二级审批
						if (channelInfo[i].needChannelApproval) {
							var approverList = channelInfo[i].approverInfoList; // 渠道审批
							var html1 = '';
							for (var j = 0; j < approverList.length; ++j) {
								if (approverList[j].approverLevel == 2) {
									html1 += '<option value="' + approverList[j].approverId + '">' + approverList[j].approverName + '</option>';
								}
							}
							if (html1 == "") {
								html1 += '<option value="sys">无</option>';
							}
							$("#channel-tab ")
								.find("li[data-channelId='" + channelInfo[i].channelId + "']")
								.find('select[data-use="channelApproverId"]').html(html1);

						}else {
							var approverList = channelInfo[i].approverInfoList; // 渠道审批
							var html1 = '<option value="sys">无</option>';
							$("#channel-tab ")
								.find("li[data-channelId='" + channelInfo[i].channelId + "']")
								.find('select[data-use="channelApproverId"]').html(html1);
						}*/
                    }
                    $("#channelListBox").html(html4);
                    sceneFlagChannel = html4;

                    //设置用户群来源是否显示
                    for(var i=0;i < customerInfo.length; i++){
                        var id = customerInfo[i].CUSTOMER_ID;
                        $("#targetUser").find("ul").find("li[data-popup='"+id+"']").css("display","block");
                    }

                    // 	.on("click", " label", channelTemp1);
                }
            },
            error: function(res) {
                showMessage("获取信息失败","error")
            }
        }); // end ajax
    }); // end click handler

    //下载数据
    $("#download").on("click",function(){
        var params = getActivitiesListInfo();
        window.location.href = './activities/getActivityListDownload?searchVal='+params.searchVal+
            '&channelId='+params.channelId+
            '&startTime='+params.startTime+
            '&endTime='+params.endTime+
            '&showState='+params.showState+
            '&cityId='+params.cityId;
    });

    //统计口径-选择业务，点击展开全部按钮，展开所有已选标签
    $(".expand-all").click(function() {
        var tab = $(this).attr("data-bindtab");
        if ($(this).text() == "展开全部") {

            $("#" + tab).find(".data-list").removeClass("h-110");
            $(this).html("收起");
        } else {
            $("#" + tab).find(".data-list").addClass("h-110");
            $(this).html("展开全部");
        }
    })

    //模型详情
    $("#excChannelShow a").click(function(e) {
        e.preventDefault();
        $(this).addClass("selected").siblings().removeClass("selected");
        $($(this).attr("href")).addClass("selected").siblings().removeClass("selected");
    });

    // 搜索
    $("#search").keypress(function(e) {
        if (e.which == 13) {
            init.getPageData();
        }
    }).next().click(function(e) {
        init.getPageData();
    })

    // 测试号码输入检测
    // $("#phoneNum").keydown(function(e) {
    // 		if (e.keyCode !== 8 && e.keyCode !== 188 && (e.keyCode < 48 || (e.keyCode > 57 && e.keyCode < 96) || e.keyCode > 105)) {
    // 			e.preventDefault();
    // 			return;
    // 		}
    $(".j-phoneNumber").bind({
        "keyup":generalExtrFuncs.limitPhoneNum,
        "change": generalExtrFuncs.checkPhoneNum
    })
    // 取消后清空
    $(".modal-box-cancel").click(function(e) {
        $(".j-input-clear").val("");
        $(".j-checkbox-clear").find(".selected").removeClass("selected");
        $(".j-checkbox-clear").find("input").removeAttr("checked");
        $("#selectedUser").tagsinput('removeAll');
        $("#channel-tab li.selected").removeClass("selected");
        $("#item-tab").html('');
        $('#removeUpload').hasClass('selected') ? $('#removeUpload').click():'';
        $("#channel-tab textarea").removeClass("over-words");
        $(".Max_msg").remove();
        // $("#excChannel .item-tab").html("");
        $("#excChannel .channel-tab").find("textarea, input").val("");
        $("#chosedBussiness").val("");
        $("#excChannel .checkbox label").removeClass("selected").find("span").removeClass("selected");
        $("#chosedBussiness").tagsinput('removeAll');
        $(".img-preview-box img").removeAttr("src");
        //清空图片上传域的值
        $(".upload-btn").text("点击这里上传图片");
        $(".upload-file-btn").text("点击这里上传文件");
        var files = $(".modal-wrap-new [name='file']");
        $.each(files,function(i,item){
            var fileInput = $(item)[0];
            fileInput.outerHTML = fileInput.outerHTML;
        });
        //实时场景 ???场景内容待清空
        $(".act-type-sub-panel li[data-typeid='0']").addClass("selectd").siblings(".selected").removeClass("selected");
        //智能地图，默认选中实时
        $(".intelligent-map").find("li").removeClass("selected").eq(0).addClass("selected");
        $(".intelligent-map").find("input").removeAttr("checked").removeAttr("data-ruleid").eq(0).attr("checked","checked").attr("data-ruleid","SR012");
        $(".intelligent-map-tab").find("div").removeClass("selected").eq(0).addClass("selected");
        $(".j-number").val("");
        globalAttrs.mapSelectedData = null;
        $(".map-table-box table").html("");
        $(".j-intelliMap-checkbox").find("input").removeAttr("checked");
        $(".j-intelliMap-checkbox").find("label").removeClass("selected");
        $(".j-intelliMap-checkbox").find("label span").removeClass("selected");
        $(".j-usergroup-filter ").addClass("hide");
        $(".j-usergroup-filter .radio-box label span").removeClass("selected").eq(0).addClass("selected");
        $(".j-usergroup-filter .radio-box input").removeAttr("checked").eq(0).attr("checked","checked");
        //pcc下拉列表重置,内容框清空
        $("#policyType").val(0);
        $("#pccList").hide();
        $("#pccList table tbody").html("");
        $("#policyDetail").hide().find("p").text("");
        $("#pccPager").html("");
        //上网行为和实时位置
        $(".u-clear").text("0");
        $("#chosedApp,#chosedSite").tagsinput("removeAll");
        //选择用户群，重置周期判断各个参数，默认选中日周期
        /*if(!$(".j-update-cycle").hasClass("hide")){
			$(".j-update-cycle").addClass("hide");
		}
		$(".j-update-cycle>p").eq(1).addClass("selected").siblings(".selected").removeClass("selected").find("input").removeAttr("checked");
		$(".j-update-cycle>p").eq(1).find("input").attr("checked","checked");
		*/
        htmlEvents.resetParams();
        //推荐业务,默认展示资费
        $("#recommand table").html("");
        $("#recommand .tab-nav li:first-child").trigger("click");
        //form表单清空
        $("form").each(function(i){
            $(this)[0].reset();
        })
    });
    $("#ensure").click(function (e) {
        e.preventDefault();
        var obj = getActivitiesListInfo();
        obj.currentPage = 1;
        init.getPageData(obj);
        $("#cancel").click();
    });
    //实时场景
    $(".j-number").keyup(function (e) {
        if( ! /^\d+\.{0,1}[0-9]*$/.test($(this).val())){
            $(this).val("")
        }
    }).focus(function(){
        if($(this).parents("li[data-typeid]").find(".over-max-msg").length){
            $(this).parents("li[data-typeid]").find(".over-max-msg").remove();
        }
    }).blur(function(){
        var val = $(this).val();
        /*var $parentss = $(this).parents("[data-typeid]");
		if($parentss.attr("data-typeid") == "10"){
			if( val > 100 ){
				$(this).val("");
			}
		}*/
        if( val.indexOf(".") > 0 ){
            var numArr = val.split(".");
            if(!numArr[1]){
                $(this).val(numArr[0]);
            }else{
                if( numArr[1].length > 2 ){
                    val = parseFloat(val);
                    $(this).val(val.toFixed(2));
                }
            }
        }
        var $parent = $(this).parents("li[data-typeid]");
        var min = $parent.find("input.j-min-value").val() || -1;
        var max = $parent.find("input.j-max-value").val() || -1;
        min = parseFloat(min);
        max = parseFloat(max);
        if( min != -1 && max != -1 && min >= max){
            $parent.find("p.hint-msg").before("<p class='over-max-msg'>最大值小于或等于最小值，请调整</p>")
        }
    });
    /*智能地图*/
    /*	$(".j-param-min").focus(function () {
		$(".j-map-box").removeClass("hide");
	}).blur(function () {
		var val = $(this).val();
		if( !val ){
			$(".j-map-box").addClass("hide");
		}
	});*/
    //URL校验
    $(".j-validate-url").focus(function (e) {
        if($(this).next("p.over-max-msg").length){
            $(this).next("p.over-max-msg").remove();
        }
    }).blur(function (e) {
        var val = $(this).val();
        if(val){
            var result = htmlEvents.IsURL(val);
            if( !result ){
                $(this).after("<p class='over-max-msg'>URL链接格式不正确，请以http/https开头，填写正确格式的URL链接</p>")
            }
        }
    });
    //pcc场景事件-通过策略类型获取列表,前台分页，前台查询
    var showPCCList = (function(){
        var funcs = {};
        var initCheckbox = function(pageData){  //只能选择一个策略；展示详细描述
            $("#pccList .checkbox label span").click(function(e){
                htmlEvents.tableSinglleCheckbox.bind(this)();
                var data = $("#pccList table").data("pageData");
                if( $(this).hasClass("selected")){
                    var policyCode = $(this).closest("tr").find("td[data-code]").attr("data-code");
                    $(".policy-detail-box").show();
                    $.each(data,function(i,item){
                        if( item.PolicyCode == policyCode ){
                            $("#policyDetail p").text(item.Description);
                            return false;
                        }
                    });
                }else{
                    $(".policy-detail-box").hide();
                    $("#policyDetail p").text("");
                }
            });
        }
        var frontSearch = function(dataArr,str){
            var newDataArr = [];
            $.each(dataArr,function(i,item){
                if(item.PolicyName.search(str) != -1) {
                    newDataArr.push(item);
                }
            });
            //$("#policyType option:selected").data("tempPolicyData",newDataArr);
            return newDataArr;
        }
        funcs.groupPolicyList = function (data) {
            var dataArr = [];
            for( var i=0;i<data.length;i += 5){
                dataArr.push(data.slice(i,i+5));
            }
            return dataArr;
        }
        funcs.fillTable = function(pageData){
            var html = "";
            $("#policyDetail").hide();
            $("#policyDetail p").text("");
            if( !pageData || !pageData.length ){
                html += "<tr><td>&nbsp;</td><td colspan='2'>暂无数据</td></tr>"
            }else{
                for (var i = 0; i < pageData.length; i++) {
                    html += "<tr class='checkbox'>";
                    html += "<td style='width:30px '>";
                    html += "<label>";
                    html += "<input type='checkbox' data-ruleid='SR010' data-tagname='label_area' name='pcc'>";
                    html += "<span></span>";
                    html += "</label>";
                    html += "</td>";
                    html += "<td style='width: 25%'>" + pageData[i].Area.CityArea + "</td>";
                    html += "<td data-code='" + pageData[i].PolicyCode + "'>" + pageData[i].PolicyName + "</td>";
                    html += "</tr>";
                }
            }
            $("#pccList table").data("pageData",pageData);  //用于展示策略描述
            $("#pccList table tbody").html(html);
            initCheckbox(pageData);
        };
        funcs.pagination=function(dataArr,totals) {
            var self = this;
            var pagerBox = document.getElementById('pccPager');
            $(pagerBox).html("");
            var pager = new Pager({
                index: 1,
                total: dataArr.length,
                parent: pagerBox,
                onchange: doChangePage
            });
            $(pagerBox).find(".cur").before("<span>第</span>");
            $(pagerBox).find(".total-page").after("<span>页,共" + (totals || "0") + "条</span>");

            function doChangePage(obj) {
                var currentPage = obj.index;
                self.fillTable(dataArr[currentPage - 1]);
            }
        }
        funcs.frontSearchHandle = function(){
            var searchStr = $.trim($(".j-policy-search").val());
            var policyData = $("#policyType option:selected").data("policyData");
            var tempPolicyData = frontSearch(policyData,searchStr);
            var groupedTempPolicyData = funcs.groupPolicyList(tempPolicyData);
            funcs.fillTable(groupedTempPolicyData[0]);
            funcs.pagination(groupedTempPolicyData,tempPolicyData.length);
        }
        return funcs;
    })();
    $("#policyType").change(function(e){
        e.preventDefault();
        var name,code,searchVal;
        var type = $("#policyType").val();
        if(type == 0){
            $("#pccList").hide();
            $("#policyDetail").hide();
            return;
        }
        $("#pccList").show();
        $("#pccList .cover-modal").show();
        $("#pccList table tbody").html("");
        $(".j-policy-search").val("");
        $("#pccPager").html("");
        $("#policyDetail").hide();
        $("#policyDetail p").text("");
        $.ajax({
            url: "./pcc/getPccSceneList?PolicyType="+type,
            type:"get",
            dataType:"json",
            cache:false,
            success:function(res){
                if(res.code != 0){
                    $("#pccList").hide();
                    showMessage(res.msg,"error");
                    return
                }
                var data = res.data.PolicyList;
                var totals = res.data.PolicyNumber;
                var html = "";
                var dataArr = showPCCList.groupPolicyList(data);
                showPCCList.fillTable(dataArr[0]);
                showPCCList.pagination(dataArr,totals);
                $("#policyType").find("option:selected").data("policyData",data);  //用于前台搜索
            },
            error:function(){
                showMessage("获取信息失败","error");
            },
            complete: function(){
                $("#pccList .cover-modal").hide();
            }
        })
    });
    $(".j-policy-search").next().click(showPCCList.frontSearchHandle);
    $(".j-policy-search").keypress(function (e) {
        if( e.keyCode == 13 ){
            showPCCList.frontSearchHandle();
        }
    });
    //驻点营销--优惠渠道，模板内容校验
    $("#dxMsg").blur(function(e){
        e.preventDefault();
        resetMaxmsg();
        var $this = $(this);
        var sceneId = $("#actTypes li.selected input").val();
        if(sceneId == 9){
            var val = $this.val();
            var isStandard = true;
            if( val ){
                var deft = ["{DATE}","{POS}","{ACTIVITY}"];
                $.each(deft,function(i,item){
                    if( val.indexOf(item) == -1 ){
                        isStandard = false;
                        return false;
                    }
                });
                if( !isStandard ){
                    htmlEvents.showHintMsg(this,'短信内容必须包含 "{DATE}" 、 "{POS}" 、 "{ACTIVITY}"字样，请按照模板规范调整 ')
                }
            }
        }
    })
});

//营销活动列表
var Init = function() {
    var self = this;
    var removeData={};
    this.getDropLists = function() {
        $.ajax({
            url: "./base/getDropInfo",
            type:'get',
            dataType:'json',
            success:function(res){
                if(res.code == 0){
                    res = res.data;
                    var cityList = res.cityList;
                    var currentCity = res.currentCity;
                    var channelInfoList = res.channelInfoList;
                    var html1 = currentCity == 1?'<option value="0">全部地市</option>':"";
                    var html2='';

                    if(res.deptId !="all" && res.deptId != ""){
                        $("#deptSpan").css("display","block");
                        var depthtml = '<option value="'+res.deptId+'">'+res.deptName+'</option>'
                        $("#dept").html(depthtml);
                        html1 += '<option value="'+res.currentCity+'">'+res.currentCityName+'</option>'
                    }else{
                        for(var i=0;i<cityList.length;i++){
                            html1 += '<option value="'+cityList[i].cityId+'">'+cityList[i].cityName+'</option>'
                        }
                    }
                    $("#area").html(html1);

                    html2 += "<option value='@'>全部渠道</option>";
                    for(var i=0;i<channelInfoList.length;i++){
                        html2 += '<option value="'+channelInfoList[i].channelId+'">'+channelInfoList[i].channelName+'</option>'
                    }
                    $("#channel").html(html2);
                } else{
                    showMessage(res.msg,'error');
                }
            },
            error:function(){
                showMessage("获取信息失败","error")
            }
        })

        //this.getPageData();
        freshIndexPage();
    };
    this.getPageData = function(param) {
        $.blockUI({
            message:"数据获取中，请稍等...",
        });
        if (!param) {
            var param = getActivitiesListInfo();
        }
        $.ajax({
            url: "./activities/getActivityList",
            type: 'get',
            data: param,
            cache: false,
            dataType: 'json',
            success: function(res) {
                $.unblockUI();
                if (res.code === 0) {
                    var total = Math.ceil(res.data.allTotals / 10);
                    fillTable(res);
                    self.pagination(total);
                    $("#activities_num").text("("+res.data.allTotals+")");
                } else {
                    showMessage(res.msg,'error');
                }
            },
            error: function() {
                $.unblockUI();
                showMessage("获取数据失败","error")
            }

        })

    };
    this.pagination = function(totalPage) {
        var pagerBox = document.getElementById('page');
        $(pagerBox).html("");
        var pager = new Pager({
            index: 1,
            total: totalPage,
            parent: pagerBox,
            onchange: doChangePage
        });

        function doChangePage(obj) {
            $.blockUI({
                message:"数据获取中，请稍等...",
            });
            var param = getActivitiesListInfo();
            $.ajax({
                url: "./activities/getActivityList",
                type: 'get',
                data: param,
                cache: false,
                dataType: 'json',
                success: function(res) {
                    $.unblockUI();
                    if (res.code == 0) {
                        fillTable(res);
                        var total = 10;
                    }else {
                        showMessage(res.msg,'error');
                    }
                },
                error: function() {
                    $.unblockUI();
                    showMessage("获取数据失败","error")
                }
            });

        }
    };
    var fillTable = function(data) {
        data = data.data.activityList;
        if(data.length == 0){
            $("#table tbody").html("<td colspan='11' style'width: 100%;text-align:center;'>暂无数据</td>");
        }else{
            var html = '';
            for (var i = 0; i < data.length; i++) {
                html += '<tr>' + '<td><i></i></td>';
                html += '<td class="text-align-left" title="' + data[i].activityName + '">' + data[i].activityName + '</td>';
                html += '<td data-state="'+data[i].activityState+'"><span class="' + self.transState(data[i].activityState).color + '">' + self.transState(data[i].activityState).status + '</span></td>';
                html += '<td data-id="'+data[i].activityId+'">' + data[i].activityId + '</td>';
                if (data[i].cityName == "undefined" || data[i].cityName == null) {
                    html += '	<td>' + data[i].cityId + '</td>';
                } else {
                    html += '	<td>' + data[i].cityName + '</td>';
                }
                html += '<td title="' + data[i].startTime + '">' + data[i].startTime + '</td>';
                html += '<td title="' + data[i].endTime + '">' + data[i].endTime + '</td>';
                html += '<td title="' + data[i].createTime + '">' + data[i].createTime + '</td>';
                if (data[i].creatorName == "undefined" || data[i].creatorName == null) {
                    html += '<td>' + data[i].creatorId + '(ID号)</td>';
                } else {
                    html += '<td>' + data[i].creatorName + '</td>';
                }
                if (data[i].activityState == 2 || data[i].activityState == 3) {
                    html += '<td>生成失败</td>';
                } else if(data[i].activityState == 0){
                    html += '<td>正在生成中</td>';
                }else{
                    html += '<td><span class="green">' + data[i].finalAmount + '<b class="pop-btn"></b></span></td>';
                }

                //根据状态，判断展示哪些操作
                html += '<td class="overflow-show">';
                if (data[i].isCanApproval == 1) { //可进行审批操作
                    html += '<a href="#" title="审批" data-id="' + data[i].activityId + '" data-name="' + data[i].activityName + '" class="operate approve-icon"></a>';
                } else {
                    html += '<a href="#" title="审批" data-id="' + data[i].activityId + '" data-name="' + data[i].activityName + '" class="operate approve-icon visible-hidden"></a>';
                }
                //恢复/暂停按钮判断
                //如果活动状态为2,3,5,7（活动驳回），或状态为9（结束），则不展示按钮；stopped: 0，暂停；1，恢复
                if(data[i].isCanDelete == 1){
                    if(data[i].activityState == 9 || data[i].activityState == 5 || data[i].activityState == 2|| data[i].activityState == 3|| data[i].activityState == 7){  //不展示按钮
                        html += '<a href="#" class="operate visible-hidden" data-id="' + data[i].activityId + '" data-name="' + data[i].activityName + '"></a>';
                    }else{
                        if(data[i].stopped == 0){
                            html += '<a href="#" title="暂停" class="operate stop-icon " data-id="' + data[i].activityId + '" data-name="' + data[i].activityName + '"></a>';
                        }else if(data[i].stopped == 1){
                            html += '<a href="#" title="恢复" class="operate start-icon " data-id="' + data[i].activityId + '" data-name="' + data[i].activityName + '"></a>';
                        }
                    }
                }else{
                    html += '<a href="#" class="operate visible-hidden" data-id="' + data[i].activityId + '" data-name="' + data[i].activityName + '"></a>';
                }
                html += '<a href="#" title="详情" class="operate detail-modal-icon" data-id="' + data[i].activityId + '" data-name="' + data[i].activityName + '"></a>';
                html += '<a href="#" title="其他操作" class="operate more-icon"></a>';
                html += '<div class="more-icon-box">';
                if (data[i].activityState != 4 || data[i].activityState != 8 || data[i].activityState != 9) {
                    //功能未完成，暂时隐藏
                    html += '<a href="#" title="修改" class="operate edit-icon visible-hidden" data-id="' + data[i].activityId + '" data-name="' + data[i].activityName + '"></a>';
                } else {
                    html += '<a href="#" title="修改" class="operate edit-icon visible-hidden" data-id="' + data[i].activityId + '" data-name="' + data[i].activityName + '"></a>';
                }
                html += '<a href="#" title="复制" class="operate copy-icon visible-hidden" data-id="' + data[i].activityId + '" data-name="' + data[i].activityName + '"></a>';
                if (data[i].isCanDelete == 1) { //创建人和审批人都可进行删除操作
                    html += '<a href="#" title="删除" class="operate delete04-icon" data-id="' + data[i].activityId + '" data-name="' + data[i].activityName + '"></a>';
                }else{
                    html += '<a href="#" title="删除" class="operate delete04-icon visible-hidden" data-id="' + data[i].activityId + '" data-name="' + data[i].activityName + '"></a>';
                }
                html += '</div>';
                html += '</td></tr>';

                removeData[data[i].activityId] = data[i].customerRemoveInfo;
            }
            $("#table tbody").html(html);
        }
        initTableStyle();
        initOperateEvent();
        initTableHover();
        initPopEvent("#table",removeData);
    };
    this.transState = function(type) {
        switch (type) {
            case 0:
                return {
                    "status": "系统判定中",
                    "color": "light-purple"
                };
                break;
            case 1:
                return {
                    "status": "待审批",
                    "color": "gery-blue"
                };
                break;
            case 2:
            case 3:
                return {
                    "status": "系统判定失败",
                    "color": "watermelon-red"
                };
                break;
            case 4:
                return {
                    "status": "审批中",
                    "color": "blue"
                };
                break;
            case 5:
            case 7:
                return {
                    "status": "审批驳回",
                    "color": "red"
                };
                break;
            case 6:
                return {
                    "status": "审批通过待执行",
                    "color": "ice-blue"
                };
                break;
            case 8:
                return {
                    "status": "执行中",
                    "color": "green"
                };
                break;
            case 9:
                return {
                    "status": "执行结束",
                    "color": "grey"
                };
            default:
        }
    };
    var initTableStyle = function() {
        /*
		 *所有动态添加列添加背景色和图标
		 */
        var icons = ["activity_red.png", "activity_yellow.png", "activity_green.png", "activity_blue.png"];
        var imgPath = "./img/";
        var len = $("#table thead td[data-index]").length;
        for (var col = "col-", eachCol, i = 5; i < len + 1; i++) {
            eachCol = col + i;
            $("#table td[data-index='col-" + i + "']").find('span').addClass(color[(i - 5) % 4]);
        }
        //表格图标添加
        imgTds = $("#table td>i");
        for (var i = 0; i < imgTds.length; i++) {
            $(imgTds[i]).css("background", "url(" + imgPath + icons[i % 4] + ")");
        }

        var table_h = parseInt($(".inner-content").css("height")) - 20 - 39 - 40 - 20;
        $("#table td").css("height", (table_h / 11) + "px");
        $("#table tbody tr:odd").addClass("even");
    };

    var initOperateEvent = function() {
        /**
         * 营销活动列表操作事件
         */

        $(".detail-modal-icon").click(function(e) { //详情
            e.preventDefault();
            $(".modal-wrap-detail").show();
            var actid = $(this).attr("data-id");
            var actname = $(this).attr("data-name");
            var actState = $(this).closest("tr").find("td:nth-child(3)").text();
            var actStateId = $(this).closest("tr").find("td[data-state]").attr("data-state");
            $("#curActivityId").val(actid);
            $("#curActivityName").val(actname);
            $("#curActivityState").val(actState);
            $("#curActivityState").data("actStateId",parseInt(actStateId));
            $(".modal-wrap-detail a[href='#detailTab_d']").parent().trigger("click");
        });
        $(".delete04-icon").click(function(e) { //删除
            e.preventDefault();
            var actid = $(this).attr("data-id");
            var actname = $(this).attr("data-name");

            var ensureDelete = function(){
                $.ajax({
                    url: "./activities/deleteActivity",
                    type:'post',
                    data:{"activityId":actid},
                    dataType:'json',
                    cache: false,
                    success:function(res){
                        if(res.code == 0){
                            showMessage("操作成功","success",1500);
                            //刷新页面
                            freshIndexPage();
                        }else{
                            showMessage("失败，"+res.msg,"error");
                        }
                    },
                    error:function(){
                        showMessage("删除失败","error");
                    }
                })
            }
            showConfirm("确定要删除此条数据吗？",ensureDelete);
        });
        $(".approve-icon").click(function(e) { //审批
            e.preventDefault();
            $(".modal-wrap-approve").show();
            var actid = $(this).attr("data-id");
            var actname = $(this).attr("data-name");
            $("#curActivityId").val(actid);
            $("#curActivityName").val(actname);
            $(".modal-wrap-approve a[href='#approveTab']").parent().trigger("click");
        });
        $(".stop-icon").click(function(e) { //暂停
            e.preventDefault();
            var actid = $(this).attr("data-id");
            var actname = $(this).attr("data-name");

            var ensureStop = function(){
                $.blockUI({
                    message:"状态修改中，请稍等...",
                });
                $.ajax({
                    url: "./activities/pausedActivity",
                    type:'post',
                    data:{"activityId":actid},
                    dataType:'json',
                    cache: false,
                    success:function(res){
                        if(res.code == 0){
                            $.unblockUI();
                            showMessage("操作成功","success",1500);
                            //刷新页面
                            freshIndexPage();
                        }else{
                            $.unblockUI();
                            showMessage("失败，"+res.msg,"error");
                        }
                    },
                    error:function(){
                        $.unblockUI();
                        showMessage("操作失败","error");
                    }
                })
            }
            showConfirm("确定要暂停此活动吗？",ensureStop);
        });
        $(".start-icon").click(function(e) { //恢复
            e.preventDefault();
            var actid = $(this).attr("data-id");
            var actname = $(this).attr("data-name");

            var ensureStart = function(){
                $.blockUI({
                    message:"状态修改中，请稍等...",
                });
                $.ajax({
                    url: "./activities/recoveryActivity",
                    type:'post',
                    data:{"activityId":actid},
                    dataType:'json',
                    cache: false,
                    success:function(res){
                        if(res.code == 0){
                            $.unblockUI();
                            //刷新页面
                            showMessage("操作成功","success",1500);
                            freshIndexPage();
                        }else{
                            $.unblockUI();
                            showMessage("失败，"+res.msg,"error");
                        }
                    },
                    error:function(){
                        $.unblockUI();
                        showMessage("操作失败","error");
                    }
                })
            }
            showConfirm("确定要恢复此活动吗？",ensureStart);
        });
        $(".more-icon").click(function(e){
            e.preventDefault();
            if($(this).next().hasClass("selected")){
                $(this).next().removeClass("selected");
            }else{
                $(".more-icon-box").removeClass("selected");
                $(this).next().addClass("selected");
            }
        })
    }
    var initPopEvent = function(table,data){    //目标客户数-剔除用户详情
        var isChoseRemove = false;
        var $pop = $(".pop-box");
        $(table+" td span").mouseover(function(){
            var actId,list;
            var isPop = $(this).find("b");
            if(!isPop.length){
                return;
            }
            actId = $(this).parent().siblings("[data-id]").attr("data-id");

            for(var k in data){
                if(k == actId){
                    var list = data[k];
                    if(!list){
                        return;
                    }
                    var each = list;
                    for(var k in each){
                        if($(".pop-content span[data-id="+k+"]").length){
                            $(".pop-content span[data-id="+k+"]").text(each[k]);
                        }
                        if( $(".pop-content li[data-id="+k+"]").length){
                            if(each[k] == 1){
                                $(".pop-content li[data-id="+k+"]").show();
                                isChoseRemove = true;
                            }
                        }
                    }
                    if(!isChoseRemove){
                        $(".pop-content li[data-id='noRemove']").show();
                    }
                }
            }

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

            $pop.show();
        });
        $(table+" td span").mouseout(function(){
            $(this).css("border-radius","15px");
            $(".pop-content>li").hide();
            $pop.hide();
            isChoseRemove = false;
        })
    }
}

$(function() {
    $('.checkbox-icon').click(function() {
        $(this).toggleClass("checked");
        if ($(this).hasClass('checked')) {
            $(this).siblings("input").attr("checked", true);
        } else {
            $(this).siblings("input").attr("checked", false);
        }
    });
    $(".checkbox label").bind('click', temp1);
    //营销活动列表，点击全选按钮，表格选中全部行
    $("#checkAll").siblings("i.checkbox-icon").click(function() {
        if ($(this).hasClass('checked')) {
            $(this).siblings("input").attr("checked", true);
            $(".table td>p>.checkbox-icon").addClass("checked");
            $(".table td>p>input").attr("checked", true)
        } else {
            $(this).siblings("input").attr("checked", false);
            $(".table td>p>.checkbox-icon").removeClass("checked");
            $(".table td>p>input").attr("checked", false)
        }
    });

    //业务订购场景，业务代码和业务名称，搜索自动匹配
    $('#bussinessCode').autocomplete( './base/getRecommendProductInfo',{
        minChars: 0,
        multiple:true,
        multipleSeparator:",",
        queryParam:"queryStr",
        cacheLength:0,
        extraParams:{
            "prcType":1,
            "nowPageNum":1,
            "lengthRow":10
        },
        parse: function(res) {
            var rows = [],
                data = res.data;
            if(res.data == null || data == null){
                return rows;
            }
            for(var i=0; i<data.businessList.length; i++){
                rows[rows.length] = {
                    data:data.businessList[i],       //下拉框显示数据格式
                    value:data.businessList[i].PRC_NAME,   //选定后实际数据格式
                    result:data.businessList[i].PRC_ID  //选定后输入框显示数据格式
                };
            }
            return rows;
        },
        formatItem: function(row, i, n) {
            return row.PRC_NAME;
        }
    });
    $('#bussinessName').autocomplete( './base/getRecommendProductInfo',{
        minChars: 0,
        multiple:true,
        multipleSeparator:",",
        queryParam:"queryStr",
        cacheLength:0,
        extraParams:{
            "prcType":1,
            "nowPageNum":1,
            "lengthRow":10
        },
        parse: function(res) {
            var rows = [],
                data = res.data;
            if(res.data == null || data == null){
                return rows;
            }
            for(var i=0; i<data.businessList.length; i++){
                rows[rows.length] = {
                    data:data.businessList[i],       //下拉框显示数据格式
                    value:data.businessList[i].PRC_NAME,   //选定后实际数据格式
                    result:data.businessList[i].PRC_NAME  //选定后输入框显示数据格式
                };
            }
            return rows;
        },
        formatItem: function(row, i, n) {
            return row.PRC_NAME;
        }
    });
    $(".modal-box-content .j-slimscroll-tab").slimScroll({   //???????
        height:'120px',
        color: "#a2a8af",
        alwaysVisible : true,
        size: "5px",
        start:'top',
        wheelStep: 50,
        scrollTo: "0px"
    })

    /*start目标用户*/
    //自定义导入-标准模式
    var choseUser1 = new tagsOperate();
    choseUser1.initTagsIput("#selectedUser");
    choseUser1.fileTagsinputEvent("#customInputWrap", "#standardMode", "#selectedUser","file");
    //自定义导入-自定义模式
    var choseUser2 = new tagsOperate();
    choseUser2.initTagsIput("#selectedUser");
    choseUser2.fileTagsinputEvent("#customInputWrap", "#customMode", "#selectedUser","file2");
    //历史导入
    var choseUser3 = new tagsOperate();
    choseUser3.initTagsIput("#selectedUser");
    choseUser3.listTagsinput("#historyInputWrap", "#selectedUser");
    //数字内容
    var choseUser4 = new tagsOperate();
    choseUser4.initTagsIput("#selectedUser");
    choseUser4.listTagsinput("#figureContWrap", "#selectedUser");

    // 上网行为绑定事件
    var choseApp = new tagsOperate();
    choseApp.initTagsIput("#chosedApp");
    choseApp.listTagsinputEvent("#internet", "#chosedApp");

    //实时位置绑定事件
    var chosePos = new tagsOperate();
    chosePos.initTagsIput("#chosedSite");
    chosePos.listTagsinputEvent("#realTimeSite", "#chosedSite");

    //剔除用户，选中自定义导入，才可上传文件
    $("#removeUpload").unbind("click", temp1);
    $("#removeUpload").click(function(e) {
        /**
         *  ie8 Function.prototype.apply: 缺少 Array 或 arguments 对象
         * try{
		*	    var args =  Array.prototype.slice.apply(params ,[0]);
		*	}catch(err){
		*	    var args = Array.prototype.concat.apply([],params);
		*	}
         *
         * */
        temp1.apply(this, e);   //ie8下报错  ？？？待解决
        // temp1.bind(this)();   //换成bind方法
        if ($(this).hasClass("selected")) {
            $("input[data-id='removeUpload']").css("display", "block");
            $("#ensureUpload").css("display", "block");
        } else {
            $("input[data-id='removeUpload']").css("display", "none");
            $('#removeUpload').next().find("a.close").trigger("click");
            $("#removeUpload").removeData("customerGroupId");
            $("#ensureUpload").css("display", "none");
        }
    })
    //上传剔除自定义导入用户文件
    $("#ensureUpload").click(function() {
        if (!$("#fileUpLoad2 span.fileinput-filename").text().trim()) {
            showMessage("请选择上传文件","error")
            return;
        }
        //if ($("input[data-id='removeUpload']")[0].files.length) {
        /*	var fileInfo = $("input[data-id='removeUpload']")[0].files[0];
			var param = {};
			param.file = fileInfo;
			param.useType = $("input[data-id='removeUpload']").attr("name");*/
        $(".modal-wrap-upload").show();
        $.ajaxFileUpload({
            url: "./files/upload",
            fileElementId: "removeGroupFile",
            data: {
                useType: 3
            },
            secureuri: false,
            dataType:'json',
            type: "post",
            async: false,
            success:function(res){
                if (res.code == 0) {
                    $("#removeUpload").data("customerGroupId", res.data.customerGroupId);// 保存id
                    showMessage("上传文件手机号码总数："+
                        res.data.fileLineCount +"，其中有效数量："+res.data.amount, "success");
                }else if (res.code == 1) {
                    showMessage(res.msg,'error');
                } else {
                    showMessage("上传文件存在错误："+res.msg, "error", 3000);
                }
                //只保留上传文件名,其他文件信息全部清空
                $("#fileUpLoad2").removeData("bs.fileinput");  //解决bootstrap-fileinput文件上传成功后不可再上传的问题
                $("#removeGroupFile").removeData();
                $("#removeGroupFile")[0].outerHTML=$("#removeGroupFile")[0].outerHTML;
                $("#removeGroupFile").attr("name", "file");
                $(".modal-wrap-upload").hide();
            },
            error:function(err){
                showMessage("上传失败", "error", 3000);
                $(".close.fileinput-exists").click();
                $("#fileUpLoad2").removeData("bs.fileinput");
                $("#removeGroupFile").removeData();
                $("#fileUpLoad2")[0].outerHTML=$("#fileUpLoad2")[0].outerHTML;
                $("#removeGroupFile").attr("name", "file");
                $("#removeGroupFile").attr("title", "");
                $(".modal-wrap-upload").hide();
            }
        })
    })

    //实时场景事件切换
    $("#actTypes").on('click','li>label',function (event) {
        var $li = $(this).parent();
        var $sib = $(this).siblings("input");
        var sId = $sib.val();
        if ($li.hasClass("selected")){
            return;
        }
        $li.siblings(".selected").find("i.icon-left-arrow").addClass("hide");
        $li.siblings(".selected").removeClass("selected").find("input").attr("checked",false);
        $li.addClass("selected").find("input").attr("checked","checked");
        $li.find("i.icon-left-arrow").removeClass("hide");

        var bindTab = $li.find("input").val();
        $(".act-type-sub-panel>ul>li[data-typeid='"+bindTab+"']").addClass("selected").siblings(".selected").removeClass("selected");
        if($("#dxMsg").hasClass("j-no-standard")){   //清除"优惠提醒"渠道提示信息
            $("#dxMsg_div").find(".j-lack-param").remove();
            $("#dxMsg").removeClass("j-no-standard");
        }
        //默认“优惠提醒”渠道，短信下发内容，必须填写
        $(".j-validate-null").find("input").removeAttr("checked");
        $(".j-validate-null").find("label").removeClass("selected");
        $(".j-validate-null").find("span").removeClass("selected");

        if(sId == "0"){
            $("#item-tab").html("");
            $("#channel-tab").find("li.selected").removeClass("selected");
            $("#channelListBox").html(sceneFlagChannel);
            return
        }

        //实时事件，根据sceneId，获取可选渠道
        $.ajax({
            url: "./base/getChannelsBySceneId?sceneId="+sId,
            type:"get",
            dataType: "json",
            cache:false,
            success:function(res){
                if(res.code != 0){
                    showMessage(res.msg,"error");
                    return;
                }
                //展示可选渠道
                var channels = res.data.channelInfo;
                var html = "";
                $("#item-tab").html("");
                $("#channel-tab").find("li.selected").removeClass("selected");
                if(channels && channels.length){
                    $.each(channels,function (i,chan) {
                        if(chan.channelId == "d03" ||chan.channelId == "d01"){
                            html += '<div class="col-1 conflict">' +
                                '<input type="checkbox" name="' +
                                chan.channelId +
                                '" id="' +chan.channelId + '" />' +
                                '<label for="' +chan.channelId + '">' +
                                '<span></span>' +
                                chan.channelName +
                                '</label>' +
                                '</div>';
                        }else{
                            html += '<div class="col-1">' +
                                '<input type="checkbox" name="' +
                                chan.channelId +
                                '" id="' +chan.channelId + '" />' +
                                '<label for="' +chan.channelId + '">' +
                                '<span></span>' +
                                chan.channelName +
                                '</label>' +
                                '</div>';
                        }

                        //填充审批人信息
                        var approverList = chan.approverInfoList;
                        var htmlNone = "<option value=''>无</option>";
                        var approverGroup = "<option value=''>审批组</option>";
                        var htmlChannel = "";
                        var htmlContent = "";
                        var $contentApprover = $("#channel-tab ")
                            .find("li[data-channelId='" + chan.channelId + "']")
                            .find('select[data-use="contentApproverId"]');
                        var $channelApprover = $("#channel-tab ")
                            .find("li[data-channelId='" + chan.channelId + "']")
                            .find('select[data-use="channelApproverId"]');
                        if( approverList.length ){
                            $.each(approverList,function(j,approver){
                                var approverLevel = approver.approverLevel;
                                if( approverLevel == 1 ){  //内容审批
                                    if( approver.approverId ){
                                        htmlContent += '<option value="' + approver.approverId + '">' + approver.approverName + '</option>';
                                        if( !$contentApprover.data("approvalGroup") && !$contentApprover.data("approvalRole") ){
                                            $contentApprover.data("approvalGroup",approver.approvalGroup);
                                            $contentApprover.data("approvalRole",approver.approvalRole);
                                        }
                                    }
                                }
                                if(  approverLevel == 2 ){ //渠道审批
                                    if( approver.approverId ){
                                        htmlChannel += '<option value="' + approver.approverId + '">' + approver.approverName + '</option>';
                                        if( !$channelApprover.data("approvalGroup") && !$channelApprover.data("approvalRole") ){
                                            $channelApprover.data("approvalGroup",approver.approvalGroup);
                                            $channelApprover.data("approvalRole",approver.approvalRole);
                                        }
                                    }
                                }
                            });
                            if( htmlContent ){
                                var charNum = htmlContent.split("value").length-1;
                                if( (chan.channelId == "d05" || chan.channelId == "d01" || chan.channelId == "f02" ) && charNum > 1 ){
                                    htmlContent = approverGroup + htmlContent;
                                }
                            }else{
                                htmlContent = htmlNone;
                            }
                            if( htmlChannel ){
                                var _charNum = htmlChannel.split("value").length-1;
                                if( (chan.channelId == "d05" || chan.channelId == "d01" || chan.channelId == "f02" ) &&  _charNum > 1) {
                                    htmlChannel = approverGroup + htmlChannel;
                                }
                            }else{
                                htmlChannel = htmlNone;
                            }
                            $contentApprover.html(htmlContent);
                            $channelApprover.html(htmlChannel);
                        }else{
                            $contentApprover.html(htmlNone);
                            $channelApprover.html(htmlNone);
                        }
                        /*// 活动审批人
						if (chan.needContentApproval) {
							var approverList = chan.approverInfoList; // 内容审批
							var html1 = '';
							for (var j = 0; j < approverList.length; ++j) {
								if (approverList[j].approverLevel == 1) {
									html1 += '<option value="' + approverList[j].approverId + '">' + approverList[j].approverName + '</option>';
								}
							}
							if (html1 == "") {
								html1 += '<option value="sys">无</option>';
							}
							$("#channel-tab ")
								.find("li[data-channelId='" + chan.channelId + "']")
								.find('select[data-use="contentApproverId"]').html(html1);

						}else {
							var approverList = chan.approverInfoList; // 内容审批
							var html1 = '<option value="sys">无</option>';
							$("#channel-tab ")
								.find("li[data-channelId='" + chan.channelId + "']")
								.find('select[data-use="contentApproverId"]').html(html1);
						}
						// 渠道审批人
						if (chan.needChannelApproval) {
							var approverList = chan.approverInfoList; // 渠道审批
							var html1 = '';
							for (var j = 0; j < approverList.length; ++j) {
								if (approverList[j].approverLevel == 2) {
									html1 += '<option value="' + approverList[j].approverId + '">' + approverList[j].approverName + '</option>';
								}
							}
							if (html1 == "") {
								html1 += '<option value="sys">无</option>';
							}
							$("#channel-tab ")
								.find("li[data-channelId='" + chan.channelId + "']")
								.find('select[data-use="channelApproverId"]').html(html1);

						}else {
							var approverList = chan.approverInfoList; // 渠道审批
							var html1 = '<option value="sys">无</option>';
							$("#channel-tab ")
								.find("li[data-channelId='" + chan.channelId + "']")
								.find('select[data-use="channelApproverId"]').html(html1);
						}*/
                    });
                    $("#channelListBox").html(html);
                }else{
                    $("#channelListBox").html("<p>此实时场景下，无可选渠道</p>");
                }
            },
            error:function(){
                showMessage("获取可选渠道信息失败","error");
            }
        })

        //场景事件--业务订购Top15
        if($sib.val() == 3){
            if($("#top15BussOrder").attr("data-isCreated") == "created"){
                return
            }
            $.ajax({
                url: "./base/getTop15Product",
                type:"get",
                dataType :"json",
                cache:false,
                success:function (res) {
                    if(res.code != 0){
                        showMessage(res.msg,"error");
                        return
                    }
                    var html = "";
                    $.each(res.data.productList,function(i,item){
                        html += "<li>";
                        html += "<input type='checkbox' name='' value='"+item.PRCID+"'>";
                        html += "<label class='col-mar' title='"+item.PRCNAME+"'><span></span>"+item.PRCNAME+"</label>";
                        html += "</li>";
                    })
                    $("#top15BussOrder").html(html);
                    $("#top15BussOrder").attr("data-isCreated","created");
                    autoAddCode();
                },
                error:function () {
                    showMessage("获取数据失败","error");
                }
            })

            function autoAddCode() {
                $("#top15BussOrder label.col-mar").click(function () {
                    var codes,one,queryStr;
                    var $this = $(this);
                    temp1.bind(this)();

                    queryStr =  $.trim($("#bussinessCode").val());
                    if( queryStr.substring(queryStr.length-1) == "," ){
                        queryStr = queryStr.substring(0,queryStr.length-1);
                    }
                    codes = queryStr? queryStr.split(","):[];
                    one = $this.prev().val();
                    if($this.hasClass("selected")){
                        codes.push(one);
                    }else{
                        codes.removeByValue(one);
                    }
                    $("#bussinessCode").val(codes.join(","));
                })
            }

        }
        //场景事件--上网行为下拉列表
        if($sib.val() == 5){
            $.ajax({
                url: "./base/getAppType",
                type:"get",
                dataType: "json",
                cache:false,
                success:function(res){
                    if(res.code != 0){
                        showMessage(res.msg,"error");
                        return;
                    }
                    var appList = res.data.appTypeList,
                        $appSelect = $("#appSelect"),
                        appTypeId;
                    $appSelect.html("<option value=''>==请选择==</option>");
                    $.each(appList,function(i,app){
                        $appSelect.append("<option data-appname='"+app.appTypeName+"' value='"+app.appTypeId+"'>"+app.appTypeDesc+"</option>")
                    });
                    appTypeId = $appSelect.val();
                    getAppInfoList(appTypeId,"");
                },
                error:function(){
                    showMessage("获取待选APP分类信息失败","error");
                }
            })
        }
        //场景事件-实时位置下拉列表
        if($sib.val() == 4){
            $.ajax({
                url: "./base/getRealTimePositionType",
                type:"get",
                dataType: "json",
                cache:false,
                success:function(res){
                    if(res.code != 0){
                        showMessage(res.msg,"error");
                        return;
                    }
                    var $siteSelect = $("#siteSelect");
                    var posList = res.data;
                    $siteSelect.html("<option value=''>==请选择==</option>");
                    $.each(posList,function(i,pos){
                        $siteSelect.append("<option data-posname='"+pos.TAG_NAME+"' value='"+pos.TAG_CODE+"'>"+pos.TAG_NAME+"</option>")
                    });
                    var deftSite = $siteSelect.val();
                    getPosInfoList(deftSite,"");//默认展示会议中心下是实时位置
                },
                error:function(){
                    showMessage("获取待选位置分类信息失败","error");
                }
            })
        }
        //场景事件--驻点营销，模板内容，展示“优惠提醒”渠道提示内容
        if($sib.val() == 9){
            $("#dxMsg").val("");
            if( $(".j-msg-template").attr("isCreated") == "created" ){
                return;
            }
            $.ajax({
                url: "./base/getSceneSmsTemplate?sceneId=9",
                type:"get",
                dataType:"json",
                cache:false,
                success:function(res){
                    if(res.code != 0){
                        showMessage(res.msg,"error");
                        return
                    }
                    $(".j-msg-template").text(res.data);
                    $(".j-msg-template").attr("isCreated","created");
                },
                error:function(){
                    showMessage("获取模板数据失败","error");
                }
            })
        }
        if($sib.val() == 13){
            if( $(".j-high-speed-train").attr("isCreated") == "created" ){
                return;
            }
            $.ajax({
                url: "./base/getSceneSmsTemplate?sceneId=13",
                type:"get",
                dataType:"json",
                cache:false,
                success:function(res){
                    if(res.code != 0){
                        showMessage(res.msg,"error");
                        return
                    }
                    $(".j-high-speed-train").text(res.data);
                    $(".j-high-speed-train").attr("isCreated","created");
                },
                error:function(){
                    showMessage("获取数据失败","error");
                }
            })
        }

        if($sib.val() == 12){
            var html = "";
            $.ajax({
                url: "./base/getRearEndType",
                type:"get",
                dataType:"json",
                cache:false,
                success:function(res){
                    if(res.code != 0){
                        showMessage(res.msg,"error");
                        return
                    }
                    var dataList = res.data.rearEndType;
                    for(var i=0;i<dataList.length;i++){
                        html +="<input type='checkbox' data-ruleid='SR015' data-tagname='label_area' name='paymentChannel' value='"+dataList[i].REMIND+"'>";
                        html +="<label class='col-mar'><span></span>"+dataList[i].REMINDNAME+"</label>"
                    }
                    $("#bossflowRadio").html(html);
                    $("#bossflowRadio").off("click","span").on("click","span",function() {
                        if ($(this).hasClass("selected")) {
                            $(this).removeClass("selected");
                            $(this).parent().removeClass("selected");
                            $(this).parent().prev().attr("");
                        }else{
                            $(this).addClass("selected");
                            $(this).parent().addClass("selected");
                            $(this).parent().prev().attr("checked","checked");
                        }
                    });
                },
                error:function(){
                    showMessage("获取数据失败","error");
                }
            })
        }
    });

    //业务订购复选框和input输入框双向绑定,输入框删除，复选框取消选中
    $("#bussinessCode").on("change",function(){
        var codes = $(this).val().split(",");
        var checkbox = $("#top15BussOrder").find("input[checked='checked']");
        $.each(checkbox,function(i,item){
            var isExist = false;
            $.each(codes,function(j,code){
                if( $(item).val() == code ){
                    isExist = true;
                }
            });
            if( !isExist ){
                $(item).removeAttr("checked");
                $(item).next().removeClass("selected");
                $(item).next().find("span").removeClass("selected");
            }
        })
    });

    //上网行为--搜索
    $("#appSearch").keypress(function () {
        var typeId = $("#appSelect").val(),
            searchVal = $(this).val();
        getAppInfoList(typeId,searchVal);
    }).next().click(function(){
        var typeId = $("#appSelect").val(),
            searchVal = $("#appSearch").val();
        getAppInfoList(typeId,searchVal);
    })
    //实时位置--搜索
    $("#siteSearch").keypress(function () {
        var posType = $("#siteSelect").val(),
            searchVal = $(this).val();
        getPosInfoList(posType,searchVal);
    }).next().click(function(){
        var posType = $("#siteSelect").val(),
            searchVal = $("#siteSearch").val();
        getPosInfoList(posType,searchVal);
    })
    //场景事件-上网行为，实时位置
    $("#appSelect,#siteSelect").change(function(){
        var bigClass = $(this).val(),
            searchVal = "";
        if( $(this).attr("id") == "appSelect" ){
            searchVal = $("#appSearch").val();
            getAppInfoList(bigClass,searchVal);
        }else{
            searchVal = $("#siteSearch").val();
            getPosInfoList(bigClass,searchVal);
        }
    });
    //上网行为和实时位置分页展示
    function appOrPosList() {
        return {
            splitData: function(data){
                var newData = [];
                for(var i=0;i<data.length;i+=12){
                    newData.push(data.slice(i,i+12));
                }
                return newData;
            },
            draw:function(id,newData){
                var html = "";
                if( !newData.length ){
                    $(id).html("<p class='margin-top-40'>暂无数据，请选择其他类型</p>");
                    return
                }
                html += "<ul class='sub-checxbox clearfix'>";
                for(var j=0;j<newData.length;j++){
                    var pid = "",pname = "";
                    if(  id == "#appTagsTab" ){
                        pid = newData[j].APPLYID;
                        pname = newData[j].APPLYNAME;
                        bigClass = newData[j].BIGCLASSID;
                    }else{
                        pid = newData[j].POSITION_CODE;
                        pname = newData[j].POSITION_NAME;
                        bigClass = "";
                    }
                    html += "<li>";
                    html += "<input type='checkbox' name='paymentChannel' value='"+pid+"' data-bigclass='"+bigClass+"'>";
                    html += "<label class='col-mar'title='"+pname+"'><span></span>"+pname+"</label>";
                    html += "</li>";
                }
                html += "</ul>";

                $(id).html(html);
            },
            makePagination:function(pagerId,data,tagsTabId){
                var self = this;
                var pager = null;
                var pages = data.length;
                $(pagerId).html("");
                pager = new Pager({
                    total: pages,
                    parent: $(pagerId)[0],
                    onchange: doChange,
                });
                function doChange(obj){
                    self.draw(tagsTabId,data[obj.index-1]);
                }
            }
        }
    }

    var getAppInfoList = function(typeId,searchVal){
        var drawAppOrPosList = appOrPosList();
        $.ajax({
            url: "./base/getAppInfoList?typeId="+typeId+"&searchVal="+searchVal,
            type: "get",
            dataType: "json",
            cache:false,
            success:function(res){
                if(res.code != 0){
                    showMessage(res.msg,"error");
                    return
                }
                var data = res.data.appInfoList;
                var newData =  drawAppOrPosList.splitData(data);
                var firstPage = newData.length?newData[0]:[];
                //globalAttrs.appList[appId] = newData;
                drawAppOrPosList.draw("#appTagsTab",firstPage);
                drawAppOrPosList.makePagination("#appPage",newData,"#appTagsTab");
                $("#appNum").text(data.length);
            },
            error:function(){
                showMessage("获取信息失败","error")
            }
        })
    };
    //实时位置
    var getPosInfoList = function(posId,searchVal){
        var drawAppOrPosList = appOrPosList();
        $.ajax({
            url: "./base/getRealTimePositionInfos?tagCode="+posId+"&searchVal="+searchVal,
            type: "get",
            dataType: "json",
            cache:false,
            success:function(res){
                var html = "";
                if(res.code != 0){
                    showMessage(res.msg,"error");
                    return
                }

                var data = res.data;
                var $siteNum = $("#siteNum");
                if ( !data || !data.length ){
                    var html = "<p class='margin-top-40'>暂无数据，请选择其他实时位置类型</p>"
                    $("#siteTagsTab").html(html);
                    $siteNum.text(0);
                }else{
                    var newData =  drawAppOrPosList.splitData(data);
                    var firstPage = newData.length?newData[0]:[];
                    //globalAttrs.appList[appId] = newData;
                    drawAppOrPosList.draw("#siteTagsTab",firstPage);
                    drawAppOrPosList.makePagination("#sitePage",newData,"#siteTagsTab");
                    $siteNum.text(data.length);
                }
            },
            error:function(){
                showMessage("获取信息失败","error")
            }
        })
    };

    $(".h-tab li").click(function(event) {
        if ($(this).find("span").hasClass("selected")) {
            return;
        }
        $(this).parent().find("span").removeClass("selected");
        $(this).find("span").addClass("selected");
        var tab = $(this).attr("data-bindtab");
        $(this).parent().siblings(".content").find("li[data-tab="+tab+"]").addClass("selected").siblings().removeClass("selected");
    });

    //场景事件-网厅缴费-缴费渠道
    $("li[data-bindtab='payChannel']").click(function(){
        var self = this;
        if(!$(this).attr("data-iscreated")){
            $.ajax({
                url: "./base/getPayType",
                type:"get",
                dataType:"json",
                cache:false,
                success:function(res){
                    if(res.code !== 0){
                        showMessage(res.msg,"error");
                        return;
                    }
                    var data = res.data.payTypeList;
                    var html = "";
                    if( data && data.length ){
                        $.each(data,function(i,chan){
                            html += "<input type='checkbox' data-ruleId='SR003' data-tagname='label_area' name='paymentChannel' value='"+chan.TYPECODE+"'/>";
                            html += "<label class='col-mar'><span></span>"+chan.TYPENAME+"</label>";
                        });
                        $("li[data-tab='payChannel']").html(html);
                        $("li[data-tab='payChannel'] label").bind('click', temp1);
                        $(self).attr("data-iscreated","created");
                    }
                },
                error:function(){
                    showMessage("获取信息失败","error")
                }
            })

        }
    });

    //实时场景-智能地图-tab切换
    $(".intelligent-map li a").click(function(){
        var $li = $(this).parent();
        var tab = $li.attr("data-bindtab");
        $li.addClass("selected").siblings(".selected").removeClass("selected");
        $li.find("input").attr("data-ruleid","SR012");
        $li.siblings("li").find("input").removeAttr("data-ruleid");
        $(".intelligent-map-tab").find("div[data-tab='"+tab+"']").addClass("selected").siblings(".selected").removeClass("selected");
    });
    $(".j-intelliMap-checkbox label").click(function () {
        $(".j-usergroup-filter").toggleClass("hide");
    })
    //实时场景-智能地图-跳转事件
    $(".j-jumpto-map").click(function () {
        //updateActivityId();
        //var taskId = $("#smartMapActivityId").val();
        var taskId =$("#activityId").text();
        //clearTable("smartMapTable");

        clearTable("intelligentMapTableR");

        goToSmartMap(taskId,1);

        //自动刷新接口
        //var	winObj= window.open("http://10.113.254.17:8080/mobd-web/page/newTask?taskId="+taskId+"&taskType=1&userId="+userId+"&city="+cityId);
        //	var loop = setInterval(function() {
        //		if(winObj.closed) {
        //			clearInterval(loop);
        //			getChoseMapDataAndReloadTable(taskId);
        //			alert('窗口关闭了');
        //		}
        //	}, 1000);
        //window.open("http://10.113.254.17:8080/mobd-web/page/newTask?taskId="+taskId+"&taskType=0");
    });

    // 执行渠道
    $("#excChannel>.checkbox label").unbind("click", temp1);

    $("#excChannel").on("click", "#channelListBox.checkbox label", channelTemp1);

    //10086群发，来电提醒，朋友圈，微信公众号与其他渠道互斥
    $("#channelListBox").on("click","label[for='d05'],label[for='d06'],label[for='q10'],label[for='q11'],label[for='q05'],label[for='d07'],label[for='q13'],label[for='q12']",function(e){
        htmlEvents.singleConflictChannel(this);
    });


    //标签导入，跳转标签库
    $(".label-input").click(function(){
        $.blockUI({ message:"跳转中，请稍等..."});
        $.ajax({
            url: "./Coc/loginUrl",
            type:'get',
            cache:false,
            dataType:'json',
            success:function(res){
                $.unblockUI();
                if(res.code == 0){
                    window.open(res.data);
                }else{
                    showMessage(res.msg,'error')
                }
            },
            error:function(){
                $.unblockUI();
                showMessage("跳转标签库失败","error")
            }
        })

    })
    function temp4(e) {
        $("#targetUser .user-group-input .popup-box").removeClass("show");
        $("#targetUser .user-group-input .li-wrap").removeClass("on");
        $("#targetUser .user-group-input .bottom-shadow-cover").removeClass("show")
        var $parent = $(this).parent();
        $parent.toggleClass("on");
        var $grandpa = $parent.parent();
        if (!$grandpa.attr("data-isCreated")) {
            popupConfig(parseInt($grandpa.attr("data-popup")))($grandpa);
            $grandpa.attr("data-isCreated", "created")
            $grandpa.find(".modal-item table .checkbox label span").click(function(e) {
                $(this).toggleClass("selected");
                var isChecked = $(this).hasClass("selected");
                isChecked ? $(this).prev("input").attr("checked", "checked") : $(this).prev("input").removeAttr("checked");
            });
            // 弹窗取消确定事件
            $grandpa.find("button.cancel").click(function(e) {
                e.preventDefault();
                var $grandpa = $(this).closest(".modal-item");
                $grandpa.removeClass("show");
                $grandpa.siblings(".li-wrap").removeClass("on")
                    .siblings(".bottom-shadow-cover").removeClass("show");
            });
            //弹窗tab切换
            $(".tab-nav>li").click(function() {
                $(this).addClass("selected").siblings(".selected").removeClass("selected");
                var id = $(this).find("a").attr("data-href");
                $("#" + id).addClass("selected").siblings(".selected").removeClass("selected");
            });
        }
        $parent.nextAll().toggleClass("show");
        //checkbox多选事件
    }
    $("#targetUser .user-group-input li[data-popup] span.user-group-icon").click(temp4);

    $("#historyInputWrap span.user-group-icon").unbind("click");
    $("#historyInputWrap span.user-group-icon").click(function() {
        $("#targetUser .user-group-input .popup-box").removeClass("show");
        $("#targetUser .user-group-input .li-wrap").removeClass("on");
        $("#targetUser .user-group-input .bottom-shadow-cover").removeClass("show")
        var $parent = $(this).parent();
        $parent.toggleClass("on");
        var $grandpa = $parent.parent();

        if (!$grandpa.attr("data-isCreated")) {
            popupConfig(parseInt($grandpa.attr("data-popup")))($grandpa);
            $grandpa.attr("data-isCreated", "created");
            if ($grandpa.attr("id") == "historyInputWrap") { //历史导入

                //默认展示置自定义导入列表
                $("#historyInput .tab-nav li:first-child").trigger("click");
            }
            // 弹窗取消确定事件
            $grandpa.find("button.cancel").click(function(e) {
                e.preventDefault();
                var $grandpa = $(this).closest(".modal-item");
                $grandpa.removeClass("show");
                $grandpa.siblings(".li-wrap").removeClass("on")
                    .siblings(".bottom-shadow-cover").removeClass("show");
            });
            //弹窗tab切换
            $(".tab-nav>li").click(function() {
                $(this).addClass("selected").siblings(".selected").removeClass("selected");
                var id = $(this).find("a").attr("data-href");
                $("#" + id).addClass("selected").siblings(".selected").removeClass("selected");
            });
        } else if ($grandpa.attr("id") == "historyInputWrap") {
            $("#historyInput .tab-nav li.selected").trigger("click");
        }
        $parent.nextAll().toggleClass("show");
    })



    $("#smartMap span.user-group-icon").unbind("click");
    $("#smartMap span.user-group-icon").click(function() {
        $("#targetUser .user-group-input .popup-box").removeClass("show");
        $("#targetUser .user-group-input .li-wrap").removeClass("on");
        $("#targetUser .user-group-input .bottom-shadow-cover").removeClass("show")
        var $parent = $(this).parent();
        $parent.toggleClass("on");
        var $grandpa = $parent.parent();

        if (!$grandpa.attr("data-isCreated")) {
            popupConfig(parseInt($grandpa.attr("data-popup")))($grandpa);
            $grandpa.attr("data-isCreated", "created");
            if ($grandpa.attr("id") == "smartMap") { //历史导入

                //默认展示置自定义导入列表
                $("#smartMapshow .tab-nav li:first-child").trigger("click");
            }
            // 弹窗取消确定事件
            $grandpa.find("button.cancel").click(function(e) {
                e.preventDefault();
                var $grandpa = $(this).closest(".modal-item");
                $grandpa.removeClass("show");
                $grandpa.siblings(".li-wrap").removeClass("on")
                    .siblings(".bottom-shadow-cover").removeClass("show");
            });

        } else if ($grandpa.attr("id") == "smartMap") {
            $("#smartMapshow .tab-nav li.selected").trigger("click");
        }
        var activityId = $("#smartMapActivityId").val();
        getChoseMapDataAndReloadTable(activityId);
        $parent.nextAll().toggleClass("show");
    });

    //TODO
    $("#smartMap").on("click","#choseUser",function(){
        $("#targetUser .user-group-input .popup-box").removeClass("show");
        $("#targetUser .user-group-input .li-wrap").removeClass("on");
        $("#targetUser .user-group-input .bottom-shadow-cover").removeClass("show");
        var activityId = $("#smartMapActivityId").val();
        getChoseMapDataAndReloadTable(activityId);
        var selectData = globalAttrs.mapSelectedData;
        var temp = "";
        $.each(selectData,function(i,item){
            temp = temp + item.name + ",";
        });
        temp = temp.substring(0,temp.length - 1);
        temp = temp.limit(100);

        var tag = {
            "id": activityId,
            "text": temp + "(0)",
            "name": temp,
            "createType":5
        };
        $("#selectedUser").tagsinput('add',tag);
    });


    $("#customerInsight").on("click","#choseResult",function(){
        clearTable("customerInsightTable");
        $("#targetUser .user-group-input .popup-box").removeClass("show");
        $("#targetUser .user-group-input .li-wrap").removeClass("on");
        $("#targetUser .user-group-input .bottom-shadow-cover").removeClass("show");
        var checkStatus = layui.table.checkStatus('customerInsightTable');
        var data = checkStatus.data;
        $.each(data,function(i,item){
            var tag = {
                "id": item.batchId,
                "text": item.name + "(0)",
                "name": item.name,
                "createType":6,
            }
            $("#selectedUser").tagsinput('add',tag);
        })
    })


    $("#customerInsight span.user-group-icon").unbind("click");
    $("#customerInsight span.user-group-icon").click(function(){
        $.blockUI({
            message:"跳转中，请稍等..."
        });
        $.ajax({
            url: "./customerInsight/loginUrl",
            type:"get",
            dataType:"json",
            cache:false,
            success:function(res){
                $.unblockUI();
                if(res.code != 0){
                    showMessage(res.msg,"error");
                    return
                }
                window.open(res.data);
            },
            error:function(){
                $.unblockUI();
                showMessage("跳转至数字内容页失败","error");
            }
        })
    })



    $("#customerInsight .li-wrap>p").unbind("click");
    $("#customerInsight .li-wrap>p").click(function() {
        $("#targetUser .user-group-input .popup-box").removeClass("show");
        $("#targetUser .user-group-input .li-wrap").removeClass("on");
        $("#targetUser .user-group-input .bottom-shadow-cover").removeClass("show")
        var $parent = $(this).parent();
        $parent.toggleClass("on");
        var $grandpa = $parent.parent();

        if (!$grandpa.attr("data-isCreated")) {
            popupConfig(parseInt($grandpa.attr("data-popup")))($grandpa);
            $grandpa.attr("data-isCreated", "created");
            if ($grandpa.attr("id") == "customerInsight") { //历史导入

                //默认展示置自定义导入列表
                $("#customerInsightshow .tab-nav li:first-child").trigger("click");
            }
            // 弹窗取消确定事件
            $grandpa.find("button.cancel").click(function(e) {
                e.preventDefault();
                var $grandpa = $(this).closest(".modal-item");
                $grandpa.removeClass("show");
                $grandpa.siblings(".li-wrap").removeClass("on")
                    .siblings(".bottom-shadow-cover").removeClass("show");
            });

        } else if ($grandpa.attr("id") == "customerInsight") {
            $("#customerInsightshow .tab-nav li.selected").trigger("click");
        }

        $parent.nextAll().toggleClass("show");
    })


    //集市导入，获取数据库列表
    $(".j-market-import").click(htmlEvents.customerGroup.getDatabaseList);


    //数字内容 跳转
    $("#figureContWrap span.user-group-icon").unbind("click");
    $("#figureContWrap span.user-group-icon").click(function(){
        $.blockUI({
            message:"跳转中，请稍等..."
        });
        $.ajax({
            url: "./miguContentConsole/token",
            type:"get",
            dataType:"json",
            cache:false,
            success:function(res){
                $.unblockUI();
                if(res.code != 0){
                    showMessage(res.msg,"error");
                    return
                }
                var backData = res.data;
                if(backData.success){
                    var urlParam = backData.result;
                    var activityId=$("#activityId").text();//编号
                    window.open("http://10.113.254.17:8080/migu-content-console/activities/default/"+activityId+"/?sid="+urlParam);
                }

            },
            error:function(){
                $.unblockUI();
                showMessage("跳转至数字内容页失败","error");
            }
        })
    })
    //数字内容弹窗
    $("#figureContWrap .li-wrap>p").click(function(){
        $("#targetUser .user-group-input .popup-box").removeClass("show");
        $("#targetUser .user-group-input .li-wrap").removeClass("on");
        $("#targetUser .user-group-input .bottom-shadow-cover").removeClass("show");
        var $parent = $(this).parent();
        $parent.toggleClass("on");
        var $grandpa = $parent.parent();
        var actId = $.trim($("#activityId").text());

        if (!$grandpa.attr("data-isCreated")) {
            popupConfig(parseInt($grandpa.attr("data-popup")))($grandpa);
            $grandpa.attr("data-isCreated", "created");
            // 弹窗取消确定事件
            $grandpa.find("button.cancel").click(function(e) {
                e.preventDefault();
                var $grandpa = $(this).closest(".modal-item");
                $grandpa.removeClass("show");
                $grandpa.siblings(".li-wrap").removeClass("on")
                    .siblings(".bottom-shadow-cover").removeClass("show");
            });
        }

        var fillTable = function(data,id,msg) {
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
                    html += "<td class='hide' data-id='" + data[i].resultId + "' data-amount='" + data[i].amount + "'>" + data[i].resultId + "</td>";
                    html += "<td title='"+ data[i].name +"'>" + data[i].name + "</td>";
                    html += "<td>" + data[i].amount + "</td>";
                    html += "</tr>";
                }
                $(id).html(html);
            }else{
                if(msg){
                    $(id).html("<tr><td clospan='4'>"+msg+"</td></tr>")
                }else{
                    $(id).html("<tr><td clospan='4'>暂无数据</td></tr>")
                }
            }
        }

        //获取选择内容
        $.ajax({
            url: " ./miguContentConsole/pageResults?activityId="+actId+"&currentPage=1&pageSize=5",
            type: 'get',
            datatType: 'json',
            success: function(res) {
                if (res.code == 0) {
                    var backData = res.data;
                    if(backData.success){
                        if(res.data.result.type == 1){
                            var data = backData.result.recommendations;
                            var totalPage = data.pageCount || 1;
                            var totals = data.totalCount || 0;
                            fillTable(data.data,"#figureTable");
                            initPage("#figurePage", totalPage, pageChangeFuncs(), totals);
                        }else{
                            //if( res.data.result.completed ){
                            var backData = res.data.result;
                            var dataArr = [];
                            var obj = {};
                            if( backData.type == 2 ){
                                obj.name = (backData.selectedTags).join("，");
                            }else{
                                obj.name = backData.fileName;
                            }
                            if( backData.completed ){
                                obj.amount = backData.amount;
                            }else{
                                obj.amount = "";
                            }
                            obj.resultId = backData.type;
                            dataArr.push(obj);
                            fillTable(dataArr,"#figureTable");
                            initPage("#figurePage", 1, pageChangeFuncs(), 1);
                            /*}else{
								var data = [];
								fillTable(data,"#figureTable","用户群计算中，请稍等...");
							}*/
                        }
                    }else{
                        var data = [];
                        fillTable(data,"#figureTable",backData.message);
                    }

                }else{
                    showMessage(res.msg,"error");
                }
            },
            error: function() {
                showMessage("获取信息失败","error");
            }
        })

        $("#figureCont table").on('click', '.checkbox label span',popupEvents.tableCheckboxEvent);
        //分页上下页点击事件
        function pageChangeFuncs() {
            var id = "#figureTable";
            return function doPageChange(obj) {
                var param = {
                    activityId:actId,
                    currentPage:obj.index,
                    pageSize:5
                }
                var array = [];

                isRemoveAll("#figureContWrap");

                $(id + " td label input[checked='checked']").closest("tr")
                    .find("td[data-id]").each(function(index, item) {
                    array.push($(item).attr("data-id")+ "_(" + $(item).attr("data-amount")  + ")_" + $(item).next().text()+"_4");
                });
                if (array.length) {
                    if (shouldRemoveAll_l) {
                        $("#selectedUser").tagsinput("removeAll");
                        shouldRemoveAll_l = false;
                    }
                }
                for (var i = 0; i < array.length; ++i) {
                    var temp = array[i].split("_")
                    var tag = {
                        id: temp[0]+"_"+temp[2] + "_" + temp[3] +  "_" +temp[4],
                        text: temp[2] +"" +temp[1]
                    }
                    $("#selectedUser").tagsinput('add', tag);
                }
                isTabChanged_l = false;
                $.ajax({
                    url: " ./miguContentConsole/pageResults",
                    type: 'get',
                    data: param,
                    datatType: 'json',
                    success: function(res) {
                        if (res.code === 0) {
                            var backData = res.data;
                            if(backData.success){
                                var data = backData.result.recommendations;
                                fillTable(data.data,"#figureTable");

                            }else{
                                var data = [];
                                fillTable(data,"#figureTable",backData.message);
                            }
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
        function initPage(id, pages, func, totals) {
            $(id).html("");
            var pager = new Pager({
                total: pages,
                parent: $(id)[0],
                onchange: func,
            });
            $(id + " .cur").before("<span>第</span>");
            $(id + " .total-page").after("<span>页,共" + (totals || "0") + "条</span>");
        }
        $("#figureContWrap table").on('click', '.checkbox label span',popupEvents.tableCheckboxEvent);
        $parent.nextAll().toggleClass("show");
    });
    //end数字内容

    // 推荐业务 弹窗
    $("#business span.user-group-icon").click(function() {
        var $parent = $(this).parent();
        $parent.toggleClass("on");
        if($parent.hasClass("on")){  //展开弹窗，滚动条滚动至底部
            var scrollH = 0;
            var scrollDivs = $(".modal-wrap-new .modal-box-content>div");
            $.each(scrollDivs,function(i,item){
                scrollH += $(item).height();
            })
            $(".modal-wrap-new .modal-box-content").slimScroll({
                scrollTo: scrollH
            });
        }
        var $grandpa = $parent.parent();
        if (!$grandpa.attr("data-isCreated")) {
            popupConfig(parseInt($grandpa.attr("data-popup")))($grandpa);
            $grandpa.attr("data-isCreated", "created");
            //默认展示资费列表
            $("#recommand .tab-nav li:first-child").trigger("click");
            // 弹窗取消确定事件
            $grandpa.find("button.cancel").click(function(e) {
                e.preventDefault();
                var $grandpa = $(this).closest(".modal-item");
                $grandpa.removeClass("show");
                $grandpa.siblings(".li-wrap").removeClass("on")
                    .siblings(".bottom-shadow-cover").removeClass("show");
            });
        }
        $parent.nextAll().toggleClass("show");
    })
    /*
	function canvasFill(id, total, itemLength, nowItemIndex, isVertical) {
		var canvas = document.getElementById(id);
		canvas.height = canvas.height;
		var ctx = canvas.getContext("2d");
		if (isVertical) {
			var change = itemLength * nowItemIndex - 30;
			ctx.translate(0.5, 0.5);
			ctx.strokeStyle = "#DCE0E8";
			ctx.moveTo(10, 0);
			ctx.lineTo(10, change);
			ctx.lineTo(0, change + 10);
			ctx.lineTo(10, change + 20);
			ctx.lineTo(10, total);
			ctx.stroke();
		} else {
			var change = (itemLength >> 1) - 10 + itemLength * nowItemIndex;
			ctx.translate(0.5, 0.5);
			ctx.strokeStyle = "#DCE0E8";
			ctx.moveTo(0, 10);
			ctx.lineTo(change, 10);
			ctx.lineTo(change + 10, 0);
			ctx.lineTo(change + 20, 10);
			ctx.lineTo(total, 10);
			ctx.stroke();
		}
	}*/



    //推荐业务tagsinput初始化
    var recommand = new tagsOperate();
    recommand.initTagsIput("#chosedBussiness");
    recommand.tableTagsinputEvent("#chooseBuss", "#fee", "#chosedBussiness");
    recommand.tableTagsinputEvent("#chooseBuss", "#vicAct", "#chosedBussiness");
    recommand.tableTagsinputEvent("#chooseBuss", "#brodcast", "#chosedBussiness");
    recommand.tableTagsinputEvent("#chooseBuss", "#common", "#chosedBussiness");
    $("#chosedBussiness").on("itemRemoved", function(event) {
        var prodId = event.item.id.split("_")[0];
        var type = event.item.id.split("_")[1];
        var $tr = $("#recommand>div[data-type=" + type + "]").find("table tr");
        $tr.removeClass("selected");
        $tr.find("input").attr("checked", false);
        $tr.find("span").removeClass("selected");
        --nowChoosedItem;
    });


    //保存新建模型，拼接实时场景信息
    var saveSceneInfo = function($scene){ //选中的场景下的input
        var activitySceneInfoList = [];
        var typeid = $scene.val();
        var $sceneTab = $(".act-type-sub-panel").find("li[data-typeid='"+typeid+"']");
        if(!$sceneTab.is(":empty")){
            var rules = $sceneTab.find("[data-ruleid]");
            $.each(rules,function (i,rule) {
                var activitySceneInfo = {};
                activitySceneInfo.sceneRuleId = $(rule).attr("data-ruleid");
                if($(rule).attr("data-tagname") == "tags_area"){    //tagsInput选中的标签
                    var tags = $(rule).prev(".bootstrap-tagsinput").find(".tag");
                    var ruleValue = "", extValue = "";
                    $.each(tags,function (i, tagItem) {
                        var data = $(tagItem).data("item");
                        ruleValue = ruleValue + data.id + ",";
                        extValue = extValue + data.text + ",";
                    })
                    activitySceneInfo.sceneRuleValue = ruleValue.substring(0,ruleValue.length-1);
                    activitySceneInfo.sceneRuleExtendedValue = extValue.substring(0,extValue.length-1);
                    activitySceneInfoList.push(activitySceneInfo);
                }else if($(rule).attr("data-tagname") == "label_area"){   //缴费渠道和pcc,label选择
                    if( $(rule).attr("data-ruleid") == "SR014" ){
                        var ifChooseUserGroup = $(".j-intelliMap-checkbox label").hasClass("selected");
                        if( !ifChooseUserGroup ){
                            return;
                        }
                    }

                    if($(rule).attr("checked")){
                        if( $(rule).attr("data-ruleid") == "SR010" ){  //pcc策略，列表形式
                            activitySceneInfo.sceneRuleValue = $(rule).closest("tr").find("td[data-code]").attr("data-code");
                            activitySceneInfo.sceneRuleExtendedValue =  $(rule).closest("tr").find("td[data-code]").text();
                        }else{
                            activitySceneInfo.sceneRuleValue = $(rule).val();
                            activitySceneInfo.sceneRuleExtendedValue = $(this).next().text();
                        }

                        activitySceneInfoList.push(activitySceneInfo);
                    }
                }else if($(rule).attr("data-tagname") == "select_area"){
                    if(  $(rule).val() != "0" ){
                        activitySceneInfo.sceneRuleValue = $(rule).val();
                        activitySceneInfo.sceneRuleExtendedValue = $(rule).find("option:selected").text();
                        activitySceneInfoList.push(activitySceneInfo);
                    }
                }else {
                    if( $(rule).attr("data-ruleid") == "SR004" || $(rule).attr("data-ruleid") == "SR005"){
                        var $li = $(rule).parent();
                        if( !$li.hasClass("selected") ){
                            return
                        }
                    }
                    if( $(rule).attr("data-ruleid") == "SR013" ){
                        var isRealTime = $(rule).closest("div.j-passbyLimitTime").hasClass("selected");
                        if( !isRealTime ){
                            return;
                        }
                    }
                    activitySceneInfo.sceneRuleValue = $(rule).val();
                    activitySceneInfo.sceneRuleExtendedValue = "";
                    activitySceneInfoList.push(activitySceneInfo);
                }
            })
        }
        return activitySceneInfoList;
    }

    //保存新建模型
    $("#saveModel").click(function() {

        // 渠道列表
        var channels = $("#item-tab").find("a"); //选中的渠道
        $.each(channels, function(i, item) {
            var msg = "";
            var channelId = $(item).attr("data-channelid");
            var $tab = $("#channel-tab li[data-channelid='" + channelId + "']");
            var $dataRuleid = $tab.find("[data-ruleid]");
            $dataRuleid.each(function(index, item) {
                var $item = $(item);
                switch($item.attr('data-tagname')) {
                    case 'text_area':
                        var ruleValue = $item.val();
                        var reg = RegExp(/本地/);
                        if(reg.exec(ruleValue)) {
                            msg += "本地,";
                        }
                        reg = RegExp(/省内/);
                        if(reg.exec(ruleValue)) {
                            msg += "省内,";
                        }
                        reg = RegExp(/漫游/);
                        if(reg.exec(ruleValue)) {
                            msg += "漫游,";
                        }
                        break;
                    default:
                }
            });

            if(msg !== "") {
                showConfirm("营销用语中包含: "+msg+"确定要提交吗？",saveActivity);
            }
        });

    });
})


function saveActivity() {
    $.blockUI({
        message:"信息保存中，请稍等...",
    });
    var now = new Date();
    var createDate = formatDate(now, 'yyyyMMdd');
    var param = {};
    param.activityId = $("#activityId").text(); // private String activityId;
    param.activityName = $("#activityName").val(); // private String activityName;
    param.creatorId = "testUSERID"; // private String creatorId;
    param.creatorName = "testUSERNAME"; // private String creatorName;
    param.createTime = createDate; //private String createTime;
    param.startTime = $("#startTime").val(); //private String startTime;
    param.endTime = $("#endTime").val(); //private String endTime;
    param.businessTypeId = $("#bussinessType").val(); //private int businessTypeId;
    param.businessSmallTypeId = $("#bussinessSmallType").val(); //private int businessTypeId;
    param.businessTypeName = $("#bussinessType").find("option:selected").text(); // private String businessTypeName;
    param.businessSmallTypeName = $("#bussinessSmallType").find("option:selected").text(); // private String businessTypeName;
    param.marketingPurposeId = $("#marketingPurpose").val(); //private int marketingPurposeId;
    param.marketingPurposeName = $("#marketingPurpose").find("option:selected").text(); //private String marketingPurposeName;
    // if(!$(".j-update-cycle").hasClass("hide")){  //?????????????????
    // 	param.customerUpdateCycle = $(".j-update-cycle").find("p.selected input").val();
    // }else{
    param.customerUpdateCycle = 1;
    //}
    // param.sceneFlag = 0;//private String sceneFlag;
    // param.sceneId = 0;//private int sceneId;
    // param.sceneName = "非实时";//private int sceneName;
    var $sel = $("#actTypes>li.selected").find("input");
    if ($sel.val() == 0) {
        param.sceneFlag = 0; //0表示选中非实时
    } else {
        param.sceneFlag = 1; //1表示未选择非实时
        param.sceneId = $sel.val();
        param.sceneName = $sel.siblings("label").text();
        var infoList = saveSceneInfo($sel);
        if(infoList.length){
            $.each(infoList,function (i, info) {
                info.activityId = $("#activityId").text();
                info.activityName = $("#activityName").val();
                info.sceneId =  $sel.val();
                info.sceneName = $sel.siblings("label").text();
            })
        }
        param.activitySceneInfoList = infoList;
    }
    param.smartMapActivityId = $("#smartMapActivityId").val();
    param.removeEmployee = ifSelected("#removeEmployee");
    param.removeRedList = ifSelected("#removeRedList");
    param.removeSensitive = ifSelected("#removeSensitive");
    param.removeCancel10086 = ifSelected("#removeCancel10086");
    param.removeGroupUser = ifSelected("#removeGroupUser");
    param.removeUpload = 0; // 默认0
    if (ifSelected("#removeUpload")) {
        param.removeUpload = 1;
        param.removeCustomerGroupId = $("#removeUpload").data("customerGroupId");
    }
    param.activityState = 0; //private int activityState;
    param.stopped = 0; //private int stopped;
    param.isCanApproval = 0; //private int isCanApproval;
    param.removeCustomerGroup_id = "";

    if($.trim($("#phoneNum").val())){
        // testPhoneNum
        var phoneNumbers = [];
        var phoneArr = $("#phoneNum").val().split(",");
        $.each(phoneArr,function(i,item){
            if (regTest(item, "phone-number")) {
                phoneNumbers.push(item);
            }
        })
        param.testPhoneNum = phoneNumbers.join(",");
    }

    // private List<ActivityChannelInfo> activityChannelInfoList;
    param.activityChannelDetailList = [];
    // private List<ActivityCustomerGroupInfo> activityCustomerGroupInfoList;
    param.activityCustomerGroupInfoList = [];
    // private List<ActivityRecommendProduct> ActivityRecommendProduct;
    param.activityRecommendProducts = [];

    // 渠道列表
    var channels = $("#item-tab").find("a"); //选中的渠道
    $.each(channels, function(i, item) {
        var channelInfo = {};
        var channelId = $(item).attr("data-channelid");
        var $tab = $("#channel-tab li[data-channelid='" + channelId + "']");
        channelInfo.activityId = param.activityId;
        channelInfo.activityName = param.activityName;
        channelInfo.channelId = channelId;

        channelInfo.approverInfoList = [];
        var approverInfoListItem = {};
        approverInfoListItem.approverLevel = 1;
        approverInfoListItem.approverId = $tab.find('select[data-use="contentApproverId"]').val();
        approverInfoListItem.approvalGroup = $tab.find('select[data-use="contentApproverId"]').data("approvalGroup")?
            $tab.find('select[data-use="contentApproverId"]').data("approvalGroup"):"";
        approverInfoListItem.approvalRole = $tab.find('select[data-use="contentApproverId"]').data("approvalRole")?
            $tab.find('select[data-use="contentApproverId"]').data("approvalRole"):"";
        channelInfo.approverInfoList.push(approverInfoListItem);
        approverInfoListItem = {};
        approverInfoListItem.approverLevel = 2;   //二级审批
        approverInfoListItem.approverId = $tab.find('select[data-use="channelApproverId"]').val();
        approverInfoListItem.approvalGroup = $tab.find('select[data-use="channelApproverId"]').data("approvalGroup")?
            $tab.find('select[data-use="channelApproverId"]').data("approvalGroup"):"";
        approverInfoListItem.approvalRole = $tab.find('select[data-use="channelApproverId"]').data("approvalRole")?
            $tab.find('select[data-use="channelApproverId"]').data("approvalRole"):"";
        channelInfo.approverInfoList.push(approverInfoListItem);
        if (channelInfo.approverInfoList[0].approverId == "sys"
            && channelInfo.approverInfoList[1].approverId == "sys")
            channelInfo.approverInfoList = [];
        channelInfo.channelName = $(item).text();
        channelInfo.activityChannelRuleDetailList = [];
        var $dataRuleid = $tab.find("[data-ruleid]");
        //console.log($dataRuleid);
        $dataRuleid.each(function(index, item) {
            if (channelId == 'q10' && $(item).attr("sub") == null) {
                return;
            }
            var item1 = {};
            item1.ruleId = $(item).attr("data-ruleid");
            var ruleValue = '';
            var extend = '';
            var $item = $(item);
            switch($item.attr('data-tagname')) {
                case 'select_area':
                    ruleValue = $item.val();
                    extend = $item.find("option:selected").text();  //下拉列表，为选中项的内容
                    if(extend == "请选择"){
                        extend = "无";
                    }
                    break;
                case 'text_area':
                    ruleValue = $item.val();
                    extend = "";
                    break;
                case 'input_area':
                    if ($item.attr('type') == 'file') {
                        ruleValue = $item.data('ruleValue')  // 获取图片的文件名
                        extend = $(item).attr("id");  //图片，为图片的id
                    }else{
                        if($item.attr("data-ruleid") == "R003"){
                            ruleValue = $item.attr("data-postypeid");
                            extend = globalAttrs.posTypeName[ruleValue];
                        }else{
                            ruleValue = $item.val();
                            extend = "";
                        }
                    }
                    break;
                case 'label_area':
                    if($item.hasClass("selected")){
                        ruleValue = $item.text();
                    }else{
                        ruleValue =  "";
                    }
                    extend = "";
                    break;
                default:
            }
            // item1.ruleValue = $(item).val() || ($(item).hasClass("selected") && $(item).text());
            item1.ruleValue = ruleValue;
            item1.ruleExtendedValue = extend;
            channelInfo.activityChannelRuleDetailList.push(item1);
        });
        param.activityChannelDetailList.push(channelInfo);
    });

    // activityCustomerGroupInfoList
    /*	var arr = $("#selectedUser").val().split(",");
     if (arr[0] != "" ){
     for (var i = 0; i < arr.length; ++i) {
     var temp = {};
     var str = arr[i].split("_");
     temp.activityId = param.activityId;
     temp.activityName = param.activityName;
     temp.customerGroupId = str[0];
     temp.customerGroupName = str[1];
     temp.createType = str[2];
     param.activityCustomerGroupInfoList.push(temp);
     }
     }*/
    var tags = $("#targetUser .bootstrap-tagsinput .tag");
    if(tags.length){
        $.each(tags,function (i, tagItem) {
            var data = $(tagItem).data("item");
            var temp = {};
            temp.activityId = param.activityId;
            temp.activityName = param.activityName;
            temp.customerGroupId = data.id;
            temp.customerGroupName = data.name;
            temp.createType = data.createType;
            param.activityCustomerGroupInfoList.push(temp);
        })
    }

    // 推荐业务ActivityRecommendProduct
    /*var recoBuss = $("#chosedBussiness").val();
     var array = recoBuss.split(",");
     if (array[0] != "") {
     // 资费，最多传5条数据；营销活动 和 宣传 只会传一条数据
     for (var i = 0; i < array.length; ++i) {
     var item = {};
     item.activityId = param.activityId;
     item.activityName = param.activityName;
     item.productType = getBusinessParam().prcType;
     var arrTemp = array[i].split("_");
     item.productId = arrTemp[0];
     item.productName = arrTemp[1];
     param.activityRecommendProducts.push(item);
     }
     }*/
    var recoTags = $("#chosedBussiness").prev(".bootstrap-tagsinput").find(".tag");
    if(recoTags.length){
        $.each(recoTags,function(i,tagItem){
            var data = $(tagItem).data("item");
            var item = {};
            item.activityId = param.activityId;
            item.activityName = param.activityName;
            item.productType = getBusinessParam().prcType;
            item.productId = data.id;
            item.productName = data.text;
            param.activityRecommendProducts.push(item);
        })
    }
    var result = fillRequired(param);
    if(!result){ // there are some wrong.
        $.unblockUI();
        return;
    }
    $.ajax({
        url: "./activities/saveActivityInfo",
        type: 'post',
        data: JSON.stringify(param),
        dataType: 'json',
        contentType: 'application/json',
        cache: false,
        success: function(res) {
            if (res.code === 0) {
                $.unblockUI();
                $(".modal-box-cancel").click();
                showMessage("保存成功","success");
                freshIndexPage();
            }else{
                $.unblockUI();
                showMessage(res.msg,"error")
            }
        },
        error: function(err) {
            $.unblockUI();
            showMessage("保存失败","error")
        }
    })
    
}

//提交前判断必填项是否填写完整,数值是否符合要求
function fillRequired(params) {
    var result = true;
    var configScene = [ "1","2","3","4","5","6","7","8","9","10","11","12","13" ]; //实时场景id数组，选择其中任意一个，可不选用户群
    //校验必填项是否填写
    var paramArr = ["activityName", "activityCustomerGroupInfoList",
        "activityRecommendProducts",
        "startTime", "endTime", "businessTypeId"];
    if( params.sceneFlag == 1 && configScene.indexOf(params.sceneId) != -1 ){
        if( params.sceneId == 11 ){
            //如果实时场景选择了智能地图--且选择了“上传用户群”，则必须选择用户群
            var chosed = $(".j-intelliMap-checkbox").find("label");
            if( !chosed.hasClass("selected") ){  //未选中，可不选用户群，不校验
                paramArr.splice(1,1);
            }
            //校验是否返回了地图选区数据
            if( !globalAttrs.mapSelectedData ){
                showMessage("智能地图场景，地图选区操作未成功","error",3000);
                result = false;
                return
            }
        }else{
            paramArr.splice(1,1);
        }

    }

    for (var i = 0; i < paramArr.length; ++i) {
        var item = paramArr[i];
        if (!params[item] || (typeof params[item] == "object"? (!params[item].length):0)) {
            showMessage("请将必要信息填写完整","error");
            result = false;
            return;
        }
    }
    /**
     * 校验渠道信息 activityChannelDetailList
     * 如果有可选渠道，则必须选择至少一个渠道；
     * 如果无可选渠道，可不选择，传[]
     */
    var ifHasChannels = $("#channelListBox>div.col-1").length;
    if( ifHasChannels ) {
        if( !params.activityChannelDetailList.length ){
            showMessage("请选择执行渠道","error")
            result = false;
            return;
        }else{
            //校验所选渠道信息是否填写完整,验证ruleValue
            var channels = params["activityChannelDetailList"];
            $.each(channels,function(i,chanInfo){
                var chanRules = chanInfo["activityChannelRuleDetailList"];
                if( chanInfo.channelId == "d01" || chanInfo.channelId == "q10" ){
                    if( $(".j-validate-null>input").attr("checked") != "checked" ){
                        $.each(chanRules,function(i,rule){
                            if(!rule["ruleValue"] ){
                                showMessage("请将所选渠道信息填写完整","error");
                                result = false;
                                return false;
                            }
                        })
                    }
                }else{
                    //微信公众号只做空白校验
                    if(chanInfo.channelId == "q10"){
                        $.each(chanRules,function(i,rule){
                            if(!rule["ruleValue"] ){
                                showMessage("请将所选渠道信息填写完整","error");
                                result = false;
                                return false;
                            }
                        })
                    }
                    var checkCount = 0;
                    $.each(chanRules,function(i,rule){
                        if(!rule["ruleValue"] && rule["ruleId"] != "R009" && rule["ruleId"] != "R007" && rule["ruleId"] != "R010"){
                            showMessage("请将所选渠道信息填写完整","error");
                            result = false;
                            return false;
                        }
                        if((!rule["ruleValue"] && rule["ruleId"] == "R007")){
                            checkCount++;
                        }
                        if((rule["ruleValue"] == "-1" && rule["ruleId"] == "R010")){
                            checkCount++;
                        }
                        if(checkCount == 2){
                            showMessage("连接码表和URL至少填写一项","error");
                            result = false;
                            return false;
                        }
                        if(rule["ruleValue"] && rule["ruleId"] == "R007" && chanInfo.channelId != "q06"){ //URL
                            if(!htmlEvents.IsURL(rule["ruleValue"])){
                                showMessage("渠道信息填写有误，URL格式不正确","error");
                                result = false;
                                return false;
                            }
                        }
                    })
                }
            })
        }
    }

    //校验营销用语/短信内容是否超过字数
    var selChannels = $("#item-tab a");
    $.each(selChannels,function(i,item){
        var channel = $(item).attr("data-channelid");
        var txtarea = $("#channel-tab [data-channelid='"+channel+"']").find("[data-ruleid='R001']");
        if(txtarea.length){
            if($(txtarea).hasClass("over-words")){
                showMessage("渠道信息填写有误，超出规定字数，请调整","error");
                result = false;
            }
            if($(txtarea).hasClass("j-no-standard")){
                showMessage("短信下发内容不符合规范，请调整","error");
                result = false;
            }
        }
    });
    //校验目标客户数是否超过150万
    // var isOnlyZt = false;
    // if( selChannels.length == 0 || selChannels.length >1 ){
    // 	isOnlyZt = false;
    // }else{
    // 	isOnlyZt = $(selChannels[0]).attr("data-channelid") == "q03"?true:false;
    // }
    // if( !isOnlyZt ){
    // 	if($("#selectedUser").hasClass("j-over-number")){
    // 		showMessage("目标用户数超过150万，请调整","error");
    // 		result = false;
    // 	}
    // }

    //校验所选实时场景信息是否填写完整,填写信息是否符合要求
    if(params.sceneFlag != 0){
        if(params.sceneId != 1 && params.sceneId != 6 && params.sceneId != 7 && params.sceneId != 9 && params.sceneId != 13 && params.sceneId != 11){  //无具体内容的实时场景
            if(!params.activitySceneInfoList || !params.activitySceneInfoList.length){
                showMessage("请将所选场景信息填写完整","error")
                result = false;
                return
            }else{
                if(params.sceneId == 2){
                    if(params.activitySceneInfoList.length < 0){
                        showMessage("请将所选场景信息填写完整","error");
                        result = false;
                        return;
                    }else{
                        $.each(params.activitySceneInfoList,function(i,sceneRule){
                            if(!sceneRule.sceneRuleValue){
                                showMessage("请将所选场景信息填写完整","error")
                                result = false;
                                return;
                            }
                        })
                    }
                }else{
                    if(params.sceneId == 8){ //pcc
                        if(params.activitySceneInfoList.length <= 1){
                            showMessage("请将所选场景信息填写完整","error");
                            result = false;
                            return;
                        }
                    }
                    $.each(params.activitySceneInfoList,function(i,sceneRule){
                        if(!sceneRule.sceneRuleValue){
                            showMessage("请将所选场景信息填写完整","error")
                            result = false;
                            return;
                        }
                    });
                    if( params.sceneId == 2 || params.sceneId == 10 ){
                        //校验数字区间是否正确
                        var $sceneTab = $(".act-type-sub-panel [data-typeid='"+params.sceneId+"']");
                        var min = parseFloat( $sceneTab.find("input.j-min-value").val() );
                        var max = parseFloat( $sceneTab.find("input.j-max-value").val() );
                        if(min >= max){
                            showMessage("所选实时场景信息填写有误，请调整","error");
                            result = false;
                            return;
                        }
                    };
                    if( params.sceneId == 11 ){ //校验智能地图，实时的逗留时长是否填写正确
                        var errorHint = $(".j-passbyLimitTime").find(".over-max-msg");
                        if( errorHint.length ){
                            showMessage("所选实时场景信息填写有误，请调整","error");
                            result = false;
                            return;
                        }
                    }
                }
            }
        }
    }

    //自定义剔除用户，校验是否已上传文件
    if( $("#removeUpload").hasClass("selected") ){
        if( !$("#removeUpload").data("customerGroupId") ){
            result = false;
            showMessage("请上传自定义剔除用户文件","error",3000);
        }
    }

    return result;
}

function ifSelected(id) {
    return $(id).hasClass("selected") ? 1 : 0;
}

//table表格中多选框点击事件
function temp1(e) {
    $(this).toggleClass("selected");
    $(this).find("span").toggleClass("selected");
    if ($(this).prev()) {
        if ($(this).prev().attr("checked") == "checked") {
            $(this).prev().removeAttr("checked");
        } else {
            $(this).prev().attr("checked", "checked");
        }
    }
}
//removeTag 事件
function temp2(tag) {
    var $temp = $(".data-list label").filter(function(index, value) {
        if ($.trim($(value).text()) == tag)
            return true;
    });
    $temp.removeClass("selected")
    $temp.find("span").removeClass("selected");
    $temp.prev().removeAttr("checked")
};

function isRemoveAll(tagListId){   //标签库：1; 数据集市：2; 文件上传:3; 数字内容:4;

    //var sels = $("#selectedUser").val();
    /*if(sels){
		var arr = sels.split(",");
		$.each(arr,function(i,sel){
			$.each(arr,function(i,item){
				var str = item.split("_");
				if(tagListId != "#figureContWrap"){      //判断已选用户群里是否有数字内容导入的，有先全部清空
					if(str[2] == 4){
						shouldRemoveAll_l = true;
						 return;
					}
				}else{     //从数字内容导入的客户群，先判断是都有其他途径导入的，有则先全部清空
					if(str[2] != 4){
						shouldRemoveAll_l = true;
						 return;
					}
				}

			})

		})
	}*/
    var sels = $("#targetUser .bootstrap-tagsinput .tag");
    if(sels.length){
        $.each(sels,function (i, tagItem) {
            var data = $(tagItem).data("item");
            if(tagListId != "#figureContWrap"){      //判断已选用户群里是否有数字内容导入的，有先全部清空
                if(data.createType == 4){
                    shouldRemoveAll_l = true;
                    return;
                }
            }else{                   //从数字内容导入的客户群，先判断是都有其他途径导入的，有则先全部清空
                if(data.createType != 4){
                    shouldRemoveAll_l = true;
                    return;
                }
            }
        })
    }
}

//集市导入
$("#marketInputWrap").on("click",".market-popup .ensure",function (e) {
    e.preventDefault();
    var _this = this;
    var $form = $("#marketImportForm");
    var params = $form.serializeJson();
    if( params.customizeFlag == null ){
        params.customizeFlag = "0";
    }

    var check = htmlEvents.customerGroup.checkIsCompleted(params);
    if( !check ){
        return
    }

    $.blockUI({message:"用户群创建中，请稍等...",});

    $.when(htmlEvents.customerGroup.marketImport(params))
        .done(function(code){
            $.unblockUI();
            if( code == 0 ){
                $(_this).next().trigger("click");
            }
        })
        .fail(function () {
            $.unblockUI();
        })
});

function tagsOperate() {
    this.initTagsIput = function(id) {
        //初始化tagsinput
        var $input = $(id);
        $input.tagsinput({
            itemValue: 'id',
            itemText: 'text',
            freeInput: false
        })
    }
}
//历史导入
tagsOperate.prototype.listTagsinput = function(tagListId, id) {   //标签库：1; 数据集市：2; 文件上传:3; 数字内容:4;
    $(tagListId).on("click", " .row.btns .ensure ", function(e) {
        isRemoveAll(tagListId);
        var type = $(this).attr("data-type");
        if (shouldRemoveAll_l) {
            $(id).tagsinput("removeAll");
            shouldRemoveAll_l = false;
        }
        var array = [];
        $(tagListId + " td label input[checked='checked']").closest("tr")
            .find("td[data-id]").each(function(index, item) {
            var tagItem = {};
            tagItem.cycle = $(item).attr("data-cycle") || 0;
            tagItem.id = $(item).attr("data-id");
            tagItem.amount = $(item).attr("data-amount");
            tagItem.name = $(item).next().text();
            tagItem.type = type;
            array.push(tagItem);
        });
        for (var i = 0; i < array.length; ++i) {

            var tag = {
                "id": array[i].id,
                "text": array[i].name+"("+array[i].amount+")",
                "name": array[i].name,
                "createType":array[i].type,
                "cycle": array[i].cycle
            }
            $(id).tagsinput('add', tag);
        }
        $(tagListId).find(".cancel").trigger("click");
    })
}


//实施位置和上网行为
tagsOperate.prototype.listTagsinputEvent = function(tagListId, id) { //tagListId是场景事件子面板的id，id是添加tag标签的input
    //$(tagListId + " .data-list>label").unbind("click");
    var numId,chosedNumId;
    chosedNumId = id == "#chosedApp"?"#chosedAppNum":"#chosedSiteNum";

    $(tagListId).on("click"," .data-list label",function(e) {
        e.preventDefault();
        e.stopPropagation();
        var tag = {};
        if( tagListId == "#realTimeSite" ){
            //htmlEvents.singleCheckbox.bind(this)();//实时位置，单选
            temp1.bind(this)(); //实时位置，多选
            tag = {
                "id":  $(this).prev().val(),
                "text": $.trim($(this).text()),
            }
        }else{
            temp1.bind(this)();
            tag = {
                "id":  $(this).prev().attr("data-bigclass")+"-"+$(this).prev().val(),
                "text": $.trim($(this).text())
            }
        }

        if ($(this).hasClass("selected")) {
            // if( tagListId == "#realTimeSite" ){  //实时位置只能选一个
            //     $(id).tagsinput('removeAll');
            // }
            $(id).tagsinput('add', tag);
        } else {
            $(id).tagsinput('remove', tag);
        }
        $(chosedNumId).text($(id).tagsinput('items').length);
    });

    // 清空已选标签
    $(tagListId + " .remove-all").click(function(e) {
        e.preventDefault();
        var tabId = $(this).attr("data-bindtab");
        $(id).tagsinput('removeAll');
        $(chosedNumId).text(0);
        //$(this).parent().siblings(".slimScrollDiv").find(".data-list").find(".selected").removeClass("selected");
        //$(this).parent().siblings(".slimScrollDiv").find(".data-list").find("input[checked]").removeAttr("checked");
        $(this).parent().siblings(".data-list").find("label").removeClass("selected");
        $(this).parent().siblings(".data-list").find("label>span").removeClass("selected");
        $(this).parent().siblings(".data-list").find("input[checked]").removeAttr("checked");
    })

    //全选标签
    $(tagListId + " .chooseAll").click(function(e) {
        e.preventDefault();
        var tabId = $(this).attr("data-bindtab");
        //var type = $(tagListId).find("select").val();
        $(this).parent().siblings(".data-list").find("label").addClass("selected");
        $(this).parent().siblings(".data-list").find("label>span").addClass("selected");
        $(this).parent().siblings(".data-list").find("input").attr("checked", "checked");

        var labelArr = $("#" + tabId).find("label");
        for (var i = 0; i < labelArr.length; i++) {
            var tag = {};
            if( tagListId == "#realTimeSite" ){
                tag = {
                    "id": $(labelArr[i]).prev().val(),
                    "text": $.trim($(labelArr[i]).text())
                }
            }else{
                tag = {
                    "id": $(labelArr[i]).prev().attr("data-bigclass")+"-"+$(labelArr[i]).prev().val(),
                    "text": $.trim($(labelArr[i]).text())
                }
            }

            $(id).tagsinput('add', tag);
        }
        $(chosedNumId).text($(id).tagsinput('items').length);
    });
    $(id).on('itemRemoved', function(event) {
        var tags = $(id).tagsinput('items');
        $(chosedNumId).text(tags.length);
        temp2(event.item.text);
    })
}
//推荐业务弹框，实现tagsinout
tagsOperate.prototype.tableTagsinputEvent = function(gid, pid, tid) { //gid是弹出框所在的父级元素ID，pid是table所在的tab内容框的id,tid是tagsinput的input测id,事件代理
    if (gid == "#chooseBuss") {
        var type;
        if (pid == "#fee") {
            type = 0;
        } else if (pid == "#brodcast") {
            type = 2;
        } else {
            type = 1;
        }
    }
    $(gid).on('click', pid + ' .ensure', function () {
        var $table = $(pid).find("table");
        var checked = $table.find("tr td:first-child input[checked]");
        var tagsArr = [];

        if( pid == "#brodcast"){   //宣传只会传一个值，所以每次都会清空
            shouldRemoveAll = true;
        }
        if( pid == "#vicAct"){  //营销活动，只能传相同大类下的小类，大类不同，清空
            var nowBigClass = $('#recommand .tab-nav [data-href="vicAct"]').data("bigclassId");
            if( nowBigClass ){
                if( lastBigClass && lastBigClass != nowBigClass){
                    shouldRemoveAll = true;
                }
                lastBigClass = nowBigClass
            }
        }
        if (isTabChanged || shouldRemoveAll) {
            $("#chosedBussiness").tagsinput("removeAll");
            isTabChanged = false;
            shouldRemoveAll = false;
        }

        if (gid == "#chooseBuss") {
            if( checked.length ){
                for (var i = 0; i < checked.length; ++i){
                    var item = checked[i];
                    var temp = $(item).closest("td");
                    //var id = temp.siblings("[data-id]").attr("data-id") + "_" + temp.siblings("[data-name]").attr("data-name");
                    var id = temp.siblings("[data-id]").attr("data-id");
                    var name = $(item).closest("td").siblings("[data-name]").attr("data-name");
                    var tag = {
                        "id": id,
                        "text": name,
                    };

                    if ($(item).closest('tr').hasClass('big-class')){  //营销活动，大类下有效类，传小类;如果大类下无小类，则传大类
                        if( checked.length != 1 ){
                            continue;
                        }
                    }

                    $(tid).tagsinput('add', tag);
                }
            }
        } else {
            $.each(checked, function(i, item) {
                var id = $(item).closest("td").siblings("[data-id]").attr("data-id");
                var name = $(item).closest("td").siblings("[data-name]").attr("data-name");
                var tag = {
                    "id": id,
                    "text": name
                };
                $(tid).tagsinput('add', tag);
            })
        }
        $(gid).find(".cancel").trigger("click");
        // isNowPageDataAdded = true;
    });
}
//有文件上传的弹出框(用户群-自定义导入),实现tagsinput
tagsOperate.prototype.fileTagsinputEvent = function(gid, pid, tid,fid) { //gid是弹出框所在父级元素id,用于事件代理;pid是file所在tab内容框的id;tid是tagsinput的id,fid为文件上传域的id
    var type;
    if(pid == "#standardMode"){
        type = 1;
    }else if(pid == "#customMode"){
        type = 2;
    }
    $(gid).on('click', pid + " .ensure", function() {
        //获取上传文件信息并上传，返回信息添加到tagsinput中
        // $(gid).find(".cancel").trigger("click");

        $(".modal-wrap-upload").show();
        $.ajaxFileUpload({
            url: "./files/upload",
            fileElementId: fid,
            secureuri: false,
            dataType:'json',
            cache: false,
            data: {
                useType: type
            },
            type: 'post',
            async: false,
            success: function(res, status) {
                if (res.code == 0) {
                    var tag = {};
                    //tag["id"] = res.data.customerGroupId+"_"+res.data.customerGroupName+"_3_"+res.data.cocGroupCycle;  //3.自定义导入
                    tag["id"] = res.data.customerGroupId;
                    tag["text"] = res.data.customerGroupName + "(" + res.data.amount + ")";
                    tag["name"] = res.data.customerGroupName;
                    tag["createType"] = "3";
                    tag["cycle"] = res.data.cocGroupCycle;

                    //判断已选用户中，是否有从数字内容中选择的，如果有，则先清空，再插值????待测试
                    isRemoveAll("#customInputWrap");
                    if (shouldRemoveAll_l) {
                        $(tid).tagsinput("removeAll");
                        shouldRemoveAll_l = false;
                    }

                    $(tid).tagsinput('add', tag);

                    showMessage("上传文件手机号码总数："+
                        res.data.fileLineCount +"，其中有效数量："+res.data.amount, "success");
                }else if (res.code == 1) {
                    showMessage("登陆超时,请重新登录!",'error');
                } else {
                    showMessage("上传文件存在错误："+res.msg, "error", 4000);
                }
                $(".modal-wrap-upload").hide();
                $(gid).find(".cancel").trigger("click");
                $(".close.fileinput-exists").click();
                $(pid+" .fileUpLoad")[0].outerHTML=$(pid+" .fileUpLoad")[0].outerHTML;
                $("#"+fid).attr("name", "file");//执行上一步之后，name属性会丢失，需手动添加
            },
            error: function(data) {
                showMessage("上传文件失败！", "error");
                $(".close.fileinput-exists").click();
                $(pid+" .fileUpLoad")[0].outerHTML=$(pid+" .fileUpLoad")[0].outerHTML;
                $("#"+fid).attr("name", "file");
                $(gid).find(".cancel").trigger("click");
                $(".modal-wrap-upload").hide();
            }
        });
    })
}


//下面用于图片上传预览功能
function setImagePreview(fid,pid,lid) {  //fid是input上传域的id,pid是img元素的id,lid是img元素外层div的id
    //console.log(fid+":"+pid+":"+lid)
    var docObj=document.getElementById(fid);

    var imgObjPreview=document.getElementById(pid);
    if(docObj.files &&docObj.files[0]){
        //火狐下，直接设img属性
        imgObjPreview.style.display = 'block';
        imgObjPreview.style.width = '130px';
        imgObjPreview.style.height = '160px';
        //imgObjPreview.src = docObj.files[0].getAsDataURL();

        //火狐7以上版本不能用上面的getAsDataURL()方式获取，需要一下方式
        imgObjPreview.src = window.URL.createObjectURL(docObj.files[0]);
    }else{
        //IE下，使用滤镜
        docObj.select();
        var imgSrc = document.selection.createRange().text;
        var localImagId = document.getElementById(lid);
        //必须设置初始大小
        localImagId.style.width = "130px";
        localImagId.style.height = "160px";
        //图片异常的捕捉，防止用户修改后缀来伪造图片
        try{
            localImagId.style.filter="progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)";
            localImagId.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = imgSrc;
        }catch(e){
            alert("您上传的图片格式不正确，请重新选择!");
            return false;
        }
        imgObjPreview.style.display = 'none';
        document.selection.empty();
    }
    return true;
}


//追尾短信模式
$(function(){
    $("#channelListBox").on("click","label[for='q09']",function(e){
        e.preventDefault();
        if(!$("#select_zhuiwei_no").attr("data-isCreated")){
            $.ajax({
                url: "./base/getRearEndType",
                type: "get",
                dataType: "json",
                cache:false,
                success:function(res){
                    var html = "";
                    if(res.code != 0){
                        showMessage(res.msg,"error");
                        return
                    }
                    var data = res.data.rearEndType;
                    $.each(data,function(i,item){
                        html += '<option value="'+item.REMIND+'">'+item.REMINDNAME+'</option>';
                    })
                    $("#select_zhuiwei_no").html(html);

                    $("#select_zhuiwei_no").attr("data-isCreated","created");
                },
                error:function(){
                    showMessage("获取信息失败","error");
                }
            })
        }
    });
})
//start-掌厅
$(function(){
    var transToSelect2Json = function(data){ //转换成Selec2默认jso格式[{id: 0, text: 'enhancement'}]
        var list = [];
        $.each(data,function(i,item){
            var newItem = {};
            newItem.id = item.C_CODE;
            newItem.text = item.C_NAME+":"+item.C_CODE;
            list.push(newItem);
        })
        return list;
    }
    //请求掌厅下拉列表
    $("#channelListBox").on("click","label[for='q03']",function(e){
        e.preventDefault();
        var $childSpan = $(this).find("span");
        if($childSpan.hasClass("selected")){
            return;
        }
        if( $('#advZT').attr("data-isCreated") == "created"  ){
            return
        }else{
            $.ajax({
                url: "./base/getZTDropInfo",
                type: "get",
                dataType: "json",
                cache: false,
                success: function(res){
                    if(res.code == 0){
                        //填充掌厅下拉列表
                        var data = res.data.adPositionList;
                        var adsense = {};
                        var adsenseId = "";
                        var codeData = [];
                        codeData.push({"id":"-1","text":"请选择"});
                        codeData = codeData.concat(transToSelect2Json(res.data.codeList));
                        //	广告位下拉列表,默认活动专区1对应的图片
                        $('#advZT').html("");
                        $("#picDetail").html("");
                        for(var i=0;i<data.length;i++){
                            adsense[data[i].adsenseId] = data[i].imgInfoList;

                            if(data[i].status == 1){  //status == 1,表示要显示
                                $('#advZT').append(
                                    "<option value="+data[i].adsenseId+">"+data[i].adsenseName+"</option>"
                                );
                            }
                            if(data[i].adsenseId == "loc_3"){
                                var imgs = data[i].imgInfoList;
                                $.each(imgs,function(i,img){
                                    $('#picDetail').append(
                                        "<div class='sub-row'>"+
                                        "<span data-id="+img.imgId+">"+
                                        img.imgDesc+
                                        "<span class='imgSize'>"+img.imgSize+"</span>"+
                                        "</span>"+
                                        "<a href='javascript:;' class='a-upload'>"+
                                        "<input data-tagname='input_area' type='file'  data-ruleid='R005'  name='file' data-id='"+img.imgId+"' id='"+img.imgId+"'  onchange='imgChange(this)' accept='image/jpeg,image/gif,image/png'>"+
                                        "<span class='upload-btn'>点击这里上传图片</span>"+
                                        "</a>"+
                                        "</div>"
                                    );
                                })
                            }
                        }
                        /*$("#lkCodeZT").html("");
						 for(var i=0;i<codeData.length;i++){
						 $("#lkCodeZT").append(
						 "<option value="+codeData[i].C_CODE+">"+codeData[i].C_NAME+" : "+codeData[i].C_CODE+"</option>"
						 );
						 }*/
                        $("#lkCodeZT").select2({
                            data: codeData,
                            language: "zh-CN"
                        });
                        $('#advZT').attr("data-isCreated","created");
                        advChange(adsense);
                    }else{
                        showMessage(res.msg,"error");
                    }
                },
                error:function(){
                    showMessage("获取掌厅相关信息失败","error");
                }
            })
        }

    })
    /*广告位变化，请求对应的图片信息*/
    function advChange(data){
        $("#advZT").change(function(){
            var param = $("#advZT option:selected").val();
            //console.log(param)
            $('#picDetail').html("");
            for(var k in data){
                if(k == param){
                    var img = data[k];
                    for(var i=0;i<img.length;i++){
                        $('#picDetail').append(
                            "<div class='sub-row'>"+
                            "<span data-id="+img[i].imgId+">"+
                            img[i].imgDesc+
                            "<span class='imgSize'>"+img[i].imgSize+"</span>"+
                            "</span>"+
                            "<a href='javascript:;' class='a-upload'>"+
                            "<input data-tagname='input_area' type='file' data-ruleid='R005' name='file' data-id='"+img[i].imgId+"' id='"+img[i].imgId+"' onchange='imgChange(this)' accept='image/jpeg,image/gif,image/png'>"+
                            "<span class='upload-btn'>点击这里上传图片</span>"+
                            "</a>"+
                            "</div>"
                        );
                    }
                }
            }
        })
    };
    /*$("#picDetail").on("mouseover","input:file",function(e){
		e.preventDefault();
		e.stopPropagation();
		var id = $(this).attr("id");
		if($(this).val()){
			//setImagePreview(id,"preview","localImage");
		}
	})*/
})

//互联网基地渠道，根据选择的运营位，限制日期选择
$(function(){
    var posTypeName = {
        "1":"横幅",
        "3":"弹窗",
        "4":"全屏",
        "5":"广告条",
        "7":"文字链广告",
        "8":"图文广告"
    }
    globalAttrs.posTypeName = posTypeName;

    $("#channelListBox").on("click","label[for='q07']",function(){
        var $childSpan = $(this).find("span");
        if($childSpan.hasClass("selected")){
            $childSpan.closest(".col-1").siblings(".col-1").removeClass("hide");
            //解除绑定
            $("#startTime,#endTime").unbind("focus");
            $("#startTime").bind("focus",htmlEvents.cycleControlDate);
            $("#endTime").bind("focus",htmlEvents.cycleCtrEndDate);
            return;
        }
        $childSpan.closest(".col-1").siblings(".col-1").addClass("hide");
        $("#channelListBox .col-1.hide").find("label>span.selected").trigger("click");
        $.ajax({
            url: "./base/getIopDropInfo",
            type: "get",
            dataType: "json",
            cache: false,
            success: function(res){
                if(res.code == 0){
                    var data = res.data.iopList;
                    var info = {};
                    var $select = $("#iop_show_select1");
                    $select.html("");
                    $.each(data,function(i,item){
                        var typeSize = {};
                        $select.append(
                            "<option value="+data[i].positionid+" data-isuse='"+data[i].isuse+"'>"+data[i].positionname+"</option>"
                        );
                        // if(data[i].positionid == "0100522171"){//默认展示 “web发件成功页右侧banner广告B”
                        // 	var postypeId = data[i].positiontype;
                        // 	$("#iop_show_select2").attr("data-postypeid",postypeId);
                        // 	$("#iop_show_select2").val(posTypeName[postypeId]);
                        // 	$("#hlwImgSize").text(data[i].positionsize);
                        //    controlDatePick(data[i].scheendtime,data[i].schestarttime);
                        // }

                        typeSize["positiontype"] = data[i].positiontype;
                        typeSize["positionsize"] = data[i].positionsize;
                        typeSize["scheendtime"] = data[i].scheendtime;
                        typeSize["schestarttime"] = data[i].schestarttime;
                        info[data[i].positionid] = typeSize;
                    })
                    positionChange(info);
                    $select.trigger("change");
                }else{
                    showMessage(res.msg,"error");
                }
            },
            error:function(){
                showMessage("获取互联网渠道相关信息失败","error");
            }
        })
    });
    /*运营位名称变化，1.运营位类型和图片改变；2.限制日期的选择； 3、运营位是否可用*/
    function positionChange(data){
        $("#iop_show_select1").change(function(){
            var $selOpt = $("#iop_show_select1 option:selected");
            var pid = $selOpt.val();
            var isuse = $selOpt.attr("data-isuse");
            for(var k in data){
                if(k == pid){
                    var postypeId = data[k].positiontype;
                    $("#iop_show_select2").attr("data-postypeid",postypeId);
                    $("#iop_show_select2").val(posTypeName[postypeId]);
                    $("#hlwImgSize").text(data[k].positionsize);
                    if( postypeId == "7"){
                        $(".j-hlwImg").addClass("hide");
                        $("#hlwImg").removeAttr("data-ruleid");
                    }else{
                        if( $(".j-hlwImg").hasClass("hide")){
                            $(".j-hlwImg").removeClass("hide");
                        }
                        $("#hlwImg").attr("data-ruleid","R005");
                    }
                    controlDatePick(data[k].scheendtime,data[k].schestarttime);
                }
            }

            var $channelTab = $("[data-channelid='q07']");
            if( isuse == 0 ){
                $channelTab.find("[data-ruleid]:not('#iop_show_select1')").attr("disabled",true);
                $channelTab.find(".u-hint-msg").removeClass("hidden");
            }else{
                $channelTab.find("[data-ruleid]:not('#iop_show_select1')").attr("disabled",false);
                $channelTab.find(".u-hint-msg").addClass("hidden");
            }
        });
    }
    function controlDatePick(eDate,sDate) {  //限制日期选择
        var end = "";
        var start = "";
        var attchMap = {};
        var today = new Date();
        $("#startTime,#endTime").val("");
        eDate = eDate.substring(0,8);
        sDate = sDate.substring(0,8);
        today = formatDate(today).split("-").join("");
        end = eDate.replace(/(\d{4})(\d{2})(\d{2})/mg,'$1-$2-$3');
        start = sDate.replace(/(\d{4})(\d{2})(\d{2})/mg,'$1-$2-$3');
        attchMap.sDate = sDate;
        attchMap.today = today;
        attchMap.start = start;
        attchMap.end = end;
        $("#startTime,#endTime").unbind("focus");
        $("#startTime").bind("focus",attchMap,startDateControl);
        $("#endTime").bind("focus",attchMap,endDateControl);
    }
    function startDateControl(event){
        if(event.data.today>event.data.sDate){//如果起始时间在今天之前，则从今天开始；如果时间起始时间在今天之后，则从起始时间开始；
            WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d',onpicked:function(){return;}});
        }else{
            WdatePicker({dateFmt:'yyyy-MM-dd',minDate:event.data.start,onpicked:function(){return;}});
        }
    }
    function endDateControl(event){
        WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:event.data.end,onpicked:function(){return;}});
    }
})

function upLoadFile(file,id,imgname) {
//function upLoadFile(isAllow,file,id,imgname) {
    //图片符合要求，上传图片
    var fileInput = $(file)[0];
    var id = $(fileInput).attr("id");
    var dataId = $(fileInput).attr("data-id");
    //if(isAllow){
    var campId=$("#activityNumber").text();//编号
    var img_name;
    if(!fileInput.files){
        //img_name = $(id).data("imgName");   //????取不到值
        img_name = imgname;
    }else{
        var imgInfo = fileInput.files[0];
        img_name=imgInfo.name;
    }

    $(".modal-wrap-upload").show();
    $.ajaxFileUpload({
        url: "./files/upload",
        fileElementId: id,
        dataType: 'json',
        async: false,
        type:'post',
        data: {
            useType: 4
        },
        success:function(data){
            var fileInput = $("#"+id)[0];
            if(data.code == 0){
                $(".modal-wrap-upload").hide();
                showMessage("图片上传成功","success");
                //fileInput.outerHTML = fileInput.outerHTML;
                $('#'+id).data('ruleValue',data.data); // 保存ruleValue
                $("#"+dataId).siblings(".upload-btn").html(img_name);   //显示图片名
                /*var $imgbox = $("#"+id).closest(".row").find(".img-preview-box");
				var imgId = $imgbox.find("img").attr("id");
				var imgboxId= $imgbox.attr("id");		*/
                //setImagePreview(id,imgId,imgboxId);
            } else{
                $(".modal-wrap-upload").hide();
                showMessage(res.msg,"error");
                fileInput.outerHTML = fileInput.outerHTML;
            }
        },
        error:function(){
            var fileInput = $("#"+id)[0];
            /*var $imgbox = $("#"+id).closest(".row").find(".img-preview-box");
			var imgId = $imgbox.find("img").attr("id");
			var imgboxId= $imgbox.attr("id");
			setImagePreview(id,imgId,imgboxId);*/
            fileInput.outerHTML = fileInput.outerHTML;
            $(".modal-wrap-upload").hide();
            showMessage("图片上传失败","error");
        }
    });
    //}else{
    // showMessage("图片尺寸不符合要求","error");
    //}
}
function testWidthHeight(_file,dftw,dfth, id){
    var file = $(_file)[0];

    var isAllow = false;
    if(file.files && file.files[0]){
        var fileData = file.files[0];

        //读取图片数据
        var reader = new FileReader();
        reader.onload = function (e) {
            var data = e.target.result;
            //加载图片获取图片真实宽度和高度
            var image = new Image();
            image.onload=function(){
                var width = image.width;
                var height = image.height;
                isAllow = width == dftw && height == dfth;
                if(isAllow){
                    upLoadFile(_file,id);
                }else{
                    showMessage("图片尺寸不符合要求","error")
                }
                //upLoadFile(isAllow,_file,id);
            };
            image.src= data;
        };
        reader.readAsDataURL(fileData);
    }else{    //针对ie9及以下版本
        //IE下使用滤镜来处理图片尺寸控制
        //文件name中IE下是完整的图片本地路径
        try{
            var file2 = _file;
            file2.select();
            file2.blur();
            var path = document.selection.createRange().text;
            var img = $('<img style="filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=image);width:300px;visibility:hidden;"  />').appendTo('body').get(0);
            img.filters.item('DXImageTransform.Microsoft.AlphaImageLoader').src = path;
            var width = img.offsetWidth;
            var height = img.offsetHeight;
            var pathArr = path.split("\\");
            var imgName = pathArr[pathArr.length-1];
            //$(id).data("imgName",imgName);   //?????图片名
            $(img).remove();
            isAllow = width == dftw && height == dfth;
            //upLoadFile(isAllow,_file,id,imgName);
            if(isAllow){
                upLoadFile(_file,id,imgName);
            }else{
                showMessage("图片尺寸不符合要求","error")
            }
        }catch(e){
            alert(e)
        }
    }
}

function imgChange(file,id){
    var img_name =  $(file).val();
    var imgNameArr = img_name.split("\\");
    var imRealName = imgNameArr[imgNameArr.length-1];
    var img_format=img_name.substring(img_name.lastIndexOf("."));
    //判断上传文件是否是以下格式的图片
    if ((img_format != ".jpg") && (img_format != ".gif") &&(img_format != ".jpeg") && (img_format != ".png") ) {
        showMessage("请上传图片","error");
        $(file)[0].focus();
        //清空file里面的值www.3ppt.com
        $(file)[0].select();
        document.selection.clear();
    }else{   //判断图片宽高是否符合要求
        var size = $(file).parents('.sub-row').find('.imgSize').html();
        if($.trim(size) != "无"){
            var defaultW = size.split('*')[0];
            var defaultH = size.split('*')[1];
            testWidthHeight(file,defaultW,defaultH,id);
        }else{  //如果没有图片尺寸，则不作图片宽高校验，都可上传
            upLoadFile(file,id,imRealName);
        }
    }
}

function fileChange(el,type){
    var id = $(el).attr("id");
    $(".modal-wrap-upload").show();
    $.ajaxFileUpload({
        url: "./files/upload",
        fileElementId: id,
        dataType: 'json',
        async: false,
        type: 'post',
        data: {
            useType: type
        },
        success: function (data) {
            var fileInput = $("#" + id)[0];
            if (data.code == 0) {
                $(".modal-wrap-upload").hide();
                showMessage("上传成功", "success");
                $('#' + id).data('ruleValue', data.data); // 保存ruleValue
                $("#" + id).siblings(".upload-file-btn").html(data.data);   //显示图片名
            } else {
                $(".modal-wrap-upload").hide();
                showMessage(res.msg, "error");
                fileInput.outerHTML = fileInput.outerHTML;
            }
        },
        error: function () {
            var fileInput = $("#" + id)[0];
            fileInput.outerHTML = fileInput.outerHTML;
            $(".modal-wrap-upload").hide();
            showMessage("上传失败", "error");
        }
    });
}
//兼容ie8，ie9，ie9以上也支持,？？？待测试
/*function getFileSize(obj,dftw,dfth){
  try{
    var file = obj;
    file.select();
    file.blur();
    var path = document.selection.createRange().text;
    var fso = new ActiveXObject("Scripting.FileSystemObject");
    fileSize = fso.GetFile(path).size;
    alert(fileSize);//弹出文件大小
    var img = $('<img style="filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=image);width:300px;visibility:hidden;"  />').appendTo('body').get(0);
    img.filters.item('DXImageTransform.Microsoft.AlphaImageLoader').src = path;
    var width = img.offsetWidth;
    var height = img.offsetHeight;
    $(img).remove();
    isAllow = width == dftw && height == dfth;
    upLoadFile(isAllow,obj,id);
  }catch(e){
      //alert(e+"\n"+"如果错误为：Error:Automation 服务器不能创建对象；"+"\n"+"请按以下方法配置浏览器："+"\n"+"请打开【Internet选项-安全-Internet-自定义级别-ActiveX控件和插件-对未标记为可安全执行脚本的ActiveX控件初始化并执行脚本（不安全）-点击启用-确定】");
	  alert(e)
	  return window.location.reload();

  }
}*/
//end-掌厅

//判断周期性，控制日期选择   //2018/1/26修改规则：非实时&&优惠提醒，只能选择一天
$(function(){
    var sceneFlag = true;
    var channel = false;
    var usergroup = false;   //非标签库导入的一次性标签
    var isLabel = false;   //非标签导入
    var isLableDay = false; //非标签导入的日周期标签
    var isUpdateOneTime = false;  //含标签导入的日周期标签，活动周期是否更新为一次性
    var userTags = 0;
    var isIntelligentMap = false;  //未选中智能地图
    var isRealtime = true;   //选中实时
    var lastScene = 0;  //默认选中非实时

    htmlEvents.resetParams = function() {
        sceneFlag = true;
        channel = false;
        usergroup = false;
        isLabel = false;
        isLableDay = false;
        isUpdateOneTime = false;
        userTags = 0;
        isIntelligentMap = false;
        isRealtime = true;
        lastScene = 0;
    }
    //监听非实时单选框     ？？？？和智能地图
    $("#actTypes").on("click"," li>label",function(e){
        var $li = $(this).parent();
        var radioVal = $(this).prev().val();
        if( lastScene == radioVal ){
            return
        }
        lastScene = radioVal;
        channel = false;
        //channel = false;  //渠道根据场景变化，每次切换场景，重置渠道选项
        if(radioVal == 0){  //选择了非实时
            sceneFlag = true;
            clearWdate();
        } else{
            sceneFlag = false;
            // if( radioVal == 11 ){ //智能地图
            // 	isIntelligentMap = true;
            // 	clearWdate();
            // }else{
            // 	isIntelligentMap = false;
            // }
        }
    });
    //监听智能地图是否选择了实时
    // $(".intelligent-map li a").click(function(){
    // 	if($(this).prev().val() == 0){
    // 		isRealtime = false;
    // 	}else{
    // 		isRealtime = true;
    // 		clearWdate();
    // 	}
    // });
    //监听优惠提醒、10086主动挂尾;  1. 与其他渠道互斥判断; 2. 周期性判断
    $("#channelListBox").on("click","label[for='d03'],label[for='d01']",function(e){
        e.preventDefault();
        $childSpan = $(this).find("span");
        if($childSpan.hasClass("selected")){
            if( $(this).attr("for") == "d01" ) {
                channel = false;
            }
            if($childSpan.closest(".col-1").siblings(".col-1.conflict").find("span").hasClass("selected")){
                return;
            }else{
                $childSpan.closest(".col-1").siblings(".col-1:not(.conflict)").removeClass("hide");
            }
        }else{
            $childSpan.closest(".col-1").siblings(".col-1:not(.conflict)").addClass("hide");
            $("#channelListBox .col-1.hide").find("label>span.selected").trigger("click");

            //只有优惠提醒渠道判断周期
            if( $(this).attr("for") == "d01" ){
                channel = true;
                clearWdate();
            }
        }

    });
    //监听目标用户选择;
    /*	$("#selectedUser").on("itemAdded",function(event){
			var isOnlyZt = false;
			var channels = $("#item-tab").find("a");
			if( channels.length == 0 || channels.length > 1){
				isOnlyZt = false;
			}else{
				isOnlyZt = $(channels[0]).attr("data-channelid") == "q03"?true:false;
			}

			// 1. 总人数超过150万，提醒不予保存？？？？？暂时隐藏;
			// var txt = event.item.text;
			// var amount = parseInt(txt.split("(")[1].split(")")[0])?parseInt(txt.split("(")[1].split(")")[0]):0;
			// userTags += amount;
			// if(userTags > 1500000){
			// 	$(this).addClass("j-over-number");
			// 	if( !isOnlyZt ) {   //如果只选择了一个渠道，且为掌厅，则不限制用户群数量；不提醒
			// 		showMessage("目标用户数超过150万", "error");
			// 	}
			// }


			// 2. 从标签库导入的用户群，周期为一次性的，或周期为日周期（可更新周期） 判断日期
			var itemArrs = event.item;
			if(itemArrs.createType == 1 && itemArrs.cycle == 3){  //标签导入的日周期标签
				isLableDay = true;
				//可选择周期
				var $cycleBox = $(".j-update-cycle");
				if($cycleBox.hasClass("hide")){
					$cycleBox.removeClass("hide");
					usergroup = false;
				}
			}
			if(itemArrs.createType == 1 && itemArrs.cycle == 1 ){ //标签导入的一次性标签  //createType(arr[2]): 1.标签导入; cycle(arr[3]) : 1.一次性; 2.月周期; 3.日周期;
				if( !isLableDay ){
					usergroup = true;
					clearWdate();
				}else{
					if( isUpdateOneTime ){
						usergroup = true;
						clearWdate();
					}else{
						usergroup = false;
					}
				}

			};

			if(itemArrs.createType == 1){  //标签库导入
				isLabel = true;
			}
		});
		$("#selectedUser").on('itemRemoved', function(event) {
			var isOnlyZt = false;
			var channels = $("#item-tab").find("a");
			if( channels.length == 0 || channels.length > 1){
				isOnlyZt = false;
			}else{
				isOnlyZt = $(channels[0]).attr("data-channelid") == "q03"?true:false;
			}

			// 1.判断目标用户数
			// var txt = event.item.text;
			// var amount = parseInt(txt.split("(")[1].split(")")[0])?parseInt(txt.split("(")[1].split(")")[0]):0;
			// userTags -= amount;
			// if(userTags > 1500000){
			// 	if( !isOnlyZt ) { //如果只选择了一个渠道，且为掌厅，则不限制用户群数量
			// 		showMessage("目标用户数超过150万", "error");
			// 	}
			// }else{
			// 	$(this).removeClass("j-over-number");
			// }


			// 2.判断周期性
			var tagArr = $("#selectedUser").tagsinput("items");
			isLableDay = false;
			usergroup = false;
			isLabel = false;
			$.each(tagArr,function(i,tag){
				clearWdate();
				if(tag.createType == 1 && tag.cycle == 3){  //标签导入的日周期标签
					isLableDay = true;
				}
				if(tag.createType == 1 &&  tag.cycle == 1){  //标签导入的一次性标签
					usergroup = true;
				}
				if(tag.createType == 1){
					isLabel = true;
				}
			});
			if( !isLableDay ){
				var $cycleBox = $(".j-update-cycle");
				if(!$cycleBox.hasClass("hide")){
					$cycleBox.addClass("hide");
					$(".j-update-cycle>p").eq(0).addClass("selected").siblings(".selected").removeClass("selected").find("input").removeAttr("checked");
					$(".j-update-cycle>p").eq(0).find("input").attr("checked","checked");
					isUpdateOneTime = false;
				}
			}else{
				if( isUpdateOneTime ){
					usergroup = true;
					clearWdate();
				}else{
					usergroup = false;
				}
			}
		});
		//监听更新客户群单选框
		$(".j-update-cycle>p").click(function(e){
			e.preventDefault();
			var cycle = $(".j-update-cycle>p.selected").find("input").val();
			if(cycle != 1){    //1:一次性；2：月周期；3：日周期
				isUpdateOneTime = false;
				usergroup = false;
		}else{
			clearWdate();
			isUpdateOneTime = true;
			usergroup = true;
		}
	});
*/

    var cycleCtrDate = function(){  //选择非实时 && ( 目标用户是从标签库导入的且标签周期为一次性 || 渠道选择了优惠提醒、主动挂尾 )；满足条件，起止日期只能为同一天
        /*
         * 判断是周期性还是一次性
         * 一次性，起始时间只能选择同一天
         */
        // console.log(sceneFlag);
        // console.log(channel);
        // console.log(usergroup);
        // console.log(isLabel);
        /* if(sceneFlag && (channel || usergroup)){
			if( channel && (!usergroup) ){
				if(isLabel){  //标签导入，但为周期性标签，时间可连续选择
					WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d',maxDate:'#F{$dp.$D(\'endTime\')}',onpicked:function(){return;}});
				}else{
					WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d',onpicked:datePicked.bind(this)});
				}
			}else{  //只能选择一天
				WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d',onpicked:datePicked.bind(this)});
			}
        }else{
			if( isIntelligentMap && isRealtime ){  //智能地图-实时，只能选择一天
				WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d',onpicked:datePicked.bind(this)});
			}else{
				WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d',maxDate:'#F{$dp.$D(\'endTime\')}',onpicked:function(){return;}});

			}

            //WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d',maxDate:'#F{$dp.$D(\'endTime\')}',onpicked:function(){return;}});
        }*/

        //修改后：非实时，优惠提醒，只能选择一天
        if( sceneFlag && channel ){
            WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d',onpicked:datePicked.bind(this)});
        }else{
            WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d',maxDate:'#F{$dp.$D(\'endTime\')}',onpicked:function(){return;}});
        }

    };
    var cycleCtrEndDate = function(){
        /*if(sceneFlag && (channel || usergroup)){
			if( channel && (!usergroup) ) {
				if(isLabel) {  //标签导入，但为周期性标签，时间可连续选择
					WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}',onpicked:function(){return;}});
				}else{
					WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d',onpicked:datePicked.bind(this)});
				}

			}else{  //只能选择一天
				WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d',onpicked:datePicked.bind(this)});
			}
		}else{
			if( isIntelligentMap && isRealtime ){  //智能地图-实时，只能选择一天
				WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d',onpicked:datePicked.bind(this)});
			}else{
				WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}',onpicked:function(){return;}});
			}
			//WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}',onpicked:function(){return;}});
		}*/


        //修改后：非实时，优惠提醒，只能选择一天
        if( sceneFlag && channel ){
            WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d',onpicked:datePicked.bind(this)});
        }else{
            WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}',onpicked:function(){return;}});
        }
    }
    var clearWdate = function(){
        $("#startTime").val("");
        $("#endTime").val("");
    };
    var datePicked = function(dateObj){
        var $other = $(dateObj.el).parents(".col-1").siblings().find("input");
        $other.val($(dateObj.el).val());
    }
    $("#startTime").bind("focus", cycleCtrDate );
    $("#endTime").bind("focus",cycleCtrEndDate);
    htmlEvents.cycleControlDate = cycleCtrDate;
    htmlEvents.cycleCtrEndDate = cycleCtrEndDate;
})
//start活动审批模态框
var initApproveModal = function() {
    this.init = function() {
        $("#actApproveNav li").click(function(e) {
            e.preventDefault();
            $(this).addClass("selected").siblings(".selected").removeClass("selected");
            var tabId = $(this).find("a").attr("href");
            $(tabId).addClass("selected").siblings(".selected").removeClass("selected");
        })
        $("[data-id='approveTab']").parent().click(function() { //获取要审批的渠道信息
            //清空详情信息
            htmlEvents.clearDetails("#detailTab");
            var actId = $("#curActivityId").val();
            $.ajax({
                url: "./activities/getCanApprovalInfo?activityId=" + actId,
                type: 'get',
                dataType: 'json',
                cache: false,
                success: function(res) {
                    if (res.code == 0) {
                        var data = res.data.channelList;
                        var html = "";
                        var hasWAIHU = false;
                        for (var i = 0; i < data.length; i++) {
                            html += '<div class="row">';
                            html += '<div class="col-3">';
                            html += '<span class="item-property h-block is-required" data-id="' + data[i].CHANNEL_ID + '">' + data[i].CHANNEL_NAME + '</span>';
                            html += '</div>';
                            html += '<div class="col-3">';
                            html += '<div class="radio-box clearfix">';
                            html += '<p class="selected">';
                            html += '<input class="radio-input" type="radio" name="filter" value="1" checked="checked">';
                            html += '<label><span class=""></span>通过</label>';
                            html += '</p>';
                            html += '<p>';
                            html += '<input class="radio-input" type="radio" name="filter" value="2">';
                            html += '<label><span></span>驳回</label>';
                            html += '</p>';
                            html += '</div>';
                            html += '</div>';

                            //外呼管理开始
                            if(data[i].CHANNEL_ID=='d07' && data[i].APPROVER_LEVEL==2){
                                hasWAIHU = true;

                                html += '<div class="col-3 outbound">';

                                html += '<div class="sub-row" style="margin-top: 8px">';
                                html += '<span>互斥资费代码：';
                                html += '</span>';
                                html += '<a href="javascript:;" class="a-upload">';
                                html += '<input data-tagname="input_area" type="file" name="file" data-id="hczfFile" id="hczfFile" onchange="fileChange(this,8);" accept="text/plain,.csv">';
                                html += '<span class="upload-file-btn">点击这里上传文件</span>';
                                html += '</a>';
                                html += '</div>';

                                html += '<div class="sub-row">';
                                html += '<span>互斥活动代码：';
                                html += '</span>';
                                html += '<a href="javascript:;" class="a-upload">';
                                html += '<input data-tagname="input_area" type="file" name="file" data-id="hchdFile" id="hchdFile" onchange="fileChange(this,9);" accept="text/plain,.csv">';
                                html += '<span class="upload-file-btn">点击这里上传文件</span>';
                                html += '</a>';
                                html += '</div>';

                                html += '<div class="sub-row">';
                                html += '<span>外呼脚本：';
                                html += '</span>';
                                html += '<a href="javascript:;" class="a-upload">';
                                html += '<input data-tagname="input_area" type="file" name="file" data-id="whjbFile" id="whjbFile" onchange="fileChange(this,10);" accept="text/plain,application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document">';
                                html += '<span class="upload-file-btn">点击这里上传文件</span>';
                                html += '</a>';
                                html += '</div>';

                                html += '<div class="sub-row">';
                                html += '<span data-id="yhfq" style="">用户分群：';
                                html += '<div class="checkbox j-checkbox-clear">';
                                html += '<ul class="sub-checxbox clearfix">';
                                html += '<li><input type="checkbox" data-order="0" name="套餐主资费" value="PROD_PRICE"><label class="col-mar"><span></span>套餐主资费<img src="./img/upload_pressed.png"></label></li>';
                                html += '<li><input type="checkbox" data-order="0" name="三月均ARPU" value="ARPU"><label class="col-mar"><span></span>三月均ARPU<img src="./img/upload_pressed.png"></label></li>';
                                html += '<li><input type="checkbox" data-order="0" name="三月均DOU" value="DOU"><label class="col-mar"><span></span>三月均DOU<img src="./img/upload_pressed.png"></label></li>';
                                html += '<li><input type="checkbox" data-order="0" name="流量溢出量" value="YC_FLOW"><label class="col-mar"><span></span>流量溢出量<img src="./img/upload_pressed.png"></label></li>';
                                html += '<li><input type="checkbox" data-order="0" name="流量溢出费用" value="CFEE"><label class="col-mar"><span></span>流量溢出费用<img src="./img/upload_pressed.png"></label></li>';
                                html += '<li><input type="checkbox" data-order="0" name="三月均流量ARPU" value="FLOW_ARPU"><label class="col-mar"><span></span>三月均流量ARPU<img src="./img/upload_pressed.png"></label></li>';
                                html += '<li><input type="checkbox" data-order="0" name="三月均通话分钟数" value="MOU"><label class="col-mar"><span></span>三月均通话分钟数<img src="./img/upload_pressed.png"></label></li>';
                                html += '</ul>';
                                html += '<div class="yhfq">';

                                html += '</div>';
                                html += '</div>';
                                html += '</div>';

                                html += '</div>';
                            }
                            //外呼管理结束

                            html += '<div class="col-3 hide">';
                            html += '<textarea class="h-block higher" placeholder="请填写驳回原因" class="selected"></textarea>';
                            html += '</div>';
                            html += '</div>';
                        }
                        $("#approveChannels").html(html);
                        if(hasWAIHU){
                            initApproveCheckbox();
                        }
                        initApproveRadiobox();
                    } else {
                        showMessage(res.msg,"error")
                    }
                },
                error: function() {
                    showMessage("获取审批活动信息失败","error")
                }
            })
        })

        function initApproveCheckbox() {
            var orderCol = new tagsOperate();
            orderCol.initTagsIput(".yhfq");
            $(".yhfq").on("itemRemoved", function(event) {
                var id = event.item.id;
                var $input = $(".outbound input[value="+id+"]");
                $input.removeAttr("checked");
                $input.next().find("span").removeClass("selected");
            });
            $(".yhfq").on("itemAdded", function(event) {
                var id = event.item.id;
                var $input = $(".outbound input[value="+id+"]");
                $input.attr("checked", "checked");
                $input.next().find("span").addClass("selected");
            });

            $(".outbound label.col-mar img").click(function(e) {
                e.preventDefault();
                e.stopPropagation();
                $input = $(this).parent().prev();
                var order = $input.data("order");

                if(order == "0"){
                    order = "1";
                    $input.data("order","1");
                    $(this).attr("src","./img/dowloadCs.png");
                } else {
                    order = "0";
                    $input.data("order","0");
                    $(this).attr("src","./img/upload_pressed.png");
                }

                if ($input.attr("checked") == "checked") {
                    var tag = {
                        "id": $input.val(),
                        "text": order == "1" ? $input.attr("name") + "-降序" : $input.attr("name"),
                        "order": $input.data("order")
                    };
                    var items = $(".yhfq").tagsinput('items');
                    var newItems = [];
                    for (var i = 0; i < items.length; i++) {
                        var item = items[i];
                        if(item["id"]==tag["id"]){
                            newItems.push(tag);
                        }else{
                            newItems.push(item);
                        }
                    }
                    $(".yhfq").tagsinput('removeAll');
                    for (var i = 0; i < newItems.length; i++) {
                        $(".yhfq").tagsinput('add',newItems[i]);
                    }
                }
            });

            $(".outbound label.col-mar").click(function() {
                var $input = $(this).prev();
                var order = $input.data("order");
                var tag = {
                    "id": $input.val(),
                    "text": order == "1" ? $input.attr("name") + "-降序" : $input.attr("name"),
                    "order": order
                };
                if ($input.attr("checked") == "checked") {
                    $(".yhfq").tagsinput('remove',tag);
                } else {
                    var length = $(".yhfq").tagsinput('items').length;
                    if(length>=5){
                        showMessage("排序字段不能超过5个！","error");
                        return;
                    }
                    $(".yhfq").tagsinput('add',tag);
                }
            });
        }

        function initApproveRadiobox() {
            $("#approveTab .radio-box label").click(function() {
                $(this).parent().addClass("selected").siblings(".selected").removeClass("selected");
                var $input = $(this).prev();
                if ($input.val() == 2) { //驳回，弹出填写驳回原因输入框
                    $(this).closest(".row").find(".outbound").addClass("hide");
                    $(this).closest(".row").find(".col-3:last-child").removeClass("hide");
                } else {
                    $(this).closest(".row").find(".col-3:last-child").addClass("hide");
                    $(this).closest(".row").find(".outbound").removeClass("hide");
                }
            })
        }
    }
    this.saveApprove = function() { //提交审批意见
        var result = ifRejectReason();
        if(!result){
            return;
        }
        var param = {};
        param.activityId = $("#curActivityId").val();
        param.activityName = $("#curActivityName").val();
        param.channelApprovalDetailList = [];
        var channels = $("#approveTab .right-content .row");
        $.each(channels, function(i, item) {
            var channelList = {};
            channelList.channelId = $(item).find(".col-3:first-child>span").attr("data-id");
            channelList.channelName = $(item).find(".col-3:first-child>span").text();
            var $selRadio = $(item).find(".radio-box p.selected input");
            if ($selRadio.val() == 2) { //审批驳回
                channelList.approvalRsult = 2;
                channelList.approvalComments = $(item).find(".col-3:last-child textarea").val();
            } else { //审批通过
                channelList.approvalRsult = 1;
                //判断是否含有外呼特有元素
                var hczfFile = $("#hczfFile").data("ruleValue");
                if(hczfFile){
                    channelList.removeProd = hczfFile;
                }
                var hchdFile = $("#hchdFile").data("ruleValue");
                if(hchdFile){
                    channelList.removeAct = hchdFile;
                }
                var whjbFile = $("#whjbFile").data("ruleValue");
                if(whjbFile){
                    channelList.script = whjbFile;
                }
                var items = $(".yhfq").tagsinput('items');
                if(items && items.length>0){
                    channelList.orders = items;
                }
            }
            param.channelApprovalDetailList.push(channelList);
        })
        var activityApproval = JSON.stringify(param);

        $.ajax({
            url: "./activities/saveApproveActivity",
            type: 'post',
            cache: false,
            data: activityApproval,
            contentType: 'application/json',
            dataType: 'json',
            success: function(res) {
                console.log(res);
                if (res.code == 0) {
                    $(".modal-wrap-approve .modal-box-cancel").trigger("click");
                    freshIndexPage();    //审批保存成功刷新页面
                }else {
                    showMessage(res.msg,"error");
                }
            },
            error: function() {
                showMessage("提交失败","error")
            }
        })
    }
    var ifRejectReason = function(){  //是否填写了驳回原因
        var result = true;
        var channels = $("#approveTab .right-content .row");
        $.each(channels, function(i, item) {
            var $selRadio = $(item).find(".radio-box p.selected input");
            if ($selRadio.val() == 2) { //审批驳回
                var comments = $(item).find(".col-3:last-child textarea").val();
                if($.trim(comments) == ""){
                    showMessage("驳回原因不能为空！","error");
                    result = false;
                    return;
                }
            }
        })
        return result;
    }
}
initApproveModal.prototype.getActDetails = function(e) { //获取详情信息

    var modal = $(e.currentTarget).find("a").attr("href");
    $(modal + ' .modal-box-nav a').eq(0).click();
    var actId = $("#curActivityId").val();
    $.ajax({
        url: "./activities/getActivityDetail?activityId=" + actId,
        type: 'get',
        cache: false,
        dataType: 'json',
        success: function(data) {
            if (data.code != 0){
                showMessage(res.msg,"error");
            }else{
                var res = data.data;
                var sceneInfos = res.activitySceneInfoList;
                var channels = res.activityChannelDetailList;
                var recGroups = res.activityCustomerGroupInfoList;
                var recProducts = res.activityRecommendProducts;

                $(modal + " [data-elemid='activityIdShow']").text(res.activityId);
                $(modal + " [data-elemid='cityIdShow']").text(res.cityName);
                $(modal + " [data-elemid='activityNameShow']").text(res.activityName);
                $(modal + " [data-elemid='startDateShow']").text(res.startTime);
                $(modal + " [data-elemid='endDateShow']").text(res.endTime);
                $(modal + " [data-elemid='businessTypeNameShow']").text(res.businessTypeName);
                $(modal + " [data-elemid='businessSmallTypeNameShow']").text(res.businessSmallTypeName);
                if(res.testPhoneNum){
                    $(modal + " [data-elemid='testPhoneNumShow']").text(res.testPhoneNum);
                }else{
                    $(modal + " [data-elemid='testPhoneNumShow']").text("无");
                }
                $(modal + " [data-elemid='marketingPurposeNameShow']").text(res.marketingPurposeName);
                //场景事件
                $(modal + ' .sceneTab').html("");
                if (res.sceneFlag == 0) {
                    $(modal + " [data-elemid='activityType']").text("非实时");
                    $(modal + " [data-elemid='activityTypeInfo']").text("无");
                } else {
                    $(modal + " [data-elemid='activityType']").text(res.sceneName);
                    var config = globalConfig.detailSceneTab[res.sceneId];
                    if(config){
                        var $html = $(config.template);
                        var str = "";
                        $.each(sceneInfos,function (i, ruleInfo) {
                            if(ruleInfo.sceneRuleId == "SR003" || ruleInfo.sceneRuleId == "SR010" || ruleInfo.sceneRuleId == "SR015"){
                                str = i == sceneInfos.length-1?str + ruleInfo.sceneRuleExtendedValue:str + ruleInfo.sceneRuleExtendedValue + "，";
                                $html.find("[data-elemid='" + ruleInfo.sceneRuleId + "']").text(str);
                            }else if(ruleInfo.sceneRuleId == "SR006" || ruleInfo.sceneRuleId == "SR007" || ruleInfo.sceneRuleId == "SR011" || ruleInfo.sceneRuleId == "SR014"){
                                $html.find("[data-elemid='" + ruleInfo.sceneRuleId + "']").text(ruleInfo.sceneRuleExtendedValue);
                            }else{
                                if( ruleInfo.sceneRuleId == "SR012" ){
                                    var txt = "实时";
                                    if( ruleInfo.sceneRuleValue == 0 ){
                                        txt = "非实时";
                                        $html.find(".j-d-passbyLimitTime").addClass("hide");
                                    }

                                    $html.find("[data-elemid='" + ruleInfo.sceneRuleId + "']").text(txt);
                                }else{
                                    $html.find("[data-elemid='" + ruleInfo.sceneRuleId + "']").text(ruleInfo.sceneRuleValue);
                                }
                            }
                        });
                        $(modal + ' .sceneTab').append($html);
                    }else{
                        $(modal + " [data-elemid='activityTypeInfo']").text("无");
                    }
                }

                //目标用户
                if( recGroups.length ){
                    var str = [];
                    for (var i = 0; i < recGroups.length; i++) {
                        str.push(recGroups[i].customerGroupName);
                    }
                    $(modal + " [data-elemid='chosedGroups']").text(str.join("，"));
                }else{
                    $(modal + " [data-elemid='chosedGroups']").text("无");
                }

                // 删除用户
                var str = '';
                str += res.removeCancel10086 ? "取消10086流量提醒客户  " : "";
                str += res.removeEmployee ? "   内部员工  " : "";
                str += res.removeRedList ? "   红名单  " : "";
                str += res.removeSensitive ? "   敏感客户  " : "";
                str += res.removeGroupUser ? "   集团重要客户  " : "";
                str += res.removeUpload ? " 自定义导入（"+res.removeCustomerGroupId+"）" : "";
                if(str.length){
                    $(modal + " [data-elemid='removeCustomerShow']").text(str);
                }else{
                    $(modal + " [data-elemid='removeCustomerShow']").text("无");
                }
                $(modal + " [data-elemeid='marketingPurposeNameShow']").text(res.marketingPurposeName);
                //执行渠道
                var $channelTabNav = $(modal + " [data-elemid='channelTabNav']");
                $channelTabNav.html('');
                $(modal + ' .channel-tab').html('');
                if( !channels.length ){
                    $channelTabNav.html("<span>此实时场景下，无可选渠道</span>");
                }else{
                    for (var i = 0; i < channels.length; i++) {
                        var html = '',
                            contentApprover = "无",
                            channelApprover = "无";
                        var channelDetails = channels[i].activityChannelRuleDetailList;
                        var approverInfoList = channels[i].approverInfoList;
                        var channelId = channels[i].channelId;
                        if(channelId=='d07'){
                            $("[data-id='attachment_d']").parent().show();
                        }
                        var config = globalConfig.detailChannelTab[channelId];
                        if(config) {
                            var $html = $(config.template);
                            $channelTabNav.append(config.channelTabNavItem);
                            var $channelTab = $(modal + " [data-channelid='" + channels[i].channelId + "']");
                            $channelTab.html("");
                            var str = "";
                            for (var j = 0; j < channelDetails.length; j++) {
                                if(channelDetails[j].ruleId == "R005"){   //?????增加图片预览功能
                                    if( $html.find('[data-elemid=' + channelDetails[j].ruleId  +']').next().text() == "无" ){
                                        $html.find('[data-elemid=' + channelDetails[j].ruleId  +']').next().remove();
                                    }
                                    if(channelDetails[j].ruleValue){
                                        $html.find('[data-elemid=' + channelDetails[j].ruleId  +']').after("<span class='view-img' title='点击预览'>"+channelDetails[j].ruleValue+"</span>")
                                    }else{
                                        $html.find('[data-elemid=' + channelDetails[j].ruleId  +']').after("<span>无</span>")
                                    }
                                }else if(channelDetails[j].ruleId == "R002" || channelDetails[j].ruleId == "R003" || channelDetails[j].ruleId == "R010" || channelDetails[j].ruleId == "R008"){ //下拉列表
                                    $html.find('[data-elemid=' + channelDetails[j].ruleId  +']').text(channelDetails[j].ruleExtendedValue);
                                }else if(channelDetails[j].ruleId == "R009"){  //渠道小类
                                    str = str + channelDetails[j].ruleValue+" ";
                                    $html.find('[data-elemid=' + channelDetails[j].ruleId  +']').text(str);
                                }else if(channelDetails[j].ruleId == "R021"){
                                    if(channelDetails[j].ruleValue){
                                        $html.find('[data-elemid=' + channelDetails[j].ruleId + ']').html("<a style='text-decoration: underline;color: #788898' href='./files/download?fileName="+channelDetails[j].ruleValue+"'>"+channelDetails[j].ruleValue+"</a>");
                                    }else{
                                        $html.find('[data-elemid=' + channelDetails[j].ruleId + ']').text("无");
                                    }
                                }else {
                                    var txt = channelDetails[j].ruleValue?channelDetails[j].ruleValue:"无";
                                    $html.find('[data-elemid=' + channelDetails[j].ruleId  +']').text(txt);
                                }
                            }
                            for (var j = 0; j < approverInfoList.length; j++) {
                                var temp = approverInfoList[j];
                                if (temp.approverLevel == 1) {  //一级审批
                                    if( temp.approverId !== "sys" ){
                                        if( temp.approverId == "" ){
                                            if( ( channels[i].channelId == 'd05' || channels[i].channelId == 'd01' || channels[i].channelId == 'f02' ) && temp.approvalGroup){
                                                contentApprover = "审批组"
                                            }
                                        }else{
                                            contentApprover = temp.approverName+"("+temp.approverPhone+")";
                                        }
                                    }
                                }
                                if (temp.approverLevel == 2) {  //二级审批
                                    if( temp.approverId !== "sys" ){
                                        if( temp.approverId == "" ){
                                            if( ( channels[i].channelId == 'd05' || channels[i].channelId == 'd01' || channels[i].channelId == 'f02' ) && temp.approvalGroup) {
                                                channelApprover = "审批组";
                                            }
                                        }else{
                                            channelApprover = temp.approverName+"("+temp.approverPhone+")";
                                        }
                                    }
                                }
                            }
                            $html.find('[data-id="contentApproverId"]').text(contentApprover);
                            $html.find('[data-id="channelApproverId"]').text(channelApprover);
                        }
                        $(modal + ' .channel-tab').append($html);
                    }
                    $channelTabNav.on('click', 'a', function (e) {
                        $(this).addClass('selected').siblings().removeClass('selected');
                        e.preventDefault();
                        var dataChannelId = $(this).attr('href').replace('#','');
                        $(modal + ' .channel-tab li[data-channelid="' +dataChannelId+'"]').addClass('selected')
                            .siblings().removeClass('selected')
                    });
                    $channelTabNav.find('a').eq(0).click()
                    // $(modal + " [data-elemid='channelTabNav']").find("a:first-child").addClass("selected");
                }

                //已选业务
                $(modal + " [data-elemid='chosedBusiness']").html("");
                var str = [];
                for (var i = 0; i < recProducts.length; i++) {
                    str.push("<span>" + recProducts[i].productName +'('+recProducts[i].productId+')'+ "</span>");
                }
                $(modal + " [data-elemid='chosedBusiness']").append(str.join(", "));
                $(modal + " .item-property").nextAll().css("color", "#788898");
                initViewImg(); //图片预览事件
            }
        },
        error: function() {
            showMessage("获取审批活动信息失败","error")
        }
    });
    var initViewImg = function(){
        $(".view-img").click(function(e){
            e.preventDefault();
            var imgName = $(this).text();
            $.ajax({
                url: "./files/getUploadFile?fileName=" + imgName,
                type:"get",
                dataType:"json",
                cache:false,
                success:function(res){
                    if(res.code !== 0){
                        showMessage(res.msg,"error");
                        return;
                    }
                    //处理图片
                    $(".modal-wrap-showImg").show();
                    $(".modal-wrap-showImg img").attr("src",res.data.imgBase64);
                    $(".j-img-name").text(imgName);

                },
                error:function(){
                    showMessage("图片获取失败","error")
                }
            })
        })
    }
}

// end活动审批模态框

//start活动查看详情模态框
var initDetailModal = function() {
    this.init = function() {
        $("#actDetailNav li").click(function(e) {
            e.preventDefault();
            $(this).addClass("selected").siblings(".selected").removeClass("selected");
            var tabId = $(this).find("a").attr("href");
            $(tabId).addClass("selected").siblings(".selected").removeClass("selected");
        });
        $("[data-id='approveTab_d']").parent().click(function() { //获取审批结果
            //清空详情模态框信息
            htmlEvents.clearDetails("#detailTab_d");
            var actId = $("#curActivityId").val();
            var actStateId = $("#curActivityState").data("actStateId");
            var stateInfo = init.transState(actStateId);
            $.ajax({
                url: "./activities/getApprovaledResultInfo?activityId="+actId,
                type: 'get',
                cache: false,
                dataType: 'json',
                success: function(res) {
                    if (res.code == 0) {
                        var data = res.data;
                        $("#approveResult").addClass("border-radius-btn "+stateInfo.color).text(stateInfo.status);
                        for(var k in data){
                            if(k == "step1"){   //阶段1
                                $(".modal-wrap-detail [data-step='step1']").removeClass("hide");
                                if(data[k] == 0){
                                    $(".step1 .sub-row li>i").addClass("icon wait-icon");
                                    $(".step1 .sub-row li>span").text("系统判断中");

                                }else if(data[k] != 2 && data[k] != 3){
                                    $(".step1 .sub-row li>i").addClass("icon success-icon");
                                    $(".step1 .sub-row li>span").text("通过");
                                }else{
                                    $(".step1 .sub-row li>i").addClass("icon error-icon");
                                    $(".step1 .sub-row li>span").text("驳回");

                                }
                            }else{  //阶段2，阶段3，阶段4
                                if(data.step1 != 0 && data.step1 != 2 && data.step1 != 3){      //待修改
                                    $(".modal-wrap-detail [data-step='"+k+"']").removeClass("hide");
                                    var channels = data[k];
                                    var html = "";
                                    $.each(channels,function(i,item){
                                        html += '<ul class="sub-row clearfix" id="systemCheck">';
                                        html += '<li style="width:34%;">';
                                        html += '<label>渠道名称：</label>';
                                        html += '<span>'+item.channelName+'</span>';
                                        html += '</li>';
                                        html += '<li style="width:20%;">';
                                        html += '<label>审批人：</label>';
                                        if(item.approverId !== "sys"){
                                            if( !item.approverId ){
                                                if( ( item.channelId == 'd05' || item.channelId == 'd01' || item.channelId == 'f02' ) && item.approvalGroup){
                                                    html += '<span>审批组</span>';
                                                }else{
                                                    html += '<span>无</span>';
                                                }
                                            }else{
                                                html += '<span>'+item.approverName+'</span>';
                                            }
                                        }else{
                                            html += '<span>无</span>';
                                        }
                                        html += '</li>';
                                        html += '<li>';
                                        if(item.approveTime){
                                            html += '<label>审批时间：</label>';
                                            html += '<span>'+item.approveTime+'</span>';
                                        }
                                        html += '</li>';
                                        if(item.approvalResult == 2){
                                            html += '<li>';
                                            html += '<i class="icon error-icon"></i>';
                                            html += '<label class="top-move"></label>';
                                            html += '<span class="top-move">驳回</span>';
                                            html += '</li>';
                                            html += '<li style="width:100%">';
                                            html +=	'<label>驳回原因：</label>';
                                            html +=	'<span>'+item.approvalComments+'</span>';
                                            html +=	 '</li>';
                                        }else if(item.approvalResult == 1){
                                            html += '<li>';
                                            html += '<i class="icon success-icon"></i>';
                                            html += '<label></label>';
                                            html += '<span class="top-move">通过</span>';
                                            html += '</li>';
                                        }else{
                                            html += '<li>';
                                            html += '<i class="icon wait-icon"></i>';
                                            html += '<label></label>';
                                            html += '<span class="top-move">未审批</span>';
                                            html += '</li>';
                                        }
                                        html += '</ul>';
                                    })
                                    $("."+k).html(html);
                                }
                            }
                        }
                    } else {
                        showMessage(res.msg,"error")
                    }
                },
                error: function() {
                    shoMessage("获取审批活动信息失败","error")
                }
            })
        });
        $("[data-id='operateTab_d']").on("click",function(){
            var actId = $("#curActivityId").val();
            $.ajax({
                url: "./activities/getOpLogs?activityId="+actId,
                type: "get",
                success: function(res){
                    if( res.code != 0 ){
                        showMessage(res.msg,"error");
                        return;
                    }
                    var data = res.data;
                    var html = "";
                    if( !data || !data.length ){
                        html = "<p class='text-center'>暂无操作记录</p>";
                    }else{
                        $.each(data,function(i,item){
                            var desc = item.OP_DESC || "无";
                            var result = "成功",color = "blue";
                            if( item.OP_RESULT != 1 ){
                                result = "失败";
                                color = "red";
                            }
                            html += '<div class="row log-row">';
                            html += '	<div class="left-mark">';
                            html += '	<span class="item-property ">记录 <span>'+(i+1)+'</span>：</span>';
                            html += '</div>';
                            html += '<div class="right-content">';
                            html += '	<div class="sub-content" >';
                            html += '	<ul class="sub-row clearfix">';
                            html += '	<li>';
                            html += '	<span>'+item.USER_NAME +  '<span class="color-grey">('+ item.USER_PHONE+ ')</span>'+'</span>';
                            html += '	</li>';
                            html += '	<li>';
                            html += '	<span>'+item.OP_NAME+'</span>';
                            html += '	</li>';
                            html += '	<li>';
                            html += '	<span class="border-radius-btn '+color+'">'+result+'</span>';
                            html += '	</li>';
                            html += '	<li>';
                            html += '	<span>'+item.OP_TIME+'</span>';
                            html += '</li>';
                            html += '	<li>';
                            html += '	<span>'+desc+'</span>';
                            html += '</li>';
                            html += '</ul>';
                            html += '</div></div></div>';
                        });
                    }
                    $("#operateTab_d .result-content").html(html);
                }
            })
        });
        //TODO 附件列表展示
        $("[data-id='attachment_d']").parent().click(function() {
            var actId = $("#curActivityId").val();
            $.ajax({
                url: "./activities/getActivityExtInfo?activityId="+actId,
                type: "get",
                success: function(res){
                    if( res.code != 0 ){
                        showMessage(res.msg,"error");
                        return;
                    }
                    var data = res.data;
                    var activityChannelRuleDetail = data.activityChannelRuleDetail;
                    if(activityChannelRuleDetail){
                        $("#attachment_d [data-attachment='whjb-o']").html("<a style='text-decoration: underline;color: #788898' href='./files/download?fileName="+activityChannelRuleDetail.ruleValue+"'>"+activityChannelRuleDetail.ruleExtendedValue+"</a>");
                    }else{
                        $("#attachment_d [data-attachment='whjb-o']").text("无");
                    }

                    var channelApprovalDetail = data.channelApprovalDetail;
                    if(channelApprovalDetail.removeProd){
                        $("#attachment_d [data-attachment='hczf']").html("<a style='text-decoration: underline;color: #788898' href='./files/download?fileName="+channelApprovalDetail.removeProd+"'>"+channelApprovalDetail.removeProdOriginal+"</a>");
                    }else{
                        $("#attachment_d [data-attachment='hczf']").text("无");
                    }
                    if(channelApprovalDetail.removeAct){
                        $("#attachment_d [data-attachment='hchd']").html("<a style='text-decoration: underline;color: #788898' href='./files/download?fileName="+channelApprovalDetail.removeAct+"'>"+channelApprovalDetail.removeActOriginal+"</a>");
                    }else{
                        $("#attachment_d [data-attachment='hchd']").text("无");
                    }
                    if(channelApprovalDetail.script){
                        $("#attachment_d [data-attachment='whjb']").html("<a style='text-decoration: underline;color: #788898' href='./files/download?fileName="+channelApprovalDetail.script+"'>"+channelApprovalDetail.scriptOriginal+"</a>");
                    }else{
                        $("#attachment_d [data-attachment='whjb']").text("无");
                    }

                    var orders = data.channelApprovalDetail.orders;
                    if(orders && orders.length){
                        var text = '';
                        for (var i = 0; i < orders.length; i++) {
                            text += orders[i].text;
                            if(orders[i].order==1){
                                text += '-降序';
                            }
                            if(i!=orders.length-1){
                                text += '，';
                            }
                        }
                        $("#attachment_d [data-attachment='yhfq']").text(text);
                    }else{
                        $("#attachment_d [data-attachment='yhfq']").text("无");
                    }
                }
            });
        });
    }
    var transApproveResult = function(type){
        var result;
        if(type == 2){
            result = "驳回"
        }else if(type == 1){
            result = "通过"
        }else{
            result = "未审批"
        }
        return result;
    }
}
initDetailModal.prototype = initApproveModal.prototype;
//end活动查看详情模态框

function channelTemp1(e) {
    // if ($(this)[0].tagName.toUpperCase() !== "LABEL")
    // return;
    $(this).find("span").toggleClass("selected");
    var $input = $(this).prev();
    var $itemTab = $("#item-tab");
    console.log($input);
    var $channelTab = $("#channel-tab");
    if ($input.attr("checked") == "checked") {
        $input.removeAttr("checked");
        $itemTab.find("a[data-channelId='" + $input.attr("id") + "']").remove();
        $channelTab.find("li[data-channelId='" + $input.attr("id") + "']").removeClass("selected");
    } else {
        $input.attr("checked", "checked");
        var $temp = $("<a href='##' data-channelid='" +
            $input.attr("id") + "'>" + $.trim($(this).text()) + "</a>");

        $temp.click(function(e) {
            e.preventDefault();
            var channelId = $(this).attr("data-channelid");
            $(this).addClass("selected").siblings().removeClass("selected");
            $(".channel-tab ").css("display", "block");
            $(".channel-tab li[data-channelid='" + channelId + "']").addClass("selected").siblings().removeClass("selected");
        })
        $itemTab.append($temp);
        $temp.click();// 点击后跳转tab
        // if ($temp.siblings().length === 0) {
        // 	$temp.click();
        // }

    }
}

function getBusinessParam() {
    // 资费1，营销2，宣传3
    var tab = $("#recommand ul li").index($("#recommand ul li.selected")[0]) + 1;
    var page = $("#recommand .tab-content.selected").find("a.cur").text() || 1;
    var queryStr = $("#recommand .tab-content.selected").find("input.search").val() || "";
    var lengthRow = 5;
    var bussinessType=$("#bussinessType").val();
    var marketingPurpose=$("#marketingPurpose").val();
    return {
        prcType: tab,
        nowPageNum: page,
        queryStr: queryStr,
        lengthRow: lengthRow,
        bussinessType:bussinessType,
        marketingPurpose:marketingPurpose
    };
}

function getCustomerInputParam() {
    var createType = $("#historyInput ul li.selected a").attr("data-type") || 3;
    var currentPage = $("#historyInput .tab-content.selected").find("a.cur").text() || 1;
    var pageSize = 5;
    return {
        createType: createType,
        currentPage: currentPage,
        pageSize: pageSize
    };
}

function getActivitiesListInfo() {
    var param = {};
    param.cityId = $("#area").val() || 0;
    if($("#search").val() == "持活动名称、创建人、营销内容搜索"){
        param.searchVal = "";
    }else{
        param.searchVal = $("#search").val() || "";
    }
    param.channelId = $("#channel").val() || "@";
    param.showState = $("#state").val() || 0;
    param.currentPage = $("#page a.cur").text() || 1;
    param.lengthRow = 10;
    param.startTime = $("#startDate").val() || "";
    param.endTime = $("#endDate").val() || "";
    return param;
}


var isTabChanged = false;
var nowChoosedItem = 0;
var shouldRemoveAll = false;
var isTabChanged_l = false;
var nowChoosedItem_l = 0;
var shouldRemoveAll_l = false;
var lastBigClass = null;

function freshIndexPage(){
    var obj = getActivitiesListInfo();
    obj.currentPage = 1;
    init.getPageData(obj);
}

function getChoseMapDataAndReloadTable(activityId) {
    $.ajax({
        url: "./mental/getMarkedInfo?activityId="+activityId,
        type:"get",
        dataType: "json",
        success:function(res){
            if( res.code != 0 ){
                showMessage(res.msg,"error");
                globalAttrs.mapSelectedData = null;
                return
            }
            var data = res.data.data;
            var selectedType= ["实时","途经人口","工作人口","常住人口"];
            if( !data || !data.length ){
                globalAttrs.mapSelectedData = null;
                return;
            }
            globalAttrs.mapSelectedData = data;
            var tableData = [];
            $.each(data,function(i,item){
                var temp = {};
                temp.num = i+1;
                temp.name = item.name+"(0)";
                //var type = selectedType[item.type];
                var type = "";
                if(item.type == 1){
                    type = "实时用户";
                }else{
                    type = "非实时用户";
                }
                temp.type = type;
                tableData.push(temp);
            })

            var table = layui.table;
            table.reload('smartMapTable', {
                data:tableData
            });
        },
        error:function(){
            showMessage("获取数据失败","error")
        }
    })
}


function updateActivityId(){
    var nowId = $("#smartMapActivityId").val();
    var activityId = $("#activityId").text();
    if("" != nowId) {
        var count = parseInt(nowId.split("_")[1])+1;
        $("#smartMapActivityId").val(activityId + "_" + count);
    }else{
        $("#smartMapActivityId").val(activityId + "_1");
    }
}



function clearTable(tableId){
    var table = layui.table;
    table.reload(tableId, {
        data:[]
    });
}










