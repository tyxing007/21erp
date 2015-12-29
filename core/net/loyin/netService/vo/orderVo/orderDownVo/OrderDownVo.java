package net.loyin.netService.vo.orderVo.orderDownVo;

import net.loyin.netService.domain.CommonMessageVO;
import net.loyin.netService.vo.orderVo.orderInfoVo.OrderMainData;
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
public class OrderDownVo extends CommonMessageVO {

    @XmlElement(name = "main_message")
    @JsonProperty("main_message")
    private OrderDownMainData maindata;

    @Override
    public boolean validate(boolean isReqAudit) {
        return false;
    }

    @Override
    public String validate() {
        String msg = "";
        if(maindata.getCustomerId()==null || maindata.getCustomerId().isEmpty()){
            msg ="缺少客户编号,";
        }
        if(maindata.getOrderContacts()==null || maindata.getOrderContacts().isEmpty()){
            msg ="缺少订单联系人,";
        }
        if(maindata.getOrderContactsPhone()==null || maindata.getOrderContactsPhone().isEmpty()){
            msg ="缺少订单联系人电话,";
        }
        if(maindata.getOrderAddress()==null || maindata.getOrderAddress().isEmpty()){
            msg ="缺少送货地址,";
        }
        if(maindata.getOrderProducts()==null || maindata.getOrderProducts().getProductInfo() == null ||maindata.getOrderProducts().getProductInfo().size()==0){
            msg = "缺少产品信息";
        }
        return msg;
    }

    @Override
    public OrderDownMainData getMaindata() {
        return maindata;
    }

    public void setMaindata(OrderDownMainData maindata) {
        this.maindata = maindata;
    }
}
