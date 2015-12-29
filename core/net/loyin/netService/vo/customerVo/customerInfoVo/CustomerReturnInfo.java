package net.loyin.netService.vo.customerVo.customerInfoVo;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Chao on 2015/11/10.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerReturnInfo {

    //客户ID
    @XmlElement(name = "customer_id")
    @JsonProperty("customer_id")
    private String customerId;
    //客户名称
    @XmlElement(name = "customer_name")
    @JsonProperty("customer_name")
    private String customerName;
    //客户电话
    @XmlElement(name = "customer_phone")
    @JsonProperty("customer_phone")
    private String customerPhone;
    //客户网店地址
    @XmlElement(name = "customer_address")
    @JsonProperty("customer_address")
    private String customerAddress;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }
}