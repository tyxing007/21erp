function initImages() {

}
/*显示客户的地理位置*/
function initializeView() {
    var latitude = model.data.lat;
    var longitude = model.data.lng;
    var lat;
    var lng;
    lat = latitude.substring(0, latitude.indexOf(".") + 7);
    lng = longitude.substring(0, longitude.indexOf(".") + 7);
    var position = new AMap.LngLat(lat, lng);
    var map = new AMap.Map("customer_map", {
        view: new AMap.View2D({//创建地图二维视口
            center: position,//创建中心点坐标
            zoom: 18, //设置地图缩放级别
            rotation: 0 //设置地图旋转角度
        }),
        lang: "zh_cn"//设置地图语言类型，默认：中文简体
    });//创建地图实例
    var marker = new AMap.Marker({ //创建自定义点标注
        map: map,
        position: position,
        offset: new AMap.Pixel(0, 0),
        icon: "http://webapi.amap.com/images/marker_sprite.png"
    });
    marker.setLabel({
            offset: new AMap.Pixel(0, -20),
            content: model.data.address
        }
    );

    AMap.HomeControl = function () {
    };
    AMap.HomeControl.prototype = {
        constructor: AMap.HomeControl,
        addTo: function (map, dom) {
            dom.appendChild(this._getHtmlDom(map));
        },
        _getHtmlDom: function (map) {
            this.map = map;

            // 创建一个能承载控件的元素
            var controlUI = this.container = document.createElement("div");
            controlUI.className = 'home-control';
            controlUI.innerHTML = '返回中心点';

            // 设置控件响应点击 click 事件
            AMap.event.addDomListener(controlUI, 'click', function () {
                var point = map.setCenter([latitude, longitude]);
                map.setZoom(15);
            });
            return controlUI;
        }
    }

    var homeControl = new AMap.HomeControl(map);
    map.addControl(homeControl);
}

var api = frameElement.api, oper = api.data.oper, id = api.data.id, $_form = $("#base_form"), area = SYSTEM.area, custRating = SYSTEM.custRating, url = rootPath + "/crm/customer", companyId = api.data.companyId;
var model = avalon.define({
    $id: 'view',
    imgArray: [],
    data: {
        province: "",
        city: "",
        name: "",
        type: 1,
        sn: "",
        remark: "",
        website: "",
        address: "",
        fax: "",
        telephone: "",
        mobile: "",
        email: "",
        head_name: "",
        id: "",
        is_delete: 0,
        lat: "",
        lng: "",
        image_src: ""
    },
    //首要联系人
    contacts: {
        name: "",
        sex: 1,
        mobile: "",
        is_main: 1,
        post: "",
        idcard: "",
        department: "",
        saltname: "",
        telephone: "",
        qq: "",
        email: "",
        address: "",
        zip_code: "",
        customer_id: "",
        description: "",
        id: ""
    },
    tabActive: 0,
    showTab: function (i, b) {
        model.tabActive = i;
        if (i == 4) {
            //model.initializeView()
        }
    }
});
avalon.filters.rating = function (v) {
    return custRating[v].name;
}
avalon.filters.type = function (v) {
    return parent.type[v];
}
var THISPAGE = {
    init: function () {
        this.initDom();
        this.loadGrid();
    },
    initImage: function () {
        var sWidth = $("#focus").width();
        var len = $("#focus ul li").length;
        var index = 0;
        var picTimer;
        var btn = "<div class='btnBg'></div><div class='btn'>";
        for (var i = 0; i < len; i++) {
            btn += "<span></span>";
        }
        btn += "</div><div class='preNext pre'></div><div class='preNext next'></div>";
        $("#focus").append(btn);
        $("#focus .btnBg").css("opacity", 0);
        $("#focus .btn span").css("opacity", 0.4).mouseenter(function () {
            index = $("#focus .btn span").index(this);
            showPics(index);
        }).eq(0).trigger("mouseenter");
        $("#focus .preNext").css("opacity", 0.0).hover(function () {
            $(this).stop(true, false).animate({"opacity": "0.5"}, 300);
        }, function () {
            $(this).stop(true, false).animate({"opacity": "0"}, 300);
        });
        $("#focus .pre").click(function () {
            index -= 1;
            if (index == -1) {
                index = len - 1;
            }
            showPics(index);
        });
        $("#focus .next").click(function () {
            index += 1;
            if (index == len) {
                index = 0;
            }
            showPics(index);
        });
        $("#focus ul").css("width", sWidth * (len));
        $("#focus").hover(function () {
            clearInterval(picTimer);
        }, function () {
            picTimer = setInterval(function () {
                showPics(index);
                index++;
                if (index == len) {
                    index = 0;
                }
            }, 2800);
        }).trigger("mouseleave");
        function showPics(index) {
            var nowLeft = -index * sWidth;
            $("#focus ul").stop(true, false).animate({"left": nowLeft}, 300);
            $("#focus .btn span").stop(true, false).animate({"opacity": "0.4"}, 300).eq(index).stop(true, false).animate({"opacity": "1"}, 300);
        }
    },
    initDom: function () {
        if (id != undefined && id != '' && id != 'undefined') {
            Public.ajaxPost(url + "/qryOp.json", {id: id, companyId: companyId}, function (json) {
                if (json.status == 200) {
                    model.data = json.data.customer;
                    if (model.data.head_id == undefined) {
                        model.data.head_id = "";
                        model.data.head_name = "";
                    }
                    Public.ajaxPost(rootPath + "/crm/contacts/mainContacts.json", {customer_id: id}, function (json) {
                        if (json.status == 200) {
                            model.contacts = json.data;
                        }
                    });
                    model.imgArray = json.data.images;
                } else {
                    parent.Public.tips({type: 1, content: json.msg});
                }

            });

            Public.ajaxPost(url + "/qryPic.json", {id: id, companyId: companyId}, function (json) {
                if (json.status == 200) {
                    //model.picList=json.data;

                    var pDiv = '<div class="bannerbox"><div id="focus"><ul>';
                    var len = json.data.length;
                    for (var i = 0; i < len; i++) {

                        pDiv += '<li><img id="'+json.data[i].id+'" style="width: 800px" src="downloadFile?id=' + json.data[i].id + '" alt="" /></li>';
                    }
                    //<a rel="group" href="downloadFile?id=' + json.data[i].id + '" title="Lorem ipsum dolor sit amet"></a>
                    pDiv += "</ul></div></div>";
                    $('#tab5').prepend(pDiv);
                    for (var i = 0; i < len; i++) {
                        $('#'+json.data[i].id).click(function(){
                            parent.$.dialog({
                                title: '查看大图',
                                content: "url:" + rootPath + "/crm/customer/viewPic.html",
                                //data: opt,
                                width: $(window.parent).width() * 0.9,
                                height: $(window.parent).height() * 0.9,
                                max: false,
                                min: false,
                                cache: false,
                                lock: true,
                                data: {
                                    id: this.id
                                }
                            })
                        })
                    }

                    //$("a[rel=group]").fancybox({type:"image",width:900,height:700,autoSize:false,autoResize:true});
                    THISPAGE.initImage();
                } else {
                    parent.Public.tips({type: 1, content: json.msg});
                }
            });

        }
        THISPAGE.initEvent();
    },
    loadGrid: function () {
        function t(val, opt, row) {
            var html_con = '<div class="operating" data-id="' + row.id + '"><span class="fa fa-eye mrb" title="查看"></span></div>';
            return html_con;
        }

        var i = Public.setGrid();
        $("#grid").jqGrid({
            url: rootPath + "/crm/contacts/list.json",
            postData: {customer_id: id},
            datatype: "json",
            mtype: 'POST',
            autowidth: true,
            height: 700,
            rownumbers: true,
            colModel: [{
                name: "operating",
                label: "操作",
                fixed: true,
                hidden: true,
                formatter: t,
                align: "center",
                title: false
            }, {
                name: "is_main",
                label: "首要", width: 80,
                align: "center", formatter: function (v) {
                    var s = ["", "<font color='red'>首要</font>"];
                    return s[v];
                },
                title: true
            }, {
                name: "name",
                label: "姓名",
                align: "center",
                width: 80,
                sortable: true,
                title: true
            }, {
                name: "sex",
                label: "性别",
                align: "center", formatter: function (v) {
                    var sex = ["女", "男"];
                    return sex[v];
                },
                title: true
            }, {
                name: "mobile",
                label: "手机",
                align: "center",
                title: true
            }, {
                name: "telephone",
                label: "电话",
                align: "center",
                sortable: true,
                title: true
            }, {
                name: "qq",
                label: "QQ",
                align: "center",

                sortable: true,
                title: true
            }, {
                name: "email",
                label: "Email",
                align: "center",
                width: 140,
                sortable: true,
                title: false
            }],
            cmTemplate: {
                sortable: false,
                title: false
            },
            viewrecords: true,
            shrinkToFit: false,
            forceFit: false,
            jsonReader: {
                root: "data",
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
                THISPAGE.viewContacts(t);
            }
        });
    },
    initEvent: function () {
        $(window).resize(function () {
            Public.resizeGrid()
        })
    },
    viewContacts: function (id) {
        $.dialog({
            id: "moreCon",
            width: 850,
            height: 600,
            min: false,
            max: false,
            title: "查看联系人",
            resize: true,
            lock: false,
            content: "url:" + rootPath + "/crm/contacts/view",
            data: {
                id: id
            }
        });
    }
};
THISPAGE.init();