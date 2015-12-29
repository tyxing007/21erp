package net.loyin.ctrl.scm;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import net.loyin.ctrl.AdminBaseController;
import net.loyin.jfinal.anatation.PowerBind;
import net.loyin.jfinal.anatation.RouteBind;
import net.loyin.model.scm.*;
import net.loyin.model.sso.FileBean;
import net.loyin.model.sso.Parame;
import net.loyin.model.sso.SnCreater;

import net.loyin.util.CS;
import org.apache.commons.lang3.StringUtils;

import com.jfinal.plugin.activerecord.Page;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONString;

import javax.servlet.http.HttpServletResponse;

/**
 * @author liugf 风行工作室
 */
@RouteBind(path = "product", sys = "进销存", model = "产品")
public class ProductCtrl extends AdminBaseController<Product> {
    public ProductCtrl() {
        this.modelClass = Product.class;
    }

    public void dataGrid() {
        Map<String, String> userMap = this.getUserMap();
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put("company_id", userMap.get("company_id"));
        filter.put("keyword", this.getPara("keyword"));
        filter.put("category", this.getPara("category"));
        filter.put("searchBrand",this.getPara("brand"));
        filter.put("customer_id",this.getPara("customer_id"));
        filter.put("specification",this.getPara("specification"));
        this.sortField(filter);
        Page<Product> page = Product.dao.pageGrid(getPageNo(), getPageSize(), filter);
        String type = this.getPara("checkType", "NULL");
        if(!type.equals("NULL")){
            List<Product> productList = page.getList();
            for(Product product : productList){
                List<Comparison> comparisonList = Comparison.dao.getComparisonByProductId(product.getStr("id"));
                    Comparison comparison = new Comparison();
                    Parame parame = Parame.dao.findById(product.getStr("unit"),this.getCompanyId());
                    comparison.put("comparison_num",1);
                    comparison.put("comparison_unit",parame==null?"未知":parame.getStr("name"));
                    comparisonList.add(0, comparison);
                    product.put("comparison", comparisonList);
                /*获取所有的库存信息*/
                List<Stock> stocks = Stock.dao.findAll();
                boolean isHave = false;
                for (Stock stock : stocks) {
                    if (stock.getStr("product_id").equals(product.getStr("id"))) {
                        product.put("stock", stock.get("sum"));
                        isHave = true;
                        break;
                    }
                }
                if(!isHave){
                    product.put("stock", 0);
                }
            }
        }
        this.rendJson(true, null, "success", page);
    }

    public void qryOp() {
        getId();
        Product m = Product.dao.findById(id, this.getCompanyId());
        if (m != null)
            this.rendJson(true, null, "", m);
        else
            this.rendJson(false, null, "记录不存在！");
    }

    public void qryComparison() {
        String productId = this.getPara("productId");
        List<Comparison> list = Comparison.dao.getComparisonByProductId(productId);
        if (list == null || list.size() == 0)
            this.rendJson(false, null, "记录不存在！");
        else
            this.rendJson(true, null, "", list);

    }


    public void qryCombination() {
        String productId = this.getPara("productId");
        List<Combination> list = Combination.dao.getCombinationByParentId(this.getCompanyId(), productId);
        if (list == null || list.size() == 0)
            this.rendJson(false, null, "记录不存在！");
        else
            this.rendJson(true, null, "", list);

    }

    @PowerBind(code = {"A2_1_E", "A3_1_E"}, funcName = "编辑")
    public void save() {
        try {
            Product po = (Product) getModel();
            if (po == null) {
                this.rendJson(false, null, "提交数据错误！");
                return;
            }
            getId();
            String sn = po.getStr("billsn");
            String company_id = this.getCompanyId();
            if (StringUtils.isEmpty(id)) {
                if(Product.dao.barCodeIsExit(sn,this.getCompanyId())){
                    this.rendJson(false, null, "该编号已经存在，请核对！");
                    return;
                }
                if (StringUtils.isEmpty(sn)) {
                    sn = SnCreater.dao.create("PRODUCT", company_id);
                    po.set("billsn", sn);
                }
                po.set("company_id", company_id);
                po.save();
                id = po.getStr("id");
                List<Depot> list = Depot.dao.list(this.getCompanyId());
                for(Depot d : list){
                    Average.dao.saveAverageFirst(this.getCompanyId(),id,"0.0","0.0",d.getStr("id"));
                }
            } else {
                Product product = Product.dao.findById(id,this.getCompanyId());
                if(product != null && !product.getStr("billsn").equals(sn)){
                    if(Product.dao.barCodeIsExit(sn,this.getCompanyId())){
                        this.rendJson(false, null, "该编号已经存在，请核对！");
                        return;
                    }
                }
                po.update();
            }
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("id", id);
            data.put("sn", sn);
            this.rendJson(true, null, "操作成功！", data);
        } catch (Exception e) {
            log.error("保存产品异常", e);
            this.rendJson(false, null, "保存数据异常！");
        }
    }

    @PowerBind(code = {"A2_1_E", "A3_1_E"}, funcName = "删除")
    public void del() {
        try {
            getId();
            OrderProduct orderProduct = OrderProduct.dao.findById(id);
            if(orderProduct == null){
                rendJson(false, null, "该产品曾经下过订单，无法删除，请使用激活/失效功能！", id);
                return;
            }
            Product.dao.del(id, this.getCompanyId());
            rendJson(true, null, "删除成功！", id);
        } catch (Exception e) {
            log.error("删除异常", e);
            rendJson(false, null, "删除失败！", id);
        }
    }

    @PowerBind(code = {"A2_1_E", "A3_1_E"}, funcName = "编辑")
    public void disable() {
        getId();
        try {
            Product.dao.disable(id, this.getCompanyId());
            this.rendJson(true, null, "操作成功！", id);
        } catch (Exception e) {
            this.rendJson(false, null, "操作失败！");
        }
    }

    /**
     * 保存导入库存
     */
    public void saveFile() {
        UploadFile uf = this.getFile();
        if (uf == null || uf.getFile() == null) {
            this.rendJson(false, null, "文件未上传！");
            return;
        }
        try {
            String userid = this.getPara(0);
            String company_id = this.getPara(1);
            String company_id_sys = this.getPara(2);
            String head_id = null;
            if (company_id_sys != null && !company_id_sys.isEmpty()) {
                company_id = company_id_sys;
            }
            if (StringUtils.isEmpty(userid)) {
                this.rendJson(false, null, "参数不整齐");
                return;
            }
            File outExcel = new File(this.getRequest().getRealPath("/excel/商品数据导出.xls"));
            FileInputStream is = new FileInputStream(outExcel);
            Workbook wb = null;
            try {
                wb = new HSSFWorkbook(is);
            } catch (Exception e) {
                is.close();
                try {
                    wb = new XSSFWorkbook(is);
                } catch (Exception e1) {
                    return;
                }
            }
            HSSFSheet sheet = (HSSFSheet) wb.getSheetAt(0);
            HSSFRow row = sheet.getRow(2);
            List<String> clist = new ArrayList<String>();//定义的列名
            for (int j = 0; j < 12; j++) {
                HSSFCell cell = row.getCell(j);
                if (cell != null) {
                    String colname = cell.getStringCellValue();
                    clist.add(colname);
                }
            }
            is.close();
            InputStream isWeb = new FileInputStream(uf.getFile());
            Workbook wkb = null;
            try {
                wkb = new HSSFWorkbook(isWeb);
            } catch (Exception e) {
                isWeb.close();
                try {
                    wkb = new XSSFWorkbook(isWeb);
                } catch (Exception e1) {
                    throw e1;
                }
            }
            List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
            Sheet sheetWeb = wkb.getSheetAt(0);
            if (sheetWeb.getLastRowNum() > 0) {
                List<Parame> plist = Parame.dao.list(company_id);
                Map<String, String> pMap = new HashMap<>();
                for (Parame p : plist) {
                    pMap.put(p.getStr("name"), p.getStr("id"));
                }
//				List<Record> productTypeList= Parame.dao.qryList(company_id, 0);
//				Map<String,String> productTypeMap = new HashMap<>();
//				for(Record r:productTypeList){
//					productTypeList
//				}
                for (int idx = 2; idx <= sheetWeb.getLastRowNum(); idx++) {
                    Map<String, Object> map = new HashMap<>();
                    Row row1 = sheetWeb.getRow(idx);
                    if (row1 == null) {
                        continue;
                    }
                    for (int k = 0; k < 12; k++) {
                        String v = null;
                        HSSFCell cell = (HSSFCell) row1.getCell(k);
                        if (cell == null) {
                            v = null;
                        } else {
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            v = cell.getStringCellValue();
                        }
                        if (k == 2) {
                            if (pMap.containsKey(v)) {
                                v = pMap.get(v);
                            } else {
                                Parame parame = new Parame();
                                parame.set("company_id", company_id);
                                parame.set("name", v);
                                parame.set("type", 0);
                                parame.set("sort_num", 9999);
                                parame.set("is_end", 0);
                                parame.save();
                                pMap.put(v, parame.getStr("id"));
                                v = parame.getStr("id");
                            }
                        }
                        if (k == 3) {
                            if (pMap.containsKey(v)) {
                                v = pMap.get(v);
                            } else {
                                HSSFCell typeCell = (HSSFCell) row1.getCell(2);
                                typeCell.setCellType(Cell.CELL_TYPE_STRING);
                                String typeValue = typeCell.getStringCellValue();
                                String typeParentId = String.valueOf(pMap.get(typeValue));
                                Parame parame = new Parame();
                                parame.set("company_id", company_id);
                                parame.set("name", v);
                                parame.set("type", 0);
                                parame.set("parent_id", typeParentId);
                                parame.set("sort_num", 9999);
                                parame.set("is_end", 0);
                                parame.save();
                                pMap.put(v, parame.getStr("id"));
                                v = parame.getStr("id");
                            }
                        }
                        if (k == 4) {
                            if (pMap.containsKey(v)) {
                                v = pMap.get(v);
                            } else {
                                Parame parame = new Parame();
                                parame.set("company_id", company_id);
                                parame.set("name", v);
                                parame.set("type", 1);
                                parame.set("sort_num", 9999);
                                parame.set("is_end", 0);
                                parame.save();
                                pMap.put(v, parame.getStr("id"));
                                v = parame.getStr("id");
                            }
                        }
                        map.put(clist.get(k), v);
                    }
                    HSSFCell cell = null;
                    Map<String, String> comparisonMap = null;
                    List<Map<String, String>> comparisonList = new ArrayList<>();
                    clist.add("comparison");
                    for (int i = 12; i < row1.getLastCellNum(); i++) {
                        cell = (HSSFCell) row1.getCell(i);
                        if(cell == null){
                            continue;
                        }
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        String unit = cell.getStringCellValue();
                        i++;
                        if (unit == null) {
                            continue;
                        }
                        cell = (HSSFCell) row1.getCell(i);
                        if(cell == null){
                            continue;
                        }
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        String num = cell.getStringCellValue();
                        if (num == null) {
                            continue;
                        }
                        comparisonMap = new HashMap<>();
                        comparisonMap.put("comparison_unit", unit);
                        comparisonMap.put("comparison_num", num);
                        comparisonList.add(comparisonMap);
                    }
                    map.put(clist.get(12), comparisonList);
                    dataList.add(map);
                }
                List<Integer> resutlList = Product.dao.impl(dataList, company_id, clist);
                if(resutlList.size()!=0){
                    String fileName = exportHave(company_id,resutlList,sheetWeb);
                    is.close();
                    this.rendJson(true, null, "导入客户产品成功！下载文件为条形码已存在的反馈",fileName);
                    return;
                }
                is.close();
                this.rendJson(true, null, "导入客户产品成功！");
            } else {
                this.rendJson(false, null, "文件数据为空！");
            }
        } catch (Exception e) {
            log.error(e);
            this.rendJson(false, null, "处理文件异常!请保证格式及数据是否正确!" + e.getMessage());
        } finally {
            uf.getFile().delete();
        }
    }


    public List parseArrayByJsonArray(JSONArray valueJsonArray) {
        List resultList = new ArrayList<>();
        for (Object j : valueJsonArray) {
            if (j instanceof JSONArray) {
                resultList.addAll(parseArrayByJsonArray((JSONArray) j));
            } else if (j instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) j;
                Iterator<String> jI = jsonObject.keySet().iterator();
                Map<String, Object> map = new HashMap<>();
                while (jI.hasNext()) {
                    String key = jI.next();
                    if (jsonObject.get(key) instanceof JSONArray) {
                        map.put(key, parseArrayByJsonArray((JSONArray) jsonObject.get(key)));
                    } else {
                        map.put(key, jsonObject.get(key).toString());
                    }
                }
                resultList.add(map);
            }
        }
        return resultList;
    }

    /**
     * 保存包装书
     */
    public void saveComparison() {
        try {
            String comparisonStr = getPara("comparison");
            boolean isAdd = Boolean.parseBoolean(getPara("isAdd"));
            JSONArray jsonArray = JSONArray.parseArray(comparisonStr);
            List<Map<String, String>> list = parseArrayByJsonArray(jsonArray);
            for (Map<String, String> v : list) {
                if (isAdd) {
                    Comparison comparison = Comparison.dao.findById(v.get("id"));
                    if(comparison!=null){
                        comparison.delete();
                    }
                }
                Comparison comparison = new Comparison();
                Iterator<String> keys = v.keySet().iterator();
                while (keys.hasNext()) {
                    String key = keys.next();
                    if(key == "id"){
                        continue;
                    }
                    comparison.set(key, v.get(key));
                }
                comparison.set("company_id",this.getCompanyId());
                comparison.save();
            }
            this.rendJson(true, null, "保存成功！");
        } catch (Exception e) {
            log.error("保存产品异常", e);
            this.rendJson(false, null, "保存数据异常！");
        }
    }

    /**
     * 获取产品类别
     */
    public void productType(){
            List<Record> parameList = Parame.dao.qryListIsHaveParen(this.getCompanyId(), 0, true, null);
            Map map = null;
            List result = new ArrayList<>();
            for(Record r: parameList) {
                map = new HashMap<>();
                List<Map> list = getChildProductByPid(r.getStr("pid"));
                    if(list!=null){
                        map.put("children",list);
                    }
                map.put("name",r.getStr("name"));
                map.put("id",r.getStr("id"));
                map.put("pid", r.getStr("pid"));
                result.add(map);
            }
        this.rendJson(true, null, "保存成功！", result);
    }

    /**
     * 通过父类别ID查找子列表
     * @param parentId
     * @return
     */
    public List<Map> getChildProductByPid(String parentId){
        List<Parame> list = Parame.dao.findByParentId(parentId, this.getCompanyId());
        Map map = null;
        List<Map> result = new ArrayList<>();
        if(list!=null&&list.size()!=0){
            for(Parame parame:list){
                map = new HashMap<>();
                List<Map> chileList =getChildProductByPid(parame.getStr(parame.getStr("parent_id")));
                if(chileList != null){
                        map.put("children", chileList);
                }
                map.put("name",parame.getStr("name"));
                map.put("id",parame.getStr("id"));
                map.put("pid",parame.getStr("parent_id"));
                result.add(map);
            }
            return result;
        }
        return null;
    }

    /**
     * 查找产品信息，通过产品ID
     */
    public void qryProductInfo(){
        String productIds = this.getPara("productId");
        List<Map<String,String>> list = new ArrayList<>();
        JSONArray jsonArray = JSON.parseArray(productIds);
        for(Object jsonObject : jsonArray){
            list.add((Map)jsonObject);
        }
        List<Product> productList = Product.dao.getProductById(this.getCompanyId(), list);
        List<Map<String,Object>> resultList = new ArrayList<>();
//        amount
        String type = this.getPara("checkType", "NULL");
        for(Map<String,String> map : list){
            String productId = map.get("id");
            for(Product product : productList){
                if(productId.equals(product.getStr("id"))){
                    Map<String,Object> resultMap = new HashMap<>();
                    Iterator<Map.Entry<String, Object>> iterator = product.getAttrsEntrySet().iterator();
                    while (iterator.hasNext()){
                        Map.Entry<String, Object> m = iterator.next();
                        resultMap.put(m.getKey(), m.getValue() == null ? "" : m.getValue().toString());
                    }
                    if(resultMap.get("product_type").toString().equals("2")){
                        resultMap.put("sale_price","0");
                    }
                    resultMap.put("amount", map.containsKey("num")?map.get("num"):"");
                    if(!type.equals("NULL")){
                            List<Comparison> comparisonList = Comparison.dao.getComparisonByProductId(product.getStr("id"));
                        resultMap.put("comparison",comparisonList);
                    }
                    resultList.add(resultMap);
                    continue;
                }
            }
        }
        this.rendJson(true, null, "success", resultList);
    }


    public void saveCombination(){
        String combinationStr = this.getPara("combination");
        boolean isAdd = Boolean.parseBoolean(getPara("isAdd"));
        JSONArray jsonArray = JSONArray.parseArray(combinationStr);
        List<Map<String, String>> list = parseArrayByJsonArray(jsonArray);
        for (Map<String, String> v : list) {

            if (!isAdd) {
                Combination combination = Combination.dao.findById(v.get("id"));
                if(combination != null){
                    combination.delete();
                }
            }

            Combination combination = new Combination();
            Iterator<String> keys = v.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                if(key == "id"){
                    continue;
                }
                combination.set(key, v.get(key));
            }
            combination.set("company_id", this.getCompanyId());
            combination.save();
        }
        this.rendJson(true, null, "保存成功！");

    }

    /**
     * 获取品牌
     */
    public void merchBrandMainList(){
        Map<String,String> map = new HashMap<>();
        String searchContent = this.getPara("keyword");
        String merchType = this.getPara("category");
        String merchStand = this.getPara("specification");
        map.put("searchContent",searchContent);
        map.put("merchType", merchType);
        map.put("merchStand", merchStand);
        List<Record> parameList = Parame.dao.qryListProductInfoByOrder(this.getCompanyId(), true, false, map);
        Record record = new Record();
        record.set("merch_brand","");
        parameList.add(0,record);
        this.rendJson(true, null, "success", parameList);
    }


    /**
     * 获取类别数
     */
    public void merchTypeMainList(){

        Map<String,Object> map = new HashMap<>();
        String searchContent = this.getPara("keyword");
        String searchBrand = this.getPara("brand");
        /*map.put("searchContent",searchContent);
        map.put("searchBrand", searchBrand);
        List<Record> parameList = Parame.dao.qryListProductInfoByOrder(this.getCompanyId(), false, false, map);*/
        map.put("searchBrand", searchBrand);
        map.put("company_id", this.getCompanyId());
        map.put("keyword", searchContent);
        Page<Product> page = Product.dao.pageGrid(1, 9999, map);
        List<Record> parameList = new ArrayList<>();
        if(page.getList().size()==0){
            Record record = new Record();
            record.set("merch_brand", "");
            parameList.add(0, record);
            this.rendJson(true, null, "success", parameList);
            return;
        }
        List<Product> products = page.getList();
        List<String> productIds = new ArrayList<>();
        for(Product p : products){
            boolean isHave = false;
            for(String s : productIds){
                if(s == p.getStr("product_id")){
                    isHave = true;
                }
            }
            if(!isHave){
                productIds.add(p.getStr("product_id"));
            }
        }
        this.rendJson(true, null,"",Parame.dao.qryListInProduct(this.getCompanyId(),this.getParaToInt("type", 0),productIds.toArray(new String[productIds.size()])));
    }


    /**
     * 获取规格树
     */
    public void merchStanderMainList(){
        Map<String,String> map = new HashMap<>();
        String searchContent = this.getPara("keyword");
        String merchType = this.getPara("category");
        String searchBrand = this.getPara("brand");
        map.put("searchContent",searchContent);
        map.put("merchType", merchType);
        map.put("searchBrand", searchBrand);
        List<Record> parameList = Parame.dao.qryListProductInfoByOrder(this.getCompanyId(), false, false, map);
        Record record = new Record();
        record.set("merch_stander", "");
        parameList.add(0, record);
        this.rendJson(true, null, "success", parameList);
    }


    private String exportHave(String company_id,List<Integer> indexList,Sheet sheet) throws IOException {
        String now = dateFormat.format(new Date());
        String fileName = now +company_id+"条形码重复产品.xls";

//        this.getResponse().reset();// 清空输出流
//        this.getResponse().setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(now + "条形码重复产品.xls", "utf-8"));// 设定输出文件头
//        this.getResponse().setContentType("application/msexcel;charset=utf-8");// 定义输出类型
//        OutputStream os = this.getResponse().getOutputStream();// 取得输出流
        String src = "";
        if (System.getenv("OS").toUpperCase().equals("WINDOWS_NT")) {
            src = "D:\\EYKJ\\fileTemp\\";
        } else {
            src = "/home/fileTemp/eykj/";
        }
        File targetDir = new File(src);
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        File target = new File(targetDir, fileName);
        if (!target.exists()) {
            target.createNewFile();
        }
        FileOutputStream fileOut = new FileOutputStream(target);
        File xls = new File(this.getRequest().getRealPath("/excel/商品数据导出.xls"));
        FileInputStream is = new FileInputStream(xls);
        Workbook wb = null;
        try {
            wb = new HSSFWorkbook(is);
        } catch (Exception e) {
            is.close();
            try {
                wb = new XSSFWorkbook(is);
            } catch (Exception e1) {
                throw e1;
            }
        }
        HSSFSheet sheetTemp = (HSSFSheet) wb.getSheetAt(0);
        Row rowTemp = sheetTemp.getRow(2);
        sheetTemp.removeRow(rowTemp);
        int i=2;
        for(int m : indexList){
            Row row = sheet.getRow((m+2));
            rowTemp = sheetTemp.createRow(i);
            for(int n=0;n<row.getLastCellNum();n++){
                Cell cellTemp = rowTemp.createCell(n);
                Cell cell = row.getCell(n);
                if(cell == null){
                    continue;
                }
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cellTemp.setCellValue(cell.getRichStringCellValue());
            }
            i++;
        }
        wb.write(fileOut);
//        byte[] buffer = new byte[512];
//        int n;
//        while ((n = is.read(buffer)) != -1) {
//            os.write(buffer, 0, n);
//        }
//        is.close();
        fileOut.flush();
        fileOut.close();
        return fileName;
    }


    public void downProductFile(){
        String src = "";
        String fileName = this.getPara("filename");
        if (System.getenv("OS").toUpperCase().equals("WINDOWS_NT")) {
            src = "D:\\EYKJ\\fileTemp\\";
        } else {
            src = "/home/fileTemp/eykj/";
        }
        try {
            HttpServletResponse response = this.getResponse();
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            getId();
            BufferedInputStream bis = null;
            OutputStream os = null;
            FileInputStream fileInputStream = new FileInputStream(new File(src+fileName));

            bis = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[512];
            response.reset();
            response.setCharacterEncoding("UTF-8");
            //不同类型的文件对应不同的MIME类型
            response.setContentType("text/*");
            //文件以流的方式发送到客户端浏览器
            response.setHeader("Content-Disposition","attachment; filename="+new String(fileName.getBytes(),"UTF-8"));
            //response.setHeader("Content-Disposition", "inline; filename=img.jpg");

            response.setContentLength(bis.available());

            os = response.getOutputStream();
            int n;
            while ((n = bis.read(buffer)) != -1) {
                os.write(buffer, 0, n);
            }
            bis.close();
            os.flush();
            os.close();
            this.rendJson(true, null, "操作成功！", id);
        } catch (Exception e) {
//			e.printStackTrace();
        }
//
    }


    /**
     * 入库单添加新产品
     */
    public void qryProductByStorage(){
        getId();
        String barCode = this.getPara("barCode");
        StorageBill storageBill = StorageBill.dao.findById(id,this.getCompanyId());
        if(storageBill == null){
            this.rendJson(false, null, "库存数据异常,请联系管理员！");
        }
        Order order = Order.dao.findByOrderSn(storageBill.getStr("ordersn"), this.getCompanyId());
        if(order == null){
            this.rendJson(false, null, "订单数据异常,请联系管理员！");
        }
        Product product = Product.dao.findByBillsn(barCode,this.getCompanyId());
        if(product == null){
            this.rendJson(false, null, "产品数据异常,请联系管理员！");
        }
//        Parame parame = Parame.dao.findById(product.getStr("unit"),this.getCompanyId());
//        String parameStr = "";
//        if(parame == null){
//            parameStr = "未知";
//        }else{
//            parameStr = parame.getStr("name");
//        }
//        String CompanyId,String productId,String customerId
        ProductHis productHis = ProductHis.dao.getProductHisBySomeId(this.getCompanyId(), product.getStr("id"), order.getStr("customer_id"));

        Map map = new HashMap();
        map.put("productId",product.getStr("id"));
        map.put("productName",product.getStr("name"));
        map.put("his_price",productHis == null?"0":productHis.getStr("his_price"));
        map.put("sale_price",product.get("sale_price").toString());
//        map.put("price",product.getStr("billsn"));
        map.put("unit",product.getStr("unit"));
        List result = new ArrayList<>();
        result.add(map);
        this.rendJson(true, null, "操作成功！", result);
    }




}
