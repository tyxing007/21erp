package net.loyin.netService.netRuntime.businessImpl;

import net.loyin.model.sso.User;
import net.loyin.netService.dataPacket.DataPacket;
import net.loyin.netService.domain.CommonHeader;
import net.loyin.netService.domain.CommonMessageVO;
import net.loyin.netService.domain.DataFormats;
import net.loyin.netService.domain.SelectedKeys;
import net.loyin.netService.netRuntime.IMessageRuntime;
import net.loyin.netService.netRuntime.impl.MessageRuntimeImpl;
import net.loyin.netService.vo.fileVo.FileSaveVo;
import net.loyin.netService.vo.sysVo.loginVo.LoginVo;
import net.loyin.netService.vo.sysVo.passWordVo.PassWordMainData;
import net.loyin.netService.vo.sysVo.passWordVo.PassWordReturnVo;
import net.loyin.netService.vo.sysVo.passWordVo.PassWordVo;
import net.loyin.util.safe.MD5;

/**
 * Created by Chao on 2015/12/8.
 */
public class ChangePassWordService extends MessageRuntimeImpl implements IMessageRuntime {


    @Override
    public DataPacket run(CommonMessageVO vo) throws Exception {
        PassWordVo wordVo = (PassWordVo)vo;
        PassWordMainData mainData = wordVo.getMaindata();
        String pwd = mainData.getOldPass();
        String pwd1 = mainData.getNewPass();
        String uid = mainData.getLoginId();
        PassWordReturnVo returnVo = new PassWordReturnVo();
        CommonHeader messageHeader = wordVo.getMessageHeader();
        pwd= MD5.getMD5ofStr(pwd);
        if(User.dao.chckPwd(pwd,uid)){
            messageHeader.setRsp_no("FAILURE");
            messageHeader.setRsp_msg("原密码不正确");
            messageHeader.setBusinessCode("SYS000006");
            returnVo.setMessageHeader(messageHeader);
        }else{
            pwd1=MD5.getMD5ofStr(pwd1);
            User.dao.upPwd(pwd1,uid);
            messageHeader.setRsp_no("SUCCESS");
            messageHeader.setRsp_msg("");
            messageHeader.setBusinessCode("SYS000006");
            returnVo.setMessageHeader(messageHeader);
        }
        DataPacket httpDataPacket = this.getDataPacket().GetBackBaseDataPacket();
        httpDataPacket.setSelectedKey(SelectedKeys.WRITE);
        httpDataPacket.setRawDataFormat(DataFormats.OBJECT);
        httpDataPacket.setDataFormat(DataFormats.JSON);
        httpDataPacket.setRawdata(returnVo);
        httpDataPacket.setHead("SYS000006");
        return httpDataPacket;
    }

    @Override
    public String packetsVerify(CommonMessageVO vo) {
        if(vo instanceof PassWordVo){
            PassWordVo passWordVo = (PassWordVo)vo;
            String msg = passWordVo.validate();
            return msg;
        }else{
            return "非法报文";
        }
    }
}