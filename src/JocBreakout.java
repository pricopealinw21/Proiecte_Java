package com.breakout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Clasa principala a jocului Breakout
 * @author Student Universitar
 * @version 1.0
 * Proiect realizat pentru cursul de Programare Concurrenta
 */
public class JocBreakout extends JPanel implements KeyListener {

    private static final int LATIME_FEREASTRA = 800;
    private static final int INALTIME_FEREASTRA = 600;
    private static final int LATIME_PALETA = 100;
    private static final int INALTIME_PALETA = 10;
    private static final int DIMENSIUNE_MINGE = 10;
    private static final int LATIME_CARAMIDA = 75;
    private static final int INALTIME_CARAMIDA = 20;
    private static final int RANDURI_CARAMIDA = 5;
    private static final int COLOANE_CARAMIDA = 10;
    
    // Elementele principale ale jocului
    private Paleta paleta;
    private List<Minge> mingi;
    private List<Caramida> caramizi;
    
    // Thread-uri pentru miscare independenta
    private Thread threadPaleta;
    private Thread threadMinge;
    
    // Starea jocului si generator de numere aleatorii
    private boolean jocActiv = true;
    private Random generatorAleatoriu = new Random();
    
    /**
     * Constructorul jocului - initializeaza elementele si thread-urile
     */
    public JocBreakout() {
        initializeazaJoc();
        configureazaThreaduri();
    }
    

    private void initializeazaJoc() {
        // Configurare fereastra joc
        setPreferredSize(new Dimension(LATIME_FEREASTRA, INALTIME_FEREASTRA));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        
        // Initializare paleta (platforma mobila orizontala)
        paleta = new Paleta(LATIME_FEREASTRA / 2 - LATIME_PALETA / 2, 
                           INALTIME_FEREASTRA - 50, 
                           LATIME_PALETA, 
                           INALTIME_PALETA);
        
        // Initializare lista de mingi
        mingi = new ArrayList<>();
        Minge mingeInitiala = new Minge(LATIME_FEREASTRA / 2 - DIMENSIUNE_MINGE / 2, 
                                      paleta.getY() - DIMENSIUNE_MINGE, 
                                      DIMENSIUNE_MINGE, 
                                      DIMENSIUNE_MINGE);
        mingi.add(mingeInitiala);
        
        // Initializare perete de caramizi cu bonusuri aleatorii
        caramizi = new ArrayList<>();
        for (int rand = 0; rand < RANDURI_CARAMIDA; rand++) {
            for (int coloana = 0; coloana < COLOANE_CARAMIDA; coloana++) {
                int x = coloana * (LATIME_CARAMIDA + 5) + 35;
                int y = rand * (INALTIME_CARAMIDA + 5) + 50;
                
                // 20% sansa pentru caramida cu bonus
                boolean areBonus = generatorAleatoriu.nextDouble() < 0.2;
                Caramida.TipBonus tipBonus = Caramida.TipBonus.FARA_BONUS;
                if (areBonus) {
                    tipBonus = generatorAleatoriu.nextBoolean() ? 
                              Caramida.TipBonus.MINGE_MULTIPLA : 
                              Caramida.TipBonus.DISTRUGE_RAND;
                }
                
                caramizi.add(new Caramida(x, y, LATIME_CARAMIDA, INALTIME_CARAMIDA, tipBonus));
            }
        }
    }
    
    /**
     * Configureaza si porneste thread-urile pentru miscare independenta
     * Thread-ul pentru paleta si thread-ul pentru mingi ruleaza simultan
     */
    private void configureazaThreaduri() {
        // Thread pentru miscarea paletei (platforma mobila)
        threadPaleta = new Thread(() -> {
            while (jocActiv) {
                paleta.actualizeazaPozitie();
                try {
                    Thread.sleep(16); // ~60 FPS pentru miscare fluida
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        
        // Thread pentru miscarea mingilor si coliziuni
        threadMinge = new Thread(() -> {
            while (jocActiv) {
                actualizeazaMingi();
                verificaColiziuni();
                try {
                    Thread.sleep(16); // ~60 FPS pentru fizica precisa
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        
        // Pornim ambele thread-uri pentru miscare simultana
        threadPaleta.start();
        threadMinge.start();
    }
    

    private void actualizeazaMingi() {
        List<Minge> mingiDeEliminat = new ArrayList<>();
        
        for (Minge minge : mingi) {
            minge.actualizeazaPozitie();
            
            // Eliminam mingile care cad sub ecran
            if (minge.getY() > INALTIME_FEREASTRA) {
                mingiDeEliminat.add(minge);
            }
        }
        
        // Eliminam mingile iesite din joc
        mingi.removeAll(mingiDeEliminat);
        
        // Game Over daca nu mai sunt mingi in joc
        if (mingi.isEmpty()) {
            jocActiv = false;
        }
    }
    
    /**
     * Verifica toate coliziunile: minge-paleta, minge-caramida, minge-pereti
     * Implementeaza fizica de ricoșare conform cerintelor proiectului
     */
    private void verificaColiziuni() {
        List<Caramida> caramiziDeEliminat = new ArrayList<>();
        
        for (Minge minge : mingi) {
            // Coliziune minge-paleta (platforma mobila)
            if (minge.intersecteazaCu(paleta)) {
                minge.ricoșeazaDePaleta(paleta);
            }
            
            // Coliziune minge-caramida
            for (Caramida caramida : caramizi) {
                if (!caramida.esteDistrusa() && minge.intersecteazaCu(caramida)) {
                    minge.ricoșeazaDeCaramida(caramida);
                    
                    // Activam bonusurile daca exista
                    if (caramida.areBonus()) {
                        activeazaBonus(caramida.getTipBonus(), minge);
                    }
                    
                    // Eliminam caramida daca nu e de tip DISTRUGE_RAND
                    if (caramida.getTipBonus() != Caramida.TipBonus.DISTRUGE_RAND) {
                        caramida.setDistrusa(true);
                        caramiziDeEliminat.add(caramida);
                    }
                }
            }
            
            // Coliziune minge-peretii laterali si superior
            if (minge.getX() <= 0 || minge.getX() >= LATIME_FEREASTRA - minge.getLatime()) {
                minge.inverseazaVitezaX();
            }
            if (minge.getY() <= 0) {
                minge.inverseazaVitezaY();
            }
        }
        
        // Eliminam caramizile distruse
        caramizi.removeAll(caramiziDeEliminat);
        
        // Verificam conditia de victorie (toate caramizile distruse)
        if (caramizi.isEmpty()) {
            jocActiv = false;
        }
    }
    
    /**
     * Activeaza bonusurile in functie de tipul caramidei lovite
     * @param tipBonus - tipul bonusului de activat
     * @param minge - minge care a lovit caramida cu bonus
     */
    private void activeazaBonus(Caramida.TipBonus tipBonus, Minge minge) {
        switch (tipBonus) {
            case MINGE_MULTIPLA:
                // Cream 2 mingi suplimentare cu viteze usor diferite
                for (int i = 0; i < 2; i++) {
                    Minge mingeNoua = new Minge(
                        minge.getX() + generatorAleatoriu.nextInt(20) - 10,
                        minge.getY(),
                        DIMENSIUNE_MINGE,
                        DIMENSIUNE_MINGE
                    );
                    mingeNoua.seteazaViteza(
                        minge.getVitezaX() + generatorAleatoriu.nextInt(4) - 2,
                        minge.getVitezaY()
                    );
                    mingi.add(mingeNoua);
                }
                break;
            case DISTRUGE_RAND:
                // Distrugem intregul rand de caramizi
                final int yRand;
                for (Caramida caramida : caramizi) {
                    if (Math.abs(caramida.getY() - minge.getY()) < INALTIME_CARAMIDA) {
                        yRand = caramida.getY();
                        caramizi.removeIf(c -> Math.abs(c.getY() - yRand) < INALTIME_CARAMIDA / 2);
                        break;
                    }
                }
                break;
        }
    }
    
    /**
     * Metoda de desenare a tuturor elementelor jocului
     * Foloseste Graphics2D pentru randare anti-aliasing
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Desenam paleta (platforma mobila) in albastru
        g2d.setColor(Color.BLUE);
        g2d.fillRect(paleta.getX(), paleta.getY(), paleta.getLatime(), paleta.getInaltime());
        
        // Desenam toate mingile in alb
        g2d.setColor(Color.WHITE);
        for (Minge minge : mingi) {
            g2d.fillOval(minge.getX(), minge.getY(), minge.getLatime(), minge.getInaltime());
        }
        
        // Desenam caramizile cu culori diferite in functie de bonus
        for (Caramida caramida : caramizi) {
            if (!caramida.esteDistrusa()) {
                switch (caramida.getTipBonus()) {
                    case MINGE_MULTIPLA:
                        g2d.setColor(Color.RED);      // Rosu pentru bonus minge multipla
                        break;
                    case DISTRUGE_RAND:
                        g2d.setColor(Color.GREEN);     // Verde pentru bonus distruge rand
                        break;
                    default:
                        g2d.setColor(Color.ORANGE);    // Portocaliu pentru caramizi normale
                }
                g2d.fillRect(caramida.getX(), caramida.getY(), caramida.getLatime(), caramida.getInaltime());
            }
        }
        
        // Afisam mesajul de final (Game Over sau Ai Castigat!)
        if (!jocActiv) {
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 36));
            String mesaj = mingi.isEmpty() ? "Joc Terminat!" : "Ai Castigat!";
            FontMetrics fm = g2d.getFontMetrics();
            int x = (LATIME_FEREASTRA - fm.stringWidth(mesaj)) / 2;
            int y = INALTIME_FEREASTRA / 2;
            g2d.drawString(mesaj, x, y);
        }
    }
    
    /**
     * Gestioneaza apasarea tastelor pentru controlul paletei
     * @param e - evenimentul de tasta apasata
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            paleta.seteazaMiscareStanga(true);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            paleta.seteazaMiscareDreapta(true);
        }
    }
    
    /**
     * Gestioneaza eliberarea tastelor
     * @param e - evenimentul de tasta eliberata
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            paleta.seteazaMiscareStanga(false);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            paleta.seteazaMiscareDreapta(false);
        }
    }
    
    /**
     * Metoda necesara pentru implementarea KeyListener (neutilizata)
     * @param e - eveniment de tasta tastata
     */
    @Override
    public void keyTyped(KeyEvent e) {}
    
    /**
     * Metoda principala - punctul de intrare in aplicatie
     * Creeaza fereastra jocului si porneste bucla principala de randare
     * @param args - argumente linie de comanda (neutilizate)
     */
    public static void main(String[] args) {
        // Creare fereastra principala a jocului
        JFrame fereastra = new JFrame("Joc Breakout - Student Universitar");
        JocBreakout joc = new JocBreakout();
        
        // Configurare fereastra
        fereastra.add(joc);
        fereastra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fereastra.setResizable(false);
        fereastra.pack();
        fereastra.setLocationRelativeTo(null); // Centrare pe ecran
        fereastra.setVisible(true);
        
        // Bucla principala de randare (thread-ul UI)
        while (joc.jocActiv) {
            joc.repaint(); // Redesenare ecran la fiecare frame
            try {
                Thread.sleep(16); // ~60 FPS pentru animatie fluida
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
