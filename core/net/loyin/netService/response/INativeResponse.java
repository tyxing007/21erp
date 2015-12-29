package net.loyin.netService.response;


import net.loyin.netService.dataPacket.NativeHttpDataPacket;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 张维 on 2014/11/5.
 * 原生HTTP请求响应
 */
public interface INativeResponse {
    /**
     * 响应
     *
     * @param nativeHttpDataPacket
     */
    public NativeHttpDataPacket response(HttpServletRequest request) throws Exception;
}
