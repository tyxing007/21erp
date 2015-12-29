var queryConditions = {}, queryTreeType = {}, url = rootPath + "/sso/position", gridQryUrl = url + "/dataGrid.json", treeQryUrl = url + "/dataTree.json", companyList = SYSTEM.companyList,
    companyId = "";
THISPAGE = {
    init: function () {
        this.initDom();
        this.loadGrid();
        this.addEvent()
        //this.loadTree();
    },
    initDom: function () {

    },
    loadGrid: function () {
        function t(t, e, row) {
            var html_con = '<div class="operating" data-id="' + row.id + '" data-type="' + row.type + '" data-name="' + row.name + '" data-nametext="' + row.nametext + '" data-department_id="' + row.department_id + '" data-department_name="' + row.department_name + '"><span class="fa fa-eye mrb" title="查看"></span><span class="fa fa-edit mrb" title="修改"></span><span class="fa fa-trash-o mrb" title="删除"></span>'
                + (row.type == 0 ? '<span class="fa fa-plus mrb subDepartment">添加子部门</span><span class="fa fa-plus mrb position">添加岗位</span>' : '<span class="fa fa-plus mrb subPosition">添加子岗位</span>')
                + (row.type == 0 ? '' : '<span class="fa fa-plus mrb permission">权限</span>')
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
                    name: "name",
                    label: "名称",
                    align: "left", formatter: function (v, e, r) {
                        return v + ((r.type == 1) ? "<i class='fa fa-user'></i>" : "");
                    },
                    title: true
                }, {
                    name: "type",
                    label: "类别",
                    align: "center",
                    formatter: function (t, e, i) {
                        var type = ['部门', '岗位'];
                        return type[i.type];
                    },
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
    reloadData: function (t) {

        $("#grid").jqGrid("setGridParam", {
            url: gridQryUrl,
            datatype: "json", mtype: 'POST',
            postData: t
        }).trigger("reloadGrid");
    },
    addEvent: function () {
        var t = this;
        $(".grid-wrap").on("click", ".fa-eye", function (t) {//查看
            t.preventDefault();
            var e = $(this).parent().data("id");
            var type = $(this).parent().data("type");
            handle.view({id: e, type: type,companyId:model.query.companyId});
        });
        $(".grid-wrap").on("click", ".fa-edit", function (e) {//编辑
            e.preventDefault();
            if (Business.verifyRight("EDIT")) {
                var id = $(this).parent().data("id");
                var type = $(this).parent().data("type");
                handle.operate({id: id, type: type, pid: '', oper: 'edit'});
            }
        });
        $(".grid-wrap").on("click", ".subDepartment", function (e) {//添加子部门
            e.preventDefault();
            if (Business.verifyRight("EDIT")) {
                var id = $(this).parent().data("id");
                var parent_name = $(this).parent().data("nametext");
                handle.operate({id: null, type: 0, pid: id, oper: 'add', parent_name: parent_name});
            }
        });
        $(".grid-wrap").on("click", ".position", function (e) {//添加部门下岗位
            e.preventDefault();
            if (Business.verifyRight("EDIT")) {
                var id = $(this).parent().data("id");
                var department_name = $(this).parent().data("nametext");
                handle.operate({
                    id: null,
                    type: 1,
                    pid: null,
                    department_id: id,
                    department_name: department_name,
                    oper: 'add'
                });
            }
        });
        $(".grid-wrap").on("click", ".subPosition", function (e) {//添加子岗位
            e.preventDefault();
            if (Business.verifyRight("EDIT")) {
                var id = $(this).parent().data("id");
                var parent_name = $(this).parent().data("nametext");
                var department_id = $(this).parent().data("department_id");
                var department_name = $(this).parent().data("department_name");
                handle.operate({
                    id: null,
                    type: 1,
                    pid: id,
                    oper: 'add',
                    parent_name: parent_name,
                    department_id: department_id,
                    department_name: department_name
                });
            }
        });
        $(".grid-wrap").on("click", ".permission", function (e) {//设置岗位权限
            e.preventDefault();
            if (Business.verifyRight("EDIT")) {
                var id = $(this).parent().data("id");
                var name = $(this).parent().data("name");
                handle.permission({id: id, name: name, companyId: companyId});
            }
        });
        $(".grid-wrap").on("click", ".fa-trash-o", function (t) {//删除
            t.preventDefault();
            if (Business.verifyRight("BU_DELETE")) {
                var e = $(this).parent().data("id");
                handle.del(e, $(this).parent().data("type"),model.query.companyId);
            }
        });

        $("#addDepartment").click(function (t) {
            t.preventDefault();
            if (Business.verifyRight("DE_ADD")) {
                handle.operate({id: null, type: 0, pid: null, oper: 'add'});
            }
        });
        $("#addPosition").click(function (t) {
            t.preventDefault();
            if (Business.verifyRight("TF_ADD")) {
                handle.operate({id: null, type: 1, pid: null, oper: 'add'});
            }
        });
        $("#btn-batchDel").click(function (e) {
            e.preventDefault();
            if (Business.verifyRight("CR_COMPANY")) {
                handle.operate({id: null, type: 0, pid: null, oper: 'add'});
            }
        });
        $(window).resize(function () {
            Public.resizeGrid()
        })
        Public.pageTab();
    },
    getTreeType: function (type, pid, id) {
        queryTreeType = {
            type: type,
            pid: pid,
            id: id
        }
    },
    getChildTree: function () {

    },
    getTree: function () {
        $.ajax({
            type: "POST",//使用get方法访问后台
            dataType: "json",//返回json格式的数据
            url: treeQryUrl,//要访问的后台地址
            data: queryConditions,
            success: function (msg) {//msg为返回的数据，在这里做数据绑定
                var data = msg.data.list;
                var tree;
                //alert(data)
                $.each(data, function (i, n) {

                    alert(n);
                    for (var i = 0; i < n.length; i++) {
                        var treeData = n[i];
                        for (var m = 0; m < treeData.length(); m++) {
                            var treeViewData = [];
                            treeViewData.push({})
                        }
                    }

                    //tree = [
                    //    {
                    //        text: "Parent 1",
                    //        nodes: [
                    //            {
                    //                text: "Child 1",
                    //                nodes: [
                    //                    {
                    //                        text: "Grandchild 1"
                    //                    },
                    //                    {
                    //                        text: "Grandchild 2"
                    //                    }
                    //                ]
                    //            },
                    //            {
                    //                text: "Child 2"
                    //            }
                    //        ]
                    //    },
                    //    {
                    //        text: "Parent 2"
                    //    },
                    //    {
                    //        text: "Parent 3"
                    //    },
                    //    {
                    //        text: "Parent 4"
                    //    },
                    //    {
                    //        text: "Parent 5"
                    //    }
                    //];


                    //alert(i)
                    //alert(n)
                    //var row = $("#template").clone();
                    //row.find("#OrderID").text(n.订单ID);
                    //row.find("#CustomerID").text(n.客户ID);
                    //row.find("#EmployeeID").text(n.雇员ID);
                    //row.find("#OrderDate").text(ChangeDate(n.订购日期));
                    //if (n.发货日期 !== undefined) row.find("#ShippedDate").text(ChangeDate(n.发货日期));
                    //row.find("#ShippedName").text(n.货主名称);
                    //row.find("#ShippedAddress").text(n.货主地址);
                    //row.find("#ShippedCity").text(n.货主城市);
                    //row.find("#more").html("<a href=OrderInfo.aspx?id=" + n.订单ID + "&pageindex=" + pageIndex + ">&nbsp;More</a>");
                    //row.attr("id", "ready");//改变绑定好数据的行的id
                    //row.appendTo("#datas");//添加到模板的容器中
                });

                return tree;

            }
        })


    },
    loadTree: function () {
        $('#depatTree').treeview("collapseAll", {data: THISPAGE.getTree()});
    }
};
var handle = {
    //opt={e,id,pid,type,department_id}
    operate: function (opt) {
        if ("add" == opt.oper)
            var i = "新增" + (opt.type == 0 ? '部门' : '岗位');
        else
            var i = "修改" + (opt.type == 0 ? '部门' : '岗位');
        $.dialog({
            title: i,
            content: "url:" + url + "/edit.html",
            data: opt,
            width: 400,
            height: 500,
            max: false,
            min: false,
            cache: false,
            lock: true
        })
    },
    permission: function (opt) {
        $.dialog({
            title: "设置权限",
            content: "url:" + url + "/permission.html",
            data: opt,
            width: 800,
            height: 600,
            max: false,
            min: false,
            cache: false,
            lock: true
        })
    },

    //opt={id,pid,type,department_id}
    view: function (opt) {
        var i = "查看" + (opt.type == 0 ? '部门' : '岗位');
        $.dialog({
            title: i,
            content: "url:" + url + "/view.html",
            data: opt,
            width: 400,
            height: 500,
            max: true,
            min: false,
            cache: false,
            lock: true
        })
    },
    del: function (e, type,companyId) {
        var tit = type == 0 ? '部门' : '岗位';
        $.dialog.confirm("删除的" + tit + "将不能恢复，请确认是否删除？", function () {
            Public.ajaxPost(rootPath + "/sso/position/del.json", {
                id: e,companyId:companyId
            }, function (t) {
                if (t && 200 == t.status) {
                    var i = t.data.id || [];
                    parent.Public.tips({type: 2, content: t.msg});
                    THISPAGE.reloadData();
                } else
                    parent.Public.tips({type: 1, content: "删除" + tit + "失败！" + t.msg})
            })
        })
    },
    callback: function (e, t, i) {
    }


};

var model = avalon.define({
    $id: "create",
    query: {
        keyword: "", companyId: "0000"
    },
    companyList: companyList,
    showMenu: function (code) {
        return SYSTEM.rights[code] == true;
    },
    reload: function () {
        companyId = model.query.companyId;
        THISPAGE.reloadData({companyId: model.query.companyId});
    }
});
THISPAGE.init();



