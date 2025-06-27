package gui;

import gui.components.iLabel;
import gui.components.iWindow;
import utils.u;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.security.PublicKey;


public class Sys {
    public Sys() {
        iWindow w = new iWindow("欢迎", 800, 800, true);
        w.setLayout(new BorderLayout());

        // 登录/注册/帮助菜单的监听器
        ActionListener menuListener = e -> {
            String cmd = e.getActionCommand();
            if ("root".equals(cmd)) {
                w.setContentPane(new gui.views.RootView());
                w.setJMenuBar(null); // 可选：登录后隐藏菜单栏
                w.revalidate();
            } else if ("user".equals(cmd)) {
                w.setContentPane(new gui.views.UserView());
                w.setJMenuBar(null);
                w.revalidate();
            }
        };

        w.setWelcomeMenuBar(menuListener);

        iLabel welcomeImage = new iLabel(
                "BookSystem",
                "car-rental.png",
                200, 200,
                iLabel.VERTICAL
        );
        welcomeImage.setFont(new Font("微软雅黑", Font.BOLD, 20));
        welcomeImage.setCenter();
        w.add(welcomeImage, BorderLayout.CENTER);
        w.done();
    }


    public static void main(String[] args) {
        new Sys();
    }
}
