package net.loyin.model.sso;

import java.util.*;

import com.jfinal.plugin.activerecord.Page;
import net.loyin.ctrl.BaseController;
import org.apache.commons.lang3.StringUtils;

import net.loyin.jfinal.anatation.TableBind;

import com.alibaba.fastjson.JSON;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

/**
 * 企业
 *
 * @author liugf 风行工作室
 */
@TableBind(name="sso_company")
public class Company extends Model<Company> {
    private static final long serialVersionUID = -584471063844941313L;
    public static final String tableName = "sso_company";
    public static Company dao = new Company();

    /**
     * 判断是否存在公司
     *
     * @param companyName
     * @return boolean
     */
    public boolean extisCompany(String companyName) {
        return qryCompanyByName(companyName) != null;
    }

    public Company qryCacheById(String id) {
        List<Company> list = this.findByCache("oneday", "company" + id, "select * from " + tableName + " where id=?", id);
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * 查询公司
     *
     * @param companyName
     * @return boolean
     */
    public Company qryCompanyByName(String companyName) {
        return this.findFirst("select * from " + tableName + " where name=? ", companyName);
    }

    public Company qryById(String company_id) {
        return this.findFirst("select * from " + tableName + " where id=? ", company_id);
    }

    public Company qryCompanyByCode(String code) {
        return this.findFirst("select * from " + tableName + " where code=? ", code);
    }


    public List<Company> qryAll() {
        return this.find("select * from " + tableName);
    }

    /**
     * 企业系统设置
     */
    public void updateConfig(String config, String companyId) {
        Db.update("update " + tableName + " set config=? where id=?", config, companyId);
    }

    /**
     * 更新企业
     */
    public void updateCompany(String config, String companyId) {
        Db.update("update " + tableName + " set config=? where id=?", config, companyId);
    }


    public Map<String, Object> getConfig() {
        String config = this.getStr("config");
        if (StringUtils.isEmpty(config)) {
            return new HashMap<String, Object>();
        }
        Map<String, Object> m = (Map<String, Object>) JSON.parse(config);
        return m;
    }

    /**
     * 分页条件查询
     *
     * @param pageNo
     * @param pageSize
     * @param filter   参数 为:k,v==>字段,值
     * @param qryType  查询类型
     * @return
     */
    public Page<Company> pageGrid(int pageNo, int pageSize, Map<String, Object> filter) {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        String userView = Parame.tableName;
        List<Object> parame = new ArrayList<Object>();
        StringBuffer sql = new StringBuffer(" from ");
        sql.append(tableName);
        sql.append(" t left join ");
        sql.append(userView);
        sql.append(" h on h.id=t.industry ");
        sql.append(" where 1=1 ");
        String keyword = (String) filter.get("keyword");
        if (StringUtils.isNotEmpty(keyword)) {
            sql.append(" and (t.name like ? or t.code like ?)");
            keyword = "%" + keyword + "%";
            parame.add(keyword);
            parame.add(keyword);
        }

        String start_date = (String) filter.get("start_date");
        if (StringUtils.isNotEmpty(start_date)) {
            sql.append(" and t.expiry_date >= ?");
            parame.add(start_date + " 00:00:00");
        }
        String end_date = (String) filter.get("end_date");
        if (StringUtils.isNotEmpty(end_date)) {
            sql.append(" and t.expiry_date <= ?");
            parame.add(end_date + " 23:59:59");
        }
        Integer is_deleted = (Integer) filter.get("is_deleted");//是否删除
        if (is_deleted != null) {
            sql.append(" and t.is_deleted = ?");
            parame.add(is_deleted);
        }
        String sortField = (String) filter.get("_sortField");
        if (StringUtils.isNotEmpty(sortField)) {
            String sort = (String) filter.get("_sort");
            sql.append(" order by ");
            sql.append(sortField);
            sql.append(" ");
            sql.append(sort);
        }
        return dao.paginate(pageNo, pageSize, "select t.*,h.name as hyname ", sql.toString(), parame.toArray());
    }
}
