var api = frameElement.api, oper = api.data.oper, id = api.data.rowId,
    $_form = $("#base_form"), addNew = false, index = 0, productListBySuggest,tree=[],productBsSuggest;
var model = avalon.define({
    $id: 'view',
    data: {
        id: "",
        category: "",
        category_name: "",
        name: "",
        billsn: "",
        stock_warn: 0,
        model: "",
        status: 1,
        unit: "",
        unit_name: "",
        sale_price: 0,
        purchase_price: 0,
        remark: "",
        brand: "",
        specification: "",
        product_type: 0
    },
    comparison: [], combination: [],
    tabActive: 0,
    setStatus: function (v) {
        model.data.status = v;
    },
    setType: function (v) {
        model.data.product_type = v;
    },
    init: function () {
        model.data = {
            id: "",
            category: "",
            category_name: "",
            name: "",
            billsn: "",
            stock_warn: 0,
            model: "",
            status: 1,
            unit: "",
            unit_name: "",
            sale_price: 0,
            purchase_price: 0,
            remark: "",
            brand: "",
            specification: "",
            product_type: 0
        },
            comparison = [],
            combination = []
    },
    showTab: function (i, b) {
        model.tabActive = i;
    }
});
model.data.$watch("$all", function (name, a, b) {
    if (a == null) {
        model.data[name] = "";
    }
});
var THISPAGE = {

    init: function () {
        this.initDom();
        this.initBtn();
    },
    initDom: function () {
        if (id != undefined && id != '' && id != 'undefined') {
            Public.ajaxPost(rootPath + "/scm/product/qryOp.json", {id: id}, function (json) {
                if (json.status == 200) {
                    avalon.mix(model.data, json.data);
                    model.data.unit_name = parent.parent.SYSTEM.custParame[json.data.unit].name;
                    model.data.category_name = parent.parent.SYSTEM.custParame[json.data.category].name;
                    THISPAGE.initEvent();

                    Public.ajaxPost(rootPath + "/scm/product/qryComparison.json", {productId: id}, function (json) {
                        if (json.status == 200) {
                            //avalon.mix(model.comparison, json.data);
                            model.comparison = json.data;
                        } else {
                            parent.Public.tips({type: 1, content: json.msg});
                        }
                    });
                    Public.ajaxPost(rootPath + "/scm/product/qryCombination.json", {productId: id}, function (json) {
                        if (json.status == 200) {
                            //avalon.mix(model.comparison, json.data);
                            model.combination = json.data;
                        } else {
                            parent.Public.tips({type: 1, content: json.msg});
                        }
                    });
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
            focus: true,
            callback: function () {
                addNew = false;
                $_form.trigger("validate");
                return false
            }
        }, {
            id: "saveAndNew",
            name: "<i class='fa fa-save mrb'></i>保存并新建",
            focus: false,
            callback: function () {
                $_form.trigger("validate");
                addNew = true;
                return false
            }
        }, {
            id: "cancel",
            name: e[1]
        })
    },
    initEvent: function () {


        THISPAGE.initComboTree();
        /**计量单位combo END*/

            //var unit = $('#unit')
            //var num = $('#num')

        $("#comparisonUnit").keyup(
            function (event) {
                //queryConditions.keyword =t.$_keyword.val();
                //THISPAGE.reloadData(queryConditions)
                if (event.keyCode == 13) {
                    $("#comparisonNum").focus()
                }
            });

        $("#comparisonNum").keyup(
            function (event) {

                if (event.keyCode == 13) {
                    THISPAGE.addComparison($("#comparisonUnit").val(), $("#comparisonNum").val())
                    $("#comparisonUnit").focus();
                }
            });
        $('#okButton').click(
            function () {
                THISPAGE.addComparison($("#comparisonUnit").val(), $("#comparisonNum").val())
                $("#comparisonUnit").focus();
            })

        $(".table-responsive").on("click", ".fa-trash-o", function (t) {
            //= $(this).eq(1)
            //$('#tbFirst').
            var index = $(this).index();
            var id = $('.tbFirst')[index].children[0].innerText
            for (var i = 0; i < model.comparison.length; i++) {
                if (model.comparison[i].id == id) {
                    model.comparison.splice(i, 1);
                }
            }
        });

        $(".table-combination").on("click", ".fa-trash-o", function (t) {
            var index = $(this).index();
            var id = $('.tbCombination')[index].children[0].innerText
            for (var i = 0; i < model.combination.length; i++) {
                if (model.combination[i].product_id == id) {
                    model.combination.splice(i, 1);
                }
            }
        });

        $("#addType").click(function(t){
            t.preventDefault();
            if(Business.verifyRight("TF_ADD")){
                THISPAGE.operateType({id:null,type:0,pid:null,oper:'add'});
            }
        });

        $("#addUnitType").click(function(t){
            t.preventDefault();
            if(Business.verifyRight("TF_ADD")){
                THISPAGE.operateType({id:null,type:1,pid:null,oper:'add'});
            }
        });

        THISPAGE.initBarCode();
        $('#keynum').keyup(function (event) {
            if (event.keyCode == 13) {
                THISPAGE.addCombinationRow($('#barCodeCheck').val(), $('#keynum').val());
            }
        })
        $('#okIndex').click(function (event) {
            THISPAGE.addCombinationRow($('#barCodeCheck').val(), $('#keynum').val());
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
                callBackFunction: function (checkProduct) {
                    var len = productListBySuggest == undefined ? 0 : productListBySuggest.length;
                    var checkLen = checkProduct.length;
                    for (var i = 0; i < checkLen; i++) {
                        for (var m = 0; m < len; m++) {
                            if (checkProduct[i].id == productListBySuggest[m].id) {
                                THISPAGE.addCombinationRow(productListBySuggest[m].billsn, checkProduct[i].num);
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
                                    THISPAGE.addCombinationRow(productListBySuggest[i].billsn, productListBySuggest[i].amount)
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
        })

        this.initValidator();
    },
    initBarCode:function(){
        var barCode = $("#productNameCheck").bsSuggest({
            url: rootPath + "/scm/product/dataGrid.json?keyword=",
            effectiveFields: ["billsn", "product_name", 'purchase_price'],
            effectiveFieldsAlias: {billsn: "一维码", product_name: "产品名称", '销售价': 'purchase_price'},
            showHeader: true,
            showBtn: false,
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
                return data;
            }
        }).on('onSetSelectValue', function (e, keyword) {
            //console.log('onSetSelectValue: ', keyword);
            $('#barCodeCheck').val(keyword.id);
            //$('#keynum').focus();
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
    },
    addCombinationRow: function (barCode, num) {
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
                    product_num: num,
                    product_name: productListBySuggest[i].product_name
                }
            }
        }
        if (parame == undefined) {
            alert("未找到您想寻找的产品，请核对");
            return;
        } else {
            var productLength = model.combination.length;
            for (var m = 0; m < productLength; m++) {
                if (model.combination[m].product_id == parame.product_id) {
                    model.combination[m].product_num = parseInt(model.combination[m].product_num) + parseInt(parame.product_num);
                    parame = undefined;
                    break;
                }
            }
            if (parame != undefined) {
                model.combination.push(parame);
            }
        }
        $('#barCodeCheck').val("");
        $('#keynum').val("")
        $('#productNameCheck').val("")
        $("#productNameCheck").focus();
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
    addComparison: function (unit, num) {
        if (!THISPAGE.isNum($("#comparisonUnit").val())) {
            alert("数量为数字，请核对");
            return;
        }
        model.comparison.push({
            id: index,
            comparison_unit: $("#comparisonUnit").val(),
            comparison_num: $("#comparisonNum").val(),
            product_id: ""
        })
        index++;
    },
    isNum: function (expression) {
        var arry;
        if (typeof (expression) == "undefined") {
            arry = $(".isNumber");
        } else {
            arry = $(expression);
        }
        for (i = 0; i < arry.length; i++) {
            var cur = $(arry[i]);
            if (!/^\d+$/.test(cur.val())) {
                alert(cur.attr("info") + "必须是整数！");
                cur.focus();
                return false;
            }
        }
        return true;
    },
    //operateType:function(opt){
    //    if ("add" == opt.oper)
    //        var i = "新增参数";
    //    else
    //        var i = "修改参数";
    //    parent.$.dialog({
    //        title:i,
    //        content:"url:"+rootPath+"/sso/parame"+"/edit.html",
    //        data:opt,
    //        width:400,
    //        height:400,
    //        max:false,
    //        min:false,
    //        cache:false,
    //        lock:true,
    //        close:function(){
    //            for(var i=0;i<tree.length;i++){
    //                tree[i].hide();
    //                tree[i].remove();
    //            }
    //            THISPAGE.initComboTree();
    //        }
    //    })
    //},
    initComboTree:function(){
        /**类别combo START*/
        var r = $("#category_sel");
        var i = Public.comboTree(r, {
            offset: {top: 0, left: 94}, url: rootPath + '/sso/parame/tree.json', postData: {type: 0},
            callback: {
                beforeClick: function (e, t) {
                    r.val(t.name);
                    model.data.category = t.id;
                    i.hide();
                },
                beforeRightClick: function () {
                    i.hide();
                    return false;
                }
            }
        });
        i.bind("contextmenu", function () {
            return false;
        });
        tree.push(i);

        /**类别combo END*/
        /**计量单位combo START*/
        var r1 = $("#unit_sel");
        var i1 = Public.comboTree(r1, {
            offset: {top: 0, left: 94}, url: rootPath + '/sso/parame/tree.json', postData: {type: 1},
            callback: {
                beforeClick: function (e, t) {
                    r1.val(t.name);
                    model.data.unit = t.id;
                    i1.hide();
                },
                beforeRightClick: function () {
                    i1.hide();
                    return false;
                }
            }
        });
        i1.bind("contextmenu", function () {
            return false;
        });
        tree.push(i1);
    }
};
function postData() {
    var e = "add" == oper ? "新增商品" : "修改商品";
    if (model.data.category == "") {
        parent.parent.Public.tips({type: 1, content: "类别未选择！"});
        return;
    }
    if (model.data.unit == "") {
        parent.parent.Public.tips({type: 1, content: "计量单位未选择！"});
        return;
    }
    if (model.data.product_type == 2) {
        if (model.combination.length == 0) {
            parent.parent.Public.tips({type: 1, content: "您未选择相应组合产品，请选择！"});
            return;
        }
    }
    Public.ajaxPost(rootPath + "/scm/product/save.json", model.data.$model, function (t) {
        if (200 == t.status) {
            parent.parent.Public.tips({
                content: e + "成功！"
            });
            var productId = t.data.id;
            var productSn = t.data.sn;
            var isAdd = "add" == oper ? true:false;
            for (var i = 0; i < model.comparison.length; i++) {
                model.comparison[i].product_id = productId;
                if (isAdd) {
                    model.comparison[i].id = "";
                }
            }
            if (model.comparison.length != 0) {
                var comparisonStr = JSON.stringify(model.comparison.$model)
                Public.ajaxPost(rootPath + "/scm/product/saveComparison.json", {
                    comparison: comparisonStr,
                    isAdd: isAdd
                }, function (t) {
                    if (200 == t.status) {

                        if (model.data.product_type != 2) {
                            parent.parent.Public.tips({
                                content: e + "成功！"
                            });
                            if (addNew) {
                                model.init();
                            } else {
                                model.data.id = productId;
                                model.data.billsn = productSn;
                            }
                        }
                    } else
                        parent.parent.Public.tips({
                            type: 1,
                            content: e + "失败！" + t.msg
                        });
                });
            }
            postCombination(isAdd, productId, productSn);
            //parent.THISPAGE.reloadData(null);
        } else
            parent.parent.Public.tips({
                type: 1,
                content: e + "失败！" + t.msg
            });
    });
}

function postCombination(isAdd, productId, productSn) {

    if (model.data.product_type != 2){
        parent.THISPAGE.reloadData(null);
        return;
    }
    var combinationStr = JSON.stringify(model.combination.$model)

    Public.ajaxPost(rootPath + "/scm/product/saveCombination.json", {
        combination: combinationStr,
        isAdd: isAdd,
        parent_id:productId
    }, function (t) {
        if (200 == t.status) {
            parent.parent.Public.tips({
                content: e + "成功！"
            });
            if (addNew) {
                model.init();
            } else {
                model.data.id = productId;
                model.data.billsn = productSn;
            }
            parent.THISPAGE.reloadData(null);
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
        productBsSuggest.bsSuggest("destroy");
        $("#productNameCheck").keyup(function (event) {
            if (event.keyCode == 13) {
                var url = rootPath + "/scm/product/dataGrid.json?keyword=" + $("#productNameCheck").val();
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