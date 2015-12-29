package net.loyin.netService.vo.customerVo.customerVisitsVo;

import net.loyin.netService.domain.CommonMessageVO;
import net.loyin.netService.vo.customerVo.customerInfoVo.CustomerMainData;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Chao on 2015/12/1.
 */
@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonRootName("data")
@JsonAutoDetect(isGetterVisibility = JsonAutoDetect.Visibility.NONE,fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CustomerVisitsVo extends CommonMessageVO {

    @XmlElement(name = "main_message")
    @JsonProperty("main_message")
    private CustomerVisitsMainData maindata;

    @Override
    public boolean validate(boolean isReqAudit) {
        return false;
    }

    @Override
    public String validate() {
        String msg = "";
        if (maindata.getPageNum() == null || maindata.getPageNum().isEmpty()) {
            msg += "缺失页码";
        }
        if (maindata.getPageSize() == null || maindata.getPageSize().isEmpty()) {
            msg += "缺失页数大小";
        }
        return msg;
    }

    @Override
    public CustomerVisitsMainData getMaindata() {
        return maindata;
    }

    public void setMaindata(CustomerVisitsMainData maindata) {
        this.maindata = maindata;
    }
}