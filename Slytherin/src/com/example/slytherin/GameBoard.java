package com.example.slytherin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameBoard extends JPanel implements Runnable {

    private final int B_WIDTH = 300;
    private final int B_HEIGHT = 300;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    private final int RAND_POS = 29;
    private final int DELAY = 1000 / 60; // 60 FPS

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private int dots;
    private int apple_x;
    private int apple_y;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Image ball;
    private Image apple;
    private Image head;

    public GameBoard() {
        setFocusable(true);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));

        MyKeyListener keyListener = new MyKeyListener();
        addKeyListener(keyListener);
        loadImages();
        initGame();

        Thread gameThread = new Thread(this);
        gameThread.start();
    }

    private void initGame() {
        dots = 5;

        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }

        locateApple();
    }

    private void locateApple() {
        int r = (int) (Math.random() * RAND_POS);
        apple_x = r * DOT_SIZE;
        r = (int) (Math.random() * RAND_POS);
        apple_y = r * DOT_SIZE;
    }

    private void loadImages() {
        ImageIcon iid = new ImageIcon(getClass().getResource("/resources/dot.png"));
        ball = iid.getImage();

        ImageIcon iia = new ImageIcon(getClass().getResource("/resources/apple.png"));
        apple = iia.getImage();

        ImageIcon iih = new ImageIcon(getClass().getResource("/resources/head.png"));
        head = iih.getImage();
    }

    @Override
    public void run() {
        long previousTime = System.nanoTime();
        double nsPerUpdate = 1000000000.0 / DELAY;

        double delta = 0;

        while (inGame) {
            long currentTime = System.nanoTime();
            delta += (currentTime - previousTime) / nsPerUpdate;
            previousTime = currentTime;

            while (delta >= 1) {
                update();
                delta--;
            }

            repaint();
        }
    }

    private void update() {
        // Move the snake's body segments
        for (int z = dots; z > 0; z--) {
            x[z] = x[z - 1];
            y[z] = y[z - 1];
        }

        // Update the snake's head position based on the direction
        if (leftDirection) {
            x[0] -= DOT_SIZE;
        } else if (rightDirection) {
            x[0] += DOT_SIZE;
        } else if (upDirection) {
            y[0] -= DOT_SIZE;
        } else if (downDirection) {
            y[0] += DOT_SIZE;
        }

        // Check for collisions and update the game state
        checkSnakeFoodCollision();
        checkSnakeWallCollision();
        checkSnakeSelfCollision();
    }

    private void checkSnakeFoodCollision() {
        Rectangle snakeHeadRect = new Rectangle(x[0], y[0], DOT_SIZE, DOT_SIZE);
        Rectangle foodRect = new Rectangle(apple_x, apple_y, DOT_SIZE, DOT_SIZE);

        if (snakeHeadRect.intersects(foodRect)) {
            dots++;
            locateApple();
            // Update the score
        }
    }

    private void checkSnakeWallCollision() {
        if (x[0] < 0 || x[0] >= B_WIDTH || y[0] < 0 || y[0] >= B_HEIGHT) {
            inGame = false;
        }
    }

    private void checkSnakeSelfCollision() {
        for (int z = dots; z > 0; z--) {
            if ((z > 3) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.clearRect(0, 0, B_WIDTH, B_HEIGHT);
        render(g);
    }

    private void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        if (inGame) {
            // Draw the snake
            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g2d.drawImage(head, x[z], y[z], this);
                } else {
                    g2d.drawImage(ball, x[z], y[z], this);
                }
            }

            // Draw the food
            g2d.drawImage(apple, apple_x, apple_y, this);

            // Draw the game board (optional)
            // You can draw the grid lines or other elements of the game board if desired
        } else {
            // Draw the game over message
            String message = "Game Over";
            Font font = new Font("Helvetica", Font.BOLD, 14);
            g2d.setColor(Color.WHITE);
            g2d.setFont(font);
            g2d.drawString(message, B_WIDTH / 2 - 40, B_HEIGHT / 2);
        }
    }

    private class MyKeyListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }
}