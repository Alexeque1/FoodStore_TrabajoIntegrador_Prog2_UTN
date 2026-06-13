package entities;

import enums.Estado;
import enums.FormaPago;
import exception.DatoInexistenteException;
import exception.DatoInvalidoException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pedido extends Base implements Calculable {

    private LocalDate fecha;
    private Estado estado;
    private Double total;
    private FormaPago formaPago;
    private List<DetallePedido> detalles;
    private Usuario usuario;

    private Long idCounter = 1L;

    public Pedido(Usuario usuario) {
        super(null);
        this.fecha = LocalDate.now();
        this.estado = Estado.PENDIENTE;
        this.total = 0.0;
        this.detalles = new ArrayList<>();
        this.formaPago = FormaPago.EFECTIVO; // Valor por defecto
        this.usuario = usuario;
    }

    public void addDetallePedido(int cantidad, Double precioUnitario, Producto producto) {
        DetallePedido findeDetalle = findDetallePedidoByProducto(producto);
        if (findeDetalle != null) {
            int nuevaCantidad = findeDetalle.getCantidad() + cantidad;
            findeDetalle.setCantidad(nuevaCantidad);
            findeDetalle.setSubtotal(nuevaCantidad * precioUnitario);
        } else {
            DetallePedido nuevoDetalle = new DetallePedido(idCounter++, cantidad, cantidad * precioUnitario, producto);
            detalles.add(nuevoDetalle);
        }
    }

    public DetallePedido findDetallePedidoByProducto(Producto producto) {
        if (producto == null) {
            throw new DatoInvalidoException("El producto no puede ser nulo");
        }

        for (DetallePedido detalle : detalles) {
            if (!detalle.isEliminado()
                    && detalle.getProducto().getId().equals(producto.getId())) {
                return detalle;
            }
        }

        return null;
    }

    public void deleteDetallePedidoByProducto(Producto producto) {
        DetallePedido detalle = findDetallePedidoByProducto(producto);
        if (detalle != null) {
            detalle.setEliminado(true);
        }
        calcularTotal();
    }

    @Override
    public void calcularTotal() {
        double suma = 0.0;
        for (DetallePedido detalle : detalles) {
            if (!detalle.isEliminado()) {
                suma += detalle.getSubtotal();
            }
        }
        this.total = suma;
    }

    // -------------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------------

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }

    public List<DetallePedido> getDetalles() {
        return new ArrayList<>(detalles);
    }

    @Override
    public String toString() {
        return "Pedido{id=" + getId() + ", fecha=" + fecha + ", estado=" + estado + ", total=" + total + ", formaPago="
                + formaPago + "}";
    }
}
