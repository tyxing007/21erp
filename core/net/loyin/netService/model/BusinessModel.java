package net.loyin.netService.model;

import com.jfinal.plugin.activerecord.Model;
import net.loyin.jfinal.anatation.TableBind;

import java.util.List;

/**
 * Created by Chao on 2015/11/10.
 * 业务逻辑处理数据表
 */
@TableBind(name = "base_businessprocess")
public class BusinessModel extends Model<BusinessModel> {
    public static final BusinessModel dao = new BusinessModel();
    private static final long serialVersionUID = 8699093530520166772L;
    public static final String tableName = "base_businessprocess";

    public List<BusinessModel> queryBusinessAll(){
        String sql = "select * from " + tableName;
        return find(sql);
    }







}
