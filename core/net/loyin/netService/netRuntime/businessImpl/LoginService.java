package net.loyin.netService.netRuntime.businessImpl;

import com.alibaba.fastjson.JSON;
import net.loyin.model.sso.Company;
import net.loyin.model.sso.User;
import net.loyin.netService.dataPacket.DataPacket;
import net.loyin.netService.dataPacket.HttpDataPacket;
import net.loyin.netService.domain.CommonHeader;
import net.loyin.netService.domain.CommonMessageVO;
import net.loyin.netService.domain.DataFormats;
import net.loyin.netService.domain.SelectedKeys;
import net.loyin.netService.netRuntime.IMessageRuntime;
import net.loyin.netService.netRuntime.impl.MessageRuntimeImpl;
import net.loyin.netService.vo.fileVo.FileSaveVo;
import net.loyin.netService.vo.sysVo.loginVo.LoginMainData;
import net.loyin.netService.vo.sysVo.loginVo.LoginReturnMainData;
import net.loyin.netService.vo.sysVo.loginVo.LoginReturnVo;
import net.loyin.netService.vo.sysVo.loginVo.LoginVo;
import net.loyin.util.PropertiesContent;
import net.loyin.util.safe.CipherUtil;
import net.loyin.util.safe.MD5;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chao on 2015/12/7.
 */
public class LoginService extends MessageRuntimeImpl implements IMessageRuntime {
    @Override
    public DataPacket run(CommonMessageVO vo) throws Exception {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        LoginVo loginVo = (LoginVo) vo;
        LoginMainData mainData = loginVo.getMaindata();
        String companyName = mainData.getCompanyName();
        Company company = Company.dao.qryCompanyByName(companyName);
        LoginReturnVo returnVo = new LoginReturnVo();
        if (company == null) {
            CommonHeader messageHeader = loginVo.getMessageHeader();
            messageHeader.setRsp_no("FAILURE");
            messageHeader.setRsp_msg("公司名称不存在");
            messageHeader.setBusinessCode("SYS000002");
            returnVo.setMessageHeader(messageHeader);
            returnVo.setMaindata(new LoginReturnMainData());
        }else{
            String pwd = MD5.getMD5ofStr(mainData.getPassWord());
            String name = mainData.getLoginName();
            User m = User.dao.login(name, pwd, company.getStr("id"));
            if (m != null) {
                String uid = m.getStr("id");
                String ip = ((HttpDataPacket)this.getDataPacket()).getIp();

                int status = m.getInt("status");
                if (status == 0) {
                    CommonHeader messageHeader = loginVo.getMessageHeader();
                    messageHeader.setRsp_no("FAILURE");
                    messageHeader.setRsp_msg("此用户被禁用，请联系公司管理员！");
                    messageHeader.setBusinessCode("SYS000002");
                    returnVo.setMessageHeader(messageHeader);
                }
                String nowStr = dateTimeFormat.format(new Date());
                m.upLogin(m.getStr("id"), ip, nowStr);

                Map<String, Object> userMap = new HashMap<String, Object>();
                userMap.put("loginTime", nowStr);
                userMap.put("ip", ip);
                userMap.put("uid", uid);
                userMap.put("company_id", company.getStr("id"));
                userMap.put("company_name", company.getStr("name"));
                userMap.put("position_id", m.get("position_id") == null ? "" : m.get("position_id"));
                userMap.put("position_name", m.get("position_name") == null ? "" : m.get("position_name"));
                userMap.put("department_id", m.get("department_id") == null ? "" : m.get("department_id"));
                userMap.put("department_name", m.get("department_name") == null ? "" : m.get("department_name"));
                String cookie =  CipherUtil.encryptData(JSON.toJSONString(userMap));
                LoginReturnMainData returnMainData = new LoginReturnMainData();
                returnMainData.setUserPic(m.getStr("head_pic")==null?"":m.getStr("head_pic"));
                returnMainData.setUserCookie(cookie);
                returnVo.setMaindata(returnMainData);
                CommonHeader messageHeader = loginVo.getMessageHeader();
                messageHeader.setRsp_no("SUCCESS");
                messageHeader.setRsp_msg("");
                messageHeader.setBusinessCode("SYS000002");
                returnVo.setMessageHeader(messageHeader);
            }else{
                CommonHeader messageHeader = loginVo.getMessageHeader();
                messageHeader.setRsp_no("FAILURE");
                messageHeader.setRsp_msg("用户/密码不正确");
                messageHeader.setBusinessCode("SYS000002");
                returnVo.setMessageHeader(messageHeader);
                returnVo.setMaindata(new LoginReturnMainData());
            }
        }
        DataPacket httpDataPacket = this.getDataPacket().GetBackBaseDataPacket();
        httpDataPacket.setSelectedKey(SelectedKeys.WRITE);
        httpDataPacket.setRawDataFormat(DataFormats.OBJECT);
        httpDataPacket.setDataFormat(DataFormats.JSON);
        httpDataPacket.setRawdata(returnVo);
        httpDataPacket.setHead("SYS000002");
        return httpDataPacket;
    }

    @Override
    public String packetsVerify(CommonMessageVO vo) {
        if(vo instanceof LoginVo){
            LoginVo loginVo = (LoginVo)vo;
            String msg = loginVo.validate();
            return msg;
        }else{
            return "非法报文";
        }
    }
}
