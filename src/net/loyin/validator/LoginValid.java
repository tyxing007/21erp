package net.loyin.validator;

import org.apache.commons.lang3.StringUtils;

import net.loyin.util.safe.CipherUtil;

import com.jfinal.core.Controller;

/**
 * 登录验证
 *
 * @author liugf
 *         2013-6-2 下午3:14:22
 */
public class LoginValid extends BaseValidtor {
    @Override
    protected void validate(Controller c) {
        //todo 在此处校验是否是APP登陆，如果是APP登陆不使用校验码，直接跳过
        filedErrKeyList = new String[]{"vid_company", "vld_userno", "vld_pwd", "vld_validCode"};

        this.validateRequired("company", "vid_company", "企业必填");
        this.validateRequired("userno", "vld_userno", "帐号必填");
        this.validateRequired("pwd", "vld_pwd", "密码必填");
        this.validateRequired("validCode", "vld_validCode", "验证码必填");
        String check = c.getCookie("ValidCode");
        String validCode = c.getPara("validCode");
        /*0 : 手机端请求*/
        String type = c.getPara("request_type");
        if (!"0".equals(type)) {
            if (StringUtils.isEmpty(check)) {
                addError("vld_validCode", "验证码已超时，请重新获取！");
            } else {
                if (!(CipherUtil.decryptData(check)).equals(validCode.toLowerCase())) {
                    addError("vld_validCode", "验证码不匹配");
                }
            }
        }
        c.setCookie("ValidCode", "", 0);
    }
}
