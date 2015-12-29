var queryConditions = {}, url = rootPath + "/scm/product", companyList = SYSTEM.companyList,
    api = frameElement.api,categoryTree,
    productUrl = rootPath + "/scm/product", gridQryUrl = productUrl + "/dataGrid.json", checkProduct = [],resultProduct = [],
    api = frameElement.api, callback = api.config.callBackFunction, treeCategoryFirst = true, treeBrandFirst = true,gridData = [],checkSimple = false;
var model = avalon.define({
    $id: 'checkProduct',
    brandList: [],
    specificationList:[],
    changeBrand: function (value) {
        queryConditions.brand = value;
        Public.ajaxPost(url + '/merchStanderMainList.json', queryConditions, function (json) {
            model.specificationList = json.data;
        })
        THISPAGE.reloadData(queryConditions,"brand");
    },

    changeStander: function (value) {
        queryConditions.specification = value;
        THISPAGE.reloadData(queryConditions,"stander");
    },

    changeKeyWord: function (value) {
        queryConditions.keyword = value;
    }
});


var queryConditions = {
    keyword: "", category: '', brand: '', type: 0,checkType:"1"
};
var THISPAGE = {
    init: function () {
        this.initDom();
        this.loadGrid();
        this.initBtn();
        this.addEvent()
    },
    initDom: function () {
        //this.$_keyword = $("#keyword")
        //api.data: {checkSimple:true},
        if(api.data && api.data.checkSimple){
            checkSimple = true;
            gridQryUrl = api.data.url;
        }
        Public.ajaxPost(url + '/merchBrandMainList.json', queryConditions, function (json) {
            model.brandList = json.data;
        })
        Public.ajaxPost(url + '/merchStanderMainList.json', queryConditions, function (json) {
            model.specificationList = json.data;
        })

    },
    //treeClick:function(e,t){
    //    r.val(t.name);
    //    queryConditions.category=t.id;
    //    i.hide();
    //},

    addEvent: function () {
        /**类别combo START*/
        $("#category").click(function () {
            if (treeCategoryFirst) {
                var r = $("#category");
                r.unbind("click");
                var i = Public.comboTree(r, {
                    url: rootPath + '/sso/parame/tree.json', postData: {},
                    callback: {
                        beforeClick: function (e, t) {
                            r.val(t.name);
                            //queryConditions = {
                            //    keyword: "", category: '', brand: '', type: 0,checkType:"1"
                            //};
                            queryConditions.category = t.id;
                            i.hide();
                            THISPAGE.reloadData(queryConditions);
                            Public.ajaxPost(url + '/merchBrandMainList.json', queryConditions, function (json) {
                                model.brandList = json.data;
                            })
                            Public.ajaxPost(url + '/merchStanderMainList.json', queryConditions, function (json) {
                                model.specificationList = json.data;
                            })
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
                i.bind("mouseenter",function(){
                    i.bind("mouseleave",function(){
                        i.unbind("mouseleave");
                        i.hide();
                    })
                })
                categoryTree = i;
            }
            treeCategoryFirst = false;
        })
        /**类别combo END*/
        $("#search").click(
            function () {
                queryConditions.keyword = $("#keyword").val();
                THISPAGE.reloadData(queryConditions)
            });

        $("#keyword").keyup(
            function (event) {
                if(event.keyCode == 13){
                    queryConditions = {
                        keyword: "", category: '', brand: '', type: 0,checkType:"1"
                    };
                    queryConditions.keyword = $("#keyword").val();
                    THISPAGE.reloadData(queryConditions)
                }
            });
        /**类别combo END*/
        $(window).resize(function () {
            Public.resizeGrid()
        })
    },
    loadGrid: function () {
        function fmtAmt(v) {
            return Public.numToCurrency(v);
        }
        var stock = {},amount = {};
        if(checkSimple){

            stock = {
                name: "amount",
                    label: "库存数量",
                align: "center",
                title: true
            };

            amount = {
                name: 'amount2',
                label: "数量",
                editable: true,
                edittype: 'text',
                editoptions: {
                    size: 10, maxlength: 15
                }
            }

        }else{
            stock = {
                name: 'allotmoney',
                label: "调拨单价",
                editable: true,
                edittype: 'text',
                editoptions: {
                    size: 10, maxlength: 15
                }
            };
            amount = {
                name: 'amount',
                label: "数量",
                editable: true,
                edittype: 'text',
                editoptions: {
                    size: 10, maxlength: 15
                }
            }
        }


        var i = Public.setGrid();
        $("#grid").jqGrid({
            url: gridQryUrl,
            postData: queryConditions,
            datatype: "json",
            mtype: 'POST',
            //autowidth : true,
            //width: windowWidth/3 *2.4,
            //height: windowHeight/3 *2.7,
            altRows: true,
            gridview: true,
            multiselect: true,
            rownumbers: true,
            multiboxonly: true,
            cellEdit: true,
            cellsubmit: 'clientArray',
            //afterSaveCell: function (rowid, cellname, value, iRow, iCol) {
            //    //alert(111);
            //    //focusNumId =
            //    var rowData = $("#grid").getRowData(rowid);
            //    $("#grid").setRowData(rowid, rowData);
            //
            //},
            loadComplete:function(json){
                gridData = json.data.list;
                //alert(11)
            },
            beforeRequest: function () {
                for(var i=0;i<checkProduct.length;i++){
                    resultProduct.push($("#grid").getRowData(checkProduct[i]));
                }
                checkProduct = [];
            },
            loadError: function () {
                parent.Public.tips({
                    type: 1,
                    content: "加载数据异常！"
                })
            },
            onCellSelect: function (rowid, index, contents, event) {

                var status = $('#jqg_grid_' + rowid)[0].checked;
                if (status && index == 1) {

                    $("#grid").jqGrid('editGridRow', rowid,
                        {recreateForm:true,closeAfterEdit:true,top:150,left:500,url:'clientArray',
                            closeOnEscape:true,reloadAfterSubmit:false,viewPagerButtons:false,onclickSubmit:function(t,data,i,y){
                            //alert(111);
                            //var rowData = data;
                            //if(rowData){
                            //    rowData.zhamt = rowData.zkl*(type==4?rowData.quoted_price:rowData.sale_price)/100;
                            //    rowData.amt = ((type==4?rowData.quoted_price:rowData.sale_price)-rowData.zhamt)*rowData.amount;
                            //    rowData.tax = rowData.amt*rowData.tax_rate/100;
                            //    rowData.amtCount =rowData.amt*(1+rowData.tax_rate/100);
                            //}
                            $('#jqg_grid_' + rowid)[0].checked = true;
                            checkProduct.push(rowid);
                        }});


                } else if(index == 1) {
                    for (var i = 0; i < checkProduct.length; i++) {
                        if (checkProduct[i] == rowid) {
                            checkProduct.splice(i, 1);
                        }
                    }
                    var len = resultProduct.length;
                    for(var m=0;m<len;m++){
                        if(resultProduct[m].product_id = rowid){
                            resultProduct.splice(m, 1);
                        }
                    }
                }
            },
            gridComplete: function () {
                var len = resultProduct.length;
                for (var i = 0; i < len; i++) {

                    if($("#grid").getRowData(resultProduct[i].product_id).product_id != undefined){
                        $("#grid").setRowData(resultProduct[i].product_id, resultProduct[i]);
                        //$('#' + resultProduct[i].product_id)[0].checked = true;
                        $('#jqg_grid_' + resultProduct[i].product_id)[0].checked = true;
                    }
                }
            },
            colModel: [{
                name: "product_id",
                label: "产品编号",
                align: "center",
                sortable: true,
                title: true,
                hidden: true
            }, {
                name: "billsn",
                label: "条形码",
                align: "center",
                sortable: true,
                title: true
            }, {
                name: "product_name",
                label: "商品名称",
                align: "center",
                sortable: true,
                title: true
            },stock,amount,{
                name: "unit",
                label: "单位",
                align: "center",
                width: 100,
                title: true,
                //editable: true,
                //edittype: 'select',
                //editoptions: {
                //    size: 10, maxlength: 15
                //},
                formatter:function(cellvalue, options, rowObject){
                    return SYSTEM.custParame[cellvalue].textname;
                },
                unformat:function(cellvalue, options){
                    var rowId = options.rowId;
                    var len = gridData.length;
                    for(var i=0;i<len;i++){
                        if(gridData[i].product_id = rowId){
                            return gridData[i].unit;
                        }
                    }
                    return cellvalue;
                }


            },{
                name: "description",
                label: "备注",
                align: "center",
                title: true,
                formatter:function(cellvalue, options, rowObject){
                    if(!cellvalue){
                        return "无";
                    }
                    return cellvalue
                }
            }],
            cmTemplate: {
                sortable: false,
                title: false
            },
            page: 1,
            sortname: "name",
            sortorder: "desc",
            pager: "#page",
            rowNum: 50,
            rowList: [50, 100, 200],
            viewrecords: true,
            shrinkToFit: false,
            forceFit: false,
            jsonReader: {
                root: "data.list",
                records: "data.totalRow",
                repeatitems: false,
                id: "product_id"
            }
        })
    },
    reloadData: function (t,type) {

        var r = $("#category");
        r.unbind("click");
        if(categoryTree){
            categoryTree.unbind("contextmenu");
            categoryTree.hide();
            $.fn.zTree.destroy("categoryTree");
        }
        var i = Public.comboTree(r, {
            url: rootPath + '/sso/parame/tree.json', postData: queryConditions,
            callback: {
                beforeClick: function (e, t) {
                    r.val(t.name);
                    //queryConditions = {
                    //    keyword: "", category: '', brand: '', type: 0,checkType:"1",customer_id:ParentCustomerId,specification:""
                    //};
                    queryConditions.category = t.id;
                    THISPAGE.reloadData(queryConditions);
                    Public.ajaxPost(url + '/merchBrandMainList.json', queryConditions, function (json) {
                        model.brandList = json.data;
                    })

                    Public.ajaxPost(url + '/merchStanderMainList.json', queryConditions, function (json) {
                        model.specificationList = json.data;
                    })
                    i.hide();
                },
                beforeRightClick: function () {
                    i.hide();
                    return false;
                }
            },treeId:'categoryTree'
        });
        i.bind("contextmenu", function () {
            return false;
        });
        i.bind("mouseenter",function(){
            i.bind("mouseleave",function(){
                i.unbind("mouseleave");
                i.hide();
            })
        });
        categoryTree = i;
        if(undefined == type){
            Public.ajaxPost(url + '/merchBrandMainList.json', queryConditions, function (json) {
                model.brandList = json.data;
            });

            Public.ajaxPost(url + '/merchStanderMainList.json', queryConditions, function (json) {
                model.specificationList = json.data;
            });
        }

        $("#grid").jqGrid("setGridParam", {
            url: gridQryUrl,
            datatype: "json", mtype: 'POST',
            postData: t
        }).trigger("reloadGrid");
    },
    initBtn: function () {
        var e = ["<i class='fa fa-save mrb'></i>确定", "关闭"];
        api.button({
            id: "confirm",
            name: e[0],
            focus: !0,
            callback: function () {
                for(var i=0;i<checkProduct.length;i++){
                    //return SYSTEM.custParame[cellvalue].textname;
                    var result = $("#grid").getRowData(checkProduct[i]);
                    if(!$.isNumeric(result.amount)){
                        parent.parent.Public.tips({
                            type: 1,
                            content: "第"+(i+1)+"行的数量为填写，请填写"
                        });
                        return false
                    }
                    resultProduct.push(result);
                }
                callback(resultProduct);
            }
        }, {
            id: "cancel",
            name: e[1]
        })
    }
};

THISPAGE.init();