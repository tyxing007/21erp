package net.loyin.netService.vo.customerVo.customerInfoVo;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * Created by Chao on 2015/12/14.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerReturnInfoList {

    @XmlElement(name = "customer_info")
    @JsonProperty("customer_info")
    private List<CustomerReturnInfo> returnInfos;


    public List<CustomerReturnInfo> getReturnInfos() {
        return returnInfos;
    }

    public void setReturnInfos(List<CustomerReturnInfo> returnInfos) {
        this.returnInfos = returnInfos;
    }
}
