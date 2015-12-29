package net.loyin.netService.domain;

import net.loyin.netService.vo.dsrVo.workVO.workInfo.workReportInfoReturnDataMain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <pre>默认返回信息</pre>
 * <pre>所属模块：交易模块</pre>
 *
 * @author 张维
 * @version 1.0 创建于 2014/8/5
 */
@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.FIELD)
public class BackMessageVO extends CommonMessageVO {

    @XmlElement(name = "main_message")
    private BackMessageMainData maindata;

    @Override
    public boolean validate(boolean isReqAudit) {
        return false;
    }

    @Override
    public String validate() {
        return null;
    }


    @Override
    public BackMessageMainData getMaindata() {
        return maindata;
    }

    public void setMaindata(BackMessageMainData maindata) {
        this.maindata = maindata;
    }
}
