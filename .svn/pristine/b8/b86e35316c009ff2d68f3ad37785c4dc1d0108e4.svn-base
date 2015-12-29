package net.loyin.netService.model;

import com.jfinal.plugin.activerecord.Model;
import net.loyin.jfinal.anatation.TableBind;
import net.loyin.netService.dataPacket.DataPacket;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Chao on 2015/11/10.
 * 业务逻辑处理数据表
 */
@TableBind(name = "base_auditprocess")
public class AuditProcessModel extends Model<AuditProcessModel> {
    public static final AuditProcessModel dao = new AuditProcessModel();
    private static final long serialVersionUID = 8699093530520166772L;
    public static final String tableName = "base_auditprocess";


    public AuditProcessModel queryBusiness(String id){
        return findFirst("select * from "+tableName+" t where t.id = ? ",id);
    }

    public String saveBusiness(String xml, String businessId, String msgNo){
        AuditProcessModel auditProcessModel = new AuditProcessModel();
        auditProcessModel.put("prodraft", xml);
        auditProcessModel.put("msg_no", msgNo);
        auditProcessModel.put("business_id", businessId);
        auditProcessModel.save();
        return  auditProcessModel.getStr("id");
    }

    public void updateBusinessInfo(String processId, String status){
        AuditProcessModel auditProcessModel = this.queryBusiness(processId);
        auditProcessModel.put("prostatu", status);
        auditProcessModel.put("id", processId);
        auditProcessModel.update();
    }
}
