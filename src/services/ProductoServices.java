package services;

import entities.Categoria;
import entities.Producto;
import java.util.ArrayList;
import java.util.List;

public class ProductoServices {

    private final List<Producto> productos = new ArrayList<>();
    private Long idCounter = 1L;

    public Producto buscarProductoPorId(Long id) {
        for (Producto producto : productos) {
            if (producto.getId().equals(id) && !producto.isEliminado()) {
                return producto;
            }
        }
        return null;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public List<Producto> getProductosActivos() {
        List<Producto> activos = new ArrayList<>();
        for (Producto producto : productos) {
            if (!producto.isEliminado()) {
                activos.add(producto);
            }
        }
        return activos;
    }

    public boolean listaProductosVacia() {
        return getProductosActivos().isEmpty();
    }

    public List<Producto> getProductosPorCategoria(Categoria categoria) {
        List<Producto> resultado = new ArrayList<>();
        for (Producto producto : productos) {
            if (!producto.isEliminado() && producto.getCategoria().getId().equals(categoria.getId())) {
                resultado.add(producto);
            }
        }
        return resultado;
    }

    public void crearProducto(String nombre, Double precio, String descripcion, int stock, String imagen, Boolean disponible, Categoria categoria) {
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        Producto nuevo = new Producto(idCounter++, nombre, precio, descripcion, stock, imagen, disponible, categoria);
        productos.add(nuevo);
    }

    public void editarProducto(Long id, String nombre, Double precio, String descripcion, int stock, String imagen, Boolean disponible, Categoria categoria) {
        Producto producto = buscarProductoPorId(id);
        if (producto == null) {
            throw new IllegalArgumentException("Producto no encontrado.");
        }
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        producto.setNombre(nombre);
        producto.setPrecio(precio);
        producto.setDescripcion(descripcion);
        producto.setStock(stock);
        producto.setImagen(imagen);
        producto.setDisponible(disponible);
        producto.setCategoria(categoria);
    }

    public void eliminarProducto(Long id) {
        Producto producto = buscarProductoPorId(id);
        if (producto == null) {
            throw new IllegalArgumentException("Producto no encontrado.");
        }
        producto.setEliminado(true);
    }
}