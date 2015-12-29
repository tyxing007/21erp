package net.loyin.model.crm;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import net.loyin.ctrl.BaseController;
import net.loyin.jfinal.anatation.TableBind;
import net.loyin.jfinal.model.IdGenerater;
import net.loyin.model.sso.Person;
import net.loyin.model.sso.SnCreater;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * 客户/供应商
 *
 * @author liugf 风行工作室
 */
@TableBind(name = "crm_customer")
public class Customer extends Model<Customer> {
    private static final long serialVersionUID = -4221825254783835788L;
    public static final String tableName = "crm_customer";
    public static Customer dao = new Customer();


    /**
     * by chenjianhui
     * 2015年9月7日11:49:33
     *
     * @param parameter
     * @return 所有的客户信息
     */
    public List<Customer> getList(Map<String, Object> parameter) {


        /*sql查询语句参数条件*/
        List<Object> parame = new ArrayList<Object>();

        String classify = (String) parameter.get("classify");
        String sort = (String) parameter.get("sort");
        String status = (String) parameter.get("state");
        String keyWord = (String) parameter.get("key_word");
        String createId = (String) parameter.get("create_id");
        String id = (String) parameter.get("id");
        String companyId = (String) parameter.get("company_id");

        StringBuffer sql = new StringBuffer();
        sql.append("select * from " + tableName + " t where 1=1 and t.type not IN(0) ");
        /**Id*/
        if (id != null) {

            sql.append(" and t.id = ? ");
            parame.add(id);

        }
        /**分类*/
        if (classify != null) {
            if (classify.equals("all")) {

            } else {
                sql.append(" and t.industry = ? ");
                parame.add(classify);
            }
        }
        /**排序*/
        if (sort != null) {
            if (sort.equals("all")) {

            } else {

            }
        }

        /**状态*/
        if (status != null) {
            if (status.equals("all")) {

            } else {
                sql.append(" and t.rating = ? ");
                parame.add(status);
            }
        }

        /**创建人*/
        if (createId != null) {

            sql.append(" and t.creater_id = ? ");
            parame.add(createId);

        }
        /**公司id*/
        if (companyId != null) {

            sql.append(" and t.company_id = ? ");
            parame.add(companyId);

        }
        /**关键字*/
        if (keyWord != null) {
            sql.append(" and t.name like '%" + keyWord + "%' ");
            /*parame.add(keyWord);*/

        }
        return dao.find(sql.toString(), parame.toArray());
    }


    /**
     * by chenjianhui
     * 2015年9月5日16:46:18
     * 获取所有的客户信息
     *
     * @param companyId 公司id
     * @return
     */
    public List<Customer> dataMap(String companyId) {
        return find("select t.* from " + tableName + " t where t.company_id=?", companyId);
    }

    /**
     * by chenjianhui
     * 2015年9月7日17:41:51
     * 根据查询条件 查询客户信息 用于手机端
     *
     * @param filter 参数 为:k,v==>字段,值
     * @return
     */
    public List<Customer> getListData(Map<String, Object> filter) {

        String sql_ = "select * ";
        List<Object> parame = new ArrayList<Object>();
        StringBuffer sql = new StringBuffer(" from ");
        sql.append(tableName);


        if (filter.size() > 0) {
            sql.append(" where 1=1 ");
            String company_id = (String) filter.get("company_id");//当前用户id
            String industry_id = (String) filter.get("industry_id");//分类id
            String type_id = (String) filter.get("type_id");//当前用户岗位id
            if (StringUtils.isNotEmpty(industry_id)) {

                sql.append(" and industry=? ");
                parame.add(industry_id);
            }
            if (StringUtils.isNotEmpty(company_id)) {

                sql.append(" and company_id=? ");
                parame.add(company_id);
            }

            if (StringUtils.isNotEmpty(type_id)) {

                sql.append(" and type_id=? ");
                parame.add(type_id);
            }
        }

        List<Customer> list = dao.find(sql_ + sql.toString(), parame.toArray());

        return list;
    }


    /**
     * by chenjianhui
     * 2015年9月5日16:47:11
     * 根据查询条件 查询客户信息
     *
     * @param filter 参数 为:k,v==>字段,值
     * @return
     */
    public List<Customer> getListData(Map<String, Object> filter, Integer qryType) {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        String userView = Person.tableName;
        String sql_ = "WITH RECURSIVE d AS (SELECT d1.id,d1.pid,d1.name,d1.sort_num,d1.type FROM v_user_position d1 where d1.id=?"
                + "union ALL SELECT d2.id,d2.pid,d2.name,d2.sort_num,d2.type FROM v_user_position d2, d WHERE d2.pid = d.id) "
                + "SELECT distinct id FROM d where type=10 and d.id not in (select id from sso_user where position_id =? and id!=?)";
        List<Object> parame = new ArrayList<Object>();
        StringBuffer sql = new StringBuffer(" from ");
        sql.append(tableName);
        sql.append(" t left join ");
        sql.append(userView);
        sql.append(" c on c.id=t.creater_id left join ");
        sql.append(userView);
        sql.append(" u on u.id=t.updater_id left join ");
        sql.append(userView);
        sql.append(" h on h.id=t.head_id ");
        sql.append(" where t.company_id=? ");
        parame.add(filter.get("company_id"));
        String user_id = (String) filter.get("user_id");//当前用户id
        String position_id = (String) filter.get("position_id");//当前用户岗位id
        String keyword = (String) filter.get("keyword");
        if (StringUtils.isNotEmpty(keyword)) {
            sql.append(" and (t.name like ? or t.sn like ?)");
            keyword = "%" + keyword + "%";
            parame.add(keyword);
            parame.add(keyword);
        }
        String uid = (String) filter.get("uid");//当前用户查询用户id
        if (StringUtils.isNotEmpty(uid)) {
            sql.append(" and (t.creater_id=? or t.head_id=?)");
            parame.add(uid);
            parame.add(uid);
        }

        String start_date = (String) filter.get("start_date");
        if (StringUtils.isNotEmpty(start_date)) {
            sql.append(" and t.create_datetime >= ?");
            parame.add(start_date + " 00:00:00");
        }
        String end_date = (String) filter.get("end_date");
        if (StringUtils.isNotEmpty(end_date)) {
            sql.append(" and t.create_datetime <= ?");
            parame.add(end_date + " 23:59:59");
        }
        Integer status = (Integer) filter.get("status");//状态
        if (status != null) {
            sql.append(" and t.status = ?");
            parame.add(status);
        }
        Integer is_deleted = (Integer) filter.get("is_deleted");//是否删除
        if (is_deleted != null) {
            sql.append(" and t.is_deleted = ?");
            parame.add(is_deleted);
        }
        Integer type = (Integer) filter.get("type");//状态
        if (type >= 0) {
            sql.append(" and t.type = ?");
            parame.add(type);
        } else {//-1默认查询客户
            sql.append(" and t.type >0 ");
        }
        switch (qryType) {
            case -1://我创建的及我负责的
                sql.append(" and (t.creater_id=? or t.head_id=?)");
                parame.add(user_id);
                parame.add(user_id);
                break;
            case 0://我创建的
                sql.append(" and t.creater_id=?");
                parame.add(user_id);
                break;
            case 1://我负责的
                sql.append(" and t.head_id=?");
                parame.add(user_id);
                break;
            case 2://下属创建的
                sql.append(" and t.creater_id in(");
                sql.append(sql_);
                sql.append(") and t.creater_id !=?");
                parame.add(position_id);
                parame.add(position_id);
                parame.add(user_id);
                parame.add(user_id);
                break;
            case 3://下属负责的
                sql.append(" and t.head_id in(");
                sql.append(sql_);
                sql.append(") and t.head_id !=?");
                parame.add(position_id);
                parame.add(position_id);
                parame.add(user_id);
                parame.add(user_id);
                break;
            case 5://一周未跟进  下属及本人的负责的
                sql.append(" and t.head_id in(");
                sql.append(sql_);
                sql.append(")");
                parame.add(position_id);
                parame.add(position_id);
                parame.add(user_id);
                sql.append(" and t.id not in(select customer_id from crm_contact_record c where c.create_datetime >=?and c.create_datetime<=? )");
                sql.append(" and t.id in(select customer_id from crm_contact_record c)");
                cal.add(Calendar.DATE, -7);
                parame.add(BaseController.dateFormat.format(cal.getTime()) + " 00:00:00");
                parame.add(BaseController.dateFormat.format(now) + " 23:59:59");
                break;
            case 6://半月未跟进  下属及本人的负责的
                sql.append(" and t.head_id in(");
                sql.append(sql_);
                sql.append(")");
                parame.add(position_id);
                parame.add(position_id);
                parame.add(user_id);
                sql.append(" and t.id not in(select customer_id from crm_contact_record c where c.create_datetime >=?and c.create_datetime<=? )");
                sql.append(" and t.id in(select customer_id from crm_contact_record c)");
                cal.add(Calendar.DATE, -15);
                parame.add(BaseController.dateFormat.format(cal.getTime()) + " 00:00:00");
                parame.add(BaseController.dateFormat.format(now) + " 23:59:59");
                break;
            case 7://一直未跟进  下属及本人的负责的
                sql.append(" and t.head_id in(");
                sql.append(sql_);
                sql.append(")");
                parame.add(position_id);
                parame.add(position_id);
                parame.add(user_id);
                sql.append(" and t.id not in(select customer_id from crm_contact_record c)");
                break;
            case 9://已购买 下属及本人的负责的
                sql.append(" and t.head_id in(");
                sql.append(sql_);
                sql.append(")");
                parame.add(position_id);
                parame.add(position_id);
                parame.add(user_id);
                sql.append(" and t.id in(select customer_id from scm_order c )");
                break;
            case 10://未购买下属及本人负责的
                sql.append(" and t.head_id in(");
                sql.append(sql_);
                sql.append(")");
                parame.add(position_id);
                parame.add(position_id);
                parame.add(user_id);
                sql.append(" and t.id not in(select customer_id from scm_order c )");
                break;
        }
        String sortField = (String) filter.get("_sortField");
        if (StringUtils.isNotEmpty(sortField)) {
            String sort = (String) filter.get("_sort");
            sql.append(" order by ");
            sql.append(sortField);
            sql.append(" ");
            sql.append(sort);
        }
        List<Customer> list = dao.find("select * " + sql.toString(), parame.toArray());

        return list;
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
    public Page<Customer> pageGrid(int pageNo, int pageSize, Map<String, Object> filter, Integer qryType) {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        String userView = Person.tableName;
        String sql_ = "WITH RECURSIVE d AS (SELECT d1.id,d1.pid,d1.name,d1.sort_num,d1.type FROM v_user_position d1 where d1.id=?"
                + "union ALL SELECT d2.id,d2.pid,d2.name,d2.sort_num,d2.type FROM v_user_position d2, d WHERE d2.pid = d.id) "
                + "SELECT distinct id FROM d where type=10 and d.id not in (select id from sso_user where position_id =? and id!=?)";
        List<Object> parame = new ArrayList<Object>();
        StringBuffer sql = new StringBuffer(" from ");
        sql.append(tableName);
        sql.append(" t left join ");
        sql.append(userView);
        sql.append(" c on c.id=t.creater_id left join ");
        sql.append(userView);
        sql.append(" u on u.id=t.updater_id left join ");
        sql.append(userView);
        sql.append(" h on h.id=t.head_id left join ");
        sql.append(Contacts.tableName);
        sql.append(" con on con.customer_id = t.id ");
        sql.append(" where t.company_id=? ");
        parame.add(filter.get("company_id"));
        sql.append(" and con.is_main = 1");
        String user_id = (String) filter.get("user_id");//当前用户id
        String position_id = (String) filter.get("position_id");//当前用户岗位id
        String keyword = (String) filter.get("keyword");
        if (StringUtils.isNotEmpty(keyword)) {
            String key[] = keyword.split(" ");
            for(String k : key){
                sql.append(" and (t.name like ? or t.sn like ?)");
                keyword = "%" + k + "%";
                parame.add(keyword);
                parame.add(keyword);
            }
        }
        String uid = (String) filter.get("uid");//当前用户查询用户id
        if (StringUtils.isNotEmpty(uid)) {
//            sql.append(" and (t.creater_id=? or t.head_id=?)");
//            parame.add(uid);
//            parame.add(uid);
            user_id = uid;
        }

        String start_date = (String) filter.get("start_date");
        if (StringUtils.isNotEmpty(start_date)) {
            sql.append(" and t.create_datetime >= ?");
            parame.add(start_date + " 00:00:00");
        }
        String end_date = (String) filter.get("end_date");
        if (StringUtils.isNotEmpty(end_date)) {
            sql.append(" and t.create_datetime <= ?");
            parame.add(end_date + " 23:59:59");
        }
        Integer status = (Integer) filter.getOrDefault("status",0);//状态
        if (status != null) {
            sql.append(" and t.status = ?");
            parame.add(status);
        }
        Integer is_deleted = (Integer) filter.get("is_deleted");//是否删除
        if (is_deleted != null) {
            sql.append(" and t.is_deleted = ?");
            parame.add(is_deleted);
        }

        Integer type = (Integer) filter.get("type");//状态
        if (type == null) {
            sql.append(" and t.type not in (0)");
        } else if (type >= 0) {
            sql.append(" and t.type = ?");
            parame.add(type);
        } else {//-1默认查询客户
            sql.append(" and t.type >0 ");
        }
        /*-------------------------*/
        /*分类*/
        String classify = (String) filter.get("classify");
        if (StringUtils.isNotEmpty(classify)) {
            if (classify.equals("all")) {

            } else {
                sql.append(" and t.industry = ? ");
                parame.add(classify);
            }
        }
        /*网点ID*/
        String id = (String) filter.get("id");
        if (StringUtils.isNotEmpty(id)) {
            sql.append(" and t.id = ? ");
            parame.add(id);
        }
        /*排序*/
        String sort1 = (String) filter.get("sort");
        if (StringUtils.isNotEmpty(sort1)) {
            if (sort1.equals("all")) {

            } else {
                sql.append(" and t.rating = ? ");
                parame.add(sort1);
            }
        }
        /*级别*/
        String status1 = (String) filter.get("status_app");
        if (StringUtils.isNotEmpty(status1)) {
            if (status1.equals("all")) {

            } else {
                sql.append(" and t.rating = ? ");
                parame.add(status1);
            }
        }

        /*-------------------------*/

        switch (qryType) {
            case -1://我创建的及我负责的
                sql.append(" and (t.creater_id=? or t.head_id=? )");
                parame.add(user_id);
                parame.add(user_id);
                break;
            case 0://我创建的
                sql.append(" and t.creater_id=?");
                parame.add(user_id);
                break;
            case 1://我负责的
                sql.append(" and t.head_id=?");
                parame.add(user_id);
                break;
            case 2://下属创建的
                sql.append(" and t.creater_id in(");
                sql.append(sql_);
                sql.append(") and t.creater_id !=?");
                parame.add(position_id);
                parame.add(position_id);
                parame.add(user_id);
                parame.add(user_id);
                break;
            case 3://下属负责的
                sql.append(" and t.head_id in(");
                sql.append(sql_);
                sql.append(") and t.head_id !=?");
                parame.add(position_id);
                parame.add(position_id);
                parame.add(user_id);
                parame.add(user_id);
                break;
            case 5://一周未跟进  下属及本人的负责的
                sql.append(" and t.head_id in(");
                sql.append(sql_);
                sql.append(")");
                parame.add(position_id);
                parame.add(position_id);
                parame.add(user_id);
                sql.append(" and t.id not in(select customer_id from crm_contact_record c where c.create_datetime >=?and c.create_datetime<=? )");
                sql.append(" and t.id in(select customer_id from crm_contact_record c)");
                cal.add(Calendar.DATE, -7);
                parame.add(BaseController.dateFormat.format(cal.getTime()) + " 00:00:00");
                parame.add(BaseController.dateFormat.format(now) + " 23:59:59");
                break;
            case 6://半月未跟进  下属及本人的负责的
                sql.append(" and t.head_id in(");
                sql.append(sql_);
                sql.append(")");
                parame.add(position_id);
                parame.add(position_id);
                parame.add(user_id);
                sql.append(" and t.id not in(select customer_id from crm_contact_record c where c.create_datetime >=?and c.create_datetime<=? )");
                sql.append(" and t.id in(select customer_id from crm_contact_record c)");
                cal.add(Calendar.DATE, -15);
                parame.add(BaseController.dateFormat.format(cal.getTime()) + " 00:00:00");
                parame.add(BaseController.dateFormat.format(now) + " 23:59:59");
                break;
            case 7://一直未跟进  下属及本人的负责的
                sql.append(" and t.head_id in(");
                sql.append(sql_);
                sql.append(")");
                parame.add(position_id);
                parame.add(position_id);
                parame.add(user_id);
                sql.append(" and t.id not in(select customer_id from crm_contact_record c)");
                break;
            case 9://已购买 下属及本人的负责的
                sql.append(" and t.head_id in(");
                sql.append(sql_);
                sql.append(")");
                parame.add(position_id);
                parame.add(position_id);
                parame.add(user_id);
                sql.append(" and t.id in(select customer_id from scm_order c )");
                break;
            case 10://未购买下属及本人负责的
                sql.append(" and t.head_id in(");
                sql.append(sql_);
                sql.append(")");
                parame.add(position_id);
                parame.add(position_id);
                parame.add(user_id);
                sql.append(" and t.id not in(select customer_id from scm_order c )");
                break;
            case 11://下属负责的
                sql.append(" and ( t.head_id in(");
                sql.append(sql_);
                sql.append(") or t.creater_id in (");
                sql.append(sql_);
                sql.append(") )");
                parame.add(position_id);
                parame.add(position_id);
                parame.add(user_id);
                parame.add(position_id);
                parame.add(position_id);
                parame.add(user_id);
                break;
        }
        String sortField = (String) filter.get("_sortField");
        if (StringUtils.isNotEmpty(sortField)) {
            String sort = (String) filter.get("_sort");
            sql.append(" order by ");
            sql.append(sortField);
            sql.append(" ");
            sql.append(sort);
        }
        Page<Customer> page = dao.paginate(pageNo, pageSize, "select t.*,h.realname as head_name,c.realname as creator_name,u.realname as updater_name,con.name as contacts_name,con.telephone as conphone,con.mobile as conmobile", sql.toString(), parame.toArray());
//        contacts
        return page;
    }

    /**
     * 查询客户信息及联系人信息
     *
     * @param filter
     * @param qryType
     * @return
     */
    public List<Record> findCustAndContactList(Map<String, Object> filter, Integer qryType) {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        String userView = Person.tableName;
        String sql_ = "WITH RECURSIVE d AS (SELECT d1.id,d1.pid,d1.name,d1.sort_num,d1.type FROM v_user_position d1 where d1.id=?"
                + "union ALL SELECT d2.id,d2.pid,d2.name,d2.sort_num,d2.type FROM v_user_position d2, d WHERE d2.pid = d.id) "
                + "SELECT distinct id FROM d where type=10 and d.id not in (select id from sso_user where position_id =? and id!=?)";
        List<Object> parame = new ArrayList<Object>();
        StringBuffer sql = new StringBuffer("select distinct t.*,h.realname as head_name,");
        sql.append("ct.name ctname,ct.mobile,ct.post,ct.department,ct.sex,ct.saltname,ct.telephone cttelephone,ct.email ctemail,ct.qq,ct.zip_code ctzip_code,ct.type cttype,ct.idcard ");
        sql.append(" from ");
        sql.append(tableName);
        sql.append(" t left join ");
        sql.append(Contacts.tableName);
        sql.append(" ct on ct.customer_id=t.id left join ");
        sql.append(userView);
        sql.append(" h on h.id=t.head_id ");
        sql.append(" where t.company_id=? ");
        parame.add(filter.get("company_id"));
        String user_id = (String) filter.get("user_id");//当前用户id
        String position_id = (String) filter.get("position_id");//当前用户岗位id
        String keyword = (String) filter.get("keyword");
        if (StringUtils.isNotEmpty(keyword)) {
            sql.append(" and (t.name like ? or t.sn like ?)");
            keyword = "%" + keyword + "%";
            parame.add(keyword);
            parame.add(keyword);
        }
        String uid = (String) filter.get("uid");//当前用户查询用户id
        if (StringUtils.isNotEmpty(uid)) {
            sql.append(" and (t.creater_id=? or t.head_id=?)");
            parame.add(uid);
            parame.add(uid);
        }

        String start_date = (String) filter.get("start_date");
        if (StringUtils.isNotEmpty(start_date)) {
            sql.append(" and t.create_datetime >= ?");
            parame.add(start_date + " 00:00:00");
        }
        String end_date = (String) filter.get("end_date");
        if (StringUtils.isNotEmpty(end_date)) {
            sql.append(" and t.create_datetime <= ?");
            parame.add(end_date + " 23:59:59");
        }
        Integer status = (Integer) filter.get("status");//状态
        if (status != null) {
            sql.append(" and t.status = ?");
            parame.add(status);
        }
        Integer is_deleted = (Integer) filter.get("is_deleted");//是否删除
        if (is_deleted != null) {
            sql.append(" and t.is_deleted = ?");
            parame.add(is_deleted);
        }
        Integer type = (Integer) filter.get("type");//状态
        if (type >= 0) {
            sql.append(" and t.type = ?");
            parame.add(type);
        } else {//-1默认查询客户
            sql.append(" and t.type >0 ");
        }
        switch (qryType) {
            case -1://我创建的及我负责的
                sql.append(" and (t.creater_id=? or t.head_id=?)");
                parame.add(user_id);
                parame.add(user_id);
                break;
            case 0://我创建的
                sql.append(" and t.creater_id=?");
                parame.add(user_id);
                break;
            case 1://我负责的
                sql.append(" and t.head_id=?");
                parame.add(user_id);
                break;
            case 2://下属创建的
                sql.append(" and t.creater_id in(");
                sql.append(sql_);
                sql.append(") and t.creater_id !=?");
                parame.add(position_id);
                parame.add(position_id);
                parame.add(user_id);
                parame.add(user_id);
                break;
            case 3://下属负责的
                sql.append(" and t.head_id in(");
                sql.append(sql_);
                sql.append(") and t.head_id !=?");
                parame.add(position_id);
                parame.add(position_id);
                parame.add(user_id);
                parame.add(user_id);
                break;
            case 5://一周未跟进  下属及本人的负责的
                sql.append(" and t.head_id in(");
                sql.append(sql_);
                sql.append(")");
                parame.add(position_id);
                parame.add(position_id);
                parame.add(user_id);
                sql.append(" and t.id not in(select customer_id from crm_contact_record c where c.create_datetime >=?and c.create_datetime<=? )");
                sql.append(" and t.id in(select customer_id from crm_contact_record c)");
                cal.add(Calendar.DATE, -7);
                parame.add(BaseController.dateFormat.format(cal.getTime()) + " 00:00:00");
                parame.add(BaseController.dateFormat.format(now) + " 23:59:59");
                break;
            case 6://半月未跟进  下属及本人的负责的
                sql.append(" and t.head_id in(");
                sql.append(sql_);
                sql.append(")");
                parame.add(position_id);
                parame.add(position_id);
                parame.add(user_id);
                sql.append(" and t.id not in(select customer_id from crm_contact_record c where c.create_datetime >=?and c.create_datetime<=? )");
                sql.append(" and t.id in(select customer_id from crm_contact_record c)");
                cal.add(Calendar.DATE, -15);
                parame.add(BaseController.dateFormat.format(cal.getTime()) + " 00:00:00");
                parame.add(BaseController.dateFormat.format(now) + " 23:59:59");
                break;
            case 7://一直未跟进  下属及本人的负责的
                sql.append(" and t.head_id in(");
                sql.append(sql_);
                sql.append(")");
                parame.add(position_id);
                parame.add(position_id);
                parame.add(user_id);
                sql.append(" and t.id not in(select customer_id from crm_contact_record c)");
                break;
            case 9://已购买 下属及本人的负责的
                sql.append(" and t.head_id in(");
                sql.append(sql_);
                sql.append(")");
                parame.add(position_id);
                parame.add(position_id);
                parame.add(user_id);
                sql.append(" and t.id in(select customer_id from scm_order c )");
                break;
            case 10://未购买下属及本人负责的
                sql.append(" and t.head_id in(");
                sql.append(sql_);
                sql.append(")");
                parame.add(position_id);
                parame.add(position_id);
                parame.add(user_id);
                sql.append(" and t.id not in(select customer_id from scm_order c )");
                break;
        }
        sql.append(" order by t.sn desc");
        return Db.find(sql.toString(), parame.toArray());
    }

    /**
     * 直接删除
     */
    @Before(Tx.class)
    public void del(String id, String company_id) {
        if (StringUtils.isNotEmpty(id)) {
            String[] ids = id.split(",");
            StringBuffer ids_ = new StringBuffer();
            List<String> parame = new ArrayList<String>();
            for (String id_ : ids) {
                ids_.append("?,");
                parame.add(id_);
            }
            ids_.append("'-'");
            Db.update("delete  from " + CustomerData.tableName + " where id in (" + ids_.toString() + ")", parame.toArray());
            parame.add(company_id);
            Db.update("delete  from " + Contacts.tableName + " where customer_id in (" + ids_.toString() + ") and company_id=? ", parame.toArray());
            Db.update("delete  from " + tableName + " where id in (" + ids_.toString() + ") and company_id=? ", parame.toArray());
        }
    }

    /**
     * 回收站
     */
    public void trash(String id, String uid, String company_id, String delete_datatime) {
        if (StringUtils.isNotEmpty(id)) {
            String[] ids = id.split(",");
            StringBuffer ids_ = new StringBuffer();
            List<String> parame = new ArrayList<String>();
            parame.add(uid);
            parame.add(delete_datatime);
            for (String id_ : ids) {
                ids_.append("?,");
                parame.add(id_);
            }
            ids_.append("'-'");
            parame.add(company_id);
            Db.update("update " + tableName + " set is_deleted=1,deleter_id=?,delete_datetime=? where id in (" + ids_.toString() + ") and company_id=? ", parame.toArray());
        }
    }

    public boolean existCust(String name, String id, int type, String company_id) {
        Customer c = null;
        if (StringUtils.isEmpty(id)) {
            c = dao.findFirst("select * from " + tableName + " where type=? and name=? and company_id=?", type, name, company_id);
        } else {
            c = dao.findFirst("select * from " + tableName + " where id!=? and type=? and name=? and company_id=?", id, type, name, company_id);
        }
        return c != null;
    }

    public Customer findCustomerByName(String name, String company_id) {
        Customer c = dao.findFirst("select * from " + tableName + " where name=? and company_id=?", name, company_id);
        return c;
    }

    public Customer findById(String id, String company_id) {
        String userView = Person.tableName;
        StringBuffer sql = new StringBuffer();
        sql.append(tableName);
        sql.append(" t left join ");
        sql.append(userView);
        sql.append(" c on c.id=t.creater_id left join ");
        sql.append(userView);
        sql.append(" u on u.id=t.updater_id left join ");
        sql.append(userView);
        sql.append(" h on h.id=t.head_id ");
        return dao.findFirst("select t.*,d.remark,d.data,h.realname as head_name,c.realname as creater_name,u.realname as updater_name from " + sql.toString() + " left join " + CustomerData.tableName + " d on d.id=t.id where t.company_id=? and t.id=?", company_id, id);
    }

    /**
     * 恢复
     */
    public void reply(String id, String company_id) {
        if (StringUtils.isNotEmpty(id)) {
            String[] ids = id.split(",");
            StringBuffer ids_ = new StringBuffer();
            List<String> parame = new ArrayList<String>();
            for (String id_ : ids) {
                ids_.append("?,");
                parame.add(id_);
            }
            ids_.append("'-'");
            parame.add(company_id);
            Db.update("update " + tableName + " set is_deleted=0,deleter_id=null,delete_datetime=null where id in (" + ids_.toString() + ") and company_id=? ", parame.toArray());
        }
    }

    /**
     * 放入客户池
     */
    public void toPool(String id, String company_id) {
        if (StringUtils.isNotEmpty(id)) {
            String[] ids = id.split(",");
            StringBuffer ids_ = new StringBuffer();
            List<String> parame = new ArrayList<String>();
            for (String id_ : ids) {
                ids_.append("?,");
                parame.add(id_);
            }
            ids_.append("'-'");
            parame.add(company_id);
            Db.update("update " + tableName + " set status=1,head_id=null where id in (" + ids_.toString() + ") and company_id=? ", parame.toArray());
        }
    }

    /**
     * 分配2或领取1
     */
    @Before(Tx.class)
    public void allot(String id, String uid, int type, String now) {
        if (StringUtils.isNotEmpty(id)) {
            String[] ids = id.split(",");
            StringBuffer ids_ = new StringBuffer();
            List<String> parame = new ArrayList<String>();
            parame.add(uid);
            for (String id_ : ids) {
                ids_.append("?,");
                parame.add(id_);
            }
            ids_.append("'-'");
            Db.update("update " + tableName + " set head_id=?,status=0 where id in (" + ids_.toString() + ")", parame.toArray());
            //记录客户流转记录
            for (String id_ : ids) {
                Db.update("INSERT INTO crm_customer_record(id, customer_id, user_id, create_datetime, type) VALUES (?, ?, ?, ?, ?);", IdGenerater.me.getIdSeq(), id_, uid, now, type);
            }
        }
    }

    public int qryCount(String uid) {
        Record r = Db.findFirst("select count(id) ct from " + tableName + " where (creater_id =? or head_id=? ) ", uid, uid);
        if (r != null) {
            Object ct = r.get("ct");
            if (ct != null) {
                return Integer.parseInt(ct.toString());
            }
        }
        return 0;

    }

    public Integer qryNewAddCount(String uid, String ytd) {
        Record r = Db.findFirst("select count(id) ct from " + tableName + " where (creater_id =? or head_id=? ) and create_datetime>=?", uid, uid, ytd + " 00:00:00");
        if (r != null) {
            Object ct = r.get("ct");
            if (ct != null) {
                return Integer.parseInt(ct.toString());
            }
        }
        return 0;
    }

    /**
     * 员工绩效
     */
    public List<Record> performance(Map<String, Object> filter) {
        String start_date = (String) filter.get("start_date");
        start_date += " 00:00:00";
        String end_date = (String) filter.get("end_date");
        end_date += " 23:59:59";

        List<Object> parame = new ArrayList<Object>();
        StringBuffer sql = new StringBuffer();
        sql.append("select u.id,p.realname,dpt.name department_name, ");
        sql.append("(select count(c.id) from crm_customer c where c.creater_id=u.id and c.create_datetime<=? )cust_ct, ");
        parame.add(start_date);
        sql.append("(select count(c.id) from crm_customer c where c.creater_id=u.id and c.create_datetime>=? and  c.create_datetime<=? )newcust_ct, ");
        parame.add(start_date);
        parame.add(end_date);
        sql.append("(select count(c.id) from crm_customer c where c.creater_id=u.id and c.create_datetime>=? and  c.create_datetime<=? )contact_ct, ");
        parame.add(start_date);
        parame.add(end_date);
        sql.append("(select count(l.id) from crm_leads l where l.creater_id=u.id and create_datetime>=? and  create_datetime<=? )leads_ct, ");
        parame.add(start_date);
        parame.add(end_date);
        sql.append("(select count(l.id) from crm_business l where l.creater_id=u.id and create_datetime>=? and create_datetime<=? )biz_ct, ");
        parame.add(start_date);
        parame.add(end_date);
        sql.append("(select sum(l.estimate_price) from crm_business l where l.creater_id=u.id and create_datetime>=? and  create_datetime<=? )biz_amt, ");
        parame.add(start_date);
        parame.add(end_date);
        sql.append("(select count(l.id) from scm_order l where l.creater_id=u.id and l.ordertype=4 and create_datetime>=? and create_datetime<=?)quoted_ct, ");
        parame.add(start_date);
        parame.add(end_date);
        sql.append("(select sum(l.order_amt) from scm_order l where l.creater_id=u.id and l.ordertype=4 and create_datetime>=? and create_datetime<=?)quoted_amt, ");
        parame.add(start_date);
        parame.add(end_date);
        sql.append("(select count(l.id) from scm_order l where l.creater_id=u.id and l.ordertype=2 and create_datetime>=? and create_datetime<=?)order_ct, ");
        parame.add(start_date);
        parame.add(end_date);
        sql.append("(select sum(l.order_amt) from scm_order l where l.creater_id=u.id and l.ordertype=2 and create_datetime>=? and create_datetime<=?)order_amt, ");
        parame.add(start_date);
        parame.add(end_date);
        sql.append("(select sum(l.order_amt) from scm_order l where l.creater_id=u.id and l.ordertype=2  and create_datetime>=? and create_datetime<=?)order_amt, ");
        parame.add(start_date);
        parame.add(end_date);
        sql.append("(select sum(l.pay_amt) from fa_pay_receiv_ables l where l.head_id=u.id and l.type=1  and create_datetime>=? and create_datetime<=?)pay_amt, ");
        parame.add(start_date);
        parame.add(end_date);
        sql.append("(select sum(l.amt-l.pay_amt) from fa_pay_receiv_ables l where l.head_id=u.id and l.type=1  and create_datetime>=? and create_datetime<=?)nopay_amt, ");
        parame.add(start_date);
        parame.add(end_date);
        sql.append("(select sum(l.order_amt) from scm_order l where l.head_id=u.id and l.ordertype=3 and create_datetime>=? and create_datetime<=?)tui_amt ");
        parame.add(start_date);
        parame.add(end_date);
        sql.append("from sso_user u ,sso_person p ,sso_position po,sso_position dpt where p.id=u.id and po.id=u.position_id and dpt.id=po.department_id ");
        sql.append(" and u.company_id=? and u.status=1 ");
        parame.add(filter.get("company_id"));
        String uid = (String) filter.get("uid");
        if (StringUtils.isNotEmpty(uid)) {
            sql.append(" and u.id=? ");
            parame.add(uid);
        }
        String department_id = (String) filter.get("department_id");
        if (StringUtils.isNotEmpty(department_id)) {
            sql.append(" and dpt.id=? ");
            parame.add(department_id);
        }
        sql.append(" order by department_name asc,p.realname asc ");
        return Db.find(sql.toString(), parame.toArray());
    }

    /**
     * 导入客户及联系人
     *
     * @param dataList
     * @param userid
     * @param clist
     */
    @Before(Tx.class)
    public void impl(List<Map<String, Object>> dataList, String company_id, String userid, List<String> clist, String now) {
        for (Map<String, Object> map : dataList) {
            Customer cust = new Customer();
            for (int i = 0; i < 23; i++) {
                String cl = clist.get(i);
                cust.set(cl, map.get(cl));
            }
            cust.set("lat_bd", map.get("lat_bd"));
            cust.set("lng_bd", map.get("lng_bd"));
            cust.set("lat", map.get("lat"));
            cust.set("lng", map.get("lng"));

//            for (int i = clist.size() - 2; i < clist.size(); i++) {
//                String cl = clist.get(i);
//                cust.set(cl, map.get(cl));
//            }
//			cust.set("head_id",userid);
            String sn = SnCreater.dao.create("CUSTOMER", company_id);
            if (map.containsKey("sn") && !String.valueOf(map.get("sn")).equals("null")) {
                sn = String.valueOf(map.get("sn"));
            }
            cust.set("sn", sn);
            cust.set("head_id", map.get("head_id"));
            cust.set("status", map.get("status"));
            cust.set("creater_id", userid);
            cust.set("create_datetime", now);
            cust.set("company_id", company_id);
            cust.set("is_deleted", 0);
            cust.save();
            String custid = cust.getStr("id");
            Contacts ct = new Contacts();
            ct.set("customer_id", custid);
            for (int i = 4; i < clist.size() - 2; i++) {
                String cl = clist.get(i);
                ct.set(cl.replace("ct", ""), map.get(cl));
            }
//			ct.set("head_id",userid);
//			map.put("head_id",map.get("head_id"));
            ct.set("head_id", map.get("head_id"));
            ct.set("is_main", 1);
            ct.set("company_id", company_id);
            ct.set("creater_id", userid);
            ct.set("create_datetime", now);
            ct.save();
        }
    }

    public Map<String, Object> getAttrs() {
        return super.getAttrs();
    }
}
