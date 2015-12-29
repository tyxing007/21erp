package net.loyin.netService.netRuntime.businessImpl;

import net.loyin.model.crm.Contacts;
import net.loyin.model.crm.Customer;
import net.loyin.model.crm.CustomerData;
import net.loyin.model.crm.CustomerRecord;
import net.loyin.model.sso.FileBean;
import net.loyin.model.sso.Parame;
import net.loyin.model.sso.SnCreater;
import net.loyin.netService.dataPacket.DataPacket;
import net.loyin.netService.dataPacket.HttpDataPacket;
import net.loyin.netService.domain.CommonHeader;
import net.loyin.netService.domain.CommonMessageVO;
import net.loyin.netService.domain.DataFormats;
import net.loyin.netService.domain.SelectedKeys;
import net.loyin.netService.netRuntime.IMessageRuntime;
import net.loyin.netService.netRuntime.impl.MessageRuntimeImpl;
import net.loyin.netService.vo.customerVo.customerAddVo.CustomerAddMainData;
import net.loyin.netService.vo.customerVo.customerAddVo.CustomerAddReturnMainData;
import net.loyin.netService.vo.customerVo.customerAddVo.CustomerAddReturnVo;
import net.loyin.netService.vo.customerVo.customerAddVo.CustomerAddVo;
import net.loyin.netService.vo.customerVo.customerDetailVo.CustomerDetailMainData;
import net.loyin.netService.vo.customerVo.customerDetailVo.CustomerDetailReturnMainData;
import net.loyin.netService.vo.customerVo.customerDetailVo.CustomerDetailReturnVo;
import net.loyin.netService.vo.customerVo.customerDetailVo.CustomerDetailVo;
import net.loyin.netService.vo.customerVo.customerInfoVo.CustomerReturnVo;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chao on 2015/11/25.
 */
public class CustomerAdddService extends MessageRuntimeImpl implements IMessageRuntime {
    @Override
    public DataPacket run(CommonMessageVO vo) throws Exception {
        CustomerAddVo addVo = (CustomerAddVo) vo;
        CustomerAddMainData mainData = addVo.getMaindata();
        Map<String, String> userMap = ((HttpDataPacket) this.getDataPacket()).getCookie();
        if(userMap == null){
            CustomerAddReturnMainData returnMainData = new CustomerAddReturnMainData();
            CustomerAddReturnVo returnVo = new CustomerAddReturnVo();
            returnVo.setMaindata(returnMainData);
            CommonHeader messageHeader = addVo.getMessageHeader();
            messageHeader.setRsp_no("FAILURE");
            messageHeader.setRsp_msg("您未登陆，请登陆");
            messageHeader.setBusinessCode("CUS000010");
            returnVo.setMessageHeader(messageHeader);
            DataPacket httpDataPacket = this.getDataPacket().GetBackBaseDataPacket();
            httpDataPacket.setSelectedKey(SelectedKeys.WRITE);
            httpDataPacket.setRawDataFormat(DataFormats.OBJECT);
            httpDataPacket.setDataFormat(DataFormats.JSON);
            httpDataPacket.setRawdata(returnVo);
            httpDataPacket.setHead("CUS000010");
            return httpDataPacket;
        }
        String customerId = saveCustomer(mainData, userMap);
        if(customerId.equals("该客户已存在")){
            CustomerAddReturnMainData returnMainData = new CustomerAddReturnMainData();
            returnMainData.setCustomerId(customerId);
            CustomerAddReturnVo returnVo = new CustomerAddReturnVo();
            returnVo.setMaindata(returnMainData);
            CommonHeader messageHeader = addVo.getMessageHeader();
            messageHeader.setRsp_no("FAILURE");
            messageHeader.setRsp_msg("网店名称已存在,请重新核对");
            messageHeader.setBusinessCode("CUS000010");
            returnVo.setMessageHeader(messageHeader);
            DataPacket httpDataPacket = this.getDataPacket().GetBackBaseDataPacket();
            httpDataPacket.setSelectedKey(SelectedKeys.WRITE);
            httpDataPacket.setRawDataFormat(DataFormats.OBJECT);
            httpDataPacket.setDataFormat(DataFormats.JSON);
            httpDataPacket.setRawdata(returnVo);
            httpDataPacket.setHead("CUS000010");
            return httpDataPacket;
        }
        CustomerAddReturnMainData returnMainData = new CustomerAddReturnMainData();
        returnMainData.setCustomerId(customerId);
        CustomerAddReturnVo returnVo = new CustomerAddReturnVo();
        returnVo.setMaindata(returnMainData);
        CommonHeader messageHeader = addVo.getMessageHeader();
        messageHeader.setRsp_no("SUCCESS");
        messageHeader.setRsp_msg("请求成功");
        messageHeader.setBusinessCode("CUS000010");
        returnVo.setMessageHeader(messageHeader);
        DataPacket httpDataPacket = this.getDataPacket().GetBackBaseDataPacket();
        httpDataPacket.setSelectedKey(SelectedKeys.WRITE);
        httpDataPacket.setRawDataFormat(DataFormats.OBJECT);
        httpDataPacket.setDataFormat(DataFormats.JSON);
        httpDataPacket.setRawdata(returnVo);
        httpDataPacket.setHead("CUS000010");
        return httpDataPacket;


    }

    private String saveCustomer(CustomerAddMainData c, Map<String, String> m) {
        if (Customer.dao.existCust(c.getCustomerName(), "", 2, m.get("company_id"))) {
            return "该客户已存在";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Customer po = new Customer();
//        company_id
        String uid = m.get("uid");
        po.set("company_id", m.get("company_id"));
        po.set("type", 2);
        po.set("head_id", uid);
        po.set("creater_id", uid);
        po.set("name", c.getCustomerName());
        po.set("address", c.getCustomerAddress());
        po.set("industry", c.getCustomerType()==null?"":c.getCustomerType());
        po.set("create_datetime", simpleDateFormat.format(new Date()));
        po.set("is_deleted", 0);
        po.set("fax", c.getContactsFax());
        po.set("telephone", c.getContactsPhone());
        po.set("address", c.getCustomerAddress());
        po.set("sn", SnCreater.dao.create("CUSTOMER", m.get("company_id")));
        po.set("integral", 0);
        po.set("status", 0);
        po.set("lat", c.getCustomerLat());
        po.set("lng", c.getCustomerLng());
        po.save();

        String customerId = po.getStr("id");
        CustomerRecord cr = new CustomerRecord();
        cr.set("create_datetime", simpleDateFormat.format(new Date()));
        cr.set("customer_id", customerId);
        cr.set("user_id", uid);
        cr.set("type", 0);
        cr.save();

        Contacts contacts = new Contacts();
        contacts.set("create_datetime", simpleDateFormat.format(new Date()));
        contacts.set("customer_id",customerId);
        contacts.set("creater_id",uid);
        contacts.set("name",c.getCustomerContacts());
        contacts.set("sex",1);
        contacts.set("ismain",1);
        contacts.set("company_id", m.get("company_id"));
        contacts.set("telephone", c.getContactsPhone());
        contacts.set("mobile", c.getContactsPhone());
        contacts.set("fax", c.getContactsFax());
        contacts.save();
        return customerId;

    }


    @Override
    public String packetsVerify(CommonMessageVO vo) {
        if (vo instanceof CustomerAddVo) {
            CustomerAddVo addVo = (CustomerAddVo) vo;
            String msg = addVo.validate();
            return msg;
        } else {
            return "非法报文";
        }
    }
}
