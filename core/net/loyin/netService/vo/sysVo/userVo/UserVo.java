package net.loyin.netService.vo.sysVo.userVo;

import net.loyin.netService.domain.CommonMessageVO;
import net.loyin.netService.vo.sysVo.passWordVo.PassWordMainData;
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
public class UserVo extends CommonMessageVO {


    @XmlElement(name = "main_message")
    @JsonProperty("main_message")
    private UserMainData maindata;

    @Override
    public boolean validate(boolean isReqAudit) {
        return false;
    }

    @Override
    public String validate() {
        String msg = "";
        if(maindata.getUid()==null || maindata.getUid().isEmpty()){
            msg ="缺少登录用户ID,请核对";
        }
        if(maindata.getPositionId()==null || maindata.getPositionId().isEmpty()){
            msg ="缺少岗位,请核对";
        }
        return msg;
    }

    @Override
    public UserMainData getMaindata() {
        return maindata;
    }

    public void setMaindata(UserMainData maindata) {
        this.maindata = maindata;
    }
}
