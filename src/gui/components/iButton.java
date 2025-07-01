package gui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

import utils.u;

public class iButton extends JButton {
    final int normalSize = 20;
    final int smallSize = 15;

    // 新增：按钮状态颜色（每种类型独立）
    private Color normalBgColor = new Color(245, 245, 245);
    private Color hoverBgColor = new Color(220, 230, 245);
    private Color pressedBgColor = new Color(200, 215, 235);
    private Color shadowColor = new Color(0, 0, 0, 40); // 阴影颜色，带透明度
    private int shadowOffsetY = 4; // 阴影向下偏移
    private int shadowArc = 20; // 阴影圆角
    private int arc = 18; // 按钮圆角
    private Color currentBgColor = normalBgColor;

    /**
     * 通过文本和类型创建按钮（适合5～6个汉字）
     *
     * @param text 按钮文本
     * @param type 按钮类型
     */
    public iButton(String text, ButtonType type) {
        super(text);
        setFont(new Font("微软雅黑", Font.PLAIN, normalSize));
        setSize(150, 50);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        switch (type) {
            case NORMAL:
                setForeground(new Color(0, 0, 0));
                normalBgColor = new Color(245, 245, 245);
                hoverBgColor = new Color(230, 230, 230);
                pressedBgColor = new Color(210, 210, 210);
                break;
            case PRIMARY:
                setForeground(Color.WHITE);
                normalBgColor = new Color(0, 123, 255);
                hoverBgColor = new Color(30, 144, 255);
                pressedBgColor = new Color(0, 90, 200);
                break;
            case WARNING:
                setForeground(new Color(255, 193, 7));
                setFont(new Font("微软雅黑", Font.BOLD, normalSize));
                normalBgColor = new Color(255, 250, 220);
                hoverBgColor = new Color(255, 236, 150);
                pressedBgColor = new Color(255, 220, 100);
                break;
            case DANGER:
                setForeground(Color.WHITE);
                setFont(new Font("微软雅黑", Font.BOLD, normalSize));
                normalBgColor = new Color(220, 53, 69);
                hoverBgColor = new Color(200, 40, 55);
                pressedBgColor = new Color(160, 30, 40);
                break;
            case SMALL:
                setForeground(new Color(0, 0, 0));
                setFont(new Font("微软雅黑", Font.PLAIN, smallSize));
                setSize(100, 40);
                normalBgColor = new Color(245, 245, 245);
                hoverBgColor = new Color(230, 230, 230);
                pressedBgColor = new Color(210, 210, 210);
                break;
            case SMALL_PRIMARY:
                setForeground(Color.WHITE);
                setFont(new Font("微软雅黑", Font.BOLD, smallSize));
                setSize(100, 40);
                normalBgColor = new Color(0, 123, 255);
                hoverBgColor = new Color(30, 144, 255);
                pressedBgColor = new Color(0, 90, 200);
                break;
        }
        currentBgColor = normalBgColor;
        addHoverEffect();
    }

    /**
     * 仅通过文本创建按钮
     *
     * @param text 按钮文本
     */
    public iButton(String text) {
        this(text, ButtonType.NORMAL);
    }
    public iButton(String text, ActionListener listener) {
        this(text);
        addListener(listener);
    }

    /**
     * 通过图片路径和大小创建按钮
     *
     * @param imagePath 图片路径
     * @param size      按钮大小
     */
    public iButton(String imagePath, Dimension size) {
        ImageIcon icon = u.getImageIcon(imagePath);
        icon.setImage(icon.getImage().getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH));
        setSize(size.width + 10, size.height + 10);
        setIcon(icon);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        addHoverEffect();
    }

    public void addListener(ActionListener listener) {
        addActionListener(listener);
    }

    // 新增：添加悬停和点击效果
    private void addHoverEffect() {
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                currentBgColor = hoverBgColor;
                repaint();
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                currentBgColor = normalBgColor;
                repaint();
            }
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                currentBgColor = pressedBgColor;
                repaint();
            }
            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                if (getBounds().contains(evt.getPoint())) {
                    currentBgColor = hoverBgColor;
                } else {
                    currentBgColor = normalBgColor;
                }
                repaint();
            }
        });
    }

    // 新增：重写paintComponent实现自定义圆角背景和阴影
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 阴影
        g2.setColor(shadowColor);
        g2.fillRoundRect(0, shadowOffsetY, getWidth(), getHeight() - shadowOffsetY, shadowArc, shadowArc);
        // 按钮背景
        g2.setColor(currentBgColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight() - shadowOffsetY, arc, arc);
        // 让文字和图标正常显示
        super.paintComponent(g2);
        g2.dispose();
    }

    /**
     * 按钮类型
     */
    public enum ButtonType {
        NORMAL,     //普通按钮
        PRIMARY,    //主要按钮
        WARNING,    //警告按钮
        DANGER,     //危险按钮
        SMALL,      //较小按钮
        SMALL_PRIMARY,   //较小主要按钮
    }

    /**
     * 静态工厂方法：创建标准化的按钮
     * @param text 按钮文本
     * @param type 按钮类型
     * @param listener 事件监听器
     * @param fontSize 字体大小
     * @return 配置好的按钮
     */
    public static iButton create(String text, ButtonType type, ActionListener listener, int fontSize) {
        iButton btn = new iButton(text, type);
        btn.setFont(new Font("微软雅黑", Font.BOLD, fontSize));
        if (listener != null) {
            btn.addActionListener(listener);
        }
        return btn;
    }

    /**
     * 静态工厂方法：创建标准化的按钮（默认字体大小16）
     * @param text 按钮文本
     * @param type 按钮类型
     * @param listener 事件监听器
     * @return 配置好的按钮
     */
    public static iButton create(String text, ButtonType type, ActionListener listener) {
        return create(text, type, listener, 16);
    }
}
