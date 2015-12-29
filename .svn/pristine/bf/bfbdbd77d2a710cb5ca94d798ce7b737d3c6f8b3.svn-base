package net.loyin.netService.response.Impl;

import net.loyin.netService.dataPacket.NativeHttpDataPacket;
import net.loyin.netService.domain.DataFormats;
import net.loyin.netService.response.INativeResponseBack;

/**
 * Created by 张维 on 2014/11/5.
 * 原生HTTP响应
 */
public class NativeHttpResponseImpl implements INativeResponseBack {
    /**
     * HTTP响应
     */
    public void responseBack(NativeHttpDataPacket httpDataPacket) throws Exception {
        if (httpDataPacket.getResponse() == null) {
            return;
        } else {
            if (httpDataPacket.getHead() != null && httpDataPacket.getHead().length() > 0) {
                httpDataPacket.getResponse().setHeader("messageHead", httpDataPacket.getHead().trim());
            }
            if (httpDataPacket.getSectionId() != null && httpDataPacket.getSectionId().length() > 0) {
                httpDataPacket.getResponse().setHeader("markId", httpDataPacket.getSectionId().trim());
            }

            if (httpDataPacket.getDataFormat().equals(DataFormats.JSON) ||
                    httpDataPacket.getDataFormat().equals(DataFormats.XML)) {
                httpDataPacket.getResponse().setHeader("Content-Type", "text/xml; charset=utf-8");
                httpDataPacket.getResponse().getWriter().write(httpDataPacket.getParsedata().toString());
            } else if (httpDataPacket.getDataFormat().equals(DataFormats.BYTE)) {
                httpDataPacket.getResponse().setHeader("Content-Type", "multipart/form-data");
                httpDataPacket.getResponse().getWriter().write(new String((byte[]) httpDataPacket.getParsedata(), "UTF-8"));
            } else {
                return;
            }
        }
    }
}
