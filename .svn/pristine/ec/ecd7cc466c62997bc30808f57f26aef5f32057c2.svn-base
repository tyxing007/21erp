package net.loyin.netService.dataPacket;

import com.sun.net.httpserver.HttpExchange;
import net.loyin.netService.domain.HttpRequestType;
import net.loyin.netService.domain.SelectedKeys;

import java.util.Map;

/**
 * <pre>HTTP通讯数据包</pre>
 * <pre>所属模块：系统通讯</pre>
 *
 * @author 吴为超
 * @version 1.0 创建于 2014/8/1
 */
public class HttpDataPacket extends SyncDataPacket {
    public HttpDataPacket() {

    }

    private String PagePath;
    private int Timeout;
    private HttpRequestType httpRequestType;
    private String RequestDataType;
    private HttpExchange httpExchange;

    private Object ResponseData;

    private Map<String,String> cookie;

    private String ip;

    public synchronized Object getResponse() {
        try {
            if (this.getSelectedKey().equals(SelectedKeys.READ)) {
                return null;
            }
            while (true) {
                if (this.ResponseData != null) {
                    break;
                }
                Thread.sleep(300);
            }
            return ResponseData;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getPagePath() {
        return PagePath;
    }

    public void setPagePath(String pagePath) {
        PagePath = pagePath;
    }

    public int getTimeout() {
        return Timeout;
    }

    public void setTimeout(int timeout) {
        Timeout = timeout;
    }

    public HttpRequestType getHttpRequestType() {
        return httpRequestType;
    }

    public void setHttpRequestType(HttpRequestType httpRequestType) {
        this.httpRequestType = httpRequestType;
    }

    public String getRequestDataType() {
        return RequestDataType;
    }

    public void setRequestDataType(String requestDataType) {
        RequestDataType = requestDataType;
    }

    public HttpExchange getHttpExchange() {
        return httpExchange;
    }

    public void setHttpExchange(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
    }

    public void setResponseData(Object responseData) {
        ResponseData = responseData;
    }

    public Object getResponseData() {
        return ResponseData;
    }

    public Map<String,String> getCookie() {
        return cookie;
    }

    public void setCookie(Map<String,String> cookie) {
        this.cookie = cookie;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * 获取返回基础数据包信息
     *
     * @param dataPacket
     */
    @Override
    protected void GetBackBaseDataPacket(DataPacket dataPacket) {
        super.GetBackBaseDataPacket(dataPacket);
        if (dataPacket instanceof HttpDataPacket) {
            HttpDataPacket httpDataPacket = (HttpDataPacket) dataPacket;
            httpDataPacket.setHttpExchange(this.httpExchange);
            httpDataPacket.setHttpRequestType(this.httpRequestType);
            httpDataPacket.setPagePath(this.PagePath);
            httpDataPacket.setTimeout(this.Timeout);
            httpDataPacket.setResponseData(this.ResponseData);
        }
    }

    /**
     * 获取返回基础数据包信息
     */
    public DataPacket GetBackBaseDataPacket()
    {
        HttpDataPacket httpDataPacket = new HttpDataPacket();
        this.GetBackBaseDataPacket(httpDataPacket);
        return httpDataPacket;
    }
}