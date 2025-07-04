package utils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日志模块，结构化输出，便于人工和工具查看
 * 日志格式：时间 | 级别 | 分类 | 主题 | 内容
 */
public class Logger {
    private static final String LOG_DIR = "logs";
    private static final String LOG_FILE_PREFIX = "bookSystem";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat FILE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    
    private static boolean isInitialized = false;
    private static PrintWriter logWriter;
    private static String currentLogFileDate = "";
    private static boolean isClosed = false;
    private static final Object lock = new Object();
    
    /**
     * 日志级别枚举
     */
    public enum Level {
        DEBUG, INFO, WARN, ERROR
    }
    
    /**
     * 初始化日志系统
     */
    public static void init() {
        synchronized (lock) {
            if (isInitialized) return;
            
            try {
                File logDir = new File(LOG_DIR);
                if (!logDir.exists()) logDir.mkdirs();
                switchLogFile(FILE_DATE_FORMAT.format(new Date()));
                isInitialized = true;
                isClosed = false;
                info("SYSTEM", "启动", "日志系统初始化成功");
                info("SYSTEM", "启动", "系统启动时间: " + DATE_FORMAT.format(new Date()));
                info("SYSTEM", "启动", "Java版本: " + System.getProperty("java.version"));
                info("SYSTEM", "启动", "操作系统: " + System.getProperty("os.name") + " " + System.getProperty("os.version"));
                
            } catch (IOException e) {
                isInitialized = false;
                logWriter = null;
                System.err.println("日志系统初始化失败: " + e.getMessage());
            }
        }
    }
    
    /**
     * 切换日志文件到指定日期
     */
    private static void switchLogFile(String date) throws IOException {
        if (logWriter != null) logWriter.close();
        String fileName = LOG_FILE_PREFIX + "_" + date + ".log";
        File logFile = new File(LOG_DIR, fileName);
        logWriter = new PrintWriter(new FileWriter(logFile, true), true);
        currentLogFileDate = date;
    }
    
    /**
     * 记录调试信息
     */
    public static void debug(String category, String action, String message) {
        log(Level.DEBUG, category, action, message);
    }
    
    /**
     * 记录一般信息
     */
    public static void info(String category, String action, String message) {
        log(Level.INFO, category, action, message);
    }
    
    /**
     * 记录警告信息
     */
    public static void warn(String category, String action, String message) {
        log(Level.WARN, category, action, message);
    }
    
    /**
     * 记录错误信息
     */
    public static void error(String category, String action, String message) {
        log(Level.ERROR, category, action, message);
    }
    
    /**
     * 记录异常信息
     */
    public static void error(String category, String action, String message, Throwable throwable) {
        error(category, action, message);
        if (throwable != null) {
            error(category, action, "异常详情: " + throwable.getMessage());
            for (String line : getStackTraceLines(throwable)) {
                error(category, action, "    " + line);
            }
        }
    }
    
    /**
     * 记录数据库连接状态
     */
    public static void logDatabaseConnection(String status, String details) {
        info("DB", "连接", status + " - " + details);
    }
    
    /**
     * 记录数据库操作
     */
    public static void logDatabaseOperation(String operation, String table, String result) {
        info("DB", operation, "表:" + table + " 结果:" + result);
    }
    
    /**
     * 记录用户操作
     */
    public static void logUserAction(String username, String action, String details) {
        info("USER", action, "用户:" + username + " 详情:" + details);
    }
    
    /**
     * 兼容单参数重载，默认分类GENERAL、主题-（横杠）
     */
    public static void debug(String message) { log(Level.DEBUG, "GENERAL", "-", message); }
    public static void info(String message) { log(Level.INFO, "GENERAL", "-", message); }
    public static void warn(String message) { log(Level.WARN, "GENERAL", "-", message); }
    public static void error(String message) { log(Level.ERROR, "GENERAL", "-", message); }
    public static void error(String message, Throwable throwable) {
        error("GENERAL", "-", message, throwable);
    }
    
    /**
     * 核心日志记录方法，线程安全，自动切换日志文件
     */
    private static void log(Level level, String category, String action, String message) {
        synchronized (lock) {
            if (isClosed) {
                System.err.println("[Logger] 日志系统已关闭，无法写入日志: " + message);
                return;
            }
            if (!isInitialized) {
                init();
            }
            
            // 检查日期是否变化，自动切换日志文件
            String today = FILE_DATE_FORMAT.format(new Date());
            if (!today.equals(currentLogFileDate)) {
                try {
                    switchLogFile(today);
                    info("SYSTEM", "切换", "日志文件已切换: " + LOG_FILE_PREFIX + "_" + today + ".log");
                } catch (IOException e) {
                    System.err.println("切换日志文件失败: " + e.getMessage());
                    logWriter = null;
                }
            }
            
            String timestamp = DATE_FORMAT.format(new Date());
            String logEntry = String.format("%s | %-5s | %-8s | %-6s | %s", timestamp, level.name(), category, action, message);
            
            // 输出到控制台
            System.out.println(logEntry);
            
            // 写入日志文件
            if (logWriter != null) {
                logWriter.println(logEntry);
                logWriter.flush();
            } else {
                // 日志文件不可用时，输出到控制台
                System.err.println("[Logger] 日志文件不可用: " + logEntry);
            }
        }
    }
    
    /**
     * 获取异常堆栈信息的每一行
     */
    private static String[] getStackTraceLines(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString().split("\n");
    }
    
    /**
     * 关闭日志系统，关闭后禁止写入
     */
    public static void close() {
        synchronized (lock) {
            if (logWriter != null) {
                info("SYSTEM", "关闭", "日志系统关闭");
                logWriter.close();
                logWriter = null;
            }
            isClosed = true;
            isInitialized = false;
        }
    }
    
    /**
     * 获取日志文件路径
     */
    public static String getLogFilePath() {
        String today = FILE_DATE_FORMAT.format(new Date());
        String fileName = LOG_FILE_PREFIX + "_" + today + ".log";
        return LOG_DIR + File.separator + fileName;
    }
    
    /**
     * 清理旧日志文件（保留最近7天的日志）
     */
    public static void cleanOldLogs() {
        try {
            File logDir = new File(LOG_DIR);
            if (!logDir.exists()) return;
            
            File[] logFiles = logDir.listFiles((dir, name) -> name.startsWith(LOG_FILE_PREFIX) && name.endsWith(".log"));
            if (logFiles == null) return;
            
            long sevenDaysAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000L);
            
            for (File file : logFiles) {
                if (file.lastModified() < sevenDaysAgo) {
                    if (file.delete()) {
                        info("SYSTEM", "清理", "清理旧日志文件: " + file.getName());
                    }
                }
            }
        } catch (Exception e) {
            error("SYSTEM", "清理", "清理旧日志文件失败", e);
        }
    }
} 