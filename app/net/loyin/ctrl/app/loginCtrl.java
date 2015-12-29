package net.loyin.ctrl.app;

import net.loyin.ctrl.AdminBaseController;
import net.loyin.jfinal.anatation.RouteBind;
import net.loyin.model.ReturnMsg;
import net.loyin.model.sso.Person;
import net.loyin.model.sso.User;
import net.loyin.util.BaseTurn;
import net.loyin.util.safe.MD5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenjianhui on 2015/10/8.
 */
@RouteBind(path = "login")
public class loginCtrl extends AdminBaseController<User> {
    private String modleStr = "sso_user";


    public loginCtrl() {
        this.modelClass = User.class;
    }

    /**
     * 更改密码
     */
    public void alterPassword() {
        List<Map<String, Object>> temp = new ArrayList<>();
        List<Map<String, Object>> list = new ArrayList<>();

        String loginName = getPara("login_name");
        String password = getPara("password");
        String newpassword = getPara("newpassword");
        String companyId = getCompanyId();


        password = MD5.getMD5ofStr(password);
        User user = User.dao.login(loginName, password, companyId);

        if (user == null) {
            rendJsonApp(false, temp, 500, "error");
            return;
        }
        newpassword = MD5.getMD5ofStr(newpassword);

        user.set("password", newpassword);
        user.update();
        list.add(user.getAttrs());
        rendJsonApp(true, list, 200, "success");
    }

    /**
     * 修改手机号码
     */
    public void alterPhone() {
        List<Map<String, Object>> temp = new ArrayList<>();
        List<Map<String, Object>> list = new ArrayList<>();

        String loginName = getPara("login_name");
        String password = getPara("password");
        String phone = getPara("phone");
        String companyId = getCompanyId();


        password = MD5.getMD5ofStr(password);
        User user = User.dao.login(loginName, password, companyId);

        if (user == null) {
            rendJsonApp(false, temp, 500, "error");
            return;
        }


        Person person = Person.dao.findById(user.get("id"));
        person.set("mobile", phone);
        person.update();
        ReturnMsg returnMsg = new ReturnMsg();
        returnMsg.setReturn_msg(person.getStr("id"));
        Map<String, Object> map = new HashMap<>();
        map.put("return_msg", returnMsg.getReturn_msg());
        list.add(map);
        rendJsonApp(true, list, 200, "success");
    }
}
