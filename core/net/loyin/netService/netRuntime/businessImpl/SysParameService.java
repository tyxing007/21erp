package net.loyin.netService.netRuntime.businessImpl;

import com.jfinal.plugin.activerecord.Record;
import net.loyin.model.sso.Parame;
import net.loyin.model.sso.User;
import net.loyin.netService.dataPacket.DataPacket;
import net.loyin.netService.dataPacket.HttpDataPacket;
import net.loyin.netService.domain.CommonHeader;
import net.loyin.netService.domain.CommonMessageVO;
import net.loyin.netService.domain.DataFormats;
import net.loyin.netService.domain.SelectedKeys;
import net.loyin.netService.netRuntime.IMessageRuntime;
import net.loyin.netService.netRuntime.impl.MessageRuntimeImpl;
import net.loyin.netService.vo.sysVo.parameVo.ParameReturnInfo;
import net.loyin.netService.vo.sysVo.parameVo.ParameReturnMainData;
import net.loyin.netService.vo.sysVo.parameVo.ParameReturnVo;
import net.loyin.netService.vo.sysVo.parameVo.ParameVo;
import net.loyin.netService.vo.sysVo.userVo.UserReturnInfo;
import net.loyin.netService.vo.sysVo.userVo.UserReturnMainData;
import net.loyin.netService.vo.sysVo.userVo.UserReturnVo;
import net.loyin.netService.vo.sysVo.userVo.UserVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Chao on 2015/12/16.
 * 获取系统参数（除文字参数外）
 */
public class SysParameService extends MessageRuntimeImpl implements IMessageRuntime {

    @Override
    public DataPacket run(CommonMessageVO vo) throws Exception {
        ParameVo parameVo = (ParameVo)vo;
        Map<String,String> userMap = ((HttpDataPacket) this.getDataPacket()).getCookie();
        CommonHeader messageHeader = parameVo.getMessageHeader();
        ParameReturnVo returnVo = new ParameReturnVo();
        if(userMap == null){
            messageHeader.setRsp_no("FAILURE");
            messageHeader.setRsp_msg("无法获取企业ID");
            messageHeader.setBusinessCode("SYS000010");
            returnVo.setMessageHeader(messageHeader);
            returnVo.setMaindata(new ParameReturnMainData());
        }else{
            String companyId = userMap.get("company_id");
            List<Record> qryList = Parame.dao.qryList(companyId, Integer.parseInt(parameVo.getMaindata().getParameType()));
            List<ParameReturnInfo> infos = new ArrayList<>();
            ParameReturnInfo info = null;
            for(Record r : qryList){
                info = new ParameReturnInfo();
                info.setParameId(r.getStr("id"));
                info.setParameName(r.getStr("name"));
                infos.add(info);
            }
            if(infos.size()==0){
                infos.add(new ParameReturnInfo());
            }
            messageHeader.setRsp_no("SUCCESS");
            messageHeader.setRsp_msg("");
            messageHeader.setBusinessCode("SYS000010");
            ParameReturnMainData returnMainData = new ParameReturnMainData();
            returnMainData.setParameInfos(infos);
            returnVo.setMaindata(returnMainData);
            returnVo.setMessageHeader(messageHeader);
        }
        DataPacket httpDataPacket = this.getDataPacket().GetBackBaseDataPacket();
        httpDataPacket.setSelectedKey(SelectedKeys.WRITE);
        httpDataPacket.setRawDataFormat(DataFormats.OBJECT);
        httpDataPacket.setDataFormat(DataFormats.JSON);
        httpDataPacket.setRawdata(returnVo);
        httpDataPacket.setHead("SYS000010");
        return httpDataPacket;
    }

    @Override
    public String packetsVerify(CommonMessageVO vo) {
        if(vo instanceof ParameVo){
            ParameVo parameVo = (ParameVo)vo;
            String msg = parameVo.validate();
            return msg;
        }else{
            return "非法报文";
        }
    }
}
