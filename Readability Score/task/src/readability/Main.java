package readability;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws IOException {

        try (Scanner sc = new Scanner(new File(args[0]))) { // Otwieranie pliku
            System.out.println("Tekst zawiera: ");
            while (sc.hasNext()) {
                System.out.println(sc.nextLine() + " ");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Plik nie znaleziony!");
        }
        String tekst = Files.readString(Path.of(args[0])); // Zapisywanie tresci pliku do Stringu
        int iloscSlow = liczSlowa(tekst);
        int zdanie = liczZdania(tekst);
        int literki = liczLiterki(tekst);
        int sylaby = Sylaby(tekst);
        int wieloSylabowe = wieloSylabowe(tekst);
        double ARI = ARI(literki, iloscSlow, zdanie);
        double FKrt = FKrt(iloscSlow, zdanie, sylaby);
        double SMOG = SMOG(wieloSylabowe, zdanie);
        double CL = CLi(literki, zdanie, iloscSlow);

        System.out.println("Wyrazy: " + iloscSlow);
        System.out.println("Zdania: " + zdanie);
        System.out.println("Literki: " + literki);
        System.out.println("Sylaby: " + sylaby);
        System.out.println("Wielosylabowe wyrazy: " + wieloSylabowe);
        System.out.println("Wpisz wynik ktory chcesz obliczyc (ARI, FK, SMOG, CL, all): ");

        Scanner scanner = new Scanner(System.in);
        String pytanie = scanner.nextLine();
        switch (pytanie) {
            case "ARI" -> {
                System.out.printf("Automated Readability Index: %.2f", ARI);
                System.out.println(Wiek(ARI));
            }
            case "FK" -> {
                System.out.printf("Flesch–Kincaid readability tests: %.2f", FKrt);
                System.out.println(Wiek(FKrt));
            }
            case "SMOG" -> {
                System.out.printf("Simple Measure of Gobbledygook: %.2f", SMOG);
                System.out.println(Wiek(SMOG));
            }
            case "CL"-> {
                System.out.printf("Coleman-Liau index: %.2f", CL);
                System.out.println(Wiek(CL));
            }
            case "all" -> {
                System.out.printf("Automated Readability Index: %.2f", ARI);
                System.out.println(Wiek(ARI));
                System.out.printf("Flesch–Kincaid readability tests: %.2f", FKrt);
                System.out.println(Wiek(FKrt));
                System.out.printf("Simple Measure of Gobbledygook: %.2f", SMOG);
                System.out.println(Wiek(SMOG));
                System.out.printf("Coleman-Liau index: %.2f", CL);
                System.out.println(Wiek(CL));
            }
        }


    }

    public static int liczSlowa(String tekst) { // Splitowanie " " w celu obliczenia ilosci wyrazow
        return tekst.split(" ").length;
    }

    public static int liczZdania(String tekst) { // Szukanie zdań które kończą się na "!.?" Z uzyciem regexu
        return tekst.split("[^0-9][!.?][\" \"]").length;
    }

    public static int liczLiterki(String tekst) { // Liczenie zawartosci tekstu z usunięciem whitespacu
        return tekst.replaceAll(" ", "").length();
    }

    public static int Sylaby(String tekst) { // Szuka sylab
        Pattern pattern = Pattern.compile("[ayeiou]+"); // Wzor do szukania a || y || e || ... || u
        String malaLitera = tekst.toLowerCase();
        Matcher matcher = pattern.matcher(malaLitera); // Szuka w zawartosci tekstu
        int licz = 0;
        while (matcher.find()) {  // Skanuje tekst w poszukiwaniu kolejnego ciągu ktory spełnia wzorzec
            licz++;
            if (malaLitera.endsWith("e")) // Jezeli konczy sie na 'e' to odejmuje
                licz--;
        }
        return licz < 0 ? 1 : licz;
    }

    public static int wieloSylabowe(String tekst) { // Szuka wyrazow wielosylabowych
        int wielosylabowe = 0;
        String[] wyrazy = tekst.split(" ");
        for (String wyraz : wyrazy) {
            String regex = "(?i)[aiou][aeiou]*|e[aeiou]*(?!d?\\\\b)"; // wzor ktory spelnia warunki
            Matcher m = Pattern.compile(regex).matcher(wyraz);
            int licz = 0;
            while (m.find()) { // jak wyzej
                licz++;
            }
            if (licz > 2) {
                wielosylabowe += 1;
            }
        }
        return wielosylabowe;
    }

    public static String Wiek(double wynik) {
        switch ((int) wynik) {
            case 1 -> {
                return (" (Około 6 lat).");
            }
            case 2 -> {
                return (" (Około 7 lat).");
            }
            case 3 -> {
                return (" (Około 9 lat).");
            }
            case 4 -> {
                return (" (Około 10 lat).");
            }
            case 5 -> {
                return (" (Około 11 lat).");
            }
            case 6 -> {
                return (" (Około 12 lat).");
            }
            case 7 -> {
                return (" (Około 13 lat).");
            }
            case 8 -> {
                return (" (Około 14 lat).");
            }
            case 9 -> {
                return (" (Około 15 lat).");
            }
            case 10 -> {
                return (" (Około 16 lat).");
            }
            case 11 -> {
                return (" (Około 17 lat).");
            }
            case 12 -> {
                return (" (Około 18 lat).");
            }
            default -> {
                return (" (Około 22 lata).");
            }
        }
    }

    public static double ARI(int literki, int iloscSlow, int zdanie) {
        return 4.71 * (double) literki / iloscSlow + 0.5 * (double) iloscSlow / zdanie - 21.43;
    } // Liczy Automated readability index - link https://en.wikipedia.org/wiki/Automated_readability_index

    public static double FKrt(int wyrazy, int zdania, int sylaby) {
        return (0.39 * (double) wyrazy / zdania + 11.8 * (double) sylaby / wyrazy - 15.59);
    } // liczy Flesch–Kincaid readability tests - link https://en.wikipedia.org/wiki/Flesch–Kincaid_readability_tests

    public static double SMOG(int wielosylabowe, int zdania) {
        return 1.043 * Math.sqrt(wielosylabowe * (30.0 / (double) zdania)) + 3.1291;
    } // liczy SMOG grade - link https://en.wikipedia.org/wiki/SMOG

    public static double CLi(int litery, int zdania, int wyrazy) {
        return 0.0588 * ((double) litery / wyrazy) * 100.0 - 0.296 * ((double) zdania / wyrazy) * 100.0 - 15.8;
    } // liczy Coleman–Liau index - link https://en.wikipedia.org/wiki/Coleman–Liau_index
}
