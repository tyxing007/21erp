package net.loyin.netService.vo.sysVo.loginVo;

import net.loyin.netService.domain.CommonMessageVO;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Chao on 2015/12/7.
 */
@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonRootName("data")
@JsonAutoDetect(isGetterVisibility = JsonAutoDetect.Visibility.NONE,fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class LoginVo extends CommonMessageVO {

    @XmlElement(name = "main_message")
    @JsonProperty("main_message")
    private LoginMainData maindata;

    @Override
    public boolean validate(boolean isReqAudit) {
        return false;
    }

    @Override
    public String validate() {
        String msg = "";
        if(maindata.getCompanyName()==null || maindata.getCompanyName().isEmpty()){
            msg ="缺少企业名称,请核对";
        }
        if(maindata.getLoginName()==null || maindata.getLoginName().isEmpty()){
            msg ="缺少登录名,请核对";
        }
        if(maindata.getPassWord()==null || maindata.getPassWord().isEmpty()){
            msg ="缺少密码,请核对";
        }
        return msg;
    }

    @Override
    public LoginMainData getMaindata() {
        return maindata;
    }

    public void setMaindata(LoginMainData maindata) {
        this.maindata = maindata;
    }
}