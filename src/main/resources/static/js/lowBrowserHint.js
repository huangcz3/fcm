/**
 * Created by Administrator on 2017/10/12.
 */
$(document).ready(function () {
    var myBrowser = generalExtrFuncs.getMyBrowser();
    $("#myBrowser").text(myBrowser);
    //判断是否是从经分跳转，是，弹出提醒框
    $.ajax({
        url: "./base/isJumpFromJF",
        type: "get",
        dataType: "json",
        cache: false,
        success: function(res){
            if( res.code != 0 ){
                showMessage(res.msg,"error");
                return
            }
            if( res.data.isFirstJump ){
                $(".j-counsel-hint-modal").removeClass("hidden");
            }
        }
    })

    $(".j-counsel-hint-close").click(function(){
        $(".j-counsel-hint-modal").hide();
    })
});