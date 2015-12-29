package net.loyin.netService.vo.sysVo.logoutVo;

import net.loyin.netService.domain.CommonMessageVO;
import net.loyin.netService.vo.sysVo.loginVo.LoginMainData;
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
public class LogoutVo extends CommonMessageVO {

    @XmlElement(name = "main_message")
    @JsonProperty("main_message")
    private LogoutMainData maindata;

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
        if(maindata.getLogoutName()==null || maindata.getLogoutName().isEmpty()){
            msg ="缺少登出名,请核对";
        }
        return msg;
    }

    @Override
    public LogoutMainData getMaindata() {
        return maindata;
    }

    public void setMaindata(LogoutMainData maindata) {
        this.maindata = maindata;
    }
}