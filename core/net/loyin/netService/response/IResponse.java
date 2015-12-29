package net.loyin.netService.response;

import com.sun.net.httpserver.HttpExchange;
import net.loyin.netService.dataPacket.HttpDataPacket;

/**
 * <pre>HTTP请求接收接口</pre>
 * <pre>所属模块：系统通讯</pre>
 * @author 张维
 * @version 1.0 创建于 2014/8/1
 */
public interface IResponse {
    /**
     * HTTP响应
     * @param httpExchange
     */
    public HttpDataPacket response(HttpExchange httpExchange) throws Exception;
}
