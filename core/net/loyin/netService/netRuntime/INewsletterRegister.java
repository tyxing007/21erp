package net.loyin.netService.netRuntime;


import net.loyin.netService.dataPacket.DataPacket;

/**
 * <pre>消息处理核心接口</pre>
 * <pre>所属模块：系统通讯</pre>
 *
 * @author 张维
 * @version 1.0 创建于 2014/8/1
 */
public interface INewsletterRegister {
    /**
     * 通讯信息注册
     *
     * @param dataPacket
     */
    public void Register(DataPacket dataPacket);

    /**
     * 网络通讯消息发送
     *
     * @param dataPacket
     */
    public void SendBySync(DataPacket dataPacket);

    /**
     * 网络通讯消息发送
     *
     * @param dataPacket
     */
    public void Send(DataPacket dataPacket) throws InterruptedException;

    /**
     * 网络通讯消息发送
     *
     * @param dataPacket
     */
    public void Send(DataPacket dataPacket, Thread joinThread) throws InterruptedException;

    /**
     * 网络通讯消息接收
     *
     * @param dataPacket
     */
    public void ReceiveBySync(DataPacket dataPacket);

    /**
     * 网络通讯消息接收
     *
     * @param dataPacket
     */
    public void Receive(DataPacket dataPacket) throws InterruptedException;

    /**
     * 网络通讯消息接收
     *
     * @param dataPacket
     */
    public void Receive(DataPacket dataPacket, Thread joinThread) throws InterruptedException;

    /**
     * 通讯信息回发注册
     *
     * @param dataPacket
     */
    public String RegisterRespond(DataPacket dataPacket);

    /**
     * 注册通讯信息回发
     *
     * @param dataPacket
     */
    public void RespondRegister(String key, DataPacket dataPacket) throws Exception;
}
