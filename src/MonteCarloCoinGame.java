import java.util.Random;
import java.util.Scanner;

public class MonteCarloCoinGame {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        System.out.print("Introduceți numărul total de aruncări (n): ");
        int n = scanner.nextInt();

        int scorJucator1 = 0;
        int scorJucator2 = 0;
        boolean rândulJucatorului1 = true;

        for (int i = 0; i < n; i++) {
            int rezultatAruncare = random.nextInt(2);
            int alegereGhicitor = random.nextInt(2);

            if (rândulJucatorului1) {
                if (alegereGhicitor == rezultatAruncare) {
                    scorJucator2++;
                    rândulJucatorului1 = false;
                } else {
                    scorJucator1++;
                }
            } else {
                if (alegereGhicitor == rezultatAruncare) {
                    scorJucator1++;
                    rândulJucatorului1 = true;
                } else {
                    scorJucator2++;
                }
            }
        }

        double procentJ1 = (double) scorJucator1 / n * 100;
        double procentJ2 = (double) scorJucator2 / n * 100;

        System.out.println("\n--- Rezultate Finale ---");
        System.out.println("Scor Jucător 1: " + scorJucator1 + " (" + String.format("%.2f", procentJ1) + "%)");
        System.out.println("Scor Jucător 2: " + scorJucator2 + " (" + String.format("%.2f", procentJ2) + "%)");

        scanner.close();
    }
}