package net.loyin.netService.vo.dsrVo.attendanceVO;

import net.loyin.netService.domain.CommonMessageVO;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Chao on 2015/12/1.
 */
@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonRootName("data")
@JsonAutoDetect(isGetterVisibility = JsonAutoDetect.Visibility.NONE,fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DsrAttendanceAddVo extends CommonMessageVO {

    @XmlElement(name = "main_message")
    @JsonProperty("main_message")
    private DsrAttendanceAddMainData maindata;

    @Override
    public boolean validate(boolean isReqAudit) {
        return false;
    }

    @Override
    public String validate() {
        String msg = "";
        if (maindata.getAttendanceType() == null || maindata.getAttendanceType().isEmpty()) {
            msg += "缺失考勤类型";
        }
        if (maindata.getAttendanceEndTime() == null || maindata.getAttendanceEndTime().isEmpty()) {
            msg += "缺失结束时间";
        }
        if (maindata.getAttendanceGenre() == null || maindata.getAttendanceGenre().isEmpty()) {
            msg += "缺失类型";
        }
        if (maindata.getAttendanceRemark() == null || maindata.getAttendanceRemark().isEmpty()) {
            msg += "缺失备注";
        }
        if (maindata.getAttendanceStartTime() == null || maindata.getAttendanceStartTime().isEmpty()) {
            msg += "缺失开始时间";
        }

        return msg;
    }

    @Override
    public DsrAttendanceAddMainData getMaindata() {
        return maindata;
    }

    public void setMaindata(DsrAttendanceAddMainData maindata) {
        this.maindata = maindata;
    }
}
