package net.loyin.ctrl.scm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.loyin.ctrl.AdminBaseController;
import net.loyin.ctrl.sso.UserCtrl;
import net.loyin.jfinal.anatation.PowerBind;
import net.loyin.jfinal.anatation.RouteBind;
import net.loyin.model.crm.Customer;
import net.loyin.model.fa.PayReceivAbles;
import net.loyin.model.scm.*;
import net.loyin.model.sso.Company;
import net.loyin.model.sso.SnCreater;

import net.loyin.model.sso.User;
import org.apache.commons.lang3.StringUtils;

import com.jfinal.plugin.activerecord.Record;

/**
 * 出入库
 *
 * @author liugf 风行工作室 2014年9月29日
 */
@RouteBind(path = "storageBill", sys = "仓库", model = "出入库")
public class StorageBillCtrl extends AdminBaseController<StorageBill> {
    public StorageBillCtrl() {
        this.modelClass = StorageBill.class;
    }

    public void dataGrid() {
        Map<String, String> userMap = this.getUserMap();
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put("company_id", userMap.get("company_id"));
        filter.put("keyword", this.getPara("keyword"));
        filter.put("start_date", this.getPara("start_date"));
        filter.put("end_date", this.getPara("end_date"));
        filter.put("status", this.getParaToInt("status"));
        filter.put("type", this.getParaToInt("type"));
        filter.put("uid", this.getPara("uid"));
        filter.put("user_id", userMap.get("uid"));
        filter.put("is_deleted", this.getParaToInt("is_deleted"));
        filter.put("position_id", userMap.get("position_id"));
        this.sortField(filter);
        this.rendJson(true, null, "", StorageBill.dao.page(this.getPageNo(), this.getPageSize(), filter, this.getParaToInt("qryType")));
    }

    /**
     * 库存流水
     */
    public void flowDataGrid() {
        Map<String, String> userMap = this.getUserMap();
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put("company_id", userMap.get("company_id"));
        filter.put("keyword", this.getPara("keyword"));
        filter.put("start_date", this.getPara("start_date"));
        filter.put("end_date", this.getPara("end_date"));
        filter.put("type", this.getParaToInt("type"));
        this.sortField(filter);
        this.rendJson(true, null, "", StorageBill.dao.flowPage(this.getPageNo(), this.getPageSize(), filter));
    }

    @PowerBind(code = "A4_1_E", funcName = "删除")
    public void del() {
        try {
            getId();
            if (StorageBill.dao.isSubmit(id)) {
                this.rendJson(false, null, "已经存在已提交的盘点单，请重新选择需要删除的数据！");
                return;
            }
            StorageBill.dao.del(id, this.getCompanyId());
            rendJson(true, null, "删除成功！", id);
        } catch (Exception e) {
            log.error("删除异常", e);
            rendJson(false, null, "删除失败！请检查是否被使用！");
        }
    }

    public void qryOp() {
        getId();
//        Map map = new HashMap<>();
//        map.put("realAmount","0");
//        map.put("differenceAmount","0");
        StorageBill m = StorageBill.dao.findById(id, this.getCompanyId());
        if (m != null) {
            if (m.getStr("creater_name") == null) {
                m.put("creater_name", "系统");
            }
            String orderId = m.getStr("order_id");
            List<OrderProduct> orderProductList = null;
            if(orderId != null){
                orderProductList  = OrderProduct.dao.list(orderId);
            }
            List<StorageBillList> productlist = StorageBillList.dao.list(id);
            List resultList = new ArrayList<>();
            for (StorageBillList storageBillList : productlist) {
                Map map = new HashMap<>();
                if(orderProductList != null){
                    for(OrderProduct o : orderProductList){
                        if(o.getStr("product_id").equals(storageBillList.getStr("product_id"))){
                            map.put("price", o.getBigDecimal("sale_price").doubleValue());
                            map.put("realPrice", o.getBigDecimal("amt").doubleValue());
                        }
                    }
                }

                map.put("realAmount", "0");
                map.put("differenceAmount", "0");
//                map.put("price", "0");
                String[] names = storageBillList.getAttrNames();
                for (String name : names) {
                    map.put(name, storageBillList.get(name));
                }
                resultList.add(map);
            }
            m.put("productlist", resultList);
            m.put("productlistlength", productlist.size());
            this.rendJson(true, null, "", m);
        } else
            this.rendJson(false, null, "记录不存在！");
    }

    @PowerBind(code = "A4_1_E", funcName = "回收站")
    public void trash() {
        try {
            getId();
            if (StorageBill.dao.isSubmit(id)) {
                this.rendJson(false, null, "已经存在已提交的出入库单，请重新选择需要删除的数据！");
                return;
            }
            StorageBill.dao.trash(id, this.getCurrentUserId(), this.getCompanyId(), dateTimeFormat.format(new Date()));
            rendJson(true, null, "移动到回收站成功！", id);
        } catch (Exception e) {
            log.error("移动到回收站异常", e);
            rendJson(false, null, "移动到回收站失败！");
        }
    }

    /**
     * 恢复
     */
    @PowerBind(code = "A4_1_E", funcName = "恢复")
    public void reply() {
        try {
            getId();
            StorageBill.dao.reply(id, this.getCompanyId());
            rendJson(true, null, "恢复成功！", id);
        } catch (Exception e) {
            log.error("恢复异常", e);
            rendJson(false, null, "恢复失败！");
        }
    }

    /**
     * 提交
     */
    @PowerBind(code = "A4_1_E", funcName = "提交")
    public void submit() {
        try {
            getId();
            if (StorageBill.dao.isSubmit(id)) {
                this.rendJson(false, null, "已经提交了，请勿重复提交！");
                return;
            }
            String company_id = this.getCompanyId();
//            StorageBill storageBill = StorageBill.dao.findById(id, company_id);
            StorageBill po = this.getModel();
            String depot_id = po.getStr("depot_id");
            String orderId = po.getStr("order_id");
            Order order = Order.dao.findById(orderId, this.getCompanyId());
            String status = "1";
            if (getParaMap().containsKey("submitType") && getPara("submitType").equals("0")) { //退回订单
                List<StorageBillList> lists = StorageBillList.dao.list(id);
                for (StorageBillList s : lists) {
                    s.delete();
                }
                order.set("audit_status", 0);
                order.set("submit_status", 0);//设置为提交状态
                order.update();
                po.delete();
                rendJson(true, null, "提交操作成功！");
                return;
            } else if (getParaMap().containsKey("submitType") && getPara("submitType").equals("1")) { //推进订单
                if (StringUtils.isEmpty(depot_id)) {
                    this.rendJson(false, null, "未选择仓库！");
                    return;
                }
                status = "4";
                String pname = "productlist";
                Integer productlistlength = this.getParaToInt("productlistlength");
                List<Map<String, Object>> productlist = new ArrayList<Map<String, Object>>();
                List<Map<String, Object>> productlistAdd = new ArrayList<Map<String, Object>>();
                String[] index = (String[]) this.getParaMap().get("productIndex[]");
//                List<Map<String, Object>> productlistIndex = this.getPa
                Map<String, Object> attr = null;
                for (int i = 0; i < productlistlength; i++) {
                    if (this.getAttr(pname + "[" + i + "][amount]").equals("0")) {
                        attr = new HashMap<String, Object>();
                        attr.put("product_id", this.getAttr(pname + "[" + i + "][product_id]"));  //产品ID
                        attr.put("amount", this.getAttr(pname + "[" + i + "][realAmount]"));//实际数量
                        attr.put("differenceAmount", this.getAttr(pname + "[" + i + "][differenceAmount]")); //差异数量
                        attr.put("price", this.getAttr(pname + "[" + i + "][price]"));//实际单价
                        attr.put("remark", this.getAttr(pname + "[" + i + "][remark]"));//备注
                        for (int m = 0; m < index.length; m++) {
                            if (index[m].equals(this.getAttr(pname + "[" + i + "][billsn]"))) {
                                attr.put("product_sort", m); //排序
                            }
                        }
                        productlistAdd.add(attr);
                    } else {
                        attr = new HashMap<String, Object>();
                        attr.put("product_id", this.getAttr(pname + "[" + i + "][product_id]")); //产品ID
                        attr.put("amount", this.getAttr(pname + "[" + i + "][realAmount]"));//实际数量
                        attr.put("differenceAmount", this.getAttr(pname + "[" + i + "][differenceAmount]")); //差异数量
                        attr.put("price", this.getAttr(pname + "[" + i + "][price]"));//实际单价
                        attr.put("remark", this.getAttr(pname + "[" + i + "][remark]"));//备注
                        for (int m = 0; m < index.length; m++) {
                            if (index[m].equals(this.getAttr(pname + "[" + i + "][billsn]"))) {
                                attr.put("product_sort", m); //排序
                            }
                        }
                        productlist.add(attr);
                    }

                }
                if (orderId != null) {
                    int orderType = order.getInt("ordertype");
                    if (orderType >= 2) {
                        Double orderSum = 0.0;
                        List<OrderProduct> orderProducts = OrderProduct.dao.list(orderId);
                        boolean temp = orderProducts.size() != 0 ? orderProducts.get(0).delete() : false;
                        for (OrderProduct o : orderProducts) {//todo 差异修改
                            for (Map<String, Object> s : productlist) {
                                if (o.getStr("product_id").equals(s.get("product_id").toString())) {
                                    OrderProduct newOrderProduct = o;
                                    // 由于业务需求的改动，在校验出库单的时候是可进行价格修改的，原需求 需求来至 老板
                                    // orderSum += Double.parseDouble(o.get("sale_price").toString()) * Double.parseDouble(s.get("amount").toString());
                                    //o.set("amt", Double.parseDouble(o.get("sale_price").toString()) * Double.parseDouble(s.get("amount").toString()));
                                    //o.set("amount", Double.parseDouble(s.get("amount").toString()));
                                    //需求改动开始
                                    orderSum += Double.parseDouble(s.get("price").toString()) * Double.parseDouble(s.get("amount").toString());
                                    o.set("sale_price",Double.parseDouble(s.get("price").toString()));
                                    o.set("amt", Double.parseDouble(s.get("price").toString()) * Double.parseDouble(s.get("amount").toString()));
                                    o.set("amount", Double.parseDouble(s.get("amount").toString()));
                                    o.save();
                                }
                            }
                        }
                        for (Map<String, Object> s : productlistAdd) {
                            OrderProduct newOrderProduct = new OrderProduct();
                            orderSum += Double.parseDouble(String.valueOf(s.get("price"))) * Double.parseDouble(s.get("amount").toString());
                            newOrderProduct.set("id", orderId);
                            newOrderProduct.set("product_id", s.get("product_id").toString());
                            newOrderProduct.set("sale_price", Double.parseDouble(String.valueOf(s.get("price"))));
                            newOrderProduct.set("amount", Double.parseDouble(s.get("amount").toString()));
                            newOrderProduct.set("zkl", 0.0);
                            newOrderProduct.set("zhamt", 0.0);
                            newOrderProduct.set("amt", Double.parseDouble(String.valueOf(s.get("price"))) * Double.parseDouble(s.get("amount").toString()));
                            newOrderProduct.set("description", "");
                            newOrderProduct.set("quoted_price", 0);
                            newOrderProduct.set("tax_rate", 0);
                            newOrderProduct.set("tax", 0);
                            newOrderProduct.save();
                        }
                        Double rebate_amt = orderSum * Double.parseDouble(order.get("rebate").toString()) / 100;
                        order.set("id", orderId);
                        order.set("rebate_amt", rebate_amt);
                        order.set("order_amt", new BigDecimal(orderSum - rebate_amt));
                        order.update();
                        productlist.addAll(productlistAdd);
                        StorageBillList.dao.insert(productlist, id);
                        PayReceivAbles.dao.createFromOrder(order, dateTimeFormat.format(new Date()));
                    } else {
                        Double amount = 0.0;
                        List<Map<String, Object>> differenceList = new ArrayList<>();
                        for (Map<String, Object> s : productlist) {
                            if (Integer.parseInt(s.get("differenceAmount").toString()) < 0) {
                                OrderProduct orderProduct = OrderProduct.dao.findByOrderIdAndProductId(orderId, String.valueOf(s.get("product_id")), this.getCompanyId());
                                if (orderProduct == null) {
                                    continue;
                                }
                                orderProduct.set("amount", Math.abs(Integer.parseInt(s.get("differenceAmount").toString())));
                                Double zkl = orderProduct.get("zkl");
                                Double purchasePrice = new BigDecimal(orderProduct.get("purchase_price").toString()).doubleValue();
                                Double zhamt = purchasePrice * zkl;
                                Double amt = (purchasePrice - zhamt) * Math.abs(Integer.parseInt(s.get("differenceAmount").toString()));
                                amount += amt;
                                s.put("purchase_price", String.valueOf(purchasePrice));
                                s.put("zkl", String.valueOf(zkl));
                                s.put("zhamt", String.valueOf(zhamt));
                                s.put("amt", String.valueOf(amt));
                                s.put("amount", String.valueOf(Math.abs(Integer.parseInt(s.get("differenceAmount").toString()))));
                                s.put("description", orderProduct.get("description").toString());
                                s.put("tax_rate", orderProduct.get("tax_rate").toString());
                                s.put("tax", orderProduct.get("tax").toString());
                                differenceList.add(s);
                            } else if (Integer.parseInt(s.get("differenceAmount").toString()) > 0) {
                                this.rendJson(false, null, "数量存在差异，请核对！");
                                return;
                            }
                        }
                        if (differenceList.size() != 0) {
                            savePurchaseBackOrder(orderId, differenceList, amount);
                        }
                    }
                    String priceType = orderType < 2 ? "his_purchase_price" : "his_price";
                    String sellType = orderType < 2 ? "purchase_price" : "sale_price";
//                    productlist.addAll(productlistAdd);
                    for (Map<String, Object> s : productlist) {
                        ProductHis productHis = ProductHis.dao.getProductHisBySomeId(this.getCompanyId(), String.valueOf(s.get("product_id")), order.getStr("customer_id"));
                        OrderProduct orderProducts = OrderProduct.dao.findByOrderIdAndProductId(orderId, String.valueOf(s.get("product_id")), this.getCompanyId());
                        if (productHis == null) {
                            productHis = new ProductHis();
                            productHis.set("company_id", this.getCompanyId());
                            productHis.set("product_id", s.get("product_id"));
                            productHis.set("customer_id", order.getStr("customer_id"));
                            productHis.set(priceType, orderProducts.get(sellType).toString());
                            productHis.save();
                        } else {
                            productHis.set(priceType, orderProducts.get(sellType).toString());
                            productHis.update();
                        }
                    }
                } else {
                    for (Map<String, Object> s : productlist) {
                        if (Integer.parseInt(s.get("differenceAmount").toString()) != 0) {
                            this.rendJson(false, null, "由于没有对应订单，所以数量存在差异时无法提交，请核对！");
                            return;
                        }
                    }
                }
            }
            StorageBill.dao.submit(id, this.getCompanyId(), company_id, dateFormat.format(new Date()), dateTimeFormat.format(new Date()), status, po);
            rendJson(true, null, "提交操作成功！", id);

        } catch (Exception e) {
            log.error("提交异常", e);
            rendJson(false, null, "操作失败！");
        }
    }

    // 出库单状态
    public void ok() {
        try {
            getId();
            if (StorageBill.dao.isSubmit(id)) {
                this.rendJson(false, null, "已经提交了，请勿重复提交！");
                return;
            }
            StorageBill po = StorageBill.dao.findById(id, this.getCompanyId());
            if (po != null) {
                po.set("complete_time", dateFormat.format(new Date()));
                po.set("status", 1);
                po.update();
            }
            rendJson(true, null, "提交成功！");
        } catch (Exception e) {
            log.error("提交异常", e);
            rendJson(false, null, "操作失败！");
        }
    }


    @PowerBind(code = "A4_1_E", funcName = "编辑")
    public void save() {
        try {
            StorageBill po = (StorageBill) getModel();
            if (po == null) {
                this.rendJson(false, null, "提交数据错误！");
                return;
            }
            getId();
            String company_id = this.getCompanyId();
            String pname = "productlist";
            Integer productlistlength = this.getParaToInt("productlistlength");
            List<Map<String, Object>> productlist = new ArrayList<Map<String, Object>>();

            for (int i = 0; i < productlistlength; i++) {
                Map<String, Object> attr = new HashMap<String, Object>();
                attr.put("product_id", this.getAttr(pname + "[" + i + "][product_id]"));
                attr.put("amount", this.getAttr(pname + "[" + i + "][amount]"));
                attr.put("remark", this.getAttr(pname + "[" + i + "][remark]"));
                productlist.add(attr);
            }

            this.pullUser(po, this.getCurrentUserId());
            String sn = "";
            Integer type = this.getParaToInt("type");
            if (StringUtils.isEmpty(id)) {
                sn = SnCreater.dao.create(type > 3 ? "OUTSTOCK" : "INSTOCK", company_id);
                po.set("billsn", sn);
                po.set("company_id", company_id);
                po.save();
                id = po.getStr("id");
            } else {
                po.update();
            }
            Map<String, String> data = new HashMap<String, String>();
            data.put("id", id);
            data.put("sn", po.getStr("billsn"));
            StorageBillList.dao.insert(productlist, id);
            this.rendJson(true, null, "操作成功！", data);
        } catch (Exception e) {
            log.error("保存订单异常", e);
            this.rendJson(false, null, "保存数据异常！");
        }
    }

    /**
     * 调拨明细表
     */
    public void rptList() {
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put("product_id", this.getPara("product_id"));
        filter.put("product_billsn",this.getPara("product_billsn"));
        filter.put("head_id", this.getPara("head_id"));
        filter.put("out_depot_id", this.getPara("out_depot_id"));
        filter.put("to_depot_id", this.getPara("to_depot_id"));
        filter.put("start_date", this.getPara("start_date"));
        filter.put("end_date", this.getPara("end_date"));
        filter.put("company_id", this.getCompanyId());
        List<Record> list = StorageBill.dao.rptList(filter);
        this.rendJson(true, null, "", list);
    }

    //获取出库单/拣货单打印信息
    public void qryPrint() {
        getId();
        StorageBill storageBill = StorageBill.dao.findById(id, this.getCompanyId());

        Company company = Company.dao.qryCacheById(this.getCompanyId());
        Map map = new HashMap<>();
        map.put("companyName", company.get("name"));
        map.put("phone", company.get("telephone"));
        map.put("addree", company.get("address"));
        Order order = Order.dao.findById(storageBill.get("order_id"));
        if (order != null) {
            String head_id = User.dao.findById(order.get("head_id"), this.getCompanyId()).getStr("realname");
            map.put("head_id", head_id);
            map.put("user_id", this.getCurrentUser().get("realname"));
            map.put("order_id", order.getStr("billsn"));
            OrderSend orderSend = OrderSend.dao.getOrderSend(order.get("id"));
            Customer customer = Customer.dao.findById(order.get("customer_id"), this.getCompanyId());
            map.put("customer＿dname", customer.get("name"));
            map.put("customer＿name", orderSend == null ? "" : orderSend.get("people"));
            map.put("customer_phone", orderSend == null ? "" : orderSend.get("phone"));
            map.put("customer_address", orderSend == null ? "" : orderSend.get("address"));
            map.put("customer_date", order.getStr("delivery_date"));
            map.put("customer_remark", "");
            map.put("order_amt",order.getNumber("order_amt"));
        } else {
            String head_id = User.dao.findById(storageBill.get("head_id"), this.getCompanyId()).getStr("realname");
            map.put("head_id", head_id);
            map.put("user_id", this.getCurrentUser().get("realname"));
        }
        String orderBy = this.getPara("orderBy");
        List<StorageBillList> storageBillLists = null;
        if (orderBy != null) {
            storageBillLists = StorageBillList.dao.listOrderCategory(storageBill.get("id"));
        } else {
            storageBillLists = StorageBillList.dao.list(storageBill.get("id"));
        }

        Map productMap = null;
        List productList = new ArrayList<>();
        List productListAll = new ArrayList<>();
        List productListTemp = new ArrayList<>();
        for (StorageBillList storageBillList : storageBillLists) {
            productMap = new HashMap<>();
            OrderProduct orderProduct = OrderProduct.dao.findByOrderIdAndProductId(order.getStr("id"), storageBillList.getStr("product_id"), this.getCompanyId());
            productMap.put("product_name", storageBillList.getStr("product_name"));
            productMap.put("brand", storageBillList.getStr("brand"));//
            productMap.put("categorychina", storageBillList.getStr("categorychina"));//
            productMap.put("product_barcode", storageBillList.getStr("billsn"));//
            productMap.put("unit", storageBillList.get("amount"));
            productMap.put("unitChina", storageBillList.get("unitChina"));
            productMap.put("remark", orderProduct.get("description"));
            productMap.put("sale_price", orderProduct.get("sale_price"));
            productList.add(productMap);
            if (productList.size() == 31) {
                productListTemp.add(productList);
                productListAll.addAll(productList);
                productList = new ArrayList<>();
            }
        }
        productListAll.addAll(productList);
        if (productList.size() != 0) {
            productListTemp.add(productList);
        }
        map.put("productListAll", productListTemp);
        map.put("productList", productListAll);
        this.rendJson(true, null, "", map);
    }

    //获取出库单/拣货单打印信息
    public void qryOutPrint() {
        getId();
        Map map = new HashMap<>();
        StorageBill storageBill = StorageBill.dao.findById(id, this.getCompanyId());
//    depot_id
//    Depot depot = Depot.dao.findById(storageBill.getStr(""), this.getCompanyId());
        map.put("depot", storageBill.getStr("depot_name"));
        Company company = Company.dao.qryCacheById(this.getCompanyId());
        map.put("companyName", company.get("name"));
        map.put("companyAddress", company.getStr("address"));
        map.put("telephone", company.getStr("telephone"));
        map.put("fax", company.getStr("fax"));
        Order order = Order.dao.findById(storageBill.get("order_id"));
        map.put("order_sn", order.getStr("billsn"));
        User user = User.dao.findById(order.get("head_id"), this.getCompanyId());
        map.put("head_id", user.getStr("realname"));
        map.put("head_phone", user.getStr("telephone") == null ? user.getStr("mobile") == null ? "" : user.getStr("mobile") : user.getStr("telephone"));
        map.put("money", order.get("order_amt"));//付款方式
        OrderSend orderSend = OrderSend.dao.getOrderSend(order.get("id"));
        Customer customer = Customer.dao.findById(order.get("customer_id"), this.getCompanyId());
        map.put("customer_name", customer.get("name"));
        map.put("customer_date", order.getStr("delivery_date"));
        map.put("customer_address", orderSend == null ? "" : orderSend.get("address"));
        map.put("customer_contacts", orderSend == null ? "" : orderSend.get("people"));
        map.put("customer_contacts_phone", orderSend == null ? "" : orderSend.get("phone"));
        List<StorageBillList> storageBillLists = StorageBillList.dao.list(storageBill.get("id"));
        Map productMap = null;
        List productList = new ArrayList<>();
        List productListAll = new ArrayList<>();
        List productListTemp = new ArrayList<>();
        int orderType = order.getInt("ordertype");
        for (StorageBillList storageBillList : storageBillLists) {
            if (storageBillList.getDouble("amount") != 0) {
                OrderProduct orderProduct = OrderProduct.dao.findByOrderIdAndProductId(order.getStr("id"), storageBillList.getStr("product_id"), this.getCompanyId());
                productMap = new HashMap<>();
                productMap.put("billsn", storageBillList.getStr("billsn"));
                productMap.put("product_name", storageBillList.getStr("product_name"));
                productMap.put("unit", storageBillList.getStr("unitChina")); // ----
                productMap.put("amount", storageBillList.getDouble("amount"));
                productMap.put("money", orderType < 2 ? orderProduct.getBigDecimal("purchase_price") : orderProduct.getBigDecimal("sale_price"));
                productMap.put("amountMoney", orderProduct.getBigDecimal("amt"));
                productMap.put("specification", storageBillList.getStr("specification"));
                productList.add(productMap);
            }
            if (productList.size() == 35) {
                productListTemp.add(productList);
                productListAll.addAll(productList);
                productList = new ArrayList<>();
            }
        }
        productListAll.addAll(productList);
        if (productList.size() != 0) {
            productListTemp.add(productList);
        }
        map.put("productListAll", productListTemp);
        map.put("productList", productListAll);
        this.rendJson(true, null, "", map);
    }

    /**
     * 保存差异，在存在差异的时候，添加采购退货单
     *
     * @param orderId
     * @param differenceList
     */
    private void savePurchaseBackOrder(String orderId, List<Map<String, Object>> differenceList, Double orderAmont) {
        Order order = Order.dao.findById(orderId, this.getCompanyId());
        order.remove("id");
        order.set("ordertype", 1);
        String sn = SnCreater.dao.create("ORDER1", this.getCompanyId());
        order.set("billsn", sn);
        order.set("company_id", this.getCompanyId());
        order.set("sign_date", dateFormat.format(new Date()));
        order.set("head_id", this.getCurrentUserId());
        order.set("audit_status", 0);
        order.set("pay_status", 0);
        order.set("create_datetime", dateFormat.format(new Date()));
        order.set("creater_id", this.getCurrentUserId());
        order.set("submit_status", 0);
        order.set("is_deleted", 0);
        order.set("company_id", this.getCompanyId());
        order.set("order_amt", orderAmont);
        order.save();
        OrderProduct.dao.insert(differenceList, order.get("id"));
    }
}