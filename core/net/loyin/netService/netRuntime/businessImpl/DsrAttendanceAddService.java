package net.loyin.netService.netRuntime.businessImpl;

import net.loyin.model.oa.Attendance;
import net.loyin.netService.dataPacket.DataPacket;
import net.loyin.netService.dataPacket.HttpDataPacket;
import net.loyin.netService.domain.CommonHeader;
import net.loyin.netService.domain.CommonMessageVO;
import net.loyin.netService.domain.DataFormats;
import net.loyin.netService.domain.SelectedKeys;
import net.loyin.netService.netRuntime.IMessageRuntime;
import net.loyin.netService.netRuntime.impl.MessageRuntimeImpl;
import net.loyin.netService.vo.customerVo.contactsVo.ContactsVo;
import net.loyin.netService.vo.dsrVo.attendanceVO.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chao on 2015/12/1.
 */
public class DsrAttendanceAddService extends MessageRuntimeImpl implements IMessageRuntime {
    @Override
    public DataPacket run(CommonMessageVO vo) throws Exception {
        SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DsrAttendanceAddVo addVo = (DsrAttendanceAddVo)vo;
        DsrAttendanceAddMainData mainData = addVo.getMaindata();
        Map<String,String> userMap = ((HttpDataPacket) this.getDataPacket()).getCookie();
        DsrAttendanceAddReturnVo returnVo = new DsrAttendanceAddReturnVo();
        CommonHeader messageHeader = addVo.getMessageHeader();
        if(userMap == null){
            DsrAttendanceAddReturnMainData returnMainData = new DsrAttendanceAddReturnMainData();
            returnVo.setMaindata(returnMainData);
            messageHeader.setRsp_no("FAILURE");
            messageHeader.setRsp_msg("您未登陆,请重新登陆");
            messageHeader.setBusinessCode("DSR000004");
            returnVo.setMessageHeader(messageHeader);
        }else {
            String companyId = userMap.get("company_id");
            String uid = userMap.get("uid");
            Map<String, Object> map = new HashMap<>();
            map.put("user_id", uid);
            map.put("attendance_type", Integer.parseInt(mainData.getAttendanceType()));
            map.put("create_time", toDate.format(new Date()));
            map.put("start_time", mainData.getAttendanceStartTime());
            map.put("end_time", mainData.getAttendanceEndTime());
            map.put("remark", mainData.getAttendanceRemark());
            map.put("attendance_type_type", Integer.parseInt(mainData.getAttendanceGenre()));
            map.put("company_id", companyId);
            map.put("check_user", mainData.getAttendanceReviewer());
            map.put("status", 0);
            String id = Attendance.dao.saveAttendanceBackId(map);
            DsrAttendanceAddReturnMainData returnMainData = new DsrAttendanceAddReturnMainData();
            returnMainData.setAttendanceId(id);
            returnVo.setMaindata(returnMainData);

            messageHeader.setRsp_no("SUCCESS");
            messageHeader.setRsp_msg("请求成功");
            messageHeader.setBusinessCode("DSR000004");
            returnVo.setMessageHeader(messageHeader);
        }
        DataPacket httpDataPacket = this.getDataPacket().GetBackBaseDataPacket();
        httpDataPacket.setSelectedKey(SelectedKeys.WRITE);
        httpDataPacket.setRawDataFormat(DataFormats.OBJECT);
        httpDataPacket.setDataFormat(DataFormats.JSON);
        httpDataPacket.setRawdata(returnVo);
        httpDataPacket.setHead("DSR000004");
        return httpDataPacket;
    }

    @Override
    public String packetsVerify(CommonMessageVO vo) {
        if(vo instanceof DsrAttendanceAddVo){
            DsrAttendanceAddVo addVo = (DsrAttendanceAddVo)vo;
            String msg = addVo.validate();
            return msg;
        }else{
            return "非法报文";
        }
    }
}
