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
public class ProductReturnInfo {

    @XmlElement(name = "product_id")
    @JsonProperty("product_id")
    private String productId;
    @XmlElement(name = "product_name")
    @JsonProperty("product_name")
    private String productName;
    @XmlElement(name = "product_billsn")
    @JsonProperty("product_billsn")
    private String productBillsn;
    @XmlElement(name = "product_his_price")
    @JsonProperty("product_his_price")
    private String productHisPrice;
    @XmlElement(name = "product_price")
    @JsonProperty("product_price")
    private String productPrice;
    @XmlElement(name = "product_brand")
    @JsonProperty("product_brand")
    private String productBrand;
    @XmlElement(name = "product_specification")
    @JsonProperty("product_specification")
    private String productSpecification;
    @XmlElement(name = "product_category")
    @JsonProperty("product_category")
    private String productCategory;

    @XmlElement(name = "product_pic")
    @JsonProperty("product_pic")
    private List<ProductPic> productPic;

    @XmlElement(name = "product_stock")
    @JsonProperty("product_stock")
    private String productStock;

    @XmlElement(name = "product_unit")
    @JsonProperty("product_unit")
    private ProductUnit productUnit;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductBillsn() {
        return productBillsn;
    }

    public void setProductBillsn(String productBillsn) {
        this.productBillsn = productBillsn;
    }

    public String getProductHisPrice() {
        return productHisPrice;
    }

    public void setProductHisPrice(String productHisPrice) {
        this.productHisPrice = productHisPrice;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public String getProductSpecification() {
        return productSpecification;
    }

    public void setProductSpecification(String productSpecification) {
        this.productSpecification = productSpecification;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public List<ProductPic> getProductPic() {
        return productPic;
    }

    public void setProductPic(List<ProductPic> productPic) {
        this.productPic = productPic;
    }

    public String getProductStock() {
        return productStock;
    }

    public void setProductStock(String productStock) {
        this.productStock = productStock;
    }

    public ProductUnit getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(ProductUnit productUnit) {
        this.productUnit = productUnit;
    }
}
