package net.loyin.netService.netRuntime.impl;

import net.loyin.netService.dataPacket.DataPacket;
import net.loyin.netService.domain.NetTypes;
import net.loyin.netService.model.AuditExceptionModel;
import net.loyin.netService.netRuntime.IMessageReceive;
import net.loyin.netService.netRuntime.MessageReceived.IMessageRead;
import net.loyin.netService.netRuntime.MessageReceived.Impl.HttpMessageReadImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <pre>消息接收核心类</pre>
 * <pre>所属模块：系统通讯</pre>
 *
 * @author 张维
 * @version 1.0 创建于 2014/8/1
 */
public class MessageReceiveImpl implements IMessageReceive {



    IMessageRead HttpMessageRead = new HttpMessageReadImpl();

    protected static Log log = LogFactory.getLog(MessageReceiveImpl.class);

    /**
     * 消息接收
     *
     * @param dataPacket
     */
    public void Receive(DataPacket dataPacket) {
        try {
            if(dataPacket.getNetType() == NetTypes.HTTP) {
                HttpMessageRead.PacketsReceive(dataPacket);
            }
        } catch (Exception ex) {
            log.error("消息接收模块发生异常，消息类型：" + dataPacket.getNetType().toString() + "；通讯渠道号：" + dataPacket.getChannelNo(), ex);
            AuditExceptionModel.dao.insertErrorLog(dataPacket, ex);
            ex.printStackTrace();
        }
    }
}