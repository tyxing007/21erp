package net.loyin.ctrl.app;

import com.jfinal.plugin.activerecord.Page;
import net.loyin.ctrl.AdminBaseController;
import net.loyin.jfinal.anatation.RouteBind;
import net.loyin.model.ReturnMsg;
import net.loyin.model.crm.Contacts;
import net.loyin.model.crm.CustRating;
import net.loyin.model.crm.Customer;
import net.loyin.model.crm.Parame;
import net.loyin.model.sso.SnCreater;
import net.loyin.util.BaseTurn;
import net.loyin.util.CS;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.sun.org.apache.xalan.internal.lib.ExsltMath.atan2;
import static com.sun.org.apache.xalan.internal.lib.ExsltMath.sqrt;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Created by chenjianhui on 2015/9/15.
 */
@RouteBind(path = "retailer")
public class retailerCtrl extends AdminBaseController<BaseTurn> {
    private String modleStr = "customer";

    static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

    /*用来转换地图使用的临时变量*/
    private static double lat = 0.0d;
    private static double lng = 0.0d;

    public retailerCtrl() {
        this.modelClass = BaseTurn.class;
    }

    /**
     * 搜索网点
     */
    public void searchRetailer() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> parameter = new HashMap<>();

        /*获取客户端传来的参数*/
//        String create_id = getPara("dsr_id");
        String keyWord = getPara("search_content");

        parameter.put("keyword", keyWord);
        parameter.put("company_id", this.getCompanyId());
        parameter.put("user_id", getCurrentUserId());
        parameter.put("position_id",this.getPositionId());



        Page<Customer> customerPage = Customer.dao.pageGrid(1, 10, parameter, 11);

        List<Customer> customers = customerPage.getList();
        Map<String, Object> map = null;

        String retls_kp_name = new String();
        Contacts contacts = new Contacts();

        for (Customer customer : customers) {
            contacts = null;
            retls_kp_name = "";
            contacts = Contacts.dao.findFirstByCustomerId(customer.get("id"));
            customer.put("retls_kp", contacts.get("name"));
            map = customer.getAttrs();
            list.add(map);
        }
        list = BaseTurn.ResponseHandle(list, modleStr);

        this.rendJsonApp(true, list, 200, "success");

    }


    /**
     * 新增网点
     */
    public void addRetailer() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        /*构建参数*/

        String create_id = getPara("create_id");
        String company_id = getPara("company_id");
        String industry = getPara("industry");
        String name = getPara("retls_name");
        String head_id = getPara("dsr_id");
        String telephone = getPara("retls_phone");
        String address = getPara("retls_address");
        String fax = getPara("retls_fax");
        String latStr = getPara("retls_latiude");
        String lngStr = getPara("retls_longitude");
        String sex = getPara("sex");


        Date dt = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String create_datetime = "";
        create_datetime = df.format(dt);
        bd_decrypt(Double.parseDouble(latStr), Double.parseDouble(lngStr));

        Customer cust = new Customer();

        cust.set("creater_id", create_id);
        cust.set("company_id", company_id);
        cust.set("industry", industry);
        cust.set("name", name);
        cust.set("head_id", head_id);
        cust.set("telephone", telephone);
        cust.set("address", address);
        cust.set("fax", fax);
        cust.set("lat_bd", lngStr);
        cust.set("lng_bd", latStr);
        cust.set("lat", lat);
        cust.set("lng", lng);
        cust.set("amt", 0);
        cust.set("type", 1);/*0:供应商 1：网点*/
        cust.set("create_datetime", create_datetime);
        String sn = SnCreater.dao.create("CUSTOMER", company_id);
        cust.set("sn", sn);
        cust.save();


        String contacts_name = getPara("retls_kp");
        String customer_id = cust.get("id");

        Contacts contacts = new Contacts();
        contacts.set("customer_id", customer_id);
        contacts.set("name", contacts_name);
        contacts.set("creater_id", create_id);
        contacts.set("create_datetime", create_datetime);
        contacts.set("company_id", company_id);
        contacts.set("telephone", telephone);
        contacts.set("sex", sex == null ? 0 : sex);
        /**1 代表是首要联系人 因为是在app上面新增的 所以默认都给他是首要联系人*/
        contacts.set("is_main", 1);

        contacts.save();

        list.add(cust.getAttrs());
        /*转换成app专用的json格式*/
       /* list = BaseTurn.ResponseHandle(list, modleStr);*/

        this.rendJsonApp(true, list, 200, "success");
    }


    /**
     * 修改网点信息
     */
    public void alterRetailer() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>(

        );
        String customer_id = getPara("id");
        String customer_name = getPara("retls_name");
        String customer_phone = getPara("retls_phone");
        String customer_contacts_name = getPara("retls_kp");
        String customer_address = getPara("retls_address");
        Customer customer = null;
        Contacts contacts = null;

        if (customer_id != null && customer_name != null && customer_phone != null && customer_address != null && customer_contacts_name != null) {
            customer = Customer.dao.findById(customer_id);
            contacts = Contacts.dao.findFirstByCustomerId(customer_id);

            customer.set("name", customer_name);
            customer.set("telephone", customer_phone);
            customer.set("address", customer_address);

            contacts.set("name", customer_contacts_name);

            /*提交更新*/
            customer.update();
            contacts.update();
            ReturnMsg returnMsg = new ReturnMsg();
            returnMsg.setReturn_msg("success");
            map.put("info", returnMsg);
            list.add(map);
            this.rendJsonApp(true, list, 200, "success");
            return;
        }


        this.rendJsonApp(true, list, 500, "error");
    }

    /**
     * 获取网点列表
     */
    public void retailerList() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> parameter = new HashMap<>();

        /*获取客户端传来的参数*/
        String classify = getPara("classify");
        String sort = getPara("sort");
        String status = getPara("state");
//        if(status == null ){
//            status = "0";
//        }
        String retail_id = getPara("id");
        String page = getPara("page");
        String pagesize = getPara("pagesize");
        String keyword = getPara("keyword");

        /*设置查询参数*/
        parameter.put("company_id", this.getCompanyId());
        parameter.put("user_id", getCurrentUserId());
        parameter.put("keyword", keyword);
        parameter.put("status_app", status);


        parameter.put("classify", classify);
        parameter.put("id", retail_id);
        parameter.put("sort", sort);

        parameter.put("position_id",this.getPositionId());
//        filter.put("position_id", this.getPositionId());


        Page<Customer> customers = Customer.dao.pageGrid(Integer.parseInt(page), Integer.parseInt(pagesize), parameter, 11);
        Map<String, Object> map = null;

        String retls_kp_name = new String();
        Contacts contacts = new Contacts();
        List<Customer> customerList = customers.getList();

        for (Customer customer : customerList) {
            map = customer.getAttrs();
            list.add(map);
        }
        list = BaseTurn.ResponseHandle(list, modleStr);

        for (int i = 0; i < list.size(); i++) {
            contacts = null;
            retls_kp_name = "";
            contacts = Contacts.dao.findFirstByCustomerId((String) list.get(i).get("id"));
            if (contacts != null) {
                retls_kp_name = contacts.get("name");
            }
            list.get(i).put("retls_kp", retls_kp_name);
        }
        this.rendJsonApp(true, list, 200, customers.getTotalPage() > 1 ? CS.ismore_TRUE : CS.ismore_FALSE);
    }

    /**
     * 获取网点列表状态
     */
    public void retailerListStatus() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        String company_id = getPara("com_id");

        List<CustRating> ratings = CustRating.dao.getStatus(company_id);

        for (CustRating rating : ratings) {
            map = rating.getAttrs();
            list.add(map);
        }
        this.rendJsonApp(true, list, 200, "success");
    }

    /**
     * 获取网点分类列表
     */
    public void retailerTypeMainList() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        String company_id = getPara("com_id");

        List<Parame> parames = Parame.dao.getAllOfRetailType(company_id);

        for (Parame parame : parames) {
            map = parame.getAttrs();
            list.add(map);
        }

        this.rendJsonApp(true, list, 200, "success");
    }


    /**
     * 修改网点经纬度
     */
    public void alterRetailLocate() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        String customer_id = getPara("retls_id");
        String retls_latiude = getPara("retls_latiude");
        String retls_longitude = getPara("retls_longitude");
        Customer customer = null;

        if (customer_id != null && retls_latiude != null && retls_longitude != null) {
            customer = Customer.dao.findById(customer_id);
            customer.set("lat", retls_latiude);
            customer.set("lng", retls_longitude);
            /*提交更新*/
            customer.update();
            ReturnMsg returnMsg = new ReturnMsg();
            returnMsg.setReturn_msg("success");
            map.put("info", returnMsg);
            list.add(map);
            this.rendJsonApp(true, list, 200, "success");
            return;
        }


        this.rendJsonApp(true, list, 500, "error");
    }


    /**
     * 百度地图 转换 高德地图坐标
     *
     * @param bd_lat
     * @param bd_lon
     */
    private static void bd_decrypt(double bd_lat, double bd_lon) {

        double x = bd_lon - 0.0066, y = bd_lat - 0.0062;
        double z = sqrt(x * x + y * y) - 0.00002 * sin(y * x_pi);
        double theta = atan2(y, x) - 0.000003 * cos(x * x_pi);
        lat = z * cos(theta);
        lng = z * sin(theta);
    }


}
