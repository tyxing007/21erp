package net.loyin.netService.vo.customerVo.customerVisitsVo;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * Created by Chao on 2015/12/1.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerVisitsReturnMainData {
    //查询列表
    @XmlElement(name = "visits_list")
    @JsonProperty("visits_list")
    private List<CustomerVisitsReturnInfo> visitsList;

    public List<CustomerVisitsReturnInfo> getVisitsList() {
        return visitsList;
    }

    public void setVisitsList(List<CustomerVisitsReturnInfo> visitsList) {
        this.visitsList = visitsList;
    }
}