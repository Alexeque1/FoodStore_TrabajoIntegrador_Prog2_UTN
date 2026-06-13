package utils;

public class MensajesProducto {
    private MensajesProducto() {}
    
    public static final String ERROR_PRECIO_INVALIDO = "El precio debe ser un número positivo.";

    public static final String ERROR_STOCK_INVALIDO = "El stock debe ser un número entero no negativo.";

    public static final String ERROR_DISPONIBILIDAD_NULA = "La disponibilidad no puede ser nula.";

    public static final String ERROR_PRODUCTO_EXISTE = "Ya existe un producto con ese nombre.";

    public static final String ERROR_PRODUCTO_NO_EXISTE = "Producto no encontrado.";

    public static final String ERROR_PRODUCTO_NULO = "El producto no puede ser nulo";

    public static final String ERROR_IMAGEN_NULA = "La imagen no puede ser nula.";

    public static final String ERROR_SIN_STOCK = "No hay stock suficiente";
}
