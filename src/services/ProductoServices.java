package services;

import entities.Categoria;
import entities.Producto;
import exception.DatoDuplicadaException;
import exception.DatoInexistenteException;
import exception.DatoInvalidoException;
import utils.MensajesCategoria;
import utils.MensajesGenerales;
import utils.MensajesProducto;
import utils.UtilsGeneral;

import java.util.ArrayList;
import java.util.List;

public class ProductoServices {

    private final List<Producto> productos = new ArrayList<>();
    private Long idCounter = 1L;
    private final CategoriaServices categoriaService;

    public ProductoServices(CategoriaServices categoriaService) {
        this.categoriaService = categoriaService;
    }

    public Producto buscarPorId(Long id) {
        if (id == null) {
            throw new DatoInvalidoException(MensajesGenerales.ERROR_ID_NULO);
        }

        for (Producto producto : productos) {
            if (producto.getId().equals(id) && !producto.isEliminado()) {
                return producto;
            }
        }

        throw new DatoInexistenteException(MensajesProducto.ERROR_PRODUCTO_NO_EXISTE);
    }

    public List<Producto> listarTodos() {
        return new ArrayList<>(productos);
    }

    public List<Producto> listarActivos() {
        List<Producto> productosActivos = new ArrayList<>();
        for (Producto producto : productos) {
            if (!producto.isEliminado()) {
                productosActivos.add(producto);
            }
        }
        return productosActivos;
    }

    public List<Producto> listarPorCategoria(Categoria categoria) {
        List<Producto> productosCategoria = new ArrayList<>();
        for (Producto producto : productos) {
            if (!producto.isEliminado() && producto.getCategoria() != null
                    && producto.getCategoria().equals(categoria)) {
                productosCategoria.add(producto);
            }
        }
        return productosCategoria;
    }

    public List<Categoria> obtenerCategoriasConProductos() {
        List<Categoria> categorias = new ArrayList<>();

        for (Producto producto : productos) {
            if (!producto.isEliminado()
                    && !categorias.contains(producto.getCategoria())) {
                categorias.add(producto.getCategoria());
            }
        }

        return categorias;
    }

    public boolean existePorNombre(String nombre) {
        if (!UtilsGeneral.tieneValor(nombre)) {
            return false;
        }

        for (Producto producto : productos) {
            if (producto.getNombre().equalsIgnoreCase(nombre) && !producto.isEliminado()) {
                return true;
            }
        }
        return false;
    }

    public void crear(String nombre, String descripcion, Double precio, int stock, String imagen, Boolean disponible,
            Categoria categoria) {
        if (!UtilsGeneral.tieneValor(nombre)) {
            throw new DatoInvalidoException(MensajesGenerales.ERROR_NOMBRE_NULO);
        }
        if (precio == null || precio <= 0) {
            throw new DatoInvalidoException(MensajesProducto.ERROR_PRECIO_INVALIDO);
        }
        if (stock < 0) {
            throw new DatoInvalidoException(MensajesProducto.ERROR_STOCK_INVALIDO);
        }
        if (imagen == null) {
            throw new DatoInvalidoException(MensajesProducto.ERROR_IMAGEN_NULA);
        }
        if (disponible == null) {
            throw new DatoInvalidoException(MensajesProducto.ERROR_DISPONIBILIDAD_NULA);
        }
        if (categoria == null) {
            throw new DatoInvalidoException(MensajesCategoria.CATEGORIA_NULA);
        }
        if (categoria.isEliminado()) {
            throw new DatoInvalidoException(MensajesCategoria.CATEGORIA_ELIMINADA);
        }

        nombre = nombre.trim();
        descripcion = UtilsGeneral.tieneValor(descripcion) ? descripcion.trim() : "Sin descripcion";
        imagen = imagen.trim();
        if (existePorNombre(nombre)) {
            throw new DatoDuplicadaException(MensajesProducto.ERROR_PRODUCTO_EXISTE);
        }
        Producto nuevoProducto = new Producto(idCounter++, nombre, descripcion, precio, stock, imagen, disponible,
                categoria);
        productos.add(nuevoProducto);
    }

    public void editar(
            Long id,
            String nombre,
            String descripcion,
            Double precio,
            Integer stock,
            String imagen,
            Boolean disponible,
            Categoria categoria) {

        Producto producto = buscarPorId(id);

        if (nombre != null) {
            if (!UtilsGeneral.tieneValor(nombre)) {
                throw new DatoInvalidoException(MensajesGenerales.ERROR_NOMBRE_NULO);
            }

            nombre = nombre.trim();

            if (!producto.getNombre().equalsIgnoreCase(nombre)
                    && existePorNombre(nombre)) {
                throw new DatoDuplicadaException(MensajesProducto.ERROR_PRODUCTO_EXISTE);
            }

            producto.setNombre(nombre);
        }

        if (descripcion != null) {
            descripcion = UtilsGeneral.tieneValor(descripcion)
                    ? descripcion.trim()
                    : "Sin descripcion";

            producto.setDescripcion(descripcion);
        }

        if (precio != null) {
            if (precio <= 0) {
                throw new DatoInvalidoException(MensajesProducto.ERROR_PRECIO_INVALIDO);
            }

            producto.setPrecio(precio);
        }

        if (stock != null) {
            if (stock < 0) {
                throw new DatoInvalidoException(MensajesProducto.ERROR_STOCK_INVALIDO);
            }

            producto.setStock(stock);
        }

        if (imagen != null) {
            producto.setImagen(imagen.trim());
        }

        if (disponible != null) {
            producto.setDisponible(disponible);
        }

        if (categoria != null) {

            if (categoria.isEliminado()) {
                throw new DatoInvalidoException(MensajesCategoria.CATEGORIA_ELIMINADA);
            }

            producto.setCategoria(categoria);
        }
    }

    public void eliminar(Long id) {
        Producto producto = buscarPorId(id);
        producto.setEliminado(true);
    }
}