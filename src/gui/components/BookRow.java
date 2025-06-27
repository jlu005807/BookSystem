package gui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import entity.Book;

/**
 * 图书信息行组件，适配 Book 实体，美观卡片样式
 */
public class BookRow extends JPanel {
    private int id;
    private Color normalBg = Color.WHITE;
    private Color hoverBg = new Color(245, 250, 255);
    private Color borderColor = new Color(220, 230, 240);

    public <E> BookRow(E book, ActionListener listener, boolean showOperation) {
        setLayout(new BorderLayout(12, 0));
        setOpaque(false);
        setBackground(normalBg);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(8, 16, 8, 16),
            BorderFactory.createLineBorder(borderColor, 1, true)
        ));
        if (book instanceof Book) {
            Book b = (Book) book;
            this.id = b.getId();
            System.out.println("BookRow 构造 id: " + this.id);

            // 左侧信息区
            JPanel infoPanel = new JPanel();
            infoPanel.setOpaque(false);
            infoPanel.setLayout(new GridLayout(2, 2, 10, 2));
            JLabel titleLabel = new JLabel(b.getTitle());
            titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
            JLabel authorLabel = new JLabel("作者: " + b.getAuthor());
            authorLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
            JLabel isbnLabel = new JLabel("ISBN: " + b.getIsbn());
            isbnLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
            JLabel numLabel = new JLabel("剩余: " + b.getNumLeft() + "/" + b.getNumAll());
            numLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
            numLabel.setForeground(new Color(33, 150, 243));
            infoPanel.add(titleLabel);
            infoPanel.add(authorLabel);
            infoPanel.add(isbnLabel);
            infoPanel.add(numLabel);
            add(infoPanel, BorderLayout.CENTER);

            // 右侧操作区
            if (showOperation) {
                JButton opBtn = new JButton("操作");
                opBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
                opBtn.setBackground(new Color(33, 150, 243));
                opBtn.setForeground(Color.WHITE);
                opBtn.setFocusPainted(false);
                opBtn.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));
                add(opBtn, BorderLayout.EAST);
            }
        } else {
            throw new IllegalArgumentException("BookRow: 参数类型不是Book");
        }
        // 鼠标悬停高亮
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(hoverBg);
                repaint();
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(normalBg);
                repaint();
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getSource() == BookRow.this) {
                    System.out.println("BookRow 被点击，id: " + id);
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
} 