package gui.views;

import gui.components.*;
import sql.BorrowController;
import sql.Book;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class BookChangeView extends JDialog {
    final int width = 500;
    Book book = new Book();

    public static void showDialog(ActionListener needUpdate) {
        new BookChangeView(needUpdate);
    }

    public BookChangeView(ActionListener needUpdate) {
        iWindow w = new iWindow("添加图书", width + 100, 400);
        w.setLayout(null);
        w.setResizable(false);
        w.setLocationRelativeTo(null);

        // 顶部Logo和标题
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        logoPanel.setBounds(20, 10, 350, 60);
        logoPanel.setOpaque(false);
        JLabel logo = new JLabel(utils.u.getImageIcon("flag", 40, 40));
        iLabel title = new iLabel("添加图书", 24);
        title.setFont(new Font("微软雅黑", Font.BOLD, 24));
        title.setForeground(new Color(33, 150, 243));
        logoPanel.add(logo);
        logoPanel.add(title);
        w.add(logoPanel);

        // 输入区（圆角白底，内边距，阴影，分割线）
        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // 阴影
                g2.setColor(new Color(0,0,0,18));
                g2.fillRoundRect(6, 8, getWidth()-12, getHeight()-12, 24, 24);
                // 背景
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight()-4, 24, 24);
                g2.dispose();
            }
        };
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBounds(40, 80, width, 180);
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(24, 36, 24, 36));
        iField isbnField = new iField("ISBN", width-60);
        iField titleField = new iField("书名", width-60);
        iField authorField = new iField("作者", width-60);
        iField numField = new iField("数量", width-60);
        contentPanel.add(isbnField);
        contentPanel.add(createDivider());
        contentPanel.add(Box.createVerticalStrut(8));
        contentPanel.add(titleField);
        contentPanel.add(createDivider());
        contentPanel.add(Box.createVerticalStrut(8));
        contentPanel.add(authorField);
        contentPanel.add(createDivider());
        contentPanel.add(Box.createVerticalStrut(8));
        contentPanel.add(numField);
        w.add(contentPanel);

        // 按钮区
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBounds(0, 270, width + 100, 60);
        iButton submit = new iButton("提交", iButton.ButtonType.PRIMARY);
        iButton cancel = new iButton("取消", iButton.ButtonType.DANGER);
        buttonPanel.add(submit);
        buttonPanel.add(cancel);
        w.add(buttonPanel);

        submit.addActionListener(e -> {
            book.setIsbn(isbnField.getText().trim());
            book.setTitle(titleField.getText().trim());
            book.setAuthor(authorField.getText().trim());
            try {
                book.setNumAll(Integer.parseInt(numField.getText().trim()));
            } catch (Exception ex) {
                iDialog.showErrorDialog(w, "输入格式错误", "数量必须为数字，请重新输入。例如：5");
                return;
            }
            book.setNumLeft(book.getNumAll());
            if(book.getTitle().equals("") || book.getIsbn().equals("") || book.getAuthor().equals("") || book.getNumAll() == 0) {
                iDialog.showWarningDialog(w, "信息未填写完整", "请填写所有字段，且数量需大于0。");
            } else {
                // 检查同名同作者
                sql.Book sameBook = sql.BorrowController.findSameBook(book.getTitle(), book.getAuthor());
                if (sameBook != null) {
                    boolean merge = iDialog.showConfirmDialog(w, "书籍已存在", "已存在同名同作者的书籍：\n《" + sameBook.getTitle() + "》\n作者: " + sameBook.getAuthor() + "\n是否合并库存？\n(合并将增加原有库存，不合并则取消添加)");
                    if (merge) {
                        // 合并库存
                        int newNumAll = sameBook.getNumAll() + book.getNumAll();
                        int newNumLeft = sameBook.getNumLeft() + book.getNumAll();
                        sameBook.setNumAll(newNumAll);
                        sameBook.setNumLeft(newNumLeft);
                        // 更新数据库
                        sql.BorrowController.updateBookNum(sameBook.getId(), newNumAll, newNumLeft);
                        iDialog.showSuccessDialog(w, "合并成功", "库存已合并，当前总库存：" + newNumAll);
                        needUpdate.actionPerformed(new ActionEvent(w, 0, "add"));
                        // 重置表单
                        book = new Book();
                        isbnField.setText("");
                        titleField.setText("");
                        authorField.setText("");
                        numField.setText("");
                    } // 否则直接取消添加
                    return;
                }
                // 不存在同名同作者，正常添加
                sql.BorrowController.create(book);
                iDialog.showSuccessDialog(w, "添加成功", "图书已成功添加！");
                // 重置表单
                book = new Book();
                isbnField.setText("");
                titleField.setText("");
                authorField.setText("");
                numField.setText("");
                needUpdate.actionPerformed(new ActionEvent(w, 0, "add"));
            }
        });
        cancel.addActionListener(e -> w.dispose());

        // 自动聚焦到ISBN
        SwingUtilities.invokeLater(() -> isbnField.getComponent(1).requestFocusInWindow());

        // 回车提交支持
        KeyAdapter enterSubmit = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    submit.doClick();
                }
            }
        };
        ((JTextField)isbnField.getComponent(1)).addKeyListener(enterSubmit);
        ((JTextField)titleField.getComponent(1)).addKeyListener(enterSubmit);
        ((JTextField)authorField.getComponent(1)).addKeyListener(enterSubmit);
        ((JTextField)numField.getComponent(1)).addKeyListener(enterSubmit);

        w.done();
    }

    private JComponent createDivider() {
        JPanel line = new JPanel();
        line.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        line.setPreferredSize(new Dimension(1, 1));
        line.setBackground(new Color(230, 230, 230));
        return line;
    }
} 