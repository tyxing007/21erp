package net.loyin.netService.netRuntime.businessImpl;

import com.jfinal.plugin.activerecord.Page;
import net.loyin.model.scm.Order;
import net.loyin.model.scm.OrderProduct;
import net.loyin.model.scm.StorageBill;
import net.loyin.model.sso.FileBean;
import net.loyin.netService.dataPacket.DataPacket;
import net.loyin.netService.dataPacket.HttpDataPacket;
import net.loyin.netService.domain.*;
import net.loyin.netService.netRuntime.IMessageRuntime;
import net.loyin.netService.netRuntime.impl.MessageRuntimeImpl;
import net.loyin.netService.vo.orderVo.orderInfoVo.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chao on 2015/11/23.
 */
public class OrderService extends MessageRuntimeImpl implements IMessageRuntime {
    @Override
    public DataPacket run(CommonMessageVO vo) throws Exception {
        OrderVo orderVo = (OrderVo) vo;
        OrderMainData orderMainData = orderVo.getMaindata();
        Map<String, Object> filter = new HashMap<>();
        filter.put("keyword",orderMainData.getKey());
        Map<String,String> userMap = ((HttpDataPacket) this.getDataPacket()).getCookie();
        if(userMap == null){
            //todo 此处添加异常
            OrderReturnVo orderReturnVo = new OrderReturnVo();
            CommonHeader messageHeader = orderVo.getMessageHeader();
            messageHeader.setRsp_no("FAILURE");
            messageHeader.setRsp_msg("您为登陆，请重新登陆");
            messageHeader.setBusinessCode("ORDER0002");
            orderReturnVo.setMessageHeader(messageHeader);
            orderReturnVo.setMaindata(new OrderReturnMainData());
            DataPacket httpDataPacket = this.getDataPacket().GetBackBaseDataPacket();
            httpDataPacket.setSelectedKey(SelectedKeys.WRITE);
            httpDataPacket.setRawDataFormat(DataFormats.OBJECT);
            httpDataPacket.setDataFormat(DataFormats.JSON);
            httpDataPacket.setRawdata(orderReturnVo);
            httpDataPacket.setHead("ORDER0002");
            return httpDataPacket;
        }
        filter.put("company_id",userMap.get("company_id"));
        filter.put("user_id", userMap.get("uid"));
        filter.put("position_id", userMap.get("position_id"));
        Page<Order> orderPage = Order.dao.page(Integer.parseInt(orderMainData.getPage()),Integer.parseInt(orderMainData.getPage()), filter, 0);
        List<OrderInfo> orderInfoList = new ArrayList<>();
        List<Order> orderList = orderPage.getList();
        OrderInfo orderInfo = null;
        for(Order o : orderList){
            orderInfo = new OrderInfo();
            orderInfo.setOrderStatus(getOrderStatus(o.getStr("id")));
            orderInfo.setOrderId(o.getStr("id"));
            orderInfo.setOrderSn(o.getStr("billsn"));
            orderInfo.setOrderRemark("");
            orderInfo.setOrderAmt(o.getNumber("order_amt").toString());
            orderInfo.setCustomerName(o.getStr("customer_name"));
            orderInfo.setOrderProductCount(getOrderProductCount(o.getStr("id")));
            List<FileBean> fileBeanList = FileBean.dao.findList(o.getStr("customer_id"));
            if(fileBeanList!=null && fileBeanList.size()!=0){
                orderInfo.setCustomerPic(fileBeanList.get(0).getStr("id"));
            }else{
                orderInfo.setCustomerPic("");
            }
            orderInfoList.add(orderInfo);
        }
        //初始化返回数据
        OrderReturnMainData mainData = new OrderReturnMainData();
        mainData.setOrderInfoList(orderInfoList);
        //设置进入报文内
        OrderReturnVo orderReturnVo = new OrderReturnVo();
        CommonHeader messageHeader = orderVo.getMessageHeader();
        messageHeader.setRsp_no("SUCCESS");
        messageHeader.setRsp_msg("请求成功");
        messageHeader.setBusinessCode("ORDER0002");
        orderReturnVo.setMessageHeader(messageHeader);
        orderReturnVo.setMaindata(mainData);
        DataPacket httpDataPacket = this.getDataPacket().GetBackBaseDataPacket();
        httpDataPacket.setSelectedKey(SelectedKeys.WRITE);
        httpDataPacket.setRawDataFormat(DataFormats.OBJECT);
        httpDataPacket.setDataFormat(DataFormats.JSON);
        httpDataPacket.setRawdata(orderReturnVo);
        httpDataPacket.setHead("ORDER0002");
        return httpDataPacket;
    }

    /**
     *获取订单状态
     * @param orderId
     * @return
     */
    private String getOrderStatus(String orderId){
        String msg = "";
        StorageBill storageBill = StorageBill.dao.getStorageBillByOrderId(orderId);
        if(storageBill == null){
            return "未提交";
        }else{
            int status = storageBill.getInt("status");
            switch (status){
                case 0:
                    msg = "未知状态";
                    break;
                case 1:
                    msg = "已出库";
                    break;
                case 2:
                    msg = "正在拣货";
                    break;
                case 3:
                    msg = "未知状态";
                    break;
                case 4:
                    msg = "等待财务审核";
                    break;
                case 5:
                    msg = "正在配送";
                    break;
            }
        }
        return msg;
    }

    /**
     *获取产品总数量
     * @param orderId
     * @return
     */
    private String getOrderProductCount(String orderId){
        List<OrderProduct> orderProducts = OrderProduct.dao.list(orderId);
        float count= 0;
        for(OrderProduct o : orderProducts){
            count += o.getFloat("amount");
        }
        return String.valueOf(count);
    }





    @Override
    public String packetsVerify(CommonMessageVO vo) {
//        return null;
        if(vo instanceof OrderVo){
            OrderVo orderVo = (OrderVo)vo;
            String msg = orderVo.validate();
            return msg;
        }else{
            return "非法报文";
        }
    }
}