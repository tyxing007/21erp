package net.loyin.netService.vo.offlineVo.OfflineProductVo;

import net.loyin.netService.domain.CommonMessageVO;
import net.loyin.netService.vo.offlineVo.offlineVo.OfflineMainData;
import net.loyin.netService.vo.orderVo.orderDetailVo.OrderDetailInfo;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Chao on 2015/12/10.
 */
@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonRootName("data")
@JsonAutoDetect(isGetterVisibility = JsonAutoDetect.Visibility.NONE,fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class OfflineProductVo extends CommonMessageVO {
    @XmlElement(name = "main_message")
    @JsonProperty("main_message")
    private OfflineProductMainData maindata;

    @Override
    public boolean validate(boolean isReqAudit) {
        return false;
    }

    @Override
    public String validate() {
        return null;
    }

    @Override
    public OfflineProductMainData getMaindata() {
        return maindata;
    }

    public void setMaindata(OfflineProductMainData maindata) {
        this.maindata = maindata;
    }
}
