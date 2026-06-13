package utils;
import java.util.Scanner;

public class UtilsGeneral {

    // METODOS PARA LEER ENTRADAS
    public static String leerString(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim();
            if (input != null && !input.isEmpty()) {
                return input;
            }
            System.out.print("Entrada no valida. Por favor, ingrese un texto: ");
        }
    }

    public static Double leerDouble(Scanner scanner) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.print("Entrada no valida. Ingrese un numero decimal: ");
            }
        }
    }

    public static int leerEntero(Scanner scanner) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Entrada no valida. Ingrese un numero entero: ");
            }
        }
    }

    public static Long leerLong(Scanner scanner) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                return Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.print("Entrada no valida. Ingrese un numero entero: ");
            }
        }
    }

    public static Boolean leerBooleano(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("s")) return true;
            if (input.equals("n")) return false;
            System.out.print("Entrada no valida. Ingrese s o n: ");
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
        }
    }

    public static String leerEmail(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim();
            if (input != null && !input.isEmpty() && input.contains("@")) {
                return input;
            }
            System.out.print("Entrada no válida. Por favor, ingrese un email válido (debe contener '@'): ");
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
