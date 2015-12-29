package net.loyin.netService.vo.dsrVo.attendanceVO;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * Created by Chao on 2015/12/1.
 * 请假出差请求
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DsrAttendanceReturnMainData {

    @XmlElement(name = "attendance_type")
    @JsonProperty("attendance_type")
    private String attendanceType;
    @XmlElement(name = "attendance_info")
    @JsonProperty("attendance_info")
    private List<DsrAttendanceReturnInfo> info;

    public String getAttendanceType() {
        return attendanceType;
    }

    public void setAttendanceType(String attendanceType) {
        this.attendanceType = attendanceType;
    }

    public List<DsrAttendanceReturnInfo> getInfo() {
        return info;
    }

    public void setInfo(List<DsrAttendanceReturnInfo> info) {
        this.info = info;
    }
}
