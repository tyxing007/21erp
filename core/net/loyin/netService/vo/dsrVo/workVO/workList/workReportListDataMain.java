package net.loyin.netService.vo.dsrVo.workVO.workList;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Chao on 2015/12/15.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class workReportListDataMain {

    @XmlElement(name = "report_page")
    @JsonProperty("report_page")
    private String reportPage;
    @XmlElement(name = "report_pageSize")
    @JsonProperty("report_pageSize")
    private String reportPageSize;
    @XmlElement(name = "report_sort")
    @JsonProperty("report_sort")
    private String reportSort;
    @XmlElement(name = "report_type")
    @JsonProperty("report_type")
    private String reportType;
    @XmlElement(name = "report_sort_time")
    @JsonProperty("report_sort_time")
    private String reportSortTime;
    @XmlElement(name = "seacrch_title")
    @JsonProperty("seacrch_title")
    private String seacrchTitle;

    public String getReportPage() {
        return reportPage;
    }

    public void setReportPage(String reportPage) {
        this.reportPage = reportPage;
    }

    public String getReportPageSize() {
        return reportPageSize;
    }

    public void setReportPageSize(String reportPageSize) {
        this.reportPageSize = reportPageSize;
    }

    public String getReportSort() {
        return reportSort;
    }

    public void setReportSort(String reportSort) {
        this.reportSort = reportSort;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getReportSortTime() {
        return reportSortTime;
    }

    public void setReportSortTime(String reportSortTime) {
        this.reportSortTime = reportSortTime;
    }

    public String getSeacrchTitle() {
        return seacrchTitle;
    }

    public void setSeacrchTitle(String seacrchTitle) {
        this.seacrchTitle = seacrchTitle;
    }
}
