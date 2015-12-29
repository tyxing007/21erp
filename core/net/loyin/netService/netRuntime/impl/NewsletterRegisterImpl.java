package net.loyin.netService.netRuntime.impl;

import net.loyin.netService.dataPacket.DataPacket;
import net.loyin.netService.dataPacket.SyncDataPacket;
import net.loyin.netService.domain.RegistWay;
import net.loyin.netService.domain.SelectedKeys;
import net.loyin.netService.netPool.NetConnPool;
import net.loyin.netService.netRuntime.IMessageReceive;
import net.loyin.netService.netRuntime.IMessageSended;
import net.loyin.netService.netRuntime.INewsletterRegister;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * <pre>通讯数据处理核心类</pre>
 * <pre>所属模块：系统通讯</pre>
 *
 * @author 张维
 * @version 1.0 创建于 2014/8/1
 */
public class NewsletterRegisterImpl extends Thread implements INewsletterRegister {

    protected static List netpool = new LinkedList(); // 通讯注册池

    IMessageSended MessageSended = new MessageSendedImpl();

    IMessageReceive MessageReceive = new MessageReceiveImpl();

    protected static Log log = LogFactory.getLog(NewsletterRegisterImpl.class);

    public void run() {
        System.out.print("成功开启通讯消息注册\n");
        while (true) {
            try {
                DataPacket dataPacket = null;
                synchronized (netpool) {
                    if (netpool.isEmpty()) {
                        netpool.wait();
                    }
                    dataPacket = (DataPacket) netpool.remove(0);
                }
                if (dataPacket.getSelectedKey().equals(SelectedKeys.READ)) {
                    Receive(dataPacket);
                } else {
                    Send(dataPacket);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("接收到消息注册时发生异常", ex);
            }
        }
    }

    /**
     * 网络通讯消息发送
     *
     * @param dataPacket
     */
    public void SendBySync(DataPacket dataPacket) {
        MessageSended.Sended(dataPacket);
    }

    /**
     * 网络通讯消息发送
     *
     * @param dataPacket
     */
    public void Send(DataPacket dataPacket) throws InterruptedException {
        Send(dataPacket, null);
    }

    /**
     * 网络通讯消息发送
     *
     * @param dataPacket
     */
    public void Send(DataPacket dataPacket, Thread joinThread) throws InterruptedException {
        SendedRunnable sendedThread = new SendedRunnable(dataPacket);
        sendedThread.messageSended = MessageSended;
        Thread pthread = null;
        if (joinThread != null) {
            pthread = new Thread(joinThread.getThreadGroup(), sendedThread);
        } else {
            pthread = new Thread(sendedThread);
        }
        pthread.start();
        if (joinThread != null) {
            dataPacket.setJoinThread(pthread);
            joinThread.join();
        }
    }

    /**
     * 网络通讯消息接收
     *
     * @param dataPacket
     */
    public void ReceiveBySync(DataPacket dataPacket) {
        MessageReceive.Receive(dataPacket);
    }

    /**
     * 网络通讯消息接收
     *
     * @param dataPacket
     */
    public void Receive(DataPacket dataPacket) throws InterruptedException {
        Receive(dataPacket, null);
    }

    /**
     * 网络通讯消息接收
     * @param dataPacket
     */
    public void Receive(DataPacket dataPacket, Thread joinThread) throws InterruptedException {
        ReceiveRunnable receiveThread = new ReceiveRunnable(dataPacket);
        receiveThread.messageReceive = MessageReceive;
        Thread pthread = new Thread(receiveThread);
        pthread.start();
        if (joinThread != null) {
            dataPacket.setJoinThread(joinThread);
            pthread.join();
        }
    }

    /**
     * 通讯信息注册
     *
     * @param dataPacket
     */
    public void Register(DataPacket dataPacket) {
        if (dataPacket.getRegistWay().equals(RegistWay.Regist)) {
            synchronized (netpool) {
                netpool.add(netpool.size(), dataPacket);
                netpool.notifyAll();
            }
        } else if (dataPacket.getRegistWay().equals(RegistWay.syncRun)) {
            if (dataPacket.getSelectedKey().equals(SelectedKeys.READ)) {
                ReceiveBySync(dataPacket);
            } else {
                SendBySync(dataPacket);
            }
        }
    }

    /**
     * 通讯信息回发注册
     *
     * @param dataPacket
     */
    public String RegisterRespond(DataPacket dataPacket) {
        if (dataPacket instanceof SyncDataPacket) {
            return NetConnPool.add(dataPacket);
        }
        return null;
    }

    /**
     * 注册通讯信息回发
     *
     * @param dataPacket
     */
    public void RespondRegister(String key, DataPacket dataPacket) throws Exception {
        Object obj = NetConnPool.get(key);
        if (obj != null) {
            DataPacket rawDataPacket = (DataPacket) obj;
            if (dataPacket.getClass().equals(rawDataPacket.getClass())) {
                /** 数据封装 **/
                DataPacket newDataPacket = rawDataPacket.GetBackBaseDataPacket();
                newDataPacket.setRawdata(dataPacket.getRawdata());
                newDataPacket.setRawDataFormat(dataPacket.getRawDataFormat());
                newDataPacket.setDataFormat(dataPacket.getDataFormat());

                Register(newDataPacket);
                NetConnPool.remove(key);
            } else {
                throw new Exception("回发数据包与原始数据包类型不匹配");
            }
        } else {
            throw new Exception("无法通过指定key获取到原始数据包");
        }
    }
}
