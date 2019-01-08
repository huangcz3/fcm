/**
 * Created by Administrator on 2017/11/1.
 */
var my = {
    taskEvalution: {},
    transDecimal: function (d) {
        return (d*100).toFixed(2);
    }
}
//活动列表
var taskEvalutionTable = function(){
    var me = this;
    this.getPageData = function(){    //获取表格信息
        var param = me.getDropList();
        param.currentPage = 1;
        $.ajax({
            url: './taskEvalution/getTaskEvalutionList',
            type:'get',
            data:param,
            dataType:'json',
            cache:false,
            success:function(res){
                if(res.code == 0){
                    fillTable(res.data);
                    pagination(res.data.totals);
                }else{
                    showMessage(res.msg,"error");
                }

            },
            error:function(){
                showMessage("获取数据失败","error");
            }

        })
    };
    this.getDropList = function(){
        var param = {};
        param.cityId = $("#area").val() || 0;
        param.startTime = transDateFormat($("#startDate").val()) || "";
        param.endTime = transDateFormat($("#endDate").val()) || "";
        if($("#search").val() == "支持任务名称搜索"){
            param.searchVal = "";
        }else{
            param.searchVal = $("#search").val();
        }
        param.pageSize = 10;
        return param;
    };
    var pagination = function(totals){  //分页
        var pagerBox = document.getElementById('page');
        $(pagerBox).html("");
        var pager = new Pager({
            index: 1,
            total: Math.ceil(totals/10),
            parent: pagerBox,
            onchange: doChangePage
        });
        function doChangePage(obj){
            var param = me.getDropList();
            param.currentPage = obj.index;
            $.ajax({
                url: './taskEvalution/getTaskEvalutionList',
                type:'get',
                data:param,
                dataType:'json',
                cache:false,
                success:function(res){
                    if(res.code == 0){
                        fillTable(res.data);
                    }else{
                        showMessage(res.msg,"error")
                    }
                },
                error:function(){
                    showMessage("获取数据失败","error")
                }

            })
        }
    };
    var fillTable = function(param){   //填充表格
        var data= param.taskEvalutionList;
        var html = '';
        $("#activities_num").text("("+ param.totals +")")
        if(!data || data.length == 0){
            $(".panel-bottom tbody").html("<tr><td colspan='10'>暂无数据</td></tr>");
        }else{
            for(var i=0;i<data.length;i++){
                html += '<tr>'+
                    '<td>'+
                    '<i></i>'+
                    '</td>'+
                    '<td class="text-align-left" title="'+data[i].taskName+'">'+data[i].taskName+'</td>'+
                    '<td>'+data[i].cityName+'</td>'+
                    '<td>'+transToDate(data[i].opTime)+'</td>'+
                    '<td>'+data[i].targetNum+'</td>'+
                    '<td>'+data[i].touchNum+'</td>'+
                    '<td>'+my.transDecimal(data[i].touchRate)+'%'+'</td>'+
                    '<td>'+data[i].vicNum+ '</td>'+
                    '<td>'+my.transDecimal(data[i].vicRate)+'%'+ '</td>'+
                    '<td>'+data[i].relatedNum+ '</td>'+
                    '</tr>';
            }
            $(".panel-bottom tbody").html(html);
        }
        initTableStyle();
        initOperateEvent();
        initTableHover();
    };
    var initTableStyle = function(){ //表格样式
        /*
         *所有动态添加列添加背景色
         */
        var len =  $("#table thead td[data-index]").length;
        for(var col="col-",eachCol,i=5;i<len+1;i++){
            eachCol = col + i;
            $("#table td[data-index='col-"+i+"']").find('span').addClass(color[(i-5)%4]);
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
    var initOperateEvent = function(){  //表格操作
        $(".top-icon").click(function(){  //置顶
            var camp_id = $(this).parent().attr("data-id");
            var update = function(){    //优先级置顶提交
                //console.log(camp_id)
                $.blockUI.defaults.overlayCSS.backgroundColor = '#fff';
                $.blockUI.defaults.overlayCSS.opacity = '0.8';
                $.blockUI({
                    message:"正在进行置顶操作，请稍等...",
                    css:{
                        border:"none",
                        fontSize:"16px"
                    }
                });

                $.ajax({
                    url: './priorities/',
                    type:'post',
                    dataType:'json',
                    cache:false,
                    data:{'activityId':camp_id},
                    success:function(res){
                        if(res.code == 0){
                            $.unblockUI();
                            showMessage("操作成功","success",1500);
                            //刷新页面
                            me.getPageData();
                        }else{
                            $.unblockUI();
                            showMessage(res.msg,"error");
                        }
                    },
                    error:function(){
                        $.unblockUI();
                        showMessage("置顶失败","error");
                    }
                });
            }
            showConfirm("确定要置顶此活动吗？",update);
        })
    };
    $("#search").keypress(function (e) {
        if (e.keyCode == 13)
            me.getPageData();
    });
    $("#search").next().click(function () {
        me.getPageData();
    })
    $("#ensure").click(function () {
        me.getPageData();
        $("#cancel").trigger("click");
    });
}

$(function () {
    //ie8模拟placeholder
    $("#search").val("支持任务名称搜索").css("color","#CCD0D9");
    $("#search").focus(function(){
        if($("#search").val() == "支持任务名称搜索"){
            $(this).val("");
        }
        $(this).css("color","black");
    });
    $("#search").blur(function(){
        if($(this).val() == ""){
            $(this).val("支持任务名称搜索").css("color","#CCD0D9");
            return;
        }
    });
    $("#download").on("click",function () {
        var params =  my.taskEvalution.getDropList();
        window.location.href = "./taskEvalution/taskEvalutionListDownload?searchVal="+params.searchVal+
                "&cityId="+params.cityId +
                "&startTime="+params.startTime+
                "&endTime="+params.endTime;
    })
});

$(window).resize(function () {
    var windowH = $(window).height();
    var innerContentH = $(".inner-content").height();
    $(".content-wrap").css("height",(windowH-46)+"px");
    $("#table td").css("height",((innerContentH-20-39-40-20)/11)+'px'    );
});
$(document).ready(function(){
    //初始化网页顶部导航模块
    var topNav = new topNavigation();
    topNav.init();
    topNav.show("#areaAssessment");
    //初始化网页左侧导航模块
    var sideNav = new sideNavigation();
    sideNav.init();
    sideNav.show("#taskAssessment");
});
$(document).ready(function(){
    //初始化任务评估列表
    var taskEvalution = new taskEvalutionTable();
    taskEvalution.getPageData();
    my.taskEvalution = taskEvalution;

    //获取地市下拉列表
    $.ajax({
        url: "./tasks/getCityList",
        type: "get",
        dateType: "json",
        success: function (res) {
            if( res.code != 0 ){
                showMessage(res.msg,"error");
                return
            }
            var html = "<option value='0'>全省</option>";
            if( !res.data || !res.data.length ){
                return
            }
            $.each(res.data,function (i, item) {
                html += "<option value='"+item.cityId+"'>"+item.cityName+"</option>"
            })
            $("#area").html(html);
        },
        error:function () {
            showMessage("获取数据失败","error");
        }
    })

});