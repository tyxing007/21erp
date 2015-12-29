var queryConditions = {
	keyword : "",category:'',status:"",year:"",month:""
},
	year=new Date(SYSTEM.date).getFullYear(),
	url=rootPath+"/scm/average",gridQryUrl=url+"/dataGrid.json",
 THISPAGE = {
	init : function() {
		this.initDom();
		this.loadGrid();
		this.addEvent()
	},
	initDom : function() {
		this.$_keyword = $("#keyword");
		var selid = document.getElementById("year");
		for(var y=year- 1,index = 1;y<year+2;y++,index++){
			//selid.option[index] = ;
			selid.add(new Option(y,y));
		}
	},
	viewdproduct:function(id){
		$.dialog({
			id : "moreCon",
			width : 850,
			height : 400,
			min : true,
			max : true,
			title : "查看商品",
			button : [{name : "关闭"	} ],
			resize : true,lock:true,
			content : "url:"+url+"/view",
			data : {id:id}
		});
	},
	loadGrid : function() {
		function fmtAmt(v) {
			return Public.numToCurrency(v);
		}
		var i = Public.setGrid();
		$("#grid").jqGrid({
			url : gridQryUrl,
			postData : queryConditions,
			datatype : "json",
			mtype:'POST',
			autowidth : true,
			height : i.h,
			altRows : true,
			gridview : true,
			multiselect : true,rownumbers:true,
			multiboxonly : true,
			colModel : [ {
				name : "billsn",
				label : "商品编号",
				align : "center",sortable:true,
				title : true
			}, {
				name : "product_name",
				label : "商品名称",
				align : "center",sortable:true,
				title : true
			}, {
				name : "year",
				label : "年份",
				align : "center",sortable:true,
				title : true
			}, {
				name : "month",
				label : "月份",
				align : "center",sortable:true,
				title : true
			}, {
				name : "day",
				label : "结算日",
				align : "center",
				width:100,
				title : true
			}, {
				//"id" varchar(10) NOT NULL,
				//"product_id" varchar(10),
				//"first_cost" float8,
				//"first_num" float8,
				//"first_money" numeric(10,2),
				//"last_cost" float8,
				//"last_num" float8,
				//"last_money" numeric(10,2),
				//"year" varchar(10),
				//"month" varchar(10),
				//"status" int4,
				//"depot_id" varchar(10),
				//"company_id" varchar(10)

				name : "first_cost",
				label : "期初成本价",
				align : "right",width:120,formatter:fmtAmt,
				sortable:true,
				title : false
			}, {
				name : "first_num",
				label : "期初数量",width:120,formatter:fmtAmt,
				align : "right",
				title : false
			}, {
				name : "first_money",
				label : "期初存货金额",width:120,
				align : "right",
				title : false
			},{
			name : "last_cost",
				label : "本期成本价",
				align : "right",width:120,formatter:fmtAmt,
				sortable:true,
				title : false
		}, {
			name : "last_num",
				label : "本期库存数量",width:120,formatter:fmtAmt,
				align : "right",
				title : false
		}, {
			name : "last_money",
				label : "本期库存结存金额",width:120,
				align : "right",
				title : false
		},{
				name : "depot_name",
				label : "仓库名称",width:120,
				align : "right",
				title : false
			}, {
				name : "status",
				label : "状态",
				align : "center",width:40,sortable:true,
				formatter:function(t,e,i){var status=['<font color="red">待结算</font>','<font color="green">已结算</font>']; return "<a class='disable_product_btn' data-id='"+i.id+"'>"+status[i.status]+"</a>";},
				title : true
			} ],
			cmTemplate : {
				sortable : false,
				title : false
			},
			page : 1,
			sortname : "name",
			sortorder : "desc",
			pager : "#page",
			rowNum : 50,
			rowList : [ 50,100, 200 ],
			viewrecords : true,
			shrinkToFit : false,
			forceFit : false,
			jsonReader : {
				root : "data.list",
				records : "data.totalRow",
				repeatitems : false,
				id : "id"
			},
			loadError : function() {
				parent.Public.tips({
					type : 1,
					content :"加载数据异常！"
				})
			},
			ondblClickRow : function(t) {
				$("#" + t).find(".fa-eye").trigger("click")
			}
		})
	},
	reloadData : function(t) {
		$("#grid").jqGrid("setGridParam", {
			url : gridQryUrl,
			datatype : "json",mtype:'POST',
			postData : t
		}).trigger("reloadGrid");
	},
	addEvent : function() {
		Public.dateCheck();
		var t = this;
		$(".grid-wrap").on("click", ".fa-eye", function(t) {
			t.preventDefault();
			var e = $(this).parent().data("id");
			THISPAGE.viewdproduct(e);
		});
		$(".grid-wrap").on("click", ".fa-edit", function(e) {
			e.preventDefault();
			if (Business.verifyRight("TD_UPDATE")) {
				var t = $(this).parent().data("id");
				handle.operate("edit", t)
			}
		});
		$(".grid-wrap").on("click", ".fa-trash-o", function(t) {
			t.preventDefault();
			if (Business.verifyRight("BU_DELETE")) {
				var e = $(this).parent().data("id");
				handle.del(e);
			}
		});
		$(".grid-wrap").on("click", ".disable_product_btn", function(t) {
			t.preventDefault();
			if (Business.verifyRight("BU_DELETE")) {
				var e = $(this).data("id");
				handle.disable(e);
			}
		});
		$("#search").click(
				function() {
					queryConditions.keyword =t.$_keyword.val();
					queryConditions.status = $('#status').val();
					queryConditions.year = $('#year').val();
					queryConditions.month = $('#month').val();
					THISPAGE.reloadData(queryConditions)
				});
		$("#import").click(
			function() {
				//queryConditions.keyword =t.$_keyword.val();
				//THISPAGE.reloadData(queryConditions)
				$.dialog({
					title: "导入商品资料",
					content: "url:" + url + "/upfile",
					width: 450,
					height: 300,
					max: true,
					min: false,
					cache: false,
					resize: true,
					lock: true
				})

			});
		$("#export").click(
			function() {
				//queryConditions.keyword =t.$_keyword.val();
				//THISPAGE.reloadData(queryConditions)
				alert("该功能优化中，敬请期待");
			});

		$("#add").click(function(t) {
			t.preventDefault();
			if(Business.verifyRight("TF_ADD")){
				handle.operate('add');
			}
		});
		$("#btn-batchDel").click(function(e) {
			e.preventDefault();
			if (Business.verifyRight("BU_DELETE")) {
				var t = $("#grid").jqGrid("getGridParam", "selarrrow");
				t.length ? handle.del(t.join()) : parent.Public.tips({
					type : 2,
					content : "请选择需要删除的项"
				})
			}
		});
		/**类别combo START*/
		var r = $("#category");
		var i=Public.comboTree(r,{url:rootPath+'/sso/parame/tree.json',postData:{type:0},
				callback : {
					beforeClick : function(e, t) {
						r.val(t.name);
						queryConditions.category=t.id;
						i.hide();
					}
				}
			});
		/**类别combo END*/
		$(window).resize(function() {
			Public.resizeGrid()
		})
	}
};
var handle = {
		operate : function(e, t) {
			if ("add" == e)
				var i = "新增商品", r = {oper : e,callback : this.callback};
			else
				var i = "修改商品", r = {oper : e,rowId : t,callback : this.callback};
			$.dialog({
				title : i,
				content : "url:"+url+"/edit",
				data : r,
				width : 850,
				height : 422,
				max :true,resize:true,
				min :false,
				cache :false,
				lock :true
			})
		},
		del : function(id) {
			$.dialog.confirm("删除的商品将不能恢复，请确认是否删除？", function() {
				Public.ajaxPost(url+"/del.json", {
					id : id
				}, function(t) {
					if (t && 200 == t.status) {
						var i = t.data || [];
						parent.Public.tips({
							type : 2,
							content : t.msg
						});
						THISPAGE.reloadData();
					} else
						parent.Public.tips({
							type : 1,
							content : "删除商品失败！" + t.msg
						})
				})
			})
		},
		disable : function(id) {
			$.dialog.confirm("确定要（禁用或激活）商品？", function() {
				Public.ajaxPost(url+"/disable", {id : id},
				function(t) {
					if (t && 200 == t.status) {
						var i = t.data|| [];
						parent.Public.tips({
							type : 2,
							content : t.msg
						});
						THISPAGE.reloadData();
					} else
						parent.Public.tips({
							type : 1,
							content : "操作失败！" + t.msg
						})
				})
			})
		},
		callback : function(e, t, i) {
			var r = $("#grid").data("gridData");
			if (!r) {
				r = {};
				$("#grid").data("gridData", r)
			}
			r[e.id] = e;
			if ("edit" == t) {
				$("#grid").jqGrid("setRowData", e.id, e);
				i && i.api.close()
			} else {
				$("#grid").jqGrid("addRowData", e.id, e, "last");
				i && i.resetForm(e)
			}
		}
	}
THISPAGE.init();