/**
 * Created by Administrator on 2017/9/27.
 */

var quotaGlobal = {};

var queryStr;

//任务列表
var InitChannelTable = function () {
    var channelTable = new Object();
    var getParams = function () {
        var params = {};
        params.currentPage = 1;
        params.pageSize = 10;
        return params;
    };
    var getPageData = function (queryStr) {

        var params = getParams();
        $.ajax({
            url: "./customerGroups/getChannelSuggestList",
            type: 'get',
            data: {
                queryStr: queryStr,
                currentPage: params.currentPage,
                pageSize: params.pageSize,
            },
            dataType: 'json',
            success: function (res) {
                if (res.code != 0) {
                    showMessage(res.msg, "error");
                    return
                }
                fillTable(res.data.resultList);
                pagination(res.data.allTotals);
            },
            error: function () {
                showMessage("获取数据失败", "error");
            }

        })
    };
    var pagination = function (totals) {
        var num = "("+totals+")";
        $("#num").html(num);
        var params = getParams();
        var pagerBox = document.getElementById('page');
        $(pagerBox).html("");
        var pager = new Pager({
            index: 1,
            total: Math.ceil(totals / 10),
            parent: pagerBox,
            onchange: doChangePage
        });

        function doChangePage(obj) {
            $.ajax({
                url: "./customerGroups/getChannelSuggestList",
                type: 'get',
                data: {
                    queryStr: queryStr,
                    currentPage: obj.index,
                    pageSize: params.pageSize,
                },
                dataType: 'json',
                success: function (res) {
                    if (res.code != 0) {
                        showMessage(res.msg, "error");
                        return
                    }
                    fillTable(res.data.resultList);
                },
                error: function () {
                    showMessage("获取数据失败", "error");
                }

            })
        }
    };
    var fillTable = function (param) {
        var data = param;
        var html = '', htmlHead = "";
        htmlHead += "<tr>";
        htmlHead += "<td width='5%'>&nbsp;</td>";
        htmlHead += "<td width='30%'>文件名</td>";
        htmlHead += "<td class='text-align-left' width='12%'>总记录数</td>";
        htmlHead += "<td class='text-align-left' width='12%'>有效用户数</td>";
        htmlHead += "<td>上传时间</td>";
        htmlHead += "<td>计算完成时间</td>";
        htmlHead += "<td width='10%'>状态</td>";
        htmlHead += "<td width='10%'>操作</td>";
        htmlHead += "</tr>";


        if (!data || !data.length) {
            html = "<tr><td colspan='7'>暂无数据</td></tr>"
        } else {
            var flag = 0;
            for (var i = 0; i < data.length; i++) {
                var status;
                var operate = "无";
                if (data[i].STATUS== "0") {
                    status = '<span class="gery-blue">'+'计算中'+'</span>';
                } else if (data[i].STATUS== "2") {
                    operate ='<a href="#" title="详情" csid="'+data[i].CUSTOMER_GROUP_ID+'" class="operate detail-modal-icon"></a>'
                    status = '<span class="green">'+'计算完成'+'</span>';
                } else if (data[i].STATUS== "-1") {
                    status = '<span class="grey">'+'无有效数据'+'</span>';
                } else {
                    flag = 1;
                    operate ='<a href="#" title="重试" csid="'+data[i].CUSTOMER_GROUP_ID+'" class="operate start-icon"></a>'
                    status = '<span class="red">'+'计算失败'+'</span>'
                }

                html += '<tr>';
                html += '<td  width="5%"><b class="closed"></b><i></i></td>';
                html += '<td class="text-align-left">&nbsp;&nbsp;&nbsp;' + data[i].ORIGINAL_FILE_NAME + '</td>';
                html += '<td class="text-align-left">' + (data[i].ALL_AMCOUNT?'<span class="orange">'+data[i].ALL_AMCOUNT+'</span>':'<span class="gery-blue">'+'计算中'+'</span>') + '</td>';
                html += '<td class="text-align-left">' + (data[i].EFFECTIVE_AMCOUNT?'<span class="red">'+data[i].EFFECTIVE_AMCOUNT+'</span>':'<span class="gery-blue">'+'计算中'+'</span>') + '</td>';
                html += '<td >' + data[i].START_TIME + '</td>';
                html += '<td>' + (data[i].COMPLETE_TIME?data[i].COMPLETE_TIME:"无") + '</td>';
                html += '<td>' + status + '</td>';
                html += '<td>' + operate + '</td>';
                html += '</tr>';
            }
        }
        $("#table thead").html(htmlHead);
        $("#table tbody").html(html);

        initTableStyle();
        initTableHover();
        if (flag == 1) {
            initReStart();
        }
        initDownload();
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
        var imgPath = 'img/';
        var icons = [ "activity_red.png","activity_yellow.png", "activity_green.png","activity_blue.png"];
        imgTds = $("#table td>i");
        for(var i=0;i<imgTds.length;i++){
            $(imgTds[i]).css("background","url("+imgPath+icons[i%4]+")");
        }
        var table_h = parseInt($(".inner-content").css("height"))-20-39-40-20;
        $("#table td").css("height",(table_h/11)+"px");
        $("#table tbody tr:odd").addClass("even");

    };

    var initReStart = function(){  //表格操作
        $(".start-icon").click(function(){  //重试
            var csid = $(this).attr("csid");
            var update = function(){    //重试
                $.blockUI.defaults.overlayCSS.backgroundColor = '#fff';
                $.blockUI.defaults.overlayCSS.opacity = '0.8';
                $.blockUI({
                    message:"正在提交重试请求，请稍等...",
                    css:{
                        border:"none",
                        fontSize:"16px"
                    }
                });

                $.ajax({
                    url: './customerGroups/reStartChannelSuggest',
                    type:'get',
                    dataType:'json',
                    cache:false,
                    data:{'customerGroupId':csid},
                    success:function(res){
                        if(res.code == 0){
                            $.unblockUI();
                            showMessage("提交成功,计算完成后将以短信告知","success",1500);
                            //刷新页面
                            getPageData("");
                        }else{
                            $.unblockUI();
                            showMessage(res.msg,"error");
                        }
                    },
                    error:function(){
                        $.unblockUI();
                        showMessage("重试失败","error");
                    }
                });
            }
            showConfirm("确定要重新尝试计算吗？",update);
        })
    }

    var initDownload = function(){  //表格操作
        $(".detail-modal-icon").click(function() {  //下载数据
            $.blockUI.defaults.overlayCSS.backgroundColor = '#fff';
            $.blockUI.defaults.overlayCSS.opacity = '0.8';
            $.blockUI({
                message:"加载数据中，请稍等...",
                css:{
                    border:"none",
                    fontSize:"16px"
                }
            });
            var $modalBox = $(".modal-wrap-edit");
            var csid = $(this).attr("csid");
            $.ajax({
                url: './customerGroups/getChannelSuggestDownloadDetail',
                type:'get',
                dataType:'json',
                cache:false,
                data:{'customerGroupId':csid},
                success:function(res){
                    $.unblockUI();
                    if(res.code == 0){
                        var param = res.data.channelList;
                        var total = res.data.total;
                        for (var i=0; i<param.length; i++) {
                            if ($('#'+param[i].CSID)!=null) {
                                $('#'+param[i].CSID+" .filedowload").attr("flag",csid+'&channel='+param[i].CSID);
                                $('#'+param[i].CSID+" .costomer").text(param[i].COU);
                                var scale = (param[i].COU / total *100)+"";
                                $('#'+param[i].CSID+" .scale").text(scale.substr(0,scale.length>5?5:scale.length)+'%');
                            }
                        }
                        $(".filedowload").click(function () {
                            if ($(this).attr("flag") != null) {
                                window.open("./customerGroups/downloadChannelSuggest?customerGroupId="+$(this).attr("flag"));
                                $(this).removeAttr("flag");
                            }
                        });
                        $modalBox.show();
                    }else{
                        showMessage(res.msg,"error");
                    }
                },
                error:function(){
                    $.unblockUI();
                    showMessage("未知错误","error");
                }
            });
        });
    };

    channelTable.query = function (queryStr) {
        getPageData(queryStr);
    };

    return channelTable;
}

$(document).ready(function () {
    //初始化网页顶部导航模块
    var topNav = new topNavigation();
    topNav.init();
    topNav.show("#marketingAct");
    //初始化网页左侧导航模块
    var sideNav = new sideNavigation();
    sideNav.init();
    sideNav.show("#vicChannelSuggest");
});


$(document).ready(function () {
    var channelTalbe = new InitChannelTable();
    channelTalbe.query("");

    var ensureUpload = function(){
        $.blockUI.defaults.overlayCSS.backgroundColor = '#fff';
        $.blockUI.defaults.overlayCSS.opacity = '0.8';
        $.blockUI({
            message:"文件上传中，请稍等...",
            css:{
                border:"none",
                fontSize:"16px"
            }
        });
        $.ajaxFileUpload({
            url: "./files/uploadChannelSuggest",
            fileElementId: "file",
            secureuri: false,
            dataType:'json',
            type: "post",
            async: false,
            success:function(res){
                $.unblockUI();
                if(res.code != 0){
                    showMessage(res.msg,"error");
                    return
                }
                showMessage("上传成功，计算完后将以短信告知","success");
                channelTalbe.query("");
            },
            error:function(){
                $.unblockUI();
                showMessage("上传失败","error");
            },
            complete: function(xmlHttpRequest) {
                //这里将input替换掉重新添加change事件
                $("#file").replaceWith('<input type="file" id="file" name="file" style="display:none;"/>');
                $("#file").change(function () {
                    var fileName = $("#file").val();
                    if(fileName.substr(fileName.length-4) != ".txt") {
                        showMessage("请选择.txt文件","error");
                    } else {
                        showConfirm("即将上传文件,确定吗？", ensureUpload);
                    }
                });
            },
        })
    };

    $("#file").change(function () {
        var fileName = $("#file").val();
        if(fileName.substr(fileName.length-4) != ".txt") {
            showMessage("请选择.txt文件","error");
        } else {
            showConfirm("即将上传文件,确定吗？", ensureUpload);
        }
    });

    $("#upload").click(function () {
        $("#file").click();
    });

    // 搜索
    $("#search").keypress(function(e) {
        if (e.which == 13) {
            queryStr = $("#search").val();
            channelTalbe.query(queryStr);
        }
    }).next().click(function(e) {
        queryStr = $("#search").val();
        channelTalbe.query(queryStr);
    });

    $(".modal-box-cancel").click(function (e) {
        e.preventDefault();
        $(".filedowload").each(function (e) {
            $(this).removeAttr("flag");
        })
        $(".costomer").each(function (e) {
            $(this).text("0");
        })
        $(".scale").each(function (e) {
            $(this).text("0%")
        })
        $(".modal-wrap").hide();
    })

})