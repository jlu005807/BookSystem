package test;

import gui.components.iWindow;
import gui.components.iDialog;
import gui.components.BookSearchPanel;
import gui.components.iButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 搜索组件演示界面
 */
public class SearchDemo {
    public static void main(String[] args) {
        // 初始化数据库连接
        sql.ConnectionPool.init(new sql.MySQLConfig("rental"));
        
        iWindow window = new iWindow("图书搜索演示", 800, 600, true);
        window.setLayout(new BorderLayout());
        
        // 创建搜索面板
        BookSearchPanel searchPanel = new BookSearchPanel(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int bookId = Integer.parseInt(e.getActionCommand());
                iDialog.showSuccessDialog(window, "选择成功", "您选择了图书ID: " + bookId);
            }
        }, false);
        
        // 添加一些控制按钮
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        controlPanel.setOpaque(false);
        
        iButton clearBtn = new iButton("清空搜索", iButton.ButtonType.NORMAL);
        iButton testBtn = new iButton("测试搜索", iButton.ButtonType.WARNING);
        
        clearBtn.addActionListener(e -> searchPanel.clearSearch());
        testBtn.addActionListener(e -> searchPanel.setSearchKeyword("Java"));
        
        controlPanel.add(clearBtn);
        controlPanel.add(testBtn);
        
        window.add(searchPanel, BorderLayout.CENTER);
        window.add(controlPanel, BorderLayout.SOUTH);
        
        window.done();
    }
} 