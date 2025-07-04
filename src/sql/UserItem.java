package sql;

import utils.u;

import java.sql.*;

/**
 * 用户数据模型，对应user表
 */
public class UserItem {
    public int id; // 用户ID
    public String username = null, password; // 用户名、密码
    public Timestamp registerTime; // 注册时间
    public String comment = null; // 备注

    /**
     * 构造方法
     * @param id 用户ID
     * @param username 用户名
     * @param password 密码
     * @param registerTime 注册时间
     * @param comment 备注
     */
    public UserItem(int id, String username, String password, Timestamp registerTime, String comment) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.registerTime = registerTime;
        this.comment = comment;
    }

    /**
     * 打印用户信息，调试用
     */
    public void print() {
        u.log("id: " + id + "\n" +
                "username: " + username + "\n" +
                "password: " + password + "\n" +
                "registerTime: " + registerTime + "\n" +
                "comment: " + comment
        );
    }
}
