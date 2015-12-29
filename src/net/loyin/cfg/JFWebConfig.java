package net.loyin.cfg;

import com.jfinal.plugin.activerecord.tx.TxByRegex;
import net.loyin.handler.ServletHandler;
import net.loyin.interceptor.OperateLogInterceptor;
import net.loyin.interceptor.PermissionInterceptor;
import net.loyin.jfinal.plugin.AutoTableBindPlugin;
import net.loyin.jfinal.plugin.MyRoutesUtil;
import net.loyin.jfinal.plugin.TableNameStyle;
import net.loyin.jfinal.plugin.activerecord.dialect.MyPostgreSqlDialect;
import net.loyin.jfinal.plugin.quartz.QuartzPlugin;
import net.loyin.kit.I18N;
import net.loyin.model.crm.Customer;
import net.loyin.model.sso.Position;
import net.loyin.netService.NetServiceInit;
import net.loyin.util.BaseTurn;
import net.loyin.util.PropertiesContent;

import net.loyin.utli.AppGlobal;
import org.apache.log4j.Logger;
import org.snaker.jfinal.plugin.SnakerPlugin;

import com.alibaba.druid.filter.stat.StatFilter;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.ext.handler.FakeStaticHandler;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.druid.DruidStatViewHandler;
import com.jfinal.plugin.ehcache.EhCachePlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * API引导式配置
 *
 * @author liugf 风行工作室
 *         2013-6-5
 */
public class JFWebConfig extends JFinalConfig {
    private Logger log = Logger.getLogger(getClass());
    public static DruidPlugin druidPlugin;

    /**
     * 配置常量O
     */
    public void configConstant(Constants me) {
        //SN校验
        /*if(SnChecker.check()==false){
            System.exit(0);
			try {
				throw new Exception("授权文件验证不通过");
			} catch (Exception e) {
				log.error(e);
			}
		}*/
//		loadPropertyFile("a_little_config.txt");				// 加载少量必要配置，随后可用getProperty(...)获取值
        me.setDevMode(PropertiesContent.getToBool("jfinal.devmode", false));

//		设定采用httl模板引擎
//		me.setMainRenderFactory(new HttlRenderFactory());
        /**无权限访问时，跳转到登录!*/
        me.setErrorView(401, "/WEB-INF/app/login.html");
        me.setErrorView(403, "/WEB-INF/app/login.html");
        me.setError404View("/WEB-INF/app/err404.html");
        me.setError500View("/WEB-INF/app/err500.html");
        me.setBaseViewPath("/app/");
        /**国际化properties文件加载 只调用一次*/
        I18N.init("i18n", "dic", null);//字典
        I18N.init("i18n", "msg", null);//信息
        I18N.init("i18n", "err", null);//异常
//		me.setUploadedFileSaveDirectory("D:/temp");
        me.setMaxPostSize(PropertiesContent.getToInteger("maxpostsize", 104857600));//100mb
    }

    /**
     * 配置路由
     */
    public void configRoute(Routes me) {
        MyRoutesUtil.add(me);
        log.debug("配置路由");
    }

    /**
     * 配置插件
     */
    public void configPlugin(Plugins me) {
        me.add(new EhCachePlugin());
        // 配置数据库连接池插件
        druidPlugin = new DruidPlugin(PropertiesContent.get("jdbc.url"),
                PropertiesContent.get("jdbc.username"),
                PropertiesContent.get("jdbc.password"),
                PropertiesContent.get("jdbc.driver"));
    /*	WallFilter wall = new WallFilter();
        wall.setDbType(PropertiesContent.get("jdbc.dbType"));
		WallConfig wc = new WallConfig();
		wc.setMultiStatementAllow(true);//支持多语句执行，要限于查询类。较危险的配置
//		wall.setConfig(wc);
		druidPlugin.addFilter(wall);*/
        druidPlugin.addFilter(new StatFilter());
        druidPlugin.start();
        me.add(druidPlugin);
        //添加自动绑定model与表插件
        AutoTableBindPlugin autoTableBindPlugin = new AutoTableBindPlugin(druidPlugin, TableNameStyle.LOWER);
        autoTableBindPlugin.setShowSql(true);
        autoTableBindPlugin.setDialect(new MyPostgreSqlDialect());
        autoTableBindPlugin.setContainerFactory(new CaseInsensitiveContainerFactory(true));//postgresql时 一定要设置为true 即采用小写
        me.add(autoTableBindPlugin);
        /**配置定时器插件 在job.properties里配置对应的定时任务*/
        QuartzPlugin quartzPlugin = new QuartzPlugin();
        me.add(quartzPlugin);
        /**snaker 工作流*/
        SnakerPlugin snakerPlugin = new SnakerPlugin(druidPlugin);
        snakerPlugin.start();
        me.add(snakerPlugin);
    }

    /**
     * 配置全局拦截器
     */
    public void configInterceptor(Interceptors me) {
        me.add(new PermissionInterceptor());//权限控制
        me.add(new TxByRegex(".*save*.*"));
        me.add(new TxByRegex(".*update*.*"));
//		me.add(new OperateLogInterceptor());//日志记录
    }

    /**
     * 配置处理器
     */
    public void configHandler(Handlers me) {
        me.add(new ServletHandler());//添加处理Servlet的handler
        DruidStatViewHandler dvh = new DruidStatViewHandler("/druid");
        me.add(dvh);
        //该处理器将request.getContextPath()存储在root中，可以解决路径问题
        ContextPathHandler path = new ContextPathHandler("root");
        me.add(path);
        me.add(new FakeStaticHandler());//伪静态.html
        me.add(new FakeStaticHandler(".json"));//通过.json后缀访问后台链接
        me.add(new FakeStaticHandler(".do"));//通过.do后缀访问后台链接
    }

    public void afterJFinalStart() {
        Position.dao.cachPermission(null);
          /*初始化所有要对应的参数*/
        List<BaseTurn> baseTurns = BaseTurn.dao.findAll();
        AppGlobal.getInstance().BaseTurns = baseTurns;
//        BusinessProcess businessProcess = new BusinessProcess();
//        businessProcess.
        NetServiceInit netServiceInit = new NetServiceInit();
        netServiceInit.Init();

    }

    public void beforeJFinalStop() {

    }
}
