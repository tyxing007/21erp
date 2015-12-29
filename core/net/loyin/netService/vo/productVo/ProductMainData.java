package net.loyin.netService.vo.productVo;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Chao on 2015/12/8.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductMainData {

    @XmlElement(name = "search_key")
    @JsonProperty("search_key")
    private String searchKey;
    @XmlElement(name = "search_customer")
    @JsonProperty("search_customer")
    private String searchCustomer;
    @XmlElement(name = "search_type")
    @JsonProperty("search_type")
    private String searchType;
    @XmlElement(name = "product_category")
    @JsonProperty("product_category")
    private String productCategory;
    @XmlElement(name = "product_brand")
    @JsonProperty("product_brand")
    private String productBrand;
    @XmlElement(name = "product_spe")
    @JsonProperty("product_spe")
    private String productSpe;
    @XmlElement(name = "page_size")
    @JsonProperty("page_size")
    private String pageSize;
    @XmlElement(name = "page_num")
    @JsonProperty("page_num")
    private String pageNum;

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public String getSearchCustomer() {
        return searchCustomer;
    }

    public void setSearchCustomer(String searchCustomer) {
        this.searchCustomer = searchCustomer;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public String getProductSpe() {
        return productSpe;
    }

    public void setProductSpe(String productSpe) {
        this.productSpe = productSpe;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getPageNum() {
        return pageNum;
    }

    public void setPageNum(String pageNum) {
        this.pageNum = pageNum;
    }
}
