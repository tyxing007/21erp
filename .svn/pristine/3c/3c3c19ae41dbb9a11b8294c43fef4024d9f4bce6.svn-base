var parameData,api = frameElement.api,oper = api.data.oper,id=api.data.id,
$_form=$("#base_form"),addNew=false,parameType=CONFIG.custParameType,companyList = SYSTEM.companyList,parentTree
	parentType=api.data.type
	;
var model=avalon.define({
	$id:'ctrl',
	data:{type:'',name:"",parent_id:"",parent_name:"",sort_num:1,description:"",id:"",is_end:0,companyId:""},
	parameType:parameType,
	chk:function(i){
		model.data.is_end=i;
	},
	companyList : companyList,
	showMenu: function (code) {
		return SYSTEM.rights[code] == true;
	}
});
model.data.$watch("$all",function(name,a,b){
	if(a==null||b==null)
		model.data[name]="";
});
var THISPAGE = {
	init : function() {
		this.initDom();
		this.initBtn();
	},
	reset:function(){
		model.data.id="";
		model.data.name="";
		model.data.parent_id=(api.data.parent_id?api.data.parent_id:"");
		model.data.parent_name=(api.data.parent_name?api.data.parent_name:"");
	},
	initDom : function() {
		if((parentType==""?-1:parentType)!=-1){
			//$("#classType")
			//$("#type").attr({readonly:true});
			$("#type").change(function(){
				this.selectedIndex=(parentType+1);
			})

		}
		model.data.type=api.data.type.toString()?api.data.type:"";
		this.reset();
		if(id!=undefined&&id!=''&&id!='undefined'){
			Public.ajaxPost(rootPath+"/sso/parame/qryOp.json",{id:id}, function(json){
				if(json.status==200){
					avalon.mix(model.data,json.data);
					THISPAGE.initEvent();
				}else{
					parent.Public.tips({type: 1, content : json.msg});
				}
			});
		}else{
			THISPAGE.initEvent();
		}
		if("add" != api.data.oper){
			$("#companyChoose").hide();
		}
	},
	initBtn:function(){
		var e = "add" ==  api.data.oper ? [ "<i class='fa fa-save mrb'></i>保存", "关闭" ] : [ "<i class='fa fa-save mrb'></i>确定", "取消" ];
		api.button({
			id : "confirm",
			name : e[0],
			focus : !0,
			callback : function() {
				addNew=false;
				$_form.trigger("validate");
				return false
			}
		}, {
			id : "saveAndNew",
			name : "<i class='fa fa-save mrb'></i>保存并新建",
			focus :false,
			callback : function() {
				addNew=true;
				$_form.trigger("validate");
				return false
			}
		}, {
			id : "cancel",
			name : e[1]
		})
	},
	initEvent:function(){
		/**类别combo START*/
		var r = $("#parent_name");
		var i=Public.comboTree(r,{offset:{top:0,left:94},url:rootPath+'/sso/parame/tree.json',postData:{type:parentType},
			callback:{
				beforeClick : function(e, t) {
					r.val(t.name);model.data.parent_id=t.id;i.hide();
				}
			},treeId:'parentNameTree'
		});

		parentTree = i;
		/**类别combo END*/
		this.initValidator();
	},
	initValidator:function() {
		$_form.validator({
			messages : {
				required : "请填写{0}"
			},
			display : function(e) {
				return $(e).closest(".row-item").find("label").text()
			},
			valid : function() {
				postData();
			},
			ignore : ":hidden",
			theme : "yellow_bottom",
			timely : 1,
			stopOnError : true
		});
	}
};
function blurParent(){
	if(parentTree){
		parentTree.hide();
	}
};


function changeType(v){
	if(parentTree){
		parentTree.hide();
		parentTree.remove();
		$.fn.zTree.destroy("parentNameTree");
	}
	/**类别combo START*/
	var r = $("#parent_name");
	var i=Public.comboTree(r,{offset:{top:0,left:94},url:rootPath+'/sso/parame/tree.json',postData:{type:v},
		callback:{
			beforeClick : function(e, t) {
				r.val(t.name);model.data.parent_id=t.id;i.hide();
			}
		},treeId:'parentNameTree'
	});

	parentTree = i;
	/**类别combo END*/

};
function postData() {
	var e = "add" == oper ? "新增" : "修改" + "参数";
	Public.ajaxPost(rootPath + "/sso/parame/save.json", model.data.$model, function (t) {
		if (200 == t.status) {
			parent.parent.Public.tips({
				content: e + "成功！"
			});
			model.data.id = (t.data);
			parent.THISPAGE.reloadData(null);

			if (addNew) {
				THISPAGE.reset();
			} else {
				api.close();
			}
		} else
			parent.parent.Public.tips({
				type: 1,
				content: e + "失败！" + t.msg
			});
	});
}
THISPAGE.init();