package gui.components;

import utils.u;

import javax.swing.*;
import java.awt.*;

public class iDialog {
    public static void dialogMessage(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(
                parent, message, title,
                JOptionPane.PLAIN_MESSAGE,
                u.getImageIcon("comment", 50, 50)
        );
    }

    public static void dialogError(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(
                parent, message, title,
                JOptionPane.ERROR_MESSAGE,
                u.getImageIcon("stop", 50, 50)
        );
    }

    public static void dialogWarning(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(
                parent, message, title,
                JOptionPane.WARNING_MESSAGE,
                u.getImageIcon("warning", 50, 50)
        );
    }

    public static boolean dialogConfirm(Component parent, String title, String message) {
        return JOptionPane.showConfirmDialog(
                parent, message, title,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                u.getImageIcon("select", 50, 50)
        ) == JOptionPane.YES_OPTION;
    }

    public static boolean dialogConfirm(Component parent, String title, String message, String imagePath) {
        return JOptionPane.showConfirmDialog(
                parent, message, title,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                u.getImageIcon(imagePath, 50, 50)
        ) == JOptionPane.YES_OPTION;
    }

    public static String dialogInput(Component parent, String title, String message) {
        return JOptionPane.showInputDialog(
                parent, message, title,
                JOptionPane.PLAIN_MESSAGE
        );
    }

    public static int dialogChoice(Component parent, String title, String message, String[] options) {
        return JOptionPane.showOptionDialog(
                parent, message, title,
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                u.getImageIcon("adjust", 50, 50),
                options,
                options[0]
        );
    }

    /**
     * 自定义退出确认对话框
     * @param parent 父组件
     * @return 用户是否确认退出
     */
    public static boolean showExitDialog(Component parent) {
        JDialog dialog = new JDialog((JFrame) null, "退出", true);
        dialog.setSize(350, 180);
        dialog.setLayout(null);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parent);

        JLabel iconLabel = new JLabel(u.getImageIcon("close", 60, 60));
        iconLabel.setBounds(30, 30, 60, 60);
        dialog.add(iconLabel);

        JLabel msgLabel = new JLabel("确定要退出系统吗？");
        msgLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        msgLabel.setBounds(110, 40, 200, 30);
        dialog.add(msgLabel);

        JButton yesBtn = new JButton("确定");
        yesBtn.setBounds(80, 110, 80, 30);
        yesBtn.setBackground(new Color(0, 123, 255));
        yesBtn.setForeground(Color.WHITE);
        yesBtn.setFocusPainted(false);
        JButton noBtn = new JButton("取消");
        noBtn.setBounds(190, 110, 80, 30);
        noBtn.setBackground(new Color(220, 53, 69));
        noBtn.setForeground(Color.WHITE);
        noBtn.setFocusPainted(false);
        final boolean[] result = {false};
        yesBtn.addActionListener(e -> {
            result[0] = true;
            dialog.dispose();
        });
        noBtn.addActionListener(e -> {
            dialog.dispose();
        });
        dialog.add(yesBtn);
        dialog.add(noBtn);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
        return result[0];
    }

    /**
     * 自定义注销确认对话框
     * @param parent 父组件
     * @return 用户是否确认注销
     */
    public static boolean showLogoutDialog(Component parent) {
        JDialog dialog = new JDialog((JFrame) null, "注销", true);
        dialog.setSize(350, 180);
        dialog.setLayout(null);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parent);

        JLabel iconLabel = new JLabel(u.getImageIcon("user", 60, 60));
        iconLabel.setBounds(30, 30, 60, 60);
        dialog.add(iconLabel);

        JLabel msgLabel = new JLabel("确定要注销当前用户吗？");
        msgLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        msgLabel.setBounds(110, 40, 220, 30);
        dialog.add(msgLabel);

        JButton yesBtn = new JButton("注销");
        yesBtn.setBounds(80, 110, 80, 30);
        yesBtn.setBackground(new Color(0, 123, 255));
        yesBtn.setForeground(Color.WHITE);
        yesBtn.setFocusPainted(false);
        JButton noBtn = new JButton("返回");
        noBtn.setBounds(190, 110, 80, 30);
        noBtn.setBackground(new Color(220, 53, 69));
        noBtn.setForeground(Color.WHITE);
        noBtn.setFocusPainted(false);
        final boolean[] result = {false};
        yesBtn.addActionListener(e -> {
            result[0] = true;
            dialog.dispose();
        });
        noBtn.addActionListener(e -> {
            dialog.dispose();
        });
        dialog.add(yesBtn);
        dialog.add(noBtn);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
        return result[0];
    }

    /**
     * 自定义登录成功对话框，自动关闭
     * @param parent 父组件
     * @param username 用户名
     */
    public static void showLoginSuccessDialog(Component parent, String username) {
        JDialog dialog = new JDialog((JFrame) null, "登录成功", true);
        dialog.setSize(340, 180);
        dialog.setLayout(null);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parent);

        JLabel iconLabel = new JLabel(u.getImageIcon("success", 60, 60));
        iconLabel.setBounds(30, 30, 60, 60);
        dialog.add(iconLabel);

        JLabel msgLabel = new JLabel("欢迎回来，" + username + "！");
        msgLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        msgLabel.setBounds(110, 40, 200, 30);
        dialog.add(msgLabel);

        JLabel tipLabel = new JLabel("登录成功，正在进入系统...");
        tipLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        tipLabel.setBounds(110, 80, 200, 20);
        dialog.add(tipLabel);

        // 1.2秒后自动关闭
        new javax.swing.Timer(800, e -> dialog.dispose()).start();
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }

    // Test
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        dialogMessage(frame, "Message", "This is a message.");
        dialogError(frame, "Error", "This is an error.");
        dialogWarning(frame, "Warning", "This is a warning.");
        System.out.println(dialogConfirm(frame, "Confirm", "Do you want to continue?"));
        System.out.println(dialogInput(frame, "Input", "Please input your name:"));
        System.out.println(dialogChoice(frame, "Choice", "Please choose one:", new String[]{"A", "B", "C", "D"}));
    }

}
