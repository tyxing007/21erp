package net.loyin.netService.netRuntime.businessImpl;

import com.jfinal.plugin.activerecord.Page;
import net.loyin.model.crm.ConcatRecord;
import net.loyin.model.sso.FileBean;
import net.loyin.netService.dataPacket.DataPacket;
import net.loyin.netService.dataPacket.HttpDataPacket;
import net.loyin.netService.domain.CommonHeader;
import net.loyin.netService.domain.CommonMessageVO;
import net.loyin.netService.domain.DataFormats;
import net.loyin.netService.domain.SelectedKeys;
import net.loyin.netService.netRuntime.IMessageRuntime;
import net.loyin.netService.netRuntime.impl.MessageRuntimeImpl;
import net.loyin.netService.vo.customerVo.customerVisitsVo.*;
import net.loyin.netService.vo.fileVo.FileSaveMainData;
import net.loyin.netService.vo.fileVo.FileSaveVo;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Chao on 2015/12/1.
 */
public class CustomerVisitsService extends MessageRuntimeImpl implements IMessageRuntime {
    SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat toString = new SimpleDateFormat("yyyy年MM月dd日");
    @Override
    public DataPacket run(CommonMessageVO vo) throws Exception {
        Map<String,String> userMap = ((HttpDataPacket) this.getDataPacket()).getCookie();
        CustomerVisitsVo visitsVo = (CustomerVisitsVo)vo;
        CustomerVisitsMainData mainData = visitsVo.getMaindata();
        Map<String,Object> filter=new HashMap<String,Object>();
        filter.put("company_id",userMap.get("company_id"));
        filter.put("keyword",mainData.getKey());
        filter.put("uid",userMap.get("uid"));
        filter.put("user_id", userMap.get("uid"));
        filter.put("position_id",userMap.get("position_id"));

        Page<ConcatRecord> page = ConcatRecord.dao.page(Integer.parseInt(mainData.getPageNum()), Integer.parseInt(mainData.getPageSize()), filter, 11);
        List<ConcatRecord> list = page.getList();
        CustomerVisitsReturnInfo info  = null;
        List<CustomerVisitsReturnInfo> returnInfoList = new ArrayList<>();
        for(ConcatRecord c : list){
            info = new CustomerVisitsReturnInfo();
            info.setCustomerName(c.getStr("customer_name"));
            info.setVisitId(c.getStr("id"));
            info.setVisitTime(toString.format(toDate.parse(c.getStr("concat_datetime"))));
            returnInfoList.add(info);
        }
        if(returnInfoList.size() == 0){
            info = new CustomerVisitsReturnInfo();
            returnInfoList.add(info);
        }
        CustomerVisitsReturnMainData returnMainData = new CustomerVisitsReturnMainData();
        returnMainData.setVisitsList(returnInfoList);
        CustomerVisitsReturnVo returnVo = new CustomerVisitsReturnVo();
        returnVo.setMaindata(returnMainData);
        CommonHeader messageHeader = visitsVo.getMessageHeader();
        messageHeader.setRsp_no("SUCCESS");
        messageHeader.setRsp_msg("请求成功");
        messageHeader.setBusinessCode("CUS000008");
        returnVo.setMessageHeader(messageHeader);
        DataPacket httpDataPacket = this.getDataPacket().GetBackBaseDataPacket();
        httpDataPacket.setSelectedKey(SelectedKeys.WRITE);
        httpDataPacket.setRawDataFormat(DataFormats.OBJECT);
        httpDataPacket.setDataFormat(DataFormats.JSON);
        httpDataPacket.setRawdata(returnVo);
        httpDataPacket.setHead("CUS000008");
        return httpDataPacket;
    }

    @Override
    public String packetsVerify(CommonMessageVO vo) {
        if(vo instanceof CustomerVisitsVo){
            CustomerVisitsVo visitsVo = (CustomerVisitsVo)vo;
            String msg = visitsVo.validate();
            return msg;
        }else{
            return "非法报文";
        }
    }
}
