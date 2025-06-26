package ec.edu.espol;

public class Contacto {

    private String nombre;
    private String tipo;  
    private ListaCircularDoble<Atributo> atributos;
    private ListaCircularDoble<Foto> fotos;

    public Contacto(String nombre, String tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.atributos = new ListaCircularDoble<>();
        this.fotos = new ListaCircularDoble<>();
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
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
    public String toString() {
        return tipo + ": " + nombre;
    }

    public void imprimirDetalles() {
        System.out.println("Nombre: " + nombre);
        System.out.println("Tipo: " + tipo);
        System.out.println("Atributos:");
        atributos.imprimir();
        System.out.println("Fotos:");
        fotos.imprimir();
    }
}