var $_form = $("#base_form");
var companyList = SYSTEM.companyList
var model = avalon.define({
    $id: "ctrl",
    data: {
        city: "",
        code: "",
        wx_secret: "",
        industry: "",
        wxid: "",
        province: "",
        wx_account: "",
        file_storage_size: 0,
        fax: "",
        area: "",
        reg_email: "",
        wx_appid: "",
        address: "",
        wx_type: "",
        expiry_date: "",
        telephone: "",
        reg_date: "",
        name: "",
        wx_qrcode: "",
        short_name: "",
        config: {
            p_pact_alarm: 30,
            p_sale_audit: "false",
            p_sysname: "",
            p_saletui_audit_type: "1",
            p_sale_audit_type: "1",
            p_custpool_c: 90,
            p_saletui_audit: "false",
            p_member_card_pay: "false",
            p_finance: "000",
            p_financetui: "000",
            p_auto_average: "false",
            p_averageJob: ""
        }
    },
    userList: [],
    parameList: SYSTEM.custParame.typeList,
    provinceList: SYSTEM.area.provinceList,
    cityList: [], changeCity: function (v) {
        model.cityList = SYSTEM.area[v + ""];
    },

    showMenu: function (code) {
        return SYSTEM.rights[code] == true;
    },
    changeOrderAu: function (v) {
        model.data.config.p_finance = v;
    },
    changeBackOrderAu: function (v) {
        model.data.config.p_financetui = v;
    },
    reload: function (v) {
        Public.ajaxPost(rootPath + "/sso/user/getAlluser.json", {companyName: model.data.name}, function (json) {
            if (json.status == 200) {
                avalon.mix(model.userList, json.data);
                Public.ajaxPost(rootPath + "/sso/company/qry.json", {companyName: model.data.name}, function (json) {
                    if (json.status == 200) {
                        avalon.mix(model.data, json.data);
                        model.changeCity(model.data.province);
                    } else {
                        parent.Public.tips({type: 1, content: json.msg});
                    }
                });
            } else {
                parent.Public.tips({type: 1, content: json.msg});
            }
        });


    },
    companyList: companyList,
    radio1: function (v) {
        model.data.config.p_sale_audit = v;
        //model.data.config.p_sale_audit_type = '1';
        if (v && (model.data.config.p_sale_audit_type == undefined || model.data.config.p_sale_audit_type == "")) {
            model.data.config.p_sale_audit_type = '1';
        }
    },
    radio2: function (v) {
        model.data.config.p_saletui_audit = v;
        if (v && (model.data.config.p_saletui_audit_type == undefined || model.data.config.p_saletui_audit_type == "")) {
            model.data.config.p_saletui_audit_type = '1';
        }

    },
    radio3: function (v) {
        model.data.config.p_sale_audit_type = v;
    },
    radio4: function (v) {
        model.data.config.p_saletui_audit_type = v;
    },
    radio5: function (v) {
        model.data.config.p_member_card_pay = v;
    },
    radio6: function (v) {
        model.data.config.p_sale_finance_type = v;
    },
    radio7: function (v) {
        model.data.config.p_sale_financetui_type = v;
    },
    radio8: function (v) {
        model.data.config.p_auto_average = v;
    },
    autoAddZero: function (v) {
        model.data.config.p_averageJob = PrefixInteger(v, 2);
    }

});

function PrefixInteger(num, n) {
    return (Array(n).join(0) + num).slice(-n);
}
var THISPAGE = {
    init: function () {
        this.initDom();
        this.initEvent();
    },
    initDom: function () {


        Public.ajaxPost(rootPath + "/sso/user/getAlluser.json", {}, function (json) {
            if (json.status == 200) {
                avalon.mix(model.userList, json.data);
                Public.ajaxPost(rootPath + "/sso/company/qry.json", {}, function (json) {
                    if (json.status == 200) {
                        avalon.mix(model.data, json.data);
                        model.changeCity(model.data.province);
                    } else {
                        parent.Public.tips({type: 1, content: json.msg});
                    }
                });
            } else {
                parent.Public.tips({type: 1, content: json.msg});
            }
        });


        THISPAGE.initEvent();
    },
    initEvent: function () {
        this.initValidator();
    },
    initValidator: function () {
        $_form.validator({
            messages: {required: "请填写{0}"},
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
    if (model.data.config.p_auto_average && (parseInt(model.data.config.p_averageJob) < 0 || parseInt(model.data.config.p_averageJob) > 28)) {
        parent.parent.Public.tips({type: 1, content: "自动平均数据有误，请重新输入！（大于0且小于28的整数）"});
        return
    }
    Public.ajaxPost(rootPath + "/sso/company/saveConfig.json", model.data.$model, function (t) {
        if (200 == t.status) {
            parent.parent.Public.tips({content: "保存成功！"});
        } else {
            parent.parent.Public.tips({type: 1, content: "保存失败！" + t.msg});
        }
    });
}
THISPAGE.init();