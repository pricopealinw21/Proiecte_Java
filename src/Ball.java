package com.breakout;

public class Ball {
    private int x;
    private int y;
    private int width;
    private int height;
    private int velocityX;
    private int velocityY;
    private static final int INITIAL_SPEED = 4;
    
    public Ball(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.velocityX = INITIAL_SPEED;
        this.velocityY = -INITIAL_SPEED;
    }
    
    public void update() {
        x += velocityX;
        y += velocityY;
    }
    
    public void bounceOffPaddle(Paddle paddle) {
        // Calculate where the ball hit the paddle (relative position 0.0 to 1.0)
        double hitPos = (double)(x + width/2 - paddle.getX()) / paddle.getWidth();
        
        // Ensure hitPos is between 0.0 and 1.0
        hitPos = Math.max(0.0, Math.min(1.0, hitPos));
        
        // Calculate angle based on hit position (-60 to 60 degrees)
        double angle = (hitPos - 0.5) * Math.PI / 3; // -60° to 60°
        
        // Set new velocity based on angle
        int speed = (int)Math.sqrt(velocityX * velocityX + velocityY * velocityY);
        velocityX = (int)(speed * Math.sin(angle));
        velocityY = -(int)(Math.abs(speed * Math.cos(angle)));
        
        // Ensure ball is above paddle
        y = paddle.getY() - height;
    }
    
    public void bounceOffBrick(Brick brick) {
        // Determine which side of the brick was hit
        double ballCenterX = x + width / 2.0;
        double ballCenterY = y + height / 2.0;
        double brickCenterX = brick.getX() + brick.getWidth() / 2.0;
        double brickCenterY = brick.getY() + brick.getHeight() / 2.0;
        
        double dx = ballCenterX - brickCenterX;
        double dy = ballCenterY - brickCenterY;
        
        double width = (brick.getWidth() + this.width) / 2.0;
        double height = (brick.getHeight() + this.height) / 2.0;
        
        double crossWidth = width * dy;
        double crossHeight = height * dx;
        
        if (Math.abs(crossWidth) > Math.abs(crossHeight)) {
            // Hit was from top or bottom
            if (crossWidth > 0) {
                // Hit from top
                y = brick.getY() - this.height;
                velocityY = -Math.abs(velocityY);
            } else {
                // Hit from bottom
                y = brick.getY() + brick.getHeight();
                velocityY = Math.abs(velocityY);
            }
        } else {
            // Hit was from left or right
            if (crossHeight > 0) {
                // Hit from left
                x = brick.getX() - this.width;
                velocityX = -Math.abs(velocityX);
            } else {
                // Hit from right
                x = brick.getX() + brick.getWidth();
                velocityX = Math.abs(velocityX);
            }
        }
    }
    
    public boolean intersects(Paddle paddle) {
        return x < paddle.getX() + paddle.getWidth() &&
               x + width > paddle.getX() &&
               y < paddle.getY() + paddle.getHeight() &&
               y + height > paddle.getY();
    }
    
    public boolean intersects(Brick brick) {
        return x < brick.getX() + brick.getWidth() &&
               x + width > brick.getX() &&
               y < brick.getY() + brick.getHeight() &&
               y + height > brick.getY();
    }
    
    public void reverseXVelocity() {
        velocityX = -velocityX;
    }
    
    public void reverseYVelocity() {
        velocityY = -velocityY;
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
    
    public int getVelocityX() {
        return velocityX;
    }
    
    public int getVelocityY() {
        return velocityY;
    }
    
    public void setVelocity(int velocityX, int velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }
}
