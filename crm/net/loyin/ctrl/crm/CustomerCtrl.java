package net.loyin.ctrl.crm;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.loyin.ctrl.AdminBaseController;
import net.loyin.jfinal.anatation.PowerBind;
import net.loyin.jfinal.anatation.RouteBind;
import net.loyin.model.crm.Contacts;
import net.loyin.model.crm.Customer;
import net.loyin.model.crm.CustomerData;
import net.loyin.model.crm.CustomerRecord;
import net.loyin.model.sso.FileBean;
import net.loyin.model.sso.Parame;
import net.loyin.model.sso.SnCreater;

import net.loyin.model.sso.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import javax.servlet.http.HttpServletResponse;

import static com.sun.org.apache.xalan.internal.lib.ExsltMath.atan2;
import static com.sun.org.apache.xalan.internal.lib.ExsltMath.sqrt;
import static java.lang.Math.*;

/**
 * 客户管理
 *
 * @author liugf 风行工作室
 */
@RouteBind(path = "customer", sys = "客户", model = "客户维护")
public class CustomerCtrl extends AdminBaseController<Customer> {

    /**
     * ----------------高德经纬度转百度经纬度-------------------*
     */

    private static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

    private static double gg_lat = 0.0d;
    private static double gg_lon = 0.0d;
    private static double bd_lat = 0.0d;
    private static double bd_lon = 0.0d;

    /**
     * -----------------------------------*
     */

    public CustomerCtrl() {
        this.modelClass = Customer.class;
    }


    /**
     * APP
     * by chenjianhui
     * 2015年9月7日11:47:41
     * 手机端获取所有的客户信息
     */
    public void getList() {
        Map<String, Object> parame = new HashMap<String, Object>();
        String industry_id = this.getPara("industry_id");
        if (industry_id != null) {
            /*等于零是因为 app 要查找所有的时候 会传0过来 ，0代表全部*/
            if (!industry_id.equals("0")) {
                parame.put("industry_id", industry_id);
            }
        }
        List data = Customer.dao.getListData(parame);
        this.rendJsonApp(true, data, 200, "success");
    }


    /**
     * by chenjianhui
     * 2015年9月5日14:07:58
     * 地图初始化的时候 将说有的用户显示在地图上面
     */
    public void dataMap() {
        List<Customer> data = Customer.dao.dataMap(this.getCompanyId());
        this.rendJson(true, null, "success", data);
    }

    /**
     * by chenjianhui
     * 2015年9月5日16:45:03
     * 根据参数查询需要的数据
     */
    public void getMapData() {
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put("keyword", this.getPara("keyword"));
        filter.put("start_date", this.getPara("start_date"));
        filter.put("end_date", this.getPara("end_date"));
        filter.put("is_deleted", this.getParaToInt("is_deleted", 0));
        filter.put("status", this.getParaToInt("status", 0));//状态 1：客户池
        filter.put("type", this.getParaToInt("type", -1));//0：供应商1：企业客户 2：个人客户
        filter.put("company_id", this.getCompanyId());
        filter.put("uid", this.getPara("uid"));
        filter.put("user_id", this.getCurrentUserId());
        filter.put("position_id", this.getPositionId());
        this.sortField(filter);
        List<Customer> list = Customer.dao.getListData(filter, this.getParaToInt("qryType", 0));
        this.rendJson(true, null, "success", list);
    }


    /**
     * 查找客户
     */
    public void dataGrid() {
        String companyId = getCompanyIdByHttp();
        if (companyId.equals(this.getCompanyId())) {
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("keyword", this.getPara("keyword"));
            filter.put("start_date", this.getPara("start_date"));
            filter.put("end_date", this.getPara("end_date"));
            filter.put("is_deleted", this.getParaToInt("is_deleted", 0));
            filter.put("status", this.getParaToInt("status", 0));//状态 1：客户池
            filter.put("type", this.getParaToInt("type", -1));//0：供应商1：企业客户 2：个人客户
            filter.put("company_id", companyId);
            filter.put("uid", this.getPara("uid"));
            filter.put("user_id", this.getCurrentUserId());
            filter.put("position_id", this.getPositionId());
            this.sortField(filter);
            Page<Customer> page = Customer.dao.pageGrid(getPageNo(), getPageSize(), filter, this.getParaToInt("qryType", 11));
            this.rendJson(true, null, "success", page);
        } else {
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("keyword", this.getPara("keyword"));
            filter.put("start_date", this.getPara("start_date"));
            filter.put("end_date", this.getPara("end_date"));
            filter.put("is_deleted", this.getParaToInt("is_deleted", 0));
            filter.put("status", this.getParaToInt("status", 0));//状态 1：客户池
            filter.put("type", this.getParaToInt("type", -1));//0：供应商1：企业客户 2：个人客户
            filter.put("company_id", companyId);
            filter.put("uid", this.getPara("uid"));
            filter.put("user_id", this.getCurrentUserId());
            filter.put("position_id", this.getPositionId());
            this.sortField(filter);
            Page<Customer> page = Customer.dao.pageGrid(getPageNo(), getPageSize(), filter, 11);
            this.rendJson(true, null, "success", page);
        }
    }

    public void qryOp() {
        getId();
        Map<String, Object> data = new HashMap<String, Object>();
        Customer m = Customer.dao.findById(id, getCompanyIdByHttp());
        if (m != null) {
            data.put("customer", m);
            Contacts mainContacts = Contacts.dao.findMainByCustomerId(id);
            if (mainContacts == null) {
                mainContacts = new Contacts();
            }
            data.put("mainContacts", mainContacts);
            this.rendJson(true, null, "", data);
        } else
            this.rendJson(false, null, "记录不存在！");
    }


    @PowerBind(code = "A1_1_E", funcName = "编辑")
    public void save() {

        try {
            Customer po = (Customer) getModel();
            if (po == null) {
                this.rendJson(false, null, "提交数据错误！");
                return;
            }
            getId();
            String uid = this.getCurrentUserId();
            String company_id = getCompanyIdByHttp();
            int type = this.getParaToInt("type");
            if (Customer.dao.existCust(this.getPara("name"), id, type, company_id)) {
                this.rendJson(false, null, "该" + (type == 0 ? "供应商" : "客户") + "已经存在！");
                return;
            }
            this.pullUser(po, uid);
            CustomerData data = (CustomerData) this.getModel2(CustomerData.class);
            String sn = po.getStr("sn");
            if (StringUtils.isEmpty(id)) {
                sn = SnCreater.dao.create(type == 0 ? "SUPPLIER" : "CUSTOMER", company_id);
                if(type != 0){
                   /*----------------------*/
                /*经纬度转换*/
                    gg_lat = Double.parseDouble(po.get("lat"));
                    gg_lon = Double.parseDouble(po.get("lng"));
                    bd_encrypt(gg_lat, gg_lon);

                    po.set("lat", po.get("lat"));
                    po.set("lng", po.get("lng"));

                    po.set("lat_bd", bd_lat);
                    po.set("lng_bd", bd_lon);

                /*----------------------*/
                }
                po.set("company_id", company_id);
                po.set("sn", sn);
                po.save();
                po.set("is_deleted", 0);
                id = po.getStr("id");
                data.set("id", id);
                data.save();
                if (type != 0) {
                    CustomerRecord cr = new CustomerRecord();
                    cr.set("create_datetime", dateTimeFormat.format(new Date()));
                    cr.set("customer_id", id);
                    cr.set("user_id", uid);
                    cr.set("type", 0);
                    cr.save();

                }
            } else {
                po.update();
                data.update();
            }
            Map<String, String> d = new HashMap<String, String>();
            d.put("id", id);
            d.put("sn", sn);
            this.rendJson(true, null, "操作成功！", d);
        } catch (Exception e) {
            log.error("保存客户异常", e);
            this.rendJson(false, null, "保存客户异常！");
        }
    }

    @PowerBind(code = "A1_1_E", funcName = "删除")
    public void del() {
        try {
            getId();
            Customer.dao.del(id, getCompanyIdByHttp());
            rendJson(true, null, "删除成功！", id);
        } catch (Exception e) {
            log.error("删除异常", e);
            rendJson(false, null, "删除失败！请检查是否被使用！");
        }
    }

    @PowerBind(code = "A1_1_E", funcName = "回收客户")
    public void trash() {
        try {
            getId();
            Customer.dao.trash(id, this.getCurrentUserId(), getCompanyIdByHttp(), dateTimeFormat.format(new Date()));
            rendJson(true, null, "移动到回收站成功！", id);
        } catch (Exception e) {
            log.error("移动到回收站异常", e);
            rendJson(false, null, "移动到回收站失败！");
        }
    }

    /**
     * 恢复
     */
    @PowerBind(code = "A1_1_E", funcName = "恢复")
    public void reply() {
        try {
            getId();
            Customer.dao.reply(id, this.getCompanyId());
            rendJson(true, null, "恢复成功！", id);
        } catch (Exception e) {
            log.error("恢复异常", e);
            rendJson(false, null, "恢复失败！");
        }
    }

    /**
     * 放入客户池
     */
    @PowerBind(code = "A1_1_E", funcName = "放入客户池")
    public void toPool() {
        try {
            getId();
            Customer.dao.toPool(id, getCompanyId());
            rendJson(true, null, "操作成功！", id);
        } catch (Exception e) {
            log.error("放入客户池异常", e);
            rendJson(false, null, "操作失败！");
        }
    }

    /**
     * 领取1 或分配2
     */
    @PowerBind(code = "A1_1_E", funcName = "分配或领取")
    public void allot() {
        try {
            getId();
            int type = this.getParaToInt("type", 0);
            String uid = this.getPara("uid");
            if (type == 2 && StringUtils.isEmpty(uid)) {
                this.rendJson(false, null, "未选择用户！");
                return;
            } else if (StringUtils.isEmpty(uid)) {
                uid = this.getCurrentUserId();
            }
            Customer.dao.allot(id, uid, type, dateTimeFormat.format(new Date()));
            rendJson(true, null, "操作成功！", id);
        } catch (Exception e) {
            log.error("分配客户异常", e);
            rendJson(false, null, "操作失败！");
        }
    }

    /**
     * 导出
     */
    @PowerBind(code = "A1_1_O", funcName = "导出")
    public void expout() {
        String company_id = getCompanyIdByHttp();
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put("keyword", this.getPara("keyword"));
        filter.put("start_date", this.getPara("start_date"));
        filter.put("end_date", this.getPara("end_date"));
        filter.put("is_deleted", this.getParaToInt("is_deleted", 0));
        filter.put("status", this.getParaToInt("status", 0));//状态 1：客户池
        filter.put("type", this.getParaToInt("type", -1));//0：供应商1：企业客户 2：个人客户
        filter.put("company_id", company_id);
        filter.put("uid", this.getPara("uid"));
        filter.put("user_id", this.getCurrentUserId());
        filter.put("position_id", this.getPositionId());
        List<Record> custList = Customer.dao.findCustAndContactList(filter, this.getParaToInt("qryType", 0));

        try {
            if (custList != null && custList.isEmpty() == false) {
                String now = dateFormat.format(new Date());
                this.getResponse().reset();// 清空输出流

                this.getResponse().setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(now + "导出客户.xls", "utf-8"));// 设定输出文件头
                this.getResponse().setContentType("application/msexcel;charset=utf-8");// 定义输出类型
                OutputStream os = this.getResponse().getOutputStream();// 取得输出流
                //自定义参数
                List<Parame> plist = Parame.dao.list(company_id);
                Map<String, String> pMap = new HashMap<String, String>();
                for (Parame p : plist) {
                    pMap.put(p.getStr("id"), p.getStr("name"));
                }
                List<Record> arealist = Parame.dao.qryAreaList();
                Map<String, String> areaMap = new HashMap<String, String>();
                for (Record p : arealist) {
                    areaMap.put(p.getStr("id"), p.getStr("name"));
                }

                File xls = new File(this.getRequest().getRealPath("/excel/客户导出.xls"));
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
                HSSFSheet sheet = (HSSFSheet) wb.getSheetAt(0);
                HSSFRow row3 = sheet.getRow(2);
                List<String> clist = new ArrayList<String>();//定义的列名
                for (int j = 1; j < 34; j++) {
                    HSSFCell cell = row3.getCell(j);
                    if (cell != null) {
                        String colname = cell.getStringCellValue();
                        clist.add(colname);

                    }
                }
                int i = 0;
                String sn = "";//客户编号
                for (Record c : custList) {
                    HSSFRow row = sheet.createRow(2 + i);
                    HSSFCell cell = row.createCell(0);
                    i++;
                    cell.setCellValue(i);
                    int k = 1;
                    boolean tt = false;
                    for (String cl : clist) {
                        Object v = c.get(cl);
                        if (k == 1) {
                            if (sn.equals(v) == false) {
                                sn = (String) v;
                                tt = false;
                            } else {
                                tt = true;
                            }
                        }
                        if (tt && k < 22) {
                            k++;
                            continue;
                        }
                        if (k == 3) {//客户类别
                            String[] sex = new String[]{"供应商", "企业客户", "个人客户", "经销商"};
                            if (v != null)
                                v = sex[(Integer) v];
                        }
                        if (k == 23) {//性别
                            String[] sex = new String[]{"女", "男"};
                            if (v != null)
                                v = sex[(Integer) v];
                        }
                        if (k == 18 || k == 19) {//省份地市
                            v = areaMap.get(v);
                        }
                        if (k == 7 || k == 8 || k == 9 || k == 10 || k == 11 || k == 12 || k == 13 || k == 14) {
                            v = pMap.get(v);
                        }
                        if (v != null) {
                            HSSFCell cell_ = row.createCell(k);
                            cell_.setCellValue(v.toString());
                        }
                        k++;
                    }
                }
                wb.write(os);
                os.flush();
                os.close();
            }
        } catch (Exception ex) {
            log.error("导出excel异常", ex);
        }
        this.renderNull();
    }

    /**
     * 上传excel文件
     */
    @PowerBind(code = "A1_1_O", funcName = "导入")
    public void saveFile() {
        UploadFile uf = this.getFile();
        if (uf == null || uf.getFile() == null) {
            this.rendJson(false, null, "文件未上传！");
            return;
        }
        String userid = this.getPara(0);
        String company_id = this.getPara(1);
        String company_id_sys = this.getPara(2);
        String head_id = null;
//        int k = 0;
        if (company_id_sys != null && !company_id_sys.isEmpty()) {
            company_id = company_id_sys;
        }
        if (StringUtils.isEmpty(userid)) {
            this.rendJson(false, null, "参数不正确！");
            return;
        }
        try {
            File xls = new File(this.getRequest().getRealPath("/excel/客户导出.xls"));
            FileInputStream is_ = new FileInputStream(xls);
            Workbook wb = null;
            try {
                wb = new HSSFWorkbook(is_);
            } catch (Exception e) {
                is_.close();
                try {
                    wb = new XSSFWorkbook(is_);
                } catch (Exception e1) {
                    return;
                }
            }
            HSSFSheet sheet_ = (HSSFSheet) wb.getSheetAt(0);
            HSSFRow row3 = sheet_.getRow(2);
            List<String> clist = new ArrayList<String>();//定义的列名
            for (int j = 1; j < 37; j++) {
                HSSFCell cell = row3.getCell(j);
                if (cell != null) {
                    String colname = cell.getStringCellValue();
                    clist.add(colname);
                }
            }
            is_.close();
            InputStream is = new FileInputStream(uf.getFile());
            Workbook wkb = null;
            try {
                wkb = new HSSFWorkbook(is);
            } catch (Exception e) {
                is.close();
                try {
                    wkb = new XSSFWorkbook(is);
                } catch (Exception e1) {
                    throw e1;
                }
            }
            List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
            Sheet sheet = wkb.getSheetAt(0);
            if (sheet.getLastRowNum() > 0) {
                //自定义参数
                List<Parame> plist = Parame.dao.list(company_id);
                Map<String, String> pMap = new HashMap<String, String>();
                for (Parame p : plist) {
                    pMap.put(p.getStr("name"), p.getStr("id"));
                }
                List<Record> arealist = Parame.dao.qryAreaList();
                Map<String, String> areaMap = new HashMap<String, String>();
                for (Record p : arealist) {
                    areaMap.put(p.getStr("name"), p.getStr("id"));
                }
                Map<String, String> typeMap = new HashMap<>();
                typeMap.put("7", "2");
                typeMap.put("8", "6");
                typeMap.put("9", "5");
                typeMap.put("10", "3");
                typeMap.put("11", "18");
                typeMap.put("12", "19");
                typeMap.put("13", "4");
                typeMap.put("14", "20");

                for (int idx = 2; idx <= sheet.getLastRowNum(); idx++) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    Row row = sheet.getRow(idx);
                    if (row == null)
                        continue;
                    for (int k = 1; k < 37; k++) {
                        Object v = null;
                        HSSFCell cell_ = (HSSFCell) row.getCell(k);
                        if (cell_ == null) {
                            v = null;
                        } else {
                            cell_.setCellType(Cell.CELL_TYPE_STRING);
                            v = cell_.getStringCellValue();
                        }
                        if (k == 3) {//客户类别
                            if (v != null) {
                                if ("供应商".equals(v)) {
                                    v = 0;
                                } else if ("企业客户".equals(v)) {
                                    v = 1;
                                } else if ("个人客户".equals(v)) {
                                    v = 2;
                                } else if ("经销商".equals(v)) {
                                    v = 3;
                                } else {
                                    v = 2;
                                }
                                v = Integer.parseInt(String.valueOf(v));
                            }
                        }
                        if (k == 23) {//性别
                            if (v != null)
                                v = "男".equals(v) ? 1 : "女".equals(v) ? 0 : 1;
                        }
                        if (k == 18 || k == 19) {//省份地市
                            v = areaMap.get(v);
                        }
                        if (k == 7 || k == 8 || k == 9 || k == 10 || k == 11 || k == 12 || k == 13 || k == 14) {
                            if (!pMap.containsKey(v)) {
                                Map<String, String> parMap = saveParam(v.toString(), Integer.parseInt(typeMap.get(String.valueOf(k))), company_id);
                                if (parMap == null) {
//                                    throw new Exception();
                                    v = null;
                                } else {
                                    v = parMap.get(v.toString());
                                    pMap.putAll(parMap);
                                }
                            } else {
                                v = pMap.get(v);
                            }
                        }
                        if (k == 5) {
                            v = Double.parseDouble(String.valueOf(v));
                        }
                        if (k == 6) {//负责人查询，如无负责人，则将其放入客户池
                            User user = User.dao.findByName(v.toString(), company_id);
                            if (user == null) {
                                map.put("head_id", null);
                                map.put("status", 1);
                            } else {
                                map.put("head_id", user.getStr("id"));
                                map.put("status", 0);
                            }
                        }
                        map.put(clist.get(k - 1), v);
                    }
                    //todo
                    /*----------------------*/
                /*经纬度转换*/
                    if(map.containsKey("maptype")&&String.valueOf(map.get("maptype")).equals("百度")){
                        bd_lat = Double.parseDouble(String.valueOf(map.getOrDefault("lat", 0.0)));
                        bd_lon = Double.parseDouble(String.valueOf(map.getOrDefault("lng",0.0)));
                        bd_decrypt(bd_lat, bd_lon);
                        map.put("lat_bd", bd_lat);
                        map.put("lng_bd", bd_lon);
                        map.put("lat", gg_lon);
                        map.put("lng", gg_lat);
                    }else if(map.containsKey("maptype")&&String.valueOf(map.get("maptype")).equals("高德")){
                        gg_lat = Double.parseDouble(String.valueOf(map.getOrDefault("lat", 0.0)));
                        gg_lon = Double.parseDouble(String.valueOf(map.getOrDefault("lng",0.0)));
                        bd_encrypt(gg_lat, gg_lon);
                        map.put("lat_bd", bd_lat);
                        map.put("lng_bd", bd_lon);
                        map.put("lat", gg_lat);
                        map.put("lng", gg_lon);
                    }else{
                        map.put("lat_bd", "39.915883");
                        map.put("lng_bd", "116.402056");
                        map.put("lat", "39.904983");
                        map.put("lng", "116.427287");
                    }

                /*----------------------*/

                    dataList.add(map);
                }
                Customer.dao.impl(dataList, company_id, userid, clist, this.dateTimeFormat.format(new Date()));
                is.close();
                this.rendJson(true, null, "导入客户资料成功！");
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

    public Map<String, String> saveParam(String name, int type, String companyId) {
        Map<String, String> hasMap = new HashMap<>();
        Parame parame = new Parame();
        if (name == null || type == -1 || name.isEmpty()) {
            return null;
        }
        parame.set("company_id", companyId);
        parame.set("name", name);
        parame.set("sort_num", 9999);
        parame.set("is_end", 0);
        parame.set("type", type);
        parame.save();
        hasMap.put(name, parame.getStr("id"));
        return hasMap;
    }


    /**
     * by chenjianhui
     * 2015年10月10日14:06:19
     * 经纬度的转换
     * <p>
     * 高德转百度
     *
     * @param gg_lat
     * @param gg_lon
     */
    void bd_encrypt(double gg_lat, double gg_lon) {
        double x = gg_lon, y = gg_lat;
        double z = sqrt(x * x + y * y) + 0.00002 * sin(y * x_pi);
        double theta = atan2(y, x) + 0.000003 * cos(x * x_pi);
        bd_lon = z * cos(theta) + 0.0066;
        bd_lat = z * sin(theta) + 0.0062;
    }

    /**
     * 百度地图 转换 高德地图坐标
     *
     * @param bd_lat
     * @param bd_lon
     */
    private static void bd_decrypt(double bd_lat, double bd_lon) {

        double x = bd_lon - 0.0066, y = bd_lat - 0.0062;
        double z = sqrt(x * x + y * y) - 0.00002 * sin(y * x_pi);
        double theta = atan2(y, x) - 0.000003 * cos(x * x_pi);
        gg_lat = z * cos(theta);
        gg_lon = z * sin(theta);
    }

    /**
     * 获取图片地址
     */
    public void qryPic() {
        getId();
//		ConcatRecord m = ConcatRecord.dao.findById(id, this.getCompanyId());
        List<FileBean> fileBeanList = FileBean.dao.findList(id);
        if (fileBeanList == null || fileBeanList.size() == 0)
            this.rendJson(false, null, "图片记录不存在！");
        else
            this.rendJson(true, null, "", fileBeanList);
    }

    /**
     * 根据ID获取图片
     */
    public void downloadFile() {
        try {
            HttpServletResponse response = this.getResponse();
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            getId();
            FileBean fileBean = FileBean.dao.findByFileId(id);
            if (fileBean == null) {
                this.rendJson(false, null, "保存数据异常！");
            }
            BufferedInputStream bis = null;
            OutputStream os = null;
            FileInputStream fileInputStream = new FileInputStream(new File(fileBean.getStr("file_path")));

            bis = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[512];
            response.reset();
            response.setCharacterEncoding("UTF-8");
            //不同类型的文件对应不同的MIME类型
            response.setContentType("image/*");
            //文件以流的方式发送到客户端浏览器
            //response.setHeader("Content-Disposition","attachment; filename=img.jpg");
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
//            this.rendJson(true, null, "操作成功！", id);
            this.renderNull();
//            this.renderFile();
        } catch (Exception e) {
//			e.printStackTrace();
        }
//
    }


}
