package net.loyin.model.scm;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import net.loyin.jfinal.anatation.TableBind;
import net.loyin.model.crm.Customer;
import net.loyin.model.sso.Person;
import net.loyin.util.TimeUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * 每次一次平均加权法数据表
 * Created by Chao on 2015/10/8.
 */
@TableBind(name = "scm_average")
public class Average extends Model<Average>{
    private static final long serialVersionUID = -3544681998668610255L;
    public static final String tableName = "scm_average";
    public static Average dao = new Average();


    /**
     * 全月一次加权数据查询（分月）
     * @param pageNo
     * @param pageSize
     * @param filter
     * @return
     */
    public Page<Average> page(int pageNo, int pageSize, Map<String, Object> filter) {
        List<Object> parame = new ArrayList<Object>();
        StringBuffer sql = new StringBuffer(" from ");
        sql.append(tableName);
        sql.append(" a left join ");
        sql.append(Product.tableName);
        sql.append(" t on t.id = a.product_id left join ");
        sql.append(Depot.tableName);
        sql.append(" d on d.id = a.depot_id ");
        sql.append(" where t.company_id=? ");
        parame.add(filter.get("company_id"));
        String keyword = (String) filter.get("keyword");
        if (StringUtils.isNotEmpty(keyword)) {
            sql.append(" and (t.billsn like ? or t.name like ?)");
            keyword = "%" + keyword + "%";
            parame.add(keyword);
            parame.add(keyword);
        }
        if (filter.containsKey("category") && filter.get("category") != null && StringUtils.isNotBlank(filter.get("category").toString())) {
            String category = (String) filter.get("category");
            if (StringUtils.isNotEmpty(category)) {
                sql.append(" and t.category = ?");
                parame.add(category);
            }
        }

        if (filter.containsKey("merchType")) {
            String merchType = (String) filter.get("merchType");
            if (StringUtils.isNotEmpty(merchType)) {
                sql.append(" and t.category = ?");
                parame.add(merchType);
            }
        }

        if (filter.containsKey("searchBrand") && filter.get("searchBrand") != null && StringUtils.isNotBlank(filter.get("searchBrand").toString())) {
            String searchBrand = (String) filter.get("searchBrand");
            if (StringUtils.isNotEmpty(searchBrand)) {
                sql.append(" and t.brand = ?");
                parame.add(searchBrand);
            }
        }

        if (filter.containsKey("status") && filter.get("status") != null && StringUtils.isNotBlank(filter.get("status").toString())) {
            String status = (String) filter.get("status");
            if (StringUtils.isNotEmpty(status)) {
                sql.append(" and a.status = ?");
                parame.add(status);
            }
        }

        if (filter.containsKey("year") && filter.get("year") != null && StringUtils.isNotBlank(filter.get("year").toString())) {
            String year = (String) filter.get("year");
            if (StringUtils.isNotEmpty(year)) {
                sql.append(" and a.year = ?");
                parame.add(year);
            }
        }

        if (filter.containsKey("month") && filter.get("month") != null && StringUtils.isNotBlank(filter.get("month").toString())) {
            String month = (String) filter.get("month");
            if (StringUtils.isNotEmpty(month)) {
                sql.append(" and a.month = ?");
                parame.add(month);
            }
        }


//        customer_id

        if (filter.containsKey("searchStander")) {
            String searchStander = (String) filter.get("searchStander");
            if (StringUtils.isNotEmpty(searchStander)) {
                sql.append(" and lower(t.specification) = ?");
                parame.add(searchStander);
            }
        }

//        String sortField = (String) filter.get("_sortField");
//        if (StringUtils.isNotEmpty(sortField)) {
//            sql.append(" order by t.");
//            sql.append(sortField);
//            sql.append(" ");
//            sql.append((String) filter.get("_sort"));
//        }

        Page<Average> page = dao.paginate(pageNo, pageSize, "select t.name as product_name,t.billsn,d.name as depot_name,a.*",
                sql.toString(), parame.toArray());


        return page;
    }

    /**
     * 初始化库存参数时，进行每月一次平均加权法的期初初始化
     */
    public void saveAverageFirst(String companyId, String productId, String firstCost, String firstNum,String depotId) throws Exception {
        try {
            String time = TimeUtil.getMonthAndYear(new Date(), 0);
            String year = time.split("-")[0];
            String month = time.split("-")[1];
            List<Average> averageList = this.find("select * from " + tableName + " s where s.product_id = ? and s.company_id = ? and s.year = ? and s.month = ? and s.status = ? and s.depot_id = ?", productId, companyId, year, month, 0,depotId);
            Average average = null;
            if(averageList.size() != 0){
                //永远只允许存在一条同年同月的
                average = averageList.get(0);
                average.set("first_cost",Double.parseDouble(firstCost));
                average.set("first_num",Double.parseDouble(firstNum));
                average.set("first_money",Double.parseDouble(firstNum) * Double.parseDouble(firstCost));
                average.update();
            }else{
                average = new Average();
                average.set("product_id",productId);
                average.set("first_cost",Double.parseDouble(firstCost));
                average.set("first_num",Double.parseDouble(firstNum));
                average.set("first_money",Double.parseDouble(firstNum) * Double.parseDouble(firstCost));
                average.set("year",year);
                average.set("month",month);
                average.set("status",0);
                average.set("depot_id",depotId);
                average.set("company_id",companyId);
                average.save();
            }
        }catch (Exception e){
            throw new Exception("初始化平均值时异常，请通知供应商管理员");
        }
    }


    /**
     * 初始化库存参数时，进行每月一次平均加权法的期初初始化
     */
    public void saveAverageFirstNextMonth(String companyId, String productId, String firstCost, String firstNum,String depotId) throws Exception {
        try {
            String time = TimeUtil.getMonthAndYear(new Date(), 1);
            String year = time.split("-")[0];
            String month = time.split("-")[1];
            List<Average> averageList = this.find("select * from " + tableName + " s where s.product_id = ? and s.company_id = ? and s.year = ? and s.month = ? and s.status = ? and s.depot_id = ?", productId, companyId, year, month, 0,depotId);
            Average average = null;
            if(averageList.size() != 0){
                //永远只允许存在一条同年同月的
                average = averageList.get(0);
                average.set("first_cost",Double.parseDouble(firstCost));
                average.set("first_num",Double.parseDouble(firstNum));
                average.set("first_money",Double.parseDouble(firstNum) * Double.parseDouble(firstCost));
                average.update();
            }else{
                average = new Average();
                average.set("product_id",productId);
                average.set("first_cost",Double.parseDouble(firstCost));
                average.set("first_num",Double.parseDouble(firstNum));
                average.set("first_money",Double.parseDouble(firstNum) * Double.parseDouble(firstCost));
                average.set("year",year);
                average.set("month",month);
                average.set("status",0);
                average.set("depot_id",depotId);
                average.set("company_id",companyId);
                average.save();
            }
        }catch (Exception e){
            throw new Exception("初始化平均值时异常，请通知供应商管理员");
        }
    }



    /**
     * 到达规定时间时，进行每月一次平均加权法的当期存档
     */
    public void saveAverageLast(String companyId, String productId, String lastCost, String lastNum,String depotId) throws Exception {
        try {
            String time = TimeUtil.getMonthAndYear(new Date(), 0);
            String year = time.split("-")[0];
            String month = time.split("-")[1];
            String day = time.split("-")[2];
            List<Average> averageList = this.find("select * from " + tableName + " s where s.product_id = ? and s.company_id = ? and s.year = ? and s.month = ? and s.status = ? and s.depot_id = ?", productId, companyId, year, month, 0,depotId);
            Average average = null;
            if(averageList.size() != 0){
                //永远只允许存在一条同年同月同日的
                average = averageList.get(0);
                average.set("last_cost",Double.parseDouble(lastCost));
                average.set("last_num",Double.parseDouble(lastNum));
                average.set("last_money",Double.parseDouble(lastNum) * Double.parseDouble(lastCost));
                average.set("day",day);
                average.set("status",1);
                average.update();

                average = new Average();
                average.set("product_id",productId);
                average.set("first_cost",Double.parseDouble(lastCost));
                average.set("first_num",Double.parseDouble(lastNum));
                average.set("first_money",Double.parseDouble(lastNum) * Double.parseDouble(lastCost));
                average.set("year",year);
                average.set("month",month);
                average.set("status",0);
                average.set("depot_id",depotId);
                average.set("company_id", companyId);
                average.save();

            }


        }catch (Exception e){
            throw new Exception("初始化平均值时异常，请通知供应商管理员");
        }
    }



    /**
     * 初始化库存参数时，进行每月一次平均加权法的期初初始化
     */
    public Average getAverage(String companyId, String productId,String depotId,boolean isClearing) throws Exception {
        try {

            List<Average> averageList = null;
            //获取未开启的全月一次平均加权法
            if(isClearing){
                averageList = this.find("select * from " + tableName + " s where s.product_id = ? and s.company_id = ? and s.status = ? and s.depot_id = ? order by cast(year as integer),cast(month as integer) desc", productId, companyId, 1,depotId);
            }else{
                averageList = this.find("select * from " + tableName + " s where s.product_id = ? and s.company_id = ? and s.status = ? and s.depot_id = ? order by cast(year as integer),cast(month as integer) desc", productId, companyId, 0,depotId);
            }
//            List<Average> averageList = this.find("select * from " + tableName + " s where s.product_id = ? and s.company_id = ? and s.year = ? and s.status = ? and s.depot_id = ? order by to_number(s.month)", productId, companyId, year, 0,depotId);
            if(averageList.size()!=0){
                return averageList.get(0);
            }else{
                throw new Exception("初始化平均值时异常，请通知供应商管理员");
            }
        }catch (Exception e){
            throw new Exception("初始化平均值时异常，请通知供应商管理员");
        }
    }

//    CREATE TABLE "public"."scm_average" (
//            "id" varchar(10) NOT NULL,
//    "product_id" varchar(10),
//    "first_cost" float8,
//            "first_num" float8,
//            "first_money" numeric(10,2),
//    "last_cost" float8,
//            "last_num" float8,
//            "last_money" numeric(10,2),
//    "year" varchar(10),
//    "month" varchar(10),
//    "status" int4,
//            "depot_id" varchar(10),
//    "company_id" varchar(10)
//    )


}
