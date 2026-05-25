package utils;
import java.util.Scanner;


public class UtilsGeneral {
    private final Scanner scanner = new Scanner(System.in);

    public static String leerString(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.print("Entrada no válida. Por favor, ingrese un texto: ");
        }
    }

    public static void esperarEnter(Scanner scanner) {
        System.out.println();
        System.out.print("Presione Enter para continuar...");
        scanner.nextLine();
    }
}
