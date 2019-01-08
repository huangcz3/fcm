$(function(){
  layui.use('table', function(){
    var table = layui.table;

    table.render({
      elem: '#intelligentMapTableR',
      //url:'/demo/table/user/',
      width: 325,
      page: {
        layout: ['count', 'prev', 'page', 'next'] //自定义分页布局
        ,limit:5
        ,groups: 1 //只显示 1 个连续页码
        ,first: false //不显示首页
        ,last: false //不显示尾页

      },
      cols: [[
        {field:'num', title: '编号',width:80}
        ,{field:'name', title: '名称',width:120}
        ,{field:'type', title: '类型',width:120}
      ]],
      data:[]
    });
  });

  $(".j-map-result").click(function(){
    //var activityId = $("#smartMapActivityId").val();
    var activityId =$("#activityId").text();
    $.ajax({
      url: "./mental/getMarkedInfo?activityId="+activityId,
      type:"get",
      dataType: "json",
      success:function(res){
        if( res.code != 0 ){
          showMessage(res.msg,"error");
          globalAttrs.mapSelectedData = null;
          return
        }
        var data = res.data.data;
        var selectedType= ["非实时用户","实时用户"];
        if( !data || !data.length ){
          globalAttrs.mapSelectedData = null;
          return
        }
        globalAttrs.mapSelectedData = data;
        var tableData = [];
        $.each(data,function(i,item){
          var temp = {};
          temp.num = i+1;
          temp.name = item.name+"(0)";
         // var type = selectedType[item.type];
          var type = "";
          if(item.type == 1){
            type = "实时用户";
          }else{
            type = "非实时用户";
          }
          temp.type = type;
          tableData.push(temp);
        })

        var table = layui.table;
        table.reload('intelligentMapTableR', {
          data:tableData
        });
      },
      error:function(){
        showMessage("获取数据失败","error")
      }
    })
  })
})