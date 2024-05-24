package org.example;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(false);
        
        MainPanel mainPanel = new MainPanel();

        window.add(mainPanel);
        
        mainPanel.startMainThread();
        
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

    }


}
