package services;
import java.util.ArrayList;
import java.util.List;
import entities.Categoria;

public class CategoriaServices {
    private final List<Categoria> categorias = new ArrayList<>();
    private Long idCounter = 1L;

    public Categoria buscarCategoriaPorId(Long id) {
        for (Categoria categoria : categorias) {
            if (categoria.getId().equals(id) && !categoria.isEliminado()) {
                return categoria;
            }
        }
        return null;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public boolean listaCategoriasVacia() {
        return categorias.isEmpty();
    }

    public boolean existeCategoriaPorNombre(String nombre) {
        for (Categoria categoria : categorias) {
            if (categoria.getNombre().equalsIgnoreCase(nombre) && !categoria.isEliminado()) {
                return true;
            }
        }
        return false;
    }

    public void crearCategoria(String nombre, String descripcion) {
        Categoria nuevaCategoria = new Categoria(idCounter++, nombre, descripcion);
        categorias.add(nuevaCategoria);
    }
}
