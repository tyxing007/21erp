package net.loyin.netService.netRuntime.MessageReceived.Impl;

import net.loyin.netService.BusinessProcess;
import net.loyin.netService.NetCache;
import net.loyin.netService.domain.*;
import net.loyin.netService.model.AuditExceptionModel;
import net.loyin.netService.model.AuditProcessModel;
import net.loyin.netService.model.BusinessModel;
import net.loyin.netService.netRuntime.IMessageRuntime;
import net.loyin.netService.dataOperate.dataSerialization.IDataSerialization;
import net.loyin.netService.dataOperate.dataSerialization.Impl.DataSerializationImpl;
import net.loyin.netService.dataPacket.DataPacket;
import net.loyin.netService.netRuntime.INetRuntimeHelper;
import net.loyin.netService.netRuntime.INewsletterRegister;
import net.loyin.netService.netRuntime.MessageReceived.IMessageRead;
import net.loyin.netService.netRuntime.impl.MessageRuntimeImpl;
import net.loyin.netService.netRuntime.impl.NetRuntimeHelperImpl;
import net.loyin.netService.netRuntime.impl.NewsletterRegisterImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <pre>消息接收核心对象</pre>
 * <pre>所属模块：系统通讯</pre>
 *
 * @author 张维
 * @version 1.0 创建于 2014/8/1
 */
public abstract class MessageReadImpl implements IMessageRead {


    IDataSerialization DataSerialization = new DataSerializationImpl();

    INetRuntimeHelper NetRuntimeHelper = new NetRuntimeHelperImpl();

    protected static Log log = LogFactory.getLog(MessageReadImpl.class);

    public void PacketsReceive(DataPacket dataPacket) throws Exception {
        try {
            if (dataPacket.getHead() == null || dataPacket.getHead().length() == 0) {
                throw new Exception("无法获取消息头");
            }
            if (dataPacket.getSectionId() == null || dataPacket.getSectionId().length() == 0) {
                dataPacket.setSectionId(NetRuntimeHelper.getNetSectionId());
            }
            this.PacketsParse(dataPacket);
            if (dataPacket.getParsedata() instanceof CommonMessageVO) {
                CommonMessageVO vo = (CommonMessageVO) dataPacket.getParsedata();
                dataPacket.setChannelNo(vo.getMessageHeader().getSnd_chnl_no()==null?"":vo.getMessageHeader().getSnd_chnl_no());
                if (this.PacketsVerify(vo)) {
                    DataPacket runDataPacked = this.BusinessProcess(vo, dataPacket);
                    this.Follow(dataPacket, runDataPacked);
                } else {
                    throw new Exception("报文头校验失败");
                }
            } else {
                throw new Exception("报文数据转换发生异常");
            }
        } catch (Exception ex) {
            String msg = "系统异常";
            if (ex instanceof Exception) {
                msg = ex.getMessage();
            }
            BackMessageVO backMessageVO = new BackMessageVO();
            BackMessageMainData backMessageMainData = new BackMessageMainData();
            backMessageVO.setMaindata(backMessageMainData);
            CommonHeader commonHeader = new CommonHeader();
            commonHeader.setRsp_no("FAILURE");
            commonHeader.setRsp_msg(msg);
            backMessageVO.setMessageHeader(commonHeader);
            DataPacket backDataPacket = dataPacket.GetBackBaseDataPacket();
            backDataPacket.setHead("999999");
            backDataPacket.setRawdata(backMessageVO);
            backDataPacket.setRawDataFormat(DataFormats.OBJECT);
            backDataPacket.setDataFormat(DataFormats.JSON);
            NewsletterRegister.Register(backDataPacket);
            throw ex;
        }
    }

    /**
     * 数据包解析
     *
     * @param dataPacket
     * @return
     */
    public abstract void PacketsParse(DataPacket dataPacket) throws Exception;

    /**
     * 后续执行
     *
     * @param dataPacket
     * @param runDataPacket
     */
    public abstract void Follow(DataPacket dataPacket, DataPacket runDataPacket) throws Exception;

    /**
     * 业务处理执行
     *
     * @param vo
     */
    public DataPacket BusinessProcess(CommonMessageVO vo, DataPacket dataPacket) throws Exception {
        DataPacket tempDataPacket = new DataPacket();
        tempDataPacket.setRawdata(vo);
        tempDataPacket.setRawDataFormat(DataFormats.OBJECT);
        tempDataPacket.setDataFormat(dataPacket.getRawDataFormat().equals(DataFormats.JSON) ? DataFormats.JSON : DataFormats.XML);
        Object obj = DataSerialization.Serialization(tempDataPacket);
        if (obj == null) {
            throw new NullPointerException("报文转换发生异常，将VO：" + vo.getClass().getName() + "序列化为XML时发生异常");
        }
        String processId = null;
        IMessageRuntime msgrun = this.GetBusinessObject(vo);
        if (msgrun instanceof MessageRuntimeImpl) {
            ((MessageRuntimeImpl) msgrun).setDataPacket(dataPacket);
        }
        String xml = obj.toString();
        processId = AuditProcessModel.dao.saveBusiness(xml, this.GetProcessId(vo), dataPacket.getMessageNo());
        try {
            if (msgrun != null) {
                String verifyMsg = msgrun.packetsVerify(vo);
                if (verifyMsg != null && verifyMsg.length() > 0) {
                    throw new NullPointerException("交易失败，参数传递错误。" + verifyMsg);
                }
                DataPacket runDataPacket = msgrun.run(vo);
                AuditProcessModel.dao.updateBusinessInfo(processId, "0");
                return runDataPacket;
            }
        } catch (NullPointerException hex) {
            throw new Exception(hex.getMessage(), hex);
        } catch (Exception ex) {
            AuditProcessModel.dao.updateBusinessInfo(processId, "0");
            throw new Exception("交易失败，业务处理异常：" + ex.getMessage(), ex);
        }
        return null;
    }

    /**
     * 获取业务处理对象
     *
     * @param vo
     */
    public IMessageRuntime GetBusinessObject(CommonMessageVO vo) throws Exception {
        try {
            BusinessProcess businessProcess = NetCache.GetBusinessProcessByProcessCodeAndVersion(vo.getMessageHeader().getBusinessCode(), vo.getMessageHeader().getAppNo());
            if (businessProcess != null) {
//                Object obj = SpringContextHolder.getBean(businessProcess.getProcessClass());
                Object obj = Class.forName("net.loyin.netService.netRuntime.businessImpl."+businessProcess.getProcessClass()).newInstance();
                if (obj instanceof IMessageRuntime) {
                    return (IMessageRuntime) obj;
                }
            }
            throw new Exception("无法找到交易号所对应的业务处理 11");
        } catch (Exception ex) {
            throw new Exception("无法找到交易号所对应的业务处理11", ex);
        }
    }

    /**
     * 获取业务处理ID
     *
     * @param vo
     * @return
     * @throws Exception
     */
    private String GetProcessId(CommonMessageVO vo) throws Exception {
        BusinessProcess businessProcess = NetCache.GetBusinessProcessByProcessCodeAndVersion(vo.getMessageHeader().getBusinessCode(), vo.getMessageHeader().getAppNo());
        if (businessProcess != null) {
            return businessProcess.getProcessId();
        }
        return null;
    }

    /**
     * 报文存储
     *
     * @param obj
     * @return
     */
    public CommonMessageVO PacketsStorage(Object obj, String head) throws Exception {
//        if (obj instanceof CommonMessageVO) {
//            return (CommonMessageVO) obj;
//        } else if (obj instanceof String) {
//            String xml = obj.toString();
//            if (xml.length() > 0) {
//
//                //通过报文前6位数判断要获取到哪个报文VO
//                CommonMessageVO vo = (CommonMessageVO) XMLUtil.XMLStringToBean(onwClass, xml);
//                return vo;
//            }
//        }
        return null;
    }

    /**
     * 报文校验
     *
     * @param vo
     * @return
     */
    public boolean PacketsVerify(CommonMessageVO vo) throws Exception {
        if (vo.getMessageHeader() == null) {
            throw new Exception("无法获取报文头信息");
        }
        String busiCode = vo.getMessageHeader().getBusinessCode();
        if (busiCode == null || busiCode.length() == 0) {
            throw new Exception("无法获取正确的交易代码");
        }
        return true;
    }
}