package ec.edu.espol;

public class Atributo {
    private String nombre;
    private String valor;

    public Atributo(String nombre, String valor) {
        this.nombre = nombre;
        this.valor = valor;
    }

    public String getNombre() {
        return nombre;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return nombre + ": " + valor;
    }
}