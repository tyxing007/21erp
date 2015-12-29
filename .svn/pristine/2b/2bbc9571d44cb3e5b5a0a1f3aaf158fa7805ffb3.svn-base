var api = frameElement.api, id = api.data.id,url = rootPath + "/crm/customer";
var THISPAGE = {
    init: function () {
        this.initDom();
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
                    //model.picList=json.data;

                    var pDiv = '<div class="bannerbox"><div id="focus" align="center"><ul>';
                    //var len = json.data.length;
                    //for (var i = 0; i < len; i++) {
                        pDiv += '<li><img style="text-align:center" src="downloadFile?id=' + id + '" alt="" /></li>';
                    //}
                    //<a rel="group" href="downloadFile?id=' + json.data[i].id + '" title="Lorem ipsum dolor sit amet"></a>
                    pDiv += "</ul></div></div>";
                    $('#pic').prepend(pDiv);
                    //$("a[rel=group]").fancybox({type:"image",width:900,height:700,autoSize:false,autoResize:true});
                    //THISPAGE.initImage();

        }
    }
};
THISPAGE.init();