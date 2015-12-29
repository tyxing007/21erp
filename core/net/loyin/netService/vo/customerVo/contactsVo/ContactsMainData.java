package net.loyin.netService.vo.customerVo.contactsVo;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Chao on 2015/11/23.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ContactsMainData {

    @XmlElement(name = "contacts_type")
    @JsonProperty("contacts_type")
    private String contactsType;

    @XmlElement(name = "contacts_key")
    @JsonProperty("contacts_key")
    private String contactsKey;

    @XmlElement(name = "page_size")
    @JsonProperty("page_size")
    private String pageSize;

    @XmlElement(name = "page_num")
    @JsonProperty("page_num")
    private String pageNum;

    public String getContactsType() {
        return contactsType;
    }

    public void setContactsType(String contactsType) {
        this.contactsType = contactsType;
    }

    public String getContactsKey() {
        return contactsKey;
    }

    public void setContactsKey(String contactsKey) {
        this.contactsKey = contactsKey;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getPageNum() {
        return pageNum;
    }

    public void setPageNum(String pageNum) {
        this.pageNum = pageNum;
    }
}
