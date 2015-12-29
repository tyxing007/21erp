package net.loyin.netService.netRuntime.businessImpl;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import net.loyin.model.scm.Order;
import net.loyin.model.scm.OrderProduct;
import net.loyin.model.scm.OrderSend;
import net.loyin.model.scm.StorageBill;
import net.loyin.model.sso.FileBean;
import net.loyin.model.sso.SnCreater;
import net.loyin.netService.dataPacket.DataPacket;
import net.loyin.netService.dataPacket.HttpDataPacket;
import net.loyin.netService.domain.CommonHeader;
import net.loyin.netService.domain.CommonMessageVO;
import net.loyin.netService.domain.DataFormats;
import net.loyin.netService.domain.SelectedKeys;
import net.loyin.netService.netRuntime.IMessageRuntime;
import net.loyin.netService.netRuntime.impl.MessageRuntimeImpl;
import net.loyin.netService.vo.orderVo.orderDetailVo.*;
import net.loyin.netService.vo.orderVo.orderDownVo.OrderDownMainData;
import net.loyin.netService.vo.orderVo.orderDownVo.OrderDownProductInfo;
import net.loyin.netService.vo.orderVo.orderDownVo.OrderDownVo;
import net.loyin.netService.vo.orderVo.orderInfoVo.*;
import net.loyin.util.CS;
import net.loyin.utli.TimeUtil;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Chao on 2015/11/23.
 */
public class OrderDownService extends MessageRuntimeImpl implements IMessageRuntime {
    @Override
    public DataPacket run(CommonMessageVO vo) throws Exception {
        OrderDownVo downVo = (OrderDownVo) vo;
        OrderDetailVo returnVo = new OrderDetailVo();
        CommonHeader messageHeader = downVo.getMessageHeader();
        Map<String,String> userMap = ((HttpDataPacket) this.getDataPacket()).getCookie();
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now=dateTimeFormat.format(new Date());
        if(userMap == null){
            messageHeader.setRsp_no("FAILURE");
            messageHeader.setRsp_msg("您未登陆，请登陆之后再进行操作，谢谢");
            messageHeader.setBusinessCode("ORDER0008");
            returnVo.setMessageHeader(messageHeader);
            returnVo.setMaindata(new OrderDetailInfo());
        }else{
            OrderDownMainData mainData = downVo.getMaindata();
            Order order = null;
            OrderSend orderSend = null;
            if(mainData.getOrderId() != null && !mainData.getOrderId().isEmpty()){
                order = Order.dao.findById(mainData.getOrderId());
                order.set("update_datetime", now);//修改时间
                order.set("updater_id", userMap.get("uid"));//修改人
                order.set("customer_id", mainData.getCustomerId());
                order.set("update_datetime", TimeUtil.getTime());
                order.set("updater_id", userMap.get("uid"));
                order.set("delivery_date", mainData.getOrderTime());
                order.set("order_amt",Double.parseDouble(mainData.getOrderAmt()));
                order.update();
                orderSend  = OrderSend.dao.getOrderSend(order.getStr("id"));
                if(orderSend == null){
                    orderSend = new OrderSend();
                    orderSend.set("order_id", order.getStr("id"));
                    orderSend.set("people", mainData.getOrderContacts());
                    orderSend.set("phone", mainData.getOrderContactsPhone());
                    orderSend.set("address", mainData.getOrderAddress());
                    orderSend.save();
                }else{
                    orderSend.set("order_id", order.getStr("id"));
                    orderSend.set("people", mainData.getOrderContacts());
                    orderSend.set("phone", mainData.getOrderContactsPhone());
                    orderSend.set("address", mainData.getOrderAddress());
                    orderSend.update();
                }
                List<OrderProduct> orderProductList = OrderProduct.dao.findBySloID(order.getStr("id"));
                boolean isAdd = true;
                List<OrderDownProductInfo> infos = mainData.getOrderProducts().getProductInfo();
                for(OrderDownProductInfo o :infos){
                    for(OrderProduct op : orderProductList){
                        if(op.getStr("product_id").equals(o.getProductId())){
                            OrderProduct.dao.updateOrderInfo(order.getStr("id"),op.getStr("product_id"),o.getProductNum(),o.getProductSale(),o.getProductAmt(),o.getProductRemark());
                            isAdd = false;
                            break;
                        }
                    }
                    if(isAdd){
                        OrderProduct product = new OrderProduct();
                        product.put("id", order.getStr("id"));
                        product.put("product_id", o.getProductId());
                        product.put("sale_price", BigDecimal.valueOf(Double.parseDouble(o.getProductSale())).doubleValue());
                        product.put("amount", BigDecimal.valueOf(Double.parseDouble(o.getProductNum())).doubleValue());
                        product.put("zkl", 0);
                        product.put("zhamt", 0);
                        BigDecimal amt = new BigDecimal(0.0);
                        amt = BigDecimal.valueOf(Double.parseDouble(o.getProductSale())).multiply(BigDecimal.valueOf(Double.parseDouble(o.getProductNum())));
                        product.put("amt", amt.doubleValue());
                        product.put("description",o.getProductRemark());
                        product.put("quoted_price", 0);//报价
                        product.put("tax_rate", 0);
                        product.put("tax", 0);
                        product.save();
                    }
                    isAdd = true;
                }
            }else {
                String sn = SnCreater.dao.create("ORDER2", userMap.get("company_id"));
                order = new Order();
                order.set("billsn", sn);
                order.set("audit_status", 0);
                order.set("company_id", userMap.get("company_id"));
                order.set("customer_id", mainData.getCustomerId());
                order.set("sign_date", TimeUtil.getTime());
                order.set("head_id", userMap.get("uid"));
                order.set("create_datetime", TimeUtil.getTime());
                order.set("creater_id", userMap.get("uid"));
                order.set("order_amt", Double.parseDouble(mainData.getOrderAmt()));
                order.set("submit_status", 0);
                order.set("is_deleted", 0);
                order.set("ordertype", 2);
                order.set("delivery_date", mainData.getOrderTime());
                order.save();

                orderSend = new OrderSend();
                orderSend.set("order_id", order.getStr("id"));
                orderSend.set("people", mainData.getOrderContacts());
                orderSend.set("phone", mainData.getOrderContactsPhone());
                orderSend.set("address", mainData.getOrderAddress());
                orderSend.save();
                List<OrderDownProductInfo> infos = mainData.getOrderProducts().getProductInfo();
                for(OrderDownProductInfo o :infos){
                        OrderProduct product = new OrderProduct();
                        product.put("id", order.getStr("id"));
                        product.put("product_id", o.getProductId());
                        product.put("sale_price", BigDecimal.valueOf(Double.parseDouble(o.getProductSale())).doubleValue());
                        product.put("amount", BigDecimal.valueOf(Double.parseDouble(o.getProductNum())).doubleValue());
                        product.put("zkl", 0);
                        product.put("zhamt", 0);
                        BigDecimal amt = new BigDecimal(0.0);
                        amt = BigDecimal.valueOf(Double.parseDouble(o.getProductSale())).multiply(BigDecimal.valueOf(Double.parseDouble(o.getProductNum())));
                        product.put("amt", amt.doubleValue());
                        product.put("description",o.getProductRemark());
                        product.put("quoted_price", 0);//报价
                        product.put("tax_rate", 0);
                        product.put("tax", 0);
                        product.save();
                }
            }
            messageHeader.setRsp_no("SUCCESS");
            messageHeader.setRsp_msg("");
            messageHeader.setBusinessCode("ORDER0008");
            returnVo.setMessageHeader(messageHeader);
            OrderDetailInfo detailInfo = new OrderDetailInfo();
            detailInfo.setOrderId(order.getStr("id"));
            returnVo.setMaindata(detailInfo);
        }
        DataPacket httpDataPacket = this.getDataPacket().GetBackBaseDataPacket();
        httpDataPacket.setSelectedKey(SelectedKeys.WRITE);
        httpDataPacket.setRawDataFormat(DataFormats.OBJECT);
        httpDataPacket.setDataFormat(DataFormats.JSON);
        httpDataPacket.setRawdata(returnVo);
        httpDataPacket.setHead("ORDER0008");
        return  httpDataPacket;
    }


    @Override
    public String packetsVerify(CommonMessageVO vo) {
//        return null;
        if(vo instanceof OrderDownVo){
            OrderDownVo downVo = (OrderDownVo)vo;
            String msg = downVo.validate();
            return msg;
        }else{
            return "非法报文";
        }
    }
}
