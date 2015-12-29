package net.loyin.netService.dataOperate.dataSerialization.Impl;


import freemarker.template.ObjectWrapper;
import net.loyin.netService.dataOperate.dataSerialization.ISerialization;
import net.loyin.netService.dataPacket.DataPacket;
import net.loyin.netService.domain.DataFormats;
import net.loyin.util.XmlUntil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.ArrayList;

/**
 * <pre>VO对象的序列化&反序列化</pre>
 * <pre>所属模块：系统通讯</pre>
 *
 * @author 张维
 * @version 1.0 创建于 2014/8/4
 */
public class ObjectSerializationImpl implements ISerialization {
    /**
     * 序列化操作
     *
     * @param dataPacket
     * @return
     */
    public Object Serialization(DataPacket dataPacket) throws Exception {
        if (dataPacket.getDataFormat().equals(DataFormats.XML)) {
            return XmlUntil.BeanToXMLString(dataPacket.getRawdata(), "UTF-8");
        } else if (dataPacket.getDataFormat().equals(DataFormats.BYTE)) {
            String xml = XmlUntil.BeanToXMLString(dataPacket.getRawdata(), "UTF-8");
            return xml.getBytes("UTF-8");
        } else if (dataPacket.getDataFormat().equals(DataFormats.JSON)) {
//            String json = encodeObject2Json(dataPacket.getRawdata());
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(dataPacket.getRawdata());
            return json;
        } else {
            throw new Exception("未知的数据序列化异常");
        }
    }

    /**
     * 将不含日期时间格式的Java对象系列化为Json资料格式
     *
     * @param pObject 传入的Java对象
     * @return
     */
    public String encodeObject2Json(Object pObject) {
        String jsonString = "";
            if (pObject instanceof ArrayList) {
                JSONArray jsonArray = JSONArray.fromObject(pObject);
                jsonString = jsonArray.toString();
            } else {
                JSONObject jsonObject = JSONObject.fromObject(pObject);
                jsonString = jsonObject.toString();
            }
        return jsonString;
    }


    /**
     * 反序列化操作
     *
     * @param dataPacket
     * @return
     */
    public Object Deserialization(DataPacket dataPacket) throws Exception {
        throw new Exception("未知的数据序列化异常");
    }
}