package net.loyin.netService.vo.offlineVo.offlineVo;

import net.loyin.netService.domain.CommonMessageVO;
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
public class OfflineVo extends CommonMessageVO {

    @XmlElement(name = "main_message")
    @JsonProperty("main_message")
    private OfflineMainData maindata;

    @Override
    public boolean validate(boolean isReqAudit) {
        return false;
    }

    @Override
    public String validate() {
        String msg = "";
        if(maindata.getUpdateType() == null || maindata.getUpdateType().isEmpty()){
            msg += "查询类型不存在，请核对";
        }
        if(maindata.getIndex() == null || maindata.getIndex().isEmpty()){
            msg +="记录行号确实,请核对";
        }
        return msg;
    }

    @Override
    public OfflineMainData getMaindata() {
        return maindata;
    }

    public void setMaindata(OfflineMainData maindata) {
        this.maindata = maindata;
    }
}
