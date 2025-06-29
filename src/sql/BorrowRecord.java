package sql;

public class BorrowRecord {
    private int id;
    private int bookId;
    private int userId;
    private java.sql.Date borrowTime;
    private java.sql.Date returnTime;

    public BorrowRecord() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public java.sql.Date getBorrowTime() { return borrowTime; }
    public void setBorrowTime(java.sql.Date borrowTime) { this.borrowTime = borrowTime; }
    public java.sql.Date getReturnTime() { return returnTime; }
    public void setReturnTime(java.sql.Date returnTime) { this.returnTime = returnTime; }
} 