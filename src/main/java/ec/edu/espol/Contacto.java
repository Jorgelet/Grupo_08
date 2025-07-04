package ec.edu.espol;

import java.util.Objects;

public class Contacto {

    private String nombre;
    private String apellido;
    private String tipo;
    private String numeroTelefono;
    private String direccion;
    private ListaCircularDoble<Atributo> atributos;
    private ListaCircularDoble<Foto> fotos;
    private ListaCircularDoble<Contacto> asociados = new ListaCircularDoble<>();

    public Contacto(String tipo, String nombre, String apellido, String numeroTelefono, String direccion) {
        this.tipo = tipo;
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.numeroTelefono = numeroTelefono;
        this.atributos = new ListaCircularDoble<>();
        this.fotos = new ListaCircularDoble<>();
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public ListaCircularDoble<Atributo> getAtributos() {
        return atributos;
    }

    public ListaCircularDoble<Foto> getFotos() {
        return fotos;
    }

    public ListaCircularDoble<Contacto> getAsociados() {
        return asociados;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void agregarAtributo(Atributo a) {
        atributos.agregar(a);
    }

    public void agregarFoto(Foto f) {
        fotos.agregar(f);
    }

    private boolean asociadosContiene(Contacto c) {
        for (Contacto a : asociados) {
            if (a.equals(c))
                return true;
        }
        return false;
    }

    public void agregarAsociado(Contacto c) {
        if (c != null && !this.equals(c) && !asociadosContiene(c)) {
            asociados.agregar(c);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Contacto contacto = (Contacto) o;
        return Objects.equals(numeroTelefono.toLowerCase(), contacto.numeroTelefono.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(numeroTelefono.toLowerCase());
    }

    @Override
    public String toString() {
        return tipo + ": " + nombre;
    }

    public void imprimirDetalles() {
        System.out.println("Nombre: " + nombre + " " + apellido);
        System.out.println("Tipo: " + tipo);
        System.out.println("Número de Teléfono: " + numeroTelefono);
        System.out.println("Dirección: " + direccion);
        System.out.println("Atributos:");
        atributos.imprimir();
        System.out.println("Fotos:");
        fotos.imprimir();
        System.out.println("Contactos asociados:");
        for (Contacto a : asociados) {
            System.out.println(" - " + a.getNombre() + " " + a.getApellido());
        }
    }
}