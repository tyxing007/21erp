package net.loyin.netService.vo.sysVo.parameVo;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Chao on 2015/12/15.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ParameMainData {

    @XmlElement(name = "sys_parame")
    @JsonProperty("sys_parame")
    private String parameType;

    @XmlElement(name = "sys_type")
    @JsonProperty("sys_type")
    private String merchType;

    @XmlElement(name = "sys_specification")
    @JsonProperty("sys_specification")
    private String specification;

    @XmlElement(name = "sys_brand")
    @JsonProperty("sys_brand")
    private String brand;

    @XmlElement(name = "sys_search")
    @JsonProperty("sys_search")
    private String search;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getParameType() {
        return parameType;
    }

    public void setParameType(String parameType) {
        this.parameType = parameType;
    }

    public String getMerchType() {
        return merchType;
    }

    public void setMerchType(String merchType) {
        this.merchType = merchType;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
