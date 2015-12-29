package net.loyin.netService.vo.offlineVo.OfflineCustomerVo;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Chao on 2015/12/10.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class OfflineCustomerInfo {

    @XmlElement(name = "customer_id")
    @JsonProperty("customer_id")
    private String customerId;
    @XmlElement(name = "customer_name")
    @JsonProperty("customer_name")
    private String customerName;
    @XmlElement(name = "customer_address")
    @JsonProperty("customer_address")
    private String customerAddress;
    @XmlElement(name = "customer_tel")
    @JsonProperty("customer_tel")
    private String customerTel;
    @XmlElement(name = "customer_lat")
    @JsonProperty("customer_lat")
    private String customerLat;
    @XmlElement(name = "customer_lng")
    @JsonProperty("customer_lng")
    private String customerLng;
    @XmlElement(name = "customer_contacts")
    @JsonProperty("customer_contacts")
    private String customerContacts;
    @XmlElement(name = "customer_contacts_phone")
    @JsonProperty("customer_contacts_phone")
    private String customerContactsPhone;
    @XmlElement(name = "customer_pic")
    @JsonProperty("customer_pic")
    private String customerPic;
    @XmlElement(name = "customer_status")
    @JsonProperty("customer_status")
    private String customerStatus;
    @XmlElement(name = "customer_count")
    @JsonProperty("customer_count")
    private String customerCount;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerTel() {
        return customerTel;
    }

    public void setCustomerTel(String customerTel) {
        this.customerTel = customerTel;
    }

    public String getCustomerLat() {
        return customerLat;
    }

    public void setCustomerLat(String customerLat) {
        this.customerLat = customerLat;
    }

    public String getCustomerLng() {
        return customerLng;
    }

    public void setCustomerLng(String customerLng) {
        this.customerLng = customerLng;
    }

    public String getCustomerContacts() {
        return customerContacts;
    }

    public void setCustomerContacts(String customerContacts) {
        this.customerContacts = customerContacts;
    }

    public String getCustomerContactsPhone() {
        return customerContactsPhone;
    }

    public void setCustomerContactsPhone(String customerContactsPhone) {
        this.customerContactsPhone = customerContactsPhone;
    }

    public String getCustomerPic() {
        return customerPic;
    }

    public void setCustomerPic(String customerPic) {
        this.customerPic = customerPic;
    }

    public String getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(String customerStatus) {
        this.customerStatus = customerStatus;
    }

    public String getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(String customerCount) {
        this.customerCount = customerCount;
    }
}
