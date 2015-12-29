package net.loyin.model.scm;

import java.util.List;
import java.util.Map;

import net.loyin.jfinal.anatation.TableBind;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.tx.Tx;
/**
 * 调拨产品明细
 * @author liugf 风行工作室
 */
@TableBind(name="scm_stock_allot_list")
public class StockAllotList extends Model<StockAllotList> {
	private static final long serialVersionUID = -1447533853973326468L;
	public static final String tableName="scm_stock_allot_list";
	public static StockAllotList dao=new StockAllotList();
	@Before(Tx.class)
	public void insert(List<Map<String,Object>> list,String id){
		Db.update("delete from "+tableName+" where id=?",id);
		Object[][]paras=new Object[list.size()][4];
		int i=0;
		for (Map<String, Object> a : list) {
			paras[i][0] = id;
			paras[i][1] = a.get("product_id");
			if (StringUtils.isNotEmpty((String) a.get("amount"))) {
				paras[i][2] = Float.parseFloat((String) a.get("amount"));
			}else{
				paras[i][2] = new Float(0.0);
			}
			if (StringUtils.isNotEmpty((String) a.get("allotmoney"))) {
				paras[i][3] = Float.parseFloat((String) a.get("allotmoney"));
			}else{
				paras[i][3] = new Float(0.0);
			}
			i++;
		}
		for(int m=0;m<paras.length;m++){
			StockAllotList stockAllotList = new StockAllotList();
			stockAllotList.set("id",paras[m][0]);
			stockAllotList.set("product_id",paras[m][1]);
			stockAllotList.set("amount",paras[m][2]);
			stockAllotList.set("allot_money",paras[m][3]);
			stockAllotList.save();
		}

//			Db.batch("INSERT INTO "+tableName+"(id,product_id,amount,allot_money)VALUES (?,?,?,?)",paras,list.size());
	}
	public List<StockAllotList> list(String id){
		return this.find("select t.*,p.name product_name,p.unit,p.billsn from "+tableName+" t,"+Product.tableName+" p where p.id=t.product_id and t.id=? order by p.name ",id);
	}

	public List<StockAllotList> list(String id,String productId){
		return this.find("select t.*,p.name product_name,p.unit,p.billsn from "+tableName+" t,"+Product.tableName+" p where p.id=t.product_id and t.id=? and t.product_id=? order by p.name ",id,productId);
	}
}