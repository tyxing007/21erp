package net.loyin.ctrl.app;

import com.jfinal.kit.PathKit;
import com.jfinal.upload.UploadFile;
import net.loyin.ctrl.AdminBaseController;
import net.loyin.jfinal.anatation.RouteBind;
import net.loyin.model.sso.FileBean;
import net.loyin.util.TimeUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by chenjianhui on 2015/10/10.
 */
@RouteBind(path = "upload")
public class uploadCtrl extends AdminBaseController<FileBean> {
    private String modleStr = "oa_file";

    public uploadCtrl() {
        this.modelClass = FileBean.class;
    }


    /**
     * by chenjianhui
     * 2015年10月12日10:33:28
     * APP图片上传
     */
    public void uploadPics() {
        List<Map<String, Object>> list = new ArrayList<>();
        List<Map<String, Object>> temp = new ArrayList<>();
        UploadFile uf = this.getFile();
        String objId = getPara("obj_id");

        if (uf == null || uf.getFile() == null) {
            this.rendJsonApp(false, temp, 203, "error");
            return;
        }

        File file = uf.getFile();
        String fileName = file.getName();
        String filePat = "";
        String extension = fileName.substring(fileName.lastIndexOf("."));
        String prefix;
        if (".png".equals(extension) || ".jpg".equals(extension) || ".gif".equals(extension)) {
            prefix = "img";
          /*  fileName = generateWord() + extension;*/
        } else {
            prefix = "file";
        }


        FileBean fileBean = new FileBean();
        /**--------实体需要参数----------------*/
        String name = "";
        String creater_id = "";
        String file_path = "";
        String create_datetime = "";
        int fsize = 0;
        String save_path = "";
        String relation_id = "";
        /**------------------------*/
        String src = "";

        try {
            FileInputStream fis = new FileInputStream(file);
            if (System.getenv("OS").toUpperCase().equals("WINDOWS_NT")) {
                src = "D:\\EYKJ\\IMAGE";
            } else {
                src = "/home/images/eykj";
            }
            File targetDir = new File(src + "/" + prefix + "/u/");
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }
            File target = new File(targetDir, fileName);
            if (!target.exists()) {
                target.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(target);
            byte[] bts = new byte[300];
            while (fis.read(bts, 0, 300) != -1) {
                fos.write(bts, 0, 300);
            }
            file_path = target.getAbsolutePath();
            save_path = targetDir.getPath();
            fsize = bts.length;
            fos.close();
            fis.close();

        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }

        fileBean.set("name", fileName);
        fileBean.set("creater_id", getCurrentUserId());
        fileBean.set("file_path", file_path);
        fileBean.set("create_datetime", TimeUtil.toDateString(new Date()));
        fileBean.set("fsize", fsize);
        fileBean.set("save_path", save_path);
        fileBean.set("relation_id", objId); /*可为空*/
        fileBean.save();
        list.add(fileBean.getAttrs());
        this.rendJsonApp(false, list, 200, "success");

    }


}
