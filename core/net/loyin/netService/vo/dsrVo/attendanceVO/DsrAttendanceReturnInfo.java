package net.loyin.netService.vo.dsrVo.attendanceVO;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Chao on 2015/12/1.
 * 请假出差请求
 */
@XmlRootElement(name = "attendance")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonRootName("data")
public class DsrAttendanceReturnInfo {

    @XmlElement(name = "attendance_genre")
    @JsonProperty("attendance_genre")
    private String attendanceType;
    @XmlElement(name = "attendance_time")
    @JsonProperty("attendance_time")
    private String attendanceTime;
    @XmlElement(name = "attendance_status")
    @JsonProperty("attendance_status")
    private String attendanceStatus;
    @XmlElement(name = "attendance_startTime")
    @JsonProperty("attendance_startTime")
    private String attendanceStartTime;
    @XmlElement(name = "attendance_endTime")
    @JsonProperty("attendance_endTime")
    private String attendanceEndTime;
    @XmlElement(name = "attendance_remark")
    @JsonProperty("attendance_remark")
    private String attendanceRemark;

    public String getAttendanceType() {
        return attendanceType;
    }

    public void setAttendanceType(String attendanceType) {
        this.attendanceType = attendanceType;
    }

    public String getAttendanceTime() {
        return attendanceTime;
    }

    public void setAttendanceTime(String attendanceTime) {
        this.attendanceTime = attendanceTime;
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public String getAttendanceStartTime() {
        return attendanceStartTime;
    }

    public void setAttendanceStartTime(String attendanceStartTime) {
        this.attendanceStartTime = attendanceStartTime;
    }

    public String getAttendanceEndTime() {
        return attendanceEndTime;
    }

    public void setAttendanceEndTime(String attendanceEndTime) {
        this.attendanceEndTime = attendanceEndTime;
    }

    public String getAttendanceRemark() {
        return attendanceRemark;
    }

    public void setAttendanceRemark(String attendanceRemark) {
        this.attendanceRemark = attendanceRemark;
    }
}
