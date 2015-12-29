package net.loyin.netService.response.Impl;

import com.alibaba.fastjson.JSON;
import com.sun.deploy.net.HttpRequest;
import com.sun.net.httpserver.HttpExchange;
import net.loyin.jfinal.model.IdGenerater;
import net.loyin.model.sso.FileBean;
import net.loyin.netService.dataPacket.HttpDataPacket;
import net.loyin.netService.domain.*;
import net.loyin.netService.response.IResponse;
import net.loyin.netService.vo.fileVo.FileSaveMainData;
import net.loyin.netService.vo.fileVo.FileSaveVo;
import net.loyin.util.PropertiesContent;
import net.loyin.util.XmlUntil;
import net.loyin.util.safe.CipherUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>HTTP POST请求接收</pre>
 * <pre>所属模块：系统通讯</pre>
 *
 * @author 张维
 * @version 1.0 创建于 2014/8/1
 */
public class PostResponseImpl implements IResponse {


    //服务器根目录，post.html, upload.html都放在该位置
    private File filePath = null;
    //mltipart/form-data方式提交post的分隔符,
    private String boundary = null;
    //post提交请求的正文的长度
    private int contentLength = 0;


    /**
     * HTTP响应
     *
     * @param httpExchange
     */
    public HttpDataPacket response(HttpExchange httpExchange) throws Exception {
        HttpDataPacket httpDataPacket = new HttpDataPacket();
        httpDataPacket.setDataFormat(DataFormats.OBJECT);
        String lengthStr = httpExchange.getRequestHeaders().getFirst("Content-length");
        if (lengthStr == null || lengthStr.length() == 0) {
            throw new Exception("无法获取请求内容大小");
        }
        String cookie = httpExchange.getRequestHeaders().getFirst("Cookie");
        Map<String, String> userMap =  null;
        String lastCookie = parseCookie(cookie);
        userMap = getUserMap(lastCookie);
        httpDataPacket.setCookie(userMap);
        String type = httpExchange.getRequestHeaders().getFirst("Content-Type");
        if (type != null && type.indexOf("boundary=") != -1) {
            String[] query = httpExchange.getRequestURI().getQuery().split("&");
            String relationId = "";
            for(String s : query){
                if(s.indexOf("id")!=-1){
                    relationId = s.split("=")[1];
                }
            }
            FileOutputStream fos = null;
            this.contentLength = Integer.parseInt(lengthStr);
            this.boundary = type.substring(type.indexOf("boundary") + 9);
            System.out.println("begin get data......");
            DataInputStream reader = new DataInputStream((httpExchange.getRequestBody()));
            IdGenerater id = new IdGenerater();
            String seqId = String.valueOf(id.getIdSeq());
            this.doMultiPart(reader, fos, seqId);
            System.out.println("start save file to db .......");
            FileSaveVo fileSaveVo = doFileVo(lengthStr,relationId,seqId);
            System.out.println("end save file to db .......");
            httpDataPacket.setRawdata(XmlUntil.BeanToXMLString(fileSaveVo,"UTF-8"));
        }else{
            byte[] diff = new byte[Integer.parseInt(lengthStr)];
            InputStream inputStream = httpExchange.getRequestBody();
            int i = 0;
            int index = 0;
            while ((i = inputStream.read()) != -1) {
                diff[index] = (byte) i;
                index++;
            }
            httpDataPacket.setRawdata(new String(diff, "UTF-8"));
        }
        String head = httpExchange.getRequestHeaders().getFirst("messageHead");
        httpDataPacket.setHttpRequestType(HttpRequestType.POST);
        String dataFormat = httpExchange.getRequestHeaders().getFirst("dataFormat");
        if (dataFormat == null || dataFormat.length() == 0) {
            throw new Exception("无法获取请求消息格式");
        }
        httpDataPacket.setRawDataFormat(dataFormat.toUpperCase().equals("JSON") ? DataFormats.JSON : DataFormats.XML);
        httpDataPacket.setNetType(NetTypes.HTTP);
        httpDataPacket.setSelectedKey(SelectedKeys.READ);
        httpDataPacket.setHttpExchange(httpExchange);
        httpDataPacket.setIp(httpExchange.getRemoteAddress().getAddress().toString());
        httpDataPacket.setHead(head);
        httpDataPacket.setSectionId(httpExchange.getRequestHeaders().getFirst("markId"));
        return httpDataPacket;
    }

    private FileSaveVo doFileVo(String lengthStr,String relationId,String id){
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat s1 = new SimpleDateFormat("yyyyMMddHHmmss");
        FileSaveMainData mainData = new FileSaveMainData();
        mainData.setFileId(id);
        mainData.setFileName(filePath.getName());
        mainData.setFilePath(filePath.getAbsolutePath());
        mainData.setFsize(Integer.parseInt(lengthStr));
        mainData.setRelationId(relationId);
        mainData.setSavePath(filePath.getPath());
        FileSaveVo fileSaveVo = new FileSaveVo();
        fileSaveVo.setMaindata(mainData);
        CommonHeader messageHeader = new CommonHeader();
        messageHeader.setBusinessCode("FILE00001");
        messageHeader.setMessageNo(s1.format(new Date()));
        messageHeader.setAppNo("1.0");
        messageHeader.setDataNo("1.0");
        messageHeader.setRequestTime(s.format(new Date()));
        fileSaveVo.setMessageHeader(messageHeader);
        return fileSaveVo;
    }


    //处理附件
    private String  doMultiPart(DataInputStream reader, OutputStream out,String id) throws Exception {
        System.out.println("doMultiPart ......");
        if (this.contentLength != 0) {
            byte[] buf = new byte[this.contentLength];
            int totalRead = 0;
            int size = 0;
            while (totalRead < this.contentLength) {
                size = reader.read(buf, totalRead, this.contentLength - totalRead);
                totalRead += size;
            }
            OutputStream fileOut = null;
            //用buf构造一个字符串，可以用字符串方便的计算出附件所在的位置
            String dataString = new String(buf, 0, totalRead);
//            System.out.println("the data user posted:/n" + dataString);
            int pos = dataString.indexOf(boundary);
            //以下略过4行就是第一个附件的位置
            String enter = System.getProperty("line.separator");
            fileOut = initFileOut(dataString.substring(pos, dataString.indexOf(enter, pos)),id);
            pos = dataString.indexOf(enter, pos) + 2;
            fileOut = fileOut == null ? initFileOut(dataString.substring(pos, dataString.indexOf(enter, pos)),id) : fileOut;
            pos = dataString.indexOf(enter, pos) + 2;
            fileOut = fileOut == null ? initFileOut(dataString.substring(pos, dataString.indexOf(enter, pos)),id) : fileOut;
            pos = dataString.indexOf(enter, pos) + 2;
            fileOut = fileOut == null ? initFileOut(dataString.substring(pos, dataString.indexOf(enter, pos)),id) : fileOut;
            pos = dataString.indexOf(enter, pos) + 2;
//            fileOut = fileOut == null ? initFileOut(dataString.substring(pos, dataString.indexOf(enter, pos))) : fileOut;
            //附件开始位置
            int start = dataString.substring(0, pos).getBytes().length;
            pos = dataString.indexOf(boundary + "--", pos) - 4;
            //附件结束位置
            int end = dataString.substring(0, pos).getBytes().length;
//            OutputStream fileOut = new FileOutputStream("c://" + fileName);
            fileOut.write(buf, start, end - start);
            fileOut.close();
        }
        reader.close();
        return null;
    }

    private FileOutputStream initFileOut(String buffValue,String id) throws IOException {
        String filename = "";
        String prefix = "";
        FileOutputStream fos = null;
        buffValue = new String(buffValue.getBytes(),"UTF-8");
        int start = buffValue.indexOf("Content-Disposition:");
        if(start == -1 ){
            return fos;
        }
        int end = buffValue.indexOf(";");
        start = buffValue.indexOf("filename=\"", end + 2);
        end = buffValue.indexOf("\"", start + 10);
        if (start != -1 && end != -1) {
            filename = buffValue.substring(start + 10, end);
            int slash = Math.max(filename.lastIndexOf(47), filename.lastIndexOf(92));
            if (slash > -1) {
                filename = filename.substring(slash + 1);
            }
            filename = id+filename.substring(filename.indexOf("."),filename.length());
        }

        if (filename.toLowerCase().endsWith(".png") || filename.toLowerCase().equals(".jpg") || filename.toLowerCase().equals(".gif")) {
            prefix = "img";
          /*  fileName = generateWord() + extension;*/
        } else {
            prefix = "file";
        }

        String src = "";
        if (System.getenv("OS").toUpperCase().equals("WINDOWS_NT")) {
            src = "D:\\EYKJ\\IMAGE";
        } else {
            src = "/home/images/eykj";
        }
        File targetDir = new File(src + "/" + prefix + "/u/");
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        File target = new File(targetDir, filename);
        if (!target.exists()) {
            target.createNewFile();
        }
        this.filePath = target;
        fos = new FileOutputStream(target);
        return fos;
    }


    /**
     * 获取cookie
     *
     * @param cookie
     * @return
     */
    private String parseCookie(String cookie) {
        if(cookie == null ){
            return null;
        }
        try{
//            String[] cookies = cookie.split(";");
//            String[] lastCookie = null;
//            for (String s : cookies) {
//                lastCookie = cookie.split("=");
//                if (lastCookie.length == 2 && lastCookie[0].equals(PropertiesContent.get("cookie_field_key"))) {
//                    return lastCookie[1];
//                }
//            }
            return cookie;
        }catch (Exception e){
            return null;
        }
    }


    public Map<String, String> getUserMap(String cookie) {
        if(cookie == null){
            return null;
        }
        Map<String, String> userMap = new HashMap<String, String>();
        if (StringUtils.isNotEmpty(cookie)) {
            cookie = CipherUtil.decryptData(cookie);
            userMap = (Map<String, String>) JSON.parse(cookie);
        }
        return userMap;
    }

    private String extractBoundary(String line) {
        int index = line.lastIndexOf("boundary=");
        if (index == -1) {
            return null;
        } else {
            String boundary = line.substring(index + 9);
            if (boundary.charAt(0) == 34) {
                index = boundary.lastIndexOf(34);
                boundary = boundary.substring(1, index);
            }

            boundary = "--" + boundary;
            return boundary;
        }
    }

    private String[] getFileName(String buffValue) throws IOException {
        String[] retval = new String[4];
        String origline = buffValue;
        buffValue = buffValue.toLowerCase();
        int start = buffValue.indexOf("content-disposition: ");
        int end = buffValue.indexOf(";");
        if (start != -1 && end != -1) {
            String disposition = buffValue.substring(start + 21, end).trim();
            if (!disposition.equals("form-data")) {
                throw new IOException("Invalid content disposition: " + disposition);
            } else {
                start = buffValue.indexOf("name=\"", end);
                end = buffValue.indexOf("\"", start + 7);
                byte startOffset = 6;
                if (start == -1 || end == -1) {
                    start = buffValue.indexOf("name=", end);
                    end = buffValue.indexOf(";", start + 6);
                    if (start == -1) {
                        throw new IOException("Content disposition corrupt: " + origline);
                    }

                    if (end == -1) {
                        end = buffValue.length();
                    }

                    startOffset = 5;
                }

                String name = origline.substring(start + startOffset, end);
                String filename = null;
                String origname = null;
                start = buffValue.indexOf("filename=\"", end + 2);
                end = buffValue.indexOf("\"", start + 10);
                if (start != -1 && end != -1) {
                    filename = origline.substring(start + 10, end);
                    origname = filename;
                    int slash = Math.max(filename.lastIndexOf(47), filename.lastIndexOf(92));
                    if (slash > -1) {
                        filename = filename.substring(slash + 1);
                    }
                }

                retval[0] = disposition;
                retval[1] = name;
                retval[2] = filename;
                retval[3] = origname;
                return retval;
            }
        } else {
            throw new IOException("Content disposition corrupt: " + origline);
        }
    }

}