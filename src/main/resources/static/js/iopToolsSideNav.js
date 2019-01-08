/**
 * Created by KangH on 2018/4/18.
 */
$(document).ready(function(){
    //初始化网页顶部导航模块
    var topNav = new topNavigation();
    topNav.init();
    topNav.show("#iopTools");
    //初始化网页左侧导航模块
    var sideNav = new sideNavigation();
    sideNav.init("#iopTools");
});
var sideNavigation=function(){
    return {
        def:{
            author:"shaojinyu",
            date:"2017-5-16",
            name:"script of OMP page header model",
            description:"有权限控制，初始化时.int(menu)时要传入当前menu的id"
        },
        element:"#sideNav",
        nav:{
            sysManagePriority:{
                text:"营销工具",
                dataId:"iopTools"
            }
        },
        deftRole:"user,approver",
        buildNavigation:function(data){
            var _this=this;
            var isfirst=true;
            var html = "";
            var isAdmin = data.indexOf("admin");
            var isAdmin10086 = data.indexOf("admin10086");
            $(_this.element).empty();

            $.each(this.nav,function(index,item){

                var $li=$("<li id='"+item.dataId+"'></li>");
                var $a=$('<a href="'+item.dataId+ ".html"+'">'+item.text+'</a>');
                $li.append($a);

                if(isfirst){
                    $li.addClass("selected");
                    isfirst=false;
                }
                // $li.click(function(){
                // 	$(this).addClass("selected").siblings(".selected").removeClass("selected");
                //  window.location.href=$(this).attr("id");
                // });
                $(_this.element).append($li);
            });
        },
        show:function(menu){
            $(menu).addClass("selected").siblings(".selected").removeClass("selected");
        },
        init:function(menu){
            var self = this;
            //获取权限
            $.ajax({
                url: " ./manage/getUserRole",
                type:"get",
                dataType:"json",
                cache:false,
                success:function (res) {
                    if(res.code != 0){
                        showMessage(res.msg,"error");
                        self.buildNavigation(self.deftRole);
                        self.show(menu);
                        return
                    }
                    var data = res.data.roleInfo;
                    if(data){
                        self.buildNavigation(data);
                        self.show(menu);
                    }else{
                        self.buildNavigation(self.deftRole);
                        self.show(menu);
                    }
                },
                error:function(){
                    showMessage("获取权限失败了","error");
                    self.buildNavigation(self.deftRole);
                    self.show(menu);
                }
            });
            //this.buildNavigation();
        }
    };
};