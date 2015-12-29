package net.loyin.netService.vo.orderVo.orderDetailVo;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Chao on 2015/11/23.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderDetailProductInfo {

    @XmlElement(name = "product_id")
    @JsonProperty("product_id")
    private String productId;

    @XmlElement(name = "product_billsn")
    @JsonProperty("product_billsn")
    private String productBillsn;

    @XmlElement(name = "product_name")
    @JsonProperty("product_name")
    private String productName;
    @XmlElement(name = "product_num")
    @JsonProperty("product_num")
    private String productNum;
    @XmlElement(name = "product_sale")
    @JsonProperty("product_sale")
    private String productSale;
    @XmlElement(name = "product_amt")
    @JsonProperty("product_amt")
    private String productAmt;
    @XmlElement(name = "product_unit")
    @JsonProperty("product_unit")
    private String productUnit;
    @XmlElement(name = "product_remark")
    @JsonProperty("product_remark")
    private String productRemark;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductBillsn() {
        return productBillsn;
    }

    public void setProductBillsn(String productBillsn) {
        this.productBillsn = productBillsn;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public String getProductRemark() {
        return productRemark;
    }

    public void setProductRemark(String productRemark) {
        this.productRemark = productRemark;
    }
}
