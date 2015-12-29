package net.loyin.netService.vo.customerVo.customerVisitsVo;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Chao on 2015/12/1.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerVisitsReturnInfo {
    //搜索关键字
    @XmlElement(name = "customer_visit_id")
    @JsonProperty("customer_visit_id")
    private String visitId;
    //页面大小
    @XmlElement(name = "customer_name")
    @JsonProperty("customer_name")
    private String customerName;
    //页码
    @XmlElement(name = "customer_visit_time")
    @JsonProperty("customer_visit_time")
    private String visitTime;

    public String getVisitId() {
        return visitId;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(String visitTime) {
        this.visitTime = visitTime;
    }
}
