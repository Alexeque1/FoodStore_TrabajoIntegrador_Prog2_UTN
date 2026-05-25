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
                    System.out.println("Volviendo al menu principal...");
                    break;
                default:
                    System.out.println("Opcion no valida. Por favor, intente nuevamente.");
            }
        }
    }

    public void mostrar() {
        System.out.println("=====================================");
        System.out.println("||     GESTION DE CATEGORIAS       ||");
        System.out.println("=====================================");
        System.out.println("|| 1. Listar categorias            ||");
        System.out.println("|| 2. Crear categoria              ||");
        System.out.println("|| 3. Editar categoria             ||");
        System.out.println("|| 4. Eliminar categoria           ||");
        System.out.println("|| 0. Volver al menu principal     ||");
        System.out.println("=====================================");
        System.out.print("Seleccione una opcion: ");
    }

    public void listarCategorias() {
        if(categoriaServices.listaCategoriasVacia()) {
            System.out.println("No hay categorias registradas.");
        } else {
            List<Categoria> categorias = categoriaServices.getCategorias();
            System.out.println("Categorias:");
            for (Categoria categoria : categorias) {
                System.out.println(categoria);
            }
        }
        utils.UtilsGeneral.esperarEnter(scanner);
    }

    public void crearCategoria() {
        System.out.print("Ingrese el nombre de la nueva categoria: ");
        String nombre = utils.UtilsGeneral.leerString(scanner);
        
        if(categoriaServices.existeCategoriaPorNombre(nombre)) {
            System.out.println("Ya existe una categoria con ese nombre.");
            utils.UtilsGeneral.esperarEnter(scanner);
            return;
        }

        System.out.print("Ingrese una descripcion de la nueva categoria: ");
        String descripcion = utils.UtilsGeneral.leerString(scanner);

        categoriaServices.crearCategoria(nombre, descripcion);
        System.out.println("Categoria creada exitosamente.");
        utils.UtilsGeneral.esperarEnter(scanner);
    }
}
