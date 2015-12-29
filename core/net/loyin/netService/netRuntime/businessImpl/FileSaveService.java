package net.loyin.netService.netRuntime.businessImpl;

import net.loyin.model.sso.FileBean;
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
import net.loyin.netService.vo.fileVo.FileSaveMainData;
import net.loyin.netService.vo.fileVo.FileSaveVo;
import sun.net.www.MessageHeader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Chao on 2015/11/30.
 */
public class FileSaveService extends MessageRuntimeImpl implements IMessageRuntime {
    @Override
    public DataPacket run(CommonMessageVO vo) throws Exception {
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        FileSaveVo fileSaveVo = (FileSaveVo)vo;
        FileSaveMainData mainData = fileSaveVo.getMaindata();
        Map<String,String> userMap = ((HttpDataPacket) this.getDataPacket()).getCookie();
        Map<String,Object> map = new HashMap<>();
        map.put("name",mainData.getFileName());
        map.put("creater_id",userMap.get("uid"));
        map.put("file_path",mainData.getFilePath());
        map.put("create_datetime",s.format(new Date()));
        map.put("fsize",mainData.getFsize());
        map.put("save_path",mainData.getSavePath());
        map.put("relation_id",mainData.getRelationId());
        map.put("id",mainData.getFileId());
        FileBean.dao.saveFile(map);
        FileSaveVo returnVo = new FileSaveVo();
        returnVo.setMaindata(mainData);
        CommonHeader messageHeader = fileSaveVo.getMessageHeader();
        messageHeader.setRsp_no("SUCCESS");
        messageHeader.setRsp_msg("请求成功");
        messageHeader.setBusinessCode("FILE00002");
        returnVo.setMessageHeader(messageHeader);
        DataPacket httpDataPacket = this.getDataPacket().GetBackBaseDataPacket();
        httpDataPacket.setSelectedKey(SelectedKeys.WRITE);
        httpDataPacket.setRawDataFormat(DataFormats.OBJECT);
        httpDataPacket.setDataFormat(DataFormats.JSON);
        httpDataPacket.setRawdata(returnVo);
        httpDataPacket.setHead("FILE00002");
        return httpDataPacket;
    }

    @Override
    public String packetsVerify(CommonMessageVO vo) {
        if(vo instanceof FileSaveVo){
            FileSaveVo fileSaveVo = (FileSaveVo)vo;
            String msg = fileSaveVo.validate();
            return msg;
        }else{
            return "非法报文";
        }
    }
}
