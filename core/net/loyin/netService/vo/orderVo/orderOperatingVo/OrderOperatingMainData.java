package net.loyin.netService.vo.orderVo.orderOperatingVo;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Chao on 2015/11/23.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderOperatingMainData {

    @XmlElement(name = "order_id")
    @JsonProperty("order_id")
    private String orderId;

    @XmlElement(name = "order_type")
    @JsonProperty("order_type")
    private String orderType;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
}
