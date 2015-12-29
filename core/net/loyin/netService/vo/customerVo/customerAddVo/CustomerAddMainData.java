package net.loyin.netService.vo.customerVo.customerAddVo;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Chao on 2015/11/25.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerAddMainData {

    @XmlElement(name = "customer_type")
    @JsonProperty("customer_type")
    private String customerType;
    @XmlElement(name = "customer_name")
    @JsonProperty("customer_name")
    private String customerName;
    @XmlElement(name = "customer_contacts")
    @JsonProperty("customer_contacts")
    private String customerContacts;
    @XmlElement(name = "contacts_phone")
    @JsonProperty("contacts_phone")
    private String contactsPhone;
    @XmlElement(name = "contacts_fax")
    @JsonProperty("contacts_fax")
    private String contactsFax;
    @XmlElement(name = "customer_address")
    @JsonProperty("customer_address")
    private String customerAddress;
    @XmlElement(name = "customer_lng")
    @JsonProperty("customer_lng")
    private String customerLng;
    @XmlElement(name = "customer_lat")
    @JsonProperty("customer_lat")
    private String customerLat;

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
}