package net.loyin.util;

import com.jfinal.plugin.activerecord.Model;
import net.loyin.jfinal.anatation.TableBind;
import net.loyin.utli.AppGlobal;

import java.util.*;

/**
 * Created by chenjianhui on 2015/9/14.
 */
@TableBind(name = "base_turn")
public class BaseTurn extends Model<BaseTurn> {
    private static final long serialVersionUID = -4221825254783837788L;
    public static final String tableName = "base_turn";
    public static BaseTurn dao = new BaseTurn();


    /**
     * 请求处理
     */
    public void RequestHandle() {

    }

    /**
     * 回复处理
     */
    public static List<Map<String, Object>> ResponseHandle(List<Map<String, Object>> list, String model) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        Map<String, Object> resultMap = null;
        for (Map<String, Object> map : list) {
            resultMap = new HashMap<>();
            Iterator<String> iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                BaseTurn baseTurn = null;
                baseTurn = findAppKeyByWebValue2(key, model);
                if (baseTurn == null) {
                    continue;
                }
                resultMap.put(baseTurn.getStr("app_key"), map.get(key));
            }
            resultList.add(resultMap);
        }
        return resultList;
    }

    /**
     * 读取缓存中的数据
     *
     * @param key
     * @param model
     * @return
     */
    public static BaseTurn findAppKeyByWebValue2(String key, String model) {
        List<BaseTurn> list = AppGlobal.getInstance().BaseTurns;
        BaseTurn resultBase = null;
        for (BaseTurn turn : list) {
            if (key.equals(turn.get("web_value")) && model.equals(turn.get("model"))) {
                resultBase = turn;
            }
        }
        return resultBase;
    }


    public static List<BaseTurn> findAll() {

        return dao.find("select * from " + tableName);
    }

    /**
     * 根据web value 找到 app_key
     *
     * @param webValue
     * @return
     */
    public static BaseTurn findAppKeyByWebValue(String webValue, String model) {
        return dao.findFirst("select * from " + tableName + " where web_value='" + webValue + "' and model= '" + model + "'");
    }

    /**
     * 根据app_key 找到 web value
     *
     * @param appKey
     * @return
     */
    public static BaseTurn findWebValueByAppKey(String appKey, String model) {
        return dao.findFirst("select * from " + tableName + " where app_key='" + appKey + "' and model='" + model + "'");
    }


    public Map<String, Object> getAttrs() {
        return super.getAttrs();
    }
}
