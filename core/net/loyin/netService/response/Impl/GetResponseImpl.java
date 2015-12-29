package net.loyin.netService.response.Impl;


import com.sun.net.httpserver.HttpExchange;
import net.loyin.netService.dataPacket.HttpDataPacket;
import net.loyin.netService.response.IResponse;

import java.io.IOException;

/**
 * <pre>HTTP GET请求接收</pre>
 * <pre>所属模块：系统通讯</pre>
 * @author 张维
 * @version 1.0 创建于 2014/8/1
 */
public class GetResponseImpl implements IResponse {

    /**
     * HTTP响应
     *
     * @param httpExchange
     */
    public HttpDataPacket response(HttpExchange httpExchange) throws IOException {
        return null;
    }
}
