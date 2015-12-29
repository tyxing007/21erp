package net.loyin.netService.response;


import net.loyin.netService.dataPacket.HttpDataPacket;

/**
 * <pre>HTTP消息响应回写接口</pre>
 * <pre>所属模块：系统通讯</pre>
 * @author 张维
 * @version 1.0 创建于 2014/8/1
 */
public interface IResponseBack {
    /**
     * HTTP消息响应回写
     * @param httpDataPacket
     */
    public void responseBack(HttpDataPacket httpDataPacket) throws Exception ;
}
