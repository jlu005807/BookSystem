package gui.views;

import gui.components.iButton;
import gui.components.iDialog;
import sql.BorrowController;
import sql.BorrowRecord;
import sql.Book;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Comparator;
import java.util.ArrayList;

public class MyBorrowView extends JDialog {
    public MyBorrowView(Frame owner, int userId, Runnable onReturn) {
        super(owner, "我的借阅", true);
        setLayout(new BorderLayout(10, 10));
        List<BorrowRecord> records = BorrowController.getRecordsByUserId(userId);
        // 优化排序：未归还的在前，已归还的在后，组内按借阅时间倒序
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
        // 合并，未归还的在前
        List<BorrowRecord> sortedRecords = new ArrayList<>();
        sortedRecords.addAll(notReturned);
        sortedRecords.addAll(returned);
        records = sortedRecords;
        // 不再截断，只显示全部记录
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
        // 保证表格始终显示滚动条
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        // 美化滚动条
        JScrollBar vBar = scrollPane.getVerticalScrollBar();
        vBar.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(180, 200, 240, 180);
                this.trackColor = new Color(245, 250, 255);
            }
            @Override
            protected JButton createDecreaseButton(int orientation) {
                JButton btn = super.createDecreaseButton(orientation);
                btn.setVisible(false);
                return btn;
            }
            @Override
            protected JButton createIncreaseButton(int orientation) {
                JButton btn = super.createIncreaseButton(orientation);
                btn.setVisible(false);
                return btn;
            }
            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(thumbColor);
                g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 12, 12);
                g2.dispose();
            }
            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(trackColor);
                g2.fillRoundRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height, 12, 12);
                g2.dispose();
            }
        });
        vBar.setPreferredSize(new Dimension(8, Integer.MAX_VALUE));
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