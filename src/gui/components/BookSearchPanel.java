package gui.components;

import sql.BorrowController;
import entity.Book;
import utils.u;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * 图书搜索组件，支持关键词模糊搜索
 */
public class BookSearchPanel extends JPanel {
    private JTextField searchField;
    private iButton searchButton;
    private BookViewer bookViewer;
    private ActionListener onBookSelected;
    private boolean showOperation;
    private iLabel resultLabel;
    private int userId = 0; // 用户ID，用于检查借阅状态

    /**
     * @param onBookSelected 当选择书籍时的回调
     * @param showOperation 是否显示操作按钮
     */
    public BookSearchPanel(ActionListener onBookSelected, boolean showOperation) {
        this(onBookSelected, showOperation, 0);
    }

    /**
     * @param onBookSelected 当选择书籍时的回调
     * @param showOperation 是否显示操作按钮
     * @param userId 用户ID，用于检查借阅状态
     */
    public BookSearchPanel(ActionListener onBookSelected, boolean showOperation, int userId) {
        this.onBookSelected = onBookSelected;
        this.showOperation = showOperation;
        this.userId = userId;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 250, 255));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        initSearchPanel();
        initBookViewer();
    }

    /**
     * 初始化搜索面板
     */
    private void initSearchPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setOpaque(false);
        
        // 搜索图标和标题
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.setOpaque(false);
        JLabel searchIcon = new JLabel(u.getImageIcon("search", 24, 24));
        iLabel titleLabel = new iLabel("图书搜索", 20);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        titleLabel.setForeground(new Color(33, 150, 243));
        titlePanel.add(searchIcon);
        titlePanel.add(titleLabel);
        
        // 搜索输入框
        searchField = new JTextField();
        searchField.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        searchField.setPreferredSize(new Dimension(300, 40));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        searchField.setBackground(Color.WHITE);
        
        // 搜索按钮
        searchButton = new iButton("搜索", iButton.ButtonType.PRIMARY);
        searchButton.setPreferredSize(new Dimension(80, 40));
        searchButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        
        // 搜索区域
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setOpaque(false);
        inputPanel.add(searchField, BorderLayout.CENTER);
        inputPanel.add(searchButton, BorderLayout.EAST);
        
        // 提示文本
        iLabel hintLabel = new iLabel("请输入书名、作者或ISBN进行搜索");
        hintLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        hintLabel.setForeground(new Color(120, 144, 156));
        hintLabel.setCenter();
        
        // 结果提示标签
        resultLabel = new iLabel("");
        resultLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        resultLabel.setCenter();
        resultLabel.setVisible(false);
        
        JPanel topPanel = new JPanel(new BorderLayout(0, 8));
        topPanel.setOpaque(false);
        topPanel.add(titlePanel, BorderLayout.NORTH);
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(hintLabel, BorderLayout.SOUTH);
        topPanel.add(resultLabel, BorderLayout.SOUTH);
        
        searchPanel.add(topPanel, BorderLayout.CENTER);
        
        // 添加事件监听
        searchButton.addActionListener(e -> performSearch());
        searchField.addActionListener(e -> performSearch());
        
        // 支持实时搜索（输入时自动搜索）
        searchField.addKeyListener(new KeyAdapter() {
            private javax.swing.Timer timer;
            
            @Override
            public void keyReleased(KeyEvent e) {
                if (timer != null) {
                    timer.stop();
                }
                timer = new javax.swing.Timer(500, evt -> performSearch());
                timer.setRepeats(false);
                timer.start();
            }
        });
        
        add(searchPanel, BorderLayout.NORTH);
    }

    /**
     * 初始化书籍显示区域
     */
    private void initBookViewer() {
        bookViewer = new BookViewer(onBookSelected, showOperation, userId);
        add(bookViewer, BorderLayout.CENTER);
        
        // 初始显示所有书籍
        refreshBookList();
    }

    /**
     * 执行搜索
     */
    private void performSearch() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            refreshBookList();
            resultLabel.setVisible(false); // 隐藏结果提示
        } else {
            Book[] books = BorrowController.searchBooks(keyword);
            bookViewer.updateItem(books);
            
            // 显示搜索结果统计
            if (books.length == 0) {
                showSearchResult("未找到相关书籍", false);
            } else {
                showSearchResult("找到 " + books.length + " 本相关书籍", true);
            }
        }
    }

    /**
     * 刷新书籍列表（显示所有书籍）
     */
    public void refreshBookList() {
        Book[] books = BorrowController.getAll();
        bookViewer.updateItem(books);
        resultLabel.setVisible(false); // 确保隐藏结果提示
    }

    /**
     * 刷新当前搜索结果（保持搜索状态）
     */
    public void refreshSearchResults() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            refreshBookList();
        } else {
            // 重新执行当前搜索
            Book[] books = BorrowController.searchBooks(keyword);
            bookViewer.updateItem(books);
            
            // 更新搜索结果统计
            if (books.length == 0) {
                showSearchResult("未找到相关书籍", false);
            } else {
                showSearchResult("找到 " + books.length + " 本相关书籍", true);
            }
        }
    }

    /**
     * 显示搜索结果提示
     */
    private void showSearchResult(String message, boolean isSuccess) {
        resultLabel.setText(message);
        resultLabel.setVisible(true);
        if (isSuccess) {
            resultLabel.setForeground(new Color(33, 150, 243));
        } else {
            resultLabel.setForeground(Color.RED);
        }
    }

    /**
     * 获取搜索框文本
     */
    public String getSearchKeyword() {
        return searchField.getText().trim();
    }

    /**
     * 清空搜索框
     */
    public void clearSearch() {
        searchField.setText("");
        resultLabel.setVisible(false);
        refreshBookList();
    }

    /**
     * 设置搜索框文本
     */
    public void setSearchKeyword(String keyword) {
        searchField.setText(keyword);
        performSearch();
    }
} 