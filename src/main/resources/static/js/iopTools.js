/**
 * Created by KangH on 2018/4/18.
 */
Vue.component('msg-item', {
    props: ['msg'],
    template: '<li v-bind:id=msg.dataId><a v-bind:href=msg.url><img class="tools_img" v-bind:src=msg.pic>{{ msg.text }}<div class="into">点击进入</div></a></li>'
});

var app = new Vue({
    el: '#tools_modules',
    data: {
        groceryList: [
            {id:0,text:"数字内容",dataId:"dgtContent",url: "##",pic:"./images/icon/icon1.png"},
            {id:1,text:"标签库",dataId:"tagLib",url: "##",pic:"./images/icon/icon2.png"},
            {id:2,text:"PCC",dataId:"pcc",url: "##",pic:"./images/icon/icon3.png"},
            {id:3,text:"策略库",dataId:"tacticLib",url: "iopScene.html",pic:"./images/icon/icon4.png"},
            {id:4,text:"客户洞察",dataId:"marketingInsight",url: "##",pic:"./images/icon/icon5.png"},
            {id:5,text:"驻点顾问",dataId:"zd",url: "##",pic:"./images/icon/icon6.png"},
            {id:6,text:"慧购街",dataId:"hgj",url: "##",pic:"./images/icon/icon7.png"},
            {id:7,text:"自动化运营",dataId:"autoMarketing",url: "##",pic:"./images/icon/icon1.png"},
            //{id:8,text:"...",dataId:"ces",url: "##",pic:"./images/icon/icon1.png"}
        ]
    }
});
