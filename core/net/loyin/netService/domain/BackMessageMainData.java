package net.loyin.netService.domain;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Administrator on 2015/12/17.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BackMessageMainData {

    @XmlElement(name = "test")
    @JsonProperty("test")
    private String loginName;
}
