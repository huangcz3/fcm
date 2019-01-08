//任务列表
var InitTaskTable = function () {
    var taskTable = new Object();
    taskTable.getParams = function () {
        var params = {};
        params.startTime = $("#startDate").val();
        params.endTime = $("#endDate").val();
        params.currentPage = 1;
        params.pageSize = 10;
        params.keyword = $("#search").val();
        params.cityId = $("#area").val() ? $("#area").val() : "1";
        return params;
    };
    taskTable.getPageData = function () {
        var params = taskTable.getParams();
        $.ajax({
            url: "./tasks/getAllTasks",
            type: 'get',
            data: {
                startTime: params.startTime,
                endTime: params.endTime,
                currentPage: params.currentPage,
                pageSize: params.pageSize,
                keyword: params.keyword,
                cityId: params.cityId
            },
            dataType: 'json',
            success: function (res) {
                if (res.code != 0) {
                    showMessage(res.msg, "error");
                    return
                }
                fillTable(res.data.dataList);
                pagination(res.data.totalRecord);
                $("#activities_num").text("(" + res.data.totalRecord + ")");
            },
            error: function () {
                showMessage("获取数据失败", "error");
            }

        })
    };
    var pagination = function (totals) {
        var params = taskTable.getParams();
        var pagerBox = document.getElementById('page');
        $(pagerBox).html("");
        var pager = new Pager({
            index: 1,
            total: Math.ceil(totals / 10),
            parent: pagerBox,
            onchange: doChangePage
        });

        function doChangePage(obj) {
            params.currentPage = obj.index;
            $.ajax({
                url: "./tasks/getAllTasks",
                type: 'get',
                data: {
                    startTime: params.startTime,
                    endTime: params.endTime,
                    currentPage: params.currentPage,
                    pageSize: params.pageSize,
                    keyword: params.keyword,
                    cityId: params.cityId
                },
                dataType: 'json',
                success: function (res) {
                    if (res.code != 0) {
                        showMessage(res.msg, "error");
                        return
                    }
                    fillTable(res.data.dataList);
                },
                error: function () {
                    showMessage("获取数据失败", "error");
                }

            })
        }
    };
    var fillTable = function (param) {
        var data = param;
        var html = '';
        if (!data || !data.length) {
            html = "<tr><td colspan='8'>暂无数据</td></tr>"
        } else {
            for (var i = 0; i < data.length; i++) {
                html += '<tr>';
                html += '<td>';
                html += '<p>';
                html += '<i class="icon"></i>';
                html += '</p>';
                html += '</td>';
                html += '<td class="text-left" title="' + data[i].taskName + '">' + data[i].taskName + '</td>';
                html += '<td>' + data[i].taskId + '</td>';
                html += '<td>' + data[i].cityName + '</td>';
                html += '<td>' + data[i].creatorName + '</td>';
                html += '<td>' + taskGlobalFuncs.transDate(data[i].createTime, "-") + '</td>';
                html += '<td>' + data[i].relativeActivityNum + '</td>';
                html += '<td  data-taskid="' + data[i].taskId + '">';
                html += '<a href="#" title="详情" class="operate detail-modal-icon"></a>';
                if (data[i].isCanDelete == 1) {
                    html += '<a href="#" title="修改" class="operate edit-icon"></a>';
                    html += '<a href="#" title="删除" class="operate delete04-icon"></a>';
                } else {
                    html += '<a href="#" title="修改" class="operate edit-icon visible-hidden"></a>'
                    html += '<a href="#" title="删除" class="operate delete04-icon visible-hidden"></a>';
                }
                html += '</td>';
                html += '</tr>';
            }
        }

        $("#table tbody").html(html);

        initTableStyle();
        initOperateEvent();
        initTableHover();
    };
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
    var initOperateEvent = function () {
        /**
         * 营销活动列表操作事件
         */

        $(".detail-modal-icon").click(function () {  //详情
            var taskId = $(this).parent().attr("data-taskid");
            $(".modal-wrap-detail").show();
            $.ajax({
                url: "./tasks/getTaskDetail?taskId=" + taskId,
                type: "get",
                dataType: "json",
                success: function (res) {
                    if (res.code != 0) {
                        showMessage(res.msg, "error");
                        return
                    }
                    //展示
                    var details = res.data.taskDetail;
                    var relActs = res.data.relativeActs;
                    for (var k in details) {
                        var id = "#" + k + "_d"
                        if ($(id).length) {
                            $(id).text(details[k]);
                        }
                    }
                    if (relActs.length) {
                        var acts = [];
                        $.each(relActs, function (i, item) {
                            acts.push(item.activityName);
                        })
                        $("#relativeActs").text(acts.join("，"))
                    }
                },
                error: function () {
                    showMessage("获取数据失败", "error");
                }

            })
        });
        $(".delete04-icon").click(function (e) {
            e.preventDefault();
            var taskId = $(this).parent().attr("data-taskid");
            var deleteTaskFunc = function () {
                $.blockUI.defaults.overlayCSS.backgroundColor = '#fff';
                $.blockUI.defaults.overlayCSS.opacity = '0.8';
                $.blockUI({
                    message: "任务删除中，请稍等...",
                    css: {
                        border: "none",
                        fontSize: "16px"
                    }
                });
                $.ajax({
                    url: "./tasks/delete?taskId=" + taskId,
                    type: "post",
                    contentType: "application/json;charset=UTF-8",
                    dataType: "json",
                    success: function (res) {
                        $.unblockUI();
                        if (res.code != 0) {
                            showMessage(res.msg, "error");
                            return
                        }
                        showMessage("操作成功", "success");
                        taskTable.getPageData();
                    },
                    error: function () {
                        $.unblockUI();
                        showMessage("操作失败", "error");
                    }
                })
            }
            showConfirm("确定删除该任务吗？", deleteTaskFunc);
        });

        // 修改
        $(".edit-icon").click(function (e) {
            taskId = $(this).parent().attr("data-taskid");
            $(".modal-wrap-new").show();
            $("#windowName").html("修改任务");

            var bussinessTypeId;
            var marketingPurposeId;
            $.ajax({
                url: "./tasks/getTaskDetail?taskId=" + taskId,
                type: "get",
                async:false,
                dataType: "json",
                success: function (res) {
                    if (res.code != 0) {
                        showMessage(res.msg, "error");
                        return
                    }
                    //展示
                    var details = res.data.taskDetail;
                    var relActs = res.data.relativeActs;
                    for (var i = 0; i < relActs.length; i++) {
                        var tag = {
                            "id": relActs[i].activityId,
                            "text": relActs[i].activityName
                        };
                        $("#chosedTasks").tagsinput('add', tag);
                    }

                    $("#taskName").val(details.taskName);
                    bussinessTypeId = details.businessTypeId;
                    marketingPurposeId = details.marketingPurposeId;
                    $.ajax({
                        url: "./activities/getActBaseInit",
                        type: 'get',
                        async:false,
                        dataType: 'json',
                        success: function (res) {
                            if (res.code != 0) {
                                showMessage(res.msg, "error");
                                return;
                            }
                            var businessTypeList = res.data.businessTypeList;
                            var marketingPurposeList = res.data.marketingPurposeList;
                            var html1 = "", html2 = "";
                            $.each(businessTypeList, function (i, item) {
                                html1 += "<option value='" + item.businessTypeId + "'>" + item.businessTypeName + "</option>";
                            })
                            $("#bussinessType").html(html1);
                            $("#bussinessType").val(bussinessTypeId);
                            $.each(marketingPurposeList, function (i, item) {
                                html2 += "<option value='" + item.marketingPurposeId + "'>" + item.marketingPurposeName + "</option>";
                            })
                            $("#marketingPurpose").html(html2);
                            $("#marketingPurpose").val(marketingPurposeId);
                        },
                        error: function () {
                            showMessage("获取信息失败", "error");
                        }
                    });
                },
                error: function () {
                    showMessage("获取数据失败", "error");
                }
            });



        });

    }
    $("#search").keypress(function (e) {
        if (e.keyCode == 13)
            taskTable.getPageData();
    });
    $("#search").next().click(function () {
        taskTable.getPageData();
    })
    $("#ensure").click(function () {
        taskTable.getPageData();
        $("#cancel").trigger("click");
    });

    return taskTable;
}
//创建任务-活动关联列表
var initRelativeActivity = (function () {
    var relativeActivity = new Object();
    var getParams = function () {
        var params = {
            currentPage: 1,
            pageSize: 5,
        }
        params.keyword = $(".j-rel-search").val();
        return params;
    }

    var getTableData = function () {
        var params = getParams();
        $.ajax({
            url: "./tasks/getActivity4Task?keyword=" + params.keyword + "&currentPage=" + params.currentPage + "&pageSize=" + params.pageSize,
            type: "get",
            dataType: "json",
            success: function (res) {
                if (res.code != 0) {
                    showMessage(res.msg, "error");
                    return;
                }
                fillTable(res.data.activityList);
                pagination(res.data.totalRecord);
            },
            error: function () {
                showMessage("获取数据失败", "error");
            }
        });
    }
    var pagination = function (totals) {
        var pagerBox = document.getElementById('pager');
        $(pagerBox).html("");
        var pager = new Pager({
            index: 1,
            total: Math.ceil(totals / 5),
            parent: pagerBox,
            onchange: doChangePage
        });
        $("#pager .cur").before("<span>第</span>");
        $("#pager .total-page").after("<span>页,共" + (totals || "0") + "条</span>");

        function doChangePage(obj) {
            var params = getParams();
            params.currentPage = obj.index;
            $.ajax({
                url: "./tasks/getActivity4Task?keyword=" + params.keyword + "&currentPage=" + params.currentPage + "&pageSize=" + params.pageSize,
                type: 'get',
                dataType: 'json',
                success: function (res) {
                    if (res.code != 0) {
                        showMessage(res.msg, "error");
                        return
                    }
                    fillTable(res.data.activityList);
                },
                error: function () {
                    showMessage("获取数据失败", "error");
                }
            })
        }
    };
    var fillTable = function (data) {
        var html = '';
        if (!data || !data.length) {
            html = "<tr><td colspan='3'>暂无数据</td></tr>"
        } else {
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

        $('#relativeTable tbody').html(html);
        initCheckbox();
    }
    var initCheckbox = function () {
        $("#relativeTable .checkbox label span").click(function (e) {
            $(this).toggleClass("selected");
            var isChecked = $(this).hasClass("selected");
            isChecked ? $(this).prev("input").attr("checked", "checked") : $(this).prev("input").removeAttr("checked");
        });
    }

    $(".j-rel-search").keypress(function (e) {
        if (e.keyCode == 13)
            getTableData();
    });
    $(".j-rel-search").next().click(function () {
        getTableData();
    })

    relativeActivity.query = function () {
        getTableData();
    }

    return relativeActivity;
})();

var taskGlobalFuncs = (function () {
    var funcs = {};
    funcs.getSaveInfo = function () {
        var infoMap = {};
        var relActs = $("#chosedTasks").tagsinput("items");
        infoMap.taskName = $("#taskName").val();
        infoMap.businessTypeId = $("#bussinessType").val();
        infoMap.businessTypeName = $("#bussinessType").find("option:selected").text();
        infoMap.marketingPurposeId = $("#marketingPurpose").val();
        infoMap.marketingPurposeName = $("#marketingPurpose").find("option:selected").text();
        infoMap.activityList = [];
        if (relActs.length) {
            $.each(relActs, function (i, item) {
                var actInfo = {};
                actInfo.activityId = item.id;
                actInfo.activityName = item.text;
                infoMap.activityList.push(actInfo);
            })
        }

        return infoMap;
    }
    funcs.fillRequired = function (params) {
        var result = true;
        for (var k in params) {
            if (k == "activityList") {
                if (!params[k].length) {
                    result = false;
                    showMessage("请将必要信息填写完整", "error");
                    return result;
                }
            } else {
                if (!params[k]) {
                    result = false;
                    showMessage("请将必要信息填写完整", "error");
                    return result;
                }
            }
        }
        return result;
    }
    funcs.transDate = function (param, split) { //transform   2017-09-27 15:17:03.252	 to   2017-09-27
        /*	var date = new Date(param);
         var year = date.getFullYear();
         var month = date.getMonth()+1;
         var day = date.getDate();
         return year+split+month+split+day*/

        return param.substring(0, 10);
    }

    return funcs;
})();

var taskGlobalAttrs = {};
var taskId = "";
$(function () {
    //点击新建模型按钮，弹出模态框
    $(".panel-title .rt .create-new").click(function () {
        $("#windowName").html("新建任务")
        taskId = "";
        $(".modal-wrap-new").show();
        //请求获取新建模型模态框页面信息
        $.ajax({
            url: "./activities/getActBaseInit",
            type: 'get',
            dataType: 'json',
            success: function (res) {
                if (res.code != 0) {
                    showMessage(res.msg, "error");
                    return;
                }
                var businessTypeList = res.data.businessTypeList;
                var marketingPurposeList = res.data.marketingPurposeList;
                var html1 = "", html2 = "";
                $.each(businessTypeList, function (i, item) {
                    html1 += "<option value='" + item.businessTypeId + "'>" + item.businessTypeName + "</option>";
                })
                $("#bussinessType").html(html1);
                $.each(marketingPurposeList, function (i, item) {
                    html2 += "<option value='" + item.marketingPurposeId + "'>" + item.marketingPurposeName + "</option>";
                })
                $("#marketingPurpose").html(html2);

            },
            error: function () {
                showMessage("获取信息失败", "error");
            }
        })
    });

    $('.checkbox-icon').click(function () {
        $(this).toggleClass("checked");
        if ($(this).hasClass('checked')) {
            $(this).siblings("input").attr("checked", true);
        } else {
            $(this).siblings("input").attr("checked", false);
        }
    });

    // 水平tab事件
    $(".h-tab li").click(function (event) {
        if ($(this).find("span").hasClass("selected")) {
            return;
        }
        $(this).parent().find("span").removeClass("selected");
        $(this).find("span").addClass("selected");
        var num = $(this).parent().children().index(this);
        var $content = $(this).parent().next()
            .find("li").eq(num).addClass("selected").siblings().removeClass("selected");
    });// end

    $(".expand-all").click(function () {
        var tab = $(this).attr("data-bindtab");
        if ($(this).text() == "展开全部") {

            $("#" + tab).find(".data-list").removeClass("h-110");
            $(this).html("收起");
        } else {
            $("#" + tab).find(".data-list").addClass("h-110");
            $(this).html("展开全部");
        }
    })//end

    //任务关联列表展开切换
    $("#taskRelativeAct .number-icon").click(function (e) {
        e.preventDefault();
        var $parent = $(this).parent();
        $("#taskRelativeAct .popup-box").toggleClass("show");
        $("#taskRelativeAct .li-wrap").toggleClass("on");
        if ($parent.hasClass("on")) {  //展开弹窗，滚动条滚动至底部
            var scrollH = 0;
            var scrollDivs = $(".modal-wrap-new .modal-box-content .content-item-new-modal>div");
            $.each(scrollDivs, function (i, item) {
                scrollH += $(item).height();
            })
            $(".modal-wrap-new .modal-box-content").slimScroll({
                scrollTo: scrollH
            });
        }

        $("#taskRelativeAct .bottom-shadow-cover").toggleClass("show");
        if (!$("#chooseActivity").attr("data-isCreated")) {
            initRelativeActivity.query();
            $("#chooseActivity").attr("data-isCreated", "created");
        }
    });
    // 关联活动列表取消事件
    $(".j-tab-cancel").click(function (e) {
        e.preventDefault();
        var $grandpa = $(this).closest(".modal-item");
        $grandpa.removeClass("show");
        $grandpa.siblings(".li-wrap").removeClass("on")
            .siblings(".bottom-shadow-cover").removeClass("show");
    });
    //新建任务保存
    $("#saveModel").click(function (e) {
            e.preventDefault();
            var params = taskGlobalFuncs.getSaveInfo();
            var result = taskGlobalFuncs.fillRequired(params);
            if (!result) {
                return
            }
            $.blockUI.defaults.overlayCSS.backgroundColor = '#fff';
            $.blockUI.defaults.overlayCSS.opacity = '0.8';

            if (taskId == "") {
                $.blockUI({
                    message: "新建任务保存中，请稍等...",
                    css: {
                        border: "none",
                        fontSize: "16px"
                    }
                });
                $.ajax({
                    url: "./tasks/add",
                    type: "post",
                    data: JSON.stringify(params),
                    contentType: "application/json;charset=UTF-8",
                    dataType: "json",
                    success: function (res) {
                        $.unblockUI();
                        if (res.code != 0) {
                            showMessage(res.msg, "error");
                            return
                        }
                        showMessage("保存任务成功", "success");
                        taskGlobalAttrs.InitTask.getPageData();
                        $(".modal-box-cancel").trigger("click");
                    },
                    error: function () {
                        $.unblockUI();
                        showMessage("保存任务失败", "error");
                    }
                })
            }else {
                $.blockUI({
                    message: "修改任务保存中，请稍等...",
                    css: {
                        border: "none",
                        fontSize: "16px"
                    }
                });
                $.ajax({
                    url: "./tasks/edit?taskId="+taskId,
                    type: "post",
                    data: JSON.stringify(params),
                    contentType: "application/json;charset=UTF-8",
                    dataType: "json",
                    success: function (res) {
                        $.unblockUI();
                        if (res.code != 0) {
                            showMessage(res.msg, "error");
                            return
                        }
                        showMessage("保存任务成功", "success");
                        taskGlobalAttrs.InitTask.getPageData();
                        $(".modal-box-cancel").trigger("click");
                    },
                    error: function () {
                        $.unblockUI();
                        showMessage("保存任务失败", "error");
                    }
                })
            }
        }
    );
    //新建模态框，取消后清空
    $(".modal-box-cancel").click(function (e) {
        e.preventDefault();
        $(".j-input-clear").val("");
        $(".j-tags-clear").tagsinput("removeAll");
        $(".j-iscreated-clear").removeAttr("data-iscreated");
        if ($("#activitiesPopup").hasClass("show")) {
            $("#chooseActivity .li-wrap").removeClass("on");
            $("#chooseActivity .bottom-shadow-cover").removeClass("show");
            $("#activitiesPopup").removeClass("show");
        }
        $(".modal-wrap .modal-box-content").slimScroll({
            scrollTo: "0px"
        });
    })
    //下载任务列表
    $("#download").on("click",function () {
        var params = taskGlobalAttrs.InitTask.getParams();
        window.location.href = './tasks/getTaskListDownload?searchVal='+params.keyword+
            '&startTime='+params.startTime+
            '&endTime='+params.endTime+
            '&cityId='+params.cityId;
    })
})


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
    var $temp = $(".data-list label").filter(function (index, value) {
        if ($(value).text().trim() == tag)
            return true;
    });
    $temp.removeClass("selected")
    $temp.find("span").removeClass("selected");
    $temp.prev().removeAttr("checked")
};
function tagsOperate() {
    this.initTagsIput = function (id) {
        //初始化tagsinput
        var $input = $(id);
        $input.tagsinput({
            itemValue: 'id',
            itemText: 'text',
        });
    }
}
tagsOperate.prototype.tableTagsinputEvent = function (gid, pid, tid) { //gid是弹出框所在的父级元素ID，pid是table所在的tab内容框的id,tid是tagsinput的input测id,事件代理
    $(gid).on('click', pid + ' .ensure', function () {
        var $table = $(pid).find("table");
        var checked = $table.find("tr td:first-child input[checked]");
        var tagsArr = [];
        $.each(checked, function (i, item) {
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

$(document).ready(function () {
    //初始化网页顶部导航模块
    var topNav = new topNavigation();
    topNav.init();
    topNav.show("#marketingAct");
    //初始化网页左侧导航模块
    var sideNav = new sideNavigation();
    sideNav.init();
    sideNav.show("#vicTaskManage");
});

$(document).ready(function () {
    //初始化营销活动列表
    var param;
    var InitTask = new InitTaskTable();
    taskGlobalAttrs.InitTask = InitTask;

    //获取地市下拉列表
    $.ajax({
        url: "./tasks/getCityList",
        type: "get",
        dateType: "json",
        success: function (res) {
            if (res.code != 0) {
                showMessage(res.msg, "error");
                return
            }
            var html = "";
            if (!res.data || !res.data.length) {
                return
            }
            $.each(res.data, function (i, item) {
                html += "<option value='" + item.cityId + "'>" + item.cityName + "</option>"
            })
            $("#area").html(html);

        },
        error: function () {
            showMessage("获取数据失败", "error");
        }
    })

    InitTask.getPageData();

    var scrollH = ($(window).height() - 200) + "px";
    $(".modal-box-content").slimScroll({
        height: scrollH,
        color: "#a2a8af",
        alwaysVisible: true,
        size: "5px",
    })
    //初始化活动关联列表活动选择tagsinput事件
    var relateActivity = new tagsOperate();
    relateActivity.initTagsIput("#chosedTasks");
    relateActivity.tableTagsinputEvent("#chooseActivity", "#activityTableBox", "#chosedTasks");
});




