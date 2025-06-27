package sql;

import java.sql.Connection;

public class ConnectionPool {
    static MySQLConfig c;
    static Connection conn = null;

    public static void init(MySQLConfig c) {
        ConnectionPool.c = c;
        if (conn == null) {
            conn = MySQLConnection.connect(c);
        }
    }

    public static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = MySQLConnection.connect(c);
            }
        } catch (Exception e) {
            conn = MySQLConnection.connect(c);
        }
        return conn;
    }
}
