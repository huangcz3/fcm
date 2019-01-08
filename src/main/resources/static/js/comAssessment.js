
var isIE8;

function getPageData(){
	var param = {};
  param.date = $("#dateInput").val().split("-").join("");
  param.filtered = $(".radio-box p input[checked]").val();
 //  fillPageData(info);
  $.ajax({
      url:'./evalution/getEvaluationReportInfo',
      data:param,
      type:"get",
      dataType:"json",
      cache:false,
      success:function(res){
          if(res.code != 0){
              showMessage(res.msg,"error");
              return
          }
          var data = res.data;
          $("span.time").html(param.date.substring(0,4)+"-"+param.date.substring(4,param.date.length));
          fillPageData(data);
      },
      error:function(){
      	alert("出错了！获取页面信息失败");
      }
  })
}
function fillPageData(data){
    var targetNum = data.targetNum;
    var top3Act = data.top3Activity;
    var goodjobs = data.goodJobs;
    var goodjobsLen = null;
    var actHtml = "";
    var chanHtml = "";
    var tableHtml = "";
    var top1, top1_w,top2,top2_w,top3,top3_w;
    var rate;
    //当月累计指标
    if(targetNum != null ){
    	for(var k in targetNum){     
            if(targetNum[k]>0 && targetNum[k]<=1){
            	$("#"+k).html((targetNum[k]*100).toFixed(2) + "%");
            }else{
            	$("#"+k).html(targetNum[k]);
            }
            if($("#"+k+"_Bar").length){
            	 rate = (targetNum[k]*100).toFixed(2);
            	$("#"+k+"_Bar").css("width",rate+"px")
            }           
        }
    }else{
/*    	for(var k in targetNum){
            $("#"+k).html("0");
            if($("#"+k+"Bar").length){
            	 rate = parseFloat((targetNum[k].split("%"))[0]);
            	$("#"+k+"Bar").css("width",rate+"px")
            }           
        }*/
    	var tds = $(".left-panel-table td")
        for(var k = 0,len = tds.length; k < len; ++k ){
            $(tds[k]).find("li:first").html("0");
            if($(this).find("span[id]").length){
                $(this).find("span[id]").css("width","0px")
            }           
        }
    }
    
    //累计活动TOP3
    if(top3Act.length >0){
    	for(var i=0;i<top3Act.length;i++){
            actHtml += '<ul class="row">'+
                        "<li>"+
                            "<p class='small-font'>"+"TOP"+(i+1)+"</p>"+
                        "</li>"+
                        "<li>"+
                            "<span>"+top3Act[i].CITY_NAME+"</span>"+
                            "<b class=top"+(i+1)+">"+top3Act[i].ACT_NUM+"</b>"+
//                                "<i>"+top3Act[i].number+"</i>"+
                        "</li>"+
                    "</ul>"
        }
        $("#top3Activity").html(actHtml);
    }else{
    	actHtml = '<ul class="row">暂无数据！</ul>';
    	$("#top3Activity").html(actHtml);
    }
    
    
    //重点业务接触比
    var pieChart = document.getElementById('mainBusiness');
    $(pieChart).html("");
    var myChart = echarts.init(pieChart);
    var option = {
    	    tooltip : {
    	        trigger: 'item',
    	        formatter: "{a} <br/>{b} : {c} ({d}%)"
    	    },   
    	    color:['#E890AF','#EEB760','#82C7A1','#85B1E3','#F5D2DF','#F9E0BA','#D1E8DB','#D1DFF0',''],
    	    series : [
    	        {      	
    	            type:'pie',
    	            radius : ['40%', '60%'],
    	            itemStyle : {
    	                normal : {
    	                    label : {
    	                        show : true,
    	                        textStyle: {
    	                        	color: '#99A6B4'
    	                        },
    	                        formatter : function (params){
    	                        	return params.name+" "+(params.percent - 0) + '%';
    	                        }
    	                    },
    	                    labelLine : {
    	                        show : true,
    	                        lineStyle:{
    	                        	color: '#DEE2E9',
    	                        	width: 1
    	                        }
    	                    }
    	                }
    	            },
    	            data:[
    	                {value:1548, name:'4G产品类'},
    	                {value:234, name:'流量类'},
    	                {value:335, name:'终端类'},
    	                {value:310, name:'基础服务类'},
    	                {value:135, name:'宽带类'}
    	                
    	            ]
    	        }
    	    ]
    	};
    	                    
    myChart.setOption(option);
 
    //页面排版
    var rtTopPanel_w = parseInt($(".rt-top-panel").css("width"));
    var rtTopPanel_h = parseInt($(".rt-top-panel").css("height"));
    var rtTopLt = parseInt($(".rt-top-lt").css("width"));
    $(".rt-top-lt .panel-content ul").css("margin-top",((rtTopPanel_h-40-31-95)/6)+"px");
    $(".rt-top-lt .panel-content ul").css("margin-bottom",((rtTopPanel_h-40-31-110)/6)+"px");
    $(".rt-top-lt b.top1").css("width",(rtTopLt-108)+"px");
    
    drawTop3Width("#top3Activity");
   
    //优秀案例TOP5
    if(isIE8){
        goodjobsLen = goodjobs.length-1;
    }else{
        goodjobsLen = goodjobs.length;
    }
    if(goodjobsLen>0){
    	for(var i=0;i<goodjobsLen;i++){
        	if(i == goodjobsLen-1){
        		tableHtml += "<tr class='last-tds'>"+
                            "<td>TOP"+(i+1)+"</td>"+
                            "<td class='td-30'>"+goodjobs[i].CAMP_NAME+"</td>"+
                            "<td>"+goodjobs[i].CITY_NAME+"</td>"+
                            "<td>"+goodjobs[i].TOUCH_NUM+"</td>"+
                            "<td>"+(goodjobs[i].TOUCH_RATE*100).toFixed(2)+"%"+"</td>"+
                            "<td>"+goodjobs[i].VIC_NUM+"</td>"+
                            "<td><span class='top"+(i+1)+"'>"+(goodjobs[i].VIC_RATE*100).toFixed(2)+"%"+"</span></td>"+
                         "</tr>";
        	}else{
        		   tableHtml += "<tr>"+
                            "<td>TOP"+(i+1)+"</td>"+
                            "<td class='td-30'>"+goodjobs[i].CAMP_NAME+"</td>"+
                            "<td>"+goodjobs[i].CITY_NAME+"</td>"+
                            "<td>"+goodjobs[i].TOUCH_NUM+"</td>"+
                            "<td>"+(goodjobs[i].TOUCH_RATE*100).toFixed(2)+"%"+"</td>"+
                            "<td>"+goodjobs[i].VIC_NUM+"</td>"+
                            "<td><span class='top"+(i+1)+"'>"+(goodjobs[i].VIC_RATE*100).toFixed(2)+"%"+"</span></td>"+
                         "</tr>";   
        	}
         
        }
        $("#goodJobs tbody").html(tableHtml);
    }else{
    	tableHtml ="<tr class='last-tds'><td>暂无数据！</td></tr>"
    	$("#goodJobs tbody").html(tableHtml);
    }
    //页面排版
    var content_h = parseInt($(".inner-content").css("height"));
    var rtBottomPanel_h = (content_h-20)*0.58;
    $(".table td").css("height",((rtBottomPanel_h-60)/6)+"px");
    $(".table tr:even").addClass("even");
}
function drawTop3Width(id){
    var top1, top1_w,top2,top2_w,top3,top3_w;
    top1 = parseInt($(id).find(".top1").html());
    top2 = parseInt($(id).find(".top2").html());
    top3 = parseInt($(id).find(".top3").html());
    top1_w = parseInt($(id).find(".top1").css("width")); 
    top2_w = parseInt(top2/top1*top1_w);
    top3_w = parseInt(top3/top1*top1_w);
    
    $(id+" .top2").css("width",top2_w+"px");
    $(id+" .top3").css("width",top3_w+"px");
}

$("#ensure").click(function(){   
	$("#filter-btn").removeClass("opened");
$(".filter-panel").css("display","none");
	getPageData();  	
});

$(document).ready(function(){
 	//初始化网页顶部导航模块
    var topNav = new topNavigation();
    topNav.init();
    topNav.show("#comAssessment");
    //初始化网页左侧导航模块
    var sideNav = new sideNavigation();
    sideNav.init();
    sideNav.show("#comAssessment");
 });
window.onload = function(){

	 isIE8 = isIE8();
    //页面排版
    var content_w = parseInt($(".inner-content").css("width"));
    var content_h = parseInt($(".inner-content").css("height"));
    var rtTopPanel_w = "";
    var rtTopPanel_h = "";
    var rtTopLt = "";
    var rtBottomPanel_h = (content_h-20)*0.58;
    $(".left-panel").css("width",((content_w-20)*0.32)+"px");
    $(".right-panel").css("margin-left",((content_w-20)*0.32+20)+"px");
    $(".rt-top-panel").css("height",((content_h-20)*0.42)+"px");
    $(".rt-bottom-panel").css("height",((content_h-20)*0.58)+"px");
    rtTopPanel_w = parseInt($(".rt-top-panel").css("width"));
    rtTopPanel_h = parseInt($(".rt-top-panel").css("height"));
    $(".rt-top-lt").css("width",((rtTopPanel_w-21)*0.4)+"px");
    $(".rt-top-rt").css("margin-left",((rtTopPanel_w-21)*0.4+20)+"px");
    $(".rt-top-rt .panel-content").css("height",(rtTopPanel_h-40-32-10)+"px");
    rtTopLt = parseInt($(".rt-top-lt").css("width"));
    $(".left-panel-table td").css("height",((content_h-72)/4)+"px");

    $(".left-panel-table td:odd").css("border-right","none");


    //日期控件默认为当前月
    var time = sDate("month");
    $("#dateInput").val(time);
    $("span.time").html(time);

    getPageData();
    
}

