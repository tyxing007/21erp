package net.loyin.netService.netRuntime.businessImpl;

import com.jfinal.plugin.activerecord.Page;
import net.loyin.model.oa.Attendance;
import net.loyin.model.sso.Parame;
import net.loyin.netService.dataPacket.DataPacket;
import net.loyin.netService.dataPacket.HttpDataPacket;
import net.loyin.netService.domain.CommonHeader;
import net.loyin.netService.domain.CommonMessageVO;
import net.loyin.netService.domain.DataFormats;
import net.loyin.netService.domain.SelectedKeys;
import net.loyin.netService.netRuntime.IMessageRuntime;
import net.loyin.netService.netRuntime.impl.MessageRuntimeImpl;
import net.loyin.netService.vo.customerVo.contactsVo.ConctactsReturnVo;
import net.loyin.netService.vo.customerVo.contactsVo.ContactsMainData;
import net.loyin.netService.vo.customerVo.contactsVo.ContactsVo;
import net.loyin.netService.vo.dsrVo.attendanceVO.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chao on 2015/12/1.
 */
public class AttendanceService extends MessageRuntimeImpl implements IMessageRuntime {
    @Override
    public DataPacket run(CommonMessageVO vo) throws Exception {
        DsrAttendanceVo attendanceVo = (DsrAttendanceVo)vo;
        DsrAttendanceMainData mainData = attendanceVo.getMaindata();
        Map<String,String> userMap = ((HttpDataPacket) this.getDataPacket()).getCookie();
        DsrAttendanceReturnVo returnVo = new DsrAttendanceReturnVo();
        CommonHeader messageHeader = attendanceVo.getMessageHeader();
        if(userMap == null){
            DsrAttendanceReturnMainData returnMainData = new DsrAttendanceReturnMainData();
            returnVo.setMaindata(returnMainData);
            messageHeader.setRsp_no("FAILURE");
            messageHeader.setRsp_msg("您未登陆,请重新登陆");
            messageHeader.setBusinessCode("DSR000002");
        }else{
            String companyId = userMap.get("company_id");
            String uid =  userMap.get("uid");
            Map<String, Object> filter = new HashMap<>();
            filter.put("company_id",companyId);
            filter.put("attendance",mainData.getAttendanceType());
            filter.put("uid",uid);
            filter.put("startTime",mainData.getStartTime());
            filter.put("endTime", mainData.getEndTime());
            Page<Attendance> page = Attendance.dao.pageGrid(Integer.parseInt(mainData.getPageNum()),Integer.parseInt(mainData.getPageSize()),filter);
            List<Attendance> list = page.getList();
            DsrAttendanceReturnInfo info = null;
            List<DsrAttendanceReturnInfo> infoList = new ArrayList<>();
            for(Attendance a : list){
                info = new DsrAttendanceReturnInfo();
                info.setAttendanceEndTime(a.getStr("end_time"));
                info.setAttendanceRemark(a.getStr("remark"));
                info.setAttendanceStartTime(a.getStr("start_time"));
                info.setAttendanceStatus(Attendance.dao.checkStatus(a.getInt("status")));
                info.setAttendanceTime(a.getStr("create_time"));
                info.setAttendanceType(a.getStr("attendance_type_type"));
                Parame parame = Parame.dao.findById(a.getStr("attendance_type_type"),companyId);
                info.setAttendanceType(parame==null?"未知":parame.getStr("name"));
                infoList.add(info);
            }
            DsrAttendanceReturnMainData returnMainData = new DsrAttendanceReturnMainData();
            returnMainData.setInfo(infoList);
            returnMainData.setAttendanceType(mainData.getAttendanceType());
            returnVo.setMaindata(returnMainData);
            messageHeader.setRsp_no("SUCCESS");
            messageHeader.setRsp_msg("请求成功");
            messageHeader.setBusinessCode("DSR000002");
        }

        returnVo.setMessageHeader(messageHeader);
        DataPacket httpDataPacket = this.getDataPacket().GetBackBaseDataPacket();
        httpDataPacket.setSelectedKey(SelectedKeys.WRITE);
        httpDataPacket.setRawDataFormat(DataFormats.OBJECT);
        httpDataPacket.setDataFormat(DataFormats.JSON);
        httpDataPacket.setRawdata(returnVo);
        httpDataPacket.setHead("DSR000002");
        return httpDataPacket;
    }

    @Override
    public String packetsVerify(CommonMessageVO vo) {
        if(vo instanceof DsrAttendanceVo){
            DsrAttendanceVo attendanceVo = (DsrAttendanceVo)vo;
            String msg = attendanceVo.validate();
            return msg;
        }else{
            return "非法报文";
        }
    }
}
