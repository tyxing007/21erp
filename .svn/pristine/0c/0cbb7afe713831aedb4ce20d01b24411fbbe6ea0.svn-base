var url=rootPath+"/fa/payReceivAbles",gridQryUrl=url+"/rptSumList.json",csttype=["供应商","个人客户","企业客户","经销商"],
ordertype=["采购","采购退货","销售","销售退货"];
var model = avalon.define({$id:'view',user:SYSTEM.user,
    query :{keyword:"",//start_date:SYSTEM.beginDate,end_date:SYSTEM.endDate,
    	type:type,custtype:'',status:'',customer_name:'',customer_id:''},
    list:[],userData:{amt0:0,amt1:0,amt2:0,amt3:0,amt4:0,amt5:0},nprint:true,
    customerList:[],custComboV:false,type_:type,
    setTyp:function(v){
    	model.typ=v;
    },
	/**
	 * 棄用
	 * @param e
	 */
	chooseCust:function(e){
    	model.query.customer_id=e.id;
    	model.query.customer_name=e.name;
    	model.custComboV=false;
    },
	/**
	 * 棄用
	 * @param v
	 */
    qryCustomer:function(v){//自动完成查询客户
    	model.custComboV=true;
    	Public.ajaxPost(rootPath+"/crm/customer/dataGrid.json",{keyword:v,type:type==0?0:'',_sortField:"name",_sort:"asc"},function(json){
    		model.customerList=json.data.list;
    	});
    },
	init:function(){
		$(".ui-datepicker-input").datepicker();
		model.loadData();
		model.initBsSuggest();
	},
	loadData:function(){
		Public.ajaxPost(gridQryUrl,model.query.$model, function(json){
			if(json.status==200){
				model.list=json.data;
				model.userData.amt0=0;
				model.userData.amt1=0;
				model.userData.amt2=0;
				for(var i=0;i<model.list.length;i++){
					var o=model.list[i];
					model.userData.amt0+=o.amt;
					model.userData.amt1+=o.pay_amt;
					model.userData.amt2+=o.amt-o.pay_amt;
				}
			}
		});
	},
	printRpt:function(){
		model.nprint=false;
		window.print();
		model.nprint=true;
	},
	initBsSuggest:function(){
		var rptSumType = type==0?0:'';
		var customer = $("#customer").bsSuggest({
			url: rootPath+"/crm/customer/dataGrid.json?type="+rptSumType+"&_sortField=name&qryType=-2&_sort=asc&keyword=",
			effectiveFields: ["name", 'id'],
			effectiveFieldsAlias: {name: "客户名称", 'id': '客户ID'},
			showHeader: true,
			showBtn: false,
			//idField: "billsn",
			//keyField: "product_name",
			indexKey: 0,
			indexId: 1,
			jsonp: false,
			listAlign: 'left',
			getDataMethod: 'url',
			processData: function (json) {// url 获取数据时，对数据的处理，作为 getData 的回调函数
				var i, len, data = {value: []};
				if (!json || !json.data.list || json.data.list.length == 0) {
					return false;
				}
				//model.customerList = json.data;
				len = json.data.list.length;
				for (i = 0; i < len; i++) {
					data.value.push({
						"name": json.data.list[i].name,
						"id": json.data.list[i].id
					});
				}
				data.defaults = 'test';
				console.log(data);
				return data;
			}
		}).on('onSetSelectValue', function (e, keyword) {
			//console.log('onSetSelectValue: ', keyword);
			//model.data.customer = keyword.id;
			$('#customer').val(keyword.key);
			$('#snCheck').val(keyword.id);
			//model.chooseCust(keyword);
		});
	}
});
model.init();
avalon.filters.ordertype=function(v){
	return ordertype[v];
}
avalon.filters.csttype=function(v){
	return csttype[v];
}