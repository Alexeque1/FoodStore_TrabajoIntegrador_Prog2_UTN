package entities;

import enums.Rol;
import java.util.ArrayList;
import java.util.List;

public class Usuario extends Base {

    private String nombre;
    private String apellido;
    private String mail;       // único
    private String celular;
    private String contraseña;
    private Rol rol;
    private List<Pedido> pedidos;

    public Usuario() {
        super();
        this.pedidos = new ArrayList<>();
    }

    public Usuario(Long id, String nombre, String apellido, String mail, String celular, String contraseña, Rol rol) {
        super(id);
        this.nombre = nombre;
        this.apellido = apellido;
        this.mail = mail;
        this.celular = celular;
        this.contraseña = contraseña;
        this.rol = rol;
        this.pedidos = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    @Override
    public String toString() {
        return "Usuario{id=" + getId() + ", nombre='" + nombre + "', apellido='" + apellido + "', mail='" + mail + "', rol=" + rol + "}";
    }
}
