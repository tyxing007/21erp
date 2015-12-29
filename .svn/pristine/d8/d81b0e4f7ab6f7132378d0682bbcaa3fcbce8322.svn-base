package net.loyin.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created by Chao on 2015/11/12.
 */
public class XmlUntil {

    /**
     * 将xml字符串转化为java bean
     *
     * @param objclass
     * @param xml
     * @param <T>
     * @return
     * @throws JAXBException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T> T XMLStringToBean(Class<T> objclass, String xml) throws JAXBException, IllegalAccessException, InstantiationException {
        T object = null;
        JAXBContext jaxbContext = JAXBContext.newInstance(objclass);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        xml=xml.trim();
        StringReader reader = new StringReader(xml);
        object = (T) unmarshaller.unmarshal(reader);
        return object;
    }

    /**
     * 将javabean转化为xml字符串
     *
     * @param obj
     * @param encoding
     * @param <T>
     * @return
     * @throws JAXBException
     */
    public static <T> String BeanToXMLString(T obj, String encoding) throws JAXBException {
        String result = null;
        JAXBContext context = JAXBContext.newInstance(obj.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);//决定是否在转换成xml时同时进行格式化（即按标签自动换行，否则即是一行的xml）
        marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);//xml的编码方式
        StringWriter writer = new StringWriter();
        marshaller.marshal(obj, writer);
        result = writer.toString();
        return result;
    }
}
