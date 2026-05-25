package ui;

import entities.Categoria;
import entities.Producto;
import java.util.List;
import java.util.Scanner;
import services.CategoriaServices;
import services.ProductoServices;
import utils.UtilsGeneral;
import utils.UtilsMenu;

public class MenuProductos {

    private final Scanner scanner;
    private final ProductoServices productoServices;
    private final CategoriaServices categoriaServices;

    public MenuProductos(Scanner scanner, ProductoServices productoServices, CategoriaServices categoriaServices) {
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
                    listarProductos();
                    break;
                case 2:
                    crearProducto();
                    break;
                case 3:
                    // Editar — próximo paso
                    break;
                case 4:
                    // Eliminar — próximo paso
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

    public void listarProductos() {
        if (productoServices.listaProductosVacia()) {
            System.out.println("No hay productos registrados.");
        } else {
            List<Producto> productos = productoServices.getProductosActivos();
            System.out.println("Productos:");
            for (Producto producto : productos) {
                System.out.println(producto);
            }
        }
        UtilsGeneral.esperarEnter(scanner);
    }

    public void crearProducto() {
        System.out.print("Ingrese el nombre del producto: ");
        String nombre = UtilsGeneral.leerString(scanner);

        System.out.print("Ingrese la descripcion del producto: ");
        String descripcion = UtilsGeneral.leerString(scanner);

        System.out.print("Ingrese el precio del producto: ");
        Double precio = UtilsGeneral.leerDouble(scanner);

        System.out.print("Ingrese el stock del producto: ");
        int stock = UtilsGeneral.leerEntero(scanner);

        System.out.print("Ingrese la imagen del producto (URL o nombre): ");
        String imagen = UtilsGeneral.leerString(scanner);

        System.out.print("Esta disponible? (s/n): ");
        Boolean disponible = UtilsGeneral.leerBooleano(scanner);

        categoriaServices.getCategorias().forEach(c -> {
            if (!c.isEliminado()) System.out.println(c);
        });
        System.out.print("Ingrese el id de la categoria: ");
        Long idCategoria = UtilsGeneral.leerLong(scanner);
        Categoria categoria = categoriaServices.buscarCategoriaPorId(idCategoria);

        if (categoria == null) {
            System.out.println("Categoria no encontrada. Operacion cancelada.");
            UtilsGeneral.esperarEnter(scanner);
            return;
        }

        try {
            productoServices.crearProducto(nombre, precio, descripcion, stock, imagen, disponible, categoria);
            System.out.println("Producto creado exitosamente.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        UtilsGeneral.esperarEnter(scanner);
    }
}
