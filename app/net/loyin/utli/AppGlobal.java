package net.loyin.utli;

import net.loyin.util.BaseTurn;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenjianhui on 2015/9/16.
 * App所需要的转换单例工具类
 */
public class AppGlobal {

    /**
     * 用来存储需要用到转换的所有数据
     */
    //public List<Map<String, Object>> BaseTurns = new ArrayList<Map<String, Object>>();
    public List<BaseTurn> BaseTurns = new ArrayList<BaseTurn>();

    public static AppGlobal mInstance;
    private final static Object syncLock = new Object();

    public static AppGlobal getInstance() {
        if (mInstance == null) {
            synchronized (syncLock) {
                if (mInstance == null) {
                    mInstance = new AppGlobal();
                }
            }
        }
        return mInstance;
    }
}
