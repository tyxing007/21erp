package net.loyin.netService.netRuntime;


import net.loyin.netService.dataPacket.DataPacket;

/**
 * <pre>消息接收接口</pre>
 * <pre>所属模块：系统通讯</pre>
 *
 * @author 吴为超
 * @version 1.0 创建于 2014/8/1
 */
public interface IMessageReceive {
    /**
     * 消息接收
     *
     * @param dataPacket
     */
    public void Receive(DataPacket dataPacket);
}