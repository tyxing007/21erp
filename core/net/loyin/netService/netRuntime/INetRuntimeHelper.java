package net.loyin.netService.netRuntime;


import net.loyin.netService.dataPacket.DataPacket;
import net.loyin.netService.netRuntime.impl.NewsletterRegisterImpl;

/**
 * <pre>通讯模块运行时帮助</pre>
 * <pre>所属模块：系统通讯</pre>
 *
 * @author 张维
 * @version 1.0 创建于 2014/8/1
 */
public interface INetRuntimeHelper {

    INewsletterRegister NewsletterRegister = new NewsletterRegisterImpl();

    /**
     * 刷新缓存
     */
    public void refreshCache();

    /**
     * 获取数据通讯段ID
     * @return
     */
    public String getNetSectionId();

    /**
     * 获取通讯层ID
     * @return
     */
    public String getNetId();

    /**
     * 通讯缓存
     * @param dataPacket
     * @return
     */
    public String doNetCache(DataPacket dataPacket);

    /**
     * 触发响应
     * @param key
     * @param dataPacket
     */
    public void doRespond(String key, DataPacket dataPacket) throws Exception;
}
