var api = frameElement.api, oper = api.data.oper, id = api.data.id, custParame = SYSTEM.custParame, typeList = custParame.typeList,
    $_form = $("#base_form"), addNew = false, type = parent.type,bsSuggest;
var model = avalon.define({
    $id: 'ctrl', parameList: typeList, type: type,showBsSuggest:false,
    data: {
        id: "",
        is_deleted: 0,
        submit_status: 0,
        remark: "",
        bill_date: "",
        billsn: "自动生成",
        head_id: SYSTEM.user.id,
        head_name: SYSTEM.user.name,
        remark: "",
        depot_id: '',
        productlistlength: 1,
        comparison:1,
        productlist: []

//{amount: 0, amount2: 0, product_id: "", unit: "", product_name: ""}
    },
    depotList: [], userList: [],
    comparison: [],
    changeUnit:function(v){
        //alert(v);
        model.data.comparison = v;
    },
    qryHead: function (v) {
        Public.ajaxPost(rootPath + "/sso/user/dataGrid.json", {
            keyword: v,
            status: 1,
            _sortField: "name",
            _sort: "asc"
        }, function (json) {
            model.userList = json.data.list;
        });
    },
    //添加商品
    productlist: [], productComboDiv: -1,
    qryProduct: function (v, idx) {//自动完成查商品

        //model.productComboDiv = idx;
        //Public.ajaxPost(rootPath + "/scm/stock/dataGrid.json", {
        //    keyword: v,
        //    _sortField: "name",
        //    _sort: "asc",
        //    depot_id: model.data.depot_id
        //}, function (json) {
        //    model.productlist = json.data.list;
        //});
    },
    changBsSuggest:function(){
        model.showBsSuggest = true;
        model.data.productlistlength = 0;
        model.data.productlist = [];
        $("#productNameCheck").unbind("focus");
        if(bsSuggest){
            bsSuggest.bsSuggest("destroy");
        }
        THISPAGE.initBsSuggest(model.data.depot_id);
    },
    //chooseProduct: function (e, idx) {
    //    model.data.productlist[idx].product_id = e.product_id;
    //    model.data.productlist[idx].product_name = e.product_name;
    //    model.data.productlist[idx].unit = e.unit;
    //    model.data.productlist[idx].amount = e.amount;
    //    model.productComboDiv = -1;
    //},
    addRow: function (parame) {
        model.data.productlist.splice(0, 0, parame);
        //model.data.productlist.push(parame);
    },//增加商品行;
    delRow: function (e) {//删除商品行
        e.preventDefault()
        var item = this.$vmodel.$remove();
        model.data.productlist.remove(item);
    }
});
var THISPAGE = {
    init: function () {
        model.qryHead();
        this.initDom();
        this.initBtn();
    },
    initDom: function () {
        $(".ui-datepicker-input").datepicker();
        Public.ajaxPost(rootPath + "/scm/depot/list.json", {}, function (json) {
            if (json.status == 200) {
                model.depotList = json.data;
            } else {
                parent.Public.tips({type: 1, content: json.msg});
            }
        });
        if (id != undefined && id != '' && id != 'undefined') {
            Public.ajaxPost(rootPath + "/scm/stockCheck/qryOp.json", {id: id}, function (json) {
                if (json.status == 200) {
                    avalon.mix(model.data, json.data);
                    if (model.data.productlistlength == undefined || model.data.productlistlength == 0) {
                        model.data.productlistlength = 1;
                        model.data.productlist = [{amount: 0, amount2: 0, product_id: "", unit: "", product_name: ""}];
                    }
                    THISPAGE.initEvent();
                } else {
                    parent.Public.tips({type: 1, content: json.msg});
                }
            });
        } else {
            THISPAGE.initEvent();
        }
    },
    initBtn: function () {
        var e = "add" == api.data.oper ? ["<i class='fa fa-save mrb'></i>保存", "关闭"] : ["<i class='fa fa-save mrb'></i>确定", "取消"];
        api.button({
            id: "confirm",
            name: e[0],
            focus: !0,
            callback: function () {
                addNew = false;
                $_form.trigger("validate");
                return false
            }
        }, {
            id: "cancel",
            name: e[1]
        })
    },
    initEvent: function () {

        $("#input-group-btn").hide();
        //THISPAGE.initBsSuggest("");
        $("#productNameCheck").focus(function(){
            if(!model.data.depot_id){
                parent.parent.Public.tips({
                    content: '请先选择仓库'
                });
            }
        })
        $('#keynum').keyup(function (event) {
            if (event.keyCode == 13) {
                THISPAGE.addRow($('#barCodeCheck').val(), $('#keynum').val());
            }
        })
        $('#okIndex').click(function (event) {
            THISPAGE.addRow($('#barCodeCheck').val(), $('#keynum').val());
        })
        $('#search').click(function (event) {
            if(!model.data.depot_id){
                parent.parent.Public.tips({
                    content: '请先选择仓库'
                });
                return;
            }
            parent.$.dialog({
                title: '选择产品',
                content: "url:" + rootPath + "/scm/product/checkSimpleProduct.html",
                data: {checkSimple:true,url:rootPath + "/scm/stock/dataGrid.json?&status=1&depot_id="+model.data.depot_id},
                width: $(window.parent).width() / 3 * 2.7,
                height: $(window.parent).height() / 3 * 2.5,
                max: false,
                min: false,
                cache: false,
                lock: true,
                callBackFunction: function (checkProduct) {
                    var len = checkProduct.length;
                    for(var i=0;i<len;i++){
                        //THISPAGE.addRow(checkProduct[i].billsn,checkProduct[i].amount);
                        var parame = checkProduct[i];
                        var productLength = model.data.productlist.length;
                        for (var m = 0; m < productLength; m++) {
                            if (model.data.productlist[m].product_id == parame.product_id) {
                                model.data.productlist[m].amount2 = parseInt(model.data.productlist[m].amount2) + parseInt(parame.amount2);
                                parame = undefined;
                                break;
                            }
                        }
                        if (parame != undefined) {
                            model.addRow(parame);
                        }

                    }
                }
            })
        });
        this.initValidator();
    },
    initValidator: function () {
        $_form.validator({
            valid: function () {
                postData();
            },
            ignore: ":hidden",
            theme: "yellow_top",
            timely: 1,
            stopOnError: true
        });
    },
    addRow: function (barCode, num) {
        var double = 1;
        if (model.data.comparison != "" && model.data.comparison != undefined) {
            double = model.data.comparison;
        }
        if (barCode == "" || barCode == undefined || num == "" || num == undefined) {
            alert("您有参数未填写，请填写");
            return;
        }
        var len = productListBySuggest.length;
        var parame;
        for (var i = 0; i < len; i++) {
            if (productListBySuggest[i].billsn == barCode) {
                parame = {
                    product_id: productListBySuggest[i].product_id,
                    unit: productListBySuggest[i].unit,
                    amount: productListBySuggest[i].amount,
                    amount2: num * double,
                    billsn:barCode,
                    product_name: productListBySuggest[i].product_name
                }
            }
        }
        if (parame == undefined) {
            alert("未找到您想寻找的产品，请核对");
            return;
        } else {
            var productLength = model.data.productlist.length;
            for (var m = 0; m < productLength; m++) {
                if (model.data.productlist[m].product_id == parame.product_id) {
                    model.data.productlist[m].amount2 = parseInt(model.data.productlist[m].amount2) + parseInt(parame.amount2);
                    parame = undefined;
                    break;
                }
            }
            if (parame != undefined) {
                model.addRow(parame);
            }
        }
        $('#barCodeCheck').val("");
        $('#keynum').val("")
        $('#productNameCheck').val("")
        $("#productNameCheck").focus();
    },
    initBsSuggest:function(dopt_id){

        $("#productNameCheck").off();
        bsSuggest = $("#productNameCheck").bsSuggest({
            url: rootPath + "/scm/stock/dataGrid.json?&status=1&_sortField='name'&_sort='asc'&depot_id="+dopt_id+"&keyword=",
            effectiveFields: ["billsn", "product_name", 'purchase_price'],
            effectiveFieldsAlias: {billsn: "一维码", product_name: "产品名称", 'purchase_price': '销售价'},
            showHeader: true,
            showBtn: false,
            //idField: "billsn",
            //keyField: "product_name",
            autoMinWidth:true,
            indexKey: 1,
            jsonp: false,
            listAlign: 'left',
            getDataMethod: 'url',
            processData: function (json) {// url 获取数据时，对数据的处理，作为 getData 的回调函数
                var i, len, data = {value: []};
                if (!json || !json.data.list || json.data.list.length == 0) {
                    return false;
                }
                productListBySuggest = json.data.list;
                len = json.data.list.length;
                for (i = 0; i < len; i++) {
                    data.value.push({
                        "billsn": json.data.list[i].billsn,
                        "product_name": json.data.list[i].product_name
                    });
                }
                data.defaults = 'test';
                console.log(data);
                return data;
            }
        }).on('onSetSelectValue', function (e, keyword) {
            console.log('onSetSelectValue: ', keyword);
            $('#barCodeCheck').val(keyword.id);
        })
        $("#productNameCheck").keyup(function (event) {
            if (event.keyCode == 13) {
                $('#keynum').focus();
                var barCode = $('#barCodeCheck').val();
                var len = productListBySuggest.length;
                var parame;
                for (var i = 0; i < len; i++) {
                    if (productListBySuggest[i].billsn == barCode) {
                        model.comparison = productListBySuggest[i].comparison;
                    }
                }
                model.data.comparison = 1;
            }
        })

        $("#productNameCheck").blur(function (event) {
            var barCode = $('#barCodeCheck').val();
            if (barCode != undefined && barCode != "") {
                var len = productListBySuggest.length;
                for (var i = 0; i < len; i++) {
                    if (productListBySuggest[i].billsn == barCode) {
                        model.comparison = productListBySuggest[i].comparison;
                    }
                }
                model.data.comparison = 1;
            }
        })




    }
};
function postData() {
    var e = "add" == oper ? "新增盘点" : "修改盘点";
    model.data.productlistlength = model.data.productlist.length;
    model.audit_status = 0;
    Public.ajaxPost(rootPath + "/scm/stockCheck/save.json", model.data.$model
        , function (t) {
            if (200 == t.status) {
                parent.parent.Public.tips({
                    content: e + "成功！"
                });
                model.data.id = t.data.id;
                model.data.billsn = t.data.sn;
                parent.model.reloadData(null);
            } else
                parent.parent.Public.tips({
                    type: 1,
                    content: e + "失败！" + t.msg
                });
        });
}

function changeModel(modelBar) {
    barCodeModel = modelBar.checked
    if (modelBar.checked) {
        bsSuggest.bsSuggest("destroy");
        $("#productNameCheck").keyup(function (event) {
            if (event.keyCode == 13) {
                var url = rootPath + "/scm/stock/dataGrid.json?&status=1&_sortField='name'&_sort='asc'&depot_id="+model.data.depot_id+"&keyword=" + $("#productNameCheck").val();
                Public.ajaxPost(url, {}, function (json) {
                    if (!json || !json.data.list || json.data.list.length == 0) {
                        parent.Public.tips({type: 1, content: '未找到对应的产品，请核对产品信息'});
                        return
                    }
                    productListBySuggest = json.data.list;
                    //var barCode = $('#barCodeCheck').val();
                    $('#barCodeCheck').val(productListBySuggest[0].billsn);
                    $('#productNameCheck').val(productListBySuggest[0].product_name);
                    model.comparison = productListBySuggest[0].comparison;
                    $('#keynum').focus();
                })
            }
        })
    } else {
        $("#productNameCheck").off();
        //THISPAGE.initBarCode()
        model.changBsSuggest();
    }
}


THISPAGE.init();