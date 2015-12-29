package net.loyin.model.scm;

import java.util.*;

import net.loyin.jfinal.anatation.TableBind;


import net.loyin.model.sso.Parame;
import net.loyin.util.CS;
import org.apache.commons.lang3.StringUtils;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;

/**
 * 产品
 *
 * @author liugf 风行工作室
 */
@TableBind(name = "scm_product")
public class Product extends Model<Product> {
    private static final long serialVersionUID = -5465375747846490039L;
    public static final String tableName = "scm_product";
    public static Product dao = new Product();


    public Map<String, Object> getAttrs() {
        return super.getAttrs();
    }

    /**
     * 分页查询
     *
     * @param pageNo
     * @param pageSize
     * @param filter   参数
     * @return
     */
    public Page<Product> pageGrid(Integer pageNo, Integer pageSize, Map<String, Object> filter) {
        List<Object> parame = new ArrayList<Object>();
        StringBuffer sql = new StringBuffer(" from ");
        sql.append(tableName);
        sql.append(" t");
//        sql.append(" left join ");
//        sql.append(ProductHis.tableName);
//        sql.append(" h");
//        sql.append(" on t.id = h.product_id");
        sql.append(" where t.company_id=? ");
        parame.add(filter.get("company_id"));
        String keyword = (String) filter.get("keyword");
        if (StringUtils.isNotEmpty(keyword)) {
            String[] keyWord = keyword.split(" ");
            for(String s : keyWord){
                sql.append(" and (t.billsn like ? or t.name like ?)");
                s = "%" + s + "%";
                parame.add(s);
                parame.add(s);
            }
        }
        if (filter.containsKey("category") && filter.get("category") != null && StringUtils.isNotBlank(filter.get("category").toString())) {
            String category = (String) filter.get("category");
            if (StringUtils.isNotEmpty(category)) {
                sql.append(" and t.category in (");
                List<String> parames = Parame.dao.getCategoryIdById(category);
                for (String s : parames) {
                    sql.append("?,");
                    parame.add(s);
                }
                sql.append("'-')");
            }
        }

        if (filter.containsKey("merchType")) {
            String merchType = (String) filter.get("merchType");
            if (StringUtils.isNotEmpty(merchType)) {
                sql.append(" and t.category = ?");
                parame.add(merchType);
            }
        }

        if (filter.containsKey("searchBrand") && filter.get("searchBrand") != null && StringUtils.isNotBlank(filter.get("searchBrand").toString())) {
            String searchBrand = (String) filter.get("searchBrand");
            if (StringUtils.isNotEmpty(searchBrand)) {
                sql.append(" and t.brand = ?");
                parame.add(searchBrand);
            }
        }

        if (filter.containsKey("specification") && filter.get("specification") != null && StringUtils.isNotBlank(filter.get("specification").toString())) {
            String specification = (String) filter.get("specification");
            if (StringUtils.isNotEmpty(specification)) {
                sql.append(" and t.specification = ?");
                parame.add(specification);
            }
        }


//        customer_id

        if (filter.containsKey("searchStander")) {
            String searchStander = (String) filter.get("searchStander");
            if (StringUtils.isNotEmpty(searchStander)) {
                sql.append(" and lower(t.specification) = ?");
                parame.add(searchStander);
            }
        }

        String sortField = (String) filter.get("_sortField");
        if (StringUtils.isNotEmpty(sortField)) {
            sql.append(" order by ");
            sql.append(sortField);
            sql.append(" ");
            sql.append((String) filter.get("_sort"));
        }

        Page<Product> page = dao.paginate(pageNo, pageSize, "select t.id,t.id product_id,t.name,t.name product_name,t.unit,t.stock_warn,t.billsn,t.status,t.model,t.sale_price,t.purchase_price,t.category,t.product_type,t.brand,t.specification",
                sql.toString(), parame.toArray());

        if (filter.containsKey("customer_id") && filter.get("customer_id") != null && StringUtils.isNotBlank(filter.get("customer_id").toString())) {
            List<Product> list = page.getList();
//            h.his_price,h.his_purchase_price,
            if(list.size() == 0){
                return page;
            }
            String[] products = new String[list.size()];
            int i = 0;
            for (Product product : list) {
                products[i] = product.get("product_id");
                i++;
            }
            List<ProductHis> productHis = ProductHis.dao.getProductHisBySomeId(filter.get("company_id").toString(), products, filter.get("customer_id").toString());
            for (Product product : list) {
                for (ProductHis p : productHis) {
                    if (p.getStr("product_id").equals(product.getStr("product_id"))) {
                        product.put("his_price", p.getStr("his_price"));
                        product.put("his_purchase_price", p.getStr("his_purchase_price"));
                    }
                }
            }
        }


        return page;
    }



    /**
     * 根据参数查询产品（无分页）
     * @param filter   参数
     * @return
     */
    public List<Product> getProductByFilter(Map<String, Object> filter) {
        List<Object> parame = new ArrayList<Object>();
        StringBuffer sql = new StringBuffer(" select t.id,t.id product_id,t.name,t.name product_name,t.unit,t.stock_warn,t.billsn,t.status,t.model,t.sale_price,t.purchase_price,t.category,t.product_type,t.brand,t.specification from ");
        sql.append(tableName);
        sql.append(" t");
//        sql.append(" left join ");
//        sql.append(ProductHis.tableName);
//        sql.append(" h");
//        sql.append(" on t.id = h.product_id");
        sql.append(" where t.company_id=? ");
        parame.add(filter.get("company_id"));
        String keyword = (String) filter.get("keyword");
        if (StringUtils.isNotEmpty(keyword)) {
            sql.append(" and (t.billsn like ? or t.name like ?)");
            keyword = "%" + keyword + "%";
            parame.add(keyword);
            parame.add(keyword);
        }
        if (filter.containsKey("category") && filter.get("category") != null && StringUtils.isNotBlank(filter.get("category").toString())) {
            String category = (String) filter.get("category");
            if (StringUtils.isNotEmpty(category)) {
                sql.append(" and t.category in (");
                List<String> parames = Parame.dao.getCategoryIdById(category);
                for (String s : parames) {
                    sql.append("?,");
                    parame.add(s);
                }
                sql.append("'-')");
            }
        }

        if (filter.containsKey("merchType")) {
            String merchType = (String) filter.get("merchType");
            if (StringUtils.isNotEmpty(merchType)) {
                sql.append(" and t.category = ?");
                parame.add(merchType);
            }
        }

        if (filter.containsKey("searchBrand") && filter.get("searchBrand") != null && StringUtils.isNotBlank(filter.get("searchBrand").toString())) {
            String searchBrand = (String) filter.get("searchBrand");
            if (StringUtils.isNotEmpty(searchBrand)) {
                sql.append(" and t.brand = ?");
                parame.add(searchBrand);
            }
        }

        if (filter.containsKey("specification") && filter.get("specification") != null && StringUtils.isNotBlank(filter.get("specification").toString())) {
            String specification = (String) filter.get("specification");
            if (StringUtils.isNotEmpty(specification)) {
                sql.append(" and t.specification = ?");
                parame.add(specification);
            }
        }


//        customer_id

        if (filter.containsKey("searchStander")) {
            String searchStander = (String) filter.get("searchStander");
            if (StringUtils.isNotEmpty(searchStander)) {
                sql.append(" and lower(t.specification) = ?");
                parame.add(searchStander);
            }
        }

        String sortField = (String) filter.get("_sortField");
        if (StringUtils.isNotEmpty(sortField)) {
            sql.append(" order by ");
            sql.append(sortField);
            sql.append(" ");
            sql.append((String) filter.get("_sort"));
        }

        List<Product> productList = dao.find(sql.toString(), parame.toArray());

        if (filter.containsKey("customer_id") && filter.get("customer_id") != null && StringUtils.isNotBlank(filter.get("customer_id").toString())) {
            List<Product> list = productList;
//            h.his_price,h.his_purchase_price,
            String[] products = new String[list.size()];
            int i = 0;
            for (Product product : list) {
                products[i] = product.get("product_id");
                i++;
            }
            List<ProductHis> productHis = ProductHis.dao.getProductHisBySomeId(filter.get("company_id").toString(), products, filter.get("customer_id").toString());

            for (Product product : list) {
                for (ProductHis p : productHis) {
                    if (p.getStr("product_id").equals(product.getStr("product_id"))) {
                        product.put("his_price", p.getStr("his_price"));
                        product.put("his_purchase_price", p.getStr("his_purchase_price"));
                    }
                }
            }
        }


        return productList;
    }

    /**
     * 分页查询(曾经下单产品)
     *
     * @param pageNo
     * @param pageSize
     * @param filter   参数
     * @return
     */
    public Page<Product> pageGridHis(Integer pageNo, Integer pageSize, Map<String, Object> filter) {
        List<Object> parame = new ArrayList<Object>();
        StringBuffer sql = new StringBuffer(" from ");
        sql.append(tableName);
        sql.append(" t");
        /*sql.append(" left join ");
        sql.append(ProductHis.tableName);
        sql.append(" h");
        sql.append(" on t.id = h.product_id");*/
        sql.append(" left join ");
        sql.append(OrderProduct.tableName);
        sql.append(" op");
        sql.append(" on op.product_id = t.id");
        sql.append(" left join ");
        sql.append(Order.tableName);
        sql.append(" o");
        sql.append(" on op.id = o.id");
        sql.append(" where t.company_id=? ");
        parame.add(filter.get("company_id"));
        sql.append(" and o.company_id=? ");
        parame.add(filter.get("company_id"));
        String keyword = (String) filter.get("keyword");
        if (StringUtils.isNotEmpty(keyword)) {
            sql.append(" and (t.billsn like ? or t.name like ?)");
            keyword = "%" + keyword + "%";
            parame.add(keyword);
            parame.add(keyword);
        }
        if (filter.containsKey("category")) {
            String category = (String) filter.get("category");
            if (StringUtils.isNotEmpty(category)) {
                sql.append(" and t.category = ?");
                parame.add(category);
            }
        }

        if (filter.containsKey("merchType")) {
            String merchType = (String) filter.get("merchType");
            if (StringUtils.isNotEmpty(merchType)) {
                sql.append(" and t.category = ?");
                parame.add(merchType);
            }
        }

        if (filter.containsKey("searchBrand")) {
            String searchBrand = (String) filter.get("searchBrand");
            if (StringUtils.isNotEmpty(searchBrand)) {
                sql.append(" and t.brand = ?");
                parame.add(searchBrand);
            }
        }

        if (filter.containsKey("searchStander")) {
            String searchStander = (String) filter.get("searchStander");
            if (StringUtils.isNotEmpty(searchStander)) {
                sql.append(" and lower(t.specification) = ?");
                parame.add(searchStander);
            }
        }

        if (filter.containsKey("retls_id")) {
            String retls_id = (String) filter.get("retls_id");
            if (StringUtils.isNotEmpty(retls_id)) {
                sql.append(" and o.customer_id = ?");
                parame.add(retls_id);
            }
        }

        if (filter.containsKey("slo_id")) {
            String slo_id = (String) filter.get("slo_id");
            if (StringUtils.isNotEmpty(slo_id)) {
                sql.append(" and o.id = ?");
                parame.add(slo_id);
            }
        }

        String sortField = (String) filter.get("_sortField");
        if (StringUtils.isNotEmpty(sortField)) {
            sql.append(" order by ");
            sql.append(sortField);
            sql.append(" ");
            sql.append((String) filter.get("_sort"));
        }

        Page<Product> page = dao.paginate(pageNo, pageSize, "select t.id,t.id product_id,t.name,t.name product_name,t.unit,t.stock_warn,t.billsn,t.status,t.model,t.sale_price,t.purchase_price,t.category,t.product_type",
                sql.toString(), parame.toArray());


        if (filter.containsKey("customer_id") && filter.get("customer_id") != null) {
            List<Product> list = page.getList();
            /*h.his_price,h.his_purchase_price,*/
            String[] products = new String[list.size()];
            int i = 0;
            for (Product product : list) {
                products[i] = product.get("product_id");
                i++;
            }

            List<ProductHis> productHis = ProductHis.dao.getProductHisBySomeId(filter.get("company_id").toString(), products, filter.get("customer_id").toString());

            for (Product product : list) {
                for (ProductHis p : productHis) {
                    if (p.getStr("product_id").equals(product.getStr("product_id"))) {
                        product.put("his_price", p.getStr("his_price"));
                        product.put("his_purchase_price", p.getStr("his_purchase_price"));
                    }
                }
            }
        }
        return page;
    }

    /**
     * 查询产品信息，通过IDMap
     *
     * @param filter 参数
     * @return
     */
    public List<Product> getProductById(String companyId, List<Map<String, String>> filter) {
        List<Object> parame = new ArrayList<Object>();
        StringBuffer sql = new StringBuffer(" from ");
        sql.append(tableName);
        sql.append(" t");
        sql.append(" left join ");
        sql.append(ProductHis.tableName);
        sql.append(" h");
        sql.append(" on t.id = h.product_id");
        sql.append(" where t.company_id=? ");
        parame.add(companyId);
        sql.append(" and t.id in (");
        for (int i = 0; i < filter.size(); i++) {
            String productId = filter.get(i).get("id");
            sql.append("?,");
            parame.add(productId);
        }
        sql.replace(sql.lastIndexOf(","), sql.length(), ")");

        return dao.find("select t.id,t.id product_id,t.name,t.name product_name,t.unit,t.stock_warn,t.billsn,t.status,t.model,t.sale_price,t.purchase_price,t.category,h.his_price,t.product_type " + sql.toString(), parame.toArray());
    }


    @Before(Tx.class)
    public void del(String id, String company_id) {
        if (StringUtils.isNotEmpty(id)) {
            String[] ids = id.split(",");
            StringBuffer ids_ = new StringBuffer();
            List<String> parame = new ArrayList<String>();
            for (String id_ : ids) {
                ids_.append("?,");
                parame.add(id_);
            }
            ids_.append("'-'");
            parame.add(company_id);
            Db.update("delete  from " + tableName + " where id in (" + ids_.toString() + ") and company_id=? ", parame.toArray());
        }
    }

    public Product findById(String id, String company_id) {
        return dao.findFirst("select t.*,t.sale_price,t.purchase_price from "
                + tableName + " t where t.company_id=? and t.id=? ", company_id, id);
    }

    public Product findByIndex(String company_id,int index) {
        return dao.findFirst("select * from ( select row_number() over() as num, t.id,t.id product_id,t.name,t.name product_name,t.unit,t.stock_warn,t.billsn,t.status,t.model,t.sale_price,t.purchase_price,t.category,t.product_type,t.brand,t.specification from "
                + tableName + " t where t.company_id=? order by t.id ) as foo where foo.num = ?", company_id, index);
    }

    public Product findByBillsn(String billSn, String company_id) {
        return dao.findFirst("select * from "
                + tableName + " t where t.company_id=? and t.billsn=? ", company_id, billSn);
    }

    /**
     * 禁用或激活
     */
    public void disable(String id, String company_id) {
        Db.update("update " + tableName + " set status=case when status=0 then 1 else 0 end where id=? and company_id=?", id, company_id);
    }

    @Before(Tx.class)
    public List<Integer> impl(List<Map<String, Object>> dataList, String company_id, List<String> clist) throws Exception {
        int index = -1;
        List<Integer> resultList = new ArrayList<>();
        boolean isPass = false;
        Map<String, Object> mapTemp = null;
        for (Map<String, Object> map : dataList) {
            index++;
            for (int i = index + 1; i < dataList.size(); i++) {
                mapTemp = dataList.get(i);
                if (map.get("billsn").toString().equals(mapTemp.get("billsn").toString())) {
                    resultList.add(i);
                    isPass = true;
                }
            }
            if (isPass) {
                resultList.add(index);
                isPass = false;
                continue;
            }

            Product product = Product.dao.findByBillsn(map.get("billsn").toString(), company_id);
            if (product != null) {
                resultList.add(index);
                continue;
            }

            product = new Product();
            for (int i = 0; i < 12; i++) {
                String cl = clist.get(i);
                if (i == 8 || i == 9 || i == 10) {
                    product.set(cl, Double.parseDouble(map.get(cl).toString()));
                } else {
                    product.set(cl, map.get(cl) == null ? "" : map.get(cl).toString());
                }
            }
            product.set("company_id", company_id);
            product.set("status", 1);
            product.save();
            String id = product.getStr("id");
            List<Depot> depotList = Depot.dao.list(company_id);
            for(Depot d : depotList){
                Average.dao.saveAverageFirst(company_id,id,"0.0","0.0",d.getStr("id"));
            }
            if (map.get(clist.get(12)) == null || ((List) map.get(clist.get(12))).size() == 0) {
                continue;
            }
            Map<String, String> comparisonMap = null;
            List<Map<String, String>> comparisonList = (List<Map<String, String>>) map.get(clist.get(12));
            for (int i = 0; i < comparisonList.size(); i++) {
                comparisonMap = comparisonList.get(i);
                if (comparisonMap.get("comparison_num") == null || comparisonMap.get("comparison_unit") == null || String.valueOf(comparisonMap.get("comparison_unit")).isEmpty() || String.valueOf(comparisonMap.get("comparison_num")).isEmpty()) {
                    continue;
                }
                Comparison comparison = new Comparison();
                comparison.set("product_id", id);
                comparison.set("comparison_num", String.valueOf(comparisonMap.get("comparison_num")));
                comparison.set("comparison_unit", String.valueOf(comparisonMap.get("comparison_unit")));
                comparison.save();
            }
        }
        return resultList;
    }


    public boolean barCodeIsExit(String barCode, String companyId) {
        String sql = "select * from " + tableName + " t where t.billsn = ? and t.company_id = ?";
        return findFirst(sql, barCode, companyId) == null ? false : true;
    }


    /**
     * by chenjianhui
     * 2015年9月23日14:33:47
     * 获取品牌信息
     *
     * @return
     */
    public List<Product> getBrand(String company_id, String category) {
        return dao.find("select id,brand from " + tableName + " where company_id = '" + company_id + "' and category ='" + category + "' group by id,brand");
    }


    /**
     * 通过企业Id获取所有产品
     *
     * @param companyId
     * @return
     */
    public List<Product> getProductByCompanyId(String companyId) {
        return this.find("select * from " + tableName + " t where t.company_id = ? ", companyId);
    }

}