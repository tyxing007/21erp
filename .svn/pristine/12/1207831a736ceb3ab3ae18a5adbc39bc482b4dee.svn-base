package net.loyin.ctrl.sso;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.loyin.ctrl.AdminBaseController;
import net.loyin.jfinal.anatation.PowerBind;
import net.loyin.jfinal.anatation.RouteBind;
import net.loyin.model.sso.Company;
import net.loyin.model.sso.Position;

import net.loyin.model.sso.User;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * 岗位/部门
 *
 * @author liugf 风行工作室
 */
@RouteBind(model = "岗位", path = "position", sys = "设置")
public class PositionCtrl extends AdminBaseController<Position> {
    public PositionCtrl() {
        this.modelClass = Position.class;
    }

    public void dataGrid() {
        List<Record> list = Position.dao.treeAllList(this.getCompanyIdByHttp());
        List<Record> list_temp = new ArrayList<Record>();
        if (list != null && list.isEmpty() == false) {
            dotree(null, list, list_temp, 0, true);
        }
        Page<Record> page = new Page<Record>(list_temp, 1, 0, 0, (list == null || list.isEmpty()) ? 0 : list.size());
        this.rendJson(true, null, "", page);
    }

    public void dataTree(){
        Map map = getRequest().getParameterMap();
        int type = 0;
        if(map.containsKey("type")){
            type = Integer.valueOf(String.valueOf(map.get("type")));
        }
        if(map.containsKey("pid")){

        }
        if(map.containsKey("id")){

        }
        List<Record> list = Position.dao.treeList(this.getCompanyId(), type);
        List<List<Map<String,Object>>> mapList = new ArrayList<>();
        for(Record r : list){
            if(r.getStr("pid")==null||r.getStr("pid").isEmpty()){
                mapList.add(doTreeDepart(list,r.getStr("id")));
            }
        }
        this.rendJson(true, null, "", mapList);
    }


    //整理数据库获取的节点，根据父节点找子节点
    private List<Map<String,Object>> doTreeDepart(List<Record> list,String pid){
        Map<String,Object> recordMap = null;
        List<Map<String,Object>> mapList = new ArrayList<>();
        for(Record r:list) {
            if(r.getStr("pid")!=null&&!r.getStr("pid").isEmpty()&&r.getStr("pid").equals(pid)){
                recordMap = new HashMap<>();
                recordMap.put("id",r.getStr("id"));
                recordMap.put("pid",r.getStr("pid"));
                recordMap.put("name", r.getStr("name"));
                recordMap.put("type",r.getInt("type"));
                recordMap.put("sort_num", r.getInt("sort_num"));
                List<Map<String,Object>> childList  = doTreeDepart(list,r.getStr("id"));
                if(childList.size()!=0){
                    recordMap.put("child",childList);
                }
                mapList.add(recordMap);
            }
        }
        return mapList;
    }

    /**
     * 维护树
     */
    public void tree() {
        List<Record> recordList = Position.dao.treeAllList(this.getCompanyIdByHttp());
        if(recordList.size()==0){
//                .id,d.pid,d.type,d.name,d.sort_num
            Record record = new Record();
            record.set("id",null);
            record.set("pid",null);
            record.set("type","空");
            record.set("name","空");
            record.set("sort_num","空");
            recordList.add(record);
        }
        this.rendJson(true, null, "", recordList);
    }

    /**
     * 岗位树
     */
    public void postionTree() {
        List<Record> recordList = Position.dao.treeList(this.getCompanyIdByHttp(), 1);
        if(recordList.size()==0){
//                .id,d.pid,d.type,d.name,d.sort_num
            Record record = new Record();
            record.set("id",null);
            record.set("pid",null);
            record.set("type","空");
            record.set("name","空");
            record.set("sort_num","空");
            recordList.add(record);
        }
        this.rendJson(true, null, "", recordList);
    }

    /**
     * 部门树
     */
    public void departMentTree() {
        try {
            List<Record> recordList = Position.dao.treeList(this.getCompanyIdByHttp(), 0);
            if(recordList.size()==0){
//                .id,d.pid,d.type,d.name,d.sort_num
                Record record = new Record();
                record.set("id",null);
                record.set("pid",null);
                record.set("type","空");
                record.set("name","空");
                record.set("sort_num","空");
                recordList.add(record);
            }
            this.rendJson(true, null, "", recordList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void qryOp() {
        getId();
        Position m = Position.dao.findById(id, this.getCompanyIdByHttp());
        if (m != null)
            this.rendJson(true, null, "", m);
        else
            this.rendJson(false, null, "记录不存在！");
    }

    /**
     * 查询权限
     */
    public void qryPermission() {
        getId();
        String permission = Position.dao.qryPermission(id, this.getCompanyIdByHttp());
        this.rendJson(true, null, "", permission);
    }

    @PowerBind(code = "A11_1_E", funcName = "设置权限")
    public void savePermission() {
        getId();
        String[] code = this.getParaValues("code");
        String permission = ArrayUtils.toString(code, "");
        try {
            permission = permission.replace("{", "").replace("}", "");
            Position.dao.savePermission(id, permission, getCompanyIdByHttp());
            this.rendJson(true, null, "保存权限设置成功！");
        } catch (Exception e) {
            log.error("保存权限设置异常", e);
            this.rendJson(false, null, "保存权限设置异常！");
        }
    }

    @PowerBind(code = "A11_1_E", funcName = "编辑")
    public void save() {
        try {
            Position po = (Position) getModel();
            if (po == null) {
                this.rendJson(false, null, "提交数据错误！");
                return;
            }
            id = po.getStr("id");
            if (StringUtils.isEmpty(id)) {
                po.set("company_id", po.get("company_id", this.getCompanyId()));
                po.save();
                id = po.getStr("id");
            } else {
                po.set("company_id", po.get("company_id", this.getCompanyId()));
                po.update();
            }
            this.rendJson(true, null, "操作成功！", id);
        } catch (Exception e) {
            log.error("保存用户异常", e);
            this.rendJson(false, null, "保存数据异常！");
        }
    }

    @PowerBind(code = "A11_1_E", funcName = "删除")
    public void del() {
        try {
            getId();
            Position.dao.del(id, this.getCompanyIdByHttp());
            rendJson(true, null, "删除成功！", id);
        } catch (Exception e) {
            log.error("删除异常", e);
            rendJson(false, null, "删除失败！");
        }
    }



    public void getCompanyTree() {
        Map<String,String> map = null;
        List<Map<String,String>> list= new ArrayList<>();
            List<Company> companyList = Company.dao.qryAll();
//        Map<String,Map> rendMap = new HashMap<>();
        for(Company company : companyList){
            map = new HashMap<>();
            map.put("id",company.getStr("id"));
            map.put("name", company.getStr("name"));
            list.add(map);
//            rendMap.put(company.getStr("id"),map);
        }
        this.rendJson(true, null, "", list);
    }
}
