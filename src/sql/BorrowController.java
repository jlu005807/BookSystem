package sql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import sql.Book;
import sql.BorrowRecord;

/**
 * 借阅控制器，负责借阅、归还、查询借阅记录等
 */
public class BorrowController {
    // 查询所有图书
    public static Book[] getAll() {
        List<Book> books = new ArrayList<>();
        try (Connection conn = ConnectionPool.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM book")) {
            while (rs.next()) {
                Book book = new Book(
                        rs.getInt("id"),
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("numLeft"),
                        rs.getInt("numAll"),
                        rs.getDate("addTime")
                );
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("getAll() 查询到的所有图书：");
        for (Book b : books) {
            System.out.println("查到的图书ID: " + b.getId() + ", 书名: " + b.getTitle());
        }
        return books.toArray(new Book[0]);
    }

    // 借阅图书
    public static boolean borrowBook(int bookId, int userId, Date borrowTime) {
        try (Connection conn = ConnectionPool.getConnection()) {
            // 检查剩余数量
            PreparedStatement checkStmt = conn.prepareStatement("SELECT numLeft FROM book WHERE id = ?");
            checkStmt.setInt(1, bookId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt("numLeft") > 0) {
                // 插入借阅记录
                PreparedStatement insertStmt = conn.prepareStatement(
                        "INSERT INTO borrow_record (book_id, user_id, borrow_time) VALUES (?, ?, ?)");
                insertStmt.setInt(1, bookId);
                insertStmt.setInt(2, userId);
                insertStmt.setDate(3, borrowTime);
                insertStmt.executeUpdate();

                // 更新图书剩余数量
                PreparedStatement updateStmt = conn.prepareStatement(
                        "UPDATE book SET numLeft = numLeft - 1 WHERE id = ?");
                updateStmt.setInt(1, bookId);
                updateStmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 归还图书
    public static boolean returnBook(int borrowId, Date returnTime) {
        try (Connection conn = ConnectionPool.getConnection()) {
            // 查找借阅记录
            PreparedStatement findStmt = conn.prepareStatement(
                    "SELECT book_id FROM borrow_record WHERE id = ? AND return_time IS NULL");
            findStmt.setInt(1, borrowId);
            ResultSet rs = findStmt.executeQuery();
            if (rs.next()) {
                int bookId = rs.getInt("book_id");
                // 更新借阅记录
                PreparedStatement updateRecord = conn.prepareStatement(
                        "UPDATE borrow_record SET return_time = ? WHERE id = ?");
                updateRecord.setDate(1, returnTime);
                updateRecord.setInt(2, borrowId);
                updateRecord.executeUpdate();

                // 更新图书剩余数量
                PreparedStatement updateBook = conn.prepareStatement(
                        "UPDATE book SET numLeft = numLeft + 1 WHERE id = ?");
                updateBook.setInt(1, bookId);
                updateBook.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 查询某本书的所有借阅记录
    public static List<BorrowRecord> getRecordsByBookId(int bookId) {
        List<BorrowRecord> records = new ArrayList<>();
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM borrow_record WHERE book_id = ?")) {
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                BorrowRecord record = new BorrowRecord();
                record.setId(rs.getInt("id"));
                record.setBookId(rs.getInt("book_id"));
                record.setUserId(rs.getInt("user_id"));
                record.setBorrowTime(rs.getDate("borrow_time"));
                record.setReturnTime(rs.getDate("return_time"));
                records.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    // 查询某用户的所有借阅记录
    public static List<BorrowRecord> getRecordsByUserId(int userId) {
        List<BorrowRecord> records = new ArrayList<>();
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM borrow_record WHERE user_id = ?")) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                BorrowRecord record = new BorrowRecord();
                record.setId(rs.getInt("id"));
                record.setBookId(rs.getInt("book_id"));
                record.setUserId(rs.getInt("user_id"));
                record.setBorrowTime(rs.getDate("borrow_time"));
                record.setReturnTime(rs.getDate("return_time"));
                records.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    // 新增图书
    public static void create(Book book) {
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "INSERT INTO book (isbn, title, author, numLeft, numAll, addTime) VALUES (?, ?, ?, ?, ?, CURRENT_DATE)")) {
            ps.setString(1, book.getIsbn());
            ps.setString(2, book.getTitle());
            ps.setString(3, book.getAuthor());
            ps.setInt(4, book.getNumLeft());
            ps.setInt(5, book.getNumAll());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 删除图书
    public static void delete(int bookId) {
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM book WHERE id = ?")) {
            ps.setInt(1, bookId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 根据关键词模糊搜索图书
    public static Book[] searchBooks(String keyword) {
        List<Book> books = new ArrayList<>();
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM book WHERE title LIKE ? OR author LIKE ? OR isbn LIKE ?")) {
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Book book = new Book(
                        rs.getInt("id"),
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("numLeft"),
                        rs.getInt("numAll"),
                        rs.getDate("addTime")
                );
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("searchBooks() 搜索关键词 '" + keyword + "' 找到 " + books.size() + " 本书");
        return books.toArray(new Book[0]);
    }

    // 检查用户是否已借阅某本书（未归还）
    public static boolean hasUserBorrowedBook(int userId, int bookId) {
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT COUNT(*) FROM borrow_record WHERE user_id = ? AND book_id = ? AND return_time IS NULL")) {
            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 获取用户当前借阅的某本书的借阅记录ID
    public static int getUserBorrowRecordId(int userId, int bookId) {
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT id FROM borrow_record WHERE user_id = ? AND book_id = ? AND return_time IS NULL")) {
            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // 获取用户当前借阅的图书数量（未归还的）
    public static int getUserCurrentBorrowCount(int userId) {
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT COUNT(*) FROM borrow_record WHERE user_id = ? AND return_time IS NULL")) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 检查用户是否达到借阅上限（3本）
    public static boolean hasReachedBorrowLimit(int userId) {
        return getUserCurrentBorrowCount(userId) >= 3;
    }

    // 判断是否存在同名同作者的书籍
    public static Book findSameBook(String title, String author) {
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM book WHERE title = ? AND author = ?")) {
            stmt.setString(1, title);
            stmt.setString(2, author);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Book(
                        rs.getInt("id"),
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("numLeft"),
                        rs.getInt("numAll"),
                        rs.getDate("addTime")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 更新图书库存数量
    public static void updateBookNum(int bookId, int numAll, int numLeft) {
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "UPDATE book SET numAll = ?, numLeft = ? WHERE id = ?")) {
            ps.setInt(1, numAll);
            ps.setInt(2, numLeft);
            ps.setInt(3, bookId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 删除某本书的所有借阅记录
    public static void deleteAllRecordsByBookId(int bookId) {
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM borrow_record WHERE book_id = ?")) {
            ps.setInt(1, bookId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
} 