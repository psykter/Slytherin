package com.example.slytherin;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Slytherin Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new GameBoard());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}