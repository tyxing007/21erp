package net.loyin.netService.netRuntime.businessImpl;

import com.jfinal.plugin.activerecord.Page;
import net.loyin.model.crm.Contacts;
import net.loyin.model.crm.Customer;
import net.loyin.model.scm.Comparison;
import net.loyin.model.scm.Product;
import net.loyin.model.scm.Stock;
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
import net.loyin.netService.vo.offlineVo.OfflineCustomerVo.OfflineCustomerInfo;
import net.loyin.netService.vo.offlineVo.OfflineCustomerVo.OfflineCustomerMainData;
import net.loyin.netService.vo.offlineVo.OfflineCustomerVo.OfflineCustomerVo;
import net.loyin.netService.vo.offlineVo.OfflineProductVo.OfflineProductMainData;
import net.loyin.netService.vo.offlineVo.OfflineProductVo.OfflineProductVo;
import net.loyin.netService.vo.offlineVo.OfflineProductVo.ProductInfo;
import net.loyin.netService.vo.offlineVo.offlineVo.OfflineMainData;
import net.loyin.netService.vo.offlineVo.offlineVo.OfflineVo;
import net.loyin.netService.vo.orderVo.orderInfoVo.OrderVo;
import net.loyin.netService.vo.productVo.ProductUnit;
import net.loyin.netService.vo.productVo.ProductUnitInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chao on 2015/12/10.
 */
public class OfflineService extends MessageRuntimeImpl implements IMessageRuntime {


    @Override
    public DataPacket run(CommonMessageVO vo) throws Exception {
        OfflineVo offlineVo = (OfflineVo) vo;
        OfflineMainData mainData = offlineVo.getMaindata();
        Map<String, String> userMap = ((HttpDataPacket) this.getDataPacket()).getCookie();
        CommonHeader messageHeader = offlineVo.getMessageHeader();
        if (userMap == null) {
            mainData.setException(true);
        }else{
            mainData.setException(false);
        }
        DataPacket httpDataPacket = this.getDataPacket().GetBackBaseDataPacket();
        switch (mainData.getUpdateType()) {
            case "0":
                OfflineCustomerVo customerVo = null;
                try {
                    if(mainData.isException()){
                        throw new Exception("您未登陆，请登陆");
                    }
                    customerVo = this.getCustomer(userMap,mainData);

                } catch (Exception e) {
                    messageHeader.setRsp_no("FAILURE");
                    messageHeader.setRsp_msg("请求失败，原因为" + e.getMessage());
                    messageHeader.setBusinessCode("OFFLINE02");
                    httpDataPacket.setRawdata(customerVo);
                    httpDataPacket.setHead("OFFLINE02");
                    httpDataPacket.setSelectedKey(SelectedKeys.WRITE);
                    httpDataPacket.setRawDataFormat(DataFormats.OBJECT);
                    httpDataPacket.setDataFormat(DataFormats.JSON);
                    return httpDataPacket;
                }
                messageHeader.setRsp_no("SUCCESS");
                messageHeader.setRsp_msg("请求成功");
                messageHeader.setBusinessCode("OFFLINE02");
                customerVo.setMessageHeader(messageHeader);
                httpDataPacket.setRawdata(customerVo);
                httpDataPacket.setHead("OFFLINE02");
                break;
            case "1":
                OfflineProductVo productVo = null;
                try {
                    if(mainData.isException()){
                        throw new Exception("您未登陆，请登陆");
                    }
                    productVo = this.getProduct(userMap,mainData);
                } catch (Exception e) {
                    messageHeader.setRsp_no("FAILURE");
                    messageHeader.setRsp_msg("请求失败，原因为" + e.getMessage());
                    messageHeader.setBusinessCode("OFFLINE03");
                    httpDataPacket.setRawdata(productVo);
                    httpDataPacket.setHead("OFFLINE03");
                    httpDataPacket.setSelectedKey(SelectedKeys.WRITE);
                    httpDataPacket.setRawDataFormat(DataFormats.OBJECT);
                    httpDataPacket.setDataFormat(DataFormats.JSON);
                    return httpDataPacket;
                }
                messageHeader.setRsp_no("SUCCESS");
                messageHeader.setRsp_msg("请求成功");
                messageHeader.setBusinessCode("OFFLINE03");
                productVo.setMessageHeader(messageHeader);
                httpDataPacket.setRawdata(productVo);
                httpDataPacket.setHead("OFFLINE03");
                break;
        }
        httpDataPacket.setSelectedKey(SelectedKeys.WRITE);
        httpDataPacket.setRawDataFormat(DataFormats.OBJECT);
        httpDataPacket.setDataFormat(DataFormats.JSON);
        return httpDataPacket;
    }

    private OfflineCustomerVo getCustomer(Map<String, String> userMap,OfflineMainData mainData) throws Exception {
        OfflineCustomerVo vo = new OfflineCustomerVo();
        OfflineCustomerMainData customerMainData = new OfflineCustomerMainData();
        OfflineCustomerInfo info = new OfflineCustomerInfo();
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("company_id", userMap.get("company_id"));
        parameter.put("user_id", userMap.get("uid"));
        parameter.put("position_id",userMap.get("position_id"));
        Page<Customer> customerPage = Customer.dao.pageGrid(Integer.parseInt(mainData.getIndex()), 1, parameter, 11);
        List<Customer> customerList = customerPage.getList();
        Customer customer = null;
        if(customerList == null || customerList.size() == 0){
            throw new Exception("无对应客户");
        }else{
            customer = customerList.get(0);
        }
        info.setCustomerId(customer.getStr("id"));
        info.setCustomerName(customer.getStr("name"));
        info.setCustomerAddress(customer.getStr("address"));
        info.setCustomerTel(customer.getStr("mobile"));
        info.setCustomerLat(customer.getStr("lat"));
        info.setCustomerLng(customer.getStr("lng"));

        Contacts contacts = Contacts.dao.findFirstByCustomerId(customer.getStr("id"));
        if(contacts != null){
            info.setCustomerContacts(contacts.getStr("name"));
            info.setCustomerContactsPhone(contacts.getStr("mobile"));
        }else{
            info.setCustomerContacts("未知");
            info.setCustomerContactsPhone("未知");
        }

        List<FileBean> fileBeans = FileBean.dao.findList(customer.getStr("id"));
        String fileId = "";
        for(FileBean f : fileBeans) {
            fileId += f.getStr("id")+"?";
        }
        fileId +="-";
        info.setCustomerPic(fileId);
        info.setCustomerStatus("ture");
        info.setCustomerCount(String.valueOf(customerPage.getTotalRow()));
        customerMainData.setCustomerInfo(info);
        vo.setMaindata(customerMainData);
        return vo;
    }

    private OfflineProductVo getProduct(Map<String, String> userMap,OfflineMainData mainData) {
        int index = Integer.parseInt(mainData.getIndex());
        Product p = Product.dao.findByIndex(userMap.get("company_id"), index);
        ProductInfo info  = new ProductInfo();
        info.setProductId(p.getStr("id"));
        info.setProductName(p.getStr("name"));
        info.setProductBillsn(p.getStr("billsn"));
        info.setProductHisPrice(p.get("his_price") == null ? "" : p.getBigDecimal("his_price").toString());
        info.setProductPrice(p.get("sale_price")==null?"":p.getBigDecimal("sale_price").toString());
        info.setProductBrand(p.getStr("brand"));
        info.setProductSpecification(p.getStr("specification"));
        info.setProductCategory(p.getStr("category"));

        ProductUnit productUnit =new ProductUnit();
        List<ProductUnitInfo> unitList = new ArrayList<>();
        ProductUnitInfo unitInfo = new ProductUnitInfo();
        Parame parame = Parame.dao.findById(p.getStr("unit"),userMap.get("company_id"));
        unitInfo.setUnitName(parame == null ? "未知" : parame.getStr("name"));
        unitInfo.setUnitNum("1");
        unitList.add(unitInfo);
        List<Comparison> comparisonList = Comparison.dao.getComparisonByProductId(p.getStr("id"));
        for(Comparison c : comparisonList){
            unitInfo = new ProductUnitInfo();
            unitInfo.setUnitName(c.getStr("comparison_unit"));
            unitInfo.setUnitNum(c.getStr("comparison_num"));
            unitList.add(unitInfo);
        }
        productUnit.setUnitInfos(unitList);
        info.setProductUnit(productUnit);
            /*获取所有的库存信息*/
        List<Stock> stocks = Stock.dao.findAll();
        boolean isHave = false;
        for (Stock stock : stocks) {
            if (stock.getStr("product_id").equals(p.getStr("id"))) {
                info.setProductStock(stock.get("sum")==null?"":stock.getDouble("sum").toString());
                isHave = true;
                break;
            }
        }
        if(!isHave){
            info.setProductStock("0");
        }
        OfflineProductMainData productMainData = new OfflineProductMainData();
        productMainData.setProductInfos(info);
        OfflineProductVo offlineProductVo = new OfflineProductVo();
        offlineProductVo.setMaindata(productMainData);
        return offlineProductVo;
    }

    @Override
    public String packetsVerify(CommonMessageVO vo) {
        if (vo instanceof OfflineVo) {
            OfflineVo offlineVo = (OfflineVo) vo;
            String msg = offlineVo.validate();
            return msg;
        } else {
            return "非法报文";
        }
    }
}
