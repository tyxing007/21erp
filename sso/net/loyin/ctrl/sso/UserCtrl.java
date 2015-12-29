package net.loyin.ctrl.sso;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.upload.UploadFile;
import net.loyin.ctrl.AdminBaseController;
import net.loyin.jfinal.anatation.PowerBind;
import net.loyin.jfinal.anatation.RouteBind;
import net.loyin.model.crm.Customer;
import net.loyin.model.em.SaleGoal;
import net.loyin.model.fa.PayReceivAbles;
import net.loyin.model.scm.Order;
import net.loyin.model.sso.Company;
import net.loyin.model.sso.Person;
import net.loyin.model.sso.Position;
import net.loyin.model.sso.User;
import net.loyin.util.safe.MD5;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 企业用户
 * @author liugf 风行工作室
 */
@RouteBind(sys = "设置", path = "user",model= "用户")
public class UserCtrl extends AdminBaseController<User> {
	public UserCtrl() {
		this.modelClass = User.class;
	}
	public void dataGrid() {
		String keyword=this.getPara("keyword");
		List<Object> param = new ArrayList<Object>();
		StringBuffer where = new StringBuffer();
		String companyId = this.getCompanyIdByHttp();
		/** 添加查询字段条件 */
		this.qryField(where, param);
		if(StringUtils.isNotEmpty(keyword)){
			keyword= "%"+keyword+"%";
			where.append(" and ( u.uname like ? or  ps.realname like ?)");
			param.add(keyword);
			param.add(keyword);
		}
		where.append(" and u.company_id=?");
		param.add(companyId);
		this.jqFilters(where, param);
		sortField(where);
		Page<User> page = User.dao.pageGrid(getPageNo(), getPageSize(), where, param);
		this.rendJson(true,null, "success", page);
	}

	public void qryOp() {
		getId();
		String companyId =  getCompanyIdByHttp();
		User m = User.dao.findById(id, companyId);
		if (m != null){
			m.set("password","");
			this.rendJson(true,null, "", m);
		}
		else
			rendJson(false,null, "记录不存在！");
	}
	@PowerBind(code="A11_1_E",funcName="编辑")
	public void save() {
		try {
			User po = (User) getModel();
			if (po == null) {
				this.rendJson(false,null, "提交数据错误！");
				return;
			}
			String password = po.getStr("password");
			if (StringUtils.isNotEmpty(password)) {
				po.set("password", MD5.getMD5ofStr(password));
			} else {
				po.remove("password");
			}
			id=po.getStr("id");
			Person person=(Person)this.getModel2(Person.class);
			if (StringUtils.isEmpty(id)) {
				String companyId =  this.getCompanyIdByHttp();
				User user = User.dao.exitUname(po.getStr("uname"), companyId);
				if(user != null){
					this.rendJson(false,null, "该账号已经存在！");
					return;
				}
				po.set("reg_date", dateFormat.format(new Date()));
				po.set("company_id",companyId);
				po.save();
				id=po.getStr("id");
				person.set("id",id);
				person.set("company_id",companyId);
				person.save();
			} else {
				po.update();
				person.set("id",id);
				person.update();
			}
			this.rendJson(true,null, "操作成功！",id);
		} catch (Exception e) {
			log.error("保存用户异常", e);
			this.rendJson(false,null, "保存数据异常！");
		}
	}
	public void person(){
		this.setAttr("uid",this.getCurrentUserId());
	}
	/**保存个人设置*/
	public void savePersonSet(){
		try {
			User po = (User) getModel();
			if (po == null) {
				this.rendJson(false,null, "提交数据错误！");
				return;
			}
			String password = po.getStr("password");
			if (StringUtils.isNotEmpty(password)) {
				po.set("password", MD5.getMD5ofStr(password));
			} else {
				po.remove("password");
			}
			id=po.getStr("id");
			Person person=(Person)this.getModel2(Person.class);
			po.update();
			person.set("id",id);
			person.update();
			this.rendJson(true,null, "操作成功！",id);
		} catch (Exception e){
			log.error("保存个人设置异常", e);
			this.rendJson(false,null, "保存数据异常！");
		}
	}
	@PowerBind(code="A11_1_E",funcName="删除")
	public void del() {
		try {
			getId();
			User.dao.del(id, this.getCompanyIdByHttp());
			rendJson(true, null, "删除成功！", id);
		} catch (Exception e) {
			log.error("删除异常", e);
			rendJson(false, null, "删除失败！");
		}
	}
	/** 获取登录信息 */
	public void loginInfo() {
		String uid = this.getCurrentUserId();
		String company_id = this.getCompanyId();
		try {
			User user = User.dao.findById(uid,company_id);
			if (user == null) {
				this.rendJson(false,null, "用户记录不存在！");
				return;
			}else if(user.getInt("status")==0){
				this.rendJson(false,null, "用户已经禁用了！");
				return;
			}
			Date now=new Date();
			Company company = Company.dao.qryById(company_id);
			if(company==null){
				this.rendJson(false,null, "企业记录不存在！");
				return;
			}else if(company.getInt("status")==0){
				this.rendJson(false,null, "企业已经禁用了！");
				return;
			}
			company.put("isExpired",now.compareTo(dateFormat.parse(company.getStr("expiry_date")))>0);//是否过期
			Map<String, Object> data = new HashMap<String, Object>();
//			user.remove("id");
//			user.remove("company_id");
			user.remove("password");
			user.remove("status");
//			company.remove("id");
			company.remove("code");
			company.remove("status");
			String config=company.getStr("config");//获取企业自定义配置项
			if(StringUtils.isNotEmpty(config)){
				Map<String,Object> cf=(Map<String,Object>)JSON.parse(config);
				company.set("config",cf);
			}
			data.put("user", user);
			data.put("company", company);
			data.put("date",dateFormat.format(new Date()));
			//查询岗位权限
			Position position=Position.dao.findById(user.getStr("position_id"));
			String permission_=position.getStr("permission");
			data.put("permission",permission_);
			if(StringUtils.isNotEmpty(permission_)){
				Map<String,Boolean> p=new HashMap<String,Boolean>();
				String[]ss=permission_.split(",");
				for(String s:ss){
					p.put(s, true);
				}
				data.put("rights",p);
			}else
			data.put("rights",null);
			
			Calendar cal=Calendar.getInstance();
			cal.setTime(now);
			cal.add(Calendar.DATE,-6);
			data.put("beginDate",dateFormat.format(cal.getTime()));
			data.put("endDate",dateFormat.format(now));
			this.rendJson(true,null, "", data);
		} catch (Exception e) {
			log.error("获取登录用户信息异常",e);
			this.rendJson(false, null, "获取用户登录信息异常！");
		}
	}
	/**
	 * 获取用户树  type 为10 表示人员
	 */
	public void userTree(){
		String company_id=getCompanyIdByHttp();
		int type= 0;
		if(company_id == getCompanyId()){
			type = this.getParaToInt("type",0);
		}
		String position_id=getPositionId();
		List<Record> list=User.dao.list4tree(position_id,company_id,type);
		this.rendJson(true,null,"",list);
	}
	/**只含用户*/
	public void list(){
		String company_id=this.getCompanyId();
		int type=this.getParaToInt("type",0);
		String position_id=getPositionId();
		List<Record> list=User.dao.list(this.getCurrentUserId(), position_id, company_id, type);
		this.rendJson(true,null,"",list);
	}
	@PowerBind(code="A11_1_E",funcName="禁用")
	public void disable(){
		getId();
		try{
		User.dao.disable(id,this.getCompanyId());
			this.rendJson(true,null, "操作成功！", id);
		}catch(Exception e){
			this.rendJson(false,null, "操作失败！");
		}
	}
	public void mainInfo(){
		String uid=this.getCurrentUserId();
		Date now=new Date();
		Calendar cal=Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.DATE,-1);
		String ytd=dateFormat.format(cal.getTime());//昨天日期
		Map<String,Object> data=new HashMap<String,Object>();
		//总客户数量 （负责及创建的）
		data.put("cust_count",Customer.dao.qryCount(uid));
		//（昨天至今天）新增客户数量
		data.put("cust_newaddcount",Customer.dao.qryNewAddCount(uid,ytd));
		//应收款 （负责及创建的）
		data.put("payab_amt1",PayReceivAbles.dao.qrySumNoPay(uid,1));
		//（昨天至今天）新增应收款
		data.put("payab_newamt1",PayReceivAbles.dao.qryNewAddNoPay(uid,ytd,1));
		//应付款 （负责及创建的）
		data.put("payab_amt",PayReceivAbles.dao.qrySumNoPay(uid,0));
		//（昨天至今天）新增应付款
		data.put("payab_newamt",PayReceivAbles.dao.qryNewAddNoPay(uid,ytd,0));
		//销售额（负责及创建的）累计
		data.put("sale_amt",Order.dao.qryDealAmt(uid,null,null));
		//（昨天至今天）新增销售额
		data.put("sale_newamt",Order.dao.qryDealAmt(uid,ytd,null));
		//消息
		cal=Calendar.getInstance();
		cal.setTime(now);
		cal.set(Calendar.DATE,0);
		//本月起始日期
		String mthday=dateFormat.format(cal.getTime());
		String month=new SimpleDateFormat("M").format(now);
		String year=new SimpleDateFormat("yyyy").format(now);
		//本月成交金额
		data.put("deal_amt",Order.dao.qryDealAmt(uid,mthday,null));
		//本月回款金额
		data.put("back_amt",PayReceivAbles.dao.qryBackAmt(uid,mthday,null));
		//本月目标金额
		data.put("goal_amt",SaleGoal.dao.qryMonth(uid,month,year));
		
		this.rendJson(true, null, "", data);
	}
	/**修改密码*/
	public void savePwd(){
		String pwd=this.getPara("pwd");
		if(StringUtils.isEmpty(pwd)){
			this.rendJson(false,null,"原密码不能为空！");
			return;
		}
		String pwd1=this.getPara("pwd1");
		if(StringUtils.isEmpty(pwd1)){
			this.rendJson(false,null,"新密码不能为空！");
			return;
		}
		String pwd2=this.getPara("pwd2");
		if(StringUtils.isEmpty(pwd2)){
			this.rendJson(false,null,"确认新密码不能为空！");
			return;
		}
		if(pwd1.equals(pwd2)==false){
			this.rendJson(false,null,"确认新密码与新密码不一致！");
			return;
		}
		String uid=this.getCurrentUserId();
		pwd=MD5.getMD5ofStr(pwd);
		if(User.dao.chckPwd(pwd,uid)){
			this.rendJson(false,null,"原密码不正确！");
			return;
		}
		pwd1=MD5.getMD5ofStr(pwd1);
		User.dao.upPwd(pwd1,uid);
		this.rendJson(true, null, "修改新密码成功！请牢记新密码！");
	}



	/**
	 * 获取所有用户
	 */
	public void getAlluser(){
//		String company_id=getCompanyIdByHttp();
		String companyName = getPara("companyName");
		String company_id=this.getCompanyId();
		if(companyName != null){
			company_id = Company.dao.qryCompanyByName(companyName).getStr("id");
		}
		List<User> list = User.dao.getAllUser(company_id);
		List result = new ArrayList<>();
		Map map = null;
		for(User user : list){
			map = new HashMap<>();
			map.put("id",user.getStr("id"));
			map.put("name",user.getStr("name"));
			result.add(map);
		}
		this.rendJson(true,null,"",result);
	}

	/**
	 * 上传文件
	 */
	public void saveFile(){
		UploadFile uf = this.getFile();
		if (uf == null || uf.getFile() == null) {
			this.rendJson(false, null, "文件未上传！");
			return;
		}
		String userid = this.getPara(0);
		String company_id = this.getPara(1);
		String company_id_sys = this.getPara(2);
		String head_id = null;
		int k = 0;
		if (company_id_sys != null && !company_id_sys.isEmpty()) {
			company_id = company_id_sys;
		}
		if (StringUtils.isEmpty(userid)) {
			this.rendJson(false, null, "参数不正确！");
			return;
		}
		try {
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
			Map<String, Object> dataMap = null;
			Map<String,String> userMap = null;
			List<Map<String, String>> userList = null;
			Sheet sheet = wkb.getSheetAt(0);
			Row row = null;
			boolean isHave = false;
			if (sheet.getLastRowNum() > 0) {
				int rowCount = sheet.getLastRowNum();
				for(int i=1;i<rowCount;i++){
					userMap = new HashMap<>();
					userList = new ArrayList<>();
					dataMap = new HashMap<>();
					row = sheet.getRow(i);
					if(row == null){
						continue;
					}
					for(Map<String, Object> map : dataList){
						if(map.get("parent").equals(row.getCell(0).getStringCellValue())
								&&map.get("child").equals(row.getCell(1).getStringCellValue())
								&&map.get("role").equals(row.getCell(2).getStringCellValue())){
							userMap.put("loginName",row.getCell(3).getStringCellValue());
							userMap.put("loginPassWord",row.getCell(4).getStringCellValue());
							userMap.put("name",row.getCell(5).getStringCellValue());
							userMap.put("email",row.getCell(6).getStringCellValue());
							userMap.put("phone",row.getCell(7).getStringCellValue());
							userMap.put("moblie",row.getCell(8).getStringCellValue());
							userList = (List<Map<String, String>>)map.get("user");
							userList.add(userMap);
//							map.put("user",userList);
//							dataList.add(map);
							isHave = true;
							break;
						}
					}
					if(!isHave){
						dataMap.put("parent",row.getCell(0).getStringCellValue());
						dataMap.put("child",row.getCell(1).getStringCellValue());
						dataMap.put("role",row.getCell(2).getStringCellValue());
						userMap.put("loginName",row.getCell(3).getStringCellValue());
						userMap.put("loginPassWord",row.getCell(4).getStringCellValue());
						userMap.put("name",row.getCell(5).getStringCellValue());
						userMap.put("email",row.getCell(6).getStringCellValue());
						userMap.put("phone",row.getCell(7).getStringCellValue());
						userMap.put("moblie", row.getCell(8).getStringCellValue());
						userList.add(userMap);
						dataMap.put("user", userList);
						dataList.add(dataMap);
					}
					isHave = false;
				}
				User.dao.saveFileUser(dataList,company_id);
			}
			this.rendJson(true, null, "导入成功");
		} catch (Exception e) {
			log.error(e);
			this.rendJson(false, null, "处理文件异常!请保证格式及数据是否正确!" + e.getMessage());
		} finally {
			uf.getFile().delete();
		}
	}

//	String password = po.getStr("password");
//	if (StringUtils.isNotEmpty(password)) {
//		po.set("password", MD5.getMD5ofStr(password));
//	} else {
//		po.remove("password");
//	}


}
