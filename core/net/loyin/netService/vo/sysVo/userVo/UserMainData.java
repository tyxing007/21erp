package net.loyin.netService.vo.sysVo.userVo;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Chao on 2015/12/15.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UserMainData {

    @XmlElement(name = "user_id")
    @JsonProperty("user_id")
    private String uid;

    @XmlElement(name = "position_id")
    @JsonProperty("position_id")
    private String positionId;

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
