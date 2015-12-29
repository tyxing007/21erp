package net.loyin.ctrl.crm;

import net.loyin.ctrl.AdminBaseController;
import net.loyin.jfinal.anatation.RouteBind;
import net.loyin.model.crm.Parame;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenjianhui on 2015/9/6.
 * 网点类型管理
 */
@RouteBind(path = "retailParame")
public class RetailParameCtrl extends AdminBaseController<Parame> {
    public RetailParameCtrl() {
        this.modelClass = Parame.class;
    }

    /**
     * 获取所有网点父类型的数据
     */
    public void getDatas() {
        Map<String, Object> map = new HashMap<String, Object>();
        List list = Parame.dao.getAllOfRetailType(getCompanyId());
        this.rendJsonApp(true,list, 200,"success");
    }

}
