package ui;

import java.util.List;
import java.util.Scanner;
import utils.UtilsMenu;
import services.CategoriaServices;
import entities.Categoria;
import exception.DatoDuplicadaException;
import exception.DatoInexistenteException;
import exception.DatoInvalidoException;
import utils.UtilsGeneral;
import utils.UtilsListar;

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
                    opcion_listarCategorias();
                    break;
                case 2:
                    opcion_crearCategoria();
                    break;
                case 3:
                    opcion_editarCategoria();
                    break;
                case 4:
                    opcion_eliminarCategoria();
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

    // OPCIONES
    public void opcion_listarCategorias() {
        UtilsGeneral.tituloSubcategoria("Listar categorías");
        List<Categoria> categoriasActivas = categoriaServices.listarActivas();
        if (categoriasActivas.isEmpty()) {
            System.out.println("No hay categorías para mostrar.");
        } else {
            UtilsListar.listarCategorias(categoriasActivas);
        }
        UtilsGeneral.esperarEnter(scanner);
    }

    public void opcion_crearCategoria() {
        UtilsGeneral.tituloSubcategoria("Crear categorías");

        while (true) {
            try {
                System.out.print("Ingrese el nombre de la nueva categoría: ");
                String nombre = UtilsGeneral.leerString(scanner);
                System.out.println("-----------------------------------");
                System.out.print("Ingrese una descripción de la nueva categoría: ");
                String descripcion = UtilsGeneral.leerString(scanner);
                System.out.println("-----------------------------------");
                System.out.print("¿Está seguro que desea crear esta categoría? (s/n): ");
                String confirmacion = UtilsGeneral.leerString(scanner);

                if (!confirmacion.equalsIgnoreCase("s")) {
                    System.out.println("Creación de categoría cancelada.");
                    break;
                }

                categoriaServices.crear(nombre, descripcion);

                System.out.println("Categoría creada exitosamente.");

                System.out.print("¿Desea crear otra categoría? (s/n): ");
                String repetir = UtilsGeneral.leerString(scanner);

                if (!repetir.equalsIgnoreCase("s")) {
                    break;
                }

            } catch (DatoDuplicadaException e) {
                System.out.println(e.getMessage());
            } catch (DatoInvalidoException e) {
                System.out.println(e.getMessage());
            }

            System.out.println("-----------------------------------");
        }

        System.out.println("Volviendo al menú principal...");
        UtilsGeneral.esperarEnter(scanner);
    }

    public void opcion_editarCategoria() {
        UtilsGeneral.tituloSubcategoria("Editar categorías");

        List<Categoria> categoriasActivas = categoriaServices.listarActivas();
        if (categoriasActivas.isEmpty()) {
            System.out.println("No hay categorías para editar.");
            UtilsGeneral.esperarEnter(scanner);
            return;
        }

        UtilsListar.listarCategorias(categoriasActivas);
        System.out.println("0. Salir");
        System.out.print("Ingrese el id de la categoría a editar o ingrese cero (0) para salir: ");

        Long id = UtilsGeneral.leerId(scanner);
        if (id.equals(0L)) {
            System.out.println("Volviendo al menú de categorías...");
            return;
        }

        Categoria categoria;
        try {
            categoria = categoriaServices.buscarPorId(id);
        } catch (DatoInexistenteException | DatoInvalidoException e) {
            System.out.println(e.getMessage());
            UtilsGeneral.esperarEnter(scanner);
            return;
        }

        System.out.println("Nombre actual: " + categoria.getNombre());
        System.out.print("Nuevo nombre (Enter para mantener): ");
        String nombre = scanner.nextLine().trim();
        if (nombre.isBlank()) {
            nombre = null;
        }

        System.out.println("Descripción actual: " + categoria.getDescripcion());
        System.out.print("Nueva descripción (Enter para mantener): ");
        String descripcion = scanner.nextLine().trim();
        if (descripcion.isBlank()) {
            descripcion = null;
        }

        try {
            categoriaServices.editar(id, nombre, descripcion);
            System.out.println("Categoría editada exitosamente.");
        } catch (DatoInvalidoException | DatoDuplicadaException | DatoInexistenteException e) {
            System.out.println("Error al editar la categoría: " + e.getMessage());
        }
        UtilsGeneral.esperarEnter(scanner);
    }

    public void opcion_eliminarCategoria() {
        UtilsGeneral.tituloSubcategoria("Eliminar categorías");
        List<Categoria> categoriasActivas = categoriaServices.listarActivas();
        if (categoriasActivas.isEmpty()) {
            System.out.println("No hay categorías para eliminar.");
            UtilsGeneral.esperarEnter(scanner);
            return;
        }

        UtilsListar.listarCategorias(categoriasActivas);
        System.out.println("0. Salir.");
        System.out.print("Ingrese el id de la categoría a eliminar o (0) para salir: ");
        Long id = UtilsGeneral.leerId(scanner);
        if (id.equals(0L)) {
            System.out.println("Volviendo al menú de categorías...");
            return;
        }

        Categoria categoria;
        try {
            categoria = categoriaServices.buscarPorId(id);
        } catch (DatoInexistenteException | DatoInvalidoException e) {
            System.out.println(e.getMessage());
            UtilsGeneral.esperarEnter(scanner);
            return;
        }
        System.out.println("-----------------------------------");
        System.out.println("Categoría a eliminar: " + categoria.getNombre());
        System.out.print("¿Está seguro que desea eliminar esta categoría? (s/n): ");
        String confirmacion = UtilsGeneral.leerString(scanner);
        if (!confirmacion.equalsIgnoreCase("s")) {
            System.out.println("Eliminación de categoría cancelada.");
            UtilsGeneral.esperarEnter(scanner);
            return;
        }
        try {
            categoriaServices.eliminar(id);
            System.out.println("Categoría eliminada exitosamente.");
        } catch (DatoInvalidoException | DatoInexistenteException e) {
            System.out.println(e.getMessage());
        }
    }
}
