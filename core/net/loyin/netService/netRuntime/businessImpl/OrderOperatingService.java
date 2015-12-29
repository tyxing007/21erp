package net.loyin.netService.netRuntime.businessImpl;

import net.loyin.model.em.SaleGoal;
import net.loyin.model.fa.PayReceivAbles;
import net.loyin.model.scm.Order;
import net.loyin.model.scm.StorageBill;
import net.loyin.model.sso.Company;
import net.loyin.netService.dataPacket.DataPacket;
import net.loyin.netService.dataPacket.HttpDataPacket;
import net.loyin.netService.domain.CommonHeader;
import net.loyin.netService.domain.CommonMessageVO;
import net.loyin.netService.domain.DataFormats;
import net.loyin.netService.domain.SelectedKeys;
import net.loyin.netService.netRuntime.IMessageRuntime;
import net.loyin.netService.netRuntime.impl.MessageRuntimeImpl;
import net.loyin.netService.vo.fileVo.FileSaveVo;
import net.loyin.netService.vo.orderVo.orderOperatingVo.OrderOperatingMainData;
import net.loyin.netService.vo.orderVo.orderOperatingVo.OrderOperatingVO;
import net.loyin.netService.vo.productVo.ProductVo;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Chao on 2015/12/9.
 */
public class OrderOperatingService extends MessageRuntimeImpl implements IMessageRuntime {
    @Override
    public DataPacket run(CommonMessageVO vo) throws Exception {
        Map<String, String> userMap = ((HttpDataPacket) this.getDataPacket()).getCookie();
        OrderOperatingVO operatingVO = (OrderOperatingVO) vo;
        OrderOperatingMainData mainData = operatingVO.getMaindata();
        CommonHeader messageHeader = operatingVO.getMessageHeader();
        OrderOperatingVO returnVo = new OrderOperatingVO();
        if (mainData.getOrderType().equals("0")) {
            try {
                if (Order.dao.isPay(mainData.getOrderId())) {
                    messageHeader.setRsp_no("FAILURE");
                    messageHeader.setRsp_msg("已经存在支付，请重新选择需要删除的数据！");
                    messageHeader.setBusinessCode("ORDER0006");
                    returnVo.setMessageHeader(messageHeader);
                    returnVo.setMaindata(mainData);
                }else{
                    Order.dao.del(mainData.getOrderId(), userMap.get("company_id"));
                    messageHeader.setRsp_no("SUCCESS");
                    messageHeader.setRsp_msg("");
                    messageHeader.setBusinessCode("ORDER0006");
                    returnVo.setMessageHeader(messageHeader);
                    returnVo.setMaindata(mainData);
                }
            } catch (Exception e) {
                messageHeader.setRsp_no("FAILURE");
                messageHeader.setRsp_msg("操作异常");
                messageHeader.setBusinessCode("ORDER0006");
                returnVo.setMessageHeader(messageHeader);
                returnVo.setMaindata(mainData);
                DataPacket httpDataPacket = this.getDataPacket().GetBackBaseDataPacket();
                httpDataPacket.setSelectedKey(SelectedKeys.WRITE);
                httpDataPacket.setRawDataFormat(DataFormats.OBJECT);
                httpDataPacket.setDataFormat(DataFormats.JSON);
                httpDataPacket.setRawdata(returnVo);
                httpDataPacket.setHead("ORDER0006");

                return httpDataPacket;

            }
        } else if (mainData.getOrderType().equals("1")) {
            String result = this.submit(mainData.getOrderId(), userMap.get("company_id"));
            if (result != null) {
                messageHeader.setRsp_no("FAILURE");
                messageHeader.setRsp_msg(result);
                messageHeader.setBusinessCode("ORDER0006");
                returnVo.setMessageHeader(messageHeader);
                returnVo.setMaindata(mainData);
            } else {
                messageHeader.setRsp_no("SUCCESS");
                messageHeader.setRsp_msg("");
                messageHeader.setBusinessCode("ORDER0006");
                returnVo.setMessageHeader(messageHeader);
                returnVo.setMaindata(mainData);
            }
        } else {
            messageHeader.setRsp_no("FAILURE");
            messageHeader.setRsp_msg("操作类型不存在");
            messageHeader.setBusinessCode("ORDER0006");
            returnVo.setMessageHeader(messageHeader);
            returnVo.setMaindata(mainData);
        }

        DataPacket httpDataPacket = this.getDataPacket().GetBackBaseDataPacket();
        httpDataPacket.setSelectedKey(SelectedKeys.WRITE);
        httpDataPacket.setRawDataFormat(DataFormats.OBJECT);
        httpDataPacket.setDataFormat(DataFormats.JSON);
        httpDataPacket.setRawdata(returnVo);
        httpDataPacket.setHead("ORDER0006");
        return httpDataPacket;
    }

    public String submit(String orderId, String companyId) {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Order order = Order.dao.findByIdOnlyOrder(orderId, companyId);
            if (order == null) {
                return "";
            }
            Company company = Company.dao.qryCacheById(companyId);
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
                    if ("3".equals(config.getOrDefault("p_sale_audit_type", "0"))) {
                        order.set("auditor_id", config.get("p_finance"));
                    }
                }
                order.set("audit_status", 1);//设置为提交状态
            }
            order.update();
        } catch (Exception e) {
            return "流程异常";
        }
        return null;
        //{"p_custpool_c":"90","p_finance":"L0Ua","p_financetui":"L0Ua","p_member_card_pay":"false","p_pact_alarm":"30","p_sale_audit":"true","p_sale_audit_type":"3","p_saletui_audit":"true","p_saletui_audit_type":"3","p_sysname":""}
    }


    @Override
    public String packetsVerify(CommonMessageVO vo) {
        if (vo instanceof OrderOperatingVO) {
            OrderOperatingVO operatingVO = (OrderOperatingVO) vo;
            String msg = operatingVO.validate();
            return msg;
        } else {
            return "非法报文";
        }
    }
}
