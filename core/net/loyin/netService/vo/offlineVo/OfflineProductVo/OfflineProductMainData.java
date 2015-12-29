package net.loyin.netService.vo.offlineVo.OfflineProductVo;

import net.loyin.netService.vo.productVo.ProductReturnInfo;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * Created by Chao on 2015/12/10.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class OfflineProductMainData {

    @XmlElement(name = "product_info")
    @JsonProperty("product_info")
    private ProductInfo productInfos;

    public ProductInfo getProductInfos() {
        return productInfos;
    }

    public void setProductInfos(ProductInfo productInfos) {
        this.productInfos = productInfos;
    }
}
