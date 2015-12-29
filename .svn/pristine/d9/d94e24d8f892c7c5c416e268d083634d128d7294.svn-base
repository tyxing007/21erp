package net.loyin.netService.listener.Impl;


import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import net.loyin.netService.handler.NetHandler;
import net.loyin.netService.listener.IStartListener;
import net.loyin.util.PropertiesContent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
//import com.jfinal.server.

/**
 * <pre>HTTP Server监听</pre>
 * <pre>所属模块：系统通讯</pre>
 *
 * @author 吴为超
 * @version 1.0 创建于 2014/8/1
 */

public class StartListenerImpl implements IStartListener {
//    PropertiesHelper pHelper = PropertiesFactory.getPropertiesHelper(PropertiesFile.APP);


    /**
     * 开始
     */
    public void run() {
        try {
            HttpHandler netHandler = new NetHandler();
            String Listener_Server_Ip = InetAddress.getLocalHost().getHostAddress();
            String Listener_Server_Port = PropertiesContent.get("Listener_Server_Port");
            HttpServer hs = HttpServer.create(new InetSocketAddress(Listener_Server_Ip, Integer.parseInt(Listener_Server_Port)), 0);
            hs.createContext("/req", netHandler);
            hs.setExecutor(null);
            hs.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
