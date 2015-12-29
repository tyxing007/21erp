package net.loyin.model.sso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.loyin.jfinal.anatation.TableBind;

import net.loyin.model.scm.Order;
import net.loyin.model.scm.OrderProduct;
import net.loyin.model.scm.Product;
import org.apache.commons.lang3.StringUtils;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

/**
 * 参数
 *
 * @author liugf 风行工作室
 */
@TableBind(name = "sso_parame")
public class Parame extends Model<Parame> {
    private static final long serialVersionUID = -3460285118690352227L;
    public static final String tableName = "sso_parame";
    public static Parame dao = new Parame();

    /**
     * 查找参数查询
     */
    public List<Record> qryList(String company_id, Integer type) {
        List<Object> parame = new ArrayList<Object>();
        StringBuffer where = new StringBuffer("where d.company_id=? ");
        parame.add(company_id);
        if (type != null) {
            where.append(" and d.type= ?");
            parame.add(type);
        }
        where.append(" order by d.sort_num asc");
        return Db.find("select d.id,d.name,d.name as textname,d.parent_id as pid,d.sort_num,d.type,d.is_end from " + tableName + " d " + where.toString(), parame.toArray());
    }




    /**
     * 查找参数查询
     */
    public List<Record> qryListInProduct(String company_id, Integer type, String[] products) {
        List<Object> parame = new ArrayList<Object>();
        StringBuffer sqlLeft = new StringBuffer("");
        StringBuffer where = new StringBuffer(" where d.company_id=? ");
        parame.add(company_id);
        if (type != null) {
            where.append(" and d.type= ? ");
            parame.add(type);
        }
        if (products.length != 0) {
            sqlLeft.append(" left join ");
            sqlLeft.append(Product.tableName);
            sqlLeft.append(" p on p.category = d.id ");
            where.append(" and p.id in (");
            for (String s : products) {
                where.append(" ? ,");
                parame.add(s);
            }
            where.append(" '-' ");
            where.append(" )");
        }

        where.append(" order by d.sort_num asc");
        if (products.length != 0) {
            return Db.find("select distinct d.id,d.name,d.name as textname,d.parent_id as pid,d.sort_num,d.type,d.is_end from " + tableName + " d " + sqlLeft.toString() + where.toString(), parame.toArray());

        }
        return Db.find("select d.id,d.name,d.name as textname,d.parent_id as pid,d.sort_num,d.type,d.is_end from " + tableName + " d " + sqlLeft.toString() + where.toString(), parame.toArray());
    }

    /**
     * @param company_id
     * @param type
     * @param isParent
     * @return
     */
    public List<Record> qryListIsHaveParen(String company_id, Integer type, boolean isParent, String searchContent) {
        List<Object> parame = new ArrayList<Object>();
        StringBuffer where = new StringBuffer("where d.company_id=? ");
        parame.add(company_id);
        if (type != null) {
            where.append(" and d.type= ?");
            parame.add(type);
        }
        if (isParent) {
            where.append(" and d.parent_id is null");
        } else {
            where.append(" and d.parent_id is not null");
        }
        if (searchContent != null && !searchContent.isEmpty()) {
            String sql = " left join " + Product.tableName + " p on p.category = d.id ";
            where = new StringBuffer(sql).append(where);
            where.append(" and (s.billsn like ? or s.name like ?) ");
            parame.add(searchContent);
            parame.add(searchContent);
        }
        where.append(" order by d.sort_num asc");
        return Db.find("select d.id,d.name,d.name as textname,d.parent_id as pid,d.sort_num,d.type,d.is_end from " + tableName + " d " + where.toString(), parame.toArray());
    }

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

    public Parame findById(String id, String company_id) {
        return dao.findFirst("select d.*,p.name as parent_name from " + tableName + " d left join " + tableName + " p on p.id=d.parent_id where d.id=? and d.company_id=?", id, company_id);
    }

    /**
     * 查询参数的类别
     */
    public List<Integer> qryTypeList(String companyId) {
        List<Record> list = Db.find("select distinct type from " + tableName + " where company_id=? order by type asc", companyId);
        if (list != null && list.isEmpty() == false) {
            List<Integer> list_ = new ArrayList<Integer>();
            for (Record r : list) {
                list_.add(r.getInt("type"));
            }
            return list_;
        }
        return null;
    }

    /**
     * 查看地区数据
     */
    public List<Record> qryAreaList() {
        return Db.find("select * from base_area order by sort_num asc");
    }

    public List<Parame> list(String company_id) {
        return this.find("select id,name from " + tableName + " where company_id=?", company_id);
    }


    public List<Parame> findByParentId(String pid, String company_id) {
        return dao.find("select d.* from " + tableName + " d where d.parent_id=? and d.company_id=?", pid, company_id);
    }


    /**
     * 查询历史产品类型
     *
     * @param company_id
     * @param type
     * @param customer_id
     * @param searchContent 可以置空，如果为空则不添加关键字
     * @return
     */
    public List<Record> qryListProductTypeByOrder(String company_id, Integer type, String customer_id, String searchContent) {

        List<Object> par = new ArrayList<>();
        String sql = tableName + " d left join " + Product.tableName + " s on s.category = d.id" +
                " left join " + OrderProduct.tableName + " sop on sop.product_id = s.id" +
                " left join " + Order.tableName + " o on o.id = sop.id" +
                " where s.company_id = ? and d.type = ? and o.customer_id = ?";
        par.add(company_id);
        par.add(type);
        par.add(customer_id);
        if (searchContent != null && !searchContent.isEmpty()) {
            sql += " and (s.billsn like ? or s.name like ?) ";
            par.add(searchContent);
            par.add(searchContent);
        }
        return Db.find("select d.id,d.name,d.name as textname,d.parent_id as pid,d.sort_num,d.type,d.is_end from " + tableName + " d " + sql.toString(), par);

    }

    /**
     * 用于查询商品属性
     *
     * @param companyId
     * @param isBrand   true 查询品牌 false 查询规格
     * @param isHis     true 查询历史  false 查询所有
     * @return
     */
    public List<Record> qryListProductInfoByOrder(String companyId, boolean isBrand, boolean isHis, Map<String, String> filter) {
        List<Record> result = new ArrayList<>();
        StringBuffer sql = new StringBuffer();
        List<Object> parame = new ArrayList<Object>();
        if (isBrand) {
            sql.append("select distinct s.brand as merch_brand  from ");
            if (isHis) {
                sql.append(Product.tableName + " s" +
                        " left join " + OrderProduct.tableName + " sop on sop.product_id = s.id" +
                        " left join " + Order.tableName + " o on o.id = sop.id" +
                        " where o.company_id = ?");
                parame.add(companyId);
            } else {
                sql.append(Product.tableName + " s" +
                        " where s.company_id = ?");
                parame.add(companyId);
            }
            if (StringUtils.isNotBlank(filter.get("merchType"))) {
                sql.append(" and s.category = ?");
                parame.add(filter.get("merchType"));
            }

            if (StringUtils.isNotBlank(filter.get("merchStand"))) {
                sql.append(" and s.specification = ?");
                parame.add(filter.get("merchStand"));
            }
            if (StringUtils.isNotBlank(filter.get("merchBrand"))) {
                sql.append(" and s.brand like ?");
                parame.add(filter.get("merchBrand"));
            }

        } else {
            sql.append("select distinct lower(s.specification) as merch_stander  from ");
            if (isHis) {
                sql.append(Product.tableName + " s" +
                        " left join " + OrderProduct.tableName + " sop on sop.product_id = s.id" +
                        " left join " + Order.tableName + " o on o.id = sop.id" +
                        " where o.company_id = ?");
                parame.add(companyId);
            } else {
                sql.append(Product.tableName + " s" +
                        " where s.company_id = ?");
                parame.add(companyId);
            }

            if (StringUtils.isNotBlank(filter.get("merchType"))) {
                sql.append(" and s.category = ?");
                parame.add(filter.get("merchType"));
            }

            if (StringUtils.isNotBlank(filter.get("searchBrand"))) {
                sql.append(" and s.brand = ?");
                parame.add(filter.get("searchBrand"));
            }
            if (StringUtils.isNotBlank(filter.get("merchStander"))) {
                sql.append(" and s.specification = ?");
                parame.add(filter.get("merchStander").toUpperCase());
            }

        }

        if (StringUtils.isNotBlank(filter.get("searchContent"))) {

            sql.append(" and (s.billsn like '%" + filter.get("searchContent") + "%' or s.name like '%" + filter.get("searchContent") + "%') ");
          /*  parame.add(filter.get("searchContent"));
            parame.add(filter.get("searchContent"));*/
        }

        return Db.find(sql.toString(), parame.toArray());

    }


    /**
     * 通过ID获取子类别ID
     * @return
     */
    public List<String> getCategoryIdById(String categoryId){
        List<String> result = new ArrayList<>();
        List<Record> list = Db.find("with recursive cte as ( select * from sso_parame where id=? union all select b.* from sso_parame b inner join cte c on b.parent_id=c.id) select id from cte",categoryId);
        for(Record r: list){
            result.add(r.getStr("id"));
        }
        return  result;
    }


}
