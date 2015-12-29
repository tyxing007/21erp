var api = frameElement.api, oper = api.data.oper, id = api.data.id, url = rootPath + "/crm/concatRecord";
var model = avalon.define({
    $id: 'view',
    data: {},
    tabActive: 0,
    picList: [],
    showTab: function (i, b) {
        model.tabActive = i;
    }
});
avalon.filters.rating = function (v) {
    return custRating[v].name;
}

var THISPAGE = {
    init: function () {
        this.initDom();
        this.initBtn();
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
            Public.ajaxPost(url + "/qryOp.json", {id: id}, function (json) {
                if (json.status == 200) {
                    model.data = json.data;
                    if (model.data.head_id == undefined) {
                        model.data.head_id = "";
                        model.data.head_name = "";
                    }
                } else {
                    parent.Public.tips({type: 1, content: json.msg});
                }
            });
            Public.ajaxPost(url + "/qryPic.json", {id: id}, function (json) {
                if (json.status == 200) {
                    //model.picList=json.data;

                var pDiv ='<div class="bannerbox"><div id="focus"><ul>';
                    var len = json.data.length;
                    for (var i = 0; i < len; i++) {
                       pDiv += '<li><img id="'+json.data[i].id+'" style="width: 800px" src="downloadFile?id='+json.data[i].id+'" alt="" /></li>';
                    }
                pDiv += "</ul></div></div>";
                    $('#tab2').prepend(pDiv);
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
                    THISPAGE.initImage();
                } else {
                    parent.Public.tips({type: 1, content: json.msg});
                }
            });
        }
    },
    initBtn: function () {
        api.button({
            id: "cancel",
            name: "关闭"
        })
    }
};

THISPAGE.init();