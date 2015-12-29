package net.loyin.netService.vo.orderVo.orderDownVo;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by Chao on 2015/12/14.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderDownProduct {

    @XmlElement(name = "product_info")
    @JsonProperty("product_info")
    private List<OrderDownProductInfo> productInfo;

    public List<OrderDownProductInfo> getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(List<OrderDownProductInfo> productInfo) {
        this.productInfo = productInfo;
    }
}
