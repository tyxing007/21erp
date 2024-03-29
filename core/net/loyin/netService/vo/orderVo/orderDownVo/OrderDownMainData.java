package net.loyin.netService.vo.orderVo.orderDownVo;

import net.loyin.netService.vo.orderVo.orderDetailVo.OrderDetailProductInfo;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * Created by Chao on 2015/11/23.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderDownMainData {

    @XmlElement(name = "customer_id")
    @JsonProperty("customer_id")
    private String customerId;

    @XmlElement(name = "order_id")
    @JsonProperty("order_id")
    private String orderId;

    @XmlElement(name = "order_contacts")
    @JsonProperty("order_contacts")
    private String orderContacts;
    @XmlElement(name = "order_contacts_phone")
    @JsonProperty("order_contacts_phone")
    private String orderContactsPhone;
    @XmlElement(name = "order_address")
    @JsonProperty("order_address")
    private String orderAddress;
    @XmlElement(name = "order_time")
    @JsonProperty("order_time")
    private String orderTime;
    @XmlElement(name = "order_remark")
    @JsonProperty("order_remark")
    private String orderRemark;
    @XmlElement(name = "order_amt")
    @JsonProperty("order_amt")
    private String orderAmt;
    @XmlElement(name = "product_count")
    @JsonProperty("product_count")
    private String productCount;
    @XmlElement(name = "order_product")
    @JsonProperty("order_product")
    private OrderDownProduct orderProducts;


    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getOrderContacts() {
        return orderContacts;
    }

    public void setOrderContacts(String orderContacts) {
        this.orderContacts = orderContacts;
    }

    public String getOrderContactsPhone() {
        return orderContactsPhone;
    }

    public void setOrderContactsPhone(String orderContactsPhone) {
        this.orderContactsPhone = orderContactsPhone;
    }

    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
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

    public OrderDownProduct getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(OrderDownProduct orderProducts) {
        this.orderProducts = orderProducts;
    }

    public String getProductCount() {
        return productCount;
    }

    public void setProductCount(String productCount) {
        this.productCount = productCount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
