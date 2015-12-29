package net.loyin.netService.vo.orderVo.orderOperatingVo;

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
 * Created by Chao on 2015/11/23.
 */
@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonRootName("data")
@JsonAutoDetect(isGetterVisibility = JsonAutoDetect.Visibility.NONE,fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class OrderOperatingVO extends CommonMessageVO {

    @XmlElement(name = "main_message")
    @JsonProperty("main_message")
    private OrderOperatingMainData maindata;

    @Override
    public boolean validate(boolean isReqAudit) {
        return false;
    }

    @Override
    public String validate() {


        String msg = "";
        if(maindata.getOrderId() == null || maindata.getOrderId().length()==0){
            msg += "订单编号不存在,请核对";
        }
        if(maindata.getOrderType() == null || maindata.getOrderType().length()==0){
            msg += "操作类型不存在,请核对";
        }
        return msg;
    }

    @Override
    public OrderOperatingMainData getMaindata() {
        return maindata;
    }

    public void setMaindata(OrderOperatingMainData maindata) {
        this.maindata = maindata;
    }
}