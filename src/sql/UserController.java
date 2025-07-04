package sql;

import utils.u;
import utils.Logger;
import sql.UserItem;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 用户控制器，负责用户相关的数据库操作和业务逻辑
 */
public class UserController {
    /**
     * 根据用户ID查询用户信息
     * @param id 用户ID
     * @return UserItem 用户对象，查无此人返回null
     */
    public static UserItem search(int id) {
        Logger.debug("USER", "查询", "查询用户ID: " + id);
        String[] result = MySQLCMD.retrieve(
                ConnectionPool.getConnection(),
                "user",
                new String[]{"id", "username", "password", "registerTime", "comment"},
                "id = " + id
        );
        if (result == null) {
            Logger.warn("USER", "查询", "未找到用户ID: " + id);
            return null;
        }
        u.log("查询结果：" + Arrays.toString(result));
        Timestamp regTime = null;
        try {
            regTime = Timestamp.valueOf(result[3]);
        } catch (Exception e) {
            // 兼容只有日期无时间的情况
            try {
                regTime = result[3] != null ? Timestamp.valueOf(result[3] + " 00:00:00") : null;
            } catch (Exception ignore) {}
        }
        UserItem user = new UserItem(
                Integer.parseInt(result[0]),
                result[1],
                result[2],
                regTime,
                result[4]
        );
        Logger.debug("USER", "查询", "成功查询用户: " + user.username);
        return user;
    }

    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return UserItem 用户对象，查无此人返回null
     */
    public static UserItem search(String username) {
        Logger.debug("USER", "查询", "查询用户名: " + username);
        String[] result = MySQLCMD.retrieve(
                ConnectionPool.getConnection(),
                "user",
                new String[]{"id", "username", "password", "registerTime", "comment"},
                "username = '" + username + "'"
        );
        if (result == null) {
            Logger.warn("USER", "查询", "未找到用户名: " + username);
            return null;
        }
        u.log("查询结果：" + Arrays.toString(result));
        java.sql.Timestamp regTime = null;
        try {
            regTime = java.sql.Timestamp.valueOf(result[3]);
        } catch (Exception e) {
            // 兼容只有日期无时间的情况
            try {
                regTime = result[3] != null ? java.sql.Timestamp.valueOf(result[3] + " 00:00:00") : null;
            } catch (Exception ignore) {}
        }
        UserItem user = new UserItem(
                Integer.parseInt(result[0]),
                result[1],
                result[2],
                regTime,
                result[4]
        );
        Logger.debug("USER", "查询", "成功查询用户: " + user.username);
        return user;
    }

    /**
     * 校验用户名和密码是否匹配
     * @param username 用户名
     * @param password 密码
     * @return boolean 是否存在该用户
     */
    public static boolean check(String username, String password) {
        Logger.debug("USER", "校验", "校验用户登录: " + username);
        String[] result = MySQLCMD.retrieve(
                ConnectionPool.getConnection(),
                "user",
                new String[]{"id", "username", "password", "registerTime", "comment"},
                "username = '" + username + "' AND password = '" + password + "'"
        );
        if (result == null) {
            Logger.warn("USER", "校验", "用户登录失败: " + username + " (用户名或密码错误)");
            return false;
        }
        u.log("查询结果：" + Arrays.toString(result));
        Logger.logUserAction(username, "登录", "登录成功");
        return true;
    }

    /**
     * 获取所有用户信息
     * @return UserItem[] 用户对象数组
     */
    public static UserItem[] getAll() {
        Logger.debug("USER", "查询", "获取所有用户信息");
        ArrayList<UserItem> data = new ArrayList<>();
        int maxID = MySQLCMD.getMaxId(ConnectionPool.getConnection(), "user");
        for (int i = 1; i <= maxID; i++) {
            UserItem item = search(i);
            if (item != null) data.add(item);
        }
        Logger.info("USER", "查询", "成功获取 " + data.size() + " 个用户信息");
        return data.toArray(new UserItem[0]);
    }

    /**
     * 创建新用户，自动写入注册时间
     * @param item 用户对象（用户名、密码、备注）
     * @return String 插入结果（success/失败原因）
     */
    public static String create(UserItem item) {
        Logger.logUserAction(item.username, "注册", "开始注册新用户");
        
        // 检查用户是否已存在
        if (search(item.username) != null) {
            Logger.warn("USER", "注册", "用户注册失败: " + item.username + " (用户已存在)");
            return "用户已存在";
        }
        
        // 获取当前时间
        java.sql.Timestamp now = new java.sql.Timestamp(System.currentTimeMillis());
        String[] values = {
                item.username,
                item.password,
                now.toString(),
                item.comment
        };
        String result = MySQLCMD.insert(
                ConnectionPool.getConnection(),
                "user",
                new String[]{"username", "password", "registerTime", "comment"},
                values
        );
        
        if ("success".equals(result)) {
            Logger.logUserAction(item.username, "注册", "注册成功");
        } else {
            Logger.error("USER", "注册", "用户注册失败: " + item.username + " - " + result);
        }
        
        return result;
    }

    /**
     * 更新指定ID的用户信息
     * @param newItem 新用户对象
     * @param id 用户ID
     * @return String 更新结果
     */
    public static String update(UserItem newItem, int id) {
        Logger.logUserAction(newItem.username, "更新信息", "更新用户ID: " + id);
        String[] values = {
                String.valueOf(newItem.id),
                newItem.username,
                newItem.password,
                String.valueOf(newItem.registerTime),
                newItem.comment
        };
        String result = MySQLCMD.update(
                ConnectionPool.getConnection(),
                "user",
                new String[]{"id", "username", "password", "registerTime", "comment"},
                values,
                "id = " + id
        );
        
        if ("success".equals(result)) {
            Logger.logUserAction(newItem.username, "更新信息", "更新成功");
        } else {
            Logger.error("USER", "更新", "用户信息更新失败: " + newItem.username + " - " + result);
        }
        
        return result;
    }

    /**
     * 修改指定ID用户的密码
     * @param newPassword 新密码
     * @param id 用户ID
     * @return String 更新结果
     */
    public static String changePassword(String newPassword, int id) {
        Logger.logUserAction("用户ID:" + id, "修改密码", "开始修改密码");
        String result = MySQLCMD.update(
                ConnectionPool.getConnection(),
                "user",
                new String[]{"password"},
                new String[]{newPassword},
                "id = " + id
        );
        
        if ("success".equals(result)) {
            Logger.logUserAction("用户ID:" + id, "修改密码", "密码修改成功");
        } else {
            Logger.error("USER", "修改密码", "密码修改失败: 用户ID " + id + " - " + result);
        }
        
        return result;
    }

    /**
     * 删除指定ID用户
     * @param id 用户ID
     * @return String 删除结果
     */
    public static String delete(int id) {
        Logger.logUserAction("用户ID:" + id, "删除用户", "开始删除用户");
        String result = MySQLCMD.delete(
                ConnectionPool.getConnection(),
                "user",
                "id = " + id
        );
        
        if ("success".equals(result)) {
            Logger.logUserAction("用户ID:" + id, "删除用户", "用户删除成功");
        } else {
            Logger.error("USER", "删除", "用户删除失败: 用户ID " + id + " - " + result);
        }
        
        return result;
    }

    /**
     * 登录校验并返回用户对象
     * @param username 用户名
     * @param password 密码
     * @return UserItem 用户对象，查无此人返回null
     */
    public static UserItem checkAndGetUser(String username, String password) {
        Logger.debug("USER", "登录校验", "登录校验并获取用户信息: " + username);
        String[] result = MySQLCMD.retrieve(
                ConnectionPool.getConnection(),
                "user",
                new String[]{"id", "username", "password", "registerTime", "comment"},
                "username = '" + username + "' AND password = '" + password + "'"
        );
        if (result == null) {
            Logger.warn("USER", "登录校验", "登录校验失败: " + username + " (用户名或密码错误)");
            return null;
        }
        u.log("查询结果：" + Arrays.toString(result));
        java.sql.Timestamp regTime = null;
        try {
            regTime = java.sql.Timestamp.valueOf(result[3]);
        } catch (Exception e) {
            try {
                regTime = result[3] != null ? java.sql.Timestamp.valueOf(result[3] + " 00:00:00") : null;
            } catch (Exception ignore) {}
        }
        UserItem user = new UserItem(
                Integer.parseInt(result[0]),
                result[1],
                result[2],
                regTime,
                result[4]
        );
        Logger.logUserAction(username, "登录", "登录成功，用户ID: " + user.id);
        return user;
    }
}
