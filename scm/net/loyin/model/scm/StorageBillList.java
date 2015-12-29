package net.loyin.model.scm;

import java.util.List;
import java.util.Map;

import net.loyin.model.sso.Parame;
import org.apache.commons.lang3.StringUtils;

import net.loyin.jfinal.anatation.TableBind;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.tx.Tx;
/**
 * 出入库单产品列表
 * @author liugf 风行工作室
 */
@TableBind(name="scm_storage_bill_list")
public class StorageBillList extends Model<StorageBillList> {
	private static final long serialVersionUID = 5708102282757729482L;
	public static final String tableName="scm_storage_bill_list";
	public static StorageBillList dao=new StorageBillList();
	@Before(Tx.class)
	public void insert(List<Map<String,Object>> list,String id){
		Db.update("delete from "+tableName+" where id=?",id);

		Object[][]paras=new Object[list.size()][5];
		int i=0;
		for (Map<String, Object> a : list) {
			paras[i][0] = id;
			paras[i][1] = a.get("product_id");
			if (StringUtils.isNotEmpty((String) a.get("amount")))
				paras[i][2] = Float.parseFloat((String) a.get("amount"));
			paras[i][3] = a.get("remark");
			paras[i][4] = a.get("product_sort");
			i++;
		}
		Db.batch("INSERT INTO "+tableName+"(id,product_id,amount,remark,product_sort)VALUES (?,?,?,?,?)",paras,list.size());
	}

	public void updateProductSort(List<Map<String,Object>> list,String id){
//		Db.update("delete from "+tableName+" where id=?",id);

//		Object[][]paras=new Object[list.size()][5];
//		int i=0;
//		for (Map<String, Object> a : list) {
//			paras[i][0] = id;
//			paras[i][1] = a.get("product_id");
//			if (StringUtils.isNotEmpty((String) a.get("amount")))
//				paras[i][2] = Float.parseFloat((String) a.get("amount"));
//			paras[i][3] = a.get("remark");
//			paras[i][4] = a.get("product_sort");
//			i++;
//		}
//		Db.batch("update "+tableName+" t set t.product_sort = ? where t.product_id",paras,list.size());
	}


	/**
	 * 根据出入库ID获取产品列表
	 * @param id
	 * @return
	 */
	public List<StorageBillList> list(String id){
		return this.find("select t.*,p.name product_name,p.unit,p.billsn,p.specification,pe.name as unitChina,p.brand,pec.name as categoryChina from "+tableName+" t,"+Product.tableName+" p, "+ Parame.tableName+" pe, "+Parame.tableName+" pec where p.id=t.product_id and pe.id = p.unit and pec.id = p.category and t.id=? order by t.product_sort ",id);
	}

	/**
	 * 根据出入库ID获取产品列表(根据品牌排序)
	 * @param id
	 * @return
	 */
	public List<StorageBillList> listOrderCategory(String id){
		return this.find("select t.*,p.name product_name,p.unit,p.billsn,p.specification,pe.name as unitChina,p.brand,pec.name as categoryChina from "+tableName+" t,"+Product.tableName+" p, "+ Parame.tableName+" pe, "+Parame.tableName+" pec where p.id=t.product_id and pe.id = p.unit and pec.id = p.category and t.id=? order by pec.parent_id,pec.id ",id);
	}

	/**
	 *
	 * @param id
	 * @param productId
	 * @return
	 */
	public List<StorageBillList> list(String id,String productId){
		return this.find("select t.*,p.name product_name,p.unit,p.billsn,p.specification,pe.name as unitChina,p.brand,pec.name as categoryChina from "+tableName+" t,"+Product.tableName+" p, "+ Parame.tableName+" pe, "+Parame.tableName+" pec where p.id=t.product_id and pe.id = p.unit and pec.id = p.category and t.id=? and t.product_id = ? order by t.product_sort ",id,productId);
	}
}
