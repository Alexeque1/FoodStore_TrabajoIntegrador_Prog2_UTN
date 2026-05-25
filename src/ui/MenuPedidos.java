package ui;

import java.util.Scanner;
import utils.UtilsMenu;

public class MenuPedidos {
    private final Scanner scanner;

    public MenuPedidos(Scanner scanner) {
        this.scanner = scanner;
    }

    public void iniciar() {
        boolean salir = false;
        while (!salir) {
            mostrar();
            int opcion = UtilsMenu.leerOpcion(scanner);
            switch (opcion) {
                case 1:
                    // Lógica para listar pedidos
                    break;
                case 2:
                    // Lógica para crear pedido con detalles
                    break;
                case 3:
                    // Lógica para actualizar estado / forma de pago del pedido
                    break;
                case 4:
                    // Lógica para eliminar pedido
                    break;
                case 0:
                    salir = true;
                    System.out.println("Volviendo al menu principal...");
                    break;
                default:
                    System.out.println("Opcion no valida. Intente nuevamente.");
            }
        }
    }

    public void mostrar() {
        System.out.println("==============================================");
        System.out.println("||        GESTION DE PEDIDOS                ||");
        System.out.println("==============================================");
        System.out.println("|| 1. Listar pedidos                        ||");
        System.out.println("|| 2. Crear pedido con detalles             ||");
        System.out.println("|| 3. Actualizar estado / forma de pago     ||");
        System.out.println("|| 4. Eliminar pedido                       ||");
        System.out.println("|| 0. Volver al menu principal              ||");
        System.out.println("==============================================");
        System.out.print("Seleccione una opcion: ");
    }
}
