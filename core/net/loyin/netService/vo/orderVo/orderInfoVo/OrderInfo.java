package net.loyin.netService.vo.orderVo.orderInfoVo;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Chao on 2015/11/23.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderInfo {

    @XmlElement(name = "order_id")
    @JsonProperty("order_id")
    private String orderId;
    @XmlElement(name = "order_sn")
    @JsonProperty("order_sn")
    private String orderSn;
    @XmlElement(name = "order_remark")
    @JsonProperty("order_remark")
    private String orderRemark;
    @XmlElement(name = "order_amt")
    @JsonProperty("order_amt")
    private String orderAmt;
    @XmlElement(name = "order_status")
    @JsonProperty("order_status")
    private String orderStatus;
    @XmlElement(name = "order_product_count")
    @JsonProperty("order_product_count")
    private String orderProductCount;
    @XmlElement(name = "customer_pic")
    @JsonProperty("customer_pic")
    private String customerPic;
    @XmlElement(name = "customer_name")
    @JsonProperty("customer_name")
    private String customerName;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }

    public String getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(String orderAmt) {
        this.orderAmt = orderAmt;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderProductCount() {
        return orderProductCount;
    }

    public void setOrderProductCount(String orderProductCount) {
        this.orderProductCount = orderProductCount;
    }

    public String getCustomerPic() {
        return customerPic;
    }

    public void setCustomerPic(String customerPic) {
        this.customerPic = customerPic;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
