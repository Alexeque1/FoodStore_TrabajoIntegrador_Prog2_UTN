package ui;
import java.util.Scanner;
import services.CategoriaServices;
import utils.UtilsMenu;
import utils.UtilsGeneral;

public class MenuPrincipal {
    private final Scanner scanner = new Scanner(System.in);
    private final CategoriaServices categoriaServices = new CategoriaServices();

    private final MenuCategoria menuCategoria;
    private final MenuProductos menuProductos;
    private final MenuUsuarios menuUsuarios;
    private final MenuPedidos menuPedidos;

    public MenuPrincipal() {
        this.menuCategoria = new MenuCategoria(scanner, categoriaServices);
        this.menuProductos = new MenuProductos(scanner);
        this.menuUsuarios = new MenuUsuarios(scanner);
        this.menuPedidos = new MenuPedidos(scanner);
    }

    public void iniciar() {
        boolean salir = false;
        while (!salir) {
            mostrar();
            int opcion = UtilsMenu.leerOpcion(scanner);
            switch (opcion) {
                case 1:
                    menuCategoria.iniciar();
                    break;
                case 2:
                    menuProductos.iniciar();
                    break;
                case 3:
                    menuUsuarios.iniciar();
                    break;
                case 4:
                    menuPedidos.iniciar();
                    break;
                case 0:
                    salir = true;
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        }
    }
    
    public void mostrar() {
        System.out.println("=====================================");
        System.out.println("|| SISTEMA DE PEDIDOS (FOOD STORE) ||");
        System.out.println("=====================================");
        System.out.println("|| 1. Categorias                   ||");
        System.out.println("|| 2. Productos                    ||");
        System.out.println("|| 3. Usuarios                     ||");
        System.out.println("|| 4. Pedidos                      ||");
        System.out.println("|| 0. Salir                        ||");
        System.out.println("=====================================");
        System.out.print("Seleccione una opción: ");
    }
}
