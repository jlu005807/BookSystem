package gui.views;

import gui.components.*;
import sql.BorrowController;
import sql.Book;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BookChangeView extends JDialog {
    final int width = 500;
    Book book = new Book();

    public static void showDialog(ActionListener needUpdate) {
        new BookChangeView(needUpdate);
    }

    public BookChangeView(ActionListener needUpdate) {
        iWindow w = new iWindow("添加图书", width + 100, 400);
        w.setLayout(new BorderLayout(30, 30));

        JPanel warningPanel = new JPanel(new FlowLayout());
        iLabel warningLabel = new iLabel("");
        warningLabel.setForeground(Color.RED);
        warningPanel.add(warningLabel);

        JPanel contentPanel = new JPanel(new GridLayout(4, 1));
        iField isbnField = new iField("ISBN", width);
        iField titleField = new iField("书名", width);
        iField authorField = new iField("作者", width);
        iField numField = new iField("数量", width);
        contentPanel.add(isbnField);
        contentPanel.add(titleField);
        contentPanel.add(authorField);
        contentPanel.add(numField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        iButton submit = new iButton("提交", iButton.ButtonType.PRIMARY);
        submit.addActionListener(e -> {
            book.setIsbn(isbnField.getText().trim());
            book.setTitle(titleField.getText().trim());
            book.setAuthor(authorField.getText().trim());
            try {
                book.setNumAll(Integer.parseInt(numField.getText().trim()));
            } catch (Exception ex) {
                warningLabel.setText("数量必须为数字");
                return;
            }
            book.setNumLeft(book.getNumAll());

            if(book.getTitle().equals("") || book.getIsbn().equals("") || book.getAuthor().equals("") || book.getNumAll() == 0) {
                warningLabel.setText("请填写完整信息");
            } else {
                BorrowController.create(book);
                boolean choice = iDialog.dialogConfirm(w, "添加成功!", "是否继续添加？");
                if(choice) {
                    book = new Book();
                    isbnField.setText("");
                    titleField.setText("");
                    authorField.setText("");
                    numField.setText("");
                } else {
                    w.dispose();
                }
                needUpdate.actionPerformed(new ActionEvent(w, 0, "add"));
            }
        });
        iButton cancel = new iButton("取消", iButton.ButtonType.DANGER);
        cancel.addActionListener(e -> w.dispose());
        buttonPanel.add(submit);
        buttonPanel.add(cancel);

        w.add(warningPanel, BorderLayout.NORTH);
        w.add(contentPanel, BorderLayout.CENTER);
        w.add(buttonPanel, BorderLayout.SOUTH);
        w.done();
    }
} 