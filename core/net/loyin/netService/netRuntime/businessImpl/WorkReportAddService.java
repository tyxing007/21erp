package net.loyin.netService.netRuntime.businessImpl;

import net.loyin.model.oa.WorkReport;
import net.loyin.netService.dataPacket.DataPacket;
import net.loyin.netService.dataPacket.HttpDataPacket;
import net.loyin.netService.domain.CommonHeader;
import net.loyin.netService.domain.CommonMessageVO;
import net.loyin.netService.domain.DataFormats;
import net.loyin.netService.domain.SelectedKeys;
import net.loyin.netService.netRuntime.IMessageRuntime;
import net.loyin.netService.netRuntime.impl.MessageRuntimeImpl;
import net.loyin.netService.vo.dsrVo.workVO.workAdd.workReportAddMainData;
import net.loyin.netService.vo.dsrVo.workVO.workAdd.workReportAddReturnMainData;
import net.loyin.netService.vo.dsrVo.workVO.workAdd.workReportAddReturnVo;
import net.loyin.netService.vo.dsrVo.workVO.workAdd.workReportAddVo;
import net.loyin.netService.vo.sysVo.userVo.UserReturnMainData;
import net.loyin.netService.vo.sysVo.userVo.UserReturnVo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chao on 2015/12/15.
 */
public class WorkReportAddService extends MessageRuntimeImpl implements IMessageRuntime {

    @Override
    public DataPacket run(CommonMessageVO vo) throws Exception {
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        workReportAddVo plan = (workReportAddVo)vo;
        Map<String,String> userMap = ((HttpDataPacket) this.getDataPacket()).getCookie();
        CommonHeader messageHeader = plan.getMessageHeader();
        workReportAddReturnVo returnVo = new workReportAddReturnVo();
        if(userMap == null){
            messageHeader.setRsp_no("FAILURE");
            messageHeader.setRsp_msg("无法获取企业ID");
            messageHeader.setBusinessCode("PLAN00002");
            returnVo.setMessageHeader(messageHeader);
            returnVo.setMaindata(new workReportAddReturnMainData());
        }else{
            workReportAddMainData mainData = plan.getMaindata();
            Map<String,Object> map = new HashMap<>();
            map.put("user_id",userMap.get("uid"));
            map.put("create_datetime",s.format(new Date()));
            map.put("subject",mainData.getReportTitle());
            map.put("content",mainData.getReportSummarize());
            map.put("type",mainData.getReportType());
            map.put("report_date",mainData.getReportStarTime());
            map.put("report_start_date",mainData.getReportStarTime());
            map.put("report_end_date",mainData.getReportEndTime());
            map.put("is_submit",1);
            map.put("plan",mainData.getReportPlan());
            map.put("order_count",mainData.getReportCount());
            map.put("money_count",mainData.getReportMoney());
            String id = WorkReport.dao.save(map);
            workReportAddReturnMainData work = new workReportAddReturnMainData();
            work.setReportId(id);
            messageHeader.setRsp_no("SUCCESS");
            messageHeader.setRsp_msg("");
            messageHeader.setBusinessCode("PLAN00002");
            returnVo.setMessageHeader(messageHeader);
            returnVo.setMaindata(work);

        }
        DataPacket httpDataPacket = this.getDataPacket().GetBackBaseDataPacket();
        httpDataPacket.setSelectedKey(SelectedKeys.WRITE);
        httpDataPacket.setRawDataFormat(DataFormats.OBJECT);
        httpDataPacket.setDataFormat(DataFormats.JSON);
        httpDataPacket.setRawdata(returnVo);
        httpDataPacket.setHead("PLAN00002");
        return httpDataPacket;
    }

    @Override
    public String packetsVerify(CommonMessageVO vo) {
        if(vo instanceof workReportAddVo){
            workReportAddVo plan = (workReportAddVo)vo;
            String msg = plan.validate();
            return msg;
        }else{
            return "非法报文";
        }
    }
}
