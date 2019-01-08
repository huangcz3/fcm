/**
 * 10086群发
 * Created by Administrator on 2017/11/7.
 */
var my = (function(){
    return {
        saveData: function(id){    //保存渠道设置信息
            var param = {};
            param.channelId = id;
            param.state = $("[data-bindtab='"+id+"'] input[name='d05']").val();
            if(  param.state == "N" ){ //启动
                param.sendFrequency = $("[data-bindtab='"+id+"'] input[name='d05ExtrAttr']").val();
            }else{
                param.sendFrequency = "";
            }
            param.sendCount = 5;  //默认为5
            return param;
        },
        validatePostParam: function(param){   //验证要提交数据是否完整
            var result = true;
            if(param && param.length){
                if( param["state"] == "N"){  //启动
                    if( !param["frequency"] ){
                        showMessage("请填写下发速度","error");
                        result = false;
                        return false;
                    }
                }
            }else{
                result = false;
            }
            return result;
        }
    }
})();

$(function(){
    $(".j-number").keyup(function (e) {
        if( ! /^\d+\.{0,1}[0-9]*$/.test($(this).val())){
            $(this).val("");
        }
    }).blur(function(){
        var $this = $(this);
        if( $this.val() > 100 ){
            showMessage("链接下发速度超出可取范围，请重新设置","error");
            $this.val("");
        }
    });

    $(".ensure").click(function () {
        $.blockUI.defaults.overlayCSS.backgroundColor = '#fff';
        $.blockUI.defaults.overlayCSS.opacity = '0.8';
        $.blockUI({
            message:"信息保存中，请稍等...",
            css:{
                border:"none",
                fontSize:"16px"
            }
        });

        var channelId = $(".j-channels-nav span.selected").attr("data-channelid");
        var params = my.saveData(channelId);
        $.ajax({
            url: "./manage/set10086GroupSend",
            type:"post",
            data:params,
            dataType:"json",
            cache:false,
            success:function (res) {
                if(res.code != 0){
                    $.unblockUI();
                    showMessage(res.msg,"error");
                    return
                }
                $.unblockUI();
                showMessage("保存成功","success");
            },
            error:function () {
                $.unblockUI();
                showMessage("保存失败","error");
            }
        })
    });
});

$(document).ready(function() {
    //初始化网页顶部导航模块
    var topNav = new topNavigation();
    topNav.init();
    topNav.show("#sysManage");
    //初始化网页左侧导航模块
    var sideNav = new sideNavigation();
    sideNav.init("#sysManage10086GroupSend");


    $(".j-channels-nav span").click(function (e) {
        e.preventDefault();
        var channelId = $(this).attr("data-channelid");
        if( $(this).hasClass("selected") ){
            return;
        }
        $(".j-channels-nav span.selected").removeClass("selected");
        $(this).addClass("selected");
        $(".j-content-tab>div.selected").removeClass("selected");
        $(".j-content-tab").find("div[data-bindtab='"+ channelId +"']").addClass("selected");

        //获取当前渠道控制信息
        $.ajax({
            url: "./manage/get10086GroupSendInfo?channelId="+channelId,
            type: "get",
            cache: false,
            dataType: "json",
            success: function (res) {
                if (res.code !== 0) {
                    showMessage(res.msg,'error');
                    return
                }
                var state = res.data.state;
                var frequency = res.data.sendFrequency;
                var $input = $("[data-bindtab='"+channelId+"'] input[name='d05']");
                var $frequency = $("[data-bindtab='"+channelId+"'] input[name='d05ExtrAttr']");
                if( state){
                    if( state == "N" ){ //启动
                        $input.bootstrapSwitch('state',true);
                        $input.val("N");
                        if( frequency ){
                            $frequency.val(frequency);
                        }
                    }else{
                        $input.bootstrapSwitch('state',false);
                        $input.val("Y");
                    }
                }
            },
            error:function () {
                showMessage("获取当前渠道配置信息失败","error");
            }
        });
    });

    $(".j-channels-nav span:first-child").trigger("click");
    //初始化switch按钮
    $(".row input[type='checkbox']").bootstrapSwitch({
        onText:"启动",
        offText:"暂停",
        onColor:"primary",
        size:"small",
        onInit: function(event,state){
            $(this).val("N");
        },
        onSwitchChange:function(event,state){  //是否暂停
            if(state == true){ //启动
                $(this).parents(".row").next().removeClass("visible-hidden");
                $(this).val("N");
            }else{
                $(this).parents(".row").next().addClass("visible-hidden");
                $(this).val("Y");
            }
        }
    });

    $.ajaxSetup({
        cache: false
    });
});
