package gui.views;

import gui.components.iDialog;
import sql.BorrowController;
import entity.BorrowRecord;
import entity.Book;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Comparator;

public class MyBorrowView extends JDialog {
    public MyBorrowView(Frame owner, int userId, Runnable onReturn) {
        super(owner, "我的借阅", true);
        setLayout(new BorderLayout(10, 10));
        List<BorrowRecord> records = BorrowController.getRecordsByUserId(userId);
        // 按借阅时间倒序排序，仅保留最近5条
        records.sort(Comparator.comparing(BorrowRecord::getBorrowTime, Comparator.nullsLast(Comparator.reverseOrder())));
        if (records.size() > 5) {
            records = records.subList(0, 5);
        }
        Book[] books = BorrowController.getAll();
        Map<Integer, String> bookIdToTitle = new HashMap<>();
        for (Book b : books) {
            bookIdToTitle.put(b.getId(), b.getTitle());
        }

        String[] columnNames = {"图书名", "借阅时间", "归还时间", "状态", "操作"};
        Object[][] data = new Object[records.size()][5];
        for (int i = 0; i < records.size(); i++) {
            BorrowRecord record = records.get(i);
            String bookTitle = bookIdToTitle.getOrDefault(record.getBookId(), "未知图书");
            data[i][0] = bookTitle;
            data[i][1] = record.getBorrowTime() == null ? "" : record.getBorrowTime().toString();
            data[i][2] = record.getReturnTime() == null ? "未归还" : record.getReturnTime().toString();
            data[i][3] = record.getReturnTime() == null ? "可归还" : "已归还";
            data[i][4] = record; // 占位，后续渲染为按钮
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4 && ((BorrowRecord) getValueAt(row, 4)).getReturnTime() == null;
            }
        };
        JTable table = new JTable(model);
        table.setRowHeight(32);
        table.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        table.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 16));
        ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // 居中渲染
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount() - 1; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // 操作列渲染为按钮
        table.getColumnModel().getColumn(4).setCellRenderer((tbl, value, isSelected, hasFocus, row, col) -> {
            BorrowRecord record = (BorrowRecord) value;
            JButton btn = new JButton("归还");
            btn.setBackground(new Color(33, 150, 243));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
            btn.setEnabled(record.getReturnTime() == null);
            if (record.getReturnTime() != null) btn.setText("-");
            return btn;
        });
        table.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(new JCheckBox()) {
            private JButton btn;
            private int editingRow;
            {
                btn = new JButton("归还");
                btn.setBackground(new Color(33, 150, 243));
                btn.setForeground(Color.WHITE);
                btn.setFocusPainted(false);
                btn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
                btn.addActionListener(e -> {
                    BorrowRecord record = (BorrowRecord) model.getValueAt(editingRow, 4);
                    boolean confirm = iDialog.showConfirmDialog(MyBorrowView.this, "确认归还", "\uD83D\uDCD6  确认归还该图书？");
                    if (confirm) {
                        BorrowController.returnBook(record.getId(), new Date(System.currentTimeMillis()));
                        iDialog.showSuccessDialog(MyBorrowView.this, "归还成功", "\uD83C\uDF89 图书已归还，感谢您的配合！");
                        dispose();
                        if (onReturn != null) onReturn.run();
                    }
                });
            }
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                editingRow = row;
                BorrowRecord record = (BorrowRecord) value;
                btn.setEnabled(record.getReturnTime() == null);
                return btn;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        // 关闭按钮放右上角，红色美化
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton closeBtn = new JButton("关闭");
        closeBtn.setBackground(new Color(244, 67, 54));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(e -> dispose());
        topPanel.add(closeBtn, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // 无数据时居中显示提示
        if (records.isEmpty()) {
            JLabel emptyLabel = new JLabel("暂无借阅记录");
            emptyLabel.setFont(new Font("微软雅黑", Font.PLAIN, 18));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            add(emptyLabel, BorderLayout.CENTER);
        }

        setSize(700, 400);
        setLocationRelativeTo(owner);
        setVisible(true);
    }
} 