/**
 * CommonHeader.java
 * 版权声明 力铭科技版权所有
 * 项目组：广州农村商业银行电子商业汇票系统
 * 修订记录：
 * 1)创建者：林邵诚
 * 时 间：Aug 11, 2010
 * 描 述：创建
 */
package net.loyin.netService.domain;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * <pre>功能描述: 公共报文头</pre>
 * <br>JDK版本：1.5+
 *
 * @author 林邵诚
 * @version 1.0.1 创建于 Aug 11, 2010
 * @since 1.0
 * 修改日期：Aug 11, 2010 林邵诚
 */
@XmlRootElement(name = "head_message")
@JsonRootName("head_message")
@XmlAccessorType(XmlAccessType.FIELD)
public class CommonHeader implements Serializable {


    public CommonHeader() {
        Date today = new Date();
        this.setHostDt(simpledateformate.format(today));
        this.setHostTime(simpletimeformate.format(today));
    }

    private static SimpleDateFormat simpledateformate = new SimpleDateFormat("yyyyMMdd");
    private static SimpleDateFormat simpletimeformate = new SimpleDateFormat("HHmmss");
    /**
     * UID
     */
    private static final long serialVersionUID = -2394248816482720141L;

    //序号
    @XmlElement(name = "message_no")
    @JsonProperty("message_no")
    private String messageNo;
    //版本号
    @XmlElement(name = "app_no")
    @JsonProperty("app_no")
    private String appNo;
    //数据结构版本号
    @XmlElement(name = "data_no")
    @JsonProperty("data_no")
    private String dataNo;
    //请求时间
    @XmlElement(name = "request_time")
    @JsonProperty("request_time")
    private String requestTime;
    //业务编号
    @XmlElement(name = "business_code")
    @JsonProperty("business_code")
    private String businessCode;

    //业务编号
    @XmlElement(name = "channel_no")
    @JsonProperty("channel_no")
    private String snd_chnl_no;

    //主机日期
    private String hostDt;
    //主机时间
    private String hostTime;

    //访问IP
    private String ip;

    /**
     * 返回码
     * <pre>
     * 	SUCCESS代表成功
     * 	FAILURE表示失败
     * </pre>
     */
    @XmlElement(name = "request_succes")
    @JsonProperty("request_succes")
    private String rsp_no;

    /**
     * 返回信息
     * <pre>
     * 	中文信息
     * </pre>
     */
    @XmlElement(name = "request_msg")
    @JsonProperty("request_msg")
    private String rsp_msg;

    public String getMessageNo() {
        return messageNo;
    }

    public void setMessageNo(String messageNo) {
        this.messageNo = messageNo;
    }

    public String getAppNo() {
        return appNo;
    }

    public void setAppNo(String appNo) {
        this.appNo = appNo;
    }

    public String getDataNo() {
        return dataNo;
    }

    public void setDataNo(String dataNo) {
        this.dataNo = dataNo;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getHostDt() {
        return hostDt;
    }

    public void setHostDt(String hostDt) {
        this.hostDt = hostDt;
    }

    public String getHostTime() {
        return hostTime;
    }

    public void setHostTime(String hostTime) {
        this.hostTime = hostTime;
    }

    public String getRsp_no() {
        return rsp_no;
    }

    public void setRsp_no(String rsp_no) {
        if(this.rsp_no == null){
            this.rsp_no = "";
        }
        this.rsp_no = rsp_no;
    }

    public String getRsp_msg() {
        return rsp_msg;
    }

    public void setRsp_msg(String rsp_msg) {
        if(this.rsp_msg == null){
            this.rsp_msg = "";
        }
        this.rsp_msg = rsp_msg;
    }


    public String getSnd_chnl_no() {
        return snd_chnl_no;
    }

    public void setSnd_chnl_no(String snd_chnl_no) {
        this.snd_chnl_no = snd_chnl_no;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
