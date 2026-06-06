package utils;

import java.util.List;

import entities.Categoria;
import entities.Producto;

public class UtilsListar {

    public static void listarCategorias(List<Categoria> categorias) {
        categorias.forEach(categoria -> {
            System.out.println(categoria);
            System.out.println("----------------------------");
        });
    }

    public static void listarProductos(List<Producto> productos) {
        productos.forEach(producto -> {
            System.out.println(producto);
            System.out.println("----------------------------");
        });
    }
}