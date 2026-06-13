package utils;

import java.util.List;

import entities.Categoria;
import entities.Pedido;
import entities.Producto;
import entities.Usuario;
import enums.FormaPago;
import enums.Rol;

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

    public static void listarUsuarios(List<Usuario> usuarios) {
        usuarios.forEach(usuario -> {
            System.out.println(usuario);
            System.out.println("----------------------------");
        });
    }

    public static void listarPedidos(List<Pedido> pedidos) {
        pedidos.forEach(pedido -> {
            System.out.println(pedido);
            System.out.println("----------------------------");
        });
    }

    public static void listarRoles() {
        Rol[] roles = Rol.values();

        for (int i = 0; i < roles.length; i++) {
            System.out.println((i + 1) + ". " + roles[i]);
        }
    }

    public static void listarFormasPago() {
        FormaPago[] formasPago = FormaPago.values();

        for (int i = 0; i < formasPago.length; i++) {
            System.out.println((i + 1) + ". " + formasPago[i]);
        }
    }
}