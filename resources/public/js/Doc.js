/**
 * 接口层  
 * @param {Object} listener
 * @param {string} api  通讯接口api
 */
function Doc(listener,docUrl, commandUrl){
	var _app = new App(this,docUrl, commandUrl);
	this.listener = listener;
	this.disposeComError = function(err){
		//
	}
	//获取文档数据请求回调
	this.dispose_getDoc = function(msg){
		this.listener.htmlDoc(msg);
	}
	//执行代码请求回调
	this.dispose_cmd = function(msg){
		this.listener.htmlCmd(msg);
	}
	//获取文档数据列表请求
	this.getDoc = function(){
		_app.getDoc();
	}
	//执行代码请求
	this.runCmd = function(obj){
		_app.runCmd(obj);
	}
}