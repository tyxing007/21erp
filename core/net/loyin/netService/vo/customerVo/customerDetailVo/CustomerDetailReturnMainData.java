package net.loyin.netService.vo.customerVo.customerDetailVo;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Chao on 2015/11/10.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerDetailReturnMainData {

    //客户类型
    @XmlElement(name = "customer_type")
    @JsonProperty("customer_type")
    private String customerType;
    //网店名称
    @XmlElement(name = "customer_name")
    @JsonProperty("customer_name")
    private String customerName;
    //联系人
    @XmlElement(name = "customer_contacts")
    @JsonProperty("customer_contacts")
    private String customerContacts;
    //联系人手机
    @XmlElement(name = "contacts_phone")
    @JsonProperty("contacts_phone")
    private String contactsPhone;
    //联系人传真
    @XmlElement(name = "contacts_fax")
    @JsonProperty("contacts_fax")
    private String contactsFax;
    //网店地址
    @XmlElement(name = "customer_address")
    @JsonProperty("customer_address")
    private String customerAddress;
    //网店经度
    @XmlElement(name = "customer_lng")
    @JsonProperty("customer_lng")
    private String customerLng;
    //网店维度
    @XmlElement(name = "customer_lat")
    @JsonProperty("customer_lat")
    private String customerLat;
    //网店图片
    @XmlElement(name = "customer_pic")
    @JsonProperty("customer_pic")
    private String customerPic;

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerContacts() {
        return customerContacts;
    }

    public void setCustomerContacts(String customerContacts) {
        this.customerContacts = customerContacts;
    }

    public String getContactsPhone() {
        return contactsPhone;
    }

    public void setContactsPhone(String contactsPhone) {
        this.contactsPhone = contactsPhone;
    }

    public String getContactsFax() {
        return contactsFax;
    }

    public void setContactsFax(String contactsFax) {
        this.contactsFax = contactsFax;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerLng() {
        return customerLng;
    }

    public void setCustomerLng(String customerLng) {
        this.customerLng = customerLng;
    }

    public String getCustomerLat() {
        return customerLat;
    }

    public void setCustomerLat(String customerLat) {
        this.customerLat = customerLat;
    }

    public String getCustomerPic() {
        return customerPic;
    }

    public void setCustomerPic(String customerPic) {
        this.customerPic = customerPic;
    }
}
