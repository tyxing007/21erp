package net.loyin.ctrl.app;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import net.loyin.ctrl.AdminBaseController;
import net.loyin.jfinal.anatation.RouteBind;
import net.loyin.model.crm.Parame;
import net.loyin.model.scm.OrderProduct;
import net.loyin.model.scm.Product;
import net.loyin.model.scm.Stock;
import net.loyin.util.BaseTurn;
import net.loyin.util.CS;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Name;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by EYKJ on 2015/9/17.
 */
@RouteBind(path = "merch")
public class MerchCtrl extends AdminBaseController<Product> {
    private String modelStr = "product";


    public MerchCtrl() {
        this.modelClass = Product.class;
    }

    /**
     * 查询产品，包括历史产品
     */
    public void merchList() {
        Map<String, String[]> map = this.getRequest().getParameterMap();
        String pagesize = map.get(CS.PAGE_SIZE)[0];
        String pageStr = map.get(CS.PAGE)[0];
        String retls_id = map.containsKey(CS.RETLS_ID) ? map.get(CS.RETLS_ID)[0] : "";
        String slo_id = map.containsKey(CS.SLO_ID) ? map.get(CS.SLO_ID)[0] : "";
        String model = map.containsKey(CS.MODEL) ? map.get(CS.MODEL)[0] : "";
        String merchType = map.containsKey(CS.MERCH_TYPE) ? map.get(CS.MERCH_TYPE)[0] : "";
        String searchBrand = map.containsKey(CS.MERCH_BRAND) ? map.get(CS.MERCH_BRAND)[0] : "";
        String searchStander = map.containsKey(CS.MERCH_STANDER) ? map.get(CS.MERCH_STANDER)[0] : "";
        String searchContent = map.containsKey(CS.SEARCH_CONTENT) ? map.get(CS.SEARCH_CONTENT)[0] : "";
        List<Map<String, Object>> temp = new ArrayList<>();/**用来提交失败时的错误空数据*/
        List<Map<String, Object>> resultList = new ArrayList<>();
        if (!StringUtils.isNotBlank(pageStr)) {
            this.rendJsonApp(false, temp, 500, "页码参数缺失");
            return;
        }
        if (!StringUtils.isNotBlank(pagesize)) {
            this.rendJsonApp(false, temp, 500, "每页大小缺失");
            return;
        }

        Map<String, Object> filter = new HashMap<>();
        filter.put("merchType", merchType);
        filter.put("searchBrand", searchBrand);
        filter.put("searchStander", searchStander);
        filter.put("retls_id", retls_id);
        filter.put("company_id", this.getCompanyId());
        filter.put("keyword", searchContent);
        filter.put("slo_id", slo_id);
        Page<Product> productList = null;
        if (model.equals(CS.model_HISTORY)) {
            productList = Product.dao.pageGridHis(Integer.parseInt(pageStr), Integer.parseInt(pagesize), filter);
        } else {
            productList = Product.dao.pageGrid(Integer.parseInt(pageStr), Integer.parseInt(pagesize), filter);
        }
        /*------------------------------*/
        /*获取所有的库存信息*/
        List<Stock> stocks = Stock.dao.findAll();
        /*------------------------------*/
        Map<String, Object> resultMap = null;
        List<Product> list = productList.getList();
        List<OrderProduct> products = new ArrayList<>();
        if (!StringUtils.isBlank(slo_id)) {
            products = OrderProduct.dao.list(slo_id);
        }
        for (Product product : list) {
            resultMap = new HashMap<>();
            String[] names = product.getAttrNames();
            for (int i = 0; i < names.length; i++) {
                resultMap.put(names[i], product.get(names[i]));
            }
            for (OrderProduct orderProduct : products) {
                if (product.get("id").toString().equals(orderProduct.get("product_id").toString())) {
                    resultMap.put("b", 1);
                }
            }
            /*循环查出来的库存 有的话就取出库存量*/
            for (Stock stock : stocks) {
                if (stock.get("product_id").equals(product.get("id"))) {
                    resultMap.put("merch_num", stock.get("sum"));
                    break;
                }
            }
            /*都没有就直接赋值为 0 */
            if (resultMap.get("merch_num") == null || resultMap.get("merch_num").equals("") || Double.parseDouble(String.valueOf(resultMap.get("merch_num"))) <= 0) {
                resultMap.put("merch_num", "0");
            }

            resultList.add(resultMap);
        }
        resultList = BaseTurn.ResponseHandle(resultList, modelStr);

        String category_name = "";
        Parame parame = null;
        for (int i = 0; i < resultList.size(); i++) {
            parame = Parame.dao.findById(resultList.get(i).get("merch_type"));
            if (parame != null) {
                category_name = parame.get("name");
                resultList.get(i).replace("merch_type", category_name);
               /* resultList.get(i).put("category_name", category_name);*/
            }
        }
        if (resultList.size() != 0) {
            this.rendJsonApp(true, resultList, 200, productList.getTotalPage() > 1 ? CS.ismore_TRUE : CS.ismore_FALSE);
            return;
        }


        this.rendJsonApp(false, resultList, 404, CS.ismore_FALSE);
    }


    /**
     * 原APP接口，整合方法
     */
    public void merchListLocalPrice() {
        this.merchList();
    }

    /**
     * 搜索商品-有历史价格请求地址
     */
    public void searchMerchWithLocalPrice() {
        this.merchList();
    }

    /**
     * 搜索商品请求地址
     */
    public void searchMerch() {
        this.merchList();
    }


    /**
     * by chenjianhui
     * 2015年9月23日11:23:37
     * 获取商品全部分类
     */
    public void merchTypeMainList() {


        Map<String, Object> map = new HashMap<>();
        Map<String, Object> temp = new HashMap<>();
        String searchContent = this.getPara("keyword");
        String searchBrand = this.getPara("brand");

        map.put("searchBrand", searchBrand);
        map.put("company_id", this.getCompanyId());
        map.put("keyword", searchContent);
        Page<Product> page = Product.dao.pageGrid(1, 9999, map);
        List<Record> parameList = new ArrayList<>();
        if (page.getList().size() == 0) {
            Record record = new Record();
            record.set("merch_brand", "");
            parameList.add(0, record);
            this.rendJsonApp(true, null, 500, "sueecss");
            return;
        }
        List<Product> products = page.getList();
        List<String> productIds = new ArrayList<>();
        List<Map<String, Object>> maps = new ArrayList<>();
        for (Product p : products) {
            boolean isHave = false;
            for (String s : productIds) {
                if (s == p.getStr("product_id")) {
                    isHave = true;
                }
            }
            if (!isHave) {
                productIds.add(p.getStr("product_id"));
            }

        }

        List<Record> records = net.loyin.model.sso.Parame.dao.qryListInProduct(this.getCompanyId(), this.getParaToInt("type", 0), productIds.toArray(new String[productIds.size()]));
        for (Record record : records) {
            temp = null;
            temp = record.getColumns();
            temp.put("merchtype_name", record.get("name"));
            maps.add(record.getColumns());
        }
        this.rendJsonApp(true, maps, 200, "success");
















        /*List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        String company_id = getPara("com_id");

        List<Parame> parames = Parame.dao.getAllOfGoodsType(company_id);

        for (Parame parame : parames) {
            map = parame.getAttrs();
            *//**将name值存到APP需要用到的字段里面*//*
            map.put("merchtype_name", map.get("name"));
            list.add(map);
        }

        this.rendJsonApp(true, list, 200, "success");*/
    }

    /**
     * by chenjianhui
     * 2015年9月23日14:29:28
     * 获取商品全部品牌
     */
    public void merchBrandMainList() {
        List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();

        Map<String, String> map = new HashMap<>();
        String searchContent = this.getPara("keyword");
        String merchType = this.getPara("merch_type");
        String merchStand = this.getPara("specification");
        String merchBrand = this.getPara("merch_brand");
        map.put("searchContent", searchContent);
        map.put("merchType", merchType);
        map.put("merchStand", merchStand);
        map.put("merchBrand", merchBrand);
        List<Record> parameList = net.loyin.model.sso.Parame.dao.qryListProductInfoByOrder(this.getCompanyId(), true, false, map);
        Record record = new Record();
        record.set("merch_brand", "");
        parameList.add(0, record);
        for (Record r : parameList) {
            maps.add(r.getColumns());
        }
        this.rendJsonApp(true, maps, 200, "success");

       /* List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        String company_id = getPara("com_id");
        String category = getPara("merch_type");

        List<Product> products = Product.dao.getBrand(company_id, category);

        for (Product product : products) {
            map = product.getAttrs();
            *//**将name值存到APP需要用到的字段里面*//*
            map.put("merch_brand", map.get("brand"));
            list.add(map);
        }

        this.rendJsonApp(true, list, 200, "success");*/
    }


    /**
     * 获取规格分类信息
     */
    public void merchStanderMainList() {
        List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
        Map<String, String> map = new HashMap<>();
        String searchContent = this.getPara("keyword");
        String merchType = this.getPara("merch_type");
        String searchBrand = this.getPara("merch_brand");
        String merchStander = this.getPara("merch_stander");
        map.put("searchContent", searchContent);
        map.put("merchType", merchType);
        map.put("searchBrand", searchBrand);
        map.put("merchStander", merchStander);
        List<Record> parameList = net.loyin.model.sso.Parame.dao.qryListProductInfoByOrder(this.getCompanyId(), false, false, map);
        Record record = new Record();
        record.set("merch_stander", "");
        parameList.add(0, record);

        for (Record r : parameList) {
            maps.add(r.getColumns());
        }
        this.rendJsonApp(true, maps, 200, "success");


    }


}