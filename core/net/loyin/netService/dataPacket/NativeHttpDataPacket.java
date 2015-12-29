package net.loyin.netService.dataPacket;


import net.loyin.netService.domain.HttpRequestType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 张维 on 2014/11/5.
 * 原生HTTP数据包
 */
public class NativeHttpDataPacket extends SyncDataPacket {
    private String responseType;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpRequestType httpRequestType;


    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public HttpRequestType getHttpRequestType() {
        return httpRequestType;
    }

    public void setHttpRequestType(HttpRequestType httpRequestType) {
        this.httpRequestType = httpRequestType;
    }

    /**
     * 获取返回基础数据包信息
     *
     * @param dataPacket
     */
    @Override
    protected void GetBackBaseDataPacket(DataPacket dataPacket) {
        super.GetBackBaseDataPacket(dataPacket);
        if (dataPacket instanceof NativeHttpDataPacket) {
            NativeHttpDataPacket httpDataPacket = (NativeHttpDataPacket) dataPacket;
            httpDataPacket.setRequest(this.request);
            httpDataPacket.setHttpRequestType(this.httpRequestType);
            httpDataPacket.setResponse(this.response);
        }
    }

    /**
     * 获取返回基础数据包信息
     */
    public DataPacket GetBackBaseDataPacket()
    {
        NativeHttpDataPacket httpDataPacket = new NativeHttpDataPacket();
        this.GetBackBaseDataPacket(httpDataPacket);
        return httpDataPacket;
    }
}
