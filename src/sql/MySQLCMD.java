package sql;

import utils.u;
import utils.Logger;

import java.sql.*;

/**
 * MySQLCMD 数据库通用操作类，封装了常用的增删改查方法
 */
public class MySQLCMD {
    /**
     * 通用查询方法
     * @param con 数据库连接
     * @param table 表名
     * @param columns 查询字段
     * @param where 查询条件
     * @return String[] 查询结果数组
     */
    public static String[] retrieve(Connection con, String table, String[] columns, String where) {
        StringBuilder sql = new StringBuilder("SELECT ");
        for (String column : columns) {
            sql.append(column).append(",");
        }
        sql = new StringBuilder(sql.substring(0, sql.length() - 1) + " FROM " + table);
        if (where != null) sql.append(" WHERE ").append(where);
        
        Logger.debug("DB", "查询", "执行查询SQL: " + sql.toString());
        
        try {
            PreparedStatement pstm = con.prepareStatement(sql.toString());
            ResultSet rs = pstm.executeQuery();
            String[] result = new String[columns.length];
            while (rs.next()) {
                for (int i = 0; i < columns.length; i++) {
                    result[i] = rs.getString(columns[i]);
                }
            }
            if (result[0] == null) {
                Logger.logDatabaseOperation("查询", table, "无结果");
                return null;
            }
            Logger.logDatabaseOperation("查询", table, "成功，返回" + result.length + "个字段");
            return result;
        } catch (SQLException e) {
            Logger.error("查询数据失败: " + e.getMessage(), e);
            Logger.logDatabaseOperation("查询", table, "失败: " + e.getSQLState());
            u.err("查询数据失败");
            throw new RuntimeException(e);
        }
    }

    /**
     * 通用插入方法
     * @param con 数据库连接
     * @param table 表名
     * @param columns 字段名数组
     * @param values 字段值数组
     * @return String 插入结果（success/异常）
     */
    public static String insert(Connection con, String table, String[] columns, String[] values) {
        StringBuilder sql = new StringBuilder("INSERT INTO " + table + "(");
        for (String column : columns) {
            sql.append(column).append(",");
        }
        sql = new StringBuilder(sql.substring(0, sql.length() - 1) + ") VALUES(");
        for (String value : values) {
            sql.append("'").append(value).append("',");
        }
        sql = new StringBuilder(sql.substring(0, sql.length() - 1) + ")");
        
        Logger.debug("DB", "插入", "执行插入SQL: " + sql.toString());
        
        try {
            PreparedStatement pstm = con.prepareStatement(sql.toString());
            pstm.execute();
            Logger.logDatabaseOperation("插入", table, "成功");
            return "success";
        } catch (SQLException e) {
            Logger.error("插入数据失败: " + e.getMessage(), e);
            Logger.logDatabaseOperation("插入", table, "失败: " + e.getSQLState());
            u.err("插入数据失败");
            throw new RuntimeException(e);
        }
    }

    /**
     * 通用更新方法
     * @param con 数据库连接
     * @param table 表名
     * @param columns 字段名数组
     * @param values 字段值数组
     * @param where 更新条件
     * @return String 更新结果
     */
    public static String update(Connection con, String table, String[] columns, String[] values, String where) {
        StringBuilder sql = new StringBuilder("UPDATE " + table + " SET ");
        for (int i = 0; i < columns.length; i++) {
            sql.append(columns[i]).append(" = '").append(values[i]).append("',");
        }
        sql = new StringBuilder(sql.substring(0, sql.length() - 1) + " WHERE " + where);
        
        Logger.debug("DB", "更新", "执行更新SQL: " + sql.toString());
        
        try {
            PreparedStatement pstm = con.prepareStatement(sql.toString());
            int affectedRows = pstm.executeUpdate();
            Logger.logDatabaseOperation("更新", table, "成功，影响行数: " + affectedRows);
            return "success";
        } catch (SQLException e) {
            Logger.error("更新数据失败: " + e.getMessage(), e);
            Logger.logDatabaseOperation("更新", table, "失败: " + e.getSQLState());
            u.err("更新数据失败");
            throw new RuntimeException(e);
        }
    }

    /**
     * 通用删除方法
     * @param con 数据库连接
     * @param table 表名
     * @param where 删除条件
     * @return String 删除结果
     */
    public static String delete(Connection con, String table, String where) {
        String sql = "DELETE FROM " + table + " WHERE " + where;
        
        Logger.debug("DB", "删除", "执行删除SQL: " + sql);
        
        try {
            PreparedStatement pstm = con.prepareStatement(sql);
            int affectedRows = pstm.executeUpdate();
            Logger.logDatabaseOperation("删除", table, "成功，影响行数: " + affectedRows);
            return "success";
        } catch (SQLException e) {
            Logger.error("删除数据失败: " + e.getMessage(), e);
            Logger.logDatabaseOperation("删除", table, "失败: " + e.getSQLState());
            u.err("删除数据失败");
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取表的最大ID
     * @param con 数据库连接
     * @param table 表名
     * @return int 最大ID
     */
    public static int getMaxId(Connection con, String table) {
        String sql = "SELECT MAX(id) FROM " + table;
        
        Logger.debug("DB", "获取最大ID", "执行获取最大ID SQL: " + sql);
        
        try {
            PreparedStatement pstm = con.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                int maxId = rs.getInt(1);
                Logger.debug("DB", "获取最大ID", "表 " + table + " 的最大ID: " + maxId);
                return maxId;
            }
        } catch (SQLException e) {
            Logger.error("获取最大ID失败: " + e.getMessage(), e);
            u.err("获取最大ID失败");
            throw new RuntimeException(e);
        }
        return 0;
    }
}

