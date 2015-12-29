var codeUrl=rootPath+"/ValidCode/jpg?id=";
var model = avalon.define({
	$id : "loginCtrl",
	data : {
		userno : "",
		pwd : "",
		company : "",
		validCode : "",
		autoLogin : true
	},
	errMsg : "",
	vcUrl:codeUrl,
	vcV:false,
	showVc:function(){
		model.vcV=true;
	},
	ldCode:function(){
		model.vcUrl=codeUrl+(new Date()).getTime();
	},
	submit : function() {
		model.errMsg ="";
		if (model.data.company == "") {
			model.errMsg = "企业名称不能为空！";
			return;
		}
		if (model.data.userno == "") {
			model.errMsg = "帐号不能为空！";
			return;
		}
		if (model.data.pwd == "") {
			model.errMsg = "密码不能为空！";
			return;
		}
		if (model.data.validCode == "") {
			model.errMsg = "验证码不能为空！";
			return;
		}
		$.ajax({
			url : rootPath + '/login',
			data : model.data.$model,
			type : 'POST',
			dataType : "json",
			success : function(data) {
				if (data.success == false) {
					$("#validCodeImg").click();
					model.data.validCode = "";
					model.data.pwd = "";
					model.errMsg = data.msg;
				} else {
					window.location.reload();
					$.cookie("company_name",model.data.company)
				}
			}
		});
	}
}
);
if($.cookie("company_name") == undefined){
	model.data.company = "厦门二一科技";
}else{
	var company = $.cookie("company_name")
	model.data.company = company;
}

function  showImg(){
	document.getElementById("qrImg").style.display='block';
}
function hideImg(){
	document.getElementById("qrImg").style.display='none';
}


function  showInfo(){
	document.getElementById("infoMessage").style.display='block';
}
function hideInfo(){
	document.getElementById("infoMessage").style.display='none';
}



