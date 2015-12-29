package net.loyin.netService.netRuntime.businessImpl;

import com.alibaba.fastjson.JSON;
import net.loyin.model.sso.Company;
import net.loyin.model.sso.User;
import net.loyin.netService.dataPacket.DataPacket;
import net.loyin.netService.dataPacket.HttpDataPacket;
import net.loyin.netService.domain.CommonHeader;
import net.loyin.netService.domain.CommonMessageVO;
import net.loyin.netService.domain.DataFormats;
import net.loyin.netService.domain.SelectedKeys;
import net.loyin.netService.netRuntime.IMessageRuntime;
import net.loyin.netService.netRuntime.impl.MessageRuntimeImpl;
import net.loyin.netService.vo.fileVo.FileSaveVo;
import net.loyin.netService.vo.sysVo.loginVo.LoginMainData;
import net.loyin.netService.vo.sysVo.loginVo.LoginReturnMainData;
import net.loyin.netService.vo.sysVo.loginVo.LoginReturnVo;
import net.loyin.netService.vo.sysVo.loginVo.LoginVo;
import net.loyin.netService.vo.sysVo.logoutVo.LogoutVo;
import net.loyin.util.PropertiesContent;
import net.loyin.util.safe.CipherUtil;
import net.loyin.util.safe.MD5;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chao on 2015/12/8.
 */

public class LogoutService extends MessageRuntimeImpl implements IMessageRuntime {

    @Override
    public DataPacket run(CommonMessageVO vo) throws Exception {
        LogoutVo logoutVo = (LogoutVo) vo;
        LoginReturnVo returnVo = new LoginReturnVo();
        LoginReturnMainData returnMainData = new LoginReturnMainData();
        returnMainData.setUserPic("");
        returnMainData.setUserCookie("");
        returnVo.setMaindata(returnMainData);
        CommonHeader messageHeader = logoutVo.getMessageHeader();
        messageHeader.setRsp_no("SUCCESS");
        messageHeader.setRsp_msg("");
        messageHeader.setBusinessCode("SYS000004");
        returnVo.setMessageHeader(messageHeader);
        DataPacket httpDataPacket = this.getDataPacket().GetBackBaseDataPacket();
        httpDataPacket.setSelectedKey(SelectedKeys.WRITE);
        httpDataPacket.setRawDataFormat(DataFormats.OBJECT);
        httpDataPacket.setDataFormat(DataFormats.JSON);
        httpDataPacket.setRawdata(returnVo);
        httpDataPacket.setHead("SYS000002");
        return httpDataPacket;
    }

    @Override
    public String packetsVerify(CommonMessageVO vo) {
        if (vo instanceof LogoutVo) {
            LogoutVo logoutVo = (LogoutVo) vo;
            String msg = logoutVo.validate();
            return msg;
        } else {
            return "非法报文";
        }
    }
}
