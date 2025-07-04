package sql;

import utils.u;
import utils.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * MySQL数据库连接管理类
 */
public class MySQLConnection {
    /**
     * 建立数据库连接
     * @param c 数据库配置信息
     * @return Connection 数据库连接对象
     */
    public static Connection connect(MySQLConfig c) {
        Connection con = null;
        
        // 记录连接尝试
        Logger.info("DB", "连接", "尝试连接数据库: " + c.ip + ":" + c.port + "/" + c.database);
        
        try { 
            // 加载数据库驱动类
            Class.forName("com.mysql.cj.jdbc.Driver");
            Logger.logDatabaseConnection("驱动加载", "MySQL JDBC驱动加载成功");
            u.log("数据库驱动加载成功");
        } catch (ClassNotFoundException e) {
            Logger.error("数据库驱动加载失败", e);
            u.err("数据库驱动加载失败");
            return null;
        }
        
        try { 
            // 通过访问数据库的URL获取数据库连接对象
            String url = "jdbc:mysql://" + c.ip + ":" + c.port + "/" + c.database +
                    "?useSSL=false&serverTimezone=UTC";
            con = DriverManager.getConnection(url, c.username, c.password);
            
            Logger.logDatabaseConnection("连接成功", 
                "数据库: " + c.database + ", 用户: " + c.username + ", 地址: " + c.ip + ":" + c.port);
            u.log("数据库连接成功");
            
        } catch (SQLException e) {
            Logger.error("数据库连接失败: " + e.getMessage(), e);
            Logger.logDatabaseConnection("连接失败", 
                "错误代码: " + e.getErrorCode() + ", SQL状态: " + e.getSQLState());
            u.err("数据库连接失败");
        }
        
        return con;
    }

    /**
     * 关闭数据库连接
     * @param con 数据库连接对象
     */
    public static void close(Connection con) {
        if (con == null) {
            Logger.warn("尝试关闭空连接");
            return;
        }
        
        try {
            con.close();
            Logger.logDatabaseConnection("连接关闭", "数据库连接已安全关闭");
            u.log("数据库连接关闭成功");
        } catch (SQLException e) {
            Logger.error("数据库连接关闭失败", e);
            u.err("数据库连接关闭失败");
        }
    }
    
    /**
     * 测试数据库连接
     * @param c 数据库配置信息
     * @return boolean 连接是否成功
     */
    public static boolean testConnection(MySQLConfig c) {
        Logger.info("开始测试数据库连接");
        Connection testCon = connect(c);
        if (testCon != null) {
            close(testCon);
            Logger.info("数据库连接测试成功");
            return true;
        } else {
            Logger.error("数据库连接测试失败");
            return false;
        }
    }
}

