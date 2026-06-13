package ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import entities.Pedido;
import entities.Producto;
import entities.Usuario;
import enums.Estado;
import enums.FormaPago;
import exception.DatoInexistenteException;
import exception.DatoInvalidoException;
import exception.StockInsuficienteException;
import services.ItemPedido;
import services.PedidosServices;
import services.ProductoServices;
import services.UsuarioServices;
import utils.UtilsGeneral;
import utils.UtilsListar;
import utils.UtilsMenu;

public class MenuPedidos {
    private final Scanner scanner;
    private final ProductoServices productoServices;
    private final UsuarioServices usuarioServices;
    private final PedidosServices pedidoServices;

    public MenuPedidos(Scanner scanner, ProductoServices productoServices, UsuarioServices usuarioServices, PedidosServices pedidoServices) {
        this.scanner = scanner;
        this.productoServices = productoServices;
        this.usuarioServices = usuarioServices;
        this.pedidoServices = pedidoServices;
    }

    public void iniciar() {
        boolean salir = false;
        while (!salir) {
            mostrar();
            int opcion = UtilsMenu.leerOpcion(scanner);
            switch (opcion) {
                case 1:
                    opcion_listarPedidos();
                    break;
                case 2:
                    opcion_crearPedido();
                    break;
                case 3:
                    opcion_actualizarEstadoYFormaPago();
                    break;
                case 4:
                    opcion_eliminarPedidos();
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

    public void opcion_listarPedidos() {
        UtilsGeneral.tituloSubcategoria("Listar pedidos");
        List<Pedido> pedidos = pedidoServices.listarActivos();
        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos para mostrar.");
        } else {
            UtilsListar.listarPedidos(pedidos);
        }
        UtilsGeneral.esperarEnter(scanner);
    }

    public void opcion_crearPedido() {
        UtilsGeneral.tituloSubcategoria("Crear pedido");

        // Verificar que haya productos disponibles con stock para crear un pedido
        List<Producto> productosDisponibles = productoServices.listarConStock();
        if (productosDisponibles.isEmpty()) {
            System.out.println("No hay productos disponibles con stock para crear un pedido.");
            UtilsGeneral.esperarEnter(scanner);
            return;
        }

        // Verificar que haya usuarios disponibles para asociar el pedido.
        List<Usuario> usuarios = usuarioServices.listarActivos();
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios disponibles para asociar el pedido.");
            UtilsGeneral.esperarEnter(scanner);
            return;
        }
        System.out.println("Lista de usuarios:");
        UtilsListar.listarUsuarios(usuarios);
        System.out.println("0. Salir.");
        System.out.print("Ingrese el id del usuario o (0) para salir: ");
        Long id = UtilsGeneral.leerId(scanner);
        if (id.equals(0L)) {
            System.out.println("Volviendo al menú...");
            UtilsGeneral.esperarEnter(scanner);
            return;
        }

        Usuario usuarioSeleccionado;
        try {
            usuarioSeleccionado = usuarioServices.buscarPorId(id);
        } catch (DatoInexistenteException | DatoInvalidoException e) {
            System.out.println(e.getMessage());
            UtilsGeneral.esperarEnter(scanner);
            return;
        }
        System.out.println("-----------------------------------");
        System.out.println(
                "Usuario seleccionado: " + usuarioSeleccionado.getNombre() + " " + usuarioSeleccionado.getApellido());
        System.out.println("-----------------------------------");

        // Creacion de lista provisoria para el pedido
        List<ItemPedido> listaItemPedidos = new ArrayList<>();

        // Lógica para seleccionar productos y cantidades
        List<Producto> productosConStock = productoServices.listarConStock();
        Double totalPedido = 0.0;
        while (true) {
            if (productosConStock.isEmpty()) {
                System.out.println("No hay más productos disponibles con stock para agregar al pedido.");
                break;
            }

            // Listar productos con stock y pedir al usuario que vaya seleccionando
            System.out.println("-----------------------------------------------------");
            System.out.println("Productos disponibles con stock:");
            listarProductosConStockProvisional(productosConStock, listaItemPedidos);
            System.out.println("0. Finalizar selección de productos.");
            System.out.print("Ingrese el id del producto para agregar al pedido o (0) para finalizar: ");
            Long idProducto = UtilsGeneral.leerId(scanner);
            if (idProducto.equals(0L)) {
                System.out.println("Finalizando selección de productos...");
                break;
            }
            Producto productoSeleccionado;
            try {
                productoSeleccionado = productoServices.buscarPorId(idProducto);
            } catch (DatoInexistenteException | DatoInvalidoException e) {
                System.out.println(e.getMessage());
                UtilsGeneral.esperarEnter(scanner);
                continue;
            }

            // Una vez seleccionado el producto, se pide cantidad
            System.out.println("-----------------------------------");
            System.out.println("Producto seleccionado: " + productoSeleccionado.getNombre());
            System.out.print("Ingrese la cantidad a agregar al pedido: ");
            int cantidadAgregar = UtilsGeneral.leerEntero(scanner);

            // Validar cantidad positiva antes de validar stock
            if (cantidadAgregar <= 0) {
                System.out.println("La cantidad debe ser mayor a 0.");
                UtilsGeneral.esperarEnter(scanner);
                continue;
            }

            // Proceso de agregacion de cantidad y validacion de stock de Item Pedido
            int stockProducto = productoSeleccionado.getStock();
            Long idProductoSeleccionado = productoSeleccionado.getId();

            ItemPedido existente = findItemPedidoByProductoId(idProductoSeleccionado, listaItemPedidos);
            int cantidadYaCargada = (existente != null) ? existente.getCantidad() : 0;
            int stockDisponibleReal = stockProducto - cantidadYaCargada;

            if (cantidadAgregar > stockDisponibleReal) {
                System.out.println("-----------------------------------");
                System.out.println("No hay suficiente stock disponible para esa cantidad.");
                System.out.println("Stock total del producto: " + stockProducto);
                if (cantidadYaCargada > 0) {
                    System.out.println("Ya tenés " + cantidadYaCargada + " cargadas en este pedido.");
                }
                System.out.println("Podés agregar hasta " + stockDisponibleReal + " más.");
                UtilsGeneral.esperarEnter(scanner);
                continue;
            }

            if (existente != null) {
                existente.agregarCantidad(cantidadAgregar);
            } else {
                listaItemPedidos.add(new ItemPedido(idProductoSeleccionado, cantidadAgregar));
            }
            System.out.println("-----------------------------------");
            Double subtotal = productoSeleccionado.getPrecio() * cantidadAgregar;
            totalPedido += subtotal;

            System.out.println("Se agrego " + cantidadAgregar + " a " + productoSeleccionado.getNombre());
            System.out.println("Subtotal: $" + subtotal);
            System.out.println("Total del pedido hasta ahora: $" + totalPedido);

            System.out.print("¿Desea agregar otro producto al pedido? (s/n): ");
            String agregarOtro = UtilsGeneral.leerString(scanner);
            if (!agregarOtro.equalsIgnoreCase("s")) {
                System.out.println("Finalizando selección de productos...");
                break;
            }
        }
        System.out.println("-----------------------------------");
        if (listaItemPedidos.isEmpty()) {
            System.out.println("No se agregaron productos. Cancelando creación del pedido...");
            UtilsGeneral.esperarEnter(scanner);
            return;
        }

        // Seleccionar forma de pago
        FormaPago formaPagoSeleccionada;
        while (true) {
            System.out.println("Seleccione la forma de pago para el pedido:");
            UtilsListar.listarFormasPago();
            System.out.print("Ingrese el número correspondiente a la forma de pago: ");
            int opcionFormaPago = UtilsGeneral.leerEntero(scanner);
            if (opcionFormaPago < 1 || opcionFormaPago > FormaPago.values().length) {
                System.out.println("-----------------------------------");
                System.out.println("Opción no válida, debe ser un número entre 1 y " + FormaPago.values().length
                        + ". Intente nuevamente.");
                continue;
            }
            formaPagoSeleccionada = FormaPago.values()[opcionFormaPago - 1];
            break;
        }

        System.out.println("-----------------------------------");
        try {
            Pedido pedidoCreado = pedidoServices.crear(usuarioSeleccionado.getId(), listaItemPedidos,
                    formaPagoSeleccionada);
            System.out.println("¡El pedido ha sido creado!");
            System.out.println("Total del pedido: " + pedidoCreado.getTotal());
        } catch (DatoInvalidoException | DatoInexistenteException | StockInsuficienteException e) {
            System.out.println(e.getMessage());
            UtilsGeneral.esperarEnter(scanner);
        }
    }

    public void opcion_actualizarEstadoYFormaPago() {
        UtilsGeneral.tituloSubcategoria("Actualizar Estado o Forma de Pago de Pedidos");
        List<Pedido> pedidosActivos = pedidoServices.listarActivos();
        if (pedidosActivos.isEmpty()) {
            System.out.println("No hay pedidos para editar.");
            UtilsGeneral.esperarEnter(scanner);
            return;
        }

        // Seleccion de Pedido
        while (true) {
            System.out.println("Seleccione el pedido que desea editar:");
            UtilsListar.listarPedidos(pedidosActivos);
            System.out.println("0. Salir.");
            System.out.print("Ingrese el id del producto que desea editar o (0) para salir: ");

            Long id = UtilsGeneral.leerId(scanner);
            if (id.equals(0L)) {
                System.out.println("Volviendo al menú...");
                UtilsGeneral.esperarEnter(scanner);
                break;
            }

            Pedido pedidoSeleccionado;
            try {
                pedidoSeleccionado = pedidoServices.buscarPorId(id);
            } catch (DatoInexistenteException | DatoInvalidoException e) {
                System.out.println(e.getMessage());
                UtilsGeneral.esperarEnter(scanner);
                continue;
            }

            // Seleccion de oocion a editar (Estado o Forma de Pago)
            // Editar Estado
            System.out.println("Estado Actual: " + pedidoSeleccionado.getEstado());
            System.out.print("¿Desea cambiar el estado? (s/n): ");
            String cambiarRol = scanner.nextLine().trim().toLowerCase();
            Estado estado = null;

            if ("s".equals(cambiarRol)) {
                Estado[] estadosActuales = Estado.values();
                System.out.println("Estados disponibles:");

                for (int i = 0; i < estadosActuales.length; i++) {
                    System.out.println((i + 1) + ". " + estadosActuales[i]);
                }

                int opcionEstado = UtilsGeneral.leerEntero(scanner);

                if (opcionEstado >= 1 && opcionEstado <= estadosActuales.length) {
                    estado = estadosActuales[opcionEstado - 1];
                } else {
                    System.out.println("Estado inválido.");
                    UtilsGeneral.esperarEnter(scanner);
                    continue;
                }
            }

            // Editar Forma de Pago
            System.out.println("Forma de Pago Actual: " + pedidoSeleccionado.getFormaPago());
            System.out.print("¿Desea cambiar la forma de pago? (s/n): ");
            String cambiarFormaPago = scanner.nextLine().trim().toLowerCase();
            FormaPago formaPago = null;

            if ("s".equals(cambiarFormaPago)) {
                FormaPago[] formasPagosActuales = FormaPago.values();
                System.out.println("Estados disponibles:");

                for (int i = 0; i < formasPagosActuales.length; i++) {
                    System.out.println((i + 1) + ". " + formasPagosActuales[i]);
                }

                int opcionFormaPago = UtilsGeneral.leerEntero(scanner);

                if (opcionFormaPago >= 1 && opcionFormaPago <= formasPagosActuales.length) {
                    formaPago = formasPagosActuales[opcionFormaPago - 1];
                } else {
                    System.out.println("Estado inválido.");
                    UtilsGeneral.esperarEnter(scanner);
                    continue;
                }
            }

            try {
                pedidoServices.editar(id, estado, formaPago);
                System.out.println("Usuario editado exitosamente.");
            } catch (DatoInexistenteException e) {
                System.out.println("Error al editar el pedido: " + e.getMessage());
                UtilsGeneral.esperarEnter(scanner);
                continue;
            }

            System.out.print("¿Desea editar otro pedido? (s/n): ");
            String repetir = UtilsGeneral.leerString(scanner);

            if (!repetir.equalsIgnoreCase("s")) {
                continue;
            }
        }
    }

    public void opcion_eliminarPedidos() {
        UtilsGeneral.tituloSubcategoria("Eliminar pedido");
        List<Pedido> pedidosActivos = pedidoServices.listarActivos();
        if (pedidosActivos.isEmpty()) {
            System.out.println("No hay pedidos para editar.");
            UtilsGeneral.esperarEnter(scanner);
            return;
        }

        System.out.println("Seleccione el pedido que desea eliminar:");
        UtilsListar.listarPedidos(pedidosActivos);
        System.out.println("0. Salir.");
        System.out.print("Ingrese el id del producto que desea editar o (0) para salir: ");

        Long id = UtilsGeneral.leerId(scanner);
        if (id.equals(0L)) {
            System.out.println("Volviendo al menú...");
            UtilsGeneral.esperarEnter(scanner);
            return;
        }

        Pedido pedidoSeleccionado;
        try {
            pedidoSeleccionado = pedidoServices.buscarPorId(id);
        } catch (DatoInexistenteException | DatoInvalidoException e) {
            System.out.println(e.getMessage());
            UtilsGeneral.esperarEnter(scanner);
            return;
        }

        System.out.println("-----------------------------------");
        System.out.println(
                "ID del pedido a eliminar: " + pedidoSeleccionado.getId());
        System.out.print("¿Está seguro que desea eliminar este pedido? (s/n): ");
        String confirmacion = UtilsGeneral.leerString(scanner);
        if (!confirmacion.equalsIgnoreCase("s")) {
            System.out.println("Eliminación de pedido cancelada.");
            UtilsGeneral.esperarEnter(scanner);
            return;
        }
        try {
            pedidoServices.eliminar(id);
            System.out.println("Pedido eliminado exitosamente.");
        } catch (DatoInvalidoException | DatoInexistenteException e) {
            System.out.println("Error al eliminar el usuario: " + e.getMessage());
            UtilsGeneral.esperarEnter(scanner);
        }
    }

    // METODOS AUXILIARES
    public ItemPedido findItemPedidoByProductoId(Long id, List<ItemPedido> listaItemPedidos) {
        for (ItemPedido itemPedido : listaItemPedidos) {
            if (itemPedido.getIdProducto().equals(id)) {
                return itemPedido;
            }
        }
        return null;
    }

    private void listarProductosConStockProvisional(List<Producto> productos, List<ItemPedido> itemsEnCurso) {
        for (Producto p : productos) {
            int cargado = cantidadYaCargada(p.getId(), itemsEnCurso);
            int stockProvisional = p.getStock() - cargado;

            // Solo mostrar productos que aún tengan stock disponible para agregar
            if (stockProvisional > 0) {
                System.out.println(p.getId() + " - " + p.getNombre()
                        + " | precio: $" + p.getPrecio()
                        + " | disponible: " + stockProvisional
                        + (cargado > 0 ? " (ya cargado: " + cargado + ")" : ""));
            }
        }
    }

    private int cantidadYaCargada(Long idProducto, List<ItemPedido> items) {
        ItemPedido existente = findItemPedidoByProductoId(idProducto, items);
        return existente != null ? existente.getCantidad() : 0;
    }
}
