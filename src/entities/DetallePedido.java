package entities;

import exception.DatoInvalidoException;
import utils.MensajesGenerales;
import utils.MensajesPedidos;
import utils.MensajesProducto;

public class DetallePedido extends Base {

    private int cantidad;
    private Double subtotal;
    private Producto producto;

    public DetallePedido() {
        super();
    }

    public DetallePedido(Long id, int cantidad, Double subtotal, Producto producto) {
        super(id);
        if (cantidad <= 0) {
            throw new DatoInvalidoException(MensajesPedidos.ERROR_CANTIDAD_BAJA);
        }
        if (subtotal == null || subtotal < 0) {
            throw new DatoInvalidoException(MensajesPedidos.ERROR_SUBTOTAL_BAJA);
        }
        if (producto == null) {
            throw new DatoInvalidoException(MensajesProducto.ERROR_PRODUCTO_NULO);
        }
        this.cantidad = cantidad;
        this.subtotal = subtotal;
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    @Override
    public String toString() {
        return "DetallePedido{id=" + getId() + ", cantidad=" + cantidad + ", subtotal=" + subtotal + ", producto="
                + producto + "}";
    }
}
