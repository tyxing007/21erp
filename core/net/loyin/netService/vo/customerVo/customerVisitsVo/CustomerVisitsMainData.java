package net.loyin.netService.vo.customerVo.customerVisitsVo;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Chao on 2015/12/1.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerVisitsMainData {
    //搜索关键字
    @XmlElement(name = "search_key")
    @JsonProperty("search_key")
    private String key;
    //页面大小
    @XmlElement(name = "page_size")
    @JsonProperty("page_size")
    private String pageSize;
    //页码
    @XmlElement(name = "page_num")
    @JsonProperty("page_num")
    private String pageNum;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
