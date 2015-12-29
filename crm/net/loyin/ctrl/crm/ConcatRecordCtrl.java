package net.loyin.ctrl.crm;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.loyin.ctrl.AdminBaseController;
import net.loyin.jfinal.anatation.PowerBind;
import net.loyin.jfinal.anatation.RouteBind;
import net.loyin.model.crm.ConcatRecord;

import net.loyin.model.sso.FileBean;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;

/**
 * 联系记录管理
 * @author liugf 风行工作室
 */
@RouteBind(path="concatRecord")
public class ConcatRecordCtrl extends AdminBaseController<ConcatRecord> {

	public ConcatRecordCtrl(){
		this.modelClass=ConcatRecord.class;
	}
	public void dataGrid(){
		Map<String,Object> filter=new HashMap<String,Object>();
		filter.put("company_id",this.getCompanyId());
		filter.put("keyword",this.getPara("keyword"));
		filter.put("start_date",this.getPara("start_date"));
		filter.put("end_date",this.getPara("end_date"));
		filter.put("status",this.getPara("status"));
		filter.put("uid",this.getPara("uid"));
		filter.put("user_id",this.getCurrentUserId());
		filter.put("position_id",this.getPositionId());
		this.sortField(filter);
		this.rendJson(true, null, "",ConcatRecord.dao.page(this.getPageNo(),this.getPageSize(),filter,this.getParaToInt("qryType",-1)));
	}
	@PowerBind(code="A1_1_E",funcName="删除")
	@RouteBind(code="000DEL", model = "", name = "")
	public void del() {
		try {
			getId();
			ConcatRecord.dao.del(id,this.getCompanyId());
			rendJson(true,null,"删除成功！",id);
		} catch (Exception e) {
			log.error("删除异常", e);
			rendJson(false,null,"删除失败！请检查是否被使用！");
		}
	}
	@PowerBind(code="A1_1_V",funcName="查看")
	public void qryOp() {
		getId();
		ConcatRecord m = ConcatRecord.dao.findById(id, this.getCompanyId());
		if (m == null)
			this.rendJson(false,null, "记录不存在！");
		else
			this.rendJson(true, null, "",m);
	}

	/**
	 * 获取图片地址
	 */
	public void qryPic() {
		getId();
//		ConcatRecord m = ConcatRecord.dao.findById(id, this.getCompanyId());
		List<FileBean> fileBeanList = FileBean.dao.findList(id);
		if (fileBeanList == null || fileBeanList.size() == 0 )
			this.rendJson(false,null, "图片记录不存在！");
		else
			this.rendJson(true, null, "",fileBeanList);
	}



	@PowerBind(code="A1_1_E",funcName="编辑")
	public void save() {
		try {
			ConcatRecord po = (ConcatRecord) getModel();
			if (po == null) {
				this.rendJson(false,null, "提交数据错误！");
				return;
			}
			getId();
			this.pullUser(po, this.getCurrentUserId());
			if (StringUtils.isEmpty(id)) {
				po.set("company_id", this.getCompanyId());
				po.save();
				id=po.getStr("id");
			} else {
				po.update();
			}
			this.rendJson(true,null, "操作成功！",id);
		} catch (Exception e) {
			log.error("保存异常", e);
			this.rendJson(false,null, "保存数据异常！");
		}
	}

	/**
	 * 获取图片
	 */
	public void downloadFile(){
		try{
			HttpServletResponse response = this.getResponse();
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			getId();
			FileBean fileBean = FileBean.dao.findByFileId(id);
			if(fileBean == null){
				this.rendJson(false,null, "保存数据异常！");
			}
			BufferedInputStream bis = null;
			OutputStream os = null;
			FileInputStream fileInputStream = new FileInputStream(new File(fileBean.getStr("file_path")));

			bis = new BufferedInputStream(fileInputStream);
			byte[] buffer = new byte[512];
			response.reset();
			response.setCharacterEncoding("UTF-8");
			//不同类型的文件对应不同的MIME类型
			response.setContentType("image/*");
			//文件以流的方式发送到客户端浏览器
			//response.setHeader("Content-Disposition","attachment; filename=img.jpg");
			//response.setHeader("Content-Disposition", "inline; filename=img.jpg");

			response.setContentLength(bis.available());

			os = response.getOutputStream();
			int n;
			while ((n = bis.read(buffer)) != -1) {
				os.write(buffer, 0, n);
			}
			bis.close();
			os.flush();
			os.close();
//			this.rendJson(true,null, "操作成功！",id);
			this.renderNull();
		}catch(Exception e){
//			e.printStackTrace();
		}
//
	}

}
