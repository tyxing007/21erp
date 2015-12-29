package net.loyin.netService.vo.sysVo.parameVo;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * Created by Chao on 2015/12/15.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ParameReturnMainData {

    @XmlElement(name = "sys_parame_list")
    @JsonProperty("sys_parame_list")
    private List<ParameReturnInfo> parameInfos;

    public List<ParameReturnInfo> getParameInfos() {
        return parameInfos;
    }

    public void setParameInfos(List<ParameReturnInfo> parameInfos) {
        this.parameInfos = parameInfos;
    }
}
