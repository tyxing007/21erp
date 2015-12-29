package net.loyin.netService.vo.offlineVo.offlineVo;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Created by Chao on 2015/12/10.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class OfflineMainData {

    @XmlElement(name = "update_type")
    @JsonProperty("update_type")
    private String updateType;

    @XmlElement(name = "index")
    @JsonProperty("index")
    private String index;

    @XmlTransient
    private boolean exception;



    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public boolean isException() {
        return exception;
    }

    public void setException(boolean exception) {
        this.exception = exception;
    }
}
