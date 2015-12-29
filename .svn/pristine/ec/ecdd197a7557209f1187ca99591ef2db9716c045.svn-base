package net.loyin.netService;

import net.loyin.netService.listener.IStartListener;
import net.loyin.netService.listener.Impl.StartListenerImpl;
import net.loyin.netService.model.BusinessModel;
import net.loyin.netService.netRuntime.INewsletterRegister;
import net.loyin.netService.netRuntime.impl.NewsletterRegisterImpl;
import org.apache.log4j.Logger;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chao on 2015/11/10.
 */
public class NetServiceInit {
    public static Logger log = Logger.getLogger(NetServiceInit.class);


    public void Init() {
        try {
            this.InitBusinessProcess();
            System.out.print("成功加载业务处理信息\n");
            log.info("成功加载业务处理信息\n");

            this.InitResolve();
            System.out.print("成功加载报文映射信息\n");
            log.info("成功加载报文映射信息\n");

            INewsletterRegister NewsletterRegister = new NewsletterRegisterImpl();
            ((Thread) NewsletterRegister).start();
//                    .start();

            IStartListener startListener = new StartListenerImpl();
            startListener.run();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 初始化业务处理
     */
    private void InitBusinessProcess() {

        List<BusinessModel> modelList = BusinessModel.dao.queryBusinessAll();
        for (BusinessModel b : modelList) {
            BusinessProcess businessProcess = new BusinessProcess();
            businessProcess.setPacketsVersion(b.getStr("version"));
            businessProcess.setProcessClass(b.getStr("key_name"));
            businessProcess.setProcessCode(b.getStr("tran_code"));
            businessProcess.setProcessId(b.getStr("business_id"));
            NetCache.AddBusinessProcess(b.getStr("business_id"), businessProcess);
        }
    }

    /**
     * 加载报文映射信息
     *
     * @throws Exception
     */
    private void InitResolve() throws Exception {
//        DocumentBuilder builder = builderFactory.newDocumentBuilder();
//        Document doc = builder.parse(this.getClass().getResource("/VoMapping.xml").getFile());
        ResolveDesc rd;
        Map<String,String> xmlMap = null;
        String head = null;
        String map = null;
        List<Map<String,String>> xmlList = this.readXMLByDom(this.getClass().getResource("/VoMapping.xml").getPath());
        for(Map<String,String> m : xmlList){
            head = m.get("head");
            map = m.get("map");
            rd = new ResolveDesc();
            rd.setHeader(head);
            rd.setVoClass(map);
            NetCache.AddResolveDesc(head, rd);
        }
    }

    public List<Map<String,String>> readXMLByDom(String xmlPath){
        List<Map<String,String>> result = new ArrayList<>();
        Map<String,String> resultMap = null;
        Document doc = init(xmlPath);
        NodeList nodeList = doc.getElementsByTagName("VoMappings");
        Node fatherNode = nodeList.item(0);
        NodeList childNodes = fatherNode.getChildNodes();
        int childLength = childNodes.getLength();
        int grandsonLength = 0;
        for(int i=0;i<childLength;i++){
            Node grandsonNode = childNodes.item(i);
            if(grandsonNode.getNodeName().equals("Vo")){
                NodeList lastNodeList = grandsonNode.getChildNodes();
                grandsonLength = lastNodeList.getLength();
                resultMap = new HashMap<String,String>();
                for(int m=0;m<grandsonLength;m++){
                    if(lastNodeList.item(m).getNodeName().equals("head")){
                        resultMap.put("head",lastNodeList.item(m).getFirstChild().getNodeValue());
                    }else if(lastNodeList.item(m).getNodeName().equals("map")){
                        resultMap.put("map",lastNodeList.item(m).getFirstChild().getNodeValue());
                    }
                }
                result.add(resultMap);
            }
        }
        return result;

//        //get attribute of father node
//        NamedNodeMap attributes = fatherNode.getAttributes();
//        NodeList childNodes = fatherNode.getChildNodes();
//        System.out.println(childNodes.getLength());
//        for (int j = 0; j < childNodes.getLength(); j++) {
//            Node childNode = childNodes.item(j);
//            // 如果这个节点属于Element ,再进行取值
//            if (childNode instanceof Element) {
//                // System.out.println("子节点名为:"+childNode.getNodeName()+"相对应的值为"+childNode.getFirstChild().getNodeValue());
//                System.out.println("子节点名为:" + childNode.getNodeName()
//                        + "相对应的值为" + childNode.getFirstChild().getNodeValue());
//
//
//            }
//        }
    }
    public Document init(String xmlPath){
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(xmlPath));
            return doc;
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
