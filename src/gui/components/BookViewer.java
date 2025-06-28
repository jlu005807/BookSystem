package gui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import entity.Book;

/**
 * 通用图书列表显示组件，可用于用户界面和管理员界面
 */
public class BookViewer extends JPanel {
    ActionListener listener;
    private boolean showOperation;
    private JPanel listPanel;
    private JScrollPane scrollPane;
    private int userId = 0; // 用户ID，用于检查借阅状态

    /**
     * @param listener 点击行时的回调
     */
    public BookViewer(ActionListener listener) {
        this(listener, false);
    }

    /**
     * @param listener 点击行时的回调
     * @param showOperation 是否显示操作按钮（如删除/编辑）
     */
    public BookViewer(ActionListener listener, boolean showOperation) {
        this(listener, showOperation, 0);
    }

    /**
     * @param listener 点击行时的回调
     * @param showOperation 是否显示操作按钮（如删除/编辑）
     * @param userId 用户ID，用于检查借阅状态
     */
    public BookViewer(ActionListener listener, boolean showOperation, int userId) {
        this.listener = listener;
        this.showOperation = showOperation;
        this.userId = userId;
        setLayout(new BorderLayout());
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);
        scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
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
    }

    public void updateItem(Object[] books) {
        listPanel.removeAll();
        if (books == null || books.length == 0) {
            // 创建美观的空状态提示
            JPanel emptyPanel = new JPanel(new BorderLayout());
            emptyPanel.setOpaque(false);
            emptyPanel.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));
            
            JLabel emptyLabel = new JLabel("暂无图书信息", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
            emptyLabel.setForeground(new Color(120, 144, 156));
            
            emptyPanel.add(emptyLabel, BorderLayout.CENTER);
            listPanel.add(emptyPanel);
        } else {
            // 添加间距，确保书籍卡片之间有适当的间隔
            for (int i = 0; i < books.length; i++) {
                Object book = books[i];
                if (book instanceof entity.Book) {
                    System.out.println("BookViewer updateItem id: " + ((entity.Book)book).getId());
                }
                listPanel.add(new BookRow(book, listener, showOperation, userId));
                
                // 在书籍卡片之间添加间距（除了最后一个）
                if (i < books.length - 1) {
                    listPanel.add(Box.createVerticalStrut(6));
                }
            }
        }
        listPanel.revalidate();
        listPanel.repaint();
    }
} 