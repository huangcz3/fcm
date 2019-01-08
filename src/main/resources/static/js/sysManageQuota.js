/**
 * Created by Administrator on 2017/9/27.
 */

var quotaGlobal = {};

//任务列表
var InitQuotaTable = function(){
    var quotaTable = new Object();
    var getParams = function(){
        var params = {};
        params.effectTime = $("#effectiveDate").val() || formatDate(new Date(),'yyyy-MM');
        params.currentPage = 1;
        params.pageSize = 10;
        return params;
    };
    var getPageData = function(channel){
        var params = getParams();
        $.ajax({
            url: "./quota/getQuotaList",
            type:'get',
            data:{
                channelId: channel,
                currentPage: params.currentPage,
                pageSize: params.pageSize,
                effectTime: params.effectTime
            },
            dataType:'json',
            success:function(res){
                if( res.code != 0 ){
                    showMessage(res.msg,"error");
                    return
                }
                var userRole = res.data.userRole;
                if( userRole.indexOf("quota") != -1){
                    $(".u-create-btn").removeClass("hidden");
                }
                fillTable(res.data.quotaList,userRole,channel);
                pagination(res.data.totalRecords,channel);
                $("#activities_num").text("("+res.data.totalRecords+")");
            },
            error:function(){
                showMessage("获取数据失败","error");
            }

        })
    };
    var pagination = function(totals,channel){
        var params = getParams();
        var pagerBox = document.getElementById('page');
        $(pagerBox).html("");
        var pager = new Pager({
            index: 1,
            total: Math.ceil(totals/10),
            parent: pagerBox,
            onchange: doChangePage
        });
        function doChangePage(obj){
            $.ajax({
                url: "./quota/getQuotaList?channelId="+channel+"&effectTime="+params.effectTime+"&currentPage="+obj.index+"&pageSize=10",
                type:'get',
                dataType:'json',
                success:function(res){
                    if( res.code != 0 ){
                        showMessage(res.msg,"error");
                        return
                    }
                    fillTable(res.data.quotaList,res.data.userRole,channel);
                },
                error:function(){
                    showMessage("获取数据失败","error");
                }

            })
        }
    };
    var fillTable = function(param,userRole,channel){
        var nowDate = formatDate(new Date(),"yyyy-MM");
        var data= param;
        var html = '',htmlHead = "";
        var isCanEdit = userRole.indexOf("quota") == -1?false:true;
        var d05SendNum = channel=='d05'?"<td>已发送量</td>":"";
        if( isCanEdit ){
            htmlHead += "<tr>";
            htmlHead += "<td style='width: 5%;' >&nbsp;</td>";
            htmlHead += "<td >地市</td>";
            htmlHead += "<td>配额量</td>";
            htmlHead += d05SendNum;
            htmlHead += "<td>生效时间</td>";
            htmlHead += "<td>操作</td>";
            htmlHead += "</tr>";
        }else{
            htmlHead += "<tr>";
            htmlHead += "<td style='width: 5%;' >&nbsp;</td>";
            htmlHead += "<td >地市</td>";
            htmlHead += "<td>配额量</td>";
            htmlHead += d05SendNum;
            htmlHead += "<td>生效时间</td>";
            htmlHead += "<!--  <td>操作</td>-->";
            htmlHead += "</tr>";
        }

        if( !data || !data.length ){
            if( isCanEdit ){
                var cols = channel == "d05"?'6':'5';
                html = "<tr><td colspan="+cols+">暂无数据</td></tr>"
            }else{
                var cols = channel == "d05"?'5':'4';
                html = "<tr><td colspan="+cols+">暂无数据</td></tr>"
            }

        }else{
            for(var i=0;i<data.length;i++){
                var hasSend = channel=='d05'?"<td>"+data[i].sendedCount+"</td>":"";
                html += '<tr>';
                html += '<td>';
                html += '<p>';
                html += '<i class="icon"></i>';
                html += '</p>';
                html += '</td>';
                html += '<td>'+data[i].cityName+'</td>';
                html += '<td>'+data[i].sendLimit+'</td>';
                html += hasSend;
                html += '<td>'+data[i].effectTime+'</td>';
                if( isCanEdit ){
                    html += '<td  data-effecttime="'+data[i].effectTime+'" data-cityid="'+data[i].cityId+'" data-cityname="'+data[i].cityName+'" data-quotanum="'+data[i].sendLimit+'">';
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
        imgTds = $("#table td>p>i.icon");
        for(var i=0;i<imgTds.length;i++){
            $(imgTds[i]).css("background","url("+imgPath+icons[i%4]+")");
        }
        var table_h = parseInt($(".inner-content").css("height"))-20-39-40-20;
        $("#table td").css("height",(table_h/11)+"px");
        $("#table tbody tr:odd").addClass("even");

    };
    var initOperateEvent = function(){

        $(".edit-icon").click(function(){  //编辑
            var cityId = $(this).parent().attr("data-cityid");
            var cityName = $(this).parent().attr("data-cityname");
            var quotaNum = $(this).parent().attr("data-quotanum");
            var effectTime = $(this).parent().attr("data-effecttime");
            $(".modal-wrap-edit").show();
            $("#curCityName").text(cityName).data("curCityId",cityId);
            $("#quotaQ").val(quotaNum);
            //$("#effectTime").val(effectTime.substring(0,7));
            $("#effectTime").text(effectTime.substring(0,7));
        });
    }
    quotaTable.query = function (channel) {
        getPageData(channel);
    }

    return quotaTable;
}

$(function () {
    //将form表单数据转换成json格式
    var getFormJson = function(frm) {
        var o = {};
        var a = $(frm).serializeArray();
        $.each(a, function () {
            if (o[this.name] !== undefined) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    }

    //查询确定按钮
    $("#ensure").on("click",function (e) {
        e.preventDefault();
        var channelId = $("#channels span.selected").attr("data-channelid");
        quotaGlobal.initQuota.query(channelId);
        $(this).next().trigger("click");
    })
    $(".modal-box-cancel").click(function (e) {
        e.preventDefault();
        $(this).closest(".modal-wrap").hide();
        if( $(this).closest(".modal-wrap").hasClass("modal-wrap-edit") ){
            $("#curCityName").text("").removeData("curCityId");
            $("#effectTime").text("");
            $("#quotaQ").val("");
        }else{
            var $modal = $(".modal-wrap-new");
            $("#addNewCityQuota")[0].reset();  //重置form表单
        }
    });
    //修改，保存配额
    $(".modal-wrap-edit .modal-box-save").click(function(e){
        e.preventDefault();
        var params = {};
        //判断配额量是否填写
        if( $("#quotaQ").val() == "" ){
            showMessage("配额量不能为空","error");
            return
        }
        params.cityId = $("#curCityName").data("curCityId");
        params.sendLimit = $("#quotaQ").val();
        params.channelId = $("#channels span.selected").attr("data-channelid");

        $.blockUI.defaults.overlayCSS.backgroundColor = '#fff';
        $.blockUI.defaults.overlayCSS.opacity = '0.8';
        $.blockUI({
            message:"信息保存中，请稍等...",
            css:{
                border:"none",
                fontSize:"16px"
            }
        });
        $.ajax({
            url: "./quota/setCityQuota",
            type: "post",
            data: JSON.stringify(params),
            contentType: "application/json;charset=UTF-8",
            dataType: "json",
            cache: false,
            success: function (res) {
                $.unblockUI();
                if( res.code != 0 ){
                    showMessage(res.msg,"error");
                    return
                }
                showMessage("配额量修改成功","success");
                quotaGlobal.initQuota.query(params.channelId);
                $(".modal-box-cancel").trigger("click");
            },
            error: function () {
                $.unblockUI();
                showMessage("配额量修改失败","error");
            }
        })
    })
    //配额量输入校验
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
    $(".create-new").on("click",function () {
        var channelId = $("#channels span.selected").attr("data-channelid");
        var $modal = $(".modal-wrap-new");
        //获取未配置渠道配额的地市列表
        $.ajax({
            url: "./tasks/getCityList?",
            type: "get",
            dataType: "json",
        }).done(function (res) {
            if( res.code != 0 ){
                showMessage(res.msg,"error",3000);
                return
            }
            var cityList = res.data;
            $("#cityList").html("");
            $(".modal-wrap-new").show();
            $.each(cityList,function (i, item) {
                $("#cityList").append("<option value='"+ item.cityId +"'>"+ item.cityName +"</option>");
            });
        }).fail(function () {
            showMessage("获取地市列表失败","error");
        })
    });
    //新增，保存配额
    $(".modal-wrap-new .modal-box-save").on("click",function () {
        var paramJson = getFormJson("#addNewCityQuota");
        for( var k in paramJson ){
            if( !$.trim(paramJson[k]) ){
                showMessage("请将必要信息填写完整","error");
                return
            }
        }

        $.blockUI.defaults.overlayCSS.backgroundColor = '#fff';
        $.blockUI.defaults.overlayCSS.opacity = '0.8';
        $.blockUI({
            message:"信息保存中，请稍等...",
            css:{
                border:"none",
                fontSize:"16px"
            }
        });
        paramJson.cityName = $("#cityList option:selected").text();
        paramJson.channelId = $("#channels span.selected").attr("data-channelid");
        paramJson.channelName = $("#channels span.selected").find("a:first-child").text();
        $.ajax({
            url: "./quota/addCityQuota",
            type: "post",
            data: JSON.stringify(paramJson),
            contentType: "application/json;charset=UTF-8",
            dataType: "json",
        }).done(function (res) {
            $.unblockUI();
            if( res.code != 0 ){
                showMessage(res.msg,"error");
                return
            }
            showMessage("保存成功","success");
            quotaGlobal.initQuota.query( paramJson.channelId);
            $(".modal-box-cancel").trigger("click");
        }).fail(function () {
            $.unblockUI();
            showMessage("保存失败","error");
        });
    });
});

$(document).ready(function(){
    //初始化网页顶部导航模块
    var topNav = new topNavigation();
    topNav.init();
    topNav.show("#sysManage");
    //初始化网页左侧导航模块
    var sideNav = new sideNavigation();
    sideNav.init("#sysManageQuota");
});
$(document).ready(function () {
    var html = "";
    var InitQuota = new InitQuotaTable();
    var channelNavEvent = function(){
        $("#channels>span").click(function (e) {
            e.preventDefault();
            var channelId = $(this).attr("data-channelid");
            $(this).addClass("selected").siblings(".selected").removeClass("selected");

            InitQuota.query(channelId);
        });
    };
    var quotaType = {
        "d05": "按月",
        "d01": "按日"
    }

    $.ajaxSetup({
        cache:false
    });

    //默认生效时间是当前月
    var curDate = new Date();
    $("#effectiveDate").val(formatDate(curDate,"yyyy-MM"));

    quotaGlobal.initQuota = InitQuota;
    //获取渠道列表
    $.ajax({
        url: "./quota/getChannels",
        type: "get",
        dateType: "json",
        success: function (res) {
            if( res.code != 0 ){
                showMessage(res.msg,"error");
                return
            }
            var html = "";
           if( !res.data || !res.data.length ){
               return
           }
           $.each(res.data,function (i, item) {
               html +=  '<span class="title" data-channelid="'+item.CHANNEL_ID+'">';
               html +=       '<a href="##">'+item.CHANNEL_NAME +'</a>';
               html +=       '<a> ('+quotaType[item.CHANNEL_ID]+')</a>';
               html +=  '</span>';
           })
            $("#channels").html(html);
            channelNavEvent();
            $("#channels>span:first-child").trigger("click");
        },
        error:function () {
            showMessage("获取数据失败","error");
        }
    });

})