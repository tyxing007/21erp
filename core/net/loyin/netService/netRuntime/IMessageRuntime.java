package net.loyin.netService.netRuntime;


import net.loyin.netService.dataPacket.DataPacket;
import net.loyin.netService.domain.CommonMessageVO;

/**
 * <pre>业务处理接口</pre>
 * <pre>所属模块：系统通讯</pre>
 *
 * @author 张维
 * @version 1.0 创建于 2014/8/1
 */
public interface IMessageRuntime {
    /**
     * 业务执行
     *
     * @param vo
     * @return
     */
    public DataPacket run(CommonMessageVO vo) throws Exception;

    /**
     * 报文业务信息校验
     *
     * @param vo
     * @return
     */
    public String packetsVerify(CommonMessageVO vo);

}
