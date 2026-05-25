package utils;

import java.util.Scanner;

public class UtilsMenu {

    public static int leerOpcion(Scanner scanner) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Entrada no válida. Por favor, ingrese un número: ");
            }
        }
    }}
