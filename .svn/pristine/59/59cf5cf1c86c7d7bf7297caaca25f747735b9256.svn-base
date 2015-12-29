package net.loyin.ctrl.app;

import net.loyin.ctrl.AdminBaseController;
import net.loyin.jfinal.anatation.RouteBind;
import net.loyin.model.ReturnMsg;
import net.loyin.model.crm.*;
import net.loyin.util.BaseTurn;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by chenjianhui on 2015/9/15.
 */
@RouteBind(path = "visit")
public class visitCtrl extends AdminBaseController<BaseTurn> {
    private String modleStr = "contact_record";


    public visitCtrl() {
        this.modelClass = BaseTurn.class;
    }

    /**
     * 查找客户访问列表
     */
    public void visitList() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> parameter = new HashMap<>();

        String create_id = getPara("dsr_id");
        String retail_id = getPara("retls_id");

        parameter.put("create_id", create_id);
        parameter.put("retail_id", retail_id);
        parameter.put("company_id", getCompanyId());

        List<ConcatRecord> records = ConcatRecord.dao.getList(parameter);
        Map<String, Object> map = null;

        String retls_kp_name = new String();
        Contacts contacts = new Contacts();

        for (ConcatRecord record : records) {
            map = record.getAttrs();
            Customer customer = Customer.dao.findById(map.get("customer_id"));
            if (customer != null) {
                map.put("retls_name", customer.get("name"));
            }
            map.put("visit_time", map.get("concat_datetime"));
            map.put("retls_id", map.get("customer_id"));
            map.put("dsr_id", map.get("creater_id"));
            map.put("visit_text", map.get("content"));
            map.put("subject", map.get("subject"));
            list.add(map);
        }

        /*list = BaseTurn.ResponseHandle(list, modleStr);*/
        this.rendJsonApp(true, list, 200, "success");

    }


    /**
     * 新增网点拜访记录
     */
    public void addVisit() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();


        String creater_id = getPara("dsr_id");
        String company_id = getPara("com_id");
        String subject = getPara("subject");
        String customer_id = getPara("retls_id");
        String content = getPara("visit_text");


        Contacts contacts = Contacts.dao.findFirstByCustomerId(customer_id);
        String contacts_id = contacts.get("id");
        String concat_type = "L0KX";
        String sale_stage = "L0qp";
        String cust_status = "L0qC";

        Date dt = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String create_datetime = "";
        create_datetime = df.format(dt);


        ConcatRecord concatRecord = new ConcatRecord();

        concatRecord.set("concat_datetime", create_datetime);
        concatRecord.set("customer_id", customer_id);
        concatRecord.set("contacts_id", contacts_id);
        concatRecord.set("concat_type", concat_type);
        concatRecord.set("sale_stage", sale_stage);
        concatRecord.set("cust_status", cust_status);

        concatRecord.set("subject", subject);
        concatRecord.set("content", content);
        concatRecord.set("create_datetime", create_datetime);
        concatRecord.set("creater_id", creater_id);
        concatRecord.set("company_id", company_id);
        concatRecord.save();


        list.add(concatRecord.getAttrs());

        list = BaseTurn.ResponseHandle(list, modleStr);

        this.rendJsonApp(true, list, 200, "success");
    }


    /**
     * by chenjianhui
     * 2015年9月28日15:43:32
     * 根据关键字查找拜访记录
     */
    public void searchVisit() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> parameter = new HashMap<>();
        String create_id = getPara("dsr_id");
        String keyWord = getPara("search_content");

        parameter.put("create_id", create_id);
        parameter.put("keyWord", keyWord);
        parameter.put("company_id", getCompanyId());

        List<ConcatRecord> records = ConcatRecord.dao.getSerchList(parameter);
        Map<String, Object> map = null;

        String retls_kp_name = new String();
        Contacts contacts = new Contacts();

        for (ConcatRecord record : records) {
            map = record.getAttrs();
            Customer customer = Customer.dao.findById(map.get("customer_id"));
            if (customer != null) {
                map.put("retls_name", customer.get("name"));
            }
            map.put("visit_time", map.get("concat_datetime"));
            map.put("retls_id", map.get("customer_id"));
            map.put("dsr_id", map.get("creater_id"));
            map.put("visit_text", map.get("content"));
            map.put("subject", map.get("subject"));
            list.add(map);
        }

        this.rendJsonApp(true, list, 200, "success");
    }

}
