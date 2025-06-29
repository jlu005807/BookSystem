package gui.components;

import gui.views.AccountView;
import gui.views.RootView;
import gui.views.UserView;
import utils.u;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static gui.actions.*;

import static gui.components.iDialog.*;

public class iMenuBar extends JMenu {
    public iMenuBar(JFrame parent, String type, ActionListener listener) {
        switch (type) {
            case "file":
                setText("文件");
                JMenuItem open = new JMenuItem("打开");
                JMenuItem save = new JMenuItem("保存");
                JMenuItem saveAs = new JMenuItem("另存为");
                JMenuItem exit = new JMenuItem("退出");
                ActionListener fileAction = e -> {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("这个功能并没有完成...");
                    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                        @Override
                        public boolean accept(java.io.File f) {
                            return f.getName().endsWith(".csv");
                        }

                        @Override
                        public String getDescription() {
                            return "表格文件(*.csv)";
                        }
                    });
                    fileChooser.showOpenDialog(parent);
                };
                open.addActionListener(fileAction);
                save.addActionListener(fileAction);
                saveAs.addActionListener(fileAction);
                exit.addActionListener(e -> {
                    exitSys();
                });
                add(open);
                add(save);
                add(saveAs);
                addSeparator(); // 添加分割线
                add(exit);
                break;
            case "login":
                setText("登录");
                JMenuItem adminLogin = new JMenuItem("管理员登录");
                JMenuItem userLogin = new JMenuItem("用户登录");
                ActionListener loginAction = e -> {
                    String userType = "";
                    if (e.getSource() == adminLogin) userType = "root";
                    new AccountView.LoginView(userType, listener);
                };
                adminLogin.addActionListener(loginAction);
                userLogin.addActionListener(loginAction);
                add(adminLogin);
                add(userLogin);
                break;
            case "register":
                setText("注册");
                JMenuItem userRegister = new JMenuItem("注册新用户");
                userRegister.addActionListener(e -> {
                    new AccountView.RegisterView(a -> {
                        u.log(a.getActionCommand());
                        new UserView(1);
                    });
                });
                add(userRegister);
                break;
            case "help":
                setText("帮助");
                JMenuItem help = new JMenuItem("帮助");
                help.addActionListener(e -> {
                    dialogMessage(null, "帮助",
                        "欢迎使用BookSystem！\n" +
                        "本系统支持用户注册、登录、车辆租赁、管理员增删改查等功能。\n" +
                        "如有疑问请联系管理员：1289406660@qq.com\n" +
                        "\n主要功能：\n" +
                        "1. 用户注册/登录\n" +
                        "2. 车辆浏览与租赁\n" +
                        "3. 管理员车辆管理\n" +
                        "4. 数据库持久化存储\n"
                    );
                });
                JMenuItem about = new JMenuItem("关于");
                about.addActionListener(e -> {
                    dialogMessage(null, "关于此系统",
                        "BookSystem v1.0\n" +
                        "开发者：ozy\n" +
                        "联系方式：1289406660@qq.com\n" +
                        "\n本系统为Java Swing课程设计项目，所有界面与功能均为原创实现。"
                    );
                });
                add(help);
                add(about);
                break;
        }
    }

    // 测试
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(500, 500);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JMenuBar menuBar = new JMenuBar();
//        menuBar.add(new iMenuBar(frame, "file"));
//        menuBar.add(new iMenuBar(frame, "login"));
//        menuBar.add(new iMenuBar(frame, "register"));
//        menuBar.add(new iMenuBar(frame, "help"));
        frame.setJMenuBar(menuBar);
        frame.setVisible(true);
    }
}
