package net.loyin.netService.netPool;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 张维 on 2014/11/3.
 */
public class NetConnPool {
    private static int id = 1;
    public static Map<String, Object> netPool = new HashMap<String, Object>();

    public static String add(Object obj) {
        //String key = java.util.UUID.randomUUID().toString();
        String key = id + "";
        id++;
        netPool.put(key, obj);
        return key;
    }

    public static Object get(String key) {
        if (netPool.containsKey(key)) {
            return netPool.get(key);
        }
        return null;
    }

    public static void remove(String key) {
        if (netPool.containsKey(key)) {
            netPool.remove(key);
        }
    }
}
