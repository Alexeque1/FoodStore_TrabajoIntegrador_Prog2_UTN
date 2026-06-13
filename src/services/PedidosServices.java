package services;

import java.util.ArrayList;
import java.util.List;

import entities.Pedido;
import entities.Producto;
import entities.Usuario;
import enums.Estado;
import enums.FormaPago;
import exception.DatoInexistenteException;
import exception.DatoInvalidoException;
import exception.StockInsuficienteException;
import utils.MensajesGenerales;
import utils.MensajesPedidos;

public class PedidosServices {
    private final List<Pedido> pedidos = new ArrayList<>();
    private Long idCounter = 1L;

    private UsuarioServices usuarioService;
    private ProductoServices productoServices;

    public PedidosServices(UsuarioServices usuarioServices, ProductoServices productoServices) {
        this.usuarioService = usuarioServices;
        this.productoServices = productoServices;
    }

    public Pedido buscarPorId(Long id) {
        if (id == null) {
            throw new DatoInvalidoException(MensajesGenerales.ERROR_ID_NULO);
        }

        for (Pedido pedido : pedidos) {
            if (pedido.getId().equals(id) && !pedido.isEliminado()) {
                return pedido;
            }
        }

        throw new DatoInexistenteException(MensajesPedidos.ERROR_PEDIDO_NO_EXISTE);
    }

    public List<Pedido> listarTodos() {
        return new ArrayList<>(pedidos);
    }

    public List<Pedido> listarActivos() {
        List<Pedido> pedidosActivos = new ArrayList<>();
        for (Pedido pedido : pedidos) {
            if (!pedido.isEliminado()) {
                pedidosActivos.add(pedido);
            }
        }
        return pedidosActivos;
    }

    public Pedido crear(Long idUsuario, List<ItemPedido> listaItems, FormaPago formaPago) {
        Usuario usuario = usuarioService.buscarPorId(idUsuario);

        if (listaItems == null || listaItems.isEmpty()) {
            throw new DatoInvalidoException("El pedido debe tener al menos un item");
        }
        if (formaPago == null) {
            throw new DatoInvalidoException("Forma de pago obligatoria");
        }

        List<Producto> productosValidados = new ArrayList<>();
        List<Integer> cantidadesValidadas = new ArrayList<>();

        for (ItemPedido item : listaItems) {
            Producto producto = productoServices.buscarPorId(item.getIdProducto());

            if (item.getCantidad() <= 0) {
                throw new DatoInvalidoException("Cantidad debe ser mayor a 0");
            }
            if (producto.getStock() < item.getCantidad()) {
                throw new StockInsuficienteException(
                        "Stock insuficiente para " + producto.getNombre() + ". Disponible: " + producto.getStock());
            }

            productosValidados.add(producto);
            cantidadesValidadas.add(item.getCantidad());
        }

        Pedido pedido = new Pedido(usuario);

        for (int i = 0; i < productosValidados.size(); i++) {
            Producto p = productosValidados.get(i);
            int c = cantidadesValidadas.get(i);
            pedido.addDetallePedido(c, p.getPrecio(), p);
        }
        pedido.setFormaPago(formaPago);
        pedido.calcularTotal();

        for (int i = 0; i < productosValidados.size(); i++) {
            Producto p = productosValidados.get(i);
            int c = cantidadesValidadas.get(i);
            p.setStock(p.getStock() - c);
        }
        pedido.setId(idCounter++);
        pedidos.add(pedido);

        return pedido;
    }

    public void editar(Long idPedido, Estado estado, FormaPago formaPago) {
        if ((estado == null) && (formaPago == null)) {
            throw new DatoInvalidoException(
                    MensajesGenerales.ERROR_NO_CAMBIOS);
        }

        Pedido pedido = buscarPorId(idPedido);

        if (estado != null) {
            pedido.setEstado(estado);
        }

        if (formaPago != null) {
            pedido.setFormaPago(formaPago);
        }
    }

    public void eliminar(Long id) {
        Pedido pedido = buscarPorId(id);
        pedido.setEliminado(true);
    }
}
