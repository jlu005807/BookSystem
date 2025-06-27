import gui.components.iLabel;
import gui.components.iWindow;
import gui.views.RootView;
import gui.views.UserView;
import sql.ConnectionPool;
import sql.MySQLConfig;
import utils.u;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

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
        ConnectionPool.init(config);
        gui.Sys sys = new gui.Sys();
        // 以后如需注销后回到欢迎界面，可直接调用 sys.showWelcome();
    }
}
