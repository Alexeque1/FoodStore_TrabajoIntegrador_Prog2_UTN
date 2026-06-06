package services;

import java.util.ArrayList;
import java.util.List;
import entities.Categoria;
import utils.MensajesCategoria;
import utils.MensajesGenerales;
import utils.UtilsGeneral;
import exception.DatoDuplicadaException;
import exception.DatoInexistenteException;
import exception.DatoInvalidoException;

public class CategoriaServices {
    private final List<Categoria> categorias = new ArrayList<>();
    private Long idCounter = 1L;

    public Categoria buscarPorId(Long id) {
        if (id == null) {
            throw new DatoInvalidoException(MensajesGenerales.ERROR_ID_NULO);
        }

        for (Categoria categoria : categorias) {
            if (categoria.getId().equals(id) && !categoria.isEliminado()) {
                return categoria;
            }
        }

        throw new DatoInexistenteException(MensajesCategoria.CATEGORIA_NO_EXISTE);
    }

    public List<Categoria> listarTodas() {
        return new ArrayList<>(categorias);
    }

    public List<Categoria> listarActivas() {
        List<Categoria> categoriasActivas = new ArrayList<>();
        for (Categoria categoria : categorias) {
            if (!categoria.isEliminado()) {
                categoriasActivas.add(categoria);
            }
        }
        return categoriasActivas;
    }

    public boolean existePorNombre(String nombre) {
        if (!UtilsGeneral.tieneValor(nombre)) {
            return false;
        }

        for (Categoria categoria : categorias) {
            if (categoria.getNombre().equalsIgnoreCase(nombre) && !categoria.isEliminado()) {
                return true;
            }
        }
        return false;
    }

    public void crear(String nombre, String descripcion) {
        if (!UtilsGeneral.tieneValor(nombre)) {
            throw new DatoInvalidoException(MensajesGenerales.ERROR_NOMBRE_NULO);
        }
        if (descripcion == null) {
            throw new DatoInvalidoException(MensajesGenerales.ERROR_DESCRIPCION_NULA);
        }

        nombre = nombre.trim();
        descripcion = descripcion.trim();
        if (existePorNombre(nombre)) {
            throw new DatoDuplicadaException(MensajesCategoria.CATEGORIA_EXISTE);
        }
        Categoria nuevaCategoria = new Categoria(idCounter++, nombre, descripcion);
        categorias.add(nuevaCategoria);
    }

    public void eliminar(Long id) {
        Categoria categoria = buscarPorId(id);
        categoria.setEliminado(true);
    }

    public void editar(Long id, String nombre, String descripcion) {
        Categoria categoria = buscarPorId(id);

        if (categoria.getProductos() != null && !categoria.getProductos().isEmpty()) {
            throw new DatoInvalidoException(MensajesCategoria.CATEGORIA_CON_PRODUCTOS);
        }

        if (!UtilsGeneral.tieneValor(nombre)) {
            throw new DatoInvalidoException(MensajesGenerales.ERROR_NOMBRE_NULO);
        }

        if (descripcion == null) {
            throw new DatoInvalidoException(MensajesGenerales.ERROR_DESCRIPCION_NULA);
        }

        String nombreLimpio = nombre.trim();
        String descripcionLimpia = descripcion.trim();

        boolean esOtroNombre = !categoria.getNombre().equalsIgnoreCase(nombreLimpio);

        if (categoria.getNombre().equalsIgnoreCase(nombreLimpio)
                && categoria.getDescripcion().equals(descripcionLimpia)) {
            throw new DatoInvalidoException(MensajesGenerales.ERROR_NO_CAMBIOS);
        }

        if (esOtroNombre && existePorNombre(nombreLimpio)) {
            throw new DatoDuplicadaException(MensajesCategoria.CATEGORIA_EXISTE);
        }

        categoria.setNombre(nombreLimpio);
        categoria.setDescripcion(descripcionLimpia);
    }
}
