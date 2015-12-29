package net.loyin.netService.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 张维 on 2014/11/5.
 * 原生HTTP请求接收地址
 */
public interface IHttpHandler {
    /**
     * 原生HTTP请求接收
     *
     * @param request
     * @param response
     */
    public void handle(HttpServletRequest request, HttpServletResponse response);
}
