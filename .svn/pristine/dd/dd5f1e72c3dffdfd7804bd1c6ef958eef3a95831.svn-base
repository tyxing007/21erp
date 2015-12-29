package net.loyin.netService.handler;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.loyin.netService.dataPacket.HttpDataPacket;
import net.loyin.netService.model.AuditExceptionModel;
import net.loyin.netService.netRuntime.INewsletterRegister;
import net.loyin.netService.netRuntime.impl.NewsletterRegisterImpl;
import net.loyin.netService.response.IResponse;
import net.loyin.netService.response.Impl.GetResponseImpl;
import net.loyin.netService.response.Impl.PostResponseImpl;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * <pre>HTTP通讯接收处理类</pre>
 * <pre>所属模块：系统通讯</pre>
 *
 * @author 张维
 * @version 1.0 创建于 2014/8/1
 */

public class NetHandler implements HttpHandler {

    public static Logger log = Logger.getLogger(NetHandler.class);

    IResponse HttpPostResponse = new PostResponseImpl();

    IResponse HttpGetResponse = new GetResponseImpl();

    INewsletterRegister NewsletterRegister = new NewsletterRegisterImpl();


//    IMessageService MessageService;



    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        HttpDataPacket httpDataPacket = null;
        try {
            if (httpExchange.getRequestMethod().toUpperCase().equals("POST")) {
                httpDataPacket = HttpPostResponse.response(httpExchange);
            } else if (httpExchange.getRequestMethod().toUpperCase().equals("GET")) {
                httpDataPacket = HttpGetResponse.response(httpExchange);
            }
            NewsletterRegister.Register(httpDataPacket);
        } catch (Exception ex) {
            ex.printStackTrace();
            if (httpDataPacket == null) {
                log.error(ex);
            } else {
                log.error("消息接收模块发生异常，消息类型：" + httpDataPacket.getNetType().toString() + "；通讯渠道号：" + httpDataPacket.getChannelNo(), ex);
                AuditExceptionModel.dao.insertErrorLog(httpDataPacket, ex);
            }
        }
//        finally {
//            httpExchange.close();
//        }
    }
}
