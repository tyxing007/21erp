package net.loyin.netService.vo.dsrVo.attendanceVO;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Chao on 2015/12/1.
 * 请假出差请求
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DsrAttendanceMainData {

    @XmlElement(name = "attendance_type")
    @JsonProperty("attendance_type")
    private String attendanceType;
    @XmlElement(name = "search_start_time")
    @JsonProperty("search_start_time")
    private String startTime;
    @XmlElement(name = "search_end_time")
    @JsonProperty("search_end_time")
    private String endTime;
    @XmlElement(name = "page_size")
    @JsonProperty("page_size")
    private String pageSize;
    @XmlElement(name = "page_num")
    @JsonProperty("page_num")
    private String pageNum;

    public String getAttendanceType() {
        return attendanceType;
    }

    public void setAttendanceType(String attendanceType) {
        this.attendanceType = attendanceType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getPageNum() {
        return pageNum;
    }

    public void setPageNum(String pageNum) {
        this.pageNum = pageNum;
    }
}