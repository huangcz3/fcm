(function(window, undefined){
				 
				  /**
				 * 检查是否含有类名
				 * @param element
				 * @param className
				 * @returns {boolean}
				 */
				 function hasClassName(element, className){
					 var aClass = element.className.split(' ');
					  index = aClass.indexOf(className);
					 if(index > 0) return true;
					 return false;
 				 }
				 
				 var slideTable = function(obj){
				 	this.firstIndex = obj.firstIndex || 1;     //所有可滑动列中可见的第一列
				 	this.displayCols = obj.displayCols;         //可滑动列可见列数
				 	this.totalCols = obj.totalCols;            //可滑动列总列数
				 	this.table = obj.table;                    //当前表格id
				 	this.init(obj);
				 }
				 

				 var proto = slideTable.prototype;
				 
				
				 proto.init = function(obj){
				 	 proto.curFirstIndex = this.firstIndex;
				 	 var colTds;
				 	 this.cols = {};
				 	 if(this.firstIndex == 1){
				 	 	$(this.table+' .pre-td').addClass('clicked-disabled');
				 	 }
				 	
				 	 /*
				 	 *查找所有含有data-index属性的列
				 	 */
				 	 for(var col="col-",eachCol,i=1;i<this.totalCols+1;i++){
  					 	if( $(this.table+" td[data-index='col-"+i+"']")){
  					 		eachCol = col + i;
  					 		(this.cols)[eachCol] = $(this.table+" td[data-index='col-"+i+"']");
  					 	}
  					 }
				 	
  					 /*
  					 *根据条件显示相应的列
  					 */
  					 for(var col="col-",eachCol,i=this.firstIndex;i<this.displayCols+this.firstIndex;i++){						
  						 
  					 	eachCol = col + i;
  					 	colTds = (this.cols)[eachCol];
  					 
  					 	for(var j=0;j<colTds.length;j++){
  					 		
  					 		$(colTds[j]).removeClass("hide");
  					 	}
  					 }
  					 
  					 $(this.table+" thead")[0].onclick = this._doClick.bind(this);
				 }
				 proto._doClick = function(e){
				 	 var e = e || window.event,
 					 target = e.target || e.srcElement;
					 //点击除两个控制按钮td之外的元素
					 if(!hasClassName(target, 'slide-btn')) return;
					 //点击了不能点击的控制按钮
					 if(hasClassName(target,'click-disabled')) return;
		 
					 //点击了“<”按钮
					 if(target == $(this.table+' .pre-td')[0]){
					 	if(this.firstIndex == 1){
					 		$(this.table+' .pre-td').addClass('click-disabled');
					 	}else{
					 		$(this.table+' .pre-td').removeClass('click-disabled');
					 		$(this.table+' .next-td').removeClass('click-disabled');
					 		$(this.table+" td[data-index]").addClass('hide');
						 	for(var col="col-",eachCol,i=this.firstIndex-1;i<this.displayCols+this.firstIndex-1;i++){
						 		
		  					 	eachCol = col + i;
		  					 	colTds = (this.cols)[eachCol];
		  					 
		  					 	for(var j=0;j<colTds.length;j++){	
		  					 		$(colTds[j]).removeClass("hide");
		  					 	}
	  					 	
	  					 	}
					 		this.firstIndex--;
					 	}
					 	
					 }
					  //点击了“>”按钮
					 if(target == $(this.table+' .next-td')[0]){
					 	if(this.firstIndex+this.displayCols == this.totalCols+1){
					 		$(this.table+' .next-td').addClass('click-disabled');
					 	}else{
					 		$(this.table+' .pre-td').removeClass('click-disabled');
					 		$(this.table+' .next-td').removeClass('click-disabled');
					 		$(this.table+" td[data-index]").addClass('hide');
						 	for(var col="col-",eachCol,i=this.firstIndex+1;i<this.displayCols+this.firstIndex+1;i++){
					
		  					 	eachCol = col + i;
		  					 	colTds = (this.cols)[eachCol];
		  					
		  					 	for(var j=0;j<colTds.length;j++){	
		  					 		$(colTds[j]).removeClass("hide");
		  					 	}
	  					 	
	  					 	}
					 		this.firstIndex++;
					 	}
					 	
					 }
					 
				 }
				 
				window.slideTable = slideTable;
			})(window);
	    	