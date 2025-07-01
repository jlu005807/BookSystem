package gui.views;

import gui.components.BookSearchPanel;
import gui.components.iButton;
import gui.components.iDialog;
import gui.components.iLabel;
import gui.components.iWindow;
import sql.BorrowController;
import sql.Book;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static gui.actions.logoutSys;

/**
 * 集成搜索功能的用户界面示例
 */
public class UserViewWithSearch extends JPanel implements ActionListener {
    private BookSearchPanel searchPanel;
    private iButton logoutButton;
    private int selectedBookId = 0;
    private int userId;

    public UserViewWithSearch(int userId) {
        this.userId = userId;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 250, 255));

        // 顶部标题区
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        JPanel leftTitle = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 12));
        leftTitle.setOpaque(false);
        JLabel icon = new JLabel(utils.u.getImageIcon("home", 48, 48));
        iLabel titleLabel = new iLabel("图书搜索与借阅", 28);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 28));
        titleLabel.setForeground(new Color(33, 150, 243));
        leftTitle.add(icon);
        leftTitle.add(titleLabel);
        titlePanel.add(leftTitle, BorderLayout.WEST);
        
        // 注销按钮
        logoutButton = new iButton("注销", iButton.ButtonType.DANGER);
        logoutButton.setPreferredSize(new Dimension(80, 40));
        logoutButton.addActionListener(this);
        titlePanel.add(logoutButton, BorderLayout.EAST);
        
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        add(titlePanel, BorderLayout.NORTH);

        // 搜索面板
        searchPanel = new BookSearchPanel(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedBookId = Integer.parseInt(e.getActionCommand());
                showBorrowDialog();
            }
        }, false);
        
        add(searchPanel, BorderLayout.CENTER);

        // 底部操作区
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setOpaque(false);
        
        iButton refreshBtn = new iButton("刷新列表", iButton.ButtonType.NORMAL);
        iButton clearBtn = new iButton("清空搜索", iButton.ButtonType.WARNING);
        
        refreshBtn.addActionListener(e -> searchPanel.refreshBookList());
        clearBtn.addActionListener(e -> searchPanel.clearSearch());
        
        bottomPanel.add(refreshBtn);
        bottomPanel.add(clearBtn);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void showBorrowDialog() {
        if (selectedBookId == 0) {
            iDialog.dialogError(this, "错误", "请先选择要借阅的图书");
            return;
        }
        
        Book[] books = BorrowController.getAll();
        Book selectedBook = null;
        for (Book book : books) {
            if (book.getId() == selectedBookId) {
                selectedBook = book;
                break;
            }
        }
        
        if (selectedBook == null) {
            iDialog.dialogError(this, "错误", "未找到该图书");
            return;
        }
        
        if (selectedBook.getNumLeft() == 0) {
            iDialog.dialogMessage(this, "借阅失败", "该图书已全部借出，可稍后再试。");
            return;
        }
        
        boolean choice = iDialog.showConfirmDialog(this, "借阅确认", 
            "确认借阅《" + selectedBook.getTitle() + "》？\n\n" +
            "作者：" + selectedBook.getAuthor() + "\n" +
            "剩余可借：" + selectedBook.getNumLeft() + " 本");
            
        if (choice) {
            // 这里需要传入实际的用户ID，暂时使用1作为示例
            boolean success = BorrowController.borrowBook(selectedBookId, this.userId, new java.sql.Date(System.currentTimeMillis()));
            if (success) {
                iDialog.showSuccessDialog(this, "借阅成功", "您已成功借阅《" + selectedBook.getTitle() + "》！");
                searchPanel.refreshBookList(); // 刷新列表
            } else {
                iDialog.dialogError(this, "借阅失败", "借阅操作失败，请重试");
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == logoutButton) {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            logoutSys(topFrame);
        }
    }
} 