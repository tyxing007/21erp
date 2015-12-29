package net.loyin.netService.vo.sysVo.userVo;

import net.loyin.netService.domain.CommonMessageVO;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Chao on 2015/12/15.
 */
@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonRootName("data")
@JsonAutoDetect(isGetterVisibility = JsonAutoDetect.Visibility.NONE,fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class UserReturnVo extends CommonMessageVO {

    @XmlElement(name = "main_message")
    @JsonProperty("main_message")
    private UserReturnMainData maindata;

    @Override
    public boolean validate(boolean isReqAudit) {
        return false;
    }

    @Override
    public String validate() {
        return null;
    }

    @Override
    public UserReturnMainData getMaindata() {
        return this.maindata;
    }

    public void setMaindata(UserReturnMainData maindata){
        this.maindata = maindata;
    }
}
