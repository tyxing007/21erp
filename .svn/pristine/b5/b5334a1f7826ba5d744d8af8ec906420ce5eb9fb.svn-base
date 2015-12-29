package net.loyin.netService.dataOperate.dataSerialization.Impl;


import com.sun.javaws.jnl.XMLUtils;
import net.loyin.netService.NetCache;
import net.loyin.netService.dataOperate.dataSerialization.ISerialization;
import net.loyin.netService.dataPacket.DataPacket;
import net.loyin.netService.domain.DataFormats;
import net.loyin.util.XmlUntil;

import java.io.UnsupportedEncodingException;

/**
 * <pre>BYTE序列化和反序列化</pre>
 * <pre>所属模块：系统通讯</pre>
 *
 * @author 张维
 * @version 1.0 创建于 2014/8/1
 */

public class BytesSerializationImpl implements ISerialization {
    /**
     * 序列化操作
     *
     * @param dataPacket
     * @return
     */
    public Object Serialization(DataPacket dataPacket) throws Exception {
        if (DataFormats.BYTE == dataPacket.getDataFormat() && dataPacket.getRawDataFormat().equals(DataFormats.XML)) {
            return StringToBytes(dataPacket.getRawdata().toString());
        } else {
            throw new Exception("未知格式转换");
        }
    }

    /**
     * 反序列化操作
     *
     * @param dataPacket
     * @return
     */
    public Object Deserialization(DataPacket dataPacket) throws Exception {
        if (dataPacket.getDataFormat().equals(DataFormats.XML)) {
            return BytesToString((byte[]) dataPacket.getRawdata());
        } else if (dataPacket.getDataFormat().equals(DataFormats.OBJECT)) {
            String xml = BytesToString((byte[]) dataPacket.getRawdata());
            if (dataPacket.getHead() == null || dataPacket.getHead().length() == 0) {
                throw new Exception("反序列化失败，无法获取正确的报文头");
            }
            Class onwClass = Class.forName(NetCache.GetResolveDesc(dataPacket.getHead()).getVoClass());
            return XmlUntil.XMLStringToBean(onwClass, xml);
        } else {
            throw new Exception("未知的数据反序列化");
        }
    }

    /**
     * 解析成String
     *
     * @param bytes
     * @return
     */
    private String BytesToString(byte[] bytes) throws UnsupportedEncodingException {
        return new String(bytes, "UTF-8");
    }

    /**
     * String to Bytes
     *
     * @param str
     * @return
     */
    private byte[] StringToBytes(String str) throws UnsupportedEncodingException {
        return str.getBytes("UTF-8");
    }
}
