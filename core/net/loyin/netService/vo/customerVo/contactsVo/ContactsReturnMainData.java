package net.loyin.netService.vo.customerVo.contactsVo;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by Chao on 2015/11/25.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ContactsReturnMainData {

    @XmlElement(name = "contacts_type")
    @JsonProperty("contacts_type")
    private String contactsType;


    @XmlElement(name = "contacts_info")
    @JsonProperty("contacts_info")
    private List<ContactsReturnInfo> contactsInfo;

    public String getContactsType() {
        return contactsType;
    }

    public void setContactsType(String contactsType) {
        this.contactsType = contactsType;
    }

    public List<ContactsReturnInfo> getContactsInfo() {
        return contactsInfo;
    }

    public void setContactsInfo(List<ContactsReturnInfo> contactsInfo) {
        this.contactsInfo = contactsInfo;
    }
}
