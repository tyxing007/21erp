var url=rootPath+"/scm/order",gridQryUrl=url+"/rptList.json";
var model = avalon.define({$id:'view',user:SYSTEM.user,
    query :{start_date:SYSTEM.beginDate,type:type,end_date:SYSTEM.endDate,f:f,m:"",tax:"",t:"",customer_id:'',customer_name:'',product_billsn:'',product_id:'',product_name:'',head_id:"",head_name:""},
    list:[],amount_total:0,amt_total:0,ml_total:0,cost_total:0,nprint:true,
    productList:[],pdctComboV:false,type_:type,
	/**
	 * 已弃用
	 * @param e
	 */
	chooseProduct:function(e){
    	model.query.product_id=e.id;
    	model.query.product_name=e.name;
    	model.pdctComboV=false;
    },
	/**
	 * 已弃用
	 * @param v
	 */
    qryProduct:function(v){//自动完成查询商品
    	model.pdctComboV=true;
    	model.query.product_id='';
    	Public.ajaxPost(rootPath+"/scm/product/dataGrid.json",{keyword:v,_sortField:"name",_sort:"asc"},function(json){
    		model.productList=json.data.list;
    	});
    },
    customerList:[],custComboV:false,


	/**
	 * 已弃用
	 * @param e
	 */
	chooseCust:function(e){
    	model.query.customer_id=e.id;
    	model.query.customer_name=e.name;
    	model.custComboV=false;
    },
	/**
	 * 已弃用
	 * @param v
	 */
    qryCustomer:function(v){//自动完成查询客户
    	model.custComboV=true;
    	model.query.customer_id='';
    	Public.ajaxPost(rootPath+"/crm/customer/dataGrid.json",{keyword:v,type:type==1?0:'',_sortField:"name",_sort:"asc"},function(json){
    		model.customerList=json.data.list;
    	});
    },
    userList:[],userComboV:false,


    chooseUser:function(e){
    	model.query.head_id=e.id;
    	model.query.head_name=e.realname;
    	model.userComboV=false;
    },
    qryUser:function(v){//自动完成查询用户
    	model.userComboV=true;
    	model.query.head_id='';
    	Public.ajaxPost(rootPath+"/sso/user/dataGrid.json",{keyword:v,_sortField:"realname",rows:9999,_sort:"asc"},function(json){
    		model.userList=json.data.list;
    	});
    },
    mCheck:function(){
    	if(model.query.m==1){
    		model.query.m=0;
    	}else{
    		model.query.m=1;
    	}
    },
    tCheck:function(){
    	if(model.query.t==1){
    		model.query.t=0;
    	}else{
    		model.query.t=1;
    	}
    },
    taxCheck:function(){
    	if(model.query.tax==1){
    		model.query.tax=0;
    	}else{
    		model.query.tax=1;
    	}
    },
	init:function(){
		$(".ui-datepicker-input").datepicker();
//		model.loadData();
		model.initBuSugest();
	},
	initBuSugest:function(){
		var barCode = $("#productNameCheck").bsSuggest({
			url: rootPath + "/scm/product/dataGrid.json?_sortField=name&_sort=asc&keyword=",
			effectiveFields: ["billsn", "product_name"],
			effectiveFieldsAlias: {billsn: "一维码", product_name: "产品名称"},
			showHeader: true,
			showBtn: false,
			//idField: "billsn",
			//keyField: "product_name",
			indexKey: 1,
			jsonp: false,
			listAlign: 'left',
			getDataMethod: 'url',
			//multiWord: true,
			//separator: "/",
			processData: function (json) {// url 获取数据时，对数据的处理，作为 getData 的回调函数
				var i, len, data = {value: []};
				if (!json || !json.data.list || json.data.list.length == 0) {
					return false;
				}
				productListBySuggest = json.data.list;
				len = json.data.list.length;
				for (i = 0; i < len; i++) {
					data.value.push({
						"billsn": json.data.list[i].billsn,
						"product_name": json.data.list[i].product_name
					});
				}
				data.defaults = 'test';
				console.log(data);
				return data;
			}
		}).on('onSetSelectValue', function (e, keyword) {
			//console.log('onSetSelectValue: ', keyword);
			$('#barCodeCheck').val(keyword.id);
			$('#productNameCheck').val(keyword.key);
		})

		if(model.query.f==1){
			//rootPath+"/crm/customer/dataGrid.json",{keyword:v,type:type==1?0:'',_sortField:"name",_sort:"asc"
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
		}

		if(model.query.f==2){
			//model.query.head_id=e.id;
			//model.query.head_name=e.realname;
			var customer = $("#head").bsSuggest({
				url: rootPath+"/sso/user/dataGrid.json?type="+rptType+"&_sortField=realname&rows=9999&_sort=asc&keyword=",
				effectiveFields: ["name", 'id',"position_name"],
				effectiveFieldsAlias: {name: "用户名称", 'id': '用户ID','position_name':'用户职位'},
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
							"name": json.data.list[i].realname,
							"id": json.data.list[i].id,
							"position_name":json.data.list[i].position_name
						});
					}
					data.defaults = 'test';
					console.log(data);
					return data;
				}
			}).on('onSetSelectValue', function (e, keyword) {
				//console.log('onSetSelectValue: ', keyword);
				//model.data.customer = keyword.id;
				$('#head').val(keyword.key);
				$('#headId').val(keyword.id);
				//model.chooseCust(keyword);
			});
		}

	},
	loadData:function(){
		Public.ajaxPost(gridQryUrl,model.query.$model, function(json){
			if(json.status==200){
				model.list=json.data;
				model.amount_total=0;
				model.amt_total=0;
				model.ml_total=0;
				model.cost_total=0;
				if(model.list.length>0){
					for(var i=0;i<model.list.length;i++){
						var a=model.list[i];
						if(a.name!=undefined&&a.name=='小计'){
							model.amount_total+=a.amount;
							model.amt_total+=a.amt;
							model.ml_total+=a.ml;
							model.cost_total+=a.cost;
						}
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