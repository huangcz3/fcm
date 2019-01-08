/**
 * Created by Administrator on 2017/8/2.
 * 10086渠道管理
 */
var msgChannels = [
    {
        "channelId": "d01",
        "channelName": "优惠提醒"
    },{
        "channelId": "d03",
        "channelName": "主动宣传模式"
    },{
        "channelId": "q09",
        "channelName": "追尾短信模式"
    },{
        "channelId": "d05",
        "channelName": "10086群发"
    }
]
var allFuncs = (function(){
    return {
        saveData: function(id){    //获取页面数据
            var subClasses = $(".content-tab div[data-bindtab='"+ id +"'] .row");
            var paramList = [];
            $.each(subClasses,function (i, item) {
                var param = {};
                var $selInput = $(item).find(".g-left-part input");
                param.channelId = id;
                if ( id == "d02" ){ //现在只有触点短信渠道有小类
                    param.subChannelId = $selInput.attr("name");
                }else{
                    param.subChannelId = id;
                }
                param.status = $selInput.val();
                if(param.status == "Y"){
                    if( param.channelId == "d02" ){
                        param.defaultMsg = $(item).find("textarea.default-msg").val();
                    }else{
                        param.defaultMsg = "";
                    }
                }else{
                    param.defaultMsg = "";
                }
                paramList.push(param);
            });
            return paramList;
        },
        validatePostParam: function(param){   //验证要提交数据是否完整
            var result = true;
            if(param && param.length){
                $.each(param,function(i,item){
                    if( item["status"] == "Y" ){
                        if( item["channelId"] == "d02" && !item["defaultMsg"] ){
                            showMessage("请填写默认短信内容","error");
                            result = false;
                            return false;
                        }
                    }
                });
            }
            return result;
        }
    }
})();

$(".radio-box p").click(function () {
    var $this = $(this);
    $this.addClass("selected").siblings(".selected").removeClass("selected");
    if($this.hasClass("j-start")){
        $(".content-tab .row .col-3:last-child").addClass("hidden");
        $this.parents(".row").find(".col-3:last-child").removeClass("hidden");
    }else{
        $this.parents(".row").find(".col-3:last-child").addClass("hidden");
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
    var subList = allFuncs.saveData(channelId);
    //判断信息是否填写完整
    var result = allFuncs.validatePostParam(subList);
    if( !result ){
        $.unblockUI();
        return;
    }
    $.ajax({
        url: "./manage/channelSubClassReset",
        type:"post",
        data:JSON.stringify(subList),
        dataType:"json",
        contentType: 'application/json;charset=UTF-8',
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
$(document).ready(function() {
    //初始化网页顶部导航模块
    var topNav = new topNavigation();
    topNav.init();
    topNav.show("#sysManage");
    //初始化网页左侧导航模块
    var sideNav = new sideNavigation();
    sideNav.init("#sysManage10086");

    $.each(msgChannels,function (i, channel) {
        var $htmlTemplate = $($(".j-html-template").html());
        var id = channel.channelId;
        var name = channel.channelName;
        $(".j-channels-nav").append('<span class="title" data-channelid="'+ id +'"><a href="##">'+ name +'</a> </span>')
        $htmlTemplate.attr("data-bindtab",id);
        $htmlTemplate.find(".s-channel-name").text(name+"：");
        $htmlTemplate.find(".switch>input").attr("name",id);
        $(".j-content-tab").append($htmlTemplate);
    });

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
            url: "./manage/getChannelSubClassInfo?channelId="+channelId,
            type: "get",
            cache: false,
            dataType: "json",
            success: function (res) {
                if (res.code !== 0) {
                    showMessage(res.msg,'error');
                    return
                }
                var data = res.data;
                for(var k in data){
                    var $input = $("[data-bindtab='"+channelId+"'] input[name="+ k +"]");
                    if( data[k].state == "N" ){
                        $input.bootstrapSwitch('state',false);
                        $input.val("N");
                    }else{
                        $input.bootstrapSwitch('state',true);
                        $input.parents(".row").find("textarea").val(data[k].defaultMsg);
                        $input.val("Y");
                    }
                }
            },
            error:function () {
                showMessage("获取当前渠道小类信息失败","error");
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
            $(this).val("Y");
        },
        onSwitchChange:function(event,state){
            if(state == true){
                $(this).parents(".row").find(".g-right-part").removeClass("visible-hidden");
                $(this).val("Y");
            }else{
                $(this).parents(".row").find(".g-right-part").addClass("visible-hidden");
                $(this).val("N");
            }
        }
    });

    $.ajaxSetup({
        cache: false
    });
});
