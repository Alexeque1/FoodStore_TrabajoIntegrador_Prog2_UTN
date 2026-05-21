package integrado.prog2.entities;

import integrado.prog2.enums.Estado;
import integrado.prog2.enums.FormaPago;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pedido extends Base implements Calculable {

    private LocalDate fecha;
    private Estado estado;
    private Double total;
    private FormaPago formaPago;
    // Composición 1:N — los detalles pertenecen y viven dentro del pedido
    private List<DetallePedido> detalles;

    public Pedido() {
        super();
        this.fecha = LocalDate.now();
        this.estado = Estado.PENDIENTE;
        this.total = 0.0;
        this.detalles = new ArrayList<>();
    }

    public Pedido(Long id, LocalDate fecha, Estado estado, FormaPago formaPago) {
        super(id);
        this.fecha = fecha;
        this.estado = estado;
        this.formaPago = formaPago;
        this.total = 0.0;
        this.detalles = new ArrayList<>();
    }

    // -------------------------------------------------------
    // Métodos propios obligatorios
    // -------------------------------------------------------

    /**
     * Agrega un DetallePedido al pedido, o incrementa la cantidad si el
     * producto ya existe. Recalcula el total al finalizar.
     */
    public void addDetallePedido(int cantidad, Double precioUnitario, Producto producto) {
        DetallePedido existente = findeDetallePedidoByProducto(producto);
        if (existente != null) {
            existente.setCantidad(existente.getCantidad() + cantidad);
            existente.setSubtotal(existente.getCantidad() * precioUnitario);
        } else {
            DetallePedido detalle = new DetallePedido();
            detalle.setCantidad(cantidad);
            detalle.setSubtotal(cantidad * precioUnitario);
            detalle.setProducto(producto);
            detalles.add(detalle);
        }
        this.total = calcularTotal();
    }

    /**
     * Busca y devuelve el DetallePedido asociado a un producto.
     * Retorna null si no se encuentra.
     */
    public DetallePedido findeDetallePedidoByProducto(Producto producto) {
        for (DetallePedido detalle : detalles) {
            if (detalle.getProducto().getId().equals(producto.getId())) {
                return detalle;
            }
        }
        return null;
    }

    /**
     * Elimina el DetallePedido asociado a un producto y recalcula el total.
     */
    public void deleteDetallePedidoByProducto(Producto producto) {
        DetallePedido detalle = findeDetallePedidoByProducto(producto);
        if (detalle != null) {
            detalles.remove(detalle);
            this.total = calcularTotal();
        }
    }

    // -------------------------------------------------------
    // Calculable
    // -------------------------------------------------------

    @Override
    public Double calcularTotal() {
        double suma = 0.0;
        for (DetallePedido detalle : detalles) {
            suma += detalle.getSubtotal();
        }
        return suma;
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
        return detalles;
    }

    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }

    @Override
    public String toString() {
        return "Pedido{id=" + getId() + ", fecha=" + fecha + ", estado=" + estado + ", total=" + total + ", formaPago=" + formaPago + "}";
    }
}
