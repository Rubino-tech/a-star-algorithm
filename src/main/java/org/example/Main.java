package org.example;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        // make a new JFrame window and set it up
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(false);
        
        MainPanel mainPanel = new MainPanel();

        window.add(mainPanel);              // the 'MainPanel' class must 'extends JPanel' in order to work
        
        mainPanel.startMainThread();
        
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

    }


}
