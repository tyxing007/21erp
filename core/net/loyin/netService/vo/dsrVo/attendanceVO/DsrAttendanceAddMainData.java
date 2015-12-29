package net.loyin.netService.vo.dsrVo.attendanceVO;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Chao on 2015/12/1.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DsrAttendanceAddMainData {

    @XmlElement(name = "attendance_type")
    @JsonProperty("attendance_type")
    private String attendanceType;
    @XmlElement(name = "attendance_genre")
    @JsonProperty("attendance_genre")
    private String attendanceGenre;
    @XmlElement(name = "attendance_remark")
    @JsonProperty("attendance_remark")
    private String attendanceRemark;
    @XmlElement(name = "attendance_startTime")
    @JsonProperty("attendance_startTime")
    private String attendanceStartTime;
    @XmlElement(name = "attendance_endTime")
    @JsonProperty("attendance_endTime")
    private String attendanceEndTime;
    @XmlElement(name = "attendance_reviewer")
    @JsonProperty("attendance_reviewer")
    private String attendanceReviewer;


    public String getAttendanceType() {
        return attendanceType;
    }

    public void setAttendanceType(String attendanceType) {
        this.attendanceType = attendanceType;
    }

    public String getAttendanceGenre() {
        return attendanceGenre;
    }

    public void setAttendanceGenre(String attendanceGenre) {
        this.attendanceGenre = attendanceGenre;
    }

    public String getAttendanceRemark() {
        return attendanceRemark;
    }

    public void setAttendanceRemark(String attendanceRemark) {
        this.attendanceRemark = attendanceRemark;
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

    public String getAttendanceReviewer() {
        return attendanceReviewer;
    }

    public void setAttendanceReviewer(String attendanceReviewer) {
        this.attendanceReviewer = attendanceReviewer;
    }
}
