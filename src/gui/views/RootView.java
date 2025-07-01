package gui.views;

import gui.components.BookViewer;
import gui.components.BookSearchPanel;
import gui.components.iButton;
import gui.components.iDialog;
import gui.components.iLabel;
import sql.ConnectionPool;
import sql.MySQLConfig;
import sql.BorrowController;
import sql.Book;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static gui.actions.logoutSys;

public class RootView extends JPanel implements ActionListener {
    BookViewer bookViewer;
    BookSearchPanel searchPanel;
    iButton addButton;
    iButton deleteButton;
    iButton exitButton;
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

        // 搜索和图书浏览区域
        JPanel searchContainer = new JPanel(new BorderLayout());
        searchContainer.setOpaque(false);
        searchContainer.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        
        // 创建搜索面板（管理员模式，显示操作按钮）
        searchPanel = new BookSearchPanel(e -> {
            selectedBookId = Integer.parseInt(e.getActionCommand());
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
        }, true, 0); // 管理员模式，userId为0
        
        // 搜索控制按钮
        JPanel searchControlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        searchControlPanel.setOpaque(false);
        
        iButton refreshBtn = iButton.create("刷新", iButton.ButtonType.NORMAL, e -> {
            searchPanel.refreshSearchResults();
            idSelectedLabel.setText("请选择要操作的图书");
            idSelectedLabel.setForeground(new Color(120, 144, 156));
        });
        
        iButton clearBtn = iButton.create("清空搜索", iButton.ButtonType.WARNING, e -> {
            searchPanel.clearSearch();
            idSelectedLabel.setText("请选择要操作的图书");
            idSelectedLabel.setForeground(new Color(120, 144, 156));
        });
        
        searchControlPanel.add(refreshBtn);
        searchControlPanel.add(clearBtn);
        
        searchContainer.add(searchPanel, BorderLayout.CENTER);
        searchContainer.add(searchControlPanel, BorderLayout.SOUTH);
        
        add(searchContainer, BorderLayout.CENTER);

        // 底部操作按钮区
        addButton = iButton.create("添加图书", iButton.ButtonType.PRIMARY, e -> {
            BookChangeView.showDialog(a -> updateView());
        });
        
        deleteButton = iButton.create("删除图书", iButton.ButtonType.DANGER, e -> {
            if (selectedBookId == 0) {
                iDialog.dialogError(this, "错误", "请先选择要删除的图书");
                return;
            }
            
            // 查找书名
            String bookTitle = "未知图书";
            Book[] allBooks = BorrowController.getAll();
            for (Book book : allBooks) {
                if (book.getId() == selectedBookId) {
                    bookTitle = book.getTitle();
                    break;
                }
            }
            
            boolean confirm = iDialog.showDeleteConfirmDialog(this, "确认删除", 
                "您即将删除《" + bookTitle + "》\n\n此操作不可撤销，删除后将无法恢复。\n请确认是否继续？");
            
            if (confirm) {
                try {
                    // 先删除所有相关借阅记录
                    BorrowController.deleteAllRecordsByBookId(selectedBookId);
                    // 执行删除操作
                    BorrowController.delete(selectedBookId);
                    
                    iDialog.showDeleteSuccessDialog(this, bookTitle, selectedBookId);
                    updateView();
                    selectedBookId = 0;
                    idSelectedLabel.setText("请选择要操作的图书");
                    idSelectedLabel.setForeground(new Color(120, 144, 156));
                } catch (Exception ex) {
                    iDialog.dialogError(this, "删除失败", "删除操作失败，请重试");
                }
            }
        });
        
        exitButton = iButton.create("注销", iButton.ButtonType.NORMAL, e -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            logoutSys(topFrame);
        });
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 18));
        btnPanel.setOpaque(false);
        btnPanel.add(addButton);
        btnPanel.add(deleteButton);
        btnPanel.add(exitButton);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void updateView() {
        // 刷新搜索面板的书籍列表，保持搜索状态
        searchPanel.refreshSearchResults();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 保留原有的actionPerformed方法以兼容可能的其他事件处理
    }
}
