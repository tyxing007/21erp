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
public class ProductReturnMainData {

    @XmlElement(name = "product_info")
    @JsonProperty("product_info")
    private List<ProductReturnInfo> productReturnInfoList;

    public List<ProductReturnInfo> getProductReturnInfoList() {
        return productReturnInfoList;
    }

    public void setProductReturnInfoList(List<ProductReturnInfo> productReturnInfoList) {
        this.productReturnInfoList = productReturnInfoList;
    }
}
