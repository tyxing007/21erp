package net.loyin.model.oa;

import com.jfinal.plugin.activerecord.Model;
import net.loyin.jfinal.anatation.TableBind;

import java.util.Iterator;
import java.util.Map;

/**
 * 消息
 *
 * @author liugf 风行工作室
 *         2014年9月19日
 */
@TableBind(name = "oa_punch")
public class Punch extends Model<Punch> {
    private static final long serialVersionUID = -5904205242847326222L;
    public static final String tableName = "oa_punch";
    public static Punch dao = new Punch();


    /**
     * 保存工作计划
     *
     * @param map
     * @return
     */
    public String save(Map<String, Object> map) {
        Iterator<String> iterator = map.keySet().iterator();
        Punch punch = new Punch();
        String key = null;
        while (iterator.hasNext()) {
            key = iterator.next();
            punch.set(key, map.get(key));
        }
        punch.save();
        return punch.getStr("id");
    }

}
