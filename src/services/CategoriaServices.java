package services;

import java.util.ArrayList;
import java.util.List;
import entities.Categoria;
import utils.UtilsGeneral;
import exception.CategoriaDuplicadaException;
import exception.CategoriaInexistenteException;

public class CategoriaServices {
    private final List<Categoria> categorias = new ArrayList<>();
    private Long idCounter = 1L;

    public Categoria buscarCategoriaPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo.");
        }

        for (Categoria categoria : categorias) {
            if (categoria.getId().equals(id) && !categoria.isEliminado()) {
                return categoria;
            }
        }

        throw new CategoriaInexistenteException("Categoría no encontrada.");
    }

    public List<Categoria> obtenerTodasLasCategorias() {
        return new ArrayList<>(categorias);
    }

    public List<Categoria> obtenerCategoriasActivas() {
        List<Categoria> categoriasActivas = new ArrayList<>();
        for (Categoria categoria : categorias) {
            if (!categoria.isEliminado()) {
                categoriasActivas.add(categoria);
            }
        }
        return categoriasActivas;
    }

    public boolean listaCategoriasEstaVacia() {
        return obtenerCategoriasActivas().isEmpty();
    }

    public boolean existeCategoriaPorNombre(String nombre) {
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

    public void crearCategoria(String nombre, String descripcion) {
        if (!UtilsGeneral.tieneValor(nombre)) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacio.");
        }
        if (descripcion == null) {
            throw new IllegalArgumentException("La descripción no puede ser nula.");
        }
        if (existeCategoriaPorNombre(nombre)) {
            throw new CategoriaDuplicadaException("Ya existe una categoría con ese nombre.");
        }
        
        nombre = nombre.trim();
        descripcion = descripcion.trim();
        Categoria nuevaCategoria = new Categoria(idCounter++, nombre, descripcion);
        categorias.add(nuevaCategoria);
    }

    public void eliminarCategoria(Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("La categoría no puede ser nula.");
        }
        categoria.setEliminado(true);
    }

    public void actualizarCategoria(Categoria categoria, String nombre, String descripcion) {
        if (categoria == null) {
            throw new IllegalArgumentException("La categoría no puede ser nula.");
        }

        if (!UtilsGeneral.tieneValor(nombre)) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío.");
        }

        if (descripcion == null) {
            throw new IllegalArgumentException("La descripción no puede ser nula.");
        }

        nombre = nombre.trim();
        descripcion = descripcion.trim();
        boolean esOtroNombre = !categoria.getNombre().equalsIgnoreCase(nombre);

        if (esOtroNombre && existeCategoriaPorNombre(nombre)) {
            throw new CategoriaDuplicadaException("Ya existe una categoría con ese nombre.");
        }

        categoria.setNombre(nombre);
        categoria.setDescripcion(descripcion);
    }
}
