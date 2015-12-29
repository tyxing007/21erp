package net.loyin.netService.vo.dsrVo.attendanceVO;

import net.loyin.netService.domain.CommonMessageVO;
import net.loyin.netService.vo.customerVo.customerVisitsVo.CustomerVisitsMainData;
import org.codehaus.jackson.annotate.JsonAutoDetect;
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
@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonRootName("data")
@JsonAutoDetect(isGetterVisibility = JsonAutoDetect.Visibility.NONE,fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DsrAttendanceVo extends CommonMessageVO {

    @XmlElement(name = "main_message")
    @JsonProperty("main_message")
    private DsrAttendanceMainData maindata;
    @Override
    public boolean validate(boolean isReqAudit) {
        return false;
    }

    @Override
    public String validate() {
        String msg = "";
        if (maindata.getPageNum() == null || maindata.getPageNum().isEmpty()) {
            msg += "缺失页码";
        }
        if (maindata.getPageSize() == null || maindata.getPageSize().isEmpty()) {
            msg += "缺失页数大小";
        }
        if(maindata.getAttendanceType() == null || maindata.getAttendanceType().isEmpty()){
            msg += "缺失搜索类型";
        }
        return msg;
    }

    @Override
    public DsrAttendanceMainData getMaindata() {
        return maindata;
    }

    public void setMaindata(DsrAttendanceMainData maindata) {
        this.maindata = maindata;
    }
}
