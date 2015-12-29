package net.loyin.model.scm;

import com.jfinal.plugin.activerecord.Model;
import net.loyin.jfinal.anatation.TableBind;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EYKJ on 2015/9/7.
 */
@TableBind(name="scm_product_his")
public class ProductHis extends Model<ProductHis> {
    private static final long serialVersionUID = -5465375747846490039L;
    public static final String tableName="scm_product_his";
    public static ProductHis dao=new ProductHis();


    public ProductHis getProductHisBySomeId(String CompanyId,String productId,String customerId){
        String sql = "select * from " +tableName+" t where t.company_id = ? and t.product_id = ? and t.customer_id = ?";
        return findFirst(sql,CompanyId,productId,customerId);
    }


    public List<ProductHis> getProductHisBySomeId(String CompanyId, String[] productId, String customerId){
//        and t.product_id = ?
        String sql = "select * from " +tableName+" t where t.company_id = ? and t.customer_id = ? and t.product_id in (";
        List<Object> objectList = new ArrayList<>();
        objectList.add(CompanyId);
        objectList.add(customerId);
        for(String s : productId){
            sql +=" ? ,";
            objectList.add(s);
        }
        ;
        sql = sql.substring(0,sql.lastIndexOf(","))+")";
        return find(sql, objectList.toArray());
    }

}
