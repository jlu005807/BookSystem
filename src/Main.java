import gui.components.iLabel;
import gui.components.iWindow;
import gui.views.RootView;
import gui.views.UserView;
import sql.ConnectionPool;
import sql.MySQLConfig;
import utils.u;
import utils.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * 图书管理系统主程序入口
 */
public class Main {
    //在这里设置MySQL的用户名、密码、IP、端口和数据库名
    static MySQLConfig config = new MySQLConfig(
            "root",    //用户名
            "123456",                 //密码
            "localhost",        //IP
            3306,               //端口
            "rental"            //数据库名
    );

    public static void main(String[] args) {
        try {
            // 初始化日志系统
            Logger.init();
            Logger.info("SYSTEM", "启动", "=== 图书管理系统启动 ===");
            Logger.info("数据库配置: " + config.ip + ":" + config.port + "/" + config.database);
            
            // 测试数据库连接
            if (!sql.MySQLConnection.testConnection(config)) {
                Logger.error("数据库连接测试失败，请检查配置");
                JOptionPane.showMessageDialog(null, 
                    "数据库连接失败！\n请检查：\n1. MySQL服务是否启动\n2. 数据库配置是否正确\n3. 网络连接是否正常\n\n详细错误信息请查看日志文件: " + Logger.getLogFilePath(), 
                    "连接错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 初始化连接池
            ConnectionPool.init(config);
            Logger.info("连接池初始化成功");
            
            // 启动GUI系统
            gui.Sys sys = new gui.Sys();
            Logger.info("GUI系统启动成功");
            
            // 注册JVM关闭钩子，确保日志正确关闭
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                Logger.info("=== 系统正常关闭 ===");
                Logger.close();
            }));
            
        } catch (Exception e) {
            Logger.error("系统启动失败", e);
            JOptionPane.showMessageDialog(null, 
                "系统启动失败！\n错误信息: " + e.getMessage() + "\n\n详细错误信息请查看日志文件: " + Logger.getLogFilePath(), 
                "启动错误", JOptionPane.ERROR_MESSAGE);
        }
        
        // 注销后回到欢迎界面，可直接调用 sys.showWelcome();
    }
}
