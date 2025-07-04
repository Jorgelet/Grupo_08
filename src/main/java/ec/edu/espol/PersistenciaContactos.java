package ec.edu.espol;

import java.io.*;
import java.util.Scanner;

public class PersistenciaContactos {
    private static final String ARCHIVO_CONTACTOS = System.getProperty("user.dir") + File.separator + "contactos.txt";

    public static void guardarContactos(ListaCircularDoble<Contacto> contactos) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARCHIVO_CONTACTOS))) {

            for (Contacto c : contactos) {
                writer.println("CONTACTO|" + c.getTipo() + "|" + c.getNombre() + "|" +
                        c.getNumeroTelefono() + "|" + c.getDireccion());

                for (Atributo a : c.getAtributos()) {
                    writer.println("ATRIBUTO|" + a.getNombre() + "|" + a.getValor());
                }

                for (Foto f : c.getFotos()) {
                    writer.println("FOTO|" + f.getNombreArchivo());
                }

                writer.println("---");
            }

            System.out.println("\033[0;32mContactos guardados exitosamente.\033[0m");

        } catch (IOException e) {
            System.out.println("\033[0;31mError al guardar contactos: " + e.getMessage() + "\033[0m");
        }
    }

    public static void cargarContactos(ListaCircularDoble<Contacto> contactos) {
        File archivo = new File(ARCHIVO_CONTACTOS);
        if (!archivo.exists()) {
            System.out.println("\033[0;33mNo se encontró archivo de contactos, se creará uno nuevo al guardar.\033[0m");
            return;
        }

        try (Scanner scanner = new Scanner(archivo)) {
            Contacto contactoActual = null;
            int contactosCargados = 0;

            // Leemos el archivo línea por línea hasta el final.
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();

                // Ignoramos líneas vacías o los separadores.
                if (linea.isEmpty() || linea.equals("---")) {
                    continue;
                }

                // Dividimos la línea por el carácter '|' para obtener los datos.
                String[] partes = linea.split("\\|");
                String tipoDato = partes[0];

                switch (tipoDato) {
                    case "CONTACTO":
                        // Creamos un nuevo objeto Contacto.
                        String tipo = partes[1];
                        String nombre = partes[2];
                        String telefono = partes[3];
                        String direccion = partes[4];
                        contactoActual = new Contacto(tipo, nombre, telefono, direccion);
                        contactos.agregar(contactoActual);
                        contactosCargados++;
                        break;

                    case "ATRIBUTO":
                        // Agregamos un atributo al último contacto que creamos.
                        if (contactoActual != null) {
                            String nombreAtributo = partes[1];
                            String valorAtributo = partes[2];
                            contactoActual.agregarAtributo(new Atributo(nombreAtributo, valorAtributo));
                        }
                        break;

                    case "FOTO":
                        // Agregamos una foto al último contacto que creamos.
                        if (contactoActual != null) {
                            String nombreFoto = partes[1];
                            contactoActual.agregarFoto(new Foto(nombreFoto));
                        }
                        break;
                }
            }

            if (contactosCargados > 0) {
                System.out.println("\033[0;32m" + contactosCargados + " contactos cargados exitosamente.\033[0m");
            }

        } catch (IOException e) {
            System.out.println("\033[0;31mError al cargar contactos: " + e.getMessage() + "\033[0m");
        }
    }
}
