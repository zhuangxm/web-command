function Page(){
	var _lib = jQuery;
	var This = this;
	var _ui = new Ui(this,_lib);
	var _doc = new Doc(this,"/getDoc","/eval.json");
	function _constructor(){
		_lib(document).ready(function(){
			This.getDoc();
		});
	}
	this.htmlDoc = function(msg){
		//data:为文档数据数组{title:"文档标题",content:"文档说明内容",code:"默认执行代码",cmd:"执行代码的指令"}
		//msg = {msgID:"getDoc",data:[{title:"show",doc:"根据ID获取单条微博信息内容",arglist:"x,y",command:"show"} {title:"update_privacy",doc:"account/update_privacy",arglist:"x1,x2",command:"update_privacy"}]};
		_ui.drawDoc(msg,"#j_docList","#j_docContent",'#j_editor','#j_argList'); 
	}
	this.htmlCmd = function(msg){
		//msg = {isSucceed:true,data:"执行结果"};
		_ui.cmdResult(msg,"#j_cmdResult");
	}
	/**
	 * 获取文档数据
	 */
	this.getDoc = function(){
		_doc.getDoc();
	}
	/**
	 * 执行代码
	 * @param {json格式} obj     {cmd:"执行代码的命令",code:"需要执行的代码"}
	 */
	this.runCmd = function(obj){
		_doc.runCmd(obj);
	}
	_constructor();
}

var _page = new Page();
