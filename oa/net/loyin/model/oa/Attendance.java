package net.loyin.model.oa;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import net.loyin.jfinal.anatation.TableBind;
import net.loyin.model.scm.ProductHis;
import net.loyin.model.sso.Parame;
import net.loyin.model.sso.Person;
import net.loyin.model.sso.User;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 评论
 * 
 * @author liugf 风行工作室 2014年9月19日
 */
@TableBind(name = "oa_attendance")
public class Attendance extends Model<Attendance> {
	private static final long serialVersionUID = -98977900492538706L;
	public static final String tableName = "oa_attendance";
	public static Attendance dao = new Attendance();

	/**
	 * 分页查询
	 *
	 * @param pageNo
	 * @param pageSize
	 * @param filter   参数
	 * @return
	 */
	public Page<Attendance> pageGrid(Integer pageNo, Integer pageSize, Map<String, Object> filter) {
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Object> parame = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer(" from ");
		sql.append(tableName);
		sql.append(" t");
//        sql.append(" left join ");
//        sql.append(ProductHis.tableName);
//        sql.append(" h");
//        sql.append(" on t.id = h.product_id");
		sql.append(" where t.company_id=? ");
		parame.add(filter.get("company_id"));

		if (filter.containsKey("attendance")) {
			String attendance = (String) filter.get("attendance");
			if (StringUtils.isNotEmpty(attendance)) {
				sql.append(" and t.attendance_type = ?");
				parame.add(Integer.parseInt(attendance));
			}
		}

		if (filter.containsKey("uid")) {
			String uid = (String) filter.get("uid");
			if (StringUtils.isNotEmpty(uid)) {
				sql.append(" and t.user_id = ?");
				parame.add(uid);
			}
		}

		String startTime = filter.get("startTime") == null ?"":String.valueOf(filter.get("startTime"));
		startTime = startTime.isEmpty()?"2000-01-01 00:00:00":startTime;
		String endTime = filter.get("endTime") == null ?"":String.valueOf(filter.get("endTime"));
		endTime = endTime.isEmpty()?s.format(new Date()):endTime;
		sql.append(" and t.create_time between ? and ? ");
		parame.add(startTime);
		parame.add(endTime);


		Page<Attendance> page = dao.paginate(pageNo, pageSize, "select t.*",
				sql.toString(), parame.toArray());
		return page;
	}


	public String saveAttendanceBackId(Map<String, Object> map) {

		Attendance attendance = new Attendance();
		Iterator<String> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			if(key.equals("id")){
				continue;
			}
			attendance.set(key, map.get(key));
		}
		attendance.save();
		return attendance.getStr("id");
	}

	/**
	 * 获取状态值
	 * @return
	 */
	public String checkStatus(int status){
		String result = "";
		switch (status){
			case 0:
				result = "待审核";
				break;
			case 1:
				result = "上级审核";
				break;
			case 2:
				result = "审核成功";
				break;
		}
	return result;
	}

}
