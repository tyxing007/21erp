package net.loyin.model.sso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map;
import java.util.logging.SimpleFormatter;

import net.loyin.jfinal.anatation.TableBind;

import net.loyin.util.safe.MD5;
import org.apache.commons.lang3.StringUtils;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

/**
 * 用户
 *
 * @author liugf 风行工作室
 */
@TableBind(name = "sso_user")
public class User extends Model<User> {
    private static final long serialVersionUID = -5301851381511273243L;
    public static final String tableName = "sso_user";
    public static User dao = new User();

    /**
     * 分页查询
     *
     * @param pageNo
     * @param pageSize
     * @param where    查询条件
     * @param param    参数
     * @return
     */
    public Page<User> pageGrid(Integer pageNo, Integer pageSize, StringBuffer where, List<Object> param) {
        return dao.paginate(pageNo, pageSize, "select  u.id,u.uname,ps.realname,ps.email,ps.mobile,ps.sex,ps.head_pic,u.status,u.reg_date,u.last_login_time,u.login_ip,p.name as position_name,d.name as department_name",
                "from " + tableName + " u, " + Position.tableName + " p ," + Position.tableName + " d ," + Person.tableName + " ps where  u.id=ps.id and d.id=p.department_id and u.position_id=p.id  " + where.toString(), param.toArray());
    }




    public User qryLoginUser(String id) {
        List<User> list = this.findByCache("user", id, "select u.*,p.* from " + tableName + " u," + Person.tableName + " p where u.id=p.id and u.id=?", id);
        if (list != null && list.isEmpty() == false)
            return list.get(0);
        return null;
    }

    public User login(String userno, String password, String companyId) {
        if (userno.equals("system")) {
            return this.findFirst("select u.* from " + tableName + " u where uname=? and password=? ", userno, password);
        }
        return this.findFirst("select u.*,per.*,p.name position_name,p.department_id,de.name department_name from " + tableName + " u," + Position.tableName + " p," + Position.tableName + " de," + Person.tableName + " per where per.id=u.id and de.id=p.department_id and p.id=u.position_id and uname=? and password=? and u.company_id=? ", userno, password, companyId);
    }

    public User findById(String id, String company_id) {
        return User.dao.findFirst("select  u.*,ps.realname,ps.email,ps.mobile,ps.sex,ps.head_pic,ps.telephone,ps.address,p.name as position_name,d.name as department_name from " + tableName + " u, " + Position.tableName + " p ," + Position.tableName + " d ," + Person.tableName + " ps where  u.id=ps.id and d.id=p.department_id and u.position_id=p.id and u.id=? and u.company_id=?", id, company_id);
    }


    public User exitUname(String uname, String company_id) {
        return User.dao.findFirst("select  u.* from " + tableName + " u where u.uname=? and u.company_id=?", uname, company_id);
    }

    public User findByName(String name, String company_id) {
        return User.dao.findFirst("select  u.*,ps.realname,ps.email,ps.mobile,ps.sex,ps.head_pic,ps.telephone,ps.address,p.name as position_name,d.name as department_name from " + tableName + " u, " + Position.tableName + " p ," + Position.tableName + " d ," + Person.tableName + " ps where  u.id=ps.id and d.id=p.department_id and u.position_id=p.id and ps.realname=? and u.company_id=?", name, company_id);
    }

    /**
     * 更新登录信息
     */
    public void upLogin(String id, String ip, String nowStr) {
        Db.update("update " + tableName + " set last_login_time=?,login_ip=? where id=? ", nowStr, ip, id);
    }

    /**
     * 禁用或激活用户
     */
    public void disable(String id, String company_id) {
        Db.update("update " + tableName + " set status=case when status=0 then 1 else 0 end where id=? and company_id=?", id, company_id);
    }

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
            Db.update("delete  from " + Person.tableName + " where id in (" + ids_.toString() + ")", parame.toArray());
            parame.add(company_id);
            Db.update("delete  from " + tableName + " where id in (" + ids_.toString() + ") and company_id=? ", parame.toArray());
        }
    }

    /**
     * 用于用户树组织
     *
     * @param position_id 岗位id
     * @param company_id  企业
     * @param type        0:全部  1：上级 2：下级
     * @return
     */
    public List<Record> list4tree(String position_id, String company_id, int type) {
        List<Record> list = null;
        switch (type) {
            case 0://全部
                String sql = "WITH RECURSIVE d AS (SELECT d1.id,d1.pid,d1.name,d1.sort_num,d1.type FROM v_user_position d1 where d1.company_id=?"
                        + "union ALL SELECT d2.id,d2.pid,d2.name,d2.sort_num,d2.type FROM v_user_position d2, d WHERE d2.id = d.pid) "
                        + "SELECT distinct * FROM d";
                list = Db.find(sql, company_id);
                break;
            case 1://上级
                sql = "WITH RECURSIVE d AS (SELECT d1.id,d1.pid,d1.name,d1.sort_num,d1.type FROM v_user_position d1 where d1.id=?"
                        + "union ALL SELECT d2.id,d2.pid,d2.name,d2.sort_num,d2.type FROM v_user_position d2, d WHERE d2.id = d.pid) "
                        + "SELECT distinct * FROM d";
                list = Db.find(sql, position_id);
                break;
            case 2://下级
                sql = "WITH RECURSIVE d AS (SELECT d1.id,d1.pid,d1.name,d1.sort_num,d1.type FROM v_user_position d1 where d1.id=?"
                        + "union ALL SELECT d2.id,d2.pid,d2.name,d2.sort_num,d2.type FROM v_user_position d2, d WHERE d2.pid = d.id) "
                        + "SELECT distinct * FROM d";
                list = Db.find(sql, position_id);
                break;
        }
        return list;
    }

    /**
     * 上下级用户
     *
     * @param uid        当前用户id
     * @param position_id 岗位id
     * @param company_id  企业
     * @param type        0:全部  1：上级 2：下级
     * @return
     */
    public List<Record> list(String uid, String position_id, String company_id, int type) {
        List<Record> list = null;
        switch (type) {
            case 0://全部
                String sql = "WITH RECURSIVE d AS (SELECT d1.id,d1.pid,d1.name,d1.sort_num,d1.type FROM v_user_position d1 where d1.company_id=?"
                        + "union ALL SELECT d2.id,d2.pid,d2.name,d2.sort_num,d2.type FROM v_user_position d2, d WHERE d2.id = d.pid) "
                        + "SELECT distinct * FROM d where type=10";
                list = Db.find(sql, company_id);
                break;
            case 1://上级
                sql = "WITH RECURSIVE d AS (SELECT d1.id,d1.pid,d1.name,d1.sort_num,d1.type FROM v_user_position d1 where d1.id=?"
                        + "union ALL SELECT d2.id,d2.pid,d2.name,d2.sort_num,d2.type FROM v_user_position d2, d WHERE d2.id = d.pid) "
                        + "SELECT distinct * FROM d where type=10";
                list = Db.find(sql, uid);
                break;
            case 2://下级
                sql = "WITH RECURSIVE d AS (SELECT d1.id,d1.pid,d1.name,d1.sort_num,d1.type FROM v_user_position d1 where d1.id=?"
                        + "union ALL SELECT d2.id,d2.pid,d2.name,d2.sort_num,d2.type FROM v_user_position d2, d WHERE d2.pid = d.id) "
                        + "SELECT distinct id FROM d where type=10 and d.id not in (select id from sso_user where position_id =? and id!=?)";
                list = Db.find(sql, position_id, position_id, uid);
                break;
        }
        return list;
    }

    /**
     * 获取用户的上级
     */
    public User findSupUser(String uid) {
        return findFirst("select u.* from " + tableName + " u," + Position.tableName + " p," + tableName + " u1 where p.id=u1.position_id and u1.id=? and u.position_id=p.parent_id ", uid);
    }

    /**
     * 检查密码
     */
    public boolean chckPwd(String pwd, String uid) {
        User u = this.findFirst("select * from " + tableName + " where id=? and password=?", uid, pwd);
        return u == null;
    }

    /**
     * 修改密码
     */
    public void upPwd(String pwd, String uid) {
        Db.update("update " + tableName + " set password=? where id=?", pwd, uid);
    }


    public List<User> getAllUser(String companyId) {
        String sql = "select t.realname as name,m.id from " + tableName + " m left join " + Person.tableName + " t on t.id = m.id where t.company_id = ?";
        return find(sql, companyId);
    }

    public void saveFileUser(List<Map<String, Object>> dataList, String companyId) {
        Position position = null;
        Position childPosition = null;
        Position rolePosition = null;
        for (Map<String, Object> map : dataList) {
            if (StringUtils.isNotEmpty(String.valueOf(map.get("parent")))) {
                position = Position.dao.findByName(String.valueOf(map.get("parent")), companyId);
                if (position == null) {
                    position = new Position();
                    position.set("company_id", companyId);
                    position.set("name", String.valueOf(map.get("parent")));
                    position.set("quota", 10);
                    position.set("type", 0);
                    position.set("sort_num", 0);
                    position.set("m", 0);
                    position.save();
                }
            }
            if (StringUtils.isNotEmpty(String.valueOf(map.get("child")))) {
                childPosition = Position.dao.findByName(String.valueOf(map.get("child")), companyId);
                if (childPosition == null) {
                    childPosition = new Position();
                    childPosition.set("company_id", companyId);
                    childPosition.set("parent_id", position.getStr("id"));
                    childPosition.set("name", String.valueOf(map.get("child")));
                    childPosition.set("quota", 10);
                    childPosition.set("type", 1);
                    childPosition.set("sort_num", 0);
                    childPosition.set("m", 1);
                    childPosition.set("is_head", 1);
                    childPosition.save();
                }
            }
            if (StringUtils.isNotEmpty(String.valueOf(map.get("role")))) {
                rolePosition = Position.dao.findByName(String.valueOf(map.get("role")), companyId);
                if (rolePosition == null) {
                    rolePosition = new Position();
                    rolePosition.set("company_id", companyId);
                    rolePosition.set("department_id", childPosition.getStr("id"));
                    rolePosition.set("name", String.valueOf(map.get("role")));
                    rolePosition.set("quota", 10);
                    rolePosition.set("type", 1);
                    rolePosition.set("sort_num", 0);
                    rolePosition.set("m", 1);
                    rolePosition.set("is_head", 1);
                    rolePosition.save();
                }
            }
            List<Map<String, String>> user = (List<Map<String, String>>) map.get("user");
            for (Map<String, String> userMap : user) {
                User insertUser = new User();
                insertUser.set("company_id", companyId);
                insertUser.set("position_id", rolePosition.getStr("id"));
                insertUser.set("is_admin", 0);
                insertUser.set("status", 1);
                insertUser.set("uname", userMap.get("loginName"));
                String password = userMap.get("loginPassWord");
                if (StringUtils.isNotEmpty(password)) {
                    insertUser.set("password", MD5.getMD5ofStr(password));
                } else {
                    insertUser.remove("password");
                }
                insertUser.set("reg_date", (new SimpleDateFormat("yyyy-MM-dd")).format(new Date()));
                insertUser.set("email", userMap.get("email"));
                insertUser.save();

                Person person = new Person();
                person.set("id",insertUser.getStr("id"));
                person.set("realname",userMap.get("name"));
                person.set("sex",1);
                person.set("email",userMap.get("email"));
                person.set("mobile",userMap.get("moblie"));
                person.set("telephone",userMap.get("phone"));
                person.set("company_id",companyId);

                person.save();
            }

        }


    }

    /**
     * by chenjianhui
     * 2015年10月8日16:32:17
     * 将字段转为map
     *
     * @return
     */
    public Map<String, Object> getAttrs() {
        return super.getAttrs();
    }


    public Page<User> getUserBySearchName(int pageSize,int pageNum,String searchName,String companyId){
        List<Object> parame = new ArrayList<Object>();
        String sql = " from " + tableName + " m left join " + Person.tableName + " t on t.id = m.id where t.company_id = ? ";
        parame.add(companyId);
        if(searchName!=null || !searchName.isEmpty()){
            String[] searchKey = searchName.split(" ");
            for(String s : searchKey){
                sql += "and ( t.realname like ? )";
                s = "%" +s+ "%";
                parame.add(s);
            }
        }
        Page<User> page = dao.paginate(pageNum, pageSize, "select t.realname as name,m.id,t.mobile,t.telephone,t.head_pic", sql, parame.toArray());
        return page;
    }


}
