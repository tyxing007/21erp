package net.loyin.netService.vo.customerVo.customerDetailVo;

import net.loyin.netService.domain.CommonMessageVO;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Chao on 2015/11/10.
 */
@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonRootName("data")
@JsonAutoDetect(isGetterVisibility = JsonAutoDetect.Visibility.NONE,fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CustomerDetailReturnVo extends CommonMessageVO {
    @XmlElement(name = "main_message")
    @JsonProperty("main_message")
    private CustomerDetailReturnMainData maindata;

    @Override
    public boolean validate(boolean isReqAudit) {
        return false;
    }

    @Override
    public String validate() {
        String msg = "";
//        if(maindata.getCustomerId()==null || maindata.getCustomerId().isEmpty()){
//            msg ="客户ID为空,请核对";
//        }
        return msg;
    }

    @Override
    public CustomerDetailReturnMainData getMaindata() {
        return maindata;
    }

    public void setMaindata(CustomerDetailReturnMainData maindata) {
        this.maindata = maindata;
    }
}
