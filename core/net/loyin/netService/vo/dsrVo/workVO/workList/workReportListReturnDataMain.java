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
public class workReportListReturnDataMain {

    @XmlElement(name = "report_info")
    @JsonProperty("report_info")
    private List<workReportListReturnInfo> reportInfo;

    public List<workReportListReturnInfo> getReportInfo() {
        return reportInfo;
    }

    public void setReportInfo(List<workReportListReturnInfo> reportInfo) {
        this.reportInfo = reportInfo;
    }
}
