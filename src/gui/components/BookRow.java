package gui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import sql.Book;
import sql.BorrowController;

/**
 * 图书信息行组件，适配 Book 实体，美观卡片样式
 */
public class BookRow extends JPanel {
    private int id;
    private Color normalBg = Color.WHITE;
    private Color hoverBg = new Color(245, 250, 255);
    private Color borderColor = new Color(220, 230, 240);
    private Color borrowedBg = new Color(255, 248, 220); // 已借阅的背景色
    private Color borrowedBorderColor = new Color(255, 193, 7); // 已借阅的边框色
    private boolean isBorrowed = false;
    private boolean isSelected = false;

    public <E> BookRow(E book, ActionListener listener, boolean showOperation) {
        this(book, listener, showOperation, 0); // 默认用户ID为0
    }

    public <E> BookRow(E book, ActionListener listener, boolean showOperation, int userId) {
        setLayout(new BorderLayout(12, 0));
        setOpaque(false);
        setBackground(normalBg);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(6, 12, 6, 12),
            BorderFactory.createLineBorder(borderColor, 1, true)
        ));
        
        // 设置固定高度，确保所有书籍卡片高度一致
        setPreferredSize(new Dimension(getPreferredSize().width, 90));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        
        if (book instanceof Book) {
            Book b = (Book) book;
            this.id = b.getId();

            // 检查用户是否已借阅这本书
            if (userId > 0) {
                isBorrowed = BorrowController.hasUserBorrowedBook(userId, this.id);
            }

            // 如果已借阅，调整样式
            if (isBorrowed) {
                setBackground(borrowedBg);
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(6, 12, 6, 12),
                    BorderFactory.createLineBorder(borrowedBorderColor, 2, true)
                ));
            }

            // 左侧信息区
            JPanel infoPanel = new JPanel();
            infoPanel.setOpaque(false);
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

            JLabel titleLabel = new JLabel(b.getTitle());
            titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
            titleLabel.setForeground(new Color(33, 150, 243));
            infoPanel.add(titleLabel);

            JLabel authorLabel = new JLabel("作者: " + b.getAuthor());
            authorLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
            authorLabel.setForeground(new Color(66, 66, 66));
            infoPanel.add(authorLabel);

            JLabel isbnLabel = new JLabel("ISBN: " + b.getIsbn());
            isbnLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
            isbnLabel.setForeground(new Color(120, 144, 156));
            infoPanel.add(isbnLabel);

            JLabel numLabel = new JLabel("剩余: " + b.getNumLeft() + "/" + b.getNumAll());
            numLabel.setFont(new Font("微软雅黑", Font.BOLD, 13));
            if (b.getNumLeft() == 0) {
                numLabel.setForeground(new Color(244, 67, 54)); // 红色
            } else if (b.getNumLeft() > 0 && b.getNumLeft() < b.getNumAll()) {
                numLabel.setForeground(new Color(255, 152, 0)); // 橙色
            } else {
                numLabel.setForeground(new Color(76, 175, 80)); // 绿色
            }
            infoPanel.add(numLabel);

            add(infoPanel, BorderLayout.CENTER);

            // 右侧状态区
            JPanel statusPanel = new JPanel(new BorderLayout());
            statusPanel.setOpaque(false);
            if (isBorrowed) {
                JLabel borrowedLabel = new JLabel("已借阅");
                borrowedLabel.setFont(new Font("微软雅黑", Font.BOLD, 12));
                borrowedLabel.setForeground(new Color(255, 152, 0));
                borrowedLabel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
                statusPanel.add(borrowedLabel, BorderLayout.NORTH);
            }
            if (userId > 0 && !showOperation) {
                int currentBorrowCount = BorrowController.getUserCurrentBorrowCount(userId);
                JLabel countLabel = new JLabel("已借: " + currentBorrowCount + "/3");
                countLabel.setFont(new Font("微软雅黑", Font.PLAIN, 11));
                if (currentBorrowCount >= 3) {
                    countLabel.setForeground(new Color(244, 67, 54));
                } else {
                    countLabel.setForeground(new Color(76, 175, 80));
                }
                countLabel.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
                statusPanel.add(countLabel, BorderLayout.CENTER);
            }
            if (showOperation) {
                JButton opBtn = new JButton("借阅记录");
                opBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
                opBtn.setBackground(new Color(33, 150, 243));
                opBtn.setForeground(Color.WHITE);
                opBtn.setFocusPainted(false);
                opBtn.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));
                opBtn.addActionListener(e -> {
                    // 弹窗显示该书所有借阅记录
                    new gui.views.BookBorrowRecordView(this.id);
                });
                statusPanel.add(opBtn, BorderLayout.SOUTH);
            }
            if (statusPanel.getComponentCount() > 0) {
                add(statusPanel, BorderLayout.EAST);
            }
        } else {
            throw new IllegalArgumentException("BookRow: 参数类型不是Book");
        }
        
        // 鼠标悬停高亮
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!isSelected) {
                    setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createEmptyBorder(6, 12, 6, 12),
                        BorderFactory.createLineBorder(new Color(33, 150, 243), 2, true)
                    ));
                    repaint();
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!isSelected) {
                    if (!isBorrowed) {
                        setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createEmptyBorder(6, 12, 6, 12),
                            BorderFactory.createLineBorder(borderColor, 1, true)
                        ));
                    } else {
                        setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createEmptyBorder(6, 12, 6, 12),
                            BorderFactory.createLineBorder(borrowedBorderColor, 2, true)
                        ));
                    }
                    repaint();
                }
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getSource() == BookRow.this) {
                    if (listener != null) {
                        ActionEvent e = new ActionEvent(BookRow.this, ActionEvent.ACTION_PERFORMED, String.valueOf(id));
                        listener.actionPerformed(e);
                    }
                }
            }
        });
        
        // 禁止子组件获取焦点，避免事件冒泡
        for (Component comp : getComponents()) {
            comp.setFocusable(false);
        }
    }

    public int getID() {
        return id;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 阴影
        g2.setColor(new Color(0,0,0,24));
        g2.fillRoundRect(4, 6, getWidth()-8, getHeight()-8, 18, 18);
        // 背景
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight()-2, 18, 18);
        super.paintComponent(g2);
        g2.dispose();
    }

    // 选中/取消选中高亮
    public void setSelected(boolean selected) {
        this.isSelected = selected;
        if (selected) {
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(6, 12, 6, 12),
                BorderFactory.createLineBorder(new Color(25, 118, 210), 3, true)
            ));
            setBackground(new Color(227, 242, 253));
        } else {
            if (isBorrowed) {
                setBackground(borrowedBg);
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(6, 12, 6, 12),
                    BorderFactory.createLineBorder(borrowedBorderColor, 2, true)
                ));
            } else {
                setBackground(normalBg);
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(6, 12, 6, 12),
                    BorderFactory.createLineBorder(borderColor, 1, true)
                ));
            }
        }
        repaint();
    }
} 