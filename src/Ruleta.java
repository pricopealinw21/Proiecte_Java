import java.util.Random;

public class Ruleta {
    public static void main(String[] args) {
        int sumaInitiala = 100;
        int numarRulaje = 1000;
        double castigMediu = simuleazaJoc(sumaInitiala, numarRulaje);
        System.out.println("Castig mediu: " + castigMediu);
    }

    public static double simuleazaJoc(int sumaInitiala, int numarSimulari) {
        Random random = new Random();
        double sumaTotala = 0;
        for (int i = 0; i < numarSimulari; i++) {
            sumaTotala += joacaRuleta(sumaInitiala, random);
        }
        return sumaTotala / numarSimulari;
    }

    public static int joacaRuleta(int sumaInitiala, Random random) {
        int suma = sumaInitiala;
        while (suma > 0) {
            int alegereRuleta = random.nextInt(36);
            int tipPariu = random.nextInt(2);
            if (tipPariu == 0) {
                int paritateJucator = random.nextInt(2);
                if (alegereRuleta == 0) {
                    suma -= 1;
                } else if ((alegereRuleta % 2 == 0 && paritateJucator == 0) || (alegereRuleta % 2 == 1 && paritateJucator == 1)) {
                    suma += 1;
                } else {
                    suma -= 1;
                }
            } else {
                int numarAles = random.nextInt(36);
                if (alegereRuleta == numarAles) {
                    suma += 35;
                } else {
                    suma -= 1;
                }
            }
        }
        return suma;
    }
}