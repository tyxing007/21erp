package net.loyin.netService.vo.sysVo.passWordVo;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Chao on 2015/12/8.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PassWordMainData {

    @XmlElement(name = "login_id")
    @JsonProperty("login_id")
    private String loginId;

    @XmlElement(name = "old_pass_world")
    @JsonProperty("old_pass_world")
    private String oldPass;

    @XmlElement(name = "new_pass_world")
    @JsonProperty("new_pass_world")
    private String newPass;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getOldPass() {
        return oldPass;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }
}