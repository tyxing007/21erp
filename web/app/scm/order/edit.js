var api = frameElement.api, oper = api.data.oper, id = api.data.id, custParame = SYSTEM.custParame, typeList = custParame.typeList,
    $_form = $("#base_form"), addNew = false, type = parent.type, editor, productListBySuggest, area = SYSTEM.area, productBsSuggest;
barCodeModel = false;
var model = avalon.define({
    $id: 'ctrl', parameList: typeList, type: type,
    data: {
        id: "",
        name: '',
        is_deleted: 0,
        status: "",
        ordertype: type,
        remark: "",
        start_date: "",
        end_date: "",
        sign_date: SYSTEM.date,
        billsn: "自动生成",
        delivery_date: SYSTEM.date,
        head_id: SYSTEM.user.id,
        head_name: SYSTEM.user.realname,
        pact: "",
        remark: "",
        audit_status: 0,
        ordersn: "",
        contacts_name: "",
        contacts_id: "",
        customer_id: "",
        customer_name: "",
        productlistlength: 1,
        rebate: 0,
        rebate_amt: 0,
        order_amt: 0,
        people: "",
        phone: "",
        address: "",
        comparison: "",
        productlist: []
    },
    comparison: [],
    sumzhamt: 0, sumAmount: 0, sumAmt: 0, sumTax: 0, sumPTax: 0,
    userList: [],
    changeUnit: function (v) {
        //alert(v);
        model.data.comparison = v;
    },
    qryHead: function (v) {
        Public.ajaxPost(rootPath + "/sso/user/dataGrid.json", {
            keyword: v,
            status: 1,
            _sortField: "realname",
            rows: 9999,
            _sort: "asc",
            rows: 9999
        }, function (json) {
            model.userList = json.data.list;
        });
    },
    customerList: [], custComboV: false,
    chooseCust: function (e) {
        model.data.customer_id = e.id;
        model.data.customer_name = e.key;
        model.qryCustomer(e.key);
    },
    qryCustomer: function (v) {//自动完成查询客户


        model.custComboV = true;
        Public.ajaxPost(rootPath + "/crm/customer/dataGrid.json", {
            keyword: v,
            _sortField: "realname",
            _sort: "asc",
            type: type > 1 ? -1 : 0
        }, function (json) {
            var e;
            model.customerList = json.data.list;
            if (model.customerList.length == 0) {
                return
            } else {
                e = json.data.list[0]
            }
            var address = "";
            var len = 0;
            var index = 0;
            if (e.province != "") {
                len = area.provinceList.length;
                for (var i = 0; i < len; i++) {
                    if (area.provinceList[i].id == e.province) {
                        address += area.provinceList[i].name
                        index = e.province;
                    }
                }
            }
            if (e.province != "" && e.city != "") {
                len = area[index].length;
                for (var m = 0; m < len; m++) {
                    if (area[index][m].id == e.city) {
                        address += area[index][m].name
                    }
                }
            }
            address += e.address;
            model.data.address = address;
            model.data.people = e.contacts_name
            model.data.phone = e.conphone == "" ? e.conmobile == "" ? "" : e.conmobile : e.conphone;
            model.custComboV = false;
        });


    },
    //添加商品
    productlist: [], productComboDiv: -1,
    addRow: function (parame) {//增加商品行;
        model.data.productlist.splice(0, 0, parame);
        //model.data.productlist.push(parame);
    },
    delRow: function (e) {//删除商品行
        e.preventDefault()
        var item = this.$vmodel.$remove();
        model.data.productlist.remove(item);
        jisuan();
    }
});
avalon.filters.subStr=function(str){
    if(str.length >14){
        return str.substring(0,7) +"..."+str.substring(str.length-7,str.length);
    }
    return str;
}
function hideThis(v) {
    //$(v).hide()
    $("#comboDiv").hide();
}


//avalon.filters.product = function(str, args, args2){//str为管道符之前计算得到的结果，默认框架会帮你传入，此方法必须返回一个值
//    /* 具体逻辑 */
//    return ret;
//}

model.data.productlist.$watch("length", function (name, a, b) {
    jisuan();
});
function jisuan(v) {//计算合计 注意：对于数组不能使用$watch 因为只能监听length。
    model.sumzhamt = 0;
    model.sumAmount = 0;
    model.sumAmt = 0;
    model.sumTax = 0;
    model.sumPTax = 0;
    for (var i = 0; i < model.data.productlist.length; i++) {
        var el = model.data.productlist[i];
        model.sumzhamt = new Number(model.sumzhamt) + new Number(el.zhamt);
        model.sumAmount = new Number(model.sumAmount) + new Number(el.amount);
        model.sumAmt = new Number(model.sumAmt) + new Number(el.amt);
        model.sumTax = new Number(model.sumTax) + new Number(el.tax);
    }
    model.sumPTax = model.sumAmt + model.sumTax;
    if (v == 0)
        model.data.rebate_amt = (model.sumPTax * model.data.rebate / 100).toFixed(2);
    else {
        if (model.sumPTax > 0)
            model.data.rebate = (model.data.rebate_amt / model.sumPTax * 100).toFixed(0);
    }
    model.data.order_amt = model.sumPTax - model.data.rebate_amt;
};
var THISPAGE = {
    init: function () {
        model.qryHead();
        this.initDom();
        this.initBtn();
//		setTimeout(jisuan,300);
    },
    initDom: function () {
        $(".ui-datepicker-input").datepicker();
        THISPAGE.initEvent();
        if (id != undefined && id != '' && id != 'undefined') {
            Public.ajaxPost(rootPath + "/scm/order/qryOp.json", {id: id}, function (json) {
                if (json.status == 200) {
//					avalon.mix(model.data,json.data);
                    model.data = json.data;
                    model.data.productlist = json.data.productlist;
                    model.data.productlistlength = json.data.productlistlength;
                    jisuan()
//					setTimeout(function(){editor.setContent(json.data.pact);},500);
                } else {
                    parent.Public.tips({type: 1, content: json.msg});
                }
            });
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
        //editor = UE.getEditor("editor", {});
        this.initValidator();
        //editor.ready(function () {
        //    jisuan();
        //    editor.setContent(model.data.pact);
        //});
        var customer = $("#customer").bsSuggest({
            url: rootPath + "/crm/customer/dataGrid.json?&qryType=11&type=" + (type > 1 ? -1 : 0) + "&keyword=",
            effectiveFields: ["name", 'id'],
            effectiveFieldsAlias: {name: "客户名称", 'id': '客户ID'},
            showHeader: true,
            showBtn: false,
            //idField: "billsn",
            //keyField: "product_name",
            indexKey: 0,
            indexId: 1,
            jsonp: false,
            listAlign: 'left',
            getDataMethod: 'url',
            processData: function (json) {// url 获取数据时，对数据的处理，作为 getData 的回调函数
                var i, len, data = {value: []};
                if (!json || !json.data.list || json.data.list.length == 0) {
                    return false;
                }
                model.customerList = json.data.list;
                len = json.data.list.length;
                for (i = 0; i < len; i++) {
                    data.value.push({
                        "name": json.data.list[i].name,
                        "id": json.data.list[i].id
                    });
                }
                data.defaults = 'test';
                console.log(data);
                return data;
            }
        }).on('onSetSelectValue', function (e, keyword) {
            //console.log('onSetSelectValue: ', keyword);
            model.data.customer_id = keyword.id;
            $('#snCheck').val(keyword.id);
            model.chooseCust(keyword);
        });
        $("#customer").keyup(function (event) {
            if (event.keyCode == 13) {

                var sn = $('#snCheck').val();
                //var len = model.customerList.length;
                var customer = model.customerList.$model;
                var len = customer.length;
                for (var i = 0; i < len; i++) {
                    if (customer[i].sn == sn) {
                        var e = customer[i];
                        model.data.customer_id = e.id;
                        model.data.customer_name = e.name;
                        var address = "";
                        var len = 0;
                        var index = 0;
                        if (e.province != "") {
                            len = area.provinceList.length;
                            for (var i = 0; i < len; i++) {
                                if (area.provinceList[i].id == e.province) {
                                    address += area.provinceList[i].name
                                    index = e.province;
                                }
                            }
                        }
                        if (e.province != "" && e.city != "") {
                            len = area[index].length;
                            for (var m = 0; m < len; m++) {
                                if (area[index][m].id == e.city) {
                                    address += area[index][m].name
                                }
                            }
                        }
                        address += e.address;
                        model.data.address = address;
                        model.custComboV = false;
                        qryContacts(e.id);
                    }
                }

                THISPAGE.initBarCode();

            }
        })

        $("#customer").blur(function (event) {
            var sn = $('#snCheck').val();
            //var len = model.customerList.length;
            var customer = model.customerList.$model;
            var len = customer.length;
            for (var i = 0; i < len; i++) {
                if (customer[i].sn == sn) {
                    var e = customer[i];
                    model.data.customer_id = e.id;
                    model.data.customer_name = e.name;
                    var address = "";
                    var len = 0;
                    var index = 0;
                    if (e.province != "") {
                        len = area.provinceList.length;
                        for (var i = 0; i < len; i++) {
                            if (area.provinceList[i].id == e.province) {
                                address += area.provinceList[i].name
                                index = e.province;
                            }
                        }
                    }
                    if (e.province != "" && e.city != "") {
                        len = area[index].length;
                        for (var m = 0; m < len; m++) {
                            if (area[index][m].id == e.city) {
                                address += area[index][m].name
                            }
                        }
                    }
                    address += e.address;
                    model.data.address = address;
                    model.custComboV = false;
                    qryContacts(e.id);
                }
            }
            THISPAGE.initBarCode();
        })


        var barCode = $("#productNameCheck").bsSuggest({
            url: rootPath + "/scm/product/dataGrid.json?checkType=bssuggest&keyword=",
            effectiveFields: ["billsn", "product_name"],
            effectiveFieldsAlias: {billsn: "一维码", product_name: "产品名称"},
            showHeader: true,
            showBtn: false,
            //idField: "billsn",
            //keyField: "product_name",
            indexKey: 1,
            jsonp: false,
            listAlign: 'left',
            getDataMethod: 'url',
            //multiWord: true,
            //separator: "/",
            processData: function (json) {// url 获取数据时，对数据的处理，作为 getData 的回调函数
                var i, len, data = {value: []};
                if (!json || !json.data.list || json.data.list.length == 0) {
                    return false;
                }
                productListBySuggest = json.data.list;
                len = json.data.list.length;

                //if (len == 1 && barCodeModel) {
                //
                //    var barCodeParent = barCode.parents(".input-group:eq(0)").find("ul.dropdown-menu");
                //    barCodeParent.hide().empty();
                //    $('#barCodeCheck').val(productListBySuggest[0].billsn);
                //    $('#productNameCheck').val(productListBySuggest[0].product_name);
                //
                //    //$("#productNameCheck").val($("#productNameCheck").val());//让鼠标输入跳到最后
                //    barCode.css("background", "rgba(255,255,255,0.1)");
                //    //barCodeParent.css("display", "");
                //    //$('#keynum').focus();
                //    ////barCode.setValue()
                //    //return fa;
                //    //$("#productNameCheck").bsSuggest("hide");
                //    return false
                //}

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
            //console.log('onSetSelectValue: ', keyword);
            $('#barCodeCheck').val(keyword.id);
        })
        productBsSuggest = barCode;
        $("#productNameCheck").keyup(function (event) {
            if (event.keyCode == 13) {
                //$("#productNameCheck").bsSuggest("disable");
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

        $('#keynum').keyup(function (event) {
            if (event.keyCode == 13) {
                THISPAGE.addRow($('#barCodeCheck').val(), $('#keynum').val());
            }
        })
        $('#keynum').focus(function (event) {
            if (barCodeModel === true) {
                productBsSuggest.bsSuggest("enable");
            }
        })
        $('#okIndex').click(function (event) {
            THISPAGE.addRow($('#barCodeCheck').val(), $('#keynum').val());
        })
        $('#search').click(function (event) {
            parent.$.dialog({
                title: '选择产品',
                content: "url:" + rootPath + "/scm/order/checkProduct.html",
                //data: opt,
                width: $(window.parent).width() / 3 * 2.7,
                height: $(window.parent).height() / 3 * 2.5,
                max: false,
                min: false,
                cache: false,
                lock: true,
                data: {
                    type: type,
                    customer_id: $('#snCheck').val()
                },
                callBackFunction: function (checkProduct) {
                    //alert(checkProduct.length);

                    var checkLen = checkProduct.length;
                    for (var i = 0; i < checkLen; i++) {
                        THISPAGE.addRowHaveInfo(checkProduct[i]);
                    }
                }
            })
        })

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
        if(num.toString().length > 6 ){
            alert("您输入的数量过大，请核对");
            $('#keynum').val("");
            $('#keynum').focus();
            return;
        }


        var len = productListBySuggest.length;
        var parame;
        for (var i = 0; i < len; i++) {
            if (productListBySuggest[i].billsn == barCode) {
                var amt = (type <= 1 ? productListBySuggest[i].purchase_price : type == 4 ? productListBySuggest[i].quoted_price : productListBySuggest[i].sale_price) * num * double;
                parame = {
                    billsn: barCode,
                    product_id: productListBySuggest[i].product_id,
                    description: "",
                    quoted_price: type == 4 ? productListBySuggest[i].quoted_price : 0,
                    unit: productListBySuggest[i].unit,
                    purchase_price: productListBySuggest[i].purchase_price,
                    zhamt: 0,
                    zkl: 0,
                    tax_rate: 0,
                    tax: 0,
                    stock:productListBySuggest[i].stock,
                    amt: fixedNum(amt),
                    sale_price: productListBySuggest[i].product_type == 1 ? "0" : productListBySuggest[i].sale_price,
                    amount: num * double,
                    his_price: productListBySuggest[i].his_price,
                    his_purchase_price: productListBySuggest[i].his_purchase_price,
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
                    model.data.productlist[m].amount = parseInt(model.data.productlist[m].amount) + parseInt(parame.amount);
                    parame = undefined;
                    break;
                }
            }
            if (parame != undefined) {
                model.addRow(parame);
            }
        }
        //comparison
        model.comparison = [];
        $('#barCodeCheck').val("");
        $('#keynum').val("")
        $('#productNameCheck').val("")
        $("#productNameCheck").focus();
    },

    addRowHaveInfo: function (parame) {
        var productLength = model.data.productlist.length;
        for (var m = 0; m < productLength; m++) {
            if (model.data.productlist[m].product_id == parame.product_id) {
                model.data.productlist[m].amount = parseInt(model.data.productlist[m].amount) + parseInt(parame.amount);
                parame = undefined;
                break;
            }
        }
        if (parame != undefined) {
            model.addRow(parame);
        }
    },


    initValidator: function () {
        $_form.validator({
//			messages : {
//				required : "请填写{0}"
//			},
//			display : function(e) {
//				return $(e).data("msg");
//			},
            valid: function () {
                postData();
            },
            ignore: ":hidden",
            theme: "yellow_bottom",
            timely: 1,
            stopOnError: true
        });
    },
    initBarCode: function () {
        if (productBsSuggest) {
            productBsSuggest.bsSuggest("destroy");
        }
        $("#productNameCheck").unbind("keyup");
        $("#productNameCheck").unbind("blur");
        var sn = $('#snCheck').val();
        if (sn != "" || sn != undefined) {
            var barCode = $("#productNameCheck").bsSuggest({
                url: rootPath + "/scm/product/dataGrid.json?checkType=bssuggest&customer_id=" + sn + "&keyword=",
                effectiveFields: ["billsn", "product_name"],
                effectiveFieldsAlias: {billsn: "一维码", product_name: "产品名称"},
                showHeader: true,
                showBtn: false,
                autoMinWidth: true,
                //idField: "billsn",
                //keyField: "product_name",
                indexKey: 1,
                jsonp: false,
                listAlign: 'left',
                getDataMethod: 'url',
                //multiWord: true,
                //separator: " ",
                processData: function (json) {// url 获取数据时，对数据的处理，作为 getData 的回调函数
                    var i, len, data = {value: []};
                    if (!json || !json.data.list || json.data.list.length == 0) {
                        return false;
                    }
                    productListBySuggest = json.data.list;
                    len = json.data.list.length;
                    //if (len == 1 && barCodeModel) {
                    //    $("#productNameCheck").bsSuggest("disable");
                    //    var barCodeParent = barCode.parents(".input-group:eq(0)").find("ul.dropdown-menu");
                    //    barCodeParent.hide().empty();
                    //    $('#barCodeCheck').val(productListBySuggest[0].billsn);
                    //    $('#productNameCheck').val(productListBySuggest[0].product_name);
                    //    //$("#productNameCheck").val($("#productNameCheck").val());//让鼠标输入跳到最后
                    //    barCode.css("background", "rgba(255,255,255,0.1)");
                    //    //barCodeParent.css("display", "");
                    //    //$('#keynum').focus();
                    //    ////barCode.setValue()
                    //    //return fa;
                    //    //$("#productNameCheck").bsSuggest("hide");
                    //    return false
                    //}
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
        } else {
            var barCode = $("#productNameCheck").bsSuggest({
                url: rootPath + "/scm/product/dataGrid.json?checkType=bssuggest&keyword=",
                effectiveFields: ["billsn", "product_name"],
                effectiveFieldsAlias: {billsn: "一维码", product_name: "产品名称"},
                showHeader: true,
                showBtn: false,
                //idField: "billsn",
                //keyField: "product_name",
                indexKey: 1,
                jsonp: false,
                listAlign: 'left',
                getDataMethod: 'url',
                //multiWord: true,
                //separator: " ",
                processData: function (json) {// url 获取数据时，对数据的处理，作为 getData 的回调函数
                    var i, len, data = {value: []};
                    if (!json || !json.data.list || json.data.list.length == 0) {
                        return false;
                    }
                    productListBySuggest = json.data.list;
                    len = json.data.list.length;

                    //if (len == 1 && barCodeModel) {
                    //
                    //    var barCodeParent = barCode.parents(".input-group:eq(0)").find("ul.dropdown-menu");
                    //    barCodeParent.hide().empty();
                    //    $('#barCodeCheck').val(productListBySuggest[0].billsn);
                    //    $('#productNameCheck').val(productListBySuggest[0].product_name);
                    //
                    //    //$("#productNameCheck").val($("#productNameCheck").val());//让鼠标输入跳到最后
                    //    barCode.css("background", "rgba(255,255,255,0.1)");
                    //    //barCodeParent.css("display", "");
                    //    //$('#keynum').focus();
                    //    ////barCode.setValue()
                    //    //return fa;
                    //    //$("#productNameCheck").bsSuggest("hide");
                    //    return false
                    //}

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
                //console.log('onSetSelectValue: ', keyword);
                $('#barCodeCheck').val(keyword.id);
            })
        }


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
            }
        })

        productBsSuggest = barCode;
    }
};
function postData() {
    var e = "add" == oper ? "新增订单" : "修改订单";
    model.data.productlistlength = model.data.productlist.length;
    model.audit_status = 0;
    if (model.data.productlistlength == 0) {
        parent.parent.Public.tips({type: 1, content: "商品必须选择"});
        return;
    }
    if (model.data.ordertype == 2 && (model.data.people == "" || model.data.phone == "" || model.data.address == "")) {
        parent.parent.Public.tips({type: 1, content: "送货信息必须填写"});
        return;
    }
    if ($('#snCheck').val() == '' || $('#snCheck').val() == undefined) {
        var content = "请选择正确的";
        content += model.type > 1 ? "客户" : model.type < 2 ? "供应商" : '';
        parent.parent.Public.tips({type: 1, content: content});
        return;
    }

    var len = model.data.productlist.length;
    for (var i = 0; i < len; i++) {
        //zkl  zhamt
        if (!isUnsignedNumeric(model.data.productlist[i].zkl) || !isDoubeNumber(model.data.productlist[i].zhamt)) {
            parent.parent.Public.tips({
                type: 1,
                content: '折扣率为非正整数或非数字，请核对，位置' + i + '行'
            });
            return;
        }
    }

    if (!isUnsignedNumeric(model.data.rebate) || !isDoubeNumber(model.data.rebate_amt)) {
        parent.parent.Public.tips({
            type: 1,
            content: '折扣率为非正整数或非数字'
        });
        return;
    }

    Public.ajaxPost(rootPath + "/scm/order/save.json", model.data.$model
        , function (t) {
            if (200 == t.status) {
                parent.parent.Public.tips({
                    content: e + "成功！"
                });
                model.data.id = t.data.id;
                model.data.billsn = t.data.sn;
                parent.model.reloadData(null);
                api.close();
            } else
                parent.parent.Public.tips({
                    type: 1,
                    content: e + "失败！" + t.msg
                });
        });
}


function qryContacts(id) {

    Public.ajaxPost(rootPath + "/crm/contacts/mainContacts.json", {customer_id: id}
        , function (t) {
            if (200 == t.status) {
                if (t.data == "") {
                    return;
                }
                model.data.people = t.data.name;
                model.data.phone = t.data.mobile;
            }
        });
}

function isUnsignedNumeric(strNumber) {
    if (strNumber == 0 || strNumber == '0') {
        return true;
    }
    var newPar = new RegExp("^[0-9]*[1-9][0-9]*$");
    return newPar.test(strNumber);
}

function isDoubeNumber(strNumber) {
    if (strNumber == 0 || strNumber == '0') {
        return true;
    }
    var newPar = new RegExp("^(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))$");
    return newPar.test(strNumber);
}
function changeModel(modelBar) {
    barCodeModel = modelBar.checked
    if (modelBar.checked) {
        productBsSuggest.bsSuggest("destroy");
        $("#productNameCheck").keyup(function (event) {
            if (event.keyCode == 13) {
                var sn = $('#snCheck').val();
                var url = "";
                if (sn != "" && sn != undefined) {
                    url = rootPath + "/scm/product/dataGrid.json?checkType=bssuggest&customer_id=" + sn + "&keyword=" + $("#productNameCheck").val();
                } else {
                    url = rootPath + "/scm/product/dataGrid.json?checkType=bssuggest&keyword=" + $("#productNameCheck").val();
                }
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
        THISPAGE.initBarCode()
    }
}


THISPAGE.init();