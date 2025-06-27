package gui.views;

import gui.components.BookViewer;
import gui.components.iButton;
import gui.components.iDialog;
import gui.components.iLabel;
import sql.ConnectionPool;
import sql.MySQLConfig;
import sql.BorrowController;
import entity.Book;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static gui.actions.logoutSys;

public class RootView extends JPanel implements ActionListener {
    BookViewer bookViewer;
    iButton addButton = new iButton("添加图书", this);
    iButton deleteButton = new iButton("删除图书", this);
    iButton exitButton = new iButton("注销", this);
    int selectedBookId = 0;

    public RootView() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 250, 255));

        // 顶部标题区美化
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        JPanel leftTitle = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 12));
        leftTitle.setOpaque(false);
        JLabel icon = new JLabel(utils.u.getImageIcon("flag", 48, 48));
        iLabel titleLabel = new iLabel("管理员界面", 28);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 28));
        titleLabel.setForeground(new Color(33, 150, 243));
        leftTitle.add(icon);
        leftTitle.add(titleLabel);
        titlePanel.add(leftTitle, BorderLayout.WEST);
        iLabel idSelectedLabel = new iLabel("请选择要操作的图书");
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
            selectedBookId = e.getID();
            idSelectedLabel.setText("您选择了ID为" + selectedBookId + "的图书");
            idSelectedLabel.setForeground(new Color(33, 150, 243));
        }, true);
        updateView();
        cardPanel.add(bookViewer, BorderLayout.CENTER);
        bookPanel.add(cardPanel, BorderLayout.CENTER);
        bookPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        add(bookPanel, BorderLayout.CENTER);

        // 底部操作按钮美化
        addButton.setFont(new Font("微软雅黑", Font.BOLD, 16));
        addButton.setBackground(new Color(33, 150, 243));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.setBorder(BorderFactory.createLineBorder(new Color(33, 150, 243), 1, true));
        addButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                addButton.setBackground(new Color(25, 118, 210));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                addButton.setBackground(new Color(33, 150, 243));
            }
        });
        deleteButton.setFont(new Font("微软雅黑", Font.BOLD, 16));
        deleteButton.setBackground(new Color(244, 67, 54));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteButton.setBorder(BorderFactory.createLineBorder(new Color(244, 67, 54), 1, true));
        deleteButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                deleteButton.setBackground(new Color(211, 47, 47));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                deleteButton.setBackground(new Color(244, 67, 54));
            }
        });
        exitButton.setFont(new Font("微软雅黑", Font.BOLD, 16));
        exitButton.setBackground(new Color(120, 144, 156));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exitButton.setBorder(BorderFactory.createLineBorder(new Color(120, 144, 156), 1, true));
        exitButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                exitButton.setBackground(new Color(84, 110, 122));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                exitButton.setBackground(new Color(120, 144, 156));
            }
        });
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 18));
        buttonPanel.setOpaque(false);
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(exitButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (selectedBookId == 0 && e.getSource() == deleteButton) {
            iDialog.dialogError(this, "错误", "请先选择要删除的图书");
            return;
        }
        if (e.getSource() == addButton) {
            BookChangeView.showDialog(a -> updateView());
        }
        if (e.getSource() == deleteButton) {
            boolean choice = iDialog.dialogConfirm(this, "确认删除", "确认删除ID为" + selectedBookId + "的图书吗？");
            if (choice) {
                BorrowController.delete(selectedBookId);
                updateView();
            }
        }
        if (e.getSource() == exitButton) {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            logoutSys(topFrame);
        }
    }

    private void updateView() {
        Book[] books = BorrowController.getAll();
        bookViewer.updateItem(books);
    }
}
