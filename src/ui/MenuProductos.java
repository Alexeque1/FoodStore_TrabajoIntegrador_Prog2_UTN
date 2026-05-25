package ui;

import java.util.Scanner;
import utils.UtilsMenu;

public class MenuProductos {
    private final Scanner scanner;

    public MenuProductos(Scanner scanner) {
        this.scanner = scanner;
    }

    public void iniciar() {
        boolean volver = false;
        while (!volver) {
            mostrar();
            int opcion = UtilsMenu.leerOpcion(scanner);
            switch (opcion) {
                case 1:
                    // Lógica para listar productos
                    break;
                case 2:
                    // Lógica para crear producto
                    break;
                case 3:
                    // Lógica para editar producto
                    break;
                case 4:
                    // Lógica para eliminar producto
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
        System.out.println("||       GESTIÓN DE PRODUCTOS      ||");
        System.out.println("=====================================");
        System.out.println("|| 1. Listar productos             ||");
        System.out.println("|| 2. Crear producto               ||");
        System.out.println("|| 3. Editar producto              ||");
        System.out.println("|| 4. Eliminar producto            ||");
        System.out.println("|| 0. Volver al menú principal     ||");
        System.out.println("=====================================");
        System.out.print("Seleccione una opción: ");
    }
}
