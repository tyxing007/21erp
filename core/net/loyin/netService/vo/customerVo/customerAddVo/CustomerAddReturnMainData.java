package net.loyin.netService.vo.customerVo.customerAddVo;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Chao on 2015/11/25.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerAddReturnMainData {

    @XmlElement(name = "customer_id")
    @JsonProperty("customer_id")
    private String customerId;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}