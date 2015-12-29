package net.loyin.netService.netRuntime.impl;


import com.jfinal.plugin.activerecord.Db;
import net.loyin.netService.NetServiceInit;
import net.loyin.netService.dataPacket.DataPacket;
import net.loyin.netService.netRuntime.INetRuntimeHelper;
import net.loyin.netService.netRuntime.INewsletterRegister;
import net.loyin.netService.netRuntime.IRuntime;

/**
 * <pre>通讯模块运行时帮助</pre>
 * <pre>所属模块：系统通讯</pre>
 *
 * @author 张维
 * @version 1.0 创建于 2014/8/1
 */
public class NetRuntimeHelperImpl implements INetRuntimeHelper {




    /**
     * 刷新缓存
     */
    public void refreshCache() {

    }

    /**
     * 获取数据通讯段ID
     *
     * @return
     */
    public String getNetSectionId() {
        String markId = String.valueOf(Db.queryLong("select nextval('id_seq')"));
        if (markId.length() < 11) {
            int count = 11 - markId.length();
            for (int i = 0; i < count; i++) {
                markId = "0" + markId;
            }
        }
        return "N" + markId;
    }

    /**
     * 获取通讯层ID
     *
     * @return
     */
    public String getNetId() {
        return String.valueOf(Db.queryLong("select nextval('id_seq')"));
    }

    /**
     * 通讯缓存
     *
     * @param dataPacket
     * @return
     */
    public String doNetCache(DataPacket dataPacket) {
        return NewsletterRegister.RegisterRespond(dataPacket);
    }

    /**
     * 触发响应
     *
     * @param key
     * @param dataPacket
     */
    public void doRespond(String key, DataPacket dataPacket) throws Exception {
        NewsletterRegister.RespondRegister(key, dataPacket);
    }
}
