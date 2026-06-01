package utils;

import java.util.List;
import java.util.Scanner;

public class UtilsGeneral {
    private final Scanner scanner = new Scanner(System.in);

    // METODOS PARA LEER ENTRADAS
    public static String leerString(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim();
            if (input != null && !input.isEmpty()) {
                return input;
            }
            System.out.print("Entrada no válida. Por favor, ingrese un texto: ");
        }
    }

    public static Long leerId(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim();
            if (input != null && !input.isEmpty()) {
                try {
                    return Long.parseLong(input);
                } catch (NumberFormatException e) {
                    System.out.print("Entrada no válida. Por favor, ingrese un número de ID válido: ");
                }
            }
            System.out.print("Entrada no válida. Por favor, ingrese un número de ID válido: ");
        }
    }

    public static boolean tieneValor(String s) {
        return s != null && !s.trim().isEmpty();
    }

    public static void esperarEnter(Scanner scanner) {
        System.out.println();
        System.out.print("Presione Enter para continuar...");
        scanner.nextLine();
    }

    public static void tituloSubcategoria(String titulo) {
        String mostrarTitulo = "|| Usted selecciono: " + titulo + " ||";
        String lineas = "=".repeat(mostrarTitulo.length());
        System.out.println(lineas);
        System.out.println(mostrarTitulo);
        System.out.println(lineas);
    }
}
