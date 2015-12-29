package net.loyin.netService.netRuntime.businessImpl;

import com.jfinal.plugin.activerecord.Page;
import net.loyin.model.oa.WorkReport;
import net.loyin.netService.dataPacket.DataPacket;
import net.loyin.netService.dataPacket.HttpDataPacket;
import net.loyin.netService.domain.CommonHeader;
import net.loyin.netService.domain.CommonMessageVO;
import net.loyin.netService.domain.DataFormats;
import net.loyin.netService.domain.SelectedKeys;
import net.loyin.netService.netRuntime.IMessageRuntime;
import net.loyin.netService.netRuntime.impl.MessageRuntimeImpl;
import net.loyin.netService.vo.dsrVo.workVO.workAdd.workReportAddReturnMainData;
import net.loyin.netService.vo.dsrVo.workVO.workAdd.workReportAddReturnVo;
import net.loyin.netService.vo.dsrVo.workVO.workAdd.workReportAddVo;
import net.loyin.netService.vo.dsrVo.workVO.workList.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chao on 2015/12/15.
 */
public class WorkReportListService extends MessageRuntimeImpl implements IMessageRuntime {

    @Override
    public DataPacket run(CommonMessageVO vo) throws Exception {
        workReportListVo listVo = (workReportListVo)vo;
        Map<String,String> userMap = ((HttpDataPacket) this.getDataPacket()).getCookie();
        CommonHeader messageHeader = listVo.getMessageHeader();
        workReportListReturnVo returnVo = new workReportListReturnVo();
        if(userMap == null){
            messageHeader.setRsp_no("FAILURE");
            messageHeader.setRsp_msg("无法获取企业ID");
            messageHeader.setBusinessCode("PLAN00004");
            returnVo.setMessageHeader(messageHeader);
            returnVo.setMaindata(new workReportListReturnDataMain());
        }else{
            workReportListDataMain main = listVo.getMaindata();
            workReportListReturnDataMain work = new workReportListReturnDataMain();
            List<workReportListReturnInfo> infos = new ArrayList<>();
            workReportListReturnInfo info = null;
            Map<String, Object> filter =  new HashMap<>();
            filter.put("keyword",main.getSeacrchTitle());
            filter.put("type",main.getReportType());
            filter.put("uid",userMap.get("uid"));
            filter.put("sort",main.getReportSort());
            filter.put("timeSort",main.getReportSortTime());
            Page<WorkReport> page = WorkReport.dao.pageGrid(Integer.parseInt(main.getReportPage()),Integer.parseInt(main.getReportPageSize()),filter,-1);
            List<WorkReport> works = page.getList();
            for(WorkReport w : works){
                info = new workReportListReturnInfo();
                info.setReportId(w.getStr("id"));
                info.setReportMoney(w.getStr("money_count"));
                info.setReportCount(w.getStr("order_count"));
                info.setReportTime(w.getStr("create_datetime"));
                info.setReportTitle(w.getStr("subject"));
                infos.add(info);
            }
            if(infos.size()==0){
                infos.add(new workReportListReturnInfo());
            }
            work.setReportInfo(infos);
            messageHeader.setRsp_no("SUCCESS");
            messageHeader.setRsp_msg("");
            messageHeader.setBusinessCode("PLAN00004");
            returnVo.setMessageHeader(messageHeader);
            returnVo.setMaindata(work);

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
        if(vo instanceof workReportListVo){
            workReportListVo listVo = (workReportListVo)vo;
            String msg = listVo.validate();
            return msg;
        }else{
            return "非法报文";
        }
    }
}
