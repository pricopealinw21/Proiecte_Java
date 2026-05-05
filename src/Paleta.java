package com.breakout;


public class Paleta {
    private int x;
    private int y;
    private int latime;
    private int inaltime;
    private boolean seMiscaStanga = false;
    private boolean seMiscaDreapta = false;
    private static final int VITEZA = 8;
    private static final int LATIME_FEREASTRA = 800;
    
    /**
     * Constructor pentru initializarea paletei
     * @param x - pozitia initiala pe axa X
     * @param y - pozitia pe axa Y (fixa, in partea de jos a ecranului)
     * @param latime - latimea paletei
     * @param inaltime - inaltimea paletei
     */
    public Paleta(int x, int y, int latime, int inaltime) {
        this.x = x;
        this.y = y;
        this.latime = latime;
        this.inaltime = inaltime;
    }

    public void actualizeazaPozitie() {
        if (seMiscaStanga && x > 0) {
            x -= VITEZA;
        }
        if (seMiscaDreapta && x < LATIME_FEREASTRA - latime) {
            x += VITEZA;
        }
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
    

    
    public void seteazaMiscareStanga(boolean seMiscaStanga) {
        this.seMiscaStanga = seMiscaStanga;
    }
    
    public void seteazaMiscareDreapta(boolean seMiscaDreapta) {
        this.seMiscaDreapta = seMiscaDreapta;
    }
}
