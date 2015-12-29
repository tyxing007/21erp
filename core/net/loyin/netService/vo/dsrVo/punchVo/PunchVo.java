package net.loyin.netService.vo.dsrVo.punchVo;

import net.loyin.netService.domain.CommonMessageVO;
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
public class PunchVo extends CommonMessageVO {

    @XmlElement(name = "main_message")
    @JsonProperty("main_message")
    private PunchDataMain maindata;

    @Override
    public boolean validate(boolean isReqAudit) {
        return false;
    }

    @Override
    public String validate() {
        String msg = "";
        if(maindata.getCheckType() == null || maindata.getCheckType().isEmpty()){
            msg += "缺失类型";
        }
        if(maindata.getCheckLat() == null || maindata.getCheckLat().isEmpty()){
            msg += "缺失经纬度";
        }
        if(maindata.getCheckLng() == null || maindata.getCheckLng().isEmpty()){
            msg += "缺失经纬度";
        }
        if(maindata.getCheckTime() == null || maindata.getCheckTime().isEmpty()){
            msg += "缺失打卡时间";
        }
        if(maindata.getCheckRemark() == null || maindata.getCheckRemark().isEmpty()){
            msg += "缺失备注";
        }
        return msg;
    }

    @Override
    public PunchDataMain getMaindata() {
        return maindata;
    }

    public void setMaindata(PunchDataMain maindata) {
        this.maindata = maindata;
    }
}
