package net.loyin.netService.netRuntime.businessImpl;

import com.jfinal.plugin.activerecord.Record;
import net.loyin.model.oa.Punch;
import net.loyin.model.sso.User;
import net.loyin.netService.dataPacket.DataPacket;
import net.loyin.netService.dataPacket.HttpDataPacket;
import net.loyin.netService.domain.CommonHeader;
import net.loyin.netService.domain.CommonMessageVO;
import net.loyin.netService.domain.DataFormats;
import net.loyin.netService.domain.SelectedKeys;
import net.loyin.netService.netRuntime.IMessageRuntime;
import net.loyin.netService.netRuntime.impl.MessageRuntimeImpl;
import net.loyin.netService.vo.dsrVo.punchVo.PunchDataMain;
import net.loyin.netService.vo.dsrVo.punchVo.PunchReturnDataMain;
import net.loyin.netService.vo.dsrVo.punchVo.PunchReturnVo;
import net.loyin.netService.vo.dsrVo.punchVo.PunchVo;
import net.loyin.netService.vo.sysVo.userVo.UserReturnInfo;
import net.loyin.netService.vo.sysVo.userVo.UserReturnMainData;
import net.loyin.netService.vo.sysVo.userVo.UserReturnVo;
import net.loyin.netService.vo.sysVo.userVo.UserVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chao on 2015/12/16.
 */
public class PunchService extends MessageRuntimeImpl implements IMessageRuntime {

    @Override
    public DataPacket run(CommonMessageVO vo) throws Exception {
        PunchVo punchVo = (PunchVo)vo;
        Map<String,String> userMap = ((HttpDataPacket) this.getDataPacket()).getCookie();
        CommonHeader messageHeader = punchVo.getMessageHeader();
        PunchReturnVo returnVo = new PunchReturnVo();
        if(userMap == null){
            messageHeader.setRsp_no("FAILURE");
            messageHeader.setRsp_msg("无法获取企业ID");
            messageHeader.setBusinessCode("DSR000006");
            returnVo.setMessageHeader(messageHeader);
            returnVo.setMaindata(new PunchReturnDataMain());
        }else{
            PunchDataMain main = punchVo.getMaindata();
            PunchReturnDataMain returnMainData = new PunchReturnDataMain();
            Map<String,Object> map = new HashMap<>();
            map.put("check_type",main.getCheckType());
            map.put("check_lng",main.getCheckLng());
            map.put("check_lat",main.getCheckLat());
            map.put("check_time",main.getCheckTime());
            map.put("check_remark", main.getCheckRemark());
            String id = Punch.dao.save(map);
            returnMainData.setCheckId(id);
            messageHeader.setRsp_no("SUCCESS");
            messageHeader.setRsp_msg("");
            messageHeader.setBusinessCode("DSR000006");
            returnVo.setMessageHeader(messageHeader);
            returnVo.setMaindata(returnMainData);
        }
        DataPacket httpDataPacket = this.getDataPacket().GetBackBaseDataPacket();
        httpDataPacket.setSelectedKey(SelectedKeys.WRITE);
        httpDataPacket.setRawDataFormat(DataFormats.OBJECT);
        httpDataPacket.setDataFormat(DataFormats.JSON);
        httpDataPacket.setRawdata(returnVo);
        httpDataPacket.setHead("DSR000006");
        return httpDataPacket;
    }

    @Override
    public String packetsVerify(CommonMessageVO vo) {
        if(vo instanceof PunchVo){
            PunchVo punchVo = (PunchVo)vo;
            String msg = punchVo.validate();
            return msg;
        }else{
            return "非法报文";
        }
    }
}
