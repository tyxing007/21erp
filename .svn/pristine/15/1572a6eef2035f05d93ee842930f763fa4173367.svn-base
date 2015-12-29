package net.loyin.netService.netRuntime.MessageReceived.Impl;

import net.loyin.netService.dataPacket.DataPacket;
import net.loyin.netService.dataPacket.HttpDataPacket;
import net.loyin.netService.dataPacket.NativeHttpDataPacket;
import net.loyin.netService.domain.DataFormats;
import net.loyin.netService.domain.NetTypes;
import net.loyin.netService.domain.SelectedKeys;
import net.loyin.netService.model.MsgStorageModel;
import net.loyin.netService.response.INativeResponseBack;
import net.loyin.netService.response.Impl.NativeHttpResponseImpl;

/**
 * <pre>HTTP消息接收</pre>
 * <pre>所属模块：系统通讯</pre>
 *
 * @author 张维
 * @version 1.0 创建于 2014/8/1
 */
public class HttpMessageReadImpl extends MessageReadImpl {


    INativeResponseBack NativeHttpResponseBack = new NativeHttpResponseImpl();

    /**
     * HTTP消息解析
     *
     * @param dataPacket
     * @return
     */
    public void PacketsParse(DataPacket dataPacket) throws Exception {
        DataPacket tempDataPacket = new DataPacket();
        tempDataPacket.setRawdata(dataPacket.getRawdata());
        tempDataPacket.setRawDataFormat(dataPacket.getRawDataFormat());
        tempDataPacket.setDataFormat(DataFormats.BYTE);

        byte[] data = (byte[]) DataSerialization.Serialization(tempDataPacket);

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
        if (!dataPacket.getRawDataFormat().equals(dataPacket.getDataFormat())) {
            tempDataPacket.setRawdata(dataPacket.getRawdata());
            tempDataPacket.setRawDataFormat(dataPacket.getRawDataFormat());
            tempDataPacket.setDataFormat(dataPacket.getDataFormat());
            tempDataPacket.setHead(dataPacket.getHead());
            dataPacket.setParsedata(DataSerialization.Deserialization(tempDataPacket));
        } else {
            dataPacket.setParsedata(dataPacket.getRawdata());
        }
    }

    /**
     * 后续执行
     *
     * @param dataPacket
     * @param runDataPacket
     */
    public void Follow(DataPacket dataPacket, DataPacket runDataPacket) throws Exception {
        if (runDataPacket == null) {
            log.info("无后续执行数据包");
            return;
        }
        if (runDataPacket instanceof HttpDataPacket) {
            HttpDataPacket rawHttpDataPacket = (HttpDataPacket) dataPacket;
            HttpDataPacket httpDataPacket = (HttpDataPacket) runDataPacket;
            if (httpDataPacket.getNetType() == null) {
                httpDataPacket.setNetType(rawHttpDataPacket.getNetType());
            }
            if (httpDataPacket.getNetType() != NetTypes.HTTP) {
                rawHttpDataPacket.getHttpExchange().close();
            }
            httpDataPacket.setHttpExchange(rawHttpDataPacket.getHttpExchange());
            httpDataPacket.setSectionId(rawHttpDataPacket.getSectionId());
            httpDataPacket.setSelectedKey(SelectedKeys.WRITE);
            NewsletterRegister.Register(httpDataPacket);
        }
        if (runDataPacket instanceof NativeHttpDataPacket) {
            NativeHttpDataPacket rawHttpDataPacket = (NativeHttpDataPacket) dataPacket;
            NativeHttpDataPacket httpDataPacket = (NativeHttpDataPacket) runDataPacket;

            if (httpDataPacket.getNetType() == null) {
                httpDataPacket.setNetType(rawHttpDataPacket.getNetType());
            }
            httpDataPacket.setRequest(rawHttpDataPacket.getRequest());
            httpDataPacket.setResponse(rawHttpDataPacket.getResponse());
            httpDataPacket.setSectionId(rawHttpDataPacket.getSectionId());
            httpDataPacket.setSelectedKey(SelectedKeys.WRITE);
            NewsletterRegister.SendBySync(httpDataPacket);
        }
    }

}
