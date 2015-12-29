package net.loyin.netService.vo.sysVo.logoutVo;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Chao on 2015/12/8.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class LogoutMainData {

    @XmlElement(name = "logout_name")
    @JsonProperty("logout_name")
    private String logoutName;

    @XmlElement(name = "logout_company")
    @JsonProperty("logout_company")
    private String companyName;

    public String getLogoutName() {
        return logoutName;
    }

    public void setLogoutName(String logoutName) {
        this.logoutName = logoutName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
