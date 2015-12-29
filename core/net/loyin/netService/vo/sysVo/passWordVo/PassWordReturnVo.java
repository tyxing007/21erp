package net.loyin.netService.vo.sysVo.passWordVo;

import net.loyin.netService.domain.CommonMessageVO;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Chao on 2015/12/8.
 */
@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonRootName("data")
@JsonAutoDetect(isGetterVisibility = JsonAutoDetect.Visibility.NONE,fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PassWordReturnVo extends CommonMessageVO {


    @XmlElement(name = "main_message")
    @JsonProperty("main_message")
    private PassWordReturnMainData maindata;
    @Override
    public boolean validate(boolean isReqAudit) {
        return false;
    }

    @Override
    public String validate() {
        return null;
    }

    @Override
    public PassWordReturnMainData getMaindata() {
        return maindata;
    }

    public void setMaindata(PassWordReturnMainData maindata) {
        this.maindata = maindata;
    }
}
