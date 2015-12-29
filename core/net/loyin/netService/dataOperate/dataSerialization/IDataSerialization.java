package net.loyin.netService.dataOperate.dataSerialization;


import net.loyin.netService.dataPacket.DataPacket;

/**
 * <pre>序列化操作对外接口</pre>
 * <pre>所属模块：系统通讯</pre>
 * @author 张维
 * @version 1.0 创建于 2014/8/1
 */
public interface IDataSerialization {
    /**
     * 序列化操作
     *
     * @param dataPacket
     * @return
     */
    public Object Serialization(DataPacket dataPacket) throws Exception;

    /**
     * 反序列化
     *
     * @param dataPacket
     * @return
     */
    public Object Deserialization(DataPacket dataPacket) throws Exception;
}
