package utils;

import java.awt.Color;
import java.awt.Font;

/**
 * 项目常量类
 * 集中管理项目中使用的常量值
 */
public class Constants {
    
    // 数据库相关常量
    public static final int MAX_BORROW_LIMIT = 3; // 最大借阅数量
    public static final String DEFAULT_FONT = "微软雅黑";
    
    // 颜色常量
    public static final Color PRIMARY_COLOR = new Color(33, 150, 243);
    public static final Color SUCCESS_COLOR = new Color(76, 175, 80);
    public static final Color WARNING_COLOR = new Color(255, 193, 7);
    public static final Color DANGER_COLOR = new Color(244, 67, 54);
    public static final Color TEXT_SECONDARY = new Color(120, 144, 156);
    public static final Color TEXT_PRIMARY = new Color(66, 66, 66);
    public static final Color BACKGROUND_COLOR = new Color(245, 250, 255);
    public static final Color BORDER_COLOR = new Color(220, 230, 240);
    
    // 字体常量
    public static final Font FONT_NORMAL = new Font(DEFAULT_FONT, Font.PLAIN, 14);
    public static final Font FONT_BOLD = new Font(DEFAULT_FONT, Font.BOLD, 16);
    public static final Font FONT_TITLE = new Font(DEFAULT_FONT, Font.BOLD, 18);
    public static final Font FONT_LARGE = new Font(DEFAULT_FONT, Font.BOLD, 20);
    
    // 尺寸常量
    public static final int BUTTON_HEIGHT = 40;
    public static final int BUTTON_WIDTH = 100;
    public static final int DIALOG_WIDTH = 400;
    public static final int DIALOG_HEIGHT = 300;
    
    // 消息常量
    public static final String MSG_BOOK_NOT_FOUND = "未找到相关书籍";
    public static final String MSG_BOOK_FOUND = "找到 %d 本相关书籍";
    public static final String MSG_BORROW_LIMIT = "您已达到借阅上限！";
    public static final String MSG_BORROW_SUCCESS = "借阅成功";
    public static final String MSG_BORROW_FAILED = "借阅失败";
    public static final String MSG_DELETE_CONFIRM = "此操作不可撤销，删除后将无法恢复。";
    
    // 私有构造函数防止实例化
    private Constants() {}
} 