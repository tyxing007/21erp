var api = frameElement.api, oper = api.data.oper, id = api.data.id, $_form = $("#base_form"),
    area = SYSTEM.area, url = rootPath + "/sso/company",custParame = SYSTEM.custParame, typeList = custParame.typeList;
var model = avalon.define({
    $id: 'companyView',
    data: {
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
        remark:""
    },
    provinceList: area.provinceList,
    parameList: typeList,
    cityList: [],
    changeCity: function (v) {
        model.cityList = area[v + ""];
    }
});

avalon.filters.rating = function (v) {
    return typeList[v].name;
}

var THISPAGE = {
    init: function () {
        this.initDom();
        //this.loadGrid();
    },
    initDom: function () {
        if (id != undefined && id != '' && id != 'undefined') {
            Public.ajaxPost(url + "/qryOp.json", {id: id}, function (json) {
                if (json.status == 200) {
                    model.data = json.data;
                    if (model.data.head_id == undefined) {
                        model.data.head_id = "";
                        model.data.head_name = "";
                    }
                } else {
                    parent.Public.tips({type: 1, content: json.msg});
                }
            });
        }
        //THISPAGE.initEvent();
    }
}

THISPAGE.init();