package com.breakout;


public class Caramida {
    private int x;
    private int y;
    private int latime;
    private int inaltime;
    private boolean distrusa;
    private TipBonus tipBonus;
    

    public enum TipBonus {
        FARA_BONUS,
        MINGE_MULTIPLA,
        DISTRUGE_RAND
    }
    
    /**

     * @param x - pozitia pe axa X
     * @param y - pozitia pe axa Y
     * @param latime - latimea caramizii
     * @param inaltime - inaltimea caramizii
     * @param tipBonus - tipul de bonus asociat caramizii
     */
    public Caramida(int x, int y, int latime, int inaltime, TipBonus tipBonus) {
        this.x = x;
        this.y = y;
        this.latime = latime;
        this.inaltime = inaltime;
        this.distrusa = false;
        this.tipBonus = tipBonus;
    }
    

    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getLatime() {
        return latime;
    }
    
    public int getInaltime() {
        return inaltime;
    }
    

    
    public boolean esteDistrusa() {
        return distrusa;
    }
    
    public void setDistrusa(boolean distrusa) {
        this.distrusa = distrusa;
    }
    

    
    public TipBonus getTipBonus() {
        return tipBonus;
    }
    


    public boolean areBonus() {
        return tipBonus != TipBonus.FARA_BONUS;
    }
}
