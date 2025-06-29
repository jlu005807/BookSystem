package test;

import gui.components.iWindow;
import gui.views.UserView;
import sql.ConnectionPool;
import sql.MySQLConfig;

import javax.swing.*;
import java.awt.*;

/**
 * UserView防重复借阅功能测试程序
 */
public class UserViewTest {
    public static void main(String[] args) {
        // 初始化数据库连接
        ConnectionPool.init(new MySQLConfig("rental"));
        
        // 创建主窗口
        iWindow window = new iWindow("图书管理系统 - 防重复借阅测试", 1000, 800, true);
        window.setLayout(new BorderLayout());
        
        // 创建用户界面
        UserView userView = new UserView(1);
        window.setContentPane(userView);
        
        // 显示窗口
        window.done();
        
        System.out.println("UserView防重复借阅功能测试程序已启动");
        System.out.println("功能说明：");
        System.out.println("1. 在搜索框中输入关键词可以搜索书籍");
        System.out.println("2. 已借阅的书籍会显示'已借阅'状态");
        System.out.println("3. 点击已借阅的书籍会提示不能重复借阅");
        System.out.println("4. 点击未借阅的书籍可以正常借阅");
        System.out.println("5. 点击'刷新'按钮保持搜索状态");
        System.out.println("6. 点击'清空搜索'按钮清空搜索框");
        System.out.println("7. 点击'我的借阅'查看借阅记录");
    }
} 