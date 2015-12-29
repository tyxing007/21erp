var api = frameElement.api,oper = api.data.oper,id=api.data.id,
	companyId = api.data.companyId,
$_form=$("#base_form"),addNew=false,url=rootPath+"/sso/position";
var model=avalon.define({
	$id:'ctrl',
	data:{code:[]},
	checkAll:false,
	showMenu: function (code) {
		haveCompany = SYSTEM.rights[code] == true;
		return haveCompany;
	}
});
var THISPAGE = {
	init : function() {
		this.initDom();
		this.initBtn();
	},
	initDom : function() {
		$("#id").val(id);
		var name=api.data.name;
		name=name.replace("├","").replace("-","").replace("-","").replace(" ","").replace("&nbsp;","");
		$("#position").val(name);
		if(id!=undefined&&id!=''&&id!='undefined'){
			Public.ajaxPost(url+"/qryPermission.json",{id:id,companyId:companyId}, function(json){
				if(json.status==200){
					var s=json.data;
					if(s!=null&&s!=''&&s!='null'){
						var ss=s.split(",");
//						for(var i=0;i<ss.length;i++){
//							$(":checkbox[value='"+ss[i]+"']").click();
//						}
						model.data.code=ss;
					}
					THISPAGE.initEvent();
				}else{
					parent.Public.tips({type: 1, content : json.msg});
				}
			});
		}else{
			THISPAGE.initEvent();
		}
	},
	initBtn:function(){
		var e =["<i class='fa fa-save mrb'></i>保存", "关闭" ];
		api.button({
			id : "confirm",
			name : e[0],
			focus : !0,
			callback : function() {
				postData();
				return false
			}
		}, {
			id : "cancel",
			name : e[1]
		})
	},
	initEvent:function(){
		$("#chk_all").click(function(){
			var len  = $(":checkbox[name='code']").length;
			var check  = $(":checkbox[name='code']");
			for(var i=0;i<len;i++){
				check[i].checked = this.checked;
			}
		});
	}
};
function postData(){
	var e="设置权限";
	var data =$_form.serialize();
	data += "&companyId="+companyId
		//companyId
	Public.ajaxPost(url+"/savePermission.json",data, function(t){
		if (200 == t.status) {
			parent.parent.Public.tips({
				content : e + "成功！"
			});
			parent.THISPAGE.reloadData({companyId:companyId});
			api.close();
		} else
			parent.parent.Public.tips({
				type : 1,
				content : e + "失败！" + t.msg
			});
	});
}
THISPAGE.init();