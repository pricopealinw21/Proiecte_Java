package com.breakout;

public class Brick {
    private int x;
    private int y;
    private int width;
    private int height;
    private boolean destroyed;
    private BonusType bonusType;
    
    public enum BonusType {
        NONE,
        MULTI_BALL,
        DESTROY_ROW
    }
    
    public Brick(int x, int y, int width, int height, BonusType bonusType) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.destroyed = false;
        this.bonusType = bonusType;
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
    
    public boolean isDestroyed() {
        return destroyed;
    }
    
    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }
    
    public BonusType getBonusType() {
        return bonusType;
    }
    
    public boolean hasBonus() {
        return bonusType != BonusType.NONE;
    }
}
