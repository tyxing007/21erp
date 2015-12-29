package net.loyin.netService.vo.productVo;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Chao on 2015/12/8.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductUnitInfo {

    @XmlElement(name = "unit_name")
    @JsonProperty("unit_name")
    private String unitName;

    @XmlElement(name = "unit_num")
    @JsonProperty("unit_num")
    private String unitNum;

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitNum() {
        return unitNum;
    }

    public void setUnitNum(String unitNum) {
        this.unitNum = unitNum;
    }
}
