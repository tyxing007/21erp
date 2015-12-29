package net.loyin.netService.vo.orderVo.orderInfoVo;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * Created by Chao on 2015/11/23.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderReturnMainData {

    @XmlElement(name = "order_info")
    @JsonProperty("order_info")
    private List<OrderInfo> orderInfoList;

    public List<OrderInfo> getOrderInfoList() {
        return orderInfoList;
    }

    public void setOrderInfoList(List<OrderInfo> orderInfoList) {
        this.orderInfoList = orderInfoList;
    }
}
