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
     * 创建通用确认按钮
     * @param text 按钮文本
     * @param type 按钮类型
     * @param action 点击动作
     * @return 配置好的iButton
     */
    private static iButton createButton(String text, iButton.ButtonType type, Runnable action) {
        iButton button = new iButton(text, type);
        button.addActionListener(e -> action.run());
        return button;
    }

    /**
     * 创建通用确认对话框按钮组
     * @param confirmText 确认按钮文本
     * @param cancelText 取消按钮文本
     * @param onConfirm 确认动作
     * @param onCancel 取消动作
     * @return 按钮数组 [确认按钮, 取消按钮]
     */
    private static iButton[] createConfirmButtons(String confirmText, String cancelText, 
                                                 Runnable onConfirm, Runnable onCancel) {
        iButton confirmBtn = createButton(confirmText, iButton.ButtonType.PRIMARY, onConfirm);
        iButton cancelBtn = createButton(cancelText, iButton.ButtonType.NORMAL, onCancel);
        return new iButton[]{confirmBtn, cancelBtn};
    }

    /**
     * 自定义退出确认对话框
     * @param parent 父组件
     * @return 用户是否确认退出
     */
    public static boolean showExitDialog(Component parent) {
        JDialog dialog = new JDialog((JFrame) null, "退出", true);
        dialog.setSize(380, 200);
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

        // 使用通用按钮创建方法
        final boolean[] result = {false};
        iButton[] buttons = createConfirmButtons("确定", "取消", 
            () -> { result[0] = true; dialog.dispose(); }, 
            dialog::dispose);
        
        buttons[0].setBounds(80, 120, 90, 38);
        buttons[1].setBounds(200, 120, 90, 38);
        
        dialog.add(buttons[0]);
        dialog.add(buttons[1]);
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
        dialog.setSize(380, 200);
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

        // 使用通用按钮创建方法
        final boolean[] result = {false};
        iButton[] buttons = createConfirmButtons("注销", "返回", 
            () -> { result[0] = true; dialog.dispose(); }, 
            dialog::dispose);
        
        buttons[0].setBounds(80, 120, 90, 38);
        buttons[1].setBounds(200, 120, 90, 38);
        
        dialog.add(buttons[0]);
        dialog.add(buttons[1]);
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

    /**
     * 通用自定义确认对话框，按钮用iButton美化
     * @param parent 父组件
     * @param title 标题
     * @param message 内容
     * @return 用户是否点击确定
     */
    public static boolean showConfirmDialog(Component parent, String title, String message) {
        JDialog dialog = new JDialog((JFrame) null, title, true);
        dialog.setSize(520, 300);
        dialog.setLayout(null);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parent);

        JLabel iconLabel = new JLabel(u.getImageIcon("select", 60, 60));
        iconLabel.setBounds(30, 30, 60, 60);
        dialog.add(iconLabel);

        JLabel msgLabel = new JLabel("<html>" + message.replace("\n", "<br>") + "</html>");
        msgLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        msgLabel.setBounds(110, 40, 380, 160);
        dialog.add(msgLabel);

        // 使用通用按钮创建方法
        final boolean[] result = {false};
        iButton[] buttons = createConfirmButtons("确定", "取消", 
            () -> { result[0] = true; dialog.dispose(); }, 
            dialog::dispose);
        
        buttons[0].setBounds(150, 220, 90, 38);
        buttons[1].setBounds(280, 220, 90, 38);
        
        dialog.add(buttons[0]);
        dialog.add(buttons[1]);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
        return result[0];
    }

    /**
     * 通用自定义成功提示对话框，按钮用iButton美化
     * @param parent 父组件
     * @param title 标题
     * @param message 内容
     */
    public static void showSuccessDialog(Component parent, String title, String message) {
        JDialog dialog = new JDialog((JFrame) null, title, true);
        dialog.setSize(520, 300);
        dialog.setLayout(null);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parent);

        JLabel iconLabel = new JLabel(u.getImageIcon("success", 60, 60));
        iconLabel.setBounds(30, 30, 60, 60);
        dialog.add(iconLabel);

        JLabel msgLabel = new JLabel("<html>" + message.replace("\n", "<br>") + "</html>");
        msgLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        msgLabel.setBounds(110, 40, 380, 160);
        dialog.add(msgLabel);

        // 使用通用按钮创建方法
        iButton okBtn = createButton("确定", iButton.ButtonType.PRIMARY, dialog::dispose);
        okBtn.setBounds(210, 220, 100, 38);
        dialog.add(okBtn);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }

    /**
     * 删除确认对话框 - 专门为删除操作设计
     * @param parent 父组件
     * @param itemName 要删除的项目名称
     * @param itemId 要删除的项目ID
     * @return 用户是否确认删除
     */
    public static boolean showDeleteConfirmDialog(Component parent, String itemName, int itemId) {
        JDialog dialog = new JDialog((JFrame) null, "确认删除", true);
        dialog.setSize(450, 280);
        dialog.setLayout(null);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parent);

        // 设置对话框背景色
        dialog.getContentPane().setBackground(new Color(245, 250, 255));

        // 警告图标
        JLabel iconLabel = new JLabel(u.getImageIcon("warning", 70, 70));
        iconLabel.setBounds(30, 30, 70, 70);
        dialog.add(iconLabel);

        // 主标题
        JLabel titleLabel = new JLabel("确认删除");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        titleLabel.setForeground(new Color(244, 67, 54));
        titleLabel.setBounds(120, 30, 200, 35);
        dialog.add(titleLabel);

        // 详细消息
        String message = String.format("<html><div style='width: 320px;'>" +
                "您即将删除以下项目：<br><br>" +
                "<b>%s</b> (ID: %d)<br><br>" +
                "此操作不可撤销，删除后将无法恢复。<br>" +
                "请确认是否继续？</div></html>", 
                itemName, itemId);
        
        JLabel msgLabel = new JLabel(message);
        msgLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        msgLabel.setForeground(new Color(66, 66, 66));
        msgLabel.setBounds(120, 70, 320, 120);
        dialog.add(msgLabel);

        // 按钮区域
        final boolean[] result = {false};
        iButton[] buttons = createConfirmButtons("确认删除", "取消", 
            () -> { result[0] = true; dialog.dispose(); }, 
            dialog::dispose);
        
        // 设置按钮样式
        buttons[0].setFont(new Font("微软雅黑", Font.BOLD, 14));
        buttons[0].setPreferredSize(new Dimension(100, 40));
        buttons[1].setFont(new Font("微软雅黑", Font.BOLD, 14));
        buttons[1].setPreferredSize(new Dimension(100, 40));
        
        buttons[0].setBounds(120, 200, 100, 40);
        buttons[1].setBounds(240, 200, 100, 40);
        
        dialog.add(buttons[0]);
        dialog.add(buttons[1]);
        
        // 设置默认按钮
        dialog.getRootPane().setDefaultButton(buttons[1]);
        
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
        return result[0];
    }

    /**
     * 删除成功对话框 - 显示删除操作结果
     * @param parent 父组件
     * @param itemName 已删除的项目名称
     * @param itemId 已删除的项目ID
     */
    public static void showDeleteSuccessDialog(Component parent, String itemName, int itemId) {
        JDialog dialog = new JDialog((JFrame) null, "删除成功", true);
        dialog.setSize(420, 260);
        dialog.setLayout(null);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parent);

        // 设置对话框背景色
        dialog.getContentPane().setBackground(new Color(245, 250, 255));

        // 成功图标
        JLabel iconLabel = new JLabel(u.getImageIcon("success", 70, 70));
        iconLabel.setBounds(30, 30, 70, 70);
        dialog.add(iconLabel);

        // 主标题
        JLabel titleLabel = new JLabel("删除成功");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        titleLabel.setForeground(new Color(76, 175, 80));
        titleLabel.setBounds(120, 30, 200, 35);
        dialog.add(titleLabel);

        // 成功消息
        String message = String.format("<html><div style='width: 300px;'>" +
                "已成功删除以下项目：<br><br>" +
                "<b>%s</b> (ID: %d)<br><br>" +
                "该项目已从系统中永久移除。</div></html>", 
                itemName, itemId);
        
        JLabel msgLabel = new JLabel(message);
        msgLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        msgLabel.setForeground(new Color(66, 66, 66));
        msgLabel.setBounds(120, 70, 300, 100);
        dialog.add(msgLabel);

        // 确定按钮
        iButton okBtn = createButton("确定", iButton.ButtonType.PRIMARY, dialog::dispose);
        okBtn.setFont(new Font("微软雅黑", Font.BOLD, 14));
        okBtn.setPreferredSize(new Dimension(100, 40));
        okBtn.setBounds(160, 180, 100, 40);
        dialog.add(okBtn);
        
        // 设置默认按钮
        dialog.getRootPane().setDefaultButton(okBtn);
        
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }

    /**
     * 通用删除确认对话框 - 简化版本
     * @param parent 父组件
     * @param title 标题
     * @param message 消息内容
     * @return 用户是否确认删除
     */
    public static boolean showDeleteConfirmDialog(Component parent, String title, String message) {
        JDialog dialog = new JDialog((JFrame) null, title, true);
        dialog.setSize(450, 250);
        dialog.setLayout(null);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parent);

        // 设置对话框背景色
        dialog.getContentPane().setBackground(new Color(245, 250, 255));

        // 警告图标
        JLabel iconLabel = new JLabel(u.getImageIcon("warning", 60, 60));
        iconLabel.setBounds(30, 30, 60, 60);
        dialog.add(iconLabel);

        // 主标题
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        titleLabel.setForeground(new Color(244, 67, 54));
        titleLabel.setBounds(110, 30, 300, 30);
        dialog.add(titleLabel);

        // 消息内容
        JLabel msgLabel = new JLabel("<html><div style='width: 350px;'>" + message + "</div></html>");
        msgLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        msgLabel.setForeground(new Color(66, 66, 66));
        msgLabel.setBounds(110, 70, 350, 80);
        dialog.add(msgLabel);

        // 按钮区域
        final boolean[] result = {false};
        iButton[] buttons = createConfirmButtons("确认删除", "取消", 
            () -> { result[0] = true; dialog.dispose(); }, 
            dialog::dispose);
        
        buttons[0].setFont(new Font("微软雅黑", Font.BOLD, 14));
        buttons[0].setPreferredSize(new Dimension(100, 40));
        buttons[1].setFont(new Font("微软雅黑", Font.BOLD, 14));
        buttons[1].setPreferredSize(new Dimension(100, 40));
        
        buttons[0].setBounds(120, 160, 100, 40);
        buttons[1].setBounds(240, 160, 100, 40);
        
        dialog.add(buttons[0]);
        dialog.add(buttons[1]);
        
        // 设置默认按钮
        dialog.getRootPane().setDefaultButton(buttons[1]);
        
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
        return result[0];
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
