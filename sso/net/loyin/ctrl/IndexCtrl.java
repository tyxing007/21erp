package net.loyin.ctrl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import net.loyin.jfinal.anatation.RouteBind;
import net.loyin.model.crm.Customer;
import net.loyin.model.sso.Company;
import net.loyin.model.sso.Person;
import net.loyin.model.sso.User;
import net.loyin.util.PropertiesContent;
import net.loyin.util.safe.CipherUtil;
import net.loyin.util.safe.MD5;
import net.loyin.validator.LoginValid;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Before;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sun.misc.Cleaner;

/**
 * 首页调用及登录退出
 *
 * @author liugf 风行工作室
 */
@SuppressWarnings("rawtypes")
@RouteBind(path = "/", name = "", sys = "", model = "", code = "")
public class IndexCtrl extends BaseController {
    public void index() {
        String uid = this.getCurrentUserId();
        if (StringUtils.isEmpty(uid)) {
            this.renderHTML("login");
            return;
        }
        super.index();
    }

    @Before(LoginValid.class)
    public void login() {
        String login = this.getPara("login_name");
        String userno = this.getPara("userno");
        String pwd = this.getPara("pwd");
        String type = this.getPara("request_type");
        String companyName = this.getPara("company");
        Company company = Company.dao.qryCompanyByName(companyName);
        if (company == null) {
            rendJson(false, null, "公司不存在！");
            return;
        }
        pwd = MD5.getMD5ofStr(pwd);
        User m = User.dao.login(userno, pwd, company.getStr("id"));
        if (m != null) {

            String uid = m.getStr("id");
            String ip = this.getRequest().getHeader("X-Real-IP");
            if (StringUtils.isEmpty(ip)) {
                ip = this.getRequest().getRemoteAddr();
            }
            int status = m.getInt("status");
            if (status == 0) {
                this.rendJson(false, null, "此用户被禁用，请联系公司管理员！");
                return;
            }
            String nowStr = dateTimeFormat.format(new Date());
            m.upLogin(m.getStr("id"), ip, nowStr);
            int maxTime = 1800;
            boolean autoLogin = this.getParaToBoolean("autoLogin", false);
            this.setCookie("autoLogin", autoLogin + "", 604800);
            if (autoLogin) {
                maxTime = 604800;//保持一星期
            }
            Map<String, Object> userMap = new HashMap<String, Object>();
            userMap.put("loginTime", nowStr);
            userMap.put("ip", ip);
            userMap.put("uid", uid);
            userMap.put("company_id", company.getStr("id"));
            userMap.put("company_name", company.getStr("name"));
            userMap.put("position_id", m.get("position_id") == null ? "" : m.get("position_id"));
            userMap.put("position_name", m.get("position_name") == null ? "" : m.get("position_id"));
            userMap.put("department_id", m.get("department_id") == null ? "" : m.get("position_id"));
            userMap.put("department_name", m.get("department_name") == null ? "" : m.get("p	osition_id"));
            this.setCookie(PropertiesContent.get("cookie_field_key"), CipherUtil.encryptData(JSON.toJSONString(userMap)), maxTime);
//            changeCity(company.getStr("id"));
            this.rendJson(true, null, "登录成功");
            return;
        } else {
            this.rendJson(false, null, "用户名或密码错误！");
            return;
        }
    }


    /**
     * by chenjianhui
     * 2015年9月6日10:13:37
     * APP 服务端请求
     */
    public void loginApp() {
        String userno = this.getPara("login_name");
        String pwd = this.getPara("pwd");
        String password = this.getPara("password");
        String appPassWord = "";
        if (password != null) {
            pwd = password;
            appPassWord = password;
        }
        String company_name = this.getPara("company_name");
        String companyName = this.getPara("company");
        if (company_name != null) {
            companyName = company_name;
        }
        Company company = null;
        company = Company.dao.qryCompanyByName(companyName);
        //String type = this.getPara("request_type");
        if (company == null) {
            rendJsonApp(false, null, 500, "公司不存在！");
            return;
        }
        pwd = MD5.getMD5ofStr(pwd);
        User m = User.dao.login(userno, pwd, company.getStr("id"));
        if (m != null) {

            String uid = m.getStr("id");
            String ip = this.getRequest().getHeader("X-Real-IP");
            if (StringUtils.isEmpty(ip)) {
                ip = this.getRequest().getRemoteAddr();
            }
            int status = m.getInt("status");
            if (status == 0) {
//                Map<String, Object> userMaps = new HashMap<String, Object>();
//                userMaps.put("msg", );
                this.rendJsonApp(true, null, 500, "该用户已被禁用！联系管理员解除禁用！");
                return;
            }
            String nowStr = dateTimeFormat.format(new Date());
            m.upLogin(m.getStr("id"), ip, nowStr);
            int maxTime = 1800;
            boolean autoLogin = this.getParaToBoolean("autoLogin", false);
            this.setCookie("autoLogin", autoLogin + "", 604800);
            if (autoLogin) {
                maxTime = 604800;//保持一星期
            }
            User userInfo = m.findById(uid);
            userInfo.set("password", appPassWord);
            userInfo.set("company_name", companyName);
            /*-------------------------*/
            Map cookieMap = new HashMap<>();
            cookieMap.put("loginTime", nowStr);
            cookieMap.put("ip", ip);
            cookieMap.put("username", userInfo.get("uname"));
            cookieMap.put("uid", uid);
            cookieMap.put("company_id", company.getStr("id"));
            cookieMap.put("company_name", company.getStr("name"));
            cookieMap.put("position_id", m.get("position_id") == null ? "" : m.get("position_id"));
            cookieMap.put("position_name", m.get("position_name") == null ? "" : m.get("position_id"));
            cookieMap.put("department_id", m.get("department_id") == null ? "" : m.get("position_id"));
            cookieMap.put("department_name", m.get("department_name") == null ? "" : m.get("position_id"));
            this.setCookie(PropertiesContent.get("cookie_field_key"), CipherUtil.encryptData(JSON.toJSONString(cookieMap)), maxTime);
//            this.setCookie(PropertiesContent.get("cookie_field_key"), CipherUtil.encryptData(JSON.toJSONString(userMap)), maxTime);
            List<Map<String, Object>> list = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            map = userInfo.getAttrs();
            Person person = Person.dao.findById(userInfo.get("id"));
            if (person != null) {
                map.put("mobile", person.get("mobile"));
            }

            list.add(map);
            this.rendJsonApp(true, list, 200, "success");
            return;
        } else {
            List<Map<String, Object>> temp = new ArrayList<>();
            this.rendJsonApp(false, temp, 500, "用户名或密码错误！");
            return;
        }
    }

    @ClearInterceptor(ClearLayer.ALL)
    public void logout() {
        this.removeCookie(PropertiesContent.get("cookie_field_key"));
        this.removeCookie("autoLogin");
//		this.renderHTML("login");
        this.redirect("/");
    }


    public void changeCity(String companyId) {
        List<Customer> customers = Customer.dao.dataMap(companyId);
        File xls = new File("C:\\Users\\Chao\\Desktop\\new.xls");
        FileInputStream is = null;
        try {
            is = new FileInputStream(xls);
            Workbook wb = null;
            try {
                wb = new HSSFWorkbook(is);
            } catch (Exception e) {
                try {
                    wb = new XSSFWorkbook(is);
                } catch (Exception e1) {
                    is.close();
                    return;
                }
            }
            Sheet sheet = wb.getSheetAt(0);
            int rowLength = sheet.getLastRowNum();
            int i;
            Cell firstCell = null;
            Cell lastCell = null;
            for(i=0;i<rowLength;i++){
                Row row = sheet.getRow(i);
                if(row != null){
                    firstCell = row.getCell(0);
                    lastCell = row.getCell(1);
                    if(firstCell == null || lastCell == null){
                        continue;
                    }else{
                        firstCell.setCellType(Cell.CELL_TYPE_STRING);
                        lastCell.setCellType(Cell.CELL_TYPE_STRING);
                    }
//                    Customer customer = Customer.dao.findCustomerByName(firstCell.getStringCellValue(), this.getCompanyId());
                    for(Customer c : customers){
                        if(c.getStr("name").replaceAll(" +","").equals(firstCell.getStringCellValue().replaceAll(" +",""))){
                            c.set("address",lastCell.getStringCellValue());
                            c.update();
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
