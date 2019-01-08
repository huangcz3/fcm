var submit = function (){
	var tds = $("td[name]");
	var json = {};
	for (var i = 0, len = tds.length; i < len; ++i ){

		var inputEle = $($(tds[i]).find("*")[0]);
		json[$(tds[i]).attr("name")] = inputEle.val();
	}
	json=JSON.stringify(json);
	console.log(json);

	$.ajax({ 
		type: "post", 
		url: "http://10.108.226.169:8888/getOperationalSave", 
		data: {'data':json},	
		dataType: "json", 
		success: function (data) { 
			console.log("success!");
		}, 
		error: function (XMLHttpRequest, textStatus, errorThrown) { 
			console.log("failed!");
		} 
	});

	alert(123)

};


var query = function(page) {
	var $page = $("#"+page).find("li");
	var page = 0;
	for (var i = 0, length = $page.length; i < length; ++i) {
		if ($($page[i]).hasClass("active")){
			page = i+1;
			break;
		}
	}
	var modelType = $($("#profile").find("#activity-type2")[0]).val()	;
	var uptime = $("#uptime2").val();
	var json = {
		dataType: page,
		templateType: modelType,
		templateSubmitTime: uptime
	}
	json = JSON.stringify(json);
	var data = {};
	console.log(json);
	$.ajax({ 
				type: "get", 
				url: "", 
				data: json,
				dataType: "json", 
				success: function (json) { 
					var tds = $('#profile').find("td[name]");
					data =json;
					console.log(json);
					for (var i = 0, len = tds.length; i < len; ++i ){
						$temp = $(tds[i]);
						$temp.text(json[$temp.attr("name")]);
					}
					// $(tds[i]).html("\<button class=\"btn btn-primary col-md-1 col-md-offset-1 \"
								//onClick=\"query('page2');\">查询\</button\>")
				}, 
				error: function (XMLHttpRequest, textStatus, errorThrown) { 
					console.log("failed!");
				} 
			});

}


function lookForDetails(){

	console.log($(this).parent().parent().children())
	var templateCode = $($(this).parent().parent().children()[0]).text();
	window.open("./model_details.html?templateCode="+templateCode); 


}