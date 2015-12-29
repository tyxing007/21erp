package net.loyin.netService.netPool;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2014/11/6.
 */
public class NetThreadPool {
    public static Map<String, Thread> netPool = new HashMap<String, Thread>();

    public static String add(Thread obj) {
        String key = java.util.UUID.randomUUID().toString();
        netPool.put(key, obj);
        return key;
    }

    public static String addToSleep(Thread obj) throws InterruptedException {
        return add(obj);
    }

    public static Object get(String key) {
        if (netPool.containsKey(key)) {
            return netPool.get(key);
        }
        return null;
    }

    public static void removeToNotify(String key) {
        if (netPool.containsKey(key)) {
            Thread thread = netPool.get(key);
            netPool.remove(key);
            thread.stop();
        }
    }
}
