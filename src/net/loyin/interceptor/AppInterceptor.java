package net.loyin.interceptor;

//import com.jfinal.core.;
import com.jfinal.core.ActionInvocation;
import com.jfinal.log.Logger;
import com.jfinal.plugin.ehcache.CacheKit;
import net.loyin.ctrl.BaseController;
import net.loyin.jfinal.anatation.PowerBind;
import net.loyin.jfinal.model.IdGenerater;
import net.loyin.model.sso.Position;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenjianhui on 2015/9/14.
 */
public class AppInterceptor extends BaseInterceptor {
    public Logger log = Logger.getLogger(getClass());
    protected static IdGenerater idGenerater = new IdGenerater();

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void doIntercept(ActionInvocation ai) {
        BaseController ctrl = (BaseController) ai.getController();
        ctrl.setAttr("port", ctrl.getRequest().getServerPort());
        PowerBind p = ai.getMethod().getAnnotation(PowerBind.class);
        log.debug("操作权限控制");

        /**判断是app的请求*/
        String isAppRequest = ctrl.getPara("is_app");
        /*if (isAppRequest != null && !isAppRequest.equals("")) {
            if (isAppRequest.equals("1")) {
                Map<String, Object> map = ctrl.getRequest().getParameterMap();

                Iterator<String> keys = map.keySet().iterator();

                while (keys.hasNext()) {
                    String key = keys.next();
                    BaseTurn baseTurn = BaseTurn.dao.findAppKeyByWebValue(key);
                    if (baseTurn == null)
                        continue;
                    ctrl.getRequest().setAttribute(baseTurn.get("web_value"), map.get(key));
                }
            }
        }*/
        if (p == null) {
            p = ai.getController().getClass().getAnnotation(PowerBind.class);
        }
        if (p == null) {
            ai.invoke();
        } else if (power_check) {
            boolean v = false;
            String[] code = null;
            v = p.v();
            code = p.code();
            boolean f = false;
            if (v == true) {
                //菜单权限判断
                String powerCodeList = (String) CacheKit.get("oneday", "power_" + ctrl.getPositionId());
                if (StringUtils.isEmpty(powerCodeList)) {
                    Position.dao.cachPermission(ctrl.getCompanyId());
                    powerCodeList = (String) CacheKit.get("oneday", "power_" + ctrl.getPositionId());
                }
                if (StringUtils.isNotEmpty(powerCodeList)) {
                    String[] s = powerCodeList.split(",");
                    if (check(s, code)) {//安全码匹配
                        ai.invoke();//注意 一定要执行此方法
                    } else {
                        f = true;
                    }
                } else {
                    f = true;
                }
            }
            if (f) {
                Map<String, Object> json = new HashMap<String, Object>();
                json.put("success", false);
                json.put("msg", "您未有此操作权限！<br>请联系管理员修改权限设置！");
                json.put("status", 401);
                ctrl.renderJson(json);//("{\"success\":false,\"msg\":\"您未有此操作权限！<br>请联系管理员修改权限设置！\",\"status\":401}");
            }
        }
    }

    private boolean check(String[] s, String[] code) {
        for (String c : code) {
            if (ArrayUtils.contains(s, c)) {
                return true;
            }
        }
        return false;
    }
}
