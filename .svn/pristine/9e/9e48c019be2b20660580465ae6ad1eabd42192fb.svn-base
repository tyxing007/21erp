package net.loyin.model.scm;

import com.jfinal.plugin.activerecord.Model;
import net.loyin.jfinal.anatation.TableBind;

/**
 * Created by EYKJ on 2015/9/1.
 */
@TableBind(name = "scm_order_send")
public class OrderSend extends Model<OrderSend> {
    private static final long serialVersionUID = 2862475552937245134L;
    public static final String tableName = "scm_order_send";
    public static OrderSend dao = new OrderSend();

    public OrderSend getOrderSend(String id) {
        String sql = "";
        sql += "select t.* from " + tableName + " t where t.order_id = '" + id + "'";
        return this.findFirst(sql);
    }


}
