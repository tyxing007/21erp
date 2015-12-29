package net.loyin.model.scm;

import com.jfinal.plugin.activerecord.Model;
import net.loyin.jfinal.anatation.TableBind;
import net.sf.json.xml.XMLSerializer;

import java.util.List;

/**
 * Created by EYKJ on 2015/9/7.
 */
@TableBind(name="scm_product_combination")
public class Combination extends Model<Combination> {
    private static final long serialVersionUID = -3544681998668610255L;
    public static final String tableName="scm_product_combination";
    public static Combination dao=new Combination();


    public List<Combination> getCombinationByParentId(String companyId,String productId){
        String sql = "select t.*,p.name as product_name from "+tableName+" t left join "+Product.tableName+" p on p.id = t.product_id where t.companyId = ? and t.parent_id = ?";
        return find(sql, companyId, productId);
    }

}