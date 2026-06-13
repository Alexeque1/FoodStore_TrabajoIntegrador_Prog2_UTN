package services;

public class ItemPedido {
    private Long idProducto;
    private int cantidad;
    
    public ItemPedido(Long idProducto, int cantidad) {
        this.idProducto = idProducto;
        this.cantidad = cantidad;
    }
    
    public Long getIdProducto() { 
        return idProducto; 
    }

    public int getCantidad() { 
        return cantidad; 
    }

    public void agregarCantidad(int cantidad) { 
        this.cantidad += cantidad; 
    }
}