package net.loyin.ctrl.oa;

import com.jfinal.plugin.activerecord.Page;
import net.loyin.ctrl.AdminBaseController;
import net.loyin.jfinal.anatation.RouteBind;
import net.loyin.model.crm.Customer;
import net.loyin.model.oa.WorkReport;
import net.loyin.model.scm.Stock;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chao on 2015/12/15.
 */
@RouteBind(path="workreport")
public class WorkReportCtrl extends AdminBaseController<WorkReport> {

    /**
     * 分页查询
     */
    public void dataGrid() {
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put("keyword", this.getPara("keyword"));
        filter.put("start_date", this.getPara("start_date"));
        filter.put("end_date", this.getPara("end_date"));
        filter.put("company_id", this.getCompanyId());
        filter.put("uid", this.getPara("uid"));
        filter.put("user_id", this.getCurrentUserId());
        filter.put("position_id", this.getPositionId());
        filter.put("type",this.getPara("type"));
        this.sortField(filter);
//        Page<Customer> page = Customer.dao.pageGrid(getPageNo(), getPageSize(), filter, this.getParaToInt("qryType", 11));
        Page<WorkReport> page = WorkReport.dao.pageGrid(getPageNo(), getPageSize(), filter, this.getParaToInt("qryType", 11));
        this.rendJson(true, null, "success", page);

    }





}
