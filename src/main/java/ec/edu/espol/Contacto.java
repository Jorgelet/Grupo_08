package ec.edu.espol;

public abstract class Contacto {
    protected String nombre;
    protected String direccion;
    protected String telefono;

    public Contacto(String nombre, String direccion, String telefono) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public abstract String tipoContacto();

    @Override
    public String toString() {
        return String.format("[%s] %s - %s", tipoContacto(), nombre, direccion);
    }
}