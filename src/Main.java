package integrado.prog2;

import integrado.prog2.entities.*;
import integrado.prog2.enums.*;

public class Main {

    public static void main(String[] args) {

        // --- Categoría ---
        Categoria categoria = new Categoria(1L, "Bebidas", "Bebidas frías y calientes");

        // --- Producto ---
        Producto producto = new Producto(1L, "Café con leche", 1500.0, "Café con leche caliente", 100, "cafe.jpg", true, categoria);
        categoria.getProductos().add(producto);

        // --- Usuario ---
        Usuario usuario = new Usuario(1L, "Alexander", "Apellido", "alex@mail.com", "1122334455", "password123", Rol.USUARIO);

        // --- Pedido ---
        Pedido pedido = new Pedido(1L, java.time.LocalDate.now(), Estado.PENDIENTE, FormaPago.EFECTIVO);
        pedido.addDetallePedido(2, producto.getPrecio(), producto);

        usuario.getPedidos().add(pedido);

        // --- Output de prueba ---
        System.out.println(usuario);
        System.out.println(pedido);
        System.out.println("Total del pedido: $" + pedido.calcularTotal());
    }
}
