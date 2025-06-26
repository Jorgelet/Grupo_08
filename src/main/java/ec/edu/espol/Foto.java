package ec.edu.espol;

public class Foto {
    private String nombreArchivo;

    public Foto(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    @Override
    public String toString() {
        return "Foto: " + nombreArchivo;
    }
}