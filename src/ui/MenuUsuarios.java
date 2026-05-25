package ui;

import java.util.Scanner;
import utils.UtilsMenu;

public class MenuUsuarios {
    private final Scanner scanner;

    public MenuUsuarios(Scanner scanner) {
        this.scanner = scanner;
    }

    public void iniciar() {
        boolean volver = false;
        while (!volver) {
            mostrar();
            int opcion = UtilsMenu.leerOpcion(scanner);
            switch (opcion) {
                case 1:
                    // Lógica para listar usuarios
                    break;
                case 2:
                    // Lógica para crear usuario
                    break;
                case 3:
                    // Lógica para editar usuario
                    break;
                case 4:
                    // Lógica para eliminar usuario
                    break;
                case 0:
                    volver = true;
                    System.out.println("Volviendo al menú principal...");
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, intente nuevamente.");
            }
        }
    }

    public void mostrar() {
        System.out.println("=====================================");
        System.out.println("||       GESTIÓN DE USUARIOS       ||");
        System.out.println("=====================================");
        System.out.println("|| 1. Listar usuarios              ||");
        System.out.println("|| 2. Crear usuario                ||");
        System.out.println("|| 3. Editar usuario               ||");
        System.out.println("|| 4. Eliminar usuario             ||");
        System.out.println("|| 0. Volver al menú principal     ||");
        System.out.println("=====================================");
        System.out.print("Seleccione una opción: ");
    }
}
