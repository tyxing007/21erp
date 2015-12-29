package net.loyin.test;

import com.alibaba.fastjson.JSON;
import net.loyin.util.PropertiesContent;
import net.loyin.util.safe.CipherUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Chao on 2015/11/19.
 */
public class httpTest {

    private String httpIp = "";


    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection)conn;
            // 设置通用的请求属性
//            conn.set
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("accept", "*/*");
            httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(param.length()));
            httpURLConnection.setRequestProperty("dataFormat", "xml"); //文本格式 必填
            httpURLConnection.setRequestProperty("messageHead", "ORDER0001"); //业务代码 必填
            httpURLConnection.setRequestProperty("Cookie", "ecp_cookie=f14221f39759ebce20672570ee7d2f3e3933b164a7a03537256171dbe99407810f1bf6d88b2234067136af43292d5c010f0daf47a02a8ab726084ef316fa22e11d533cf0861a2bd415ec44686553202c89558b296c1780e74ef065a573b1fad7998702a3a231a63cac6a93db67e8e2250b05882d7d0d9931f775cbf9b18b771dfe61e0d5d9711392ba312742df88a9b415ec44686553202c494b1571f3fb4830bb006d38bdad1117b066ce48c120e77918a96ea8423876a010e1d5f7fad8cdb5270dbe899e205530");
            // 发送POST请求必须设置如下两行
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = null;
            OutputStreamWriter outputStreamWriter = null;
            InputStream inputStream = null;
            InputStreamReader inputStreamReader = null;
            BufferedReader reader = null;
            StringBuffer resultBuffer = new StringBuffer();
            String tempLine = null;

            try {
                outputStream = httpURLConnection.getOutputStream();
                outputStreamWriter = new OutputStreamWriter(outputStream);

                outputStreamWriter.write(param.toString());
                outputStreamWriter.flush();

                if (httpURLConnection.getResponseCode() >= 300) {
                    throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
                }

                inputStream = httpURLConnection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                reader = new BufferedReader(inputStreamReader);

                while ((tempLine = reader.readLine()) != null) {
                    resultBuffer.append(tempLine);
                }

            } finally {

                if (outputStreamWriter != null) {
                    outputStreamWriter.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }

                if (reader != null) {
                    reader.close();
                }

                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }

            }

            return resultBuffer.toString();


        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static void main(String[] avg) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
        String body =
                "<data>" +
                        "<head_message>" +
                        "<message_no>"+simpleDateFormat.format(new Date())+"</message_no>" +
                        "<app_no>1.0</app_no>" +
                        "<data_no>1.0</data_no>" +
                        "<request_time>"+simpleDateFormat2.format(new Date())+"</request_time>" +
                        "<business_code>ORDER0001</business_code>" +
                        "</head_message>" +
                        "<main_message>" +
                        "<order_page>1</order_page>" +
                        "<order_pageSize>10</order_pageSize>" +
                        "<customer_id></customer_id>" +
                        "<customer_name></customer_name>" +
                        "</main_message>" +
                        "</data>";

        //发送 POST 请求
        String sr = sendPost("http://192.168.4.179:10000/req", body);
        System.out.println(sr);
    }
}
