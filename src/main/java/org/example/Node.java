package org.example;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

public class Node extends JButton implements ActionListener {
    
    Node parent;
    int col;
    int row;
    
    int fCost;
    int gCost;
    int hCost;
    
    boolean isStart;
    boolean isGoal;
    
    boolean isOpen;
    boolean isChecked;
    boolean isWall;
    
    boolean isPath;
    
    
    public Node(int col, int row){
    
        this.col = col;
        this.row = row;
        
        setBackground(Color.white);
        setForeground(Color.black);
        setFocusable(false);
        addActionListener(this);
    }
    
    public void setAsStart(){
        setBackground(Color.blue);
        setForeground(Color.white);
        setText("Start");
        isStart = true;
    }
    
    public void setAsGoal(){
        setBackground(Color.yellow);
        setForeground(Color.black);
        setText("Goal");
        isGoal = true;
    }
    
    public void setAsWall(){
        setBackground(Color.black);
        setForeground(Color.black);
        isWall = true;
    }
    
    public void setAsOpen(){
        if(isStart == false && isGoal == false && isChecked == false){
            setBackground(Color.LIGHT_GRAY);
            setForeground(Color.black);
        }
        isOpen = true;
    }
    
    public void setAsChecked(){
        if(isStart == false && isGoal == false){
            setBackground(Color.orange);
            setForeground(Color.black);
        }
        isChecked = true;
    }
    
    public void setAsPath(){
        setBackground(Color.magenta);
        setForeground(Color.white);
        isPath = true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setBackground(Color.orange);
    }
    
    
}
