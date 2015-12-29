package net.loyin.netService.netRuntime.MessageSend.Impl;


import net.loyin.netService.dataOperate.dataSerialization.IDataSerialization;
import net.loyin.netService.dataOperate.dataSerialization.Impl.DataSerializationImpl;
import net.loyin.netService.dataPacket.DataPacket;
import net.loyin.netService.netRuntime.INetRuntimeHelper;
import net.loyin.netService.netRuntime.MessageSend.IMessageWrite;
import net.loyin.netService.netRuntime.impl.NetRuntimeHelperImpl;

/**
 * <pre>消息发送核心抽象</pre>
 * <pre>所属模块：系统通讯</pre>
 *
 * @author 张维
 * @version 1.0 创建于 2014/8/1
 */
public abstract class MessageWriteImpl implements IMessageWrite {


    IDataSerialization DataSerialization = new DataSerializationImpl();


    INetRuntimeHelper NetRuntimeHelper = new NetRuntimeHelperImpl();
//
    /**
     * 消息发送
     *
     * @param dataPacket
     */
    public void MessageSend(DataPacket dataPacket) throws Exception {
        if (dataPacket.getSectionId() == null || dataPacket.getSectionId().length() == 0) {
            dataPacket.setSectionId(NetRuntimeHelper.getNetSectionId());
        }
        if (this.DataFormatVerify(dataPacket)) {
            this.Send(dataPacket);
        } else {
            throw new Exception("数据通道格式校验失败");
        }
    }

    /**
     * 数据发送
     *
     * @param dataPacket
     */
    protected abstract void Send(DataPacket dataPacket) throws Exception;

    /**
     * 数据发送格式校验
     *
     * @param dataPacket
     * @return
     */
    protected abstract boolean DataFormatVerify(DataPacket dataPacket);
}
