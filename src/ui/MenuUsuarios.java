package ui;

import java.util.List;
import java.util.Scanner;

import entities.Usuario;
import enums.Rol;
import exception.DatoDuplicadaException;
import exception.DatoInexistenteException;
import exception.DatoInvalidoException;
import services.UsuarioServices;
import utils.UtilsGeneral;
import utils.UtilsListar;
import utils.UtilsMenu;

public class MenuUsuarios {
    private final Scanner scanner;
    private final UsuarioServices usuarioServices;

    public MenuUsuarios(Scanner scanner, UsuarioServices usuarioServices) {
        this.scanner = scanner;
        this.usuarioServices = usuarioServices;
    }

    public void iniciar() {
        boolean volver = false;
        while (!volver) {
            mostrar();
            int opcion = UtilsMenu.leerOpcion(scanner);
            switch (opcion) {
                case 1:
                    opcion_listarUsuarios();
                    break;
                case 2:
                    opcion_crearUsuario();
                    break;
                case 3:
                    opcion_editarUsuario();
                    break;
                case 4:
                    opcion_eliminarUsuario();
                    break;
                case 0:
                    volver = true;
                    System.out.println("Volviendo al menu principal...");
                    break;
                default:
                    System.out.println("Opcion no valida. Por favor, intente nuevamente.");
            }
        }
    }

    public void mostrar() {
        System.out.println("=====================================");
        System.out.println("||       GESTION DE USUARIOS       ||");
        System.out.println("=====================================");
        System.out.println("|| 1. Listar usuarios              ||");
        System.out.println("|| 2. Crear usuario                ||");
        System.out.println("|| 3. Editar usuario               ||");
        System.out.println("|| 4. Eliminar usuario             ||");
        System.out.println("|| 0. Volver al menu principal     ||");
        System.out.println("=====================================");
        System.out.print("Seleccione una opcion: ");
    }

    public void opcion_listarUsuarios() {
        UtilsGeneral.tituloSubcategoria("Listar usuarios");
        List<Usuario> usuariosActivos = usuarioServices.listarActivos();
        if (usuariosActivos.isEmpty()) {
            System.out.println("No hay usuarios para mostrar.");
        } else {
            UtilsListar.listarUsuarios(usuariosActivos);
        }
        UtilsGeneral.esperarEnter(scanner);
    }

    public void opcion_crearUsuario() {
        UtilsGeneral.tituloSubcategoria("Crear usuario");
        System.out.print("Ingrese el nombre del usuario: ");
        String nombre = UtilsGeneral.leerString(scanner);
        System.out.println("-------------------------------");

        System.out.print("Ingrese el apellido del usuario: ");
        String apellido = UtilsGeneral.leerString(scanner);
        System.out.println("-------------------------------");

        System.out.print("Ingrese el mail del usuario: ");
        String mail = UtilsGeneral.leerEmail(scanner);
        System.out.println("-------------------------------");

        System.out.print("Ingrese el celular del usuario: ");
        String celular = UtilsGeneral.leerString(scanner);
        System.out.println("-------------------------------");

        System.out.print("Ingrese la contraseña del usuario: ");
        String contrasena = UtilsGeneral.leerString(scanner);
        System.out.println("-------------------------------");

        Rol rol = null;
        while (true) {
            System.out.println("Ingrese el rol del usuario: ");
            Rol[] roles = Rol.values();
            UtilsListar.listarRoles();
            int rolOpcion = UtilsGeneral.leerEntero(scanner);
            if (rolOpcion < 1 || rolOpcion > roles.length) {
                System.out.println("Rol inválido. Por favor, intente nuevamente.");
                continue;
            }
            rol = roles[rolOpcion - 1];
            break;
        }

        try {
            Long id = usuarioServices.crear(nombre, apellido, mail, celular, contrasena, rol);
            System.out.println("Usuario creado correctamente con ID: " + id);
            UtilsGeneral.esperarEnter(scanner);
        } catch (DatoInvalidoException | DatoDuplicadaException e) {
            System.out.println("Error al crear el usuario: " + e.getMessage());
            UtilsGeneral.esperarEnter(scanner);
        }
    }

    public void opcion_editarUsuario() {
        UtilsGeneral.tituloSubcategoria("Editar usuario");

        List<Usuario> usuariosActivos = usuarioServices.listarActivos();
        if (usuariosActivos.isEmpty()) {
            System.out.println("No hay usuarios para editar.");
            UtilsGeneral.esperarEnter(scanner);
            return;
        }

        UtilsListar.listarUsuarios(usuariosActivos);
        System.out.println("0. Salir");
        System.out.print("Ingrese el id del usuario a editar o ingrese cero (0) para salir: ");
        Long id = UtilsGeneral.leerId(scanner);
        if (id.equals(0L)) {
            System.out.println("Volviendo al menú de usuarios...");
            return;
        }

        Usuario usuarioSeleccionado;
        try {
            usuarioSeleccionado = usuarioServices.buscarPorId(id);
        } catch (DatoInexistenteException | DatoInvalidoException e) {
            System.out.println(e.getMessage());
            UtilsGeneral.esperarEnter(scanner);
            return;
        }

        System.out.println("Nombre actual: " + usuarioSeleccionado.getNombre());
        System.out.print("Nuevo nombre (Enter para mantener): ");
        String nombre = scanner.nextLine().trim();
        if (nombre.isBlank()) {
            nombre = null;
        }

        System.out.println("Apellido actual: " + usuarioSeleccionado.getApellido());
        System.out.print("Nuevo apellido (Enter para mantener): ");
        String apellido = scanner.nextLine().trim();
        if (apellido.isBlank()) {
            apellido = null;
        }

        System.out.println("Mail actual: " + usuarioSeleccionado.getMail());
        System.out.print("Nuevo mail (Enter para mantener): ");
        String mail = scanner.nextLine().trim();
        if (mail.isBlank()) {
            mail = null;
        }

        System.out.println("Celular actual: " + usuarioSeleccionado.getCelular());
        System.out.print("Nuevo celular (Enter para mantener): ");
        String celular = scanner.nextLine().trim();
        if (celular.isBlank()) {
            celular = null;
        }

        System.out.println("Contraseña actual: ********");
        System.out.print("Nueva contraseña (Enter para mantener): ");
        String contraseña = scanner.nextLine().trim();
        if (contraseña.isBlank()) {
            contraseña = null;
        }

        System.out.println("Rol actual: " + usuarioSeleccionado.getRol());
        System.out.print("¿Desea cambiar el rol? (s/n): ");
        String cambiarRol = scanner.nextLine().trim().toLowerCase();
        Rol rol = null;

        if ("s".equals(cambiarRol)) {
            Rol[] roles = Rol.values();
            System.out.println("Roles disponibles:");

            for (int i = 0; i < roles.length; i++) {
                System.out.println((i + 1) + ". " + roles[i]);
            }

            int opcionRol = UtilsGeneral.leerEntero(scanner);

            if (opcionRol >= 1 && opcionRol <= roles.length) {
                rol = roles[opcionRol - 1];
            } else {
                System.out.println("Rol inválido.");
                UtilsGeneral.esperarEnter(scanner);
                return;
            }
        }

        try {
            usuarioServices.editar(
                    id,
                    nombre,
                    apellido,
                    mail,
                    celular,
                    contraseña,
                    rol);

            System.out.println("Usuario editado exitosamente.");

        } catch (DatoInvalidoException | DatoDuplicadaException | DatoInexistenteException e) {
            System.out.println("Error al editar el usuario: " + e.getMessage());
            UtilsGeneral.esperarEnter(scanner);
        }
    }

    public void opcion_eliminarUsuario() {
        UtilsGeneral.tituloSubcategoria("Eliminar usuarios");
        List<Usuario> usuariosActivos = usuarioServices.listarActivos();
        if (usuariosActivos.isEmpty()) {
            System.out.println("No hay usuarios para eliminar.");
            UtilsGeneral.esperarEnter(scanner);
            return;
        }
        UtilsListar.listarUsuarios(usuariosActivos);
        System.out.println("0. Salir");
        System.out.print("Ingrese el id del usuario a eliminar o ingrese cero (0) para salir: ");
        Long id = UtilsGeneral.leerId(scanner);
        if (id.equals(0L)) {
            System.out.println("Volviendo al menú de usuarios...");
            return;
        }
        Usuario usuarioSeleccionado;
        try {
            usuarioSeleccionado = usuarioServices.buscarPorId(id);
        } catch (DatoInexistenteException | DatoInvalidoException e) {
            System.out.println(e.getMessage());
            UtilsGeneral.esperarEnter(scanner);
            return;
        }

        System.out.println("-----------------------------------");
        System.out.println(
                "Usuario a eliminar: " + usuarioSeleccionado.getNombre() + " " + usuarioSeleccionado.getApellido());
        System.out.print("¿Está seguro que desea eliminar este usuario? (s/n): ");
        String confirmacion = UtilsGeneral.leerString(scanner);
        if (!confirmacion.equalsIgnoreCase("s")) {
            System.out.println("Eliminación de usuario cancelada.");
            UtilsGeneral.esperarEnter(scanner);
            return;
        }
        try {
            usuarioServices.eliminar(id);
            System.out.println("Usuario eliminado exitosamente.");
        } catch (DatoInvalidoException | DatoInexistenteException e) {
            System.out.println("Error al eliminar el usuario: " + e.getMessage());
            UtilsGeneral.esperarEnter(scanner);
        }
    }

}
