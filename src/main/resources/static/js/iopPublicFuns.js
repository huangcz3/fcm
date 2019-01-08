/**
 * Created by Administrator on 2017/12/14.
 */
var iopPublicAttrs = {
    activityType: {
        "1": "4G产品类",
        "2": "终端类",
        "3": "流量类",
        "4": "数字化服务类",
        "5": "基础服务类",
        "6": "PCC类",
        "7": "宽带类",
        "9": "其他类"
    },
    state: {
        "1": "保存成功待上报",
        "2": "上报中",
        "4": "集团审批通过",
        "5": "集团审批驳回",
        "10": "上报成功",
        "11": "上报失败"
    }
}
var iopPublicFuns = (function(){
    var funs = {};
    //文件上传和附件下载事件
    funs.fileEvent = function (type) {  //5:场景；6：模型； 7：案例
        //上传文件
        $(".j-upload-file-btn").click(function (e) {
            e.preventDefault();
            var $this = $(this);
            var $hintMsg = $this.closest(".file-box").find("i.upload-hint-msg");
            var $nameInput = $this.prev();
            var $fileInput = $nameInput.parent().next();
            var uploadId = $fileInput.attr("id");
            var currentActId = $(".modal-wrap-new").data("currentActId");
            $.ajaxFileUpload({
                url: "./policyFile/upload",
                type: "post",
                data: {
                    useType: type,
                    currentActId: currentActId
                },
                fileElementId: uploadId,
                secureuri: false,
                dataType:'json',
                cache: false,
                async: false,
                success: function (res) {
                    $("#"+uploadId).on("change",function () {
                        $nameInput.val($(this).val());
                    });
                    if(res.code != 0){
                        showMessage(res.msg,"error");
                        return
                    }
                    showMessage("文件上传成功","success");
                    $hintMsg.show();
                    var fileName = res.data;
                    $nameInput.attr('data-attachmentId',fileName);
                },
                error: function () {
                    showMessage("文件上传失败","error");
                    $("#"+uploadId).on("change",function () {
                        $nameInput.val($(this).val());
                    });
                }

            })
        });
        $(".file-box").find("[name='file']").on("change",function () {
            var $this = $(this);
            $this.prev().find("input[type='text']").val($this.val());
        });
        //取消
        $(".j-upload-file-btn").next().on("click",function (e) {
            e.preventDefault();
            var $this = $(this);
            var $fileNameParent = $this.parent();
            var $fileName = $fileNameParent.find("input[type='text']");
            var $fileUpload = $fileNameParent.next();
            $fileName.attr("data-attachmentid","");
            $fileName.val("点击此处选取上传文件");
            $fileUpload.val("");
            $fileUpload.next().hide();
        })
        //详情，附件下载
        $("[data-name='attachmentName'],[data-file]").on("click","a",function (e) {
            var $this = $(this);
            var attachmentId = $this.attr("data-attachmentid");
            var attachmentName = $this.text();
            var stype = $(".modal-wrap-detail").data("currentType");  //1:省公司；2：集团下发
            var currentActId = $(".modal-wrap-detail").data("currentActId");
            window.location.href='./policyFile/downLoad?fileName='+attachmentId+"&fileOriginalName="+attachmentName+"&useType="+type+"&type="+stype+"&currentActId="+currentActId;
        });
        //清空data
        $(".modal-box-cancel,.modal-box-close").on("click",function (e) {
            $modal = $(this).closest(".modal-wrap");
            $modal.data("currentActId","");
            if( $modal.hasClass("modal-wrap-detail") ){
                $modal.data("currentType","");
            }
            if( $modal.hasClass("modal-wrap-new") ){
                //重置表单
                $(".upload-hint-msg").hide();
                $("[data-attachmentid]").attr("data-attachmentid","").val("点击此处选取上传文件");
                $("[name='file']").val("");
            }
        })
    }
    //检验是否填写完整
    funs.checkForm = function (data) {
        var result = true;
        for( var k in data){
            var $elem = $("[name='"+k+"']");
            if( typeof($elem.attr("data-attachmentid")) == "undefined"){  //非文件上传域校验
                var $titleElem = $("[name='"+k+"']").siblings(".item-property");
                var isRequired = $titleElem.hasClass("is-required");
                var title = $.trim($titleElem.text())? $.trim($titleElem.text()):"必要信息：";
                if( isRequired && !data[k]){
                    showMessage("请将"+title.substring(0,title.length-1)+"填写完整","error");
                    result = false;
                    break;
                }
            }else{
                var $file = $elem;
                var fileName = $file.val();
                var fileId = $file.attr("data-attachmentid");
                if ( fileName == "" || fileName == "点击此处选取上传文件" ){
                    continue;
                }
                if( fileId == "" ){
                    showMessage("请先上传已选附件","error");
                    result = false;
                    break;
                }
            }
        }
        return result;
    }
    //参数attrMap
    funs.transToKeyValue = function (data) {
        if( !data ){
            return
        }
        var mapList = [];
        for( var  k in data){
            var obj = {};
            obj.key = k;
            obj.value = data[k];
            mapList.push(obj);
        }
        return mapList;
    }
    //表格样式
    funs.initTableStyle = function(id){ //表格样式
        /*
         *所有动态添加列添加背景色
         */
        var $table = $("#"+id);
        var len =  $table.find("thead td[data-index]").length;
        for(var col="col-",eachCol,i=5;i<len+1;i++){
            eachCol = col + i;
            $table.find("td[data-index='col-"+i+"']").find('span').addClass(color[(i-5)%4]);
        }
        //表格图标添加
        var imgPath = 'img/';
        var icons = [ "activity_red.png","activity_yellow.png", "activity_green.png","activity_blue.png"];
        imgTds = $table.find("td>i");
        for(var i=0;i<imgTds.length;i++){
            $(imgTds[i]).css("background","url("+imgPath+icons[i%4]+")");
        }
        var table_h = parseInt($(".inner-content").css("height"))-20-39-40-40;
        $table.find("td").css("height",(table_h/11)+"px");
        $table.find("tbody tr:odd").addClass("even");

    }
    funs.toggleCreateNewBtn = function () {
        var $this = $(this);
        var type = $this.attr("data-type");
        if( type == 1 ){
            $(".filter-panel").addClass("g-right-128");
        }else{
            $(".filter-panel").removeClass("g-right-128");
        }
        if(!$this.hasClass("selected")){
            $("#filter").reset();
            $search.val("");
        }
    }
    //新建模型和新建案例，编码格式一致，同一个接口
    funs.generateActivityId = function(){
        $(".modal-wrap-new").show();
        $.ajax({
            url: "./policyCase/getNewActBasicInfo",
            type: "get",
            dataType:"json",
            success: function (res) {
                if( res.code != 0 ){
                    showMessage(res.msg,"error");
                    return
                }
                var data = res.data;
                $(".modal-wrap-new").data("currentActId",data.activityId);
                for (var k in data){
                    var $elem = $("#"+k);
                    var $nameElem = $elem.next();
                    if( $elem.length && $nameElem.length){
                        $elem.text(data[k]);
                        $nameElem.val(data[k]);
                    }
                }
            }
        })
    }
    funs.fillSearchResult = function(total){
        var txt = "";
        var type = $("#actType").find("option:selected").text();
        var startTime = $("#startDate").val();
        var endTime = $("#endDate").val();
        txt = "\""+type+"\"、 \""+startTime+"~"+endTime+"\"相关搜索结果"+total+"个";
        return txt;
    }

    return funs;
})();