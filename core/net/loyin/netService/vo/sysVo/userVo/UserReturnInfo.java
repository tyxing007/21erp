package net.loyin.netService.vo.sysVo.userVo;

import net.loyin.netService.domain.CommonMessageVO;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Chao on 2015/12/15.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UserReturnInfo{

    @XmlElement(name = "user_id")
    @JsonProperty("user_id")
    private String userId;

    @XmlElement(name = "user_name")
    @JsonProperty("user_name")
    private String userName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
