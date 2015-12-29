package net.loyin.netService.vo.customerVo.customerInfoVo;

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
public class CustomerVo extends CommonMessageVO {
    @XmlElement(name = "main_message")
    @JsonProperty("main_message")
    private CustomerMainData maindata;

    @Override
    public boolean validate(boolean isReqAudit) {
        return false;
    }

    @Override
    public String validate() {
        String msg = "";
        if(maindata.getNum()==null || maindata.getNum().isEmpty()){
            msg ="缺少页码,请核对";
        }
        if(maindata.getSize() ==null || maindata.getSize().isEmpty()){
            msg ="缺少记录数量,请核对";
        }
        return msg;
    }

    @Override
    public CustomerMainData getMaindata() {
        return maindata;
    }

    public void setMaindata(CustomerMainData maindata) {
        this.maindata = maindata;
    }
}
