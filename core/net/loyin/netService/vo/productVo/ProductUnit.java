package net.loyin.netService.vo.productVo;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * Created by Chao on 2015/12/8.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductUnit {
    @XmlElement(name = "unit")
    @JsonProperty("unit")
    private List<ProductUnitInfo> unitInfos;

    public List<ProductUnitInfo> getUnitInfos() {
        return unitInfos;
    }

    public void setUnitInfos(List<ProductUnitInfo> unitInfos) {
        this.unitInfos = unitInfos;
    }
}
