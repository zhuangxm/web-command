/**
 * 应用层
 */
function App(listener,docUrl,commandUrl){
	var _docUrl = docUrl;
	var _commandUrl = commandUrl;
	var _com = new HttpCom(this);
	var _sendMsg = function(url,obj,type){
		_com.sendMsg(url,obj,type);
	}
	var _this = this;
	this.listener = listener;
	this.disposeMsg = function(msg){
		var msgID=msg.msgID;
		delete msg.msgID;
		var fn=_this.listener["dispose_" + msgID];
		((typeof (fn) == "function")&&(fn.call(_this.listener,msg)));
	}
	
	this.getDoc = function(){
		var _obj = {
			msgID:"getDoc"
		}
		_sendMsg(docUrl,_obj);
	}
	this.runCmd = function(obj){
		obj.msgID = "cmd";
		_sendMsg(commandUrl,obj);
	}

}
