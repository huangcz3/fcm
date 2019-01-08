/**
 * 不引用，已整合到topNav.js
 */
$(document).ready(function () {
    //获取登录用户名
    $.ajax({
        url: "./base/getUserInfo",
        type: "get",
        cache: false,
        dataType: "json",
        success: function (data) {
            if (data.code !== 0) {
                showMessage(data.msg,'error');
                //window.location="http://localhost:8080/fcm/test.html";
            }else{
                $("#user").text(data.data.userName || "请登录");
                //window.location="http://localhost:8080/fcm/test.html";
            }
        },
        error:function () {
            showMessage("获取当前登录用户名失败","error");
        }
    });
})