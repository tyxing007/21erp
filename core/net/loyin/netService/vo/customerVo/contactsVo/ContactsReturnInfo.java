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
@XmlRootElement(name = "contacts")
@XmlAccessorType(XmlAccessType.FIELD)
public class ContactsReturnInfo {

    @XmlElement(name = "contacts_name")
    @JsonProperty("contacts_name")
    private String contactsName;
    @XmlElement(name = "contacts_pic")
    @JsonProperty("contacts_pic")
    private String contactsPic;
    @XmlElement(name = "contactsPhone")
    @JsonProperty("contactsPhone")
    private String contactsPhone;

    public String getContactsName() {
        return contactsName;
    }

    public void setContactsName(String contactsName) {
        this.contactsName = contactsName;
    }

    public String getContactsPhone() {
        return contactsPhone;
    }

    public void setContactsPhone(String contactsPhone) {
        this.contactsPhone = contactsPhone;
    }

    public String getContactsPic() {
        return contactsPic;
    }

    public void setContactsPic(String contactsPic) {
        this.contactsPic = contactsPic;
    }
}
