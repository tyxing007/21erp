package net.loyin.quartz.job;

import com.jfinal.plugin.activerecord.Record;
import net.loyin.model.scm.*;
import net.loyin.model.sso.Company;
import net.loyin.util.TimeUtil;
import org.omg.CORBA.MARSHAL;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.DoubleBinaryOperator;

/**
 * 全月一次平均加权法JOB使用方法
 * Created by Chao on 2015/10/8.
 */
public class AverageJob {

    private String companyId = "";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");
    private SimpleDateFormat yearMonthDateFormat = new SimpleDateFormat("YYYY-MM");
    private SimpleDateFormat allDateFormat = new SimpleDateFormat("YYYY-MM-dd");

    /**
     * 保存全月一次平均加权法
     */
    public void saveAverage() {
//        TimeUtil.get
        String nowTime = TimeUtil.getMonthAndYear(new Date(), 0);
        String lastTime = TimeUtil.getMonthAndYear(new Date(), -1);
        String[] time = lastTime.split("-");
        List<String> companyList = getCompanyIdByDate();
        List<String> depotList = null;
        List<Map<String, Object>> storageMap = null;
        for (String companyId : companyList) {
            this.companyId = companyId;
            depotList = getDepotByCompanyId(companyId);
            List<Product> products = Product.dao.getProductByCompanyId(companyId);
            for (Product product : products) {
                String productId = product.getStr("id");
                for (String depotId : depotList) {
                    storageMap = new ArrayList<>();
                    Average average = getAverage(companyId, productId, depotId,false);
                    Average clearingAverage = getAverage(companyId, productId, depotId,true);
                    if(clearingAverage != null){
                        lastTime = clearingAverage.getStr("year") + "-" + clearingAverage.getStr("month") + "-" + clearingAverage.getStr("day");
                    }
                    if (average != null) {
                        List<StorageBill> billList = StorageBill.dao.getStorageBillByDepotId(companyId, depotId, nowTime, lastTime);
                        Map<Integer, List<StorageBill>> billMap = sortStorageBill(billList);
                        Iterator<Integer> iterator = billMap.keySet().iterator();
                        while (iterator.hasNext()) {
                            int key = iterator.next();
                            storageMap.add(storageBillCheckType(key, billMap.get(key), productId));
                        }
                        sortAverage(storageMap, average);
                    }
                }
            }
        }
    }
//    public String

    /**
     * 整理平均价格
     *
     * @param lists
     * @param average
     */
    public void sortAverage(List<Map<String, Object>> lists, Average average) {
        Double productAverage = 0.0; //平均价格
        Double purchaseIn = 0.0;//采购入库数量
        Double purchaseMoneyIn = 0.0;//采购入库金额
        Double purchaseOut = 0.0;//采购退货数量
        Double purchaseMoneyOut = 0.0;//采购退货价格
        Double sellOut = 0.0;//出售出库
        Double sellIn = 0.0;//出售退货
        Double sellMoenyIn = 0.0;//出售退货金额
        Double sellMoenyOut = 0.0;//出售出库金额
//        Double firstCost = Double.valueOf(average.getFloat("first_cost"));
        Double firstNum = average.getDouble("first_num");
        Double firstMoney = Double.valueOf(average.getNumber("first_money").toString());
//        "first_cost" float8,
//                "first_num" float8,
//                "first_money" numeric(10,2),
        for (Map<String, Object> m : lists) {
            int type = Integer.parseInt(m.get("type").toString());
            Double amount = Double.parseDouble(String.valueOf(m.get("amount")));
            Double amt = Double.parseDouble(String.valueOf(m.get("amt")));
            switch (type) {
                case 0:
                    purchaseIn += amount;
                    purchaseMoneyIn += amt;
                    break;
                case 1:
                    sellIn += amount;
                    sellMoenyIn += amt;
                    break;
                case 2:
                    purchaseIn += amount;
                    purchaseMoneyIn += amt;
                    break;
                case 4:
                    sellOut += amount;
                    sellMoenyOut += amt;
                    break;
                case 5:
                    purchaseOut += amount;
                    purchaseMoneyOut += amt;
                    break;
                case 6:
                    purchaseOut += amount;
                    purchaseMoneyOut += amt;
                    break;
            }
        }
        Double money = firstMoney + purchaseMoneyIn - purchaseMoneyOut;
        Double amont = firstNum + purchaseIn - purchaseOut;
        if (amont == 0.0) {
            productAverage = 0.0;
        } else {
            productAverage = money / amont;
        }
        /** 防止长时间不结算时，记录不存在，而进行修改**/
        String time = TimeUtil.getMonthAndYear(new Date(), 0);
        String year = time.split("-")[0];
        String month = time.split("-")[1];
        String day = time.split("-")[2];
        average.set("year", year);
        average.set("month", month);
        average.set("day", day);
        /** 未结算时间间隔内所有的出入库结算**/
        average.set("last_cost", productAverage);
        average.set("last_num", amont - sellOut);
        average.set("status", 1);
        average.set("last_money", productAverage * (amont - sellOut));

        average.update();

        try {
            Average.dao.saveAverageFirstNextMonth(average.getStr("company_id"), average.getStr("product_id"), String.valueOf(productAverage), String.valueOf(amont - sellOut), average.getStr("depot_id"));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    /**
     * 整理出入库
     *
     * @param bills
     * @return
     */
    public Map<Integer, List<StorageBill>> sortStorageBill(List<StorageBill> bills) {
        List<StorageBill> result = null;
        Map<Integer, List<StorageBill>> map = new HashMap<Integer, List<StorageBill>>();
        for (StorageBill s : bills) {
            result = new ArrayList<>();
            if (map.containsKey(s.getInt("type"))) {
                result = map.get(s.getInt("type"));
                result.add(s);
            } else {
                result.add(s);
            }
            map.put(s.getInt("type"), result);
        }
        return map;
    }

    /**
     * 获取当天需要进行加权结算的企业
     *
     * @return
     */
    public List<String> getCompanyIdByDate() {
        List<String> result = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");
        String day = simpleDateFormat.format(new Date());
        List<Company> companyList = Company.dao.qryAll();
        for (Company company : companyList) {
            Map<String, Object> map = company.getConfig();
            if (map.containsKey("p_auto_average") && "true".equals(map.getOrDefault("p_auto_average", "false"))) {
                String job = String.valueOf(map.get("p_averageJob"));
                if (job.equals(day)) {
                    result.add(company.getStr("id"));
                }
            }
        }
        return result;
    }

    /**
     * 获取仓库通过企业ID
     *
     * @param companyId
     * @return
     */
    public List<String> getDepotByCompanyId(String companyId) {
        List<String> result = new ArrayList<>();
        List<Depot> depots = Depot.dao.list(companyId);
        for (Depot depot : depots) {
            result.add(depot.getStr("id"));
        }
        return result;
    }


    /**
     * 获取全月一次加权法记录
     *
     * @param companyId
     * @param productId
     * @param depotId
     * @throws Exception
     */
    public Average getAverage(String companyId, String productId, String depotId,boolean isClearing) {
        try {
            return Average.dao.getAverage(companyId, productId, depotId,isClearing);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 出入库处理（除调拨）
     *
     * @param type
     * @param bills
     * @param productId
     * @return
     */
    public Map<String, Object> storageBillCheckType(int type, List<StorageBill> bills, String productId) {
        Map<String, Object> map = null;
        switch (type) {
            case 0:
                map = sumIn(bills, productId);
                break;
            case 1:
                map = sumIn(bills, productId);
                break;
            case 2:
                map = storageBillByAllot(type, bills, productId);
                break;
            case 4:
                map = sumOut(bills, productId);
                break;
            case 5:
                map = sumOut(bills, productId);
                break;
            case 6:
                map = storageBillByAllot(type, bills, productId);
                break;
        }
        map.put("type", type);
        return map;
    }

    /**
     * 出入库处理（调拨）
     *
     * @param type
     * @param bills
     * @param productId
     * @return
     */
    public Map<String, Object> storageBillByAllot(int type, List<StorageBill> bills, String productId) {
        Map<String, Object> map = null;
        switch (type) {
            case 2:
                map = sumAllotIn(bills, productId);
                break;
            case 6:
                map = sumAllotOut(bills, productId);
                break;
        }
        return map;
    }


    /**
     * 商品出库，除调拨
     *
     * @param bills
     * @param productId
     * @return
     */
    public Map<String, Object> sumOut(List<StorageBill> bills, String productId) {
        Map<String, Object> resultMap = new HashMap<>();
        double productAmount = 0.0;
        double productAmt = 0.0;
        for (StorageBill s : bills) {
            String orderId = s.get("order_id");
            OrderProduct orderProduct = OrderProduct.dao.findByOrderIdAndProductId(orderId, productId, this.companyId);
            if (orderProduct != null) {
                productAmount += orderProduct.getDouble("amount");
                productAmt += Double.parseDouble(orderProduct.getNumber("amt").toString());
            }
        }
        resultMap.put("amount", productAmount);
        resultMap.put("amt", productAmt);
        return resultMap;
    }

    /**
     * 商品入库(除调拨)
     *
     * @param bills
     * @param productId
     * @return
     */
    public Map<String, Object> sumIn(List<StorageBill> bills, String productId) {
        Map<String, Object> resultMap = new HashMap<>();
        double productAmount = 0.0;
        double productAmt = 0.0;
        for (StorageBill s : bills) {
            String orderId = s.get("order_id");
            OrderProduct orderProduct = OrderProduct.dao.findByOrderIdAndProductId(orderId, productId, this.companyId);
            if (orderProduct != null) {
                productAmount += orderProduct.getDouble("amount");
                productAmt += Double.parseDouble(orderProduct.getNumber("amt").toString());
            }
        }
        resultMap.put("amount", productAmount);
        resultMap.put("amt", productAmt);
        return resultMap;
    }

    /**
     * 调拨入库
     *
     * @param bills
     * @param productId
     * @return
     */
    public Map<String, Object> sumAllotIn(List<StorageBill> bills, String productId) {
        Map<String, Object> resultMap = new HashMap<>();
        double productAmount = 0.0;
        double productAmt = 0.0;
        for (StorageBill s : bills) {
            String allotId = s.get("allot_id");
            List<StockAllotList> stockAllots = StockAllotList.dao.list(allotId, productId);
            for (StockAllotList allot : stockAllots) {
                productAmount += allot.getDouble("amount");
                productAmt += Double.parseDouble(allot.getFloat("allot_money").toString());
            }
        }
        resultMap.put("amount", productAmount);
        resultMap.put("amt", productAmt);
        return resultMap;
    }


    /**
     * 调拨出库
     *
     * @param bills
     * @param productId
     * @return
     */
    public Map<String, Object> sumAllotOut(List<StorageBill> bills, String productId) {
        Map<String, Object> resultMap = new HashMap<>();
        double productAmount = 0.0;
        double productAmt = 0.0;
        for (StorageBill s : bills) {
            String allotId = s.get("allot_id");
            List<StockAllotList> stockAllots = StockAllotList.dao.list(allotId, productId);
            for (StockAllotList allot : stockAllots) {
                productAmount += allot.getDouble("amount");
                productAmt += Double.parseDouble(allot.getFloat("allot_money").toString());
            }
        }
        resultMap.put("amount", productAmount);
        resultMap.put("amt", productAmt);
        return resultMap;
    }


}
