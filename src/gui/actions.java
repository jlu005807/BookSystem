package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static gui.components.iDialog.*;

public class actions {
    public static void exitSys() {
        if (dialogConfirm(null, "警告", "您真的要退出吗？", "close")) {
            System.exit(0);
        }
    }

    public static void logoutSys(JFrame currentFrame) {
        if (showLogoutDialog(currentFrame)) {
            // 先移除所有WindowListener，防止弹出退出系统对话框
            for (java.awt.event.WindowListener wl : currentFrame.getWindowListeners()) {
                currentFrame.removeWindowListener(wl);
            }
            currentFrame.dispose();
            // 注销后回到初始欢迎界面
            new gui.Sys();
        }
    }

    public static void actionPopup(Component component, ActionListener listener, int id, String cmd) {
        listener.actionPerformed(new ActionEvent(component, id, cmd));
    }
}
