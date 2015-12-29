package net.loyin.ctrl.scm;

import net.loyin.ctrl.AdminBaseController;
import net.loyin.jfinal.anatation.RouteBind;
import net.loyin.model.scm.Average;
import net.loyin.model.scm.Order;

import java.util.HashMap;
import java.util.Map;

/**
 * average
 * Created by Chao on 2015/10/10.
 */
@RouteBind(path="average",sys="全月一次平均加权",model="查询")
public class AverageCtrl extends AdminBaseController<Average> {
    public AverageCtrl() {
        this.modelClass = Average.class;
    }

    public void dataGrid(){
        Map<String, String> userMap = this.getUserMap();
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put("company_id", userMap.get("company_id"));
        filter.put("keyword", this.getPara("keyword"));
        filter.put("category", this.getPara("category"));
        filter.put("searchBrand",this.getPara("brand"));
        filter.put("customer_id",this.getPara("customer_id"));
        filter.put("status",this.getPara("status"));
        filter.put("year",this.getPara("year"));
        filter.put("month",this.getPara("month"));
        this.sortField(filter);
        this.rendJson(true, null, "", Average.dao.page(this.getPageNo(), this.getPageSize(), filter));
    }


}
