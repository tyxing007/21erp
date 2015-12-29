package net.loyin.netService.netRuntime.businessImpl;

import com.jfinal.plugin.activerecord.Page;
import net.loyin.model.crm.Customer;
import net.loyin.netService.dataPacket.DataPacket;
import net.loyin.netService.dataPacket.HttpDataPacket;
import net.loyin.netService.domain.CommonHeader;
import net.loyin.netService.domain.CommonMessageVO;
import net.loyin.netService.domain.DataFormats;
import net.loyin.netService.domain.SelectedKeys;
import net.loyin.netService.netRuntime.IMessageRuntime;
import net.loyin.netService.netRuntime.impl.MessageRuntimeImpl;
import net.loyin.netService.vo.customerVo.contactsVo.ContactsMainData;
import net.loyin.netService.vo.customerVo.contactsVo.ContactsVo;
import net.loyin.netService.vo.customerVo.customerInfoVo.*;
import net.loyin.netService.vo.orderVo.orderInfoVo.OrderVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chao on 2015/11/25.
 */
public class CustomerInfoService extends MessageRuntimeImpl implements IMessageRuntime {
    @Override
    public DataPacket run(CommonMessageVO vo) throws Exception {

        CustomerVo customerVo = (CustomerVo) vo;
        CustomerMainData mainData = customerVo.getMaindata();
        Map<String, String> userMap = ((HttpDataPacket) this.getDataPacket()).getCookie();
        String companyId = userMap.get("company_id");
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("keyword", mainData.getKey());
        parameter.put("company_id", companyId);
        parameter.put("user_id", userMap.get("uid"));
        parameter.put("position_id", userMap.get("position_id"));
        parameter.put("status_app", mainData.getStatus());
        parameter.put("classify", mainData.getType());
        parameter.put("sort", mainData.getSort());
        Page<Customer> customerPage = Customer.dao.pageGrid(Integer.parseInt(mainData.getNum()), Integer.parseInt(mainData.getSize()), parameter, 11);
        List<Customer> customerList = customerPage.getList();
        CustomerReturnInfo returnInfo = null;
        List<CustomerReturnInfo> infos = new ArrayList<>();
        for (Customer c : customerList) {
            returnInfo = new CustomerReturnInfo();
            returnInfo.setCustomerId(c.getStr("id"));
            returnInfo.setCustomerName(c.getStr("name"));
            returnInfo.setCustomerAddress(c.getStr("address"));
            returnInfo.setCustomerPhone(c.getStr("mobile") + "?" + c.getStr("telephone") + "?" + c.getStr("fax"));
            infos.add(returnInfo);
        }
        CustomerReturnMainData customerReturnMainData = new CustomerReturnMainData();
        CustomerReturnInfoList infoList =new CustomerReturnInfoList();
        infoList.setReturnInfos(infos);
        customerReturnMainData.setReturnInfoList(infoList);
        CustomerReturnVo returnVo = new CustomerReturnVo();
        returnVo.setMaindata(customerReturnMainData);
        CommonHeader messageHeader = customerVo.getMessageHeader();
        messageHeader.setRsp_no("SUCCESS");
        messageHeader.setRsp_msg("请求成功");
        messageHeader.setBusinessCode("CUS000002");
        returnVo.setMessageHeader(messageHeader);
        DataPacket httpDataPacket = this.getDataPacket().GetBackBaseDataPacket();
        httpDataPacket.setSelectedKey(SelectedKeys.WRITE);
        httpDataPacket.setRawDataFormat(DataFormats.OBJECT);
        httpDataPacket.setDataFormat(DataFormats.JSON);
        httpDataPacket.setRawdata(returnVo);
        httpDataPacket.setHead("CUS000002");
        return httpDataPacket;
    }

    @Override
    public String packetsVerify(CommonMessageVO vo) {
        if (vo instanceof CustomerVo) {
            CustomerVo customerVo = (CustomerVo) vo;
            String msg = customerVo.validate();
            return msg;
        } else {
            return "非法报文";
        }
    }
}
