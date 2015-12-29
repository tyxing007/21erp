var api = frameElement.api, barCode = api.data.barcode, num = api.data.num, id = api.data.id,price = api.data.price,callback = api.config.callBackFunction;

var THISPAGE = {
    init: function () {
        THISPAGE.initDom();
        THISPAGE.initBtn();
        THISPAGE.initEvent();
    },
    initDom: function () {
        if (barCode != undefined) {
            //var url = rootPath + "/scm/product/dataGrid.json?keyword=" + barCode +"&customer_id="+customerId;
            var url = rootPath + "/scm/product/qryProductByStorage"
            Public.ajaxPost(url, {barCode: barCode, id: id}, function (json) {

                if (!json || !json.data || json.data.length == 0) {
                    parent.Public.tips({type: 1, content: '未找到对应的产品，请核对产品信息'});
                    return
                }
                var product = json.data[0];
                for (var key in product) {
                    $('#' + key).val(product[key]);
                }
                $('#barCode').val(barCode);
                $('#num').val(num);
                $('#price').val(price == undefined ? "":price);
                $('#price').focus();
                ////var barCode = $('#barCodeCheck').val();
                //$('#barCodeCheck').val(productListBySuggest[0].billsn);
                //$('#productNameCheck').val(productListBySuggest[0].product_name);
                //model.comparison = productListBySuggest[0].comparison;
                //$('#keynum').focus();
            }, function (json) {
                parent.Public.tips({type: 1, content: json.data});
                return
            })
        }

    },
    initBtn: function () {
        var e = ["<i class='fa fa-save mrb'></i>确定", "关闭"];
        api.button({
            id: "confirm",
            name: e[0],
            focus: !0,
            callback: function () {
                //window.location = rootPath + "/logout";
                var backVar = {
                    amount: '',
                    remark: '',
                    product_id: '',
                    unit: '',
                    realAmount: '',
                    product_name: '',
                    billsn: '',
                    differenceAmount: '',
                    price: '',
                    realPrice:''
                };
                backVar.amount = 0;
                backVar.remark = "新增产品价格为："+$('#price').val();
                backVar.product_id = $('#productId').val();
                backVar.unit = $('#unit').val();
                backVar.realAmount = $('#num').val();
                backVar.product_name = $('#productName').val();
                backVar.billsn = $('#barCode').val();
                backVar.differenceAmount = $('#num').val();
                backVar.price = $('#price').val();
                backVar.realPrice = new Number($('#price').val()) * $('#num').val();
                callback(backVar);
            }
        }, {
            id: "cancel",
            name: e[1]
        })
    },
    initEvent: function () {
        $('#price').keyup(function (event) {
            if (event.keyCode == 13) {
                var backVar = {
                    amount: '',
                    remark: '',
                    product_id: '',
                    unit: '',
                    realAmount: '',
                    product_name: '',
                    billsn: '',
                    differenceAmount: '',
                    price: '',
                    realPrice:''
                };
                backVar.amount = 0;
                backVar.remark = "";
                backVar.product_id = $('#productId').val();
                backVar.unit = $('#unit').val();
                backVar.realAmount = $('#num').val();
                backVar.product_name = $('#productName').val();
                backVar.billsn = $('#barCode').val();
                backVar.differenceAmount = $('#num').val();
                backVar.price = $('#price').val();
                backVar.realPrice = new Number($('#price').val()) * $('#num').val();
                callback(backVar);
                api.close();
            }
        })
        $('#num').keyup(function (event) {
            if (event.keyCode == 13) {
                var backVar = {
                    amount: '',
                    remark: '',
                    product_id: '',
                    unit: '',
                    realAmount: '',
                    product_name: '',
                    billsn: '',
                    differenceAmount: '',
                    price: ''
                };
                backVar.amount = 0;
                backVar.remark = "";
                backVar.product_id = $('#productId').val();
                backVar.unit = $('#unit').val();
                backVar.realAmount = $('#num').val();
                backVar.product_name = $('#productName').val();
                backVar.billsn = $('#barCode').val();
                backVar.differenceAmount = $('#num').val();
                backVar.price = $('#price').val();
                callback(backVar);
                api.close();
            }
        })
    }
}
THISPAGE.init();