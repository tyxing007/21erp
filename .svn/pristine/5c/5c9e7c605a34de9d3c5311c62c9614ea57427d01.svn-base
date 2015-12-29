var url = rootPath + "/crm/customer", gridQryUrl = url + "/dataGrid.json", qryData = url + "/getMapData.json", initMapUrl = url + "/dataMap.json", type = ["供应商", "企业", "个人"];


/*map 高德地图主控件*/
var map;


var model = avalon.define({
    $id: 'view',
    query: {
        keyword: "", start_date: '', end_date: '', type: -1, qryType: -1, status: 0, is_deleted: 0, uid: ""
    },
    expoutUrl: "",
    fastQryText: "快速查询",
    fastQry: [
        {text: "我创建的", sl: false},
        {text: "我负责的", sl: false},
        {text: "下属创建的", sl: false},
        {text: "下属负责的", sl: false},
        {text: "", sl: true},
        {text: "一周未跟进", sl: false},
        {text: "半月未跟进", sl: false},
        {text: "一直未跟进", sl: false},
        {text: "", sl: true},
        {text: "已购买", sl: false},
        {text: "未购买", sl: false}],
    showMenu: function (code) {
        return SYSTEM.rights[code] == true;
    },
    qry: function (type) {
        model.query.qryType = type;
        if (type == 100) {
            model.query.is_deleted = 1;
            model.fastQryText = "回收站";
        } else {
            model.fastQryText = model.fastQry[type].text;
            model.query.is_deleted = 0;
        }
        THISPAGE.reloadData();
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
        this.initMap();
        this.loadMapData();
        this.addEvent()
    },
    loadMapData: function () {
        Public.ajaxPost(initMapUrl, null, function (json_) {
            THISPAGE.addMarker(json_);

        });

    },
    initMap: function () {
        var latitude = 118.068175;
        var longitude = 24.44464;
        var position = new AMap.LngLat(latitude, longitude);
        map = new AMap.Map("mapContainer", {
            view: new AMap.View2D({
                center: position,
                zoom: 10
            }),
            lang: "zh_cn"
        });
        /*增加工具栏*/
        map.plugin(['AMap.ToolBar'], function () {
            var toolBar = new AMap.ToolBar();
            map.addControl(toolBar);
        });

        /*添加3D视图切换*/
        map.plugin(["AMap.MapType"], function () {
            var type = new AMap.MapType({
                defaultType: 0,
                showRoad: true
            });
            map.addControl(type);
        });
        /*移动地图的时候显示当前地图的市*/
        map.on('moveend', function () {
            map.getCity(function (data) {
                if (data['province'] && typeof data['province'] === 'string') {
                    document.getElementById('info').innerHTML = '城市：' + (data['city'] || data['province']);
                }
            });
        });
        /*加载比例尺插件*/
        map.plugin(["AMap.Scale"], function () {
            scale = new AMap.Scale();
            map.addControl(scale);
        });
        /*近距离拉近后显示3D视图*/
        if (document.createElement('canvas') && document.createElement('canvas').getContext && document.createElement('canvas').getContext('2d')) {
            var buildings = new AMap.Buildings();
            buildings.setMap(map);
        }
        /*添加右键的菜单*/
        //创建右键菜单
        var contextMenu = new AMap.ContextMenu();
        contextMenu.addItem("放大一级", function () {
            map.zoomIn();
        }, 0);
        contextMenu.addItem("缩小一级", function () {
            map.zoomOut();
        }, 1);
        contextMenu.addItem("缩放至全国范围", function (e) {
            map.setZoomAndCenter(4, [108.946609, 34.262324]);
        }, 4);
        map.on('rightclick', function (e) {
            contextMenu.open(map, e.lnglat);
            contextMenuPositon = e.lnglat;
        });


    },
    initDom: function () {
        $(".ui-datepicker-input").datepicker();
        /**负责人*/
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
        /**负责人combo END*/
    },
    addEvent: function () {
        var t = this;

        $("#search").click(function () {
            THISPAGE.reloadData()
        });


        $("#refresh").click(function () {
            model.init();
            THISPAGE.reloadData()
        });

        $(window).resize(function () {
            Public.resizeGrid()
        })
    },
    reloadData: function () {
        Public.ajaxPost(qryData, model.query.$model, function (json_) {
            THISPAGE.addMarker(json_);
        });

    },
    addMarker: function (json) {
        /*清空所有的点标记*/
        map.clearMap();


        for (var data in json.data) {
            if (json.data[data].lng == "" || json.data[data].lng == undefined || json.data[data].lng == "undefined" || json.data[data].lat == "" || json.data[data].lat == undefined || json.data[data].lat == "undefined")
                continue;
            //lnglatXY = new AMap.LngLat(json.data[data].lng, json.data[data].lat);
            var marker = new AMap.Marker({
                map: map,
                icon: "http://webapi.amap.com/images/marker_sprite.png",
                position: [json.data[data].lat, json.data[data].lng],
                title: json.data[data].name
            });
            var info = [];
            info.push('<b>' + json.data[data].name + '</b>');
            info.push('  电话 :' + json.data[data].telephone);
            info.push('  地址 :' + json.data[data].address);

            var inforWindow = new AMap.InfoWindow({

                closeWhenClickMap: true,
                showShadow: true,
                offset: new AMap.Pixel(5, -30),
                content: info.join("<br/>")
            });

            marker.inforWin = inforWindow;

            AMap.event.addListener(marker, 'click', function (e) {
                this.inforWin.open(map, this.getPosition());
            });


        }
        /*自适应*/
        var newCenter = map.setFitView();
    }
};
THISPAGE.init();