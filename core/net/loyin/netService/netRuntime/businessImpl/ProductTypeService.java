package net.loyin.netService.netRuntime.businessImpl;

import com.jfinal.plugin.activerecord.Record;
import net.loyin.model.sso.Parame;
import net.loyin.netService.dataPacket.DataPacket;
import net.loyin.netService.dataPacket.HttpDataPacket;
import net.loyin.netService.domain.CommonHeader;
import net.loyin.netService.domain.CommonMessageVO;
import net.loyin.netService.domain.DataFormats;
import net.loyin.netService.domain.SelectedKeys;
import net.loyin.netService.netRuntime.IMessageRuntime;
import net.loyin.netService.netRuntime.impl.MessageRuntimeImpl;
import net.loyin.netService.vo.sysVo.parameVo.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chao on 2015/12/16.
 * 获取系统参数（除文字参数外）
 */
public class ProductTypeService extends MessageRuntimeImpl implements IMessageRuntime {

    @Override
    public DataPacket run(CommonMessageVO vo) throws Exception {
        ParameVo parameVo = (ParameVo)vo;
        Map<String,String> userMap = ((HttpDataPacket) this.getDataPacket()).getCookie();
        CommonHeader messageHeader = parameVo.getMessageHeader();
        ParameReturnVo returnVo = new ParameReturnVo();
        if(userMap == null){
            messageHeader.setRsp_no("FAILURE");
            messageHeader.setRsp_msg("无法获取企业ID");
            messageHeader.setBusinessCode("SYS000012");
            returnVo.setMessageHeader(messageHeader);
            returnVo.setMaindata(new ParameReturnMainData());
        }else{
            String companyId = userMap.get("company_id");
            ParameMainData mainData = parameVo.getMaindata();
            String parameType = mainData.getParameType();
            List<Record> qryList = Parame.dao.qryList(companyId, Integer.parseInt(parameVo.getMaindata().getParameType()));
            List<ParameReturnInfo> infos = new ArrayList<>();
            switch (parameType){
                case "0":
                    infos = this.merchBrandMainList(companyId,mainData.getSearch(),mainData.getMerchType(),mainData.getSpecification(),mainData.getBrand());
                    break;
                case "1":
                    infos = this.merchStanderMainList(companyId,mainData.getSearch(),mainData.getMerchType(),mainData.getSpecification(),mainData.getBrand());
                    break;
            }
            if(infos.size()==0){
                infos.add(new ParameReturnInfo());
            }
            messageHeader.setRsp_no("SUCCESS");
            messageHeader.setRsp_msg("");
            messageHeader.setBusinessCode("SYS000012");
            ParameReturnMainData returnMainData = new ParameReturnMainData();
            returnMainData.setParameInfos(infos);
            returnVo.setMaindata(returnMainData);
            returnVo.setMessageHeader(messageHeader);
        }
        DataPacket httpDataPacket = this.getDataPacket().GetBackBaseDataPacket();
        httpDataPacket.setSelectedKey(SelectedKeys.WRITE);
        httpDataPacket.setRawDataFormat(DataFormats.OBJECT);
        httpDataPacket.setDataFormat(DataFormats.JSON);
        httpDataPacket.setRawdata(returnVo);
        httpDataPacket.setHead("SYS000012");
        return httpDataPacket;
    }

    /**
     * by chenjianhui
     * 2015年9月23日14:29:28
     * 获取商品全部品牌
     */
    public List<ParameReturnInfo> merchBrandMainList(String companyId,String searchContent,String merchType,String merchStand,String merchBrand) {
        List<ParameReturnInfo> infos = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("searchContent", searchContent);
        map.put("merchType", merchType);
        map.put("merchStand", merchStand);
        map.put("merchBrand", merchBrand);
        List<Record> parameList = Parame.dao.qryListProductInfoByOrder(companyId, true, false, map);
        ParameReturnInfo info = new ParameReturnInfo();
        info.setParameName("");
        infos.add(info);
        for (Record r : parameList) {
            info = new ParameReturnInfo();
            info.setParameName(r.getStr("merch_brand")==null?"":r.getStr("merch_brand"));
            infos.add(info);
        }
        return infos;
    }


    /**
     * 获取规格分类信息
     */
    public List<ParameReturnInfo> merchStanderMainList(String companyId,String searchContent,String merchType,String merchStand,String merchBrand) {
        List<ParameReturnInfo> infos = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("searchContent", searchContent);
        map.put("merchType", merchType);
        map.put("searchBrand", merchBrand);
        map.put("merchStander", merchStand);
        List<Record> parameList = Parame.dao.qryListProductInfoByOrder(companyId, false, false, map);
        ParameReturnInfo info = new ParameReturnInfo();
        info.setParameName("");
        infos.add(info);
        for (Record r : parameList) {
            info = new ParameReturnInfo();
            info.setParameName(r.getStr("merch_stander")==null?"":r.getStr("merch_stander"));
            infos.add(info);
        }
        return infos;

    }


    @Override
    public String packetsVerify(CommonMessageVO vo) {
        if(vo instanceof ParameVo){
            ParameVo parameVo = (ParameVo)vo;
            String msg = parameVo.validate();
            return msg;
        }else{
            return "非法报文";
        }
    }
}
