package net.loyin.model.scm;

import com.jfinal.plugin.activerecord.Model;
import net.loyin.jfinal.anatation.TableBind;

import java.util.List;

/**
 * Created by EYKJ on 2015/8/26.
 */
@TableBind(name="scm_product_unit")
public class Comparison extends Model<Comparison> {
    private static final long serialVersionUID = -3544681998668610255L;
    public static final String tableName="scm_product_unit";
    public static Comparison dao=new Comparison();

    public List<Comparison> getComparisonByProductId(String productId){
        return this.find("select * from "+tableName+" where product_id = ?",productId);
    }
}
