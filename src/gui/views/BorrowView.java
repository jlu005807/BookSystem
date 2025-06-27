package gui.views;

import gui.components.iDialog;
import sql.BorrowController;
import entity.Book;
import java.awt.event.ActionListener;
import java.sql.Date;

public class BorrowView {
    public BorrowView(int bookId, int userId, ActionListener needUpdate) {
        System.out.println("BorrowView 传入 bookId: " + bookId);
        Book[] books = BorrowController.getAll();
        Book book = null;
        for (Book b : books) {
            if (b.getId() == bookId) {
                book = b;
                break;
            }
        }
        if (book == null) {
            iDialog.dialogError(null, "错误", "未找到该图书\n请刷新列表后重试。");
            return;
        }
        if (book.getNumLeft() == 0) {
            iDialog.dialogMessage(null, "借阅失败", "该图书已全部借出\n可稍后再试。");
            return;
        }
        // 美化确认弹窗，按钮居中、主色高亮、圆角
        boolean choice = iDialog.showConfirmDialog(
            null,
            "借阅确认",
            "\uD83D\uDCD6  确认借阅《" + book.getTitle() + "》？\n\n作者：" + book.getAuthor() + "\n剩余可借：" + book.getNumLeft() + " 本"
        );
        if (!choice) return;
        boolean success = BorrowController.borrowBook(bookId, userId, new Date(System.currentTimeMillis()));
        if (success) {
            iDialog.showSuccessDialog(null, "借阅成功", "\uD83C\uDF89 您已成功借阅《" + book.getTitle() + "》！\n请及时归还，祝您阅读愉快。");
            needUpdate.actionPerformed(null);
        } else {
            iDialog.dialogError(null, "借阅失败", "借阅操作失败，请重试\n如多次失败请联系管理员。");
        }
    }
} 