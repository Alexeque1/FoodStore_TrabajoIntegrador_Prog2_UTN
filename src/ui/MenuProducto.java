package ui;

import entities.Categoria;
import entities.Producto;
import exception.DatoDuplicadaException;
import exception.DatoInexistenteException;
import exception.DatoInvalidoException;

import java.util.List;
import java.util.Scanner;
import services.CategoriaServices;
import services.ProductoServices;
import utils.UtilsGeneral;
import utils.UtilsListar;
import utils.UtilsMenu;

public class MenuProducto {

    private final Scanner scanner;
    private final ProductoServices productoServices;
    private final CategoriaServices categoriaServices;

    public MenuProducto(Scanner scanner, ProductoServices productoServices, CategoriaServices categoriaServices) {
        this.scanner = scanner;
        this.productoServices = productoServices;
        this.categoriaServices = categoriaServices;
    }

    public void iniciar() {
        boolean volver = false;
        while (!volver) {
            mostrar();
            int opcion = UtilsMenu.leerOpcion(scanner);
            switch (opcion) {
                case 1:
                    opcion_listarProductos();
                    break;
                case 2:
                    opcion_crearProducto();
                    break;
                case 3:
                    opcion_editarProducto();
                    break;
                case 4:
                    opcion_eliminarProducto();
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
        System.out.println("||       GESTION DE PRODUCTOS      ||");
        System.out.println("=====================================");
        System.out.println("|| 1. Listar productos             ||");
        System.out.println("|| 2. Crear producto               ||");
        System.out.println("|| 3. Editar producto              ||");
        System.out.println("|| 4. Eliminar producto            ||");
        System.out.println("|| 0. Volver al menu principal     ||");
        System.out.println("=====================================");
        System.out.print("Seleccione una opcion: ");
    }

    public void opcion_listarProductos() {
        UtilsGeneral.tituloSubcategoria("Listar productos");

        List<Producto> productosActivos = productoServices.listarActivos();

        if (productosActivos.isEmpty()) {
            System.out.println("No hay productos para mostrar.");
            UtilsGeneral.esperarEnter(scanner);
            return;
        }

        System.out.println("¿Deseas filtrar por categoría? (s/n)");
        String respuesta = scanner.nextLine().trim().toLowerCase();

        if ("s".equals(respuesta)) {
            List<Categoria> categoriasDisponibles = productoServices.obtenerCategoriasConProductos();

            if (categoriasDisponibles.isEmpty()) {
                System.out.println("No hay categorías con productos.");
                UtilsGeneral.esperarEnter(scanner);
                return;
            }

            UtilsListar.listarCategorias(categoriasDisponibles);
            System.out.println("0. Salir.");
            System.out.print("Ingrese el id de la categoría que desea filtrar o (0) para salir: ");

            Long id = UtilsGeneral.leerId(scanner);
            if (id.equals(0L)) {
                System.out.println("Volviendo al menú...");
                UtilsGeneral.esperarEnter(scanner);
                return;
            }

            Categoria categoria = categoriasDisponibles.stream()
                    .filter(c -> c.getId().equals(id))
                    .findFirst()
                    .orElse(null);

            if (categoria == null) {
                System.out.println("Debe seleccionar una categoría válida.");
                UtilsGeneral.esperarEnter(scanner);
                return;
            }

            System.out.println("-----------------------------------");
            List<Producto> productosFiltrados = productoServices.listarPorCategoria(categoria);
            if (productosFiltrados.isEmpty()) {
                System.out.println("No hay productos para mostrar en esta categoría.");
            } else {
                UtilsListar.listarProductos(productosFiltrados);
            }

        } else {
            UtilsListar.listarProductos(productosActivos);
        }

        UtilsGeneral.esperarEnter(scanner);
    }

    public void opcion_crearProducto() {
        UtilsGeneral.tituloSubcategoria("Crear producto");

        List<Categoria> categoriasActivas = categoriaServices.listarActivas();

        if (categoriasActivas.isEmpty()) {
            System.out.println(
                    "No hay categorías disponibles. Por favor, cree una categoría antes de crear un producto.");
            UtilsGeneral.esperarEnter(scanner);
            return;
        }

        while (true) {

            System.out.print("Ingrese el nombre del producto: ");
            String nombre = UtilsGeneral.leerString(scanner);
            System.out.println("-----------------------------------");
            System.out.print("Ingrese la descripción del producto: ");
            String descripcion = UtilsGeneral.leerString(scanner);
            System.out.println("-----------------------------------");
            System.out.print("Ingrese el precio del producto: ");
            Double precio = UtilsGeneral.leerDouble(scanner);
            System.out.println("-----------------------------------");
            System.out.print("Ingrese el stock del producto: ");
            int stock = UtilsGeneral.leerEntero(scanner);
            System.out.println("-----------------------------------");
            System.out.print("Ingrese la imagen del producto: ");
            String imagen = UtilsGeneral.leerString(scanner);
            System.out.println("-----------------------------------");
            System.out.print("Ingrese la disponibilidad del producto (true/false): ");
            Boolean disponible = UtilsGeneral.leerBooleano(scanner);

            Categoria categoriaSeleccionada = seleccionarCategoria(categoriasActivas);

            try {
                productoServices.crear(
                        nombre,
                        descripcion,
                        precio,
                        stock,
                        imagen,
                        disponible,
                        categoriaSeleccionada);

                System.out.println("Producto creado exitosamente.");
                break;

            } catch (DatoInvalidoException | DatoDuplicadaException e) {
                System.out.println("Error al crear el producto: " + e.getMessage());
                System.out.println("Intente nuevamente.");
            }
        }

        UtilsGeneral.esperarEnter(scanner);
    }

    public void opcion_editarProducto() {
        UtilsGeneral.tituloSubcategoria("Editar producto");

        List<Producto> productosActivos = productoServices.listarActivos();
        if (productosActivos.isEmpty()) {
            System.out.println("No hay productos para editar.");
            UtilsGeneral.esperarEnter(scanner);
            return;
        }

        System.out.println("Seleccione el producto que desea editar:");
        UtilsListar.listarProductos(productosActivos);
        System.out.println("0. Salir.");
        System.out.print("Ingrese el id del producto que desea editar o (0) para salir: ");

        Long id = UtilsGeneral.leerId(scanner);
        if (id.equals(0L)) {
            System.out.println("Volviendo al menú...");
            UtilsGeneral.esperarEnter(scanner);
            return;
        }

        Producto productoSeleccionado;
        try {
            productoSeleccionado = productoServices.buscarPorId(id);
        } catch (DatoInexistenteException | DatoInvalidoException e) {
            System.out.println(e.getMessage());
            UtilsGeneral.esperarEnter(scanner);
            return;
        }

        System.out.println("Nombre actual: " + productoSeleccionado.getNombre());
        System.out.print("Nuevo nombre (Enter para mantener): ");
        String nombre = scanner.nextLine().trim();
        if (nombre.isBlank()) {
            nombre = null;
        }

        System.out.println("Descripción actual: " + productoSeleccionado.getDescripcion());
        System.out.print("Nueva descripción (Enter para mantener): ");
        String descripcion = scanner.nextLine().trim();
        if (descripcion.isBlank()) {
            descripcion = null;
        }

        System.out.println("Precio actual: " + productoSeleccionado.getPrecio());
        System.out.print("Nuevo precio (Enter para mantener): ");
        String precioStr = scanner.nextLine().trim();
        Double precio = null;
        if (!precioStr.isBlank()) {
            try {
                precio = Double.parseDouble(precioStr);
            } catch (NumberFormatException e) {
                System.out.println("Precio inválido.");
            }
        }

        System.out.println("Stock actual: " + productoSeleccionado.getStock());
        System.out.print("Nuevo stock (Enter para mantener): ");
        String stockStr = scanner.nextLine().trim();
        Integer stock = null;
        if (!stockStr.isBlank()) {
            try {
                stock = Integer.parseInt(stockStr);
            } catch (NumberFormatException e) {
                System.out.println("Stock inválido.");
            }
        }

        System.out.println("Imagen actual: " + productoSeleccionado.getImagen());
        System.out.print("Nueva imagen (Enter para mantener): ");
        String imagen = scanner.nextLine().trim();
        if (imagen.isBlank()) {
            imagen = null;
        }

        System.out.println("Disponibilidad actual: " + productoSeleccionado.getDisponible());
        System.out.print("Nueva disponibilidad (true/false, Enter para mantener): ");
        String disponibleStr = scanner.nextLine().trim();
        Boolean disponible = null;
        if (!disponibleStr.isBlank()) {
            if (disponibleStr.equalsIgnoreCase("true")) {
                disponible = true;
            } else if (disponibleStr.equalsIgnoreCase("false")) {
                disponible = false;
            } else {
                System.out.println("Disponibilidad inválida.");
            }
        }

        System.out.print("¿Desea cambiar la categoría? (s/n): ");
        String cambiarCategoria = scanner.nextLine().trim().toLowerCase();

        Categoria categoria = null;

        if ("s".equals(cambiarCategoria)) {
            List<Categoria> categoriasActivas = categoriaServices.listarActivas();

            categoria = seleccionarCategoria(categoriasActivas);
        }

        try {
            productoServices.editar(
                    id,
                    nombre,
                    descripcion,
                    precio,
                    stock,
                    imagen,
                    disponible,
                    categoria);

            System.out.println("Producto editado exitosamente.");
        } catch (Exception e) {
            System.out.println("Error al editar el producto: " + e.getMessage());
            System.out.println("Intente nuevamente.");
        }
    }

    public void opcion_eliminarProducto() {
        UtilsGeneral.tituloSubcategoria("Eliminar producto");
        List<Producto> productosActivos = productoServices.listarActivos();
        if (productosActivos.isEmpty()) {
            System.out.println("No hay productos para eliminar.");
            UtilsGeneral.esperarEnter(scanner);
            return;
        }
        System.out.println("Seleccione el producto que desea eliminar:");
        UtilsListar.listarProductos(productosActivos);
        System.out.println("0. Salir.");
        System.out.print("Ingrese el id del producto a eliminar o (0) para salir: ");
        Long id = UtilsGeneral.leerId(scanner);
        if (id.equals(0L)) {
            System.out.println("Volviendo al menú...");
            UtilsGeneral.esperarEnter(scanner);
            return;
        }

        Producto productoSeleccionado;
        try {
            productoSeleccionado = productoServices.buscarPorId(id);
        } catch (DatoInexistenteException | DatoInvalidoException e) {
            System.out.println(e.getMessage());
            UtilsGeneral.esperarEnter(scanner);
            return;
        }

        System.out.println("-----------------------------------");
        System.out.println("Producto a eliminar: " + productoSeleccionado.getNombre());
        System.out.print("¿Está seguro que desea eliminar este producto? (s/n): ");
        String confirmacion = UtilsGeneral.leerString(scanner);
        if (!confirmacion.equalsIgnoreCase("s")) {
            System.out.println("Eliminación de producto cancelada.");
            UtilsGeneral.esperarEnter(scanner);
            return;
        }
        try {
            productoServices.eliminar(id);
            System.out.println("Producto eliminado exitosamente.");
        } catch (DatoInvalidoException | DatoInexistenteException e) {
            System.out.println("Error al eliminar el producto: " + e.getMessage());
        }
    }

    // METODOS AUXILIARES
    private Categoria seleccionarCategoria(List<Categoria> categorias) {

        while (true) {
            System.out.println("Seleccione una categoría:");
            UtilsListar.listarCategorias(categorias);

            Long id = UtilsGeneral.leerId(scanner);

            Categoria categoria = categorias.stream()
                    .filter(c -> c.getId().equals(id))
                    .findFirst()
                    .orElse(null);

            if (categoria != null) {
                return categoria;
            }

            System.out.println("Debe seleccionar una categoría válida.");
        }
    }
}
