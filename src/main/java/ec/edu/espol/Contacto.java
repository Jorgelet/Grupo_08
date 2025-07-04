package ec.edu.espol;

import java.util.Objects;

public class Contacto {

    private String nombre;
    private String tipo;
    private String numeroTelefono;
    private String direccion;
    private ListaCircularDoble<Atributo> atributos;
    private ListaCircularDoble<Foto> fotos;

    public Contacto(String tipo, String nombre, String numeroTelefono, String direccion) {
        this.tipo = tipo;
        this.nombre = nombre;
        this.direccion = direccion;
        this.numeroTelefono = numeroTelefono;
        this.atributos = new ListaCircularDoble<>();
        this.fotos = new ListaCircularDoble<>();
    }

    public String getNombre() {
        return nombre;
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

    public void agregarAtributo(Atributo a) {
        atributos.agregar(a);
    }

    public void agregarFoto(Foto f) {
        fotos.agregar(f);
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
        System.out.println("Nombre: " + nombre);
        System.out.println("Tipo: " + tipo);
        System.out.println("Número de Teléfono: " + numeroTelefono);
        System.out.println("Dirección: " + direccion);
        System.out.println("Atributos:");
        atributos.imprimir();
        System.out.println("Fotos:");
        fotos.imprimir();
    }
}