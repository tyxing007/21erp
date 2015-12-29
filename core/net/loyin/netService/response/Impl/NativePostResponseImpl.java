package net.loyin.netService.response.Impl;


import net.loyin.netService.dataPacket.NativeHttpDataPacket;
import net.loyin.netService.domain.DataFormats;
import net.loyin.netService.domain.HttpRequestType;
import net.loyin.netService.domain.NetTypes;
import net.loyin.netService.domain.SelectedKeys;
import net.loyin.netService.response.INativeResponse;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;

/**
 * <pre>HTTP POST请求接收</pre>
 * <pre>所属模块：系统通讯</pre>
 *
 * @author 张维
 * @version 1.0 创建于 2014/8/1
 */
public class NativePostResponseImpl implements INativeResponse {

//    @Qualifier("MessageService")
//    @Autowired
//    IMessageService MessageService;

    /**
     * HTTP响应
     *
     */
    public NativeHttpDataPacket response(HttpServletRequest request) throws Exception {
        NativeHttpDataPacket httpDataPacket = new NativeHttpDataPacket();
        httpDataPacket.setDataFormat(DataFormats.OBJECT);
        // 取HTTP请求流
        InputStream inputStream = request.getInputStream();
        byte[] diff = new byte[request.getContentLength()];
        int i = 0;
        int index = 0;
        while ((i = inputStream.read()) != -1) {
            diff[index] = (byte) i;
            index++;
        }
        httpDataPacket.setRawdata(new String(diff, "UTF-8"));
        httpDataPacket.setHttpRequestType(HttpRequestType.POST);
        String dataFormat = request.getHeader("dataFormat");
        if (dataFormat == null || dataFormat.length() == 0) {
            throw new Exception("无法获取请求消息格式");
        }
        httpDataPacket.setRawDataFormat(dataFormat.toUpperCase().equals("JSON") ? DataFormats.JSON : DataFormats.XML);
        httpDataPacket.setNetType(NetTypes.HTTP);
        httpDataPacket.setSelectedKey(SelectedKeys.READ);
        httpDataPacket.setRequest(request);
        httpDataPacket.setHead(request.getHeader("messageHead"));
        httpDataPacket.setSectionId(request.getHeader("markId"));

        return httpDataPacket;
    }
}