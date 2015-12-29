package net.loyin.netService.dataPacket;


import net.loyin.netService.domain.DataFormats;
import net.loyin.netService.domain.NetTypes;
import net.loyin.netService.domain.RegistWay;
import net.loyin.netService.domain.SelectedKeys;

/**
 * <pre>基础通讯数据包</pre>
 * <pre>所属模块：系统通讯</pre>
 *
 * @author 张维
 * @version 1.0 创建于 2014/8/1
 */
public class DataPacket {
    private Object Rawdata;//原始数据
    private DataFormats RawDataFormat;
    private DataFormats DataFormat;//发送数据格式
    private String ChannelNo;//渠道号
    private NetTypes NetType;//通讯类型
    private Object Parsedata;//解析数据
    private SelectedKeys SelectedKey;//数据通讯流向
    private String Head;//消息头
    private String MessageNo;//报文编号
    private String ProcessId;//报文处理表ID
    private String SectionId;//报文段ID
    private Thread joinThread;
    private RegistWay registWay;

    public DataPacket() {
        this.SelectedKey = SelectedKeys.WRITE;//将数据通讯流向默认设置为“写”
        this.registWay = RegistWay.Regist;
    }

    public Object getRawdata() {
        return Rawdata;
    }

    public void setRawdata(Object rawdata) {
        Rawdata = rawdata;
    }

    public DataFormats getDataFormat() {
        return DataFormat;
    }

    public void setDataFormat(DataFormats dataFormat) {
        DataFormat = dataFormat;
    }

    public String getChannelNo() {
        return ChannelNo;
    }

    public void setChannelNo(String channelNo) {
        ChannelNo = channelNo;
    }

    public NetTypes getNetType() {
        return NetType;
    }

    public void setNetType(NetTypes netType) {
        NetType = netType;
    }

    public Object getParsedata() {
        return Parsedata;
    }

    public void setParsedata(Object parsedata) {
        Parsedata = parsedata;
    }

    public SelectedKeys getSelectedKey() {
        return SelectedKey;
    }

    public void setSelectedKey(SelectedKeys selectedKey) {
        SelectedKey = selectedKey;
    }

    public DataFormats getRawDataFormat() {
        return RawDataFormat;
    }

    public void setRawDataFormat(DataFormats rawDataFormat) {
        RawDataFormat = rawDataFormat;
    }

    public String getHead() {
        return Head;
    }

    public void setHead(String head) {
        Head = head;
    }

    public String getMessageNo() {
        return MessageNo;
    }

    public void setMessageNo(String messageNo) {
        MessageNo = messageNo;
    }

    public String getProcessId() {
        return ProcessId;
    }

    public void setProcessId(String processId) {
        ProcessId = processId;
    }

    public String getSectionId() {
        return SectionId;
    }

    public void setSectionId(String sectionId) {
        SectionId = sectionId;
    }

    public Thread getJoinThread() {
        return joinThread;
    }

    public void setJoinThread(Thread joinThread) {
        this.joinThread = joinThread;
    }

    public RegistWay getRegistWay() {
        return registWay;
    }

    public void setRegistWay(RegistWay registWay) {
        this.registWay = registWay;
    }

    /**
     * 获取返回基础数据包信息
     * @param dataPacket
     */
    protected void GetBackBaseDataPacket(DataPacket dataPacket) {
        dataPacket.setDataFormat(this.RawDataFormat);
        dataPacket.setRawDataFormat(DataFormats.OBJECT);
        dataPacket.setChannelNo(this.ChannelNo);
        dataPacket.setHead(this.Head);
        dataPacket.setSectionId(this.SectionId);
        dataPacket.setNetType(this.NetType);
        dataPacket.setRegistWay(this.registWay);
        dataPacket.setSelectedKey(this.SelectedKey.equals(SelectedKeys.READ) ? SelectedKeys.WRITE : SelectedKeys.READ);
    }

    /**
     * 获取返回基础数据包信息
     */
    public DataPacket GetBackBaseDataPacket()
    {
        DataPacket dataPacket = new DataPacket();
        GetBackBaseDataPacket(dataPacket);
        return dataPacket;
    }
}
