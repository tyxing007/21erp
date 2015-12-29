package net.loyin.netService.netRuntime.impl;


import net.loyin.netService.dataPacket.DataPacket;
import net.loyin.netService.netRuntime.IMessageReceive;

/**
 * <pre>消息接收处理线程</pre>
 * <pre>所属模块：系统通讯</pre>
 *
 * @author 张维
 * @version 1.0 创建于 2014/8/1
 */
public class ReceiveRunnable implements Runnable {

    public IMessageReceive messageReceive = new MessageReceiveImpl();

    private DataPacket dataPacket;

    public ReceiveRunnable(DataPacket dataPacket)
    {
        this.dataPacket = dataPacket;
    }

    @Override
    public void run()
    {
        messageReceive.Receive(dataPacket);
    }
}
