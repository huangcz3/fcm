/**
 * Created by Administrator on 2017/7/31.
 * 黑白红名单查询
 */
$(document).ready(function() {
    //初始化网页顶部导航模块
    var topNav = new topNavigation();
    topNav.init();
    topNav.show("#sysManage");
    //初始化网页左侧导航模块
    var sideNav = new sideNavigation();
    sideNav.init("#sysManageBWR");
    //sideNav.show("#sysManageBWR");

    $.ajaxSetup({
        cache: false
    });
});

var funcs = (function () {
   return {
           /*!
            * @param  {str} 需要测试的字符串
            * @param  {typeString} 测试字符串的方式,可选内容为: ["phone-number"]
            * @return {boolean}
            */
           regTest : function  (str, typeString) {
                switch(typeString) {
                    case "phone-number":
                        var reg = /^1\d{10}$/;
                        return reg.test(str);
                        break;
                    case "city-id":

                        break;
                    default:
                        return;
                }
           },
        checkboxChange:  function (e) {
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
   }
})();
//校验输入号码
$("#phoneNum").keyup(function (e) {
    $(this).val($(this).val().replace(/[^\d,]*/g,''));
}).change(function(e) {
    if(!$(e.target).val()){
        return;
    }
    var arr = $(e.target).val().split(",");
    var phoneNumbers = [];
    var isWrong = false;
    var wrongNum = [];
    for (var i = 0; i < arr.length; ++i) {
        if (!funcs.regTest(arr[i], "phone-number")) {
            isWrong = true;
            wrongNum.push(arr[i]);
        } else {
            phoneNumbers.push(arr[i]);
        }
    }
    var wrongNumString = wrongNum.join(", ");
    if (wrongNumString.length > 35) {
        wrongNumString = wrongNumString.substring(0, 35) + "...";
    }
    if (isWrong) {
        var str = "存在错误号码，\""+wrongNumString+"\",请及时修改!"
        showMessage(str,"error", 3000);
    }
})
//查询
$(".search-btn").click(function () {
    var param = $("#phoneNum").val();
    $.ajax({
        url: "./manage/queryCustomerType?phoneNo="+param,
        type:"get",
        dataType:"json",
        cache:false,
        success:function (res) {
            if(res.code != 0){
                showMessage(res.msg,"error");
                return;
            }
            var data = res.data;
            for(var k in data){
                if(data[k].length){
                    var $span = $("[data-id='"+k+"']").siblings(".g-left-mark").find("span.color-grey");
                    var str = "（"+ data[k].length +"）：";
                    $span.text(str);
                    $("[data-id='"+k+"']").html("");
                    $.each(data[k],function (i, num) {
                        $("[data-id='"+k+"']").append("<li>"+ num +"；</li>");
                    })
                }else{
                    var $span = $("[data-id='"+k+"']").siblings(".g-left-mark").find("span.color-grey");
                    var str = "（0）：";
                    $span.text(str);
                    $("[data-id='"+k+"']").html("");
                    $("[data-id='"+k+"']").append("<li>无</li>");
                }
            }
        },
        error:function () {
            showMessage("查询失败","error");
        }
    })
})

//添加
$(".new-btn").click(function (e) {
    e.preventDefault();
    $(".modal-wrap-add").show();
});
//添加黑名单只能输入一个号码
$(".j-blackPhone").keyup(function () {
    var str = $(this).val();
    if(str.length>11){
        str = str.substr(0,11);
    }
    $(this).val(str);
});
//模态框确定事件
$(".modal-box-save").click(function (e) {
    e.preventDefault();
    var blackWhiteRedUserInfo = {};
    var phone = $("[name='blackPhone']").val();
    if(!phone){
        showMessage("请输入手机号码","error");
        return;
    }
    if(!funcs.regTest(phone,'phone-number')){
        showMessage("输入号码有误,请修改","error");
        return;
    }
    blackWhiteRedUserInfo.phoneNo = phone;
    blackWhiteRedUserInfo.userType = "black";
    blackWhiteRedUserInfo.userSource = 1;

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
        url: "./manage/addBlackWhiteRedUser",
        type: "post",
        data: JSON.stringify(blackWhiteRedUserInfo),
        dataType:"json",
        contentType: 'application/json;charset=UTF-8',
        cache:false,
        success:function (res) {
            $.unblockUI();
            if(res.code != 0){
                showMessage(res.msg,"error");
                return
            }
            showMessage("保存成功","success");
            $(".modal-box-cancel").trigger("click");
        },
        error:function () {
            $.unblockUI();
            showMessage("保存失败","error");
        }
    })
});

//模态框取消事件
$(".modal-box-cancel").click(function (e) {
    e.preventDefault();
    $(".modal-wrap-add").hide();
    $("[name='blackPhone']").val("");
});
