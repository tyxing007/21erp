var api = frameElement.api, oper = api.data.oper, id = api.data.id,type = api.data.type,
    width = api.data.width,height = api.data.height,
    billType = ['采购入库', '销售退货入库', '调拨入库', '其它入库', '销售出库', '采购退货出库', '调拨出库', '其它出库'];
var uE = UE.getEditor('container',{
    autoHeightEnabled: true,
    autoFloatEnabled: true,
    scaleEnabled:true,
    initialFrameWidth:width,
    initialFrameHeight:height,
    fullscreen:true
});

var model = avalon.define({
    $id: 'print',
    billType:billType,
    type : type,
    page:false,
    first:0,
    productPageSize:[],
    productPageIndex:-1,
    productPageStart:true,
    data: {
        companyName:'',
        phone:'',
        addree:'',
        head_id:'',
        user_id:'',
        order_id:'',
        customer＿dname:'',
        customer＿name:'',
        customer_phone:'',
        customer_address:'',
        customer_date:'',
        customer_dsr:'',
        customer_remark:'',
        productList: [{unit: "",sale_price:'', product_name: "",product_barcode:'',brand:"",categorychina:"",remark:""}],
        productListAll:[{billsn:'',product_name:'',unit:'',amount:'',money:'',amountMoney:'',specification:''}]
    },
    pageIndex:function(index) {
        model.productPageIndex == index
        return true;
    },
    keys:function(a){
        if(model.productPageIndex==-1){
            return;
        }
        return model.data.productListAll[model.productPageIndex];
    }

});

var THISPAGE = {
    init: function () {
        this.initDom();
    },
    initDom: function () {
        if (id != undefined && id != '' && id != 'undefined') {
            Public.ajaxPost(rootPath + "/scm/storageBill/qryPrint.json", {id: id,orderBy:"brand"}, function (json) {
                if (json.status == 200) {
                    avalon.mix(model.data, json.data);
                    var len = model.data.productList.length;
                    for(var i=0;i<=len/31;i++){
                        model.productPageSize.push({});
                    }
                    uE.ready(function() {
                        uE.setContent($('.manage-wrap').html());
                    });
                } else {
                    parent.Public.tips({type: 1, content: json.msg});
                }
            });
        }
    }

};

THISPAGE.init();
