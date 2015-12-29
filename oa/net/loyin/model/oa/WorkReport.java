package net.loyin.model.oa;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import net.loyin.ctrl.BaseController;
import net.loyin.jfinal.anatation.TableBind;
import net.loyin.model.crm.Contacts;
import net.loyin.model.crm.Parame;
import net.loyin.model.sso.Person;
import net.loyin.model.sso.User;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Created by Chao on 2015/12/15.
 */
@TableBind(name = "oa_workreport")
public class WorkReport extends Model<WorkReport> {
    private static final long serialVersionUID = -5904205242847326222L;
    public static final String tableName="oa_workreport";
    public static WorkReport dao=new WorkReport();


    /**
     * 保存工作计划
     * @param map
     * @return
     */
    public String save(Map<String,Object> map){
        Iterator<String> iterator = map.keySet().iterator();
        WorkReport workReport = new WorkReport();
        String key = null;
        while (iterator.hasNext()){
            key = iterator.next();
            workReport.set(key,map.get(key));
        }
        workReport.save();
        return workReport.getStr("id");
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
    public Page<WorkReport> pageGrid(int pageNo, int pageSize, Map<String, Object> filter, Integer qryType) {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        String sql_ = "WITH RECURSIVE d AS (SELECT d1.id,d1.pid,d1.name,d1.sort_num,d1.type FROM v_user_position d1 where d1.id=?"
                + "union ALL SELECT d2.id,d2.pid,d2.name,d2.sort_num,d2.type FROM v_user_position d2, d WHERE d2.pid = d.id) "
                + "SELECT distinct id FROM d where type=10 and d.id not in (select id from sso_user where position_id =? and id!=?)";
        List<Object> parame = new ArrayList<Object>();
        StringBuffer sql = new StringBuffer(" from ");
        sql.append(tableName);
        sql.append(" t left join ");
        sql.append(Parame.tableName);
        sql.append(" p on p.id = t.type left join ");
        sql.append(Person.tableName);
        sql.append(" u on u.id = t.user_id where 1=1 ");
        String user_id = (String) filter.get("user_id");//当前用户id
        String position_id = (String) filter.get("position_id");//当前用户岗位id
        String keyword = (String) filter.get("keyword");
        if (StringUtils.isNotEmpty(keyword)) {
            String key[] = keyword.split(" ");
            for(String k : key){
                sql.append(" and t.subject like ?");
                keyword = "%" + k + "%";
                parame.add(keyword);
            }
        }
        String uid = (String) filter.get("uid");
        if (StringUtils.isNotEmpty(uid)) {
            user_id = uid;
        }
        String type = (String) filter.get("type");
        if (StringUtils.isNotEmpty(type)) {
            sql.append(" and t.type=?");
            parame.add(type);
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

        switch (qryType) {
            case -1://我创建的及我负责的
                sql.append(" and t.user_id=?");
                parame.add(user_id);
                break;
            case 0://我创建的
                sql.append(" and t.user_id=?");
                parame.add(user_id);
                break;
            case 1://我负责的
                sql.append(" and t.user_id=?");
                parame.add(user_id);
                break;
            case 2://下属创建的
                sql.append(" and t.user_id in(");
                sql.append(sql_);
                sql.append(") and t.user_id !=?");
                parame.add(position_id);
                parame.add(position_id);
                parame.add(user_id);
                parame.add(user_id);
                break;
            case 3://下属负责的
                sql.append(" and t.user_id in(");
                sql.append(sql_);
                sql.append(") and t.user_id !=?");
                parame.add(position_id);
                parame.add(position_id);
                parame.add(user_id);
                parame.add(user_id);
                break;
            case 5://一周未跟进  下属及本人的负责的
                sql.append(" and t.user_id in(");
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
                sql.append(" and t.user_id in(");
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
                sql.append(" and t.user_id in(");
                sql.append(sql_);
                sql.append(")");
                parame.add(position_id);
                parame.add(position_id);
                parame.add(user_id);
                sql.append(" and t.id not in(select customer_id from crm_contact_record c)");
                break;
            case 9://已购买 下属及本人的负责的
                sql.append(" and t.user_id in(");
                sql.append(sql_);
                sql.append(")");
                parame.add(position_id);
                parame.add(position_id);
                parame.add(user_id);
                sql.append(" and t.id in(select customer_id from scm_order c )");
                break;
            case 10://未购买下属及本人负责的
                sql.append(" and t.user_id in(");
                sql.append(sql_);
                sql.append(")");
                parame.add(position_id);
                parame.add(position_id);
                parame.add(user_id);
                sql.append(" and t.id not in(select customer_id from scm_order c )");
                break;
            case 11://下属负责的
                sql.append(" and ( t.user_id in(");
                sql.append(sql_);
                sql.append(") or t.user_id in (");
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

        sql.append(orderBy(String.valueOf(filter.get("sort")),String.valueOf(filter.get("timeSort"))));
        Page<WorkReport> page = dao.paginate(pageNo, pageSize, "select t.*,u.realname as user_name,p.name as type_name ", sql.toString(), parame.toArray());
//        contacts
        return page;
    }

    private String orderBy(String sort,String time){
        String sql = "";
        switch (time){
            case "1":
                sql += "order by to_date(t.create_datetime) ";
                break;
            case "2":
                sql += "order by to_date(t.subject) ";
                break;
        }
        switch (sort){
            case "1":
                sql += " desc ";
                break;
            case "2":
                sql += " asc ";
        }
        return sql;
    }




}
