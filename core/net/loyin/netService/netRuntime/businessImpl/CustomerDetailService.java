package net.loyin.netService.netRuntime.businessImpl;

import net.loyin.model.crm.Contacts;
import net.loyin.model.crm.Customer;
import net.loyin.model.sso.FileBean;
import net.loyin.model.sso.Parame;
import net.loyin.netService.dataPacket.DataPacket;
import net.loyin.netService.dataPacket.HttpDataPacket;
import net.loyin.netService.domain.CommonHeader;
import net.loyin.netService.domain.CommonMessageVO;
import net.loyin.netService.domain.DataFormats;
import net.loyin.netService.domain.SelectedKeys;
import net.loyin.netService.netRuntime.IMessageRuntime;
import net.loyin.netService.netRuntime.impl.MessageRuntimeImpl;
import net.loyin.netService.vo.customerVo.customerDetailVo.CustomerDetailMainData;
import net.loyin.netService.vo.customerVo.customerDetailVo.CustomerDetailReturnMainData;
import net.loyin.netService.vo.customerVo.customerDetailVo.CustomerDetailReturnVo;
import net.loyin.netService.vo.customerVo.customerDetailVo.CustomerDetailVo;

import java.util.List;
import java.util.Map;

/**
 * Created by Chao on 2015/11/25.
 */
public class CustomerDetailService extends MessageRuntimeImpl implements IMessageRuntime {
    @Override
    public DataPacket run(CommonMessageVO vo) throws Exception {
        CustomerDetailVo detailVo = (CustomerDetailVo)vo;
        CustomerDetailMainData mainData = detailVo.getMaindata();
        Map<String,String> userMap = ((HttpDataPacket) this.getDataPacket()).getCookie();
        String customerId = mainData.getCustomerId();
        Customer customer = Customer.dao.findById(customerId, userMap.get("company_id"));
        CustomerDetailReturnMainData returnMainData = new CustomerDetailReturnMainData();
        String industry = customer.getStr("industry");
        if(industry!=null && !industry.isEmpty()){
            Parame parame = Parame.dao.findById(industry,userMap.get("company_id"));
            if(parame != null){
                returnMainData.setCustomerType(parame.getStr("name"));
            }else{
                returnMainData.setCustomerType("");
            }
        }
        returnMainData.setCustomerType(returnMainData.getCustomerType() == null ? "":returnMainData.getCustomerType());
        returnMainData.setCustomerAddress(customer.getStr("address"));
        returnMainData.setCustomerName(customer.getStr("name"));
        returnMainData.setCustomerLng(customer.getStr("lng"));
        returnMainData.setCustomerLat(customer.getStr("lat"));
        returnMainData.setContactsFax(customer.getStr("fax"));
        returnMainData.setContactsPhone(customer.getStr("telephone") + "?" + customer.getStr("mobile"));
        Contacts contacts = Contacts.dao.findFirstByCustomerId(customerId);
        if(contacts != null){
            returnMainData.setCustomerContacts(contacts.getStr("name"));
        }else{
            returnMainData.setCustomerContacts("未知");
        }
        List<FileBean> fileBeans = FileBean.dao.findList(customerId);
        String fileId = "";
        for(FileBean f : fileBeans){
            fileId += f.getStr("id")+"?";
        }
        fileId +="-";
        returnMainData.setCustomerPic(fileId);
        CustomerDetailReturnVo returnVo = new CustomerDetailReturnVo();
        returnVo.setMaindata(returnMainData);
        CommonHeader messageHeader = detailVo.getMessageHeader();
        messageHeader.setRsp_no("SUCCESS");
        messageHeader.setRsp_msg("请求成功");
        messageHeader.setBusinessCode("CUS000006");
        returnVo.setMessageHeader(messageHeader);
        DataPacket httpDataPacket = this.getDataPacket().GetBackBaseDataPacket();
        httpDataPacket.setSelectedKey(SelectedKeys.WRITE);
        httpDataPacket.setRawDataFormat(DataFormats.OBJECT);
        httpDataPacket.setDataFormat(DataFormats.JSON);
        httpDataPacket.setRawdata(returnVo);
        httpDataPacket.setHead("CUS000006");
        return httpDataPacket;


    }


    @Override
    public String packetsVerify(CommonMessageVO vo) {
        if(vo instanceof CustomerDetailVo){
            CustomerDetailVo detailVo = (CustomerDetailVo)vo;
            String msg = detailVo.validate();
            return msg;
        }else{
            return "非法报文";
        }
    }
}
