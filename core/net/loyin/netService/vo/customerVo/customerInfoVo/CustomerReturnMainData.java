package net.loyin.netService.vo.customerVo.customerInfoVo;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * Created by Chao on 2015/11/10.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerReturnMainData {

    //客户信息
    @XmlElement(name = "customer_info")
    @JsonProperty("customer_info")
    private CustomerReturnInfoList returnInfoList;

    public CustomerReturnInfoList getReturnInfoList() {
        return returnInfoList;
    }

    public void setReturnInfoList(CustomerReturnInfoList returnInfoList) {
        this.returnInfoList = returnInfoList;
    }
}
