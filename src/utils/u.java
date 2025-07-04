package utils;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * 通用工具类，提供图片加载、文件检查、日志记录等功能
 */
public class u {
    /**
     * 获取图片图标
     * @param name 图片名称（不含扩展名）
     * @return ImageIcon 图片图标
     */
    public static ImageIcon getImageIcon(String name) {
        String path;
        if (fileExist("resource/icon/" + name + ".png"))
            path = "resource/icon/" + name + ".png";
        else if (fileExist("resource/img/" + name))
            path = "resource/img/" + name;
        else {
            path = "src/resource/img/404.png";
            Logger.warn("UTIL", "图片文件不存在", name + "，使用默认404图片");
        }
        return new ImageIcon(path);
    }

    /**
     * 获取指定尺寸的图片图标
     * @param name 图片名称
     * @param width 宽度
     * @param height 高度
     * @return ImageIcon 缩放后的图片图标
     */
    public static ImageIcon getImageIcon(String name, int width, int height) {
        return new ImageIcon(getImageIcon(name).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }

    /**
     * 检查文件是否存在
     * @param path 文件路径
     * @return boolean 文件是否存在
     */
    public static boolean fileExist(String path) {
        return new File(path).exists();
    }

    /**
     * 记录一般日志信息
     * @param msg 日志消息
     */
    public static void log(String msg) {
        Logger.info("UTIL", "日志", msg);
    }

    /**
     * 记录错误日志信息
     * @param msg 错误消息
     */
    public static void err(String msg) {
        Logger.error("UTIL", "错误", msg);
    }
}
