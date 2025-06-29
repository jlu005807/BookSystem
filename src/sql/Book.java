package sql;

/**
 * 图书实体类
 */
public class Book {
    private int id;
    private String isbn;
    private String title;
    private String author;
    private int numLeft;
    private int numAll;
    private java.sql.Date addTime;

    public Book() {}

    public Book(int id, String isbn, String title, String author, int numLeft, int numAll, java.sql.Date addTime) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.numLeft = numLeft;
        this.numAll = numAll;
        this.addTime = addTime;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public int getNumLeft() { return numLeft; }
    public void setNumLeft(int numLeft) { this.numLeft = numLeft; }
    public int getNumAll() { return numAll; }
    public void setNumAll(int numAll) { this.numAll = numAll; }
    public java.sql.Date getAddTime() { return addTime; }
    public void setAddTime(java.sql.Date addTime) { this.addTime = addTime; }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", numLeft=" + numLeft +
                ", numAll=" + numAll +
                ", addTime=" + addTime +
                '}';
    }
} 