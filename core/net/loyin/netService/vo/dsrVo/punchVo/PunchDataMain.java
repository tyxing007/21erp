package net.loyin.netService.vo.dsrVo.punchVo;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Chao on 2015/12/15.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PunchDataMain {

    @XmlElement(name = "check_type")
    @JsonProperty("check_type")
    private String checkType;
    @XmlElement(name = "check_lng")
    @JsonProperty("check_lng")
    private String checkLng;
    @XmlElement(name = "check_lat")
    @JsonProperty("check_lat")
    private String checkLat;
    @XmlElement(name = "check_time")
    @JsonProperty("check_time")
    private String checkTime;
    @XmlElement(name = "check_remark")
    @JsonProperty("check_remark")
    private String checkRemark;

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    public String getCheckLng() {
        return checkLng;
    }

    public void setCheckLng(String checkLng) {
        this.checkLng = checkLng;
    }

    public String getCheckLat() {
        return checkLat;
    }

    public void setCheckLat(String checkLat) {
        this.checkLat = checkLat;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getCheckRemark() {
        return checkRemark;
    }

    public void setCheckRemark(String checkRemark) {
        this.checkRemark = checkRemark;
    }


}
