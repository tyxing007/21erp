var queryConditions = {}, queryTreeType = {}, url = rootPath + "/sso/company", gridQryUrl = url + "/dataGrid.json", treeQryUrl = url + "/dataTree.json", companyList = SYSTEM.companyList;
var model = avalon.define({
    $id: 'company',
    query: {
        keyword: "", start_date: '', end_date: '', is_deleted: 0, uid: ""
    },
    init: function () {
        model.query = {
            keyword: "", start_date: '', end_date: '', type: -1, qryType: -1, status: 0, is_deleted: 0, uid: ""
        };
    }
});
var THISPAGE = {
    init: function () {
        this.initDom();
        this.loadGrid();
        this.addEvent()
        //this.loadTree();
    },
    initDom: function () {
        $(".ui-datepicker-input").datepicker();
    },
    loadGrid: function () {
        function t(t, e, row) {
            var html_con = '<div class="operating" data-id="' + row.id + '" data-type="' + row.type + '" data-name="' + row.name + '" data-nametext="' + row.nametext + '" data-department_id="' + row.department_id + '" data-department_name="' + row.department_name + '"><span class="fa fa-eye mrb" title="查看"></span><span class="fa fa-edit mrb" title="修改"></span><span class="fa fa-trash-o mrb" title="删除"></span>'
                + '</div>';
            return html_con;
        }

        function e(t) {
            var e = t.join('<p class="line" />');
            return e
        }

        var i = Public.setGrid();
        $("#grid").jqGrid({
            url: gridQryUrl,
            postData: queryConditions,
            datatype: "json",
            mtype: 'POST',
            autowidth: true,
            height: i.h,
            altRows: true,
            gridview: true, rownumbers: true,
            multiselect: false,
            multiboxonly: false,
            colModel: [
                {
                    name: "operating",
                    label: "操作",
                    fixed: true,
                    formatter: t,
                    width: 250,
                    align: "left",
                    title: false
                }, {
                    name: "code",
                    label: "编码",
                    align: "center",
                    //formatter: function (t, e, i) {
                    //    var type = ['部门', '岗位'];
                    //    return type[i.type];
                    //},
                    title: true
                }, {
                    name: "name",
                    label: "名称",
                    align: "left", formatter: function (v, e, r) {
                        return v + ((r.type == 1) ? "<i class='fa fa-user'></i>" : "");
                    },
                    title: true
                }, {
                    name: "short_name",
                    label: "简称",
                    align: "center",
                    //formatter: function (t, e, i) {
                    //    var type = ['部门', '岗位'];
                    //    return type[i.type];
                    //},
                    title: true
                }, {
                    name: "hyname",
                    label: "行业",
                    align: "center",
                    //formatter: function (t, e, i) {
                    //    var type = ['部门', '岗位'];
                    //    return type[i.type];
                    //},
                    title: true
                }, {
                    name: "telephone",
                    label: "电话",
                    align: "center",
                    //formatter: function (t, e, i) {
                    //    var type = ['部门', '岗位'];
                    //    return type[i.type];
                    //},
                    title: true
                }, {
                    name: "fax",
                    label: "传真",
                    align: "center",
                    //formatter: function (t, e, i) {
                    //    var type = ['部门', '岗位'];
                    //    return type[i.type];
                    //},
                    title: true
                }, {
                    name: "reg_email",
                    label: "邮箱",
                    align: "center",
                    //formatter: function (t, e, i) {
                    //    var type = ['部门', '岗位'];
                    //    return type[i.type];
                    //},
                    title: true
                }, {
                    name: "reg_date",
                    label: "注册时间",
                    align: "center",
                    //formatter: function (t, e, i) {
                    //    var type = ['部门', '岗位'];
                    //    return type[i.type];
                    //},
                    title: true
                }, {
                    name: "expiry_date",
                    label: "失效时间",
                    align: "center",
                    //formatter: function (t, e, i) {
                    //    var type = ['部门', '岗位'];
                    //    return type[i.type];
                    //},
                    title: true
                }],
            cmTemplate: {
                sortable: false,
                title: false
            },
            page: 1,
            sortname: "",
            sortorder: "",
            pager: "#page",
            rowNum: 99999,
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
                $("#" + t).find(".fa-eye").trigger("click")
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

    view: function (id) {
        $.dialog({
            id: "moreCon",
            width: 850,
            height: 600,
            min: true,
            max: true,
            title: "查看企业信息",
            button: [{
                name: "关闭"
            }],
            resize: true,
            lock: true,
            content: "url:" + url + "/view",
            data: {
                id: id
            }
        });
    },

    addEvent: function () {
        var t = this;
        $(".grid-wrap").on("click", ".fa-eye", function (t) {
            t.preventDefault();
            var e = $(this).parent().data("id");
            //alert(e);
            THISPAGE.view(e);
        });
        $(".grid-wrap").on("click", ".fa-edit", function (e) {
            e.preventDefault();
            if (Business.verifyRight("TD_UPDATE")) {
                var t = $(this).parent().data("id");
                handle.createCompany("edit", t)
            }
        });
        $(".grid-wrap").on("click", ".fa-trash-o", function (t) {
            //t.preventDefault();
            //if (Business.verifyRight("BU_DELETE")) {
            //    var e = $(this).parent().data("id");
            //    if (model.query.qryType == 100)
            //        handle.del(e);
            //    else
            //        handle.trash(e);
            //}
            alert("暂不支持删除功能");
        });
        $("#search").click(function () {
            THISPAGE.reloadData()
        });
        $("#refresh").click(function () {
            model.init();
            THISPAGE.reloadData()
        });
        $("#addCompany").click(function (e) {
            e.preventDefault();
            if (Business.verifyRight("BU_DELETE")) {
                handle.createCompany("add");

            }
        });
        $(window).resize(function () {
            Public.resizeGrid()
        })
        Public.pageTab();
    }

};
var dg;
var handle = {
    closeDialogCom:function(){
        dg.close()
    },
    createCompany: function (e, t) {
        if ("add" == e)
            var i = "新增公司", r = {
                oper: e,
                callback: this.closeDialogCom
            };
        else
            var i = "修改公司", r = {
                oper: e,
                id: t,
                callback: this.closeDialogCom
            };
        dg = $.dialog({
            title: i,
            content: "url:" + url + "/edit.html",
            data: r,
            width: 800,
            height: 600,
            max: false,
            min: false,
            cache: false,
            lock: true
        })

    }
};


THISPAGE.init()

