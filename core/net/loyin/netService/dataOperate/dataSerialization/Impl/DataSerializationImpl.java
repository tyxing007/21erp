package net.loyin.netService.dataOperate.dataSerialization.Impl;


import net.loyin.netService.dataOperate.dataSerialization.IDataSerialization;
import net.loyin.netService.dataOperate.dataSerialization.ISerialization;
import net.loyin.netService.dataPacket.DataPacket;
import net.loyin.netService.domain.DataFormats;

/**
 * <pre>数据序列化和反序列化对核心操作</pre>
 * <pre>所属模块：系统通讯</pre>
 *
 * @author 张维
 * @version 1.0 创建于 2014/8/1
 */
public class DataSerializationImpl implements IDataSerialization {

    ISerialization BytesSerialization = (ISerialization)new BytesSerializationImpl();

    ISerialization TextSerialization = (ISerialization) new TextSerializationImpl();

    ISerialization ObjectSerialization = (ISerialization)new ObjectSerializationImpl();

    ISerialization JsonSerialization = (ISerialization)new JsonSerializationImpl();

    /**
     * 序列化操作
     *
     * @param dataPacket
     * @return
     */
    public Object Serialization(DataPacket dataPacket) throws Exception {
        DataFormats fromDataFormats = dataPacket.getRawDataFormat();
        DataFormats toDataFormats = dataPacket.getDataFormat();
        Object obj = dataPacket.getRawdata();
        if (!fromDataFormats.equals(toDataFormats)) {
            if (dataPacket.getRawDataFormat().equals(DataFormats.BYTE)) {
                obj = BytesSerialization.Serialization(dataPacket);
            } else if (dataPacket.getRawDataFormat().equals(DataFormats.XML)) {
                obj = TextSerialization.Serialization(dataPacket);
            } else if (dataPacket.getRawDataFormat().equals(DataFormats.OBJECT)) {
                obj = ObjectSerialization.Serialization(dataPacket);
            } else if (dataPacket.getRawDataFormat().equals(DataFormats.JSON)) {
                obj = JsonSerialization.Serialization(dataPacket);
            } else {
                throw new Exception("未知格式的序列化");
            }
        }
        return obj;
    }

    /**
     * 反序列化
     *
     * @param dataPacket
     * @return
     */

    public Object Deserialization(DataPacket dataPacket) throws Exception {
        DataFormats fromDataFormats = dataPacket.getRawDataFormat();
        DataFormats toDataFormats = dataPacket.getDataFormat();
        Object obj = dataPacket.getRawdata();
        if (!fromDataFormats.equals(toDataFormats)) {
            if (dataPacket.getRawDataFormat().equals(DataFormats.XML)) {
                obj = TextSerialization.Deserialization(dataPacket);
            } else if (dataPacket.getRawDataFormat().equals(DataFormats.BYTE)) {
                obj = BytesSerialization.Deserialization(dataPacket);
            } else if (dataPacket.getRawDataFormat().equals(DataFormats.JSON)) {
                obj = JsonSerialization.Deserialization(dataPacket);
            } else {
                throw new Exception("未知格式的反序列化");
            }
        }
        return obj;
    }
}
