package gui.views;

import gui.components.iButton;
import gui.components.iField;
import gui.components.iLabel;
import gui.components.iWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import sql.UserController;
import entity.UserItem;
import utils.u;
import gui.components.iDialog;

import static gui.actions.actionPopup;

public class AccountView {
    public static class LoginView {
        final int width = 300;
        iWindow w;

        public LoginView(String username, ActionListener listener) {
            String title = "root".equals(username) ? "管理员登录" : "用户登录";
            w = new iWindow(title, width, 340);
            w.getContentPane().setBackground(new Color(245, 250, 255));
            w.setLayout(null);

            // 顶部Logo和欢迎语同一行左对齐
            JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
            logoPanel.setBounds(10, 10, 280, 60);
            logoPanel.setOpaque(false);
            JLabel logo = new JLabel(u.getImageIcon("user", 40, 40));
            iLabel welcome = new iLabel("欢迎使用BookSystem", 18);
            logoPanel.add(logo);
            logoPanel.add(welcome);
            w.add(logoPanel);

            // 用户名标签
            JLabel userLabel = new JLabel("用户名");
            userLabel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
            userLabel.setBounds(20, 80, 100, 20);
            w.add(userLabel);
            // 用户名输入框
            JTextField usernameField = new JTextField();
            usernameField.setText(username);
            usernameField.setBounds(20, 100, 260, 36);
            usernameField.setFont(new Font("微软雅黑", Font.PLAIN, 16));
            w.add(usernameField);

            // 密码标签
            JLabel passLabel = new JLabel("密码");
            passLabel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
            passLabel.setBounds(20, 140, 100, 20);
            w.add(passLabel);
            // 密码输入框
            JPasswordField passwordField = new JPasswordField();
            passwordField.setBounds(20, 160, 260, 36);
            passwordField.setFont(new Font("微软雅黑", Font.PLAIN, 16));
            w.add(passwordField);

            // 登录按钮
            iButton loginButton = new iButton("登录", iButton.ButtonType.PRIMARY);
            loginButton.setBounds(100, 200, 100, 38);
            w.add(loginButton);

            // 错误提示
            iLabel errorMessage = new iLabel("");
            errorMessage.setBounds(0, 245, width, 30);
            errorMessage.setForeground(Color.RED);
            errorMessage.setFont(new Font("微软雅黑", Font.BOLD, 14));
            errorMessage.setCenter();
            w.add(errorMessage);

            // 登录逻辑
            ActionListener doLogin = e -> {
                final String user = usernameField.getText();
                final String password = new String(passwordField.getPassword());
                if (UserController.check(user, password)) {
                    iDialog.showLoginSuccessDialog(w, user);
                    actionPopup(w, listener, 0,
                            user.equals("root") ? "root" : "user"
                    );
                    w.dispose();
                } else {
                    errorMessage.setText("用户名或密码错误!");
                    errorMessage.setForeground(new Color(220, 53, 69));
                    errorMessage.setFont(new Font("微软雅黑", Font.BOLD, 15));
                    // 输入框边框仅变红，使用CompoundBorder
                    javax.swing.border.Border redBorder = BorderFactory.createLineBorder(new Color(220,53,69), 2);
                    javax.swing.border.Border margin = BorderFactory.createEmptyBorder(4, 8, 4, 8);
                    usernameField.setBorder(BorderFactory.createCompoundBorder(redBorder, margin));
                    passwordField.setBorder(BorderFactory.createCompoundBorder(redBorder, margin));
                    // 标签字体变红加粗
                    userLabel.setForeground(new Color(220,53,69));
                    userLabel.setFont(new Font("微软雅黑", Font.BOLD, 15));
                    passLabel.setForeground(new Color(220,53,69));
                    passLabel.setFont(new Font("微软雅黑", Font.BOLD, 15));
                }
            };
            loginButton.addActionListener(doLogin);
            // 支持回车登录
            passwordField.addActionListener(doLogin);
            usernameField.addActionListener(doLogin);

            // 增加上下方向键切换输入框焦点功能（登录界面）
            usernameField.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyPressed(java.awt.event.KeyEvent e) {
                    if (e.getKeyCode() == java.awt.event.KeyEvent.VK_DOWN) {
                        passwordField.requestFocus();
                    }
                }
            });
            passwordField.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyPressed(java.awt.event.KeyEvent e) {
                    if (e.getKeyCode() == java.awt.event.KeyEvent.VK_UP) {
                        usernameField.requestFocus();
                    }
                }
            });

            w.done();
        }

        public LoginView(ActionListener listener) {
            this("", listener);
        }
    }

    public static class RegisterView {
        final int width = 330;
        iWindow w = new iWindow("新用户注册", width, 460);
        iButton loginButton = new iButton("注册", iButton.ButtonType.SMALL);
        String username;
        String password;
        iField usernameField = new iField("用户名", width);
        iField passwordField1 = new iField("密码", width);
        iField passwordField2 = new iField("确认密码", width);
        iLabel errorMessage = new iLabel("");

        public RegisterView(ActionListener listener) {
            w.setLayout(null);

            // 顶部Logo和欢迎语整体左对齐
            JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
            logoPanel.setBounds(0, 20, 330, 60);
            logoPanel.setOpaque(false);
            JLabel logo = new JLabel(u.getImageIcon("user", 48, 48));
            iLabel welcome = new iLabel("欢迎注册BookSystem", 18);
            logoPanel.add(logo);
            logoPanel.add(welcome);
            w.add(logoPanel);

            int baseY = 80;
            int gap = 62;

            // 用户名标签
            JLabel userLabel = new JLabel("用户名");
            userLabel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
            userLabel.setBounds(20, baseY, 100, 20);
            w.add(userLabel);
            // 用户名输入框
            JTextField usernameField = new JTextField();
            usernameField.setBounds(20, baseY+24, 260, 36);
            usernameField.setFont(new Font("微软雅黑", Font.PLAIN, 16));
            w.add(usernameField);

            // 密码标签
            JLabel passLabel = new JLabel("密码");
            passLabel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
            passLabel.setBounds(20, baseY+gap, 100, 20);
            w.add(passLabel);
            // 密码输入框
            JPasswordField passwordField1 = new JPasswordField();
            passwordField1.setBounds(20, baseY+gap+24, 260, 36);
            passwordField1.setFont(new Font("微软雅黑", Font.PLAIN, 16));
            w.add(passwordField1);

            // 确认密码标签
            JLabel passLabel2 = new JLabel("确认密码");
            passLabel2.setFont(new Font("微软雅黑", Font.PLAIN, 15));
            passLabel2.setBounds(20, baseY+gap*2, 100, 20);
            w.add(passLabel2);
            // 确认密码输入框
            JPasswordField passwordField2 = new JPasswordField();
            passwordField2.setBounds(20, baseY+gap*2+24, 260, 36);
            passwordField2.setFont(new Font("微软雅黑", Font.PLAIN, 16));
            w.add(passwordField2);

            // 注册按钮
            iButton loginButton = new iButton("注册", iButton.ButtonType.PRIMARY);
            loginButton.setBounds(110, baseY+gap*3+32, 100, 38);
            w.add(loginButton);

            // 错误提示
            iLabel errorMessage = new iLabel("");
            errorMessage.setBounds(0, baseY+gap*3+80, 330, 30);
            errorMessage.setForeground(Color.RED);
            errorMessage.setFont(new Font("微软雅黑", Font.BOLD, 14));
            errorMessage.setCenter();
            w.add(errorMessage);

            // 注册逻辑
            ActionListener doRegister = e -> {
                final String user = usernameField.getText();
                final String password1 = new String(passwordField1.getPassword());
                final String password2 = new String(passwordField2.getPassword());

                // 统一红色边框和标签字体
                javax.swing.border.Border defaultBorder = BorderFactory.createLineBorder(new Color(200,200,200), 1);
                javax.swing.border.Border margin = BorderFactory.createEmptyBorder(4, 8, 4, 8);
                usernameField.setBorder(BorderFactory.createCompoundBorder(defaultBorder, margin));
                passwordField1.setBorder(BorderFactory.createCompoundBorder(defaultBorder, margin));
                passwordField2.setBorder(BorderFactory.createCompoundBorder(defaultBorder, margin));
                userLabel.setForeground(new Color(33,33,33));
                userLabel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
                passLabel.setForeground(new Color(33,33,33));
                passLabel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
                passLabel2.setForeground(new Color(33,33,33));
                passLabel2.setFont(new Font("微软雅黑", Font.PLAIN, 15));

                boolean error = false;
                if (!password1.equals(password2)) {
                    errorMessage.setText("两次密码不一致！");
                    error = true;
                } else if (user.equals("") || password1.equals("")) {
                    errorMessage.setText("用户名或密码不能为空！");
                    error = true;
                } else if (user.length() < 3 || user.length() > 20) {
                    errorMessage.setText("用户名长度应在3-20之间！");
                    error = true;
                } else if (!UserController.create(new UserItem(0, user, password1, null, "")).equals("success")) {
                    errorMessage.setText("用户已存在！");
                    error = true;
                }
                if (error) {
                    errorMessage.setForeground(new Color(220, 53, 69));
                    errorMessage.setFont(new Font("微软雅黑", Font.BOLD, 15));
                    javax.swing.border.Border redBorder = BorderFactory.createLineBorder(new Color(220,53,69), 2);
                    usernameField.setBorder(BorderFactory.createCompoundBorder(redBorder, margin));
                    passwordField1.setBorder(BorderFactory.createCompoundBorder(redBorder, margin));
                    passwordField2.setBorder(BorderFactory.createCompoundBorder(redBorder, margin));
                    userLabel.setForeground(new Color(220,53,69));
                    userLabel.setFont(new Font("微软雅黑", Font.BOLD, 15));
                    passLabel.setForeground(new Color(220,53,69));
                    passLabel.setFont(new Font("微软雅黑", Font.BOLD, 15));
                    passLabel2.setForeground(new Color(220,53,69));
                    passLabel2.setFont(new Font("微软雅黑", Font.BOLD, 15));
                    return;
                }

                //向数据库储存用户名和密码
                iDialog.dialogMessage(null, "完成！", "注册成功！去登录吧～");
                w.dispose();
            };
            loginButton.addListener(doRegister);
            usernameField.addActionListener(doRegister);
            passwordField1.addActionListener(doRegister);
            passwordField2.addActionListener(doRegister);

            // 增加上下方向键切换输入框焦点功能
            usernameField.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyPressed(java.awt.event.KeyEvent e) {
                    if (e.getKeyCode() == java.awt.event.KeyEvent.VK_DOWN) {
                        passwordField1.requestFocus();
                    }
                }
            });
            passwordField1.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyPressed(java.awt.event.KeyEvent e) {
                    if (e.getKeyCode() == java.awt.event.KeyEvent.VK_UP) {
                        usernameField.requestFocus();
                    } else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_DOWN) {
                        passwordField2.requestFocus();
                    }
                }
            });
            passwordField2.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyPressed(java.awt.event.KeyEvent e) {
                    if (e.getKeyCode() == java.awt.event.KeyEvent.VK_UP) {
                        passwordField1.requestFocus();
                    }
                }
            });

            w.done();
        }
    }
}
