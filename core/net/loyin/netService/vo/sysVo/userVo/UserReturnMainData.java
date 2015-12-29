package net.loyin.netService.vo.sysVo.userVo;

import net.loyin.netService.domain.CommonMessageVO;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by Chao on 2015/12/15.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UserReturnMainData {

    @XmlElement(name = "user_info")
    @JsonProperty("user_info")
    private List<UserReturnInfo> userReturnInfos;

    public List<UserReturnInfo> getUserReturnInfos() {
        return userReturnInfos;
    }

    public void setUserReturnInfos(List<UserReturnInfo> userReturnInfos) {
        this.userReturnInfos = userReturnInfos;
    }
}
