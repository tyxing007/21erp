package net.loyin.netService.vo.sysVo.parameVo;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Chao on 2015/12/15.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ParameReturnInfo {

    @XmlElement(name = "sys_parame_id")
    @JsonProperty("sys_parame_id")
    private String parameId;

    @XmlElement(name = "sys_parame_name")
    @JsonProperty("sys_parame_name")
    private String parameName;

    public String getParameId() {
        return parameId;
    }

    public void setParameId(String parameId) {
        this.parameId = parameId;
    }

    public String getParameName() {
        return parameName;
    }

    public void setParameName(String parameName) {
        this.parameName = parameName;
    }
}
