var mapObj;
var addressStr = "";
var areaList = ["010", "021", "022", "023"];
var areaStr;

function initAddress() {
    var P = "";
    var C = "";
    var areaIndex;
    var Pindex = addressStr.indexOf("省");
    var Cindex = addressStr.indexOf("市");
    var isArea = false;


    /*判断是不是直辖市，是就先保存*/
    for (var i = 0; i < areaList.length; i++) {
        if (areaStr == areaList[i]) {
            isArea = true;
            break;
        }
    }

    /*如果是直辖市就开始按直辖市划分*/
    if (isArea) {
        areaIndex = addressStr.indexOf("区");
        P = addressStr.substring(0, Cindex);
        C = addressStr.substring(Cindex + 1, areaIndex);
    } else {
        P = addressStr.substring(0, Pindex);
        C = addressStr.substring(Pindex + 1, Cindex);
    }

    for (var province in model.provinceList) {
        var provinceV = model.provinceList[province];
        if (provinceV.name == P && model.data.province != provinceV.id) {
            model.data.province = provinceV.id;
            model.changeCity(provinceV.id);
            break;
        }
    }

    for (var city in model.cityList) {
        var cityV = model.cityList[city];
        if (cityV.name == C) {
            model.data.city = cityV.id;
            break;
        }
    }

}
/*初始化地图*/
function initialize() {

    mapObj = new AMap.Map("initMaps", {
        view: new AMap.View2D({
            resizeEnable: true
        }),
        lang: "zh_cn"
    });


    mapObj.plugin(['AMap.ToolBar'], function () {
        var toolBar = new AMap.ToolBar();
        mapObj.addControl(toolBar);
    });

    /*地理编码*/

    addressStr = provinceName + cityName + model.data.address + "";
    if (addressStr == "NaN") {
        addressStr = "福建省厦门市集美大学";
    }


    var MGeocoder;
    //加载地理编码插件
    mapObj.plugin(["AMap.Geocoder"], function () {     //加载地理编码插件
        MGeocoder = new AMap.Geocoder();
        //返回地理编码结果
        AMap.event.addListener(MGeocoder, "complete", geocoder_CallBack_address);
        MGeocoder.getLocation(addressStr);  //地理编码
    });


    //为地图注册click事件获取鼠标点击出的经纬度坐标
    var clickEventListener = AMap.event.addListener(mapObj, 'click', function (e) {
        var lat = e.lnglat.getLat();
        var lng = e.lnglat.getLng();


        var lnglatXY = new AMap.LngLat(lng, lat);

        document.getElementById("lng").value = lat;
        document.getElementById("lat").value = lng;
        document.getElementById("latlng").value = lat + "," + lng;

        var MGeocoder;
        AMap.service(["AMap.Geocoder"], function () {
            //逆地理编码
            MGeocoder = new AMap.Geocoder({
                radius: 1000,
                extensions: "all"
            });
            MGeocoder.getAddress(lnglatXY, function (status, result) {
                if (status === 'complete' && result.info === 'OK') {
                    geocoder_CallBack(result, lnglatXY, mapObj);
                }
            });
        });
    });

}

/*将检验地图位置的控制清除*/
function main() {
    haveNullValue = false;
}
//地理编码返回结果展示
function geocoder_CallBack_address(data) {

    var marker;
    //地理编码结果数组
    var geocode = new Array();
    geocode = data.geocodes;
    mapObj.clearMap();
    for (var i = 0; i < geocode.length; i++) {
        //拼接输出html
        marker = new AMap.Marker({ //创建自定义点标注
            map: mapObj,
            position: new AMap.LngLat(geocode[i].location.getLng(), geocode[i].location.getLat()),
            offset: new AMap.Pixel(0, 0),
            icon: "http://webapi.amap.com/images/marker_sprite.png"
        });
        marker.setLabel({
                offset: new AMap.Pixel(-0, -20),

                content: geocode[i].formattedAddress
            }
        );
    }


    mapObj.setFitView();

}


//回调函数
function geocoder_CallBack(data, lnglat, map) {
    var marker;
    var address;
    var area;
    //返回地址描述
    address = data.regeocode.formattedAddress;
    area = data.regeocode.addressComponent.citycode;
    var str = "<div>" + address + "</div>";
    document.getElementById("RequestAddress").innerHTML = str;
    map.clearMap();
    marker = new AMap.Marker({ //创建自定义点标注
        map: map,
        position: lnglat,
        offset: new AMap.Pixel(-10, -33),
        icon: "http://webapi.amap.com/images/marker_sprite.png"
    });


    marker.setLabel({
            offset: new AMap.Pixel(0, -20),
            content: address
        }
    );
    document.getElementById("address").value = address;
    addressStr = address;
    areaStr = area;
}

var api = frameElement.api, oper = api.data.oper, id = api.data.id, $_form = $("#base_form"),
    area = SYSTEM.area, custRating = SYSTEM.custRating, custParame = SYSTEM.custParame, typeList = custParame.typeList, url = rootPath + "/crm/customer", urlFile = rootPath + "/file",
    companyId = api.data.companyId, cityName, provinceName, haveNullValue;
;
var model = avalon.define({
    $id: 'view',
    data: {
        address: "",
        amt: 0,
        city: "",
        email: "",
        ent_cate: "",
        ent_stage: "",
        fax: "",
        head_id: SYSTEM.user.id,
        head_name: SYSTEM.user.realname,
        id: "",
        industry: "",
        integral: 0,
        is_deleted: 0,
        member_card: "",
        mobile: "",
        name: "",
        origin: "",
        ownership: "",
        province: "",
        province_name: "",
        rating: "",
        remark: "",
        sn: "",
        staff_size: "",
        status: 0,
        telephone: "",
        type: 1,
        year_turnover: "",
        zip_code: "",
        companyId: companyId,
        lat: "",
        lng: ""
    },
    //首要联系人
    contacts: {
        name: "",
        type: 1,
        sex: 1,
        mobile: "",
        is_main: 1,
        post: "",
        idcard: "",
        department: "",
        saltname: "",
        telephone: "",
        qq: "",
        email: "",
        address: "",
        zip_code: "",
        customer_id: "",
        description: "",
        id: ""
    },
    provinceList: area.provinceList, custType: parent.type,
    cityList: [],
    tabActive: 0,
    parameList: typeList,
    custRating: custRating.list,
    changeCity: function (v) {
        var province = area.provinceList;
        for (var i = 0; i < province.length; i++) {
            if (province[i].id == v) {
                provinceName = province[i].name;
            }
        }
        model.cityList = area[v + ""];
    },
    getCity: function (v) {
        var cityList = area[model.data.province + ""];
        for (var i = 0; i < cityList.length; i++) {
            if (cityList[i].id == v) {
                cityName = cityList[i].name;
            }
        }
    },
    showTab: function (i, b) {
        if (haveNullValue == true) {
            return false;
        }
        model.tabActive = i;
    },
    setType: function (v) {
        model.data.type = v;
        model.contacts.type = v;
    },
    setSex: function (v) {
        model.contacts.sex = v;
    }
});
model.data.$watch("$all", function (name, a, b) {
    if (a == null || a == "null") {
        model.data[name] = "";
    }
});
model.contacts.$watch("$all", function (name, a, b) {
    if (a == null || a == "null") {
        model.contacts[name] = "";
    }
});
var THISPAGE = {
    init: function () {
        this.initDom();
        this.initBtn();
    },
    upload: function () {
        $("#upfile_btn").uploadify('upload', "*");
    },
    initDom: function () {
        if (id != undefined && id != '' && id != 'undefined') {
            Public.ajaxPost(url + "/qryOp.json", {id: id}, function (json) {
                if (json.status == 200) {
                    avalon.mix(model.data, json.data.customer);
                    model.changeCity(model.data.province);
                } else {
                    parent.Public.tips({type: 1, content: json.msg});
                }
            });
            Public.ajaxPost(rootPath + "/crm/contacts/mainContacts.json", {customer_id: id}, function (json) {
                if (json.status == 200) {
                    avalon.mix(model.contacts, json.data);
                }
            });
        }
        /**负责人*/
        var r = $("#headCombo");
        var i = Public.comboTree(r, {
            offset: {top: 0, left: 94}, url: rootPath + '/sso/user/userTree.json', postData: {type: 2}, callback: {
                beforeClick: function (e, t) {
                    if (t.type == 10) {
                        r.val(t.name);
                        model.data.head_id = t.id;
                        i.hide();
                    } else {
                        r.val("");
                        model.data.head_id = "";
                    }
                }
            }
        });
        /**负责人combo END*/
        THISPAGE.initEvent();
        /*by chenjianhui 2015年8月27日12:59:51*/
        /*文件上传*/

        $("#upfile_btn").uploadify({

            'auto': false, /*设置成不自动上传*/
            'height': 30,
            'swf': rootPath + '/assets/js/plugins/uploadify/uploadify.swf',
            'uploader': urlFile + '/upImg',
            'width': 220,
            'method': 'get',
            'formData': {"key": "someValue"},
            'buttonClass': "fileStyle",
            'buttonText': '点我选择照片哟',//浏览文件',
            'fileTypeExts': '*.png;*.jpg',
            'onUploadStart': function (file) {
                alert("onUploadStart");
                //$("#upfile_btn").uploadify("settings", "Key", 2);
            },
            'onUploadSuccess': function (file, data, response) {

                alert("success");
            },
            'onUploadError': function () {
                alert("error");
            }
        });
        /*-----------*/
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
    },
    addTypeClick: function (type) {
        if (Business.verifyRight("TF_ADD")) {
            THISPAGE.operateType({id: null, type: type, pid: null, oper: 'add'});
        }
    },
    operateType: function (opt) {
        if ("add" == opt.oper)
            var i = "新增参数";
        else
            var i = "修改参数";
        parent.$.dialog({
            title: i,
            content: "url:" + rootPath + "/sso/parame" + "/edit.html",
            data: opt,
            width: 400,
            height: 400,
            max: false,
            min: false,
            cache: false,
            lock: true,
            close: function () {
                /** 获取自定义参数 */
                Public.ajaxPost(rootPath + "/sso/parame/list.json", {}, function (json) {
                    model.parameList = json.typeList;

                });
                /**客户等级参数*/
                Public.ajaxPost(rootPath + "/crm/custRating/list.json", {}, function (json) {
                    model.custRating = json.data.list;
                });
            }
        })
    }

};

function postData() {


    var e = "add" == oper ? "新增客户" : "修改客户";
    if (model.contacts.name == '') {
        model.tabActive = 1;
        parent.parent.Public.tips({
            type: 1,
            content: "联系人姓名必填！"
        });
        return;
    }
    //telephone/mobile
    if (model.contacts.telephone == '' && model.contacts.mobile == "") {
        model.tabActive = 1;
        parent.parent.Public.tips({
            type: 1,
            content: "联系人联系方式必填（电话/手机选其一）！"
        });
        return;
    }
    Public.ajaxPost(url + "/save.json", model.data.$model, function (json) {
        if (200 == json.status) {

            /*联系人保存成功后，上传图片文件！*/
          /*  $("#upfile_btn").uploadify("settings", "formData", {"customer_id": json.data.id});
            THISPAGE.upload();*/


            parent.parent.Public.tips({
                content: e + "成功！"

            });
            model.data.id = json.data.id;
            model.data.sn = json.data.sn;
            model.contacts.customer_id = json.data.id;
            Public.ajaxPost(rootPath + "/crm/contacts/save.json", model.contacts.$model, function (json_) {
                if (json_.status == 200) {
                    model.contacts.id = json.data.id;
                } else {
                    parent.parent.Public.tips({
                        type: 1,
                        content: "保存联系人失败！" + json_.msg
                    });
                }
            });
            parent.THISPAGE.reloadData(null);


        } else
            parent.parent.Public.tips({
                type: 1,
                content: e + "失败！" + json.msg
            });
    });
}
THISPAGE.init();