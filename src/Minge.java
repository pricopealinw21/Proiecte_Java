package com.breakout;

/**
 * Clasa care reprezinta mingea din jocul Breakout
 * Implementeaza fizica de ricoșare conform cerintelor proiectului
 * @author Student Universitar
 */
public class Minge {
    private int x;
    private int y;
    private int latime;
    private int inaltime;
    private int vitezaX;
    private int vitezaY;
    private static final int VITEZA_INITIALA = 4;
    
    /**
     * Constructor pentru initializarea mingei
     * @param x - pozitia initiala pe axa X
     * @param y - pozitia initiala pe axa Y
     * @param latime - latimea mingei (diametru)
     * @param inaltime - inaltimea mingei (diametru)
     */
    public Minge(int x, int y, int latime, int inaltime) {
        this.x = x;
        this.y = y;
        this.latime = latime;
        this.inaltime = inaltime;
        this.vitezaX = VITEZA_INITIALA;
        this.vitezaY = -VITEZA_INITIALA; // Incepe miscarea in sus
    }
    
    /**
     * Actualizeaza pozitia mingei in functie de viteza curenta
     */
    public void actualizeazaPozitie() {
        x += vitezaX;
        y += vitezaY;
    }
    
    /**
     * Calculeaza ricoșarea mingii de paleta conform fizicii reale
     * Unghiul de ricoșare depinde de pozitia de impact pe paleta
     * @param paleta - paleta de care ricoșează mingea
     */
    public void ricoșeazaDePaleta(Paleta paleta) {
        // Calculam pozitia relativa a impactului (0.0 = margine stanga, 1.0 = margine dreapta)
        double pozitieImpact = (double)(x + latime/2 - paleta.getX()) / paleta.getLatime();
        
        // Ne asiguram ca valoarea este intre 0.0 si 1.0
        pozitieImpact = Math.max(0.0, Math.min(1.0, pozitieImpact));
        
        // Calculam unghiul de ricoșare (-60° la 60°)
        double unghi = (pozitieImpact - 0.5) * Math.PI / 3; // -60° to 60°
        
        // Setam noua viteza in functie de unghi
        int viteza = (int)Math.sqrt(vitezaX * vitezaX + vitezaY * vitezaY);
        vitezaX = (int)(viteza * Math.sin(unghi));
        vitezaY = -(int)(Math.abs(viteza * Math.cos(unghi)));
        
        // Asiguram ca mingea este deasupra paletei
        y = paleta.getY() - inaltime;
    }
    
    /**
     * Calculeaza ricoșarea mingii de o caramida
     * Determina latura lovita si inverseaza viteza corespunzatoare
     * @param caramida - caramida de care ricoșează mingea
     */
    public void ricoșeazaDeCaramida(Caramida caramida) {
        // Determinam centrul mingii si caramizii
        double centruMingeX = x + latime / 2.0;
        double centruMingeY = y + inaltime / 2.0;
        double centruCaramidaX = caramida.getX() + caramida.getLatime() / 2.0;
        double centruCaramidaY = caramida.getY() + caramida.getInaltime() / 2.0;
        
        double dx = centruMingeX - centruCaramidaX;
        double dy = centruMingeY - centruCaramidaY;
        
        double latimeTotala = (caramida.getLatime() + this.latime) / 2.0;
        double inaltimeTotala = (caramida.getInaltime() + this.inaltime) / 2.0;
        
        double crossWidth = latimeTotala * dy;
        double crossHeight = inaltimeTotala * dx;
        
        if (Math.abs(crossWidth) > Math.abs(crossHeight)) {
            // Lovitura din partea de sus sau de jos
            if (crossWidth > 0) {
                // Lovitura de sus
                y = caramida.getY() - this.inaltime;
                vitezaY = -Math.abs(vitezaY);
            } else {
                // Lovitura de jos
                y = caramida.getY() + caramida.getInaltime();
                vitezaY = Math.abs(vitezaY);
            }
        } else {
            // Lovitura din partea stanga sau dreapta
            if (crossHeight > 0) {
                // Lovitura din stanga
                x = caramida.getX() - this.latime;
                vitezaX = -Math.abs(vitezaX);
            } else {
                // Lovitura din dreapta
                x = caramida.getX() + caramida.getLatime();
                vitezaX = Math.abs(vitezaX);
            }
        }
    }
    
    /**
     * Verifica daca mingea intersecteaza paleta
     * @param paleta - paleta de verificat
     * @return true daca exista intersectie
     */
    public boolean intersecteazaCu(Paleta paleta) {
        return x < paleta.getX() + paleta.getLatime() &&
               x + latime > paleta.getX() &&
               y < paleta.getY() + paleta.getInaltime() &&
               y + inaltime > paleta.getY();
    }
    
    /**
     * Verifica daca mingea intersecteaza o caramida
     * @param caramida - caramida de verificat
     * @return true daca exista intersectie
     */
    public boolean intersecteazaCu(Caramida caramida) {
        return x < caramida.getX() + caramida.getLatime() &&
               x + latime > caramida.getX() &&
               y < caramida.getY() + caramida.getInaltime() &&
               y + inaltime > caramida.getY();
    }
    
    // Metode pentru controlul vitezei
    
    public void inverseazaVitezaX() {
        vitezaX = -vitezaX;
    }
    
    public void inverseazaVitezaY() {
        vitezaY = -vitezaY;
    }
    
    // Gettere
    
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
    
    public int getVitezaX() {
        return vitezaX;
    }
    
    public int getVitezaY() {
        return vitezaY;
    }
    
    /**
     * Seteaza viteza mingei
     * @param vitezaX - componenta X a vitezei
     * @param vitezaY - componenta Y a vitezei
     */
    public void seteazaViteza(int vitezaX, int vitezaY) {
        this.vitezaX = vitezaX;
        this.vitezaY = vitezaY;
    }
}
