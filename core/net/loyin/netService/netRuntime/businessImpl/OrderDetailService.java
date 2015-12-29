package net.loyin.netService.netRuntime.businessImpl;

import net.loyin.model.scm.Order;
import net.loyin.model.scm.OrderProduct;
import net.loyin.model.scm.OrderSend;
import net.loyin.netService.dataPacket.DataPacket;
import net.loyin.netService.dataPacket.HttpDataPacket;
import net.loyin.netService.domain.CommonHeader;
import net.loyin.netService.domain.CommonMessageVO;
import net.loyin.netService.domain.DataFormats;
import net.loyin.netService.domain.SelectedKeys;
import net.loyin.netService.netRuntime.IMessageRuntime;
import net.loyin.netService.netRuntime.impl.MessageRuntimeImpl;
import net.loyin.netService.vo.orderVo.orderDetailVo.OrderDetailProductInfo;
import net.loyin.netService.vo.orderVo.orderDetailVo.OrderDetailReturnInfo;
import net.loyin.netService.vo.orderVo.orderDetailVo.OrderDetailReturnVo;
import net.loyin.netService.vo.orderVo.orderDetailVo.OrderDetailVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Chao on 2015/11/23.
 */
public class OrderDetailService extends MessageRuntimeImpl implements IMessageRuntime {
    @Override
    public DataPacket run(CommonMessageVO vo) throws Exception {
        OrderDetailVo detailVo = (OrderDetailVo) vo;
        OrderDetailReturnVo returnVo = new OrderDetailReturnVo();
        CommonHeader messageHeader = detailVo.getMessageHeader();
        String orderId = detailVo.getMaindata().getOrderId();
        Map<String,String> userMap = ((HttpDataPacket) this.getDataPacket()).getCookie();
        Order order = Order.dao.findById(orderId,userMap.get("company_id"));
        if(order != null){
            OrderDetailReturnInfo returnInfo = new OrderDetailReturnInfo();
            returnInfo.setOrderId(orderId);
            returnInfo.setCustomerId(order.getStr("customer_id"));
            returnInfo.setCustomerName(order.getStr("customer_name"));
            returnInfo.setOrderTime(order.getStr("delivery_date"));
            returnInfo.setOrderRemark(order.getStr(""));
            returnInfo.setOrderAmt(String.valueOf(order.getNumber("order_amt")));
            OrderSend orderSend = OrderSend.dao.getOrderSend(orderId);
            if(orderSend!=null){
                returnInfo.setOrderContacts(orderSend.getStr("people"));
                returnInfo.setOrderContactsPhone(orderSend.getStr("phone"));
                returnInfo.setOrderAddress(orderSend.getStr("address"));
            }else{
                returnInfo.setOrderContacts("");
                returnInfo.setOrderContactsPhone("");
                returnInfo.setOrderAddress("");
            }
            List<OrderProduct> list = OrderProduct.dao.list(orderId);
            OrderDetailProductInfo productInfo = null;
            List<OrderDetailProductInfo> infoList = new ArrayList<>();
            for(OrderProduct o : list){
                productInfo = new OrderDetailProductInfo();
                productInfo.setProductId(o.getStr("product_id"));
                productInfo.setProductBillsn(o.getStr("billsn"));
                productInfo.setProductName(o.getStr("product_name"));
                productInfo.setProductNum(String.valueOf(o.getDouble("amount")));
                productInfo.setProductSale(String.valueOf(o.getNumber("sale_price")));
                productInfo.setProductUnit(o.getStr("unit"));
                productInfo.setProductRemark(o.getStr("description"));
                infoList.add(productInfo);
            }
            returnInfo.setOrderProducts(infoList);
            messageHeader.setRsp_no("SUCCESS");
            messageHeader.setRsp_msg("请求成功");
            messageHeader.setBusinessCode("ORDER0004");
            returnVo.setMessageHeader(messageHeader);
            returnVo.setMaindata(returnInfo);
        }else{
            messageHeader.setRsp_no("FAILURE");
            messageHeader.setRsp_msg("无法获取订单,请核对订单号");
            messageHeader.setBusinessCode("ORDER0004");
            returnVo.setMessageHeader(messageHeader);
        }

        DataPacket httpDataPacket = this.getDataPacket().GetBackBaseDataPacket();
        httpDataPacket.setSelectedKey(SelectedKeys.WRITE);
        httpDataPacket.setRawDataFormat(DataFormats.OBJECT);
        httpDataPacket.setDataFormat(DataFormats.JSON);
        httpDataPacket.setRawdata(returnVo);
        httpDataPacket.setHead("ORDER0004");
        return  httpDataPacket;
    }



    @Override
    public String packetsVerify(CommonMessageVO vo) {
//        return null;
        if(vo instanceof OrderDetailVo){
            OrderDetailVo detailVo = (OrderDetailVo)vo;
            String msg = detailVo.validate();
            return msg;
        }else{
            return "非法报文";
        }
    }
}
