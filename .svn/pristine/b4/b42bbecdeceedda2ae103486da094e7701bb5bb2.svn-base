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
@TableBind(name = "base_auditexception")
public class AuditExceptionModel extends Model<AuditExceptionModel> {
    public static final AuditExceptionModel dao = new AuditExceptionModel();
    private static final long serialVersionUID = 8699093530520166772L;
    public static final String tableName = "base_auditexception";

    public void insertErrorLog(DataPacket dataPacket, Exception ex){
        AuditExceptionModel auditExceptionModel = new AuditExceptionModel();
        String msg = ex.getMessage();
        StackTraceElement[] stackTraceElements = ex.getStackTrace();
        int count = stackTraceElements.length;
        for (int i = 0; i < count; i++) {
            msg += "\n";
            msg += stackTraceElements[i].toString();
        }
        auditExceptionModel.put("exceptiondes", msg);
        auditExceptionModel.put("exceptiontype", ex.getClass().getName());
        if (dataPacket.getMessageNo() != null && dataPacket.getMessageNo().length() > 0) {
            auditExceptionModel.put("from_id", dataPacket.getMessageNo());
        }
        if (dataPacket.getProcessId() != null && dataPacket.getProcessId().length() > 0) {
            auditExceptionModel.put("from_id", dataPacket.getProcessId());
        }
        auditExceptionModel.save();

    }


}
