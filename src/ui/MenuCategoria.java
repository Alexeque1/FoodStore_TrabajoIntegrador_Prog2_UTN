package ui;

import java.util.List;
import java.util.Scanner;
import utils.UtilsMenu;
import services.CategoriaServices;
import entities.Categoria;
import exception.CategoriaDuplicadaException;
import exception.CategoriaInexistenteException;
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

    // OPCIONES
    public void opcion_listarCategorias() {
        UtilsGeneral.tituloSubcategoria("Listar categorías");
        categoriaServices.obtenerCategoriasActivas().forEach(categoria -> {
            System.out.println(categoria);
            System.out.println("----------------------------");
        });
        UtilsGeneral.esperarEnter(scanner);
    }

    public void opcion_crearCategoria() {
        try {
            UtilsGeneral.tituloSubcategoria("Crear categorías");
            System.out.print("Ingrese el nombre de la nueva categoría: ");
            String nombre = UtilsGeneral.leerString(scanner);

            System.out.print("Ingrese una descripcion de la nueva categoría: ");
            String descripcion = UtilsGeneral.leerString(scanner);
            System.out.println("-----------------------------------");
            System.out.println("¿Esta seguro que desea crear esta categoría? (s/n)");
            String confirmacion = UtilsGeneral.leerString(scanner);

            if (confirmacion.equalsIgnoreCase("s")) {
                categoriaServices.crearCategoria(nombre, descripcion);
                System.out.println("Categoría creada exitosamente.");
            } else {
                System.out.println("Creación de categoría cancelada.");
            }
            System.out.println("-----------------------------------");
            UtilsGeneral.esperarEnter(scanner);
        } catch (CategoriaDuplicadaException e) {
            System.out.println(e.getMessage());
            UtilsGeneral.esperarEnter(scanner);
        }
    }

    public void opcion_editarCategoria() {
        UtilsGeneral.tituloSubcategoria("Editar categorías");
        if (categoriaServices.listaCategoriasEstaVacia()) {
            System.out.println("No hay categorías para editar.");
            UtilsGeneral.esperarEnter(scanner);
            return;
        }

        categoriaServices.obtenerCategoriasActivas().forEach(categoria -> {
            System.out.println(categoria);
            System.out.println("----------------------------");
        });
        System.out.println("0. Salir");
        System.out.print("Ingrese el id de la categoría a editar o ingrese cero (0) para salir: ");
        Long id = UtilsGeneral.leerId(scanner);
        if (id == 0) {
            System.out.println("Volviendo al menú de categorías...");
            return;
        }

        try {
            System.out.println("----------------------------");
            Categoria categoria = categoriaServices.buscarCategoriaPorId(id);
            boolean salir = false;
            while (!salir) {
                System.out.println("Editando categoría: " + categoria.getNombre());
                System.out.println("¿Que deseas editar?");
                System.out.println("(1) Nombre");
                System.out.println("(2) Descripción");
                System.out.println("(0) Salir");
                int opcion = UtilsMenu.leerOpcion(scanner);
                String descripcionActual = categoria.getDescripcion();
                String nombreActual = categoria.getNombre();
                switch (opcion) {
                    case 1:
                        System.out.print("Ingrese el nuevo nombre de la categoría: ");
                        String nuevoNombre = UtilsGeneral.leerString(scanner);
                        categoriaServices.actualizarCategoria(categoria, nuevoNombre, descripcionActual);
                        break;
                    case 2:
                        System.out.print("Ingrese la nueva descripción de la categoría: ");
                        String nuevaDescripcion = UtilsGeneral.leerString(scanner);
                        categoriaServices.actualizarCategoria(categoria, nombreActual, nuevaDescripcion);
                        break;
                    case 0:
                        System.out.println("Volviendo al menú de categorías...");
                        return;
                    default:
                        System.out.println("Opción no válida.");
                }

                System.out.println("¿Deseas volver a editar esta categoria? (s/n)");
                String volver = UtilsGeneral.leerString(scanner);
                if (!volver.equalsIgnoreCase("s")) {
                    salir = true;
                }
            }
        } catch (CategoriaInexistenteException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (CategoriaDuplicadaException e) {
            System.out.println(e.getMessage());
        }
    }

    public void opcion_eliminarCategoria() {
        UtilsGeneral.tituloSubcategoria("Eliminar categorías");
        if (categoriaServices.listaCategoriasEstaVacia()) {
            System.out.println("No hay categorías para eliminar.");
            UtilsGeneral.esperarEnter(scanner);
            return;
        }

        categoriaServices.obtenerCategoriasActivas().forEach(categoria -> {
            System.out.println(categoria);
            System.out.println("----------------------------");
        });
        System.out.println("0. Salir.");
        System.out.print("Ingrese el id de la categoría a eliminar o (0) para salir: ");
        Long id = UtilsGeneral.leerId(scanner);
        if (id == 0) {
            System.out.println("Volviendo al menú de categorías...");
            return;
        }

        try {
            Categoria categoria = categoriaServices.buscarCategoriaPorId(id);
            if (categoria == null) {
                System.out.println("Categoría no encontrada.");
                UtilsGeneral.esperarEnter(scanner);
                return;
            }

            System.out.println("¿Está seguro que desea eliminar esta categoría? (s/n)");
            String confirmacion = UtilsGeneral.leerString(scanner);

            if (confirmacion.equalsIgnoreCase("s")) {
                categoriaServices.eliminarCategoria(categoria);
                System.out.println("Categoría eliminada exitosamente.");
            } else {
                System.out.println("Eliminación de categoría cancelada.");
            }

            UtilsGeneral.esperarEnter(scanner);
        } catch (CategoriaInexistenteException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
