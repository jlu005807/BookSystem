package gui.views;

import gui.components.iDialog;
import sql.BorrowController;
import sql.BorrowRecord;
import sql.Book;
import sql.UserController;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Comparator;
import java.util.ArrayList;

public class BookBorrowRecordView extends JDialog {
    public BookBorrowRecordView(int bookId) {
        super((Frame) null, "借阅记录", true);
        setLayout(new BorderLayout(10, 10));
        List<BorrowRecord> records = BorrowController.getRecordsByBookId(bookId);
        // 排序：未归还在前，已归还在后，组内按借阅时间倒序
        List<BorrowRecord> notReturned = new ArrayList<>();
        List<BorrowRecord> returned = new ArrayList<>();
        for (BorrowRecord r : records) {
            if (r.getReturnTime() == null) {
                notReturned.add(r);
            } else {
                returned.add(r);
            }
        }
        Comparator<BorrowRecord> byTimeDesc = Comparator.comparing(BorrowRecord::getBorrowTime, Comparator.nullsLast(Comparator.reverseOrder()));
        notReturned.sort(byTimeDesc);
        returned.sort(byTimeDesc);
        List<BorrowRecord> sortedRecords = new ArrayList<>();
        sortedRecords.addAll(notReturned);
        sortedRecords.addAll(returned);
        records = sortedRecords;

        String[] columnNames = {"用户ID", "用户名", "借阅时间", "归还时间", "状态"};
        Object[][] data = new Object[records.size()][5];
        for (int i = 0; i < records.size(); i++) {
            BorrowRecord record = records.get(i);
            int userId = record.getUserId();
            String username = "未知";
            sql.UserItem user = UserController.search(userId);
            if (user != null) username = user.username;
            data[i][0] = userId;
            data[i][1] = username;
            data[i][2] = record.getBorrowTime() == null ? "" : record.getBorrowTime().toString();
            data[i][3] = record.getReturnTime() == null ? "未归还" : record.getReturnTime().toString();
            data[i][4] = record.getReturnTime() == null ? "未归还" : "已归还";
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setRowHeight(32);
        table.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        table.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 16));
        ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        setSize(600, 400);
        setLocationRelativeTo(null);
        add(scrollPane, BorderLayout.CENTER);

        // 关闭按钮
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton closeBtn = new JButton("关闭");
        closeBtn.setBackground(new Color(244, 67, 54));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(e -> dispose());
        topPanel.add(closeBtn, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        if (records.isEmpty()) {
            JLabel emptyLabel = new JLabel("暂无借阅记录");
            emptyLabel.setFont(new Font("微软雅黑", Font.PLAIN, 18));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            add(emptyLabel, BorderLayout.CENTER);
        }

        setVisible(true);
    }
} 