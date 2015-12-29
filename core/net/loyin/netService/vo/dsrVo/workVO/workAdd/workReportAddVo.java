package net.loyin.netService.vo.dsrVo.workVO.workAdd;

import net.loyin.netService.domain.CommonMessageVO;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Chao on 2015/12/15.
 */
@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonRootName("data")
@JsonAutoDetect(isGetterVisibility = JsonAutoDetect.Visibility.NONE,fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class workReportAddVo extends CommonMessageVO {

    @XmlElement(name = "main_message")
    @JsonProperty("main_message")
    private workReportAddMainData maindata;

    @Override
    public boolean validate(boolean isReqAudit) {
        return false;
    }

    @Override
    public String validate() {
        String msg = "";
        if (maindata.getReportPlan() == null || maindata.getReportPlan().isEmpty()) {
            msg += "缺失工作计划";
        }
        if (maindata.getReportSummarize() == null || maindata.getReportSummarize().isEmpty()) {
            msg += "缺失工作总结";
        }
        if (maindata.getReportStarTime() == null || maindata.getReportEndTime().isEmpty()
                || maindata.getReportEndTime() == null || maindata.getReportEndTime().isEmpty()) {
            msg += "缺失报告时间";
        }
        if (maindata.getReportTitle() == null || maindata.getReportTitle().isEmpty()) {
            msg += "缺失报告标题";
        }
        if (maindata.getReportType() == null || maindata.getReportType().isEmpty()) {
            msg += "缺失报告类型";
        }
        return msg;
    }

    @Override
    public workReportAddMainData getMaindata() {
        return maindata;
    }

    public void setMaindata(workReportAddMainData maindata) {
        this.maindata = maindata;
    }
}
