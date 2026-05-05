package com.breakout;

public class Paddle {
    private int x;
    private int y;
    private int width;
    private int height;
    private boolean movingLeft = false;
    private boolean movingRight = false;
    private static final int SPEED = 8;
    private static final int WINDOW_WIDTH = 800;
    
    public Paddle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    public void update() {
        if (movingLeft && x > 0) {
            x -= SPEED;
        }
        if (movingRight && x < WINDOW_WIDTH - width) {
            x += SPEED;
        }
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }
    
    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }
}
