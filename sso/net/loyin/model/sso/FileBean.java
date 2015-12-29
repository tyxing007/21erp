package net.loyin.model.sso;

import com.alibaba.fastjson.JSON;
import com.jfinal.plugin.activerecord.Model;
import net.loyin.jfinal.anatation.TableBind;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 用来保存上传文件
 * by chenjianhui
 * 2015年8月28日11:37:12
 */
@TableBind(name = "oa_file")
public class FileBean extends Model<FileBean> {
    private static final long serialVersionUID = 89043516544927778L;
    public static final String tableName = "oa_file";
    public static FileBean dao = new FileBean();


    public FileBean findById(String id) {
        StringBuffer sql = new StringBuffer("select * from ");
        sql.append(tableName);
        sql.append(" where relation_id=?");
        return dao.findFirst(sql.toString(), id);
    }


    public FileBean findByFileId(String id) {
        StringBuffer sql = new StringBuffer("select * from ");
        sql.append(tableName);
        sql.append(" where id=?");
        return dao.findFirst(sql.toString(), id);
    }


    /**
     * 将图片信息保存到数据库
     * by chenjianhui
     * 2015年8月28日12:41:50
     *
     * @param map
     * @return
     */
    public boolean saveFile(Map<String, Object> map) {

        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            this.set(key, map.get(key));
        }
        save();

        return true;
    }
    public String saveFileBackId(Map<String, Object> map) {

        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            this.set(key, map.get(key));
        }
        save();
        return getStr("id");
    }


    /**
     * 获取客户的图片
     *
     * @param cid
     * @return
     */
    public String getImageSrc(String cid) {
        return this.findByCId(cid);
    }

    private String findByCId(String cid) {
        String src = "";
        FileBean fileBean = this.findFirst("select * from " + tableName + " where relation_id='?'", cid);
        if (fileBean == null)
            return null;
        //将data转换成json
        Map<String, Object> map = fileBean.getAttrs();
        String data = fileBean.getStr("data");
        if (StringUtils.isNotEmpty(data)) {
            map.put("data", JSON.parse(data));
        }

        return null;
    }


    public List<FileBean> findList(String cid) {
        return dao.find("select * from " + tableName + " where relation_id = ?", cid);
    }

    public Map<String, Object> getAttrs() {
        return super.getAttrs();
    }
}
