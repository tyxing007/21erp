var api = frameElement.api, id = api.data.id, parame = {status: ["禁用", "激活"]};
var model = avalon.define({
    $id: 'view', data: {}, comparison: [], tabActive: 0,
    showTab: function (i, b) {
        model.tabActive = i;
    }
});
avalon.filters.status = function (str) {
    return parame.status[str];
}
avalon.filters.money = function (str) {
    return Public.numToCurrency(str);
}
var THISPAGE = {
    init: function () {
        if (id != '')
            Public.ajaxPost(rootPath + "/scm/product/qryOp.json", {id: id}, function (json) {
                if (json.status == 200) {
                    model.data = json.data
                    Public.ajaxPost(rootPath + "/scm/product/qryComparison.json", {productId: id}, function (json) {
                        if (json.status == 200) {
                            //avalon.mix(model.comparison, json.data);
                            model.comparison = json.data;
                        } else {
                            parent.Public.tips({type: 1, content: json.msg});
                        }
                    });
                } else {
                    parent.Public.tips({type: 1, content: json.msg});
                }
            })
    }
};
THISPAGE.init();