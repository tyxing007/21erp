var api = frameElement.api, oper = api.data.oper, id = api.data.id, custParame = SYSTEM.custParame, typeList = custParame.typeList,
    $_form = $("#base_form"), addNew = false, type = api.data.type, billType = parent.billType,isPass = false,bsSuggest;
var model = avalon.define({
    billType: billType, fillOrder: false,
    $id: 'ctrl', parameList: typeList, type: type,
    data: {
        id: "",
        is_deleted: 0,
        submit_status: 0,
        remark: "",
        order_id: '',
        ordersn: '',
        type: "",
        bill_date: SYSTEM.date,
        billsn: "自动生成",
        head_id: SYSTEM.user.id,
        head_name: SYSTEM.user.realname,
        remark: "",
        depot_id: '',
        audit_status:'',
        submitType:"",
        productIndex : [],
        productlistlength: 1,
        productlist: [{
            amount: 0,
            remark: '',
            product_id: "",
            unit: "",
            realAmount: 0,
            product_name: "",
            billsn: "",
            differenceAmount: '',
            price : '',
            realPrice:''
        }]
    },
    comparison:[],
    depotList: [], userList: [], totalAmount: 0, totalAmountReal: 0, totalAmountDifference: 0,totalAmountPrice:0,
    changePrice:function(el){
        if(el.amount == undefined || el.amount == "" || el.amount == 0){
            //THISPAGE.addProduct(el.billsn,el.realAmount,id,el.price);
            $.dialog.confirm("请确认是否删除该产品？", function () {
                var len = model.data.productlist.length;
                for(var i=0;i<len;i++){
                    if(el.billsn == model.data.productlist[i].billsn){
                        model.data.productlist.splice(i,1);
                        return;
                    }
                }
            })

        }
        //alert(el)
    },
    qryHead: function (v) {
        Public.ajaxPost(rootPath + "/sso/user/dataGrid.json", {
            keyword: v,
            status: 1,
            _sortField: "realname",
            rows: 9999,
            _sort: "asc"
        }, function (json) {
            model.userList = json.data.list;
        });
    },
    amountChange:function(value){
        //alert(111);
        var len = model.data.productlist.length;
        for(var i=0;i<len;i++){
            if(model.data.productlist[i].billsn == value.billsn){
                var add =  new Number(value.realAmount) - new Number(model.data.productlist[i].realAmount);
                THISPAGE.changeProductList(value.billsn,add+"");
                return;
            }
        }
    },
    priceChange:function(value){
        //alert(111);
        var len = model.data.productlist.length;
        for(var i=0;i<len;i++){
            if(model.data.productlist[i].billsn == value.billsn){
                model.data.productlist[i].realPrice = value.price * value.realAmount;
                return;
            }
        }
        jisuanByChange();
    },
    //添加商品
    productlist: [], productComboDiv: -1
});
model.data.$watch("type", function (a, b) {
    if (a == 0 || a == 1 || a == 4 || a == 5) {// 0：'采购入库' 1：'销售退货入库',4：'销售出库',5：'采购退货出库',
        model.fillOrder = true;
    } else {
        model.data.order_id = "";
        model.data.order_name = "";
        model.fillOrder = false;
    }
});
var THISPAGE = {
    init: function () {
        model.qryHead();
        this.initDom();
        this.initBtn();
        this.initBsSuggest();
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
            Public.ajaxPost(rootPath + "/scm/storageBill/qryOp.json", {id: id}, function (json) {
                if (json.status == 200) {
                    avalon.mix(model.data, json.data);
                    if (model.data.productlistlength == undefined || model.data.productlistlength == 0) {
                        model.data.productlistlength = 1;
                        model.data.productlist = [{
                            amount: 0,
                            remark: '',
                            product_id: "",
                            unit: "",
                            realAmount: 0,
                            product_name: "",
                            billsn: "",
                            differenceAmount: '',
                            price:'',
                            realPrice:''
                        }];
                    }
                    jisuan();
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
        if(type<2){
            var e = ["<i class='fa fa-save mrb'></i>确定", "取消"];
            api.button({
                id: "confirm",
                name: e[0],
                focus: !0,
                callback: function () {
                    addNew = false;
                    $_form.trigger("validate");
                    return false
                }
            },{
                id: "cancel",
                name: e[1]
            })
        }else{
            var e = ["<i class='fa fa-save mrb'></i>确定", "取消","<i class='fa fa-back mrb'></i>货品不足"];
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
                name: e[2],
                callback: function () {
                    addNew = false;
                    THISPAGE.backOrder();
                    return false
                }
            },{
                id: "cancel",
                name: e[1]
            })
        }
    },
    initEvent: function () {

        $('#allCheck').click(function(){
            var len  = model.data.productlist.length;
            var billsn = new Array();
            var amount = new Array();
            for(var i=0;i<len;i++){
                billsn[i] =  model.data.productlist[i].billsn;
                amount[i] =  model.data.productlist[i].amount;
            }
            for(var i=0;i<len;i++){
                THISPAGE.changeProductList(billsn[i],amount[i]);
            }
        })
        this.initValidator();
    },
    addProduct:function(code,num,id,price){
        var r = {barcode:code,num:num,id:id,price:price};
        $.dialog({
            title: "提交出库入库单",
            content: "url:" + rootPath + "/scm/storageBill" + "/addProduct.html",
            data: r,
            width:400,height :300,
            max: true,
            resize: true,
            min: false,
            cache: false,
            lock: true,
            callBackFunction:function(backData){
                var len = model.data.productlist.length;
                var isNew = true;
                for(var i=0;i<len;i++){
                    if(model.data.productlist[i].billsn == backData.billsn){
                        model.data.productlist[i] = backData;
                        isNew = false;
                        break;
                    }
                }
                if(isNew){
                    model.data.productlist.push(backData)
                    //model.data.productlist.splice(0,0,backData);

                }
                $("#productNameCheck").val("");
                $('#keynum').val("");
                $('#barCodeCheck').val("");
                THISPAGE.lastToFist(backData.billsn);
                //THISPAGE.bubbleSort(model.data.productlist)
                THISPAGE.changeBackground();
                jisuanByChange();
                len = model.data.productIndex.length;
                for(var i=0;i<len;i++){
                    if(model.data.productIndex[i] == code){
                        return;
                    }
                }
                model.data.productIndex.push(code);
                $("#productNameCheck").focus();
            }
        })
    },
    changeProductList: function (code, num) {
        var isOk = true;
        if (code == "" || num == "") {
            alert("您未填写相关信息");
            return;
        }
        if(num.length > 6 ){
            alert("您输入的数量过大，请核对");
            $('#keynum').val("");
            $('#keynum').focus();
            return;
        }
        var haveBarCode = false;
        for (var i = model.data.productlist.length - 1; i >= 0; i--) {
            var productList = model.data.productlist[i];
            var productBarCode = productList.billsn;
            var productTemp;
            if (code == productBarCode) {
                var productAmount = productList.amount;
                model.data.productlist[i].realAmount = new Number(model.data.productlist[i].realAmount) + new Number(num)
                model.data.productlist[i].differenceAmount = new Number(model.data.productlist[i].realAmount) - new Number(productAmount);
                model.data.productlist[i].realPrice = new Number(model.data.productlist[i].realAmount * model.data.productlist[i].price)
                haveBarCode = true;
            }
        }
        if(!haveBarCode){
            if(type == 4){
                THISPAGE.addProduct(code,num,id);
            }else{
                alert("您输入的产品不存在，请核对");
                $("#productNameCheck").val("");
                $('#keynum').val("");
                $('#barCodeCheck').val("");
            }
            return;
        }

        $("#productNameCheck").val("");
        $('#keynum').val("");
        $('#barCodeCheck').val("");

        THISPAGE.lastToFist(code);
        THISPAGE.changeBackground();
        jisuanByChange();
        var len = model.data.productIndex.length;
        for(var i=0;i<len;i++){
            if(model.data.productIndex[i] == code){
                return;
            }
        }
        model.data.productIndex.push(code);

    },
    bubbleSort: function (arr) {
        var i = arr.length, j;
        var tempExchangVal;
        while (i > 0) {
            for (j = 0; j < i - 1; j++) {
                if (arr[j].differenceAmount < arr[j + 1].differenceAmount) {
                    tempExchangVal = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = tempExchangVal;
                }
            }
            i--;
        }
        model.data.productlist = arr;
        //return arr;
    },
    //最后输入置顶
    lastToFist: function (arr) {
        var i = model.data.productlist.length;
        var productTemp;
        for(var j=0;j<i;j++){
            if(model.data.productlist[j].billsn == arr){
                productTemp = model.data.productlist[j];
                model.data.productlist.splice(j,1);
                model.data.productlist.splice(0,0,productTemp);
            }
        }
        //var tempExchangVal;
        //while (i > 0) {
        //    for (j = 0; j < i - 1; j++) {
        //        if (arr[j].differenceAmount < arr[j + 1].differenceAmount) {
        //            tempExchangVal = arr[j];
        //            arr[j] = arr[j + 1];
        //            arr[j + 1] = tempExchangVal;
        //        }
        //    }
        //    i--;
        //}
        //model.data.productlist = arr;


        //return arr;
    },
    initBsSuggestProduct:function(){
        var barCode = $("#productNameCheck").bsSuggest({
            url: rootPath + "/scm/product/dataGrid.json?checkType=bssuggest&keyword=",
            effectiveFields: ["billsn", "product_name", 'purchase_price'],
            effectiveFieldsAlias: {billsn: "一维码", product_name: "产品名称", 'purchase_price': '销售价'},
            showHeader: true,
            showBtn: false,
            //idField: "billsn",
            //keyField: "product_name",
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
            //console.log('onSetSelectValue: ', keyword);
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
        bsSuggest = barCode;
    },
    initBsSuggest:function(){
        THISPAGE.initBsSuggestProduct();
        $('#keynum').keyup(function (event) {
            if (event.keyCode == 13) {
                $('#productNameCheck').focus();
                THISPAGE.changeProductList($('#barCodeCheck').val(), $('#keynum').val());
            }
        })
        $('#okIndex').click(function (event) {
            $('#productNameCheck').focus();
            THISPAGE.changeProductList($('#barCodeCheck').val(), $('#keynum').val());
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
                data:{type:type},
                callBackFunction: function (checkProduct) {
                    //alert(checkProduct.length);

                    var len = productListBySuggest == undefined ? 0 : productListBySuggest.length;
                    var checkLen = checkProduct.length;
                    for (var i = 0; i < checkLen; i++) {
                        for (var m = 0; m < len; m++) {
                            if (checkProduct[i].id == productListBySuggest[m].id) {
                                THISPAGE.changeProductList(productListBySuggest[m].billsn, checkProduct[i].num);
                                checkProduct.splice(i, 1);
                                i--;
                            }
                        }
                        checkLen = checkProduct.length;
                    }
                    checkLen = checkProduct.length;
                    var modelValue = JSON.stringify(checkProduct)
                    Public.ajaxPost(rootPath + "/scm/product/qryProductInfo.json", {productId: modelValue}
                        , function (t) {
                            if (200 == t.status) {
                                productListBySuggest = t.data;
                                var len = productListBySuggest.length;
                                for (var i = 0; i < len; i++) {
                                    THISPAGE.changeProductList(productListBySuggest[i].billsn, productListBySuggest[i].amount)
                                }
                                productListBySuggest = [];
                            } else
                                parent.parent.Public.tips({
                                    type: 1,
                                    content: "获取数据失败！" + t.msg
                                });
                        });
                }
            })
        });
    },
    changeBackground: function () {
        var td;
        $(".productTr").each(function () {
                 td = $(this);
                //var value = td.children().eq(5).text();
                //var realValue = td.children().eq(4).text();
                var index = td.children().eq(0).text();
                var amount = model.data.productlist[index-1].amount;
                var realValue = model.data.productlist[index-1].realAmount;
                var value = model.data.productlist[index-1].differenceAmount;
                if(amount != 0 && realValue != 0){
                    if (value < 0) {
                        td.removeClass()
                        td.addClass("over");
                    } else if (value == 0) {
                        td.removeClass()
                        td.addClass("equal");
                    } else if (value > 0 ) {
                        td.removeClass()
                        td.addClass("LessThan");
                    }
                }else if(amount == 0 && realValue >0){
                    td.removeClass()
                    td.addClass("add");
                } else{
                    td.removeClass()
                    td.addClass("default");
                }
            }
        )

    },

    initValidator: function () {
        $_form.validator({
            valid: function () {
                checkData();
            },
            ignore: ":hidden",
            theme: "yellow_top",
            timely: 1,
            stopOnError: true
        });
    },
    backOrder:function(){
        postData(false);
    },
    onOrder:function(){
        postData(true);
    },
    ajaxPost: function (url, params, callback, errCallback) {

    }
};
function jisuan() {
    model.totalAmount = 0;
    model.totalAmountReal = 0;
    model.totalAmountDifference = 0;
    model.totalAmountPrice = 0;
    for (var i = 0; i < model.data.productlist.length; i++) {
        model.data.productlist[i].differenceAmount = model.data.productlist[i].amount;
        model.totalAmount += new Number(model.data.productlist[i].amount);
        model.totalAmountDifference += new Number(model.data.productlist[i].differenceAmount);
        model.totalAmountReal += new Number(model.data.productlist[i].realAmount);
        model.totalAmountPrice += new Number(model.data.productlist[i].realPrice);
    }
}

function jisuanByChange() {
    model.totalAmount = 0;
    model.totalAmountReal = 0;
    model.totalAmountDifference = 0;
    model.totalAmountPrice = 0;
    for (var i = 0; i < model.data.productlist.length; i++) {
        model.totalAmount += new Number(model.data.productlist[i].amount);
        model.totalAmountDifference += new Number(model.data.productlist[i].differenceAmount);
        model.totalAmountReal += new Number(model.data.productlist[i].realAmount);
        model.totalAmountPrice += new Number(model.data.productlist[i].realPrice);
    }
}

function checkData(){
    var isPass = false;
    if(model.data.productIndex.length == 0 ){
        parent.parent.Public.tips({
            type:1,
            content: "您的出库单未做校验，请校验"
        });
        return;
    }

    for (var i = model.data.productlistlength - 1; i >= 0; i--) {
        if(model.data.productlist[i].realAmount<0){
            //return;
            parent.parent.Public.tips({
                type:1,
                content: "您的出库单实际数量存在负数，请核对"
            });
            return;
        }
    }
    for (var i = model.data.productlistlength - 1; i >= 0; i--) {
        if(model.data.productlist[i].differenceAmount!=0){
            isPass = true;
                $.dialog.confirm("您的出库单与订单存在差异，确定要提交吗？提交将会更改订单数据", function(){
                //postData(true)
                //alert(111)
                THISPAGE.onOrder()
            });
            //return;
        }
    }
    if(!isPass){
        postData(true);
    }

}

function postData(isSubmit) {
    model.data.productlistlength = model.data.productlist.length;
    //model.data.audit_status = 4;
    if(isSubmit){
        model.data.submitType = 1;
    }else{
        model.data.submitType = 0;
    }
    var modelValue = JSON.stringify(model.data.$model)

    $.ajax({
        type: "POST",
        url: rootPath + "/scm/storageBill/submit.json",
        data: JSON.parse(modelValue),
        dataType: "json",
        success: function (data, status) {
            //callback(data);
            var t = data;
            if (200 == t.status) {
                parent.parent.Public.tips({
                    content: "审核" + "成功！"
                });
                model.data.id = t.data.id;
                model.data.billsn = t.data.sn;
                parent.model.reloadData(null);
                api.close();
            } else
                parent.parent.Public.tips({
                    type: 1,
                    content: "审核" + "失败！" + t.msg
                });

        },
        error: function (err) {
            parent.Public.tips({type: 1, content: '操作失败了哦，请检查您的网络链接！'});
            errCallback && errCallback(err);
        }
    });




}

function changeModel(modelBar) {
    barCodeModel = modelBar.checked
    if (modelBar.checked) {
        bsSuggest.bsSuggest("destroy");
        $("#productNameCheck").keyup(function (event) {
            if (event.keyCode == 13) {
                var url = rootPath + "/scm/product/dataGrid.json?checkType=bssuggest&keyword=" + $("#productNameCheck").val();
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
        THISPAGE.initBsSuggestProduct();
    }
}
THISPAGE.init();