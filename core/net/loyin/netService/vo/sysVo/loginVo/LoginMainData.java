package net.loyin.netService.vo.sysVo.loginVo;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Chao on 2015/12/7.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class LoginMainData {

    @XmlElement(name = "login_name")
    @JsonProperty("login_name")
    private String loginName;

    @XmlElement(name = "login_pass_world")
    @JsonProperty("login_pass_world")
    private String passWord;

    @XmlElement(name = "login_company")
    @JsonProperty("login_pass_world")
    private String companyName;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
