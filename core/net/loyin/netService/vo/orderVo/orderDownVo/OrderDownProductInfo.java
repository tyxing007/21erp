package net.loyin.netService.vo.orderVo.orderDownVo;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Chao on 2015/12/9.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderDownProductInfo {
    @XmlElement(name = "product_id")
    @JsonProperty("product_id")
    private String productId;
    @XmlElement(name = "product_num")
    @JsonProperty("product_num")
    private String productNum;
    @XmlElement(name = "product_sale")
    @JsonProperty("product_sale")
    private String productSale;
    @XmlElement(name = "product_amt")
    @JsonProperty("product_amt")
    private String productAmt;
    @XmlElement(name = "product_remark")
    @JsonProperty("product_remark")
    private String productRemark;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductNum() {
        return productNum;
    }

    public void setProductNum(String productNum) {
        this.productNum = productNum;
    }

    public String getProductSale() {
        return productSale;
    }

    public void setProductSale(String productSale) {
        this.productSale = productSale;
    }

    public String getProductAmt() {
        return productAmt;
    }

    public void setProductAmt(String productAmt) {
        this.productAmt = productAmt;
    }

    public String getProductRemark() {
        return productRemark;
    }

    public void setProductRemark(String productRemark) {
        this.productRemark = productRemark;
    }
}
