package net.loyin.netService.vo.orderVo.orderInfoVo;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Chao on 2015/11/23.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderMainData {

    //页码
    @XmlElement(name = "order_page")
    @JsonProperty("order_page")
    private String page;

    //去处几条记录
    @XmlElement(name = "order_pageSize")
    @JsonProperty("order_pageSize")
    private String pageSize;

    //关键字搜索
    @XmlElement(name = "search_key")
    @JsonProperty("search_key")
    private String key;


    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
