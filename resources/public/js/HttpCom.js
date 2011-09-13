/**
* http通讯层
* 
*/

/*
 * listener：消息成功接收后的通过函数回调
 * listener.disposeMsgs(obj);
 * listener.disposeComError(errObj);
 */
String.prototype.trim = function(){
    return this.replace(/(^\s*)|(\s*$)/g, "");
}
function Ajax(url){
	this.type = "Post";
	this.cache = false;
	this.async = true;
	this.contentType = "application/x-www-form-urlencoded; charset=UTF-8";
	this.dataType = "json";
	this.success = function(){}
	this.error = function(){}
	var _this = this;
	var xmlHttp = null;
	var privates = {};
	var _accepts = {
			xml: "application/xml, text/xml",
			html: "text/html",
			script: "text/javascript, application/javascript",
			json: "application/json, text/javascript",
			text: "text/plain",
			_default: "*/*"
	};
	privates._createXMLHttp = function(){
		var _xmlhttp;
		try {
			_xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
		} 
		catch (ex) {
			try {
				_xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
			} 
			catch (ex) {
				_xmlhttp = new XMLHttpRequest();
			}
		}
		return _xmlhttp;
	}
	privates._error = function( msg ) {
		throw msg;
	}
	privates._parseJSON = function(data) {
		if (typeof data !== "string" || !data) {
			return null;
		}
		// Make sure leading/trailing whitespace is removed (IE can't handle it)
		data = data.trim();
		
		// Make sure the incoming data is actual JSON
		// Logic borrowed from http://json.org/json2.js
		if ( /^[\],:{}\s]*$/.test(data.replace(/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g, "@")
			.replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g, "]")
			.replace(/(?:^|:|,)(?:\s*\[)+/g, "")) ) {

			// Try to use the native JSON parser first
			return window.JSON && window.JSON.parse ?
				window.JSON.parse( data ) :
				(new Function("return " + data))();

		} else {
			this._error( "Invalid JSON: " + data );
		}
	}
	privates._httpData = function(xhr, type, s ) {
		var ct = xhr.getResponseHeader("content-type") || "",
			xml = type === "xml" || !type && ct.indexOf("xml") >= 0,
			data = xml ? xhr.responseXML : xhr.responseText;
		
		if ( xml && data.documentElement.nodeName === "parsererror" ) {
			this.error( "parsererror" );
		}
		if ( s && s.dataFilter ) {
			data = s.dataFilter( data, type );
		}
		if ( typeof data === "string" ) {
			if (type === "json" || !type && ct.indexOf("json") >= 0) {
				data = this._parseJSON(data);
			}
		}
		return data;
	}
	privates._SendBack = function(){
		try {
			if (xmlHttp.readyState == 4) {
				if (xmlHttp.status == 200) {
					var data = privates._httpData(xmlHttp, _this.dataType, _this);
					_this.success.call(_this, data);
				}
				else {
					var error = "error";
					_this.error(error);
				}
			}
		} 
		catch (e) {
			_this.error("error");
		}
		
	}
	this.send =	function ()
	{
		if (!_this.cache){
			url = url + "&_=" + Math.random();
		}
	    xmlHttp = privates._createXMLHttp();
	    if(xmlHttp == null)
	    {
	        alert("浏览器不支持XMLHttp");
	    }else
	    {
			
				var _dataType = _this.dataType;
				xmlHttp.onreadystatechange = privates._SendBack;
				xmlHttp.open(_this.type, url, _this.async);
				xmlHttp.setRequestHeader("Content-Type", _this.contentType);
				xmlHttp.setRequestHeader("Accept", _dataType && _accepts[_dataType] ? _accepts[_dataType] + ", */*" : _accepts._default);
				try {
					xmlHttp.send();
				} 
				catch (e) {
					_this.error("error");
				}
	    }
	}
}
function HttpCom(listener){
	this.listener = listener;
	var _this = this;
	this.sendMsg = function(sendurl, obj,param){
		function serialize(url,obj){    //序列化url
			 var tmps = [];
			 tmps.push(url);
			 for (var key in obj){
			 	tmps.push(encodeURI(obj[key]));
			 }
			 return tmps.join('/');
		}
		function serializeParam(url,obj){
			 var tmps = [];
			 for (var key in obj){
			 	tmps.unshift(key + "=" + encodeURIComponent(obj[key]));
			 }
			 return url+ "?"+tmps.join('&');			
		}
		var msg = "";
		msg = serializeParam(sendurl,obj);
		var ajax = new Ajax(msg);
		ajax.success = function(msg){
			try {
				dispatchMsgs(msg);
			} 
			catch (e) {
				
			}
		}
		ajax.error = function(err){
			try {
				_this.listener.disposeComError(err);
			} 
			catch (e) {
			}
		}
		ajax.send();
	}
	function dispatchMsgs(msgs){
		for (var i=0,len = msgs.length;i < len; i++) {
			_this.listener.disposeMsg(msgs[i]);
		}
		
	}
}

