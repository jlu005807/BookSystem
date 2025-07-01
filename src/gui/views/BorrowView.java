package gui.views;

import gui.components.iButton;
import gui.components.iDialog;
import sql.Book;
import sql.BorrowController;
import java.awt.event.ActionListener;
import java.sql.Date;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class BorrowView extends JDialog {
    public BorrowView(int bookId, int userId, ActionListener needUpdate) {
        Book[] books = BorrowController.getAll();
        Book book = null;
        for (Book b : books) {
            if (b.getId() == bookId) {
                book = b;
                break;
            }
        }
        if (book == null) {
            iDialog.showSuccessDialog(null, "错误", 
                "❌ 未找到该图书\n\n" +
                "💡 请刷新列表后重试。");
            return;
        }
        if (book.getNumLeft() == 0) {
            iDialog.showSuccessDialog(null, "借阅失败", 
                "📚 该图书已全部借出\n\n" +
                "💡 可稍后再试。");
            return;
        }
        
        // 检查用户是否已经借阅了这本书
        if (BorrowController.hasUserBorrowedBook(userId, bookId)) {
            iDialog.showSuccessDialog(null, "借阅失败", 
                "\uD83D\uDCD6  您已经借阅了《" + book.getTitle() + "》\n\n" +
                "📋 请先归还后再借阅，查看您的借阅记录进行归还。\n" +
                "💡 提示：点击'我的借阅'可以查看当前借阅状态");
            return;
        }
        
        // 检查用户是否达到借阅上限（3本）
        if (BorrowController.hasReachedBorrowLimit(userId)) {
            int currentCount = BorrowController.getUserCurrentBorrowCount(userId);
            iDialog.showSuccessDialog(null, "借阅失败", 
                "\uD83D\uDCD6  您已达到借阅上限！\n\n" +
                "📊 当前已借阅：" + currentCount + " 本\n" +
                "🎯 借阅上限：" + 3 + " 本\n\n" +
                "💡 请先归还部分图书后再借阅。\n" +
                "📋 点击'我的借阅'可以查看和归还图书");
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
            iDialog.showSuccessDialog(null, "借阅失败", 
                "❌ 借阅操作失败\n\n" +
                "💡 请重试，如多次失败请联系管理员。");
        }
    }
} 