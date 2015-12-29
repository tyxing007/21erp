package net.loyin.netService.netRuntime.impl;

import net.loyin.netService.dataPacket.DataPacket;
import net.loyin.netService.netRuntime.IMessageSended;

/**
 * <pre>消息发送处理线程</pre>
 * <pre>所属模块：系统通讯</pre>
 *
 * @author 张维
 * @version 1.0 创建于 2014/8/1
 */
public class SendedRunnable implements Runnable {

    protected IMessageSended messageSended;

    private DataPacket dataPacket;

    public SendedRunnable(DataPacket dataPacket)
    {
        this.dataPacket = dataPacket;
    }

    @Override
    public void run()
    {
        if(dataPacket!=null) {
            messageSended.Sended(dataPacket);
        }
    }
}
