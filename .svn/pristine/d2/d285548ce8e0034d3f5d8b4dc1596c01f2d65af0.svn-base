package net.loyin.netService.handler;

import net.loyin.netService.dataPacket.NativeHttpDataPacket;
import net.loyin.netService.domain.RegistWay;
import net.loyin.netService.netRuntime.INewsletterRegister;
import net.loyin.netService.netRuntime.impl.NewsletterRegisterImpl;
import net.loyin.netService.response.INativeResponse;
import net.loyin.netService.response.Impl.NativePostResponseImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Administrator on 2014/11/5.
 */

public class HttpHandler implements IHttpHandler {

    INativeResponse nativePostResponse = new NativePostResponseImpl();

    INewsletterRegister NewsletterRegister = new NewsletterRegisterImpl();

    /**
     * 原生HTTP请求接收
     *
     * @param request
     * @param response
     */
    public void handle(HttpServletRequest request, HttpServletResponse response) {
        NativeHttpDataPacket nativeHttpDataPacket = null;
        if (request.getMethod().equals("POST")) {
            try {
                nativeHttpDataPacket = nativePostResponse.response(request);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                response.getWriter().write("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (nativeHttpDataPacket != null) {
            nativeHttpDataPacket.setResponse(response);
            nativeHttpDataPacket.setRegistWay(RegistWay.syncRun);
            NewsletterRegister.ReceiveBySync(nativeHttpDataPacket);
//            try {
//                NewsletterRegister.Receive(nativeHttpDataPacket,Thread.currentThread());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }
}