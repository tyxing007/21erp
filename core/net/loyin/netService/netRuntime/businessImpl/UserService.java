package net.loyin.netService.netRuntime.businessImpl;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import net.loyin.model.sso.User;
import net.loyin.netService.dataPacket.DataPacket;
import net.loyin.netService.dataPacket.HttpDataPacket;
import net.loyin.netService.domain.CommonHeader;
import net.loyin.netService.domain.CommonMessageVO;
import net.loyin.netService.domain.DataFormats;
import net.loyin.netService.domain.SelectedKeys;
import net.loyin.netService.netRuntime.IMessageRuntime;
import net.loyin.netService.netRuntime.impl.MessageRuntimeImpl;
import net.loyin.netService.vo.productVo.ProductRenturnVo;
import net.loyin.netService.vo.productVo.ProductReturnMainData;
import net.loyin.netService.vo.productVo.ProductVo;
import net.loyin.netService.vo.sysVo.userVo.UserReturnInfo;
import net.loyin.netService.vo.sysVo.userVo.UserReturnMainData;
import net.loyin.netService.vo.sysVo.userVo.UserReturnVo;
import net.loyin.netService.vo.sysVo.userVo.UserVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Chao on 2015/12/15.
 */
public class UserService extends MessageRuntimeImpl implements IMessageRuntime {
    @Override
    public DataPacket run(CommonMessageVO vo) throws Exception {
        UserVo userVo = (UserVo)vo;
        Map<String,String> userMap = ((HttpDataPacket) this.getDataPacket()).getCookie();
        CommonHeader messageHeader = userVo.getMessageHeader();
        UserReturnVo returnVo = new UserReturnVo();
        if(userMap == null){
            messageHeader.setRsp_no("FAILURE");
            messageHeader.setRsp_msg("无法获取企业ID");
            messageHeader.setBusinessCode("SYS000008");
            returnVo.setMessageHeader(messageHeader);
            returnVo.setMaindata(new UserReturnMainData());
        }else{
            UserReturnMainData returnMainData = new UserReturnMainData();
            String uid = userMap.get("uid");
            String positionId = userMap.get("position_id");
            String companyId = userMap.get("company_id");
            List<UserReturnInfo> infos = new ArrayList<>();
            UserReturnInfo info = new UserReturnInfo();
            List<Record> records =  User.dao.list4tree(positionId, companyId, 1);
            if(records != null && records.size()!=0){
                StringBuffer buffer = new StringBuffer();
                List<Object> param = new ArrayList<>();
                buffer.append(" and u.position_id in ( ");
                for(Record r : records){
                    if(!r.getStr("id").equals(positionId)){
                        buffer.append(" ? ,");
                        param.add(r.getStr("id"));
                    }
                }
                buffer.append("'-' )");
                Page<User> list = User.dao.pageGrid(1,9999,buffer,param);
                List<User> users = list.getList();
                for(User u : users){
                info = new UserReturnInfo();
                info.setUserId(u.getStr("id"));
                info.setUserName(u.getStr("realname"));
                infos.add(info);
                }

            }

            if(infos.size()==0){
                infos.add(info);
            }
            returnMainData.setUserReturnInfos(infos);
            returnVo.setMaindata(returnMainData);
        }
        DataPacket httpDataPacket = this.getDataPacket().GetBackBaseDataPacket();
        httpDataPacket.setSelectedKey(SelectedKeys.WRITE);
        httpDataPacket.setRawDataFormat(DataFormats.OBJECT);
        httpDataPacket.setDataFormat(DataFormats.JSON);
        httpDataPacket.setRawdata(returnVo);
        httpDataPacket.setHead("SYS000008");
        return httpDataPacket;
    }

    @Override
    public String packetsVerify(CommonMessageVO vo) {
        if(vo instanceof UserVo){
            UserVo userVo = (UserVo)vo;
            String msg = userVo.validate();
            return msg;
        }else{
            return "非法报文";
        }
    }
}
