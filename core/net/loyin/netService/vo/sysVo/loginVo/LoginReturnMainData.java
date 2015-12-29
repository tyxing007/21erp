package net.loyin.netService.vo.sysVo.loginVo;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Chao on 2015/12/7.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class LoginReturnMainData {

    @XmlElement(name = "user_pic")
    @JsonProperty("user_pic")
    private String userPic;

    @XmlElement(name = "user_cookie")
    @JsonProperty("user_cookie")
    private String userCookie;

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    public String getUserCookie() {
        return userCookie;
    }

    public void setUserCookie(String userCookie) {
        this.userCookie = userCookie;
    }
}
