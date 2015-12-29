package net.loyin.netService;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <pre>通讯模块缓存</pre>
 * <pre>所属模块：系统通讯</pre>
 *
 * @author 吴为超
 * @version 1.0 创建于 2015/11/10
 */
public class NetCache {

    /**
     * 业务处理缓存
     */
    private static HashMap<String, BusinessProcess> BusinessProcessCache = new HashMap<String, BusinessProcess>();
    /**
     * 解析描述缓存
     */
    private static HashMap<String, ResolveDesc> ResolveDescCache = new HashMap<String, ResolveDesc>();


    /**
     * 添加业务处理缓存
     *
     * @param key
     */
    public static void AddBusinessProcess(String key, BusinessProcess businessProcess) {
        BusinessProcessCache.put(key, businessProcess);
    }

    /**
     * 移除业务处理缓存
     *
     * @param key
     */
    public static void RemovedBusinessProcess(String key) {
        BusinessProcessCache.remove(key);
    }

    /**
     * 业务处理是否存在
     *
     * @param key
     * @return
     */
    public static Boolean BusinessProcessContains(String key) {
        return BusinessProcessCache.containsKey(key);
    }

    /**
     * 获取业务处理缓存
     *
     * @param key
     * @return
     */
    public static BusinessProcess GetBusinessProcess(String key) {
        return BusinessProcessCache.get(key);
    }

    /**
     * 根据交易编号&版本号获取业务处理
     *
     * @param code
     * @param ver
     * @return
     */
    public static BusinessProcess GetBusinessProcessByProcessCodeAndVersion(String code, String ver) {
        BusinessProcess businessProcess = null;
        Iterator iter = BusinessProcessCache.entrySet().iterator();
        BusinessProcess bp = null;
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            bp = (BusinessProcess) entry.getValue();
            if (bp.getPacketsVersion().equals(ver) && bp.getProcessCode().equals(code)) {
                break;
            } else {
                bp = null;
            }
        }
        return bp;
    }

    /**
     * 添加报文解析缓存
     *
     * @param key
     */
    public static void AddResolveDesc(String key, ResolveDesc resolveDesc) {
        ResolveDescCache.put(key, resolveDesc);
    }

    /**
     * 移除报文解析缓存
     *
     * @param key
     */
    public static void RemovedResolveDesc(String key) {
        ResolveDescCache.remove(key);
    }

    /**
     * 报文解析是否存在
     *
     * @param key
     * @return
     */
    public static Boolean ResolveDescContains(String key) {
        return ResolveDescCache.containsKey(key);
    }

    /**
     * 获取报文解析缓存
     *
     * @param key
     * @return
     */
    public static ResolveDesc GetResolveDesc(String key) {
        return ResolveDescCache.get(key);
    }

    /**
     * 获取报文解析缓存
     * @return
     */
    public static ResolveDesc GetResolveDescByValue(String value) {
        Iterator iter = ResolveDescCache.entrySet().iterator();
        ResolveDesc rs = null;
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            rs = (ResolveDesc) entry.getValue();
            if (rs.getVoClass() == value) {
                break;
            }
        }
        return rs;
    }
}
