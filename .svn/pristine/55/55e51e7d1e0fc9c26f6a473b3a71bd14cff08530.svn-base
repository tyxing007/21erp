package net.loyin.netService.netRuntime.impl;

import net.loyin.netService.dataOperate.dataSerialization.IDataSerialization;
import net.loyin.netService.dataOperate.dataSerialization.Impl.DataSerializationImpl;
import net.loyin.netService.dataPacket.DataPacket;
import net.loyin.netService.domain.NetTypes;
import net.loyin.netService.model.AuditExceptionModel;
import net.loyin.netService.netRuntime.IMessageSended;
import net.loyin.netService.netRuntime.MessageSend.IMessageWrite;
import net.loyin.netService.netRuntime.MessageSend.Impl.HttpMessageWriteImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

/**
 * <pre>消息发送核心类</pre>
 * <pre>所属模块：系统通讯</pre>
 *
 * @author 张维
 * @version 1.0 创建于 2014/8/1
 */
public class MessageSendedImpl implements IMessageSended {
    IMessageWrite HttpMessageWrite = new HttpMessageWriteImpl();
    IDataSerialization DataSerialization = new DataSerializationImpl();
    public static Logger log = Logger.getLogger(MessageReceiveImpl.class);

    /**
     * 数据发送
     *
     * @param dataPacket
     */
    public void Sended(DataPacket dataPacket) {
        try {
            this.DataParse(dataPacket);
            IMessageWrite messageWrite = null;
//            if (dataPacket.getNetType() == NetTypes.IBMMQ) {
//                messageWrite = IbmMqMessageWrite;
//            } else
            if (dataPacket.getNetType() == NetTypes.HTTP) {
                messageWrite = HttpMessageWrite;
            }
            if (messageWrite != null) {
                messageWrite.MessageSend(dataPacket);
            }
        } catch (Exception ex) {
            log.error("消息接发送块发生异常，消息类型：" + dataPacket.getNetType().toString() + "；通讯渠道号：" + dataPacket.getChannelNo(), ex);
            AuditExceptionModel.dao.insertErrorLog(dataPacket,ex);

        }
    }

    /**
     * 数据转换
     *
     * @param dataPacket
     */
    protected void DataParse(DataPacket dataPacket) throws Exception {
        if (dataPacket.getRawDataFormat() != dataPacket.getDataFormat()) {
            dataPacket.setParsedata(DataSerialization.Serialization(dataPacket));
            //ResolveDesc rs = NetCache.GetResolveDescByValue(dataPacket.getRawdata().getClass().toString());
        } else {
            dataPacket.setParsedata(dataPacket.getRawdata());
        }
    }
}
