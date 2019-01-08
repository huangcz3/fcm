
//动态刷新界面标识
var flag = false;

//非实时场景渠道
var globalAttrs = {
    deftSpeed: {
        "10086": 150,
        "10658211": 280
    },
    channel: {
        "10086": "10086群发",
        "10658211": "优惠提醒"
    }
};
globalAttrs.selectedType = null;

$(document).ready(function() {
    //初始化网页顶部导航模块
    var topNav = new topNavigation();
    topNav.init();
    topNav.show("#marketingAct");
    //初始化网页左侧导航模块
    var sideNav = new sideNavigation();
    sideNav.init();
    sideNav.show("#sms_manage");

    $.ajaxSetup({
        cache:false
    });
    // $(document).on('click', function (e) {
    //     if ( !$(e.target).hasClass('filter-btn')
    //         && !$(e.target).closest('.filter-panel').length ) {
    //         $("#filter-btn").removeClass("opened");
    //         $(".filter-panel").css("display","none");
    //     }
    //     if (e.target !== $('.more-icon-box.selected')[0] && !$(e.target).hasClass('operate more-icon') ) {
    //         if($('.more-icon-box.selected').length){
    //             $('.more-icon-box.selected').removeClass("selected");
    //         }
    //     }
    // });

});

$(document).ready(function() {
    $.blockUI.defaults.overlayCSS.backgroundColor = '#fff';
    $.blockUI.defaults.overlayCSS.opacity = '0.8';
    $.blockUI.defaults.css.border = 'none';
    $.blockUI.defaults.css.fontSize = '16px';


    //重置详情模态框slimscroll的高度
    $(".detailTab .modal-box-content").slimScroll({
        height:'350px',
        color: "#a2a8af",
        alwaysVisible : true,
        size: "5px",
    });
    $(".approveTab .modal-box-content").slimScroll({
        height:'300px',
        color: "#a2a8af",
        alwaysVisible : true,
        size: "5px",
    })


    $(".main a").click(function (e) {
        e.preventDefault();
    })
    //初始化营销活动列表
    var init = new Init();
    window.init = init;
    freshIndexPage();

    $('#myTab a').click(function (e) {
        e.preventDefault()
        $(this).tab('show')
    })

    //下载数据
    $("#download").on("click",function(){
        var params = getActivitiesListInfo();
        window.location.href = './smsManage/getActivityListDownload?searchVal='+params.searchVal+
            '&channelId='+params.channelId+
            '&startTime='+params.startTime+
            '&endTime='+params.endTime+
            '&status='+params.status;
    });

    // 搜索
    $("#search ").keypress(function(e) {
        if (e.which == 13) {
            flag = false;//禁用自动刷新
            init.getPageData();
            flag = true;//启用自动刷新
        }
    }).next().click(function(e) {
        flag = false;//禁用自动刷新
        init.getPageData();
        flag = true;//启用自动刷新
    })

    $("#ensure").click(function (e) {
        flag = false;//禁用自动刷新
        e.preventDefault();
        var obj = getActivitiesListInfo();
        obj.currentPage = 1;
        init.getPageData(obj);
        $("#cancel").click();
        flag = true;//启用自动刷新
    });

});

//发送列表
var Init = function() {
    var self = this;
    var removeData={};
    /**
     *  定时刷新界面数据
     */
    this.freshPageDate = function(param) {
        if (!param) {
            var param = getActivitiesListInfo();
        }
        $.ajax({
            url: "./smsManage/getActivityList",
            type: 'get',
            data: param,
            cache: false,
            dataType: 'json',
            success: function(res) {
                if (res.code === 0) {
                    if(flag){
                        fillTable(res.data);
                    }
                } else {
                    console.info(res.msg);
                }
            },
            error: function() {
                console.info("程序异常");
            }

        })

    };

    this.getPageData = function(param) {
        $.blockUI({message:"数据获取中，请稍等..."});
        if (!param) {
            var param = getActivitiesListInfo();
        }
        $.ajax({
            url: "./smsManage/getActivityList",
            type: 'get',
            data: param,
            cache: false,
            dataType: 'json',
            success: function(res) {
                $.unblockUI();
                if (res.code === 0) {
                    var pageNum = Math.ceil(res.data.total / 10);
                    fillTable(res.data);
                    self.pagination(pageNum);
                    $("#activities_num").text("("+res.data.total+")");
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
                message:"数据获取中，请稍等..."
            });
            var param = getActivitiesListInfo();
            $.ajax({
                url: "./smsManage/getActivityList",
                type: 'get',
                data: param,
                cache: false,
                dataType: 'json',
                success: function(res) {
                    $.unblockUI();
                    if (res.code == 0) {
                        fillTable(res.data);
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
    var fillTable = function(d) {
        //权限信息
        var power = d.sendOperator;
        var data = d.result;
        var singleOperator = d.userRole.indexOf("singleOperator") == -1?false:true;
        var hideIcon = '<a href="#" class="operate visible-hidden"></a>';

        if(data.length == 0){
            $("#table tbody").html("<td colspan='12' style'width: 100%;text-align:center;'>暂无数据</td>");
        }else{
            var html = '';
            for (var i = 0; i < data.length; i++) {
                var remainNum = data[i].remainNum<0 || data[i].sendState == 3?0:data[i].remainNum,
                    channelName = globalAttrs.channel[data[i].businessId];

                html += '<tr>' + '<td><i></i></td>';
                html += '<td class="text-align-left" title="' + data[i].idNoName + '">' + data[i].idNoName + '</td>';
                html += '<td class="text-align-left" title="' + channelName + '">' + channelName + '</td>';
                html += '<td data-state="'+data[i].sendState+'"><span class="' + transState(data[i].sendState).color + '">' + transState(data[i].sendState).status + '</span></td>';
                html += '<td data-id="'+data[i].idNo+'">' + data[i].idNo + '</td>';
                html += '<td title="' + data[i].startTime + '">' + data[i].startTime + '</td>';
                html += '<td title="' + data[i].endTime + '">' + data[i].endTime + '</td>';
                if(data[i].opTime == null || data[i].opTime == undefined){
                    html += '<td title="' + "" + '">' + "" + '</td>';
                }else{
                    html += '<td title="' + data[i].opTime + '">' + data[i].opTime + '</td>';
                }
                html += '<td><span class="green">' + data[i].sendedNum + '<b class="pop-btn"></b></span></td>';
                html += '<td>' + remainNum + '</td>';
                if(data[i].sendState == '1'){  //发送中
                    if(power && data[i].status != '1'){  //status: 1  发送暂停
                        html += '<td><div class="speed" businessIds="'+data[i].businessId+'" data-id="' + data[i].idNo + '" data-name="' + data[i].idNoName + '" ' +
                            'style="width: 90%;border-radius:10px;float: left;margin-left: 10%;cursor: pointer;">' +
                            '<a style="color: #82C7A1;"><span>'+data[i].speed+'</span>条/S</a>' +
                            '<img style="height: 14px;width: 14px;padding-bottom: 2px;padding-left: 2px;" src="./img/icon_bj.png">' +
                            '</div></td>';
                    }else{
                        html += '<td><div businessIds="'+data[i].businessId+'" data-id="' + data[i].idNo + '" data-name="' + data[i].idNoName + '" ' +
                            'style="width: 90%;border-radius:10px;float: left;margin-left: 10%;">' +
                            '<a style="color: #82C7A1;"><span>'+data[i].speed+'</span>条/S</a>' +
                            '<img style="height: 14px;width: 14px;padding-bottom: 2px;padding-left: 2px;" src="./img/icon_bj.png">' +
                            '</div></td>';
                    }

                }else{
                    html += '<td><div businessIds="'+data[i].businessId+'" style="width: 90%;border-radius:10px;float: left;margin-left: 10%;"><span>— —</span></div></td>';
                }

                //根据状态，判断展示哪些操作
                html += '<td class="overflow-show" businessIds="'+data[i].businessId+'" data-id="' + data[i].idNo + '" data-name="' + data[i].idNoName + '">';
                //恢复/暂停按钮判断
                if(data[i].sendState != '1' || !power){  //不展示按钮；只有发送中才可暂停和恢复；
                    html += '<a href="#" class="operate visible-hidden" businessIds="'+data[i].businessId+'" data-id="' + data[i].idNo + '" data-name="' + data[i].idNoName + '"></a>';
                }else{
                    if(data[i].status == '0'){
                        html += '<a href="#" title="暂停" class="operate stop-icon" businessIds="'+data[i].businessId+'" data-id="' + data[i].idNo + '" data-name="' + data[i].idNoName + '"></a>';
                    }else{
                        html += '<a href="#" title="恢复" class="operate start-icon" businessIds="'+data[i].businessId+'" data-id="' + data[i].idNo + '" data-name="' + data[i].idNoName + '"></a>';
                    }
                }
                //插队按钮判断
                if(!power){  //不展示按钮
                    html += '<a href="#" class="operate visible-hidden" businessIds="'+data[i].businessId+'" data-id="' + data[i].idNo + '" data-name="' + data[i].idNoName + '"></a>';
                }else{
                    if(data[i].sendState == '2'){
                        html += '<a href="#" title="提前" class="operate top-icon" businessIds="'+data[i].businessId+'" data-id="' + data[i].idNo + '" data-name="' + data[i].idNoName + '"></a>';
                    }else{
                        html += '<a href="#" title="提前" class="operate bk_icon" businessIds="'+data[i].businessId+'" data-id="' + data[i].idNo + '" data-name="' + data[i].idNoName + '"></a>';
                    }
                }

                //详情
                html += '<a href="#" title="详情" class="operate detail-modal-icon" businessIds="'+data[i].businessId+'" data-id="' + data[i].idNo + '" data-name="' + data[i].idNoName + '"></a>';
                html += '<input type="hidden" class="sms_info" value="'+data[i].content+'">';
                if ( singleOperator && ( data[i].sendState == 1 || data[i].sendState == 2 )){
                    html += '<a href="#" title="更改" class="operate edit-icon" data-sendState="'+data[i].sendState+'"></a>';
                }else {
                    html += hideIcon;
                }
                html += '</td></tr>';
                removeData[data[i].idNo] = data[i].successNum + "," + data[i].failNum;
            }
            $("#table tbody").html(html);
        }
        initTableStyle();
        initOperateEvent();
        initTableHover();
        initPopEvent("#table",removeData);
    };

    var initTableStyle = function() {
		/**
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

        var table_h = parseInt($(".inner-content").css("height")) - 20 - 39 - 60;
        $("#table td").css("height", (table_h / 11) + "px");
        $("#table tbody tr:odd").addClass("even");
    };

    var initOperateEvent = function() {
        /**
         * 初始化速率操作事件
         */
        $(".speed").click(function(e){
            e.preventDefault();
            var businessId = $(this).attr("businessIds");
            //禁用空白处点击关闭，ESC关闭
            $('.modal').on('show.bs.modal', centerModals);
            $('#speed_model').modal({backdrop: 'static', keyboard: false});
            $("#speed_input").val("");
            $("#speedText").val("");
            $("#speed_actId").val($(this).attr("data-id"));
            $("#speed_businessIds").val(businessId);
            $("#activityName_speed").text($.trim($(this).attr("data-name")));
            $("#currentSpeed").text($.trim($(this).find("span").text()));
            $(".j-deftSpeed").text(globalAttrs.deftSpeed[businessId]);
        })
        /**
         * 初始化短信详情列表
         */
        $(".detail-modal-icon").click(function(e) { //详情
            e.preventDefault();
            $(".modal-wrap-detail").show();
            $("#myTabContent").show();
            $("#home_tab").click();
            var actid = $(this).attr("data-id");
            var actname = $(this).attr("data-name");
            var sms = $(this).next("input").val() || "";
            $("#smsContent").text(sms);
            $("#actId").val(actid);
            $("#actname").val(actname);
        });
        /**
         * 初始化操作详情界面
         */
        $("#operation_info").click(function(){
            var actid = $("#actId").val();
            $.ajax({
                url:"./smsManage/showOperateList" ,
                type:'post',
                data:{"activityId":actid},
                dataType:'json',
                cache: false,
                success:function(res){
                    if(res.code == 0){
                        fillOperateTable(res.data);
                    }else{
                        showMessage("失败，"+res.msg,"error");
                    }
                },
                error:function(){
                    showMessage("加载失败","error");
                }
            })
        })
        /**
         * 初始化暂停模态框
         */
        $(".stop-icon").click(function(e) { //暂停活动
            e.preventDefault();
            //禁用空白处点击关闭，ESC关闭
            $('.modal').on('show.bs.modal', centerModals);
            $('.reason_model').modal({backdrop: 'static', keyboard: false});
            $("#reasonArea").val("");
            $("#activityName_reason").text($(this).attr("data-name"));
            $("#reason_actId").val($(this).attr("data-id"));
            $("#reason_businessIds").val($(this).attr("businessIds"));
            $("#kind").val("stop");
        });
        /**
         * 初始化开始模态框
         */
        $(".start-icon").click(function(e) { //恢复活动
            e.preventDefault();
            //禁用空白处点击关闭，ESC关闭
            $('.modal').on('show.bs.modal', centerModals);
            $('.reason_model').modal({backdrop: 'static', keyboard: false});
            $("#reasonArea").val("");
            $("#activityName_reason").text($(this).attr("data-name"));
            $("#reason_actId").val($(this).attr("data-id"));
            $("#reason_businessIds").val($(this).attr("businessIds"));
            $("#kind").val("start");
        });
        /**
         * 初始化提前活动模块模态框
         */
        $(".top-icon").click(function(e) {
            e.preventDefault();
            //禁用空白处点击关闭，ESC关闭
            $('.modal').on('show.bs.modal', centerModals);
            $('.reason_model').modal({backdrop: 'static', keyboard: false});
            $("#reasonArea").val("");
            $("#activityName_reason").text($(this).attr("data-name"));
            $("#reason_actId").val($(this).attr("data-id"));
            $("#reason_businessIds").val($(this).attr("businessIds"));
            $("#kind").val("advance");
        });
        /**
         * 更改发送策略
         */
        $(".edit-icon").on("click",function () {
            var $this = $(this),
                 $parent = $this.parent(),
                 state = $this.attr("data-sendState"),
                 actId = $parent.attr("data-id"),
                bussinessId = $parent.attr("businessids");

            var editEvent = function () {
                var $modal = $(".modal-wrap-edit")
                $modal.show();
                $modal.data("state",state);
                $modal.data("actId",actId);
                $modal.data("businessids",bussinessId);
            }
            if ( state == 2 ){
                editEvent();
            }else{
                showConfirm("此活动正在发送中，须先暂停活动后才能修改，确定暂停并修改此活动吗？",editEvent)
            }
        })
    }

    //发送量初始化
    var initPopEvent = function(table,data){    //发送量展示
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
                    var list = data[k];//successNum,failNum
                    if(!list){
                        return;
                    }
                    var num = data[k].split(","); //[successNum,failNum]
                    $(".pop-content span[data-id='successNumAmount']").text(num[0]);
                    $(".pop-content span[data-id='failNumAmount']").text(num[1]);
                    $(".pop-content li[data-id='successNum']").show();
                    $(".pop-content li[data-id='failNum']").show();
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
function transState(type) {
    switch (type) {
        case 1:
            return {
                "status": "发送中",
                "color": "green"
            };
            break;
        case 2:
            return {
                "status": "待发送",
                "color": "gery-blue"
            };
            break;
        case 3:
            return {
                "status": "发送完成",
                "color": "grey"
            };
            break;
        case 4:
            return {
                "status": "活动结束",
                "color": "red"
            };
            break;
        default:
    }
};

/**
 * 操作列表填充数据
 */
function fillOperateTable(data){
    var html = "";
    if(data.length == 0){
        $("#operate_info tbody").html("<td colspan='5' style'width: 100%;text-align:center;'>暂无数据</td>");
    }else{
        for(var x = 0; x < data.length; x++){
            html += "<tr>";
            html += '<td>'+data[x].userName+'</td>';
            html += '<td>'+data[x].opName+'</td>';
            html += '<td>'+(data[x].opResult == 1 ?"成功":"失败")+'</td>';
            html += '<td>'+data[x].opTime+'</td>';
            html += '<td>'+((data[x].opDesc==null || data[x].opDesc=="")?"空":data[x].opDesc)+'</td>';
            html += "</tr>";
        }
        $("#operate_info tbody").html(html);
    }

}

/**
*   修改短信发送速率
*/
function speedChange(){
    var businessId = $("#speed_businessIds").val();
    var oldSpeed = $.trim($("#currentSpeed").text());//当前速度
    var speed = $.trim($("#speed_input").val());//修改速度
    var reason = $.trim($("#speedText").val());//修改原因
    if(speed == ""){
        showMessage("速度不能为空！","error");
        return
    }
    if(speed < 1 || speed > globalAttrs.deftSpeed[businessId]){
        showMessage("速度填写有误，阀值为[1,"+ globalAttrs.deftSpeed[businessId]+"]！","error");
        return
    }
    if(oldSpeed == speed){
        showMessage("修改前后速度不能一样！","error");
        return
    }
    if(reason == ""){
        showMessage("修改原因不能为空！","error");
        return
    }
    closedModel = "speed_model";
    showConfirm("确定要修改发送速率吗？",change, showModel);
    hiddenModel();

}

function change(){
    var actid = $("#speed_actId").val();//活动id
    var speed = $.trim($("#speed_input").val());//修改速度
    var reason = $.trim($("#speedText").val());//修改原因
    var businessIds = $.trim($("#speed_businessIds").val());//修改原因
    $.blockUI({
        message:"修改中，请稍等..."
    });
    $.ajax({
        url:"./smsManage/changeActivitySpeed" ,
        type:'post',
        data:{"activityId":actid,"speed":speed,"reason":reason,"businessIds":businessIds},
        dataType:'json',
        cache: false,
        success:function(res){
            if(res.code == 0){
                $.unblockUI();
                showMessage("操作成功","success",1500);
                $("#speedClose").click();
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

var closedModel = "";

function initReasonModelData(){
    //活动ID
    var actid = $("#reason_actId").val();
    //业务ID
    var businessIds = $.trim($("#reason_businessIds").val());//修改原因
    //操作动作
    var kind = $("#kind").val();
    //操作原因
    var reason = $.trim($("#reasonArea").val());
    if(reason == ""){
        showMessage("操作原因不能为空","error");
        return false;
    }
    var url;
    var ensureSS = function(){

        $.blockUI({
            message:"状态修改中，请稍等..."
        });
        $.ajax({
            url: url,
            type:'post',
            data:{"activityId":actid,"reason":reason,"businessIds":businessIds},
            dataType:'json',
            cache: false,
            success:function(res){
                if(res.code == 0){
                    $.unblockUI();
                    showMessage("操作成功","success",1500);
                    $("#reasonClose").click();
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
    if(kind == "stop"){
        url = "./smsManage/pausedActivity";
        closedModel = "reason_model";
        showConfirm("确定要暂停此活动吗？",ensureSS, showModel);
        hiddenModel();
    }else if (kind == "start"){
        url = "./smsManage/recoveryActivity";
        closedModel = "reason_model";
        showConfirm("确定要开始此活动吗？",ensureSS, showModel);
        hiddenModel();
    }else{
        url = "./smsManage/advanceActivity";
        closedModel = "reason_model";
        showConfirm("确定要置顶此活动吗？",ensureSS, showModel);
        hiddenModel();
    }

}

function getActivitiesListInfo() {
    var param = {};
    if($("#search").val() == "持活动ID、营销内容搜索"){
        param.searchVal = "";
    }else{
        param.searchVal = $("#search").val() || "";
    }
    param.channelId = $("#channel").val() || "ALL";
    param.status = $("#state").val() || 0;
    param.currentPage = $("#page a.cur").text() || 1;
    param.lengthRow = 10;
    param.startTime = $("#startDate").val() || "";
    param.endTime = $("#endDate").val() || "";
    return param;
}

function freshPage(){
    flag = false;//禁用自动刷新
    var obj = getActivitiesListInfo();
    init.getPageData(obj);
    flag = true;//启动自动刷新
}

function freshIndexPage(){
    flag = false;//禁用自动刷新
    var obj = getActivitiesListInfo();
    obj.currentPage = 1;
    init.getPageData(obj);
    flag = true;//启动自动刷新
}

function hiddenModel(){
    $(".close").click();
}

function showModel() {
    $("#" + closedModel).modal({backdrop: 'static', keyboard: false});
}

function centerModals(){
    $('.modal').each(function(i){
        var $clone = $(this).clone().css('display', 'block').appendTo('body');
        var top = Math.round(($clone.height() - $clone.find('.modal-content').height()) / 2);
        top = top > 50 ? top : 0;
        $clone.remove();
        $(this).find('.modal-content').css("margin-top", top-50);
    });
}

$(function () {
    $("#saveReset").on("click",function (e) {
        e.preventDefault();
        var $modal = $(".modal-wrap-edit");
        var state = $modal.data("state"),
            actId = $modal.data("actId"),
            businessIds = $modal.data("businessids");
        var $form = $("#resetForm");
        var params = $form.serializeJson();
        params.sendState = state;
        params.activityId = actId;
        params.businessIds = businessIds;

        var recoverSend = function () {
            $.blockUI({
                message:"状态修改中，请稍等..."
            });
            var reason = "发送中活动,更改发送策略成功后，恢复活动发送";
            $.ajax({
                url: "./smsManage/recoveryActivity",
                type:'post',
                data:{"activityId":actId,"reason":reason,"businessIds":businessIds},
                dataType:'json',
                cache: false,
                success:function(res){
                    $.unblockUI();
                    if(res.code == 0){
                        showMessage("操作成功","success");
                        //刷新页面
                        freshIndexPage();
                    }else{
                        showMessage("失败，"+res.msg,"error");
                    }
                },
                error:function(){
                    $.unblockUI();
                    showMessage("操作失败","error");
                }
            })
        }

        $.ajax({
            url: "./smsManage/changeIsFilter",
            type: "post",
            data: {
                isFilter: params.isFilter,
                sendState: state,
                activityId: actId,
            }
        }).done(function (res) {
            if( res.code != 0 ){
                showMessage(res.msg,"error");
                return
            }
            $modal.hide();
            $modal.find(".radio-box p:first-child").trigger("click");
            if( res.data == "2" ){
                showConfirm("更改成功，是否立即恢复发送？",recoverSend);
            }else{
                showMessage("更改发送策略成功","success");
                freshIndexPage();
            }
        }).fail(function () {
            showModel("更改发送策略失败","error");
        });
    });

    $(".radio-box p").unbind("click");
    $(".radio-box p").click(function(){
        var $this = $(this);
            $sib = $this.siblings("p");
        $sib.find("input").removeProp("checked");
        $sib.find("span").removeClass("selected");
        $this.find("input").prop("checked",true);
        $this.find("span").addClass("selected");
    });
});