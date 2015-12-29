package net.loyin.netService.netRuntime.businessImpl;

import com.jfinal.plugin.activerecord.Page;
import net.loyin.model.oa.WorkReport;
import net.loyin.model.sso.Parame;
import net.loyin.netService.dataPacket.DataPacket;
import net.loyin.netService.dataPacket.HttpDataPacket;
import net.loyin.netService.domain.CommonHeader;
import net.loyin.netService.domain.CommonMessageVO;
import net.loyin.netService.domain.DataFormats;
import net.loyin.netService.domain.SelectedKeys;
import net.loyin.netService.netRuntime.IMessageRuntime;
import net.loyin.netService.netRuntime.impl.MessageRuntimeImpl;
import net.loyin.netService.vo.dsrVo.workVO.workInfo.workReportInfoDataMain;
import net.loyin.netService.vo.dsrVo.workVO.workInfo.workReportInfoReturnDataMain;
import net.loyin.netService.vo.dsrVo.workVO.workInfo.workReportInfoReturnVo;
import net.loyin.netService.vo.dsrVo.workVO.workInfo.workReportInfoVo;
import net.loyin.netService.vo.dsrVo.workVO.workList.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chao on 2015/12/15.
 */
public class WorkReportInfoService extends MessageRuntimeImpl implements IMessageRuntime {

    @Override
    public DataPacket run(CommonMessageVO vo) throws Exception {
        workReportInfoVo infoVo = (workReportInfoVo)vo;
        Map<String,String> userMap = ((HttpDataPacket) this.getDataPacket()).getCookie();
        CommonHeader messageHeader = infoVo.getMessageHeader();
        workReportInfoReturnVo returnVo = new workReportInfoReturnVo();
        if(userMap == null){
            messageHeader.setRsp_no("FAILURE");
            messageHeader.setRsp_msg("无法获取企业ID");
            messageHeader.setBusinessCode("PLAN00004");
            returnVo.setMessageHeader(messageHeader);
            returnVo.setMaindata(new workReportInfoReturnDataMain());
        }else{
            workReportInfoDataMain main = infoVo.getMaindata();
            workReportInfoReturnDataMain work = new workReportInfoReturnDataMain();
           WorkReport workReport = WorkReport.dao.findById(main.getReportId());
            if(workReport == null){
                messageHeader.setRsp_no("FAILURE");
                messageHeader.setRsp_msg("计划ID不存在");
                messageHeader.setBusinessCode("PLAN00004");
                returnVo.setMessageHeader(messageHeader);
                returnVo.setMaindata(new workReportInfoReturnDataMain());
            }else{
                work.setReportTitle(workReport.getStr("subject"));
                work.setReportTime(workReport.getStr("report_date"));
                Parame parame = Parame.dao.findById(workReport.getStr("type"), userMap.get("company_id"));
                if(parame == null){
                    work.setReportType("未知");
                }else{
                    work.setReportType(parame.getStr("name"));
                }
                work.setReportCount(workReport.getStr("order_count"));
                work.setReportMoney(workReport.getStr("money_count"));
                work.setReportSummarize(workReport.getStr("content"));
                work.setReportPlan(workReport.getStr("plan"));
                work.setReportLeader(workReport.getStr("leader"));
                messageHeader.setRsp_no("SUCCESS");
                messageHeader.setRsp_msg("");
                messageHeader.setBusinessCode("PLAN00004");
                returnVo.setMessageHeader(messageHeader);
                returnVo.setMaindata(work);
            }



        }
        DataPacket httpDataPacket = this.getDataPacket().GetBackBaseDataPacket();
        httpDataPacket.setSelectedKey(SelectedKeys.WRITE);
        httpDataPacket.setRawDataFormat(DataFormats.OBJECT);
        httpDataPacket.setDataFormat(DataFormats.JSON);
        httpDataPacket.setRawdata(returnVo);
        httpDataPacket.setHead("PLAN00004");
        return httpDataPacket;
    }

    @Override
    public String packetsVerify(CommonMessageVO vo) {
        if(vo instanceof workReportInfoVo){
            workReportInfoVo infoVo = (workReportInfoVo)vo;
            String msg = infoVo.validate();
            return msg;
        }else{
            return "非法报文";
        }
    }
}
