package net.loyin.netService.vo.customerVo.customerInfoVo;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Chao on 2015/11/10.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerMainData {

    //搜索关键字
    @XmlElement(name = "search_key")
    @JsonProperty("search_key")
    private String key;
    //搜索类型
    @XmlElement(name = "seacrch_type")
    @JsonProperty("seacrch_type")
    private String type;
    //排序
    @XmlElement(name = "seacrch_sort")
    @JsonProperty("seacrch_sort")
    private String sort;
    //状态
    @XmlElement(name = "seacrch_status")
    @JsonProperty("seacrch_status")
    private String status;
    //每次多少个
    @XmlElement(name = "page_size")
    @JsonProperty("page_size")
    private String size;
    //第几月
    @XmlElement(name = "page_num")
    @JsonProperty("page_num")
    private String num;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}