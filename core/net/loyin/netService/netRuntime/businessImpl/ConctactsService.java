package net.loyin.netService.netRuntime.businessImpl;

import com.jfinal.plugin.activerecord.Page;
import net.loyin.model.crm.Contacts;
import net.loyin.model.sso.FileBean;
import net.loyin.model.sso.User;
import net.loyin.netService.dataPacket.DataPacket;
import net.loyin.netService.dataPacket.HttpDataPacket;
import net.loyin.netService.domain.CommonHeader;
import net.loyin.netService.domain.CommonMessageVO;
import net.loyin.netService.domain.DataFormats;
import net.loyin.netService.domain.SelectedKeys;
import net.loyin.netService.netRuntime.IMessageRuntime;
import net.loyin.netService.netRuntime.impl.MessageRuntimeImpl;
import net.loyin.netService.vo.customerVo.contactsVo.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chao on 2015/11/25.
 */
public class ConctactsService extends MessageRuntimeImpl implements IMessageRuntime {
    @Override
    public DataPacket run(CommonMessageVO vo) throws Exception {
        ContactsVo contactsVo = (ContactsVo)vo;
        ContactsMainData mainData = contactsVo.getMaindata();
        Map<String,String> userMap = ((HttpDataPacket) this.getDataPacket()).getCookie();
        String companyId = userMap.get("company_id");
        ContactsReturnMainData contactsReturnMainData = null;
        switch (mainData.getContactsType()){
            case "0":
                contactsReturnMainData = getInternal(mainData,companyId);
                contactsReturnMainData.setContactsType("0");
                break;
            case "1":
                contactsReturnMainData = getExternal(mainData,companyId);
                contactsReturnMainData.setContactsType("1");
                break;
        }
        ConctactsReturnVo conctactsReturnVo = new ConctactsReturnVo();
        conctactsReturnVo.setMainData(contactsReturnMainData);
        CommonHeader messageHeader = contactsVo.getMessageHeader();
        messageHeader.setRsp_no("SUCCESS");
        messageHeader.setRsp_msg("请求成功");
        messageHeader.setBusinessCode("CUS000004");
        conctactsReturnVo.setMessageHeader(messageHeader);
        DataPacket httpDataPacket = this.getDataPacket().GetBackBaseDataPacket();
        httpDataPacket.setSelectedKey(SelectedKeys.WRITE);
        httpDataPacket.setRawDataFormat(DataFormats.OBJECT);
        httpDataPacket.setDataFormat(DataFormats.JSON);
        httpDataPacket.setRawdata(conctactsReturnVo);
        httpDataPacket.setHead("CUS000004");
        return httpDataPacket;


    }

    /**
     * 获取内部联系人
     * @param mainData
     * @return
     */
    private ContactsReturnMainData getInternal(ContactsMainData mainData,String companyId){
        Page<User> list = User.dao.getUserBySearchName(Integer.parseInt(mainData.getPageSize()), Integer.parseInt(mainData.getPageNum()), mainData.getContactsKey(), companyId);
        List<User> pageList = list.getList();
        ContactsReturnMainData contactsReturnMainData = new ContactsReturnMainData();
        List<ContactsReturnInfo> infoList = new ArrayList<>();
        ContactsReturnInfo info  = null;
        for(User u : pageList){
            String phone = "";
            if (u.get("mobile") != null && !u.getStr("mobile").isEmpty()){
                phone += u.getStr("mobile");
            }
            phone = phone.isEmpty()?"":phone+"?";
            if (u.get("telephone") != null && !u.getStr("telephone").isEmpty()){
                phone += u.getStr("telephone");
            }
            if(phone.isEmpty()){
                continue;
            }else{
                if(phone.indexOf("?") != phone.length()){
                    phone = phone.replaceAll("\\?","");
                }
            }
            info = new ContactsReturnInfo();
            info.setContactsName(u.getStr("name"));
            info.setContactsPic(u.getStr("head_pic")); //此参数有可能会更改，暂时先放置
            info.setContactsPhone(phone);
            infoList.add(info);
        }
        contactsReturnMainData.setContactsInfo(infoList);
        return contactsReturnMainData;
    }

    /**
     * 获取网店联系人（仅存在联系人的客户）
     * @param mainData
     * @return
     */
    private ContactsReturnMainData getExternal(ContactsMainData mainData,String companyId){
        Page<User> list = User.dao.getUserBySearchName(Integer.parseInt(mainData.getPageSize()), Integer.parseInt(mainData.getPageNum()), mainData.getContactsKey(), companyId);
        Map<String, Object> filter = new HashMap<>();
        filter.put("keyword",mainData.getContactsKey());
        filter.put("company_id", companyId);
        Page<Contacts> pages = Contacts.dao.pageGrid(Integer.parseInt(mainData.getPageNum()), Integer.parseInt(mainData.getPageSize()), filter);
        List<Contacts> pageList = pages.getList();
        ContactsReturnMainData contactsReturnMainData = new ContactsReturnMainData();
        List<ContactsReturnInfo> infoList = new ArrayList<>();
        ContactsReturnInfo info  = null;
        for(Contacts c : pageList){
            String phone = "";
            if (c.get("mobile") != null && !c.getStr("mobile").isEmpty()){
                phone += c.getStr("mobile");
            }
            phone = phone.isEmpty()?"":phone+"?";
            if (c.get("telephone")!=null && !c.getStr("telephone").isEmpty()){
                phone += c.getStr("telephone");
            }
            if(phone.isEmpty()){
                continue;
            }else{
                if(phone.indexOf("?") != phone.length()){
                    phone = phone.replaceAll("\\?","");
                }
            }
            info = new ContactsReturnInfo();
            info.setContactsName(c.getStr("name"));
            FileBean fileBean = FileBean.dao.findById(c.getStr("customer_id"));
            info.setContactsPic(fileBean==null?"":fileBean.getStr("id")); //此参数有可能会更改，暂时先放置
            info.setContactsPhone(phone);
            infoList.add(info);
        }
        contactsReturnMainData.setContactsInfo(infoList);
        return contactsReturnMainData;
    }


    @Override
    public String packetsVerify(CommonMessageVO vo) {
        if(vo instanceof ContactsVo){
            ContactsVo contactsVo = (ContactsVo)vo;
            String msg = contactsVo.validate();
            return msg;
        }else{
            return "非法报文";
        }
    }
}
