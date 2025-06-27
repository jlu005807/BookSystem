package gui.views;

import gui.components.BookViewer;
import gui.components.iLabel;
import sql.ConnectionPool;
import sql.MySQLConfig;
import sql.BorrowController;
import entity.Book;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class UserView extends JPanel {
    BookViewer bookViewer;
    int selectedBookId;
    int userId = 1; // TODO: 实际项目中应由登录用户ID赋值

    public UserView() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 250, 255));

        // 顶部标题区美化
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        JPanel leftTitle = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 12));
        leftTitle.setOpaque(false);
        JLabel icon = new JLabel(utils.u.getImageIcon("user", 48, 48));
        iLabel titleLabel = new iLabel("欢迎来到图书馆", 28);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 28));
        titleLabel.setForeground(new Color(33, 150, 243));
        leftTitle.add(icon);
        leftTitle.add(titleLabel);
        titlePanel.add(leftTitle, BorderLayout.WEST);
        iLabel idSelectedLabel = new iLabel("请选择要借阅的图书");
        idSelectedLabel.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        idSelectedLabel.setForeground(new Color(120, 144, 156));
        idSelectedLabel.setCenter();
        titlePanel.add(idSelectedLabel, BorderLayout.SOUTH);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 0, 30));
        add(titlePanel, BorderLayout.NORTH);

        // 图书浏览区加圆角白色面板和阴影
        JPanel bookPanel = new JPanel(new BorderLayout());
        bookPanel.setBackground(new Color(0,0,0,0));
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(18, 18, 18, 18),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 230, 240), 1, true),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                )));
        bookViewer = new gui.components.BookViewer(e -> {
            int selectedBookId = Integer.parseInt(e.getActionCommand());
            System.out.println("UserView 选中图书ID: " + selectedBookId);
            idSelectedLabel.setText("您选择了ID为" + selectedBookId + "的图书");
            idSelectedLabel.setForeground(new Color(33, 150, 243));
            // 借阅操作
            new BorrowView(selectedBookId, userId, a -> updateView());
        }, false);
        updateView();
        cardPanel.add(bookViewer, BorderLayout.CENTER);
        bookPanel.add(cardPanel, BorderLayout.CENTER);
        bookPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        add(bookPanel, BorderLayout.CENTER);

        // 底部按钮区：我的借阅、注销（全部用iButton美化+复用）
        gui.components.iButton myBorrowBtn = createButton("我的借阅", gui.components.iButton.ButtonType.PRIMARY, e -> {
            new MyBorrowView((Frame) SwingUtilities.getWindowAncestor(this), userId, this::updateView);
        });
        gui.components.iButton logoutBtn = createButton("注销", gui.components.iButton.ButtonType.DANGER, e -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            gui.actions.logoutSys(topFrame);
        });
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 18));
        btnPanel.setOpaque(false);
        btnPanel.add(myBorrowBtn);
        btnPanel.add(logoutBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void updateView() {
        Book[] books = BorrowController.getAll();
        bookViewer.updateItem(books);
    }

    /**
     * 创建美观的iButton，简化按钮复用
     */
    private gui.components.iButton createButton(String text, gui.components.iButton.ButtonType type, java.awt.event.ActionListener listener) {
        gui.components.iButton btn = new gui.components.iButton(text, type);
        btn.setFont(new Font("微软雅黑", Font.BOLD, 16));
        if (listener != null) btn.addActionListener(listener);
        return btn;
    }
}
