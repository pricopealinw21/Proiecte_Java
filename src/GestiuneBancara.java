public class GestiuneBancara {
    static int totalPersoane = 0;

    public static void main(String[] args) {
        System.out.println("== START SIMULARE BANCA ==\n");

        Flexibil cont1 = new Flexibil("BT111", "Andrei Nistor", "Str. Garii 2");
        Fix cont2 = new Fix("BT222", "Elena Radu", "Str. Mare 45");

        System.out.println("Clienti totali: " + totalPersoane + "\n");

        System.out.println("== ACTIUNI CONT FLEXIBIL ==");
        cont1.puneBani(1500);
        cont1.scoateBani(300);
        System.out.println("Bani ramasi: " + cont1.verificareSold() + " EUR");
        System.out.println("Rata dobanda: " + cont1.rata() + "%");

        System.out.println("\n== ACTIUNI CONT FIX ==");
        cont2.puneBani(4000);
        cont2.scoateBani(500);
        System.out.println("Bani ramasi: " + cont2.verificareSold() + " EUR");
        System.out.println("Rata dobanda: " + cont2.rata() + "%");

        System.out.println("\n== ACTUALIZARE SCADENTA ==");
        cont1.finalDeLuna();
        cont2.finalDeLuna();

        System.out.println("\n== REZULTATE FINALE ==");
        System.out.println("Sold Andrei: " + cont1.verificareSold() + " EUR");
        System.out.println("Sold Elena: " + cont2.verificareSold() + " EUR");
        System.out.println("Numar final clienti: " + totalPersoane);
    }
}

class Cont {
    String idCont;
    String tip;
    String detinator;
    String adresa;
    double bani;
    double procent;

    public Cont(String idCont, String tip, String detinator, String adresa) {
        this.idCont = idCont;
        this.tip = tip;
        this.detinator = detinator;
        this.adresa = adresa;
        this.bani = 0;
        this.procent = 0;
        GestiuneBancara.totalPersoane++;
    }

    public void puneBani(double suma) {
        if (suma > 0) {
            this.bani += suma;
            System.out.println("Incasare: " + suma + " EUR. Total: " + this.bani);
        }
    }

    public void scoateBani(double suma) {
        if (suma > 0 && this.bani >= suma) {
            this.bani -= suma;
            System.out.println("Retragere: " + suma + " EUR. Total: " + this.bani);
        } else {
            System.out.println("Nu se poate efectua retragerea!");
        }
    }

    public double verificareSold() {
        return bani;
    }

    public double rata() {
        return procent;
    }

    public void finalDeLuna() {
        double profit = this.bani * (this.procent / 100);
        this.bani += profit;
        System.out.println("Profit adaugat (" + detinator + "): " + profit + " EUR");
    }
}

class Flexibil extends Cont {
    public Flexibil(String id, String nume, String adr) {
        super(id, "flexibil", nume, adr);
        this.procent = 0.5;
    }
}

class Fix extends Cont {
    boolean restrictionat;

    public Fix(String id, String nume, String adr) {
        super(id, "fix", nume, adr);
        this.procent = 3.5;
        this.restrictionat = true;
    }

    @Override
    public void scoateBani(double suma) {
        if (restrictionat) {
            System.out.println("Eroare: Banii sunt blocati in depozit!");
        } else {
            super.scoateBani(suma);
        }
    }

    @Override
    public void finalDeLuna() {
        super.finalDeLuna();
        this.restrictionat = false;
        System.out.println("Depozitul lui " + detinator + " a fost deblocat.");
    }
}