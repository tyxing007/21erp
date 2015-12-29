package net.loyin.netService.dataOperate.dataSerialization.Impl;


import net.loyin.netService.NetCache;
import net.loyin.netService.dataOperate.dataSerialization.ISerialization;
import net.loyin.netService.dataPacket.DataPacket;
import net.loyin.netService.domain.DataFormats;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * <pre>JSON串的序列化和反序列化</pre>
 * <pre>所属模块：系统通讯</pre>
 *
 * @author 张维
 * @version 1.0 创建于 2014/8/5
 */

public class JsonSerializationImpl implements ISerialization {
    /**
     * 序列化操作
     *
     * @param dataPacket
     * @return
     */
    public Object Serialization(DataPacket dataPacket) throws Exception {
        if (dataPacket.getDataFormat().equals(DataFormats.BYTE)) {
            return dataPacket.getRawdata().toString().getBytes("UTF-8");
        } else {
            throw new Exception("未知格式的序列化");
        }
    }

    /**
     * 反序列化操作
     *
     * @param dataPacket
     * @return
     */
    public Object Deserialization(DataPacket dataPacket) throws Exception {
        if (dataPacket.getDataFormat().equals(DataFormats.OBJECT)) {
            if (dataPacket.getHead() == null || dataPacket.getHead().length() == 0) {
                throw new Exception("反序列化失败，无法获取正确的报文头");
            }
            Class onwClass = Class.forName(NetCache.GetResolveDesc(dataPacket.getHead()).getVoClass());
            JSONObject jsObj = JSONObject.fromObject(dataPacket.getRawdata().toString());
            return JSONObject.toBean(jsObj, onwClass);
//            return JsonHelper.parseJson2Object(dataPacket.getRawdata().toString(), onwClass);
        } else {
            throw new Exception("未知格式的序列化");
        }
    }
}
