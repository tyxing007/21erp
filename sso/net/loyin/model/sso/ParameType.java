package net.loyin.model.sso;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import net.loyin.jfinal.anatation.TableBind;

import java.util.List;

/**
 * Created by EYKJ on 2015/8/27.
 */
@TableBind(name="sso_parame_type")
public class ParameType extends Model<ParameType> {
    private static final long serialVersionUID = -3460285118690352227L;
    public static final String tableName="sso_parame_type";
    public static Parame dao=new Parame();

    public List<ParameType> getAllParameType(){
        String sql = "select * from " + tableName + " order by to_number(type_index,'99G999D9S')";
        return this.find(sql);
    }
}
