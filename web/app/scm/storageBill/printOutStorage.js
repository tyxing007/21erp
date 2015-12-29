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
        companyAddress:"",
        telephone:"",
        fax:"",
        order_sn:'',
        customer_name:'',
        customer_date:'',
        depot:'',
        head_id:'',
        head_phone:'',
        money:'',
        customer_address:'',
        customer_contacts:'',
        customer_contacts_phone:'',
        productList:[{billsn:'',product_name:'',unit:'',amount:'',money:'',amountMoney:'',specification:''}],
        productListAll:[{billsn:'',product_name:'',unit:'',amount:'',money:'',amountMoney:'',specification:''}],
        amount:0,
        sjMoney:0,
        bigMoney:'',
        smallMoney:""
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
    },
    showPBr:function(v){
        //if(model.productPageIndex==-1){
        //    return false;
        //}
        alert($('.getHeight')[model.productPageIndex].height());
    }

});

var THISPAGE = {
    init: function () {
        this.initDom();
    },
    initDom: function () {
        if (id != undefined && id != '' && id != 'undefined') {
            Public.ajaxPost(rootPath + "/scm/storageBill/qryOutPrint.json", {id: id}, function (json) {
                if (json.status == 200) {
                    avalon.mix(model.data, json.data);
                    var len = model.data.productList.length;
                    for(var i=0;i<=len/35;i++){
                        model.productPageSize.push({});
                    }
                    THISPAGE.jisuan();
                } else {
                    parent.Public.tips({type: 1, content: json.msg});
                }
            });
        }
    },
    jisuan:function(){
        var products = model.data.productList;
        var len = products.length;
        for(var i=0;i<len;i++){
            model.data.amount += new Number(products[i].amount);
            model.data.sjMoney += new Number(products[i].amountMoney);
        }
        model.data.bigMoney = model.data.sjMoney;
        model.data.smallMoney = model.data.sjMoney;
        THISPAGE.uedit();
    },
    uedit:function(){
        uE.ready(function() {
            uE.setContent($('.manage-wrap').html());
        });
    }
};

THISPAGE.init();
