package net.loyin.netService.vo.dsrVo.attendanceVO;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Chao on 2015/12/1.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DsrAttendanceAddReturnMainData {

    @XmlElement(name = "attendance_id")
    @JsonProperty("attendance_id")
    private String attendanceId;

    public String getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(String attendanceId) {
        this.attendanceId = attendanceId;
    }
}
