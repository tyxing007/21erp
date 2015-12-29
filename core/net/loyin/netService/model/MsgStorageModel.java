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
@TableBind(name = "base_msgstorage")
public class MsgStorageModel extends Model<MsgStorageModel> {
    public static final MsgStorageModel dao = new MsgStorageModel();
    private static final long serialVersionUID = 8699093530520166772L;
    public static final String tableName = "base_msgstorage";

    public List<MsgStorageModel> queryBusinessAll(){
        String sql = "select * from " + tableName;
        return find(sql);
    }

    public void saveMsgStorage(Map<String,Object> map){
        Set<String> keySet = map.keySet();
        Iterator<String> keyIterator = keySet.iterator();
        MsgStorageModel msg = new MsgStorageModel();
        while (keyIterator.hasNext()){
            String key = keyIterator.next();
            msg.set(key,map.get(key));
        }
        msg.save();
    }

    public void insertErrorLog(DataPacket dataPacket, Exception ex){
        MsgStorageModel msgModel = new MsgStorageModel();
        String msg = ex.getMessage();
        StackTraceElement[] stackTraceElements = ex.getStackTrace();
        int count = stackTraceElements.length;
        for (int i = 0; i < count; i++) {
            msg += "\n";
            msg += stackTraceElements[i].toString();
        }
        msgModel.put("msg", msg);
        msgModel.put("net_type", ex.getClass().getName());

//        Set<String> keySet = map.keySet();
//        Iterator<String> keyIterator = keySet.iterator();
//
//        while (keyIterator.hasNext()){
//            String key = keyIterator.next();
//            msg.set(key,map.get(key));
//        }
//        msg.save();
    }


}
