var api = frameElement.api, oper = api.data.oper, id = api.data.id, $_form = $("#base_form"),
    area = SYSTEM.area, url = rootPath + "/sso/company",custParame = SYSTEM.custParame, typeList = custParame.typeList;
var model = avalon.define({
    $id: 'createCompany',
    data: {
        id:"",
        name:"",
        code:"",
        short_name:"",
        industry:"",
        industry_name:"",
        telephone:"",
        fax:"",
        province:"",
        city:"",
        reg_email:"",
        address:"",
        expiry_date:"",
        remark:"",
        reg_date:""
    },
    provinceList: area.provinceList,
    parameList: typeList,
    cityList: [],
    changeCity: function (v) {
        model.cityList = area[v + ""];
    }
});

//model.data.$watch("$all", function (name, a, b) {
//    if (a == null || a == "null") {
//        model.data[name] = "";
//    }
//});
//model.contacts.$watch("$all", function (name, a, b) {
//    if (a == null || a == "null") {
//        model.contacts[name] = "";
//    }
//});

var THISPAGE = {
    init: function () {
        this.initDom();
        this.initBtn();
    },
    initDom: function () {
        var v = new Date();
        $(".ui-datepicker-input").datepicker({minDate:new Date()});
        if (id != undefined && id != '' && id != 'undefined') {
            Public.ajaxPost(url + "/qryOp.json", {id: id}, function (json) {
                if (json.status == 200) {
                    avalon.mix(model.data, json.data);
                    model.changeCity(model.data.province);
                } else {
                    parent.Public.tips({type: 1, content: json.msg});
                }
            });
        }
        if("add" == oper){
            $("#name").attr("disabled",false);
            $("#short_name").attr("disabled",false);
        }else {
            $("#name").attr("disabled",true);
            $("#short_name").attr("disabled",true);
        }


        THISPAGE.initEvent();
    },
    initBtn: function () {
        var e = "add" == api.data.oper ? ["<i class='fa fa-save mrb'></i>保存", "关闭"] : ["<i class='fa fa-save mrb'></i>确定", "取消"];
        api.button({
            id: "confirm",
            name: e[0],
            focus: !0,
            callback: function () {
                $_form.trigger("validate");
                return false
            }
        }, {
            id: "cancel",
            name: e[1]
        })
    },
    initEvent: function () {
        this.initValidator();
    },
    initValidator: function () {

        //jQuery.validator.addMethod("fax",function(value,element){
        //    var fax = /^(\d{3,4})?[-]?\d{7,8}$/;
        //    return this.optional(element) || (fax.test(value));
        //},"传真格式如：0371-68787027");

        $_form.validator({
            messages: {
                required: "请填写{0}"
            },
            display: function (e) {
                return $(e).closest(".row-item").find("label").text()
            },
            valid: function () {
                postData();
            },
            ignore: ":hidden",
            theme: "yellow_bottom",
            timely: 1,
            stopOnError: true
        });
    }
};
function postData() {
    var e = "add" == oper ? "新增企业" : "修改企业";
    var urlType = "add" == oper ? "/saveCompany" : "/updateCompany";
    Public.ajaxPost(url + urlType, model.data.$model, function (json) {
        if (200 == json.status) {
            parent.parent.Public.tips({
                content: e + "成功！"
            });
            parent.THISPAGE.reloadData(null);
            //reloadCache();
            api.data.callback();
        } else
            parent.parent.Public.tips({
                type: 1,
                content: e + "失败！" + json.msg
            });
    });
}
THISPAGE.init();