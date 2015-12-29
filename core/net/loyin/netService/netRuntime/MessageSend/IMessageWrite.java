package net.loyin.netService.netRuntime.MessageSend;


import net.loyin.netService.dataPacket.DataPacket;

/**
 * <pre>消息发送接口</pre>
 * <pre>所属模块：系统通讯</pre>
 *
 * @author 张维
 * @version 1.0 创建于 2014/8/1
 */
public interface IMessageWrite {
    /**
     * 消息发送
     * @param dataPacket
     */
    public void MessageSend(DataPacket dataPacket) throws Exception;
}
