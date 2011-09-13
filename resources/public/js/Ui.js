/**
 * 界面层
 */
function Ui(listener, lib){
	this.listener = listener;
	var _lib = lib;
	var This= this;
	/**
	 * 代码执行区编辑器
	 * @param {string} dom  代码执行区编辑区dom id
	 */
	function editor(dom){
		dom = _lib(dom);
		var _editor = _lib("textarea",dom);
		var _btnRun = _lib("a.btn-run",dom);
		var _btnCancel = _lib("a.btn-cancel",dom);
		var _setFocus = function(){
			_editor.focus();
		}
		function _bindRun(cmd){
			_btnRun
			.unbind()
			.bind("click",function(){
				_runCmd(cmd);
			});
		}
		_btnCancel.bind("click",function(){
			_editor
			.val('')
			.focus();
		});
		function _runCmd(cmd){
			var _eContent = _lib.trim(_editor.val());
			if (_eContent == ""){
				alert("请输入您要执行的命令^_^");
				_setFocus();
				return false;
			}
			_eContent = _eContent.split('\n');
			This.listener.runCmd({
				cmd:cmd,
				code:_eContent
			});
		}
		return {
			defaul:function(val){
				_editor.val(val);
				return this;
			},
			run:function(cmd){
				_bindRun(cmd);
			}
		}
	}
	/**
	 * 绘制文档列表
	 * @param {json} msg  文档数据列表
	 * @param {string} lID  列表显示区的dom id
	 * @param {string} cID  文档内容显示区的dom id
	 * @param {string} editorID   代码执行区dom id
	 */
	this.drawDoc = function(msg,lID,cID,editorID,argListID){
		var _data = msg.data;
		var _listDom = _lib(lID);
		var _cDom = _lib(cID);
		var _editor = editor(editorID);
		var _argList = _lib(argListID);
		var _liHtml=[];
		var _drawContent = function(index){
			var _docInfo = _data[index];
			_cDom.html('<textarea readonly="true" class="editor" rows="10">'+_docInfo.doc+'</textarea>');
			_editor
			.defaul(_docInfo.command)
			.run(_docInfo.command);
			_argList.html('<p>' + _docInfo.arglists + '</p>')
		}
		_lib.each(_data,function(index,info){
			_liHtml.push('<li rel="'+index+'"><a href="javascript:;">'+info.title+'</a></li>');
		});
		_liHtml = _liHtml.join('');
		_listDom.html(_liHtml);
		_lib("li",_listDom).bind("click",function(){
			var _self = _lib(this);
			var _index = _self.attr("rel");
			_lib("li",_listDom).attr("class","");
			_self.attr("class","current");
			_drawContent(_index);
		});
		_lib("li:eq(0)").trigger("click");
	}
	/**
	 * 绘制代码执行结果
	 * @param {json} msg   执行代码返回的结果数据
	 * @param {string} domID  执行结果区dom id
	 */
	this.cmdResult = function(msg,domID){
		_lib(domID).html(msg.result);
	}
}
