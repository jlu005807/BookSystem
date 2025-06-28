package gui;

import gui.components.iLabel;
import gui.components.iWindow;
import gui.views.RootView;
import gui.views.UserView;
import utils.u;

import javax.swing.*;
import java.awt.*;

public class Sys {
    private iWindow w;
    public Sys() {
        w = new iWindow("BookSystem", 800, 800, true);
        showWelcome();
        w.done();
    }

    public void showWelcome() {
        w.getContentPane().removeAll();
        w.setLayout(new BorderLayout());

        // 欢迎图片和欢迎词
        JPanel welcomePanel = new JPanel();
        welcomePanel.setOpaque(false);
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        iLabel welcomeImage = new iLabel(
                "",
                "car-rental.png",
                200, 200,
                iLabel.VERTICAL
        );
        welcomeImage.setAlignmentX(Component.CENTER_ALIGNMENT);
        iLabel welcomeText = new iLabel("欢迎使用BookSystem", 28);
        welcomeText.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeText.setFont(new Font("微软雅黑", Font.BOLD, 28));
        welcomeText.setCenter();
        welcomePanel.add(Box.createVerticalGlue());
        welcomePanel.add(welcomeImage);
        welcomePanel.add(Box.createVerticalStrut(20));
        welcomePanel.add(welcomeText);
        welcomePanel.add(Box.createVerticalGlue());

        // 按钮面板
        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.setLayout(new GridLayout(1, 3, 40, 0));

        gui.components.iButton loginBtn = new gui.components.iButton("登录", gui.components.iButton.ButtonType.PRIMARY);
        gui.components.iButton registerBtn = new gui.components.iButton("注册", gui.components.iButton.ButtonType.NORMAL);
        gui.components.iButton helpBtn = new gui.components.iButton("帮助", gui.components.iButton.ButtonType.WARNING);
        loginBtn.setFont(new Font("微软雅黑", Font.BOLD, 22));
        registerBtn.setFont(new Font("微软雅黑", Font.BOLD, 22));
        helpBtn.setFont(new Font("微软雅黑", Font.BOLD, 22));
        loginBtn.setPreferredSize(new Dimension(160, 60));
        registerBtn.setPreferredSize(new Dimension(160, 60));
        helpBtn.setPreferredSize(new Dimension(160, 60));

        // 登录按钮弹窗选择
        loginBtn.addActionListener(e -> {
            gui.components.CustomDialogs.showLoginTypeDialog(w, type -> {
                if ("user".equals(type)) {
                    new gui.views.AccountView.LoginView(cmd -> {
                        if ("user".equals(cmd.getActionCommand())) {
                            w.setContentPane(new UserView());
                            w.setJMenuBar(null);
                            w.revalidate();
                        }
                    });
                } else if ("root".equals(type)) {
                    new gui.views.AccountView.LoginView("root", cmd -> {
                        if ("root".equals(cmd.getActionCommand())) {
                            w.setContentPane(new RootView());
                            w.setJMenuBar(null);
                            w.revalidate();
                        }
                    });
                }
            });
        });
        registerBtn.addActionListener(e -> new gui.views.AccountView.RegisterView(null));
        helpBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(w,
                    "BookSystem 图书管理系统\n" +
                    "\n主要功能：\n" +
                    "1. 用户注册/登录（支持普通用户和管理员）\n" +
                    "2. 图书浏览、借阅、归还、借阅记录查询\n" +
                    "3. 管理员可添加、删除图书，查看所有借阅记录\n" +
                    "4. 数据持久化，界面美观现代\n" +
                    "\n使用说明：\n" +
                    "- 登录后，用户可浏览和借阅图书，查看和归还自己的借阅记录。\n" +
                    "- 管理员可对图书进行管理。\n" +
                    "- 如有疑问请联系管理员：1289406660@qq.com",
                    "帮助", JOptionPane.INFORMATION_MESSAGE);
        });

        btnPanel.add(loginBtn);
        btnPanel.add(registerBtn);
        btnPanel.add(helpBtn);

        // 主面板布局
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(welcomePanel);
        centerPanel.add(Box.createVerticalStrut(40));
        JPanel btnPanelWrap = new JPanel();
        btnPanelWrap.setOpaque(false);
        btnPanelWrap.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        btnPanelWrap.add(btnPanel);
        centerPanel.add(btnPanelWrap);
        centerPanel.add(Box.createVerticalGlue());

        w.add(centerPanel, BorderLayout.CENTER);
        w.revalidate();
        w.repaint();
    }

    public iWindow getWindow() {
        return w;
    }

    public static void main(String[] args) {
        new Sys();
    }
}
