var url = rootPath + "/scm/product";
var api = frameElement.api;
var companyList = SYSTEM.companyList, upload;
var mode = avalon.define({
        $id: 'ctrl',
        data: {
            companyId: ""
        },
        companyList: companyList,
        showMenu: function (code) {
            haveCompany = SYSTEM.rights[code] == true;
            return haveCompany
        },
        changeCompany: function (v) {
            //upload.uploader = url + '/saveFile/' + SYSTEM.user.id + "-" + SYSTEM.user.company_id + "-" + v

            upload = $("#upfile_btn").uploadify({
                height: 30,
                swf: rootPath + '/assets/js/plugins/uploadify/uploadify.swf',
                uploader: url + '/saveFile/' + SYSTEM.user.id + "-" + SYSTEM.user.company_id + "-" + v,
                width: 220,
                buttonText: '选择文件',//浏览文件',
                fileTypeExts: '*.xls;*.xlsx',
                fileTypeDesc: 'Excel文件(*.xls;*.xlsx);',
                successTimeout: 3600,
                fileSizeLimit: 512,
                onUploadSuccess: function (file, data, response) {
                    eval("var json=" + data + ";");
                    if (json.success) {
                        //model.data.id = json.data;
                        THISPAGE.download(url + '/downProductFile', "filename=" + json.data);
                        parent.parent.Public.tips({
                            content: json.msg
                        });
                        parent.THISPAGE.reloadData(null);
                    } else {
                        parent.parent.Public.tips({
                            content: json.msg, type: 1
                        });
                    }
                }
            });
        }
    }
)
var THISPAGE = {
    init: function () {
        this.initDom();
        this.initBtn();
    },
    initDom: function () {
        upload = $("#upfile_btn").uploadify({
            height: 30,
            swf: rootPath + '/assets/js/plugins/uploadify/uploadify.swf',
            uploader: url + '/saveFile/' + SYSTEM.user.id + "-" + SYSTEM.user.company_id,
            width: 220,
            buttonText: '选择文件',//浏览文件',
            fileTypeExts: '*.xls;*.xlsx',
            fileTypeDesc: 'Excel文件(*.xls;*.xlsx);',
            successTimeout: 3600,
            fileSizeLimit: 512,
            onUploadSuccess: function (file, data, response) {
                eval("var json=" + data + ";");
                if (json.success) {
                    //model.data.id = json.data;
                    THISPAGE.download(url + '/downProductFile', "filename=" + json.data);
                    parent.parent.Public.tips({
                        content: json.msg
                    });
                    parent.THISPAGE.reloadData(null);
                    parent.parent.Public.tips({
                        content: json.msg
                    });
                    parent.THISPAGE.reloadData(null);
                } else {
                    parent.parent.Public.tips({
                        content: json.msg, type: 1
                    });
                }
            }
        });

    },
    initBtn: function () {
        api.button({
            id: "cancel",
            name: "关闭"
        })
    },
    download: function (url, data, method) {    // 获得url和data
        if (url && data) {
            // data 是 string 或者 array/object
            data = typeof data == 'string' ? data : jQuery.param(data);        // 把参数组装成 form的  input
            var inputs = '';
            jQuery.each(data.split('&'), function () {
                var pair = this.split('=');
                inputs += '<input type="hidden" name="' + pair[0] + '" value="' + pair[1] + '" />';
            });        // request发送请求
            jQuery('<form action="' + url + '" method="' + (method || 'post') + '">' + inputs + '</form>')
                .appendTo('body').submit().remove();
        }
        ;
    }
};
THISPAGE.init();