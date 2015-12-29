package net.loyin.netService.vo.dsrVo.workVO.workAdd;

import net.loyin.netService.domain.CommonMessageVO;
import net.loyin.netService.vo.dsrVo.attendanceVO.DsrAttendanceMainData;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Chao on 2015/12/15.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class workReportAddMainData{

    @XmlElement(name = "report_title")
    @JsonProperty("report_title")
    private String reportTitle;
    @XmlElement(name = "report_end_time")
    @JsonProperty("report_end_time")
    private String reportEndTime;
    @XmlElement(name = "report_start_time")
    @JsonProperty("report_start_time")
    private String reportStarTime;
    @XmlElement(name = "report_type")
    @JsonProperty("report_type")
    private String reportType;
    @XmlElement(name = "report_summarize")
    @JsonProperty("report_summarize")
    private String reportSummarize;
    @XmlElement(name = "report_plan")
    @JsonProperty("report_plan")
    private String reportPlan;
    @XmlElement(name = "report_count")
    @JsonProperty("report_count")
    private String reportCount;
    @XmlElement(name = "report_money")
    @JsonProperty("report_money")
    private String reportMoney;

    public String getReportCount() {
        return reportCount;
    }

    public void setReportCount(String reportCount) {
        this.reportCount = reportCount;
    }

    public String getReportMoney() {
        return reportMoney;
    }

    public void setReportMoney(String reportMoney) {
        this.reportMoney = reportMoney;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getReportSummarize() {
        return reportSummarize;
    }

    public void setReportSummarize(String reportSummarize) {
        this.reportSummarize = reportSummarize;
    }

    public String getReportPlan() {
        return reportPlan;
    }

    public void setReportPlan(String reportPlan) {
        this.reportPlan = reportPlan;
    }

    public String getReportEndTime() {
        return reportEndTime;
    }

    public void setReportEndTime(String reportEndTime) {
        this.reportEndTime = reportEndTime;
    }

    public String getReportStarTime() {
        return reportStarTime;
    }

    public void setReportStarTime(String reportStarTime) {
        this.reportStarTime = reportStarTime;
    }
}
