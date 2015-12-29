package net.loyin.ctrl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.loyin.jfinal.model.IdGenerater;
import net.loyin.model.crm.Customer;
import net.loyin.model.sso.User;
import net.loyin.util.BaseTurn;
import net.loyin.util.CS;
import net.loyin.util.PropertiesContent;
import net.loyin.util.safe.CipherUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.render.JsonRender;

/**
 * 基础Controller
 *
 * @author 刘声凤
 *         2012-9-3 下午10:37:28
 */
public abstract class BaseController<M extends Model<M>> extends Controller {
    public Logger log = Logger.getLogger(getClass());
    protected static final IdGenerater idGenerater = new IdGenerater();
    protected String id;
    /**
     * yyyy-MM-dd HH:mm:ss 日期时间
     */
    public static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * yyyyMMddHHmmss 日期时间
     */
    public static final SimpleDateFormat dateTimeFormat1 = new SimpleDateFormat("yyyyMMddHHmmss");
    /**
     * yyyy-MM-dd 日期
     */
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * HH:mm:ss 时间
     */
    public static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    /**
     * HH:mm 小时分钟
     */
    public static final SimpleDateFormat hmFormat = new SimpleDateFormat("HH:mm");
    protected Class<M> modelClass;
    protected static final String tokenName = "token";

    /**
     * 文件保存路径
     */
    private static String upload_root = "/upload/file";
    /**
     * 全路径
     */
    private static String upload_dir;
    /**
     * token超时时间600s
     */
    protected int tokenTout = 600;

    @SuppressWarnings("deprecation")
    protected void init() {
        if (upload_dir == null)
            upload_dir = this.getRequest().getRealPath(upload_root);
    }

    public void index() {
        String para = this.getPara();
        if (StringUtils.isNotEmpty(para))
            this.renderHTML(para.replaceAll("\\-", "/"));
        else {
            String gotoUrl = this.getPara("go");
            if (StringUtils.isNotEmpty(gotoUrl))
                this.renderHTML(gotoUrl);
        }
    }

    /**
     * HTML视图
     *
     * @param view 视图文件名不含.html
     */
    protected void renderHTML(String view) {
        if (view.endsWith(".html")) {
            super.render(view);
        } else {
            super.render(view + ".html");
        }
    }

    /**
     * 获取当前系统操作人
     */
    protected User getCurrentUser() {
        return User.dao.qryLoginUser(getCurrentUserId());
    }

    /**
     * 获取当前系统操作人ID
     */
    public String getCurrentUserId() {
        return getUserMap().get("uid");
    }

    /**
     * 获取当前系统操作人企业ID
     */
    public String getCompanyId() {
        return getUserMap().get("company_id");
    }

    /**
     * 获取当前系统操作人岗位ID
     */
    public String getPositionId() {
        return getUserMap().get("position_id");
    }

    /**
     * 获取当前系统操作人部门ID
     */
    public String getDepartmentId() {
        return getUserMap().get("department_id");
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> getUserMap() {
        Map<String, String> userMap = new HashMap<String, String>();
        String cookieVal = this.getCookie(PropertiesContent.get("cookie_field_key"));
        if (StringUtils.isNotEmpty(cookieVal)) {
            cookieVal = CipherUtil.decryptData(cookieVal);
            userMap = (Map<String, String>) JSON.parse(cookieVal);
        }
        return userMap;
    }

    /**
     * 从jar包获取
     */
    protected void renderFormJar(String view) {

    }

    /**
     * @param success
     * @param statusCode 状态码默认为401
     * @param msg
     * @param data       数组 [0]:data [1]:tokenid
     */
    protected void rendJson(boolean success, Integer statusCode, String msg, Object... data) {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("success", success);
        json.put("status", success ? 200 : (statusCode == null ? 401 : statusCode));
        json.put("msg", msg);
        if (data != null && data.length > 0) {
            json.put("data", data[0]);
            if (data.length > 1) {
                json.put("tokenid", data[1]);
            }
        }
        rendJson(json);
    }


    /**
     * by chenjianhui 2015年8月25日15:49:56
     * change by wuweichao 2015年9月8日10:32:44
     * 手机端请求
     *
     * @param list
     * @param c
     * @param info
     */
    protected void rendJsonApp(boolean isSuccess, List<Map<String, Object>> list, int c, String info) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (isSuccess) {
            map.put("d", list);
            map.put("c", c);
            map.put("info", info);
        } else {
            Map<String, Object> remap = new HashMap<String, Object>();
            remap.put(CS.RETURN_MSG, CS.ERROR);
            list.add(remap);
            map.put("d", list);
            map.put("c", c);
            map.put("info", info);
        }
        this.render(new JsonRender(map));
        return;
    }

    /**
     * by chenjianhui 2015年8月25日15:49:56
     * 手机端请求
     *
     * @param list
     * @param isMobile
     */
    /*protected void rendJsonApp(List list, boolean isMobile, String msg) {
        int status = 0;

        Map<String, Object> returnValues = new HashMap<String, Object>();
        if (isMobile) {
            if (list != null) {
                status = 500;
            }
            returnValues.put("c", status);
            returnValues.put("d", list);
            returnValues.put("info", "error");
            returnValues.put("meg", msg);

            this.render(new JsonRender(returnValues));
            return;
        }
    }*/


    /**
     * by chenjianhui 2015年8月25日15:49:11
     * web端请求
     *
     * @param json
     */
    protected void rendJson(Object json) {

        this.render(new JsonRender(json));
        String agent = getRequest().getHeader("User-Agent");
        if (agent.contains("MSIE"))
            this.render(new JsonRender(json).forIE());
        else {
            this.render(new JsonRender(json));
        }
    }

    protected String getId() {
        id = this.getPara(0);
        if (StringUtils.isEmpty(id)) {
            id = this.getPara("id");
        }
        return id;
    }
//	/**
//	 * 获取国际化资源
//	 * @param key 键
//	 * @param defval 默认值
//	 * @return
//	 */
//	protected String getI18nText(String key,String defval){
//		return I18N.getText(key, defval, this.getRequest().getLocale());
//	}


    /*
    获取前段传入的companyId，如果没有则从缓存中取出
     */
    protected String getCompanyIdByHttp() {
        String companyId = null;
        Map<String, String[]> map = getRequest().getParameterMap();
        if (map.containsKey("companyId")) {
            companyId = map.get("companyId")[0];
            if (companyId.isEmpty()) {
                companyId = this.getCompanyId();
            }
        } else {
            companyId = this.getCompanyId();
        }
        return companyId;
    }
}
