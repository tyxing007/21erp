package net.loyin.model.crm;

import com.jfinal.plugin.activerecord.Model;
import net.loyin.jfinal.anatation.TableBind;

import java.util.List;
import java.util.Map;

/**
 * Created by chenjianhui on 2015/9/6.
 */
@TableBind(name = "sso_parame")
public class Parame extends Model<Parame> {
    private static final long serialVersionUID = -4221825254783835788L;
    public static final String tableName = "sso_parame";
    public static Parame dao = new Parame();

    public Map<String, Object> getAttrs() {
        return super.getAttrs();
    }

    /**
     * 查询所有网点父类型
     *
     * @return
     */
    public List<Parame> getAllOfRetailType(String company_id) {

        return find("select * from " + tableName + " where company_id = '" + company_id + "' and type = '3'");
    }
   public List<Parame> getAllOfGoodsType(String company_id) {

        return find("select * from " + tableName + " where company_id = '" + company_id + "' and type = '0'");
    }


}
