package gui.views;

import gui.components.BookViewer;
import gui.components.BookSearchPanel;
import gui.components.iLabel;
import gui.components.iButton;
import sql.ConnectionPool;
import sql.MySQLConfig;
import sql.BorrowController;
import sql.Book;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class UserView extends JPanel {
    BookViewer bookViewer;
    BookSearchPanel searchPanel;
    int selectedBookId;
    int userId;

    public UserView(int userId) {
        this.userId = userId;
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

        // 搜索区域
        JPanel searchContainer = new JPanel(new BorderLayout());
        searchContainer.setOpaque(false);
        searchContainer.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        
        // 创建搜索面板
        searchPanel = new BookSearchPanel(e -> {
            int selectedBookId = Integer.parseInt(e.getActionCommand());
            // 查找书名
            String bookTitle = "未知图书";
            Book[] allBooks = BorrowController.getAll();
            for (Book book : allBooks) {
                if (book.getId() == selectedBookId) {
                    bookTitle = book.getTitle();
                    break;
                }
            }
            idSelectedLabel.setText("您选择了《" + bookTitle + "》");
            idSelectedLabel.setForeground(new Color(33, 150, 243));
            // 借阅操作
            new BorrowView(selectedBookId, this.userId, a -> updateView());
        }, false, userId);
        
        // 搜索控制按钮
        JPanel searchControlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        searchControlPanel.setOpaque(false);
        
        iButton refreshBtn = iButton.create("刷新", iButton.ButtonType.NORMAL, e -> {
            searchPanel.refreshSearchResults();
            idSelectedLabel.setText("请选择要借阅的图书");
            idSelectedLabel.setForeground(new Color(120, 144, 156));
        });
        
        iButton clearBtn = iButton.create("清空搜索", iButton.ButtonType.WARNING, e -> {
            searchPanel.clearSearch();
            idSelectedLabel.setText("请选择要借阅的图书");
            idSelectedLabel.setForeground(new Color(120, 144, 156));
        });
        
        searchControlPanel.add(refreshBtn);
        searchControlPanel.add(clearBtn);
        
        searchContainer.add(searchPanel, BorderLayout.CENTER);
        searchContainer.add(searchControlPanel, BorderLayout.SOUTH);
        
        add(searchContainer, BorderLayout.CENTER);

        // 底部按钮区：我的借阅、注销（全部用iButton美化+复用）
        iButton myBorrowBtn = iButton.create("我的借阅", iButton.ButtonType.PRIMARY, e -> {
            new MyBorrowView((Frame) SwingUtilities.getWindowAncestor(this), this.userId, () -> updateView());
        });
        iButton logoutBtn = iButton.create("注销", iButton.ButtonType.DANGER, e -> {
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
        // 刷新搜索面板的书籍列表，保持搜索状态
        searchPanel.refreshSearchResults();
    }
}
