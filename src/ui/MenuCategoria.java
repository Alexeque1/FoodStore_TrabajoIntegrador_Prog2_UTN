package ui;

import java.util.List;
import java.util.Scanner;
import utils.UtilsMenu;
import services.CategoriaServices;
import entities.Categoria;
import utils.UtilsGeneral;

public class MenuCategoria {
    private final Scanner scanner;
    private final CategoriaServices categoriaServices;

    public MenuCategoria(Scanner scanner, CategoriaServices categoriaServices) {
        this.scanner = scanner;
        this.categoriaServices = categoriaServices;
    }

    public void iniciar() {
        boolean volver = false;
        while (!volver) {
            mostrar();
            int opcion = UtilsMenu.leerOpcion(scanner);
            switch (opcion) {
                case 1:
                    listarCategorias();
                    break;
                case 2:
                    crearCategoria();
                    break;
                case 3:
                    // Lógica para editar categoría
                    break;
                case 4:
                    // Lógica para eliminar categoría
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
        System.out.println("||     GESTIÓN DE CATEGORÍAS       ||");
        System.out.println("=====================================");
        System.out.println("|| 1. Listar categorías            ||");
        System.out.println("|| 2. Crear categoría              ||");
        System.out.println("|| 3. Editar categoría             ||");
        System.out.println("|| 4. Eliminar categoría           ||");
        System.out.println("|| 0. Volver al menú principal     ||");
        System.out.println("=====================================");
        System.out.print("Seleccione una opción: ");
    }

    public void listarCategorias() {
        if(categoriaServices.listaCategoriasVacia()) {
            System.out.println("No hay categorías registradas.");
        } else {
            List<Categoria> categorias = categoriaServices.getCategorias();
            System.out.println("Categorías:");
            for (Categoria categoria : categorias) {
                System.out.println(categoria);
            }
        }
        utils.UtilsGeneral.esperarEnter(scanner);
    }

    public void crearCategoria() {
        System.out.print("Ingrese el nombre de la nueva categoría: ");
        String nombre = utils.UtilsGeneral.leerString(scanner);
        
        if(categoriaServices.existeCategoriaPorNombre(nombre)) {
            System.out.println("Ya existe una categoría con ese nombre.");
            utils.UtilsGeneral.esperarEnter(scanner);
            return;
        }

        System.out.print("Ingrese una descripcion de la nueva categoría: ");
        String descripcion = utils.UtilsGeneral.leerString(scanner);

        categoriaServices.crearCategoria(nombre, descripcion);
        System.out.println("Categoría creada exitosamente.");
        utils.UtilsGeneral.esperarEnter(scanner);
    }
}
