package net.loyin.netService.request.Impl;


import net.loyin.netService.NetCache;
import net.loyin.netService.dataPacket.HttpDataPacket;
import net.loyin.netService.domain.DataFormats;
import net.loyin.netService.request.IRequest;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * <pre>处理HTTP POST请求</pre>
 * <pre>所属模块：系统通讯</pre>
 *
 * @author 张维
 * @version 1.0 创建于 2014/8/1
 */

public class PostRequestImpl implements IRequest {

//    @Qualifier("NetRuntimeHelper")
//    @Autowired
//    INetRuntimeHelper NetRuntimeHelper;

    /**
     * 发起请求
     *
     * @param dataPacket
     */
    public void request(HttpDataPacket httpDataPacket) throws Exception {
//        Channel channel = NetCache.GetChannel(httpDataPacket.getChannelNo());
//        if (channel != null) {
//            if (httpDataPacket.getDataFormat() == DataFormats.BYTE || httpDataPacket.getDataFormat() == DataFormats.XML || httpDataPacket.getDataFormat() == DataFormats.JSON) {
//                String POST_URL = "http://" + channel.getIpAddress() + ":" + channel.getPort() + "/" + httpDataPacket.getPagePath();
//                URL url = new URL(POST_URL);
//                HttpURLConnection httpURLConn = (HttpURLConnection) url.openConnection();
//                httpURLConn.setDoOutput(true);
//                httpURLConn.setRequestMethod("POST");
//                if (httpDataPacket.getRequestDataType() != null && httpDataPacket.getRequestDataType().length() > 0) {
//                    httpURLConn.setRequestProperty("RequestDataType", httpDataPacket.getRequestDataType());
//                }
//                httpURLConn.setRequestProperty("messageHead", httpDataPacket.getHead());
//                String sectionId;
//                if (httpDataPacket.getSectionId() != null && httpDataPacket.getSectionId().length() > 0) {
//                    sectionId = httpDataPacket.getSectionId();
//                } else {
//                    sectionId = NetRuntimeHelper.getNetSectionId();
//                }
//                httpURLConn.setRequestProperty("markId", sectionId);
//                httpURLConn.setRequestProperty("dataFormat", httpDataPacket.getDataFormat().toString());
//                httpDataPacket.setSectionId(sectionId);
//                DataOutputStream out = new DataOutputStream(httpURLConn.getOutputStream());
//                byte[] data;
//                if (httpDataPacket.getDataFormat() == DataFormats.XML || httpDataPacket.getDataFormat() == DataFormats.JSON) {
//                    data = httpDataPacket.getParsedata().toString().getBytes("UTF-8");
//                } else if (httpDataPacket.getDataFormat() == DataFormats.BYTE) {
//                    data = (byte[]) httpDataPacket.getParsedata();
//                } else {
//                    throw new Exception("发送异常，数据转换后的格式无法发送成功。数据格式为：" + httpDataPacket.getDataFormat().toString());
//                }
//                httpURLConn.setConnectTimeout(50000);
//                httpURLConn.setReadTimeout(50000);
//                out.write(data);
//                out.flush();
//                out.close();
//                byte[] diff = new byte[httpURLConn.getInputStream().available()];
//                httpURLConn.getInputStream().read(diff, 0, diff.length);
//                String str = new String(diff, "UTF-8");
//                httpDataPacket.setResponseData(str);
//            } else {
//                throw new Exception("所指定的数据发送格式无法通过HTTP POST请求发送");
//            }
//        } else {
//            throw new Exception("无法找到指定的渠道号");
//        }
    }
}
