package net.loyin.netService.vo.dsrVo.workVO.workList;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * Created by Chao on 2015/12/15.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class workReportListReturnInfo {

    @XmlElement(name = "report_id")
    @JsonProperty("report_id")
    private String reportId;
    @XmlElement(name = "report_title")
    @JsonProperty("report_title")
    private String reportTitle;
    @XmlElement(name = "report_time")
    @JsonProperty("report_time")
    private String reportTime;
    @XmlElement(name = "report_count")
    @JsonProperty("report_count")
    private String reportCount;
    @XmlElement(name = "report_money")
    @JsonProperty("report_money")
    private String reportMoney;

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

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
}
