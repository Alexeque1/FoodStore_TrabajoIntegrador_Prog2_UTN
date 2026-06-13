package services;

import java.util.ArrayList;
import java.util.List;

import entities.Usuario;
import enums.Rol;
import exception.DatoDuplicadaException;
import exception.DatoInexistenteException;
import exception.DatoInvalidoException;
import utils.MensajesGenerales;
import utils.MensajesUsuarios;
import utils.UtilsGeneral;

public class UsuarioServices {
    private final List<Usuario> usuarios = new ArrayList<>();
    private Long idCounter = 1L;

    public Usuario buscarPorId(Long id) {
        if (id == null) {
            throw new DatoInvalidoException(MensajesGenerales.ERROR_ID_NULO);
        }

        for (Usuario usuario : usuarios) {
            if (usuario.getId().equals(id) && !usuario.isEliminado()) {
                return usuario;
            }
        }

        throw new DatoInexistenteException(MensajesUsuarios.ERROR_USUARIO_NO_EXISTE);
    }

    public List<Usuario> listarTodos() {
        return new ArrayList<>(usuarios);
    }

    public List<Usuario> listarActivos() {
        List<Usuario> usuariosActivos = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            if (!usuario.isEliminado()) {
                usuariosActivos.add(usuario);
            }
        }
        return usuariosActivos;
    }

    public Long crear(String nombre, String apellido, String mail, String celular, String contraseña, Rol rol) {
        if (!UtilsGeneral.tieneValor(nombre)) {
            throw new DatoInvalidoException(MensajesGenerales.ERROR_NOMBRE_NULO);
        }
        if (!UtilsGeneral.tieneValor(apellido)) {
            throw new DatoInvalidoException(MensajesUsuarios.ERROR_APELLIDO_NULO);
        }

        if (!UtilsGeneral.tieneValor(mail)) {
            throw new DatoInvalidoException(MensajesUsuarios.ERROR_MAIL_NULO);
        }

        if (!UtilsGeneral.tieneValor(celular)) {
            throw new DatoInvalidoException(MensajesUsuarios.ERROR_CELULAR_NULO);
        }

        if (!UtilsGeneral.tieneValor(contraseña)) {
            throw new DatoInvalidoException(MensajesUsuarios.ERROR_CONTRASENA_NULA);
        }
        if (rol == null) {
            throw new DatoInvalidoException(MensajesUsuarios.ERROR_ROL_NULO);
        }
        nombre = nombre.trim();
        apellido = apellido.trim();
        mail = mail.trim();
        celular = celular.trim();
        contraseña = contraseña.trim();

        for (Usuario usuario : usuarios) {
            if (usuario.getMail().equalsIgnoreCase(mail) && !usuario.isEliminado()) {
                throw new DatoDuplicadaException(MensajesUsuarios.ERROR_USUARIO_EXISTE);
            }
        }
        Long id = idCounter++;
        Usuario nuevoUsuario = new Usuario(id, nombre, apellido, mail, celular, contraseña, rol);
        usuarios.add(nuevoUsuario);

        return id;
    }

    public void editar(Long id, String nombre, String apellido, String mail, String celular, String contraseña,
            Rol rol) {
        if ((nombre == null)
                && (apellido == null)
                && (mail == null)
                && (celular == null)
                && (contraseña == null)
                && (rol == null)) {

            throw new DatoInvalidoException(
                    MensajesGenerales.ERROR_NO_CAMBIOS);
        }

        Usuario usuario = buscarPorId(id);

        if (nombre != null) {
            if (!UtilsGeneral.tieneValor(nombre)) {
                throw new DatoInvalidoException(MensajesGenerales.ERROR_NOMBRE_NULO);
            }

            usuario.setNombre(nombre.trim());
        }

        if (apellido != null) {

            if (!UtilsGeneral.tieneValor(apellido)) {
                throw new DatoInvalidoException(MensajesUsuarios.ERROR_APELLIDO_NULO);
            }

            usuario.setApellido(apellido.trim());
        }

        if (mail != null) {

            if (!UtilsGeneral.tieneValor(mail)) {
                throw new DatoInvalidoException(MensajesUsuarios.ERROR_MAIL_NULO);
            }

            mail = mail.trim();

            boolean esOtroMail = !usuario.getMail().equalsIgnoreCase(mail);

            if (esOtroMail) {
                for (Usuario otroUsuario : listarActivos()) {
                    if (!otroUsuario.getId().equals(usuario.getId())
                            && otroUsuario.getMail().equalsIgnoreCase(mail)) {
                        throw new DatoDuplicadaException(
                                MensajesUsuarios.ERROR_USUARIO_EXISTE);
                    }
                }
            }

            usuario.setMail(mail);
        }

        if (celular != null) {
            usuario.setCelular(celular.trim());
        }

        if (contraseña != null) {

            if (!UtilsGeneral.tieneValor(contraseña)) {
                throw new DatoInvalidoException(
                        MensajesUsuarios.ERROR_CONTRASENA_NULA);
            }

            usuario.setContrasena(contraseña.trim());
        }

        if (rol != null) {
            usuario.setRol(rol);
        }
    }

    public void eliminar(Long id) {
        Usuario usuario = buscarPorId(id);
        usuario.setEliminado(true);
    }
}
