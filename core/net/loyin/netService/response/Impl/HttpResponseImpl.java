package net.loyin.netService.response.Impl;

import com.sun.net.httpserver.HttpExchange;
import net.loyin.netService.dataPacket.HttpDataPacket;
import net.loyin.netService.domain.DataFormats;
import net.loyin.netService.response.IResponseBack;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <pre>HTTP请求响应</pre>
 * <pre>所属模块：系统通讯</pre>
 *
 * @author 张维
 * @version 1.0 创建于 2014/8/1
 */
public class HttpResponseImpl implements IResponseBack {

    private final Object lock = new Object();
    private static int tcount = 1;

    /**
     * HTTP响应
     */
    public void responseBack(HttpDataPacket httpDataPacket) throws Exception {
        if (httpDataPacket.getHttpExchange() == null) {
            return;
        } else {
            byte[] data;
            if (httpDataPacket.getDataFormat().equals(DataFormats.JSON) ||
                    httpDataPacket.getDataFormat().equals(DataFormats.XML)) {
                httpDataPacket.getHttpExchange().getResponseHeaders().set("Content-Type", "text/xml; charset=utf-8");
                data = httpDataPacket.getParsedata().toString().getBytes("UTF-8");
            } else if (httpDataPacket.getDataFormat().equals(DataFormats.BYTE)) {
                httpDataPacket.getHttpExchange().getResponseHeaders().set("Content-Type", "multipart/form-data");
                data = (byte[]) httpDataPacket.getParsedata();
            } else {
                return;
            }
            if (httpDataPacket.getHead() != null && httpDataPacket.getHead().length() > 0) {
                httpDataPacket.getHttpExchange().getResponseHeaders().set("messageHead", httpDataPacket.getHead().trim());
            }
            if (httpDataPacket.getSectionId() != null && httpDataPacket.getSectionId().length() > 0) {
                httpDataPacket.getHttpExchange().getResponseHeaders().set("markId", httpDataPacket.getSectionId().trim());
            }
            String tt = httpDataPacket.getHttpExchange().getResponseHeaders().getFirst("Content-length");
            if (tt == null || tt.length() == 0) {
                httpDataPacket.getHttpExchange().sendResponseHeaders(200, data.length);
            }
            httpDataPacket.getHttpExchange().getResponseHeaders().set("Content-length", data.length + "");
            OutputStream os = httpDataPacket.getHttpExchange().getResponseBody();
            os.write(data);
            os.flush();
            os.close();
            httpDataPacket.getHttpExchange().close();
        }
    }

    private String GetRequestBodyToString(HttpExchange httpExchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        return br.readLine();
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
