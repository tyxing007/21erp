var url = rootPath + "/scm/storageBill", gridQryUrl = url + "/dataGrid.json", custParame = SYSTEM.custParame, typeList = custParame.typeList,
    billType = ['采购入库', '销售退货入库', '调拨入库', '其它入库', '销售出库', '采购退货出库', '调拨出库', '其它出库'];
var model = avalon.define({
    $id: 'view',
    query: {
        keyword: "",
        start_date: SYSTEM.beginDate,
        end_date: SYSTEM.endDate,
        type: "",
        is_deleted: 0,
        qryType: 5,
        status: ""
    },
    parameList: typeList, billType: billType,
    fastQryText: "快速查询",
    fastQry: [
        {text: "我创建的", sl: false},
        {text: "我负责的", sl: false},
        {text: "下属创建的", sl: false},
        {text: "下属负责的", sl: false},
        {text: "回收站", sl: false},
        {text: "", sl: true},
        {text: "未提交", sl: false},
        {text: "已提交", sl: false},
        {text: "未审核", sl: false}
    ],
    qry: function (type) {
        if (type == 4) {
            model.query.is_deleted = 1;
            model.query.qryType = -1;//查看自己的回收站信息
            model.fastQryText = "回收站";
            model.query.status = "";
        } else if (type > 5) {//提交情况
            model.fastQryText = model.fastQry[type].text;
            model.query.status = type - 6;
            model.query.qryType = 5;
            model.query.is_deleted = 0;
        } else {
            model.fastQryText = model.fastQry[type].text;
            model.query.is_deleted = 0;
            model.query.qryType = type;
            model.query.status = "";
        }
        model.reloadData();
    },
    init: function () {
        $(".ui-datepicker-input").datepicker();
        this.loadGrid();
        this.addEvent()
    },
    resetQry: function () {
        model.query = {
            keyword: "",
            start_date: SYSTEM.beginDate,
            end_date: SYSTEM.endDate,
            status: "",
            ordertype: type
        };
        model.reloadData();
    },
    loadGrid: function () {
        function t(val, opt, row) {
            var html_con = '<div class="operating" data-id="' + row.id + '"><span class="fa fa-eye mrb" title="查看"></span>';
            if (row.is_deleted == 0) {
                if (row.status == 0) { // 0 为除网点订单之外的所有状态
                    html_con += '<span class="fa fa-edit mrb" title="修改"></span><span class="fa fa-trash-o mrb" title="删除"></span>';
                    html_con += '<span class="glyphicon glyphicon-print mrb printCheck" title="打印拣货单"></span>';
                    html_con += '<a href="#" class="fa mrb submit" title="提交">提交</a>';
                } else if (row.status == 2) { //出库核对   1为已完成 2为拣货单 4为等待财务核对  5为出库单
//glyphicon glyphicon-print　　　
                    html_con += '<span class="glyphicon glyphicon-print mrb printCheck" title="打印拣货单"></span>' +
                        '<span class="glyphicon glyphicon-list-alt mrb" title="出库单核对"></span>'
//<textarea id="editor" ms-duplex="data.pact" style="height:200px;width:100%;"></textarea>
                } else if (row.status == 5) {
                    html_con += '<span class="glyphicon glyphicon-print mrb printOut" title="打印'+billType[row.type]+'单"></span>' +
                        '<span class="glyphicon glyphicon-ok mrb" title="'+billType[row.type]+'"></span>'
                }
            }
            if (row.is_deleted == 1){
                html_con += '<span class="fa fa-reply mrb" title="恢复"></span>';
            }
            html_con += '</div>';
            return html_con;
        }

        var i = Public.setGrid();
        $("#grid").jqGrid({
            url: gridQryUrl,
            postData: model.query.$model,
            datatype: "json",
            mtype: 'POST',
            autowidth: true,
            height: i.h,
            altRows: true,
            gridview: true, rownumbers: true,
            multiselect: true,
            multiboxonly: true,
            colModel: [{
                name: "operating",
                label: "操作",
                fixed: true, width: 150,
                formatter: t,
                align: "center",
                title: false
            }, {
                name: "type",
                label: "类别",
                align: "center",
                formatter: function (v) {
                    return billType[v];
                },
                unformat: function (v) {
                    var len = billType.length;
                    for (var i = 0; i < len; i++) {
                        if (billType[i] == v) {
                            return i;
                        }
                    }
                    return v;
                },
                width: 100, sortable: true,
                title: false
            }, {
                name: "billsn",
                label: "单号",
                align: "center",
                width: 100, sortable: true,
                title: false
            }, {
                name: "busbillsn",
                label: "业务单号",
                align: "center",
                width: 100, sortable: true,
                title: false
            }, {
                name: "customer",
                label: "客户名称",
                align: "center",
                width: 100, sortable: true,
                title: false
            }, {
                name: "depot_name",
                label: "仓库",
                align: "center", sortable: true,
                width: 100,
                title: false
            }, {
                name: "bill_date",
                label: "单据日期",
                align: "center",
                width: 100, sortable: true,
                title: false
            }, {
                name: "creater_name",
                label: "创建人", sortable: true,
                align: "center", formatter: function (v) {
                    return v == '' ? '系统' : v;
                },
                width: 100,
                title: false
            }, {
                name: "create_datetime",
                label: "创建时间", sortable: true,
                align: "center",
                width: 100,
                title: false
            }],
            cmTemplate: {
                sortable: false,
                title: false
            },
            page: 1,
            sortname: "create_datetime",
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
                id: "id"
            },
            loadError: function () {
                parent.Public.tips({
                    type: 1,
                    content: "加载数据异常！"
                })
            },
            ondblClickRow: function (t) {
                model.view(t);
            }
        })
    },
    reloadData: function () {
        $("#grid").jqGrid("setGridParam", {
            url: gridQryUrl,
            datatype: "json", mtype: 'POST',
            postData: model.query.$model
        }).trigger("reloadGrid");
    },
    addEvent: function () {
        Public.dateCheck();
        var t = this;
        $(".grid-wrap").on("click", ".fa-eye", function (t) {
            t.preventDefault();
            var e = $(this).parent().data("id");
            model.view(e);
        });
        $(".grid-wrap").on("click", ".fa-edit", function (e) {
            e.preventDefault();
            if (Business.verifyRight("TD_UPDATE")) {
                var t = $(this).parent().data("id");
                model.operate("edit", t)
            }
        });
        $(".grid-wrap").on("click", ".submit", function (e) {
            e.preventDefault();
            if (Business.verifyRight("TD_UPDATE")) {
                var t = $(this).parent().data("id");
                var type = $("#grid").getRowData(t).type;
                model.check(t, type);
            }
        });
        $(".grid-wrap").on("click", ".fa-trash-o", function (t) {
            t.preventDefault();
            if (Business.verifyRight("BU_DELETE")) {
                var e = $(this).parent().data("id");
                if (model.query.qryType == 4)
                    model.del(e);
                else
                    model.trash(e);
            }
        });
        $(".grid-wrap").on("click", ".fa-reply", function (t) {
            t.preventDefault();
            var e = $(this).parent().data("id");
            model.reply(e);
        });

        $(".grid-wrap").on("click", ".printCheck", function (t) {
            t.preventDefault();
            var e = $(this).parent().data("id");
            var type = $("#grid").getRowData(e).type;
            model.print("check", e,type);
        });

        $(".grid-wrap").on("click", ".printOut", function (t) {
            t.preventDefault();
            var e = $(this).parent().data("id");
            var type = $("#grid").getRowData(e).type;
            model.print("out", e,type);
        });

        //


        $(".grid-wrap").on("click", ".glyphicon-list-alt", function (t) {
            t.preventDefault();
            var e = $(this).parent().data("id");
            var type = $("#grid").getRowData(e).type;
            model.check(e, type);
        });

        $(".grid-wrap").on("click", ".glyphicon-ok", function (t) {
            t.preventDefault();
            var e = $(this).parent().data("id");
            model.ok(e);
        });


        $("#add").click(function (t) {
            t.preventDefault();
            if (Business.verifyRight("TF_ADD")) {
                model.operate('add');
            }
        });
        $("#btn-batchDel").click(function (e) {
            e.preventDefault();
            if (Business.verifyRight("BU_DELETE")) {
                var t = $("#grid").jqGrid("getGridParam", "selarrrow");
                t.length ?
                    ((model.query.qryType == 4) ? model.del(t.join()) : model.trash(t.join()))
                    : parent.Public.tips({
                    type: 2,
                    content: "请选择需要删除的项"
                })
            }
        });
        $("#btn-batchReply").click(function (e) {
            e.preventDefault();
            var t = $("#grid").jqGrid("getGridParam", "selarrrow");
            if (t.length) {
                model.reply(t.join());
            } else
                parent.Public.tips({type: 2, content: "请选择需要恢复的出库入库单"});
        });
        $(window).resize(function () {
            Public.resizeGrid()
        })
    },


    operate: function (e, t) {
        if ("add" == e)
            var i = "新增出库入库单", r = {oper: e};
        else
            var i = "修改出库入库单", r = {oper: e, id: t};
        $.dialog({
            title: i, content: "url:" + url + "/edit.html",
            data: r,width:$(window).width()*0.8,height :$(window).height()*0.9, max: true, resize: true, min: false, cache: false, lock: true
        })
    },


    submitView: function (t) {
        var r = {id: t};
        $.dialog({
            title: "提交出库入库单",
            content: "url:" + url + "/submitView.html",
            data: r,
            width:$(window).width()*0.8,height :$(window).height()*0.9,
            max: true,
            resize: true,
            min: false,
            cache: false,
            lock: true,
            close: function () {
                model.reloadData();
            }
        })
    },


    print: function (e, t,type) {
        if ("check" == e) {
            var i = "打印"+billType[type]+"拣货单", r = {oper: e, id: t,type:type,width:$(window).width()*0.5,height :$(window).height()*0.6};
            $.dialog({
                title: i, content: "url:" + url + "/printStorage.html",
                data: r, width:$(window).width()*0.5,height :$(window).height()*0.6, max: true, resize: true, min: false, cache: false, lock: true
            })
        }
        else {
            var i = "打印"+billType[type]+"单", r = {oper: e, id: t,type:type,width:$(window).width()*0.5,height :$(window).height()*0.6};
            $.dialog({
                title: i, content: "url:" + url + "/printOutStorage.html",
                data: r, width:$(window).width()*0.5,height :$(window).height()*0.6, max: true, resize: true, min: false, cache: false, lock: true
            })
        }
    },
    check: function (t, type) {
        //核对取货单
        var r = {id: t, type: type};

        $.dialog({
            title: '核对' + billType[type] + '单', content: "url:" + url + "/checkStorage.html",
            data: r, width:$(window).width()*0.8,height :$(window).height()*0.9, max: true, resize: true, min: false, cache: false, lock: true
        })
    },
    view: function (id) {
        $.dialog({
            id: "dialog1", width:$(window).width()*0.8,height :$(window).height()*0.9, min: true, max: true,
            title: "查看出库/入库单", button: [{name: "关闭"}], resize: true, lock: true,
            content: "url:" + url + "/view.html", data: {id: id, type: type}
        });
    },
    reply: function (e) {
        Public.ajaxPost(url + "/reply.json", {id: e}, function (t) {
            if (t && 200 == t.status) {
                parent.Public.tips({type: 2, content: t.msg});
                model.reloadData();
            } else
                parent.Public.tips({type: 1, content: "恢复出库入库单失败！" + t.msg})
        });
    },
    trash: function (e) {
        $.dialog.confirm("删除的出库入库单将不能恢复，请确认是否删除？", function () {
            Public.ajaxPost(url + "/trash", {id: e}, function (t) {
                if (t && 200 == t.status) {
                    parent.Public.tips({type: 2, content: t.msg});
                    model.reloadData();
                } else
                    parent.Public.tips({type: 1, content: "删除出库入库单失败！" + t.msg});
            });
        })
    },
    del: function (id) {
        $.dialog.confirm("删除的出库入库单将不能恢复，请确认是否删除？", function () {
            Public.ajaxPost(url + "/del.json", {id: id}, function (t) {
                if (t && 200 == t.status) {
                    parent.Public.tips({type: 2, content: t.msg});
                    model.reloadData();
                } else {
                    parent.Public.tips({type: 1, content: "删除出库入库单失败！请检查是否被引用！" + t.msg});
                }
            })
        });
    },
    submit: function (id) {
        $.dialog.confirm("提交后数据将不可修改，确定要提交吗？", function () {
            Public.ajaxPost(url + "/submit.json", {id: id}, function (t) {
                if (t && 200 == t.status) {
                    parent.Public.tips({type: 2, content: t.msg});
                    model.reloadData();
                } else {
                    parent.Public.tips({type: 1, content: t.msg});
                }
            });
        });
    },
    ok: function (e) {
        $.dialog.confirm("提交后数据将不可修改，确定要提交吗？", function () {
            Public.ajaxPost(url + "/ok.json", {id: e}, function (t) {
                if (t && 200 == t.status) {
                    parent.Public.tips({type: 2, content: t.msg});
                    model.reloadData();
                } else {
                    parent.Public.tips({type: 1, content: t.msg});
                }
            });
        });
    }
});
model.init();