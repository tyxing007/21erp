package net.loyin.ctrl.sso;

import java.text.SimpleDateFormat;
import java.util.*;

import com.jfinal.plugin.activerecord.Page;
import net.loyin.ctrl.AdminBaseController;
import net.loyin.jfinal.anatation.RouteBind;
import net.loyin.model.crm.Customer;
import net.loyin.model.sso.Company;

import net.loyin.model.sso.Parame;
import net.loyin.model.sso.SnCreater;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.jfinal.plugin.ehcache.CacheKit;

/**
 * 企业设置
 *
 * @author liugf 风行工作室 2014年10月9日
 */
@RouteBind(path = "company")
public class CompanyCtrl extends AdminBaseController<Company> {
    public CompanyCtrl() {
        this.modelClass = Company.class;
    }

    /**
     * 获取公司信息*
     */
    public void dataGrid() {
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put("keyword", this.getPara("keyword"));
        filter.put("start_date", this.getPara("start_date"));
        filter.put("end_date", this.getPara("end_date"));
        this.sortField(filter);
        Page<Company> page = Company.dao.pageGrid(getPageNo(), getPageSize(), filter);
        this.rendJson(true, null, "success", page);
    }

    /**
     * 查询当前用户企业
     */
    public void qry() {
        String companyName = getPara("companyName");
        String company_id = this.getCompanyId();
        if (companyName != null) {
            company_id = Company.dao.qryCompanyByName(companyName).getStr("id");
        }

        Company company = Company.dao.findById(company_id);
        Map<String, Object> map = new HashMap<String, Object>();
        String config = company.getStr("config");
        if (StringUtils.isNotEmpty(config)) {
            map = (Map<String, Object>) JSON.parse(config);
            if(!map.containsKey("p_auto_average") || (map.containsKey("p_auto_average") && map.get("p_auto_average").equals("false"))){
                map.put("p_auto_average", false);
                map.put("p_averageJob", 15);
            }
            if(!map.containsKey("p_sale_audit") || (map.containsKey("p_sale_audit") && map.get("p_sale_audit").equals("false"))){
                map.put("p_sale_audit", false);
                map.put("p_sale_audit_type", "1");
                map.put("p_finance", "000");

            }
            if(!map.containsKey("p_saletui_audit") || (map.containsKey("p_saletui_audit") && map.get("p_saletui_audit").equals("false"))){
                map.put("p_saletui_audit", false);
                map.put("p_saletui_audit_type", "1");
                map.put("p_financetui", "000");
            }
        } else {
            map.put("p_member_card_pay", false);
            map.put("p_sale_audit", false);
            map.put("p_sale_audit_type", "1");
            map.put("p_saletui_audit", false);
            map.put("p_saletui_audit_type", "1");
            map.put("p_pact_alarm", "");
            map.put("p_custpool_c", "");
            map.put("p_auto_average", false);
            map.put("p_averageJob", 15);
        }
        company.set("config", map);
        this.rendJson(true, null, "", company);
    }

    /**
     * 查询对应企业信息
     */
    public void qryOp() {
        String company_id = this.getId();
        if (this.getId() == null || StringUtils.isEmpty(company_id)) {
            company_id = this.getCompanyId();
        }
        Company company = Company.dao.findById(company_id);
        if (company != null) {
            String industry = company.getStr("industry");
            if (industry != null) {
                Parame parame = Parame.dao.findById(industry);
                if (parame != null) {
                    industry = parame.getStr("name");
                    company.put("industry_name", industry);
                }
            }
        }
        Map newMap = new HashMap<>();
        newMap.put("company", company);
        this.rendJson(true, null, "", company);
    }

    /**
     * 查询企业设置 即系统设置
     */
    public void qryConfig() {
        String company_id = this.getCompanyId();
        Company company = Company.dao.findById(company_id);
        Map<String, Object> map = new HashMap<String, Object>();
        String config = company.getStr("config");
        if (StringUtils.isNotEmpty(config)) {
            map = (Map<String, Object>) JSON.parse(config);
        }
        this.rendJson(true, null, "", map);
    }

    /**
     * 保存系统设置
     */
    public void saveConfig() {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            Enumeration<String> attrs = this.getAttrNames();
            while (attrs.hasMoreElements()) {
                String name = (String) attrs.nextElement();
                if (name.startsWith("config[p_")) {
                    map.put(name.replace("config[", "").replace("]", ""), this.getAttr(name));
                }
            }
            if (map.keySet().isEmpty()) {
                this.rendJson(false, null, "未提交数据");
                return;
            }
            String code = getPara("code");
            Company company = Company.dao.qryCompanyByCode(code);
            company.set("name", this.getPara("name"));
            company.set("short_name", this.getPara("short_name"));
            company.set("industry", this.getPara("industry"));
            company.set("province", this.getPara("province"));
            company.set("city", this.getPara("city"));
            company.set("address", this.getPara("address"));
            company.set("fax", this.getPara("fax"));
            company.set("telephone", this.getPara("telephone"));
            company.set("config", JSON.toJSONString(map));
            company.update();
            this.rendJson(true, null, "保存企业设置成功！");
            CacheKit.remove("oneday", "company" + company.get("id"));
        } catch (Exception e) {
            log.error(e);
            this.rendJson(false, null, "保存异常！");
        }
    }


    /**
     * 保存开户
     */
    public void saveCompany() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Company po = (Company) getModel();
            if (po == null) {
                this.rendJson(false, null, "提交数据错误！");
                return;
            }
            if (Company.dao.extisCompany(po.getStr("name"))) {
                this.rendJson(false, null, "该企业已经存在！");
                return;
            }
            String uid = this.getCurrentUserId();
            this.pullUser(po, uid);
            String code = SnCreater.dao.create("COMPANYCODE", this.getCompanyId());
            po.set("code", code);
            po.set("reg_date", simpleDateFormat.format(new Date()));
            po.set("status", 1);
//			COMPANYCODE
            po.save();
            String companyId = po.getStr("id");
            createSn(companyId);
            this.rendJson(true, null, "操作成功！");
        } catch (Exception e) {
            e.printStackTrace();
            this.rendJson(false, null, "后台出错，请联系管理员，错误代码001");
        }
    }

    public void createSn(String companyId) {
        List<SnCreater> list = SnCreater.dao.list(this.getCompanyId());
        for (SnCreater snCreater : list) {
            if (snCreater.getStr("code").equals("COMPANYCODE")) {
                continue;
            }
            snCreater.set("company_id", companyId);
//			snCreater.set("id","");
            snCreater.remove("id");
            snCreater.remove("udate");
            snCreater.set("dqxh", 1);
            snCreater.save();
        }
    }

    /**
     * 更新开户*
     */
    public void updateCompany() {
        try {
            Company po = (Company) getModel();
            if (po == null) {
                this.rendJson(false, null, "提交数据错误！");
                return;
            }
            if (!Company.dao.extisCompany(po.getStr("name"))) {
                this.rendJson(false, null, "该企业不存在！");
                return;
            }
            po.set("status", 1);
            po.update();
            this.rendJson(true, null, "操作成功！");
        } catch (Exception e) {
            e.printStackTrace();
            this.rendJson(false, null, "后台出错，请联系管理员，错误代码001");
        }
    }


}
