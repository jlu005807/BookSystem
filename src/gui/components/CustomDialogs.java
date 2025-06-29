package gui.components;


import utils.u;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class CustomDialogs {
    /**
     * 显示登录类型选择弹窗
     * @param parent 父窗口
     * @param onSelect 选择后回调("user"/"root")
     */
    public static void showLoginTypeDialog(JFrame parent, Consumer<String> onSelect) {
        JDialog dialog = new JDialog(parent, "请选择登录类型", true);
        dialog.setSize(380, 260);
        dialog.setLayout(null);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parent);

        // 图标
        JLabel iconLabel = new JLabel(u.getImageIcon("user", 60, 60));
        iconLabel.setBounds(30, 30, 60, 60);
        dialog.add(iconLabel);

        // 标题
        JLabel titleLabel = new JLabel("请选择登录类型");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        titleLabel.setBounds(110, 40, 200, 30);
        dialog.add(titleLabel);

        // 用户登录按钮
        iButton userBtn = new iButton("用户", iButton.ButtonType.PRIMARY);
        userBtn.setBounds(60, 110, 110, 44);
        dialog.add(userBtn);

        // 管理员登录按钮
        iButton adminBtn = new iButton("管理员", iButton.ButtonType.NORMAL);
        adminBtn.setBounds(200, 110, 110, 44);
        dialog.add(adminBtn);

        // 取消按钮
        iButton cancelBtn = new iButton("取消", iButton.ButtonType.DANGER);
        cancelBtn.setBounds(130, 170, 110, 38);
        dialog.add(cancelBtn);

        userBtn.addActionListener(e -> {
            dialog.dispose();
            onSelect.accept("user");
        });
        adminBtn.addActionListener(e -> {
            dialog.dispose();
            onSelect.accept("root");
        });
        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }
} 