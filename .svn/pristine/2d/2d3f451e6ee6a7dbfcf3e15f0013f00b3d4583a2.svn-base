package net.loyin.ctrl.app;

import com.jfinal.plugin.activerecord.Page;
import net.loyin.ctrl.AdminBaseController;
import net.loyin.jfinal.anatation.PowerBind;
import net.loyin.jfinal.anatation.RouteBind;
import net.loyin.model.em.SaleGoal;
import net.loyin.model.fa.PayReceivAbles;
import net.loyin.model.scm.*;
import net.loyin.model.sso.Company;
import net.loyin.model.sso.SnCreater;
import net.loyin.util.BaseTurn;
import net.loyin.util.CS;
import net.loyin.utli.TimeUtil;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by EYKJ on 2015/9/17.
 */
@RouteBind(path = "order")
public class OrderCtrl extends AdminBaseController<Order> {
    private String modelStr = "order";


    public OrderCtrl() {
        this.modelClass = Order.class;
    }

    /**
     * 获取未提交的订单
     */
    public void uOrderList() {
        List<Map<String, Object>> temp = new ArrayList<>();
        // 接收传来的参数
        String userId = this.getPara(CS.DSR_ID);
        if (StringUtils.isBlank(userId)) {
            this.rendJsonApp(false, temp, 203, CS.DSR_ID);//203表示参数缺失
            return;
        }

        String id = this.getPara(CS.ID);//根据订单id来查，如果有传来
        List<Order> orderList = Order.dao.findById(id, this.getCompanyId(), userId, 2, 0);
        Map<String, Object> resultMap = null;
        List<Map<String, Object>> resultList = new ArrayList<>();

        for (Order o : orderList) {
            resultMap = new HashMap<>();
            String[] names = o.getAttrNames();
            for (int i = 0; i < names.length; i++) {
                resultMap.put(names[i], o.get(names[i]));
                resultMap.put("slo_code", o.get("billsn"));
                resultMap.put("retls_name", o.get("customer_name"));
                resultMap.put("retls_id", o.get("customer_id"));

            }
            resultList.add(resultMap);
        }
        /*resultList = BaseTurn.ResponseHandle(resultList, modelStr);*/
        if (resultList.size() != 0) {
            this.rendJsonApp(true, resultList, 200, CS.ismore_FALSE);
            return;
        }
        this.rendJsonApp(false, temp, 404, CS.ismore_FALSE);
    }

    /**
     * 增加未提交订单接口
     */
    public void adduOrder() {
        List temp = new ArrayList<>();
        String dsrid = this.getPara(CS.DSR_ID);
        String retls_id = this.getPara(CS.RETLS_ID);
        if (StringUtils.isBlank(dsrid)) {
            this.rendJsonApp(false, temp, CS.MISS_PARAMS, CS.DSR_ID);
            return;
        }
        if (StringUtils.isBlank(retls_id)) {
            this.rendJsonApp(false, temp, CS.MISS_PARAMS, CS.RETLS_ID);
            return;
        }
        //判断这个网点是否存在未提交订单，如果存在，就不要新增未提交订单 return
        List<Order> orderList = Order.dao.findById(id, this.getCompanyId(), dsrid, 2, 0);
        for (Order order : orderList) {
            if (order.get("customer_id").equals(retls_id)) {//
                //构造返回
                Map<String, Object> map = new HashMap<String, Object>();
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                map.put(CS.RETURN_MSG, order.get("id"));//订单id返回一下
                list.add(map);
                this.rendJsonApp(true, list, CS.SUCCESS, "exit_id");
                return;
            }
        }
        //订单编号
        String sn = SnCreater.dao.create("ORDER2", this.getCompanyId());
        Order order = new Order();
        order.set("billsn", sn);
        order.set("audit_status", 0);
        this.pullUser(order, this.getCurrentUserId());
        order.set("company_id", this.getCompanyId());
        order.set("customer_id", retls_id);
        order.set("sign_date", TimeUtil.getTime());
        order.set("head_id", dsrid);
        order.set("create_datetime", TimeUtil.getTime());
        order.set("creater_id", dsrid);
        order.set("submit_status", 0);
        order.set("is_deleted", 0);
        order.set("ordertype", 2);

        order.save();
        //构造返回
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        map.put(CS.RETURN_MSG, order.getStr("id"));
        list.add(map);
        this.rendJsonApp(true, list, CS.SUCCESS, "order_id");
    }


    /**
     * 新增一个订单详情
     *
     * @return
     */
    public void addOrderInfo() {

        String slo_id = this.getPara(CS.SLO_ID);//订单id
        String dsr_id = this.getPara("dsr_id");
        String merch_id = this.getPara(CS.MERCH_ID);//商品id
        String merch_num = this.getPara(CS.MERCH_NUM);//商品数量
        String merch_price = this.getPara(CS.MERCH_PRICE);//商品价格
        String remark = this.getPara("remarks");//商品价格
        OrderProduct product = new OrderProduct();
        product.put("id", slo_id);
        product.put("ordertype", 0);


        product.put("billsn", slo_id);
        product.put("submit_status", 0);

        product.put("sign_date", TimeUtil.getTime());
        product.put("head_id", dsr_id);
        product.put("create_datetime", TimeUtil.getTime());
        product.put("creater_id", dsr_id);
        product.put("company_id", getCompanyId());
        product.put("product_id", merch_id);
        product.put("sale_price", BigDecimal.valueOf(Double.parseDouble(merch_price)));
        product.put("quoted_price", 0);//报价
        product.put("amount", Double.parseDouble(merch_num));
        product.put("zkl", 0);
        product.put("zhamt", 0);
        product.put("tax_rate", 0);
        product.put("tax", 0);
        product.put("description",remark);
        double amt = Double.parseDouble(merch_price) * Double.parseDouble(merch_num);
        product.put("amt", amt);
        product.save();

        //构造返回
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        map.put(CS.RETURN_MSG, product.getStr("id"));
        list.add(map);
        list = BaseTurn.ResponseHandle(list, modelStr);
        this.rendJsonApp(true, list, CS.SUCCESS, "orderinfo_id");
    }


    /**
     * 修改订单详情
     */
    public void updateOrderInfo() {
        String slo_id = this.getPara("id");//订单id
        String merch_id = this.getPara(CS.MERCH_ID);//商品id
        String merch_num = this.getPara(CS.MERCH_NUM);//商品数量
        String merch_price = this.getPara(CS.MERCH_PRICE);//商品价格
        OrderProduct.dao.updateOrderInfo(slo_id, merch_id, merch_num, merch_price);
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        map.put(CS.RETURN_MSG, "更新成功");
        list.add(map);
       /* list = BaseTurn.ResponseHandle(list, modelStr);*/
        this.rendJsonApp(true, list, CS.SUCCESS, "orderinfo_id");
    }

    /**
     * 根据订单id查询订单详情
     *
     * @return
     */
    public void uOrderInfoList() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        // 接收传来的参数
        String slo_id = this.getPara(CS.SLO_ID);
        /*String dsrid = request.getParameter(CS.DSR_ID);
        List<Map<String, Object>> orderinfoList = service.queryOrderinfoListById(slo_id);*/
        List<OrderProduct> products = OrderProduct.dao.findBySloID(slo_id);
        for (OrderProduct orderProduct : products) {
            map = orderProduct.getAttrs();

            map.put("id", map.get("id"));
            map.put("slo_id", map.get("id"));
            map.put("merch_id", map.get("product_id"));
            map.put("merch_num", map.get("amount"));
            map.put("merch_price", map.get("sale_price"));
            map.put("remarks", map.get("description"));


            Product product = Product.dao.findById(map.get("product_id"));
            String merch_name = product.getStr("name");
            String merch_barcode = product.getStr("billsn");

            map.put("merch_name", merch_name);
            map.put("merch_barcode", merch_barcode);
            list.add(map);
        }
        this.rendJsonApp(true, list, CS.SUCCESS, "success");
    }

    /**
     * 根据id删除订单详情
     *
     * @return
     */
    public void deleteOrderinfoById() {

        List<Map<String, Object>> temp = new ArrayList<>();
        String slo_id = this.getPara("id");//订单id
        String merch_id = this.getPara(CS.MERCH_ID);//商品id

        if (StringUtils.isBlank(slo_id)) {
            this.rendJsonApp(false, temp, CS.MISS_PARAMS, CS.ID);
            return;
        }

        if (StringUtils.isBlank(merch_id)) {
            this.rendJsonApp(false, temp, CS.MISS_PARAMS, CS.ID);
            return;
        }
        OrderProduct orderProduct = OrderProduct.dao.findByOrderIdAndProductId(slo_id, merch_id, this.getCompanyId());
        if (orderProduct != null) {
            orderProduct.dao.deleteByProductId(orderProduct.get("id"), orderProduct.get("product_id"));
        }

        //构造返回
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        map.put(CS.RETURN_MSG, "删除成功");//返回200删除成功
        list.add(map);
        list = BaseTurn.ResponseHandle(list, modelStr);
        this.rendJsonApp(true, list, CS.SUCCESS, "delete_orderinfo");
    }


    /**
     * 删除订单。需要把该订单下的订单详情全部删了
     *
     * @return
     */
    public void deleteOrderById() {
        List<Map<String, Object>> temp = new ArrayList<>();
        String orderId = this.getPara(CS.ID);
        if (StringUtils.isBlank(orderId)) {
            this.rendJsonApp(false, temp, CS.MISS_PARAMS, CS.ID);
            return;
        }
        OrderProduct.dao.deleteById(orderId);
        Order.dao.deleteById(orderId);
        //构造返回
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        map.put(CS.RETURN_MSG, "删除成功");//返回200删除成功
        list.add(map);
        list = BaseTurn.ResponseHandle(list, modelStr);
        this.rendJsonApp(true, list, CS.SUCCESS, "delete_order");
    }


    /**
     * 查询某个网点的历史已提交订单
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public void retailComfirmOrderList() {
        List<Map<String, Object>> temp = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        // 接收传来的参数
        String dsrid = this.getPara(CS.DSR_ID);
        String retailid = this.getPara(CS.RETLS_ID);//网点id
        String pagesize = this.getPara(CS.PAGE_SIZE);
        String page = this.getPara(CS.PAGE);
        String retlsId = this.getPara("retls_id");
        //检验参数
        if (StringUtils.isBlank(dsrid)) {
            this.rendJsonApp(true, temp, 203, CS.DSR_ID);
            return;
        }
        if (StringUtils.isBlank(page)) {
            this.rendJsonApp(true, temp, 203, CS.PAGE);
            return;
        }
        if (StringUtils.isBlank(pagesize)) {
            this.rendJsonApp(true, temp, 203, CS.PAGE_SIZE);
            return;
        }
        if (StringUtils.isBlank(retailid)) {
            this.rendJsonApp(true, temp, 203, CS.RETLS_ID);
            return;
        }
        // ordertype
        //company_id is_deleted user_id position_id

        Map<String, Object> filter = new HashMap<>();
        filter.put("company_id", this.getCompanyId());
        filter.put("is_deleted", 0);
        filter.put("retls_id", retlsId);
        filter.put("submit_type", "1");
        filter.put("user_id", this.getCurrentUserId());
        filter.put("position_id", this.getCurrentUser().getStr("position_id"));
        filter.put("ordertype", 2);

        Page<Order> orderPage = Order.dao.page(Integer.parseInt(page), Integer.parseInt(pagesize), filter, 0);
        if (orderPage.getList().size() == 0) {
            this.rendJsonApp(false, temp, 203, CS.ismore_FALSE);
            return;
        }
        OrderSend orderSend = null;
        List<Order> orders = orderPage.getList();
        List resultList = new ArrayList<>();
        for (Order o : orders) {
            orderSend = OrderSend.dao.getOrderSend(o.get("id"));
            map = o.getAttrs();
            map.put("date", o.get("sign_date"));
            map.put("address", orderSend.get("address"));
            map.put("contact_name", orderSend.get("people"));
            map.put("contact_phone", orderSend.get("phone"));
            map.put("remarks", "");
            map.put("slo_time", o.get("sign_date"));
            map.put("slo_code", o.get("billsn"));
            map.put("retls_name", o.get("customer_name"));
            map.put("retls_id", o.get("customer_id"));
            list.add(map);
            /*String[] names = o.getAttrNames();
            Map<String, String> resultMap = new HashMap<>();

            for (int i = 0; i < names.length; i++) {
                resultMap.put(names[i], o.getStr(names[i]));
            }
            resultList.add(resultMap);*/
        }
        /*list = BaseTurn.ResponseHandle(list, modelStr);*/
        boolean ismore = orderPage.getTotalPage() > 1 ? true : false;//是否有更多
        this.rendJsonApp(true, list, CS.SUCCESS, ismore ? CS.ismore_TRUE : CS.ismore_FALSE);
    }


    /**
     * 查询历史已提交订单
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public void dsrComfirmOrderList() {
        List<Map<String, Object>> temp = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        // 接收传来的参数
        String dsrid = this.getPara(CS.DSR_ID);
        String pagesize = this.getPara(CS.PAGE_SIZE);
        String page = this.getPara(CS.PAGE);
        //检验参数
        if (StringUtils.isBlank(dsrid)) {
            this.rendJsonApp(true, temp, 203, CS.DSR_ID);
            return;
        }
        if (StringUtils.isBlank(page)) {
            this.rendJsonApp(true, temp, 203, CS.PAGE);
            return;
        }
        if (StringUtils.isBlank(pagesize)) {
            this.rendJsonApp(true, temp, 203, CS.PAGE_SIZE);
            return;
        }


        Map<String, Object> filter = new HashMap<>();
        filter.put("company_id", this.getCompanyId());
        filter.put("is_deleted", 0);
        filter.put("submit_type", "1");
        filter.put("user_id", this.getCurrentUserId());
        filter.put("position_id", this.getCurrentUser().getStr("position_id"));
        filter.put("ordertype", 2);

        Page<Order> orderPage = Order.dao.page(Integer.parseInt(page), Integer.parseInt(pagesize), filter, 0);
        if (orderPage.getList().size() == 0) {
            this.rendJsonApp(false, temp, 203, CS.ismore_FALSE);
            return;
        }
        OrderSend orderSend = null;
        List<Order> orders = orderPage.getList();
        List resultList = new ArrayList<>();
        for (Order o : orders) {

            orderSend = OrderSend.dao.getOrderSend(o.get("id"));

            map = o.getAttrs();


            map.put("date", o.get("sign_date"));
            map.put("address", orderSend.get("address"));
            map.put("contact_name", orderSend.get("people"));
            map.put("contact_phone", orderSend.get("phone"));
            map.put("remarks", "");


            map.put("slo_time", o.get("sign_date"));
            map.put("slo_code", o.get("billsn"));
            map.put("retls_name", o.get("customer_name"));
            map.put("retls_id", o.get("customer_id"));
            list.add(map);

            /*String[] names = o.getAttrNames();
            Map<String, String> resultMap = new HashMap<>();

            for (int i = 0; i < names.length; i++) {
                resultMap.put(names[i], o.getStr(names[i]));
            }
            resultList.add(resultMap);*/
        }
        /*list = BaseTurn.ResponseHandle(list, modelStr);*/
        boolean ismore = orderPage.getTotalPage() > 1 ? true : false;//是否有更多
        this.rendJsonApp(true, list, CS.SUCCESS, ismore ? CS.ismore_TRUE : CS.ismore_FALSE);
    }

    /**
     * 提交订单
     */
    public void updateOrder() {
        List<Map<String, Object>> temp = new ArrayList<>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String orderId = this.getPara(CS.ID);
        if (orderId.isEmpty()) {
            this.rendJsonApp(false, temp, 400, "订单ID缺失");
            return;
        }
        /*Order order = Order.dao.findById(orderId, this.getCompanyId());*/
        Order order = Order.dao.findById(orderId);
        OrderSend orderSend = new OrderSend();
        orderSend.set("order_id", order.getStr("id"));
        orderSend.set("people", this.getPara(CS.CONTACT_NAME));
        orderSend.set("phone", this.getPara(CS.CONTACT_PHONE));
        orderSend.set("address", this.getPara(CS.ADDRESS));
        orderSend.save();
        this.submit(order.getStr("id"),list);
//        order.set("submit_status", 1);
//        order.update();
        this.rendJsonApp(true, list, 200, "提交成功");
    }

    /**
     * 提交
     */
    @PowerBind(code = {"A2_1_E", "A3_1_E"}, funcName = "提交")
    public void submit(String orderId,List<Map<String, Object>> list) {
        try {
            this.getId();
            String company_id = this.getCompanyId();
            Order order = Order.dao.findByIdOnlyOrder(orderId, company_id);
            if (order == null) {
                this.rendJsonApp(true, list, 200, "数据不存在");
                return;
            }
            Company company = Company.dao.qryCacheById(company_id);
            Map<String, Object> config = company.getConfig();
            Integer ordertype = order.getInt("ordertype");
            if ((ordertype < 2)//如果是采购订单 采购退货 直接生成应收应付单
                    || (ordertype == 2 && "false".equals(config.getOrDefault("p_sale_audit", "false")))//销售订单审核 false
                    || (ordertype == 3 && "false".equals(config.getOrDefault("p_saletui_audit", "false")))) {//销售退货审核 false
                String now = dateTimeFormat.format(new Date());
                PayReceivAbles.dao.createFromOrder(order, now);
                //生成对应的出入库单
                StorageBill.dao.createFromOrder(order, now, 0);
                //更新销售目标
                String[] s = now.split("-");
                if (ordertype == 2 || ordertype == 3) {//销售或退货
                    BigDecimal zero = new BigDecimal(0);
                    BigDecimal amt = order.getBigDecimal("order_amt");
                    if (ordertype == 3)
                        amt = zero.subtract(amt);
                    SaleGoal.dao.updateVal(order.getStr("head_id"), Integer.parseInt(s[1]), Integer.parseInt(s[0]), amt);
                }
                order.set("audit_status", 2);//设置为审核通过
                order.set("submit_status", 1);//设置为提交状态
            } else {
                if (ordertype == 3) {
                    if ("3".equals(config.getOrDefault("p_saletui_audit_type", "0"))) {
                        order.set("auditor_id", config.get("p_financetui"));
                    }
                } else if (ordertype == 2) {
                    if ("3".equals(config.getOrDefault("p_sale_audit_type","0"))) {
                        order.set("auditor_id", config.get("p_finance"));
                    }
                }
                order.set("audit_status", 1);//设置为提交状态
            }
            order.update();
            this.rendJsonApp(true, list, 200, "提交成功");
        } catch (Exception e) {
            log.error("提交订单异常", e);
            this.rendJsonApp(false, list, 400, "提交订单异常");
        }
        //{"p_custpool_c":"90","p_finance":"L0Ua","p_financetui":"L0Ua","p_member_card_pay":"false","p_pact_alarm":"30","p_sale_audit":"true","p_sale_audit_type":"3","p_saletui_audit":"true","p_saletui_audit_type":"3","p_sysname":""}
    }




    /**
     * 根据订单id获取总价
     */
    public void getOrderTotlePrice() {
        List<Map<String, Object>> temp = new ArrayList<>();
        String sloId = this.getPara(CS.SLO_ID);
        String dsrId = this.getPara(CS.DSR_ID);
        Order order = Order.dao.findById(sloId, this.getCompanyId());
        if (order == null) {
            this.rendJsonApp(false, temp, 404, "订单缺失");
            return;
        }
        List list = new ArrayList<>();
        Map<String, String> result = new HashMap<>();
        result.put("return_msg", order.get("order_amt"));
        list.add(result);
        this.rendJsonApp(true, list, 200, "orderAmt");
    }

    /**
     * by chenjianhui
     * 2015年9月26日15:04:32
     * 计算总价
     */
    public void calculateOrdertTotalPrice() {
        List<Map<String, Object>> temp = new ArrayList<>();
        String sloId = this.getPara(CS.SLO_ID);
        List<OrderProduct> products = OrderProduct.dao.findBySloID(sloId);
        if (products == null) {
            this.rendJsonApp(false, temp, 404, "订单缺失");
            return;
        }
        /**订单ID已经是唯一了 所有不用加company_id了*/
        /**2015年10月8日15:20:20*/
        Order order = Order.dao.findById(sloId);
        Double sum = 0.0;

        for (OrderProduct orderProduct : products) {
            BigDecimal count = new BigDecimal(orderProduct.get("amount").toString());
            BigDecimal price = new BigDecimal(orderProduct.get("sale_price").toString());
            sum += price.multiply(count).doubleValue();
        }


        order.set("order_amt", sum);
        order.update();


        List list = new ArrayList<>();
        Map<String, String> result = new HashMap<>();
        result.put("return_msg", order.get("order_amt"));
        list.add(result);
        this.rendJsonApp(true, list, 200, "orderAmt");
    }


    public void searchHistoryOrder() {
        String dsrId = "";
    }


}
