var url=rootPath+"/fa/payReceivAbles",gridQryUrl=url+"/wlqkList.json",csttype=["供应商","个人客户","企业客户","经销商"];
var model = avalon.define({$id:'view',
	user:SYSTEM.user,
    query :{keyword:"",start_date:SYSTEM.beginDate,type:type,end_date:SYSTEM.endDate,csttype:'',customer_id:'',customer_name:""},
    list:[],amt0_t:0,amt1_t:0,nprint:true,csttypeList:csttype,
    customerList:[],custComboV:false,
	chooseCust:function(e){
    	model.query.customer_id=e.id;
    	model.custComboV=false;
    },
    qryCustomer:function(v){//自动完成查询客户
    	model.custComboV=true;
    	Public.ajaxPost(rootPath+"/crm/customer/dataGrid.json",{keyword:v,_sortField:"name",_sort:"asc"},function(json){
    		model.customerList=json.data.list;
    	});
    },
	init:function(){
		$(".ui-datepicker-input").datepicker();
		model.loadData();
		model.initBsSuggest();
	},
	initBsSuggest:function(){
		var rptType = type==1?0:'';
		var customer = $("#customer").bsSuggest({
			url: rootPath+"/crm/customer/dataGrid.json?type="+rptType+"&_sortField=name&_sort=asc&keyword=",
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
	},
	loadData:function(){
		Public.ajaxPost(gridQryUrl,model.query.$model, function(json){
			if(json.status==200){
				model.list=json.data;
				if(model.list.length>0){
					model.amt0_t=0;
					model.amt1_t=0;
					for(var i=0;i<model.list.length;i++){
						var a=model.list[i];
						model.amt0_t+=new Number(a.amt0);
						model.amt1_t+=new Number(a.amt1);
					}
				}
			}
		});
	},
	printRpt:function(){
		model.nprint=false;
		window.print();
		model.nprint=true;
	}
});
model.init();
avalon.filters.ordertype=function(v){
	return ordertype[v];
}
avalon.filters.csttype=function(v){
	return csttype[v];
}