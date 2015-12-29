package net.loyin.netService.netRuntime.MessageSend.Impl;

import net.loyin.netService.dataPacket.DataPacket;
import net.loyin.netService.dataPacket.HttpDataPacket;
import net.loyin.netService.dataPacket.NativeHttpDataPacket;
import net.loyin.netService.domain.DataFormats;
import net.loyin.netService.domain.HttpRequestType;
import net.loyin.netService.model.MsgStorageModel;
import net.loyin.netService.request.IRequest;
import net.loyin.netService.request.Impl.PostRequestImpl;
import net.loyin.netService.response.INativeResponseBack;
import net.loyin.netService.response.IResponseBack;
import net.loyin.netService.response.Impl.HttpResponseImpl;
import net.loyin.netService.response.Impl.NativeHttpResponseImpl;

/**
 * <pre>HTTP消息发送</pre>
 * <pre>所属模块：系统通讯</pre>
 *
 * @author 张维
 * @version 1.0 创建于 2014/8/1
 */
public class HttpMessageWriteImpl extends MessageWriteImpl {

    IRequest HttpPostRequest = new PostRequestImpl();

    IResponseBack HttpResponseBack = new HttpResponseImpl();

    INativeResponseBack NativeHttpResponseBack = new NativeHttpResponseImpl();

    /**
     * 数据发送
     *
     * @param dataPacket
     */
    protected void Send(DataPacket dataPacket) throws Exception {

        DataPacket tempDataPacket = new DataPacket();
        tempDataPacket.setRawdata(dataPacket.getParsedata());
        tempDataPacket.setRawDataFormat(dataPacket.getDataFormat());
        tempDataPacket.setDataFormat(DataFormats.BYTE);

        byte[] data = (byte[]) DataSerialization.Serialization(tempDataPacket);
        dataPacket.setRawdata(data);
        dataPacket.setRawDataFormat(DataFormats.BYTE);

        MsgStorageModel msgStorageModel = new MsgStorageModel();
        msgStorageModel.put("head_no", dataPacket.getHead());
        msgStorageModel.put("section_no", dataPacket.getSectionId());
        msgStorageModel.put("path", dataPacket.getSelectedKey().toString());
        msgStorageModel.put("net_type", dataPacket.getNetType().toString());
        msgStorageModel.put("channel_no", dataPacket.getChannelNo());
        msgStorageModel.put("msg", data);
        msgStorageModel.put("data_format", dataPacket.getRawDataFormat().toString());
        msgStorageModel.save();
        String msgId = msgStorageModel.getStr("id");

        dataPacket.setMessageNo(msgId);

        if (dataPacket instanceof HttpDataPacket) {
            HttpDataPacket httpDataPacket = (HttpDataPacket) dataPacket;
            if (httpDataPacket.getHttpExchange() != null) {
                HttpResponseBack.responseBack(httpDataPacket);
            } else if (httpDataPacket.getHttpRequestType() == HttpRequestType.POST) {
                HttpPostRequest.request(httpDataPacket);
            } else {
                throw new Exception("无法无法正确的使用HTTP数据包");
            }
        } else if (dataPacket instanceof NativeHttpDataPacket) {
            NativeHttpDataPacket nativeHttpDataPacket = (NativeHttpDataPacket) dataPacket;
            if (nativeHttpDataPacket.getResponse() != null) {
                NativeHttpResponseBack.responseBack(nativeHttpDataPacket);
            } else {
                throw new Exception("无法使用原生HTTP请求包发起HTTP请求");
            }
        } else {
            throw new Exception("HTTP请求的数据包对象必须为：HttpDataPacket");
        }
    }

    /**
     * 数据发送格式校验
     *
     * @param dataPacket
     * @return
     */
    protected boolean DataFormatVerify(DataPacket dataPacket) {
        if (dataPacket.getDataFormat().equals(DataFormats.JSON) || dataPacket.getDataFormat().equals(DataFormats.BYTE) || dataPacket.getDataFormat().equals(DataFormats.XML)) {
            return true;
        }
        return false;
    }
}
