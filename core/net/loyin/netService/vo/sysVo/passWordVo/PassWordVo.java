package net.loyin.netService.vo.sysVo.passWordVo;

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
public class PassWordVo extends CommonMessageVO {

    @XmlElement(name = "main_message")
    @JsonProperty("main_message")
    private PassWordMainData maindata;

    @Override
    public boolean validate(boolean isReqAudit) {
        return false;
    }

    @Override
    public String validate() {
        String msg = "";
        if(maindata.getLoginId()==null || maindata.getLoginId().isEmpty()){
            msg ="缺少登录用户ID,请核对";
        }
        if(maindata.getNewPass()==null || maindata.getNewPass().isEmpty()){
            msg ="缺少新密码,请核对";
        }
        if(maindata.getOldPass()==null || maindata.getOldPass().isEmpty()){
            msg ="缺少旧密码,请核对";
        }
        return msg;
    }

    @Override
    public PassWordMainData getMaindata() {
        return maindata;
    }

    public void setMaindata(PassWordMainData maindata) {
        this.maindata = maindata;
    }


}
