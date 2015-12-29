package net.loyin.netService.netRuntime.impl;


import com.jfinal.plugin.activerecord.Model;
import net.loyin.netService.dataPacket.DataPacket;
import net.loyin.netService.model.BusinessModel;
import net.loyin.netService.netRuntime.IMessageRuntime;
import net.loyin.netService.netRuntime.INetRuntimeHelper;

/**
 * <pre>业务处理抽象</pre>
 * <pre>所属模块：系统通讯</pre>
 *
 * @author 张维
 * @version 1.0 创建于 2014/8/1
 */
public abstract class MessageRuntimeImpl implements IMessageRuntime {


    private INetRuntimeHelper NetRuntimeHelper = new NetRuntimeHelperImpl();

    /**
     * 获取消息编号
     * @return
     */
    protected String getMessageNo()
    {
        return dataPacket.getMessageNo();
    }

    public DataPacket getDataPacket() {
        return dataPacket;
    }

    public void setDataPacket(DataPacket dataPacket) {
        this.dataPacket = dataPacket;
    }

    /**
     * 包数据
     */
    private DataPacket dataPacket;

    public String doNetCache()
    {
        return NetRuntimeHelper.doNetCache(this.dataPacket);
    }
}
