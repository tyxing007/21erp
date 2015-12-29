package net.loyin.netService.vo.offlineVo.OfflineCustomerVo;

import net.loyin.netService.vo.offlineVo.OfflineProductVo.ProductInfo;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Chao on 2015/12/10.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class OfflineCustomerMainData {
    @XmlElement(name = "customer_info")
    @JsonProperty("customer_info")
    private OfflineCustomerInfo customerInfo;

    public OfflineCustomerInfo getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(OfflineCustomerInfo customerInfo) {
        this.customerInfo = customerInfo;
    }
}
