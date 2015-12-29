package net.loyin.netService.netRuntime.businessImpl;

import com.jfinal.plugin.activerecord.Page;
import net.loyin.model.scm.Comparison;
import net.loyin.model.scm.Product;
import net.loyin.model.scm.Stock;
import net.loyin.model.sso.Parame;
import net.loyin.netService.dataPacket.DataPacket;
import net.loyin.netService.dataPacket.HttpDataPacket;
import net.loyin.netService.domain.CommonHeader;
import net.loyin.netService.domain.CommonMessageVO;
import net.loyin.netService.domain.DataFormats;
import net.loyin.netService.domain.SelectedKeys;
import net.loyin.netService.netRuntime.IMessageRuntime;
import net.loyin.netService.netRuntime.impl.MessageRuntimeImpl;
import net.loyin.netService.vo.fileVo.FileSaveVo;
import net.loyin.netService.vo.productVo.*;
import net.loyin.netService.vo.sysVo.loginVo.LoginVo;
import net.loyin.util.CS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chao on 2015/12/8.
 */
public class ProductService extends MessageRuntimeImpl implements IMessageRuntime {


    @Override
    public DataPacket run(CommonMessageVO vo) throws Exception {
        Map<String,String> userMap = ((HttpDataPacket) this.getDataPacket()).getCookie();
        ProductVo productVo = (ProductVo) vo;
        CommonHeader messageHeader = productVo.getMessageHeader();
        ProductRenturnVo returnVo = new ProductRenturnVo();
        if(userMap == null){
            messageHeader.setRsp_no("FAILURE");
            messageHeader.setRsp_msg("无法获取企业ID");
            messageHeader.setBusinessCode("PRO000002");
            returnVo.setMessageHeader(messageHeader);
            returnVo.setMaindata(new ProductReturnMainData());
        }else{
            ProductMainData mainData = productVo.getMaindata();
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("company_id", userMap.get("company_id"));
            filter.put("keyword", mainData.getSearchKey());
            filter.put("category", mainData.getProductCategory());
            filter.put("searchBrand",mainData.getProductBrand());
            filter.put("customer_id",mainData.getSearchCustomer());
            filter.put("specification", mainData.getProductSpe());
            Page<Product> page = null;
            if (mainData.getSearchType().equals("1")) {
                page = Product.dao.pageGridHis(Integer.parseInt(mainData.getPageNum()), Integer.parseInt(mainData.getPageSize()), filter);
            } else if(mainData.getSearchType().equals("0")) {
                page = Product.dao.pageGrid(Integer.parseInt(mainData.getPageNum()), Integer.parseInt(mainData.getPageSize()), filter);
            }else{
                page = new Page<Product>(new ArrayList<Product>(),0,0,0,0);
            }
//            Page<Product> page = Product.dao.pageGrid(Integer.parseInt(mainData.getPageNum()), Integer.parseInt(mainData.getPageSize()), filter);
            List<Product> list = page.getList();

            ProductReturnMainData returnMainData = new ProductReturnMainData();
            List<ProductReturnInfo> infos = new ArrayList<>();
            ProductReturnInfo info = null;
            for(Product p : list){
                info = new ProductReturnInfo();
                info.setProductId(p.getStr("id"));
                info.setProductName(p.getStr("name"));
                info.setProductBillsn(p.getStr("billsn"));
                info.setProductHisPrice(p.getStr("his_price")==null?"":p.getStr("his_price"));
                info.setProductPrice(p.getBigDecimal("sale_price").toString());
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
                        info.setProductStock(stock.getDouble("sum").toString());
                        isHave = true;
                        break;
                    }
                }
                if(!isHave){
                    info.setProductStock("0");
                }
                infos.add(info);
            }
            //todo 未填加图片，后期改进
            returnMainData.setProductReturnInfoList(infos);
            returnVo.setMaindata(returnMainData);
            messageHeader.setRsp_no("SUCCESS");
            messageHeader.setRsp_msg("");
            messageHeader.setBusinessCode("PRO000002");
            returnVo.setMessageHeader(messageHeader);
        }
        DataPacket httpDataPacket = this.getDataPacket().GetBackBaseDataPacket();
        httpDataPacket.setSelectedKey(SelectedKeys.WRITE);
        httpDataPacket.setRawDataFormat(DataFormats.OBJECT);
        httpDataPacket.setDataFormat(DataFormats.JSON);
        httpDataPacket.setRawdata(returnVo);
        httpDataPacket.setHead("PRO000002");
        return httpDataPacket;
    }

    @Override
    public String packetsVerify(CommonMessageVO vo) {
        if(vo instanceof ProductVo){
            ProductVo productVo = (ProductVo)vo;
            String msg = productVo.validate();
            return msg;
        }else{
            return "非法报文";
        }
    }
}
