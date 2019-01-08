$(document).ready(function(){
    //初始化网页顶部导航模块
    var topNav = new topNavigation();
    topNav.init();
    topNav.show("#sysManage");
    //初始化网页左侧导航模块
    var sideNav = new sideNavigation();
    sideNav.init("#sysManageSMSApproval");
    //sideNav.show("#sysManagePriority");
});

$(document).ready(function(){
    //获取用户名
    $.ajax({
        url: "./base/getUserInfo",
        type: "get",
        cache: false,
        dataType: "json",
        success: function (data) {
            if (data.code !== 0) {
                showMessage(res.msg,'error');
            }else{
                $("#user").text(data.data || "请登录");
            }

        }
    })

    //初始化渠道审批列表
    var initSMSApproval = new InitSMSApproval();
    initSMSApproval.getPageData();



    //ie8模拟placeholder
    $("#search").val("支持渠道ID、名称搜索").css("color","#CCD0D9");
    $("#search").focus(function(){
        if($("#search").val() == "支持渠道ID、名称搜索"){
            $(this).val("");
        }
        $(this).css("color","black");
    });
    $("#search").blur(function(){
        if($(this).val() == ""){
            $(this).val("支持渠道ID、名称搜索").css("color","#CCD0D9");
            return;
        }
    });
    // $('.search-box>i').click(function(){
    //     initSMSApproval.getPageData();
    // });

    // 搜索
    $("#search").keypress(function (e) {
        if (e.which == 13) {
            initSMSApproval.getPageData();
        }
    }).next().click(function () {
        initSMSApproval.getPageData();
    });
    $.ajaxSetup({
        cache:false
    })
});

//修改短信审批设置
$(function () {
    var initSMSApproval = new InitSMSApproval();
    var getFormData = function ($parent) {
        var channelInfo = {};
        // channelInfo.channelId = $parent.find(".u-channel").val();
        channelInfo.channelName = $parent.find("#channel").val();
        channelInfo.channelTypeName = $parent.find("#channelTypeName").val();
        channelInfo.isCanSmsApproval = $parent.find("#smsAproval").val();
        return channelInfo;
    }


    //取消后清空
    $(".modal-box-cancel").click(function (e) {
        var $modalWrap = $(this).closest(".modal-wrap");
        e.preventDefault();
        $modalWrap.hide();
        $modalWrap.find(".u-channel").val("");
        $modalWrap.find(".u-channelTypeName").val(1);
        $modalWrap.find(".j-approve-sms").val(0);

        if( $modalWrap == $(".modal-wrap-edit") ){
            $modalWrap.removeData("channelId");
        }
    });

    //保存修改
    $(".modal-wrap-edit .modal-box-save").click(function (e) {
        //获取表单中的值
        var $parent = $(".modal-wrap-edit")
        var params = getFormData($parent);
        params.channelId = $parent.data("channelId");
        //验证数据

        if (params.channelId == "") {
            showMessage("ID不能为空", "error");
        } else {
            //数据合法进行提交
            $.blockUI.defaults.overlayCSS.backgroundColor = '#fff';
            $.blockUI.defaults.overlayCSS.opacity = '0.8';
            $.blockUI({
                message: "提交中，请稍等...",
                css: {
                    border: "none",
                    fontSize: "16px"
                }
            });

            $.ajax({
                url: "./manage/setSMSApproval",
                type: "post",
                data: JSON.stringify(params),
                dataType: "json",
                contentType: 'application/json;charset=UTF-8',
                cache: false,
                success: function (res) {
                    $.unblockUI();
                    if( res.code != 0 ){
                        showMessage(res.msg,"error");
                        return
                    }
                    showMessage("提交成功", "success");
                    initSMSApproval.getPageData();
                    $(".modal-box-cancel").click();
                },
                error: function () {
                    $.unblockUI();
                    showMessage("提交失败", "error");
                }
            })
        }
    });

});




var InitSMSApproval = function () {

    var me = this;
    this.getPageData = function () {
        var param = {};
        if($("#search").val() == "支持渠道ID、名称搜索"){
            param.searchVal = "";
        }else{
            param.searchVal = $("#search").val() || "";
        }
        param.lengthRow = 10;
        param.currentPage = 1;

        $.ajax({
            url: './manage/getSMSApprovalInfoList',
            type:'get',
            data:param,
            dataType:'json',
            cache:false,
            success:function (res) {
                if (res.code != 0) {
                    showMessage(res.msg, "error");
                    return
                }
                fillTable(res.data.channelInfoList,res.data.userRole);
                pagination(res.data.totals);
                $("#channel_num").text("("+res.data.totals+")");
            },
            error: function () {
                showMessage("获取数据失败", "error");
            }
        })
    };

    //分页
    var pagination = function (totals) {
        var pagerBox = document.getElementById('page');
        $(pagerBox).html("");
        var pager = new Pager({
            index: 1,
            total: Math.ceil(totals / 10),
            parent: pagerBox,
            onchange: doChangePage
        });

        function doChangePage(obj) {
            var param = {};
            if($("#search").val() == "支持渠道ID、名称搜索"){
                param.searchVal = "";
            }else{
                param.searchVal = $("#search").val() || "";
            }
            param.lengthRow = 10;
            param.currentPage = obj.index;

            $.ajax({
                url: "./manage/getSMSApprovalInfoList",
                type: 'get',
                data: param,
                dataType: 'json',
                success: function (res) {
                    if (res.code != 0) {
                        showMessage(res.msg, "error");
                        return
                    }
                    fillTable(res.data.channelInfoList, res.data.userRole);
                },
                error: function () {
                    showMessage("获取数据失败", "error");
                }

            })
        }
    };


    var fillTable = function (param,userRole){
        var data = param;
        var html = '', htmlHead = "";
        var isCanEdit = userRole.indexOf("admin") == -1 ? false : true;

        if (isCanEdit) {
            htmlHead += "<tr>";
            htmlHead += "<td style='width: 5%;' >&nbsp;</td>";
            htmlHead += "<td>渠道</td>";
            htmlHead += "<td>渠道类型</td>";
            htmlHead += "<td>是否需要内容审批</td>";
            htmlHead += "<td>是否需要渠道审批</td>";
            htmlHead += "<td>是否需要分管领导审批</td>";
            htmlHead += "<td>是否支持短息审批</td>";
            htmlHead += "<td>操作</td>";
            htmlHead += "</tr>";
        } else {
            htmlHead += "<tr>";
            htmlHead += "<td style='width: 5%;' >&nbsp;</td>";
            htmlHead += "<td>渠道</td>";
            htmlHead += "<td>渠道类型</td>";
            htmlHead += "<td>是否需要内容审批</td>";
            htmlHead += "<td>是否需要渠道审批</td>";
            htmlHead += "<td>是否需要分管领导审批</td>";
            htmlHead += "<td>是否支持短息审批</td>";
            // htmlHead += "<!--  <td>操作</td>-->";
            htmlHead += "</tr>";
        }

        if (!data || !data.length) {
            if (isCanEdit) {
                html = "<tr><td colspan='8'>暂无数据</td></tr>"
            } else {
                html = "<tr><td colspan='8'>暂无数据</td></tr>"
            }

        } else {
            for (var i = 0; i < data.length; i++) {
                html += '<tr>';
                html += '<td>';
                html += '<p>';
                html += '<i class="icon"></i>';
                html += '</p>';
                html += '</td>';
                html += '<td>' + data[i].channelName + '</td>';

                html += '<td>' + data[i].channelTypeName + '</td>';

                if (data[i].needContentApproval == 0){
                    html += '<td>' + "否" + '</td>';
                }else{
                    html += '<td>' + "是" + '</td>';
                }
                if (data[i].needChannelApproval == 0){
                    html += '<td>' + "否" + '</td>';
                }else{
                    html += '<td>' + "是" + '</td>';
                }
                if (data[i].needLeaderApproval == 0){
                    html += '<td>' + "否" + '</td>';
                }else{
                    html += '<td>' + "是" + '</td>';
                }
                if (data[i].isCanSmsApproval == 0){
                    html += '<td>' + "否" + '</td>';
                }else{
                    html += '<td>' + "是" + '</td>';
                }
                if (isCanEdit) {
                    html += '<td data-channelId="'+data[i].channelId+'" data-channelName="' + data[i].channelName + '" data-channelTypeId="' + data[i].channelTypeId + '" data-channelTypeName="' + data[i].channelTypeName + '" data-needContentApproval="' + data[i].needContentApproval + '" data-needChannelApproval="' + data[i].needChannelApproval + '" data-needLeaderApproval="' + data[i].needLeaderApproval + '" data-isCanSmsApproval="' + data[i].isCanSmsApproval + '" >';
                    html += '<a href="#" title="修改" class="operate edit-icon"></a>';
                    html += '</td>';
                }

                html += '</tr>';
            }
        }
        $("#table thead").html(htmlHead);
        $("#table tbody").html(html);

        initTableStyle();
        initOperateEvent();
        initTableHover();

    };
    //初始化表格的样式
    var initTableStyle = function () {
        /*
         *所有动态添加列添加背景色
         */
        var len = $("#table thead td[data-index]").length;
        for (var col = "col-", eachCol, i = 5; i < len + 1; i++) {
            eachCol = col + i;
            $("#table td[data-index='col-" + i + "']").find('span').addClass(color[(i - 5) % 4]);
        }

        //表格图标添加
        imgPath = 'img/';
        icons = ['file_red.png', 'file_yellow.png', 'file_green.png', 'file_blue.png']
        imgTds = $("#table td>p>i.icon");
        for (var i = 0; i < imgTds.length; i++) {
            $(imgTds[i]).css("background", "url(" + imgPath + icons[i % 4] + ")");
        }
        var table_h = parseInt($(".inner-content").css("height")) - 20 - 39 - 40 - 20;
        $("#table td").css("height", (table_h / 11) + "px");
        $("#table tbody tr:odd").addClass("even");

    };

    //初始化点击事件
    var initOperateEvent = function () {
        $(".edit-icon").click(function () {  //编辑
            var $parent = $(this).parent();
            var $modalBox = $(".modal-wrap-edit");
            var channelId = $parent.attr("data-channelId");
            var channelName = $parent.attr("data-channelName");
            var channelTypeId = $parent.attr("data-channelTypeId");
            var channelTypeName = $parent.attr("data-channelTypeName");
            var needContentApproval = $parent.attr("data-needContentApproval");
            var needChannelApproval = $parent.attr("data-needChannelApproval");
            var needLeaderApproval = $parent.attr("data-needLeaderApproval");
            var isCanSmsApproval = $parent.attr("data-isCanSmsApproval");

            $modalBox.show();
            $modalBox.data("channelId",channelId);

            $("#channel").val(channelName);
         /*   $("#channel").data("channelId",channelId);*/
            $("#channelTypeName").val(channelTypeName);
            $("#channelTypeName").data("channelTypeId",channelTypeId);
            $("#smsAproval").val(isCanSmsApproval);
        });

}}