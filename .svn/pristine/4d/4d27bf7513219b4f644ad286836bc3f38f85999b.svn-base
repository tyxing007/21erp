package net.loyin.netService.request.Impl;


import com.sun.net.httpserver.HttpExchange;
import net.loyin.netService.dataPacket.HttpDataPacket;
import net.loyin.netService.request.IRequest;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.*;

/**
 * <pre>处理HTTP GET请求</pre>
 * <pre>所属模块：系统通讯</pre>
 * @author 张维
 * @version 1.0 创建于 2014/8/1
 */

public class GetRequestImpl implements IRequest {

    /**
     * 发起请求
     *
     */
    public void request(HttpDataPacket httpDataPacket) throws Exception
    {

    }

    /**
     * HTTP响应
     *
     * @param httpExchange
     */
    public void response(HttpExchange httpExchange) throws Exception {
        Map<String, Object> parameters = new HashMap<String, Object>();
        URI requestedUri = httpExchange.getRequestURI();
        String query = requestedUri.getRawQuery();
        parseQuery(query, parameters);
        Iterator it = parameters.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry m = (Map.Entry) it.next();
            System.out.print(m.getKey() + ":" + m.getValue() + "\n");
        }
        httpExchange.close();
    }

    private void parseQuery(String query, Map<String, Object> parameters) throws UnsupportedEncodingException {
        if (query != null) {
            String pairs[] = query.split("[&]");
            for (String pair : pairs) {
                String param[] = pair.split("[=]");
                String key = null;
                String value = null;
                if (param.length > 0) {
                    key = URLDecoder.decode(param[0], System.getProperty("file.encoding"));
                }
                if (param.length > 1) {
                    value = URLDecoder.decode(param[1], System.getProperty("file.encoding"));
                }
                if (parameters.containsKey(key)) {
                    Object obj = parameters.get(key);
                    if (obj instanceof List<?>) {
                        List<String> values = (List<String>) obj;
                        values.add(value);
                    } else if (obj instanceof String) {
                        List<String> values = new ArrayList<String>();
                        values.add((String) obj);
                        values.add(value);
                        parameters.put(key, values);
                    }
                } else {
                    parameters.put(key, value);
                }
            }
        }
    }
}
