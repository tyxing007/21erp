package net.loyin.netService.vo.dsrVo.workVO.workList;

import net.loyin.netService.domain.CommonMessageVO;
import net.loyin.netService.vo.dsrVo.workVO.workAdd.workReportAddMainData;
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
public class workReportListVo extends CommonMessageVO {

    @XmlElement(name = "main_message")
    @JsonProperty("main_message")
    private workReportListDataMain maindata;

    @Override
    public boolean validate(boolean isReqAudit) {
        return false;
    }

    @Override
    public String validate() {
        String msg = "";
        if(maindata.getReportPage() == null || maindata.getReportPage().isEmpty()){
            msg += "缺失页码";
        }
        if(maindata.getReportPageSize() == null || maindata.getReportPageSize().isEmpty()){
            msg += "缺失页面大小";
        }
        return msg;
    }

    @Override
    public workReportListDataMain getMaindata() {
        return maindata;
    }

    public void setMaindata(workReportListDataMain maindata) {
        this.maindata = maindata;
    }
}
