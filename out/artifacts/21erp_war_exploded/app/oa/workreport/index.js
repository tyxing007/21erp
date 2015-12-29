var url = rootPath + "/oa/workreport", gridQryUrl = url + "/dataGrid.json", type = ["供应商", "企业", "个人", "经销商"]
    , companyList = SYSTEM.companyList,haveCompany,tree,first=true
sysCompanyId = SYSTEM.company.id;
var model = avalon.define({
    $id: 'index',
    query:  {
        keyword: "", start_date: '', end_date: '', type: '', qryType: -1, status: 0, is_deleted: 0, uid: "",companyId:sysCompanyId
    }, expoutUrl: "",
    fastQryText: "快速查询",
    fastQry: [
        {text: "我创建的", sl: false},
        {text: "我负责的", sl: false},
        {text: "下属创建的", sl: false},
        {text: "下属负责的", sl: false},
        {text: "", sl: true},
    ],
    typeQryText:"报告类型",
    typeQry:[],
    qry: function (type) {
        model.query.qryType = type;
        model.fastQryText = model.fastQry[type].text;
        THISPAGE.reloadData();
    },
    qryType:function(type){
        model.query.type = type.id;
        model.typeQryText = type.name;
        THISPAGE.reloadData();
    }
})

var THISPAGE = {
    init: function () {
        this.initDom();
        this.loadGrid();
        this.addEvent()
    },
    view: function (id) {
        $.dialog({
            id: "moreCon",
            width: 850,
            height: 600,
            min: true,
            max: true,
            title: "查看客户",
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
    initDom: function () {
        $(".ui-datepicker-input").datepicker();
        //$.fancybox({type:"image",width:900,height:700,autoSize:false,fitToView:true});
        var r = $("#headCombo");
        var i = Public.comboTree(r, {
            width: 260, url: rootPath + '/sso/user/userTree.json?type=2', callback: {
                beforeClick: function (e, t) {
                    if (t.type == 10) {
                        r.val(t.name);
                        model.query.uid = t.id;
                    } else {
                        r.val("");
                        model.query.uid = "";
                    }
                    i.hide();
                }
            }
        });
        //url: rootPath + '/sso/parame/tree.json', postData: {type: 0},
        Public.ajaxPost(rootPath + '/sso/parame/tree.json', {type: 21}, function (t) {
            model.typeQry = t.data;
        });
    },
    view: function (id) {
        $.dialog({
            id: "moreCon",
            width: 850,
            height: 600,
            min: true,
            max: true,
            title: "查看客户",
            button: [{
                name: "关闭"
            }],
            resize: true,
            lock: true,
            content: "url:" + url + "/view",
            data: {
                id: id,
                companyId:model.query.companyId
            }
        });
    },
    loadGrid: function () {
        function t(val, opt, row) {
            var html_con = '<div class="operating" data-id="'
                + row.id
                + '"><span class="fa fa-eye mrb" title="查看"></span></div>';
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
            gridview: true,
            rownumbers: true,
            multiselect: true,
            multiboxonly: true,
            colModel: [{
                name: "operating",
                label: "操作",
                fixed: true,
                width: 100,
                formatter: t,
                align: "center",
                title: false
            }, {
                name: "subject",
                label: "主题",
                align: "center",
                width: 100,
                sortable: true,
                title: true
            }, {
                name: "type_name",
                label: "类型",
                align: "center",
                width: 100,
                sortable: true,
                title: true
            }, {
                name: "report_date",
                label: "报告时间",
                align: "center",
                width: 100,
                sortable: true,
                title: true
            },{
                name: "order_count",
                label: "订单总数量",
                align: "center",
                width: 100, sortable: true,
                title: false
            }, {
                name: "money_count",
                label: "订单金额",
                align: "center",
                width: 100, sortable: true,
                title: true
            }, {
                name: "user_name",
                label: "报告人",
                align: "center",
                width: 100, sortable: true,
                title: true
            }],
            cmTemplate: {
                sortable: false,
                title: false
            },
            page: 1,
            sortname: "subject",
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
            }
        });
    },
    reloadData: function () {
        $("#grid").jqGrid("setGridParam", {
            url: gridQryUrl,
            datatype: "json",
            mtype: 'POST',
            postData: model.query.$model
        }).trigger("reloadGrid");
    },
    addEvent: function () {
        $("#search").click(function () {
            THISPAGE.reloadData()
        });

        $(".grid-wrap").on("click", ".fa-eye", function (t) {
            t.preventDefault();
            var e = $(this).parent().data("id");
            THISPAGE.view(e);
        });
    }
};
var handle = {

}
THISPAGE.init();