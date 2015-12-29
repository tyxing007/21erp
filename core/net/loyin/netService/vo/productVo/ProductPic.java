package net.loyin.netService.vo.productVo;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Chao on 2015/12/8.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductPic {

    @XmlElement(name = "pic")
    @JsonProperty("pic")
    private String pic;

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
