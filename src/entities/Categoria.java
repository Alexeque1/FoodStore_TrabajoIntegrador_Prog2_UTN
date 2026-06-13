package entities;

import java.util.ArrayList;
import java.util.List;

public class Categoria extends Base {

    private String nombre;
    private String descripcion;

    public Categoria() {
        super();
    }

    public Categoria(Long id, String nombre, String descripcion) {
        super(id);
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "ID: " + getId() + "\n" +
               "Nombre: " + nombre + "\n" +
               "Descripción: " + descripcion + "\n";
    }
}
