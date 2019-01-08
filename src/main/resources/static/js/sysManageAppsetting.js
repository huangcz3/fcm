/**
 * Created by Administrator on 2017/10/26.
 * 审批人设置
 */
var approverGlobal = {
    getDropList: function () {
        $.ajax({
            url: './base/getEvaCityInit',
            type: "get",
            dataType: 'json',
            cache: false,
            success: function (res) {
                if (res.code != 0) {
                    showMessage(res.msg, "error");
                    return
                }
                res = res.data;
                var areaHtml = '';
                var channelHtml = "";
                var filterChannel = '<option id="allChannel" value="@">全部渠道</option>';
                var filterCity = '<option id="0" value=0>全部地市</option>';
                var levelHtml = '<option value="0">全部</option>';
                var area = res.cityOption;
                var channel = res.channelOption;
                var level = res.allLevel;
                //填充地市的下拉列表
                for (var i = 0; i < area.length; i++) {
                    areaHtml += "<option id='" + area[i].CITYID + "' value='" + area[i].CITYID + "' >" + area[i].CITYNAME + "</option>";
                }
                $(".filter-panel .u-city").html(filterCity + areaHtml);
                $(".modal-wrap .u-city").html(areaHtml);
                //填充渠道的下拉列表
                for (var i = 0; i < channel.length; i++) {
                    channelHtml += "<option id='" + channel[i].CHANNELID + "' value='" + channel[i].CHANNELID + "' >" + channel[i].CHANNELNAME + "</option>";
                }
                $(".filter-panel .u-channel").html(filterChannel + channelHtml);
                $(".modal-wrap .u-channel").html(channelHtml);

                for (var i = 0; i < level.length; i++) {
                    levelHtml += "<option ' value='" + level[i] + "' >" + level[i] + "</option>";
                }
                $("#priority2").html(levelHtml);
            },
            error: function () {
                showMessage("出错了！获取数据失败", "error");
            }
        });
    },
    // 得到层级信息
    getLevelList: function ($chnnelId, appLevel, cityId) {
        $.ajax({
            url: './manage/getLevelList',
            type: "get",
            data: {"channelId": $chnnelId},
            dataType: 'json',
            cache: false,
            success: function (res) {
                if (res.code != 0) {
                    showMessage(res.msg, "error");
                    return
                }
                res = res.data;
                var levelHtml = '';
                //填充层级的下拉列表
                for (var i = 0; i < res.length; i++) {
                    levelHtml += "<option value='" + res[i] + "' >" + res[i] + "</option>";
                }
                $(".j-approve-priority").html(levelHtml);

                approverGlobal.getCityList($chnnelId, cityId, res[0]);

                if (appLevel != -1) {
                    $("#priority1").val(appLevel);
                }
            },
            error: function () {
                showMessage("出错了！获取数据失败", "error");
            }
        });
    },
    // 得到指定级审批下的地市信息
    getCityList: function ($chnnelId, cityId, level) {
        $.ajax({
            url: './manage/getCityList',
            type: "get",
            data: {"channelId": $chnnelId, "level": level},
            dataType: 'json',
            cache: false,
            success: function (res) {
                if (res.code != 0) {
                    showMessage(res.msg, "error");
                    return
                }
                res = res.data;
                var areaHtml = '';
                //填充地市的下拉列表
                for (var i = 0; i < res.length; i++) {
                    areaHtml += "<option id='" + res[i].CITYID + "' value='" + res[i].CITYID + "' >" + res[i].CITYNAME + "</option>";
                }
                $(".u-city2").html(areaHtml);
                if (cityId != -1) {
                    $("#area1").val(cityId);
                }
            },
            error: function () {
                showMessage("出错了！获取数据失败", "error");
            }
        });
    },
    getApproverListInfo: function () {
        var param = {};
        if ($("#search").val() == "支持ID、姓名、手机号码搜索") {
            param.searchVal = "";
        } else {
            param.searchVal = $("#search").val() || "";
        }
        param.channelId = $("#channel2").val() || "@";
        param.cityId = $("#area2").val() || 0;
        param.approverLevel = $("#priority2").val() || 0;
        param.lengthRow = 10;
        return param;
    }
};
$(document).ready(function () {
    //初始化网页顶部导航模块
    var topNav = new topNavigation();
    topNav.init();
    topNav.show("#sysManage");
    //初始化网页左侧导航模块
    var sideNav = new sideNavigation();
    sideNav.init("#sysManageAppsetting");
});
$(document).ready(function () {
    var html = "";
    //初始化审批人列表
    var initApproverTable = new InitApproverTable();
    initApproverTable.getPageData();
    approverGlobal.initApproverTable = initApproverTable;

    //动态获取渠道与城市
    approverGlobal.getDropList();

    $.ajaxSetup({
        cache: false
    })

});

$(function () {
    //ie8模拟placeholder
    $("#search").val("支持ID、姓名、手机号码搜索").css("color", "#CCD0D9");
    $("#search").focus(function () {
        if ($("#search").val() == "支持ID、姓名、手机号码搜索") {
            $(this).val("");
        }
        $(this).css("color", "black");
    }).blur(function () {
        if ($(this).val() == "") {
            $(this).val("支持ID、姓名、手机号码搜索").css("color", "#CCD0D9");
            return;
        }
    });
    // 搜索
    $("#search").keypress(function (e) {
        if (e.which == 13) {
            approverGlobal.initApproverTable.getPageData();
        }
    }).next().click(function () {
        approverGlobal.initApproverTable.getPageData();
    });
    $("#ensure").on("click", function () {
        $("#cancel").trigger("click");
        approverGlobal.initApproverTable.getPageData();
    })

    //电话号码只允许输入数字
    $(".phoneNumber").keyup(function () {
        this.value = this.value.replace(/[^\d]/g, '');
    });
    //新建、修改审批人信息，审批层级选择: 1，可选地市；审批层级：2/3，不可选地市，且前台不展示
    $(".j-approve-priority").on("change", function () {
        var val = $(this).val();

        if ($(this).attr("id") == "priority1") {
            approverGlobal.getCityList($("#channel1").val(), editCityId, val);
        } else {
            approverGlobal.getCityList($("#channel").val(), -1, val);
        }

    })
    //
    $(".u-channel2").on("change", function () {
        approverGlobal.getLevelList($(this).val(), -1, -1);
    })

})
var editCityId;
//审批人新建和修改
$(function () {
    var getFormData = function ($parent) {
        var approverInfo = {};
        var $cityBox = $parent.find(".u-city-box");
        approverInfo.channelId = $parent.find(".u-channel").val();
        approverInfo.channelName = $parent.find(".u-channel option:selected").text();
        if ($cityBox.css("display") == "block") {
            approverInfo.cityId = $parent.find(".u-city").val();
            approverInfo.cityName = $parent.find(".u-city option:selected").text();
        } else {
            approverInfo.cityId = null;
            approverInfo.cityName = null;
        }
        approverInfo.approverId = $parent.find(".appsettingId").val();
        approverInfo.approverName = $parent.find(".appsettingname").val();
        approverInfo.approverPhone = $parent.find(".phoneNumber").val();
        approverInfo.approverLevel = parseInt($parent.find(".priority").val());

        return approverInfo;
    }
    //点击新建审批人
    $(".create-new").click(function () {
        approverGlobal.getLevelList($("#channel").val(), -1, -1);
        $(".modal-wrap-new").show();
    });

    //点击保存新增审批人
    $(".modal-wrap-new .modal-box-save").click(function () {
        var params = getFormData($(".modal-wrap-new"));

        //验证数据
        if (params.approverId == "") {
            showMessage("ID不能为空", "error");
        } else if (!(/^1[34578]\d{9}$/.test(params.approverPhone))) {
            showMessage("请输入正确的手机号码", "error");
        } else {
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
                url: "./manage/addApprover",
                type: "post",
                data: JSON.stringify(params),
                dataType: "json",
                contentType: 'application/json;charset=UTF-8',
                cache: false,
                success: function (res) {
                    $.unblockUI();
                    if (res.code != 0) {
                        showMessage(res.msg, "error");
                        return
                    }
                    showMessage("提交成功", "success");
                    approverGlobal.initApproverTable.getPageData();
                    $(".modal-box-cancel").click();
                },
                error: function () {
                    $.unblockUI();
                    showMessage("提交失败", "error");
                }
            })
        }
    });

    //取消后清空
    $(".modal-box-cancel").click(function (e) {
        var $modalWrap = $(this).closest(".modal-wrap");
        e.preventDefault();
        $modalWrap.hide();
        $modalWrap.find(".u-channel").val("f01");
        if ($modalWrap.find(".u-city-box").css("display") == "none") {
            $modalWrap.find(".u-city-box").show();
        }
        $modalWrap.find(".u-city").val(1);
        $modalWrap.find(".priority").val(1);
        $modalWrap.find(".j-input-clear").val("");

        if ($modalWrap == $(".modal-wrap-edit")) {
            $modalWrap.removeData("curUUId");
        }
    });

    //保存修改
    $(".modal-wrap-edit .modal-box-save").click(function (e) {
        //获取表单中的值
        var $parent = $(".modal-wrap-edit")
        var params = getFormData($parent);
        params.uuid = $parent.data("curUUId");
        //验证数据
        if ($parent.find(".u-city-box").css("display") == "block") {
            if (!params.cityId) {
                showMessage("请选择地市", "error");
                return
            }
        }

        if (isNaN(params.approverLevel)) {
            showMessage("请选择层级", "error");
            return
        }
        if (params.approverId == "") {
            showMessage("ID不能为空", "error");
        } else if (!(/^1[34578]\d{9}$/.test(params.approverPhone))) {
            showMessage("请输入正确的手机号码", "error");
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
                url: "./manage/setApprover",
                type: "post",
                data: JSON.stringify(params),
                dataType: "json",
                contentType: 'application/json;charset=UTF-8',
                cache: false,
                success: function (res) {
                    $.unblockUI();
                    if (res.code != 0) {
                        showMessage(res.msg, "error");
                        return
                    }
                    showMessage("提交成功", "success");
                    approverGlobal.initApproverTable.getPageData();
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


//审批人列表
var InitApproverTable = function () {
    var me = this;
    var approverTable = new Object();
    approverTable.getPageData = function () {
        var params = approverGlobal.getApproverListInfo();
        params.currentPage = 1;
        //对应方法
        $.ajax({
            url: "./manage/getApproverInfoList",
            type: 'get',
            data: params,
            dataType: 'json',
            success: function (res) {
                if (res.code != 0) {
                    showMessage(res.msg, "error");
                    return
                }
                fillTable(res.data.approverList, res.data.userRole);
                pagination(res.data.totals);
                $("#activities_num").text("(" + res.data.totals + ")");
            },
            error: function () {
                showMessage("获取数据失败", "error");
            }

        })
    };
    var pagination = function (totals) {
        //var params = getParams();
        var pagerBox = document.getElementById('page');
        $(pagerBox).html("");
        var pager = new Pager({
            index: 1,
            total: Math.ceil(totals / 10),
            parent: pagerBox,
            onchange: doChangePage
        });

        function doChangePage(obj) {
            var params = approverGlobal.getApproverListInfo();
            params.currentPage = obj.index;

            $.ajax({
                url: "./manage/getApproverInfoList",
                type: 'get',
                data: params,
                dataType: 'json',
                success: function (res) {
                    if (res.code != 0) {
                        showMessage(res.msg, "error");
                        return
                    }
                    fillTable(res.data.approverList, res.data.userRole);
                },
                error: function () {
                    showMessage("获取数据失败", "error");
                }

            })
        }
    };
    var fillTable = function (param, userRole) {
        var nowDate = formatDate(new Date(), "yyyy-MM");
        var data = param;
        var html = '', htmlHead = "";
        var isCanEdit = userRole.indexOf("admin") == -1 ? false : true;

        if (isCanEdit) {
            htmlHead += "<tr>";
            htmlHead += "<td style='width: 5%;' >&nbsp;</td>";
            htmlHead += "<td >渠道</td>";
            htmlHead += "<td >地市</td>";
            htmlHead += "<td>优先级</td>";
            htmlHead += "<td>审批人ID</td>";
            htmlHead += "<td>审批人姓名</td>";
            htmlHead += "<td>电话号码</td>";
            htmlHead += "<td>操作</td>";
            htmlHead += "</tr>";
        } else {
            htmlHead += "<tr>";
            htmlHead += "<td style='width: 5%;' >&nbsp;</td>";
            htmlHead += "<td >渠道</td>";
            htmlHead += "<td >地市</td>";
            htmlHead += "<td>优先级</td>";
            htmlHead += "<td>审批人ID</td>";
            htmlHead += "<td>审批人姓名</td>";
            htmlHead += "<td>电话号码</td>";
            // htmlHead += "<!--  <td>操作</td>-->";
            htmlHead += "</tr>";
        }

        if (!data || !data.length) {
            if (isCanEdit) {
                html = "<tr><td colspan='8'>暂无数据</td></tr>"
            } else {
                html = "<tr><td colspan='7'>暂无数据</td></tr>"
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
                if (!data[i].cityName) {
                    html += '<td>' + "无" + '</td>';
                } else {
                    html += '<td>' + data[i].cityName + '</td>';
                }
                html += '<td>' + data[i].approverLevel + '</td>';
                html += '<td>' + data[i].approverId + '</td>';
                html += '<td>' + data[i].approverName + '</td>';
                html += '<td>' + data[i].approverPhone + '</td>';
                if (isCanEdit) {
                    html += '<td data-uuid="' + data[i].uuid + '" data-channelId="' + data[i].channelId + '" data-cityid="' + data[i].cityId + '" data-approverLevel="' + data[i].approverLevel + '" data-approverId="' + data[i].approverId + '" data-approverName="' + data[i].approverName + '" data-approverPhone="' + data[i].approverPhone + '" >';
                    html += '<a href="#" title="修改" class="operate edit-icon"></a>';
                    html += '<a href="#" title="删除" class="operate delete04-icon" data-id="' + data[i].approverId + '" data-name="' + data[i].approverName + '" ></a>';
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
            var cityId = $parent.attr("data-cityid");
            var approverLevel = $parent.attr("data-approverLevel");
            var approverId = $parent.attr("data-approverId");
            var approverName = $parent.attr("data-approverName");
            var approverPhone = $parent.attr("data-approverPhone");
            var uuid = $parent.attr("data-uuid");
            approverGlobal.getLevelList(channelId, approverLevel, cityId);
            $modalBox.show();
            $modalBox.data("curUUId", uuid);
            editCityId = cityId;
            $("#channel1").val(channelId);


            $("#appsettingId").val(approverId);
            $("#appsettingName").val(approverName);
            $("#phoneNumber").val(approverPhone);

        });
        $(".delete04-icon").click(function (e) { //删除
            e.preventDefault();
            var $parent = $(this).parent();
            var uuid = $parent.attr("data-uuid");
            var ensureDelete = function () {
                $.ajax({
                    url: "./manage/deleteApprover",
                    type: 'post',
                    data: {"uuid": uuid},
                    dataType: 'json',
                    cache: false,
                    success: function (res) {
                        if (res.code == 0) {
                            showMessage("操作成功", "success", 1500);
                            //刷新页面
                            approverGlobal.initApproverTable.getPageData();
                        } else {
                            showMessage("失败，" + res.msg, "error");
                        }
                    },
                    error: function () {
                        showMessage("删除失败", "error");
                    }
                })
            }
            showConfirm("确定要删除此条数据吗？", ensureDelete);
        });
    }

    approverTable.query = function () {
        getPageData();
    }
    return approverTable;
}






