package net.loyin.netService.vo.customerVo.customerAddVo;

import net.loyin.netService.domain.CommonMessageVO;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Chao on 2015/11/25.
 */
@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonRootName("data")
@JsonAutoDetect(isGetterVisibility = JsonAutoDetect.Visibility.NONE,fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CustomerAddVo  extends CommonMessageVO {

    @XmlElement(name = "main_message")
    @JsonProperty("main_message")
    private CustomerAddMainData maindata;

    @Override
    public boolean validate(boolean isReqAudit) {
        return false;
    }

    @Override
    public String validate() {
        String msg = "";
        if(maindata.getCustomerType()==null || maindata.getCustomerType().isEmpty()){
            msg ="缺少网店类型,请核对";
        }
        if(maindata.getCustomerName()==null || maindata.getCustomerName().isEmpty()){
            msg ="缺少网点名称,请核对";
        }
        if(maindata.getCustomerContacts()==null || maindata.getCustomerContacts().isEmpty()){
            msg ="缺少网店联系人";
        }
        if(maindata.getContactsPhone()==null || maindata.getContactsPhone().isEmpty()){
            msg ="缺少网点联系电话";
        }
        if(maindata.getCustomerLat() == null || maindata.getCustomerLat().isEmpty()
                ||maindata.getCustomerLng() == null || maindata.getCustomerLng().isEmpty()){
            msg ="缺少网点经纬度";
        }
        return msg;
    }

    @Override
    public CustomerAddMainData getMaindata() {
        return maindata;
    }

    public void setMaindata(CustomerAddMainData maindata) {
        this.maindata = maindata;
    }
}
