package net.loyin.netService.vo.orderVo.orderInfoVo;

import net.loyin.netService.domain.CommonMessageVO;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Chao on 2015/11/23.
 */
@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonRootName("data")
@JsonAutoDetect(isGetterVisibility = JsonAutoDetect.Visibility.NONE,fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class OrderVo extends CommonMessageVO {

    @XmlElement(name = "main_message")
    @JsonProperty("main_message")
    private OrderMainData maindata;

    @Override
    public boolean validate(boolean isReqAudit) {
        return false;
    }

    @Override
    public String validate() {
        String msg = "";
        if(maindata.getPage()==null || maindata.getPage().isEmpty()){
            msg ="缺少页码,请核对";
        }
        if(maindata.getPageSize()==null || maindata.getPageSize().isEmpty()){
            msg ="缺少记录数量,请核对";
        }
        return msg;
    }

    @Override
    public OrderMainData getMaindata() {
        return maindata;
    }

    public void setMaindata(OrderMainData maindata) {
        this.maindata = maindata;
    }
}
